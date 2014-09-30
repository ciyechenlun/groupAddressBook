// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.groupaddressbook.dao.PCEmployeeDao;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.groupaddressbook.webservice.client.GrayMembersClient;
import com.cmcc.zysoft.groupaddressbook.webservice.client.MemberShipICTClient;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.SysEmployeeService;
import com.cmcc.zysoft.sysmanage.service.SystemUserPCService;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCEmployeeService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午11:17:47
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Service
public class PCEmployeeService extends BaseServiceImpl<Employee, String>{
	
	@Resource
	private PCEmployeeDao pcEmployeeDao;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private SysEmployeeService employeeService;
	
	@Resource
	private SystemUserPCService systemUserPCService;
	
	@Resource
	private DepartmentVersionService departmentVersionService;
	
	@Resource
	private ImportService importService;
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private UserDepartmentService userDepartmentService;
	
	@Override
	public HibernateBaseDao<Employee, String>getHibernateBaseDao(){
		return this.pcEmployeeDao;
	}
	
	/**
	 * 用户创建群组之后,将其他人加入群组,则在员工表里添加条数据,之后将会在系统用户表里添加条数据,供新用户登录.
	 * @param employeeName
	 * @param mobile
	 * @return 
	 * 返回类型：String
	 */
	public String add(String employeeName,String mobile,String departmentId,String headshipId,int displayOrder,String gridNumber){
		Employee employee = new Employee();
		employee.setEmployeeName(employeeName);
		employee.setMobile(mobile);
		employee.setDepartmentId(departmentId);
		employee.setHeadshipId(headshipId);
		employee.setDelFlag("0");
		employee.setDisplayOrder(displayOrder);
		if (StringUtils.hasText(gridNumber))
		{
			employee.setGridNumber(gridNumber);
		}
		String employeeId = this.pcEmployeeDao.save(employee);
		if(StringUtils.hasText(employeeId)){
			return employeeId;
		}else{
			return "";
		}
	}
	
	/**
	 * 判断一个手机号码是否已经是用户.
	 * @param mobile
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> isUser(String mobile){
		return this.pcEmployeeDao.isUser(mobile);
	}
	
	/**
	 * 判断一个手机号码是否已经是用户.包括删除的用户
	 * @param mobile
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> isUserAll(String mobile){
		return this.pcEmployeeDao.isUserAll(mobile);
	}
	
	/**
	 * 恢复禁用的用户
	 * @param employee_id
	 */
	public void enableUser(String employee_id)
	{
		this.pcEmployeeDao.enableUser(employee_id);
	}
	
