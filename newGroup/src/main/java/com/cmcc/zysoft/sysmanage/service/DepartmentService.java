// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.dao.DepartmentDao;
import com.cmcc.zysoft.sysmanage.dao.SysEmployeeDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * DepartmentService.java
 * @author 李三来
 * @mail li.sanlai@ustcinfo.com
 * @date 2012-11-29 下午8:16:31
 */
@Service
public class DepartmentService extends BaseServiceImpl<Department, String> {

	@Resource
	private DepartmentDao departmentDao;
	
	@Resource
	private SysEmployeeDao sysEmployeeDao;
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private SysEmployeeService sysEmployeeService;
	
	@Override
	public HibernateBaseDao<Department, String> getHibernateBaseDao() {
		return this.departmentDao;
	}
	
	/**
	 * 根据特定组织机构的ID获取其子级组织结构树
	 * @param pid
	 * @return
	 */
	public List<?> departmentTree(String pid){
		List<?> list = null;
		list = departmentDao.departmentTree(pid);
		return list;
	}
	
	/**
	 * 添加部门
	 * @param department
	 * @return
	 */
	public String addDepartment(Department department){
		department.setDelFlag("0");
		department.setHaveChildDeparment("N");
		department.setCreateTime(new Date());
		String id = departmentDao.save(department);
		if(!"0".equals(department.getParentDepartmentId())){
			Department father_dept = departmentDao.get(department.getParentDepartmentId());
			father_dept.setHaveChildDeparment("Y");
			departmentDao.update(father_dept);
		}
		return id;
	}

	/**
	 * 查找特定公司id下的所有部门
	 * @param companyId
	 * @return List
	 */
	public List<Department> getDepartmentsByComId(String companyId) {
		List<Department> departmentsList = departmentDao.getDepartmentsByComId(companyId);
		return departmentsList;
	}
	/**
	 * 查询特定部门id下的所有子部门
	 * @param pid
	 * @return 
	 */
	public List<Department> getDepartmentByPid(String pid) {
		List<Department> departmentsList = departmentDao.getDepartmentsByPid(pid);
		return departmentsList;
	}

	/**
	 * 查询所有的部门
	 * @return
	 */
	public List<Department> getAllDepartments(){
		List<Department> departments = departmentDao.getAllDepartments();
		return departments;
	}
	
	/**
	 * 删除部门
	 * @param departmentId
	 * @param up
	 * @param down
	 */
	public void deleteDepartment(String departmentId, boolean up, boolean down) {
		Department dept = departmentDao.get(departmentId);
		dept.setDelFlag("1");
		this.departmentDao.update(dept);
		if(down && "Y".equals(dept.getHaveChildDeparment())){
			List<Department> list = this.findByNamedParam(new String[]{"parentDepartmentId" ,"delFlag"} , new Object[]{departmentId,"0" });
			if(null != list && list.size() > 0){
				Department subDept = null;
				for(int i=0;i<list.size();i++){
					subDept = list.get(i);
					this.deleteDepartment(subDept.getDepartmentId(), false, down);
				}
				dept.setHaveChildDeparment("N");
				departmentDao.update(dept);
			}
		}
		if(up && !"0".equals(dept.getParentDepartmentId())){
			int count = this.departmentDao.getSubDeptCount(dept.getParentDepartmentId(), departmentId);
			if(count == 0){
				Department parent_dept = this.departmentDao.get(dept.getParentDepartmentId());
				parent_dept.setHaveChildDeparment("N");
				departmentDao.update(parent_dept);
			}
		}
	}
	
