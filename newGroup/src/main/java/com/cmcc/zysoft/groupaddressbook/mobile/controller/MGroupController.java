package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.mobile.service.MCompanyService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MUserCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.HolidaySkinService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MGroupController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-22 下午午15:20:11
 * <br />CopyRight ahmobile.
 */
@Controller
@RequestMapping("mobile/group")
public class MGroupController extends BaseController {
	
	@Value("${upload.mobile.img.path}")
	private String uploadPath;
	
	@Value("${project.skin.path}")
	private String skinPath;
	
	@Resource
	private MCompanyService mCompanyService;
	
	@Resource
	private MUserCompanyService mUserCompanyService;
	
	@Resource
	private HolidaySkinService holidaySkinService;
	
	/**
	 * 获取我的分组.
	 * @param request
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws IOException 
	 */
	@RequestMapping("/mygroups")
	@ResponseBody
	public Map<String,Object> getMyGroups(HttpServletRequest request) throws IOException
	{
		String usercode = request.getParameter("usercode");
		Map<String,Object> map = this.mCompanyService.getMyGroups(usercode);
		try{
		if (Integer.parseInt(map.get("total").toString()) > 0)
			{
				List<Map<String,Object>> list = (List)map.get("value");
				for(Map<String,Object> m : list)
				{
					String skin = m.get("index_pictrue").toString();
					if(StringUtils.hasText(skin)){
						copySkinToResource(skin,request);
					}
				}
			}
		}
		catch(Exception e)
		{}
		return map;
	}
	
	/**
	 * 下载群组用户.若groupVersion不为空,则为更新数据.
	 * @param usercode
	 * @param token
	 * @param group_id
	 * @param group_version
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping("/getgroupusers")
	@ResponseBody
	public Map<String,Object> getGroupUsers(String usercode,String token,String group_id,String group_version)
	{
		return this.mUserCompanyService.getGroupUsers(group_id, group_version);
	}
	
	/**
	 * 更新我的群组.
	 * @param cmd
	 * @param version
	 * @param usercode
	 * @param token
	 * @return 
	 * 返回类型：Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/updateMygroups")
	@ResponseBody
	public Map<String,Object> updateMygroups(String cmd,String version ,String usercode,String token,HttpServletRequest request) throws IOException{
		//如果Logo不存在，则拷贝
		Map<String,Object> map = this.mCompanyService.updateMyGroups(usercode, version);
		if (Integer.parseInt(map.get("total").toString()) > 0)
		{
			List<Map<String,Object>> list = (List)map.get("value");
			for(Map<String,Object> m : list)
			{
				String skin = m.get("index_pictrue").toString();
				if(StringUtils.hasText(skin)){
					copySkinToResource(skin,request);
				}
			}
		}
		return map;
	}
	
	/**
	 * 将皮肤拷贝到资源目录
	 * @param filename
	 * @throws IOException
	 */
	private void copySkinToResource(String filename,HttpServletRequest request) throws IOException
	{
		//String destPath = "/opt/application/jttxl/tomcat/webapps/ROOT/resources/mobileImg/";
		String destPath = skinPath;
		//String destPath = "D:/MyWorkSpace/groupAddressBook/src/main/webapp/resources";
		File file = null;
		file = new File(uploadPath + File.separator + filename);
		File destFile = new File(destPath +  File.separator + filename);
		//File destFile = new File(request.getSession().getServletContext().getRealPath("")+"/resources/skin/"+filename);
		if(file.exists()){
			if(!destFile.exists()){
				FileUtils.copyFile(file, destFile);
			}
		}
	}
	
	/**
	 * 节假日皮肤
	 * @return
	 */
	@RequestMapping("/getHolidaySkin")
	@ResponseBody
	public Map<String, Object> getHolidaySkin() {
		return this.holidaySkinService.getHolidaySkin();
	}
}