	/**
	 * 从workbook读入数据.
	 * @param workbook
	 * @return 
	 * 返回类型：String
	 */
	@Transactional
	public String importWorkbook(Workbook workbook){
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		String checkText = "";
		for (int r = 1; r < rows; r++){
			String[] checkDeptInfo = sheet.getCell(8, r).getContents().split("[-]");
			if(checkDeptInfo.length<2){
				checkText += "第"+(r+1)+"行组织机构格式有误,导入失败</br>";
				continue;
			}
			String checkMobile= sheet.getCell(2, r).getContents();
			if(checkMobile.length()>11 || "".equals(checkMobile)){
				checkText += "第"+(r+1)+"行电话号码有误,导入失败</br>";
				continue;
			}
		}
		if(StringUtils.hasText(checkText)){
			return checkText;
		}else{
			for (int r = 1; r < rows; r++){
				Company company = this.pcCompanyService.getEntity(SecurityContextUtil.getCompanyId());
				String companyId = company.getCompanyId();
				Employee employee = new Employee();
				SystemUser systemUser = new SystemUser();
				String parentDepartmentId="0";
				String[] deptInfo = sheet.getCell(8, r).getContents().split("[-]");
				//插入部门信息
				for(int i=1;i<deptInfo.length;i++){
					Department department = new Department();
					String dept_flag =this.deptMagService.checkDept(i, sheet.getCell(8, r).getContents(),companyId,parentDepartmentId); 
					//判断部门是否已存在.
					if(dept_flag == null){
						department.setDelFlag("0");
						department.setDepartmentFirstword(concatPinyin.
								changeToPinyin(deptInfo[i].substring(0, 1)));
						department.setParentDepartmentId(parentDepartmentId);
						department.setDepartmentName(deptInfo[i]);
						department.setCompany(company);
						department.setDepartmentLevel(i);
						department.setDisplayOrder(9999);
						parentDepartmentId = this.deptMagService.insertEntity(department);
						this.departmentVersionService.save("0", parentDepartmentId);
					}else{
						parentDepartmentId = this.deptMagService.
								checkDept(i, sheet.getCell(8, r).getContents(),companyId,parentDepartmentId);
						continue;
					}
				}
				//插入人员信息
				//先判断名字是否重复(姓名,部门,手机号)
				String employeId = this.importService.checkEmployee(sheet.getCell(2, r).getContents());
				//V网编号,如果为空  默认8888
				String VNumber = sheet.getCell(6, r).getContents();
				if("".equals(VNumber)){
					VNumber = "8888";
				}
//				如果重复,则修改该条记录,不重复则当做新记录插入.
				if(StringUtils.hasText(employeId)){
					Employee employees = this.employeeService.getEntity(employeId);
					employees.setDepartmentName(deptInfo[1]);
					employees.setDepartmentId(parentDepartmentId);
					String displayOrder = sheet.getCell(11, r).getContents();
					if("".equals(displayOrder)){
						employees.setDisplayOrder(999999);
					}else{
						employees.setDisplayOrder(Integer.parseInt(displayOrder));
					}
					boolean name = false;
					boolean headship = false;
					boolean mobileShort = false;
					boolean telShort = false;
					boolean telephone2 = false;
					boolean email = false;
					boolean gridNumber = false;
					if(!employees.getEmployeeName().equals(sheet.getCell(0, r).getContents())){
						employees.setEmployeeName(sheet.getCell(0, r).getContents());
						name = true;
					}
					if(!employees.getHeadshipId().equals(sheet.getCell(9, r).getContents())){
						employees.setHeadshipId(this.importService.getHeadShipCodeByName(sheet.getCell(9, r).getContents(),companyId));
						headship = true;
					}
					if(!employees.getMobileShort().equals(sheet.getCell(3, r).getContents())){
						employees.setMobileShort(sheet.getCell(3, r).getContents());
						mobileShort = true;
					}
					if(!employees.getTelShort().equals(sheet.getCell(5, r).getContents())){
						employees.setTelShort(sheet.getCell(5, r).getContents());
						telShort = true;
					}
					if(!employees.getTelephone2().equals(sheet.getCell(4, r).getContents())){
						employees.setTelephone2(sheet.getCell(4, r).getContents());
						telephone2 = true;
					}
					if(!employees.getEmail().equals(sheet.getCell(12, r).getContents())){
						employees.setEmail(sheet.getCell(12, r).getContents());
						email = true;
					}
					if(!employees.getGridNumber().equals(VNumber)){
						employees.setGridNumber(VNumber);
						gridNumber = true;
					}
					if(name || email || telephone2 || telShort || mobileShort || headship || gridNumber){
						this.employeeService.updateEntity(employees);
						List<UserCompany> ucList = this.userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, 
								new Object[]{companyId,employees.getEmployeeId(),"0"});
						if(ucList.size()>0){
							UserCompany userCompany = ucList.get(0);
							userCompany.setEmployeeName(sheet.getCell(0, r).getContents()); //组内成员名称
							userCompany.setMobile(sheet.getCell(2, r).getContents()); //手机号码
							userCompany.setMobileShort(sheet.getCell(3, r).getContents()); //手机短号
							userCompany.setTelephone2(sheet.getCell(4, r).getContents()); // 办公固话
							userCompany.setTelShort(sheet.getCell(5, r).getContents()); //办公短号
							userCompany.setUserCompany(sheet.getCell(7, r).getContents()); //单位
							userCompany.setDepartmentName(sheet.getCell(8, r).getContents()); //部门
							userCompany.setHeadshipName(sheet.getCell(9, r).getContents()); //职位
							userCompany.setAddress(sheet.getCell(10, r).getContents()); //办公地址
							userCompany.setEmail(sheet.getCell(12, r).getContents()); //邮箱
							userCompany.setQq(sheet.getCell(13, r).getContents()); //qq号码
							userCompany.setWeibo(sheet.getCell(14, r).getContents()); //微博
							userCompany.setWeixin(sheet.getCell(15, r).getContents()); //微信
							userCompany.setSchool(sheet.getCell(16, r).getContents()); //学校
							userCompany.setUserMajor(sheet.getCell(17, r).getContents()); //专业
							userCompany.setUserGrade(sheet.getCell(18, r).getContents()); //年级
							userCompany.setUserClass(sheet.getCell(19, r).getContents()); //班级
							userCompany.setStudentId(sheet.getCell(20, r).getContents()); //学号
							userCompany.setNativePlace(sheet.getCell(21, r).getContents()); //籍贯
							userCompany.setHomeAddress(sheet.getCell(23, r).getContents()); //家庭住址
							userCompany.setTelephone(sheet.getCell(24, r).getContents()); //宅电
							userCompany.setMood(sheet.getCell(26, r).getContents()); //心情
							userCompany.setManageFlag("0");//默认为普通成员
							userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(sheet.getCell(0, r).getContents()));
							userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(sheet.getCell(0, r).getContents()));
							this.userCompanyService.updateEntity(userCompany);
							this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "1");
						}
						List<SystemUser> systemUsers = this.systemUserPCService.
								findByNamedParam("employeeId", employees.getEmployeeId());
						SystemUser systemUser2 = systemUsers.get(0);
						systemUser2.setUserName(sheet.getCell(2, r).getContents());
						systemUser2.setRealName(sheet.getCell(0, r).getContents());
						systemUser2.setModifyTime(new Date());
						this.systemUserPCService.updateEntity(systemUser2);
					}
				}else{
					employee.setEmployeeName(sheet.getCell(0, r).getContents());
					employee.setDepartmentName(deptInfo[1]);
					employee.setHeadshipId(this.importService.getHeadShipCodeByName(sheet.getCell(9, r).getContents(),companyId));
					employee.setMobile(sheet.getCell(2, r).getContents());
					employee.setMobileShort(sheet.getCell(3, r).getContents());
					employee.setTelShort(sheet.getCell(5, r).getContents());
					employee.setTelephone2(sheet.getCell(4, r).getContents());
					employee.setEmail(sheet.getCell(12, r).getContents());
					String displayOrder = sheet.getCell(11, r).getContents();
					if("".equals(displayOrder)){
						employee.setDisplayOrder(999999);
					}else{
						employee.setDisplayOrder(Integer.parseInt(displayOrder));
					}
					employee.setGridNumber(VNumber);
					employee.setDelFlag("0");
					employee.setDepartmentId(parentDepartmentId);
					String employeeId = this.employeeService.insertEntity(employee);
					UserCompany userCompany = new UserCompany();
					userCompany.setEmployeeId(employeeId);
					userCompany.setCompanyId(companyId);
					userCompany.setEmployeeName(sheet.getCell(0, r).getContents()); //组内成员名称
					userCompany.setMobile(sheet.getCell(2, r).getContents()); //手机号码
					userCompany.setMobileShort(sheet.getCell(3, r).getContents()); //手机短号
					userCompany.setTelephone2(sheet.getCell(4, r).getContents()); // 办公固话
					userCompany.setTelShort(sheet.getCell(5, r).getContents()); //办公短号
					userCompany.setUserCompany(sheet.getCell(7, r).getContents()); //单位
					userCompany.setDepartmentName(sheet.getCell(8, r).getContents()); //部门
					userCompany.setHeadshipName(sheet.getCell(9, r).getContents()); //职位
					userCompany.setAddress(sheet.getCell(10, r).getContents()); //办公地址
					userCompany.setEmail(sheet.getCell(12, r).getContents()); //邮箱
					userCompany.setQq(sheet.getCell(13, r).getContents()); //qq号码
					userCompany.setWeibo(sheet.getCell(14, r).getContents()); //微博
					userCompany.setWeixin(sheet.getCell(15, r).getContents()); //微信
					userCompany.setSchool(sheet.getCell(16, r).getContents()); //学校
					userCompany.setUserMajor(sheet.getCell(17, r).getContents()); //专业
					userCompany.setUserGrade(sheet.getCell(18, r).getContents()); //年级
					userCompany.setUserClass(sheet.getCell(19, r).getContents()); //班级
					userCompany.setStudentId(sheet.getCell(20, r).getContents()); //学号
					userCompany.setNativePlace(sheet.getCell(21, r).getContents()); //籍贯
					userCompany.setHomeAddress(sheet.getCell(23, r).getContents()); //家庭住址
					userCompany.setTelephone(sheet.getCell(24, r).getContents()); //宅电
					userCompany.setMood(sheet.getCell(26, r).getContents()); //心情
					userCompany.setManageFlag("0");//默认为普通成员
					userCompany.setDelFlag("0");
					userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(sheet.getCell(0, r).getContents()));
					userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(sheet.getCell(0, r).getContents()));
					String userCompanyId = this.userCompanyService.insertEntity(userCompany);
					this.groupVersionService.addGroupVersion(userCompanyId, "0");
					String salt = UUIDUtil.generateUUID();
					systemUser.setCompany(company);
					systemUser.setEmployeeId(employeeId);
					systemUser.setDelFlag("0");
					systemUser.setCreateTime(new Date());
					systemUser.setPassSalt(salt);
					systemUser.setPassword(MD5Tools.encodePassword("111111", salt));
					systemUser.setRealName(sheet.getCell(0, r).getContents());
					systemUser.setUserName(sheet.getCell(2, r).getContents());
					this.systemUserPCService.insertEntity(systemUser);
				}
			}
			return "导入成功";
		}
	}
	
	/**
	 * 获取需进行灰度用户同步的所有员工信息.
	 * @param companyId 公司ID
	 * @param empIds 特定部门下的所有用户
	 * @param syncAllFlag 同步所有标志(true-同步所有人;false-同步选择部门下的人员)
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getEmpsList(String companyId, String empIds, Boolean syncAllFlag) {
		if(syncAllFlag) {
			return this.pcEmployeeDao.getEmpsList(companyId);
		} else {
			return this.pcEmployeeDao.getEmpByIds(empIds);
		}
	}
	
	/**
	 * 灰度用户同步-同步选择公司下的所有用户或者同步指定公司下特定部门下的所有用户(先进行成员订购,在进行灰度发布).
	 * @param companyId 公司ID
	 * @param empIds 特定部门下的所有用户
	 * @param syncAllFlag 同步所有标志(true-同步所有人;false-同步选择部门下的人员)
	 * @param testFlag 测试标记(0：非测试交易, 1：测试交易)
	 * @param ECCode EC企业代码: 要修改成员的业务的企业计费代码
	 * @param ECName EC企业名称: 该业务的企业名称
	 * @param PrdOrdNum 集团产品代码: 订购关系的唯一标示ICT0006->
	 * @param ServiseCode 服务标识: 服务的唯一标识
	 * @param ServiseName 服务名称
	 * @param ItemName 扩展信息名称
	 * @param ItemValue 信息值
	 * @param OptType 操作类型: 01－加入名单 02－退出名单  03－定购  04－取消定购  05-业务信息变更
	 * @param AuthType 登录鉴权类型: 0-普通鉴权 1-手机号码+IMSI绑定鉴权
	 * @return map
	 */
	public Map<String, Object> syncEmps(String companyId, String empIds, Boolean syncAllFlag, int testFlag, String ECCode,
			String ECName, String PrdOrdNum, String ServiseCode, String ServiseName, String ItemName, String ItemValue,
			String OptType, String AuthType) {
		//获取所有成员列表
		List<Map<String, Object>> empList = this.getEmpsList(companyId, empIds, syncAllFlag);
		Map<String, Object> map1 = new HashMap<String, Object>();
		//成员订购
		map1 = MemberShipICTClient.syncMemberShip(testFlag, ECCode, ECName, PrdOrdNum, ServiseCode, ServiseName, ItemName,
				ItemValue, empList, OptType, AuthType);
		//成员订购成功,进行灰度发布
		if("true".equals(map1.get("success").toString())) {
			//Map<String, Object> map2 = this.syncGrayMembers(empList);
			//map2.put("code", "0");
			//return map2;
			map1.put("code", "0");
			return map1;
		} else {
			map1.put("code", "1");
			return map1;
		}
	}
	
	/**
	 * 灰度用户发布.
	 * @param empList 灰度发布用户列表
	 * @return map
	 */
	public Map<String, Object> syncGrayMembers(List<Map<String, Object>> empList) {
		//测试标记:发起方填写，0：非测试交易，1：测试交易；测试必须是业务级别，即在同一个业务流水中所有交易必须具有相同的测试标记(默认为0即可)
		int testFlag = 1;
		//由BOSS统一分配的产品标识,如果不走BOSS进行集团订购的产品，则由云平台分配 如集团通讯录标准版:ICT0006
		String ProductCode = "ICT0006";
		//产品名称
		String ProductName = "集团通讯录标准版";
		//版本号:如：1.2.3。该版本必需在云平台上已存在且为体验版。根据版本号可以查询该版本的功能描述。
		String Version = "1.2.1";
		//描述信息:描述本次发布的目的。如：网达定制版、ICT中心内部测试版等等。 具体内容由业务平台提供，用于管理员审核的依据之一。
		String Desc = "测试test";
		//发布范围:对于灰度发布范围的描述。对应业务平台的用户筛选条件。
		String Scope = "测试test";
		//操作说明:01：添加体验用户 02：删除体验用户
		String Operate = "01";
		Map<String, Object> map = new HashMap<String, Object>();
		map = GrayMembersClient.syncGrayMembers(testFlag, ProductCode, ProductName, Version, Desc, Scope, Operate, empList);
		return map;
	}
	
	/**
	 * 获取用户所属分公司（顶级部门）
	 * @param employee_id
	 * @return
	 */
	public String getUserParentDepartment(String employee_id)
	{
		return this.pcEmployeeDao.getUserParentDepartment(employee_id);
	}
}
