// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeParamService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MInterfaceLogService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MSystemUserService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MUserCompanyService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MUserModifyService;
import com.cmcc.zysoft.groupaddressbook.service.AuditConfigService;
import com.cmcc.zysoft.groupaddressbook.service.GroupVersionService;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.model.UserModify;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：EmployeeController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-7 下午3:08:50
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("mobile/UserInfo")
public class MEmployeeController extends BaseController {
	
	@Resource
	private MEmployeeService employeeService; 
	
	@Resource
	private MSystemUserService mSystemUserService;
	
	@Resource
	private MInterfaceLogService mInterfaceLogService;
	
	@Resource
	private AuditConfigService auditConfigService;
	
	@Resource
	private MUserCompanyService mUserCompanyService;
	
	@Resource
	private MUserModifyService mUserModifyService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private MEmployeeParamService mEmployeeParamService;
	
	/**
	 * 下载通讯录.
	 * @param departmentIdStr 上次登录时,登录人所在部门,若部门修改,则重新下载数据.
	 * @param userId  
	 * @param versionNum 版本号,若服务器端权限和本人部门未改变,则仅下载版本号大于上次登录时的版本号的数据
	 * @param filter_company：2013/11/12新增参数，更新接口只更新有用户的企业，由客户端上传需要过滤的编号，用逗号分隔
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public Map<String,Object> getUserInfoByDeptId(String departmentIdStr,
			String userId,String versionNum,String filter_company){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			SystemUser systemUser = this.mSystemUserService.getEntity(userId);
			if(systemUser!=null){
				String companyId = systemUser.getCompany().getCompanyId();
				String employeeId = systemUser.getEmployeeId();
				this.mInterfaceLogService.addLog(employeeId, companyId, "联系人下载");
			}
			return this.employeeService.getUserInfoByDeptId(departmentIdStr,userId,versionNum,filter_company);		
		}catch (Exception e) {
			map.put("success", "false");
			map.put("error", e.getMessage());
			return map;
		}
	}
	
	
	/**
	 * 获取指定公司下的用户
	 * @param userId：用户id
	 * @param versionNum：版本号，如果为0，则返回所有
	 * @param companyId：公司编号
	 * @return
	 */
	@RequestMapping(value="/getUserInfoByCompany")
	@ResponseBody
	public Map<String,Object> getUserInfoByCompany(String userId,String versionNum,String companyId)
	{
		return this.employeeService.getUserInfoByCompany(userId, versionNum, companyId);
	}
	
	/*************************************************************************
	 *       初次登录时用户下载 / 暂时未用                                                                                                                                     *
	 *       用户部门一次下载速度过慢，从V1.2.4版本开始，分成两个接口下载                                                                  * 
	 *                                                                       *
	 *************************************************************************/
	
	@RequestMapping(value="/downloadUserInfoFirstTime")
	@ResponseBody
	/**
	 * 首次登录时用户下载
	 * @param userId
	 * @return
	 */
	public Map<String,Object> downloadUserInfoFirstTime(String userId)
	{
		return this.employeeService.downloadUserInfoFirstTime(userId);
	}
	
	@RequestMapping(value="/downloadDepartmentInfoFirstTime")
	@ResponseBody
	/**
	 * 首次登录时下载用户部门隶属关系
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> downloadDepartmentInfoFirstTime(String companyId)
	{
		return this.employeeService.downloadDepartmentInfoFirstTime(companyId);
	}
	
	/*************************************************************************
	 *       初次登录时用户下载                                                                                                                                                           *
	 *       END                                                             * 
	 *                                                                       *
	 *************************************************************************/
	
