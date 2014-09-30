// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.DepartmentVersionDao;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.DepartmentChanged;
import com.cmcc.zysoft.sellmanager.model.DepartmentChangedId;
import com.cmcc.zysoft.sellmanager.model.DepartmentVersion;
import com.cmcc.zysoft.sysmanage.service.DepartmentService;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DepartmentVersionService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-7 下午12:45:07
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class DepartmentVersionService extends BaseServiceImpl<DepartmentVersion, Integer>{
	
	@Resource
	private DepartmentChangedService departmentChangedService;
	
	@Resource
	private DepartmentVersionDao departmentVersionDao;
	
	@Resource
	private DepartmentService departmentService;
	
	@Override
	public HibernateBaseDao<DepartmentVersion, Integer>getHibernateBaseDao(){
		return this.departmentVersionDao;
	}
	
	/**
	 * 部门表增删改后,在版本表里更新.
	 * @param type 更新类型
	 * @param departmentId  部门Id
	 * 返回类型：void
	 */
	public void save(String type,String departmentId){
		DepartmentVersion departmentVersion = new DepartmentVersion();
		DepartmentChanged departmentChanged = new DepartmentChanged();
		Department department = new Department();
		department.setDepartmentId(departmentId);
		departmentVersion.setUpdateDate(new Date());
		int versionNum = this.departmentVersionDao.save(departmentVersion);
		DepartmentChangedId departmentChangedId = new DepartmentChangedId();
		departmentChangedId.setDepartmentId(departmentId);
		departmentChangedId.setDepartmentVersionNum(versionNum);
		departmentChanged.setId(departmentChangedId);
		departmentChanged.setDepartmentVersion(departmentVersion);
		departmentChanged.setDepartment(department);
		departmentChanged.setUpdateType(type);
		this.departmentChangedService.insertEntity(departmentChanged);
	}
	//add by zhangjun 2013/11/20
	/**
	 *删除企业时需删除部门表
	 * @param type 更新类型
	 * @param companyId  公司Id
	 * 返回类型：void
	 */
	public void saveVerBydelCompany(String type,String companyId){
		List<Map<String, Object>> list = this.departmentService.getDepartbyCompany(companyId);
		DepartmentVersion departmentVersion = new DepartmentVersion();
		departmentVersion.setUpdateDate(new Date());
		int versionNum = this.departmentVersionDao.save(departmentVersion);
		for (Map<String, Object> map : list) {
			DepartmentChanged departmentChanged = new DepartmentChanged();
			Department department = new Department();
			department.setDepartmentId(map.get("department_id").toString());
			DepartmentChangedId departmentChangedId = new DepartmentChangedId();
			departmentChangedId.setDepartmentId(map.get("department_id").toString());
			departmentChangedId.setDepartmentVersionNum(versionNum);
			departmentChanged.setId(departmentChangedId);
			departmentChanged.setDepartmentVersion(departmentVersion);
			departmentChanged.setDepartment(department);
			departmentChanged.setUpdateType(type);
			this.departmentChangedService.insertEntity(departmentChanged);
		}
		
	}
	//add by zhangjun 2013/11/20
}
