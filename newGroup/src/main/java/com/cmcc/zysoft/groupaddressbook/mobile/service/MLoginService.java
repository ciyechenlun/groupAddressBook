// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MLoginDao;
import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.groupaddressbook.util.Tokens;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.Token;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MLoginService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 上午11:11:49
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MLoginService extends BaseServiceImpl<SystemUser, String>{
	
	@Resource
	private MLoginDao mLoginDao;
	
	@Resource
	private TokenService tokenService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MInterfaceLogService mInterfaceLogService;
	
	@Override
	public HibernateBaseDao<SystemUser, String>getHibernateBaseDao(){
		return this.mLoginDao;
	}
	
	/**
	 * 手机侧登陆.
	 * @param userCode 登陆账号
	 * @param password 密码
	 * @param IMEI 
	 * @param IMSI 
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws ParseException 
	 */
	@Transactional
	public Map<String, Object> checkLogin(String userCode, String password, 
			String IMEI, String IMSI) throws ParseException{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userName", userCode);
		List<SystemUser> userList = this.findByNamedParam
				(new String[]{"userName","delFlag"}, new Object[]{userCode,"0"});
		if(userList.size()>0){
			//成员管理判断
			boolean forbiddenFlag = checkForbidden(userCode); 
			if(forbiddenFlag){
				String passSalt = userList.get(0).getPassSalt().toString();
				String realPass = userList.get(0).getPassword().toString();
				String pass = MD5Tools.encodePassword(password, passSalt);
				if(pass.equals(realPass)){
					Map<String, Object> loginUser = this.mLoginDao.loginUser(userCode);
					String salt = UUIDUtil.generateUUID();
					boolean isEmployee = this.mLoginDao.checkIsEmployee(userList.get(0).getUserId());
					if(isEmployee){
						String empId = userList.get(0).getEmployeeId().toString();
						String userId = userList.get(0).getUserId().toString();
						SystemUser systemUser = this.userService.getEntity(userId);
						systemUser.setLoginTime(new Date());
						this.userService.updateEntity(systemUser); //更新最后登录时间
						Date createDate = new Date();
						SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String token = Tokens.generate(IMEI, createDate, salt);
						this.tokenService.add(empId, token, salt, sd.format(createDate)); //生成令牌插入令牌表
						map.put("cmd", "login");
						map.put("code", "0");
						map.put("user_id", loginUser.get("user_id").toString());
						map.put("employee_id", loginUser.get("empId").toString());
						map.put("employee_name", loginUser.get("empName").toString());
						map.put("department_id", loginUser.get("deptId").toString());
						map.put("department_name", loginUser.get("deptName").toString());
						map.put("department_level", loginUser.get("deptLevel").toString());
						map.put("mobile", loginUser.get("mobile").toString());
						map.put("mobile_short", loginUser.get("mobile_short").toString());
						map.put("tel", loginUser.get("tel").toString());
						map.put("tel_short", loginUser.get("tel_short").toString());
						map.put("email", "");
						map.put("token", token);
						String employeeId = loginUser.get("empId").toString();
						String companyId = userList.get(0).getCompany().getCompanyId();
						this.mInterfaceLogService.addLog(employeeId, companyId, "登陆");//日志表添加数据
					}else{
						map.put("cmd", "login");
						map.put("code", "-1");
					}
				}else{
					map.put("cmd", "login");
					map.put("code", "11");
				}
			}else{
				//成员被禁用，无权登陆
				map.put("cmd", "login");
				map.put("code", "14");
			}
		}else{
			map.put("cmd", "login");
			map.put("code", "10");
		}
		return map;
	}
	
	/**
	 * 修改密码.
	 * @param userId 用户Id
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param token 令牌
	 * @param IMEI 
	 * @return Map<String,Object>
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> changePwd(String userId,String oldPwd,String newPwd,String token,String IMEI){
		Map<String,Object> map = new HashMap<String,Object>();
		SystemUser systemUser = this.userService.getEntity(userId);
		List<Token> tokeList = this.tokenService.findByNamedParam("employeeId", systemUser.getEmployeeId());
		Token toke = tokeList.get(0);
//		Token toke = this.tokenService.getToken(systemUser.getEmployeeId());
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag;
		try {
			flag = Tokens.validate(IMEI, toke.getTokenValue(), 
					toke.getPassSalt(), sd.format(toke.getTokenDate()), sd.format(toke.getAddDate()));
			if(flag){
				if(!MD5Tools.isPasswordValid(systemUser.getPassword(), oldPwd, systemUser.getPassSalt())){
					map.put("success", "false");
					map.put("msg", "旧密码输入不正确");
				}else{
					String passwordSalt = UUIDUtil.generateUUID();
					systemUser.setPassSalt(passwordSalt);
					systemUser.setPassword(MD5Tools.encodePassword(newPwd, passwordSalt));
					systemUser.setModifyTime(new Date());
					this.userService.updateEntity(systemUser);
					String companyId = systemUser.getCompany().getCompanyId();
					this.mInterfaceLogService.addLog(systemUser.getEmployeeId(), companyId, "修改密码");
					map.put("success", "true");
				}
			}else{
				map.put("success", "false");
				map.put("msg", "令牌失效,请重新登陆");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("msg", "系统故障,请稍后再试");
		}
		return map;
	}
	
	/**
	 * 收集用户设备号
	 * @author zhouyusgs#ahmobile.com
	 * @param user_id 系统用户表id
	 * @param imsi 手机imsi号
	 * @param imei 手机imei号
	 */
	public void updateDeviceNo(String userCode,String imsi,String imei)
	{
		this.mLoginDao.updateDeviceNo(userCode, imsi, imei);
		
	}
	
	/**
	 * 判断成员是否被完全禁用（web端成员管理）
	 * @param userCode 手机号
	 * @return
	 */
	public boolean checkForbidden(String userCode){
		return this.mLoginDao.checkForbidden(userCode);
	}

}
