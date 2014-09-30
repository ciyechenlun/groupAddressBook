// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.LookGroupService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MessageController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-5 下午3:11:52
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("pc/message")
public class MessageController extends BaseController{
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private LookGroupService lookGroupService;
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@RequestMapping(value="/main.htm")
	public String main(){
		return "web/message";
	}
	
	
	/**
	 * 部门树,异步加载.
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/msgTree.htm")
	@ResponseBody
	public List<Map<String,Object>> treeTest(String id){
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		String companyId = SecurityContextUtil.getCompanyId();
		if(StringUtils.hasText(id)){
			fullList = this.deptMagService.deptTree(id,companyId,"");
		}else{
			fullList = this.deptMagService.deptTree("0",companyId,"");
		}
		return fullList;
	}
	/**
	 * 根据分公司，加载对应的部门树
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/msgTreeByCompanyId.htm")
	@ResponseBody
	public List<Map<String,Object>> treeByCompanyId(String companyId,String id){
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		if(StringUtils.hasText(id)){
			fullList = this.deptMagService.deptTreeByCompanyId(companyId,id);
		}else{
			fullList = this.deptMagService.deptTreeByCompanyId(companyId,"0");
		}
		return fullList;
	}
	
	/**
	 * 根据分公司，加载对应的部门树
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/msgTreeByCompanyIdAndDeptID.htm")
	@ResponseBody
	public List<Map<String,Object>> treeByCompanyIdAndDeptId(String companyId,String id,String department_id){
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		if(StringUtils.hasText(id)){
			fullList = this.deptMagService.deptTreeByCompanyId(companyId,id);
		}else{
			fullList = this.deptMagService.deptTreeByCompanyId(companyId,department_id);
		}
		return fullList;
	}
	
	/**
	 * 根据部门id,查找下面所有人员(包括子部门)
	 * @param id 
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	@RequestMapping("/empBydepartmentId.htm")
	@ResponseBody
	public List<Map<String,Object>> empAllbyId(String id){
		String childDepts=this.deptMagService.allChildrenDept(id);
		List<Map<String, Object>> fullList = new ArrayList<Map<String, Object>>();
		fullList=this.deptMagService.empListByIds(childDepts);
		return fullList;
	}
	/**
	 * 消息类型选择
	 * @param id 
	 * @return 
	 * 返回类型：
	 */
	@RequestMapping("/massageType.htm")
	@ResponseBody
	public List<Map<String,Object>> getMassageType(String id){
		return this.pcCompanyService.getMassageType();
	}
	/**
	 * 公司选择
	 * @param id 
	 * @return 
	 * 返回类型：
	 */
	@RequestMapping("/company.htm")
	@ResponseBody
	public List<Map<String,Object>> company(String id){
		return this.pcCompanyService.getAllCompany();
	}
	
	/**
	 * 根据用户权限返回公司列表
	 * @return
	 */
	@RequestMapping("/getAllCompanyByUser.htm")
	@ResponseBody
	public List<Map<String,Object>> getAllCompanyByUser()
	{
		return this.pcCompanyService.getAllCompanyByUser();
	}
	
	@RequestMapping("/msgEmployee.htm")
	@ResponseBody
	public List<Map<String,Object>> msgEmployee(String id){
		return this.lookGroupService.getEmployeeByDepartmentId(id);
	}
	/**
	 * 跳转至添加对象页面
	 * 
	 * @param noticeState
	 * @return
	 */
	@RequestMapping(value = "/userChoseView")
	public String userChoseView(Model model,String companyId) {
		model.addAttribute("companyId", companyId);
		return "web/userChoseDraftsView";
	}
	/**
	 * 推送消息
	 * 
	 * @param noticeState
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/sendMessage.htm")
	@ResponseBody
	public String sendMessage(String companyId,String empIdD,String msg_text,Boolean checkboxName,String msg_type) throws UnsupportedEncodingException {
		try {
			List<Map<String, Object>> imeiList = this.lookGroupService.getImei(empIdD, checkboxName,companyId);
			String text = "";
			for(Map<String, Object> map : imeiList){
				String url = "http://120.209.131.146/webcloud/client/push/push2Device.html?appkey=appkey-207-31&deviceId=";
				String isLeaf = map.get("imei").toString();
				if("".equals(isLeaf)){
					String employeeName = map.get("employee_name").toString();
					text += employeeName +" imei号为空,推送消息失败</br>";
					continue;
				}
			    url+=isLeaf;
				if(msg_type.equals("085")){
					url+="&message=appkey-207-31_"+isLeaf+"@versionupgrade";
				}else if(msg_type.equals("086")){
					url+="&message=appkey-207-31_"+isLeaf+"_"+map.get("mobile").toString()+"@versionupgrade";
				}else{
					url+="&message="+URLEncoder.encode(msg_text,"utf-8");
				}
				HttpClientUtil.get(url);
			}
			if(StringUtils.hasText(text)){
				return text;
			}else{
				return "SUCCESS";
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return "FALSE";
		}
	}
	
	/**
	 * 短信推广页面.
	 * @return String
	 */
	@RequestMapping("/sms.htm")
	public String sms(ModelMap modelMap,HttpServletRequest request) {
		String companyId = SecurityContextUtil.getCompanyId();
		Company company = this.pcCompanyService.getEntity(companyId);
		if(null != request.getSession().getAttribute("selCompany")) {
			company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		modelMap.addAttribute("companyId", companyId);
		modelMap.addAttribute("companyName", company.getCompanyName());
		return "web/sms";
	}
	
	/**
	 * 短信推广.
	 * @param companyId 公司ID
	 * @param content 消息内容
	 * @return Map<String, Object>
	 * @throws ParseException 
	 */
	@RequestMapping("/sendMsg.htm")
	@ResponseBody
	public Map<String, Object> sendMsg(String companyId, String content,String sendObj) throws ParseException {
		return this.userCompanyService.sendMsg(companyId, content,sendObj);
	}
}