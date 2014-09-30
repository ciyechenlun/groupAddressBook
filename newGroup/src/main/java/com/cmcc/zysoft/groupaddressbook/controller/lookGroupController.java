// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.ImportService;
import com.cmcc.zysoft.groupaddressbook.service.LookGroupService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.RightconfigService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.util.StringUtil;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.starit.common.dao.support.Pagination;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：lookGroupController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-1 上午11:36:50
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("pc/lookGroup")
public class lookGroupController extends BaseController {
	
	@Resource
	private LookGroupService lookGroupService;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private RightconfigService rightconfigService;
	
	@Resource
	private CompanyService companyService;
	@Resource
	private UserCompanyService userCompanyService;
	@Resource
	private ImportService importService;
	@Resource
	private PCCompanyService pcCompanyService;
	
	@RequestMapping(value="/forward.htm")
	public String forward(ModelMap modelMap,String companyId,String key){
		modelMap.addAttribute("companyId", companyId);
		modelMap.addAttribute("key", key);
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.addAttribute("user", user);
		if("".equals(companyId) || null==companyId){
			companyId = SecurityContextUtil.getCompanyId();
		}
		Company company = this.companyService.getEntity(companyId);
		modelMap.addAttribute("company", company);
		String companyName = company.getCompanyName();
		String orgFlag = company.getOrgFlag();
		modelMap.addAttribute("org_flag", orgFlag);//将企业标志push至客户端，以控制权限
		//查看当前用户是否是企业级管理员
		List<Role> roles = user.getRoles();
		String manager = "";
		boolean isAdmin = false;
		boolean isCompanyAdmin = false;
		for(Role r : roles)
		{
			if("1".equals(r.getRoleId()) || "0".equals(r.getRoleId())
					|| "3".equals(r.getRoleId()) || "4".equals(r.getRoleId()))
			{
				isAdmin = true;
				break;
			}
			else if("6".equals(r.getRoleId()) || "5".equals(r.getRoleId()))
			{
				isCompanyAdmin = true;
			}
		}
		if(isCompanyAdmin && !isAdmin)
		{
			List<Map<String,Object>> manlist = this.pcCompanyService.getManageCompany(user.getUserId());
			for (Map<String, Object> map : manlist) {
				if(companyId.equals(getMapStr(map,"company_id"))){
					if("1".equals(getMapStr(map,"manage_flag"))){
						manager="1";
					}else if("3".equals(getMapStr(map,"manage_flag"))){
						manager="2";
					}
				}
			}
		}
		//如果角色里有roleId=1，则为系统管理员
		if(isAdmin)
		{
			manager = "1";
		}
		Pagination<?> seDepList =null;
		String employeeId = user.getEmployeeId();
		if(null != employeeId && !employeeId.equals("")){
			List<UserCompany> uerCompanyList = this.userCompanyService.findByNamedParam
					(new String[]{"companyId","employeeId","manageFlag","delFlag"}, 
					new Object[]{companyId,employeeId,"3","0"});
			if(null !=uerCompanyList && uerCompanyList.size()>0){
				modelMap.addAttribute("manageFlag", "3");
				//List<Map<String,Object>> manageDeptList = this.deptMagService.getManageDept(companyId,employeeId);
				//modelMap.addAttribute("manageDeptList", manageDeptList);
			}else{
				seDepList =  this.deptMagService.getDepartment(10, 1, companyId, "0", "1");
				
			}
		}else{
			seDepList =  this.deptMagService.getDepartment(10, 1, companyId, "0", "1");
		}
		
		
		modelMap.addAttribute("seDepList", seDepList);
		
		modelMap.addAttribute("manager", manager);
		return "web/groupBook";
	}
	@RequestMapping(value="/manageDeptList.htm")
	@ResponseBody
	public List<Map<String,Object>> getManageDeptList(HttpServletRequest request){
		User user = SecurityContextUtil.getCurrentUser();
		String companyId = "";
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		else
		{
			companyId = SecurityContextUtil.getCompanyId();
		}
		List<Map<String,Object>> manageDeptList = this.deptMagService.getManageDept(companyId,user.getEmployeeId());
		if(manageDeptList==null ||manageDeptList.isEmpty()){
			manageDeptList = (List<Map<String,Object>>)this.deptMagService.getSecondDepartment(companyId,user.getEmployeeId()).getResult();
		}
		return manageDeptList;
	}
	/**
	 * 查看通讯录,可查看指定部门联系人.
	 * @param modelMap 
	 * @param search 
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/searchEmployee.htm")
	public String searchEmployee(ModelMap modelMap,Search search,String companyId,String key){
		if(key ==null){
			key ="";
		}
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.addAttribute("user", user);
		if("".equals(companyId) || null==companyId){
			companyId = SecurityContextUtil.getCompanyId();
		}
		Company company = this.companyService.getEntity(companyId);
		modelMap.addAttribute("company", company);
		String companyName = company.getCompanyName();
		String orgFlag = company.getOrgFlag();
		modelMap.addAttribute("org_flag", orgFlag);//将企业标志push至客户端，以控制权限
		//查看当前用户是否是企业级管理员
		List<Role> roles = user.getRoles();
		String manager = "";
		for(Role role : roles)
		{
			if(role.getRoleId().equals("0"))
			{
				manager = "0";
				break;
			}
			if(role.getRoleId().equals("1"))
			{
				manager = "1";
				break;
			}else if(role.getRoleId().equals("2"))
			{
				manager = "2";
				break;
			}
			else if(role.getRoleId().equals("3"))
			{
				manager = "3";
				break;
			}
			else if(role.getRoleId().equals("4"))
			{
				manager = "4";
				break;
			}
			else if(role.getRoleId().equals("5"))
			{
				manager = "5";
				break;
			}
			else if(role.getRoleId().equals("6"))
			{
				manager = "6";
				break;
			}
		}
		String employeeId = user.getEmployeeId();
		if(null != employeeId && !employeeId.equals("")){
			List<UserCompany> uerCompanyList = this.userCompanyService.findByNamedParam
					(new String[]{"companyId","employeeId","manageFlag","delFlag"}, 
							new Object[]{companyId,employeeId,"3","0"});
			if(null !=uerCompanyList && uerCompanyList.size()>0){
				modelMap.addAttribute("manageFlag", "3");
			}
		}
		modelMap.addAttribute("manager", manager);
		if("1".equals(orgFlag)){
			//企业通讯录
			Pagination<?> pagination = this.lookGroupService.getGroupInfo(4, search.getPageNo(),
					null, companyId,key);
			List<Map<String,Object>> list = (List<Map<String,Object>>)pagination.getResult();
			for (Map<String, Object> map : list) {
				Long relativeOrder = this.lookGroupService.getUserRelativeInDept(map.get("user_company_id").toString(),map.get("department_id").toString());
				map.put("relative_order", relativeOrder);
				String deptName =companyName + "-" + this.importService.fullDepartmentName("", map.get("department_id").toString());
				map.put("department_name", deptName);
			}
			modelMap.addAttribute("pagination", pagination);
		}else{
			//Pagination<?> pagination = this.lookGroupService.getOrgGroupInfo(4, search.getPageNo(), companyId,key);
			//modelMap.addAttribute("pagination", pagination);
		}
		modelMap.addAttribute("key", key);
		return "web/searchResult";
	}
	
	/**
	 * 部门下拉框.
	 * @return 
	 * 返回类型：list<Map<String,Object>>
	 */
	@RequestMapping(value="/findDepartment.htm")
	@ResponseBody
	public List<Map<String,Object>> findDepartment(){
		return this.deptMagService.deptList();	
	}
	
