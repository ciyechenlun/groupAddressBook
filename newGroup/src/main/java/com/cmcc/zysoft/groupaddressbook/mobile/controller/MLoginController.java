// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MDeviceService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MInterfaceLogService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MLoginService;
import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.groupaddressbook.util.SendMsgTest;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Device;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.sysmanage.service.CompanyService;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MLoginController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-4 上午10:20:11
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("mobile/login")
public class MLoginController extends BaseController{
	
	@Resource
	private MLoginService mLoginService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MInterfaceLogService mInterfaceLogService;
	
	@Resource
	private MDeviceService mDeviceService;
	
	@Resource
	private CompanyService companyService;
	
	/**
	 * 手机侧登陆接口.
	 * @param userCode 
	 * @param password 
	 * @param IMEI 
	 * @param IMSI 
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws ParseException 
	 */
	@RequestMapping(value="/check")
	@ResponseBody
	public Map<String, Object> login(@RequestParam String userCode, @RequestParam String password,
			@RequestParam String IMEI, @RequestParam String IMSI,HttpServletRequest request) throws ParseException{
		Map<String,Object> map = new HashMap<String,Object>();
		Object attr = request.getSession().getAttribute("count_"+userCode);
		int count=0;
		if(attr !=null){
			count = (int)request.getSession().getAttribute("count_"+userCode);
			if(count>4){
				map.put("cmd", "login");
				map.put("userName",userCode);
				map.put("code", "11");
				return map;
			}
		}
		map = mLoginService.checkLogin(userCode, password, IMEI, IMSI);
		if("11".equals(map.get("code").toString())){
			request.getSession().setAttribute("count_"+userCode, count+1);
		}
		map.put("skins", companyService.getCompanySkin(userCode));
		return map;
	}
	
	/**
	 * 
	 * @param userId 用户Id
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param token 令牌
	 * @param IMEI
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value="/changePwd")
	@Transactional
	@ResponseBody
	public Map<String, Object> changePwd(@RequestParam String userId,@RequestParam String oldPwd,
			@RequestParam String newPwd,@RequestParam String token,@RequestParam String IMEI){
		return this.mLoginService.changePwd(userId, oldPwd, newPwd, token, IMEI);
	}
	
	/**
	 * 手机侧注册.
	 * @param mobile 手机号码
	 * @param type 0为注册,1为忘记密码
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping(value="register")
	@Transactional
	@ResponseBody
	public Map<String, Object> register(@RequestParam String mobile, @RequestParam String type,
			@RequestParam String IMEI, @RequestParam String IMSI, String sysType,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Date lastDate = (Date)request.getSession().getAttribute(mobile);
		
		if(null !=lastDate){
			long between=(new Date().getTime()-lastDate.getTime())/1000;
			if(between<60){
				map.put("success", "false");
				map.put("msg", "时间间隔小于60秒");
				return map;
			}
		}
		//成员管理判断
		boolean forbiddenFlag = this.mLoginService.checkForbidden(mobile);
		//首选检测是否是异网用户，如果是异网用户，判断当前企业是否开启允许异网用户使用选项
		if(StringUtils.hasText(mobile))
		{
			String subMobile = mobile.substring(0, 3);
			if(isOtherNet(subMobile))
			{
				boolean otherNetLimit = true;
				List<Map<String,Object>> list = this.companyService.getCompanyByMobile(mobile);
				if(list.size() == 0)
				{
					map.put("success", "false");
					map.put("msg", "用户信息错误");
					return map;
				}else{
					if(!forbiddenFlag){
						map.put("success", "false");
						map.put("msg", "无权登陆，请联系企业管理员");
						return map;
					}
				}
				for (Map<String,Object> comp :list )
				{
					if(StringUtils.hasText(comp.get("other_net_flag").toString())){
						//只要有一家公司未开启外网限制，则允许登录。other_net_flag 0不限制，1限制
						if(comp.get("other_net_flag").toString().equals("0"))
						{
							otherNetLimit = false;
						}
					}
				}
				if(otherNetLimit)
				{
					map.put("success", "false");
					map.put("msg", "对不起，异网禁止注册");
					return map;
				}
			}
		}
		List<SystemUser> list = this.userService.
				findByNamedParam(new String[]{"userName","delFlag"}, new Object[]{mobile,"0"});
		if(list.size()>0){
			if(forbiddenFlag){
				SystemUser systemUser = list.get(0);
				//if device is forbidden
				List<Device> list_dev = this.mDeviceService.findByNamedParam("employeeId", 
						systemUser.getEmployeeId());
				boolean isForbidden = false;
				if(list_dev.size()>0)
				{
					if(list_dev.get(0).getStatus().equals("0"))
						isForbidden = true;
				}
				
				if(isForbidden){
					map.put("success", "false");
					map.put("msg", "设备被禁用,禁止注册");
				}
				else{
					String salt = UUIDUtil.generateUUID();
					int number = (int)((Math.random()+1)*1000000);
					String code = ""+number;
					code = code.substring(1, 7);
					String passWord = MD5Tools.encodePassword(code, salt);
					String companyId = systemUser.getCompany().getCompanyId();
					String employeeId = systemUser.getEmployeeId();
					String msg = "";
					if("0".equals(type)){
						msg = "集团通讯录业务注册成功，您的密码为"+code;
					}else{
						msg = "您的集团通讯录业务新密码为"+code;
					}
					String result = SendMsgTest.sendMsg(msg, mobile);
					//String result = "1111";
					if(StringUtils.hasText(result)&&"SUCCESS".equals(result)){
						systemUser.setPassSalt(salt);
						systemUser.setPassword(passWord);
						this.userService.updateEntity(systemUser);
						this.mDeviceService.addOrUpdateRegistercode(systemUser.getEmployeeId(), mobile, IMEI, IMSI,sysType);
						if("0".equals(type)){
							this.mInterfaceLogService.addLog(employeeId, companyId, "注册");
						}else{
							this.mInterfaceLogService.addLog(employeeId, companyId, "忘记密码");
						}
						request.getSession().setAttribute(mobile, new Date());
						map.put("success", "true");
						map.put("msg", "注册成功");
					}else{
						map.put("success", "false");
						map.put("msg", "注册失败");
					}
				}
			}else{
				//被禁用
				map.put("success", "false");
				map.put("msg", "无权登陆，请联系企业管理员");
				return map;
			}
		}else{
			map.put("success", "false");
			map.put("msg", "用户不存在");
		}
		return map;
	}
	
	/**
	 * 判断是否为异网号码
	 */
	private boolean isOtherNet(String mobile)
	{
		boolean ret = false;
		if (mobile.equals("130") || mobile.equals("131") || mobile.equals("132") || 
				mobile.equals("155") || mobile.equals("156") || mobile.equals("186") || mobile.equals("185"))
		{
			//联通号段
			ret = true;
		}
		else if(mobile.equals("133") || mobile.equals("153") || mobile.equals("189") || mobile.equals("180"))
		{
			//电信号段
			ret = true;
		}
		else
		{
			ret = false;
		}
		return ret;
	}
	
	/**
	 * @author zhouyusgs#ahmobile.com
	 * @param usercode ->employee_id
	 * @param imsicode
	 * @param imei
	 * @return
	 */
	@RequestMapping(value="update_device")
	@ResponseBody
	public Map<String,Object> update_device(@RequestParam String userCode,@RequestParam String imsi,@RequestParam String imei)
	{
		Map<String,Object> map = new HashMap<>();
		this.mLoginService.updateDeviceNo(userCode, imsi, imei);
		return map;
	}

}
