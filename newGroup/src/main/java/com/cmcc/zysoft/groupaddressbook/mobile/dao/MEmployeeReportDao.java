/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.zysoft.groupaddressbook.dto.UserRecordDto;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.cmcc.zysoft.sellmanager.model.EmployeeReport;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午4:20:29
 */

@Repository
public class MEmployeeReportDao extends
		HibernateBaseDaoImpl<EmployeeReport, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;

	/**
	 * 统计部门下员工的健步记录
	 * 
	 * @param departmentId
	 *            ：部门编号
	 * @param startDate
	 *            :开始日期，可以为空
	 * @param endDate
	 *            :结束日期，可以为空（为空时，如果startDate不为空，则为当前日期）
	 * @param type
	 *            :统计字段
	 * @param movement_id
	 *            : 活动编号
	 * @return
	 */
	public List<Map<String, Object>> getReportByDepartmentId(
			String departmentId, String startDate, String endDate, String type,
			String movement_id) {
		String sql = "SELECT emp.employee_name,emp.employee_name,IFNULL(rpt.all_sport_times,0) as all_sport_times,"
				+ "IFNULL(rpt.all_sport_elpse_time,0) AS all_sport_elpse_time,IFNULL(rpt.all_sport_steps,0) AS all_sport_steps,IFNULL(rpt.all_sport_distence,0) all_sport_distence,"
				+ "IFNULL(rpt.avg_sport_speed,0) AS avg_sport_speed,IFNULL(rpt.avg_sport_steps,0) AS avg_sport_steps,"
				+ "IFNULL(rpt.avg_sport_time,0) AS avg_sport_time,IFNULL(rpt.current_rank,0) AS current_rank,"
				+ "IFNULL(rpt.sport_level,0) AS sport_level,IFNULL(rpt.single_max_steps,0) AS single_max_steps,"
				+ "IFNULL(rpt.single_max_time,0) AS single_max_time,IFNULL(rpt.medal_numbers,0) AS medal_numbers"
				+ " FROM tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON emp.employee_id=rpt.employee_id WHERE 1=1 ";
		if (StringUtils.hasText(departmentId)) {
			sql += " AND emp.department_id='" + departmentId + "'";
		}
		if (StringUtils.hasText(movement_id)) {
			sql += " AND rpt.movement_id='" + movement_id + "'";
		}
		sql += " ORDER BY rpt." + type + " DESC limit 0,10";
		if (StringUtils.hasText(startDate)) {
			if (!StringUtils.hasText(endDate)) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				endDate = sdf.format(now);
			}
			sql = "SELECT emp.employee_name,emp.employee_name,IFNULL(rec.all_sport_times,0) as all_sport_times,"
					+ "IFNULL(rec.all_sport_elpse_time,0) AS all_sport_elpse_time,IFNULL(rec.all_sport_steps,0) AS all_sport_steps,IFNULL(rec.all_sport_distence,0) all_sport_distence,"
					+ "IFNULL(rec.avg_sport_speed,0) AS avg_sport_speed,IFNULL(rec.avg_sport_steps,0) AS avg_sport_steps,"
					+ "IFNULL(rec.avg_sport_time,0) AS avg_sport_time,IFNULL(rpt.current_rank,0) AS current_rank,"
					+ "IFNULL(rpt.sport_level,0) AS sport_level,IFNULL(rec.single_max_steps,0) AS single_max_steps,"
					+ "IFNULL(rec.single_max_time,0) AS single_max_time,IFNULL(rpt.medal_numbers,0) AS medal_numbers"
					+ " FROM tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON rpt.employee_id=emp.employee_id "
					+ "LEFT JOIN (SELECT MIN(employee_id) AS employee_id,COUNT(sport_Date) AS all_sport_times,"
					+ "SUM(sport_distence) all_sport_distence,SUM(sport_elapse_time) AS all_sport_elpse_time,"
					+ "SUM(sport_step) AS all_sport_steps,AVG(sport_speed) AS avg_sport_speed,"
					+ "AVG(sport_step) AS avg_sport_steps,AVG(sport_elapse_time) AS avg_sport_time,MAX(sport_elapse_time) AS single_max_time,"
					+ "MAX(sport_step) AS single_max_steps "
					+ "FROM tb_c_employee_record WHERE sport_date>=? AND sport_date<=? GROUP BY employee_id) AS rec ON  rec.employee_id=emp.employee_id "
					+ "WHERE 1=1";
			if (StringUtils.hasText(departmentId)) {
				sql += " AND emp.department_id='" + departmentId + "'";
			}
			if (StringUtils.hasText(movement_id)) {
				sql += " AND movement_id='" + movement_id + "'";
			}
			sql += " ORDER BY rec." + type + " DESC limit 0,10";
			System.out.print("sql====>" + sql);
			return this.jdbcTemplate.queryForList(sql, startDate, endDate);
		} else {
			return this.jdbcTemplate.queryForList(sql);
		}
	}

	/**
	 * 根据部门获取用户列表
	 * 
	 * @param rows
	 * @param page
	 * @param departmentId
	 *            ：部门编号
	 * @param company_id
	 *            ：公司编号
	 * @param movement_id
	 *            :活动编号
	 * @return
	 */
	public Pagination<?> getUserListByDepartment(int rows, int page,
			String departmentId, String company_id, String movement_id) {
		User user = SecurityContextUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		String where = "";
		String sql = "SELECT emp.employee_id,emp.employee_name,IFNULL(rpt.all_sport_times,0) as all_sport_times,"
				+ "IFNULL(sport_level,0) AS sport_level,"
				+ "CONCAT('',SEC_TO_TIME(IFNULL(all_sport_elpse_time,0)/1000)) as all_sport_elpse_time,"
				+ "IFNULL(avg_sport_speed,0) AS avg_sport_speed,"
				+ "IFNULL(all_sport_steps,0) AS all_sport_steps,IFNULL(avg_sport_steps,0) AS avg_sport_steps FROM "
				+ "tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON rpt.employee_id=emp.employee_id WHERE "
				+ "emp.del_flag='0' ";
		if (StringUtils.hasText(departmentId)) {
			where += " AND emp.department_id='" + departmentId + "' ";
		} else {
			if(!"s_admin".equals(user.getUsername())){
				where += " AND emp.department_id in (SELECT department_id FROM tb_c_department WHERE company_id='"
					+ company_id + "') ";
			}
		}
		if (StringUtils.hasText(movement_id)) {
			where += " AND rpt.movement_id='" + movement_id + "'";
		}
		sql += where;
		sql += " ORDER BY all_sport_steps DESC";
		String countSql = "SELECT COUNT(emp.employee_id) FROM "
				+ "tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON rpt.employee_id=emp.employee_id "
				+ " WHERE emp.del_flag='0'";
		countSql += where;
		int offset = (page - 1) * rows;
		sql += " limit :offset, :limit";
		return namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset,
				rows, map);
	}
	
	
	/**
	 * 导出数据源
	 * @param movement_id
	 *            :活动编号
	 * @return
	 */
	public List<UserRecordDto> getUserRecordExportSource(String movement_id) {
		String where = "";
		String sql = "SELECT emp.employee_name,IFNULL(rpt.all_sport_times,0) as all_sport_times,"
				+ "IFNULL(sport_level,0) AS sport_level,"
				+ "CONCAT('',SEC_TO_TIME(IFNULL(all_sport_elpse_time,0)/1000)) as all_sport_elpse_time,"
				+ "IFNULL(avg_sport_speed,0) AS avg_sport_speed,"
				+ "IFNULL(all_sport_steps,0) AS all_sport_steps,IFNULL(avg_sport_steps,0) AS avg_sport_steps," +
				"current_rank FROM "
				+ "tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON rpt.employee_id=emp.employee_id WHERE "
				+ "emp.del_flag='0' AND movement_id=?";
		sql += where;
		sql += " ORDER BY all_sport_steps DESC";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,movement_id);
		List<UserRecordDto> lur = new ArrayList<UserRecordDto>();
		for(Map<String,Object> m : list)
		{
			UserRecordDto ur = new UserRecordDto();
			ur.setAll_sport_elpse_time(m.get("all_sport_elpse_time").toString());
			ur.setAll_sport_steps(m.get("all_sport_steps").toString());
			ur.setAll_sport_times(m.get("all_sport_times").toString());
			ur.setAvg_sport_speed(m.get("avg_sport_speed").toString());
			ur.setAvg_sport_steps(m.get("avg_sport_steps").toString());
			ur.setCurrent_rank(m.get("current_rank").toString());
			ur.setEmployee_name(m.get("employee_name").toString());
			lur.add(ur);
		}
		return lur;
	}

	/**
	 * 获取用户的排名
	 * 
	 * @param userCode
	 * @param token
	 * @param movement_id
	 *            活动编号
	 * @return
	 */
	public Map<String, Object> getEmpOrder(String userCode, String token,
			String movement_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT report.current_rank AS current_rank, report.all_sport_steps AS steps"
				+ " FROM tb_c_employee_report report"
				+ " WHERE movement_id=? AND report.employee_id = ? ORDER BY current_rank ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				movement_id, userCode);
		map.put("cmd", "getmyorder");
		try {
			map.put("code", "0");
			map.put("order", list.get(0).get("current_rank").toString());
			map.put("steps", list.get(0).get("steps").toString());
		} catch (Exception e) {
			sql = "SELECT IFNULL(max(current_rank),0) as current_rank FROM tb_c_employee_report WHERE movement_id='"
					+ movement_id + "'";
			int order = this.jdbcTemplate.queryForInt(sql) + 1;
			map.put("order", order);
			map.put("steps", "0");
			e.printStackTrace();
			map.put("code", "0");
		}
		return map;
	}

	/**
	 * 获取某一活动所有排名
	 * 
	 * @param usercode
	 * @param movement_id
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(String usercode,
			String movement_id) {
		// 先把level修改为0，为用户的积分
		String sql = "SELECT emp.employee_name AS name, report.current_rank AS `order`, report.all_sport_steps AS steps,report.medal_numbers AS medals,'0' AS level "
				+ " FROM tb_c_employee_report report LEFT JOIN"
				+ " tb_c_employee emp ON report.employee_id = emp.employee_id WHERE movement_id=?"
				+ " ORDER BY `order` ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				movement_id);
		return list;
	}

	/**
	 * 获得某个用户的详细信息
	 * 
	 * @param employee_id
	 * @return
	 */
	public Map<String, Object> getUserDetail(String employee_id) {
		String sql = "SELECT emp.employee_name,employee_code,emp.sex,emp.birthday,emp.mobile,emp.parent_department_name,dept.department_id,param.employee_height,"
				+ "param.employee_weight,rpt.all_sport_times,rpt.sport_first_date,rpt.all_sport_steps,rpt.sport_nearest_time,CAST(SEC_TO_TIME(IFNULL(rpt.all_sport_elpse_time,0)/1000) AS CHAR(40)) AS all_sport_elpse_time,rpt.gps_times,"
				+ "rpt.all_sport_distence,rpt.all_gps_distence,rpt.avg_sport_speed,rpt.avg_sport_time,rpt.all_sport_steps,rpt.avg_sport_steps,rpt.steps,rpt.single_max_steps,"
				+ "rpt.single_max_time,rpt.single_max_speed,rpt.sport_level,rpt.current_rank,"
				+ "(SELECT COUNT(*) FROM tb_c_employee_medal medal WHERE medal.employee_id=emp.employee_id AND medal_id IN"
				+ " (SELECT medal_id FROM tb_c_medal WHERE medal_type='1' AND medal_id=medal.medal_id)) AS times_number,"
				+ " (SELECT COUNT(*) FROM tb_c_employee_medal medal2  WHERE medal2.employee_id=emp.employee_id AND medal_id IN "
				+ "(SELECT medal_id FROM tb_c_medal WHERE medal_type='0' AND medal_id=medal2.medal_id)) AS movement_number "
				+ "FROM tb_c_employee emp LEFT JOIN tb_c_employee_report rpt ON rpt.employee_id=emp.employee_id "
				+ " LEFT JOIN tb_c_department dept ON dept.department_id=emp.department_id LEFT JOIN "
				+ " tb_c_employee_param param ON param.employee_id=emp.employee_id WHERE emp.employee_id=? ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				employee_id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获得个人统计信息（gps除外）
	 * 
	 * @param usercode
	 * @return
	 */
	public List<Map<String, Object>> getMyRecordReport(String usercode,String movement_id) {
		String sql = "SELECT rpt.employee_id,rpt.all_sport_times,rpt.all_sport_elpse_time,rpt.all_sport_steps,"
				+ "rpt.all_sport_distence,rpt.avg_sport_speed,rpt.avg_sport_steps,rpt.avg_sport_time,rpt.current_rank,IFNULL(rpt.sport_level,0) AS sport_level,rpt.sport_first_date,"
				+ "rpt.sport_nearest_time,rpt.single_max_steps,rpt.single_max_time,"
				+ "max(rec.sport_distence) AS max_sport_distence,max(sport_speed) AS max_sport_speed,max(sport_calories) AS max_sport_calories,"
				+ "sum(rec.sport_calories) AS all_sport_calories"
				+ " from tb_c_employee_report rpt LEFT JOIN tb_c_employee_record rec"
				+ " ON rec.employee_id=rpt.employee_id AND rec.movement_id=rpt.movement_id where rpt.employee_id=? AND "
				+ "rpt.movement_id=?"
				+ " GROUP BY rpt.employee_id";
		return this.jdbcTemplate.queryForList(sql, usercode, movement_id);
	}

	/**
	 * 获取排名前十的用户列表
	 * 
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getTopTen(String userCode, String token,
			String movement_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT emp.employee_name, report.current_rank AS `order`, report.all_sport_steps AS steps,"
				+ "report.all_sport_distence as distence,report.medal_numbers,IFNULL(report.sport_level,'') AS sport_level "
				+ " FROM tb_c_employee_report report LEFT JOIN"
				+ " tb_c_employee emp ON report.employee_id = emp.employee_id WHERE movement_id=?"
				+ " ORDER BY `order` ASC" + " LIMIT 0, 10";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				movement_id);
		map.put("cmd", "gettop10");
		try {
			map.put("code", "0");
			map.put("value", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "1");
		}
		return map;
	}

	/**
	 * 获取排名前十的用户列表(保持向下兼容，返回所有)
	 * 
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getTopTenV2(String userCode, String token,
			String movement_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT emp.employee_id,emp.employee_name, report.current_rank AS `order`, report.all_sport_steps AS steps,"
				+ "report.all_sport_distence as distence,report.medal_numbers,IFNULL(report.sport_level,'') AS sport_level,"
				+ "IFNULL(report.all_sport_elpse_time,'0') AS all_time "
				+ " FROM tb_c_employee_report report LEFT JOIN"
				+ " tb_c_employee emp ON report.employee_id = emp.employee_id WHERE movement_id=?"
				+ " ORDER BY `order` ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				movement_id);
		map.put("cmd", "gettop10");
		try {
			map.put("code", "0");
			map.put("value", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "1");
		}
		return map;
	}
	
	/**
	 * 返回某个活动的前100名（疯了，早知道写个共通了）
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getAllTop100(String movement_id)
	{
		String sql = "SELECT emp.employee_id,emp.employee_name, report.current_rank AS `order`, report.all_sport_steps AS steps,"
				+ "report.all_sport_distence as distence,report.medal_numbers,IFNULL(report.sport_level,'') AS sport_level,"
				+ "IFNULL(report.all_sport_elpse_time,'0') AS all_time "
				+ " FROM tb_c_employee_report report LEFT JOIN"
				+ " tb_c_employee emp ON report.employee_id = emp.employee_id WHERE movement_id=?"
				+ " ORDER BY `order` ASC limit 0,100";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				movement_id);
		return list;
	}

	/**
	 * 与我排名相近的5个排名
	 * 
	 * @param myOrder
	 *            当前登录用户的排名
	 * @param movement_id
	 *            活动编号
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(int myOrder,
			String movement_id) {
		String sql = "SELECT emp.employee_name, report.current_rank AS `order`, report.all_sport_steps AS steps,report.medal_numbers,report.sport_level "
				+ "FROM tb_c_employee emp,tb_c_employee_report report WHERE report.employee_id=emp.employee_id AND report.movement_id=? ";
		if (myOrder <= 3) {
			sql += " AND report.current_rank>=1";
		} else if (myOrder <= 6) {
			sql += " AND report.current_rank>3 ";
		} else {
			String csql = "SELECT MAX(current_rank) FROM tb_c_employee_report "
					+ "WHERE movement_id='" + movement_id + "'";
			int maxRank = this.jdbcTemplate.queryForInt(csql);
			if (myOrder >= maxRank - 2) {
				sql += " AND report.current_rank>=" + (maxRank - 5);
			} else {
				sql += " AND report.current_rank>=" + (myOrder - 2)
						+ " AND report.current_rank<=" + (myOrder + 3);
			}
		}
		sql += " ORDER BY report.current_rank ASC limit 0,5";
		System.out.print("sql=====" + sql);
		return this.jdbcTemplate.queryForList(sql, movement_id);
	}

	/**
	 * 与我排名相近的10个排名
	 * 
	 * @param myOrder
	 *            当前登录用户的排名
	 * @param movement_id
	 *            活动编号
	 * @return
	 */
	public List<Map<String, Object>> getOrderListNearestTen(int myOrder,
			String movement_id) {
		String sql = "SELECT emp.employee_name, report.current_rank AS `order`, report.all_sport_steps AS steps,"
				+ "report.all_sport_distence as distence,report.medal_numbers,IFNULL(report.sport_level,'') AS sport_level "
				+ "FROM tb_c_employee emp,tb_c_employee_report report WHERE report.employee_id=emp.employee_id AND report.movement_id=? ";
		if (myOrder <= 5) {
			sql += " AND report.current_rank>=1";
		} else {
			String csql = "SELECT MAX(current_rank) FROM tb_c_employee_report "
					+ "WHERE movement_id='" + movement_id + "'";
			int maxRank = this.jdbcTemplate.queryForInt(csql);
			if (myOrder >= maxRank - 4) {
				sql += " AND report.current_rank>=" + (maxRank - 10);
			} else {
				sql += " AND report.current_rank>=" + (myOrder - 4)
						+ " AND report.current_rank<=" + (myOrder + 5);
			}
		}
		sql += " ORDER BY report.current_rank ASC limit 0,10";
		System.out.print("sql=====" + sql);
		return this.jdbcTemplate.queryForList(sql, movement_id);
	}

	/**
	 * 更新用户统计表信息
	 * 
	 * @param userCode
	 *            ：当前登录用户编号
	 * @param empRec
	 *            ：上传的计步明细数据
	 * @return
	 */
	public List<Map<String, Object>> updateUserReport(String userCode,
			List<EmployeeRecord> empRecList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (EmployeeRecord empRec : empRecList) {
			List<Map<String, Object>> map = updateUserReportData(userCode,
					empRec);
			if (map != null) {
				list.addAll(map);
			}
		}
		return list;
	}

	/**
	 * 更新用户统计信息表
	 * 
	 * @param userCode
	 *            当前登录用户的employee_id
	 * @param empRec
	 *            上传的数据转换成实体
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> updateUserReportData(String userCode,
			EmployeeRecord empRec) {
		// 防作弊
		if (!empRec.getMark1().equals("1")) {
			// 先判断用户在用户信息表是否存在
			// String sql =
			// "SELECT count(employee_id) FROM tb_c_employee_report WHERE employee_id=?";
			String hql = "FROM EmployeeReport empRpt where empRpt.employeeId=? AND movementId=?";
			List<EmployeeReport> listUserReport = this.findByHQL(hql, userCode,
					empRec.getMovementId());
			EmployeeReport empRpt = null;
			if (listUserReport.size() > 0) {
				// 已经存在，更新相关信息
				empRpt = (EmployeeReport) listUserReport.get(0);
				// 0.单次top数据更新
				if (empRpt.getSingleMaxSteps() < empRec.getSportStep()) {
					empRpt.setSingleMaxSteps(empRec.getSportStep());
				}
				if (empRpt.getSingleMaxTime() < empRec.getSportElapseTime()) {
					empRpt.setSingleMaxTime(empRec.getSportElapseTime());
				}
				if (empRpt.getSingleMaxSpeed()
						.compareTo(empRec.getSportSpeed()) == -1) {
					empRpt.setSingleMaxSpeed(empRec.getSportSpeed());
				}
				// 1.运动总次数+1
				empRpt.setAllSportTimes(empRpt.getAllSportTimes() + 1);
				// 2.总用时相加
				long lonElpseTime = empRec.getSportElapseTime();

				BigInteger elpseTime = new BigInteger(
						String.valueOf(lonElpseTime));
				elpseTime = elpseTime.add(empRpt.getAllSportElpseTime());
				empRpt.setAllSportElpseTime(elpseTime);
				// 3.总步数相加
				empRpt.setAllSportSteps(empRpt.getAllSportSteps()
						+ empRec.getSportStep());
				// 4.总里程相加
				BigDecimal distence = empRpt.getAllSportDistence().add(
						empRec.getSportDistence());
				empRpt.setAllSportDistence(distence);
				// 5.平均速度计算-->统一计算了

				// 6.平均步数=总步数/总次数
				Integer avgSteps = empRpt.getAllSportSteps()
						/ empRpt.getAllSportTimes();
				empRpt.setAvgSportSteps(avgSteps);

				// 7.平均用时-->在后面统一用方法进行一次运算

				// 8.当前排名计算

				// 9.等级计算

				// 10.最近一次使用时间
				empRpt.setSportNearestTime(empRec.getSportStartTime());

				// 11.GPS统计次数
				if (empRec.getGpsStatus().equals("1")) {
					empRpt.setGpsTimes(empRpt.getGpsTimes() + 1);
				}

				// 12.GPS记录总里程数
				distence = empRpt.getAllGpsDistence().add(
						empRec.getGpsDistence());
				empRpt.setAllGpsDistence(distence);

				// 13.GPS记录总步数
				empRpt.setAllGpsSteps(empRpt.getAllGpsSteps()
						+ empRec.getGpsSteps());

				// 最后，更新至数据库
				this.update(empRpt);
			} else {
				// 用户统计信息不存在，插入一条记录
				empRpt = new EmployeeReport();
				empRpt.setMovementId(empRec.getMovementId());
				empRpt.setAllGpsDistence(empRec.getGpsDistence());
				empRpt.setAllGpsSteps(empRec.getGpsSteps());
				empRpt.setAllSportDistence(empRec.getSportDistence());
				empRpt.setAllSportElpseTime(new BigInteger(String
						.valueOf(empRec.getSportElapseTime())));
				empRpt.setAllSportSteps(empRec.getSportStep());
				empRpt.setAllSportTimes(1);
				empRpt.setSportLevel(0);
				// 时速=总里程/总时间
				// BigDecimal avgSpeed = empRec.getSportDistence() /
				// empRpt.setAvgSportSpeed(empRpt.get)
				empRpt.setAvgSportSteps(empRec.getSportStep());
				// 平均用时，改用BigInt类型，统一进行计算
				// empRpt.setAvgSportTime(empRec.getSportElapseTime());
				// 排名要进行计算

				empRpt.setEmployeeId(userCode);
				if (empRec.getGpsStatus().equals("1")) {
					empRpt.setGpsTimes(1);
				} else {
					empRpt.setGpsTimes(0);
				}

				// 勋章数也要进行计算
				// empRpt.setMedalNumbers(0);
				empRpt.setSingleMaxSteps(empRec.getSportStep());
				empRpt.setSingleMaxTime(empRec.getSportElapseTime());
				empRpt.setSingleMaxSpeed(empRec.getSportSpeed());
				empRpt.setSportFirstDate(empRec.getSportStartTime());
				// Level也要进行计算
				// updateMyLevel(userCode, empRpt);

				empRpt.setSportNearestTime(empRec.getSportStartTime());
				// 步长推算
				empRpt.setSteps(empRec.getStepCalc());
				this.save(empRpt);
			}

			// 取出活动信息，分发给相应的子函数
			Map<String, Object> m = this.jdbcTemplate
					.queryForList(
							"SELECT movement_id,movement_name,IFNULL(medal_sys_id,'') AS medal_sys_id,IFNULL(order_type,'0') AS order_type FROM "
									+ "tb_b_movement WHERE movement_id=?",
							empRec.getMovementId()).get(0);

			// 平均速度
			getAvgSpeed(userCode, empRec.getMovementId());
			// 平均用时
			setAvgSportTime(userCode, empRec.getMovementId());
			// 更新用户等级
			// updateMyLevel(userCode, empRpt);
			// 更新用户步数勋章
			List<Map<String, Object>> map = updateMedal(userCode, empRpt, m
					.get("medal_sys_id").toString(), m.get("order_type")
					.toString(), m.get("movement_id").toString());
			// 更新排名信息
			updateRanks(empRec.getMovementId(), m.get("order_type").toString());
			return map;
		} else {
			return null;
		}
	}

	/**
	 * 更新勋章信息
	 * 
	 * @param userCode
	 *            当前登录用户
	 * @param empRpt
	 * @param medalSysId
	 *            ：勋章系统，如果为空，则赋值1
	 * @param orderType
	 *            ：排名依据
	 * @param movement_id
	 *            ：活动编号
	 */
	private List<Map<String, Object>> updateMedal(String userCode,
			EmployeeReport empRpt, String medalSysId, String orderType,
			String movement_id) {
		// @2013.12.11添加勋章与活动对应信息
		if (!StringUtils.hasText(medalSysId)) {
			medalSysId = "1";
		}
		// 取勋章leve的最小值
		String minSQL = "SELECT MIN(medal_level) AS minLevel FROM tb_c_medal WHERE "
				+ " medal_sys_id=?";
		List<Map<String, Object>> minList = this.jdbcTemplate.queryForList(
				minSQL, medalSysId);
		String minValue = minList.get(0).get("minLevel").toString();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		// 1.更新步数 or 勋章
		int steps = empRpt.getAllSportSteps();
		int times = empRpt.getAllSportTimes();
		BigDecimal distence = empRpt.getAllSportDistence();
		// @2013.12.12添加活动和排名依据
		Map<String, Object> map1 = updateStepMedal(steps, userCode, "0",
				medalSysId, orderType, minValue, distence, movement_id);
		// 2.更新次数勋章
		Map<String, Object> map2 = updateStepMedal(times, userCode, "1",
				medalSysId, orderType, "2", distence, movement_id);
		// 更新个人的勋章总数
		String sql = "SELECT COUNT(user_medal_id) as count FROM tb_c_employee_medal WHERE employee_id=? "
				+ " AND (movement_id='1' OR movement_id=?)";
		int count = this.jdbcTemplate.queryForInt(sql, userCode, movement_id);
		// 更新数据库
		sql = "UPDATE tb_c_employee_report SET medal_numbers=? WHERE employee_id=? AND (movement_id='1' OR movement_id=?)";
		this.jdbcTemplate.update(sql, count, userCode, movement_id);
		if (map1 != null) {
			map.add(map1);
		}
		if (map2 != null) {
			map.add(map2);
		}
		return map;
	}

	/**
	 * 更新步数或次数勋章
	 * 
	 * @param steps
	 * @param type
	 *            0：步数勋章，1：次数勋章
	 * @param 2013.12.12添加勋章系统编号medalsyid
	 * @return
	 */
	private Map<String, Object> updateStepMedal(int steps, String userCode,
			String type, String medalSysId, String orderType, String minValue,
			BigDecimal distence, String movement_id) {
		// minValue = type.equals("0")?minValue:"2";
		String medal_id = "";
		String sql = "SELECT * FROM tb_c_medal WHERE medal_level>=" + minValue
				+ "  AND medal_type='" + type + "' ";
		if (!"1".equals(type)) {
			if ("0".equals(orderType)) {
				// 步数勋章
				sql += " AND medal_level<=" + steps + "";
			} else {
				// 里程勋章
				sql += " AND medal_level<=" + distence;
			}
			// 通用的次数勋章是不分勋章系统的。所以这个条件要放到条件里面进行过滤
			sql += " AND medal_sys_id IN (SELECT medal_sys_id FROM tb_c_medal_sys WHERE medal_sys_id='"
					+ medalSysId + "') ";
		} else {
			sql += " AND medal_level<=" + steps + "";
		}
		sql += " ORDER BY medal_level DESC limit 1";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		if (list.size() > 0) {
			medal_id = list.get(0).get("medal_id").toString();
		} else {
			medal_id = "";
		}
		if (!medal_id.equals("")) {
			sql = "SELECT *,(SELECT mark1 FROM tb_c_medal where medal_id=tb_c_employee_medal.medal_id) AS city FROM tb_c_employee_medal WHERE medal_id=? AND employee_id=? "
					+ " AND movement_id=?";
			list = this.jdbcTemplate.queryForList(sql, medal_id, userCode,
					movement_id);
			if (list.size() == 0) {
				UUID uuid = UUID.randomUUID();
				String user_medal_id = uuid.toString().replace("-", "");
				sql = "INSERT INTO tb_c_employee_medal (user_medal_id,medal_id,employee_id,movement_id) "
						+ "VALUES ('"
						+ user_medal_id
						+ "','"
						+ medal_id
						+ "','" + userCode + "','" + movement_id + "')";
				this.jdbcTemplate.execute(sql);
				sql = "SELECT medal_name,medal_level,medal_type,medal_picture,IFNULL(mark1,'') AS city FROM "
						+ "tb_c_medal WHERE medal_id=?";
				return this.jdbcTemplate.queryForList(sql, medal_id).get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 更新所有人的排名
	 * 
	 * @param movementId
	 *            ：活动编号
	 * @param orderType
	 *            增加排名方式@2013.12.11
	 */
	@SuppressWarnings("unchecked")
	private void updateRanks(String movementId, String orderType) {
		// String hql = "FROM EmployeeReport WHERE movementId=?";
		// List<EmployeeReport> list = this.findByHQL(hql,movementId);
		/*
		 * for(EmployeeReport empRpt : list) { int rank =
		 * getMyRank(empRpt.getEmployeeId(),movementId,orderType); String sql =
		 * "UPDATE tb_c_employee_report SET current_rank=" + rank + " WHERE " +
		 * "employee_id='"+empRpt.getEmployeeId()+"' AND movement_id='" +
		 * movementId + "'" ; System.out.print("====update rank sql is " + sql);
		 * this.jdbcTemplate.execute(sql); }
		 */
		String updateField = orderType.equals("0") ? "all_sport_steps"
				: "all_sport_distence";
		String sql = "update tb_c_employee_report  as rpt set rpt.current_rank="
				+ "(select count(*)+1 from (select * from tb_c_employee_report) as b where b.movement_id='"
				+ movementId
				+ "' and b."
				+ updateField
				+ ">rpt."
				+ updateField
				+ ")" + "where movement_id='" + movementId + "'";
		this.jdbcTemplate.execute(sql);
	}

	/**
	 * 获得我的排名
	 * 
	 * @param usercode
	 * @param movementId
	 *            ：活动编号
	 * @return
	 */
	private int getMyRank(String usercode, String movementId, String orderType) {
		String updateRankSQL = "all_sport_steps>(select all_sport_steps from tb_c_employee_report where employee_id=? AND movement_id=?)";
		if ("1".equals(orderType)) {
			updateRankSQL = "all_sport_distence>(select all_sport_distence from tb_c_employee_report where employee_id=? AND movement_id=?)";
		}
		String sql = "SELECT COUNT(employee_id) FROM tb_c_employee_report WHERE "
				+ updateRankSQL + " AND " + " movement_id=?";
		int rank = this.jdbcTemplate.queryForInt(sql, usercode, movementId,
				movementId);
		return rank + 1;
	}

	/**
	 * 平均速度计算
	 * 
	 * @param usercode
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getAvgSpeed(String usercode, String movementId) {
		String sql = "SELECT IFNULL(AVG(sport_speed),0) as speed FROM tb_c_employee_record where employee_id=? AND movement_id=?";
		List list = this.jdbcTemplate.queryForList(sql, usercode, movementId);
		Map<String, Object> map = (Map<String, Object>) list.get(0);
		sql = "UPDATE tb_c_employee_report SET avg_sport_speed="
				+ map.get("speed").toString() + " WHERE " + "employee_id=?";
		this.jdbcTemplate.update(sql, usercode);
	}

	/**
	 * 平均用时计算
	 * 
	 * @param usercode
	 * @param movementId
	 *            活动编号
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setAvgSportTime(String usercode, String movementId) {
		String sql = "SELECT IFNULL(AVG(sport_elapse_time),0) as elapse_time FROM tb_c_employee_record where employee_id=? AND movement_id=?";
		List list = this.jdbcTemplate.queryForList(sql, usercode, movementId);
		Map<String, Object> map = (Map<String, Object>) list.get(0);
		sql = "UPDATE tb_c_employee_report SET avg_sport_time="
				+ map.get("elapse_time").toString() + " WHERE "
				+ "employee_id=?";
		this.jdbcTemplate.update(sql, usercode);
	}

	/**
	 * 上传用户的运动记录--方法废弃
	 * 
	 * @param userCode
	 * @param token
	 * @param value
	 * @return
	 */
	@Transactional
	public Map<String, Object> uploadRecord(String userCode, String token,
			String value) {
		// 解析运动记录数据
		JSONArray jsonArray = JSONObject.parseArray(value);
		JSONObject object = jsonArray.getJSONObject(0);
		List<EmployeeRecord> list = JSON
				.parseArray(value, EmployeeRecord.class);
		System.out.print("===>upload list size" + list.size());
		for (EmployeeRecord empRec : list) {
			empRec.setEmployeeId(userCode);
			// this.save(empRec);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		return map;

		/**
		 * String employeeRecordModel =
		 * object.getString("employee_record_model"); Date sportDate =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.YYYY_MM_DD,
		 * object.getString("sport_date")); Date sportStartTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_start_time")); Date sportEndTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_end_time")); Date sportElapseTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_elapse_time"));
		 * 
		 * Integer sportStep =
		 * UgcStringUtil.parseInteger(object.getString("sport_step")); Integer
		 * singleMaxSteps =
		 * UgcStringUtil.parseInteger(object.getString("sport_step"));
		 * BigDecimal sportDistence = new
		 * BigDecimal(object.getString("sport_distence")); BigDecimal sportSpeed
		 * = new BigDecimal(object.getString("sport_speed")); BigDecimal
		 * sportCalories = new BigDecimal(object.getString("sport_calories"));
		 * String gpsStatus = object.getString("gps_status"); BigDecimal
		 * gpsDistence = new BigDecimal(object.getString("gps_distence"));
		 * Integer modeChangeTimes =
		 * UgcStringUtil.parseInteger(object.getString("mode_change_times"));
		 * BigDecimal stepCalc = new BigDecimal(object.getString("step_calc"));
		 * BigDecimal stepError = new
		 * BigDecimal(object.getString("step_error"));
		 * 
		 * System.out.println(
		 * "==========================================================================="
		 * ); System.out.println("employeeRecordModel : " +
		 * employeeRecordModel); System.out.println("sportDate           : " +
		 * sportDate); System.out.println("sportStartTime      : " +
		 * sportStartTime); System.out.println("sportEndTime        : " +
		 * sportEndTime); System.out.println("sportElapseTime     : " +
		 * sportElapseTime); System.out.println("sportStep           : " +
		 * sportStep); System.out.println("singleMaxSteps      : " +
		 * singleMaxSteps); System.out.println("sportDistence       : " +
		 * sportDistence); System.out.println("sportSpeed          : " +
		 * sportSpeed); System.out.println("sportCalories       : " +
		 * sportCalories); System.out.println("gpsStatus           : " +
		 * gpsStatus); System.out.println("gpsDistence         : " +
		 * gpsDistence); System.out.println("modeChangeTimes     : " +
		 * modeChangeTimes); System.out.println("stepCalc            : " +
		 * stepCalc); System.out.println("stepError           : " + stepError);
		 * System.out.println(
		 * "==========================================================================="
		 * );
		 * 
		 * for (int i = 1; i < jsonArray.size(); i++) { object =
		 * jsonArray.getJSONObject(i); employeeRecordModel =
		 * object.getString("employee_record_model"); sportDate =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.YYYY_MM_DD,
		 * object.getString("sport_date")); sportStartTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_start_time")); sportEndTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_end_time")); sportElapseTime =
		 * UgcDateUtil.convertStringToDate(UgcDateUtil.TIME_PATTERN,
		 * object.getString("sport_elapse_time")); gpsStatus =
		 * object.getString("gps_status");
		 * 
		 * 
		 * if(singleMaxSteps <
		 * UgcStringUtil.parseInteger(object.getString("sport_step"))) {
		 * singleMaxSteps =
		 * UgcStringUtil.parseInteger(object.getString("sport_step")); }
		 * sportStep +=
		 * UgcStringUtil.parseInteger(object.getString("sport_step"));
		 * sportDistence = sportDistence.add(new
		 * BigDecimal(object.getString("sport_distence"))); sportSpeed =
		 * sportSpeed.add(new BigDecimal(object.getString("sport_speed")));
		 * sportCalories = sportCalories.add(new
		 * BigDecimal(object.getString("sport_calories"))); gpsDistence =
		 * gpsDistence.add(new BigDecimal(object.getString("gps_distence")));
		 * 
		 * modeChangeTimes +=
		 * UgcStringUtil.parseInteger(object.getString("mode_change_times"));
		 * stepCalc = stepCalc.add(new
		 * BigDecimal(object.getString("step_calc"))); stepError =
		 * stepError.add(new BigDecimal(object.getString("step_error"))); }
		 * 
		 * //判断"员工运动统计信息表(EmployeeReport)"中userCode对应的记录是否已存在 Boolean result =
		 * true; //存在标识(true : 已存在, false : 不存在) String judgeSql =
		 * "SELECT report.* FROM tb_c_employee_report report WHERE report.employee_id = ?"
		 * ; List<Map<String, Object>> judgeResultList =
		 * this.jdbcTemplate.queryForList(judgeSql, userCode); EmployeeReport
		 * record = null; BigDecimal avgSpeed = sportSpeed.divide(new
		 * BigDecimal(jsonArray.size()), 2, BigDecimal.ROUND_HALF_UP); Integer
		 * avgSteps = sportStep / jsonArray.size(); BigDecimal steps =
		 * stepCalc.divide(new BigDecimal(jsonArray.size()), 2,
		 * BigDecimal.ROUND_HALF_UP); if(judgeResultList.isEmpty()) {
		 * //不存在，插入一条主键为userCode的新纪录 record = new EmployeeReport();
		 * record.setEmployeeId(userCode);
		 * record.setAllSportTimes(jsonArray.size()); //
		 * record.setAllSportElpseTime(sportElapseTime);
		 * record.setAllSportSteps(sportStep);
		 * record.setAllSportDistence(sportDistence);
		 * record.setAvgSportSpeed(sportSpeed.divide(new
		 * BigDecimal(jsonArray.size()), 2, BigDecimal.ROUND_HALF_UP));
		 * record.setAvgSportSteps(sportStep/jsonArray.size()); //
		 * record.setAvgSportTime(avgSportTime); record.setCurrentRank(0);
		 * record.setSportLevel(0); // record.setSportFirstDate(sportFirstDate);
		 * // record.setSportNearestTime(sportNearestTime);
		 * record.setSingleMaxSteps(singleMaxSteps); //
		 * record.setSingleMaxTime(singleMaxTime); //
		 * record.setGpsTimes(gpsTimes); record.setAllGpsDistence(gpsDistence);
		 * record.setAllGpsSteps(gpsDistence.divide(steps, 2,
		 * BigDecimal.ROUND_HALF_UP).intValue()); record.setSteps(steps);
		 * record.setMedalNumbers(0); System.out.println(
		 * "==========================================================================="
		 * ); this.saveOrUpdate(record); // System.out.println("ID  : " + id);
		 * System.out.println(
		 * "==========================================================================="
		 * );
		 * 
		 * } else { result = true;
		 * record.setAllSportTimes(Integer.parseInt(judgeResultList
		 * .get(0).get("all_sport_times").toString()) + jsonArray.size()); //
		 * record.setAllSportElpseTime(sportElapseTime);
		 * record.setAllSportSteps(
		 * Integer.parseInt(judgeResultList.get(0).get("all_sport_steps"
		 * ).toString()) + sportStep);
		 * record.setAllSportDistence(sportDistence.add(new
		 * BigDecimal(object.getString("all_sport_distence")))); BigDecimal
		 * totalSpeed = avgSpeed.add(new
		 * BigDecimal(object.getString("avg_sport_speed")));
		 * record.setAvgSportSpeed(totalSpeed.divide(new BigDecimal(2), 2,
		 * BigDecimal.ROUND_HALF_UP)); record.setAvgSportSteps((avgSteps +
		 * Integer
		 * .parseInt(judgeResultList.get(0).get("all_sport_times").toString()))
		 * / 2);
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * record = (EmployeeReport) judgeResultList.get(0); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Map<String, Object> map = new HashMap<String, Object>(); //
		 * map.put("cmd", "uploadrecord"); // try { // String sql = ""; // // //
		 * // // //查询员工运动统计信息表中的所有记录 // String sqlExt =
		 * "SELECT report.* FROM tb_c_employee_report report ORDER BY report.all_sport_steps DESC"
		 * ; // //更新用户排名SQL // String updateSQL =
		 * "UPDATE tb_c_employee_report SET current_rank = "; //
		 * List<Map<String, Object>> list =
		 * this.jdbcTemplate.queryForList(sqlExt); // //更新用户排名 // for(int i = 0;
		 * i < list.size(); i++) { // // this.jdbcTemplate.execute(updateSQL);
		 * // } // map.put("code", "0"); // } catch (Exception e) { //
		 * e.printStackTrace(); // map.put("code", "1"); // } return map;
		 **/
	}
}
