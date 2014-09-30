package com.cmcc.zysoft.sysmanage.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sysmanage.service.HeadshipService;
import com.starit.common.dao.support.Pagination;

/**
 * HeadshipController.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-2 下午6:04:03
 *
 */
@Controller
@RequestMapping(value="/pc/headship")
public class HeadshipController extends BaseController {
	
	private static Logger _logger = LoggerFactory.getLogger(HeadshipController.class);
	
	@Resource
	private HeadshipService headshipService;
	
	/**
	 * 跳转至岗位页面
	 * 
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String headship(){
		_logger.debug("跳转至岗位页面");
		return "sysmanage/headship/headship";
	}
	
	/**
	 * 获取所有岗位信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all.htm", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<Object> getAllHeadships(int page,int rows,String idIcon) {
		_logger.debug("获取所有岗位信息");
		String isAdmin = "isAdmin";
		return this.headshipService.getAllHeadships(page,rows,idIcon,isAdmin);
	}
	
	/**
	 * 根据查询条件获取岗位信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/allByCondition.htm", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<Object> getHeadshipsByCondition(@RequestParam("headshipName") String headshipName,int page,int rows) {
		_logger.debug("根据查询条件获取岗位信息");
		return this.headshipService.getHeadshipsByCondition(headshipName,page,rows);
	}
	
	/**
	 * 跳转至岗位添加页面 
	 * @return
	 */
	@RequestMapping(value = "/add.htm")
	public String add() {
		_logger.debug("跳转至岗位添加页面");
		return "sysmanage/headship/addHeadship";
	}
	
	/**
	 * 添加岗位信息
	 * 
	 * @param headship 岗位对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/save.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String saveHeadship(Headship headship, HttpServletRequest request) {
		_logger.debug("添加岗位信息");
		try{
			String isAdmin = "0";	//管理员
			
			if(!"0".equals(isAdmin)){	//非管理员
				headship.setCompanyId("登录人员公司id");
			}
			
			headship.setCreateMan("userId");	//登录用户id
			headship.setCreateTime(new Date());
			headship.setDelFlag("0");			//0：未删除，1：已删除
			this.headshipService.saveHeadship(headship);
		}catch(Exception e){
			_logger.error("添加岗位信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * 跳转至岗位修改页面
	 * 
	 * @param headshipId 岗位信息Id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit.htm")
	public String update(@RequestParam("headshipId") String headshipId, Model model) {
		_logger.debug("跳转至岗位修改页面");
		Headship headship = new Headship();
		headship = this.headshipService.getHeadship(headshipId);
		model.addAttribute("headship", headship);
		return "sysmanage/headship/editHeadship";
	}
	
	/**
	 * 修改岗位信息
	 * 
	 * @param headship 岗位对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editHeadship.htm", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String updateHeadShip(Headship headship ,HttpServletRequest request) {
		_logger.debug("修改岗位信息");
		try {
			//管理员
			String admin = "isAdmin";
			if(!"isAdmin".equals(admin)){
				//登录人员公司id
				//headship.setCompanyId("登录人员公司id");
			}
			headship.setModifyTime(new Date());
			//登录用户
			headship.setModifyMan("修改人");
			this.headshipService.updateHeadship(headship);
		} catch (Exception e) {
			_logger.error("修改岗位信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * 删除岗位信息
	 * 
	 * @param headships 岗位信息id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/del.htm", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String deleteHeadship(@RequestParam("headships") String headships ,HttpServletRequest request) {
		_logger.debug("删除岗位信息");
		try {
			String[] headshipIds = headships.split(",");
			for(int j=0;j<headshipIds.length;j++){
				this.headshipService.deleteHeadship(headshipIds[j]);
			}
		} catch (Exception e) {
			_logger.error("删除岗位信息失败");
			return "0";
		}
		return "1";
	}
	
	/**
	 * 根据公司id获取岗位下拉框
	 * @param companyId 公司id
	 * @return
	 */
	@RequestMapping("/headshipTree/{companyId}.htm")
	@ResponseBody
	public List<Map<String,Object>> headshipCombo(@PathVariable String companyId){
		_logger.debug("根据公司id获取岗位下拉框");
		return this.headshipService.headshipCombo(companyId);
	}
}
