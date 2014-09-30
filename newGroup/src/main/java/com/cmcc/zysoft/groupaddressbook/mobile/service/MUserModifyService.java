// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MUserModifyDao;
import com.cmcc.zysoft.sellmanager.model.UserModify;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MUserModifyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午10:39:13
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MUserModifyService extends BaseServiceImpl<UserModify, String>{
	
	@Resource
	private MUserModifyDao mUserModifyDao;
	
	@Override
	public HibernateBaseDao<UserModify, String>getHibernateBaseDao(){
		return this.mUserModifyDao;
	}

}
