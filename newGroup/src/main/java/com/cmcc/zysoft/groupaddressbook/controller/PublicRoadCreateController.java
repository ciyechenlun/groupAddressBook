package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.service.PublicRoadService;
import com.cmcc.zysoft.groupaddressbook.service.PublicUserService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.PublicRoad;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
/**
 * 
 * @author liyuchen
 *
 */
@Controller
@RequestMapping("/pc/publicRoadCreate")
public class PublicRoadCreateController extends BaseController{
	@Resource
	private PublicUserService publicUserService;
	
	@Resource
	private PublicRoadService publicRoadService;
	
	@Resource
	private CompanyService companyService;
	/**
	 * 跳往添加公众号界面
	 * 第一步： 设定基本信息
	 * @return
	 */
	@RequestMapping("/toBasic")
	public String toBasicInfo(){
		return "web/publicRoadCreate";
	}
	
//	/**
//	 * 第二步：设定公告对象
//	 * @param modelMap
//	 * @param publicName
//	 * @param picture
//	 * @param toRange
//	 * @return
//	 */
//	@RequestMapping("/toSetSendTarget")
//	public String toSetSendTarget(ModelMap modelMap,String publicName,String picture,String toRange){
//		modelMap.addAttribute("toRange", toRange);
//		modelMap.addAttribute("publicName", publicName);
//		modelMap.addAttribute("picture", picture);
//		return "web/setSendTarget";
//	}
//	
//	/**
//	 * 第三步：设定公告管理员
//	 * @param modelMap
//	 * @param publicName 公告名
//	 * @param picture 图片名
//	 * @param toRange 发送范围
//	 * @param target 发送对象
//	 * @param toName 发送对象名称
//	 * @return
//	 */
//	@RequestMapping("/toSetManager")
//	public String toSetManager(ModelMap modelMap,String publicName,String picture,String toRange,String target,String toName){
//		modelMap.addAttribute("toRange", toRange);
//		modelMap.addAttribute("publicName", publicName);
//		modelMap.addAttribute("picture", picture);
//		modelMap.addAttribute("target", target);
//		modelMap.addAttribute("toName", toName);
//		return "web/setManager";
//	}
	/**
	 * 点击完成 保存信息
	 * @param request 
	 * @param publicName 公告名
	 * @param picture 图片名
	 * @param toRange 发送范围
	 * @param target 发送对象
	 * @param toName 发送对象名称
	 * @param managerMember 管理员
	 * @param companyId 企业Id
	 * @return
	 */
	@RequestMapping("/addPublicRoad.htm")
	@ResponseBody
	public Boolean addPublicRoad(HttpServletRequest request,String publicId,String publicName,String picture,String toRange,String target,String toName,String managerMember,String companyId){
		if("".equals(companyId) || null == companyId){//获取company实体 
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				Company company = (Company)request.getSession().getAttribute("selCompany");
				companyId = company.getCompanyId();
			}
			else{
				companyId = SecurityContextUtil.getCompanyId();
			}
		}
		//添加公告号
		PublicRoad publicRoad = new PublicRoad();
		publicRoad.setPublicName(publicName);
		publicRoad.setPicture(picture);
		publicRoad.setToName(toName);
		publicRoad.setToRange(toRange);
		publicRoad.setCompanyId(companyId);
		publicRoad.setStatus("0");
		if(StringUtils.hasText(publicId)){
			//编辑后保存
			publicRoad.setPublicId(publicId);
			//更新			
			publicRoadService.updateEntity(publicRoad);
			//删除后添加
			publicRoadService.deleteRoad(false,publicId);
			this.publicUserService.addInfo(publicId, target, managerMember, toRange);
			return true;
		}else{
			//新建
			publicId = this.publicRoadService.insertEntity(publicRoad);
			if(publicId!=null&&StringUtils.hasText(publicId)){
				this.publicUserService.addInfo(publicId, target, managerMember, toRange);
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	@RequestMapping("/uploadPhoto.htm")
	@ResponseBody
	public String uploadPhoto(HttpServletRequest request,@RequestParam("logo") MultipartFile logo,String picture){
		if(logo!=null&&logo.getSize()!=0){
			String filename = logo.getOriginalFilename();
	        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
			if(extName.matches("gif|jpg|jpeg|png|bmp")){
				//判断是不是图片
				String fileName = this.publicRoadService.uploadPhoto(logo,request);
				if(fileName=="ERROR"){
					return "01";
				}else{
					return fileName;
				}
			}else{
				//不是图片
				return "02";
			}
			
		}else{
			return picture;
		}
	}
	
	
	/**
	 * 人员树
	 * @param request
	 * @param id 部门Id
	 * @param companyId 企业Id
	 * @return
	 */
	@RequestMapping("/getUserTree.htm")
	@ResponseBody
	public List<Map<String,Object>> getUserTree(HttpServletRequest request,String id,String companyId){
		Company company;
		if("".equals(companyId) || null == companyId){//获取company实体 
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				company = (Company)request.getSession().getAttribute("selCompany");
				companyId = company.getCompanyId();
			}
			else{
				companyId = SecurityContextUtil.getCompanyId();
				company = this.companyService.getEntity(companyId);
			}
		}
		else
		{
			company = this.companyService.getEntity(companyId);
		}
		List<Map<String,Object>> childList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(id==null||!StringUtils.hasText(id)){
			id = "0";
			childList = this.publicRoadService.deptUserTree(id,companyId);
			Map<String, Object> firstMap = new HashMap<String, Object>();
			
			firstMap.put("id", "0");
			firstMap.put("text", company.getCompanyName());
			firstMap.put("isLeaf", "N");
			firstMap.put("children", childList);
			firstMap.put("state", "open");
			result.add(firstMap);
		}else{
			result = this.publicRoadService.deptUserTree(id,companyId);
		}
		return result;
	}
	/**
	 * 编辑跳转 
	 * @param modelMap
	 * @param request
	 * @param publicId
	 * @param manager 
	 * @return
	 */
	@RequestMapping("/editPublicRoad.htm")
	public String editPublicRoad(ModelMap modelMap,HttpServletRequest request,String publicId,String manager){
		PublicRoad publicRoad = this.publicRoadService.getEntity(publicId);
		modelMap.put("publicName", publicRoad.getPublicName());
		modelMap.put("picture", publicRoad.getPicture());
		modelMap.put("toRange", publicRoad.getToRange());
		modelMap.put("toName", publicRoad.getToName());
		modelMap.put("publicId", publicId);
		modelMap.put("manager", manager);
		//获取发送对象
		List<Map<String,Object>> targetMap = this.publicUserService.getMemberInfo(publicId, "1", publicRoad.getToRange());
		modelMap.put("targetMap", targetMap);
		//获取管理员
		List<Map<String,Object>> managerMap = this.publicUserService.getMemberInfo(publicId, "0","");
		modelMap.put("managerMap", managerMap);
		return "web/editPublicRoad";
	}

}
