// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.sellmanager.model.DictData;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author yandou
 */
@Repository
public class DictDataDao extends HibernateBaseDaoImpl<DictData, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 数据字典数据类型
	 * @param typeId
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getDictDatas(String typeId, int page, int rows) {
		int offset = (page - 1) * rows;
		String rowSql = "select dd.* from `tb_c_dict_data` dd where dd.type_id = ? limit ?,?";
		String countSql = "select count(*) from tb_c_dict_data dd where dd.type_id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(rowSql, typeId, offset, rows);
		int total = this.jdbcTemplate.queryForInt(countSql,typeId);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		map.put("total", total);
		return map;
	}

	/**
	 * 删除数据字典类型，并把对应的数据删掉
	 * @param typeId
	 */
	
	@SuppressWarnings("unchecked")
	public void deletDictDataByTypeId(String typeId) {
		String hql = "from DictData d where d.dictType.typeId = ?";
		List<DictData> list = this.findByHQL(hql,typeId);
		for(DictData dictData : list){
			this.delete(dictData);
		}
	}
	
	/**
	 * 获取数据字典下拉框
	 * 
	 * @param typeCode 字典类型编码
	 * @return
	 */
	public List<Map<String, Object>> getDataComo(String typeCode) {
		String sql = "SELECT d.data_Code as dataCode,d.data_Content as dataContent "
				+ "FROM tb_c_dict_data d ,tb_c_dict_type t "
				+ "WHERE d.type_id=t.type_id AND "
				+ "t.type_code= '"
				+ typeCode + "'";
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 保存数据字典数据
	 * @param dictData
	 */
	@Transactional
	public String saveDictData(DictData dictData) {
		return this.save(dictData);
	}
}
