// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.RightconfigService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：RightController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-18 上午11:35:50
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/right")
public class RightController extends BaseController{
	
	@Resource
	private RightconfigService rightconfigService;
	
	@Resource
	private DeptMagService deptMagService;
	
	/**
	 * 跳转到权限设置页面.
	 * @param modelMap 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/main.htm")
	public String main(ModelMap modelMap){
		String companyId = SecurityContextUtil.getCompanyId();
		this.rightconfigService.addRightconfigSelf(companyId);
		//当前的部门级别
		int deptLevel = this.deptMagService.deptLevel(companyId);
		//当前的权限级别
		int rightLevel = this.rightconfigService.rightconfigLevel(companyId);
		if(deptLevel-1 > rightLevel){
			this.rightconfigService.addRightconfig(deptLevel, rightLevel, companyId);
		}
		List<Map<String, Object>> list0 = this.rightconfigService.rightConfig("0");
		List<Map<String, Object>> list1 = this.rightconfigService.rightConfig("1");
		List<Map<String, Object>> list2 = this.rightconfigService.rightConfig("2");
		modelMap.addAttribute("list0", list0);
		modelMap.addAttribute("list1", list1);
		modelMap.addAttribute("list2", list2);
		return "web/right";
	}
	
	/**
	 * 保存选择的权限,选中的设为1,未选中的设为0.
	 * @param ids 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/save.htm")
	@ResponseBody
	public String save(String ids){
		if(StringUtils.hasText(ids)){
			String[] rightId = ids.split("[_]");
			String newId = "''";
			for(int i=0;i<rightId.length;i++){
				newId += ",'" + rightId[i] + "'";
			}
			this.rightconfigService.check(newId);
		}else{
			this.rightconfigService.check("");
		}
		return "SUCCESS";
	}
	

}
