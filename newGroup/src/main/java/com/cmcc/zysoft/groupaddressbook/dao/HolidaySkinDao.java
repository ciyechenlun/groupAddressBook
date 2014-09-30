package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Holidayskin;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：HolidaySkinDao
 * <br />版本:1.0.0
 * <br />日期： 2013-7-23 上午8:49:16
 * <br />CopyRight © Chinamobile Anhui cmp Ltd.
 */

@Repository
public class HolidaySkinDao extends HibernateBaseDaoImpl<Holidayskin, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 获取节假日皮肤分页列表.
	 * @param rows
	 * @param page
	 * @return pagination
	 */
	public Pagination<?> getSkinList(int rows, int page) {
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT skin.holidayskin_id AS holidayskinId, " +
				" skin.holidayskin_name AS holidayskinName, " +
				" skin.holiday_name AS holidayName, " +
				" skin.holidayskin_start_date AS holidayskinStartDate, " +
				" skin.holidayskin_end_date AS holidayskinEndDate, " +
				" skin.holidayskin_path AS holidayskinPath, " +
				" skin.holidayskin_status AS holidayskinStatus, " +
				" skin.holidayskin_repeat AS holidayskinRepeat, " +
				" skin.mark1 AS mark1, " +
				" skin.mark2 AS mark2 " +
				" FROM tb_c_holidayskin skin " +
				" WHERE skin.holidayskin_status = '1'";
		String countSql = "SELECT COUNT(*) FROM tb_c_holidayskin skin WHERE skin.holidayskin_status = '1'";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	
	/**
	 * 获取节假日皮肤.
	 * @param skinId
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Holidayskin> getHolidaySkin(String skinId) {
		String hql = "from Holidayskin skin where skin.holidayskinStatus = '1'";
		if(null != skinId) {
			hql += " and skin.holidayskinId != '" + skinId + "'";
		}
		return this.findByHQL(hql);
	}
	
	/**
	 * 通过皮肤ID查找节假日皮肤.
	 * @param skinId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getSkinById(String skinId) {
		String sql = "select skin.* from tb_c_holidayskin skin where skin.holidayskin_id = '" + skinId + "'";
		return this.jdbcTemplate.queryForList(sql);
	}
}