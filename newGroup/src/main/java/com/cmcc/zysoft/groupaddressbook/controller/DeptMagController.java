/** ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved. */
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.util.CacheFileUtil;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.starit.common.dao.support.Pagination;

/**
 * @author li.menghua
 * @2013-2-28
 */
@Controller
@RequestMapping("/pc/deptMag")
public class DeptMagController extends BaseController{
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private CompanyService companyService;
	@Resource
	private UserCompanyService userCompanyService;
	/**
	 * 部门树.
	 * @param parentDepartmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/tree.htm")
	@ResponseBody
	public List<Map<String,Object>> tree(String parentDepartmentId,HttpServletRequest request){
		String companyId = "";
		Company company;
		if (request.getSession().getAttribute("selCompany")!=null)
		{
			company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		else
		{	
			companyId = SecurityContextUtil.getCompanyId();
			company = this.companyService.getEntity(companyId);
		}List<Map<String,Object>> childList = this.deptMagService.deptList(companyId);
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		Map<String, Object> firstMap = new HashMap<String, Object>();
		firstMap.put("id", "0");
		firstMap.put("text", company.getCompanyName());
		firstMap.put("isLeaf", "N");
		firstMap.put("children", childList);
		firstMap.put("state", "open");
		fullList.add(firstMap);
		return fullList;
	}
	
	/**
	 * 跳转到  部门维护  页面.
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String main(ModelMap modelMap,HttpServletRequest request){
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
		Pagination<?> seDepList =null;
		String employeeId = SecurityContextUtil.getCurrentUser().getEmployeeId();
		if(null != employeeId && !employeeId.equals("")){
			List<UserCompany> uerCompanyList = this.userCompanyService.findByNamedParam
					(new String[]{"companyId","employeeId","manageFlag","delFlag"}, 
					new Object[]{companyId,employeeId,"3","0"});
			if(null !=uerCompanyList && uerCompanyList.size()>0){
					modelMap.addAttribute("manageFlag", "3");
				   // seDepList = this.deptMagService.getSecondDepartment(companyId,employeeId);
				}else{
					seDepList =  this.deptMagService.getDepartment(10, 1, companyId, "0", "1");
				}
		}else{
			seDepList =  this.deptMagService.getDepartment(10, 1, companyId, "0", "1");
		}
		//Pagination<?> seDepList =  this.deptMagService.getDepartment(10, 1, companyId, "0", "1");
		/*Pagination<?> thDepList =  this.deptMagService.getDepartment(10, 1, companyId, "", "2");
		Pagination<?> foDepList =  this.deptMagService.getDepartment(10, 1, companyId, "", "3");
		Pagination<?> fiDepList =  this.deptMagService.getDepartment(10, 1, companyId, "", "4");*/
		modelMap.addAttribute("seDepList", seDepList);
		/*modelMap.addAttribute("thDepList", thDepList);
		modelMap.addAttribute("foDepList", foDepList);
		modelMap.addAttribute("fiDepList", fiDepList);*/
		return "web/deptMag";
	}
	
	/**
	 * 增加或修改组织机构.
	 * @param department 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/save.htm")
	@Transactional
	@ResponseBody
	public String save(Department department,HttpServletRequest request){
		String companyId = "";
		String companyName="";
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
			companyName=company.getCompanyName();
		}
		else
		{
			companyId = SecurityContextUtil.getCompanyId();
		}
		//先清除缓存文件
		CacheFileUtil.deleteCahceFiles(companyId);
		return this.deptMagService.save(department,companyId,companyName);
	}
	
	/**
	 * 删除部门.
	 * @param departmentId  
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/del.htm")
	@Transactional
	@ResponseBody
	public String del(String departmentId){
		//Department dept = this.deptMagService.getEntity(departmentId);
		return this.deptMagService.del(departmentId);
	}
	
	/**
	 * 部门树,异步加载.
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/treeTest.htm")
	@ResponseBody
	public List<Map<String,Object>> treeTest(String id,String companyId,HttpServletRequest request,String isRecycle){
		if(isRecycle==null){
			isRecycle = "";
		}
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		Company company;
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
		else
		{
			company = this.companyService.getEntity(companyId);
		}
		if(StringUtils.hasText(id)){
			fullList = this.deptMagService.deptTree(id,companyId,isRecycle);
		}else{
			List<Map<String,Object>> childList = null;
			String employeeId = SecurityContextUtil.getCurrentUser().getEmployeeId();
			if(null != employeeId && !employeeId.equals("")){
				List<UserCompany> uerCompanyList = this.userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","manageFlag","delFlag"}, 
						new Object[]{companyId,employeeId,"3","0"});
				if(null !=uerCompanyList && uerCompanyList.size()>0){
					//String roleId = getUserRoleId();
					//if(roleId.equals("6")){
						childList = this.deptMagService.curSeconedDeptTree(companyId,isRecycle);
						//childList =this.deptMagService.getManageDept(companyId,employeeId);
					}else{
						childList = this.deptMagService.deptTree("0",companyId,isRecycle);
					}
			}else{
				childList = this.deptMagService.deptTree("0",companyId,isRecycle);
			}
			
			Map<String, Object> firstMap = new HashMap<String, Object>();
			firstMap.put("id", "0");
			firstMap.put("text", company.getCompanyName());
			firstMap.put("isLeaf", "N");
			firstMap.put("children", childList);
			firstMap.put("state", "open");
			fullList.add(firstMap);
		}
		return fullList;
	}

	/**
	 * 更新指定父部门下所有大于等于某个显示顺序的部门显示顺序+10
	 * @param parentDepartmentId
	 * @param companyId
	 * @param displayOrder
	 * @return
	 */
	@RequestMapping("/updateDisplayOrder.htm")
	@ResponseBody
	public String updateDisplayOrder(HttpServletRequest request,String parentDepartmentId,int displayOrder){
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
		this.deptMagService.updateDisplayOrder(parentDepartmentId, companyId, displayOrder);
		return "SUCCESS";
	}
	/**
	 * 部门树,异步加载.
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/load1LevelDepartment.htm")
	@ResponseBody
	public List<Map<String,Object>> load1LevelDepartment(String id,String companyId,HttpServletRequest request,String isRecycle){
		if(isRecycle==null){
			isRecycle = "";
		}
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		Company company;
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
		else
		{
			company = this.companyService.getEntity(companyId);
		}
		if(StringUtils.hasText(id)){
			fullList = this.deptMagService.deptTree(id,companyId,isRecycle);
		}else{
			String employeeId = SecurityContextUtil.getCurrentUser().getEmployeeId();
			if(null != employeeId && !employeeId.equals("")){
				List<UserCompany> uerCompanyList = this.userCompanyService.findByNamedParam
						(new String[]{"companyId","employeeId","manageFlag","delFlag"}, 
						new Object[]{companyId,employeeId,"3","0"});
				if(null !=uerCompanyList && uerCompanyList.size()>0){
					fullList = this.deptMagService.curSeconedDeptTree(companyId,isRecycle);
					}else{
						fullList = this.deptMagService.deptTree("0",companyId,isRecycle);
					}
			}else{
				fullList = this.deptMagService.deptTree("0",companyId,isRecycle);
			}
		}
		return fullList;
	}
	/**
	 * 根据父部门获取子部门
	 * @param modelMap
	 * @param pageNo
	 * @param companyId
	 * @param parentDeptId
	 * @param departmentLevel
	 * @return
	 */
	@RequestMapping(value="/getDepartment.htm")
	@ResponseBody
	public Pagination<?> getDepartment(ModelMap modelMap,int pageNo, String companyId,String parentDeptId,String departmentLevel){
		Pagination<?> depList =  this.deptMagService.getDepartment(10, pageNo, companyId, parentDeptId, departmentLevel);
		return depList;
	}
	@RequestMapping(value="/toAddDept.htm")
	public String toAddDept(){
		return "web/addDept";
	}
	@RequestMapping(value="/toEditDept.htm")
	public String toEditEmployee(ModelMap modelMap,String departmentId){
		Department dept = this.deptMagService.getEntity(departmentId);
		String parentId = dept.getParentDepartmentId();
		if(!"0".equals(parentId)){
			Department parentDept = this.deptMagService.getEntity(dept.getParentDepartmentId());
			modelMap.addAttribute("parentDept", parentDept);
		}
		Long relativeOrder = this.deptMagService.getRelativeOrderById(dept.getCompany().getCompanyId(),dept.getParentDepartmentId(),dept.getDepartmentId());
		modelMap.addAttribute("dept", dept);
		modelMap.addAttribute("relativeOrder", relativeOrder);
		return "web/editDept";
	}
	/**
	 * 保存排序.
	 * @param 'firstMove' :二级部门移动元素,'secondMove' :三级部门移动元素,
	 *'thirdMove' :四级部门移动元素,'fourthMove' :五级部门移动元素,
	 *'lastOrder1' :二级部门最终排序,'lastOrder2' :三级部门最终排序,
	 *'lastOrder3' :四级部门最终排序,'lastOrder4' :五级部门最终排序
	 *				
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/saveOrder.htm")
	@ResponseBody
	@Transactional
	public String saveOrder(@RequestBody HashMap<String,Object> map){
		try {
			List<String> firstMove = (ArrayList<String>)map.get("firstMove");
			List<String> secondMove = (ArrayList<String>)map.get("secondMove");
			List<String> thirdMove = (ArrayList<String>)map.get("thirdMove");
			List<String> fourthMove = (ArrayList<String>)map.get("fourthMove");
			List<String> lastOrder1 = (ArrayList<String>)map.get("lastOrder1");
			List<String> lastOrder2 = (ArrayList<String>)map.get("lastOrder2");
			List<String> lastOrder3 = (ArrayList<String>)map.get("lastOrder3");
			List<String> lastOrder4 = (ArrayList<String>)map.get("lastOrder4");
			String firstDeptId ="";
			String secondDeptId ="";
			String thirdDeptId ="";
			if(null != map.get("firstDeptId")){
				firstDeptId = map.get("firstDeptId").toString();
			}
			if(null != map.get("secondDeptId")){
			    secondDeptId = map.get("secondDeptId").toString();
			}
			if(null != map.get("thirdDeptId")){
				thirdDeptId = map.get("thirdDeptId").toString();
			}
			if(!firstMove.equals(lastOrder1)){
				for(int i=0;i<lastOrder1.size();i++){
					this.deptMagService.updateDisplayOrderById("0","1",lastOrder1.get(i), (i+1)*10);
				}
			}
			if(!secondMove.equals(lastOrder2)){
				for(int i=0;i<lastOrder2.size();i++){
					this.deptMagService.updateDisplayOrderById(firstDeptId,"2",lastOrder2.get(i), (i+1)*10);
				}
			}
			if(!thirdMove.equals(lastOrder3)){
				for(int i=0;i<lastOrder3.size();i++){
					this.deptMagService.updateDisplayOrderById(secondDeptId,"3",lastOrder3.get(i), (i+1)*10);
				}
			}
			if(!fourthMove.equals(lastOrder4)){
				for(int i=0;i<lastOrder4.size();i++){
					this.deptMagService.updateDisplayOrderById(thirdDeptId,"4",lastOrder4.get(i), (i+1)*10);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "FAUILE";
		}
		
		return "SUCCESS";
	}
	@RequestMapping(value="/mTree.htm")
	@ResponseBody
	public List<Map<String,Object>> treeOfUserCompany(String employeeId,String companyId){
		return this.deptMagService.treeOfUserCompany(companyId, employeeId);
	}
	@RequestMapping(value="/checkRole.htm")
	@ResponseBody
	public boolean checkRole(HttpServletRequest request,String departmentId){
		Department department = this.deptMagService.getEntity(departmentId);
		int departmentLevel =department.getDepartmentLevel();
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
		User user = SecurityContextUtil.getCurrentUser();
		List<Map<String, Object>> listMan = this.deptMagService.getDeptOfManage(companyId, user.getEmployeeId());
		if(listMan==null ||listMan.isEmpty()){
			listMan = (List<Map<String,Object>>)this.deptMagService.getSecondDepartment(companyId,user.getEmployeeId()).getResult();
		}
		for (Map<String, Object> map : listMan) {
			String deptId = map.get("id").toString();
			int deptLevel = this.deptMagService.getEntity(deptId).getDepartmentLevel();
			if(departmentId.equals(deptId)||
					departmentLevel>deptLevel){
				return true;
			}
		}
		return false;
	}
}
