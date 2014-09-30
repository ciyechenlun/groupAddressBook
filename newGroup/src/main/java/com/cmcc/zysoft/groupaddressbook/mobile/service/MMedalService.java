/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MMedalDao;
import com.cmcc.zysoft.sellmanager.model.Medal;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午4:07:23
 */

@Service
public class MMedalService extends BaseServiceImpl<Medal, String> {

	@Resource
	private MMedalDao mMedalDao;
	
	@Override
	public HibernateBaseDao<Medal, String> getHibernateBaseDao() {
		return this.mMedalDao;
	}
	
	/**
	 * 获取用户的勋章
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getEmpMedals(String userCode, String token, String movement_id) {
		return this.mMedalDao.getEmpMedals(userCode, token, movement_id);
	}
	
	/**
	 * 获取某个活动下的勋章
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalByMovement(String movement_id)
	{
		return this.mMedalDao.getMedalByMovement(movement_id);
	}
	
	/**
	 * 根据活动ID返回勋章系统
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalSysByMovement(String movement_id)
	{
		return this.mMedalDao.getMedalSysByMovement(movement_id);
	}
}
