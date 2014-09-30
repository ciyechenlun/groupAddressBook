package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.zysoft.groupaddressbook.service.ReplyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

@Controller
@RequestMapping("/pc/reply")
public class ReplyController extends BaseController{
	
	@Resource
	private ReplyService ReplyService;
	/**
	 * 打开回复反馈信息的子页面
	 * @param modelMap 
	 * @param adviseId 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String toReply(ModelMap modelMap,String adviseId){
		Map<String,Object> map = ReplyService.getAdviseInfo(adviseId);
		modelMap.putAll(map);
		return "web/adviseReply";
	}
	

}
