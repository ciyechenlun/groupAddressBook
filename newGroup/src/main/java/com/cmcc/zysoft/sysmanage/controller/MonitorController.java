// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：监控模块控制器
 * <br />版本:1.0.0
 * <br />日期： 2013-2-17 下午5:16:27
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/monitor")
public class MonitorController extends BaseController {
	
	
	/**
	 * 登录日志查看页面
	 */
	private static String LOGIN_LOG_VIEW = "sysmanage/monitor/loginLog";
	
	/**
	 * 跳转到登录日志查看页面
	 * 
	 * @return 
	 * 返回类型：String
	 */
	
	public String loginLog(){
		return LOGIN_LOG_VIEW;
	}
	
}
