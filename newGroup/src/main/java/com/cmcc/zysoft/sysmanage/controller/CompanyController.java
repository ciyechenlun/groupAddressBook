// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.service.CompanyVersionService;
import com.cmcc.zysoft.groupaddressbook.service.DepartmentVersionService;
import com.cmcc.zysoft.groupaddressbook.service.GroupVersionService;
import com.cmcc.zysoft.groupaddressbook.util.UnZip;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.CompanyPlug;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyPlugService;
import com.cmcc.zysoft.sysmanage.service.CompanyService;

/**
 * @author li.menghua
 * @date 2012-11-28 下午4:07:11
 */
@Controller
@RequestMapping("/pc/company")
public class CompanyController extends BaseController{
	
	private static Logger _logger = LoggerFactory.getLogger(CompanyController.class); 
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private CompanyVersionService companyVersionService;
	
	@Resource
	private DepartmentVersionService departmentVersionService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private CompanyPlugService companyPlugService;
	
	@Value("${upload.file.path}")
	private String path;
	
	/**
	 * combortree 获取公司树
	 * @author yandou
	 * @param companyId
	 * @return
	 */
	@RequestMapping("/companyTree/{companyId}.htm")
	@ResponseBody
	public List<Map<String,Object>> companyTree(@PathVariable String companyId){
		_logger.debug("获取公司树");
		return this.companyService.companyTree(companyId);
	}
	
