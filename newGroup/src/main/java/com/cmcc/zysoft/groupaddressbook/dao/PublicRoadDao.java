// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.PublicRoad;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：PublicRoadDao
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class PublicRoadDao extends HibernateBaseDaoImpl<PublicRoad, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取某企业所有的公告
	 * @param rows 每页行数
	 * @param page 页数
	 * @param companyId 企业ID
	 * @return 
	 */
	public Pagination<?> getManagerList(int rows, int page,String companyId,String manager,String userId){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT pr.public_id,pr.public_name,pr.to_name,pr.status,GROUP_CONCAT(uc.employee_name) AS manager " +
				"FROM tb_c_public_road pr LEFT JOIN tb_c_public_user pu ON pr.public_id=pu.public_id  AND pu.relate_type='0' LEFT JOIN tb_c_user_company uc ON pu.user_company_id=uc.user_company_id AND uc.del_flag='0' " +
				"WHERE pr.company_id='"+companyId+"' " ;
				
		if(!"1".equals(manager)){
			rowSql +="and pr.public_id in (select DISTINCT public_id from tb_c_public_user x," +
					"tb_c_user_company y,tb_c_system_user z WHERE x.user_company_id=y.user_company_id " +
					"and y.employee_id=z.employee_id and y.del_flag='0' and z.user_id='"+userId+"' " +
					"and y.company_id='"+companyId+"' and x.relate_type='0') ";
		}
		rowSql +="GROUP BY pr.public_id,pr.public_name,pr.to_name";
		String countSql = "SELECT COUNT(*) FROM tb_c_public_road pr WHERE pr.company_id='"+companyId+"' ";
		if(!"1".equals(manager)){
			countSql="select count(1) from tb_c_public_user x," +
					"tb_c_user_company y,tb_c_system_user z WHERE x.user_company_id=y.user_company_id " +
					"and y.employee_id=z.employee_id and y.del_flag='0' and z.user_id='"+userId+"' " +
					"and y.company_id='"+companyId+"' and x.relate_type='0'";
		}
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	/**
	 * 变更状态
	 * @param publicId 公告Id
	 * @param status 状态：0：正常，1：暂停
	 */
	public void updateStatus(String publicId,String status){
		String sql = "UPDATE tb_c_public_road SET status='"+status+"' WHERE public_id='"+publicId+"'";
		jdbcTemplate.update(sql);
	}
	/**
	 * 删除公告以及与其相关联的信息
	 * @param flag:true:公告号以及其关联信息全部删除（删除操作）；false：删除public_user的信息（编辑操作）
	 * @param publicId 公告Id
	 */
	public void deleteRoad(boolean flag,String publicId){
		//删除与此公告相关联的信息
		String sql = "DELETE FROM tb_c_public_user WHERE public_id='"+publicId+"'";
		this.jdbcTemplate.update(sql);
		//删除公告
		if(flag){
			this.delete(publicId);
		}
	}
	
	/**
	 * 部门与人员树
	 * @param parentDeptId
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> deptUserTree(String parentDeptId,String companyId){
		String sql = "SELECT dept.department_name AS text, dept.department_id AS id," +
				"dept.parent_department_id AS parentId," +
				"'N' AS isLeaf,'closed' as state FROM tb_c_department dept " +
				"WHERE dept.parent_department_id=? " +
				"AND dept.del_flag='0' AND dept.company_id = ? " +
				"ORDER BY dept.display_order ASC";
		List<Map<String, Object>> listDept = this.jdbcTemplate.queryForList(sql,parentDeptId,companyId);
		sql="select uc.employee_name AS text,uc.user_company_id AS id," +
				"ud.department_id AS parentId," +
				"'Y' AS isLeaf,'open' as state from tb_c_user_company uc,tb_c_user_department ud " +
				"where uc.user_company_id=ud.user_company_id and uc.del_flag='0' " +
				"and ud.visible_flag='1' and ud.department_id=? " +
				"and uc.company_id=? ORDER BY uc.display_order ASC";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDeptId,companyId);
		list.addAll(listDept);
		return list;
	}
}