	/**
	 * 修改部门
	 * @param department
	 */
	public void updateDepartment(Department department) {
		if(!"0".equals(department.getParentDepartmentId())){
			Department father_dept = departmentDao.get(department.getParentDepartmentId());
			father_dept.setHaveChildDeparment("Y");
			departmentDao.update(father_dept);
		}
		Department new_dept = departmentDao.get(department.getDepartmentId());
		if(!new_dept.getParentDepartmentId().equals(department.getParentDepartmentId()) && !"0".equals(new_dept.getParentDepartmentId())){
			List<Department> list = this.findByNamedParam(new String[]{"parentDepartmentId","delFlag"}, new Object[]{new_dept.getParentDepartmentId(),"0"});
			if(list.size() == 1){
				Department parent_new_dept = departmentDao.get(new_dept.getParentDepartmentId());
				parent_new_dept.setHaveChildDeparment("N");
				departmentDao.update(parent_new_dept);
			}
		}
		new_dept.setCompany(department.getCompany());
		new_dept.setParentDepartmentId(department.getParentDepartmentId());
		new_dept.setDepartmentName(department.getDepartmentName());
		new_dept.setDepartmentAddress(department.getDepartmentAddress());
		new_dept.setTelephone(department.getTelephone());
		new_dept.setFax(department.getFax());
		new_dept.setDepartmentType(department.getDepartmentType());
		new_dept.setDepartmentArea(department.getDepartmentArea());
		new_dept.setSaleTask(department.getSaleTask());
		new_dept.setDisplayOrder(department.getDisplayOrder());
		departmentDao.update(new_dept);
	}
	/**
	 * 根据公司Id获取下面的部门树
	 * @author yandou
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> deptTreeByCompanyId(String companyId) {
		List<Map<String,Object>> deptList = this.departmentDao.departmentTree("0",companyId);
		return getChildDeptComp(deptList,companyId);
	}
	
	/**
	 * 获取子部门，递归
	 * @author yandou
	 * @param list
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getChildDeptComp(List<Map<String, Object>> list,String companyId){
		for(Map<String,Object> map : list){
			String haveChildDept = map.get("haveChildDept").toString();
			map.put("iconCls", "department");
			if("Y".equals(haveChildDept)){//存在子部门
				String deptId = map.get("id").toString();
				List<Map<String, Object>> childList = this.departmentDao.departmentTree(deptId,companyId);
				getChildDeptComp(childList,companyId);
				map.put("state", "closed");
				map.put("children", childList);
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 查看或修改指定部门时,通过部门Id获得部门信息 
	 * @param deptId
	 * @return
	 */
	public Department updateInfo(String deptId) {
		return this.departmentDao.get(deptId);
	}
	
	
	/**
	 * combortree 获取父部门
	 * @return
	 */
	public List<Map<String, Object>> deptTree(String companyId){
		List<Map<String, Object>> list = this.departmentDao.deptTree("0",companyId);
		return childDept(list,companyId);
	}
	
