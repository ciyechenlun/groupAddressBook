package com.cmcc.zysoft.sysmanage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.support.Pagination;

/**
 * SystemUserDao.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-4 下午2:39:16
 *
 */
@Repository
public class SysEmployeeDao extends HibernateBaseDaoImpl<Employee,String> {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 立即定位，关键词搜索
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> empByKeyword(String keyword) {
		String hql = "select new Map(emp.mobile as id, emp.employeeName as text) from Employee emp " +
				"where emp.delFlag='0' and emp.mobile like ? or emp.employeeName like ?";
		return this.findByHQL(hql, keyword,keyword);
	}
	/**
	 * 立即定位，根据部门获取员工列表
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> empByDeptId(String deptId){
		String hql = "select new Map(emp.mobile as id, emp.employeeName as text,emp.employeeId as empId) from Employee emp where emp.delFlag='0' and emp.departmentId = ?";
		return this.findByHQL(hql,deptId);
	}
	
	/**
	 * 获取全部员工信息列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getAllEmployees(int page, int rows) {
		String rowSql = " select new Map(" +
						" e.employeeId as employeeId ," +
						" e.departmentId as departmentId ," +
						" e.headshipId as headshipId ," +
						" e.employeeCode as employeeCode ," +
						" e.employeeName as employeeName ," +
						" e.idCard as idCard ," +
						" e.resume as resume ," +
						" e.age as age ," +
						" e.sex as sex ," +
						" e.politicsStatus as politicsStatus ," +
						" e.nativePlace as nativePlace ," +
						" e.school as school ," +
						" e.degree as degree ," +
						" e.graduationDate as graduationDate ," +
						" e.mobile as mobile ," +
						" e.backupMobile as backupMobile ," +
						" e.telephone as telephone ," +
						" e.email as email ," +
						" e.qq as qq ," +
						" e.birthday as birthday ," +
						" e.picture as picture ," +
						" e.higherUp as higherUp ," +
						" e.joinDate as joinDate ," +
						" e.leaveDate as leaveDate ," +
						" e.leaveReason as leaveReason ," +
						" e.comment as comment ," +
						" e.status as status ," +
						" e.delFlag as delFlag," +
						" d.departmentName as departmentName, " +
						" h.headshipName as headshipName," +
						" d.company.companyName as companyName " +
						" ) " +
						" from Employee e,Headship h,Department d " +
						" where e.delFlag='0' and " +
						" e.departmentId = d.departmentId and " +
						" e.headshipId = h.headshipId " +
						" order by e.employeeId desc ";
		String countSql = "select count(*) from Employee e,Headship h,Department d " +
						" where e.delFlag='0' and " +
						" e.departmentId = d.departmentId and " +
						" e.headshipId = h.headshipId " +
						" order by e.employeeId desc ";
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 点击左边公司部门树获取员工信息
	 * 
	 * @return
	 */
	public Pagination<Object> getEmployeesByTree(int page, int rows,String idIcon) {
		String[] idIconCls;
		String comSql = " and d.company.companyId = ";
		String deptSql = " and e.departmentId = ";
		String orderSql = " order by e.employeeId desc ";
		String rowSql = " select new Map(" +
				" e.employeeId as employeeId ," +
				" e.departmentId as departmentId ," +
				" e.headshipId as headshipId ," +
				" e.employeeCode as employeeCode ," +
				" e.employeeName as employeeName ," +
				" e.idCard as idCard ," +
				" e.resume as resume ," +
				" e.age as age ," +
				" e.sex as sex ," +
				" e.politicsStatus as politicsStatus ," +
				" e.nativePlace as nativePlace ," +
				" e.school as school ," +
				" e.degree as degree ," +
				" e.graduationDate as graduationDate ," +
				" e.mobile as mobile ," +
				" e.backupMobile as backupMobile ," +
				" e.telephone as telephone ," +
				" e.email as email ," +
				" e.qq as qq ," +
				" e.birthday as birthday ," +
				" e.picture as picture ," +
				" e.higherUp as higherUp ," +
				" e.joinDate as joinDate ," +
				" e.leaveDate as leaveDate ," +
				" e.leaveReason as leaveReason ," +
				" e.comment as comment ," +
				" e.status as status ," +
				" e.delFlag as delFlag," +
				" d.departmentName as departmentName, " +
				" d.company.companyName as companyName " +
				" ) " +
				" from Employee e,Department d " +
				" where e.delFlag='0' and " +
				" e.departmentId = d.departmentId and ";
		String countSql = "select count(*) from Employee e,Department d " +
				" where e.delFlag='0' and " +
				" e.departmentId = d.departmentId and ";
		
			if(StringUtils.hasText(idIcon)){
				idIconCls = idIcon.split("_");
				if("department".equals(idIconCls[1])){
					//点击部门
					rowSql += deptSql + "'" + idIconCls[0] + "'";
					countSql +=  deptSql + "'" + idIconCls[0] + "'";
				}else{
						//点击公司
					rowSql += comSql + "'" + idIconCls[0] + "'";
					countSql += comSql + "'" + idIconCls[0] + "'";
				}
			}
			
			rowSql += orderSql;
			countSql += orderSql;
		
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 根据查询条件获取员工信息列表
	 * 
	 * @param employeeName 员工名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getEmployeesByContion(String employeeName, int page, int rows) {
		String rowSql = "";
		String countSql = "";
		rowSql = " select new Map(" +
				" e.employeeId as employeeId ," +
				" e.departmentId as departmentId ," +
				" e.headshipId as headshipId ," +
				" e.employeeCode as employeeCode ," +
				" e.employeeName as employeeName ," +
				" e.idCard as idCard ," +
				" e.resume as resume ," +
				" e.age as age ," +
				" e.sex as sex ," +
				" e.politicsStatus as politicsStatus ," +
				" e.nativePlace as nativePlace ," +
				" e.school as school ," +
				" e.degree as degree ," +
				" e.graduationDate as graduationDate ," +
				" e.mobile as mobile ," +
				" e.backupMobile as backupMobile ," +
				" e.telephone as telephone ," +
				" e.email as email ," +
				" e.qq as qq ," +
				" e.birthday as birthday ," +
				" e.picture as picture ," +
				" e.higherUp as higherUp ," +
				" e.joinDate as joinDate ," +
				" e.leaveDate as leaveDate ," +
				" e.leaveReason as leaveReason ," +
				" e.comment as comment ," +
				" e.status as status ," +
				" e.delFlag as delFlag," +
				" d.departmentName as departmentName, " +
				" h.headshipName as headshipName," +
				" d.company.companyName as companyName " +
				" ) " +
				" from Employee e,Headship h,Department d " +
				" where e.delFlag='0' and " +
				" e.departmentId = d.departmentId and " +
				" e.headshipId = h.headshipId ";
				
		countSql = "select count(*) from Employee e,Headship h,Department d " +
					" where e.delFlag='0' and " +
					" e.departmentId = d.departmentId and " +
					" e.headshipId = h.headshipId " ;
		if(employeeName.trim().length()!=0){
			rowSql += " and e.employeeName like '%"+ employeeName.trim() +"%' ";
			countSql += " and e.employeeName like '%"+ employeeName.trim() +"%' ";
		}
		
		rowSql += " order by e.employeeId desc ";
		countSql += " order by e.employeeId desc ";
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows);
	}
	
