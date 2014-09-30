// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.sellmanager.util.UUIDUtil;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-14 下午3:21:23
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/user")
public class UserController extends BaseController{
	
	@Resource
	private UserService userService;
	
	/**
	 * 修改密码.
	 * @param oldPassword 
	 * @param newPassword 
	 * @param request 
	 * @return 
	 * 返回类型：Map<String,String>
	 */
	@RequestMapping(value="/changePassword.htm")
	@Transactional
	@ResponseBody
	public Map<String, String> changePassword(String oldPassword,String newPassword,HttpServletRequest request){
		Map<String, String>  map = new HashMap<String, String>();
		User user = SecurityContextUtil.getCurrentUser();
		SystemUser systemUser = this.userService.getEntity(user.getUserId());
		if(!MD5Tools.isPasswordValid(systemUser.getPassword(), oldPassword, systemUser.getPassSalt())){
			map.put("success", "false");
			map.put("msg", "旧密码输入不正确");
			return map;
		}
		String passwordSalt = UUIDUtil.generateUUID();
		systemUser.setPassSalt(passwordSalt);
		systemUser.setPassword(MD5Tools.encodePassword(newPassword, passwordSalt));
		systemUser.setModifyTime(new Date());
		this.userService.updateEntity(systemUser);
		map.put("success", "true");
		return map;
	}
	
	

}
