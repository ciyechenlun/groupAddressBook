/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:MedalService.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-8
 */
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MedalDao;
import com.cmcc.zysoft.sellmanager.model.Medal;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.service
 * 创建时间：2013-12-8
 */
@Service
public class MedalService extends BaseServiceImpl<Medal, String> {

	@Resource 
	private MedalDao medalDao;
	/* (non-Javadoc)
	 * @see com.starit.common.dao.service.BaseServiceImpl#getHibernateBaseDao()
	 */
	@Override
	public HibernateBaseDao<Medal, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return medalDao;
	}
	
	/**
	 * 获取勋章系统列表
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pagination<?> getPage(int rows,int page)
	{
		return this.medalDao.getPage(rows, page);
	}
	
	/**
	 * 返回指定勋章系统下的勋章列表
	 * @param medal_sys_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalListBySysId(String medal_sys_id)
	{
		return this.medalDao.getMedalListBySysId(medal_sys_id);
	}
	/**
	 * 更新实体
	 * @param medal
	 */
	public void MegeMedal(Medal medal)
	{
		this.medalDao.MegeMedal(medal);
	}
}
