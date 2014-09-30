// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dto.LookGroupInfoDto;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：LookGroupDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 下午12:04:17
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class LookGroupDao extends HibernateBaseDaoImpl<Employee, String> {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	/**
	 * 查询集团通讯录信息.
	 * @param dto 
	 * @return 
	 * 返回类型：List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getGroupInfo(LookGroupInfoDto dto) {
		int offset = (dto.getPage() - 1) * dto.getRows();
		String sql = "SELECT emp.employee_name,emp.employee_id,emp.telephone,emp.tel_short,emp.mobile," +
				" emp.mobile_short,emp.email FROM tb_c_employee emp" +
				" WHERE emp.del_flag = '0'";
		
		if(StringUtils.hasText(dto.getDeptId())){
			sql += " AND emp.department_id ='" + dto.getDeptId()+ "'";				
		}
		sql += " LIMIT ?,?";			
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, offset, dto.getRows());
		if(list == null || list.size() == 0) {
			return null;
		}
		return list;
	}
	
	/**
	 * 集团通讯录个人信息条数.
	 * @param dto 
	 * @return 
	 * 返回类型：Map<String,List<Map<String,Object>>>
	 */
	public int getGroupInfoCount(LookGroupInfoDto dto) {
		String sql = "SELECT count(*) FROM tb_c_employee emp WHERE emp.del_flag = '0'";
		if(StringUtils.hasText(dto.getDeptId())){
			 sql += " And emp.department_id ='" + dto.getDeptId()+ "'";				
		}				
		int sqlCount = this.jdbcTemplate.queryForInt(sql);
		System.out.println("集团通讯录个人信息条数："+sqlCount+"\n");
		return sqlCount;		
	}
	
	/**
	 * 岗位下拉框.
	 * @param companyId 所属公司
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findHeadship(String companyId){
		String sql = "SELECT " +
				"hs.headship_id AS data_code," +
				"hs.headship_name AS data_content " +
				"FROM tb_c_headship hs " +
				"WHERE hs.del_flag='0' AND hs.company_id='"+companyId+"'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;		
	}
	
	/**
	 * 查询用户信息.
	 * @param employeeId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getUserById(String employeeId){
		String sql = "SELECT" +
				" emp.employee_id," +
				" emp.employee_name," +
				" dept.department_id," +
				" dept.department_name," +
				" emp.headship_id," +
				" (SELECT hs.headship_name FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id)" +
				" AS headship_name," +
				" emp.telephone2," +
				" emp.tel_short," +
				" emp.mobile," +
				" emp.mobile_short," +
				" emp.email," +
				" emp.display_order," +
				" emp.grid_number" +
				" FROM tb_c_employee emp," +
				" tb_c_department dept" +
				" WHERE emp.department_id = dept.department_id" +
				" AND emp.employee_id = '"+employeeId+"'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;	
	}
	
	/**
	 * 个人通讯录成员信息.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getUserCompanyById(String userCompanyId,String departmentId){
		String sql = "select * from (select (@row := @row + 1) as row,x.* from ("+
				"SELECT uc.*,udept.department_id,udept.headship_id,udept.taxis,emp.grid_number " +
				"FROM tb_c_user_company uc,tb_c_employee emp,tb_c_user_department udept " +
				"WHERE emp.employee_id=uc.employee_id" +
				" AND uc.del_flag='0' AND emp.del_flag='0' AND udept.visible_flag='1' AND udept.user_company_id=uc.user_company_id "+
				" AND udept.department_id=? order by uc.display_order) x ,(SELECT @row := 0) r ) y where user_company_id=?";
		return this.jdbcTemplate.queryForList(sql,departmentId,userCompanyId);
	}

	/**
	 * 根据人员id查询imei号
	 * @param employeeId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getImei(String employeeIds,String companyId){
		String sql = "SELECT" +
				" IFNULL(dev.imei,'') AS imei,emp.mobile,emp.employee_name " +
				" FROM tb_c_employee emp,tb_c_device dev,tb_c_system_user us " +
				" WHERE emp.employee_id=dev.employee_id " +
				" AND us.employee_id=emp.employee_id " +
				" AND us.company_id='"+companyId+"' ";
		if(!employeeIds.equals("-1")){
		  sql+=	" AND emp.employee_id in( "+employeeIds+"'-1') ";
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		System.out.println(sql);
		return list;	
	}
	/**
	 * 查询通讯录版本号.
	 * @return 
	 * 返回类型：int
	 */
    public int getVersionNum(){
    	String sql = " SELECT uv.txluser_version_num as versionNum,uv.update_date" +
    			" FROM tb_b_txluser_version uv" +
    			" ORDER BY uv.update_date DESC";
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
    	return Integer.parseInt(list.get(0).get("versionNum").toString());   	
    }
    
    /**
     * 由部门Id获取父部门Id.
     * @param departmentId 
     * @return 
     * 返回类型：String
     */
    public String getPidByDeptid (String departmentId){
    	String sql = "SELECT dept.parent_department_id AS pid" +
    			" FROM tb_c_department dept" +
    			" WHERE dept.department_id = '"+departmentId+"'";
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
    	return list.get(0).get("pid").toString();    	
    }
     
    
    /**
	 * 逻辑删除用户.
	 * @param employeeId 
	 * 返回类型：void
	 */
	public void delUser(String employeeId){
		Date modify_date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate = sd.format(modify_date);
		String sql = "UPDATE tb_c_system_user SET del_flag='1'," +
				"modify_time = ?  WHERE employee_id=?";
		this.jdbcTemplate.update(sql,nowDate,employeeId);
	}
    
	/**
	 * 查看通讯录,可查看指定部门联系人(企业通讯录).
	 * @param rows 
	 * @param page 
	 * @param deptId 
	 * @param rights 
	 * @param departmentLevel 
	 * @param selfDepartmentId 
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
    public Pagination<?> getGroupInfo(int rows, int page, String deptId, String companyId,String key){
    	Map<String, Object> map = new HashMap<String,Object>();
    	String rowSql = "select uc.*,userDept.department_id,hs.headship_name as headshipName,emp.grid_number " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept,tb_c_headship hs " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			" AND userDept.user_company_id=uc.user_company_id " +
    			" AND userDept.headship_id=hs.headship_id " +
    			"AND uc.del_flag='0' " +
    			"AND emp.del_flag='0' " +
    			"AND userDept.visible_flag='1' "+
    			"AND uc.company_id='"+companyId+"' ";
    	
    	String countSql = "SELECT COUNT(*) " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept ,tb_c_headship hs " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			"AND userDept.user_company_id=uc.user_company_id " +
    			" AND userDept.headship_id=hs.headship_id " +
    			"AND uc.del_flag='0' " +
    			"AND emp.del_flag='0' " +
    			"AND userDept.visible_flag='1' "+
    			"AND uc.company_id='"+companyId+"' ";
         if(null !=key && !key.equals("")){
        	 String searchSql = " AND " +
         			"(uc.employee_name LIKE :key " +
         			"OR uc.mobile LIKE :key " +
         			"OR uc.mobile_short LIKE :key " +
         			"OR uc.tel_short LIKE :key " +
         			"OR uc.telephone2 LIKE :key)";
        	 map.put("key", "%"+key+"%");
        	 rowSql += searchSql;
        	 countSql += searchSql;
         }
    	if(StringUtils.hasText(deptId)){
    		rowSql += "  AND userDept.department_id='"+deptId+"'";
    		countSql += " AND userDept.department_id='"+deptId+"'";
    	}
    	int offset = (page - 1) * rows;
    	rowSql += " ORDER BY uc.display_order,uc.user_company_id limit :offset, :limit ";
    	System.out.println(rowSql);
    	return namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
    }
    
    /**
     * 非企业通讯录.
     * @param rows
     * @param page
     * @param companyId
     * @return 
     * 返回类型：Pagination<?>
     */
    public Pagination<?> getOrgGroupInfo(int rows, int page, String companyId,String key){
    	Map<String, Object> map = new HashMap<String,Object>();
    	String rowSql = "SELECT " +
    			"uc.*,emp.grid_number " +
    			"FROM tb_c_employee emp,tb_c_user_company uc " +
    			"WHERE emp.del_flag = '0' " +
    			"AND uc.del_flag='0' " +
    			"AND uc.employee_id = emp.employee_id " +
    			"AND uc.company_id = '"+companyId+"' ";
    	
    	String countSql = "SELECT " +
    			"COUNT(uc.user_company_id) " +
    			"FROM tb_c_employee emp,tb_c_user_company uc " +
    			"WHERE emp.del_flag = '0' " +
    			"AND uc.del_flag='0' " +
    			"AND uc.employee_id = emp.employee_id " +
    			"AND uc.company_id = '"+companyId+"' ";
    	if(null !=key && !key.equals("")){
    		 String searchSql = " AND " +
          			"(uc.employee_name LIKE :key " +
          			"OR uc.mobile LIKE :key " +
          			"OR uc.mobile_short LIKE :key " +
          			"OR uc.tel_short LIKE :key " +
          			"OR uc.telephone2 LIKE :key)";
         	 map.put("key", "%"+key+"%");
         	 rowSql += searchSql;
         	 countSql += searchSql;
        }
    	int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
    }
    
    /**
	 * 查询用户.
	 * @param rows 
	 * @param page 
	 * @param key 关键字
	 * @param rights 权限
	 * @param departmentLevel 登陆用户部门级别
	 * @param selfDepartmentId 登陆用户部门Id
	 * @param companyName 公司名称
	 * @return 
	 * 返回类型：Pagination<?>
	 */
    public Pagination<?> searchList(int rows, int page, String key, 
    		String rights, int departmentLevel, String selfDepartmentId,
    		String companyName, String companyId){
    	Map<String, Object> map = new HashMap<String,Object>();
    	String searchSql = " AND " +
    			"(emp.employee_name LIKE '%"+key+"%' " +
    			"OR emp.mobile LIKE '%"+key+"%' " +
    			"OR emp.mobile_short LIKE '%"+key+"%' " +
    			"OR emp.tel_short LIKE '%"+key+"%' " +
    			"OR emp.telephone2 LIKE '%"+key+"%')";
    	String rowSql = "SELECT " +
    			"emp.employee_id AS empId," +
    			"emp.employee_name AS empName," +
    			"emp.mobile AS mobileLong," +
    			"emp.mobile_short AS mobileShot," +
    			"emp.tel_short AS telShort," +
    			"emp.telephone2 AS telLong," +
    			"emp.email AS email," +
    			"dept.department_id AS deptId," +
    			"dept.department_name AS deptName," +
    			"IFNULL((SELECT dep.department_name " +
    			"FROM tb_c_department dep " +
    			"WHERE dep.department_id=dept.parent_department_id),'"+companyName+"') " +
    			"AS parentDeptName "+
    			"FROM tb_c_employee emp,tb_c_department dept " +
    			"WHERE emp.department_id=dept.department_id " +
    			"AND dept.company_id='"+companyId+"' " +
    			"AND emp.del_flag='0'" + searchSql;
    	String countSql = "SELECT " +
    			"COUNT(emp.employee_id) " +
    			"FROM tb_c_employee emp,tb_c_department dept " +
    			"WHERE emp.department_id=dept.department_id " +
    			"AND dept.company_id='"+companyId+"' " +
    			"AND emp.del_flag='0'" + searchSql;
    	if(StringUtils.hasText(rights)){
    		String[] rightConfig = rights.split(",");
    		String up = rightConfig[0];//上级标志位
    		String down = rightConfig[2];//下级标志位
    		String self = rightConfig[1];//平级标志位
    		if(!"0".equals(up)){
        		String beginLevel = "";
        		if(Integer.parseInt(up) >= departmentLevel){
        			beginLevel = "1";
        		}else{
        			beginLevel = ""+(departmentLevel - Integer.parseInt(up));
        		}
        		String beginSql1 = " AND (dept.department_level >= '"+beginLevel+"' ";
        		rowSql += beginSql1;
        		countSql += beginSql1;
        	}else{
        		String beginSql2 = " AND (dept.department_level >= '"+departmentLevel+"' ";
        		rowSql += beginSql2;
        		countSql += beginSql2;
        	}
        	if(!"0".equals(down)){
        		//包含下级部门
        		String endSql ="" + (departmentLevel+Integer.parseInt(down));
        		rowSql += " AND dept.department_level <= '"+endSql+"' ";
        		countSql += " AND dept.department_level <= '"+endSql+"' ";
        	}else{
        		rowSql += " AND dept.department_level < '"+departmentLevel+"' ";
        		countSql += " AND dept.department_level < '"+departmentLevel+"' ";
        	}
        	if(!"0".equals(self)){
        		rowSql += " OR dept.department_level = '"+departmentLevel+"') ";
        		countSql += " OR dept.department_level = '"+departmentLevel+"') ";
        	}else{
        		String selfSql = " AND dept.department_level <> '"+departmentLevel+"' " +
        				"OR dept.department_id='"+selfDepartmentId+"') ";
        		rowSql += selfSql;
        		countSql += selfSql;
        	}
    	}
    	int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
    }
    
    /**
	 * 找到部门的最上级部门.
	 * @param departmentName 
	 * @param parentDepartmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String parentDepartmentName(String departmentName, String parentDepartmentId){
		String sql = "SELECT " +
				"dept.department_name AS deptName, " +
				"dept.parent_department_id AS parentId " +
				"FROM tb_c_department dept WHERE dept.department_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDepartmentId);
		if(list.size()==0){
			return "";
		}else{
			String name = list.get(0).get("deptName")==null?"":list.get(0).get("deptName").toString();
			String parentId = list.get(0).get("parentId")==null?"":list.get(0).get("parentId").toString();
			if("0".equals(parentId)){
				return name;
			}else{
				return this.parentDepartmentName(name, parentId);
			}
		}
		
	}
	
	/**
	 * 找到指定部门下人员.
	 * @param departmentId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getEmployeeByDepartmentId(String departmentId){
		String sql = "SELECT " +
				"emp.employee_name AS text," +
				"emp.employee_id AS id " +
				"FROM tb_c_employee emp " +
				"WHERE emp.department_id = '"+departmentId+"' " +
				"AND emp.del_flag='0'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		for(Map<String, Object> map : list){
			map.put("iconCls", "employee");
		}
		return list;
	}
	
	/**
	 *根据手机号码返回员工信息 
	 * @param mobile
	 * @return
	 */
	public Map<String,Object> getEmployeeByMobile(String mobile)
	{
		String sql = "SELECT * FROM tb_c_employee WHERE mobile=?";
		List<Map<String,Object>> list= this.jdbcTemplate.queryForList(sql, mobile);
		return list.size()>0?list.get(0):null;
	}
	/**add by zhangjun 2013/11/15*/
	/**
	 *清空集团内部们及员工数据
	 * @param companyId
	 * @return
	 */
	public void clearGroupByCompany(String companyId)
	{
		
		String sql2 ="select employee_id from tb_c_user_company where company_id=?" 
				+ " and employee_id not in (select distinct employee_id from tb_c_user_company where company_id != ?)";
		List<Map<String,Object>> list= this.jdbcTemplate.queryForList(sql2,companyId,companyId);//查找唯一企业下的员工
	
		String sql5 ="delete from tb_c_user_department where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_company where company_id=?) ";
		this.jdbcTemplate.update(sql5,companyId);//删除用户部门关联表中公司为当前的数据
		String sql8 ="delete from tb_c_user_company_changed where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_company where company_id=?) ";
		this.jdbcTemplate.update(sql8,companyId);//删除用户公司关联版本控制表中公司为当前的数据
		String sql13 ="delete from tb_c_user_modify where user_company_id in "
				+"(select distinct user_company_id from tb_c_user_company where company_id=?) ";
		this.jdbcTemplate.update(sql13,companyId);//删除用户修改表中公司为当前的数据
		String sql6 ="delete from tb_c_user_company where company_id=?";
		this.jdbcTemplate.update(sql6,companyId);//删除用户公司关联表中公司为当前的数据
		String sql7  = "delete from tb_c_department_changed where department_id in"
				+" (select distinct department_id from tb_c_department where company_id =?)";
		this.jdbcTemplate.update(sql7,companyId);//删除部门版本控制表中公司为当前的数据
		String sql = "delete from tb_c_department where company_id =?";
		this.jdbcTemplate.update(sql,companyId);//删除当前公司下的所有部门
		String sql3 ="";
		for (Map<String, Object> map : list) {
			sql3 = "delete from tb_c_employee_medal where employee_id= ?";//删除tb_c_employee_medal表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_employee_param where employee_id= ?";//删除tb_c_employee_param表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_employee_record where employee_id= ?";//删除tb_c_employee_record表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_employee_report where employee_id= ?";//删除tb_c_employee_report表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_token where employee_id= ?";//删除员工令牌表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_device where employee_id= ?";//删除员工设备令牌表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_employee where employee_id= ?";//删除员工表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_user_role where user_id in "+
			"(select user_id from tb_c_system_user where employee_id= ?)";//删除用户角色表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
			sql3 = "delete from tb_c_system_user where employee_id= ?";//删除用户表
			this.jdbcTemplate.update(sql3,map.get("employee_id"));
		}
	}
	
	/**add by zhangjun 2013/11/15*/
	/**
	 * 获取用户所在二级部门
	 * @param companyId
	 * @return
	 */
	public String getUserDepartments(String userCompanyId){
		//String employeeId = SecurityContextUtil.getCurrentUser().getEmployeeId();
		//获取用户所在部门
		String sql1 ="select cd.department_id,cd.parent_department_id from tb_c_department cd,tb_c_user_company c,tb_c_user_department d"
					+" WHERE  c.user_company_id=d.user_company_id and d.department_id=cd.department_id"
					+" and c.user_company_id=?";
		
		String sql2 = "select dept.department_id,dept.parent_department_id FROM tb_c_department dept where dept.department_id=?";//获取上级部门
		
		String seconedDepartmentId = "";
		String parentDepartmentId = "";
		List<Map<String, Object>> list1 = this.jdbcTemplate.queryForList(sql1,userCompanyId);
		if(list1.size()>0){
			parentDepartmentId = list1.get(0).get("parent_department_id").toString();
			seconedDepartmentId = list1.get(0).get("department_id").toString();
			while(!parentDepartmentId.equals("0")){
				List<Map<String, Object>> list2 = this.jdbcTemplate.queryForList(sql2,parentDepartmentId);
				if(list2.size()>0){
					parentDepartmentId = list2.get(0).get("parent_department_id").toString();
					seconedDepartmentId = list2.get(0).get("department_id").toString();
				}
				
			}
		}
		return seconedDepartmentId;
	}
    public List<Map<String, Object>> getRelativeOrder(String deptId, String companyId){
    	String rowSql = "SELECT uc.*,userDept.department_id " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			" AND userDept.user_company_id=uc.user_company_id " +
    			" AND uc.del_flag='0' " +
    			" AND emp.del_flag='0' " +
    			" AND userDept.visible_flag='1' "+
    			" AND uc.company_id=? "+
    			" AND userDept.department_id=?"+
    			" ORDER BY uc.display_order ASC ";
    	
    	return this.jdbcTemplate.queryForList(rowSql,companyId,deptId);
    }
    public Long getRelativeCount(String deptId, String companyId){
    	String rowSql = "SELECT count(*) " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			" AND userDept.user_company_id=uc.user_company_id " +
    			" AND uc.del_flag='0' " +
    			" AND emp.del_flag='0' " +
    			" AND userDept.visible_flag='1' "+
    			" AND uc.company_id=? "+
    			" AND userDept.department_id=?";
    	
    	return this.jdbcTemplate.queryForLong(rowSql,companyId,deptId);
    }
    public List<Map<String, Object>> getUserByOrder(String deptId, String companyId,String realtiveOrder){
    	String rowSql = "SELECT uc.*,userDept.department_id " +
    			"FROM tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept " +
    			"WHERE uc.employee_id=emp.employee_id " +
    			" AND userDept.user_company_id=uc.user_company_id " +
    			" AND uc.del_flag='0' " +
    			" AND emp.del_flag='0' " +
    			" AND userDept.visible_flag='1' "+
    			" AND uc.company_id=:companyId "+
    			" AND userDept.department_id=:deptId"+
    			" ORDER BY uc.display_order ASC "+
    			" limit :realtiveOrder,1";
    	Map<String, Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("companyId", companyId);
    	paramMap.put("deptId", deptId);
    	paramMap.put("realtiveOrder", Integer.valueOf(realtiveOrder));
    	return this.namedParameterJdbcTemplateExt.queryForList(rowSql,paramMap);
    }
    public void updateDisplayOrder(String deptId, String companyId,String displayOrder){
    	String rowSql = "update tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept"+
    			" set uc.display_order=ifnull(uc.display_order,0)+10" +
    			" where uc.employee_id=emp.employee_id AND userDept.user_company_id=uc.user_company_id " +
    			" AND uc.del_flag='0'  AND emp.del_flag='0'  AND userDept.visible_flag='1' AND uc.company_id=:companyId" +
    			" AND userDept.department_id=:deptId and uc.display_order >=:displayOrder";
    	Map<String, Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("companyId", companyId);
    	paramMap.put("deptId", deptId);
    	paramMap.put("displayOrder", Integer.valueOf(displayOrder));
    	this.namedParameterJdbcTemplateExt.update(rowSql,paramMap);
    }
    public List<Map<String,Object>> selectDisplayOrder(String deptId, String companyId,String displayOrder){
    	String rowSql = "select uc.user_company_id from tb_c_employee emp,tb_c_user_company uc,tb_c_user_department userDept"+
    			" where uc.employee_id=emp.employee_id AND userDept.user_company_id=uc.user_company_id " +
    			" AND uc.del_flag='0'  AND emp.del_flag='0'  AND userDept.visible_flag='1' AND uc.company_id=:companyId" +
    			" AND userDept.department_id=:deptId and uc.display_order >=:displayOrder";
    	Map<String, Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("companyId", companyId);
    	paramMap.put("deptId", deptId);
    	paramMap.put("displayOrder", Integer.valueOf(displayOrder));
    	return this.namedParameterJdbcTemplateExt.queryForList(rowSql,paramMap);
    }
    /**
	 * 部门显示顺序
	 * @param userCompanyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public Long getUserRelativeInDept(String userCompanyId,String departmentId){
		String sql = "select row from (select (@row := @row + 1) as row,user_company_id from ("+
				"SELECT uc.user_company_id " +
				"FROM tb_c_user_company uc,tb_c_employee emp,tb_c_user_department udept " +
				"WHERE emp.employee_id=uc.employee_id" +
				" AND uc.del_flag='0' AND emp.del_flag='0' AND udept.visible_flag='1' AND udept.user_company_id=uc.user_company_id "+
				" AND udept.department_id=? order by uc.display_order) x ,(SELECT @row := 0) r ) y where user_company_id=? limit 1";
		return this.jdbcTemplate.queryForLong(sql,departmentId,userCompanyId);
	}
}