	/**
	 * 添加用户信息
	 * 
	 * @param employee 用户对象
	 */
	public void saveEmployee(Employee employee) {
		this.getHibernateTemplate().merge(employee);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param employee 用户对象
	 */
	public void updateEmployee(Employee employee) {
		this.getHibernateTemplate().merge(employee);
	}

	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param employeeId 用户id
	 * @return
	 */
	public Employee getEmployee(String employeeId) {
		Employee employee = this.get(employeeId);
		return employee;
	}

	/**
	 * 删除用户信息
	 * 
	 * @param employeeId
	 */
	public void deleteEmployee(String employeeId) {
		//String sql = " delete from tb_c_employee where employee_id = '" + employeeId + "'";
		String  sql = " update tb_c_employee set del_flag='1' where employee_id = ?";
		jdbcTemplate.update(sql,employeeId);
	}

	/**
	 * 根据部门id获取员工信息，带分页哦
	 * @param page
	 * @param rows
	 * @param deptId
	 * @return
	 */
	public Pagination<Object> employeesByDeptmentId(int page, int rows,String deptId) {
		int offset = (page-1)*rows;
		String rowSql = "from Employee emp where emp.delFlag = '0' and emp.departmentId = :deptId and emp.employeeId not in (select su.employeeId from SystemUser su where su.delFlag='0')";
		String countSql = "select count(employeeId) from Employee emp where emp.delFlag = '0' and emp.departmentId = :deptId and emp.employeeId not in (select su.employeeId from SystemUser su where su.delFlag='0')";
		return this.findPageByHQL(rowSql, countSql, offset, rows,"deptId",deptId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> employeesByTel(String tel) {
		String hql = "from Employee emp where emp.mobile = ?";
		return this.findByHQL(hql,tel);
	}
	
	/**
	 * 员工管理 左侧部门树
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param parentDepartmentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> deptTree(String parentDepartmentId){
		String hql = "select new Map(dept.departmentId as id," +
				"dept.departmentName as text," +
				"dept.haveChildDeparment as isLeaf," +
				"dept.haveChildDeparment as haveChildDeparment," +
				"dept.departmentType as attributes," +
				"dept.departmentType as deptType," +
				"(case dept.departmentName when 'Y' then 'closed' when 'N' then 'open' else 'open' end) as state)" +
				" from Department dept where dept.delFlag='0' and dept.parentDepartmentId = ? ";
		return this.findByHQL(hql, parentDepartmentId);
	}
	
	/**
	 * 分页获取员工列表
	 * @author li.menghua
	 * @date 2012-12-22 下午3:20:16
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getList(int page,int rows,String deptId){
		String companyId = SecurityContextUtil.getCompanyId();
		int offset = (page - 1) * rows;
		String rowSql = "SELECT emp.* ," +
				"dept.department_name as deptName, " +
				"(SELECT COUNT(*) FROM tb_c_system_user su WHERE su.employee_id=emp.employee_id and su.del_flag='0') as isUser " +
				"FROM tb_c_employee emp,tb_c_department dept " +
				"WHERE emp.department_id = dept.department_id AND emp.del_flag='0' AND dept.company_id='"+companyId+"' ";
		String countSql = "SELECT count(*) " +
				"FROM tb_c_employee emp,tb_c_department dept " +
				"WHERE emp.department_id = dept.department_id AND emp.del_flag='0' AND dept.company_id='"+companyId+"' ";
		if(StringUtils.hasText(deptId)){
			rowSql += " and emp.department_id in ("+deptId+")";
			countSql += " and emp.department_id in ("+deptId+")";
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
	 * 获取是否领导状态
	 * @return
	 */
	public List<Map<String, Object>> isDeptLeader(){
		String sql = "SELECT * FROM tb_c_dict_data dat,tb_c_dict_type typ WHERE dat.type_id=typ.type_id AND typ.type_code='is_dept_leader'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取登陆人所在公司的角色列表
	 * @return
	 */
	public List<Map<String, Object>> roleList(){
		String companyId = SecurityContextUtil.getCompanyId();
		String sql = "SELECT " +
				"role.role_name AS roleName," +
				"role.role_id AS roleId " +
				"FROM tb_c_role role WHERE role.status='0' AND role.company_id=?";
		return this.jdbcTemplate.queryForList(sql,companyId);
		
	}

	
	
	@Resource
	private DepartmentDao departmentDao;
	
	/**
	 * 弹出框人员列表
	 * @author yandou
	 * @param page
	 * @param rows
	 * @param idIcon
	 * @param keyword
	 * @return
	 */
	public Map<String,Object> emps(int page, int rows, String idIcon,String keyword) {
		
		int offset = (page-1)*rows;
		String conditon = "select emp.employee_id as employeeId,emp.employee_code as employeeCode,emp.employee_name as employeeName,emp.id_card as idCard,emp.mobile as mobile" +
				"  from tb_c_employee emp where emp.del_flag='0' ";
		String countSql = "select count(emp.employee_id) from tb_c_employee emp where emp.del_flag='0' ";
		if(StringUtils.hasText(idIcon)){
			if(idIcon.endsWith("company")){
				conditon += " and emp.department_id in (select dept.department_id from tb_c_department dept where dept.company_id = '"+idIcon.split("_")[0]+"')";
				countSql += " and emp.department_id in (select dept.department_id from tb_c_department dept where dept.company_id = '"+idIcon.split("_")[0]+"')";
			}else{
				conditon += " and emp.department_id in ("+ this.departmentDao.allChildrenDept(idIcon.split("_")[0])+")";
				countSql += " and emp.department_id in ("+ this.departmentDao.allChildrenDept(idIcon.split("_")[0])+")";
			}
		}else{
			conditon += " and emp.department_id in (select dept.department_id from tb_c_department dept where dept.company_id = '"+SecurityContextUtil.getCompanyId()+"')";
			countSql += " and emp.department_id in (select dept.department_id from tb_c_department dept where dept.company_id = '"+SecurityContextUtil.getCompanyId()+"')";
		}
		if(StringUtils.hasText(keyword)){
			conditon += " and (emp.employee_name like '%"+keyword+"%' or emp.mobile like '%"+keyword+"%')";
			countSql += " and (emp.employee_name like '%"+keyword+"%' or emp.mobile like '%"+keyword+"%')";
		}
		conditon += " limit ?,?";
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(conditon,offset,rows);
		int total = this.jdbcTemplate.queryForInt(countSql);
		map.put("rows", list);
		map.put("total", total);
		return map;
	}

	/**
	 * 获取所有成员列表---用于同步成员信息(此处对已逻辑删除的成员进行了过滤).
	 * add by yuan.fengjian@ustcinfo.com
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployee() {
		String sql = "FROM Employee a WHERE a.delFlag = '0'";
		return this.findByHQL(sql);
	}
	
	/**
	 * 返回所有用户列表，供与云平台同步使用
	 * @return
	 */
	public List<Employee> getAllEmployeeWithIMSI(String where)
	{
		String sql = "SELECT employee_id,department_id,mobile_short,tel_short,telephone2,headship_id,employee_code,employee_name,employee_firstword," +
				"employee_fullword,parent_department_name," +
				"(SELECT imsi FROM tb_c_device where employee_id=tb_c_employee.employee_id limit 1) as id_card," +
				"resume,age,sex,politics_status,native_place,school," +
				"degree,graduation_date,mobile,backup_mobile,telephone,email,qq,birthday,picture,higher_up,is_dept_leader,join_date,leave_date,leave_reason," +
				"comment,status,display_order,grid_number,del_flag " +
				" FROM tb_c_employee WHERE del_flag='0'";
		if (StringUtils.hasText(where))
		{
			sql = sql + " AND " + where;
		}
		System.out.print("sql==="+sql);
		return this.jdbcTemplate.queryForList(sql, Employee.class);
	}
	
	/**
	 * 返回所有用户列表，供与云平台同步使用
	 * @return
	 */
	public List<Map<String,Object>> getAllEmployeeWithIMSISQL(String where)
	{
		String sql = "SELECT employee_id,department_id,mobile_short,tel_short,telephone2,headship_id,employee_code,employee_name,employee_firstword," +
				"employee_fullword,parent_department_name," +
				"IFNULL((SELECT imsi FROM tb_c_device where employee_id=tb_c_employee.employee_id limit 1),'') as id_card," +
				"resume,age,sex,politics_status,native_place,school," +
				"degree,graduation_date,mobile,backup_mobile,telephone,email,qq,birthday,picture,higher_up,is_dept_leader,join_date,leave_date,leave_reason," +
				"comment,status,display_order,grid_number,del_flag " +
				" FROM tb_c_employee WHERE del_flag='0' and employee_id in (select employee_id from tb_c_device)";
		if (StringUtils.hasText(where))
		{
			sql = sql + " AND " + where;
		}
		//sql += " limit 10";
		System.out.print("sql==="+sql);
		return this.jdbcTemplate.queryForList(sql);
	}

}
