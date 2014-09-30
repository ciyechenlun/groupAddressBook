// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.capatch;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：验证码错误异常
 * <br />版本:1.0.0
 * <br />日期： 2013-1-10 下午8:37:47
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class BadCapatchCodeException extends AuthenticationException {
	
	// ~ serialVersionUID ： long
	private static final long serialVersionUID = -1580576914189293374L;

	/**
	 * 构造方法
	 * @param msg 详细信息
	 */
	public BadCapatchCodeException(String msg) {
		super(msg);
	}

    /**
     * 构造方法 
     * @param 详细信息
     * @param t 异常根信息
     */
    public BadCapatchCodeException(String msg, Throwable t) {
        super(msg, t);
    }

}
