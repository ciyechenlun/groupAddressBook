/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmcc.zysoft.groupaddressbook.dto.WalkRecordDto;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * @email zhouyusgs#ahmobile.com
 * @date 2013-6-11 上午11:38:29
 */

@Repository
public class MEmployeeRecordDao extends HibernateBaseDaoImpl<EmployeeRecord, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 上传用户的运动记录
	 * @param userCode 当前登录用户
	 * @param token 用户令牌
	 * @param value 上传的json串，详见接口文档
	 * @param movementId 活动编号
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("deprecation")
	@Transactional
	public List<EmployeeRecord> uploadRecord(String userCode, String token, String value, String movementId) throws ParseException {
		//解析运动记录数据
	    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
		List<EmployeeRecord> list = JSON.parseArray(value, EmployeeRecord.class);
		List<EmployeeRecord> retList = new ArrayList<EmployeeRecord>();
		for(EmployeeRecord empRec : list)
		{
			empRec.setEmployeeId(userCode);
			empRec.setMovementId(movementId);
			//如果有相同开始时间上传的记录，则认为是重复上传数据
			String sql = "SELECT employee_record_id FROM tb_c_employee_record WHERE employee_id=? AND sport_date=? AND sport_start_time=?";
			List<Map<String,Object>> listRec = this.jdbcTemplate.queryForList(sql, 
					empRec.getEmployeeId(),formatDate.format(empRec.getSportDate()),formatTime.format(empRec.getSportStartTime()));
			if(listRec.size()>0)
			{
				//认为是重复数据
				empRec.setMark1("1");
				empRec.setMark2("与" + listRec.get(0).get("employee_record_id").toString() + "重复");
			}
			else
			{
				//防作弊判断
				empRec.setMark1("0");
				empRec = isCheat(empRec);
			}
			
			this.save(empRec);
			retList.add(empRec);
		}
		return retList;
	}
	
	/**
	 * 代替单元测试，打印所有已上传的作弊未作弊清单
	 */
	public List<EmployeeRecord> getAllSportRecord()
	{
		List<EmployeeRecord> retList = new ArrayList<EmployeeRecord>();
		String sql = "FROM EmployeeRecord WHERE sportDate>='2013-07-29' AND gps_status='1' ORDER by sportDate DESC";
		List<EmployeeRecord> list = this.getHibernateTemplate().find(sql);
		for(EmployeeRecord empRec : list)
		{
			empRec.setMark1("0");
			empRec = isCheat(empRec);
			retList.add(empRec);
		}
		return retList;
	}
	
	/**
	 * 获取某次运动的轨迹
	 * @param employee_record_id
	 * @return
	 */
	public List<Map<String,Object>> getGpsByEmployeeRecordId(String employee_record_id)
	{
		String sql = "SELECT * FROM tb_c_record_gps WHERE employee_record_id=? " +
				"ORDER BY gps_date ASC";
		return this.jdbcTemplate.queryForList(sql, employee_record_id);
	}

	/**
	 * 判断是否作弊
	 * @return
	 */
	private EmployeeRecord isCheat(EmployeeRecord empRec)
	{
		if(empRec.getSportStep()>=5000)
		{
			//B场景如果开启GPS，且速度小于2Km/h，且步数*0.8*0.65=>里程>=步数*0.8*.0.12
			if(empRec.getSportSpeed().doubleValue()<=2 && 
					(empRec.getSportDistence().doubleValue()<=empRec.getSportStep()*8*0.65 && 
					empRec.getSportDistence().doubleValue()>=empRec.getSportStep()*0.8*1.2))
			{
				Double d = empRec.getSportDistence().doubleValue()/0.65;
				logger.debug(d.intValue());
				int steps = d.intValue();
				empRec.setGpsSteps(empRec.getSportStep());
				empRec.setSportStep(steps);
				empRec.setMark2("B场景 如果开启GPS，且速度小于2Km/h，且步数*0.8*0.65=>里程>=步数*0.8*.0.12");
			}
		}
		else
		{
			if(empRec.getGpsStatus().equals("1"))
			{
				//A场景
				//如果 步数*0.8*1.2>=Gps里程>=步数*0.6*0.8，则认为正常行走，则直接调用步数
				if(empRec.getSportStep()*0.8*1.2>=empRec.getSportDistence().doubleValue()
						&& empRec.getSportDistence().doubleValue()>=empRec.getSportStep()*0.6*0.8)
				{
					// do nothing
				}
				//B场景如果开启GPS，且速度小于2Km/h，且步数*0.8*0.65=>里程>=步数*0.8*.0.12
				else if(empRec.getSportSpeed().doubleValue()<=2 && 
						(empRec.getSportDistence().doubleValue()<=empRec.getSportStep()*8*0.65 && 
						empRec.getSportDistence().doubleValue()>=empRec.getSportStep()*0.8*1.2))
				{
					Double d = empRec.getSportDistence().doubleValue()/0.65;
					logger.debug(d.intValue());
					int steps = d.intValue();
					empRec.setGpsSteps(empRec.getSportStep());
					empRec.setSportStep(steps);
					empRec.setMark2("B场景 如果开启GPS，且速度小于2Km/h，且步数*0.8*0.65=>里程>=步数*0.8*.0.12");
				}
				else{
					//判断是否作弊 全部属于C场景
					//1.里程<50 && 步数大于500的情况
					if(empRec.getSportDistence().doubleValue()<50 && empRec.getSportStep()>500)
					{
						empRec.setMark1("1");
						empRec.setMark2("1.里程<50 && 步数大于500的情况");
						return empRec; //作弊，直接return，不再向后判断了
					}
					//2.里程>500 && 步数 < 距离/0.65/8
					if(empRec.getSportDistence().doubleValue()>500 && empRec.getSportStep()<(empRec.getSportDistence().doubleValue()/0.65/8))
					{
						empRec.setMark1("1");
						empRec.setMark2("2.里程>500 && 步数 < 距离/0.65/8");
						return empRec; //作弊，直接return，不再向后判断了
					}
					//3距离>500 && 速度>2m/s
					if(empRec.getSportDistence().doubleValue()>500 && empRec.getSportSpeed().doubleValue()>2)
					{
						empRec.setMark1("1");
						empRec.setMark2("3.距离>500 && 速度>2m/s");
						return empRec; //作弊，直接return，不再向后判断了
					}
				}
			}
			else
			{
				//未开启GPS，不需要进行步数计算
				if(empRec.getSportStep()>500 && empRec.getSportSpeed().doubleValue()>2)
				{
					//作弊
					empRec.setMark2("未开启GPS，不需要进行步数计算");
					empRec.setMark1("1");
				}
			}
		}
		return empRec;
	}
	
	/**
	 * 健步记录列表
	 * @param rows:行数
	 * @param page：页码
	 * @param company_id：公司id，在部门id为空时，取当前公司所有人的健步记录
	 * @param department_id：按部门id进行检索
	 * @param usercode：按employee_id进行检索
	 * @param startDate：开始日期
	 * @param endDate：结束日期
	 * @param movement_id: 活动编号
	 * @return
	 */
	public Pagination<?> getRecordList(int rows,int page,String company_id,String department_id,String usercode,String user_name,String startDate,String endDate,String movement_id)
	{
		User user = SecurityContextUtil.getCurrentUser();
		Map<String,Object> map = new HashMap<String,Object>();
		String where = "";
		String sql = "SELECT " +
				"rec.employee_record_id,emp.employee_id,emp.employee_name,emp.employee_code,sex,dept.department_name,emp.mobile," +
				"para.employee_height,para.employee_weight," +
				"rec.update_date,rec.sport_date,rec.sport_start_time,rec.sport_end_time," +
				"IFNULL(rec.sport_elapse_time,0)/1000 AS sport_elapse_time,rec.sport_step," +
				"rec.sport_distence,rec.sport_speed,rec.sport_calories,rec.gps_status,rec.gps_distence," +
				"rec.mode_change_times,rec.step_calc,rec.step_error FROM " +
				"tb_c_employee_record rec LEFT JOIN tb_c_employee emp ON " +
				" rec.employee_id=emp.employee_id " +
				" LEFT JOIN tb_c_department dept ON " +
				"dept.department_id=emp.department_id " +
				"LEFT JOIN tb_c_employee_param para ON para.employee_id=emp.employee_id " +
				" WHERE emp.del_flag='0' ";
		if (StringUtils.hasText(department_id))
		{
			where += " AND emp.department_id='" + department_id + "'";
		}
		if(StringUtils.hasText(usercode))
		{
			where += " AND emp.employee_id='" + usercode + "'";
		}
		if(StringUtils.hasText(user_name))
		{
			where += " AND emp.employee_name like '%" + user_name + "%'";
		}
		if(StringUtils.hasText(startDate))
		{
			if(!StringUtils.hasText(endDate))
			{
				Date now = new Date(); 
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			    endDate = sdf.format(now);   
			}
			where += " AND rec.sport_date>='" + startDate + "' AND rec.sport_date<='" + endDate + "'";
		}
		if(!StringUtils.hasText(department_id) && !StringUtils.hasText(usercode) && !"s_admin".equals(user.getUsername()))
		{
			//如果部门和用户传入都为空，则取当前公司所有人
			where += " AND emp.department_id IN (SELECT department_id FROM tb_c_department WHERE company_id='" + company_id + "')";
		}
		
		if(StringUtils.hasText(movement_id))
		{
			where += " AND (rec.movement_id='" + movement_id + "' OR rec.movement_id IN (" +
					"SELECT movement_id FROM tb_b_movement WHERE parent_movement_id='"+movement_id+"'))";
		}
		
		where += " ORDER BY rec.sport_date desc";
		
		String countSql = "SELECT COUNT(emp.employee_id) FROM " +
				"tb_c_employee emp,tb_c_department dept,tb_c_employee_record rec WHERE " +
				"dept.department_id=emp.department_id AND rec.employee_id=emp.employee_id AND emp.del_flag='0' ";
		countSql += where;
		int offset = (page - 1) * rows;
		sql += where;
		sql += " limit :offset, :limit";
		return namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
	}
	
	/**
	 * 健步记录列表（供导出
	 * @param company_id：公司id，在部门id为空时，取当前公司所有人的健步记录
	 * @param department_id：按部门id进行检索
	 * @param usercode：按employee_id进行检索
	 * @param startDate：开始日期
	 * @param endDate：结束日期
	 * @param movement_id: 活动编号
	 * @return
	 */
	public List<WalkRecordDto> getRecordExportSource(String company_id,String department_id,String usercode,String user_name,String startDate,String endDate,String movement_id)
	{
		//{"姓名","所属部门","手机号","体重/kg","身高/cm","上传日期","运动日期",
		//"起始时间","结束时间","耗时","步数","里程/米","速度km/h","耗能/千卡","GPS状态","GPS里程",
		//"计步方式切换次数","步长推算/cm","步长误差/cm"};
		/**
		 *  employee_name; department_name;mobile;employee_height;
			employee_weight;update_date; sport_date;sport_start_time;sport_end_time;
			sport_elapse_time; sport_step; sport_distence; sport_speed; sport_calories;
			gps_status;gps_distence;mode_change_times;step_calc;step_error;
		 */
		User user = SecurityContextUtil.getCurrentUser();
		String where = "";
		String sql = "SELECT " +
				"emp.employee_name,dept.department_name,emp.mobile," +
				"IFNULL(para.employee_weight,'') AS employee_weight,IFNULL(para.employee_height,'') AS employee_height," +
				"IFNULL(rec.update_date,'') AS update_date,rec.sport_date,rec.sport_start_time,rec.sport_end_time," +
				"IFNULL(rec.sport_elapse_time,0)/1000 AS sport_elapse_time,rec.sport_step," +
				"rec.sport_distence,rec.sport_speed,rec.sport_calories,rec.gps_status,rec.gps_distence," +
				"IFNULL(rec.mode_change_times,'') AS mode_change_tims,rec.step_calc,rec.step_error FROM " +
				"tb_c_employee_record rec LEFT JOIN tb_c_employee emp ON " +
				" rec.employee_id=emp.employee_id " +
				" LEFT JOIN tb_c_department dept ON " +
				"dept.department_id=emp.department_id " +
				"LEFT JOIN tb_c_employee_param para ON para.employee_id=emp.employee_id " +
				" WHERE emp.del_flag='0' ";
		if (StringUtils.hasText(department_id))
		{
			where += " AND emp.department_id='" + department_id + "'";
		}
		if(StringUtils.hasText(usercode))
		{
			where += " AND emp.employee_id='" + usercode + "'";
		}
		if(StringUtils.hasText(user_name))
		{
			where += " AND emp.employee_name like '%" + user_name + "%'";
		}
		if(StringUtils.hasText(startDate))
		{
			if(!StringUtils.hasText(endDate))
			{
				Date now = new Date(); 
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			    endDate = sdf.format(now);   
			}
			where += " AND rec.sport_date>='" + startDate + "' AND rec.sport_date<='" + endDate + "'";
		}
		if(!StringUtils.hasText(department_id) && !StringUtils.hasText(usercode) && !"s_admin".equals(user.getUsername()))
		{
			//如果部门和用户传入都为空，则取当前公司所有人
			where += " AND emp.department_id IN (SELECT department_id FROM tb_c_department WHERE company_id='" + company_id + "')";
		}
		
		if(StringUtils.hasText(movement_id))
		{
			where += " AND rec.movement_id='" + movement_id + "'";
		}
		
		where += " ORDER BY rec.sport_date desc";
		
		sql += where;
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		List<WalkRecordDto> lw =new ArrayList<WalkRecordDto>();
		for(Map<String,Object> map : list)
		{
			WalkRecordDto wr = new WalkRecordDto();
			wr.setDepartmentName(map.get("department_name").toString());
			wr.setEmployeeHeight(map.get("employee_height").toString());
			wr.setEmployeeName(map.get("employee_name").toString());
			wr.setEmployeeWeight(map.get("employee_weight").toString());
			wr.setGpsDistence(map.get("gps_distence").toString());
			wr.setGpsStatus(map.get("gps_status").toString());
			wr.setMobile(map.get("mobile").toString());
			wr.setModeChangeTimes(map.get("mode_change_tims").toString());
			wr.setSportCalories(map.get("sport_calories").toString());
			wr.setSportDate(map.get("sport_date").toString());
			wr.setSportDistence(map.get("sport_distence").toString());
			wr.setSportElapseTime(map.get("sport_elapse_time").toString());
			wr.setSportEndTime(map.get("sport_end_time").toString());
			wr.setSportSpeed(map.get("sport_speed").toString());
			wr.setSportStartTime(map.get("sport_start_time").toString());
			wr.setSportStep(map.get("sport_step").toString());
			wr.setStepCalc(map.get("step_calc").toString());
			wr.setStepError(map.get("step_error").toString());
			wr.setUpdateDate(map.get("update_date").toString());
			lw.add(wr);
		}
		return lw;
	}
}
