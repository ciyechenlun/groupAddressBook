package com.cmcc.zysoft.sysmanage.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserRole;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.sellmanager.util.UUIDUtil;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.dao.SysEmployeeDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * SystemUserService.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-4 下午2:40:02
 *
 */
@Service
public class SysEmployeeService extends BaseServiceImpl<Employee,String> {
	
	private static Logger _logger = LoggerFactory.getLogger(SysEmployeeService.class);
	
	@Resource
	private SysEmployeeDao sysEmployeeDao;
	
	@Resource
	private SystemUserPCService systemUserPCService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@Value("${upload.file.path}")
	private String path;

	@Override
	public HibernateBaseDao<Employee, String> getHibernateBaseDao() {
		return sysEmployeeDao;
	}
	
	/**
	 * 获取所有员工信息
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getAllEmployees(int page, int rows) {
		return this.sysEmployeeDao.getAllEmployees(page,rows);
	}
	
	/**
	 * 点击左边公司部门树获取员工信息
	 * @return
	 */
	public Pagination<Object> getEmployeesByTree(int page, int rows,String idIcon) {
		return this.sysEmployeeDao.getEmployeesByTree(page,rows,idIcon);
	}
	
	/**
	 * 根据查询条件，获取员工信息
	 * @param employeeName 员工名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getEmployeesByContion(String employeeName, int page, int rows) {
		return this.sysEmployeeDao.getEmployeesByContion(employeeName,page,rows);
	}
	
	/**
	 * 保存员工信息
	 * @param employee
	 * @param request
	 * @param picture
	 * @return
	 */
    public String saveEmployee(Employee employee,HttpServletRequest request,MultipartFile picture){
    	employee.setDelFlag("0");
    	if(picture.getSize() == 0){
    		employee.setPicture("");
		} else {
			String employeePicture = this.uploadPhoto(request, picture);
			if(employeePicture=="ERROR PHOTO"){
				_logger.debug("图片格式错误");
				return employeePicture;
			}else{
				employee.setPicture(employeePicture);
			}
		}
    	String mobile = employee.getMobile();
    	List<Employee> mobileList = this.findByNamedParam(new String[]{"mobile","delFlag"}, new Object[]{mobile,"0"});
    	if(null != mobileList && mobileList.size()>0){
    		_logger.debug("电话号码重复");
    		return "MOBILE";
    	}
    	String empId = this.sysEmployeeDao.save(employee);
    	if(StringUtils.hasText(empId)){
			_logger.debug("添加员工成功");
			return "SUCCESS";
		}else{
			_logger.debug("添加员工失败");
			return "ERROR";
		}
    }
	
    /**
     * 修改员工信息
     * @param employee
     * @param request
     * @param picture
     * @return
     */
	public String updateEmployee(Employee employee,HttpServletRequest request,MultipartFile picture){
		Employee new_employee = this.sysEmployeeDao.get(employee.getEmployeeId());
		if(picture.getSize() == 0){
			new_employee.setPicture(new_employee.getPicture());
		}else{
			String employeePicture = this.uploadPhoto(request, picture);
			if(employeePicture=="ERROR PHOTO"){
				_logger.debug("图片格式错误");
				return employeePicture;
			}else{
				new_employee.setPicture(employeePicture);
			}
		}
		new_employee.setEmployeeCode(employee.getEmployeeCode());
		new_employee.setEmployeeName(employee.getEmployeeName());
		new_employee.setDepartmentId(employee.getDepartmentId());
		new_employee.setIsDeptLeader(employee.getIsDeptLeader());
		if(!employee.getMobile().equals(new_employee.getMobile())){
			List<Employee> mobileList = this.findByNamedParam(new String[]{"mobile","delFlag"}, new Object[]{employee.getMobile(),"0"});
			if(mobileList.size()>0){
				_logger.debug("电话号码重复");
	    		return "MOBILE";
			}
		}
		new_employee.setMobile(employee.getMobile());
		new_employee.setBackupMobile(employee.getBackupMobile());
		new_employee.setJoinDate(employee.getJoinDate());
		new_employee.setLeaveDate(employee.getLeaveDate());
		new_employee.setStatus(employee.getStatus());
		this.sysEmployeeDao.update(new_employee);
		return "SUCCESS";
	}
	
