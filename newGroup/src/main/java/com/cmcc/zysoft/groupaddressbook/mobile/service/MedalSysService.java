/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:MedalService.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-8
 */
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MedalDao;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MedalSysDao;
import com.cmcc.zysoft.sellmanager.model.Medal;
import com.cmcc.zysoft.sellmanager.model.MedalSys;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.service
 * 创建时间：2013-12-8
 */
@Service
public class MedalSysService extends BaseServiceImpl<MedalSys, String> {

	@Resource 
	private MedalSysDao medalSysDao;
	/* (non-Javadoc)
	 * @see com.starit.common.dao.service.BaseServiceImpl#getHibernateBaseDao()
	 */
	@Override
	public HibernateBaseDao<MedalSys, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return medalSysDao;
	}
}
