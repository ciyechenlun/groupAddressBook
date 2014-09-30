// ~ CopyRight © 20123 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.LookGroupService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

/**
 * @author 张军
 * <br />邮箱：zhang.jun3@ustcinfo.com
 * <br />描述：clearGroupController
 * <br />版本:1.0.0
 * <br />日期： 2013-11-15 上午11:36:50
 * <br />CopyRight © 2013 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("pc/clearGroup")
public class clearGroupController extends BaseController {
	
	@Resource
	private LookGroupService lookGroupService;
	
	/**
	 * 删除指定公司下的所有通讯录.（物理删除）
	 * @param modelMap 
	 * @param companyId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/clearByCompany.htm")
	@Transactional
	@ResponseBody
	public String clearByCompany(ModelMap modelMap,String companyId){
		if("".equals(companyId) || null==companyId){
			companyId = SecurityContextUtil.getCompanyId();
		}
		String result = "";
		try{
			this.lookGroupService.clearGroupByCompany(companyId);
			result = "SUCCESS";
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}