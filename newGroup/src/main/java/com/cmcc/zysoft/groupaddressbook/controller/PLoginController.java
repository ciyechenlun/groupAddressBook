/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.databinding.types.soapencoding.Decimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.framework.utils.StringUtil;
import com.cmcc.zysoft.groupaddressbook.dto.UserCompanyDto;
import com.cmcc.zysoft.groupaddressbook.dto.UserRecordDto;
import com.cmcc.zysoft.groupaddressbook.dto.WalkRecordDto;
import com.cmcc.zysoft.groupaddressbook.mobile.service.IntegrationService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MDepartmentReportService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeRecordService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeReportService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MedalService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MedalSysService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MovementService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MovementUsersService;
import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.PCEmployeeService;
import com.cmcc.zysoft.groupaddressbook.util.ExportExcel;
import com.cmcc.zysoft.groupaddressbook.util.UnZip;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Medal;
import com.cmcc.zysoft.sellmanager.model.MedalSys;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.util.TimeConvert;
import com.cmcc.zysoft.sellmanager.util.UUIDUtil;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.Constant;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.DepartmentService;
import com.starit.common.dao.support.Pagination;

/**
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：MLoginController.java
 * <br />版本: 1.0.0
 * <br />日期：2013-6-14 下午4:00:18
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

@Controller
@RequestMapping("/pedmeter")
public class PLoginController extends BaseController {
	
	//~ fields------------------------------------------------------
	
	@Value("${upload.file.path}")
	private String path;
	
	@Value("${project.medal.path}")
	private String medalPath;
	
	/**
	 * 登录页面
	 */
	private static final String LOGIN_VIEW = "mLogin";
	
	/**
	 * 404页面
	 */
	private static final String NO_PAGE_VIEW = "404";
	
	/**
	 * 属性名称：companyService 类型：CompanyService
	 */
	@Resource
	private CompanyService companyService;
	
	@Resource
	private MEmployeeService mEmployeeService;
	
	/**
	 * 属性名称：mEmployeeReportService 类型：MEmployeeReportService
	 */
	@Resource
	private MEmployeeReportService mEmployeeReportService;
	
	@Resource
	private DepartmentService departmentService;
	
	@Resource
	private MEmployeeRecordService mEmployeeRecordService;
	
	@Resource
	private MovementService movementService;
	
	@Resource
	private MovementUsersService movementUsersService;
	
	@Resource
	private MedalService medalService;
	
	@Resource
	MedalSysService medalSysService;
	
	@Resource
	private PCEmployeeService pcEmployeeService;
	
	@Resource
	private IntegrationService integrationService;
	
	/**
	 * 日志.
	 */
	private static Logger _logger = LoggerFactory.getLogger(PLoginController.class);
	
	
	//~ methods------------------------------------------------------
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/mLogin.htm")
	public String loginViewFirst(HttpServletRequest request, String errorCode){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	@RequestMapping("/login.htm")
	public String login(HttpServletRequest request,String errorCode)
	{
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/")
	public String loginViewSecond(HttpServletRequest request,String errorCode){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping("/mLogin")
	public String loginViewThird(HttpServletRequest request,String errorCode){
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		return LOGIN_VIEW;
	}
	
	/**
	 * 跳转到不通公司的登录页面
	 * @return
	 */
	@RequestMapping("/mLogin/{loginUrl}")
	public String loginViewByCompany(HttpServletRequest request,@PathVariable("loginUrl") String loginUrl,String errorCode){
		//查看地址是否带有errorCode=401参数
		if(!StringUtil.isNullOrEmpty(errorCode) 
				&& !StringUtil.isNullOrEmpty(Constant.ERROR_MESSAGE_MAP.get(errorCode))){
			request.setAttribute(Constant.ERROR_MESSAGE, Constant.ERROR_MESSAGE_MAP.get(errorCode));
		}
		//如果登录的时候带了后缀，去匹配后缀对应的公司登录地址
		if(!StringUtil.isNullOrEmpty(loginUrl)){
			Company company = companyService.getCompanyByUrl(loginUrl);
			//如果通过登录地址找不到公司，跳转到404页面
			if(company==null){
				return NO_PAGE_VIEW;
			}else{
				//如果有公司和响应的登录地址匹配，将公司信息放到session
				request.getSession().setAttribute(Constant.SESSION_COMPANY, company);
				request.getSession().setAttribute(Constant.SESSION_COMPANY_LOGO, company.getIndexLogo());
				request.getSession().setAttribute(Constant.SESSION_LOGINURL, company.getLoginUrl());
				return LOGIN_VIEW;
			}
		}
		//如果没有后缀，跳到默认的登录页面，这个登录页面默认是属于移动公司的，只有移动公司的用户才能登录,
		//安徽移动公司的登录地址只能是"/"
		else{
			Company company = companyService.getCompanyByUrl("/");
			request.getSession().setAttribute(Constant.SESSION_COMPANY, company);
			request.getSession().setAttribute(Constant.SESSION_COMPANY_LOGO, company.getIndexLogo());
			request.getSession().setAttribute(Constant.SESSION_LOGINURL, company.getLoginUrl());
			return LOGIN_VIEW;
		}
	}
	
	/**
	 * 跳转到首页.
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pedometer/index.htm")
	public String index(ModelMap modelMap, HttpServletRequest request) {
		_logger.debug("#跳转到首页");
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.put("user", user);
		return "pedometer/index";
	}
	
	/**
	 * 框架右侧首页
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pedometer/content.htm")
	public String content(ModelMap modelMap) {
		_logger.debug("#跳转到首页");
		
		return "pedometer/content";
	}
	
	/**
	 * 获取gps轨迹信息
	 * @return
	 */
	@RequestMapping("/pedometer/getGPS.htm")
	@ResponseBody
	public Map<String,Object> getGps(String employee_record_id)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		//取一个轨迹示例
		if(!StringUtils.hasText(employee_record_id))
		{
			employee_record_id="f8510a2d40437c3b014043d2f00b001d";
		}
		List<Map<String,Object>> list = this.mEmployeeRecordService.getGpsByEmployeeRecordId(employee_record_id);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 跳转到健步记录页面
	 * @param modelMap
	 * @param employee_id：按员工进行检索（可以为空）
	 * @param department_id：按部门进行检索（可以为空）
	 * @param startDate：开始日期（可以为空）
	 * @param endDate：结束日期（可以为空）
	 * @param movement_id：活动编号（按活动查看历史记录）
	 * @return
	 */
	@RequestMapping("/pedometer/walkRecord.htm")
	public String walkRecord(ModelMap modelMap,Search search,String usercode,String user_name,String department_id,String startDate,String endDate,String movement_id) {
		_logger.debug("#跳转到健步记录页");
		User user = SecurityContextUtil.getCurrentUser();
		String company_id = user.getCompanyId();
		
		//活动列表
		List<Movement> list = this.movementService.getMovementListByLoginedUser();
		String movementId = StringUtils.hasText(movement_id)?movement_id:list.isEmpty()?"":list.get(0).getMovementId();
		
		Pagination<?> pagination = this.mEmployeeRecordService.getRecordList(10, search.getPageNo(), company_id, department_id, usercode, user_name, startDate, endDate, movementId);
		modelMap.addAttribute("pagination", pagination);
		modelMap.addAttribute("department_id",department_id);
		modelMap.addAttribute("usercode", usercode);
		modelMap.addAttribute("user_name", user_name);
		modelMap.addAttribute("startDate",startDate);
		modelMap.addAttribute("endDate",endDate);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("movementId", movementId);
		return "pedometer/walkRecord";
	}
	
	/**
	 * 导出健步记录
	 * @param response
	 * @param request
	 * @param usercode
	 * @param user_name
	 * @param department_id
	 * @param startDate
	 * @param endDate
	 * @param movement_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/pedometer/exportWalkRecord.htm")
	public Map<String,Object> exportWalkRecord(HttpServletResponse response,HttpServletRequest request,
			String usercode,String user_name,String department_id,String startDate,String endDate,String movement_id) throws IOException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		User user = SecurityContextUtil.getCurrentUser();
		String company_id = user.getCompanyId();
		String[] headers = {"姓名","所属部门","手机号","体重/kg","身高/cm","上传日期","运动日期","起始时间","结束时间","耗时","步数","里程/米","速度km/h","耗能/千卡","GPS状态","GPS里程","计步方式切换次数","步长推算/cm","步长误差/cm"};
		String title = "walkRecordExport";
		String pattern = "yyyy-MM-dd hh:mm:ss";
		try{
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流  
			//// 设定输出文件头  
			response.setHeader("Content-disposition", "attachment; filename=" + title + ".xls");
			response.setContentType("application/ms-excel;charset=UTF-8");// 定义输出类型
			List<WalkRecordDto> list = this.mEmployeeRecordService.getRecordExportSource(company_id, department_id, usercode, user_name, startDate, endDate, movement_id);
			ExportExcel<WalkRecordDto> exportExcel = new ExportExcel<WalkRecordDto>();
			exportExcel.exportExcel("健步记录", headers, list, out, pattern);
			result.put("msg", "报表导出成功");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			result.put("msg", "报表导出失败，请重试");
		}
		return result;
	}
	
	/**
	 * 显示用户轨迹地图界面
	 * @param modelMap
	 * @param employee_record_id：记录编号
	 * @param sport_date：运动日期
	 * @param sport_start_time：运动开始时间
	 * @param sport_end_time：运动结束时间
	 * @param sport_elapse_time：运动耗时
	 * @param sport_step：步数
	 * @param sport_distence：里程
	 * @param sport_speed：速度
	 * @param employee_name：员工姓名
	 * @return
	 */
	@RequestMapping(value="/pedometer/showMap")
	public String showMap(ModelMap modelMap, String employee_record_id, String sport_date, 
			String sport_start_time,String sport_end_time,String sport_elapse_time, String sport_step,
			String sport_distence, String sport_speed, String employee_name)
	{
		modelMap.addAttribute("sport_date", sport_date);
		modelMap.addAttribute("sport_start_time", sport_start_time);
		modelMap.addAttribute("sport_end_time", sport_end_time);
		modelMap.addAttribute("sport_elapse_time", sport_elapse_time);
		modelMap.addAttribute("sport_step", sport_step);
		modelMap.addAttribute("sport_distence", sport_distence);
		modelMap.addAttribute("sport_speed", sport_speed);
		modelMap.addAttribute("employee_name", employee_name);
		modelMap.addAttribute("employee_record_id", employee_record_id);
		return "pedometer/showMap";
	}
	
	/**
	 * 跳转到用户记录页.
	 * @param modelMap
	 * @param departmentId
	 * @param movement_id：活动编号
	 * @return
	 */
	@RequestMapping("/pedometer/userRecord.htm")
	public String userRecord(ModelMap modelMap,Search search,String departmentId,String movement_id) {
		_logger.debug("#跳转到用户记录页");
		User user = SecurityContextUtil.getCurrentUser();
		String company_id = user.getCompanyId();
		String department_id = "0";
		if("4".equals(user.getRoles().get(0).getRoleId()))
		{
			department_id = this.pcEmployeeService.getUserParentDepartment(user.getEmployeeId());
		}
		modelMap.put("ID", department_id);
		//活动列表
		List<Movement> list = this.movementService.getMovementListByLoginedUser();
		String movementId = StringUtils.hasText(movement_id)?movement_id:list.isEmpty()?"":list.get(0).getMovementId();
		
		
		modelMap.put("company_id", company_id);
		modelMap.put("department_id",departmentId);
		modelMap.put("movementId", movementId);
		modelMap.put("list", list);
		
		Pagination<?> pagination = this.mEmployeeReportService.getUserListByDepartment(4,search.getPageNo(),departmentId, company_id, movementId);
		modelMap.addAttribute("pagination", pagination);
		return "pedometer/userRecord";
	}
	
	
	@RequestMapping("/pedometer/exportUserRecord.htm")
	@ResponseBody
	public Map<String,Object> exportUserRecord(HttpServletResponse response,HttpServletRequest request,String movement_id)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		User user = SecurityContextUtil.getCurrentUser();
		/*
		 *private String employee_name;
			private String all_sport_times;
			private String all_sport_elpse_time;
			private String avg_sport_speed;
			private String all_sport_steps;
			private String avg_sport_steps;
			private String current_rank;
		 */
		String[] headers = {"姓名","总次数","总用时","平均速度","总步数","平均步数","排名"};
		String title = "userRecordExport";
		String pattern = "yyyy-MM-dd hh:mm:ss";
		try{
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流  
			//// 设定输出文件头  
			response.setHeader("Content-disposition", "attachment; filename=" + title + ".xls");
			response.setContentType("application/ms-excel;charset=UTF-8");// 定义输出类型
			List<UserRecordDto> list = this.mEmployeeReportService.getUserRecordExportSource(movement_id);
			ExportExcel<UserRecordDto> exportExcel = new ExportExcel<UserRecordDto>();
			exportExcel.exportExcel("健步记录", headers, list, out, pattern);
			result.put("msg", "报表导出成功");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			result.put("msg", "报表导出失败，请重试");
		}
		return result;
	}
	
	/**
	 * 跳转到用户分析页.
	 * @param modelMap
	 * @param departmentId
	 * @param type：按照某个维度进行的统计
	 * @param movement_id: 活动编号
	 * @return
	 */
	@RequestMapping("/pedometer/userAnalyze.htm")
	public String userAnalyze(ModelMap modelMap,String departmentId,String type, String movement_id) {
		_logger.debug("#跳转到用户分析页");
		//如果部门id为空，则提取当前用户的id
		User user = SecurityContextUtil.getCurrentUser();
		modelMap.put("companyId", user.getCompanyId());
		//如果是企业管理员
		String department_id = "0";
		if("4".equals(user.getRoles().get(0).getRoleId()))
		{
			department_id = this.pcEmployeeService.getUserParentDepartment(user.getEmployeeId());
		}
		
		if(StringUtil.nvl(departmentId).equals(""))
		{
			if(StringUtils.hasText(user.getEmployeeId())){
				//Employee emp = this.mEmployeeService.getEntity(user.getEmployeeId());
				//departmentId = emp.getDepartmentId();
			}
			else
			{
				departmentId = "";
			}
		}
		
		//活动列表
		List<Movement> mList = this.movementService.getMovementListByLoginedUser();
		String movementId = StringUtils.hasText(movement_id)?movement_id:mList.isEmpty()?"":mList.get(0).getMovementId();

		com.cmcc.zysoft.sellmanager.model.Department department = null;
		if(StringUtils.hasText(departmentId)){
			department = this.departmentService.getEntity(departmentId);
		}
		List<Map<String,Object>> list = this.mEmployeeReportService.getReportByDepartmentId(departmentId,"","","all_sport_times",movementId);
		String json = getReportDataSource(list,"employee_name","all_sport_times","总次数");
		modelMap.put("data", json);
		modelMap.put("department", department);
		modelMap.put("list", mList);
		modelMap.put("movementId", movementId);
		//部门管理员专用
		modelMap.put("department_id",department_id);
		return "pedometer/userAnalyze";
	}
	
	/**
	 * ajax方式获取用户报表信息
	 * @param departmentId：部门ID
	 * @param type：统计字段
	 * @param tips：提示文字（显示在柱状图）
	 * @param startDate：开始日期
	 * @param endDate：结束日期
	 * @param movement_id:活动编号
	 * @return
	 */
	@RequestMapping(value="/pedometer/getUserReport.htm")
	@ResponseBody
	public Map<String,Object> getUserReportByAjax(String departmentId,String type,String tips,String startDate,String endDate,String movement_id)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		/*if(!StringUtils.hasText(departmentId))
		{
			map.put("code", "81");
		}else{*/
			List<Map<String,Object>> list = this.mEmployeeReportService.getReportByDepartmentId(departmentId,startDate,endDate,type,movement_id);
			String json = getReportDataSource(list, "employee_name",type, tips);
			map.put("code", "0");
			map.put("json", json);
		//}
		return map;
	}
	
	/**
	 * 获取echart报表的数据格式(json string)
	 * @param list：数据源
	 * @param category：分类轴字段名称
	 * @param value：值字段名称
	 * @param valueTips：提示语(值字段的汉字含义)
	 * @return json String
	 */
	private String getReportDataSource(List<Map<String,Object>> list,String category,String value,String valueTips)
	{
		String json = "{tooltip : {show: true,trigger: 'item'},";
		json += "toolbox: {show : true,"+
    	        "feature : {mark : true,dataView : {readOnly: false},"+
    	            "magicType:['line', 'bar'],"+
    	            "refresh : true}},";
		json += "xAxis:[{type:'category',data:[";
		//提取报表展示
		int i = 0;
		String yAxis = "yAxis:[{type:'value',splitArea : {show : true}}],";
		String yAxisValue = "series:[{name:'" + valueTips + "',type:'bar',data:[";
		for(Map<String,Object> map : list)
		{
			json += "'" + map.get(category).toString() + "'";
			if(value.equals("all_sport_elpse_time") || value.equals("avg_sport_time"))
			{
				long time = Long.parseLong(map.get(value).toString());
				yAxisValue += TimeConvert.LongToMinutes(time);
			}
			else{
				yAxisValue += map.get(value).toString();
			}
			if (i<list.size()-1){
				json += ",";
				yAxisValue += ",";
			}
			i++;
		}
		json += "]}],";
		yAxisValue += "]}]";
		json += yAxis;
		json += yAxisValue;
		json += "}";
		System.out.print("return json is ===============>" + json);
		return json;
	}
	
	/**
	 * 跳转到部门分析页.
	 * @param modelMap
	 * @param department_id: 部门编号，为空则为当前公司所有部门
	 * @return
	 */
	@RequestMapping("/pedometer/deptAnalyze.htm")
	public String deptAnalyze(ModelMap modelMap,String department_id) {
		_logger.debug("#跳转到部门分析页");
		//默认显示当前公司所有一级部门
		//User user = SecurityContextUtil.getCurrentUser();
		//List<Map<String,Object>> list = this.mDepartmentReportService.getDeptReport(user.getCompanyId(), department_id);
		return "pedometer/deptAnalyze";
	}
	
	/**
	 * 跳转到信息导入页.
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pedometer/infoImport.htm")
	public String infoImport(ModelMap modelMap) {
		_logger.debug("#跳转到信息导入页");
		return "pedometer/import";
	}
	
	/**
	 * 跳转到权限设置页.
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pedometer/authConfig.htm")
	public String authConfig(ModelMap modelMap) {
		_logger.debug("#跳转到权限设置页");
		return "pedometer/authConfig";
	}
	
	/**
	 * 跳转至用户报表页
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pedometer/useReport.htm")
	public String userReport (ModelMap modelMap)
	{
		_logger.debug("#跳转到用户报表页");
		return "pedometer/userReport";
	}
	
	/**
	 * 用户详情界面
	 * @param modelMap
	 * @param employee_id
	 * @return
	 */
	@RequestMapping("/pedometer/userDetail.htm")
	public String userDetail(ModelMap modelMap,String employee_id)
	{
		_logger.debug("#跳转到用户详情页");
		Map<String,Object> map = this.mEmployeeReportService.getUserDetail(employee_id);
		modelMap.addAttribute("user", map);
		modelMap.addAttribute("employee_id", employee_id);
		return "pedometer/userDetail";
	}
	
	/**
	 * 跳转到活动列表
	 * @return
	 */
	@RequestMapping("/pedometer/movement.htm")
	public String movment(ModelMap modelMap,Search search)
	{
		_logger.debug("#跳转到活动列表页面");
		Pagination<?> pagination = this.movementService.getMovementPage(10, search.getPageNo());
		modelMap.addAttribute("pagination", pagination);
		return "pedometer/movement";
	}
	
	/**
	 * 禁用OR启用活动
	 * @param movement_id
	 * @return
	 */
	@RequestMapping("/pedometer/movementConfig.htm")
	@ResponseBody
	public String movementConfig(String movement_id)
	{
		if(!StringUtils.hasText(movement_id))
		{
			return "ERROR";
		}
		else{
			this.movementService.movementConfig(movement_id);
			return "SUCCESS";
		}
	}
	
	/**
	 * 跳转至活动用户设置界面
	 * @param movement_id
	 * @param keyword 搜索关键词，可以为空
	 * @return
	 */
	@RequestMapping("/pedometer/movementUsers.htm")
	public String movementUsers(ModelMap modelMap, String movement_id,Search search,String keyword)
	{
		_logger.debug("#跳转至活动用户界面");
		String company_id = SecurityContextUtil.getCompanyId();
		modelMap.addAttribute("company_id", company_id);
		Movement movement = this.movementService.getEntity(movement_id);
		modelMap.addAttribute("movement", movement);
		modelMap.addAttribute("keyword", keyword);
		//分页获取用户列表
		Pagination<?> pagination = this.movementUsersService.getUserListByMovementId(movement_id, 10, search.getPageNo(), keyword);
		modelMap.addAttribute("pagination", pagination);
		return "pedometer/movementUsers";
	}
	
	/**
	 * 选择用户对话框
	 * @param company_id
	 * @return
	 */
	@RequestMapping("/pedometer/userChoseDraftsView.htm")
	public String userChoseDraftsView(ModelMap modelMap, String company_id, String movement_id)
	{
		User user = SecurityContextUtil.getCurrentUser();
		if("4".equals(user.getRoles().get(0).getRoleId()))
		{
			String department_id = this.pcEmployeeService.getUserParentDepartment(user.getEmployeeId());
			modelMap.addAttribute("department_id",department_id);
		}
		else
		{
			modelMap.addAttribute("department_id","0");
		}
		modelMap.addAttribute("movement_id", movement_id);
		modelMap.addAttribute("company_id", company_id);
		return "pedometer/userChoseDraftsView";
	}
	
	/**
	 * 重新设置活动下的用户
	 * @param user  用户列表，用逗号分隔
	 * @param movement_id 活动编号
	 * @return
	 */
	@RequestMapping("/pedometer/addUsers.htm")
	@ResponseBody
	public String addUsers(String user,String movement_id)
	{
		//先将活动下用户删除，再添加
		this.movementUsersService.addUsers(user, movement_id);
		return "SUCCESS";
	}
	
	/**
	 * 取得活动下用户列表（不分页）
	 * @param movement_id
	 * @return
	 */
	@RequestMapping("/pedometer/getDefaultUsers.htm")
	@ResponseBody
	public List<Map<String,Object>> getDefaultUsers(String movement_id)
	{
		return this.movementUsersService.getUserListByMovementId(movement_id);
	}
	
	/**
	 * 活动管理页面
	 * @param type
	 * @param movement_id
	 * @return
	 */
	@RequestMapping("/pedometer/movementEdit.htm")
	public String movementEdit(ModelMap modelMap, String type, String movement_id)
	{
		_logger.debug("#跳转至活动管理界面");
		String typeName = type.equals("add")?"添加":"编辑";
		modelMap.addAttribute("typeName", typeName);
		modelMap.addAttribute("type", type);
		//是否为企业管理员
		User user = SecurityContextUtil.getCurrentUser();
		String eadmin = "4".equals(user.getRoles().get(0).getRoleId())?"1":"0";
		modelMap.addAttribute("eadmin",eadmin);
		if("1".equals(eadmin))
		{
			String department_id = this.pcEmployeeService.getUserParentDepartment(user.getEmployeeId());
			Department dept = this.departmentService.getEntity(department_id);
			modelMap.put("department_id", department_id);
			modelMap.put("deptName", dept.getDepartmentName());
		}
		if(StringUtils.hasText(movement_id))
		{
			Movement movement = this.movementService.getEntity(movement_id);
			modelMap.addAttribute("movement", movement);
		}
		return "pedometer/movementEdit";
	}
	
	/**
	 * 勋章列表界面
	 * @param modelMap
	 * @param search
	 * @return
	 */
	@RequestMapping("/pedometer/medal.htm")
	public String medalList(ModelMap modelMap,Search search)
	{
		_logger.debug("#跳转到勋章系统");
		Pagination<?> pagination = this.medalService.getPage(15, search.getPageNo());
		modelMap.addAttribute("pagination", pagination);
		return "pedometer/medal";
	}
	
	/**
	 * 添加Or编辑勋章界面
	 * @param modelMap
	 * @param type
	 * @param medal_sys_id
	 * @return
	 */
	@RequestMapping("/pedometer/medalEdit.htm")
	public String medalEdit(ModelMap modelMap,String type,String medal_sys_id)
	{
		return "pedometer/medalEdit";
	}
	
	@RequestMapping("/pedometer/medalList.htm")
	public String medalList(ModelMap modelMap,String medal_sys_id)
	{
		List<Map<String,Object>> list = this.medalService.getMedalListBySysId(medal_sys_id);
		modelMap.addAttribute("list",list);
		MedalSys ms = this.medalSysService.getEntity(medal_sys_id);
		modelMap.addAttribute("ms",ms);
		
		return "pedometer/medalList";
	}
	
	/**
	 * 勋章系统保存
	 * @param medalSys
	 * @param picture
	 * @return
	 */
	@RequestMapping("/pedometer/medalSysEditSubmit.htm")
	@ResponseBody
	public Map<String,Object> medalSysEditSubmit(MedalSys medalSys,@RequestParam("medalPicture") MultipartFile picture)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String medalPictureName = "";
		if(!StringUtils.hasText(medalSys.getMedalSysName()))
		{
			map.put("code", "-1");
			map.put("msg","勋章名称不能为空");
		}
		else if(picture == null)
		{
			map.put("code", "-1");
			map.put("msg", "勋章图片不能为空");
		}
		else{
			//先生成主键
			String uuid = UUIDUtil.generateUUID();
			User user = SecurityContextUtil.getCurrentUser();
			medalSys.setAddMan(user.getUserId());
			medalSys.setAddDate(new Date());
			medalSys.setMedalSysId(uuid);
			String medalPath2 = medalPath + "\\" +  uuid;
			//上传图片包
			if(picture.getSize()!=0)
			{
				makeDir(new File(medalPath2));
				String photoName = picture.getOriginalFilename();
				String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
				try {
		        	if((".zip").equals(extName.trim())){
		    	        String lastFileName = System.currentTimeMillis()+extName;
		    	        medalPictureName = File.separator+"medal"+lastFileName;
		    	        String fileFullPath = path+medalPictureName;
		    			FileCopyUtils.copy(picture.getBytes(),new File(fileFullPath));
		    			//压缩包名称先放到这个字段，防止图片不存在时可以通过这个字段再次解压
		    			medalSys.setMark1("medal"+lastFileName);
		    			//开始解压
		    			String files = UnZip.unzipFile(fileFullPath, medalPath2);
		    			//压缩包也拷贝到指定的目录
		    			FileCopyUtils.copy(picture.getBytes(),new File(medalPath2+medalPictureName));
		    			//加入勋章系统
		    			this.medalSysService.insertEntity(medalSys);
		    			//加入勋章
		    			String[] medalFiles = files.split("[|]");
		    			for(String file:medalFiles)
		    			{
		    				if(StringUtils.hasText(file))
		    				{
		    					Medal medal = new Medal();
		    					medal.setMedalLevel(BigDecimal.ZERO);
		    					medal.setMedalName(file.substring(0,file.length()-extName.length()));
		    					medal.setMedalSysId(uuid);
		    					medal.setMedalPicture(file);
		    					medal.setMedalType("0");
		    					this.medalService.insertEntity(medal);
		    				}
		    			}
		    			map.put("code", "0");
		    			map.put("msg", uuid);
		            }else{
		    			map.put("code", "-1");
		            	map.put("msg", "错误的压缩包类型");
		            }
				} catch (Exception e) {
					map.put("code", "-1");
					map.put("msg", "解压错误，" + e.getMessage());
				}
			}
			else
			{
				map.put("code", "-1");
				map.put("msg", "勋章图片大小为0");
			}
		}
		return map;
	}
	
	/**
	 * 勋章详细编辑保存入库
	 * @param medalId
	 * @param medalName
	 * @param medalLevel
	 * @return
	 */
	@RequestMapping("/pedometer/medalEditSubmit.htm")
	@ResponseBody
	public Map<String,Object> medalEditSubmit(String medalId,String medalName,String medalLevel)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String[] medalIds = medalId.split("[,]");
		String[] medalNames = medalName.split("[,]");
		String[] medalLevels = medalLevel.split("[,]");
		int i = 0;
		for(String id : medalIds)
		{
			if(StringUtils.hasText("id"))
			{
				Medal medal = this.medalService.getEntity(id);
				if(medal!=null)
				{
					medal.setMedalName(medalNames[i]);
					double level = Double.parseDouble(medalLevels[i]);
					medal.setMedalLevel(BigDecimal.valueOf(level));
					this.medalService.MegeMedal(medal);
				}
			}
			i++;
		}
		map.put("code", "0");
		return map;
	}
	
	/**
	 * 创建目录，暂时先放在这里
	 * @param dir
	 */
	 private void makeDir(File dir) {  
        if(! dir.getParentFile().exists()) {  
            makeDir(dir.getParentFile());  
        }  
        dir.mkdir();  
    }  
	
	/**
	 * 获取勋章系统列表
	 * @return
	 */
	@RequestMapping("/pedometer/getMedalSystem.htm")
	@ResponseBody
	public List<Map<String,Object>> getgetMedalSystem()
	{
		return this.movementService.getMedalSystem();
	}
	
	/**
	 * 返回所属赛季
	 * @return
	 */
	@RequestMapping("/pedometer/getParentMovement.htm")
	@ResponseBody
	public List<Map<String,Object>> getParentMovement()
	{
		return this.movementService.getParentMovement();
	}
	
	/**
	 * 添加或编辑活动
	 * @param movement
	 * @param type 'add' 新增，'edit' 修改
	 * @return
	 */
	@RequestMapping("/pedometer/movementEditSubmit.htm")
	@ResponseBody
	public String movementEditSubmit(Movement movement, String eadmin, String type,@RequestParam("pictureZip") MultipartFile movementPicture)
	{
		if(!StringUtils.hasText(movement.getMovementName()) || !StringUtils.hasText(movement.getMovementEndDate().toString())
				|| !StringUtils.hasText(movement.getMovementStartDate().toString()))
		{
			return "EMPTY";
		}
		else
		{
			//校验
			if(movement.getMovementStartDate().equals(movement.getMovementEndDate()))
			{
				return "SAME DATE";
			}
			else if(movement.getMovementEndDate().before(movement.getMovementStartDate()))
			{
				return "MORE";
			}
			else if(!StringUtils.hasText(movement.getMedalSysId()))
			{
				return "MEDAL";
			}
			else if(!StringUtils.hasText(movement.getOrderType()))
			{
				return "ORDER";
			}
			else{
				//先判断图片是否为空
				if(movementPicture.getSize()>0)
				{
					String photoName = movementPicture.getOriginalFilename();
					String extName = photoName.substring(photoName.lastIndexOf(".")).toLowerCase();
					try {
			        	if((".zip").equals(extName.trim())){
			    	        String lastFileName = System.currentTimeMillis()+extName;
			    	        String movementPicturePath = File.separator+"movement"+lastFileName;
			    	        String fileFullPath = path+movementPicturePath;
			    	        FileCopyUtils.copy(movementPicture.getBytes(),new File(fileFullPath));
			    	        FileCopyUtils.copy(movementPicture.getBytes(), new File(medalPath + "\\" + movementPicturePath));
			    	        movement.setMovementPicture("movement" + lastFileName);
			        	}
					}
					catch(Exception e)
					{}
			        	
				}
				if(type.equals("add"))
				{
					//添加
					movement.setMovementStatus("1");
					this.movementService.insertEntity(movement);
				}
				else
				{
					//修改
					if(!StringUtils.hasText(movement.getMovementStatus()))
					{
						movement.setMovementStatus("1");
					}
					this.movementService.updateEntity(movement);
				}
			}
		}
		return "SUCCESS";
	}
	
	
	
	/**
	 * 删除活动下用户(不删除健步记录)
	 * @param movement_id
	 * @param employee_id
	 * @return
	 */
	@RequestMapping("/pedometer/movementUserDelete.htm")
	@ResponseBody
	public String movementUserDelete(String movement_id, String employee_id)
	{
		this.movementUsersService.deleteMovementUserByMovementIdAndEmployeeId(movement_id, employee_id);
		return "SUCCESS";
	}	
	
	/**
	 * 为自动补全提供数组源
	 * @return
	 */
	@RequestMapping("/pedometer/getEmployee.htm")
	@ResponseBody
	public List<Map<String,Object>> getEmployee(String q)
	{
		User user = SecurityContextUtil.getCurrentUser();
		List<Map<String,Object>> list = this.mEmployeeService.searchUserByNameOrPY(q, null, user.getCompanyId());
		return list;
	}
	
	/**
	 * 积分查询
	 * @return
	 */
	@RequestMapping("/pedometer/integration.htm")
	public String integration(ModelMap modelMap,Search search)
	{
		_logger.debug("#跳转到活动列表页面");
		Pagination<?> pagination = this.integrationService.getIntegration(10,search.getPageNo());
		modelMap.addAttribute("pagination", pagination);
		return "pedometer/integration";
	}
	
	/**
	 * 更新积分
	 * @return
	 */
	@RequestMapping("/pedometer/updateIntegration")
	@ResponseBody
	public String updateIntegration()
	{
		return this.integrationService.updateIntegration();
	}
}