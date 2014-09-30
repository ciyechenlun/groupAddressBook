// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.DictType;
import com.cmcc.zysoft.sysmanage.dao.DictDataDao;
import com.cmcc.zysoft.sysmanage.dao.DictTypeDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author yandou
 */
/**
 * DictTypeService.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-13 下午11:10:03
 *
 */
@Service
public class DictTypeService extends BaseServiceImpl<DictType, String> {
	
	@Resource
	private DictTypeDao dictTypeDao;
	
	@Resource
	private DictDataDao dictDataDao;

	@Override
	public HibernateBaseDao<DictType, String> getHibernateBaseDao() {
		return dictTypeDao;
	}

	public Map<String,Object> getDictTypes(int page, int rows) {
		return this.dictTypeDao.getDictTypes(page,rows);
	}

	/**
	 * 保存数据字典类型
	 * @param configType
	 */
	public void saveConfigType(DictType dictType) {
		dictType.setStatus("0");
		String typeId = dictType.getTypeId();
		if(StringUtils.hasText(typeId)){
			this.dictTypeDao.saveOrUpdate(dictType);
		}else{
			this.dictTypeDao.save(dictType);
		}
	}
	
	/**
	 * 保存数据字典类型
	 * 
	 * @param dictType
	 * @return 返回类型：String
	 */
	public String saveDictType(DictType dictType){
		return  this.dictTypeDao.saveDictType(dictType);
	}

	public List<DictType> dictTypesForCombo() {
		return this.dictTypeDao.dictTypesForCombo();
	}

	/**
	 * 删除数据字典类型，并把对应的数据删掉
	 * @param typeId
	 */
	public void deleteConfigTypeById(String typeId) {
		this.deleteEntity(typeId);
		this.dictDataDao.deletDictDataByTypeId(typeId);
	}
	
	/**
	 * 获取数据字典下拉框
	 * 
	 * @param typeCode 字典类型编码
	 * @return
	 */
	public List<Map<String, Object>> getDataComo(String typeCode) {
		return this.dictDataDao.getDataComo(typeCode);
	}
}
