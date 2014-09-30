// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Headship;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：HeadshipDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-16 上午10:33:48
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class PCHeadshipDao extends HibernateBaseDaoImpl<Headship, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 岗位级别列表.
	 * @param rows
	 * @param page
	 * @param companyId
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> list(int rows, int page, String companyId,String key){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT * FROM tb_c_headship sh WHERE sh.del_flag='0' AND sh.company_id='"+companyId+"' ";
		String countSql = "SELECT COUNT(*) FROM tb_c_headship sh WHERE sh.del_flag='0' AND sh.company_id='"+companyId+"' ";
		if(null !=key && !key.equals("")){
			rowSql +="and headship_name like :key ";
			countSql +="and headship_name like :key ";
			map.put("key", "%"+key+"%");
		}
		
		rowSql += "ORDER BY CAST(sh.headship_level AS SIGNED) ASC ";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	
	/**
	 * 获取指定岗位Id的岗位信息.
	 * @param headshipId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> headship(String headshipId){
		String sql = "SELECT " +
				"hs.headship_id AS headshipId," +
				"hs.headship_name AS headshipName," +
				"hs.headship_level AS headshipLevel," +
				"hs.description AS description " +
				"FROM tb_c_headship hs " +
				"WHERE hs.headship_id=?";
		return this.jdbcTemplate.queryForList(sql,headshipId);
	}
	/**
	 * 获取该职位是否被使用
	 * @param headshipId
	 * @return
	 */
	public Long getUseHeadshipCount(String headshipId){
		String sql= "select count(*) from tb_c_user_department where headship_id=? and visible_flag='1'";
		return this.jdbcTemplate.queryForLong(sql, headshipId);
	}

}
