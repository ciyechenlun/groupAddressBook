// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.ConfigType;
import com.cmcc.zysoft.sysmanage.dao.ConfigTypeDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author yandou
 */
@Service
public class ConfigTypeService extends BaseServiceImpl<ConfigType, String> {
	
	@Resource
	private ConfigTypeDao configTypeDao;

	@Override
	public HibernateBaseDao<ConfigType, String> getHibernateBaseDao() {
		return configTypeDao;
	}

	public List<ConfigType> getAllConfigTypes() {
		return this.configTypeDao.getAllConfigTypes();
	}

	/**
	 * 保存元数据类型
	 * @param configType
	 */
	public void saveConfigType(ConfigType configType) {
		String typeId = configType.getTypeId();
		if(StringUtils.hasText(typeId)){
			this.configTypeDao.saveOrUpdate(configType);
		}else{
			this.configTypeDao.save(configType);
		}
	}
}
