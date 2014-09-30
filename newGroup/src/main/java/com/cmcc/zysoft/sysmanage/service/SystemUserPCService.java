// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserRole;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.sellmanager.util.UUIDUtil;
import com.cmcc.zysoft.sysmanage.dao.SystemUserPCDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author yandou
 */
@Service
public class SystemUserPCService extends BaseServiceImpl<SystemUser, String> {

	/**
	 * 属性名称：systemUserPCDao 类型：SystemUserPCDao
	 */
	@Resource
	private SystemUserPCDao systemUserPCDao;
	
	/**
	 * 属性名称：companyService 类型：CompanyService
	 */
	@Resource
	private CompanyService companyService;//公司管理
	
	/**
	 * 属性名称：departmentService 类型：DepartmentService
	 */
	@Resource
	private DepartmentService departmentService;//部门管理
	
	/**
	 * 属性名称：roleService 类型：RoleService
	 */
	@Resource
	private RoleService roleService;//角色
	
	/**
	 * 属性名称：userRoleService 类型：UserRoleService
	 */
	@Resource
	private UserRoleService userRoleService;//用户角色
	
	/**
	 * 属性名称：sysEmployeeService 类型：SysEmployeeService
	 */
	@Resource
	private SysEmployeeService sysEmployeeService;//雇员信息
	
	@Override
	public HibernateBaseDao<SystemUser, String> getHibernateBaseDao() {
		return systemUserPCDao;
	}

	/**
	 * 修改用户信息
	 * @param systemUser 返回类型：void
	 */
	public void updateSystemUser(SystemUser systemUser){
		systemUserPCDao.updateSystemUser(systemUser);
	}
	
	/**
	 * 系统用户列表分页
	 * @param page
	 * @param rows
	 * @param idIcon 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Pagination<Object> systemUsers(int page, int rows, String idIcon, String nowUserDept) {
		Pagination<Object> pagination = this.systemUserPCDao.systemUsers(page,rows,idIcon,nowUserDept);
		List<Object> list = pagination.getResult();
		for(Object obj : list){
			Map<String,Object> map = (Map<String, Object>) obj;
			String employeeId = map.get("employeeId")==null?"":map.get("employeeId").toString();
			if(StringUtils.hasText(employeeId)){
				Employee employee = this.sysEmployeeService.getEntity(employeeId.toString());
				map.put("employeeCode", employee.getEmployeeCode()==null?"":employee.getEmployeeCode());
				map.put("realName", employee.getEmployeeName());
				map.put("mobile", employee.getMobile());
			}
		}
		return pagination;
	}

	/**
	 * 保存用户信息 
	 * @param systemUser
	 * @param roleId 
	 * @param roleId 
	 */
	public void saveSystemUser(SystemUser systemUser, String userRoleId, String roleId) {
		String passSalt = UUIDUtil.generateUUID();
		systemUser.setDelFlag("0");
		systemUser.setPassSalt(passSalt);
		systemUser.setPassword(MD5Tools.encodePassword(systemUser.getPassword(),passSalt));
		Role role = this.roleService.getEntity(roleId);
		if(StringUtils.hasText(userRoleId)){
			this.updateEntity(systemUser);
			userRoleService.updateUserRole(userRoleId,role);
		}else{
			this.systemUserPCDao.save(systemUser);
			//用户角色处理
			userRoleService.save(role, systemUser);
		}
	}
	
	/**
	 * 保存系统用户
	 * 
	 * @param systemUser
	 * @return 返回类型：String
	 */
	public String saveSystemUser(SystemUser systemUser){
		return systemUserPCDao.saveSystemUser(systemUser);
	}

	/**
	 * 根据userId删除系统用户信息
	 * @param userRoleId
	 */
	public void deleteSystemUserByUserId(String userRoleId) {
		UserRole userRole = this.userRoleService.getEntity(userRoleId);
		this.userRoleService.deleteEntity(userRole.getId());
		SystemUser systemUser = userRole.getSystemUser();
		systemUser.setDelFlag("1");
		systemUser.setModifyTime(new Date());
		this.updateEntity(systemUser);
	}

	/**
	 * 公司部门树
	 * @return
	 */
	public List<Map<String, Object>> companyDeptTree() {
		List<Map<String,Object>> companyTree = this.companyService.companyTree("0");
		return companyDeptChildrenTree(companyTree);
	}
	
	/**
	 * 获取公司部门树辅助方法
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> companyDeptChildrenTree(List<Map<String,Object>> list){
		for(Map<String,Object> map : list){
			//添加公司下面的部门
			String companyId = map.get("id").toString();
			List<Map<String,Object>> deptList = this.departmentService.deptTreeByCompanyId(companyId);//根据公司id获取下面的部门
			Object obj = map.get("children");//获取子公司
			if(null != obj){
				List<Map<String,Object>> companyList = (List<Map<String, Object>>)obj;
				companyDeptChildrenTree(companyList);
				if(!deptList.isEmpty()){
					if(companyList.addAll(deptList)){
						map.put("children", companyList);
					}
				}
			}else{
				if(!deptList.isEmpty()){
					map.put("children", deptList);
					map.put("state", "closed");
				}
			}
		}
		return list;
	}

	public String uploadPhoto(HttpServletRequest request,MultipartFile photoFile) {
		//在这里就可以对file进行处理了，可以根据自己的需求把它存到数据库或者服务器的某个文件夹  
        String filename = photoFile.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase(); 
        try {
        	if(extName.trim().equals(".gif")||extName.trim().equals(".jpg")||extName.trim().equals(".jpeg")||extName.trim().equals(".png")||extName.trim().equals(".bmp")){
    	        String lastFileName = System.currentTimeMillis()+extName;
    	        String path = "/resources/image/upload/systemUserPhoto/";
    	        //图片存储的相对路径   
    	        String fileFullPath = request.getServletContext().getRealPath("")+path+lastFileName;
    			FileCopyUtils.copy(photoFile.getBytes(),new File(fileFullPath));  
    			return "/"+path.substring(1)+lastFileName;
            }else{
            	return "ERROR";
            }
		} catch (Exception e) {
			return "ERROR";
		}
	}

	/**
	 * 根据员工Id获取员工对象
	 * @param employeeId
	 * @return
	 */
	public Employee getEmpByEmpId(String employeeId) {
		if(StringUtils.hasText(employeeId)){
			return this.sysEmployeeService.getEntity(employeeId);
		}else{
			return null;
		}
	}
	
	/**
	 * 做为一名产品运营人员,可通过查询某个时间段内销售管家用户账号开通及停用信息,以便了解产品的发展情况.
	 * @param page
	 * @param rows
	 * @param createStartDate 账号开通   查询开始时间
	 * @param createEndDate 账号开通  查询结束时间
	 * @param stopStartDate 账号停用  查询开始时间
	 * @param stopEndDate  账号停用  查询结束时间
	 * @return
	 */
	public Map<String, Object> checkUser(int page , int rows , String createStartDate , 
			String createEndDate , String stopStartDate , String stopEndDate){
		return this.systemUserPCDao.checkUser(page, rows, createStartDate, createEndDate, stopStartDate, stopEndDate);
	}
	/**
	 * 用于密码输入超过最大值时，锁定该用户
	 * @param userName
	 */
	public void lockUser(String userName){
		this.systemUserPCDao.lockUser(userName);
	}
}
