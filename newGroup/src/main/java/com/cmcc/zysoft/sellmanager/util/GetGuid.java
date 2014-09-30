package com.cmcc.zysoft.sellmanager.util;

import java.util.UUID;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：获取UUID
 * <br />版本:1.0.0
 * <br />日期： 2013-1-11 下午7:46:24
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class GetGuid {
	
	/**
	 * 获取UUID
	 * @return
	 */
	public String getGuid(){
		UUID uuid = UUID.randomUUID(); 
		String guid = uuid.toString();   
		return guid;
	}

}
