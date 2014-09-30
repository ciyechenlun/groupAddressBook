// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.PCHeadshipService;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：PCHeadshipController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-16 上午11:38:53
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/pheadship")
public class PCHeadshipController {
	
	@Resource
	private PCHeadshipService pcHeadshipService;
	
	/**
	 * 调整到岗位管理页面.
	 * @param modelMap
	 * @param search
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/main.htm")
	public String list(ModelMap modelMap,Search search,String key,HttpServletRequest request){
		
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		Pagination<?> pagination = this.pcHeadshipService.list(8, search.getPageNo(), companyId,key);
		modelMap.addAttribute("pagination", pagination);
		modelMap.addAttribute("key", key);
		return "web/headship";
	}
	
	/**
	 * 获取指定岗位Id的岗位信息.
	 * @param headshipId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/detail.htm")
	@ResponseBody
	public List<Map<String, Object>> headship(String headshipId){
		return this.pcHeadshipService.headship(headshipId);
	}
	
	/**
	 * 新增或修改岗位.
	 * @param headship
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/saveHeadship.htm")
	@ResponseBody
	public String saveHeadship(Headship headship,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		return this.pcHeadshipService.saveHeadship(companyId, headship);
	}
	@RequestMapping("/plSaveHeadship.htm")
	@Transactional
	@ResponseBody
	public String plSaveHeadship(String [] headshipName,String [] headshipLevel,String [] description,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		String retStr ="SUCCESS";
		for (int i=0;i<headshipName.length;i++) {
			if(null != headshipName[i] && !"".equals(headshipName[i])){
				Headship headship = new Headship();
				headship.setHeadshipName(headshipName[i]);
				headship.setHeadshipLevel(headshipLevel[i]);
				headship.setDescription(description[i]);
				retStr = this.pcHeadshipService.saveHeadship(companyId, headship);
				if(retStr.equals("NAME") || retStr.equals("FALSE")){
					break;
				}
			}
		}
	   return retStr;
	}
	
	/**
	 * 单条删除岗位.
	 * @param headshipId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/delete.htm")
	@Transactional
	@ResponseBody
	public String deleteHeadship(String headshipId){
		return this.pcHeadshipService.deleteHeadship(headshipId);
	}
	/**
	 * 跳转到添加职位页面
	 * @return
	 */
	@RequestMapping("/toAddHeadship.htm")
	public String toAddHeadship(){
		return "web/addHeadship";
	}
	/**
	 * 跳转到添加职位页面
	 * @return
	 */
	@RequestMapping("/toEditHeadship.htm")
	public String toEditHeadship(ModelMap modelMap,String headshipId){
		List<Map<String,Object>> list =  this.pcHeadshipService.headship(headshipId);
		Map<String,Object> map = new HashMap<String,Object>();
		if(null != list){
			map= list.get(0);
		}
		modelMap.addAttribute("headship", map);
		return "web/editHeadship";
	}
}
