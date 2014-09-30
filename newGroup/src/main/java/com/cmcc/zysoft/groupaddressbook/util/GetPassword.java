// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.util;

import com.cmcc.zysoft.sellmanager.util.MD5Tools;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：GetPassword
 * <br />版本:1.0.0
 * <br />日期： 2013-4-25 上午10:29:05
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class GetPassword {
	
	/**
	 * @param args 
	 * 返回类型：void
	 */
	public static void main(String[] args) {
		String salt = "";
		String passWord = "";
		String testStr = "";
		for(int i=0;i<1000000;i++){
			if(i<10){
				testStr = "00000"+i;
			}else if(i<100){
				testStr = "0000"+i;
			}else if(i<1000){
				testStr = "000"+i;
			}else if(i<10000){
				testStr = "00"+i;
			}else if(i<100000){
				testStr = "0"+i;
			}else{
				testStr = ""+i;
			}
			String testPassword = MD5Tools.encodePassword(testStr, salt);
			if(testPassword.equals(passWord)){
				System.out.println(testStr);
				break;
			}
		}
		
	}

}
