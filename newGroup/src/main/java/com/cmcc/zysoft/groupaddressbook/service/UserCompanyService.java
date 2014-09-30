// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.PCHeadshipDao;
import com.cmcc.zysoft.groupaddressbook.dao.UserCompanyDao;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MUserCompanyService;
import com.cmcc.zysoft.groupaddressbook.util.SendMsgTest;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sellmanager.model.SmsConfig;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.DepartmentService;
import com.cmcc.zysoft.sysmanage.service.SystemUserPCService;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserCompanyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午9:25:01
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Service
public class UserCompanyService extends BaseServiceImpl<UserCompany, String> {
	
	@Resource
	private UserCompanyDao userCompanyDao;
	
	@Resource
	private PCHeadshipDao pcHeadshipDao;
	@Resource
	private PCEmployeeService pcEmployeeService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private DepartmentService departmentService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private ImportService importService;
	
	@Resource
	private PCHeadshipService pcHeadshipService;
	
	@Resource
	private MUserCompanyService mUserCompanyService;
	
	@Resource
	private MEmployeeService employeeService; 
	
	@Resource
	private UserDepartmentService userDepartmentService;
	
	@Resource
	private SmsConfigService smsConfigService;
	
	@Resource
	private SystemUserPCService systemUserPCService;
	@Resource
	private CompanyVersionService companyVersionService;
	
	@Override
	public HibernateBaseDao<UserCompany, String>getHibernateBaseDao(){
		return this.userCompanyDao;
	}
	
