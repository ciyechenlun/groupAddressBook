/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:ManageCityService.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-10-24
 */
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.ManageCityDao;
import com.cmcc.zysoft.sellmanager.model.ManageCity;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.service
 * 创建时间：2013-10-24
 */
@Service
public class ManageCityService extends BaseServiceImpl<ManageCity, String> {

	@Resource
	private ManageCityDao manageCityDao;
	
	/* (non-Javadoc)
	 * @see com.starit.common.dao.service.BaseServiceImpl#getHibernateBaseDao()
	 */
	@Override
	public HibernateBaseDao<ManageCity, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.manageCityDao;
	}

}
