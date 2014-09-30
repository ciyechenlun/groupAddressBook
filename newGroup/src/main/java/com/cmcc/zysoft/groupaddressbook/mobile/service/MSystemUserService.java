// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MSystemUserDao;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MSystemUserService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-12 上午11:28:53
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MSystemUserService extends BaseServiceImpl<SystemUser, String>{
	
	@Resource
	private MSystemUserDao mSystemUserDao;
	
	@Override
	public HibernateBaseDao<SystemUser, String>getHibernateBaseDao(){
		return this.mSystemUserDao;
	}

}
