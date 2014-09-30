// ~ CopyRight © 2013 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.ImportService;
import com.cmcc.zysoft.groupaddressbook.service.RecycleService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱：zhang.jun3@ustcinfo.com
 * <br />描述：RecycleController
 * <br />版本:1.0.0
 * <br />日期： 2013-11-21 上午10:25:32
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/recycle")
public class RecycleController extends BaseController{
	
	@Resource
	private RecycleService recycleService;
	@Resource
	private DeptMagService deptMagService;
	@Resource
	private CompanyService companyService;
	@Resource
	private ImportService importService;
	@RequestMapping(value="/forward.htm")
	public String forward(ModelMap modelMap,String companyId){
		modelMap.addAttribute("companyId", companyId);
		return "web/recycle";
	}
	/**
	 * 挑战到回收站页面
	 * @param modelMap
	 * @param search
	 * @param departmentId
	 * @param companyId
	 * @param treeId
	 * @return
	 */
	@RequestMapping(value="/getDelElement.htm")
	public String getDelElement(ModelMap modelMap,Search search,HttpServletRequest request,String departmentId,String companyId,String treeId){
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.addAttribute("user", user);
		Company company = null;
		if("".equals(companyId) || null == companyId){
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				company = (Company)request.getSession().getAttribute("selCompany");
				companyId = company.getCompanyId();
			}
			else{
				companyId = SecurityContextUtil.getCompanyId();
				company = this.companyService.getEntity(companyId);
			}
		}
		company = this.companyService.getEntity(companyId);
		modelMap.addAttribute("company", company);
		String companyName = company.getCompanyName();
		String orgFlag = company.getOrgFlag();
		modelMap.addAttribute("org_flag", orgFlag);//将企业标志push至客户端，以控制权限
		//查看当前用户是否是企业级管理员
		List<Role> roles = user.getRoles();
		String manager = "0";
		for(Role role : roles)
		{
			if(role.getRoleId().equals("1"))
			{
				manager = "1";
			}
		}
		
		modelMap.addAttribute("manager", manager);
		if("1".equals(orgFlag)){
			//企业通讯录
			Pagination<?> pagination = this.recycleService.getGroupInfo(8, search.getPageNo(),
					departmentId, companyId);
			List<Map<String, Object>> resultList = (List<Map<String, Object>>)pagination.getResult();
			for (Map<String, Object> map : resultList) {
				String deptName = "";
				if("".equals(map.get("parent_department_id").toString())){
					deptName =companyName + "-" + this.importService.fullDepartmentName("", map.get("department_id").toString());
				}else if("0".equals(map.get("parent_department_id").toString())){
					deptName =companyName;
				}else{
					deptName =companyName + "-" + this.importService.fullDepartmentName("", map.get("parent_department_id").toString());
				}
				
				map.put("department_name", deptName);
			}
			modelMap.addAttribute("pagination", pagination);
			modelMap.put("departmentId", departmentId);
			if(StringUtils.hasText(departmentId) && !"0".equals(departmentId)){
				Department department = this.deptMagService.getEntity(departmentId);
				modelMap.put("departmentName", department.getDepartmentName());
				if("0".equals(department.getParentDepartmentId())){
					modelMap.put("parentDepartmentName", companyName);
				}else{
					Department parentDept = this.deptMagService.getEntity(department.getParentDepartmentId());
					modelMap.put("parentDepartmentName", parentDept.getDepartmentName());
				}
				modelMap.put("parentDepartmentId", department.getParentDepartmentId());
			}else{
				modelMap.put("departmentName", companyName);
				modelMap.put("parentDepartmentName", "");
			}
		}else{
			Pagination<?> pagination = this.recycleService.getOrgGroupInfo(8, search.getPageNo(), companyId);
			modelMap.addAttribute("pagination", pagination);
			if(company.getVitrueFlag().equals("1")){
				modelMap.put("departmentName", "");
			}else{
				modelMap.put("departmentName", companyName);
			}
			modelMap.put("parentDepartmentName", "个人通讯录");
		}
		modelMap.addAttribute("treeId", treeId);
		return "web/recycleMain";
	}
	/**
     * 部门是否被删除
     * @param deptId
     * @return
     */
	@RequestMapping(value="/isDepartmentDelete.htm")
	@ResponseBody
    public boolean isDepartmentDelete(String departmentId){
    	return this.recycleService.isDepartmentDelete(departmentId);
    }
    /**
     * 恢复部门
     * @param deptId
     * @return
     */
	@RequestMapping(value="/recycleDepartment.htm")
	@ResponseBody
    public boolean recycleDepartment(String departmentId){
		boolean success=false;
		try{
			this.recycleService.recycleDepartment(departmentId);
			success = true;
		}catch(Exception e){
			e.printStackTrace();
		}
    	 return success;
    }
	
	/**
	 * 恢复用户
	 * @param employeeId
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value="/recycleEmployee.htm")
	@ResponseBody
    public String recycleEmployee(String departmentId,String userCompanyId){
		String success = "";
		if(this.recycleService.checkInfo(userCompanyId,departmentId)!=0){
			success = "exist";
		}else{
			try{
					this.recycleService.recycleEmployee(departmentId,userCompanyId);
					success = "success";
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		
    	 return success;
    }
    /**
     * 彻底删除部门
     * @param deptId
     * @return
     */
	@RequestMapping(value="/delRecycleDepartment.htm")
	@ResponseBody
    public boolean delRecycleDepartment(String departmentId){
		boolean success=false;
		try{
			this.recycleService.delRecycleDepartment(departmentId);
			success = true;
		}catch(Exception e){
			e.printStackTrace();
		}
    	 return success;
    }
	/**
     * 彻底删除用户
     * @param deptId
     * @return
     */
	@RequestMapping(value="/delRecycleEmployee.htm")
	@ResponseBody
    public boolean delRecycleEmployee(String departmentId,String userCompanyId){
		boolean success=false;
		try{
			this.recycleService.delRecycleEmployee(userCompanyId,departmentId);
			success = true;
		}catch(Exception e){
			e.printStackTrace();
		}
    	 return success;
    }
}
