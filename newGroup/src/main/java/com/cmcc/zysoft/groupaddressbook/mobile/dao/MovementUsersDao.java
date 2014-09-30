package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.sellmanager.model.MovementUsers;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MovementUsersDao
 * <br />版本:1.0.0
 * <br />日期： 2013-8-16 上午10:35:29
 * <br />CopyRight © Chinamobile Anhui Ltd Cmp
 */

@Repository
public class MovementUsersDao extends HibernateBaseDaoImpl<MovementUsers, String> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 根据活动编号返回活动下用户列表
	 * @param movement_id：活动编号
	 * @param rows：分页大小
	 * @param page：页码
	 * @param keyword 搜索关键司，可以为空
	 * @return
	 */
	public Pagination<Map<String, Object>> getUserListByMovementId(String movement_id,int rows,int page, String keyword)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT emp.employee_id,emp.employee_name,dept.department_name,muser.movement_id FROM tb_c_employee emp,tb_b_movement_users muser,tb_c_department dept WHERE " +
				"muser.employee_id=emp.employee_id AND dept.department_id=emp.department_id AND muser.movement_id='"+movement_id+"'";
		String countSql = "SELECT COUNT(muser.employee_id) FROM tb_b_movement_users muser,tb_c_employee emp WHERE " +
				"emp.employee_id=muser.employee_id AND movement_id='"+movement_id+"'";
		if(StringUtils.hasText(keyword))
		{
			rowSql += " AND (emp.employee_name like '%"+keyword+"%' or emp.mobile like '"+keyword+"%') ";
			countSql += " AND (emp.employee_name like '%"+keyword+"%' or emp.mobile like '"+keyword+"%') ";
		}
		int offset = (page - 1) * rows;
		rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql,countSql,offset,rows,map);
	}
	
	/**
	 * 根据活动编号返回活动下用户列表（不分页）
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getUserListByMovementId(String movement_id)
	{
		String rowSql = "SELECT emp.employee_id,emp.employee_name,dept.department_name,muser.movement_id FROM tb_c_employee emp,tb_b_movement_users muser,tb_c_department dept WHERE " +
				"muser.employee_id=emp.employee_id AND dept.department_id=emp.department_id AND muser.movement_id='"+movement_id+"'";
		return this.jdbcTemplate.queryForList(rowSql);
	}
	
	/**
	 * 删除活动下指定的用户
	 * @param movement_id
	 * @param employee_id
	 */
	public void deleteMovementUserByMovementIdAndEmployeeId(String movement_id,String employee_id)
	{
		String sql = "DELETE FROM tb_b_movement_users WHERE movement_id=? AND " +
				"employee_id=?";
		this.jdbcTemplate.update(sql,movement_id,employee_id);
	}
	
	/**
	 * 添加活动下用户
	 * @param users
	 * @param movement
	 */
	@Transactional
	public void addUsers(String user,Movement movement)
	{
		//1.清除
		//String sql = "DELETE FROM tb_b_movement_users WHERE movement_id='" + movement.getMovementId() + "'";
		//this.jdbcTemplate.execute(sql);
		//2.添加
		//(1) 得到用户编号 姓名
		String[] users = user.split("[,]");
		for(String u : users)
		{
			if(StringUtils.hasText(u)){
				//(2)得到用户employee_id
				String[] userInfos = u.split("[ ]");
				if(userInfos.length>0)
				{
					//先删除后添加
					this.jdbcTemplate.execute("delete from tb_b_movement_users where employee_id='"+userInfos[0]+"' and movement_id in (" +
							"select movement_id from tb_b_movement where movement_status='1')");
					addUser(userInfos,movement);
					
				}
			}
			
		}
	}
	
	private void addUser(String[] userInfos,Movement movement)
	{
		MovementUsers musers = new MovementUsers();
		musers.setEmployeeId(userInfos[0]);
		musers.setMovement(movement);
		this.save(musers);
	}
}
