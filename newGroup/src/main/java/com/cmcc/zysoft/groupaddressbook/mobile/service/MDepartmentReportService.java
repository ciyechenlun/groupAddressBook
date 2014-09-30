package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MDepartmentReportDao;
import com.cmcc.zysoft.sellmanager.model.DepartmentReport;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
/**
 * @author 周瑜
 * @email zhouyusgs#ahmobile.com
 * @date 2013-6-11 下午22:51:29
 */

@Service
public class MDepartmentReportService extends BaseServiceImpl<DepartmentReport, String> {

	@Resource
	private MDepartmentReportDao mDepartmentReportDao;
	
	@Override
	public HibernateBaseDao<DepartmentReport, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.mDepartmentReportDao;
	}
	
	
	/**
	 * 部门统计报表，部门ID如果为空，则统计当前公司下所有的部门
	 * @param company_id
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getDeptReport(String company_id,String department_id)
	{
		return this.mDepartmentReportDao.getDeptReport(company_id, department_id);
	}
	
	/**
	 * 用户上传记录后，更新部门的统计信息
	 * @param userCode：当前登录的用户employee_id
	 * @param empRecList：上传的记录列表
	 */
	public void updateDepartmentReport(String userCode,List<EmployeeRecord> empRecList)
	{
		this.mDepartmentReportDao.updateDepartmentReport(userCode, empRecList);
	}
	
}
