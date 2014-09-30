/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:IntegrationService.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-17
 */
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.IntegrationDao;
import com.cmcc.zysoft.sellmanager.model.Integration;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.service
 * 创建时间：2013-12-17
 */

@Service
public class IntegrationService extends BaseServiceImpl<Integration, String> {
	@Resource
	private IntegrationDao integrationDao;

	/* (non-Javadoc)
	 * @see com.starit.common.dao.service.BaseServiceImpl#getHibernateBaseDao()
	 */
	@Override
	public HibernateBaseDao<Integration, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.integrationDao;
	}
	
	/**
	 * 积分列表
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pagination<?> getIntegration(int rows,int page)
	{
		return integrationDao.getIntegration(rows, page);
	}
	/**
	 * 更新积分
	 * @return
	 */
	public String updateIntegration(){
		return this.integrationDao.updateIntegration();
	}
}
