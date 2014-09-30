// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author yandou
 */
@Repository
public class SystemUserPCDao extends HibernateBaseDaoImpl<SystemUser, String> {
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存系统用户
	 * 
	 * @param systemUser
	 * @return 返回类型：String
	 */
	@Transactional
	public String saveSystemUser(SystemUser systemUser){
		return this.save(systemUser);
	}
	
	/**
	 * 修改用户信息
	 * @param systemUser 返回类型：void
	 */
	@Transactional
	public void updateSystemUser(SystemUser systemUser){
		this.update(systemUser);
	}
	
	/**
	 * 系统用户分页列表
	 * @param page
	 * @param rows
	 * @param idIcon 
	 * @return
	 */
	public Pagination<Object> systemUsers(int page, int rows, String idIcon,String nowUserDept) {
		//公司或部门id
		String queryParam = "";
		String[] idIconCls = null;
		Boolean flag = true;
		Boolean companySystemAdministrator = false;
		if(StringUtils.hasText(idIcon)){
			idIconCls = idIcon.split("_");
			String icon = idIconCls[1];
			if("company".equals(icon)){
				if(nowUserDept.startsWith("comp_")) {
					queryParam = " and ur.systemUser.company.companyId = :companyId";
					companySystemAdministrator = true;
				} else {
					queryParam = " and ur.systemUser.employeeId in (select emp.employeeId from Employee emp where emp.departmentId = :departmentId)";
				}
				//queryParam = " and ur.systemUser.company.companyId = :companyId";
			}else{
//				if(nowUserDept.startsWith("comp_")) {
//					queryParam = " and ur.systemUser.company.companyId = :companyId";
//					companySystemAdministrator = true;
//				} else {
					queryParam = " and ur.systemUser.employeeId in (select emp.employeeId from Employee emp where emp.departmentId = :departmentId)";
//				}
				flag = false;
			}
		} else {
			idIconCls = new String[1];
			idIconCls[0] = nowUserDept;
			if(nowUserDept.startsWith("comp_")) {
				queryParam = " and ur.systemUser.company.companyId = :companyId";
				companySystemAdministrator = true;
			} else {
				queryParam = " and ur.systemUser.employeeId in (select emp.employeeId from Employee emp where emp.departmentId = :departmentId)";
			}
			flag = false;
		}
		//delFlag=0默认可用
		String condition = "from UserRole ur where ur.systemUser.delFlag ='0' "+ (StringUtils.hasText(queryParam)?queryParam:"");
		String rowSql = "select new Map(" +
				"ur.id as id," +
				"ur.systemUser.company.companyName as companyName," +
				"ur.role.roleName as roleName," +
				"ur.systemUser.employeeId as employeeId," +
				"ur.systemUser.realName as realName," +
				"ur.systemUser.userName as userName," +
				"ur.systemUser.mobile as mobile," +
				"ur.systemUser.loginTime as loginTime) "+condition;
		String countSql = "select count(ur.id) "+condition;
		int offset = (page-1)*rows;
		if(StringUtils.hasText(queryParam)){
			if(flag == true) {
				if(companySystemAdministrator) {
					String[] admin = nowUserDept.split("_");
					return this.findPageByHQL(rowSql, countSql, offset, rows,"companyId",admin[1]);
				} else {
					return this.findPageByHQL(rowSql, countSql, offset, rows,"departmentId",nowUserDept);
				}
			} else {
				if(companySystemAdministrator) {
					String[] admin = nowUserDept.split("_");
					return this.findPageByHQL(rowSql, countSql, offset, rows,"companyId",admin[1]);
				} else {
					return this.findPageByHQL(rowSql, countSql, offset, rows,"departmentId",idIconCls[0]);
				}
			}
		}else{
			return this.findPageByHQL(rowSql, countSql, offset, rows);
		}
	}
	
	/**
	 * 做为一名产品运营人员,可通过查询某个时间段内销售管家用户账号开通及停用信息,以便了解产品的发展情况.
	 * @param page
	 * @param rows
	 * @param createStartDate 账号开通   查询开始时间
	 * @param createEndDate 账号开通  查询结束时间
	 * @param stopStartDate 账号停用  查询开始时间
	 * @param stopEndDate  账号停用  查询结束时间
	 * @return
	 */
	public Map<String, Object> checkUser(int page , int rows , String createStartDate , 
			String createEndDate , String stopStartDate , String stopEndDate){
		int offset = (page - 1) * rows;
		String rowSql = "SELECT * FROM tb_c_system_user us WHERE 1=1 ";
		String countSql = "SELECT COUNT(*) FROM tb_c_system_user us WHERE 1=1 ";
		if(StringUtils.hasText(createStartDate)){
			createStartDate += " 00:00:00";
			rowSql += " AND us.create_time >= '"+createStartDate+"'" ;
			countSql += " AND us.create_time >= '"+createStartDate+"'" ;
		}
		if(StringUtils.hasText(createEndDate)){
			createEndDate += " 23:59:59";
			rowSql += " AND us.create_time <= '"+createEndDate+"'" ;
			countSql += " AND us.create_time <= '"+createEndDate+"'" ;
		}
		if(StringUtils.hasText(stopStartDate) || StringUtils.hasText(stopEndDate)){
			rowSql += " AND us.del_flag='1'";
			countSql += " AND us.del_flag='1'";
		}
		if(StringUtils.hasText(stopStartDate)){
			stopStartDate += " 00:00:00";
			rowSql += " AND us.modify_time >= '"+stopStartDate+"'" ;
			countSql += " AND us.modify_time >= '"+stopStartDate+"'" ;
		}
		if(StringUtils.hasText(stopEndDate)){
			stopEndDate += " 23:59:59";
			rowSql += " AND us.modify_time <= '"+stopEndDate+"'" ;
			countSql += " AND us.modify_time <= '"+stopEndDate+"'" ;
		}
		rowSql += " limit ?,?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,offset,rows);
		int total = this.jdbcTemplate.queryForInt(countSql);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		map.put("total", total);
		return map;
	}
	/**
	 * 用于密码输入超过最大值时，锁定该用户
	 * @param userName
	 */
	public void lockUser(String userName){
		String sql="update tb_c_system_user set del_flag='2' where user_name=?";
		this.jdbcTemplate.update(sql, userName);
	}
}
