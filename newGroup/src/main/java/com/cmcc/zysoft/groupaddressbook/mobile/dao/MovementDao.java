package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.PCEmployeeDao;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MovementDao
 * <br />版本:1.0.0
 * <br />日期： 2013-8-16 上午10:30:19
 * <br />CopyRight © Chinamobile Anhui Ltd Cmp
 */
@Repository
public class MovementDao extends HibernateBaseDaoImpl<Movement, String> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	
	/**
	 * 根据用户编号，获取用户参与的活动列表
	 * @param userCode：employee_id
	 * @return
	 */
	public List<Map<String,Object>> getMovementListByUser(String userCode)
	{
		String sql = "SELECT movement_id,movement_name,movement_start_date,movement_end_date,movement_status," +
				"company_id,order_type,IFNULL(movement_picture,'') AS movement_picture," +
				"movement_flag,parent_movement_id,medal_sys_id,department_id FROM tb_b_movement WHERE now()>=movement_start_date AND " +
				" now()<=movement_end_date AND movement_status='1' AND movement_id IN " +
				"(SELECT movement_id FROM tb_b_movement_users WHERE employee_id=?) limit 0,1";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, userCode);
		if(list.size()>0)
		{
			return list;
		}
		else
		{
			sql = "SELECT * FROM tb_b_movement WHERE movement_id='0000' limit 0,1";
			return this.jdbcTemplate.queryForList(sql);
		}
	}
	
	/**
	 * 返回当前用户所在公司的活动列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Movement> getMovementListByLoginedUser(String pDepartmentId)
	{
		User user = SecurityContextUtil.getCurrentUser();
		String hql = "FROM Movement ";
		if(!"s_admin".equals(user.getUsername()))
		{
			if("4".equals(user.getRoles().get(0).getRoleId()))
			{
				hql += " WHERE department_id='"+pDepartmentId+"' ";
			}else{
				hql += " WHERE companyId='" + user.getCompanyId() + "'";
			}
		}
		hql += " ORDER BY movementStartDate DESC";
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 获取所有活动（分页）
	 * @param rows：分页大小
	 * @param page：当前页码
	 * @return
	 */
	public Pagination<?> getMovementPage(int rows,int page, String pDepartmentId)
	{
		User user = SecurityContextUtil.getCurrentUser();
		String where = "s_admin".equals(user.getUsername())?"":" WHERE company_id='"+user.getCompanyId()+"' ";
		
		String filter = "";
		//如果是部门管理员
		if("4".equals(user.getRoles().get(0).getRoleId()))
		{
			filter = " department_id='" + pDepartmentId + "' ";
			where = " WHERE " + filter;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT * FROM tb_b_movement ";
		rowSql += where;
		rowSql += "ORDER BY movement_start_date DESC";
		String countSql = "SELECT COUNT(movement_id) FROM tb_b_movement";
		countSql += where;
		int offset = (page - 1) * rows;
		rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql,countSql,offset,rows,map);
	}
	
	/**
	 * 禁用 or 启用 活动
	 * @param movement_id
	 */
	public void movementConfig(String movement_id)
	{
		String sql = "UPDATE tb_b_movement SET movement_status=IF(movement_status='1','2','1') WHERE " +
				"movement_id=?";
		this.jdbcTemplate.update(sql,movement_id);
	}
	
	/**
	 * 获取所属赛季
	 * @return
	 */
	public List<Map<String,Object>> getParentMovement()
	{
		String sql = "SELECT movement_id,movement_name FROM tb_b_movement WHERE " +
				"movement_status='1' AND movement_start_date<=now() AND movement_end_date>=now()" +
				" AND movement_flag='1'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取勋章系统列表
	 * @return
	 */
	public List<Map<String,Object>> getMedalSystem()
	{
		String sql = "SELECT medal_sys_id,medal_sys_name FROM tb_c_medal_sys";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	//public String addMovement(String userCode,String start)
}
