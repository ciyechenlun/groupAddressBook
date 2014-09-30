// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author li.menghua
 * @date 2012-11-28 下午3:40:06
 */
@Repository
public class CompanyDao extends HibernateBaseDaoImpl<Company, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * treegrid  获取公司树
	 * @param parentCompanyId
	 * @return
	 */
	public List<Map<String, Object>> getCompanyTree(String parentCompanyId,String companyId){
		String sql = "SELECT comp.*," +
				"(case comp.have_child_company when 'N' then 'open' when 'Y' then 'closed' else 'open' end) as state," +
				"(case comp.have_child_company when 'N' then true when 'Y' then false else true end) as isLeaf," +
				"'folder' as iconCls ";
		sql += " FROM tb_c_company comp";
		sql += " WHERE vitrue_flag='0' AND org_flag='1' AND comp.del_flag='0' " +
				" and comp.parent_company_id=?";
		if(StringUtils.hasText(companyId)){
			sql += " and comp.company_id='"+companyId+"'";
		}
		return this.jdbcTemplate.queryForList(sql,parentCompanyId);
	}
	
	/**
	 * tree 获取公司树
	 * @param parentCompanyId
	 * @param companyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCompanyTreeHql(String companyId) {
		String hql = "select new Map(" +
				"com.companyId as id," +
				"com.companyName as text," +
				"com.haveChildCompany as haveChildCom) " +
				"from Company com where com.delFlag='0'";
		if(StringUtils.hasText(companyId)){
			hql += " and com.companyId='"+companyId+"'";
		}
		return this.findByHQL(hql);
	}
	
	/**
	 * combortree 获取公司树
	 * @author yandou 
	 * @param parentCompanyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> companyTree(String parentCompanyId) {
		//判断是否超级管理员，是获取全部公司
		User user = SecurityContextUtil.getCurrentUser();
		String currentCompanyId = "";
		if(!isSupperAdmin(user.getRoles())){
			currentCompanyId = SecurityContextUtil.getCompanyId();
		}
		
		String hql = "select new Map(com.companyId as id,com.companyName as text,com.haveChildCompany as haveChildCom) from Company com where com.parentCompanyId = ? and com.delFlag='0' ";
		if(StringUtils.hasText(currentCompanyId)){
			hql  += " and com.companyId = '"+currentCompanyId+"'"; 
		}
		return this.findByHQL(hql,StringUtils.hasText(parentCompanyId)?parentCompanyId:"0");
	}
	
	/**
	 * 判断当前管理员是否有超级管理员角色
	 * @param roles
	 * @return
	 */
	private boolean isSupperAdmin(List<Role> roles)
	{
		boolean isAdmin = false;
		for(Role role : roles)
		{
			if(role.getRoleId().equals("0"))
			{
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
	
	/**
	 * 根据company_id parent_company_id查找属于同一父公司下其他子公司数量
	 * @param companyId
	 * @param exceptId
	 * @return
	 */
	public int getSubComCountExcept(String companyId, String exceptId){
		String sql = "select count(*) from tb_c_company where parent_company_id = ? and company_id <> ? and del_flag <> '1'";
		return this.jdbcTemplate.queryForInt(sql, companyId, exceptId);
	}
	
	/**
	 * 查找公司
	 * @param companyName
	 * @return
	 */
	public List<Map<String, Object>> searchComp(String companyName){
		String sql = "SELECT * FROM tb_c_company comp WHERE comp.del_flag='0' AND comp.company_name like '%"+companyName.trim()+"%'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 通过公司Id查找公司
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> searchCompByCompId(String companyId){
		String sql = "SELECT * FROM tb_c_company comp WHERE comp.del_flag='0' AND comp.company_id = ? ";
		return this.jdbcTemplate.queryForList(sql,companyId);
	}
	
	/**
	 * 根据手机号码返回公司列表
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getCompanyByMobile(String mobile)
	{
		String sql= "SELECT * FROM tb_c_company WHERE company_id in (SELECT company_id FROM " +
				"tb_c_user_company WHERE mobile=?)";
		return this.jdbcTemplate.queryForList(sql, mobile);
	}
	
	/**
	 * 添加公司
	 * 
	 * @param company
	 * @return 返回ID
	 */
	@Transactional
	public String addCompany(Company company){
		return this.save(company);
	}
	
	public void test(String company_id,String company_name)
	{
		String sql = "update tb_c_company set company_name='" + company_name + "' where company_id='" + company_id + "'";
		this.jdbcTemplate.execute(sql);
	}

	/**
	 * 获取所有公司列表
	 * 
	 * @return 
	 * 返回类型：List<Company>
	 */
	public List<Company> getAllCompany() {
		return this.loadAll();
	}
	
	/**
	 * 根据员工ID查找该员工的公司ID
	 * 
	 * @param employeeId
	 * @return  公司ID
	 * 返回类型：String
	 */
	@SuppressWarnings("unchecked")
	public String getCompanyIdByEmployeeId(String employeeId){
		String hql = 
			"select \n" +
			"	c.*\n" +
			"FROM\n" +
			"	Employee as e,\n" +
			"	Company as c,\n" +
			"	Department as d\n" +
			"where\n" +
			"	e.departmentId = d.departmentId AND\n" +
			"	d.companyId = c.companyId AND\n" +
			"	e.employeeId = ? ";
		List<Company> companys = this.findByHQL(hql, employeeId);
		return companys.isEmpty()?null:companys.get(0).getCompanyId();
		
	}
	
	/**
	 * 根据手机号码，返回用户皮肤资源文件地址
	 * @param mobile
	 * @return
	 */
	public List<Map<String, Object>> getCompanySkin(String mobile)
	{
		String sql = "SELECT company_id,company_name,IFNULL(index_pictrue,'') AS skin FROM tb_c_company WHERE " +
				"company_id IN (SELECT company_id FROM tb_c_user_company WHERE mobile=? AND del_flag='0' " +
				//add by liyuchen 成员管理，判断成员是否被禁用
				"AND forbidden_flag<>'1' " +
				//end add
				") ORDER BY display_order ASC";
		return this.jdbcTemplate.queryForList(sql,mobile);
	}
	
	/**
	 * 获取所有企业信息列表---用于同步企业信息(此处对企业进行了过滤).
	 * add by yuan.fengjian@ustcinfo.com
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Company> getAllCompanys() {
		String hql = "FROM Company company WHERE company.delFlag = '0' and company.orgFlag = '1' AND company.vitrueFlag = '0'";
		return this.findByHQL(hql);
	}
	/**add by zhangjun 2013/11/20*/
	/**
	 *清空集团内部们及员工数据
	 * @param companyId
	 * @return
	 */
	public void delGroupByCompany(String companyId)
	{
		
		String sql2 ="select employee_id from tb_c_user_company where del_flag='0' and company_id='" + companyId 
				+ "' and employee_id not in (select distinct employee_id from tb_c_user_company where del_flag='0' and company_id != '"+companyId+"')";
		List<Map<String,Object>> list= this.jdbcTemplate.queryForList(sql2);//查找唯一企业下的员工
	
		String sql5 ="delete from tb_c_user_department where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_company where del_flag='0' and company_id='" + companyId +"') ";
		this.jdbcTemplate.execute(sql5);//删除用户部门关联表中公司为当前的数据
		String sql = "update tb_c_department set del_flag='1' where company_id ='" + companyId + "'";
		this.jdbcTemplate.execute(sql);//删除当前公司下的所有部门
		String sql6 ="update tb_c_user_company set del_flag='1' where company_id='" + companyId +"'";
		this.jdbcTemplate.execute(sql6);//删除用户公司关联表中公司为当前的数据
		String sql3 ="";
		String sql4 ="";
		for (Map<String, Object> map : list) {
			sql3 = "update  tb_c_employee set del_flag = '1' where employee_id= '"+map.get("employee_id") +"'";//删除员工表
			this.jdbcTemplate.execute(sql3);
			sql4 = "update tb_c_system_user set del_flag ='1' where employee_id= '"+map.get("employee_id") +"'";//删除用户表
			this.jdbcTemplate.execute(sql4);
		}
	}
	/**add by zhangjun 2013/11/20*/
}
