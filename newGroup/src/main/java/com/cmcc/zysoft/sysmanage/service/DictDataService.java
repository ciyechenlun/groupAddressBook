// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.DictData;
import com.cmcc.zysoft.sysmanage.dao.DictDataDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author yandou
 */
@Service
public class DictDataService extends BaseServiceImpl<DictData, String> {
	
	@Resource
	private DictDataDao dictDataDao;

	@Override
	public HibernateBaseDao<DictData, String> getHibernateBaseDao() {
		return dictDataDao;
	}

	public Map<String,Object> getDictDatas(String typeId, int page, int rows) {
		return this.dictDataDao.getDictDatas(typeId,page,rows);
	}

	/**
	 * 根据dataIds批量数据字典数据
	 * @param dataIds
	 */
	public void deleteDictDataByIds(String dataIds) {
		String[] dataIdArr = dataIds.split(",");
		for(String dataId : dataIdArr){
			this.deleteEntity(dataId);
		}
	}
	/**
	 * 数据字典数据
	 * @param dictData
	 */
	public void saveDictData(DictData dictData) {
		if(StringUtils.hasText(dictData.getDataId())){
			this.dictDataDao.saveOrUpdate(dictData);
		}else{
			this.dictDataDao.save(dictData);
		}
	}
	
	/**
	 * 保存数据字典数据
	 * @param dictData
	 */
	public String addDictData(DictData dictData) {
		return this.dictDataDao.saveDictData(dictData);
	}
}
