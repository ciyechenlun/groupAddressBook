package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.UserDepartmentDao;
import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 周瑜
 * <br />邮箱： zhouyu#sgs@ahmobile.com
 * <br />描述：UserDepartmentService
 * <br />版本:1.0.0
 * <br />日期： 2013-6-24 下午18:58:36
 * <br />
 */

@Service
public class UserDepartmentService extends BaseServiceImpl<UserDepartment, String> {
	@Resource
	private UserDepartmentDao userDepartmentDao;

	@Override
	public HibernateBaseDao<UserDepartment, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.userDepartmentDao;
	}
	
	/**
	 * 根据部门和岗位返回信息，用来确认当前用户的部门隶属关系是否已经存在。（允许同一部门不同岗位情况存在）
	 * @param headship_id：岗位ID
	 * @param department_id：部门ID
	 * @return
	 */
	public List<Map<String,Object>> getUserDeptList(String user_company_id,String headship_id,String department_id)
	{
		return this.userDepartmentDao.getUserDeptList(user_company_id, headship_id, department_id);
	}
	
	/**
	 * 查看某个人的部门隶属关系是否存在
	 * @param user_company_id
	 * @param department_id
	 * @param headship_id
	 * @return
	 */
	public UserDepartment getUserDeptInfoByUserCompanyIdAndDeptIdAndHeadShipId(String user_company_id,String department_id,String headship_id)
	{
		return this.userDepartmentDao.getUserDeptInfoByUserCompanyIdAndDeptIdAndHeadShipId(user_company_id, department_id, headship_id);
	}
	
	/**
	 * 根据user_copmany_id删除用户部门隶属关系
	 * @param user_company_id
	 */
	public void deleteUserDepartment(String user_company_id,String department_id)
	{
		this.userDepartmentDao.deleteUserDepartment(user_company_id,department_id);
	}
}
