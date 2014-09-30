package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MovementUsersDao;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.sellmanager.model.MovementUsers;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MovementUsersService
 * <br />版本:1.0.0
 * <br />日期： 2013-8-16 上午10:37:22
 * <br />CopyRight © Chinamobile Anhui Ltd Cmp
 */
@Service
public class MovementUsersService extends BaseServiceImpl<MovementUsers, String> {

	@Resource
	private MovementUsersDao movementUsersDao;
	
	@Resource
	private MovementService movementService;

	@Override
	public HibernateBaseDao<MovementUsers, String> getHibernateBaseDao() {
		return this.movementUsersDao;
	}
	
	/**
	 * 根据活动编号返回活动下用户列表
	 * @param movement_id：活动编号
	 * @param rows：分页大小
	 * @param page：页码
	 * @return
	 */
	public Pagination<Map<String, Object>> getUserListByMovementId(String movement_id,int rows,int page, String keyword)
	{
		return this.movementUsersDao.getUserListByMovementId(movement_id, rows, page, keyword);
	}
	
	/**
	 * 根据活动编号返回活动下用户列表（不分页）
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getUserListByMovementId(String movement_id)
	{
		return this.movementUsersDao.getUserListByMovementId(movement_id);
	}
	
	/**
	 * 删除活动下指定的用户
	 * @param movement_id
	 * @param employee_id
	 */
	public void deleteMovementUserByMovementIdAndEmployeeId(String movement_id,String employee_id)
	{
		this.movementUsersDao.deleteMovementUserByMovementIdAndEmployeeId(movement_id, employee_id);
	}
	
	/**
	 * 添加活动下用户
	 * @param users
	 * @param movement
	 */
	public void addUsers(String user,String movement_id)
	{
		Movement movement = this.movementService.getEntity(movement_id);
		this.movementUsersDao.addUsers(user, movement);
	}

}
