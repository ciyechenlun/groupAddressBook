// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.sellmanager.model.DictType;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author yandou
 */
@Repository
public class DictTypeDao extends HibernateBaseDaoImpl<DictType, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 分页获取数据字典类型列表
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String,Object> getDictTypes(int page, int rows) {
		int offset = (page-1)*rows;
		String rowSql = "select dd.*,cc.company_name from tb_c_dict_type dd left join tb_c_company cc on dd.company_id = cc.company_id limit ?,?";
		String countSql = "select count(*) from tb_c_dict_type";
		List<Map<String,Object>> result = this.jdbcTemplate.queryForList(rowSql,offset,rows);
		int count = this.jdbcTemplate.queryForInt(countSql);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", result);
		map.put("total", count);
		return map;
	}
	/**
	 * combobox 数据字典数据添加所需数据字典类型下拉框
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DictType> dictTypesForCombo() {
		String hql = "from DictType";
		return this.findByHQL(hql);
	}
	
	/**
	 * 保存数据字典类型
	 * 
	 * @param dictType
	 * @return 返回类型：String
	 */
	@Transactional
	public String saveDictType(DictType dictType){
		return this.save(dictType);
	}
}