	/**
	 * 手动更新联系人信息.
	 * @param versionNum 
	 * @param employeeId 
	 * @param pictureName 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/updateUserInfo")
	@ResponseBody
	public Map<String,Object> updateUserInfo(String versionNum,String userCompanyId,String pictureName){
		try{
			UserCompany userCompany = this.mUserCompanyService.getEntity(userCompanyId);
			this.mInterfaceLogService.addLog(userCompany.getEmployeeId(), userCompany.getCompanyId(), "更新单个联系人");
			return this.employeeService.updateUserInfo(versionNum,userCompanyId,pictureName);
		}catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", "false");
			return map;
		}
	}
	
	/**
	 * 保存图片.
	 * @param employeeId 
	 * @param pictureName 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/uploadPic")
	@Transactional
	@ResponseBody
	public Map<String, Object> uploadPicture(String employeeId,String pictureName){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Employee employee = this.employeeService.getEntity(employeeId);
			employee.setPicture(pictureName);
			this.employeeService.updateEntity(employee);
			List<SystemUser> systemUserList = this.mSystemUserService.findByNamedParam("employeeId", employeeId);
			SystemUser systemUser = systemUserList.get(0);
			String companyId = systemUser.getCompany().getCompanyId();
			this.mInterfaceLogService.addLog(employeeId, companyId, "保存联系人头像");
			map.put("success", "true");
		}catch (Exception e){
			map.put("success", "false");
		}
		return map;
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
	@RequestMapping(value = "/update")
	@Transactional
	@ResponseBody
	public Map<String, Object> updateUserCompany(String userCompanyId,String employeeName, 
			String userCompany, String departmentName, String headshipName, String mobile, 
			String telephone, String weibo, String weixin, String school, String userMajor, 
			String userGrade, String userClass, String studentId, String nativePlace, 
			String address, String homeAddress, String remark,  String mobileShort, 
			String telShort, String telephone2, String email, String qq, String mood,String picture){
		Map<String,Object> map = new HashMap<String,Object>();
		UserCompany old_userCompany = this.mUserCompanyService.getEntity(userCompanyId);
		List<Map<String, Object>> auditList = this.auditConfigService.list();
		if(!mobile.equals(old_userCompany.getMobile())){
			//主要号码更改,需将登陆账号修改.
			//判断是否重复
			List<Employee> empList = this.employeeService.findByNamedParam("mobile", mobile);
			if(empList.size()>0){
				map.put("success", "fasle");
				map.put("msg", "手机号码重复");
				return map;
			}else{
				//判断时候需要审核
				if(auditList.size()>0){
					UserModify userModify = new UserModify();
					userModify.setUserCompanyId(userCompanyId);
					userModify.setEmployeeName(employeeName);
					userModify.setUserCompany(userCompany);
					//userModify.setDepartmentName(departmentName);
					userModify.setHeadshipName(headshipName);
					userModify.setMobile(mobile);
					userModify.setMobileShort(mobileShort);
					userModify.setTelephone(telephone2);
					userModify.setHomeTelephone(telephone);
					userModify.setTelShort(telShort);
					userModify.setWeibo(weibo);
					userModify.setWeixin(weixin);
					userModify.setSchool(school);
					userModify.setUserMajor(userMajor);
					userModify.setUserGrade(userGrade);
					userModify.setUserClass(userClass);
					userModify.setStudentId(studentId);
					userModify.setNativePlace(nativePlace);
					userModify.setAddress(address);
					userModify.setHomeAddress(homeAddress);
					userModify.setRemark(remark);
					userModify.setEmail(email);
					userModify.setQq(qq);
					userModify.setMood(mood);
					userModify.setIsAudited("0");
					userModify.setPicture(picture);
					String userModifyId = this.mUserModifyService.insertEntity(userModify);
					if(StringUtils.hasText(userModifyId)){
						map.put("success", "true");
						map.put("msg", "已提交审核");
						return map;
					}else{
						map.put("success", "fasle");
						map.put("msg", "系统故障,请稍后重试");
						return map;
					}
				}else{
					//不需要审核
					old_userCompany.setMobile(mobile);
					old_userCompany.setEmployeeName(employeeName);
					old_userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(employeeName));
					old_userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(employeeName));
					old_userCompany.setUserCompany(userCompany);
					//old_userCompany.setDepartmentName(departmentName);
					old_userCompany.setHeadshipName(headshipName);
					old_userCompany.setMobileShort(mobileShort);
					old_userCompany.setTelephone2(telephone2);
					old_userCompany.setTelephone(telephone);
					old_userCompany.setTelShort(telShort);
					old_userCompany.setWeibo(weibo);
					old_userCompany.setWeixin(weixin);
					old_userCompany.setSchool(school);
					old_userCompany.setUserMajor(userMajor);
					old_userCompany.setUserGrade(userGrade);
					old_userCompany.setUserClass(userClass);
					old_userCompany.setStudentId(studentId);
					old_userCompany.setNativePlace(nativePlace);
					old_userCompany.setAddress(address);
					old_userCompany.setHomeAddress(homeAddress);
					old_userCompany.setRemark(remark);
					old_userCompany.setEmail(email);
					old_userCompany.setQq(qq);
					old_userCompany.setMood(mood);
					old_userCompany.setPicture(picture);
					this.mUserCompanyService.updateEntity(old_userCompany);
					this.employeeService.updateEmpMobile(old_userCompany.getEmployeeId(), mobile);
					Employee employee = this.employeeService.getEntity(old_userCompany.getEmployeeId());
					employee.setPicture(picture);
					this.employeeService.updateEntity(employee);
					List<UserCompany> updateEmplist = this.mUserCompanyService.findByNamedParam("employeeId", old_userCompany.getEmployeeId());
					for(UserCompany us : updateEmplist){
						this.groupVersionService.addGroupVersion(us.getUserCompanyId(), "1");
					}
					map.put("success", "true");
					map.put("msg", "已修改");
					return map;
				}
			}
		}else{
			if(auditList.size()>0 &&( 
					!operateNull(mobileShort).equals(operateNull(old_userCompany.getMobileShort()))
					|| !operateNull(userCompany).equals(operateNull(old_userCompany.getUserCompany()))
					|| !operateNull(departmentName).equals(operateNull(old_userCompany.getDepartmentName()))
					|| !operateNull(headshipName).equals(operateNull(old_userCompany.getHeadshipName())))){
				UserModify userModify = new UserModify();
				userModify.setUserCompanyId(userCompanyId);
				userModify.setEmployeeName(employeeName);
				userModify.setUserCompany(userCompany);
				//userModify.setDepartmentName(departmentName);
				userModify.setHeadshipName(headshipName);
				userModify.setMobile(mobile);
				userModify.setMobileShort(mobileShort);
				userModify.setTelephone(telephone2);
				userModify.setHomeTelephone(telephone);
				userModify.setTelShort(telShort);
				userModify.setWeibo(weibo);
				userModify.setWeixin(weixin);
				userModify.setSchool(school);
				userModify.setUserMajor(userMajor);
				userModify.setUserGrade(userGrade);
				userModify.setUserClass(userClass);
				userModify.setStudentId(studentId);
				userModify.setNativePlace(nativePlace);
				userModify.setAddress(address);
				userModify.setHomeAddress(homeAddress);
				userModify.setRemark(remark);
				userModify.setEmail(email);
				userModify.setQq(qq);
				userModify.setMood(mood);
				userModify.setIsAudited("0");
				userModify.setPicture(picture);
				String userModifyId = this.mUserModifyService.insertEntity(userModify);
				if(StringUtils.hasText(userModifyId)){
					map.put("success", "true");
					map.put("msg", "已提交审核");
					return map;
				}else{
					map.put("success", "fasle");
					map.put("msg", "系统故障,请稍后重试");
					return map;
				}
			}else{
				old_userCompany.setMobile(mobile);
				old_userCompany.setEmployeeName(employeeName);
				old_userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(employeeName));
				old_userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(employeeName));
				old_userCompany.setUserCompany(userCompany);
				//old_userCompany.setDepartmentName(departmentName);
				old_userCompany.setHeadshipName(headshipName);
				old_userCompany.setMobileShort(mobileShort);
				old_userCompany.setTelephone2(telephone2);
				old_userCompany.setTelephone(telephone);
				old_userCompany.setTelShort(telShort);
				old_userCompany.setWeibo(weibo);
				old_userCompany.setWeixin(weixin);
				old_userCompany.setSchool(school);
				old_userCompany.setUserMajor(userMajor);
				old_userCompany.setUserGrade(userGrade);
				old_userCompany.setUserClass(userClass);
				old_userCompany.setStudentId(studentId);
				old_userCompany.setNativePlace(nativePlace);
				old_userCompany.setAddress(address);
				old_userCompany.setHomeAddress(homeAddress);
				old_userCompany.setRemark(remark);
				old_userCompany.setEmail(email);
				old_userCompany.setQq(qq);
				old_userCompany.setMood(mood);
				old_userCompany.setPicture(picture);
				this.mUserCompanyService.updateEntity(old_userCompany);
				Employee employee = this.employeeService.getEntity(old_userCompany.getEmployeeId());
				employee.setPicture(picture);
				this.employeeService.updateEntity(employee);
				this.groupVersionService.addGroupVersion(userCompanyId, "1");
				map.put("success", "true");
				map.put("msg", "已修改");
				return map;
			}
		}
	}
	private String operateNull(String data){
		if(data==null){
			return "";
		}
		return data;
	}
	/**
	 * 无权限用户.
	 * @param key 
	 * @param department_fax 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public Map<String, Object> downloadEmployee(String key,String department_fax){
		return this.employeeService.downloadEmployee(key,department_fax);
	}
	
	/**
	 * 增量下载数据.
	 * @param userId
	 * @param mobileRights
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/addUserInfo")
	@ResponseBody
	public Map<String, Object> addUserInfo(String userId,String mobileRights){
		return this.employeeService.addUserInfo(userId, mobileRights);
	}
	
	/**
	 * 修改用户的短信签名
	 * @param usercode employee_id
	 * @param sign 签名
	 * @return
	 */
	@RequestMapping(value = "/updateUserSign")
	@ResponseBody
	public Map<String,Object> updteUserSign(String usercode,String sign)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(this.mEmployeeParamService.updateUserSign(usercode, sign))
		{
			map.put("code", "0");
			map.put("msg", "更新成功");
		}
		else
		{
			map.put("code", "-1");
			map.put("msg", "更新失败");
		}
		return map;
	}
	
	/**
	 * 获取用户签名
	 * @param usercode
	 * @return
	 */
	@RequestMapping(value="/getUserSign")
	@ResponseBody
	public Map<String,Object> getUserSign(String usercode)
	{
		Map<String,Object> map= new HashMap<String, Object>();
		String sign = this.mEmployeeParamService.getUserSign(usercode);
		if(StringUtils.hasText(sign))
		{
			map.put("code", "0");
			map.put("sign", sign);
		}
		else
		{
			map.put("code", "-1");
			map.put("sing", "");
		}
		return map;
	}
	
	/**
	 * 首次下载下载通讯录.
	 * @param userId  用户
	 * @param company 公司
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value = "/getUserInfo/v2")
	@ResponseBody
	public String getUserInfo(String userId,String company){
		SystemUser systemUser = this.mSystemUserService.getEntity(userId);
		String companyId = systemUser.getCompany().getCompanyId();
		String employeeId = systemUser.getEmployeeId();
		this.mInterfaceLogService.addLog(employeeId, companyId, "联系人下载");
		return this.employeeService.getUserInfo4json(userId,company);		
	}
	/**
	 * 更新下载下载通讯录.
	 * @param userId  用户的ID
	 * @param version 版本号,若服务器端权限和本人部门未改变,则仅下载版本号大于上次登录时的版本号的数据
	 * @param filterCompany：2013/11/12新增参数，更新接口只更新有用户的企业，由客户端上传需要过滤的编号，用逗号分隔
	 * @param headship 上次本机用户权限
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value = "/updateUser/v2")
	@ResponseBody
	public String updateUser(String userId,String version,String filterCompany,String headship){
		//1.异常数据判断
		Map<String,StringBuilder>  shipmap=new HashMap();
		if(StringUtils.hasText(headship)){
			List<Map<String, String>> ships=(List<Map<String, String>>) JSONArray.parse(headship);
			if(ships!=null){
				for(Map<String, String> itm:ships){
					String cmpid=itm.get("company");
					if(shipmap.containsKey(cmpid)){
						shipmap.get(cmpid).append(",'").append(itm.get("id")).append("'");
					}else{
						shipmap.put(cmpid,new StringBuilder("'").append(itm.get("id")).append("'"));
					}
				}
			}
		}
		SystemUser systemUser = this.mSystemUserService.getEntity(userId);
		String companyId = systemUser.getCompany().getCompanyId();
		String employeeId = systemUser.getEmployeeId();
		this.mInterfaceLogService.addLog(employeeId, companyId, "联系人更新");
		return this.employeeService.getUpdateUser4json(userId,filterCompany,version,shipmap);		
	}
	/**
	 * 获取用户的身份ID信息.
	 * @param departmentIdStr 上次登录时,登录人所在部门,若部门修改,则重新下载数据.
	 * @param userId  
	 * @param versionNum 版本号,若服务器端权限和本人部门未改变,则仅下载版本号大于上次登录时的版本号的数据
	 * @param filter_company：2013/11/12新增参数，更新接口只更新有用户的企业，由客户端上传需要过滤的编号，用逗号分隔
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value = "/getuid")
	@ResponseBody
	public Map<String,String> getUserId(String userId,String mobile,String companyId){
		if(!StringUtils.hasText(companyId)){
			companyId="8a1896523c29d5ec013c29da5f0f0000";
		}
		Map<String,String> result=new HashMap<>();
		if(!StringUtils.hasText(mobile)){
			result.put("uid", userId);
			result.put("code", "01");
			return result;
		}
		String uid= this.employeeService.getUserId(userId,mobile,companyId);
		if(StringUtils.hasText(uid)){
			result.put("uid", uid);
			result.put("code", "00");
			return result;
		}
		result.put("uid", uid);
		result.put("code", "99");
		return result;
	}
	/**
	 * 修改心情
	 * @param userCompanyId
	 * @param mood
	 * @return
	 */
	@RequestMapping(value = "/updateUserMood")
	@ResponseBody
	public Map<String,Object> updateUserMood(String userCompanyId,String mood){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			UserCompany userCompany = mUserCompanyService.getEntity(userCompanyId);
			userCompany.setMood(mood);
			mUserCompanyService.updateEntity(userCompany);
			this.groupVersionService.addGroupVersion(userCompanyId, "1");
			map.put("success", "true");
			map.put("msg", "已修改");
		} catch (Exception e) {
			map.put("success", "false");
			map.put("msg", "修改失败");
			e.printStackTrace();
		}
		return map;
	}
}
