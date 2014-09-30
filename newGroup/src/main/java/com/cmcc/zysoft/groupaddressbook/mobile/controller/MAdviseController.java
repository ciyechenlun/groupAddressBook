// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.mobile.service.MAdviseService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MInterfaceLogService;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MAdviseController
 * <br />版本:1.0.0
 * <br />日期： 2013-4-9 下午4:28:35
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("mobile/advise")
public class MAdviseController extends BaseController{
	
	@Resource
	private MAdviseService mAdviseService;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private MEmployeeService employeeService;
	
	@Resource
	private MInterfaceLogService mInterfaceLogService;
	
	/**
	 * 提交建议.
	 * @param employeeId 
	 * @param content 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping("/add")
	@Transactional
	@ResponseBody
	public Map<String,Object> addAdvise(String employeeId, String content){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Employee employee = this.employeeService.getEntity(employeeId);
			Department department = this.deptMagService.getEntity(employee.getDepartmentId());
			String companyId = department.getCompany().getCompanyId();
			this.mInterfaceLogService.addLog(employeeId, companyId, "反馈建议");
			return this.mAdviseService.addAdvise(employeeId, content, department.getCompany().getCompanyId());
		} catch(Exception e){
			map.put("success", "false");
			return map;
		}
	}

}
