/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:PlugService.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-9-16
 */
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.PlugDao;
import com.cmcc.zysoft.sellmanager.model.Plug;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.service
 * 创建时间：2013-9-16
 */
@Service
public class PlugService extends BaseServiceImpl<Plug, String> {

	@Resource
	private PlugDao plugDao;
	
	/* (non-Javadoc)
	 * @see com.starit.common.dao.service.BaseServiceImpl#getHibernateBaseDao()
	 */
	@Override
	public HibernateBaseDao<Plug, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.plugDao;
	}
	
	/**
	 * 根据用户编号获得插件列表
	 * @param usercode
	 * @return
	 */
	public List<Map<String,Object>> getMyPlugs(String usercode)
	{
		return this.plugDao.getMyPlugs(usercode);
	}
	
}
