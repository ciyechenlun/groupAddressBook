package com.cmcc.zysoft.sysmanage.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.service.ManageCityService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.ManageCity;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.DepartmentService;
import com.cmcc.zysoft.sysmanage.service.SysEmployeeService;
import com.cmcc.zysoft.sysmanage.service.SystemUserPCService;
import com.starit.common.dao.support.Pagination;

/**
 * EmployeeController.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-4 下午2:42:02
 *
 */
@Controller
@RequestMapping("/pc/employee")
public class SysEmployeeController extends BaseController {
	
	private static Logger _logger = LoggerFactory.getLogger(SysEmployeeController.class);
	
	@Resource
	private SysEmployeeService SysEmployeeService;
	
	@Resource
	private DepartmentService departmentService;
	
	@Resource
	private SystemUserPCService systemUserPCService;
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private ManageCityService manageCityService;

	/**
	 * 弹出框人员列表 
	 * @author yandou
	 * @param page
	 * @param rows
	 * @param idIcon
	 * @param keyword
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Map<String,Object> emps(int page,int rows,String idIcon,String keyword){
		return this.SysEmployeeService.emps(page, rows, idIcon, keyword);
	}
	
	/**
	 * 系统用户管理  选择员工
	 * 根据部门id获取员工信息，带分页哦
	 * @param page
	 * @param rows
	 * @param deptId 部门id
	 * @return
	 */
	@RequestMapping(value="/employees/{deptId}.htm")
	@ResponseBody
	public Pagination<Object> employeesByDeptmentId(int page,int rows,@PathVariable String deptId) {
		_logger.debug("根据部门id获取员工信息");
		return this.SysEmployeeService.employeesByDeptmentId(page,rows,deptId);
	}
	
	/**
	 * 跳转至员工信息首页
	 * 
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String employee(){
		_logger.debug("跳转至员工信息首页");
		return "sysmanage/employee/employee";
	}
	
	/**
	 * 获取所有员工信息
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/all.htm")
	@ResponseBody
	public Pagination<Object> getAllEmployees(int page,int rows) {
		_logger.debug("获取所有员工信息");
		return this.SysEmployeeService.getAllEmployees(page,rows);
	}
	
	/**
	 * 点击左边公司部门树获取员工信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/allTree.htm", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<Object> getEmployeesByTree(int page,int rows,String idIcon) {
		_logger.debug("点击左边公司部门树获取员工信息");
		return this.SysEmployeeService.getEmployeesByTree(page,rows,idIcon);
	}
	
	/**
	 * 跳转至添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add.htm")
	public String add(Model model,ModelMap modelMap){
		_logger.debug("跳转至添加页面");
		return "sysmanage/employee/addEmployee";
	}

	/**
	 * 添加员工信息
	 * 
	 * @param sysuser 员工对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/save.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveEmployee(Employee employee, HttpServletRequest request,@RequestParam("photo") MultipartFile photo) {
		return this.SysEmployeeService.saveEmployee(employee, request, photo);
	}

	/**
	 * 跳转至修改员工信息页面
	 * 
	 * @param userId 角色id
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/edit.htm")
	public String update(String employeeId, Model model,ModelMap modelMap) throws ParseException {
		_logger.debug("跳转至修改员工信息页面");
		Employee employee = new Employee();
		employee = this.SysEmployeeService.getEmployee(employeeId);
		model.addAttribute("employee", employee);
		return "sysmanage/employee/editEmployee";
	}
	
	
	/**
	 * 修改员工信息
	 * 
	 * @param sysuser 员工对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editEmployee.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String updateEmployee(Employee employee,HttpServletRequest request,@RequestParam("photo") MultipartFile photo) {
		_logger.debug("修改员工信息");
		return this.SysEmployeeService.updateEmployee(employee, request, photo);
	}
	
	/**
	 * 跳转至查看员工信息页面
	 * 
	 * @param employeeId 员工id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/look.htm")
	public String look(@RequestParam("employeeId") String employeeId, Model model) {
		_logger.debug("跳转至查看员工信息页面");
		Employee employee = new Employee();
		employee = this.SysEmployeeService.getEmployee(employeeId);
		//获取部门信息
		Department dept = new Department();
		dept = this.departmentService.updateInfo(employee.getDepartmentId());
		//获取岗位信息
		//获取公司信息
		model.addAttribute("dept",dept);
		model.addAttribute("employee", employee);
		return "sysmanage/employee/detailEmployee";
	}
	
	/**
	 * 删除员工信息
	 * 
	 * @param employees 员工信息id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/del.htm", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String deleteEmployee(@RequestParam("employees") String employees ,HttpServletRequest request) {
		_logger.debug("删除员工信息");
		try {
			  String[] employeeIds = employees.split(",");
				for(int j=0;j<employeeIds.length;j++){
					this.SysEmployeeService.deleteEmployee(employeeIds[j]);
				}
		} catch (Exception e) {
			_logger.error("删除员工信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * 根据查询条件，获取员工信息列表
	 * 
	 * @param employeeName 员工姓名
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/allByCondition.htm")
	@ResponseBody
	public Pagination<Object> getEmployeesByContion(@RequestParam("employeeName") String employeeName, int page,int rows) {
		_logger.debug("根据查询条件获取员工信息列表");
		return this.SysEmployeeService.getEmployeesByContion(employeeName,page,rows);
	}
	
	/**
	 * 获取左侧部门树
	 * @return
	 */
	@RequestMapping(value="/deptTree.htm")
	@ResponseBody
	public List<Map<String, Object>> deptTree(){
		return this.departmentService.companyDeptTree();
	}
	
