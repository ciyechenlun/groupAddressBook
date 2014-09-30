// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sysmanage.service.StatisticsConfigService;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：模块统计控制器
 * <br />版本:1.0.0
 * <br />日期： 2013-2-4 上午11:30:42
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/statistics")
public class StatisticsController extends BaseController {
	
	@Resource
	private StatisticsConfigService statisticsConfigService;
	
	/**
	 * 属性名称：logger <br/>
	 * 类型：Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
	
	/**
	 * 模块统计配置页面
	 */
	private static final String STATISTICS_CONFIG_VIEW = "sysmanage/statistics/statisticsConfig";
	
	/**
	 * 模块统计配置
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/config.htm")
	public String statisticsConfig(){
		logger.debug("#跳转模块统计配置");
		return STATISTICS_CONFIG_VIEW;
	}
	
	/**
	 * 获取已选的配置信息
	 * @return
	 */
	@RequestMapping("/list.htm")
	@ResponseBody
	public List<Map<String, Object>> list(){
		return this.statisticsConfigService.list();
	}
	
	/**
	 * 添加叶子节点
	 * @param menuId
	 * @param menuName
	 * @param path
	 * @return
	 */
	@RequestMapping("/save.htm")
	@Transactional
	@ResponseBody
	public String save(String menuId,String menuName,String path){
		String id = this.statisticsConfigService.save(menuId, menuName, path);
		if(StringUtils.hasText(id)){
			logger.debug("添加叶子节点成功");
			return "SUCCESS";
		}else{
			logger.debug("添加叶子节点失败");
			return "FAILURE";
		}
	}
	
	/**
	 * 删除叶子节点
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/delete.htm")
	@Transactional
	@ResponseBody
	public String delete(String menuId){
		this.statisticsConfigService.delete(menuId);
		logger.debug("删除叶子节点");
		return "SUCCESS";
	}
	
	/**
	 * 增加非叶子节点
	 * @param menuId
	 * @param menuName
	 * @param path
	 * @return
	 */
	@RequestMapping("/saveAll.htm")
	@Transactional
	@ResponseBody
	public String saveAll(String menuId,String menuName,String path){
		this.statisticsConfigService.saveAll(menuId, menuName, path);
		logger.debug("增加非叶子节点");
		return "SUCCESS";
	}
	
	/**
	 * 删除非叶子节点
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/deleteAll.htm")
	@Transactional
	@ResponseBody
	public String deleteAll(String menuId){
		this.statisticsConfigService.deleteAll(menuId);
		logger.debug("删除非叶子节点");
		return "SUCCESS";
	}
	
}
