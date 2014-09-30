// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.groupaddressbook.dao.LookGroupDao;
import com.cmcc.zysoft.groupaddressbook.dto.LookGroupInfoDto;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusers;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusersId;
import com.cmcc.zysoft.sellmanager.model.TxluserVersion;
import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.HeadshipService;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：LookGroupService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 上午11:52:35
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class LookGroupService extends BaseServiceImpl<Employee, String>{
	
	@Value("${upload.file.path}")
	private String path;
	
	@Resource
	private LookGroupDao lookGroupDao;
	
	@Resource
	private  TxlVersionService txlVersionService;
	
	@Resource
	private TxluserModifyService txluserModifyService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserDepartmentService userDepartmentService;
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private ImportService importService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private HeadshipService headshipService;
	@Resource
	private GroupVersionService groupVersionService;
	@Resource
	private CompanyVersionService companyVersionService;
	
	@Override
	public HibernateBaseDao<Employee, String> getHibernateBaseDao() {
		return this.lookGroupDao;
	}
	
	/**
	 * 查询集团通讯录人员信息.
	 * @param dto 
	 * @return 
	 * 返回类型：List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getGroupInfo(LookGroupInfoDto dto) {
		return this.lookGroupDao.getGroupInfo(dto);
	}
	
	/**
	 * 集团通讯录人员信息条数.
	 * @param dto 
	 * @return 
	 * 返回类型：int
	 */
	public int getGroupInfoCount(LookGroupInfoDto dto) {
		return this.lookGroupDao.getGroupInfoCount(dto);
	}
	
	/**
	 * 岗位下拉框.
	 * @param companyId 所属公司
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findHeadship(String companyId) {
		return this.lookGroupDao.findHeadship(companyId);
	}
	
	/**
	 * 新增、修改用户.
	 * @param emp 
	 * @param picture 
	 * @return 
	 * 返回类型：String
	 */
	public String add(Employee emp,String userCompanyId,MultipartFile picture){
		String pictureName = "";
		if(picture.getSize() != 0){
			String photoName = picture.getOriginalFilename();
			String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
			try {
	        	if((".gif").equals(extName.trim())||(".jpg").equals(extName.trim())||(".jpeg").equals(extName.trim())
	        			||(".png").equals(extName.trim())||(".bmp").equals(extName.trim())){
	    	        String lastFileName = System.currentTimeMillis()+extName;
	    	        String fileFullPath = path+File.separator+"picture_"+lastFileName;
	    			FileCopyUtils.copy(picture.getBytes(),new File(fileFullPath));
	    			pictureName = "picture_"+lastFileName;
	            }else{
	            	return "ERROR PHOTO";
	            }
			} catch (Exception e) {
				return "ERROR";
			}
		}
		String gridNumber = emp.getGridNumber();
		if("".equals(gridNumber)){
			gridNumber = "8888";
		}
		Company company = this.companyService.getEntity(SecurityContextUtil.getCompanyId());
		String employeeId = emp.getEmployeeId();
		//更新类型 0：新增，1：修改，2：删除
		String updateTypeFlag = "0";
		if(StringUtils.isBlank(emp.getEmployeeId())){
			String checkEmployee = this.importService.checkEmployee(emp.getMobile());
			if(org.springframework.util.StringUtils.hasText(checkEmployee)){
				return "SAME MOBILE"; //手机号码已存在
			}
			//新增人员表记录
			emp.setDelFlag("0");
			emp.setPicture(pictureName);
			emp.setDepartmentName(this.lookGroupDao.
					parentDepartmentName(emp.getDepartmentName(), emp.getDepartmentId()));
			employeeId = this.lookGroupDao.save(emp);
			Headship headship = this.headshipService.getEntity(emp.getHeadshipId());
			String headshipName = "";
			if(headship != null){
				headshipName = headship.getHeadshipName();
			}
			String ucDepartmentName = company.getCompanyName() + "-" + this.importService.fullDepartmentName("", emp.getDepartmentId());
			String uId = this.userCompanyService.addSelf(emp, company.getCompanyId(),ucDepartmentName,headshipName,"0");
	        if(StringUtils.isBlank(employeeId)){
	        	return "ERROR";
	        }else{
	        	SystemUser systemUser = new SystemUser();
	        	systemUser.setEmployeeId(employeeId);
	        	systemUser.setCompany(company);
	        	systemUser.setUserName(emp.getMobile());
	        	systemUser.setRealName(emp.getEmployeeName());
	        	systemUser.setCreateTime(new Date());
	        	systemUser.setDelFlag("0");
	        	String salt = UUIDUtil.generateUUID();
	        	systemUser.setPassSalt(salt);
	        	systemUser.setPassword(MD5Tools.encodePassword("111111", salt));
	        	this.userService.insertEntity(systemUser);
	        	
	        	//用户部门隶属关系添加
	        	UserDepartment udept = new UserDepartment();
	        	udept.setDepartmentId(emp.getDepartmentId());
	        	udept.setHeadshipId(emp.getHeadshipId());
	        	udept.setDepartmentPath(ucDepartmentName);
	        	udept.setHeadshipName(headshipName);
	        	udept.setVisibleFlag("1");
	        	udept.setTaxis(emp.getDisplayOrder());
	        	this.userDepartmentService.insertEntity(udept);
	        }
		}else{
			//修改人员表记录
			updateTypeFlag = "1";
			Employee employee = this.lookGroupDao.get(emp.getEmployeeId());
			if(!employee.getMobile().equals(emp.getMobile())){
				String checkEmployee2 = this.importService.checkEmployee(emp.getMobile());
				if(org.springframework.util.StringUtils.hasText(checkEmployee2)){
					return "SAME MOBILE"; //手机号码已存在
				}
			}
			if(!"".equals(pictureName)){
				employee.setPicture(pictureName);
			}
			employee.setEmployeeName(emp.getEmployeeName());
			employee.setDepartmentName(this.lookGroupDao.
					parentDepartmentName(emp.getEmployeeName(), emp.getDepartmentId()));
			employee.setDepartmentId(emp.getDepartmentId());
			employee.setHeadshipId(emp.getHeadshipId());
			employee.setMobile(emp.getMobile());
			employee.setMobileShort(emp.getMobileShort());
			employee.setTelShort(emp.getTelShort());
			employee.setTelephone2(emp.getTelephone2());
			employee.setEmail(emp.getEmail());
			employee.setDisplayOrder(emp.getDisplayOrder());
			employee.setGridNumber(emp.getGridNumber());
			this.lookGroupDao.update(employee);
			Headship headship = this.headshipService.getEntity(employee.getHeadshipId());
			String headshipName = "";
			if(headship != null){
				headshipName = headship.getHeadshipName();
			}
			this.userCompanyService.addSelf(employee, company.getCompanyId(), employee.getDepartmentName(),headshipName,"0");
			List<SystemUser> userList = this.userService.findByNamedParam("employeeId", emp.getEmployeeId());
			SystemUser systemUser = userList.get(0);
			systemUser.setRealName(emp.getEmployeeName());
			systemUser.setUserName(emp.getMobile());
			this.userService.updateEntity(systemUser);
			
			//更新用户部门信息
			List<UserDepartment> list = this.userDepartmentService.
					findByNamedParam(new String[]{"departmentId","userCompanyId"}, new String[]{emp.getDepartmentId(),userCompanyId});
			if(list.size()>0)
			{
				//更新岗位信息
				UserDepartment dept = list.get(0);
				dept.setHeadshipId(emp.getHeadshipId());
				this.userDepartmentService.updateEntity(dept);
			}
		}
		this.txlVersionService.saveAll(updateTypeFlag, employeeId);
		return "SUCCESS";
	}
	
	/**
	 * 删除用户.
	 * @param employeeId 
	 * @return 
	 * 返回类型：String
	 */
	public String deleteById(String employeeId){
		//修改人员表删除标识
		Employee employee = this.lookGroupDao.get(employeeId);
		employee.setDelFlag("1");
		this.lookGroupDao.update(employee);	
		//新增通讯录版本号
		TxluserVersion txluserVersion = new TxluserVersion();
		txluserVersion.setUpdateDate(new Timestamp(new Date().getTime()));
		int versionNum = this.txlVersionService.insertEntity(txluserVersion);
		//新增通讯录修改表记录
		TxluserModifyusers txluserModifyusers = new TxluserModifyusers();
		txluserModifyusers.setEmployee(employee);
		txluserModifyusers.setTxluserVersion(txluserVersion);
		txluserModifyusers.setUpdateType("2");
		TxluserModifyusersId txluserModifyusersId = new TxluserModifyusersId();
		txluserModifyusersId.setEmployeeId(employeeId);
		txluserModifyusersId.setTxluserVersionNum(versionNum);
		txluserModifyusers.setId(txluserModifyusersId);			
		this.txluserModifyService.insertEntity(txluserModifyusers);
		this.lookGroupDao.delUser(employeeId);
		return "SUCCESS";		
	}
	
	/**
	 * 查询用户信息.
	 * @param employeeId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getUserById(String employeeId){
		return this.lookGroupDao.getUserById(employeeId);	
	}
	
	/**
	 * 个人通讯录成员信息.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getUserCompanyById(String userCompanyId,String departmentId){
		return this.lookGroupDao.getUserCompanyById(userCompanyId,departmentId);
	}
	
	/**
	 * 查看通讯录,可查看指定部门联系人.
	 * @param rows 
	 * @param page 
	 * @param deptId 
	 * @param rights 
	 * @param departmentLevel 
	 * @param selfDepartmentId
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> getGroupInfo(int rows, int page, String deptId, String companyId,String key){
		return this.lookGroupDao.getGroupInfo(rows, page, deptId, companyId,key);
	}
	
	/**
     * 非企业通讯录.
     * @param rows
     * @param page
     * @param companyId
     * @return 
     * 返回类型：Pagination<?>
     */
    public Pagination<?> getOrgGroupInfo(int rows, int page, String companyId,String key){
    	return this.lookGroupDao.getOrgGroupInfo(rows, page, companyId,key);
    }
	
	/**
	 * 查询用户.
	 * @param rows 
	 * @param page 
	 * @param key 关键字
	 * @param rights 权限
	 * @param departmentLevel 登陆用户部门级别
	 * @param selfDepartmentId 登陆用户部门Id
	 * @param companyId 公司Id
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> searchList(int rows, int page, 
			String key,String rights, int departmentLevel, String selfDepartmentId, String companyId){
		Company company = this.companyService.getEntity(companyId);
		return this.lookGroupDao.searchList(rows, page, key,rights,
				departmentLevel,selfDepartmentId,company.getCompanyName(),companyId);
	}
	
	/**
	 * 找到指定部门下人员.
	 * @param departmentId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getEmployeeByDepartmentId(String departmentId){
		return this.lookGroupDao.getEmployeeByDepartmentId(departmentId);
	}
	
	
	/**
	 * 若勾选了给所有人发送信息,则发给选中的公司的所有人,employeeIds="-1";否则只发给指定人.
	 * @param employeeIds
	 * @param flag
	 * @param companyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getImei(String employeeIds,Boolean flag,String companyId){
		if(flag){
			employeeIds="-1";
		}
		return this.lookGroupDao.getImei(employeeIds,companyId);
	}
	
	/**
	 *根据手机号码返回员工信息 
	 * @param mobile
	 * @return
	 */
	public Map<String,Object> getEmployeeByMobile(String mobile)
	{
		return this.lookGroupDao.getEmployeeByMobile(mobile);
	}
	/**add by zhangjun 2013/11/15*/
	/**
	 *清空公司下的部门及员工
	 * @param companyId
	 * @return
	 */
	public void clearGroupByCompany(String companyId)
	{
		this.lookGroupDao.clearGroupByCompany(companyId);
		companyVersionService.addCompanyVersion(companyId, "3");
		
	}
	/**add by zhangjun 2013/11/15*/
	public String getUserDepartments(String userCompanyId)
	{
		return this.lookGroupDao.getUserDepartments(userCompanyId);
	}
	/**
	 * 获取相对顺序
	 * @param deptId
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getRelativeOrder(String deptId, String companyId){
		return this.lookGroupDao.getRelativeOrder(deptId,companyId);
	}
	/**
	 * 获取员工数量
	 * @param deptId
	 * @param companyId
	 * @return
	 */
	public Long getRelativeCount(String deptId, String companyId){
		return this.lookGroupDao.getRelativeCount(deptId,companyId);
	}
	/**
	 * 获取指定显示顺序的员工
	 * @param deptId
	 * @param companyId
	 * @param displayOrder
	 * @return
	 */
	public List<Map<String, Object>> getUserByOrder(String deptId, String companyId,String relativeOrder){
		return this.lookGroupDao.getUserByOrder(deptId,companyId,relativeOrder);
	}
	/**
	 * 
	 * @param deptId
	 * @param companyId
	 * @param displayOrder
	 */
	public void updateDisplayOrder(String deptId, String companyId,String displayOrder){
		List<Map<String,Object>> list = this.lookGroupDao.selectDisplayOrder(deptId, companyId, displayOrder);
		this.lookGroupDao.updateDisplayOrder(deptId,companyId,displayOrder);
		if(null != list){
			for (Map<String, Object> map : list) {
				this.groupVersionService.addGroupVersion(map.get("user_company_id").toString(), "1");
			}
		}
	}
	/**
	 * 获取员工所在部门的相对顺序
	 * @param userCompanyId
	 * @param departmentId
	 * @return
	 */
	public Long getUserRelativeInDept(String userCompanyId,String departmentId){
		return this.lookGroupDao.getUserRelativeInDept(userCompanyId,departmentId);
	}
}
