package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.DepartmentReport;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MDepartmentReportDao
 * <br />版本:1.0.0
 * <br />日期： 2013-6-11 上午22:38:51
 */

@Repository
public class MDepartmentReportDao extends HibernateBaseDaoImpl<DepartmentReport, String> {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 部门统计报表，部门ID如果为空，则统计当前公司下所有的部门
	 * @param company_id
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getDeptReport(String company_id,String department_id)
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "SELECT dept.department_name,rpt.count_times,rpt.avg_times,rpt.max_times," +
				"rpt.count_steps,rpt.avg_steps,rpt.max_steps," +
				"rpt.count_use_time,rpt.avg_use_time,rpt.max_use_time,rpt.max_level,max_order" +
				" FROM tb_c_department dept LEFT JOIN tb_c_department_report rpt ON " +
				"rpt.department_id=dept.department_id ";
		if(StringUtils.hasText(department_id))
		{
			//逗号分隔的部门ID
		}
		else
		{
			sql += " AND dept.company_id='" + company_id + "'";
		}
		return list;
	}
	
	/**
	 * 用户上传记录后，更新部门的统计信息
	 * @param userCode：当前登录的用户employee_id
	 * @param empRecList：上传的记录列表
	 */
	public void updateDepartmentReport(String userCode,List<EmployeeRecord> empRecList)
	{
		//难点是，不仅要更新当前用户所在的部门，还要更新当前用户的所有上级部门
		for(EmployeeRecord empRec : empRecList)
		{
			//防作弊
			if(!empRec.getMark1().equals("1")){
				String employee_id = empRec.getEmployeeId();
				String sql = "SELECT department_id FROM tb_c_employee WHERE employee_id=?";
				List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, employee_id);
				if(list.size()>0)
				{
					String department_id = list.get(0).get("department_id").toString();
					//调用递归函数，更新部门统计数据
					updateParentDepartmentReport(department_id,empRec);
				}
			}
		}
	}
	
	/**
	 * 更新部门统计数据
	 * @param departmentId 部门编号
	 * @param empRpt 当前上传的记录信息
	 */
	private void updateParentDepartmentReport(String departmentId,EmployeeRecord empRpt)
	{
		if(departmentId.equals("0")){
			return;
		}
		//先更新当前部门，再更新上级部门
		DepartmentReport deptRpt = this.get(departmentId);
		if(deptRpt != null)
		{
			deptRpt.setAvgSteps((deptRpt.getAvgSteps() + empRpt.getSportStep())/2);
			deptRpt.setAvgTimes((deptRpt.getAvgTimes() + 1)/2);
			//deptRpt.setAvgUseTime(getAvgFieldByDept(departmentId, "all_sport_elpse_time")); //平均用时
			deptRpt.setCountSteps(deptRpt.getCountSteps() + empRpt.getSportStep());
			deptRpt.setCountTimes(deptRpt.getCountTimes() + 1);
			deptRpt.setCountUseTime(deptRpt.getCountUseTime() + empRpt.getSportElapseTime()); //总用时计算
			deptRpt.setDepartmentId(departmentId);
			deptRpt.setMaxLevel(getMaxFieldByDept(departmentId,"sport_level"));
			deptRpt.setMaxOrder(getMaxFieldByDept(departmentId, "current_rank"));
			deptRpt.setMaxSteps(getMaxFieldByDept(departmentId, "all_sport_steps"));
			deptRpt.setMaxTimes(getMaxFieldByDept(departmentId, "all_sport_times"));
			//deptRpt.setMaxUseTime((long)getMaxFieldByDept(departmentId, "single_max_time"));
			this.saveOrUpdate(deptRpt);
		}
		else
		{
			deptRpt = new DepartmentReport();
			deptRpt.setAvgSteps(empRpt.getSportStep());
			deptRpt.setAvgTimes(1);
			long avgUseTime = empRpt.getSportElapseTime();
			deptRpt.setAvgUseTime(avgUseTime); //平均用时
			deptRpt.setCountSteps(empRpt.getSportStep());
			deptRpt.setCountTimes(1);
			deptRpt.setCountUseTime(avgUseTime); //总用时计算
			deptRpt.setDepartmentId(departmentId);
			deptRpt.setMaxLevel(getMaxFieldByDept(departmentId,"sport_level"));
			deptRpt.setMaxOrder(getMaxFieldByDept(departmentId, "current_rank"));
			deptRpt.setMaxSteps(getMaxFieldByDept(departmentId, "all_sport_steps"));
			deptRpt.setMaxTimes(getMaxFieldByDept(departmentId, "all_sport_times"));
			deptRpt.setMaxUseTime(avgUseTime);
			this.saveOrUpdate(deptRpt);
		}
		//如果有上级部门，则需求对上线部门进行更新
		String sql = "SELECT department_id,parent_department_id FROM tb_c_department WHERE department_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, departmentId);
		if(!list.get(0).get("parent_department_id").equals("0"))
		{
			updateParentDepartmentReport(list.get(0).get("parent_department_id").toString(), empRpt);
		}
	}
	
	/**
	 * 按部门提取当前部门某个字段的最大值
	 * @param departmentId
	 * @return
	 */
	private int getMaxFieldByDept(String departmentId,String field)
	{
		String sql = "SELECT IFNULL(MAX("+field+"),0) AS out_field FROM tb_c_employee_report WHERE " +
				"employee_id IN (SELECT employee_id FROM tb_c_employee WHERE department_id=?)";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, departmentId);
		return Integer.parseInt(list.get(0).get("out_field").toString());
	}
	
	/**
	 * 根据部门返回某个字段的平均值
	 * @param departmentId
	 * @param field
	 * @return
	 */
	private long getAvgFieldByDept(String departmentId,String field)
	{
		String sql = "SELECT IFNULL(AVG("+field+"),0) AS avg FROM tb_c_employee_report WHERE " +
				"employee_id IN (SELECT employee_id FROM tb_c_employee WHERE department_id=?)";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, departmentId);
		return Long.getLong(list.get(0).get("avg").toString());
	}
}
