// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserCompanyDao
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午9:22:35
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Repository
public class UserCompanyDao extends HibernateBaseDaoImpl<UserCompany, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 导入时检查当前登录用户是否已在分组信息里,如果不存在,则接下来应该插入一条记录.
	 * @param companyId
	 * @param employeeId
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkSelf(String companyId,String employeeId){
		String sql = "SELECT " +
				"COUNT(uc.user_company_id) " +
				"FROM tb_c_user_company uc " +
				"WHERE uc.company_id='"+companyId+"' " +
				"AND uc.employee_id='"+employeeId+"'";
		int count = this.jdbcTemplate.queryForInt(sql);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	public List<Map<String,Object>> checkSelfReturnKey(String companyId,String employeeId)
	{
		
		return null;
	}
	
	/**
	 * 判断当前用户是否某个分组的管理员
	 * @param companyId：分组Id编号
	 * @param employeeId：当前登录用户的员工编号
	 * @return true:管理员,false：非管理员
	 */
	public boolean checkGroupUserManage(String companyId,String employeeId)
	{
		String sql = "SELECT COUNT(*) FROM tb_c_user_company WHERE company_id=? AND employee_id=? " +
				"AND (manage_flag='1' or manage_flag='2')";
		int ret = this.jdbcTemplate.queryForInt(sql, companyId,employeeId);
		return ret > 0;
	}
	
	/**
	 * 找到某个账号的群组数量.
	 * @param employeeId
	 * @return 
	 * 返回类型：int
	 */
	public int groupNum(String employeeId){
		String sql = "SELECT " +
				"COUNT(uc.user_company_id) " +
				"FROM tb_c_user_company uc " +
				"WHERE uc.employee_id="+employeeId;
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	/**
	 * 无群组时删除登录账号.
	 * @param employeeId 
	 * 返回类型：void
	 */
	public void updateSystemUser(String employeeId){
		String sql = "UPDATE tb_c_system_user SET del_flag = '1' WHERE employee_id = ?";
		this.jdbcTemplate.update(sql,employeeId);
		sql = "UPDATE tb_c_employee SET del_flag='1' WHERE employee_id=?";
		this.jdbcTemplate.update(sql,employeeId);
	}
	
	/**
	 * 根据公司ID从tb_c_user_company表中获取所有人员的手机号-用于短信推广.
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getUsersByCompanyId(String companyId) {
		String sql = "SELECT DISTINCT mobile FROM tb_c_user_company WHERE company_id = ? AND del_flag='0'";
		return this.jdbcTemplate.queryForList(sql, companyId);
	}
	/**
	 * 根据公司ID从tb_c_user_company表中获取所有非注册人员的手机号-用于短信推广.
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getNoRegByCompanyId(String companyId) {
		String sql = "SELECT DISTINCT mobile FROM tb_c_user_company WHERE "+
						"employee_id not in (select employee_id from tb_c_device) "+
						"and company_id = ? AND del_flag='0'";
		return this.jdbcTemplate.queryForList(sql, companyId);
	}
	
	/**
	 * 查看同一公司同一部门是否存在同一个人，在增加时做校验用
	 * @param mobile
	 * @param company_id
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getUserByMobileANDCompanyANDDepartment(String mobile,String company_id,String department_id)
	{
		String sql = "SELECT * FROM tb_c_user_department WHERE " +
				"department_id=? AND user_company_id IN (SELECT user_company_id FROM " +
				"tb_c_user_company WHERE company_id=? AND mobile=? AND del_flag='0') and visible_flag = '1'";
		return this.jdbcTemplate.queryForList(sql,department_id,company_id,mobile);
	}
	
	/**
	 * 根据部门获取所有用户
	 * @param department_id
	 * @return
	 */
	public List<Map<String,Object>> getUserCompanyByDepartmentId(String department_id)
	{
		String sql = "SELECT * FROM tb_c_user_company WHERE user_company_id IN (" +
				"SELECT distinct user_company_id FROM tb_c_user_department WHERE department_id=?)";
		return this.jdbcTemplate.queryForList(sql,department_id);
	}
	
	/**
	 * 企业管理员设置
	 * @param userCompanyId
	 *  @param managerType 1为管理员2为二级部门管理员
	 * @return
	 */
	public boolean manageEdit(String userCompanyId,String managerType,String managerDept)
	{
		String sql = "FROM UserCompany WHERE userCompanyId=?";
		@SuppressWarnings("unchecked")
		List<UserCompany> list = this.getHibernateTemplate().find(sql,userCompanyId);
		if(null != list && list.size()>0)
		{
			UserCompany uc = list.get(0);
			sql = "SELECT user_id FROM tb_c_system_user WHERE employee_id=? and del_flag='0'";
			List<Map<String,Object>> listSys = this.jdbcTemplate.queryForList(sql, uc.getEmployeeId());
			String userId="";
			if(listSys.size()>0){
				userId = listSys.get(0).get("user_id").toString();
				sql = "DELETE FROM tb_c_user_role WHERE user_id='" + userId + "' limit 1";
				this.jdbcTemplate.execute(sql);
				sql = "DELETE FROM tb_c_user_manager WHERE employee_id=? and company_id=?";
				this.jdbcTemplate.update(sql,uc.getEmployeeId(),uc.getCompanyId());
				if(!StringUtils.hasText(managerType)){
					uc.setManageFlag("0");
				}else if(StringUtils.hasText(managerType)){
					uc.setManageFlag(managerType);
					if(managerType.equals("1")&&StringUtils.hasText(userId)){
						sql = "INSERT INTO tb_c_user_role (id,user_id,role_id) VALUES (replace(uuid(),'-',''),?,?)";
						this.jdbcTemplate.update(sql,userId,"5");
						sql = "select count(*) from tb_b_company_plug where company_id=?";
						int count = this.jdbcTemplate.queryForInt(sql, uc.getCompanyId());
						if(count>0){
							sql="select count(*) from jttxl_test.tb_c_system_user where user_id=?";
							int count1 = this.jdbcTemplate.queryForInt(sql, userId);
							if(count1>0){
								sql="insert into jttxl_test.tb_c_user_role(id,user_id,role_id,company_id) " +
										"values (replace(uuid(),'-',''),?,'3',?)";
								this.jdbcTemplate.update(sql, userId,uc.getCompanyId());
							}
						}
					}else if(managerType.equals("3")&&StringUtils.hasText(userId)){
						if(StringUtils.hasText(managerDept)){
							sql = "INSERT INTO tb_c_user_role (id,user_id,role_id) VALUES (replace(uuid(),'-',''),?,?)";
							this.jdbcTemplate.update(sql,userId,"6");
							String[] depts = managerDept.split(",");
							if(depts!=null&&depts.length>0){
								for (String dept : depts) {
									sql ="insert INTO tb_c_user_manager (employee_id,company_id,department_id) " +
											"VALUES(?,?,?)";
									this.jdbcTemplate.update(sql,uc.getEmployeeId(),uc.getCompanyId(),dept);
								}
							}
							
						}
					}
				}
				
				this.update(uc);
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	//add by zhangjun 2013/11/20
	/**
	 * 判断同一个企业同一个部门下是否已经存在指定手机号
	 * @param userCompanyId
	 * @param departmentId
	 * @param mobile
	 * @return
	 */
	public boolean isExistMobileByDep(String userCompanyId,String departmentId,String mobile)
	{
		String sql = "SELECT count(*) from tb_c_user_company uc left join tb_c_user_department ud on uc.user_company_id=ud.user_company_id "
        +" where uc.company_id='"+userCompanyId+"' and ud.department_id ='"+departmentId+"' and uc.mobile ='"+mobile+"' and uc.del_flag='0' and ud.visible_flag='1'";
			Long count = this.jdbcTemplate.queryForLong(sql);
			if(count !=0){
				return true;
			}
			return false;
	}
	/**
	 * 根据公司ID获取用户企业关联表数据
	 * @param companyId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getUserCompanyByCompanyId(String companyId) {
		String sql = "SELECT * from tb_c_user_company where del_flag='0' and company_id = '"+companyId+"'";
		return this.jdbcTemplate.queryForList(sql);
	}
	/**
	 * 拖拽排序后更新display_order
	 * @param usercompanyId
	 * @param displayOrder
	 */
	public void updateDisplayOrderById(String usercompanyId,String fullName,int displayOrder){
		String sql1 = "update tb_c_user_company set display_order=?,department_name=? where user_company_id=?";
		this.jdbcTemplate.update(sql1, displayOrder,fullName,usercompanyId);
	}
	//add by zhangjun 2013/11/20
	/**
	 * 根据手机号更新短号和v网id
	 * @param mobile
	 * @param companyId
	 * @param shortNum
	 * @param groupCode
	 */
	public void updateShortNumVIdByMobile(String mobile,String shortNum,String groupCode){
		String sql1 = "update tb_c_user_company set mobile_short=? where del_flag=0 and mobile=?";
		this.jdbcTemplate.update(sql1, shortNum,mobile);
		String sql2 ="update tb_c_employee set mobile_short=?,grid_number=? where del_flag=0 and mobile=?";
		this.jdbcTemplate.update(sql2, shortNum,groupCode,mobile);
	}
	/**
	 * 获取没有短号的员工
	 * @return
	 */
	public List<Map<String,Object>> getUserCompanyNoShort(int number){
		String sql = "select distinct mobile from tb_c_user_company where (mobile_short='' or mobile_short is null) and del_flag='0' and mobile like ? ";
		return this.jdbcTemplate.queryForList(sql,"%"+number);
	}
	/**
	 * 获取除去移动客服外的最大display_order
	 * @param companyId
	 * @return
	 */
	public int getMaxDisplayOrder(String companyId){
		String sql="select IFNULL(MAX(display_order),0) from tb_c_user_company where company_id=? AND department_name!='移动客服'";
		return this.jdbcTemplate.queryForInt(sql,companyId);
	}
	
	/**
	 * 获取禁用人员信息
	 * @param companyId
	 * @return
	 */
	public List<Map<String,Object>> getForbiddenUser(String companyId){
		String sql = "SELECT user_company_id AS id ,employee_name AS name FROM tb_c_user_company WHERE forbidden_flag='1' AND del_flag='0' AND company_id='"+companyId+"'";
		return this.jdbcTemplate.queryForList(sql);
	}
	/**
	 * 保存数据 
	 * @param userCompanyIds
	 * @param companyId
	 */
	public void saveInfo(String userCompanyIds,String companyId){
		//初始化 将原来被禁止的人员恢复
		String sql = "UPDATE tb_c_user_company SET forbidden_flag='0' WHERE forbidden_flag='1' AND del_flag='0' AND company_id='"+companyId+"'";
		this.jdbcTemplate.update(sql);
		if(StringUtils.hasText(userCompanyIds)){
			//更新最新的被禁止的成员信息
			sql = "UPDATE tb_c_user_company SET forbidden_flag='1' WHERE user_company_id IN ( "+userCompanyIds+")";
			this.jdbcTemplate.update(sql);
		}
		
	}
	public List<Map<String,Object>> getUserAndHeadshipLevel(String headshipId){
		String sql="select uc.user_company_id,MIN(h.headship_level) as headship_level " +
				"from  tb_c_user_company uc join tb_c_user_department ud on uc.user_company_id=ud.user_company_id " +
				"join tb_c_headship h on ud.headship_id=h.headship_id " +
				"where uc.del_flag='0' and ud.visible_flag='1' and h.del_flag='0' " +
				"and h.headship_id=? GROUP BY uc.user_company_id";
		return this.jdbcTemplate.queryForList(sql, headshipId);
	}
	public String getMinHeadshipLevel(String userCompanyId){
		String sql="select MIN(h.headship_level) as headship_level " +
				"from tb_c_user_department ud join tb_c_headship h on ud.headship_id=h.headship_id " +
				"where  ud.visible_flag='1' and h.del_flag='0' and ud.user_company_id=? ";
		return String.valueOf(this.jdbcTemplate.queryForLong(sql, userCompanyId));
	}
	public void updateDeptName(String deptName,String userCompanyId){
		String sql="update tb_c_user_company set department_name=? where user_company_id=?";
		this.jdbcTemplate.update(sql, deptName,userCompanyId);
	}
}