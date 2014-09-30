// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.spring.security.capatch;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 闫豆
 * 没有权限异常
 * @mail yan.dou@ustcinfo.com
 * @date 2013-3-15 上午10:42:15
 */
public class NoAuthorityException extends AuthenticationException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8198504477099365245L;

	/**
	 * 构造方法
	 * @param msg 详细信息
	 */
	public NoAuthorityException(String msg) {
		super(msg);
	}

    /**
     * 构造方法 
     * @param 详细信息
     * @param t 异常根信息
     */
    public NoAuthorityException(String msg, Throwable t) {
        super(msg, t);
    }

}
