package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.MasterRulesService;
import com.cmcc.zysoft.groupaddressbook.service.MasterService;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Master;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：角色操作控制器
 * <br />版本:1.0.0
 * <br />日期： 2013-5-22 下午21:25:32
 * <br />CopyRight China Mobile Anhui cmp Ltd
 */
@Controller
@RequestMapping("/pc/right")
public class MasterController {
	
	@Resource
	private MasterService masterService;
	
	@Resource
	private MasterRulesService masterRulesService;
	
	@RequestMapping("/master_list.htm")
	/**
	 * 角色列表界面
	 * @param modelMap
	 * @param search
	 * @param key 搜索关键词，暂未用到
	 * @return
	 */
	public String indexPage(ModelMap modelMap,Search search,String key,HttpServletRequest request)
	{
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		Pagination<?> pagination = this.masterService.masterList(6, search.getPageNo(), companyId,key);
		modelMap.addAttribute("pagination", pagination);
		return "right/master_list";
	}
	
	@RequestMapping("/master_right_set")
	/**
	 * 权限设置
	 * @param modelMap
	 * @param master_id 角色id
	 * @param search
	 * @return
	 */
	public String master_right_set(ModelMap modelMap,String master_id,Search search)
	{
		//根据master_id取出设置的条件
		Master master = this.masterService.getEntity(master_id);
		modelMap.addAttribute("master",master);
		
		//角色下的权限集合
		Pagination<?> masterRules = this.masterRulesService.masterRulesList(10,1,master_id,"");
		modelMap.addAttribute("pagination", masterRules);
		
		return "right/master_right_set";
	}
	
	@RequestMapping("/deleteMasterRules")
	@ResponseBody
	/**
	 * 删除角色规则条件
	 * @param rules_id：规则条件编号
	 * @return 1成功，0失败
	 */
	public String deleteMasterRules(String rules_id)
	{
		if(masterRulesService.deleteMasterRules(rules_id))
			return "1";
		else
			return "0";
	}
	
	@RequestMapping("/master_add.htm")
	@Transactional
	@ResponseBody
	/**
	 * 添加角色提交
	 * @param master_name
	 * @param master_taxis
	 * @return
	 */
	public String master_add(String masterName,String masterTaxis,HttpServletRequest request)
	{
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		this.masterService.saveEntry(masterName, masterTaxis,companyId);
		return "SUCCESS";
	}
	

	
	@RequestMapping("/master_del.htm")
	@ResponseBody
	/**
	 * 删除角色
	 * @param master_id 角色id
	 * @return
	 */
	public String master_delete(String master_id)
	{
		this.masterService.master_delete(master_id);
		return "SUCCESS";
	}
	
	@RequestMapping("/add_master_rules.htm")
	/**
	 * 添加规则页面
	 * @return
	 */
	public String add_master_rules(ModelMap modelMap,String master_id,String rules_id,String type)
	{
		modelMap.put("master_id", master_id);
		modelMap.put("rules_id", rules_id);
		modelMap.put("type", "type");
		return "right/add_master_rules";
	}
	
	@RequestMapping("/add_master_rules_save.htm")
	@ResponseBody
	/**
	 * 保存规则提交至数据库
	 * @param selReleation：与前一个关系的类型
	 * @param selType：条件类型
	 * @param selOperator：操作符
	 * @param selGroups：群组
	 * @param txtValue：比较值
	 * @return SUCCESS为成功，其他情况为失败
	 */
	public String add_master_rules_save(String txtMasterId,String selReleation,String selType,String selOperator,String selGroups,String txtValue)
	{
		String master_rules_content = "{\"rules\":[{";
		String value = "";
		switch(selType)
		{
			case "1": //部门级别
				master_rules_content += "\"field\":\"department_level\",\"op\":\""+selOperator+"\",\"value\":\""+txtValue+"\",\"type\":\"number\"";
				break;
			case "2": //岗位级别
				master_rules_content += "\"field\":\"headship_level\",\"op\":\""+selOperator+"\",\"value\":\""+txtValue+"\",\"type\":\"number\"";
				break;
			case "3": //分组
				master_rules_content += "\"field\":\"group\",\"op\":\""+selOperator+"\",\"value\":\""+selGroups+"\",\"type\":\"string\"";
				break;
			case "4": //分管领导
				master_rules_content += "\"field\":\"upleader\",\"op\":\""+selOperator+"\",\"value\":\""+txtValue+"\",\"type\":\"string\"";
				break;
		}
		master_rules_content += "}]}";
		this.masterRulesService.addMasterRules(txtMasterId, selReleation, master_rules_content);
		return "SUCCESS";
	}
	
	/**
	 * 角色列表界面
	 * @param modelMap
	 * @param search
	 * @param key 搜索关键词，暂未用到
	 * @return
	 */
	@RequestMapping("/getMasterRule.htm")
	public String getMasterRule(ModelMap modelMap,HttpServletRequest request)
	{
		String companyId = SecurityContextUtil.getCompanyId();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		HashMap<String,Object> map = (HashMap<String,Object>)this.masterRulesService.getMasterRule(companyId);
		modelMap.addAttribute("companyId",companyId);
		if(null == map || null == map.get("rules_sql") || "".equals(map.get("rules_sql").toString())){
			return "right/noRight";
		}
		modelMap.addAttribute("masterRule", map);
		return "right/master";
	}
	/**
	 * 角色列表界面
	 * @param modelMap
	 * @param search
	 * @param key 搜索关键词，暂未用到
	 * @return
	 */
	@RequestMapping("/toSetMaster.htm")
	public String toSetMaster(ModelMap modelMap,HttpServletRequest request,String companyId)
	{
		HashMap<String,Object> map = (HashMap<String,Object>)this.masterRulesService.getMasterRule(companyId);
		modelMap.addAttribute("companyId",companyId);
		modelMap.addAttribute("masterRule", map);
		return "right/master";
	}
	/**
	 * 保存权限设置
	 * @param txtMasterId
	 * @param selReleation
	 * @return
	 */
	@RequestMapping("/addMasterRule.htm")
	@ResponseBody
	public String addMasterRule(String companyId,String txtMasterRulesId,String txtMasterId,String selReleation)
	{
		if(null == txtMasterId || txtMasterId.equals("")){
			Master master = new Master();
			master.setCompanyId(companyId);
			master.setMasterName(companyId);
			master.setTaxis(1);
			txtMasterId = this.masterService.insertEntity(master);
		}
		this.masterRulesService.addMasterRule(txtMasterRulesId,txtMasterId, selReleation);
		
		return "SUCCESS";
	}
	@RequestMapping("/delMasterRule.htm")
	@ResponseBody
	public String delMasterRule(String txtMasterRulesId)
	{
		if(null != txtMasterRulesId && !txtMasterRulesId.equals("")){
			this.masterRulesService.deleteMasterRules(txtMasterRulesId);
		}
		
		return "SUCCESS";
	}
}
