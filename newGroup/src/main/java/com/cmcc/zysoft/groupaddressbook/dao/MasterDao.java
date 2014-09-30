package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Master;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MasterDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-23 上午10:09:16
 * <br />CopyRight © Chinamobile Anhui cmp Ltd.
 */
@Repository
public class MasterDao extends HibernateBaseDaoImpl<Master, String> {
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 分页获取反馈建议列表.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @param key 关键字，暂未处理
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> masterList(int rows, int page, String companyId,String key){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT " +
		"master_id,company_id,master_name,master_type,taxis " + 
		" FROM tb_b_master WHERE company_id='"+companyId+"' ";
		String countSql = "SELECT COUNT(master_id) " +
				"FROM tb_b_master " +
				"WHERE company_id='"+companyId+"' ORDER BY taxis ";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	
	/**
	 * 删除角色
	 * @param master_id
	 */
	public void master_delete(String master_id)
	{
		//删除角色规则
		String sql = "DELETE FROM tb_b_master_rules where master_id=?";
		this.jdbcTemplate.update(sql,master_id);
		
		//删除用户角色
		sql = "DELETE FROM tb_b_user_master WHERE master_id=?";
		this.jdbcTemplate.update(sql,master_id);
		
		//删除角色
		sql = "DELETE FROM tb_b_master WHERE master_id=?";
		this.jdbcTemplate.update(sql,master_id);
	}
}
