/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.PCEmployeeService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：GrayPublishController.java
 * <br />版本: 1.0.0
 * <br />日期：2013-7-30 上午8:36:36
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Controller
@RequestMapping("/pc/gray")
public class GrayPublishController extends BaseController {

	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private PCEmployeeService pcEmployeeService;
	
	/**
	 * 灰度发布页面.
	 * @return String
	 */
	@RequestMapping("/main.htm")
	public String grayIndex() {
		return "web/grayPublish";
	}
	
	/**
	 * 跳转至选择部门页面.
	 * @param model
	 * @param companyId
	 * @return String
	 */
	@RequestMapping("/chooseDept.htm")
	public String userChoseView(Model model, String companyId) {
		model.addAttribute("companyId", companyId);
		return "web/selectDept";
	}
	
	/**
	 * 根据部门id,查找下面所有人员(包括子部门).
	 * @param id 
	 * @return List<Map<String, Object>>
	 */
	@RequestMapping("/empBydepartmentId.htm")
	@ResponseBody
	public List<Map<String, Object>> empAllbyId(String id) {
		String childDepts = this.deptMagService.allChildrenDept(id);
		return this.deptMagService.empsByDepts(childDepts);
	}
	
	/**
	 * 根据公司ID查找所有人员.
	 * @param companyId
	 * @return
	 */
	@RequestMapping("/getEmpsByCompId.htm")
	@ResponseBody
	public Map<String, Object> getEmpsByCompId(String companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.pcEmployeeService.getEmpsList(companyId, null, true);
		if(list.isEmpty()) {
			map.put("empNum", "0");
		} else {
			String empNames = "";
			for(Map<String, Object> empMap : list) {
				empNames += empMap.get("employee_name").toString() + ",";
			}
			map.put("empNum", list.size());
			map.put("empNames", empNames.substring(0, empNames.length() - 1));
		}
		return map;
	}
	
	
	/**
	 * 灰度用户同步-同步选择公司下的所有用户或者同步指定公司下特定部门下的所有用户.
	 * @param companyId 公司ID
	 * @param empIds 特定部门下的所有用户
	 * @param syncAllFlag 同步所有标志(true-同步所有人;false-同步选择部门下的人员)
	 * @param eccode
	 * @param ecname
	 * @param prdornum
	 * @param servicecode
	 * @param servicename
	 * @param opttype
	 * @return map
	 */
	@RequestMapping("/syncEmps.htm")
	@ResponseBody
	public Map<String, Object> syncEmps(String companyId, String empIds, Boolean syncAllFlag, String eccode, String ecname,
			String prdornum, String servicecode, String servicename, String opttype) {
		//测试标记(0-非测试交易, 1-测试交易)
		int testFlag = 1;
		//EC企业代码: 要修改成员的业务的企业计费代码
		String ECCode = "AH_CMCC";
		if (StringUtils.hasText(eccode))
		{
			ECCode = eccode;
		}
		//EC企业名称: 该业务的企业名称
		String ECName = "安徽移动";
		if (StringUtils.hasText(ecname)) {
			ECName = ecname;
		}
		//集团产品代码: 订购关系的唯一标示
		String PrdOrdNum = "ICT0001";
		if (StringUtils.hasText(prdornum)) {
			PrdOrdNum = prdornum;
		}
		//服务标识: 服务的唯一标识
		String ServiseCode = "ICT0001";
		if (StringUtils.hasText(servicecode)) {
			ServiseCode = servicecode;
		}
		//服务名称
		String ServiseName = "集团通讯录";
		if (StringUtils.hasText(servicename)) {
			ServiseName = servicename;
		}
		//操作类型:01－加入名单 02－退出名单  03－定购  04－取消定购  05-业务信息变更(本次增加)
		String OptType = "03";
		if (StringUtils.hasText(opttype)) {
			OptType = opttype;
		}
		//登录鉴权类型:0-普通鉴权 1-手机号码+IMSI绑定鉴权
		String AuthType = "1";
		//属性名称
		String ItemName = "";
		//属性值
		String ItemValue = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.pcEmployeeService.syncEmps(companyId, empIds, syncAllFlag, testFlag, ECCode, ECName, PrdOrdNum, 
				ServiseCode, ServiseName, ItemName, ItemValue, OptType, AuthType);
		return map;
	}
}