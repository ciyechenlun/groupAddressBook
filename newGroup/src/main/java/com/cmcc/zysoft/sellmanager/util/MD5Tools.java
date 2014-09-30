// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sellmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：MD5工具类
 * <br />@version:1.0.0
 * <br />日期： 2012-12-10 下午3:07:17
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
public abstract class MD5Tools {

	/**
	 * 构造方法
	 * @throws IllegalAccessException
	 */
	private MD5Tools() throws IllegalAccessException {
		throw new IllegalAccessException("MD5Tools不能被实例化");
	}

	/**
	 * messagedigest
	 */
	private static MessageDigest messagedigest;

	/**
	 * MD5_CHARS
	 */
	private static char          MD5_CHARS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取指定File的MD5码
	 *
	 * @author www.TheWk.cn.vc
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
		messagedigest = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}
	
	
	/**
     * Used by subclasses to generate a merged password and salt <code>String</code>.<P>The generated password
     * will be in the form of <code>password{salt}</code>.</p>
     *  <p>A <code>null</code> can be passed to either method, and will be handled correctly. If the
     * <code>salt</code> is <code>null</code> or empty, the resulting generated password will simply be the passed
     * <code>password</code>. The <code>toString</code> method of the <code>salt</code> will be used to represent the
     * salt.</p>
     *
     * @param password the password to be used (can be <code>null</code>)
     * @param salt the salt to be used (can be <code>null</code>)
     * @param strict ensures salt doesn't contain the delimiters
     *
     * @return a merged password and salt <code>String</code>
     *
     * @throws IllegalArgumentException if the salt contains '{' or '}' characters.
     */
    protected static String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }

        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
    
    /**
     * Get a MessageDigest instance for the given algorithm.
     * Throws an IllegalArgumentException if <i>algorithm</i> is unknown
     *
     * @return MessageDigest instance
     * @throws IllegalArgumentException if NoSuchAlgorithmException is thrown
     */
    protected static final MessageDigest getMessageDigest() throws IllegalArgumentException {
    	String algorithm = "md5";
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
        }
    }
	
	/**
	 * 给密码加盐
	 * @param rawPass 密码明文
	 * @param salt 盐值
	 * @return 加盐后的密码密文
	 */
	public static String encodePassword(String rawPass, Object salt) {
        //利用Spring的md5密码加密器加密
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder.encodePassword(rawPass, salt);
    }
	
	/**
	 * 验证密码是否正确
	 * @param encPass 加密过的密码密文
	 * @param rawPass 原始密码
	 * @param salt 密码盐
	 * @return true：密码验证一致  false：密码验证不一致
	 */
	public static boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);

        return PasswordEncoderUtils.equals(pass1,pass2);
    }
	
	/**
	 * 获取指定字符串的MD5码
	 *
	 * @author www.TheWk.cn.vc
	 * @throws NoSuchAlgorithmException
	 */
	public static String getStringMD5(String str) throws NoSuchAlgorithmException {
		messagedigest = MessageDigest.getInstance("MD5");
		messagedigest.update(str.getBytes());
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * 验证一个字符串的MD5码,与指定的MD5码是否相等
	 *
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean check(String str, String md5) throws NoSuchAlgorithmException {
		return getStringMD5(str).equals(md5);
	}

	/**
	 * 验证一个FILE的MD5码,与指定的MD5码是否相等
	 *
	 * @author www.TheWk.cn.vc
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean check(File f, String md5) throws NoSuchAlgorithmException, IOException {
		return getFileMD5(f).equals(md5);
	}

	/**
	 * bufferToHex
	 * @param bytes
	 * @return
	 */
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	/**
	 * bufferToHex
	 * @param bytes
	 * @param m
	 * @param n
	 * @return
	 */
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	/**
	 * appendHexPair
	 * @param bt
	 * @param stringbuffer
	 */
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = MD5_CHARS[(bt & 0xf0) >> 4];
		char c1 = MD5_CHARS[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static void main(String[] args) {
//		try {
//			String str = MD5Tools.getStringMD5("ccc");
//			System.out.println(str);
//			int num = 0;
//			for (char c : str.toCharArray()) {
//				num += c;
//			}
//			System.out.println(num % 1000 % 100);
//		}
//		catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
		
		String password = "111111";
		String salt = UUIDUtil.generateUUID();
		System.out.println("盐="+salt);
		String pwd = MD5Tools.encodePassword(password, salt);
		System.out.println("pwd="+pwd);
		
		System.out.println(MD5Tools.isPasswordValid(pwd, password, salt));
		
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		System.out.println("pwd=" + md5PasswordEncoder.encodePassword(password, salt));
	}

}
