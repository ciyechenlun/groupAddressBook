package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Headship;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.support.Pagination;

/**
 * HeadshipDao.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-2 下午6:07:38
 *
 */
@Repository
public class HeadshipDao extends HibernateBaseDaoImpl<Headship,String> {
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取所有岗位信息List
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Headship> getHeadshipList() {
		String hql = "";
		hql = " from Headship r where del_flag='0' order by r.headshipId desc ";

		return this.findByHQL(hql);
	}

	/**
	 * 获取所有岗位信息:分页
	 * 
	 * @return
	 */
	public Pagination<Object> getAllHeadships(int page, int rows,String idIcon,String isAdmin) {
		String[] idIconCls;
		String rowSql = "";
		String countSql = "";
		
		//String rowSql = " from Headship r where del_flag='0' order by r.headshipId desc  ";
		//String countSql = "select count(*) from Headship h where h.delFlag='0'   ";
			if(StringUtils.hasText(idIcon)){
				idIconCls = idIcon.split("_");
				if("department".equals(idIconCls[1])){
					//点击部门
					rowSql = " select new Map(" +
							" h.headshipId as headshipId," +
							" h.companyId as companyId," +
							" h.headshipName as headshipName," +
							" h.description as description," +
							" h.headshipLevel as headshipLevel," +
							" h.createTime as createTime," +
							" h.createMan as createMan," +
							" h.modifyTime as modifyTime," +
							" h.modifyMan as modifyMan," +
							" h.status as status," +
							" h.delFlag as delFlag," +
							" c.companyName as companyName," +
							" d.dataContent as dataContent )" + 
							" from Headship h ,Company c ,Department d,DictData d,DictType t " +
							" where " +
							" h.delFlag='0' and " +
							" h.companyId = c.companyId and " +
							" h.headshipLevel = d.dataCode and " +
							" t.typeCode = 'headship_level' and " +
							" d.dictType.typeId=t.typeId and " +
							" c.companyId = '" + idIconCls[0] +"'";
					countSql =  "select count(*) from Headship h ,Company c ,Department d,DictData d,DictType t " +
							" where " +
							" h.delFlag='0' and " +
							" h.companyId = c.companyId and " +
							" h.headshipLevel = d.dataCode and " +
							" t.typeCode = 'headship_level' and " +
							" d.dictType.typeId=t.typeId and " +
							" c.companyId = '" + idIconCls[0] +"'";
				}else{
						//点击公司
					rowSql = "select new Map(" +
						" h.headshipId as headshipId," +
						" h.companyId as companyId," +
						" h.headshipName as headshipName," +
						" h.description as description," +
						" h.headshipLevel as headshipLevel," +
						" h.createTime as createTime," +
						" h.createMan as createMan," +
						" h.modifyTime as modifyTime," +
						" h.modifyMan as modifyMan," +
						" h.status as status," +
						" h.delFlag as delFlag," +
						" c.companyName as companyName," +
						" d.dataContent as dataContent )" +
						" from Headship h,Company c,DictData d,DictType t " +
						" where " +
						" h.delFlag='0' and " +
						" h.companyId = c.companyId and " +
						" h.headshipLevel = d.dataCode and " +
						" t.typeCode = 'headship_level' and " +
						" d.dictType.typeId=t.typeId and " +
						" h.companyId = '" + idIconCls[0] + "'";
					countSql = "select count(*) from Headship h,Company c,DictData d,DictType t " +
						" where " +
						" h.delFlag='0' and " +
						" h.companyId = c.companyId and " +
						" h.headshipLevel = d.dataCode and " +
						" t.typeCode = 'headship_level' and " +
						" d.dictType.typeId=t.typeId and " +
						" h.companyId = '" + idIconCls[0] + "'";
					}
				}else{
					rowSql =" select new Map(" +
							" h.headshipId as headshipId," +
							" h.companyId as companyId," +
							" h.headshipName as headshipName," +
							" h.description as description," +
							" h.headshipLevel as headshipLevel," +
							" h.createTime as createTime," +
							" h.createMan as createMan," +
							" h.modifyTime as modifyTime," +
							" h.modifyMan as modifyMan," +
							" h.status as status," +
							" h.delFlag as delFlag," +
							" c.companyName as companyName," +
							" d.dataContent as dataContent )" + 
							" from Headship h,Company c,DictData d,DictType t " +
							" where h.delFlag='0' and" +
							" h.companyId = c.companyId and" +
							" h.headshipLevel = d.dataCode and " +
							" t.typeCode = 'headship_level' and " +
							" d.dictType.typeId=t.typeId ";
					countSql = "select count(*) from Headship h,Company c,DictData d,DictType t " +
							" where h.delFlag='0' and" +
							" h.companyId = c.companyId and" +
							" h.headshipLevel = d.dataCode and " +
							" t.typeCode = 'headship_level' and " +
							" d.dictType.typeId=t.typeId ";
				}
			
			rowSql += " order by h.headshipId desc ";
			countSql += " order by h.headshipId desc ";
		
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 根据查询条件啊，获取岗位信息列表
	 * 
	 * @param headshipName 岗位名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getHeadshipsByCondition(String headshipName, int page, int rows) {
		String rowSql = "";
		String countSql = "";
		if(headshipName.trim().length()==0){
			//rowSql = " from Headship r where del_flag='0' order by r.headshipId desc  ";
			//countSql = "select count(*) from Headship r where del_flag='0' order by r.headshipId desc  ";
			rowSql = " select new Map(" +
					" h.headshipId as headshipId," +
					" h.companyId as companyId," +
					" h.headshipName as headshipName," +
					" h.description as description," +
					" h.headshipLevel as headshipLevel," +
					" h.createTime as createTime," +
					" h.createMan as createMan," +
					" h.modifyTime as modifyTime," +
					" h.modifyMan as modifyMan," +
					" h.status as status," +
					" h.delFlag as delFlag," +
					" c.companyName as companyName," +
					" d.dataContent as dataContent )" + 
					" from Headship h,Company c,DictData d,DictType t " +
					" where h.delFlag='0' and" +
					" h.companyId = c.companyId and" +
					" h.headshipLevel = d.dataCode and " +
					" t.typeCode = 'headship_level' and " +
					" d.dictType.typeId=t.typeId " +
					" order by h.headshipId desc  ";
			countSql = "select count(*) from Headship h,Company c " +
					" where h.delFlag='0' and" +
					" h.companyId = c.companyId " +
					" order by h.headshipId desc  ";
		}else{
			rowSql = " select new Map(" +
					" h.headshipId as headshipId," +
					" h.companyId as companyId," +
					" h.headshipName as headshipName," +
					" h.description as description," +
					" h.headshipLevel as headshipLevel," +
					" h.createTime as createTime," +
					" h.createMan as createMan," +
					" h.modifyTime as modifyTime," +
					" h.modifyMan as modifyMan," +
					" h.status as status," +
					" h.delFlag as delFlag," +
					" c.companyName as companyName )" + 
					" from Headship h,Company c " + 
					" where " +
					" h.delFlag='0' and " +
					" h.companyId = c.companyId and " +
					" h.headshipName like '%"+ headshipName.trim() +"%' " +
					" order by h.headshipId desc ";
			countSql = " select count(*) from Headship h,Company c where h.delFlag='0' and h.companyId = c.companyId and h.headshipName like '%"+ headshipName.trim() +"%'  order by h.headshipId desc ";
		}
		
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 修改岗位信息
	 * 
	 * @param headship 岗位对象
	 */
	public void saveHeadship(Headship headship) {
		this.getHibernateTemplate().merge(headship);
	}
	
	/**
	 * 根据角色id获取岗位信息
	 * 
	 * @param headshipId 岗位信息id
	 * @return
	 */
	public Headship getHeadship(String headshipId) {
		Headship headship = this.get(headshipId);
		return headship;
	}
	
	/**
	 * 修改岗位信息
	 * 
	 * @param headship 岗位对象
	 */
	public void updateHeadship(Headship headship) {
		this.getHibernateTemplate().merge(headship);
	}
	
	/**
	 * 删除岗位信息：逻辑删除
	 * 
	 * @param headshipId 岗位信息id
	 */
	public void deleteHeadship(String headshipId) {
		//String sql = " delete from tb_c_headship where headship_id = '" + headshipId + "'";
		String  sql = " update tb_c_headship set del_flag='1' where headship_id = ?";
		jdbcTemplate.update(sql,headshipId);
	}
	
	/**
	 * 根据公司id获取岗位下拉框
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> headshipCombo(String companyId) {
		String hql = "select new Map(h.headshipId as id,h.headshipName as text) from Headship h where h.companyId = ?";
		return this.findByHQL(hql,companyId);
	}
}
