// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.DepartmentService;

/**
 * DepartmentController.java
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-29 下午8:16:39
 */
@Controller
@RequestMapping("/pc/department")
public class DepartmentController extends BaseController {
	
	private static Logger _logger = LoggerFactory.getLogger(DepartmentController.class); 
	
	@Resource
	private DepartmentService departmentService;
	@Resource
	private CompanyService companyService;
	
	/**
	 * 跳转到组织机构管理页面
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String deparment(){
		_logger.debug("跳转到组织机构管理页面");
		return "sysmanage/department/department";
	}
	
	/**
	 * 根据公司id获取对应的部门，树
	 * @author yandou
	 * @param companyId
	 * @return
	 */
	@RequestMapping("/departmentTree/{companyId}.htm")
	@ResponseBody
	public List<Map<String, Object>> departmentTree(@PathVariable String companyId,HttpServletRequest request){
		_logger.debug("根据公司id获取对应的部门树");
		return this.departmentService.deptTreeByCompanyId(companyId);
	}
	
	/**
	 * 跳转到新增组织机构页面
	 * @return
	 */
	@RequestMapping(value="/addPage.htm")
	public String addPage(){
		_logger.debug("跳转到新增组织机构页面");
		return "sysmanage/department/add";
	}
	
	/**
	 * 跳转到修改组织机构页面
	 * @param deptId
	 * @return
	 */
	@RequestMapping(value="/updatePage.htm")
	public String updatePage(String deptId,Model model){
		Department dept = this.departmentService.updateInfo(deptId);
		model.addAttribute("dept",dept);
		_logger.debug("跳转到修改组织机构页面");
		return "sysmanage/department/update";
	}
	
	/**
	 * 跳转到查看组织机构页面
	 * @param deptId
	 * @return
	 */
	@RequestMapping(value="/detailPage.htm")
	public String detailPage(String deptId,Model model){
		Department dept = this.departmentService.updateInfo(deptId);
		model.addAttribute("dept",dept);
		_logger.debug("跳转到查看组织机构详情页面");
		return "sysmanage/department/detail";
	}
	
	/**
	 * 增加或修改部门信息中,部门树
	 * @return
	 */
	@RequestMapping(value="/getDept.htm")
	@ResponseBody
	public List<Map<String, Object>> getDept(){
		String companyId = SecurityContextUtil.getCompanyId();
		List<Map<String, Object>> list = this.departmentService.deptTree(companyId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "0");
		map.put("text", "无");
		list.add(map);
		_logger.debug("增加或修改部门信息中,获取部门树");
		return list;
	}
	
	
	/**
	 * 根据特定组织机构的ID获取其子级组织结构树(异步加载)
	 * @param id 这个名称是前台easyui 的treegrid传过来的，如果前台默认参数，那么这个地方必须用id作为参数名
	 * @return
	 */
	@RequestMapping(value="/tree.htm",method=RequestMethod.POST)
	@ResponseBody
	public List<?> departmentTree(String id){
		List<?> list = null;
		list = departmentService.departmentTree(id);
		return list;
	}
	
	/**
	 * 获取部门树   部门管理    列表展示(同步加载)
	 * @return
	 */
	@RequestMapping(value="/deptTree.htm",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> tree(){
		return this.departmentService.tree();
	}
	
	/**
	 * 根据部门ID获取其所属公司ID
	 * @param departmentId 
	 * @return companyId
	 */
	@RequestMapping(value="/findCompanyByDeptId.htm",method=RequestMethod.POST)
	@ResponseBody
	public String findCompanyByDeptId(String departmentId){
		Department dept = this.departmentService.updateInfo(departmentId);
		_logger.debug("根据部门ID获取其所属公司ID");
		return dept.getCompany().getCompanyId();
	}
	
	/**
	 * 根据部门ID获取其部门区域
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value="/findDepartmentAreaByDeptId.htm",method=RequestMethod.POST)
	@ResponseBody
	public String findDepartmentAreaByDeptId(String departmentId){
		Department dept = this.departmentService.updateInfo(departmentId);
		_logger.debug("根据部门ID获取其部门区域");
		return dept.getDepartmentArea();
	}
	
	/**
	 * 返回所有部门列表
	 * @return
	 */
	@RequestMapping(value="/all.htm",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,List<Department>> getAllDepartments() {
		Map<String,List<Department>> map = new HashMap<String, List<Department>>();
		List<Department> departments = departmentService.getAllDepartments();
		map.put("departments", departments);
		return map;
	}
	
	/**
	 * 添加部门
	 * @param department
	 * @return
	 */
	@RequestMapping(value="/add.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String add(Department department){
		String companyId = SecurityContextUtil.getCompanyId();
		department.setCompany(companyService.getEntity(companyId));
		String id =  departmentService.addDepartment(department);
		if(StringUtils.hasText(id)){
			_logger.debug("添加部门成功");
			return "SUCCESS";
		}else{
			_logger.debug("添加部门失败");
			return "FAILURE";
		}
	}
	/**
	 * 修改部门信息
	 * @param department
	 * @return
	 */
	@RequestMapping(value="/update.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String update(Department department){
		departmentService.updateDepartment(department);
		_logger.debug("修改部门信息成功");
		return "SUCCESS";
	}
	
	/**
	 * 删除部门
	 * @param deptIds
	 * @return
	 */
	@RequestMapping(value="/delete.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String deleteDepartment(String deptIds){
		String[] new_ids = deptIds.split(",");
		for(int i=0;i<new_ids.length;i++){
			this.departmentService.deleteDepartment(new_ids[i], true, true);
		}
		_logger.debug("删除部门信息成功");
		return "SUCCESS";
	}
	
	/**
	 * 查找部门
	 * @param deptName
	 * @return
	 */
	@RequestMapping(value="/search.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public List<Map<String, Object>> searchDept(String deptName){
		return this.departmentService.searchDept(deptName);
	}
	
	/**
	 * 获取部门类型列表
	 * @return
	 */
	@RequestMapping(value="/type.htm")
	@ResponseBody
	public List<Map<String, Object>> departmentType(){
		return this.departmentService.departmentType();
	}
	
	/**
	 * 获取部门区域列表
	 * @return
	 */
	@RequestMapping(value="/area.htm")
	@ResponseBody
	public List<Map<String, Object>> departmentArea(){
		return this.departmentService.departmentArea();
	}
}
