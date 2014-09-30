// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DeptMagDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-5 下午5:31:02
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class DeptMagDao extends HibernateBaseDaoImpl<Department, String>{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	/**
	 * 获取某个部门的子部门.
	 * @param parentDepartmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptList(String parentDepartmentId){
		String sql = "SELECT " +
				"dept.department_name AS text, " +
				"dept.department_id AS id," +
				"dept.fax AS fax," +
				"dept.parent_department_id AS parentId," +
				"dept.department_level AS deptLevel," +
				"dept.display_order AS displayOrder," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id) AS num  " +
				"FROM tb_c_department dept " +
				"WHERE dept.parent_department_id=? " +
				"AND dept.del_flag='0' " +
				"ORDER BY dept.display_order ASC ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDepartmentId);
		int size = list.size();
		for(int i=0;i<size;i++){
			Map<String, Object> firstMap = new HashMap<String, Object>();
			Map<String, Object> map = list.get(i);
			firstMap.put("parent", map.get("parentId").toString());
			firstMap.put("deptLevel", map.get("deptLevel").toString());
			firstMap.put("relativeOrder", i+1);
			firstMap.put("departmentNum", size);
			firstMap.put("displayOrder", map.get("displayOrder")==null?"9999":map.get("displayOrder").toString());
			firstMap.put("fax", map.get("fax")==null?"":map.get("fax").toString());
			map.put("attributes", firstMap);
			if(("0").equals(map.get("num").toString())) {
				map.put("isLeaf", "Y");
				map.put("state", "closed");
			}else{
				map.put("isLeaf", "N");
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 获取某个部门的子部门.
	 * @param parentDepartmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptList(String parentDepartmentId,String companyId){
		String sql = "SELECT " +
				"dept.department_name AS text, " +
				"dept.department_id AS id," +
				"dept.fax AS fax," +
				"dept.parent_department_id AS parentId," +
				"dept.department_level AS deptLevel," +
				"dept.display_order AS displayOrder," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id) AS num  " +
				"FROM tb_c_department dept " +
				"WHERE dept.parent_department_id=? " +
				"AND dept.del_flag='0' " +
				"AND dept.company_id = '"+companyId+"'" +
				"ORDER BY dept.display_order ASC ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDepartmentId);
		int size = list.size();
		for(int i=0;i<size;i++){
			Map<String, Object> firstMap = new HashMap<String, Object>();
			Map<String, Object> map = list.get(i);
			firstMap.put("parent", map.get("parentId").toString());
			firstMap.put("deptLevel", map.get("deptLevel").toString());
			firstMap.put("relativeOrder", i+1);
			firstMap.put("departmentNum", size);
			firstMap.put("displayOrder", map.get("displayOrder")==null?"9999":map.get("displayOrder").toString());
			firstMap.put("fax", map.get("fax")==null?"":map.get("fax").toString());
			map.put("attributes", firstMap);
			if(("0").equals(map.get("num").toString())) {
				map.put("isLeaf", "Y");
				map.put("state", "closed");
			}else{
				map.put("isLeaf", "N");
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 找到部门下的人员.
	 * @param departmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> empList(String departmentId){
		String sql = "SELECT emp.employee_id AS empId " +
				"FROM tb_c_employee emp WHERE emp.del_flag='0' AND emp.department_id=?";
		return this.jdbcTemplate.queryForList(sql,departmentId);
	}
	/**
	 * 根据部门ids,找到一个部门或多个部门下的人员.
	 * @param departmentId 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> empListByIds(String departmentIds){
		String sql = "SELECT emp.employee_id AS userId,emp.employee_name AS usreName " +
				"FROM tb_c_employee emp WHERE emp.del_flag='0' AND emp.department_id in("+departmentIds+")";
		System.out.println(departmentIds);
		return this.jdbcTemplate.queryForList(sql);
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
	 * 测试部门是否存在.
	 * @param i 
	 * @param fullName 
	 * @return 
	 * 返回类型：boolean
	 */
	public String checkDept(int i, String fullName,String company_id,String parentDepartmentId){
		String[] full = fullName.split("[-]");
		String selectSql = "SELECT ";
		String fromSql = " FROM ";
		String whereSql = " WHERE ";
		for(int j =i;j>0;j--){
			if(j==i){
				whereSql += " dept"+j+".department_name='"+full[j]+"'AND  dept"+j+".department_level='"+j+"' AND dept"+j+".company_id='"+company_id+"' AND dept"+j+".del_flag='0'";
			}else{
				int k = j+1;
				whereSql += " AND dept"+k+".parent_department_id=dept"+
						j+".department_id AND dept"+j+".department_name='"+full[j]+"' AND  dept"+j+".department_level='"+j+"' AND dept"+j+".company_id='"+company_id+"' AND dept"+j+".del_flag='0'";
			}
			if(j==i){
				selectSql += "dept"+j+".department_name AS deptName"+j+", " +
						"dept"+j+".department_id AS deptId"+j;
				fromSql += "tb_c_department dept"+j;
			}else{
				selectSql +="," + "dept"+j+".department_name AS deptName"+j+", " +
						"dept"+j+".department_id AS deptId"+j+" ";
				fromSql +="," + "tb_c_department dept"+j;
			}
			
		}
		whereSql+=" and dept"+i+".parent_department_id='"+parentDepartmentId+"'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(selectSql+fromSql+whereSql);
		if(list.size()>0){
			if(i==1){
				return list.get(0).get("deptId1").toString();
			}else{
				String parentId = "deptId"+i;
				return list.get(0).get(""+parentId+"").toString();
			}
		}else{
			return null;
		}
		
	}
	
	/**
	 * 获取当前登陆人所在公司的部门级别.
	 * @param companyId 
	 * @return 
	 * 返回类型：int
	 */
	public int deptLevel(String companyId){
		String sql = "SELECT dept.department_level as level " +
				"FROM tb_c_department dept WHERE dept.company_id=? " +
				"GROUP BY dept.department_level " +
				"ORDER BY dept.department_level DESC";
		List<Map<String, Object>> levelList = this.jdbcTemplate.queryForList(sql,companyId);
		if(levelList.size()>0){
			return Integer.parseInt(levelList.get(0).get("level").toString());
		}else{
			return 0;
		}
		
	}
	/**
	 * 根据部门级别获取部门
	 * @param rows
	 * @param page
	 * @param companyId
	 * @param parentDeptId
	 * @param departmentLevel
	 * @return
	 */
	public Pagination<?> getDepartment(int rows, int page, String companyId,String parentDeptId,String departmentLevel){
		Map<String, Object> map = new HashMap<String,Object>();
		String sql ="select * from tb_c_department where "+
					" company_id='"+companyId+"' and department_level='"+departmentLevel+"' and del_flag='0' ";
				
		String countSql = "select count(*) from tb_c_department where "+
					" company_id='"+companyId+"' and department_level='"+departmentLevel+"' and del_flag='0'";
		if(null != parentDeptId && !"".equals(parentDeptId)){
			sql +=" and parent_department_id='"+parentDeptId+"'";
			countSql +=" and parent_department_id='"+parentDeptId+"'";
		}
		int offset = (page - 1) * rows;
		sql += " ORDER BY display_order ASC limit :offset, :limit ";
    	System.out.println(sql);
    	return namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, rows, map);
		
	}
	/**
	 * 部门树,异步加载.
	 * @param parentDeptId 父部门Id
	 * @param isRecycle 是否为回收站的部门加载
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptTree(String parentDeptId,String companyId,String isRecycle){
		String sql = "SELECT " +
				"dept.department_name AS text, " +
				"dept.department_id AS id," +
				"dept.fax AS fax," +
				"dept.parent_department_id AS parentId," +
				"dept.department_level AS deptLevel," +
				"dept.display_order AS displayOrder," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id";
		if(isRecycle==null||isRecycle.equals("")){
			sql+=" AND d.del_flag='0'";
		}
		sql +=") AS num  FROM tb_c_department dept " +
				"WHERE dept.parent_department_id=? " ;
		if(isRecycle!=null&&!isRecycle.equals("")){
			sql +=" AND dept.company_id = '"+companyId+"'" 
					/*+" and (select count(*) from tb_c_user_department where department_id in ("
					+" select a.department_id from  tb_c_department a where a.department_id=dept.department_id"

					+" union "
					+" select b.department_id from tb_c_department a left join tb_c_department b on  a.department_id=b.parent_department_id"
					
					+" where a.department_id=dept.department_id and b.department_id is not null"
					+" UNION"
					+" select c.department_id from tb_c_department a left join tb_c_department b on  a.department_id=b.parent_department_id"
					
					+" left join tb_c_department c on b.department_id = c.parent_department_id "
					
					+" where a.department_id=dept.department_id and c.department_id is not null"
					+" UNION"
					+" select d.department_id"
					
					+" from tb_c_department a left join tb_c_department b on  a.department_id=b.parent_department_id"
					
					+" left join tb_c_department c on b.department_id = c.parent_department_id  LEFT JOIN tb_c_department d on c.department_id = d.parent_department_id"
					
					+" where a.department_id=dept.department_id and d.department_id is not null"
					+") and visible_flag='0')>0"*/
					+" ORDER BY dept.modify_time desc ";
		}else{
			sql +=" AND dept.del_flag='0' AND dept.company_id = '"+companyId+"'" +
					"ORDER BY dept.display_order ASC ";
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDeptId);
		int size = list.size();
		for(int i=0;i<size;i++){
			Map<String, Object> firstMap = new HashMap<String, Object>();
			Map<String, Object> map = list.get(i);
			firstMap.put("parent", map.get("parentId").toString());
			firstMap.put("deptLevel", map.get("deptLevel").toString());
			firstMap.put("relativeOrder", i+1);
			firstMap.put("departmentNum", size);
			firstMap.put("displayOrder", map.get("displayOrder")==null?"9999":map.get("displayOrder").toString());
			firstMap.put("fax", map.get("fax")==null?"":map.get("fax").toString());
			map.put("attributes", firstMap);
			if(("0").equals(map.get("num").toString())) {
				map.put("isLeaf", "Y");
				map.put("state", "open");
			}else{
				map.put("isLeaf", "N");
				map.put("state", "closed");
			}
		}
		return list;
	}
	/**
	 * 根据分公司ID，部门树,异步加载.
	 * @param parentDeptId 父部门Id
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> deptTreeByCompanyId(String companyId,String parentDeptId){
		String sql = "SELECT " +
				"dept.department_name AS text, " +
				"dept.department_id AS id," +
				"dept.fax AS fax," +
				"dept.parent_department_id AS parentId," +
				"dept.display_order AS displayOrder," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id) AS num  " +
				"FROM tb_c_department dept " +
				"WHERE dept.parent_department_id=? " +
				"AND dept.del_flag='0' " +
				"AND dept.company_id = '"+companyId+"'" +
				"ORDER BY dept.display_order ASC ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,parentDeptId);
		for(Map<String, Object> map : list){
			Map<String, Object> firstMap = new HashMap<String, Object>();
			firstMap.put("parent", map.get("parentId").toString());
			firstMap.put("displayOrder", map.get("displayOrder")==null?"9999":map.get("displayOrder").toString());
			firstMap.put("fax", map.get("fax")==null?"":map.get("fax").toString());
			map.put("attributes", firstMap);
			if(("0").equals(map.get("num").toString())) {
				map.put("isLeaf", "Y");
				map.put("state", "open");
			}else{
				map.put("isLeaf", "N");
				map.put("state", "closed");
			}
		}
		return list;
	}
	/**
	 * 判断同一部门下是否有同名的部门.
	 * @param departmentName 部门名字
	 * @param parentDepartmentId 上级部门Id
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkDeptName(String departmentName, String parentDepartmentId,String companyId){
		String sql = "SELECT " +
				"COUNT(dept.department_id) " +
				"FROM tb_c_department dept " +
				"WHERE dept.department_name=? " +
				"AND dept.parent_department_id=? AND company_id=? and del_flag='0'";
		int num = this.jdbcTemplate.queryForInt(sql,departmentName,parentDepartmentId,companyId);
		if(num==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDept(String deptId,String deptIds){
		String sql = "SELECT *," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id AND del_flag='0') AS num  "  +
				" FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.parent_department_id =? ";
		List<Map<String, Object>> list= this.jdbcTemplate.queryForList(sql,deptId);
		if(list.size()>0){
			for(Map<String, Object> map:list){
				deptIds += ","+"'"+map.get("department_id").toString()+"'";
				if(!map.get("num").toString().equals("0")){
					deptIds = this.allChildrenDept(map.get("department_id").toString(),deptIds);
				}
			}
		}
		return deptIds;
	}
	
	/**
	 * 找出指定部门下所有子部门及子部门的子部门..
	 * @param deptId
	 * @return
	 */
	public String allChildrenDeptNew(String deptId){
		String sql = "SELECT department_id,parent_department_id "  +
				" FROM tb_c_department dept WHERE dept.del_flag='0' AND dept.parent_department_id =? ";
		List<Map<String, Object>> list= this.jdbcTemplate.queryForList(sql,deptId);
		if(list.size()>0){
			deptId = "'" + deptId + "'";
			for(Map<String, Object> map:list){
				deptId += "," + this.allChildrenDeptNew(map.get("department_id").toString());
			}
			return deptId;
		}else {
			return "'" + deptId + "'";
		}
	}
	
	
	/**
	 * 找到指定部门的最上级部门,返回其部门名称.
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String getParentDepartmentName(String departmentId){
		String sql = "SELECT " +
				"dept.department_id AS departmentId," +
				"dept.department_name AS departmentName," +
				"dept.parent_department_id AS parentDepartmentId " +
				"FROM tb_c_department dept WHERE dept.department_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,departmentId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()>0){
			map = list.get(0);
			String parentDepartmentId = map.get("parentDepartmentId")==null?"":map.get("parentDepartmentId").toString();
			String departmentName = map.get("departmentName")==null?"空":map.get("departmentName").toString();
			if("0".equals(parentDepartmentId)){
				return departmentName;
			}else{
				return this.getParentDepartmentName(parentDepartmentId);
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 获取当前公司部门最大排序
	 * @param companyId
	 * @return
	 */
	public int getMaxDisplayOrder(String companyId)
	{
		String sql = "SELECT IFNULL(MAX(display_order),0) FROM tb_c_department WHERE " +
				"company_id=? AND department_name!='移动客服'";
		return this.jdbcTemplate.queryForInt(sql, companyId);
	}
	
	/**
	 * 将旧的二级部门名字更新为新的.
	 * @param newDepartmentName 
	 * @param oldDepartmentName  
	 * 返回类型：void
	 */
	public void updateAllDepartmentName(String newDepartmentName,String oldDepartmentName){
		String sql = "UPDATE tb_c_employee " +
				"SET parent_department_name =? " +
				"WHERE parent_department_name =?";
		this.jdbcTemplate.update(sql,newDepartmentName,oldDepartmentName);
	}
	
	public void updatePartDepartmentName(String departmentId,String departmentName){
		String sql = "";
		System.out.println(sql);
	}
	
	/**
	 * 根据部门ids,找到一个部门或多个部门下的人员.
	 * @param departmentIds 
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> empsByDepts(String departmentIds) {
		//再往下找一级
		String sql = " SELECT emp.employee_id AS userId, emp.employee_name AS userName " +
				" FROM tb_c_employee emp WHERE emp.del_flag='0' " +
				" AND emp.employee_id IN (SELECT employee_id FROM tb_c_device) " +
				" AND emp.department_id in ( " + departmentIds + " ) ";

		String[] depts = departmentIds.split(",");
		//再往下找两级
		//1.第一级
		for(String dept : depts)
		{
			departmentIds = allChildrenDept(dept.replace("'",""), departmentIds);
		}
		
		//2.第二级
		depts = departmentIds.split(",");
		//再往下找两级
		//1.第一级
		for(String dept : depts)
		{
			departmentIds = allChildrenDept(dept.replace("'",""), departmentIds);
		}
		sql = " SELECT emp.employee_id AS userId, emp.employee_name AS userName " +
				" FROM tb_c_employee emp WHERE emp.del_flag='0' " +
				" AND emp.employee_id IN (SELECT employee_id FROM tb_c_device) " +
				" AND emp.department_id in ( " + departmentIds + " ) ";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	/**
	 * 当前用户所在二级部门树
	 * @param companyId
	 * @param isRecycle
	 * @return
	 */
	public List<Map<String, Object>> curSeconedDeptTree(String companyId,String isRecycle){
		
		String employeeId = SecurityContextUtil.getCurrentUser().getEmployeeId();
		//获取当前用户所在部门
		String sql1 ="select cd.department_id,cd.parent_department_id from tb_c_department cd,tb_c_user_company c,tb_c_user_department d"
					+" WHERE  c.user_company_id=d.user_company_id and d.department_id=cd.department_id and d.visible_flag='1' "
					+" and c.employee_id='"+employeeId+"' and c.company_id='"+companyId+"' and c.manage_flag='3'";
		
		String sql2 = "select dept.department_id,dept.parent_department_id FROM tb_c_department dept where dept.department_id=?";//获取上级部门
		
		String seconedDepartmentId = "";
		String parentDepartmentId = "";
		List<Map<String, Object>> list1 = this.jdbcTemplate.queryForList(sql1);
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
		String sql = "SELECT " +
				"dept.department_name AS text, " +
				"dept.department_id AS id," +
				"dept.fax AS fax," +
				"dept.parent_department_id AS parentId," +
				"dept.display_order AS displayOrder," +
				"(SELECT COUNT(*)  FROM tb_c_department d " +
				"WHERE d.parent_department_id=dept.department_id) AS num  " +
				"FROM tb_c_department dept " +
				"WHERE dept.department_id='"+seconedDepartmentId+"' " ;
		if(isRecycle!=null&&!isRecycle.equals("")){
			sql +=" AND dept.company_id = '"+companyId+"'" +
					" and ((select count(*) from tb_c_user_department where department_id=dept.department_id and visible_flag='0')>0 "
					+" or (select count(*) from tb_c_user_department a LEFT JOIN tb_c_department b on a.department_id=b.department_id "
					+" where b.parent_department_id=dept.department_id and a.visible_flag='0')>0)"
					+" ORDER BY dept.modify_time desc ";
		}else{
			sql +=" AND dept.del_flag='0' AND dept.company_id = '"+companyId+"'" +
					"ORDER BY dept.display_order ASC ";
		}
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		for(Map<String, Object> map : list){
			Map<String, Object> firstMap = new HashMap<String, Object>();
			firstMap.put("parent", map.get("parentId").toString());
			firstMap.put("displayOrder", map.get("displayOrder")==null?"9999":map.get("displayOrder").toString());
			firstMap.put("fax", map.get("fax")==null?"":map.get("fax").toString());
			map.put("attributes", firstMap);
			if(("0").equals(map.get("num").toString())) {
				map.put("isLeaf", "Y");
				map.put("state", "open");
			}else{
				map.put("isLeaf", "N");
				map.put("state", "closed");
			}
		}
		return list;
	}
	public void updateDisplayOrder(String parentDepartmentId,String companyId,int displayOrder){
		String sql="update  tb_c_department dept set dept.display_order=IFNULL(dept.display_order,0)+10"+
				" where dept.parent_department_id=? AND dept.del_flag='0' AND dept.company_id = ?"+
				" and dept.display_order>=?";
		this.jdbcTemplate.update(sql,parentDepartmentId,companyId,displayOrder);
	}
	public List<Map<String, Object>> selectDisplayOrder(String parentDepartmentId,String companyId,int displayOrder){
		String sql="select dept.department_id from  tb_c_department dept "+
				" where dept.parent_department_id=? AND dept.del_flag='0' AND dept.company_id = ?"+
				" and dept.display_order>=?";
		return this.jdbcTemplate.queryForList(sql,parentDepartmentId,companyId,displayOrder);
	}
    /**
     * 获取当前部门所在的同级部门中的相对位置
     * @param companyId
     * @param parentDeptId
     * @param departmentId
     * @return
     */
    public Long getRelativeOrderById(String companyId,String parentDeptId,String departmentId){
    	String sql = "select row from (select (@row := @row + 1) as row ,d.* from"+
              "(select * from tb_c_department where company_id=?"+
    		  " and parent_department_id=? and del_flag='0' order by display_order) as d,(SELECT @row := 0) r) as v"+
    			" where department_id=?";
    	return this.jdbcTemplate.queryForLong(sql,companyId,parentDeptId,departmentId);
    }
    /**
	 * 拖拽排序后更新display_order
	 * @param departmentId
	 * @param displayOrder
	 */
	public void updateDisplayOrderById(String parentDeptId,String deptLevel,String departmentId,int displayOrder){
		String sql1 = "update tb_c_department set display_order=?,parent_department_id=?,department_level=? where department_id=?";
		this.jdbcTemplate.update(sql1, displayOrder,parentDeptId,deptLevel,departmentId);
	}
	public Pagination<?> getSecondDepartment(String companyId,String employeeId){
		
		//获取当前用户所在部门
		String sql1 ="select cd.department_id,cd.parent_department_id from tb_c_department cd,tb_c_user_company c,tb_c_user_department d"
					+" WHERE  c.user_company_id=d.user_company_id and d.department_id=cd.department_id and d.visible_flag='1' "
					+" and c.employee_id='"+employeeId+"' and c.company_id='"+companyId+"' and c.manage_flag='3'";
		
		String sql2 = "select dept.department_id,dept.parent_department_id FROM tb_c_department dept where dept.department_id=?";//获取上级部门
		
		String seconedDepartmentId = "";
		String parentDepartmentId = "";
		List<Map<String, Object>> list1 = this.jdbcTemplate.queryForList(sql1);
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
		Map<String, Object> map = new HashMap<String,Object>();
		String sql ="select department_id as id,parent_department_id as pid,department_name as text, " 
				+" department_level,company_id from tb_c_department where "+
					" department_id='"+seconedDepartmentId+"' and del_flag='0' ";
				
		String countSql = "select count(*) from tb_c_department where "+
				" department_id='"+seconedDepartmentId+"' and del_flag='0' ";
		int offset = 0;
		sql += " limit :offset, :limit ";
    	return namedParameterJdbcTemplateExt.queryPage(sql, countSql, offset, 10, map);
	}
	
	public List<Map<String,Object>> getUserCompanyByDepartmentId(String department_id)
	{
		String sql = "SELECT * FROM tb_c_user_company WHERE user_company_id IN (" +
				"SELECT distinct user_company_id FROM tb_c_user_department WHERE department_id=?) AND del_flag!='1'";
		return this.jdbcTemplate.queryForList(sql,department_id);
	}
	
	public List<Map<String,Object>> getUserCompanyByDeptName(String deptName,String companyId){
		String sql="select user_company_id,department_name from tb_c_user_company where company_id=? and department_name REGEXP ?";
		return this.jdbcTemplate.queryForList(sql, companyId,deptName+"(-.+)?$");
	}
	public List<Map<String,Object>> treeOfUserCompany(String companyId,String employeeId){
		//获取当前用户所在部门
		String sql1 ="select cd.department_id as id,cd.parent_department_id as pid,cd.department_name as text, " 
					+" cd.department_level,cd.company_id from tb_c_department cd,tb_c_user_company c,tb_c_user_department d"
					+" WHERE  c.user_company_id=d.user_company_id and d.department_id=cd.department_id "
					+" and d.visible_flag='1' and cd.del_flag='0' and c.del_flag='0'"
					+" and c.employee_id='"+employeeId+"' and c.company_id='"+companyId+"'";
		
		List<Map<String, Object>> list1 = this.jdbcTemplate.queryForList(sql1);
		return treeOfDeptList(list1);
	}
	public List<Map<String,Object>> treeOfDeptList(List<Map<String, Object>> list){
		List<Map<String,Object>> ListTemp =new ArrayList<Map<String,Object>>();
		String sql2 = "select dept.department_id as id,dept.parent_department_id as pid,dept.department_name as text, " +
				"dept.department_level,dept.company_id FROM tb_c_department dept where dept.department_id=?";//获取上级部门
		if(null != list && list.size()>0){
			String parentDepartmentId = "";
			for (Map<String, Object> map : list) {
				Map<String, Object> attrMap = new HashMap<String, Object>();
				attrMap.put("pid", map.get("pid").toString());
				map.put("attributes", attrMap);
				if(!ListTemp.contains(map)){
					ListTemp.add(map);
				}
				parentDepartmentId = map.get("pid").toString();
				while(!parentDepartmentId.equals("0")){
					Map<String, Object> mapTemp = this.jdbcTemplate.queryForMap(sql2,parentDepartmentId);
					if(null != mapTemp && !mapTemp.isEmpty()){
						attrMap = new HashMap<String, Object>();
						attrMap.put("pid", mapTemp.get("pid").toString());
						mapTemp.put("attributes", attrMap);
						parentDepartmentId = mapTemp.get("pid").toString();
						if(!ListTemp.contains(mapTemp)){
							ListTemp.add(mapTemp);
						}
					}
					
				}
			}
		}
		 List<Map<String, Object>> list0 = new ArrayList<Map<String,Object>>();
		 for(Map<String, Object> mapTemp : ListTemp ){
			if("0".equals(mapTemp.get("pid").toString())){
				list0.add(mapTemp);
			}
		}
		return getChildren(list0,ListTemp);
	}
	public List<Map<String, Object>> getChildren (List<Map<String, Object>> list0,List<Map<String, Object>> list){
		for(Map<String, Object> map : list0){
			String parentDepartmentId = map.get("id").toString();
			List<Map<String, Object>> childList=new ArrayList<Map<String,Object>>();;
			for(Map<String, Object> mapTemp : list){
				if(parentDepartmentId.equals(mapTemp.get("pid").toString())){
					childList.add(mapTemp);
				}
			}
			getChildren(childList,list);
			if(null != childList&&childList.size()>0){
				map.put("children", childList);
			}
			map.put("state", "open");
		}
		return list0;
	}
	/**
	 * 获取部门管理员管理的项目
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	public List<Map<String,Object>> getManageDept(String companyId,String employeeId){
		String sql="select cd.department_id as id,cd.parent_department_id as pid,cd.department_name as text, " 
				+" cd.department_level,cd.company_id from tb_c_department cd, tb_c_user_manager m " +
				"where m.department_id=cd.department_id and cd.del_flag='0' and m.employee_id=? and m.company_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, employeeId,companyId);
		return treeOfDeptList(list);
	}
	public List<Map<String,Object>>	 getDeptOfManage(String companyId,String employeeId){
		String sql="select department_id as id from tb_c_user_manager where employee_id=? and company_id=?";
		return this.jdbcTemplate.queryForList(sql, employeeId,companyId);
	}
}