	/**
	 * 获取员工列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/getList.htm")
	@ResponseBody
	public Map<String, Object> getList(int page,int rows,@RequestParam("deptId") String deptId){
		User user = SecurityContextUtil.getCurrentUser();
		String roleCode = user.getRoles().get(0).getRoleCode();//获取当前登录人的角色
		String[] typeAndId = new String[]{};
		if(roleCode.equals("4")){
			if(StringUtils.hasText(deptId)){
				typeAndId = deptId.split("_");
				if(typeAndId[0].equals("company")){
					deptId = "";
				}else{
					deptId = this.departmentService.allChildrenDept(typeAndId[1]);
				}
			}else{
				deptId = "";
			}
			return this.SysEmployeeService.getList(page, rows, deptId);
		}else if(roleCode.equals("6")){
			return this.SysEmployeeService.getList(page, rows, "");
		}else{
			Employee employee = SysEmployeeService.getEmployee(user.getEmployeeId());
			if(StringUtils.hasText(deptId)){
				typeAndId = deptId.split("_");
				if(typeAndId[0].equals("company")){
					deptId = this.departmentService.allChildrenDept(employee.getDepartmentId());
				}else{
					deptId = this.departmentService.allChildrenDept(typeAndId[1]);
				}
			}else{
				deptId = this.departmentService.allChildrenDept(employee.getDepartmentId());
			}
			return this.SysEmployeeService.getList(page, rows, deptId);
		}
	}
	
	/**
	 * 获取是否领导状态
	 * @return
	 */
	@RequestMapping(value="/isDeptLeader.htm")
	@ResponseBody
	public List<Map<String, Object>> isDeptLeader(){
		return this.SysEmployeeService.isDeptLeader();
	}
	
	/**
	 * 获取登陆人所在公司的角色列表
	 * @return
	 */
	@RequestMapping(value="/roleList.htm")
	@ResponseBody
	public List<Map<String, Object>> roleList(){
		return this.SysEmployeeService.roleList();
	}
	
	/**
	 * 跳转到启用账号页面
	 * @param employeeId
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="/addUser.htm")
	public String addUser(String employeeId,ModelMap modelMap){
		Employee employee = this.SysEmployeeService.getEmployee(employeeId);
		List<SystemUser> list = this.systemUserPCService.findByNamedParam("employeeId", employeeId);
		String userId = "";
		if(list.size()>0){
			userId = list.get(0).getUserId();
		}
		modelMap.addAttribute("employeeId", employeeId);
		modelMap.addAttribute("mobile", employee.getMobile());
		modelMap.addAttribute("userId", userId);
		return "sysmanage/employee/addUser";
	}
	
	/**
	 * 禁用账号：删除用户表和用户角色表里的记录
	 * @param employeeId
	 * @return
	 */
	@RequestMapping(value="/deleteUser.htm")
	@Transactional
	@ResponseBody
	public String deleteUser(String employeeId){
		return this.SysEmployeeService.deleteUser(employeeId);
	}
	
	/**
	 * 在员工管理页面将员工启用成系统用户
	 * @param systemUser
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/saveUser.htm")
	@Transactional
	@ResponseBody
	public String saveUser(SystemUser systemUser,String roleId){
		return this.SysEmployeeService.addUser(systemUser, roleId);
	}
	
	/**
	 * 添加管理员界面
	 * @return
	 */
	@RequestMapping(value="/addManage.htm")
	public String addManage()
	{
		return "sysmanage/employee/addManage";
	}
	
	/**
	 * 添加管理员保存
	 * @return
	 */
	@RequestMapping(value="/addManageSave.htm")
	@Transactional
	@ResponseBody
	public Map<String,Object> addManageSave(String loginName,String roleId,String mobile,String city)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		List<SystemUser> list = this.systemUserPCService.findByNamedParam(new String[]{"userName","delFlag"}, new String[]{loginName,"0"});
		if(list.size()==0)
		{
		
			//添加员工
			Employee emp = new Employee();
			emp.setEmployeeName(loginName);
			emp.setDelFlag("0");
			if(roleId.equals("4"))
			{
				//地市管理员
				emp.setcompanyCity(city);
			}
			String employeeId = this.SysEmployeeService.insertEntity(emp);
			
			//添加系统用户
			Company company = this.companyService.getEntity("001");
			SystemUser sysUser = new SystemUser();
			sysUser.setCompany(company);
			sysUser.setUserName(loginName);
			sysUser.setMobile(mobile);
			sysUser.setDelFlag("0");
			sysUser.setEmployeeId(employeeId);
			String userId = this.systemUserPCService.saveSystemUser(sysUser);
			
			//如果是ict管理员，添加分管区域
			if(roleId.equals("3"))
			{
				String[] citys = city.split("[,]");
				for(String c : citys)
				{
					ManageCity mc = new ManageCity();
					mc.setManageCityCode(c);
					mc.setUserId(userId);
					this.manageCityService.insertEntity(mc);
				}
			}
			map.put("code", "0");
			map.put("msg","添加成功");
		}
		else
		{
			map.put("code", "-1");
			map.put("msg", "账户已存在");
		}
		return null;
	}
	
}
