// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.CompanyMapService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李雨辰
 * <br />邮箱：li.yuchen@ustcinfo.com
 * <br />描述：CompanyMapController
 * <br />版本:1.0.0
 * <br />日期： 2014-08-04 上午11:36:50
 * <br />CopyRight © 2013 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Controller
@RequestMapping("pc/companyMap")
public class CompanyMapController extends BaseController{
	
	@Autowired
	private CompanyMapService companyMapService;
	
	@RequestMapping("main.htm")
	public String toMain(ModelMap modelMap,Search search){
		User user = SecurityContextUtil.getCurrentUser();
		//将当前用户信息也送到客户端，以做权限控制
		modelMap.addAttribute("user", user);
		Pagination<?> pagination = this.companyMapService.getCompanyMapInfo(8, search.getPageNo());
		modelMap.addAttribute("pagination", pagination);
		return "web/companyMap";
	}
	
	@RequestMapping("add.htm")
	@ResponseBody
	public String addMapInfo(HttpServletRequest request,@RequestParam("mapZip") MultipartFile mapZip,String companyIds){
		if(mapZip!=null&&mapZip.getSize()!=0){
			String filename = mapZip.getOriginalFilename();
	        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
	        System.out.println(filename.substring(0,filename.lastIndexOf(".")));
			if("zip".equals(extName)){
				//判断是不是图片
				String fileName = this.companyMapService.uploadPhoto(mapZip, request);
				if(fileName=="ERROR"){
					return "01";
				}else{
					this.companyMapService.setCompanyMap(fileName, companyIds,request,filename.substring(0,filename.lastIndexOf(".")));
					return fileName;
				}
			}else{
				//不是图片
				return "02";
			}
			
		}else{
			return "03";
		}
	}

}
