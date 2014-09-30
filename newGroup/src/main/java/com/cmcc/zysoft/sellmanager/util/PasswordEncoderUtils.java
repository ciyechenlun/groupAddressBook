// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

import org.springframework.security.crypto.codec.Utf8;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：密码加密工具
 * <br />@version:1.0.0
 * <br />日期： 2012-12-10 下午3:03:44
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public class PasswordEncoderUtils {

	/**
     * Constant time comparison to prevent against timing attacks.
     * @param expected
     * @param actual
     * @return
     */
    static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
        int actualLength = actualBytes == null ? -1 : actualBytes.length;
        if (expectedLength != actualLength) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expectedLength; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }

    /**
     * 获取字符串的字节集
     * @param s
     * @return
     */
    private static byte[] bytesUtf8(String s) {
        if(s == null) {
            return null;
        }

        return Utf8.encode(s);
    }
    
    /**
     * 构造方法
     */
    private PasswordEncoderUtils() {}
}