    /**
     * 根据员工id获取员工信息
     * 
     * @param sysuserId 员工id
     * @return
     */
    public Employee getEmployee(String employeeId){
    	Employee employee = this.sysEmployeeDao.getEmployee(employeeId);
    	return employee;
    }
    
    /**
     * 根据员工id删除员工信息
     * 
     * @param sysuserId 员工id
     */
    public void deleteEmployee(String employeeId){
    	this.sysEmployeeDao.deleteEmployee(employeeId);
    }

    /**
	 * 根据部门id获取员工信息，带分页哦
	 * @param page
	 * @param rows
	 * @param deptId
	 * @return
	 */
	public Pagination<Object> employeesByDeptmentId(int page, int rows,String deptId) {
		return this.sysEmployeeDao.employeesByDeptmentId(page, rows, deptId);
	}

	/**
	 * 立即定位
	 * @param deptId
	 * @return
	 */
	public List<Map<String, Object>> empByDeptId(String deptId) {
		List<Map<String,Object>> emps = this.sysEmployeeDao.empByDeptId(deptId);
		for(Map<String,Object> map : emps){
			map.put("id", "e_"+map.get("id").toString());
			map.put("iconCls", "employee");
			map.put("state", "open");
			map.put("attributes", map.get("empId").toString());
		}
		return emps;
	}
	/**
	 * 立即定位
	 * @param deptId
	 * @return
	 */
	public List<Map<String, Object>> empByKeyword(String keyword) {
		List<Map<String,Object>> emps = this.sysEmployeeDao.empByKeyword(keyword);
		for(Map<String,Object> map : emps){
			map.put("id", "e_"+map.get("id").toString());
			map.put("iconCls", "employee");
			map.put("state", "open");
		}
		return emps;
	}

	/**
	 * 立即定位 
	 * @author yandou
	 * @param tel
	 * @return
	 */
	public List<Employee> employeesByTel(String tel) {
		return this.sysEmployeeDao.employeesByTel(tel);
	}
	
	/**
	 * 员工管理 左侧部门树  
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @return
	 */
	public List<Map<String, Object>> deptTree(){
		List<Map<String, Object>> list = this.sysEmployeeDao.deptTree("0");
		for(Map<String, Object> map:list){
			if(map.get("attributes").toString().equals("01")){
				map.put("iconCls", "sale");
			}else if(map.get("attributes").toString().equals("02")){
				map.put("iconCls", "rep");
			}
		}
		return childDept(list);
	}
	
	/**
	 * 递归获取子部门
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> childDept(List<Map<String, Object>> list){
		for(Map<String, Object> map : list){
			String haveChildDeparment = map.get("haveChildDeparment").toString();
			if("Y".equals(haveChildDeparment)){
				String parentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.sysEmployeeDao.deptTree(parentId);
				for(Map<String, Object> childmap:childList){
					if(childmap.get("attributes").toString().equals("01")){
						childmap.put("iconCls", "sale");
					}else if(childmap.get("attributes").toString().equals("02")){
						childmap.put("iconCls", "rep");
					}
				}
				childDept(childList);
				map.put("children", childList);
				map.put("state", "closed");
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 分页获取员工列表
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getList(int page,int rows,String deptId){
		return this.sysEmployeeDao.getList(page, rows,deptId);
	}
	
	/**
	 * 上传员工照片
	 * @author yandou
	 * @return
	 */
	public String uploadPhoto(HttpServletRequest request,MultipartFile photoFile) {
		//在这里就可以对file进行处理了，可以根据自己的需求把它存到数据库或者服务器的某个文件夹  
        String filename = photoFile.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase(); 
        try {
        	if(extName.trim().equals(".gif")||extName.trim().equals(".jpg")||extName.trim().equals(".jpeg")||extName.trim().equals(".png")||extName.trim().equals(".bmp")){
    	        String lastFileName = System.currentTimeMillis()+extName;
    	        //图片存储的相对路径   
    	        String fileFullPath = path+File.separator+"emp_"+lastFileName;
    			FileCopyUtils.copy(photoFile.getBytes(),new File(fileFullPath));
    			return "emp_"+lastFileName;
            }else{
            	return "ERROR PHOTO";
            }
		} catch (Exception e) {
			return "ERROR";
		}
	}
	
	/**
	 * 获取是否领导状态
	 * @return
	 */
	public List<Map<String, Object>> isDeptLeader(){
		return this.sysEmployeeDao.isDeptLeader();
	}
	
