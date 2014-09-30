/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:MedalDao.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-8
 */
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Medal;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.dao
 * 创建时间：2013-12-8
 */
@Repository
public class MedalDao extends HibernateBaseDaoImpl<Medal, String> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 获取勋章列表
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pagination<?> getPage(int rows,int page)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT medal_sys_id,medal_sys_name,add_date,add_man," +
				"(SELECT COUNT(medal_id) FROM tb_c_medal WHERE medal_type='0' AND medal_sys_id=tb_c_medal_sys.medal_sys_id) AS medal_count" +
				" FROM tb_c_medal_sys ORDER BY add_date DESC";
		String countSql = "SELECT COUNT(medal_sys_id) FROM tb_c_medal";
		int offset = (page - 1) * rows;
		rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql,countSql,offset,rows,map);
	}
	
	/**
	 * 返回指定勋章系统下的勋章列表
	 * @param medal_sys_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalListBySysId(String medal_sys_id)
	{
		String sql = "SELECT * FROM tb_c_medal WHERE medal_sys_id=? AND medal_type='0'" +
				" ORDER BY medal_level DESC";
		return this.jdbcTemplate.queryForList(sql,medal_sys_id);
	}
	
	/**
	 * 更新实体
	 * @param medal
	 */
	public void MegeMedal(Medal medal)
	{
		this.getHibernateTemplate().merge(medal);
	}
}