	/**
	 * combortree 获取公司树,企业管理用
	 * @return
	 */
	@RequestMapping("/tree.htm")
	@ResponseBody
	public List<Map<String,Object>> tree(){
		_logger.debug("获取公司树");
		List<Map<String, Object>> list = this.companyService .companyTree("0");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "0");
		map.put("text", "无");
		list.add(map);
		return list;
	}
	
	/**
	 * 跳转到企业管理页面
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String main(Model model){
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> roleList = user.getRoles();
		String roleCode = roleList.get(0).getRoleCode();
		model.addAttribute("roleCode", roleCode);
		_logger.debug("跳转到企业管理页面");
		//获取当前登陆用户所属的公司
		String companyId = SecurityContextUtil.getCompanyId();
		List<Map<String, Object>> list =null;
		if(user.getUsername().equals("s_admin")){
			_logger.debug("获取所有企业列表");
			list = this.companyService.getCompanyTree("");
			model.addAttribute("list", list);
		}else{
			_logger.debug("获取自己所在公司");
			list = this.companyService.getCompanyTree(companyId);
			model.addAttribute("list", list);
		}
		return "sysmanage/company/company";
	}
	
	/**
	 * 获取企业列表
	 * @return
	 */
	@RequestMapping(value="/getAll.htm")
	@ResponseBody
	public List<Map<String, Object>> getCompanyTree(){
		//获取当前登陆用户所属的公司
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		if(user.getUsername().equals("s_admin")){
			_logger.debug("获取所有企业列表");
			return this.companyService.getCompanyTree("");
		}else{
			_logger.debug("获取自己所在公司");
			return this.companyService.getCompanyTree(companyId);
		}
		
	}
	
	/**
	 * 跳转到新增企业页面
	 * @return
	 */
	@RequestMapping(value="/add.htm")
	public String add(ModelMap modelMap){
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> role = user.getRoles();
		
		String flag = "0";
		for(Role r:role){
			String roleId = r.getRoleId();
			//登陆用户为地市级以上管理员才可以管理计步器开关
			if("0".equals(roleId)||"1".equals(roleId)||"3".equals(roleId)){
				flag = "1";
				break;
			}
		}
		//flag：0 计步器开关隐藏；1：计步器开关显示
		modelMap.addAttribute("pedometerFlag", flag);
		_logger.debug("跳转到新增企业页面");
		return "sysmanage/company/add";
	}
	
	/**
	 * 新增企业
	 * @param comp
	 * @param request
	 * @param indexLogo 
	 * @param pedometer 计步器开关：0：计步器打开；1：计步器关闭
	 * @return
	 */
	@RequestMapping(value="/addCompany.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String addCompany(Company comp,HttpServletRequest request,@RequestParam("logo") MultipartFile indexLogo,String pedometer) {
		User user = SecurityContextUtil.getCurrentUser();
		comp.setOrgFlag("1");
		comp.setOrgFrom("1");
		comp.setUserLimitFlag("0");
		comp.setCreateMan(user.getEmployeeId());
		comp.setCreateDate(new Date());
		//comp.setOtherNetFlag(otherNetFlag)
		if(!StringUtils.hasText(comp.getOtherNetFlag()))
		{
			comp.setOtherNetFlag("1");
		}
		String companyId = this.companyService.addCompany(comp, request, indexLogo,pedometer);
		if(!companyId.equals("ERROR"))
		{
			this.companyVersionService.addCompanyVersion(companyId, "0");
			return "SUCCESS";
		}
		else
		{
			return "ERROR";
		}
	}
	
	@RequestMapping(value="/checkCompanyCode.htm",method=RequestMethod.POST)
	@ResponseBody
	public String checkCompanyCode(String company_code)
	{
		return this.companyService.checkCompanyCode(company_code)>0?"YES":"NO";
	}
	
	/**
	 * 2013年1月24日 11:05:32 jinren
	 * @param fileCode
	 * @param suffix
	 * @param request
	 * @param response
	 */
	@RequestMapping("/images/{fileCode}.{suffix}")
	public void showImage(@PathVariable String fileCode, @PathVariable String suffix, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setHeader("Content-Type", "application/octet-stream");
			File file = null;
			//如果没有上传附件，默认处理方式，分图片和文件两种方式
			// 获取文件
			file = new File(path +File.separator+ fileCode +"."+ suffix);
			if(file.canRead()) {
				file = new File(path +File.separator+ fileCode+File.separator+"m_logo.png" );
				if(file.canRead()){
					FileUtils.copyFile(file, response.getOutputStream());
				}else{
					UnZip.unzipFile(path +File.separator+ fileCode +"."+ suffix, path +File.separator+ fileCode);
					file = new File(path +File.separator+ fileCode+File.separator+"m_logo.png" );
					if(file.canRead()){
						FileUtils.copyFile(file, response.getOutputStream());
					}else{
						file = new File(request.getSession().getServletContext().getRealPath("")+"/resources/images/imgb.png");
						FileUtils.copyFile(file, response.getOutputStream());
					}
				}
			} else {
				file = new File(request.getSession().getServletContext().getRealPath("")+"/resources/images/imgb.png");
				FileUtils.copyFile(file, response.getOutputStream());
				System.out.println(file);
			}
		} catch (IOException e) {
			_logger.error(e.getMessage());
		}
	}
	
	/**
	 * 跳转到修改企业页面
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value="/update.htm")
	public String update(String companyId,Model model){
		Company comp = companyService.update(companyId);
		List<CompanyPlug> companyPlugList = companyPlugService.findByNamedParam(new String[]{"companyId","plugId"}, new Object[]{companyId,"1"});
		if(companyPlugList!=null&&companyPlugList.size()!=0){
			//计步器开关的值
			String pedometer = "1";
			model.addAttribute("pedometer",pedometer);
		}
		User user = SecurityContextUtil.getCurrentUser();
		List<Role> role = user.getRoles();
		String flag = "0";
		for(Role r:role){
			String roleId = r.getRoleId();
			if("0".equals(roleId)||"1".equals(roleId)||"3".equals(roleId)){
				flag = "1";
				break;
			}
		}
		//flag：0 计步器开关隐藏；1：计步器开关显示
		model.addAttribute("pedometerFlag", flag);
		model.addAttribute("comp",comp);
		_logger.debug("跳转到修改企业页面");
		return "sysmanage/company/update";
	}
	
	/**
	 * 修改企业
	 * @param comp
	 * @param request
	 * @param logo
	 * @param pedometer 计步器开关：0：计步器打开；1：计步器关闭
	 * @return
	 */
	@RequestMapping(value="/updateCompany.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String updateCompany(Company comp,HttpServletRequest request,@RequestParam("logo") MultipartFile logo,String pedometer){
		String result = this.companyService.updateCompany(comp, request, logo,pedometer);
		if (result.equals("SUCCESS"))
		{
			if(!StringUtils.hasText(comp.getOtherNetFlag()))
			{
				comp.setOtherNetFlag("1");
			}
			this.companyVersionService.addCompanyVersion(comp.getCompanyId(), "1");
			return "SUCCESS";
		}else if(result.equals("ERROR PHOTO")){
			return "ERROR PHOTO";
		}
		else
		{
			return "ERROR";
		}
	}
	
	/**
	 * 删除企业
	 * @param companyIds
	 * @return
	 */
	@RequestMapping(value="/delete.htm")
	@Transactional
	@ResponseBody
	public String deleteCompany(String companyIds){
		String[] new_ids = companyIds.split(",");
		for(int i=0;i<new_ids.length;i++){
			this.companyService.deleteCompany(new_ids[i]);
			this.companyVersionService.addCompanyVersion(new_ids[i],"2");
			//add by zhangjun 2013/11/20 删除企业时底下部门，用户，关联表都要做逻辑删除
			this.departmentVersionService.saveVerBydelCompany("2", new_ids[i]);
			this.groupVersionService.addGroupVerByCompany(new_ids[i],"2");
			this.companyService.delGroupByCompany(new_ids[i]);
			//add by zhangjun 2013/11/20
		}
		_logger.debug("删除企业成功");
		return "SUCCESS";
	}

	/**
	 * 上传图片重复操作处理，前面上传的删除
	 * @param request
	 * @param photoAddr
	 */
	@RequestMapping("/deleteUpLoadedPhoto.htm")
	@Transactional
	@ResponseBody
	public void deleteUpLoadedPhoto(HttpServletRequest request,String photoAddr){
		String fileFullPath = request.getServletContext().getRealPath("")+photoAddr;
		File file = new File(fileFullPath);
		_logger.debug("删除图片");
		file.delete();
	}
	
	/**
	 * 系统用户图片上传
	 * @param request
	 * @param photoFile
	 * @return
	 */
	@RequestMapping("/uploadPhoto.htm")
	@ResponseBody
	public String uploadPhoto(HttpServletRequest request, @RequestParam("photoFile") MultipartFile photoFile){
		_logger.debug("上传图片");
		return this.companyService.uploadPhoto(request,photoFile);
	}
	
	/**
	 * 查找公司
	 * @param companyName
	 * @return
	 */
	@RequestMapping(value="/search.htm",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public List<Map<String, Object>> searchComp(String companyName){
		List<Map<String, Object>> list = this.companyService.searchComp(companyName);
		return list;
	}
}