	/**
	 * 递归获取子部门
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> childDept(List<Map<String, Object>> list,String companyId){
		for(Map<String,Object> map : list){
			String haveChildDeparment = map.get("haveChildDept").toString();
			if("Y".equals(haveChildDeparment)){
				String departmentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.departmentDao.deptTree(departmentId,companyId);
				childDept(childList,companyId);
				map.put("state", "closed");
				map.put("children", childList);
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 查询部门
	 * @param deptName
	 * @return
	 */
	public List<Map<String, Object>> searchDept(String deptName){
		Map<String, Object> map = null;
		List<Map<String, Object>> list = this.departmentDao.searchDept(deptName);
		List<Map<String, Object>> parentList = new ArrayList<Map<String, Object>>();
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				if(!"0".equals(map.get("parent_department_id").toString())){
					parentList.addAll(searchParent(map.get("parent_department_id").toString()));
				}
			}
			list.addAll(parentList);
		}
		return list;
	}
	
	/**
	 * 通过parentDepartmentId找到所有的父菜单
	 * @param parentDepartmentId
	 * @return
	 */
	public List<Map<String, Object>> searchParent(String parentDepartmentId){
		List<Map<String, Object>> parentList = this.departmentDao.searchDeptByDeptId(parentDepartmentId);
		Map<String,Object> parentMap = parentList.get(0);
		if(!"0".equals(parentMap.get("parent_department_id").toString())){
			parentList.addAll(searchParent(parentMap.get("parent_department_id").toString()));
		}
		return parentList;
	}
	
	/**
	 * 获取部门树   部门管理    列表展示
	 * @return
	 */
	public List<Map<String, Object>> tree(){
		List<Map<String, Object>> list = this.departmentDao.tree("0");
		return getSubDept(list);
	}
	
	/**
	 * 递归获取子部门列表
	 * @return 
	 */
	public List<Map<String, Object>> getSubDept(List<Map<String, Object>> list){
		for(Map<String, Object> map : list){
			String haveChildDeparment = map.get("have_child_deparment").toString();
			if("Y".equals(haveChildDeparment)){
				String parentDepartmentId = map.get("department_id").toString();
				List<Map<String, Object>> ChildList = this.departmentDao.tree(parentDepartmentId);
				getSubDept(ChildList);
				map.put("children", ChildList);
				map.put("state", "closed");
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}

	/**
	 * 立即定位，部门树
	 * @author yandou
	 * @param parentDepartmentId
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> deptsForImmeLocat(String parentDepartmentId,String companyId) {
		List<Map<String, Object>> depts = this.departmentDao.departmentTree(parentDepartmentId, companyId);
		for(Map<String, Object> map : depts){
			String deptId = map.get("id").toString();
			map.put("id", "d_"+deptId);
			map.put("iconCls", "department");
			List<Map<String, Object>> empsList = this.sysEmployeeDao.empByDeptId(deptId);
			if("Y".equals(map.get("haveChildDept").toString())){
				map.put("state", "closed");
			}else{
				if(empsList.isEmpty()){
					map.put("state", "open");
				}else{
					map.put("state", "closed");
				}
			}
		}
		return depts;
	}

	/**
	 * 立即定位，部门树
	 * @author yandou
	 * @param parentDepartmentId
	 * @return
	 */
	public List<Map<String, Object>> deptsForImmeLocat(String parentDepartmentId) {
		String companyId = SecurityContextUtil.getCompanyId();
		List<Map<String, Object>> depts = this.departmentDao.deptTree(parentDepartmentId,companyId);
		for(Map<String, Object> map : depts){
			String deptId = map.get("id").toString();
			map.put("id", "d_"+deptId);
			map.put("iconCls", "department");
			List<Map<String, Object>> empsList = this.sysEmployeeDao.empByDeptId(deptId);
			if("Y".equals(map.get("haveChildDept").toString())){
				map.put("state", "closed");
			}else{
				if(empsList.isEmpty()){
					map.put("state", "open");
				}else{
					map.put("state", "closed");
				}
			}
		}
		return depts;
	}
	
	/**
	 * 获取部门类型列表
	 * @return
	 */
	public List<Map<String, Object>> departmentType(){
		return this.departmentDao.departmentType();
	}
	
	/**
	 * 获取部门区域列表
	 * @return
	 */
	public List<Map<String, Object>> departmentArea(){
		return this.departmentDao.departmentArea();
	}
	
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDept(String deptId){
		String deptId_new = "'"+deptId+"'";
		return this.departmentDao.allChildrenDept(deptId, deptId_new);
	}
	
	/**
	 * 获取指定部门的最上级部门
	 * @param deptId
	 * @return
	 */
	public String grandFather(String deptId){
		Department dept = this.departmentDao.get(deptId);
		if(dept.getParentDepartmentId().equals("0")){
			return deptId;
		}else{
			return this.departmentDao.grandFather(dept.getParentDepartmentId());
		}
	}
	
	/**
	 * 公司部门树(员工管理,基础数据模块,销售管理模块)
	 * 首先获取所有子公司,目前公司无上下级关系
	 * @return
	 */
	public List<Map<String, Object>> companyDeptTree() {
		//获取当前登陆用户所属的公司,如果用户角色为超级管理员,则获取所有公司;其他则获取当前公司
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roleList = user.getRoles();
		if(roleList.get(0).getRoleCode().equals("0")){
			companyId = "";
		}
		List<Map<String,Object>> companyTree = this.companyService.getCompanyTreeHql(companyId);
		return companyDeptChildrenTree(companyTree);
	}
	
	/**
	 * 获取公司部门树辅助方法
	 * @param list
	 * @return
	 */
	public List<Map<String,Object>> companyDeptChildrenTree(List<Map<String,Object>> list){
		for(Map<String,Object> map : list){
			//添加公司下面的部门
			String companyId = map.get("id").toString();
			map.put("iconCls", "company");
			List<Map<String,Object>> deptList = this.deptTreeForCompany(companyId);//根据公司id获取下面的部门
			if(deptList.size()==0){
				map.put("state", "open");
			}else{
				map.put("children", deptList);
				map.put("state", "closed");
			}
		}
		return list;
	}
	
	/**
	 * 员工管理 左侧部门树  
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @return
	 */
	public List<Map<String, Object>> deptTreeForCompany(String companyId){
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roleList = user.getRoles();
		String deptId="";
		if(roleList.get(0).getRoleCode().equals("0")||roleList.get(0).getRoleCode().equals("4")){
			deptId = "0";
		}else{
			Employee employee = this.sysEmployeeService.getEntity(user.getEmployeeId());
			deptId = employee.getDepartmentId();
		}
		List<Map<String, Object>> list = this.departmentDao.deptTreeForCompany(deptId,companyId);
		for(Map<String, Object> map:list){
			if(map.get("attributes").toString().equals("01")){
				map.put("iconCls", "sale");
			}else if(map.get("attributes").toString().equals("02")){
				map.put("iconCls", "rep");
			}else{
				map.put("iconCls", "nav");
			}
		}
		return childDeptForCompany(list,companyId);
	}
	
	/**
	 * 递归获取子部门
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> childDeptForCompany(List<Map<String, Object>> list,String companyId){
		for(Map<String, Object> map : list){
			String haveChildDeparment = map.get("haveChildDeparment").toString();
			if("Y".equals(haveChildDeparment)){
				String parentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.departmentDao.deptChildTreeForCompany(parentId,companyId);
				for(Map<String, Object> childmap:childList){
					if(childmap.get("attributes").toString().equals("01")){
						childmap.put("iconCls", "sale");
					}else if(childmap.get("attributes").toString().equals("02")){
						childmap.put("iconCls", "rep");
					}else{
						childmap.put("iconCls", "nav");
					}
				}
				childDeptForCompany(childList,companyId);
				map.put("children", childList);
				map.put("state", "closed");
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	/**
	 * 根据企业id获取所有部门
	 * @return
	 */
	public List<Map<String, Object>> getDepartbyCompany(String companyId){
		return this.departmentDao.getDepartbyCompany(companyId);
	}
}
