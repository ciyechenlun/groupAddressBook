// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.DeptMagDao;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DeptMagService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-6 下午3:11:03
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class DeptMagService extends BaseServiceImpl<Department, String>{
	
	@Resource
	private DepartmentVersionService departmentVersionService;
	
	@Resource
	private DeptMagDao deptMagDao;
	
	@Resource
	private LookGroupService lookGroupService;
	
	@Resource
	private TxlVersionService txlVersionService;
	
	@Resource
	private UserCompanyChangedService userCompanyChangedService;
	
	@Resource
	private UserCompanyService userCompanyService;
	@Resource
	private CompanyVersionService companyVersionService;
	@Resource
	private ImportService importService;
	@Resource
	private GroupVersionService groupVersionService;
	
	@Override
	public HibernateBaseDao<Department, String>getHibernateBaseDao(){
		return this.deptMagDao;
	}
	
	/**
	 * 获取最上级的部门.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptList(){
		List<Map<String, Object>> list = this.deptMagDao.deptList("0");
		return childDept(list);
	}
	
	/**
	 * 获取所有部门.当前选中的公司.
	 * @param parentDepartmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptList(String companyId){
		List<Map<String,Object>> list = this.deptMagDao.deptList("0", companyId);
		return childDept(list,companyId);
	}
	/**
	 * 递归获取所有子部门.
	 * @param list 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> childDept (List<Map<String, Object>> list){
		for(Map<String, Object> map : list){
			String isLeaf = map.get("isLeaf").toString();
			if("N".equals(isLeaf)){
				String parentDepartmentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.deptMagDao.deptList(parentDepartmentId);
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
	 * 递归获取所有子部门.当前公司.
	 * @param list 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> childDept (List<Map<String, Object>> list,String companyId){
		for(Map<String, Object> map : list){
			String isLeaf = map.get("isLeaf").toString();
			if("N".equals(isLeaf)){
				String parentDepartmentId = map.get("id").toString();
				List<Map<String, Object>> childList = this.deptMagDao.deptList(parentDepartmentId,companyId);
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
	 * 通过部门Id找到部门级别.
	 * @param departmentId 
	 * @return 
	 * 返回类型：int
	 */
	public int departmentLevel(String departmentId){
		if("0".equals(departmentId)){
			return 0;
		}else{
			Department department = this.deptMagDao.get(departmentId);
			return department.getDepartmentLevel();
		}
	}
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDept(String deptId){
		//String deptId_new = "'"+deptId+"'";
		return this.deptMagDao.allChildrenDeptNew(deptId);
	}
	/**
	 * 增加或修改组织机构信息.
	 * @param department 
	 * @return 
	 * 返回类型：String
	 */
	public String save(Department department,String companyId,String companyName){
		//删除type=3的版本
		companyVersionService.delVsersion(companyId);
		Company company = new Company(companyId);
		String departmentName = department.getDepartmentName();
		String firstword = concatPinyin.changeToPinyin(departmentName.substring(0, 1));
		department.setDepartmentFirstword(firstword);
		//检测名字是否重复
		boolean checkDepartmentName = this.deptMagDao.
				checkDeptName(departmentName, department.getParentDepartmentId(),companyId);
		if(StringUtils.hasText(department.getDepartmentId())){
			Department old_department = this.deptMagDao.get(department.getDepartmentId());
			if(!old_department.getDepartmentName().equals(department.getDepartmentName()) 
					|| !old_department.getParentDepartmentId().equals(department.getParentDepartmentId())){
				if(checkDepartmentName){
					String oldDeptPath = companyName + "-" + this.importService.fullDepartmentName("", department.getDepartmentId());
					List<Map<String,Object>> ucList = this.deptMagDao.getUserCompanyByDeptName(oldDeptPath, companyId);
					old_department.setDepartmentName(department.getDepartmentName());
					old_department.setDepartmentFirstword(firstword);
					old_department.setFax(department.getFax());
					old_department.setParentDepartmentId(department.getParentDepartmentId());
					old_department.setDisplayOrder(department.getDisplayOrder());
					old_department.setDepartmentLevel(this.departmentLevel(department.getParentDepartmentId())+1);
					this.deptMagDao.update(old_department);
					this.deptMagDao.flush();
					this.updateChildDeptLevel(old_department.getDepartmentId(), old_department.getDepartmentLevel(),department.getFax());
					this.departmentVersionService.save("1", department.getDepartmentId());
					String newDeptPath = companyName + "-" + this.importService.fullDepartmentName("", department.getDepartmentId());
					for (Map<String, Object> map : ucList) {
						String oldDeptName = map.get("department_name").toString();
						String newDeptName =oldDeptName.replace(oldDeptPath, newDeptPath);
						String userCompanyId=map.get("user_company_id").toString();
						this.userCompanyService.updateDeptName(newDeptName, userCompanyId);
						this.groupVersionService.addGroupVersion(userCompanyId, "1");
					}
					return "SUCCESS";
				}else{
					return "NAME"; //名字重复
				}
			}else{
				old_department.setDepartmentName(department.getDepartmentName());
				old_department.setDepartmentFirstword(firstword);
				old_department.setParentDepartmentId(department.getParentDepartmentId());
				old_department.setDisplayOrder(department.getDisplayOrder());
				old_department.setFax(department.getFax());
				old_department.setDepartmentLevel(this.departmentLevel(department.getParentDepartmentId())+1);
				this.deptMagDao.update(old_department);
				this.updateChildDeptLevel(old_department.getDepartmentId(), old_department.getDepartmentLevel(),department.getFax());
				this.departmentVersionService.save("1", department.getDepartmentId());
				return "SUCCESS"; //部门名字和上级部门ID都未修改
			}
			
		}else{
			if(checkDepartmentName){
				department.setCompany(company);
				department.setDepartmentLevel(this.departmentLevel(department.getParentDepartmentId())+1);
				department.setDelFlag("0");
				String departmentId = this.deptMagDao.save(department);
				if(StringUtils.hasText(departmentId)){
					this.departmentVersionService.save("0", departmentId);
					return "SUCCESS";
				}else{
					return "ERROR";
				}
			}else{
				return "NAME";
			}
		}
	}
	
	/**
	 * 删除部门.
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String del(String departmentId){
		this.deleteChildren(departmentId);
		return "SUCCESS";
	}
	
	/**
	 * 删除部门时,删除子部门.
	 * @param departmentId  
	 * 返回类型：void
	 */
	private void deleteChildren(String departmentId){
		Department department = this.deptMagDao.get(departmentId);
		if(department != null){
			department.setDelFlag("1");
			department.setModifyTime(new Date());
			this.deptMagDao.update(department);
			//this.delEmp(departmentId);
			this.departmentVersionService.save("2", departmentId);
			//删除部门下用户
			List<Map<String,Object>> list = this.deptMagDao.getUserCompanyByDepartmentId(departmentId);
			for(Map<String,Object> map: list)
			{
				this.userCompanyService.deleteUserCompany(map.get("user_company_id").toString(), departmentId);
			}
		}
		List<Map<String, Object>> list = this.deptMagDao.deptList(departmentId);
		if(list.size()>0){
			for(Map<String, Object> map:list){
				String parentDepartmentId = map.get("id").toString();
				this.deleteChildren(parentDepartmentId);
			}
		}
	}
	
	/**
	 * 删除部门下所有员工.
	 * @param departmentId  
	 * 返回类型：void
	 */
	private void delEmp(String departmentId){
		List<Map<String, Object>> empList = this.deptMagDao.empList(departmentId);
		if(empList.size()>0){
			for(Map<String, Object> map : empList){
				Employee employee = this.lookGroupService.getEntity(map.get("empId").toString());
				employee.setDelFlag("1");
				this.lookGroupService.updateEntity(employee);
				this.deptMagDao.delUser(employee.getEmployeeId());
				this.txlVersionService.saveAll("2", employee.getEmployeeId());
			}
		}
	}
	
	/**
	 * 删除员工时删除用户.
	 * @param employeeId 
	 * 返回类型：void
	 */
	public void delUser(String employeeId){
		this.deptMagDao.delUser(employeeId);
	}
	
	/**
	 * 检查部门是否存在.
	 * @param i 
	 * @param fullName 
	 * @return 
	 * 返回类型：boolean
	 */
	public String checkDept(int i, String fullName,String company_id,String parentDepartmentId){
		return this.deptMagDao.checkDept(i, fullName,company_id,parentDepartmentId);
	}
	
	public String checkDept(String level,String deptName)
	{
		return null;
	}
	
	/**
	 * 修改部门时,修改其子部门的级别.
	 * @param departmentId 
	 * @param departmentLevel 
	 * @param fax V网编号
	 * 返回类型：void
	 */
	public void updateChildDeptLevel(String departmentId,int departmentLevel,String fax){
		List<Map<String, Object>> childList = this.deptMagDao.deptList(departmentId);
		if(childList.size()>0){
			for(Map<String, Object> map :childList){
				String id = map.get("id").toString();
				Department department = this.deptMagDao.get(id);
				department.setFax(fax);
				department.setDepartmentLevel(departmentLevel+1);
				this.deptMagDao.update(department);
				this.updateChildDeptLevel(id, departmentLevel+1,fax);
			}
		}
	}
	
	/**
	 * 获取当前登陆人所在公司的部门级别.
	 * @param companyId 
	 * @return 
	 * 返回类型：int
	 */
	public int deptLevel(String companyId){
		return this.deptMagDao.deptLevel(companyId);
	}
	
	/**
	 * 部门树,异步加载.
	 * @param parentDeptId 父部门Id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptTree(String parentDeptId, String companyId,String isRecycle){
		return this.deptMagDao.deptTree(parentDeptId,companyId,isRecycle);
	}
	/**
	 * 根据分公司id，部门树,异步加载.
	 * @param parentDeptId 父部门Id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptTreeByCompanyId(String companyId,String parentDeptId){
		return this.deptMagDao.deptTreeByCompanyId(companyId,parentDeptId);
	}
	/**
	 * 根据部门ids,找到一个部门或多个部门下的人员.
	 * @param departmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> empListByIds(String departmentIds){
		return this.deptMagDao.empListByIds(departmentIds);
	}
	
	/**
	 * 获取当前公司部门最大排序
	 * @param companyId
	 * @return
	 */
	public int getMaxDisplayOrder(String companyId)
	{
		return this.deptMagDao.getMaxDisplayOrder(companyId);
	}
	
	/**
	 * 修改一级部门名称之后将对应的人员信息更新.
	 * @param oldDepartmentName 
	 * @param newDepartmentName 
	 * @param oldParentDepartmentId 修改前的上级部门
	 * @param newParentDepartmentId 修改后的上级部门
	 * 返回类型：void
	 */
	public void updateParentDepartmentNameForEmployee(String oldDepartmentName,
			String newDepartmentName,String oldParentDepartmentId,String newParentDepartmentId){
		String name = this.deptMagDao.getParentDepartmentName(newParentDepartmentId);
		if("0".equals(newParentDepartmentId)){
			//修改之后,新的上级部门ID为0
			if("0".equals(oldParentDepartmentId)){
				//且修改之前上级部门ID也为0
				if(!newDepartmentName.equals(oldDepartmentName)){
					//修改之后部门名字更换了,将原来的名字改为新的
					this.deptMagDao.updateAllDepartmentName(newDepartmentName, oldDepartmentName);
				}
			}else{
				//修改之前的上级部门ID不为0,即将原来的某个子部门升级为一级部门.
				name =  "";
			}
		}else{
			//修改之后,新的上级部门ID不为0
			if("0".equals(oldParentDepartmentId) || !newParentDepartmentId.equals(oldParentDepartmentId)){
				//修改之前为0,修改之后不为0或者修改之后上级部门ID不同,需找到新的上级部门的最上级部门
				this.deptMagDao.updateAllDepartmentName(name, oldDepartmentName);
			}
		}
	}
	
	/**
	 * 根据部门ids,找到一个部门或多个部门下的人员.
	 * @param departmentIds 
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> empsByDepts(String departmentIds) {
		return this.deptMagDao.empsByDepts(departmentIds);
	}
	/**
	 * 当前用户所在部门树,异步加载.
	 * @param parentDeptId 父部门Id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> curSeconedDeptTree(String companyId,String isRecycle){
		return this.deptMagDao.curSeconedDeptTree(companyId,isRecycle);
	}
	/**
	 * 更新指定父部门下所有大于等于某个显示顺序的部门
	 * @param parentDepartmentId
	 * @param companyId
	 * @param displayOrder
	 */
	public void updateDisplayOrder(String parentDepartmentId,String companyId,int displayOrder){
		List<Map<String, Object>> list = this.deptMagDao.selectDisplayOrder(parentDepartmentId, companyId, displayOrder);
		this.deptMagDao.updateDisplayOrder(parentDepartmentId,companyId,displayOrder);
		if(null != list){
			for (Map<String, Object> map : list) {
				this.departmentVersionService.save("1", map.get("department_id").toString());
			}
		}
	}
	/**
	 * 根据部门级别获取部门
	 * @param rows
	 * @param page
	 * @param companyId
	 * @param parentDeptId
	 * @param departmentLevel
	 * @return
	 */
	public Pagination<?> getDepartment(int rows, int page, String companyId,String parentDeptId,String departmentLevel){
		return this.deptMagDao.getDepartment(rows, page, companyId,parentDeptId,departmentLevel);
	}
	/**
	 * 获取当前用户的二级部门
	 * @param rows
	 * @param page
	 * @param companyId
	 * @param parentDeptId
	 * @param departmentLevel
	 * @return
	 */
	public Pagination<?> getSecondDepartment(String companyId,String employeeId){
		return this.deptMagDao.getSecondDepartment(companyId,employeeId);
	}
    /**
     * 获取当前部门所在的同级部门中的相对位置
     * @param companyId
     * @param parentDeptId
     * @param departmentId
     * @return
     */
    public Long getRelativeOrderById(String companyId,String parentDeptId,String departmentId){
    	return this.deptMagDao.getRelativeOrderById(companyId,parentDeptId,departmentId);
    }
	/**
	 * 拖拽排序后更新display_order
	 * @param usercompanyId
	 * @param displayOrder
	 */
	public void updateDisplayOrderById(String parentDeptId,String deptLevel,String departmentId,int displayOrder){
		this.deptMagDao.updateDisplayOrderById(parentDeptId,deptLevel,departmentId, displayOrder);
		this.departmentVersionService.save("1",departmentId);
	}
	public List<Map<String,Object>> treeOfUserCompany(String companyId,String employeeId){
		return this.deptMagDao.treeOfUserCompany(companyId, employeeId);
	}
	/**
	 * 获取部门管理员管理的项目
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	public List<Map<String,Object>> getManageDept(String companyId,String employeeId){
		return this.deptMagDao.getManageDept(companyId, employeeId);
	}
	public List<Map<String,Object>>	 getDeptOfManage(String companyId,String employeeId){
		return this.deptMagDao.getDeptOfManage(companyId, employeeId);
	}
}
