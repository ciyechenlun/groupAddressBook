// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：checkController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-1 上午10:25:01
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/check")
public class checkController extends BaseController{
	
	
	/**
	 * 跳转到 查看通讯录页面.
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String main(){
		return "web/check";
	}

}
