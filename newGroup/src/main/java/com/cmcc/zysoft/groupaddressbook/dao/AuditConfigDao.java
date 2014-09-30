// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.AuditConfig;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：AuditConfigDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午10:21:38
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class AuditConfigDao extends HibernateBaseDaoImpl<AuditConfig, String>{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 判断是否需要审核.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> list(){
		String sql = "SELECT * FROM tb_c_audit_config ac WHERE ac.audit_flag='1' AND ac.modify_flag='0'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 审核配置表里的记录数.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> checkList(){
		String sql = "SELECT * FROM tb_c_audit_config ac";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 修改审核配置表数据.
	 * @param audit
	 * @param modify 
	 * 返回类型：void
	 */
	public void updateAudit(String audit,String modify){
		String sql = "UPDATE tb_c_audit_config SET audit_flag = ?,modify_flag = ?";
		this.jdbcTemplate.update(sql,audit,modify);
	}

}
