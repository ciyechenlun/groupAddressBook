/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
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

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午3:43:57
 */

@Repository
public class MMedalDao extends HibernateBaseDaoImpl<Medal, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取用户的勋章
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getEmpMedals(String userCode, String token, String movement_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT medal.*" +
				" FROM tb_c_medal medal LEFT JOIN" +
				" tb_c_employee_medal empMedal ON medal.medal_id = empMedal.medal_id LEFT JOIN" +
				" tb_c_employee emp ON empMedal.employee_id = emp.employee_id" +
				" WHERE emp.employee_id = ? AND movement_id=?" +
				" ORDER BY medal.medal_level ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, userCode, movement_id);
		map.put("cmd", "getmedal");
		try {
			map.put("code", "0");
			map.put("value", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "1");
		}
		return map;
	}
	
	/**
	 * 获取某个活动下的勋章
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalByMovement(String movement_id)
	{
		String sql = "SELECT medal_id,medal_name,medal_picture,medal_level,medal_sys_id,medal_type " +
				"FROM tb_c_medal WHERE medal_sys_id=(SELECT medal_sys_id FROM tb_b_movement WHERE movement_id=?)" +
				" AND medal_type='0' ORDER BY medal_level ASC";
		return this.jdbcTemplate.queryForList(sql,movement_id);
	}
	
	/**
	 * 根据活动ID返回勋章系统
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getMedalSysByMovement(String movement_id)
	{
		String sql = "SELECT medal_sys_id,medal_sys_name,IFNULL(mark1,'') AS medal_picture_url FROM tb_c_medal_sys WHERE " +
				"medal_sys_id=(SELECT medal_sys_id FROM tb_b_movement WHERE " +
				"movement_id=?)";
		return this.jdbcTemplate.queryForList(sql,movement_id);
	}
}
