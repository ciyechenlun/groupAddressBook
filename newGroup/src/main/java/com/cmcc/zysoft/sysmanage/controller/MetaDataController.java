// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.ConfigType;
import com.cmcc.zysoft.sellmanager.model.MetaData;
import com.cmcc.zysoft.sysmanage.service.ConfigTypeService;
import com.cmcc.zysoft.sysmanage.service.MetaDataService;
import com.starit.common.dao.support.Pagination;

/**
 * 元数据
 * @authod 闫豆
 * @date 2012-11-28 下午7:56:07
 */
@Controller
@RequestMapping("/pc/metaData")
public class MetaDataController extends BaseController {
	private static Logger _logger = LoggerFactory.getLogger(MetaDataController.class); 
	
	@Resource
	private MetaDataService metaDataService;
	
	@Resource
	private ConfigTypeService configTypeService;
	
	/**
	 * 根据metaIds批量删除元数据类型记录
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/meta/detele.htm")
	@ResponseBody
	@Transactional
	public String deletemetaByIds(String metaIds){
		_logger.debug("#根据metaIds批量删除元数据类型记录,metaIds={}",metaIds);
		try {
			this.metaDataService.deletemetaByIds(metaIds);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 根据typeid删除元数据类型记录
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/type/detele.htm")
	@ResponseBody
	@Transactional
	public String deleteConfigTypeById(String typeId){
		try {
			this.configTypeService.deleteEntity(typeId);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 元数据编辑跳转
	 * @return
	 */
	@RequestMapping("/meta/metaDataEdit.htm")
	public String metaDataEditEdit(String metaId,ModelMap map){
		if(StringUtils.hasText(metaId)){
			MetaData metaData =  this.metaDataService.getEntity(metaId);
			map.addAttribute("metaData", metaData);
		}
		return "sysmanage/metaData/metaDataEdit";
	}
	
	/**
	 * 元数据类型编辑跳转
	 * @return
	 */
	@RequestMapping("/type/configTypeEdit.htm")
	public String metaConfigTypeEdit(String typeId,ModelMap map){
		if(StringUtils.hasText(typeId)){
			ConfigType configType =  this.configTypeService.getEntity(typeId);
			map.addAttribute("configType", configType);
		}
		return "sysmanage/metaData/metaConfigTypeEdit";
	}

	/**
	 * 保存元数据
	 * @param metaData
	 * @return
	 */
	@RequestMapping("/meta/save.htm")
	@ResponseBody
	@Transactional
	public String saveMetaData(MetaData metaData){
		try {
			this.metaDataService.saveMetaData(metaData);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保存元数据配置信息
	 * @param configType
	 * @return
	 */
	@RequestMapping("/type/save.htm")
	@ResponseBody
	@Transactional
	public String saveConfigType(ConfigType configType){
		try {
			this.configTypeService.saveConfigType(configType);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 元数据列表
	 * @return
	 */
	@RequestMapping(value="/metaDatas")
	@ResponseBody
	public Pagination<Object> getAllMetaDatas(String typeId,int page,int rows) {
		return this.metaDataService.getAllMetaDatas(typeId,page,rows);
	}
	
	/**
	 * 元数据类型列表
	 * @return
	 */
	@RequestMapping(value="/configTypes")
	@ResponseBody
	public List<ConfigType> getAllConfigTypes() {
		return this.configTypeService.getAllConfigTypes();
	}
	
	/**
	 * 跳转到菜单管理页面
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String main(){
		return "sysmanage/metaData/metaData";
	}
}