	/**
	 * 获取登陆人所在公司的角色列表
	 * @return
	 */
	public List<Map<String, Object>> roleList(){
		return this.sysEmployeeDao.roleList();
	}
	
	/**
	 * 在员工管理页面将员工启用成系统用户
	 * @param systemUser
	 * @param roleId
	 * @return
	 */
	public String addUser(SystemUser systemUser,String roleId){
		Company comp = new Company();
		UserRole ur = new UserRole();
		Role role = new Role();
		if(StringUtils.hasText(systemUser.getUserId())){
			systemUser.setDelFlag("0");
			systemUser.setCreateTime(new Date());
			//设置密码
			String passSalt = UUIDUtil.generateUUID();
			systemUser.setPassSalt(passSalt);
			systemUser.setPassword(MD5Tools.encodePassword(systemUser.getPassword(),passSalt));
			//设置公司
			String companyId = SecurityContextUtil.getCompanyId();
			comp.setCompanyId(companyId);
			systemUser.setCompany(comp);
			//设置角色
			role.setRoleId(roleId);
			ur.setSystemUser(systemUser);
			ur.setRole(role);
			//保存角色
			this.userRoleService.insertEntity(ur);
			//修改用户信息
			this.systemUserPCService.updateEntity(systemUser);
			return "SUCCESS";
		}else{
			List<SystemUser> list = this.systemUserPCService.findByNamedParam("userName", systemUser.getUserName());
			if(list.size()>0){
				return "NAME";
			}
			role.setRoleId(roleId);
			String companyId = SecurityContextUtil.getCompanyId();
			comp.setCompanyId(companyId);
			systemUser.setCompany(comp);
			String passSalt = UUIDUtil.generateUUID();
			systemUser.setPassSalt(passSalt);
			systemUser.setPassword(MD5Tools.encodePassword(systemUser.getPassword(),passSalt));
			systemUser.setDelFlag("0");
			systemUser.setCreateTime(new Date());
			String userId = this.systemUserPCService.insertEntity(systemUser);
			ur.setSystemUser(systemUser);
			ur.setRole(role);
			this.userRoleService.insertEntity(ur);
			if(StringUtils.hasText(userId)){
				return "SUCCESS";
			}else{
				return "FAIL";
			}
		}
	}
	
	/**
	 * 禁用账号：删除用户表和用户角色表里的记录
	 * @param employeeId
	 * @return
	 */
	public String deleteUser(String employeeId){
		List<SystemUser> list = this.systemUserPCService.findByNamedParam("employeeId", employeeId);
		if(null != list){
			String userId = list.get(0).getUserId();
			List<UserRole> userRole = this.userRoleService.findByNamedParam("systemUser.userId", userId);
			if(null != userRole){
				String userRoleId = userRole.get(0).getId();
				this.userRoleService.deleteEntity(userRoleId);
			}else{
				return "ERROR1";
			}
			SystemUser systemUser = list.get(0);
			systemUser.setDelFlag("1");
			systemUser.setModifyTime(new Date());
			this.systemUserPCService.updateEntity(systemUser);
			return "SUCCESS";
		}else{
			return "ERROR2";
		}
	}

	/**
	 * @author yandou
	 * @param page
	 * @param rows
	 * @param idIcon
	 * @param keyword
	 * @return
	 */
	public Map<String,Object> emps(int page, int rows, String idIcon,String keyword) {
		return this.sysEmployeeDao.emps(page, rows, idIcon, keyword);
	}
	
	/**
	 * 获取所有成员列表---用于同步成员信息.
	 * add by yuan.fengjian@ustcinfo.com
	 * @return list
	 */
	public List<Employee> getAllEmployee() {
		return this.sysEmployeeDao.getAllEmployee();
	}
	
	/**
	 * 返回所有用户列表，供与云平台同步使用
	 * @return
	 */
	public List<Employee> getAllEmployeeWithIMSI(String where)
	{
		return this.sysEmployeeDao.getAllEmployeeWithIMSI(where);
	}
	
	/**
	 * 返回所有用户列表，供与云平台同步使用
	 * @return
	 */
	public List<Map<String,Object>> getAllEmployeeWithIMSISQL(String where)
	{
		return this.sysEmployeeDao.getAllEmployeeWithIMSISQL(where);
	}
}