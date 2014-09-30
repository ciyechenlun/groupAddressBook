package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.CompanyMap;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;
/**
 * @author 李雨辰	
 * <br />邮箱： li.yuchen@ustcinfo.com
 * <br />描述：CompanyMapDao
 * <br />版本:1.0.0
 * <br />日期： 2014-08-04 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Repository
public class CompanyMapDao extends HibernateBaseDaoImpl<CompanyMap, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Pagination<?> getMapInfo(int rows, int page){
		Map<String, Object> map = new HashMap<String,Object>();
		String sql = "SELECT MAX(tmp.version) AS version,tmp.company_name, date_format(tmp.update_time,'%Y-%m-%d %H:%i:%s') AS updateDate " +
						"FROM (SELECT map.update_time,c.company_name, map.version,map.company_id " +
						"FROM tb_c_map map ,tb_c_company c " +
						"WHERE map.company_id=c.company_id " +
						"ORDER BY map.update_time DESC) AS tmp " +
					"GROUP BY tmp.company_id " +
					"ORDER BY tmp.update_time DESC";
		String countSql = "SELECT COUNT(*) " +
				"FROM (SELECT company_id FROM tb_c_map map Group by company_id) AS tmp" ;
		int offset = (page - 1) * rows;
		sql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
	}
	
	/**
	 * 按企业获取地图信息
	 * @param companyId
	 * @return
	 */
	public String getMayByCompany(String companyId){
		String sql = "SELECT map_id FROM tb_c_map WHERE company_id=? order by version";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,companyId);
		if(list!=null&&list.size()>0){
			return String.valueOf(list.get(0).get("map_id"));
		}else{
			return null;
		}
	}
	
	/**
	 * 获取最大的版本号
	 * @return
	 */
	public Integer getMaxNum(){
		String sql = "SELECT MAX(version) AS version FROM tb_c_map";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		if(list!=null&&list.size()>0){
			return Integer.valueOf(list.get(0).get("version").toString());
		}else{
			return 0;
		}
	}

}
