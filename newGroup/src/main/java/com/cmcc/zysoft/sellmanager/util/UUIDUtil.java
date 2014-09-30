// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

import java.util.UUID;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：UUID工具
 * <br />版本:1.0.0
 * <br />日期： 2013-1-11 下午7:50:10
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class UUIDUtil {

	/**
	 * 生成UUID字符串
	 * @return
	 */
	public static String generateUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
