// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.InterfaceLogService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：InterfaceLogController
 * <br />版本:1.0.0
 * <br />日期： 2013-4-12 下午3:39:12
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/interface")
public class InterfaceLogController extends BaseController{
	
	@Resource
	private InterfaceLogService interfaceLogService;
	
	/**
	 * 跳转到接口日志页面.
	 * @param modelMap 
	 * @param search 
	 * @param key 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String logList(ModelMap modelMap,Search search,String key){
		String companyId = SecurityContextUtil.getCompanyId();
		Pagination<?> pagination = this.interfaceLogService.logList(6, search.getPageNo(), companyId, key);
		modelMap.addAttribute("pagination", pagination);
		return "web/interface";
	}
	

}
