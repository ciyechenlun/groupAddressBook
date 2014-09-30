// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.mobile.service.MDeviceService;
import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MDeviceController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-14 下午4:29:58
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("mobile/device")
public class MDeviceController extends BaseController{
	
	@Resource
	private MDeviceService mDeviceService;
	
	/**
	 * 校验设备号
	 * @param imei
	 * @param imsi
	 * @param phone
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public Map<String, Object> validate(String imei, String imsi, String phone){
		Map<String,Object> map = new HashMap<String,Object>();
		String msg = "";
		if(phone.equals("0") || phone.equals("12345678"))
		{
			//例外处理，便于ios版在appstore上传验证
			msg = "SUCCESS";
		}else{
			String deviceId = this.mDeviceService.getDeviceId(phone);
			if(StringUtils.hasText(deviceId)){
				msg = this.mDeviceService.validateRegisterCode(deviceId, phone, imei, imsi);
			}else{
				msg = "-1";
			}
		}
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 根据imei+imsi+mobile生成registerCode.
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping("/md5")
	@ResponseBody
	public String md5(){
		this.mDeviceService.md5();
		return "SUCCESS";
	}

}