	/**
	 * 新增通讯录成员(tb_c_user_company表）.
	 * @param userCompany
	 * @return 
	 * 返回类型：String
	 */
	public String add(UserCompany userCompany,String departmentId,String headshipId,String gridNumber){
		//删除type=3的版本
		companyVersionService.delVsersion(userCompany.getCompanyId());
		//同部门、同职位的人是否已存在
		if(userCompanyDao.getUserByMobileANDCompanyANDDepartment(userCompany.getMobile().replaceAll("\\s", ""), userCompany.getCompanyId(), departmentId).size()>0)
		{
			return "SAME";
		}
		userCompany.setManageFlag("0");
		String mobile = userCompany.getMobile().replaceAll("\\s", "");
		//这个变量放到外面，如果是组织用户，还需要添加用户部门之间的对应关系
		Company company = this.companyService.getEntity(userCompany.getCompanyId());
		//判断该群组成员是否是系统用户,若已经被加入其它群组则是系统用户
		String employeeId = "";
		String employeeName = userCompany.getEmployeeName();
		List<Map<String, Object>> isUser = this.pcEmployeeService.isUserAll(mobile);
		if(isUser.size() > 0){
			employeeId = isUser.get(0).get("employee_id").toString();
			userCompany.setDepartmentName(company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId));
			//更改状态
			this.pcEmployeeService.enableUser(employeeId);
		}else{
			if("1".equals(company.getOrgFlag())){
				employeeId = this.pcEmployeeService.add(employeeName, mobile,departmentId,headshipId,userCompany.getDisplayOrder(),gridNumber);
				this.userService.addUser(employeeId, mobile,userCompany.getCompanyId());
				userCompany.setDepartmentName(company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId));
				Headship headship = this.pcHeadshipService.getEntity(headshipId);
				userCompany.setHeadshipName(headship.getHeadshipName());
			}else{
				employeeId = this.pcEmployeeService.add(employeeName, mobile,"001","",userCompany.getDisplayOrder(),gridNumber);
				this.userService.addUser(employeeId, mobile,"001");
			}
		}
		//mod by zhangjun 20131206 不管企业下是否已存在该用户都添加一笔数据
		//List<UserCompany> isUerCompany = this.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, 
		//		new Object[]{userCompany.getCompanyId(),employeeId,"0"}); 
		//if(isUerCompany.size()==0){
			userCompany.setEmployeeId(employeeId);
			userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
			userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
			userCompany.setDelFlag("0");
			String userCompanyId = this.userCompanyDao.save(userCompany);
			if("1".equals(company.getOrgFlag())){
				addUserDept(userCompanyId, departmentId, headshipId, company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId),userCompany.getDisplayOrder(),userCompany.getHeadshipName());
			}
			if(StringUtils.hasText(userCompanyId)){
				this.groupVersionService.addGroupVersion(userCompanyId, "0");
				return "SUCCESS";
			}else{
				return "FAILURE";
			}
		/*}else{
			//如果是当前登录用户的公司，只是添加一个新部门，则是兼职情况
			boolean isUNIQUE = true;
			String currentUserCmpId = userCompany.getCompanyId();
			for(UserCompany userCmp : isUerCompany)
			{
				//更新员工姓名
				userCmp.setEmployeeName(employeeName);
				this.userCompanyDao.saveOrUpdate(userCmp);
				String cmpId = userCmp.getCompanyId();
				if(currentUserCmpId.equals(cmpId))
				{
					addUserDept(userCmp.getUserCompanyId(), departmentId, headshipId, company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId),userCompany.getDisplayOrder(),userCompany.getHeadshipName());
					isUNIQUE = false;
					this.groupVersionService.addGroupVersion(userCmp.getUserCompanyId(), "0");
					break;
				}
			}
			return isUNIQUE?"UNIQUE":"SUCCESS";
		}*/
	}
	
	/**
	 * 添加部门、用户的对应关系
	 * @param user_company_id
	 * @param department_id
	 * @param headship
	 * @param department_path
	 * @param displayOrder 排序字段
	 */
	private void addUserDept(String user_company_id,String department_id,String headship,String department_path,int displayOrder,String headship_name)
	{
		UserDepartment userDept = new UserDepartment();
		userDept.setDepartmentId(department_id);
		userDept.setDepartmentPath(department_path);
		userDept.setHeadshipId(headship);
		userDept.setHeadshipName(headship_name);
		userDept.setTaxis(displayOrder);
		userDept.setUserCompanyId(user_company_id);
		userDept.setVisibleFlag("1");
		this.userDepartmentService.insertEntity(userDept);
	}
	
	/**
	 * 更新信息.
	 * @param userCompany 
	 * 返回类型：void
	 */
	public String update(UserCompany userCompany,String departmentId,String headshipId,String gridNumber,String userCompanyIdAndDepartmentId){
		String userCompanyId = userCompany.getUserCompanyId();
		String[] strs = userCompanyIdAndDepartmentId.split("[|]");
		String old_department_id = strs[1];
		Company company = this.companyService.getEntity(userCompany.getCompanyId());
		UserCompany old_userCompany = this.userCompanyDao.get(userCompanyId);
		//Employee old_employee = this.employeeService.getEntity(old_userCompany.getEmployeeId());
		String employeeId=this.employeeService.getEmployeeIdByMobile(userCompany.getMobile().replaceAll("\\s", ""));
		if(!"".equals(employeeId)){
			//启用system_user
			List<SystemUser> list = this.systemUserPCService.findByNamedParam("employeeId", employeeId);
			if(!list.isEmpty()){
				if(list.get(0).getDelFlag().equals("1"))
				{
					SystemUser sysUser = list.get(0);
					sysUser.setDelFlag("0");
					this.systemUserPCService.updateEntity(sysUser);
				}
			}
		}
		if(!userCompany.getMobile().replaceAll("\\s", "").equals(old_userCompany.getMobile().replaceAll("\\s", ""))){
			//主要号码更改.
			//主要号码更改,需将登陆账号修改.
			//判断是否重复
			//mod by zhangjun 2013/11/20 同部门下号码不可重复
			if(this.userCompanyDao.isExistMobileByDep(userCompany.getCompanyId(), departmentId, userCompany.getMobile().replaceAll("\\s", ""))){
				return "号码重复,请重新输入手机号码！";
			}
			//如果部门有更换，则先删除原部门
			if(!old_department_id.equals(departmentId))
			{
				//同部门、同职位的人是否已存在
				if(userCompanyDao.getUserByMobileANDCompanyANDDepartment(userCompany.getMobile().replaceAll("\\s", ""), userCompany.getCompanyId(), departmentId).size()>0)
				{
					return "SAME";
				}
				this.userDepartmentService.deleteUserDepartment(userCompanyId, old_department_id);
			}
			//这种情况增加一个逻辑，为了修改客户端修改号码更新的bug--2013.12.25@zhouyu
			this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "2", old_userCompany.getMobile().replaceAll("\\s", ""));
			
			
			List<Employee> empList = this.pcEmployeeService.findByNamedParam(new String[]{"mobile","delFlag"}, new String[]{userCompany.getMobile().replaceAll("\\s", ""),"0"});
			if("1".equals(company.getOrgFlag())){
				old_userCompany.setDepartmentName(company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId));
				Headship headship = this.pcHeadshipService.getEntity(headshipId);
				old_userCompany.setHeadshipName(headship.getHeadshipName());
				
				//更新用户部门隶属信息
				List<UserDepartment> listUdept = this.userDepartmentService.
						findByNamedParam(new String[]{"userCompanyId","departmentId"},
								new String[]{userCompany.getUserCompanyId(),departmentId});
				if (listUdept.size()>0)
				{
					UserDepartment userDept = listUdept.get(0);
					logger.debug("判断用户部门信息是否存在。貌似是没有必要的操作");
					userDept.setDepartmentId(departmentId);
					userDept.setDepartmentPath(company.getCompanyName() + "-" + this.importService.fullDepartmentName("", departmentId));
					userDept.setHeadshipId(headshipId);
					userDept.setUserCompanyId(userCompanyId);
					userDept.setTaxis(userCompany.getDisplayOrder());
					userDept.setHeadshipName(headship.getHeadshipName());
					userDept.setVisibleFlag("1");
					this.userDepartmentService.updateEntity(userDept);
					this.userDepartmentService.getHibernateBaseDao().flush();
				}
				else
				{
					UserDepartment userDept = new UserDepartment();
					userDept.setDepartmentId(departmentId);
					userDept.setDepartmentPath(company.getCompanyName() + "-" + this.importService.fullDepartmentName("", departmentId));
					userDept.setHeadshipId(headshipId);
					userDept.setHeadshipName(headship.getHeadshipName());
					userDept.setUserCompanyId(userCompanyId);
					userDept.setTaxis(userCompany.getDisplayOrder());
					userDept.setVisibleFlag("1");
					this.userDepartmentService.insertEntity(userDept);
				}
				
			}else{
				old_userCompany.setDepartmentName(userCompany.getDepartmentName());
				old_userCompany.setHeadshipName(userCompany.getHeadshipName());
			}
			old_userCompany.setMobile(userCompany.getMobile().replaceAll("\\s", ""));
			if(!"".equals(employeeId)){
				old_userCompany.setEmployeeId(employeeId);
			}
			old_userCompany.setEmployeeName(userCompany.getEmployeeName());
			old_userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
			old_userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
			old_userCompany.setMobile(userCompany.getMobile().replaceAll("\\s", ""));
			old_userCompany.setMobileShort(userCompany.getMobileShort());
			old_userCompany.setTelephone2(userCompany.getTelephone2());
			old_userCompany.setTelShort(userCompany.getTelShort());
			old_userCompany.setWeibo(userCompany.getWeibo());
			old_userCompany.setWeixin(userCompany.getWeixin());
			old_userCompany.setSchool(userCompany.getSchool());
			old_userCompany.setUserMajor(userCompany.getUserMajor());
			old_userCompany.setUserGrade(userCompany.getUserGrade());
			old_userCompany.setUserClass(userCompany.getUserClass());
			old_userCompany.setStudentId(userCompany.getStudentId());
			old_userCompany.setEmail(userCompany.getEmail());
			old_userCompany.setQq(userCompany.getQq());
			old_userCompany.setNativePlace(userCompany.getNativePlace());
			old_userCompany.setAddress(userCompany.getAddress());
			old_userCompany.setHomeAddress(userCompany.getHomeAddress());
			old_userCompany.setRemark(userCompany.getRemark());
			old_userCompany.setMood(userCompany.getMood());
			old_userCompany.setDisplayOrder(userCompany.getDisplayOrder());
			this.userCompanyDao.update(old_userCompany);
			List<UserCompany> updateEmplist = this.mUserCompanyService.findByNamedParam("employeeId", old_userCompany.getEmployeeId());
			for(UserCompany us : updateEmplist){
				this.groupVersionService.addGroupVersion(us.getUserCompanyId(), "1");
			}
			if(empList.size()==0){
				if("1".equals(company.getOrgFlag())){
					Employee employee = this.employeeService.getEntity(old_userCompany.getEmployeeId());
					employee.setDepartmentId(departmentId);
					employee.setDepartmentName(getParentDepartmentName(departmentId));
					employee.setMobile(userCompany.getMobile().replaceAll("\\s", ""));
					employee.setEmployeeName(userCompany.getEmployeeName());
					employee.setHeadshipId(headshipId);
					if(StringUtils.hasText(gridNumber))
					{
						employee.setGridNumber(gridNumber);
					}
					employee.setDisplayOrder(userCompany.getDisplayOrder());
					this.employeeService.updateEntity(employee);
				}
				this.employeeService.updateEmpMobile(old_userCompany.getEmployeeId(), userCompany.getMobile().replaceAll("\\s", ""));
			}
			return "SUCCESS";
			//mod by zhangjun 2013/11/20
		}else{
			//如果部门有更换，则先删除原部门
			if(!old_department_id.equals(departmentId))
			{
				//同部门、同职位的人是否已存在
				if(userCompanyDao.getUserByMobileANDCompanyANDDepartment(userCompany.getMobile().replaceAll("\\s", ""), userCompany.getCompanyId(), departmentId).size()>0)
				{
					return "SAME";
				}
				this.userDepartmentService.deleteUserDepartment(userCompanyId, old_department_id);
			}
			if("1".equals(company.getOrgFlag())){
				Employee employee = this.employeeService.getEntity(old_userCompany.getEmployeeId());
				employee.setDepartmentId(departmentId);
				old_userCompany.setDepartmentName(company.getCompanyName()+"-"+this.importService.fullDepartmentName("", departmentId));
				Headship headship = this.pcHeadshipService.getEntity(headshipId);
				employee.setHeadshipId(headshipId);
				employee.setDepartmentName(getParentDepartmentName(departmentId));
				employee.setEmployeeName(userCompany.getEmployeeName());
				if(StringUtils.hasText(gridNumber))
				{
					employee.setGridNumber(gridNumber);
				}
				employee.setDisplayOrder(userCompany.getDisplayOrder());
				old_userCompany.setHeadshipName(headship.getHeadshipName());
				this.employeeService.updateEntity(employee);

				
				//更新用户部门隶属信息
				List<UserDepartment> listUdept = this.userDepartmentService.
						findByNamedParam(new String[]{"userCompanyId","departmentId"},
								new String[]{userCompany.getUserCompanyId(),departmentId});
				if (listUdept.size()>0)
				{
					UserDepartment userDept = listUdept.get(0);
					logger.debug("判断用户部门信息是否存在。貌似是没有必要的操作");
					userDept.setDepartmentId(departmentId);
					userDept.setDepartmentPath(company.getCompanyName() + "-" + this.importService.fullDepartmentName("", departmentId));
					userDept.setHeadshipId(headshipId);
					userDept.setUserCompanyId(userCompanyId);
					userDept.setTaxis(userCompany.getDisplayOrder());
					userDept.setVisibleFlag("1");
					userDept.setHeadshipName(headship.getHeadshipName());
					this.userDepartmentService.updateEntity(userDept);
					this.userDepartmentService.getHibernateBaseDao().flush();
				}
				else
				{
					UserDepartment userDept = new UserDepartment();
					userDept.setDepartmentId(departmentId);
					userDept.setDepartmentPath(company.getCompanyName() + "-" + this.importService.fullDepartmentName("", departmentId));
					userDept.setHeadshipId(headshipId);
					userDept.setHeadshipName(headship.getHeadshipName());
					userDept.setUserCompanyId(userCompanyId);
					userDept.setTaxis(userCompany.getDisplayOrder());
					userDept.setVisibleFlag("1");
					this.userDepartmentService.insertEntity(userDept);
				}
				
			}else{
				old_userCompany.setDepartmentName(userCompany.getDepartmentName());
				old_userCompany.setHeadshipName(userCompany.getHeadshipName());
			}
			old_userCompany.setEmployeeName(userCompany.getEmployeeName());
			old_userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
			old_userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
			old_userCompany.setMobile(userCompany.getMobile().replaceAll("\\s", ""));
			old_userCompany.setMobileShort(userCompany.getMobileShort());
			old_userCompany.setTelephone2(userCompany.getTelephone2());
			old_userCompany.setTelShort(userCompany.getTelShort());
			old_userCompany.setWeibo(userCompany.getWeibo());
			old_userCompany.setWeixin(userCompany.getWeixin());
			old_userCompany.setSchool(userCompany.getSchool());
			old_userCompany.setUserMajor(userCompany.getUserMajor());
			old_userCompany.setUserGrade(userCompany.getUserGrade());
			old_userCompany.setUserClass(userCompany.getUserClass());
			old_userCompany.setStudentId(userCompany.getStudentId());
			old_userCompany.setEmail(userCompany.getEmail());
			old_userCompany.setQq(userCompany.getQq());
			old_userCompany.setNativePlace(userCompany.getNativePlace());
			old_userCompany.setAddress(userCompany.getAddress());
			old_userCompany.setHomeAddress(userCompany.getHomeAddress());
			old_userCompany.setRemark(userCompany.getRemark());
			old_userCompany.setMood(userCompany.getMood());
			old_userCompany.setDisplayOrder(userCompany.getDisplayOrder());
			this.userCompanyDao.update(old_userCompany);
			this.getHibernateBaseDao().flush();
			this.groupVersionService.addGroupVersion(userCompanyId, "1");
			/*List<UserCompany> updateEmplist = this.mUserCompanyService.findByNamedParam("employeeId", old_userCompany.getEmployeeId());
			for(UserCompany us : updateEmplist){
				this.groupVersionService.addGroupVersion(us.getUserCompanyId(), "1");
			}*/
			return "SUCCESS";
		}	
	}
	
	/**
	 * 获取某一部门的顶级部门名称
	 * @param department_id
	 * @return
	 */
	private String getParentDepartmentName(String department_id)
	{
		String department_name = "";
		Department dept = this.departmentService.getEntity(department_id);
		if(!dept.getParentDepartmentId().equals("0"))
		{
			department_name = getParentDepartmentName(dept.getParentDepartmentId());
		}
		else
		{
			department_name = dept.getDepartmentName();
		}
		return department_name;
	}
	
	/**
	 * 导入时检查当前登录用户是否已在分组信息里,如果不存在,则接下来应该插入一条记录.
	 * @param companyId
	 * @param employeeId
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkSelf(String companyId,String employeeId){
		return this.userCompanyDao.checkSelf(companyId, employeeId);
	}
	
	/**
	 * 检测当前用户是否是某个分组的管理员
	 * @param companyId:分组Id
	 * @param employeeId：当前用户的员工编号
	 * @return
	 */
	public boolean checkGroupUserManage(String companyId,String employeeId)
	{
		return this.userCompanyDao.checkGroupUserManage(companyId, employeeId);
	}
	
	/**
	 * 导入时,导入信息人即登录人不在导入的表格里,则默认插入一条记录.
	 * @param employee
	 * @param companyId  所在企业分组
	 * @param departmentName 部门
	 * 返回类型：void
	 */
	public String addSelf(Employee employee,String companyId,String departmentName,String headshipName,String type){
		Company company = this.companyService.getEntity(companyId);
		boolean checkSelf = this.userCompanyDao.checkSelf(companyId, employee.getEmployeeId());
		if(!checkSelf){
			UserCompany userCompany = new UserCompany();
			userCompany.setCompanyId(companyId);
			userCompany.setEmployeeId(employee.getEmployeeId());
			userCompany.setUserCompany(company.getCompanyName());
			userCompany.setEmployeeName(employee.getEmployeeName());
			userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
			userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
			userCompany.setDepartmentName(company.getCompanyName()+"-"+this.importService.fullDepartmentName("", employee.getDepartmentId()));
			userCompany.setHeadshipName(headshipName);
			userCompany.setMobile(employee.getMobile().replaceAll("\\s", ""));
			userCompany.setMobileShort(employee.getMobileShort());
			userCompany.setTelephone2(employee.getTelephone2());
			userCompany.setTelShort(employee.getTelShort());
			userCompany.setEmail(employee.getEmail());
			userCompany.setManageFlag(type);
			userCompany.setDelFlag("0");
			String userCompanyId = this.userCompanyDao.save(userCompany);
			this.groupVersionService.addGroupVersion(userCompanyId, "0");
			return userCompanyId;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 导入时,若为修改已导入的数据,则更新数据.
	 * @param employee
	 * @param companyId 
	 * 返回类型：void
	 */
	public void updateImportEmployee(Employee employee,String companyId){
		List<UserCompany> userCompanyList = this.findByNamedParam(new String[]{"companyId","employeeId","delFlag"},
				new Object[]{companyId,employee.getEmployeeId(),"0"});
		if(userCompanyList.size()>0){
			UserCompany userCompany = userCompanyList.get(0);
			userCompany.setEmployeeName(employee.getEmployeeName());
			userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userCompany.getEmployeeName()));
			userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userCompany.getEmployeeName()));
			userCompany.setMobile(employee.getMobile().replaceAll("\\s", ""));
			userCompany.setMobileShort(employee.getMobileShort());
			userCompany.setTelephone2(employee.getTelephone2());
			userCompany.setTelShort(employee.getTelShort());
			this.userCompanyDao.update(userCompany);
			this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "1");
		}
	}
	
	/**
	 * 根据部门获取所有用户
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getUserCompanyByDepartmentId(String department_id)
	{
		return this.userCompanyDao.getUserCompanyByDepartmentId(department_id);
	}
	
	/**
	 * 逻辑删除群组用户.
	 * @param userCompanyId 
	 * 返回类型：void
	 */
	public void deleteUserCompany(String userCompanyId,String departmentId){
		//删除用户部门隶属关系
		this.userDepartmentService.deleteUserDepartment(userCompanyId,departmentId);
		//如果还存在别的部门，则不禁用，否则，禁用之
		List<UserDepartment> listDept = this.userDepartmentService.findByNamedParam(new String[]{"userCompanyId","visibleFlag"}, new String[]{userCompanyId,"1"});
		UserCompany userCompany = this.userCompanyDao.get(userCompanyId);
		if(listDept.size() == 0){
			//禁用
			userCompany.setDelFlag("1");
			this.userCompanyDao.update(userCompany);
			this.groupVersionService.addGroupVersion(userCompanyId, "2");
		}
		else
		{
			this.groupVersionService.addGroupVersion(userCompanyId, "1");
		}
		List<UserCompany> list = this.findByNamedParam(new String[]{"employeeId","delFlag"}, new Object[]{userCompany.getEmployeeId(),"0"});
		if(list.size() == 0){
			//用户无群组.
			this.userCompanyDao.updateSystemUser(userCompany.getEmployeeId());
		}
	}

	/**
	 * 根据公司ID从tb_c_user_company表中获取所有人员的手机号-用于短信推广.
	 * @param companyId
	 * @param content
	 * @return Map<String, Object>
	 * @throws ParseException
	 * @code说明:0-进行短信推广(成功与否看result值),1-三天内不能再次进行推广
	 */
	public Map<String, Object> sendMsg(String companyId, String content,String sendObj) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> list = null;
		if("2".equals(sendObj)){//未注册用户
			list = this.userCompanyDao.getNoRegByCompanyId(companyId);
		}else{
			list = this.userCompanyDao.getUsersByCompanyId(companyId);
		}
		map.put("userNum", list.size());
		if(!list.isEmpty()) {
			String mobile = "";
			String allMobile ="";//所有用户
			String otherMobile ="";//异网用户
			String cmccMobile="";//移动用户
			String result = "";
			Date date = new Date();
			SmsConfig smsConfig = new SmsConfig();
			for(Map<String, Object> userMap : list) {
				String mobileStr = userMap.get("mobile").toString();
				allMobile += mobileStr + ",";
				if(isOtherNet(mobileStr)){
					otherMobile +=mobileStr+",";
				}else{
					cmccMobile +=mobileStr+",";
				}
			}
			if("1".equals(sendObj)){
				if("".equals(cmccMobile)){
					map.put("userNum", 0);
					return map;
				}
				mobile = cmccMobile.substring(0, cmccMobile.length() - 1);
			}else if("3".equals(sendObj)){
				if("".equals(otherMobile)){
					map.put("userNum", 0);
					return map;
				}
				mobile = otherMobile.substring(0, otherMobile.length() - 1);
				
			}else if("4".equals(sendObj) || "2".equals(sendObj)){
				if("".equals(allMobile)){
					map.put("userNum", 0);
					return map;
				}
				mobile = allMobile.substring(0, allMobile.length() - 1);
			}
			List<Map<String, Object>> sendHistory = this.smsConfigService.getConfigByCompanyId(companyId);
			if(sendHistory.isEmpty()) { //首次进行短信推广
				result = SendMsgTest.sendMsg(content, mobile);
				if("SUCCESS".equals(result)) { //短信推广成功,添加推广记录
					smsConfig.setCompanyId(companyId);
					smsConfig.setSendDate(date);
					this.smsConfigService.insertEntity(smsConfig);
				}
				map.put("code", "0");
			} else { //非首次进行短信推广-三天后才能再次进行推广(三天内不能再次进行推广)
				smsConfig = this.smsConfigService.getEntity(sendHistory.get(0).get("sms_config_id").toString());
				String sendDateStr = sendHistory.get(0).get("send_date").toString();
				Date sendDate = simpleDateFormat.parse(sendDateStr);
				if((date.getTime() - sendDate.getTime()) / (24*60*60*1000) >= 3) {
					result = SendMsgTest.sendMsg(content, mobile);
					if("SUCCESS".equals(result)) { //短信推广成功,更新推广记录
						smsConfig.setSendDate(date);
						this.smsConfigService.updateEntity(smsConfig);
					}
					map.put("code", "0");
				} else {
					map.put("code", "1");
				}
			}
			map.put("result", result);
		}
		return map;
	}
	/**
	 * 判断是否为异网号码
	 */
	private boolean isOtherNet(String mobile)
	{
		mobile = mobile.substring(0, 3);
		boolean ret = false;
		if (mobile.equals("130") || mobile.equals("131") || mobile.equals("132") || 
				mobile.equals("155") || mobile.equals("156") || mobile.equals("186") || mobile.equals("185"))
		{
			//联通号段
			ret = true;
		}
		else if(mobile.equals("133") || mobile.equals("153") || mobile.equals("189") || mobile.equals("180"))
		{
			//电信号段
			ret = true;
		}
		else
		{
			ret = false;
		}
		return ret;
	}
	/**
	 * 企业管理员设置
	 * @param userCompanyId
	 * @return
	 */
	public boolean manageEdit(String userCompanyId,String managerType,String managerDept){
		return this.userCompanyDao.manageEdit(userCompanyId,managerType,managerDept);
	}
	/**
	 * 根据企业ID获取所有用户企业关联信息
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getUserCompanyByCompanyId(String companyId)
	{
		return this.userCompanyDao.getUserCompanyByCompanyId(companyId);
	}
	/**
	 * 根据手机号更新短号和v网id
	 * @param mobile
	 * @param companyId
	 * @param shortNum
	 * @param groupCode
	 */
	public void updateShortNumVIdByMobile(String mobile,String shortNum,String groupCode){
		this.userCompanyDao.updateShortNumVIdByMobile(mobile,shortNum,groupCode);
		List<UserCompany> userCompanyList = this.findByNamedParam(new String[]{"mobile","delFlag"},
				new Object[]{mobile,"0"});
		if(null != userCompanyList && userCompanyList.size()>0){
			String userCompanyId = userCompanyList.get(0).getUserCompanyId();
			this.groupVersionService.addGroupVersion(userCompanyId, "1");
		}
		
	}
	/**
	 * 拖拽排序后更新display_order
	 * @param usercompanyId
	 * @param displayOrder
	 */
	public void updateDisplayOrderById(String companyName,String departmentId,String userCompanyId,int displayOrder){
		String fullName = companyName + "-" + this.importService.fullDepartmentName("", departmentId);
		this.userCompanyDao.updateDisplayOrderById(userCompanyId,fullName,displayOrder);
		//更新用户部门隶属信息
		List<UserDepartment> listUdept = this.userDepartmentService.
				findByNamedParam(new String[]{"userCompanyId","departmentId","visibleFlag"},
						new String[]{userCompanyId,departmentId,"1"});
		List<UserDepartment> lastDept = this.userDepartmentService.
				findByNamedParam(new String[]{"userCompanyId","visibleFlag"},
						new String[]{userCompanyId,"1"});
		if (listUdept.size()==0 && lastDept.size()>0)
		{
			UserDepartment userDept  = lastDept.get(0);
			userDept.setDepartmentId(departmentId);
			userDept.setDepartmentPath(fullName);
			this.userDepartmentService.updateEntity(userDept);
			
		}
		this.groupVersionService.addGroupVersion(userCompanyId, "1");
	}
	public List<Map<String,Object>> getUserCompanyNoShort(int number){
		return this.userCompanyDao.getUserCompanyNoShort(number);
	}
	
	/**
	 * 对于没有的职位进行添加
	 * @param companyId
	 * @param headshipName
	 * @return
	 */
	public String saveHeadship(String companyId,String headshipName ){
		String result = "";
		List<Headship> list = pcHeadshipService.findByNamedParam(new String[]{"companyId","headshipName"}, new Object[]{companyId,headshipName});
		if(list.size()<=0){
			Headship headship = new Headship();
			headship.setHeadshipName(headshipName);
			headship.setCompanyId(companyId);
			headship.setHeadshipLevel("3");
			headship.setDelFlag("0");
			//添加新的职位
			result = this.pcHeadshipDao.save(headship);
		}else{
			result=list.get(0).getHeadshipId();
		}
		
		return result;
	}	
	/**
	 * 获取最大的display_order
	 * @param companyId
	 * @return
	 */
	public int getMaxDisplayOrder(String companyId){
		return this.userCompanyDao.getMaxDisplayOrder(companyId);
	}
	/**
	 * 获取
	 * @param companyId 企业Id
	 * @return
	 */
	public List<Map<String,Object>> getFrobiddenInfo(String companyId){
		return this.userCompanyDao.getForbiddenUser(companyId);
	}
	/**
	 * 保存被禁用成员信息
	 * @param forbiddenMember
	 * @param companyId
	 */
	public void saveInfo(String forbiddenMember,String companyId){
		String userCompanyIds = "";
		if(forbiddenMember!=null&&StringUtils.hasText(forbiddenMember)){
			//解析字符串 "1,2,3,4"-->"'1','2','3','4'"
			userCompanyIds = praseStr(forbiddenMember);
		}
		this.userCompanyDao.saveInfo(userCompanyIds, companyId);
		
	}
	/**
	 * 解析字符用于SQL检索
	 * 
	 * @param str  "1,2,3,4"-->"'1','2','3','4'"
	 * @return
	 */
	private String praseStr(String str){
		String[] strList = str.split(",");
		String result = "";
		for(String id:strList){
			result+="'"+id+"',";
		}
		//截掉最后的逗号
		result = result.substring(0, result.length()-1);
		return result;
	}
	public List<Map<String,Object>> getUserAndHeadshipLevel(String headshipId){
		return this.userCompanyDao.getUserAndHeadshipLevel(headshipId);
	}
	public String getMinHeadshipLevel(String userCompanyId){
		return this.userCompanyDao.getMinHeadshipLevel(userCompanyId);
	}
	public void updateDeptName(String deptName,String userCompanyId){
		this.userCompanyDao.updateDeptName(deptName, userCompanyId);
	}
}