// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cmcc.zysoft.sellmanager.util.MD5Tools;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：Token 生成令牌及令牌识别
 * <br />版本:1.0.0
 * <br />日期： 2013-3-5 下午5:45:56
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class Tokens {
	
	
	/**
	 * 生成令牌.
	 * @param IMEI
	 * @param nowDate 当前时间
	 * @param salt 令牌盐
	 * @return 
	 * 返回类型：String
	 */
	public static String generate(String IMEI,Date nowDate,String salt){
//		Date now_date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		String now_time = sd.format(nowDate);
//		String salt = UUIDUtil.generateUUID();
		String token = MD5Tools.encodePassword(IMEI+now_time, salt);
		return token;
	}
	
	/**
	 * 令牌校验.
	 * @param IMEI
	 * @param tokenValue 令牌值
	 * @param tokenSalt 令牌盐
	 * @param tokenDate 令牌有效期
	 * @param createTime 令牌生成时间
	 * @return
	 * @throws ParseException 
	 * 返回类型：boolean
	 */
	public static boolean validate(String IMEI,String tokenValue,String tokenSalt,String tokenDate,String createTime) throws ParseException{
		Date now_date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now_time = now_date.getTime();
		long value_time = sd.parse(tokenDate).getTime();
		if(now_time>value_time){
			//令牌已过期
			return false;
		}else{
			Date createDate = sd.parse(createTime);
			String new_token = generate(IMEI,createDate,tokenSalt);
			if(new_token.equals(tokenValue)){
				return true;
			}else{
				//令牌在有效期内,但是令牌不匹配
				return false;
			}
		}
	}
	
}
