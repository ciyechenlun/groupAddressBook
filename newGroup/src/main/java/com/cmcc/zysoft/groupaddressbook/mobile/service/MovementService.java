package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.PCEmployeeDao;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MovementDao;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MovementService
 * <br />版本:1.0.0
 * <br />日期： 2013-8-16 上午10:37:22
 * <br />CopyRight © Chinamobile Anhui Ltd Cmp
 */
@Service
public class MovementService extends BaseServiceImpl<Movement, String> {

	@Resource
	private MovementDao movementDao;
	
	@Resource
	private PCEmployeeDao pcEmployeeDao;
	
	@Override
	public HibernateBaseDao<Movement, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.movementDao;
	}
	
	/**
	 * 根据用户编号，获取用户参与的活动列表
	 * @param userCode：employee_id
	 * @return
	 */
	public List<Map<String,Object>> getMovementListByUser(String userCode)
	{
		return this.movementDao.getMovementListByUser(userCode);
	}
	
	/**
	 * 返回当前用户所在公司的活动列表
	 * @return
	 */
	public List<Movement> getMovementListByLoginedUser()
	{
		User user = SecurityContextUtil.getCurrentUser();
		String pDepartmentId = !"4".equals(user.getRoles().get(0).getRoleId())?"":this.pcEmployeeDao.getUserParentDepartment(user.getEmployeeId());
		return this.movementDao.getMovementListByLoginedUser(pDepartmentId);
	}
	
	/**
	 * 获取所有活动（分页）
	 * @param rows：分页大小
	 * @param page：当前页码
	 * @return
	 */
	public Pagination<?> getMovementPage(int rows,int page)
	{
		User user = SecurityContextUtil.getCurrentUser();
		String pDepartmentId = "";
		if("4".equals(user.getRoles().get(0).getRoleId())){
			pDepartmentId = this.pcEmployeeDao.getUserParentDepartment(user.getEmployeeId());
		}
		return this.movementDao.getMovementPage(rows, page, pDepartmentId);
	}
	
	/**
	 * 禁用 or 启用 活动
	 * @param movement_id
	 */
	public void movementConfig(String movement_id)
	{
		this.movementDao.movementConfig(movement_id);
	}
	
	/**
	 * 获取勋章系统列表
	 * @return
	 */
	public List<Map<String,Object>> getMedalSystem()
	{
		return this.movementDao.getMedalSystem();
	}
	
	/**
	 * 获取所属赛季
	 * @return
	 */
	public List<Map<String,Object>> getParentMovement(){
		return this.movementDao.getParentMovement();
	}

}
