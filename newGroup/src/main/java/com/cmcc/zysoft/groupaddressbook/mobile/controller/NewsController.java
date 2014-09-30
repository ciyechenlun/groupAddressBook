/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:NewsController.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-11-12
 */
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.controller
 * 创建时间：2013-11-12
 */

@Controller
@RequestMapping("mobile/news")
public class NewsController extends BaseController {
	
	@RequestMapping("/showNews.htm")
	/**
	 * 显示活动内容
	 * @param news_id
	 * @return
	 */
	public String showNews(String news_id,ModelMap modelMap)
	{
		modelMap.addAttribute("news_id", news_id);
		return "news/showNews";
	}
}
