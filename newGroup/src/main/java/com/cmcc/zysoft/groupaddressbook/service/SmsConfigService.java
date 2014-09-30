/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.SmsConfigDao;
import com.cmcc.zysoft.sellmanager.model.SmsConfig;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：SmsConfigService.java
 * <br />版本: 1.0.0
 * <br />日期：2013-8-2 上午10:35:15
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Service
public class SmsConfigService extends BaseServiceImpl<SmsConfig, String> {

	@Resource
	private SmsConfigDao smsConfigDao;
	
	@Override
	public HibernateBaseDao<SmsConfig, String> getHibernateBaseDao() {
		return this.smsConfigDao;
	}
	
	/**
	 * 根据公司ID获取短信推广记录.
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getConfigByCompanyId(String companyId) {
		return this.smsConfigDao.getConfigByCompanyId(companyId);
	}
}