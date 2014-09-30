/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:PlugDao.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-9-16
 */
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Plug;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.dao
 * 创建时间：2013-9-16
 */
@Repository
public class PlugDao extends HibernateBaseDaoImpl<Plug, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据用户编号获得插件列表
	 * @param usercode
	 * @return
	 */
	public List<Map<String,Object>> getMyPlugs(String usercode)
	{
		String sql = "SELECT DISTINCT plug.plug_name,plug.plug_icon,plug.plug_icon_hover,plug.plug_href," +
				"plug.plug_type,plug.display_order,psort.plug_sort_name " +
				"FROM tb_b_company_plug cplug,tb_b_plug plug,tb_b_plug_sort psort WHERE " +
				"cplug.plug_id=plug.plug_id AND plug.plug_sort_id=psort.plug_sort_id AND " +
				"cplug.company_id IN (SELECT company_id FROM tb_c_user_company WHERE employee_id=?)" +
				"AND psort.del_flag='0' AND plug.del_flag='0' ORDER BY plug.display_order DESC";
		return this.jdbcTemplate.queryForList(sql, usercode);
	}
}
