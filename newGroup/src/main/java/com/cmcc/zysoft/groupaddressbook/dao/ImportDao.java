// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dto.EmployeeDto;
import com.cmcc.zysoft.groupaddressbook.dto.UserCompanyDto;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：importDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 下午4:26:25
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class ImportDao extends HibernateBaseDaoImpl<Employee, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 增加联系人时,选择的员工职位类型.
	 * @param companyId 所在公司
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> headShipList(String companyId){
		String sql = "SELECT " +
				"hs.headship_id AS dataCode," +
				"hs.headship_name AS dataContent " +
				"FROM tb_c_headship hs " +
				"WHERE hs.del_flag='0' AND hs.company_id=?";
		return this.jdbcTemplate.queryForList(sql,companyId);
	}
	
	/**
	 * 通过员工职位名称获取职位编码.
	 * @param headShipName 员工职位名称
	 * @param companyId 所在公司
	 * @return 
	 * 返回类型：String
	 */
	public String getHeadShipCodeByName(String headShipName,String companyId){
		String sql = "SELECT " +
				"hs.headship_id AS headshipId," +
				"hs.headship_name AS headshipName " +
				"FROM tb_c_headship hs " +
				"WHERE hs.del_flag='0' AND hs.company_id=? " +
				"AND hs.headship_name=? ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,companyId,headShipName);
		if(list.size()==0){
			return "";
		}else{
			return list.get(0).get("headshipId").toString();
		}
	}
	
	/**
	 * 导入前删除之前的部门,人员信息.
	 * 返回类型：void
	 */
	public void deleteData(){
		String sql1 = "DELETE FROM tb_c_department";
		this.jdbcTemplate.execute(sql1);
		String sql2 = "DELETE FROM tb_c_system_user WHERE user_name <> 's_admin'";
		this.jdbcTemplate.execute(sql2);
	}
	
	/**
	 * 导出的人员信息.
	 * @param rights 权限
	 * @param departmentLevel 登陆人部门级别
	 * @param selfDepartmentId 登陆人部门Id
	 * @param companyId 导出数据
	 * @return 
	 * 返回类型：List<EmployeeDto>
	 */
	public List<EmployeeDto> exportList(String rights, int departmentLevel, String selfDepartmentId,String companyId){
		String rowSql = "SELECT " +
				"emp.employee_name AS empName," +
				"emp.department_id AS deptId," +
				"(SELECT hs.headship_name FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id) " +
				"AS headShip," +
				"emp.mobile_short AS mobileShort," +
				"emp.mobile AS mobileLong," +
				"emp.tel_short AS telShort," +
				"emp.telephone2 AS telLong," +
				"emp.email AS eMail," +
				"emp.display_order AS displayOrder " +
				"FROM tb_c_system_user us,tb_c_employee emp,tb_c_department dept " +
				"WHERE us.employee_id=emp.employee_id AND dept.department_id=emp.department_id AND us.company_id=? ";
		
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,companyId);
		List<EmployeeDto> empDtoList = new ArrayList<>();
		for(Map<String, Object> map : list){
			EmployeeDto employeeDto = new EmployeeDto();
			String fullName = this.fullDepartmentName("", map.get("deptId").toString());
			employeeDto.setName(map.get("empName")==null?"":map.get("empName").toString());
			employeeDto.setHeadShip(map.get("headShip")==null?"":map.get("headShip").toString());
			employeeDto.setDepartmentName(fullName);
			employeeDto.setMobileShort(map.get("mobileShort")==null?"":map.get("mobileShort").toString());
			employeeDto.setMoblieLong(map.get("mobileLong")==null?"":map.get("mobileLong").toString());
			employeeDto.setTelShort(map.get("telShort")==null?"":map.get("telShort").toString());
			employeeDto.setTelLong(map.get("telLong")==null?"":map.get("telLong").toString());
			employeeDto.setEmail(map.get("eMail")==null?"":map.get("eMail").toString());
			employeeDto.setDisplayOrder(
					map.get("displayOrder")==null?999999:Integer.parseInt(map.get("displayOrder").toString()));
			empDtoList.add(employeeDto);
		}
		return empDtoList;
	}
	
	/**
	 * 获取某一公司下所有的用户
	 * @param company_id
	 * @return
	 */
	public List<UserCompanyDto> getUsersList(String company_id)
	{
		Map<String,Object> m = this.jdbcTemplate.queryForMap("SELECT company_name FROM tb_c_company WHERE company_id=?",company_id);
		//"姓名","性别","主要号码","手机短号","办公固话","办公短号","单位","二级部门","三级部门","四级部门","五级部门",
		//"职位","办公地址","显示顺序","邮箱","QQ","微博账号","微信账号","学校","专业","年级","班级","学号","籍贯","家庭住址","家庭电话","生日","个性签名"
		String sql = "SELECT ucmp.employee_name,emp.sex,ucmp.mobile,ucmp.mobile_short,ucmp.telephone2," +
				"ucmp.tel_short,ucmp.department_name,ucmp.headship_name,ucmp.address," +
				"ucmp.display_order,ucmp.email,ucmp.qq,ucmp.weibo,ucmp.weixin,ucmp.school,ucmp.user_major," +
				"ucmp.user_grade,ucmp.user_class,ucmp.student_id,ucmp.native_place,ucmp.home_address,ucmp.telephone,ucmp.birthday," +
				"ucmp.mood FROM tb_c_user_company ucmp LEFT JOIN tb_c_employee emp ON " +
				"emp.employee_id=ucmp.employee_id WHERE ucmp.del_flag='0' AND ucmp.company_id=? ORDER BY ucmp.department_name,ucmp.display_order ASC";
		List<UserCompanyDto> ucmpList = new ArrayList<UserCompanyDto>();
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, company_id);
		for(Map<String,Object> map : list)
		{
			UserCompanyDto uc = new UserCompanyDto();
			uc.setName(map.get("employee_name")==null?"":map.get("employee_name").toString());
			uc.setAddress(map.get("address")==null?"":map.get("address").toString());
			uc.setBirthday(map.get("birthday")==null?"":map.get("birthday").toString());
			uc.setCompanyName(m.get("company_name").toString());
			if(map.get("department_name")!=null)
			{
				String deptName = map.get("department_name").toString();
				String[] depts = deptName.split("[-]");
				if(depts.length>1)
				{
					uc.setDepartmentName2(depts[1]);
				}
				else
				{
					uc.setDepartmentName2("");
				}
				if(depts.length>2)
				{
					uc.setDepartmentName3(depts[2]);
				}
				else
				{
					uc.setDepartmentName3("");
				}
				if(depts.length>3)
				{
					uc.setDepartmentName4(depts[3]);
				}
				else
				{
					uc.setDepartmentName4("");
				}
				if(depts.length>4)
				{
					uc.setDepartmentName5(depts[4]);
				}
				else
				{
					uc.setDepartmentName5("");
				}
			}
			else{
				uc.setDepartmentName2("");
				uc.setDepartmentName3("");
				uc.setDepartmentName4("");
				uc.setDepartmentName5("");
			}
			if(map.get("display_order")!=null){
				uc.setDisplayOrder(Integer.parseInt(map.get("display_order").toString()));
			}
			else{
				uc.setDisplayOrder(9999);
			}
			uc.setEmail(map.get("email")==null?"":map.get("email").toString());
			uc.setHeadShip(map.get("headship_name")==null?"":map.get("headship_name").toString());
			uc.setHomeAddress(map.get("home_address")==null?"":map.get("home_address").toString());
			uc.setHomeTel(map.get("telephone")==null?"":map.get("telephone").toString());
			uc.setMobileShort(map.get("mobile_short")==null?"":map.get("mobile_short").toString());
			uc.setMoblieLong(map.get("mobile")==null?"":map.get("mobile").toString());
			uc.setMood(map.get("mood")==null?"":map.get("mood").toString());
			uc.setNativePlace(map.get("native_place")==null?"":map.get("native_place").toString());
			uc.setQq(map.get("qq")==null?"":map.get("qq").toString());
			uc.setSchool(map.get("school")==null?"":map.get("school").toString());
			uc.setSchoolClass(map.get("user_class")==null?"":map.get("user_class").toString());
			uc.setSchoolGrade(map.get("user_grade")==null?"":map.get("user_grade").toString());
			uc.setSchoolMajor(map.get("user_major")==null?"":map.get("user_major").toString());
			uc.setSex(map.get("sex")==null?"":map.get("sex").toString());
			uc.setStudentId(map.get("student_id")==null?"":map.get("student_id").toString());
			uc.setTelLong(map.get("telephone2")==null?"":map.get("telephone2").toString());
			uc.setTelShort(map.get("tel_short")==null?"":map.get("tel_short").toString());
			uc.setWeiBo(map.get("weibo")==null?"":map.get("weibo").toString());
			uc.setWeiXin(map.get("weixin")==null?"":map.get("weixin").toString());
			ucmpList.add(uc);
		}
		return ucmpList;
	}
	
	/**
	 * 通过部门名字找到部门的全部上级部门名称.
	 * @param fullName 
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String fullDepartmentName(String fullName,String departmentId){
		if(StringUtils.hasText(fullName)){
			fullName = this.departmentNameById(departmentId) + "-" + fullName;
		}else{
			fullName = this.departmentNameById(departmentId);
		}
		String parentDepartmentId = this.parentDepartmentId(departmentId);
		if(null != parentDepartmentId){
			if(!("0").equals(parentDepartmentId)){
				return this.fullDepartmentName(fullName, parentDepartmentId);
			}else{
				return fullName;
			}
		}
		return null;
	}
	
	/**
	 * 通过部门Id获取部门名字.
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String departmentNameById(String departmentId){
		String sql = "SELECT dept.department_name AS deptName FROM tb_c_department dept WHERE dept.department_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,departmentId);
		if(list.size()>0){
			return list.get(0).get("deptName").toString();
		}
		return null;
	}
	
	/**
	 * 通过部门Id找到上级部门Id.
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String parentDepartmentId(String departmentId){
		String sql = "SELECT dept.parent_department_id AS parentId " +
				"FROM tb_c_department dept WHERE dept.department_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,departmentId);
		if(list.size()>0){
			return list.get(0).get("parentId").toString();
		}
		return null;
	}
	
	/**
	 * 判断是否存在该电话号码.
	 * @param mobile 
	 * @return 
	 * 返回类型：String
	 */
	public String checkEmployee(String mobile){
		String sql = "SELECT emp.employee_id as empId FROM tb_c_employee emp WHERE emp.mobile=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,mobile);
		if(list.size()>0){
			return list.get(0).get("empId").toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 根据用户手机号码返回当前用户隶属信息（多部门、职位）
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getEmployeeInfoByMobile(String mobile)
	{
		String sql = "SELECT emp.employee_id,emp.employee_name,dept.department_id,dept.department_name,hsp.headship_id,hsp.headship_name " + 
				 " FROM tb_c_user_department user_dept left JOIN tb_c_department dept on dept.department_id=user_dept.department_id " + 
				 " left JOIN tb_c_headship hsp on hsp.headship_id=user_dept.headship_id " +  
				 " left JOIN tb_c_user_company cmp on cmp.user_company_id=user_dept.user_company_id " +  
				 " left JOIN tb_c_employee emp on emp.employee_id=cmp.employee_id " +  
				 " WHERE  emp.mobile=?";
		return this.jdbcTemplate.queryForList(sql,mobile);
	}
	/**
	 * 根据用户手机号码返回当前用户隶属信息（多部门、职位）
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getEmployeeIdByMobile(String mobile)
	{
		String sql = "select emp.employee_id,emp.del_flag from tb_c_employee emp where emp.mobile=?";
		return this.jdbcTemplate.queryForList(sql,mobile);
	}
}
