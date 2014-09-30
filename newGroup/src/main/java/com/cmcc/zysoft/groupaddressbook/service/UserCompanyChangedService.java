// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.UserCompanyChangedDao;
import com.cmcc.zysoft.sellmanager.model.UserCompanyChanged;
import com.cmcc.zysoft.sellmanager.model.UserCompanyChangedId;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserCompanyChangedService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-24 下午4:58:36
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class UserCompanyChangedService extends BaseServiceImpl<UserCompanyChanged, UserCompanyChangedId>{
	
	@Resource
	private UserCompanyChangedDao userCompanyChangedDao;
	
	@Override
	public HibernateBaseDao<UserCompanyChanged, UserCompanyChangedId>getHibernateBaseDao(){
		return this.userCompanyChangedDao;
	}

}
