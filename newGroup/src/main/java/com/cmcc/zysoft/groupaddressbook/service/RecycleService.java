// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.DeptMagDao;
import com.cmcc.zysoft.groupaddressbook.dao.RecycleDao;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱：zhang.jun3@ustcinfo.com
 * <br />描述：RecycleService
 * <br />版本:1.0.0
 * <br />日期： 2013-11-22 上午11:52:35
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class RecycleService extends BaseServiceImpl<Employee, String>{
	
	@Resource
	private RecycleDao recycleDao;
	@Resource
	private DeptMagDao deptMagDao;
	@Resource
	private DepartmentVersionService departmentVersionService;
	@Resource
	private GroupVersionService groupVersionService;
	@Resource
	private UserCompanyService userCompanyService;
	

	@Override
	public HibernateBaseDao<Employee, String> getHibernateBaseDao() {
		return this.recycleDao;
	}
	
	
	/**
	 * 查看回收站通讯录,可查看指定部门联系人.
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
	public Pagination<?> getGroupInfo(int rows, int page, String deptId, String companyId){
		return this.recycleDao.getGroupInfo(rows, page, deptId, companyId);
	}
	
	/**
     * 回收站非企业通讯录.
     * @param rows
     * @param page
     * @param companyId
     * @return 
     * 返回类型：Pagination<?>
     */
    public Pagination<?> getOrgGroupInfo(int rows, int page, String companyId){
    	return this.recycleDao.getOrgGroupInfo(rows, page, companyId);
    }
    /**
     * 部门是否被删除
     * @param deptId
     * @return
     */
    public boolean isDepartmentDelete(String deptId){
    	return this.recycleDao.isDepartmentDelete(deptId);
    }
    /**
     * 恢复部门
     * @param deptId
     * @return
     */
    public void recycleDepartment(String deptId){
    	 this.recycleDao.recycleDepartment(deptId);
    	 this.departmentVersionService.save("0", deptId);
    }
    /**
     * 恢复用户
     * @param employeeId
     * @param companyId
     * @param departmentId
     */
    public void recycleEmployee(String departmentId,String userCompanyId){
   	 this.recycleDao.recycleEmployee(userCompanyId,departmentId);
   	 this.groupVersionService.addGroupVersion(userCompanyId, "0");
   }
    /**
     * 彻底删除部门
     * @param departmentId
     */
    public void delRecycleDepartment(String departmentId){
		this.recycleDao.delRecycleDepartment(departmentId);
		List<Map<String, Object>> list = this.deptMagDao.deptList(departmentId);
		if(list.size()>0){
			for(Map<String, Object> map:list){
				String parentDepartmentId = map.get("id").toString();
				this.delRecycleDepartment(parentDepartmentId);
			}
		}
	}
    /**
     * 彻底删除用户
     * @param employeeId
     * @param companyId
     * @param departmentId
     */
    public void delRecycleEmployee(String userCompanyId,String departmentId){
    	this.recycleDao.delRecycleEmployee(userCompanyId,departmentId);
    }
    
    public int checkInfo(String userCompanyId,String departmentId){
    	List<UserCompany> userCompany = this.userCompanyService.findByNamedParam("userCompanyId", userCompanyId);
    	int result = 0;
    	if(userCompany.size()!=0){
    		String mobile = userCompany.get(0).getMobile();
    		String companyId = userCompany.get(0).getCompanyId();
    		if(StringUtils.hasText(mobile)){
    			result = this.recycleDao.checkInfo(companyId, departmentId, mobile);
    		}
    	}
    	return result ;
    }
}
