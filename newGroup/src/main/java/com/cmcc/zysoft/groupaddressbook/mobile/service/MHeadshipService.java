// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MHeadshipDao;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MHeadshipService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-2 上午10:08:29
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MHeadshipService extends BaseServiceImpl<Headship, String>{
	
	@Resource
	private MHeadshipDao mHeadshipDao;
	
	@Override
	public HibernateBaseDao<Headship, String>getHibernateBaseDao(){
		return this.mHeadshipDao;
	}

}
