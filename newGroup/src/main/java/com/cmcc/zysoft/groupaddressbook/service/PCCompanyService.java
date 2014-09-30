// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.PCCompanyDao;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCCompanyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午10:51:49
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class PCCompanyService extends BaseServiceImpl<Company, String>{
	
	@Resource
	private PCCompanyDao pcCompanyDao;
	
	@Resource
	private CompanyVersionService companyVersionService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource 
	private PCEmployeeService pcEmployeeService;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private PCHeadshipService pcHeadshipService;
	
	@Resource
	private ImportService importService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Override
	public HibernateBaseDao<Company, String>getHibernateBaseDao(){
		return this.pcCompanyDao;
	}
	
	/**
	 * 新建分组(未删除,非虚拟,非组织)
	 * @param companyName
	 * @param employeeId
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> addUserCompany(String companyName,String employeeId){
		Map<String,Object> map = new HashMap<String,Object>();
		Company company = new Company();
		Employee employee = this.pcEmployeeService.getEntity(employeeId);
		Department department = this.deptMagService.getEntity(employee.getDepartmentId());
		Company company2 = this.pcCompanyDao.get(department.getCompany().getCompanyId());
		if(company2.getOrgFlag().equals("0")){
			map.put("success", "fasle");
			map.put("msg", "非企业用户不能添加群组");
		}else{
			List<Company> list = this.findByNamedParam(new String[]{"companyName","createMan","delFlag","vitrueFlag","orgFlag"}, 
					new Object[]{companyName,employeeId,"0","0","0"});
			if(list.size()>0){
				//同一用户创建相同名称通讯录
				String msg = "您已创建名为["+ companyName + "]的通讯录";
				map.put("success", "fasle");
				map.put("msg", msg);
			}else{
				company.setCompanyName(companyName);
				company.setParentCompanyId("0");
				company.setCreateMan(employeeId);
				company.setDelFlag("0");
				company.setVitrueFlag("0");
				company.setOrgFlag("0");
				String companyId = this.pcCompanyDao.save(company);
				if(StringUtils.hasText(companyId)){
					map.put("success", "true");
					map.put("msg", "创建成功");
					map.put("companyId", companyId);
					this.companyVersionService.addCompanyVersion(companyId, "0");
					Headship headship = this.pcHeadshipService.getEntity(employee.getHeadshipId());
					UserCompany userCompany = new UserCompany();
					userCompany.setCompanyId(companyId);
					userCompany.setEmployeeId(employeeId);
					userCompany.setUserCompany(company2.getCompanyName());
					userCompany.setEmployeeName(employee.getEmployeeName());
					userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
					userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
					userCompany.setDepartmentName(company2.getCompanyName()+"-"+importService.fullDepartmentName("", employee.getDepartmentId()));
					userCompany.setHeadshipName(headship.getHeadshipName());
					userCompany.setMobile(employee.getMobile());
					userCompany.setMobileShort(employee.getMobileShort());
					userCompany.setTelephone2(employee.getTelephone2());
					userCompany.setTelShort(employee.getTelShort());
					userCompany.setEmail(employee.getEmail());
					userCompany.setManageFlag("1");
					userCompany.setDelFlag("0");
					String userCompanyId = this.userCompanyService.insertEntity(userCompany);
					this.groupVersionService.addGroupVersion(userCompanyId, "0");
					
				}else{
					map.put("success", "fasle");
					map.put("msg", "创建失败");
				}
			}
		}
		return map;
	}
	
	/**
	 * 修改个人通讯录名称.
	 * @param companyId
	 * @param companyName
	 * @param employeeId
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> updateUserCompany(String companyId, String companyName,String employeeId){
		Map<String,Object> map = new HashMap<String,Object>();
		 Company company= this.pcCompanyDao.get(companyId);
		 if(companyName.equals(company.getCompanyName())){
			 map.put("success", "true");
			 map.put("msg", "修改成功");
			 this.companyVersionService.addCompanyVersion(companyId, "1");
		 }else{
			 List<Company> list = this.findByNamedParam(new String[]{"companyName","createMan"}, new Object[]{companyName,employeeId});
			 if(list.size()>0){
				 map.put("success", "false");
				 map.put("msg", "已存在该名称的通讯录"); 
			 }else{
				 company.setCompanyName(companyName);
				 this.pcCompanyDao.update(company);
				 map.put("success", "true");
				 map.put("msg", "修改成功");
				 this.companyVersionService.addCompanyVersion(companyId, "1");
			 }
		 }
		 return map;
	}
	
	/**
	 * 登录人所在企业通讯录.
	 * @param employee_id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public Pagination<?> orglist(int rows, int page, String employee_id,String key){
		return this.pcCompanyDao.orglist(rows, page, employee_id,key);
	}
	
	/**
	 * 登录人所在的非企业通讯录或自己创建的通讯录.
	 * @param employeeId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public  Pagination<?> list(int rows, int page,String employeeId,String key){
		return this.pcCompanyDao.list(rows, page, employeeId,key);
	}
	/**
	 * 登录人所在企业通讯录.
	 * @param employee_id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> orgAllList(String employee_id){
		return this.pcCompanyDao.orgAllList(employee_id);
	}
	
	/**
	 * 获取所有公司列表
	 * @return 
	 * 返回类型：List<Company>
	 */
	public List<Map<String, Object>> getAllCompany(){
		return this.pcCompanyDao.getAllCompany();
	}
	/**
	 * 根据用户权限返回公司 列表
	 * @return
	 */
	public List<Map<String,Object>> getAllCompanyByUser()
	{
		return this.pcCompanyDao.getAllCompanyByUser();
	}
	
	/**
	 * 获取消息类型列表
	 * @return
	 */
	public List<Map<String, Object>> getMassageType(){
		return this.pcCompanyDao.getMassageType();
	}
	
	/**
	 * 删除群组.
	 * @param companyId
	 * @return 
	 * 返回类型：String
	 */
	public String delete(String companyId) {
		Company company = this.pcCompanyDao.get(companyId);
		company.setDelFlag("1");
		this.pcCompanyDao.update(company);
		this.companyVersionService.addCompanyVersion(companyId, "2");
		return "SUCCESS";
	}
	
	/**
	 * 根据企业名称，返回企业信息
	 * @param company_name
	 * @return
	 */
	public Company getCompanyByCompanyName(String company_name)
	{
		return this.pcCompanyDao.getCompanyByCompanyName(company_name);
	}
	/**
	 * 获取所有企业所有人数
	 * @return
	 */
	public Long getAllCompanyEmployee(String key){
		return this.pcCompanyDao.getAllCompanyEmployee(key);
	}
	public List<Map<String,Object>> getManageCompany(String userId){
		return this.pcCompanyDao.getManageCompany(userId);
	}
}
