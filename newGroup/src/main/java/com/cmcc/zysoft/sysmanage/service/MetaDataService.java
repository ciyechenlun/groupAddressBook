// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.MetaData;
import com.cmcc.zysoft.sysmanage.dao.MetaDataDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author yandou
 */
@Service
public class MetaDataService extends BaseServiceImpl<MetaData, String> {
	
	@Resource
	private MetaDataDao metaDataDao;

	@Override
	public HibernateBaseDao<MetaData, String> getHibernateBaseDao() {
		return metaDataDao;
	}

	public Pagination<Object> getAllMetaDatas(String typeId, int page, int rows) {
		return this.metaDataDao.getAllMetaDatas(typeId,page,rows);
	}

	/**
	 * 根据metaIds批量删除元数据
	 * @param metaIds
	 */
	public void deletemetaByIds(String metaIds) {
		String[] metaIdArr = metaIds.split(",");
		for(String metaId : metaIdArr){
			this.deleteEntity(metaId);
		}
	}
	/**
	 * 保存元数据
	 * @param metaData
	 */
	public void saveMetaData(MetaData metaData) {
		if(StringUtils.hasText(metaData.getMetaId())){
			this.metaDataDao.saveOrUpdate(metaData);
		}else{
			this.metaDataDao.save(metaData);
		}
	}
}
