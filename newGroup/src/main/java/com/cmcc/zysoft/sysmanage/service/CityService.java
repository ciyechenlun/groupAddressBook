// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Cities;
import com.cmcc.zysoft.sysmanage.dao.CityDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：
 * <br />@version:1.0.0
 * <br />日期： 2012-12-12 上午11:04:56
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class CityService extends BaseServiceImpl<Cities, Integer> {
	
	@Resource
	private CityDao dao;
	
	@Override
	public HibernateBaseDao<Cities, Integer> getHibernateBaseDao() {
		return this.dao;
	}

	/**
	 * 根据省份获取城市列表
	 * @param provinceid 省份ID
	 * @return 城市列表
	 */
	public List<Map<String, Object>> cities(String provinceid){
		return dao.cities(provinceid);
	}
	
}
