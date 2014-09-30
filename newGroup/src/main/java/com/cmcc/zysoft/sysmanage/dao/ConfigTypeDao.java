// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.ConfigType;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author yandou
 */
@Repository
public class ConfigTypeDao extends HibernateBaseDaoImpl<ConfigType, String> {

	@SuppressWarnings("unchecked")
	public List<ConfigType> getAllConfigTypes() {
		String hql = "from ConfigType as ct";
		return this.findByHQL(hql);
	}
	
}
