// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MEmployeeDao;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.TxlVersionService;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：EmployeeService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-7 下午3:14:33
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MEmployeeService extends BaseServiceImpl<Employee, String> {
	
	@Resource
	private MEmployeeDao employeeDao;
	
	@Resource
	private MSystemUserService mSystemUserService;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private TxlVersionService txlVersionService;
	
	private static final Logger LOG=Logger.getLogger(MEmployeeService.class);
	//阜阳公司固定SPID
	private static final  String AYD_FUYANG="023";
	@Override
	public HibernateBaseDao<Employee, String> getHibernateBaseDao() {
		return this.employeeDao;
	}
	
	/**
	 * 下载通讯录.
	 * @param departmentIdStr 上次登录时,登录人所在部门,若部门修改,则重新下载数据.
	 * @param userId  
	 * @param versionNum 版本号,若服务器端权限和本人部门未改变,则仅下载版本号大于上次登录时的版本号的数据
	 * @param filter_company：2013/11/12新增参数，更新接口只更新有用户的企业，由客户端上传需要过滤的编号，用逗号分隔
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getUserInfoByDeptId(String departmentIdStr,String userId,String versionNum,String filter_company){
		try{
			//SystemUser systemUser = this.mSystemUserService.getEntity(userId);
			//Employee employee = this.employeeDao.get(systemUser.getEmployeeId());
			//Department department = this.deptMagService.getEntity(employee.getDepartmentId());//服务器端当前登录人所在部门
			//String selfDepartmentId = department.getDepartmentId();
			//下载第一个分公司
			
			return this.employeeDao.getUserInfoByDeptId(userId,departmentIdStr,"",versionNum,"",filter_company);
		}catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("errmsg", e.getMessage());
			map.put("success", "false");
			return map;
		}
	}
	
	/**
	 * 下载指定公司的用户
	 * @param userId
	 * @param versionNum
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> getUserInfoByCompany(String userId,String versionNum,String companyId)
	{
		try{
			return this.employeeDao.getUserInfoByCompany(userId,versionNum,companyId);
		}catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", "false");
			return map;
		}
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
		return this.employeeDao.searchUserByNameOrPY(key, department_id, company_id);
	}
	
	/**
	 * 手动更新联系人.
	 * @param versionNum 
	 * @param userCompanyId 
	 * @param pictureName 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> updateUserInfo(String versionNum,String userCompanyId,String pictureName){
		return this.employeeDao.updateUserInfo(versionNum,userCompanyId,pictureName);
	}
	
	/**
	 * 判断是否存在该电话号码.
	 * @param mobile 
	 * @return 
	 * 返回类型：String
	 */
	public String checkEmployee(String mobile){
		return this.employeeDao.checkEmployee(mobile);
	}
	
	/**
	 * 个人信息编辑.
	 * @param employeeId 员工Id
	 * @param employeeName 员工姓名
	 * @param mobileLong 手机长号
	 * @param mobileShort 手机短号
	 * @param telLong 办公长号
	 * @param telShort 办公短号
	 * @param email 邮箱
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> updateEmployee(String employeeId, String employeeName, String mobileLong, 
			String mobileShort, String telLong, String telShort,String email){
		Map<String,Object> map = new HashMap<String,Object>();
		Employee employee = this.employeeDao.get(employeeId);
		List<SystemUser> systemUserList = this.mSystemUserService.findByNamedParam("employeeId", employeeId);
		SystemUser systemUser = systemUserList.get(0);
		if(employee.getMobile().equals(mobileLong)){
			employee.setEmployeeName(employeeName);
			employee.setEmployeeFirstword(concatPinyin.firstwordOfName(employeeName));
			employee.setEmployeeFullword(concatPinyin.changeToPinyin(employeeName));
			employee.setMobileShort(mobileShort);
			employee.setTelephone2(telLong);
			employee.setTelShort(telShort);
			employee.setEmail(email);
			this.employeeDao.update(employee);
			systemUser.setRealName(employeeName);
			systemUser.setMobileShort(mobileShort);
			systemUser.setTelephone(telLong);
			systemUser.setTelShort(telShort);
			systemUser.setEmail(email);
			this.mSystemUserService.updateEntity(systemUser);
			this.txlVersionService.saveAll("1", employeeId);
			map.put("success", "true");
		}else{
			String mobileCheck = this.checkEmployee(mobileLong);
			if(StringUtils.hasText(mobileCheck)){
				map.put("success", "false");
				map.put("msg", "mobile");
			}else{
				employee.setEmployeeName(employeeName);
				employee.setEmployeeFirstword(concatPinyin.firstwordOfName(employeeName));
				employee.setEmployeeFullword(concatPinyin.changeToPinyin(employeeName));
				employee.setMobile(mobileLong);
				employee.setMobileShort(mobileShort);
				employee.setTelephone2(telLong);
				employee.setTelShort(telShort);
				employee.setEmail(email);
				this.employeeDao.update(employee);
				systemUser.setRealName(employeeName);
				systemUser.setUserName(mobileLong);
				systemUser.setMobile(mobileLong);
				systemUser.setMobileShort(mobileShort);
				systemUser.setTelephone(telLong);
				systemUser.setTelShort(telShort);
				systemUser.setEmail(email);
				this.mSystemUserService.updateEntity(systemUser);
				this.txlVersionService.saveAll("1", employeeId);
				map.put("success", "true");
			}
		}
		return map;
	}
	
	/**
	 * 下载无权限的员工,根据手机长短号、办公长短号.
	 * @param key 
	 * @param department_fax 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> downloadEmployee(String key,String department_fax){
		return this.employeeDao.downloadEmployee(key,department_fax);
	}
	
	/**
	 * 增量下载数据.
	 * @param userId 登录用户
	 * @param mobileRights 手机端权限
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> addUserInfo(String userId,String mobileRights){
		try{
			SystemUser systemUser = this.mSystemUserService.getEntity(userId);
			Employee employee = this.employeeDao.get(systemUser.getEmployeeId());
			Department department = this.deptMagService.getEntity(employee.getDepartmentId());//服务器端当前登录人所在部门
			int departmentLevel = department.getDepartmentLevel();
			String selfDepartmentId = department.getDepartmentId();
			return this.employeeDao.addUserInfo(departmentLevel, selfDepartmentId, mobileRights);
		}catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", "false");
			return map;
		}
	}
	
	
	
	/**
	 * 修改个人信息中的主要号码之后,修改登陆账号.
	 * @param employeeId
	 * @param mobile 
	 * 返回类型：void
	 */
	public void updateEmpMobile(String employeeId,String mobile){
		this.employeeDao.updateEmpMobile(employeeId, mobile);
	}
	
	/**
	 * 根据手机号码返回employee_id
	 * @param mobile
	 * @return
	 */
	public String getEmployeeIdByMobile(String mobile)
	{
		return this.employeeDao.getEmployeeIdByMobile(mobile);
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
		return this.employeeDao.downloadUserInfoFirstTime(userId);
	}
	
	/**
	 * 首次登录时下载所有用户部门隶属关系
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> downloadDepartmentInfoFirstTime(String companyId)
	{
		return this.employeeDao.downloadDepartmentInfoFirstTime(companyId);
	}
	
	/*************************************************************************
	 *       初次登录时用户下载                                                                                                                                                           *
	 *       END                                                             * 
	 *                                                                       *
	 *************************************************************************/
	/**
	 * 首次下载集团通讯录数据 json格式.
	 * @param userId 用户ID
	 * @param companyId 用户所在公司
	 * @return  json格式的通讯录名单
	 * @since v1.3.0
	 */
	public String getUserInfo4json(String userId,String companyId){
		long t0=new Date().getTime();
		Object[] arr= this.employeeDao.getFirstUserInfo(userId,companyId);
		if(LOG.isDebugEnabled()){
			LOG.debug("get first user info [get date time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		if(arr==null||arr.length!=6){//非法数据
			return "";
		}
		if(!(arr[0] instanceof List)){//非法数据
			return "";
		}
		if(!(arr[1] instanceof Map)){//非法数据
			return "";
		}
		if(!(arr[2] instanceof String)){//非法数据
			return "";
		}
		//拼接最终的字符串
		StringBuilder res=new StringBuilder();
		String[] src=new String[]{"grid_number","display_order","picture","user_company_id","employee_id","employee_name","company_id","company_address","mobile","mobile_short","telephone2","tel_short","email","telephone","weibo","weixin","qq","school","user_major","user_grade","user_class","student_id","birthday","native_place","address","home_address","manage_flag","employee_firstword","employee_fullword","mood","user_company","parent_department_name","remark","visible_flag","department_name","headship_name"};
		String[] target=new String[]{"a","b","c","d","e","f","g","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","a0","a1","a2","a3","a4","a5","a6","a7","a8","b0","b1"};
		String[] src1=new String[]{"user_company_id","user_department_id","headship_id","headship_name","department_id","department_path","taxis"};
		String[] target1=new String[]{"a","c","e","f","g","h","i"};
		int j=0;
		List<Map<String,Object>> list=(List<Map<String,Object>>)arr[0];
		Map<String,List<Map<String,Object>>> cmp=(Map)arr[1];
		Integer headship=(Integer)arr[4];
		for(Map<String,Object> map : list)
		{
			this.appendUsrJson(companyId,headship,src,target,map,src1,target1,cmp,arr[5],res);
		}
		res.append("]}");
		if(LOG.isDebugEnabled()){
			LOG.debug("get first user info [format date time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		cmp=null;list=null;
		return "{\"k\":\""+arr[2]+"\",\"r\":["+res.toString();
	}
	/**
	 * 自己组装json.
	 * @param k 字符键
	 * @param v 字符串
	 * @param b 字符
	 * @return StringBuilder
	 * @since 1.3.0
	 */
	private StringBuilder appendJson(String k,Object v,StringBuilder b){
		b.append("\"").append(k).append("\":\"").append(v==null?"":v).append("\"");
		return b;
	}
	/**
	 * 添加业务数据的默认值.
	 * @param k 字符键
	 * @param v 字符串
	 * @return String
	 * @since 1.3.0
	 */
	private String fmtCol(String k,Object v){
		switch (k) {
		case "department_fax":
			return v==null||v.equals("")?"8888":v.toString();
		case "display_order":
			return v==null||((Integer)v)==0?"999999":v.toString();
		case "manage_flag":
			return v==null||v.equals("")?"0":v.toString();
		default:
			return v==null?"":v.toString();
		}
	}
	/**
	 * 获取集团通讯录更新数据 json格式.
	 * @param userId 用户ID
	 * @param filterCompany 需要过滤的企业
	 * @param curVersion 用户当前的版本
	 * @param headships 用户所处的headship
	 * @return  json格式的通讯录名单
	 * @since v1.3.0
	 */
	public String getUpdateUser4json(String userId,String filterCompany,String curVersion,Map<String,StringBuilder> headships){
		long t0=new Date().getTime();
		Object[] arr= this.employeeDao.getUpdateUserInfo(userId,filterCompany,curVersion,headships);
		if(LOG.isDebugEnabled()){
			LOG.debug("update user info [get date time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		if(arr==null||arr.length!=2){
			//异常数据
			return null;
		}
		//返回结果
		StringBuilder rw=new StringBuilder();
		StringBuilder lv=new StringBuilder();
		String[] src=new String[]{"grid_number","display_order","picture","user_company_id","employee_id","employee_name","company_id","company_address","mobile","mobile_short","telephone2","tel_short","email","telephone","weibo","weixin","qq","school","user_major","user_grade","user_class","student_id","birthday","native_place","address","home_address","manage_flag","employee_firstword","employee_fullword","mood","user_company","parent_department_name","remark","visible_flag","update_type","department_name","headship_name"};
		String[] target=new String[]{"a","b","c","d","e","f","g","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","a0","a1","a2","a3","a4","a5","a6","a7","a8","a9","b0","b1"};
		String[] src1=new String[]{"user_company_id","user_department_id","headship_id","headship_name","department_id","department_path","taxis"};
		String[] target1=new String[]{"a","c","e","f","g","h","i"};
		if(arr[1]==null){
			return "{\"k\":\""+arr[0]+"\"}";
		}
		//遍历更新，按公司
		for(Object[] item: (List<Object[]>)arr[1]){
			String companyId=(String)item[0];//公司
			Map<String,List<Map<String,Object>>> cmp=(Map)item[2];
			Integer headship=(Integer)item[4];
			if(item[1]!=null){
				//添加资料变更信息
				for(Map<String,Object> users: (List<Map<String,Object>>)item[1]){
					this.appendUsrJson(companyId,headship,src,target,users,src1,target1,cmp,item[5],rw);
				}
			}
			if(item[6]!=null){
				//添加可见性,权限变更
				for(String usercmpid : (List<String>)item[6]){
					lv.append(lv.length()>0?",{":"{");
					this.appendJson("a",usercmpid,lv).append(",");
					this.appendJson("b",item[7],lv);
					lv.append("}");
				}
			}
		}
		//返回字符串
		StringBuilder res=new StringBuilder("{\"k\":\"").append(arr[0]).append("\"");
		if(rw.length()>0){
			res.append(",\"r\":[").append(rw).append("]");
		}
		if(lv.length()>0){
			res.append(",\"v\":[").append(lv).append("]");
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("update user info [format date time]--"+(new Date().getTime()-t0));
			t0=new Date().getTime();
		}
		return res.append("}").toString();
	}
	
	/**
	 * 数据格式化为json字符串，带权限控制功能.
	 * @param cmp 所在公司
	 * @param headship 用户的级别
	 * @param src 用户数据字段
	 * @param target 用户数据格式化字段
	 * @param users 用户数据
	 * @param src1 部门数据字段
	 * @param target1 部门数据格式化字段
	 * @param dparts 部门信息
	 * @param prilivalg 权限信息
	 * @param rw 添加的字符串
	 * @since 1.3.0
	 */
	private void appendUsrJson(String cmp,Integer headship,String[] src,String[] target,Map<String,Object> users,
							String[] src1,String[] target1,Map<String,List<Map<String,Object>>> dparts,
							Object prilivalg,StringBuilder rw){
		rw.append(rw.length()>0?",{":"{");
		StringBuilder tmp=new StringBuilder();
		String user_company_id=(String)users.get("user_company_id");
		List<Map<String,Object>> d=dparts.get(user_company_id);
		StringBuilder dpts=new StringBuilder();
		//添加部门
		if(d!=null&&!d.isEmpty()){
			for(Map<String,Object> dmp : d){
				tmp.append(tmp.length()>0?",{":"{");
				for(int i=0;i<src1.length;i++){
					if(i>0){
						tmp.append(",");
					}
					this.appendJson(target1[i],fmtCol(src1[i],dmp.get(src1[i])),tmp);
				}
				tmp.append("}");
				if(AYD_FUYANG.equals(cmp)&&headship>3){
					dpts.append(dpts.length()>0?",":"").append(dmp.get("department_id"));
				}
			}
		}
		//添加通讯录信息
		for(int i=0;i<src.length;i++){
			if(i>0){
				rw.append(",");
			}
			if(prilivalg==null||!"visible_flag".equals(src[i])){
				this.appendJson(target[i],fmtCol(src[i],users.get(src[i])),rw);
				continue;
			}
			//判断权限
			if(AYD_FUYANG.equals(cmp)&&headship<=3){
				//阜阳公司,小于3的主要部门可见	[四个主要部门内员工，可以查看除领导外的所有员工]
				this.appendJson(target[i],((Map)prilivalg).containsKey(user_company_id)?"0":"1",rw);
			}else if(AYD_FUYANG.equals(cmp)&&headship>3){
				this.appendJson(target[i],dpts!=null&&dpts.toString().equals(prilivalg)?"1":"0",rw);
			}else{
				//通用，可见小于一级
				String lv=(String)((Map)prilivalg).get(user_company_id);
				String fg="0";
				if(StringUtils.hasText(lv)){
					fg=Integer.parseInt(lv)>=(headship-1)?"1":"0";
				}
				this.appendJson(target[i],fg,rw);
			}
		}
		if(d==null||d.isEmpty()){
			rw.append("}");
			return;
		}
		//添加部门
		rw.append(",\"dp\":[").append(tmp).append("]}");
	}
	/**
	 * 返回用户的ID
	 * @param userId 用户的ID
	 * @param mobile 用户的手机号
	 * @param companyId 用户所在的公司
	 * @return 用户的ID
	 */
	public String getUserId(String userId,String mobile,String companyId){
		return this.employeeDao.getUserId(userId,mobile,companyId);
	}
}
