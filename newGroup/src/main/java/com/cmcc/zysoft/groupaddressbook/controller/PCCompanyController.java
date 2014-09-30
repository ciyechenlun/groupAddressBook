// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCCompanyController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-22 上午10:54:26
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/pcompany")
public class PCCompanyController extends BaseController{
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	/**
	 * 新增通讯录
	 * @param modelMap
	 * @param companyName
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping("/save.htm")
	@Transactional
	@ResponseBody
	public Map<String, Object> save(ModelMap modelMap,String companyName,String companyId){
		Map<String,Object> map = new HashMap<String,Object>();
		User user = SecurityContextUtil.getCurrentUser();
		if("s_admin".equals(user.getUsername())){
			map.put("success", "fasle");
			map.put("msg", "管理员不能新增或修改通讯录信息");
			return map;
		}else{
			if(StringUtils.hasText(companyId)){
				return this.pcCompanyService.updateUserCompany(companyId, companyName, user.getEmployeeId());
			}else{
				return this.pcCompanyService.addUserCompany(companyName, user.getEmployeeId());
			}
		}
	}
	
	/**
	 * 删除群组
	 * @param companyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("delete.htm")
	@Transactional
	@ResponseBody
	public String delete(String companyId){
		return this.pcCompanyService.delete(companyId);
	}

}