	@RequestMapping(value="/findDepartmentList.htm")
	@ResponseBody
	public List<Map<String,Object>> findDepartmentList(HttpServletRequest request,String id)
	{
		String companyId = "";
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		else
		{
			companyId = SecurityContextUtil.getCompanyId();
		}
		return this.deptMagService.deptList(companyId);	
	}
	
	/**
	 * 岗位下拉框. 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping(value="/findHeadship.htm")
	@ResponseBody
	public List<Map<String,Object>> findHeadship(HttpServletRequest request){
		String companyId = "";
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			companyId = ((Company)request.getSession().getAttribute("selCompany")).getCompanyId();
		}
		else
		{
			companyId = SecurityContextUtil.getCompanyId();
		}
		return this.lookGroupService.findHeadship(companyId);
	}
	
	/**
	 * 新增、修改用户.
	 * @param emp 
	 * @param apicture 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/addContacterInfo.htm")
	@Transactional
	@ResponseBody	
	public String add(Employee emp,String userCompanyId,@RequestParam("apicture") MultipartFile apicture){
		return this.lookGroupService.add(emp,userCompanyId,apicture);		
	}
	
	/**
	 * 删除用户.
	 * @param employeeId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/deleteUserInfo.htm")
	@Transactional
	@ResponseBody
	public String deleteById(String employeeId){
		return this.lookGroupService.deleteById(employeeId);
	}
	
	/**
	 * 查询用户信息.
	 * @param employeeId 
	 * @return 
	 * 返回类型：list<Map<String,Object>>
	 */
	@RequestMapping(value="/getUserById.htm")
	@ResponseBody
	public List<Map<String,Object>> getUserById(String employeeId){
		return this.lookGroupService.getUserById(employeeId);
	}
	
