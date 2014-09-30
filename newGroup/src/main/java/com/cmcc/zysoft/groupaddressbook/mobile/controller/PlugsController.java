/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:PlugsController.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-9-16
 */
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.PlugService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.controller
 * 创建时间：2013-9-16
 */
@Controller
@RequestMapping("mobile/plugs")
public class PlugsController extends BaseController {
	
	@Resource
	private PlugService plugService;
	
	@RequestMapping("/getMyPlugs")
	@ResponseBody
	/**
	 * 根据用户编号，返回插件列表
	 * @param usercode：员工的employee_id
	 * @return
	 */
	public Map<String,Object> getMyPlugs(String usercode)
	{
		Map<String,Object> map = new Hashtable<String,Object>();
		map.put("cmd", "getMyPlugs");
		if(StringUtils.hasText(usercode)){
			try{
				List<Map<String,Object>> list = this.plugService.getMyPlugs(usercode);
				map.put("code", "0");
				map.put("value", list);
			}
			catch(Exception e)
			{
				map.put("code", "-1");
				map.put("value", "");
			}
		}
		else
		{
			map.put("code", "-1");
			map.put("value", "");
		}
		return map;
	}
}
