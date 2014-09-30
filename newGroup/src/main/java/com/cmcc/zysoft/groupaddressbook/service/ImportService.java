// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.ImportDao;
import com.cmcc.zysoft.groupaddressbook.dto.EmployeeDto;
import com.cmcc.zysoft.groupaddressbook.dto.UserCompanyDto;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：importService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 下午4:27:44
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class ImportService extends BaseServiceImpl<Employee, String>{
	
	@Resource
	private PCHeadshipService pcHeadshipService;
	
	@Resource
	private ImportDao importDao;
	
	@Override
	public HibernateBaseDao<Employee, String>getHibernateBaseDao(){
		return this.importDao;
	}
	
	/**
	 * 增加联系人时,选择的员工职位类型.
	 * @param companyId 所属公司
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	List<Map<String, Object>> headShipList(String companyId){
		return this.importDao.headShipList(companyId);
	}
	
	/**
	 * 通过员工职位名称获取职位编码.
	 * @param headShipName 职位名称
	 * @param companyId 所属公司
	 * @return 
	 * 返回类型：String
	 */
	public String getHeadShipCodeByName(String headShipName, String companyId){
		String headShipId = this.importDao.getHeadShipCodeByName(headShipName,companyId);
		if("".equals(headShipId)){
			Headship headship = new Headship();
			headship.setCompanyId(companyId);
			headship.setHeadshipName(headShipName);
			headship.setHeadshipLevel("3");
			headship.setDelFlag("0");
			headShipId = this.pcHeadshipService.insertEntity(headship);
		}
		return headShipId;
	}
	
	/**
	 * 导入前删除之前的部门,人员信息.
	 * 返回类型：void
	 */
	public void deleteData(){
		this.importDao.deleteData();
	}
	
	/**
	 * 导出的人员信息.
	 * @param rights 权限
	 * @param departmentLevel 登陆人部门级别
	 * @param selfDepartmentId 登陆人部门Id
	 * @param companyId 公司Id
	 * @return 
	 * 返回类型：List<EmployeeDto>
	 */
	public List<EmployeeDto> exportList(String rights, int departmentLevel, String selfDepartmentId,String companyId){
		return this.importDao.exportList(rights, departmentLevel, selfDepartmentId,companyId);
	}
	
	/**
	 * 获取某一公司下所有的用户
	 * @param company_id
	 * @return
	 */
	public List<UserCompanyDto> getUsersList(String company_id)
	{
		return this.importDao.getUsersList(company_id);
	}
	/**
	 * 判断是否存在该电话号码.
	 * @param mobile 
	 * @return 
	 * 返回类型：String
	 */
	public String checkEmployee(String mobile){
		return this.importDao.checkEmployee(mobile);
	}
	
	/**
	 * 通过部门名字找到部门的全部上级部门名称.
	 * @param fullName 
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String fullDepartmentName(String fullName,String departmentId){
		return this.importDao.fullDepartmentName(fullName, departmentId);
	}
	
	/**
	 * 根据用户手机号码返回当前用户隶属信息（多部门、职位）
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getEmployeeInfoByMobile(String mobile)
	{
		return this.importDao.getEmployeeInfoByMobile(mobile);
	}
	/**
	 * 根据用户手机号码返回当前用户Id
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getEmployeeIdByMobile(String mobile)
	{
		return this.importDao.getEmployeeIdByMobile(mobile);
	}
}