	/**
	 * 个人通讯录成员信息.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping(value="/getUserCompanyById.htm")
	@ResponseBody
	public List<Map<String, Object>> getUserCompanyById(String userCompanyId,String current_departmentId){
		return this.lookGroupService.getUserCompanyById(userCompanyId,current_departmentId);
	}
	
	/**
	 * 查询.
	 * @param modelMap 
	 * @param search 
	 * @param key 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/search.htm")
	public String searchList(ModelMap modelMap,Search search,String key){
		//过滤特殊字符
		key = StringUtil.HTMLEncode(key);
		//获取设置的查看权限
		String rights = "";
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		String employeeId = user.getEmployeeId();
		int departmentLevel = 0;
		String selfDepartmentId = "";
		if(StringUtils.hasText(employeeId)){
			Employee employee = this.lookGroupService.getEntity(employeeId);
			Department department = this.deptMagService.getEntity(employee.getDepartmentId());
			departmentLevel = department.getDepartmentLevel();
			selfDepartmentId = department.getDepartmentId();
			rights = this.rightconfigService.rights();
		}
		Pagination<?> pagination = this.lookGroupService.searchList(search.getPageSize(), search.getPageNo(), 
				key, rights, departmentLevel, selfDepartmentId ,companyId);
		modelMap.addAttribute("pagination", pagination);
		modelMap.put("key", key);
		return "web/result";
	}
	/**
	 * 获取用户所在二级部门
	 * @param userCompanyId
	 * @return
	 */
	@RequestMapping(value="/getUserDepartments.htm")
	@ResponseBody
	public String getUserDepartments(String userCompanyId){
			return this.lookGroupService.getUserDepartments(userCompanyId);
	}

	/**
	 * 获取同部门下的用户数
	 * @param departmentId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value="/getRelativeCount.htm")
	@ResponseBody
	public Long getRelativeCount(String departmentId,String companyId){
		return this.lookGroupService.getRelativeCount(departmentId,companyId);
	}
	/**
	 * 根据相对顺序获得对应用户数据
	 * @param departmentId
	 * @param companyId
	 * @param relativeOrder
	 * @return
	 */
	@RequestMapping(value="/getUserByOrder.htm")
	@ResponseBody
	public Map<String, Object> getUserByOrder(String departmentId,String companyId,int relativeOrder){
		
		List<Map<String, Object>> list = this.lookGroupService.getUserByOrder(departmentId,companyId,String.valueOf(relativeOrder-1));
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据相对顺序获得对应用户数据
	 * @param departmentId
	 * @param companyId
	 * @param relativeOrder
	 * @return
	 */
	@RequestMapping(value="/updateDisplayOrder.htm")
	@ResponseBody
	public String updateDisplayOrder(String departmentId,String companyId,int displayOrder){
		
		this.lookGroupService.updateDisplayOrder(departmentId,companyId,String.valueOf(displayOrder));
		
		return "SUCCESS";
	}
	/**
	 * 查看通讯录,可查看指定部门联系人.
	 * @param modelMap 
	 * @param search 
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/loadEmployeeByDepartment.htm")
	@ResponseBody
	public Pagination<?> loadEmployeeByDepartment(String departmentId,String companyId,String key,int pageNo){
		if(key ==null){
			key ="";
		}
		if("".equals(companyId) || null==companyId){
			companyId = SecurityContextUtil.getCompanyId();
		}
		//企业通讯录
		Pagination<?> pagination = this.lookGroupService.getGroupInfo(10, pageNo,
				departmentId, companyId,key);
		return pagination;
	}
	/**
	 * 跳转到成员信息修改页面.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：页面
	 */
	@RequestMapping(value="/toEditEmployee.htm")
	public String toEditEmployee(ModelMap modelMap,String userCompanyId,String current_departmentId){
		List<Map<String, Object>> list = this.lookGroupService.getUserCompanyById(userCompanyId,current_departmentId);
		if(null != list && list.size()>0){
			modelMap.addAttribute("empInfo", list.get(0));
			if("3".equals(list.get(0).get("manage_flag"))){
				UserCompany uc = this.userCompanyService.getEntity(userCompanyId);
				List<Map<String, Object>> listMan = this.deptMagService.getDeptOfManage(uc.getCompanyId(), uc.getEmployeeId());
				String manage_dept_id="";
				if(listMan==null ||listMan.isEmpty()){
					listMan = (List<Map<String,Object>>)this.deptMagService.getSecondDepartment(uc.getCompanyId(),uc.getEmployeeId()).getResult();
				}
				for (Map<String, Object> map : listMan) {
					if(!manage_dept_id.equals("")){
			 			manage_dept_id+=",";
					}
			 		manage_dept_id +=map.get("id").toString();
				}
				modelMap.addAttribute("manage_dept_id", manage_dept_id);
			}
		}
		return "web/editEmployee";
	}
	/**
	 * 跳转到成员信息新增页面.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：页面
	 */
	@RequestMapping(value="/toAddEmployee.htm")
	public String toAddEmployee(ModelMap modelMap){
		return "web/addEmployee";
	}
	/**
	 * 跳转到成员信息批量新增页面.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：页面
	 */
	@RequestMapping(value="/toBatchAddEmp.htm")
	public String toBatchAddEmp(ModelMap modelMap){
		return "web/batchAddEmployee";
	}
	private String getMapStr(Map<String,Object> map,String str){
		if(null == map.get(str)){
			return "";
		}
		return map.get(str).toString();
	}
}