// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.UCache;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：EmployeeDao
 * <br />版本:1.0.0
 * <br />日期： 2013-3-7 下午3:17:44
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class MEmployeeDao extends HibernateBaseDaoImpl<Employee, String>{
	private static final Logger LOG=Logger.getLogger(MEmployeeDao.class);
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private CacheDao cacheDao;

	//阜阳公司固定SPID
	private static final  String AYD_FUYANG="023";
	
	/**
	 * 下载联系人信息.
	 * @param departmentIdStr 手机客户端存储的登录人上次登录时所在部门
	 * 2013.12.12为了兼容安徽移动版升级至标准版，出现user_id对应不起来的情况，把手机号码也一道传上来，使用departmentIdStr，反正这个接口也用不了多久了，好吧
	 * @param selfDepartmentId 废弃字段
	 * @param versionNum 手机客户端存储的登录人上次登录时服务器端人员版本号
	 * @param companyId 废弃字段
	 * @param filter_company：2013/11/12新增参数，更新接口只更新有用户的企业，由客户端上传需要过滤的编号，用逗号分隔
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getUserInfoByDeptId(String userId,String departmentIdStr, String selfDepartmentId,String versionNum,String companyId,String filter_company) throws IOException, ClassNotFoundException{	
		//返回数据列表
		Map<String, Object> map = new HashMap<String, Object>();
		String companyFilterId = "";
		if(StringUtils.hasText(departmentIdStr)&&departmentIdStr.length()==11)
		{
			//这个时候传上来的是手机号码，根据手机号码找出user_id
			String usql = "SELECT user_id FROM tb_c_system_user WHERE del_flag='0' and (user_id=? or user_name=?)";
			List<Map<String,Object>> userList = this.jdbcTemplate.queryForList(usql,userId,userId);
			if(userList==null||userList.isEmpty()){
				map.put("updateFlag", "0");		
				return map;
			}
			userId = userList.get(0).get("user_id").toString();
		}
		//非首次下载，下载所有更新数据
		if(!"0".equals(versionNum)){
			companyFilterId = getSpliteCompanyIds(filter_company);
			//加一个判断，如果当前人员不需要更新就停止
			String compSQL ="select 1 "+
			"from  tb_c_user_company t0 "+
			"join tb_c_user_company_changed t1 on t0.user_company_id=t1.user_company_id "+
			"where t0.company_id  in( "+
			"select DISTINCT company_id  "+
			"from tb_c_user_company where del_flag='0'  "+
			"and employee_id in(select employee_id from tb_c_system_user where user_id=? and del_flag='0') "+
			(StringUtils.hasText(companyFilterId)?" and company_id not in ("+companyFilterId+")":"")+
			") and t1.group_version_num>? limit 0,1";
			List<String> changes=this.jdbcTemplate.queryForList(compSQL,String.class,userId,versionNum);
			if(changes==null||changes.isEmpty()){
				map.put("updateFlag", "0");		
				return map;
			}
		}else{
			//首次下载第一家公司
			String compSQL = "SELECT " +
					"comp.company_id AS company_id," +
					"comp.company_name AS group_name," +
					"comp.org_flag AS org_flag," +
					"IFNULL(comp.index_logo,'') AS index_log,IFNULL(index_pictrue,'') AS index_pictrue," +
					"temp.company_version AS company_version " +
					"FROM tb_c_company comp, tb_c_user_company uc,tb_c_employee emp,tb_c_system_user us," +
					"(SELECT * FROM tb_c_company_changed cc ORDER BY cc.company_version DESC) AS temp " +
					"WHERE comp.company_id=uc.company_id " +
					"AND emp.employee_id=uc.employee_id " +
					"AND us.employee_id=emp.employee_id " +
					"AND us.user_id=? " +
					"AND temp.company_id=comp.company_id AND uc.del_flag='0' AND comp.del_flag='0' AND emp.del_flag='0' AND us.del_flag='0' " +
					"GROUP BY temp.company_id ORDER " +
					"BY comp.org_flag DESC,comp.company_name ASC limit 0,1 ";
			List<Map<String,Object>> compList = this.jdbcTemplate.queryForList(compSQL, userId);
			if(compList==null||compList.isEmpty()){
				map.put("updateFlag", "0");		
				return map;
			}
			companyId = compList.get(0).get("company_id").toString();
		}
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		//按用户职位级别建立缓存文件
		String uSql = "SELECT headship_level FROM tb_c_headship " +
				" WHERE headship_id=(SELECT headship_id FROM tb_c_employee WHERE " +
				"employee_id=(SELECT employee_id FROM tb_c_system_user where user_id=?))";
		int headship_level = this.jdbcTemplate.queryForInt(uSql, userId);
		String retContent = "";
		//公司编号
		if((companyId.equals("8a1896523c29d5ec013c29da5f0f0000") || 
				companyId.equals("f8510a2d428f3da40142995e272828e2")||
				"f8510a2d41027f4b0141156d5ae63dd9".equals(companyId))
				&& versionNum.equals("0")){
			String cSql = "SELECT * FROM tb_c_cache WHERE company_id=? AND h=?";
			List<Map<String,Object>> clist = this.jdbcTemplate.queryForList(cSql, companyId,String.valueOf(headship_level));
			if(clist.size()>0)
			{
				retContent = clist.get(0).get("content").toString();
			}
		}
		//新增逻辑：为了客户端用户修改号码后不能更新问题设计：
		List<Map<String,Object>> updateMobileList = new ArrayList<Map<String,Object>>();
		if(!"0".equals(versionNum))
		{
			String upSql = "SELECT " +
					"group_version_num,'2' as update_type,'' AS department_fax," +
					"1 AS display_order,'' AS picture,user_company_id,'' AS employee_id," +
					"'' AS employee_name,'' AS department_id,'' AS department_name," +
					"'' AS headship_id,'' AS headship_name,'' AS company_id,'' AS company_name," +
					"'' AS company_address,mark1 AS mobile,'' AS mobile_short,'' AS tel," +
					"'' AS tel_short,'' AS email,'' AS home_telephone,'' AS weibo," +
					"'' AS weixin,'' AS qq,'' AS school,'' AS user_major,'' AS user_grade," +
					"'' AS user_class,'' AS student_id,'' AS birthday,'' AS native_place," +
					"'' AS address,'' AS home_address,'' AS manage_flag,'' AS employee_firstword," +
					"'' AS employee_fullword,'' AS mood,'' AS user_company,'' AS parent_department_name," +
					"'' AS remark,'0' AS visible_flag,'' AS headship_level" +
					" FROM tb_c_user_company_changed WHERE " +
					"(mark1 is not null AND mark1!='') AND group_version_num>'"+versionNum+"'";
			updateMobileList = this.jdbcTemplate.queryForList(upSql);
		}
		
		if(StringUtils.hasText(retContent))
		{
			retList = (List<Map<String, Object>>) JSONArray.parse(retContent);
		}
		else
		{
			String rowSql = "SELECT " +
					"temp.group_version_num," +
					"temp.update_type as update_type," +
					"IFNULL(emp.grid_number,'8888') AS department_fax," +
					"IFNULL(emp.display_order,999999) AS display_order," +
					"IFNULL(uc.picture,'') AS picture, " +
					"uc.user_company_id AS user_company_id," +
					"uc.employee_id AS employee_id," +
					"uc.employee_name AS employee_name," +
					"emp.department_id AS department_id," +
					"uc.department_name AS department_name," +
					"emp.headship_id AS headship_id," +
					"IFNULL(uc.headship_name,'') AS headship_name," +
					"comp.company_id AS company_id," +
					"comp.company_name AS company_name," +
					"IFNULL(comp.company_address,'') AS company_address," +
					"uc.mobile AS mobile," +
					"IFNULL(uc.mobile_short,'') AS mobile_short," +
					"IFNULL(uc.telephone2,'') AS tel," +
					"IFNULL(uc.tel_short,'') AS tel_short," +
					"IFNULL(uc.email,'') AS email," +
					"IFNULL(uc.telephone,'') AS home_telephone," +
					"IFNULL(uc.weibo,'') AS weibo," +
					"IFNULL(uc.weixin,'') AS weixin," +
					"IFNULL(uc.qq,'') AS qq," +
					"IFNULL(uc.school,'') AS school," +
					"IFNULL(uc.user_major,'') AS user_major," +
					"IFNULL(uc.user_grade,'') AS user_grade," +
					"IFNULL(uc.user_class,'') AS user_class," +
					"IFNULL(uc.student_id,'') AS student_id," +
					"IFNULL(uc.birthday,'') AS birthday," +
					"IFNULL(uc.native_place,'') AS native_place," +
					"IFNULL(uc.address,'') AS address," +
					"IFNULL(uc.home_address,'') AS home_address," +
					"IFNULL(uc.manage_flag,'0') AS manage_flag," +
					"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
					"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
					"IFNULL(uc.mood,'') AS mood," +
					"IFNULL(uc.user_company,'') AS user_company," + 
					"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
					"IFNULL(uc.remark,'') AS remark,'1' AS visible_flag," +
					"IFNULL(hs.headship_level,'') AS headship_level " +
					"FROM " +
					"tb_c_user_company uc " +
					" LEFT JOIN tb_c_company comp ON  comp.company_id = uc.company_id " +
					" LEFT JOIN tb_c_employee emp ON emp.employee_id = uc.employee_id " +
					" LEFT JOIN  tb_c_user_company_changed AS temp " +
					" ON temp.user_company_id = uc.user_company_id " +
					" LEFT JOIN tb_c_headship hs ON hs.headship_id=emp.headship_id " +
					" WHERE ";
			
			//当前用户企业是否有权限设定
			String sql = "SELECT * FROM tb_b_master WHERE company_id=?";
			List<Map<String,Object>> mlist = this.jdbcTemplate.queryForList(sql, companyId);
			if (mlist.size()>0){
				rowSql +=	"  " + getUserRight(userId,"0") + " AND ";
			}
			else
			{
				mlist = this.jdbcTemplate.queryForList("select * from tb_b_master where company_id in (select company_id from tb_c_user_company where employee_id=(select employee_id from tb_c_system_user where user_id=?))",userId);
				if(mlist.size()>0)
				{
					rowSql += " " + getUserRight(userId,"0") + " AND ";
				}
			}
			rowSql += "  comp.org_flag='1' ";
			
			//首次下载
			if(StringUtils.hasText(companyId))
			{
				rowSql += " AND uc.company_id= '" + companyId + "' ";
			}
			else{
				//非首次下载
				rowSql += " AND uc.company_id IN (SELECT company_id FROM tb_c_user_company WHERE employee_id=(" +
					"SELECT employee_id FROM tb_c_system_user WHERE user_id='"+userId+"')) ";
			}
			
			rowSql += " AND temp.user_company_id = uc.user_company_id ";
			if(versionNum.equals("0"))
			{
				rowSql += " AND uc.del_flag='0' ";
			}
			else{
				rowSql += " AND (uc.del_flag='0' OR (temp.update_type='2' AND uc.del_flag='1')) " ;
			}
			
			if(StringUtils.hasText(companyFilterId))
			{
				rowSql += " AND uc.company_id NOT IN ("+companyFilterId+")";
			}
			
			//两次登录期间部门未做改变,只下载更新的数据,否则重新下载
			//if(selfDepartmentId.equals(departmentIdStr)){
				rowSql += " AND temp.group_version_num > '"+versionNum+"' ";
			//}
			rowSql += "GROUP BY uc.user_company_id ";
			rowSql += "ORDER BY temp.group_version_num DESC ";
			
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
			if (mlist.size()>0){
				list.addAll(getNoRightsUsers( userId, departmentIdStr,  selfDepartmentId, versionNum, companyId,companyFilterId));
			}
			
			
			
			//遍历map，添加部门隶属关系
			for(Map<String,Object> map1 : list)
			{
				String dSql = "SELECT dept.department_name,udept.user_department_id,udept.user_company_id,udept.headship_id,IFNULL(udept.headship_name,'') AS headship_name,udept.department_id,IFNULL(udept.department_path,'') AS department_path,udept.taxis,udept.visible_flag FROM " +
						"tb_c_user_department udept LEFT JOIN tb_c_department dept ON dept.department_id=udept.department_id WHERE" +
						"  udept.user_company_id=? AND udept.visible_flag='1' ";
				List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(dSql, map1.get("user_company_id").toString());
				map1.put("deptList", dList);
				retList.add(map1);
			}
			
			if((companyId.equals("8a1896523c29d5ec013c29da5f0f0000") 
					|| companyId.equals("f8510a2d428f3da40142995e272828e2")||
					"f8510a2d41027f4b0141156d5ae63dd9".equals(companyId))
					&& versionNum.equals("0"))
			{
				retContent = JSON.toJSONString(retList);
				UCache cache = new UCache();
				//cache.setCacheId("1");
				cache.setCompanyId(companyId);
				cache.setH(String.valueOf(headship_level));
				cache.setContent(retContent);
				this.cacheDao.save(cache);
			}
		}
		String severNum = "";
		if(retList.size()==0){
			severNum = versionNum;
		}else{
			severNum = retList.get(0).get("group_version_num").toString();
		}
		
		// updateFlag:0  不需要修改，1 需要修改       
		if(severNum.equals(versionNum)){
			map.put("updateFlag", "0");		
		}else{
			//添加删除用户逻辑，为了兼容客户端不能更新更换号码的问题
			for(Map<String,Object> uMap : updateMobileList)
			{
				retList.add(0, uMap);
			}
			map.put("updateFlag", "1");
			map.put("value", retList);
			map.put("total", retList.size());
		}
		return map;	
	}
	
	/**
	 * 下载指定企业联系人信息(兼容后来将用户添加到指定企业）
	 * @param departmentIdStr 手机客户端存储的登录人上次登录时所在部门
	 * @param selfDepartmentId 服务器端当前登陆人部门
	 * @param versionNum 手机客户端存储的登录人上次登录时服务器端人员版本号
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String,Object> getUserInfoByCompany(String userId,String versionNum,String companyId) throws IOException, ClassNotFoundException{
		//按用户职位级别建立缓存文件
		String uSql = "SELECT headship_level FROM tb_c_headship " +
				" WHERE headship_id=(SELECT headship_id FROM tb_c_employee WHERE " +
				"employee_id=(SELECT employee_id FROM tb_c_system_user where user_id=?))";
		int headship_level = this.jdbcTemplate.queryForInt(uSql, userId);
		
		//返回数据列表
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		
		String retContent = "";
		
		//公司编号
		if((companyId.equals("8a1896523c29d5ec013c29da5f0f0000") ||
				"f8510a2d41027f4b0141156d5ae63dd9".equals(companyId))&& versionNum.equals("0"))
		{
			String cSql = "SELECT * FROM tb_c_cache WHERE company_id=? AND h=?";
			List<Map<String,Object>> clist = this.jdbcTemplate.queryForList(cSql, companyId,String.valueOf(headship_level));
			if(clist.size()>0)
			{
				retContent = clist.get(0).get("content").toString();
			}
		}
		
		if(StringUtils.hasText(retContent))
		{
			retList = (List<Map<String, Object>>) JSONArray.parse(retContent);
		}
		else
		{
			String rowSql = "SELECT " +
					"temp.group_version_num," +
					"temp.update_type as update_type," +
					"IFNULL(emp.grid_number,'8888') AS department_fax," +
					"IFNULL(emp.display_order,999999) AS display_order," +
					"IFNULL(uc.picture,'') AS picture, " +
					"uc.user_company_id AS user_company_id," +
					"uc.employee_id AS employee_id," +
					"uc.employee_name AS employee_name," +
					"emp.department_id AS department_id," +
					"uc.department_name AS department_name," +
					"emp.headship_id AS headship_id," +
					"IFNULL(uc.headship_name,'') AS headship_name," +
					"comp.company_id AS company_id," +
					"comp.company_name AS company_name," +
					"IFNULL(comp.company_address,'') AS company_address," +
					"uc.mobile AS mobile," +
					"IFNULL(uc.mobile_short,'') AS mobile_short," +
					"IFNULL(uc.telephone2,'') AS tel," +
					"IFNULL(uc.tel_short,'') AS tel_short," +
					"IFNULL(uc.email,'') AS email," +
					"IFNULL(uc.telephone,'') AS home_telephone," +
					"IFNULL(uc.weibo,'') AS weibo," +
					"IFNULL(uc.weixin,'') AS weixin," +
					"IFNULL(uc.qq,'') AS qq," +
					"IFNULL(uc.school,'') AS school," +
					"IFNULL(uc.user_major,'') AS user_major," +
					"IFNULL(uc.user_grade,'') AS user_grade," +
					"IFNULL(uc.user_class,'') AS user_class," +
					"IFNULL(uc.student_id,'') AS student_id," +
					"IFNULL(uc.birthday,'') AS birthday," +
					"IFNULL(uc.native_place,'') AS native_place," +
					"IFNULL(uc.address,'') AS address," +
					"IFNULL(uc.home_address,'') AS home_address," +
					"IFNULL(uc.manage_flag,'0') AS manage_flag," +
					"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
					"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
					"IFNULL(uc.mood,'') AS mood," +
					"IFNULL(uc.user_company,'') AS user_company," + 
					"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
					"IFNULL(uc.remark,'') AS remark,'1' AS visible_flag," +
					"IFNULL((SELECT hs.headship_level FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') AS headship_level " +
					"FROM " +
					"tb_c_user_company uc," +
					"tb_c_company comp," +
					"tb_c_employee emp," +
					"(SELECT  * FROM tb_c_user_company_changed ucc ORDER BY ucc.group_version_num DESC) AS temp WHERE ";
			
			//当前用户企业是否有权限设定
			String sql = "SELECT * FROM tb_b_master WHERE company_id=?";
			List<Map<String,Object>> mlist = this.jdbcTemplate.queryForList(sql, companyId);
			if (mlist.size()>0){
				rowSql +=	" " + getUserRight(userId,"0") + " AND ";
			}
			rowSql += "  uc.company_id = comp.company_id AND comp.org_flag='1' " +
					" AND uc.company_id='"+companyId+"' " +
					" AND emp.employee_id = uc.employee_id " +
					" AND temp.user_company_id = uc.user_company_id AND uc.del_flag='0' AND comp.del_flag='0' " ;
			rowSql += "GROUP BY uc.user_company_id ";
			rowSql += "ORDER BY temp.group_version_num DESC ";
			
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
	
			if (mlist.size()>0){
				list.addAll(getNoRightsUsersByCompanyId( userId, companyId));
			}
			
			
			//遍历map，添加部门隶属关系
			for(Map<String,Object> map : list)
			{
				String dSql = "SELECT dept.department_name,udept.user_department_id,udept.user_company_id,udept.headship_id,udept.headship_name,hsp.headship_name,udept.department_id,IFNULL(udept.department_path,'') AS department_path,udept.taxis,udept.visible_flag FROM " +
						"tb_c_user_department udept,tb_c_department dept,tb_c_headship hsp WHERE " +
						" dept.department_id=udept.department_id AND hsp.headship_id=udept.headship_id AND udept.user_company_id=?";
				List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(dSql, map.get("user_company_id").toString());
				map.put("deptList", dList);
				retList.add(map);
			}
			
			if((companyId.equals("8a1896523c29d5ec013c29da5f0f0000") ||
					"f8510a2d41027f4b0141156d5ae63dd9".equals(companyId)) && versionNum.equals("0"))
			{
				retContent = JSON.toJSONString(retList);
				UCache cache = new UCache();
				cache.setCompanyId(companyId);
				cache.setH(String.valueOf(headship_level));
				cache.setContent(retContent);
				this.cacheDao.save(cache);
			}
			
			/*//写入到本地文本文件
			if(!new File(fileDir + fileName).exists() && versionNum.equals("0"))
			{
				FileOutputStream outStream = new FileOutputStream(fileDir+fileName);
				ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
				objOutStream.writeObject(retList);
				outStream.close();
				objOutStream.close();
			}*/
		}

		String severNum = "";
		if(retList.size()==0){
			severNum = versionNum;
		}else{
			severNum = retList.get(0).get("group_version_num").toString();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// updateFlag:0  不需要修改，1 需要修改
		map.put("updateFlag", "1");
		map.put("value", retList);
		map.put("total", retList.size());
		
		retList = null;
		
		return map;	
	}
	
	/**
	 * 无权限用户下载
	 * @param userId
	 * @param departmentIdStr
	 * @param selfDepartmentId
	 * @param versionNum
	 * @param companyId
	 * @param companyFilter 新增参数，用来过滤不需要更新的企业信息
	 * @return
	 */
	private List<Map<String,Object>> getNoRightsUsers(String userId,String departmentIdStr, String selfDepartmentId,String versionNum,String companyId,String companyFilter)
	{
		String rowSql = "SELECT " +
				"temp.group_version_num," +
				"temp.update_type as update_type," +
				"IFNULL(emp.grid_number,'8888') AS department_fax," +
				"IFNULL(emp.display_order,999999) AS display_order," +
				"IFNULL(uc.picture,'') AS picture, " +
				"uc.user_company_id AS user_company_id," +
				"uc.employee_id AS employee_id," +
				"uc.employee_name AS employee_name," +
				"emp.department_id AS department_id," +
				"uc.department_name AS department_name," +
				"emp.headship_id AS headship_id," +
				"IFNULL(uc.headship_name,'') AS headship_name," +
				"comp.company_id AS company_id," +
				"comp.company_name AS company_name," +
				"IFNULL(comp.company_address,'') AS company_address," +
				"uc.mobile AS mobile," +
				"IFNULL(uc.mobile_short,'') AS mobile_short," +
				"IFNULL(uc.telephone2,'') AS tel," +
				"IFNULL(uc.tel_short,'') AS tel_short," +
				"IFNULL(uc.email,'') AS email," +
				"IFNULL(uc.telephone,'') AS home_telephone," +
				"IFNULL(uc.weibo,'') AS weibo," +
				"IFNULL(uc.weixin,'') AS weixin," +
				"IFNULL(uc.qq,'') AS qq," +
				"IFNULL(uc.school,'') AS school," +
				"IFNULL(uc.user_major,'') AS user_major," +
				"IFNULL(uc.user_grade,'') AS user_grade," +
				"IFNULL(uc.user_class,'') AS user_class," +
				"IFNULL(uc.student_id,'') AS student_id," +
				"IFNULL(uc.birthday,'') AS birthday," +
				"IFNULL(uc.native_place,'') AS native_place," +
				"IFNULL(uc.address,'') AS address," +
				"IFNULL(uc.home_address,'') AS home_address," +
				"IFNULL(uc.manage_flag,'0') AS manage_flag," +
				"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
				"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
				"IFNULL(uc.mood,'') AS mood," +
				"IFNULL(uc.user_company,'') AS user_company," + 
				"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
				"IFNULL(uc.remark,'') AS remark,'0' AS visible_flag," +
				"IFNULL(hs.headship_level,'') AS headship_level " +
				"FROM " +
				"tb_c_user_company uc " +
				" LEFT JOIN tb_c_company comp ON  comp.company_id = uc.company_id " +
				" LEFT JOIN tb_c_employee emp ON emp.employee_id = uc.employee_id " +
				" LEFT JOIN  tb_c_user_company_changed AS temp " +
				" ON temp.user_company_id = uc.user_company_id " +
				" LEFT JOIN tb_c_headship hs ON hs.headship_id=emp.headship_id " +
				" WHERE  " + getUserRight(userId,"1") +
				" AND uc.company_id = comp.company_id AND comp.org_flag='1' " +
				" AND emp.employee_id = uc.employee_id " +
				" AND temp.user_company_id = uc.user_company_id " ;
		if(StringUtils.hasText(companyId))
		{
			rowSql += " AND uc.company_id='" + companyId + "' ";
		}
		if(versionNum.equals("0"))
		{
			rowSql += " AND uc.del_flag='0' ";
		}
		else{
			rowSql += " AND (uc.del_flag='0' OR (temp.update_type='2' AND uc.del_flag='1')) " ;
			if(StringUtils.hasText(companyFilter))
			{
				rowSql += " AND uc.company_id NOT IN ("+companyFilter+")";
			}
		}
		//两次登录期间部门未做改变,只下载更新的数据,否则重新下载
		//if(selfDepartmentId.equals(departmentIdStr)){
			rowSql += "AND temp.group_version_num > '"+versionNum+"' ";
		//}
		rowSql += "GROUP BY uc.user_company_id ";
		rowSql += "ORDER BY temp.group_version_num DESC ";
		
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		
		//遍历map，添加部门隶属关系
		for(Map<String,Object> map : list)
		{
			String dSql = "SELECT dept.department_name,udept.user_department_id,udept.user_company_id,udept.headship_id,udept.headship_name,hsp.headship_name,udept.department_id,IFNULL(udept.department_path,'') AS department_path,udept.taxis,udept.visible_flag FROM " +
					"tb_c_user_department udept,tb_c_department dept,tb_c_headship hsp WHERE " +
					" dept.department_id=udept.department_id AND hsp.headship_id=udept.headship_id AND udept.user_company_id=?";
			List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(dSql, map.get("user_company_id").toString());
			map.put("deptList", dList);
			retList.add(map);
		}
		return retList;
	}
	
	/**
	 * 下载指定公司无权限用户
	 * @param userId
	 * @param departmentIdStr
	 * @param selfDepartmentId
	 * @param versionNum
	 * @param companyId
	 * @return
	 */
	private List<Map<String,Object>> getNoRightsUsersByCompanyId(String userId,String companyId)
	{
		String rowSql = "SELECT " +
				"temp.group_version_num," +
				"temp.update_type as update_type," +
				"IFNULL(emp.grid_number,'8888') AS department_fax," +
				"IFNULL(emp.display_order,999999) AS display_order," +
				"IFNULL(uc.picture,'') AS picture, " +
				"uc.user_company_id AS user_company_id," +
				"uc.employee_id AS employee_id," +
				"uc.employee_name AS employee_name," +
				"emp.department_id AS department_id," +
				"uc.department_name AS department_name," +
				"emp.headship_id AS headship_id," +
				"IFNULL(uc.headship_name,'') AS headship_name," +
				"comp.company_id AS company_id," +
				"comp.company_name AS company_name," +
				"IFNULL(comp.company_address,'') AS company_address," +
				"uc.mobile AS mobile," +
				"IFNULL(uc.mobile_short,'') AS mobile_short," +
				"IFNULL(uc.telephone2,'') AS tel," +
				"IFNULL(uc.tel_short,'') AS tel_short," +
				"IFNULL(uc.email,'') AS email," +
				"IFNULL(uc.telephone,'') AS home_telephone," +
				"IFNULL(uc.weibo,'') AS weibo," +
				"IFNULL(uc.weixin,'') AS weixin," +
				"IFNULL(uc.qq,'') AS qq," +
				"IFNULL(uc.school,'') AS school," +
				"IFNULL(uc.user_major,'') AS user_major," +
				"IFNULL(uc.user_grade,'') AS user_grade," +
				"IFNULL(uc.user_class,'') AS user_class," +
				"IFNULL(uc.student_id,'') AS student_id," +
				"IFNULL(uc.birthday,'') AS birthday," +
				"IFNULL(uc.native_place,'') AS native_place," +
				"IFNULL(uc.address,'') AS address," +
				"IFNULL(uc.home_address,'') AS home_address," +
				"IFNULL(uc.manage_flag,'0') AS manage_flag," +
				"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
				"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
				"IFNULL(uc.mood,'') AS mood," +
				"IFNULL(uc.user_company,'') AS user_company," + 
				"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
				"IFNULL(uc.remark,'') AS remark,'0' AS visible_flag," +
				"IFNULL((SELECT hs.headship_level FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') AS headship_level " +
				"FROM " +
				"tb_c_user_company uc," +
				"tb_c_company comp," +
				"tb_c_employee emp," +
				"(SELECT  * FROM tb_c_user_company_changed ucc ORDER BY ucc.group_version_num DESC) AS temp " + 
				"WHERE " + getUserRight(userId,"1") +
				" AND uc.company_id = comp.company_id AND comp.org_flag='1' " +
				" AND uc.company_id='"+companyId+"' " +
				" AND emp.employee_id = uc.employee_id " +
				" AND temp.user_company_id = uc.user_company_id AND uc.del_flag='0' " ;
		rowSql += "GROUP BY uc.user_company_id ";
		rowSql += "ORDER BY temp.group_version_num DESC ";
		
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		
		//遍历map，添加部门隶属关系
		for(Map<String,Object> map : list)
		{
			String dSql = "SELECT dept.department_name,udept.user_department_id,udept.user_company_id,udept.headship_id,udept.headship_name,hsp.headship_name,udept.department_id,IFNULL(udept.department_path,'') AS department_path,udept.taxis,udept.visible_flag FROM " +
					"tb_c_user_department udept,tb_c_department dept,tb_c_headship hsp WHERE " +
					" dept.department_id=udept.department_id AND hsp.headship_id=udept.headship_id AND udept.user_company_id=?";
			List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(dSql, map.get("user_company_id").toString());
			map.put("deptList", dList);
			retList.add(map);
		}
		return retList;
	}
	
	/**
	 * 按姓名、拼音检索员工信息
	 * @param key
	 * @param department_id
	 * @param company_id
	 * @return
	 */
	public List<Map<String,Object>> searchUserByNameOrPY(String key,String department_id,String company_id)
	{
		String sql = "SELECT employee_name,employee_id FROM " +
				"tb_c_employee WHERE del_flag='0' ";
		if(StringUtils.hasText(department_id))
		{
			sql += " AND department_id='" + department_id + "'";
		}
		else
		{
			sql += " AND department_id IN (SELECT department_id FROM " +
					"tb_c_department WHERE company_id='"+company_id+"')";
		}
		if(StringUtils.hasText(key)){
			sql += " AND (employee_name like '%"+key+"%' OR employee_firstword='"+key+"' OR employee_fullword like '" + key + "%')";
		}
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 通过部门Id找到所有的子部门.
	 * @param allDept 
	 * @param departmentId 
	 * @return 
	 * 返回类型：String
	 */
	public String getAllChildDept(String allDept,String departmentId){
		allDept += ",'" + departmentId +"'";
		String rowSql = "SELECT * FROM tb_c_department dept WHERE dept.parent_department_id=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql,departmentId);
		if(list.size()>0){
			for(Map<String, Object> map :list){
				allDept = this.getAllChildDept(allDept, map.get("department_id").toString());
			}
		}
		return allDept;
	}
	
	/**
	 * 手动更新联系人.
	 * @param versionNum 
	 * @param employeeId 
	 * @param pictureName 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> updateUserInfo(String versionNum,String userCompanyId,String pictureName){
		String rowSql = "SELECT " +
				"temp.group_version_num," +
				"temp.update_type as update_type," +
				"IFNULL(emp.grid_number,'8888') AS department_fax," +
				"IFNULL(emp.display_order,999999) AS display_order," +
				"IFNULL(uc.picture,'') AS picture, " +
				"uc.user_company_id AS user_company_id," +
				"uc.employee_id AS employee_id," +
				"uc.employee_name AS employee_name," +
				"emp.department_id AS department_id," +
				"uc.department_name AS department_name," +
				"emp.headship_id AS headship_id," +
				"uc.headship_name AS headship_name," +
				"comp.company_id AS company_id," +
				"comp.company_name AS company_name," +
				"IFNULL(comp.company_address,'') AS company_address," +
				"uc.mobile AS mobile," +
				"uc.mobile_short AS mobile_short," +
				"IFNULL(uc.telephone2,'') AS tel," +
				"IFNULL(uc.tel_short,'') AS tel_short," +
				"IFNULL(uc.email,'') AS email," +
				"IFNULL(uc.telephone,'') AS home_telephone," +
				"IFNULL(uc.weibo,'') AS weibo," +
				"IFNULL(uc.weixin,'') AS weixin," +
				"IFNULL(uc.qq,'') AS qq," +
				"IFNULL(uc.school,'') AS school," +
				"IFNULL(uc.user_major,'') AS user_major," +
				"IFNULL(uc.user_grade,'') AS user_grade," +
				"IFNULL(uc.user_class,'') AS user_class," +
				"IFNULL(uc.student_id,'') AS student_id," +
				"IFNULL(uc.birthday,'') AS birthday," +
				"IFNULL(uc.native_place,'') AS native_place," +
				"IFNULL(uc.address,'') AS address," +
				"IFNULL(uc.home_address,'') AS home_address," +
				"IFNULL(uc.manage_flag,'0') AS manage_flag," +
				"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
				"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
				"IFNULL(uc.mood,'') AS mood," +
				"IFNULL(uc.user_company,'') AS user_company," + 
				"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
				"IFNULL(uc.remark,'') AS remark " +
				"FROM " +
				"tb_c_user_company uc," +
				"tb_c_company comp," +
				"tb_c_employee emp," +
				"tb_c_department dept," +
				"(SELECT  * FROM tb_c_user_company_changed ucc ORDER BY ucc.group_version_num DESC) AS temp " + 
				"WHERE uc.company_id = comp.company_id " +
				" AND emp.employee_id = uc.employee_id " +
				" AND emp.department_id = dept.department_id " +
				" AND temp.user_company_id = uc.user_company_id " +
				" AND uc.user_company_id='"+userCompanyId+"'" ;
		rowSql += "GROUP BY uc.user_company_id ";
		rowSql += "ORDER BY temp.group_version_num DESC ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
		String serverVersionNum = list.get(0).get("group_version_num").toString();
		String picture = list.get(0).get("picture").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		if(serverVersionNum.equals(versionNum) && picture.equals(pictureName)){
			// updateFlag:0  不需要修改，1 需要修改 
			map.put("updateFlag", "0");		
		}else{
		//用户部门信息
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		
		//遍历map，添加部门隶属关系
		for(Map<String,Object> m : list)
		{
			String dSql = "SELECT dept.department_name,udept.user_department_id,udept.user_company_id,udept.headship_id,udept.headship_name,hsp.headship_name,udept.department_id,IFNULL(udept.department_path,'') AS department_path,udept.taxis,udept.visible_flag FROM " +
					"tb_c_user_department udept,tb_c_department dept,tb_c_headship hsp WHERE " +
					" dept.department_id=udept.department_id AND hsp.headship_id=udept.headship_id AND udept.user_company_id=?";
			List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(dSql, m.get("user_company_id").toString());
			map.put("deptList", dList);
			retList.add(map);
		}
		map.put("value", retList);
		map.put("updateFlag","1");
		}
		return map;		
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
	 * 下载无权限的员工,根据手机长短号、办公长短号.
	 * @param key 
	 * @param department_fax 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> downloadEmployee(String key,String department_fax){
		String sql = "SELECT " +
				"emp.employee_id AS employee_id," +
				"emp.employee_name AS employee_name," +
				"dept.department_id   AS department_id," +
				"dept.department_name AS department_name," +
				"IFNULL(emp.grid_number,'8888') AS department_fax," +
				"IFNULL((SELECT hs.headship_name FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') " +
				"AS headshipName," +
				"IFNULL((SELECT hs.headship_level FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') " +
				"AS headship_level," +
				"IFNULL(emp.headship_id,'') AS headship_id," +
				"IFNULL(emp.mobile,'') AS mobile," +
				"IFNULL(emp.mobile_short,'') AS mobile_short," +
				"IFNULL(emp.telephone2,'') AS tel," +
				"IFNULL(emp.tel_short,'') AS tel_short," +
				"IFNULL(emp.email,'') AS email," +
				"IFNULL(emp.display_order,999999) AS display_order," +
				"emp.del_flag AS delFlag," +
				"IFNULL(emp.picture,'') AS picture, " +
				"emp.parent_department_name AS parent_department_name," +
				"IFNULL(emp.employee_firstword,'') AS employee_firstword, " +
				"IFNULL(emp.employee_fullword,'') AS employee_fullword " +
				"FROM tb_c_employee emp,tb_c_department dept " +
				"WHERE  emp.del_flag='0' AND dept.department_id =emp.department_id " +
				"AND (emp.mobile = '"+key+"' " +
    			"OR emp.mobile_short = '"+key+"' " +
    			"OR emp.telephone2 = '"+key+"') ";
		if(StringUtils.hasText(department_fax) && department_fax.length()==6){
			sql += "AND emp.grid_number= '"+department_fax+"' ";
		}
		sql += " limit 0,1";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", list);
		map.put("total", list.size());
		return map;
	}
	
	/**
	 * 增量下载数据.
	 * @param departmentLevel 登录人所在部门级别
	 * @param selfDepartmentId 登录人所在部门
	 * @param mobileRights 手机端权限
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> addUserInfo(int departmentLevel, String selfDepartmentId,String mobileRights){
		String rowSql = "SELECT " +
				"emp.employee_id AS employee_id " +
				"FROM tb_c_employee emp,tb_c_department dept " +
				"WHERE emp.department_id = dept.department_id ";
		if(StringUtils.hasText(mobileRights)){
    		String[] rightConfig = mobileRights.split(",");
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
        	}else{
        		String beginSql2 = " AND (dept.department_level >= '"+departmentLevel+"' ";
        		rowSql += beginSql2;
        	}
        	if(!"0".equals(down)){
        		//包含下级部门
        		String endSql ="" + (departmentLevel+Integer.parseInt(down));
        		rowSql += " AND dept.department_level <= '"+endSql+"' ";
        	}else{
        		rowSql += " AND dept.department_level < '"+departmentLevel+"' ";
        	}
        	if(!"0".equals(self)){
        		rowSql += " OR dept.department_level = '"+departmentLevel+"') ";
        	}else{
        		String selfSql = " AND dept.department_level <> '"+departmentLevel+"' " +
        				"OR dept.department_id='"+selfDepartmentId+"') ";
        		rowSql += selfSql;
        	}
    	}
		String sql = "SELECT " +
				"emp.employee_id AS employee_id," +
				"emp.employee_name AS employee_name," +
				"dept.department_id   AS department_id," +
				"dept.department_name AS department_name," +
				"IFNULL(emp.grid_number,'8888') AS department_fax," +
				"IFNULL((SELECT hs.headship_name FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') " +
				"AS headshipName," +
				"IFNULL((SELECT hs.headship_level FROM tb_c_headship hs WHERE hs.headship_id=emp.headship_id),'') " +
				"AS headship_level," +
				"IFNULL(emp.headship_id,'') AS headship_id," +
				"IFNULL(emp.mobile,'') AS mobile," +
				"IFNULL(emp.mobile_short,'') AS mobile_short," +
				"IFNULL(emp.telephone2,'') AS tel," +
				"IFNULL(emp.tel_short,'') AS tel_short," +
				"IFNULL(emp.email,'') AS email," +
				"IFNULL(emp.display_order,999999) AS display_order," +
				"emp.del_flag AS delFlag," +
				"temp.update_type AS update_type, " +
				"temp.txluser_version_num AS versionNum, " +
				"IFNULL(emp.picture,'') AS picture, " +
				"emp.parent_department_name AS parent_department_name," +
				"IFNULL(emp.employee_firstword,'') AS employee_firstword, " +
				"IFNULL(emp.employee_fullword,'') AS employee_fullword " +
				"FROM (SELECT * FROM tb_b_txluser_modifyusers txl ORDER BY txl.txluser_version_num DESC) AS temp, " +
				"tb_c_employee emp,tb_c_department dept " +
				"WHERE  emp.del_flag='0' " +
				"AND dept.department_id =emp.department_id " +
				"AND emp.employee_id = temp.employee_id " +
				"AND emp.employee_id NOT IN (" + rowSql + ") " +
				"GROUP BY temp.employee_id ";
		sql +=  "ORDER BY temp.txluser_version_num DESC ";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", list);
		map.put("total", list.size());
		if(list.size()>0){
			map.put("updateFlag","1");
		}else{
			map.put("updateFlag","0");
		}
		return map;
	}
	
	/**
	 * 修改个人信息中的主要号码之后,修改登陆账号.
	 * @param employeeId
	 * @param mobile 
	 * 返回类型：void
	 */
	public void updateEmpMobile(String employeeId,String mobile){
		String sql1 = "UPDATE tb_c_user_company SET mobile = ? WHERE employee_id = ?";
		String sql2 = "UPDATE tb_c_employee SET mobile = ? WHERE employee_id = ?";
		String sql3 = "UPDATE tb_c_system_user SET user_name = ? , mobile = ? WHERE employee_id = ?";
		this.jdbcTemplate.update(sql1,mobile,employeeId);
		this.jdbcTemplate.update(sql2,mobile,employeeId);
		this.jdbcTemplate.update(sql3,mobile,mobile,employeeId);
	}
	
	/**
	 * 根据手机号码返回employee_id
	 * @param mobile
	 * @return
	 */
	public String getEmployeeIdByMobile(String mobile)
	{
		String sql = "SELECT employee_id FROM tb_c_employee WHERE mobile=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, mobile);
		if(list.size()>0)
		{
			return list.get(0).get("employee_id").toString();
		}
		else
		{
			return "";
		}
	}
	
	
	/*************************************************************************
	 *       初次登录时用户下载                                                                                                                                                           *
	 *       用户部门一次下载速度过慢，从V1.2.4版本开始，分成两个接口下载                                                                  * 
	 *                                                                       *
	 *************************************************************************/
	/**
	 * 首次注册后下载用户信息
	 * @param userId
	 * @return
	 */
	public Map<String,Object> downloadUserInfoFirstTime(String userId)
	{
		String companyId = "";
		//首次下载第一家公司
		String compSQL = "SELECT " +
				"comp.company_id AS company_id," +
				"comp.company_name AS group_name," +
				"comp.org_flag AS org_flag," +
				"IFNULL(comp.index_logo,'') AS index_log,IFNULL(index_pictrue,'') AS index_pictrue " +
				"FROM tb_c_company comp, tb_c_user_company uc,tb_c_employee emp,tb_c_system_user us " +
				"WHERE comp.company_id=uc.company_id " +
				"AND emp.employee_id=uc.employee_id " +
				"AND us.employee_id=emp.employee_id " +
				"AND us.user_id=? " +
				"AND uc.del_flag='0' AND comp.del_flag='0' AND emp.del_flag='0' AND us.del_flag='0' " +
				"GROUP BY uc.company_id ORDER " +
				"BY comp.org_flag DESC,comp.company_name ASC limit 0,1 ";
		List<Map<String,Object>> compList = this.jdbcTemplate.queryForList(compSQL, userId);
		if(!compList.isEmpty())
		{
			companyId = compList.get(0).get("company_id").toString();
			String rowSql = "SELECT " +
					"IFNULL(emp.grid_number,'8888') AS department_fax," +
					"IFNULL(emp.display_order,999999) AS display_order," +
					"IFNULL(uc.picture,'') AS picture, " +
					"uc.user_company_id AS user_company_id," +
					"uc.employee_id AS employee_id," +
					"uc.employee_name AS employee_name," +
					"emp.department_id AS department_id," +
					"uc.department_name AS department_name," +
					"emp.headship_id AS headship_id," +
					"IFNULL(uc.headship_name,'') AS headship_name," +
					"comp.company_id AS company_id," +
					"comp.company_name AS company_name," +
					"IFNULL(comp.company_address,'') AS company_address," +
					"uc.mobile AS mobile," +
					"IFNULL(uc.mobile_short,'') AS mobile_short," +
					"IFNULL(uc.telephone2,'') AS tel," +
					"IFNULL(uc.tel_short,'') AS tel_short," +
					"IFNULL(uc.email,'') AS email," +
					"IFNULL(uc.telephone,'') AS home_telephone," +
					"IFNULL(uc.weibo,'') AS weibo," +
					"IFNULL(uc.weixin,'') AS weixin," +
					"IFNULL(uc.qq,'') AS qq," +
					"IFNULL(uc.school,'') AS school," +
					"IFNULL(uc.user_major,'') AS user_major," +
					"IFNULL(uc.user_grade,'') AS user_grade," +
					"IFNULL(uc.user_class,'') AS user_class," +
					"IFNULL(uc.student_id,'') AS student_id," +
					"IFNULL(uc.birthday,'') AS birthday," +
					"IFNULL(uc.native_place,'') AS native_place," +
					"IFNULL(uc.address,'') AS address," +
					"IFNULL(uc.home_address,'') AS home_address," +
					"IFNULL(uc.manage_flag,'0') AS manage_flag," +
					"IFNULL(uc.employee_firstword,'') AS employee_firstword, " +
					"IFNULL(uc.employee_fullword,'') AS employee_fullword," +
					"IFNULL(uc.mood,'') AS mood," +
					"IFNULL(uc.user_company,'') AS user_company," + 
					"IFNULL(emp.parent_department_name,'') AS parent_department_name," +
					"IFNULL(uc.remark,'') AS remark,'1' AS visible_flag " +
					"FROM " +
					"tb_c_user_company uc " +
					" LEFT JOIN tb_c_company comp ON  comp.company_id = uc.company_id " +
					" LEFT JOIN tb_c_employee emp ON emp.employee_id = uc.employee_id " +
					" LEFT JOIN (SELECT  * FROM tb_c_user_company_changed ucc WHERE update_type='0' ORDER BY ucc.group_version_num DESC) AS temp " +
					" ON temp.user_company_id = uc.user_company_id" +
					" WHERE ";
			
			//当前用户企业是否有权限设定
			String sql = "SELECT * FROM tb_b_master WHERE company_id=?";
			List<Map<String,Object>> mlist = this.jdbcTemplate.queryForList(sql, companyId);
			if (mlist.size()>0){
				rowSql +=	"  " + getUserRight(userId,"0") + " AND ";
			}
			rowSql += "  comp.org_flag='1' ";
			
			//首次下载
			if(StringUtils.hasText(companyId))
			{
				rowSql += " AND uc.company_id= '" + companyId + "' ";
			}
			else{
				//非首次下载
				rowSql += " AND uc.company_id IN (SELECT company_id FROM tb_c_user_company WHERE employee_id=(" +
					"SELECT employee_id FROM tb_c_system_user WHERE user_id='"+userId+"')) ";
			}
			
			rowSql += " AND temp.user_company_id = uc.user_company_id ";
			
				rowSql += " AND uc.del_flag='0' ";

			rowSql += "GROUP BY uc.user_company_id ";
			rowSql += "ORDER BY temp.group_version_num DESC ";
			
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(rowSql);
			if (mlist.size()>0){
				list.addAll(getNoRightsUsers( userId, "",  "", "0", companyId,""));
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("updateFlag", "1");
			map.put("value", list);
			map.put("total", list.size());
			map.put("companyId", companyId);
			return map;
		}
		else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("updateFlag", "0");
			map.put("value", "");
			map.put("total", "0");
			return map;
		}
	}
	
	/**
	 * 首次登录时下载所有用户部门隶属关系
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> downloadDepartmentInfoFirstTime(String companyId)
	{
		String sql = "SELECT udept.* FROM tb_c_user_department udept LEFT JOIN tb_c_user_company uc ON uc.user_company_id=udept.user_company_id " +
				" WHERE uc.company_id=? AND uc.del_flag='0'";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, companyId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", "0");
		map.put("value", list);
		map.put("total", list.size());
		return map;
	}
	/*************************************************************************
	 *       初次登录时用户下载                                                                                                                                                           *
	 *       END                                                             * 
	 *                                                                       *
	 *************************************************************************/
	
	/**
	 * 权限 
	 * @param userId
	 * @param type：0为有权限用户，1为无权限用户
	 * @return
	 */
	private String getUserRight(String userId,String type)
	{
		//当前登录用户的company_id如果为023，则使用特殊权限，为阜阳写死
		
		String cSql = "SELECT * FROM tb_c_system_user WHERE user_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(cSql, userId);
		if(list.size()>0)
		{
			if(list.get(0).get("company_id").toString().equals("023"))
			{
				return getUserRight(userId,type,list.get(0).get("company_id").toString());
			}
		}
		String uSql = "SELECT headship_level FROM tb_c_headship " +
				" WHERE headship_id=(SELECT headship_id FROM tb_c_employee WHERE " +
				"employee_id=(SELECT employee_id FROM tb_c_system_user where user_id=?))";
		int headship_level = this.jdbcTemplate.queryForInt(uSql, userId);
		String sql = "";
		if (type.equals("0")){
			sql += "(emp.headship_id in  (select headship_id from tb_c_headship where " +
				"headship_level>="+(headship_level-1)+" or headship_level="+headship_level+"))";
		}
		else
		{
			sql += "(emp.headship_id not in  (select headship_id from tb_c_headship where " +
					"headship_level>="+(headship_level-1)+" or headship_level="+headship_level+"))";
		}
		return sql;
	}
	
	/**
	 * For gov of fuyang
	 * @param userId
	 * @param type
	 * @param company_id
	 * @return
	 */
	private String getUserRight(String userId,String type,String company_id)
	{
		String uSql = "SELECT headship_level FROM tb_c_headship " +
				" WHERE headship_id=(SELECT headship_id FROM tb_c_employee WHERE " +
				"employee_id=(SELECT employee_id FROM tb_c_system_user where user_id=?))";
		int headship_level = this.jdbcTemplate.queryForInt(uSql, userId);
		String Sql = "";
		if(headship_level == 1)
		{
			//公司领导 可以查看所有人
			if(type.equals("0")){
				Sql = "1=1";
			}
			else
			{
				Sql = "1=2";
			}
		}
		else if(headship_level <= 3)
		{
			//四个主要部门内员工，可以查看除领导外的所有员工
			if(type.equals("0"))
			{
				Sql = "emp.headship_id IN (SELECT headship_id FROM tb_c_headship WHERE headship_level>=2)";
			}
			else
			{
				Sql = "emp.headship_id NOT IN (SELECT headship_id FROM tb_c_headship WHERE headship_level=1)";
			}
		}
		else
		{
			//其他员工，只能看到自己所在部门用户
			if(type.equals("0"))
			{
				Sql = "emp.department_id IN " +
						"(SELECT department_id FROM tb_c_employee WHERE employee_id=" +
						"(SELECT employee_id FROM tb_c_system_user WHERE user_id='"+userId+"'))";
			}
			else
			{
				Sql = "emp.department_id NOT IN " +
						"(SELECT department_id FROM tb_c_employee WHERE employee_id=" +
						"(SELECT employee_id FROM tb_c_system_user WHERE user_id='"+userId+"'))";
			}
		}
		return Sql;
	}
	
	/**
	 * 将以逗号分隔的企业ID转成供sql语句调用的字符串
	 * @param filter_company
	 * @return
	 */
	private String getSpliteCompanyIds(String filter_company)
	{
		String retValue = "";
		// 过滤所有客户端为空的企业，拼成'company_id','company_id'直接供sql语句使用
		if(StringUtils.hasText(filter_company))
		{
			String[] companys = filter_company.split("[,]");
			for(String comp : companys)
			{
				if(StringUtils.hasText(comp))
				{
					retValue += "'" + comp + "',";
				}
			}
			if(StringUtils.hasText(retValue))
			{
				retValue = retValue.substring(0, retValue.length()-1);
			}
		}
		return retValue;
	}

	/**
	 * 返回用户的ID
	 * @param userId 用户的ID
	 * @param mobile 用户的手机号
	 * @param companyId 用户所在的公司
	 * @return 用户的ID
	 */
	public String getUserId(String userId,String mobile,String companyId){
		List<String> userids=this.jdbcTemplate.queryForList("select user_id from tb_c_system_user where user_id=? and del_flag='0' and company_id=?",
				String.class,new String[]{userId,companyId});
		if(userids==null||userids.isEmpty()){
			userids= this.jdbcTemplate.queryForList("select user_id from tb_c_system_user where user_name=? and del_flag='0' and company_id=?",
				String.class,new String[]{mobile,companyId});
		}
		return userids==null||userids.isEmpty()?null:userids.get(0);
	}
	
	
	/**
	 * 重构首次下载用户信息.
	 * @param userId 用户信息
	 * @param companyId 所属公司，为空则取第一个公司
	 * @return 联系人信息，部门信息,最大版本号
	 */
	public Object[] getFirstUserInfo(String userId,String companyId){
		//异常字段的排除
		if(!StringUtils.hasText(userId)){
			return null;
		}
		//首次下载，必须要查询到companyid（找到用户所在的第一家公司）
		if(!StringUtils.hasText(companyId)){
			String compSQL ="SELECT comp.company_id "+
							"FROM tb_c_system_user us "+
							"join tb_c_user_company uc on uc.del_flag='0' and us.employee_id=uc.employee_id   "+
							"join tb_c_company comp on comp.del_flag='0' and comp.company_id=uc.company_id  "+
							"WHERE  us.del_flag='0'  "+
							"and    us.user_id=? "+				
							"ORDER BY comp.org_flag DESC,comp.company_name ASC  "+
							"limit 0,1";
			List<String> company=this.jdbcTemplate.queryForList(compSQL,String.class,userId);
			if(company==null){
				return null;
			}
			companyId= company.get(0);
		}
		//获取用户在该公司内的最大权限(基于用户表上的headship_level 为最小的headship_level)
		String uSql = "select  min(headship_level) as headship_level "+
				"from tb_c_headship hs "+
				"join  tb_c_user_department udp on udp.headship_id=hs.headship_id "+
				"join  tb_c_user_company uc on uc.user_company_id=udp.user_company_id and uc.company_id=? "+
				"join  tb_c_system_user su on su.employee_id=uc.employee_id and su.user_id=? ";
		int headship_level= this.jdbcTemplate.queryForInt(uSql, companyId,userId);
		//获取当前企业最大的用户版本号。
		int max_version=this.jdbcTemplate.queryForInt("select max(group_version_num) from tb_c_user_company_changed ucc ");
		//进行数据库查询（所有数据，权限仅控制是否可见visible_flag）
		StringBuilder sq=new StringBuilder("SELECT emp.grid_number, emp.display_order, "+
				"uc.picture,uc.user_company_id,uc.employee_id, "+
				"uc.employee_name,comp.company_id,comp.company_address, "+
				"uc.mobile,uc.mobile_short,uc.telephone2,uc.tel_short,uc.email,uc.telephone, "+
				"uc.weibo,uc.weixin,uc.qq,uc.school,uc.user_major,uc.user_grade,uc.user_class, "+
				"uc.student_id,uc.birthday,uc.native_place,uc.address,uc.home_address,uc.manage_flag, "+
				"uc.employee_firstword, uc.employee_fullword,uc.mood,uc.user_company,emp.parent_department_name," +
				"uc.remark,'1' AS visible_flag,uc.department_name,uc.headship_name " +
				"FROM tb_c_user_company uc "+ 
				"JOIN tb_c_company comp ON comp.org_flag='1' and comp.company_id = uc.company_id "+
				"JOIN tb_c_employee emp ON comp.del_flag='0' and emp.employee_id = uc.employee_id  " +
				"where uc.del_flag='0' and uc.company_id= ? ");
		//添加部门隶属关系,查处部门所有人的隶属关系
		StringBuilder sq1=new StringBuilder("SELECT tuc.user_company_id,udept.user_department_id,udept.headship_id,udept.headship_name,udept.department_id,udept.department_path,udept.taxis "+ 
				"FROM  tb_c_user_department udept "+
				"join tb_c_user_company tuc on tuc.user_company_id=udept.user_company_id "+
				"where udept.visible_flag='1' and tuc.company_id=? order by tuc.user_company_id,udept.department_id");
		List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sq.toString(),companyId);
		//例外，为空，直接返回
		if(list==null||list.isEmpty()){
			return null;
		}
		List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(sq1.toString(), companyId);
		//按usercompany_id进行归并
		Map<String,List<Map<String,Object>>> cmp=new HashMap<>();
		for(Map<String,Object> m : dList)
		{
			String user_company_id=(String)m.get("user_company_id");
			List<Map<String,Object>> rws=cmp.get(user_company_id);
			if(rws==null){
				rws=new ArrayList<>();
				rws.add(m);
				cmp.put(user_company_id, rws);
			}else{
				rws.add(m);
			}
		}
		Object[] result=new Object[]{list,cmp,String.valueOf(max_version),companyId,headship_level,null};
		//进行权限控制，特例，公司老总不需要进行权限控制
		if(headship_level<=1){
			return result;
		}
		//判断是否有权限控制
		String sql = "SELECT count(1) FROM tb_b_master WHERE company_id=? ";
		int sz= this.jdbcTemplate.queryForInt(sql, companyId);
		if(sz<=0){
			return result;
		}
		//阜阳公司,大于3仅可见自己部门内的员工,查询自己的部门
		if(AYD_FUYANG.equals(companyId)&&headship_level>3){
			String depart=this.jdbcTemplate.queryForObject("select group_concat(distinct t0.department_id) "+
			"from tb_c_user_department t0 "+
			"join tb_c_user_company t1 on t1.user_company_id=t0.user_company_id "+
			"join tb_c_system_user t2 on t1.employee_id=t2.employee_id "+
			"where t2.user_id=? and t1.company_id=?"+
			"order by department_id",String.class,userId,companyId);
			result[5]=depart;
			return result;
		}
		//通用权限查询
		String hlql="select min(t0.headship_level) as level,t2.user_company_id "+
				"from tb_c_user_company t2  "+
				"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
				"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
				"where t2.company_id= ? "+
				"group by t2.user_company_id ";
		//阜阳公司,小于3的主要部门可见	[四个主要部门内员工，可以查看除领导外的所有员工]
		if(AYD_FUYANG.equals(companyId)&&headship_level<=3){
			hlql="select distinct t2.user_company_id, 1 as level "+
				"from tb_c_user_company t2  "+
				"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
				"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
				"where t2.company_id= ? and t0.headship_level=1 ";
		}
		//需要进行权限控制
		List<Map<String,Object>> pList=this.jdbcTemplate.queryForList(hlql,companyId);
		//按usercompany_id进行归并
		Map<String,Object> level=new HashMap<>();
		for(Map<String,Object> m : pList)
		{
			level.put((String)m.get("user_company_id"), m.get("level"));
		}
		result[5]=level;
		sq=null;sq1=null;pList=null;
		return result;
	}
	
	/**
	 * 获取更新的用户资料以及权限.
	 * @param userId 用户帐号
	 * @param filterCompany 过滤的公司
	 * @param curVersion 当前的版本号
	 * @param headships 传输来的版本号
	 * @return 更新的记录以及权限信息
	 * @since 1.3.0
	 */
	public Object[] getUpdateUserInfo(String userId,String filterCompany,String curVersion,Map<String,StringBuilder> headships){
		long t0=new Date().getTime();
		//1.找出当前变更包含的公司，按公司进行更新
		String sql ="select distinct  tc.company_id "+
					"from tb_c_user_company tc  "+
					"join tb_c_user_company_changed tcc on tcc.user_company_id=tc.user_company_id "+
					"where tcc.group_version_num>? "+
					"and tc.company_id in( "+
					"select distinct t0.company_id "+
					"from tb_c_user_company t0 "+
					"join tb_c_system_user t1 on t1.employee_id=t0.employee_id "+
					"where t0.del_flag='0' and t1.user_id=? )";
		List<String> cmplist=this.jdbcTemplate.queryForList(sql,String.class,curVersion,userId);
		if(LOG.isDebugEnabled()){
			LOG.debug("update user dao [get update company time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		//获取当前企业最大的用户版本号。
		int max_version=this.jdbcTemplate.queryForInt("select max(group_version_num) from tb_c_user_company_changed ucc ");
		if(LOG.isDebugEnabled()){
			LOG.debug("update user dao [get max group version time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		Object[] arr=new Object[]{max_version,null};	
		if(cmplist==null||cmplist.isEmpty()){//无更新
			return arr;
		}
		List<Object[]> result=new ArrayList<>();
		//2.按公司进行更新
		for(String cmpid : cmplist){
			//过滤企业
			if(filterCompany!=null&&filterCompany.indexOf(cmpid)>=0){
				continue;
			}
			Object[] row=new Object[8];
			row[0]=cmpid;
			//3.1.资料变更，更新当前公司人员的资料,所有变更
			StringBuilder sq=new StringBuilder("SELECT emp.grid_number, emp.display_order,tcc.update_type,"+
					"uc.picture,uc.user_company_id,uc.employee_id, "+
					"uc.employee_name,comp.company_id,comp.company_address, "+
					"uc.mobile,uc.mobile_short,uc.telephone2,uc.tel_short,uc.email,uc.telephone, "+
					"uc.weibo,uc.weixin,uc.qq,uc.school,uc.user_major,uc.user_grade,uc.user_class, "+
					"uc.student_id,uc.birthday,uc.native_place,uc.address,uc.home_address,uc.manage_flag, "+
					"uc.employee_firstword, uc.employee_fullword,uc.mood,uc.user_company,emp.parent_department_name," +
					"uc.remark,'1' AS visible_flag,uc.department_name,uc.headship_name " +
					"FROM tb_c_user_company uc   " +
					"join tb_c_user_company_changed tcc on tcc.user_company_id=uc.user_company_id " +
					"JOIN tb_c_company comp ON comp.org_flag='1' and comp.company_id = uc.company_id  " +
					"JOIN tb_c_employee emp ON comp.del_flag='0' and emp.employee_id = uc.employee_id    " +
					"where uc.company_id= ? and  tcc.group_version_num>? and " +
					"not exists(select 1 from tb_c_user_company_changed t0 where t0.user_company_id=tcc.user_company_id " +
					//" and t0.update_type=tcc.update_type " +
					" and t0.group_version_num>tcc.group_version_num) ");
			List<Map<String, Object>>  list = this.jdbcTemplate.queryForList(sq.toString(),cmpid,curVersion);
			row[1]=list;
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get update user info time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			StringBuilder sq1=new StringBuilder("SELECT tuc.user_company_id,udept.user_department_id,udept.headship_id,udept.headship_name,udept.department_id,udept.department_path,udept.taxis "+ 
					"FROM  tb_c_user_department udept "+
					"join tb_c_user_company tuc on tuc.user_company_id=udept.user_company_id "+
					"where udept.visible_flag='1' and tuc.company_id=? and "+
					"exists(select 1 from tb_c_user_company_changed m0 where m0.user_company_id=udept.user_company_id " +
					"and m0.group_version_num>?) ");
			List<Map<String,Object>> dList = this.jdbcTemplate.queryForList(sq1.toString(), cmpid,curVersion);
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get update user depart time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			//按usercompany_id进行归并
			Map<String,List<Map<String,Object>>> cmp=new HashMap<>();
			for(Map<String,Object> m : dList)
			{
				String user_company_id=(String)m.get("user_company_id");
				List<Map<String,Object>> rws=cmp.get(user_company_id);
				if(rws==null){
					rws=new ArrayList<>();
					rws.add(m);
					cmp.put(user_company_id, rws);
				}else{
					rws.add(m);
				}
			}
			row[2]=cmp;
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [format user depart  time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			//3.3.获取更新人员的权限，按公司进行匹配
			List<Map<String,Object>> pList=this.jdbcTemplate.queryForList("select min(t0.headship_level) as level,t2.user_company_id "+
				"from tb_c_user_company t2  "+
				"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
				"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
				"where t2.company_id= ?  and " +
				"exists(select 1 from tb_c_user_company_changed m0 where m0.user_company_id=t2.user_company_id " +
				"and m0.group_version_num>?) "+
				"group by t2.user_company_id ",cmpid,curVersion);
			row[3]=pList;
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get user headship  time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			//4.1 权限更新，判断是否需要进行权限控制
			//读取当前用户的权限
			int sz= this.jdbcTemplate.queryForInt("SELECT count(1) FROM tb_b_master WHERE company_id=? ", cmpid);
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get master control time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			if(sz<=0){//无需权限控制，不变更权限信息
				result.add(row);
				continue;
			}
			
			//4.2获取当前人员的级别
			Integer mlv=this.jdbcTemplate.queryForObject("select min(t0.headship_level) as level "+
				"from tb_c_system_user tu " +
				"join tb_c_user_company t2 on tu.employee_id=t2.employee_id "+
				"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
				"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
				"where tu.user_id=? and t2.company_id= ? ",Integer.class,userId,cmpid);
			row[4]=mlv;
			//4.3更新人员的可见性
			List<Map<String,Object>> lList=null;
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get cur update user headship time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			if(AYD_FUYANG.equals(cmpid)&&mlv>3){
				//4.3.1 阜阳公司,大于3仅可见自己部门内的员工,查询自己的部门
				String depart=this.jdbcTemplate.queryForObject("select group_concat(distinct t0.department_id) "+
				"from tb_c_user_department t0 "+
				"join tb_c_user_company t1 on t1.user_company_id=t0.user_company_id "+
				"join tb_c_system_user t2 on t1.employee_id=t2.employee_id "+
				"where t2.user_id=? and t1.company_id=?"+
				"order by department_id",String.class,userId,cmpid);
				row[5]=depart;
			}else if(AYD_FUYANG.equals(cmpid)&&mlv<=3){
				//4.3.3 阜阳公司,小于3的主要部门可见	[四个主要部门内员工，可以查看除领导外的所有员工]
				lList=this.jdbcTemplate.queryForList("select distinct t2.user_company_id, 1 as level"+
					"from tb_c_user_company t2  "+
					"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
					"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
					"where t2.company_id= ? and t0.headship_level=1 and "+
					"exists(select 1 from tb_c_user_company_changed m0 where m0.user_company_id=t2.user_company_id " +
					"and m0.group_version_num>?) ",cmpid,curVersion);
			}else{
				//4.3.2 通用权限查询
				lList=this.jdbcTemplate.queryForList("select min(t0.headship_level) as level,t2.user_company_id "+
						"from tb_c_user_company t2  "+
						"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
						"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
						"where t2.company_id= ? and "+
						"exists(select 1 from tb_c_user_company_changed m0 where m0.user_company_id=t2.user_company_id " +
						"and m0.group_version_num>?) "+
						"group by t2.user_company_id ",cmpid,curVersion);
			}
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get update user privlage time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			if(lList!=null&&!lList.isEmpty()){
				//按usercompany_id进行归并
				Map<String,Object> level=new HashMap<>();
				for(Map<String,Object> m : lList)
				{
					level.put((String)m.get("user_company_id"), m.get("level"));
				}
				row[5]=level;
			}
			StringBuilder headshipId=headships.get(cmpid);
			int curlv=999;
			if(StringUtils.hasText(headshipId)){
				curlv=this.jdbcTemplate.queryForInt("select min(headship_level) from tb_c_headship where del_flag='0' and headship_id in("+headshipId+")");
			}
			if(curlv<=0){
				curlv=999;
			}
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [format update user privlage  time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			//4.4.按照权限的差值进行更新
			if(mlv==curlv){
				//未发生权限变化，不做变更
				result.add(row);
				continue;
			}
			if(AYD_FUYANG.equals(cmpid)&&mlv>1&&curlv<=1){
				//特殊阜阳的用户,从公司领导降为主要领导，公司领导权限不可见
				row[6]=this.jdbcTemplate.queryForList("select distinct t2.user_company_id"+
						"from tb_c_user_company t2  "+
						"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id  "+
						"join tb_c_headship t0 on t1.headship_id=t0.headship_id "+
						"where t2.company_id= ? and t0.headship_level=1 ", String.class,cmpid);
			}else if(AYD_FUYANG.equals(cmpid)&&mlv>3&&curlv<=3){
				//阜阳，从主要领导降为普通员工，本部门外其他权限均不可见
				row[6]=this.jdbcTemplate.queryForList("select distinct tuc.user_company_id from tb_c_user_company tuc "+
						"join tb_c_user_department tcd on tcd.user_company_id=tuc.user_company_id "+
						"where tuc.company_id=? and tcd.department_id not in( "+
						"select distinct t0.department_id "+
						"from tb_c_user_department t0 "+
						"join tb_c_user_company t1 on t1.user_company_id=t0.user_company_id "+
						"join tb_c_system_user t2 on t1.employee_id=t2.employee_id "+
						"where t2.user_id=? and t1.company_id=?)", String.class,cmpid,userId,cmpid);
			}else if(!AYD_FUYANG.equals(cmpid)){
				//通用用户,从curlv-1 到mlv-1不可见
				StringBuilder sq2=new StringBuilder("select distinct t2.user_company_id "+
						"from tb_c_user_company t2 "+
						"join tb_c_user_department t1 on t2.user_company_id=t1.user_company_id   "+
						"join tb_c_headship t0 on t1.headship_id=t0.headship_id  "+
						"where t2.company_id=? and t0.headship_level<=? and t0.headship_level>=? ");
				if(curlv<=1||mlv<=1){
					sq2.append("and not EXISTS( "+
							"select 1  "+
							"from tb_c_user_department r2  "+
							"join tb_c_headship r3 on r2.headship_id=r3.headship_id "+
							"where r2.user_company_id=t2.user_company_id and r3.headship_level<? )");
					if(mlv<curlv){
						row[6]=this.jdbcTemplate.queryForList(sq2.toString(), String.class,cmpid,curlv-1,mlv-1,mlv-1);
					}else{
						row[6]=this.jdbcTemplate.queryForList(sq2.toString(), String.class,cmpid,mlv-1,curlv-1,curlv-1);
					}
				}else{
					if(mlv<curlv){
						row[6]=this.jdbcTemplate.queryForList(sq2.toString(), String.class,cmpid,curlv-1,mlv-1);
					}else{
						row[6]=this.jdbcTemplate.queryForList(sq2.toString(), String.class,cmpid,mlv-1,curlv-1);
					}
				}
				
			}
			if(LOG.isDebugEnabled()){
				LOG.debug("update user dao [get cur user privlage update time]--"+(new Date().getTime()-t0));
				t0=new Date().getTime();
			}
			row[7]=mlv<curlv?"1":"0";
			result.add(row);
		}
		arr[1]=result;
		return arr;
	}
}
