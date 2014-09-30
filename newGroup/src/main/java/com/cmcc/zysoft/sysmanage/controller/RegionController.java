// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sysmanage.service.CityService;
import com.cmcc.zysoft.sysmanage.service.ProvinceService;
import com.cmcc.zysoft.sysmanage.service.TownService;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：省市区信息
 * <br />@version:1.0.0
 * <br />日期： 2012-12-12 上午11:11:34
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/region")
public class RegionController extends BaseController {

	@Resource
	private ProvinceService provinceService;
	
	@Resource
	private CityService cityService;
	
	@Resource
	private TownService townService;
	
	/**
	 * 获取省份列表
	 * @return
	 */
	@RequestMapping(value="/province")
	@ResponseBody
	public List<Map<String, Object>> getPro(){
		return provinceService.province();
	}
	
	/**
	 * 根据省份ID获取城市列表
	 * @param provinceid 省份ID
	 * @return  城市列表
	 */
	@RequestMapping(value="/city")
	@ResponseBody
	public List<Map<String, Object>> city(String provinceid){
		return cityService.cities(provinceid);
	}
	
	/**
	 * 根据城市ID获取区县列表
	 * @param cityid 城市ID
	 * @return  区县列表
	 */
	@RequestMapping(value="/town")
	@ResponseBody
	public List<Map<String, Object>> area(String cityid){
		return townService.areas(cityid);
	}
	
}
