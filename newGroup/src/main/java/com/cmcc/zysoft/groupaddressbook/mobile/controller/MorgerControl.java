/**
 * @author 徐刚强
 */

package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.mobile.service.MorgerService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

@Controller
@RequestMapping("mobile/orger")
public class MorgerControl extends BaseController{
	@Resource
	private MorgerService morgerService; 
	
	/**
	 * 下载所有部门
	 * @param cmd
	 * @param userCode
	 * @param deptCode
	 * @param deptList
	 * @param token
	 * @param company_id：可为空。如果为空则取当前用户所在的所有公司。指定公司 id则下载指定公司的部门
	 * @return
	 */
	@RequestMapping(value = "/downall")
	@ResponseBody
	public Map<String,Object> getAllorgerList(String cmd, String userCode, int deptCode, String deptList, String token,String company_id){
		return this.morgerService.getAllorgerList(cmd, userCode, deptCode, deptList, token, company_id);		
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	/**
	 * 部门更新接口
	 * @param cmd 操作符，无具体含义
	 * @param userCode 用户编号（登录表的userId)
	 * @param deptCode 部门的版本号，服务器端根据此版本号返回更新的部门
	 * @param deptList 废除字段
	 * @param token 
	 * @return
	 */
	public Map<String,Object> updateorgerList(String cmd, String userCode, int deptCode, String deptList, String token,String filter_company){
		return this.morgerService.updateorgerList(cmd, userCode, deptCode, deptList, token, filter_company);		
	}

}
