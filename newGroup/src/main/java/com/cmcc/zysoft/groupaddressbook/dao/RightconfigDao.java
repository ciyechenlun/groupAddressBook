// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Rightconfig;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：RightconfigDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-18 上午11:46:51
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class RightconfigDao extends HibernateBaseDaoImpl<Rightconfig, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 权限列表.
	 * @param type 权限类型.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> rightConfig(String type){
		String sql = "SELECT " +
				"rc.rightconfig_id AS configId ," +
				"rc.rightconfig_name AS configName," +
				"rc.rightconfig_checked AS rightCheck  " +
				"FROM tb_c_rightconfig rc WHERE rc.rightconfig_type=?";
		return this.jdbcTemplate.queryForList(sql,type);
	}
	
	/**
	 * 保存选择的权限,选中的设为1,未选中的设为0.
	 * @param rightId 
	 * 返回类型：void
	 */
	public void check(String rightId){
		String sql = "UPDATE tb_c_rightconfig ";
		if(StringUtils.hasText(rightId)){
			String sqlSet = sql + "SET rightconfig_checked = '1' WHERE rightconfig_id IN("+rightId+")";
			String sqlCancel = sql + "SET rightconfig_checked = '0' WHERE rightconfig_id NOT IN("+rightId+")";
			this.jdbcTemplate.execute(sqlSet);
			this.jdbcTemplate.execute(sqlCancel);
		}else{
			sql += "SET rightconfig_checked = '0'";
			this.jdbcTemplate.execute(sql);
		}
	}
	
	/**
	 * 获取选中的权限.
	 * @param type 
	 * @return 
	 * 返回类型：String
	 */
	public String rights(String type){
		String sql = "SELECT cf.level AS rightLevel " +
				"FROM tb_c_rightconfig cf WHERE cf.rightconfig_type=?" +
				" AND cf.rightconfig_checked='1' ORDER BY cf.level desc";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,type);
		if(list.size()>0){
			return list.get(0).get("rightLevel").toString();
		}else{
			return "0";
		}
	}
	
	/**
	 * 获取当前登陆人所在公司的权限级别.
	 * @param companyId 
	 * @return 
	 * 返回类型：int
	 */
	public int rightconfigLevel(String companyId){
		String sql = "SELECT rc.level as rightLevel FROM tb_c_rightconfig rc " +
				"WHERE rc.company_id=? " +
				"GROUP BY rc.level " +
				"ORDER BY rc.level desc";
		List<Map<String, Object>> rightLevel = this.jdbcTemplate.queryForList(sql,companyId);
		if(rightLevel.size()>0){
			return Integer.parseInt(rightLevel.get(0).get("rightLevel").toString());
		}else{
			return 0;
		}
	}
	
	/**
	 * 判断当前公司是否有平级权限.
	 * @param companyId 
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkSelf(String companyId){
		String sql = "SELECT * FROM tb_c_rightconfig rc WHERE rc.level='0' AND rc.company_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,companyId);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
