// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.DictData;
import com.cmcc.zysoft.sellmanager.model.DictType;
import com.cmcc.zysoft.sysmanage.service.DictDataService;
import com.cmcc.zysoft.sysmanage.service.DictTypeService;

/**
 * 数据字典
 * @authod 闫豆
 * @date 2012-11-28 下午7:56:07
 */
@Controller
@RequestMapping("/pc/dictData")
public class DictDataController extends BaseController {

	@Resource
	private DictDataService dictDataService;
	
	@Resource
	private DictTypeService dictTypeService;
	
	/**
	 * 没有分页的数据字典类型列表
	 */
	@RequestMapping("/type/dictTypesForCombo")
	@ResponseBody
	public List<DictType> dictTypesForCombo(){
		return this.dictTypeService.dictTypesForCombo();
	}
	
	/**
	 * 根据dataIds批量数据字典数据
	 * @param dataIds
	 */
	@RequestMapping("/data/detele.htm")
	@ResponseBody
	@Transactional
	public String deleteDictDataByIds(String dataIds){
		try {
			this.dictDataService.deleteDictDataByIds(dataIds);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 根据typeid删除数据字典类型记录
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/type/detele.htm")
	@ResponseBody
	@Transactional
	public String deleteConfigTypeById(String typeId){
		try {
			this.dictTypeService.deleteConfigTypeById(typeId);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 数据字典数据编辑跳转
	 * @return
	 */
	@RequestMapping("/data/dictDataEdit.htm")
	public String metaDataEditEdit(String dataId,ModelMap map){
		if(StringUtils.hasText(dataId)){
			DictData dictData =  this.dictDataService.getEntity(dataId);
			map.addAttribute("dictData", dictData);
		}
		return "sysmanage/dictData/dictDataEdit";
	}
	
	/**
	 * 数据字典类型编辑跳转
	 * @return
	 */
	@RequestMapping("/type/dictTypeEdit.htm")
	public String metaConfigTypeEdit(String typeId,ModelMap map){
		if(StringUtils.hasText(typeId)){
			DictType dictType =  this.dictTypeService.getEntity(typeId);
			map.addAttribute("dictType", dictType);
		}
		return "sysmanage/dictData/dictTypeEdit";
	}

	/**
	 * 保存数据字典数据
	 * @param dictData
	 * @return
	 */
	@RequestMapping("/data/save.htm")
	@ResponseBody
	@Transactional
	public String saveDictData(DictData dictData){
		try {
			this.dictDataService.saveDictData(dictData);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保存数据字典类型
	 * @param dictType
	 * @return
	 */
	@RequestMapping("/type/save.htm")
	@ResponseBody
	@Transactional
	public String saveDictType(DictType dictType){
		try {
			this.dictTypeService.saveConfigType(dictType);
			return "SUCCESS";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 数据字典数据列表
	 * @return
	 */
	@RequestMapping(value="/data/metaDatas.htm")
	@ResponseBody
	public Map<String,Object> getAllMetaDatas(String typeId,int page,int rows) {
		return this.dictDataService.getDictDatas(typeId,page,rows);
	}
	
	/**
	 * 数据字典类型列表
	 * @return
	 */
	@RequestMapping(value="/type/dictTypes.htm")
	@ResponseBody
	public Map<String,Object> getDictTypes(int page,int rows) {
		Map<String,Object> pagination = this.dictTypeService.getDictTypes(page,rows);
		return pagination;
	}
	
	/**
	 * 跳转到菜单管理页面
	 * @return
	 */
	@RequestMapping(value="/main.htm")
	public String main(){
		return "sysmanage/dictData/dictData";
	}
	
	/**
	 * 获取数据字典下拉框
	 * 
	 * @param typeCode 字典类型编码
	 * @return
	 */
	@RequestMapping("/comoTree.htm")
	@ResponseBody
	public List<Map<String,Object>> comoTree(String typeCode){
		return this.dictTypeService.getDataComo(typeCode);
	}
}
