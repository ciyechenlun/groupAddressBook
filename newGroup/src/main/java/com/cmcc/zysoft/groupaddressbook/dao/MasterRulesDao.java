package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.MasterRules;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MasterRulesDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-24 上午10:09:16
 * <br />CopyRight © Chinamobile Anhui cmp Ltd.
 */
@Repository
public class MasterRulesDao extends HibernateBaseDaoImpl<MasterRules, String> {
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取角色下权限集合
	 * @param rows
	 * @param page
	 * @param masterId
	 * @param key 关键词，可以为空
	 * @return
	 */
	public Pagination<?> masterRulesList(int rows, int page, String masterId,String key)
	{
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT " +
				"rules_id,master_id,rules_content,rules_sql,mark1 FROM tb_b_master_rules " +
				"WHERE master_id='"+masterId+"'";
		String countSql = "SELECT count(rules_id) FROM tb_b_master_rules WHERE " +
				"master_id='"+masterId+"'";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	
	/**
	 * 删除角色条件明细
	 * @param rules_id 条件编号
	 * @return  删除语无返回值，直接返回true
	 */
	public boolean deleteMasterRules(String rules_id)
	{
		String deleteSql = "delete from tb_b_master_rules where rules_id=?";
		this.jdbcTemplate.update(deleteSql,rules_id);
		return true;
	}
	
	/**
	 * 添加规则明细
	 * @param master_id：角色ID
	 * @param selReleation：与前一个条件的关系
	 * @param content：规则内容
	 */
	public void addMasterRules(String master_id,String selReleation,String content)
	{
		String sql ="insert into tb_b_master_rules (rules_id,master_id,rules_content,mark1) values (replace(uuid(),'-',''),?,?,?)";
		this.jdbcTemplate.update(sql, master_id,content,selReleation);
	}
	/**
	 * 获取企业下的权限设置（唯一）
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> getMasterRule(String companyId)
	{
		String rowSql = "select mr.rules_id,m.master_id,mr.rules_content,mr.rules_sql,mr.mark1 "+
				" FROM tb_b_master_rules mr right join tb_b_master m on mr.master_id=m.master_id where m.company_id=? limit 1";
    	List<Map<String,Object>> list =  this.jdbcTemplate.queryForList(rowSql, companyId);
    	if(null != list && list.size()>0){
    		return list.get(0);
    	}
    	return null;
	}
	/**
	 * 添加规则详细配置
	 * @param master_id
	 * @param rules_sql
	 */
	public void addMasterRule(String rules_id,String master_id,String rules_sql)
	{
		String sql ="";
		if(null == rules_id || rules_id.equals("")){
			sql ="insert into tb_b_master_rules (rules_id,master_id,rules_sql) values (replace(uuid(),'-',''),?,?)";
			this.jdbcTemplate.update(sql, master_id,rules_sql);
		}else{
			sql ="update tb_b_master_rules set rules_sql =? where rules_id=?";
			this.jdbcTemplate.update(sql, rules_sql,rules_id);
		}
		
	}
}
