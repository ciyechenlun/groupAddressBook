package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 附件操作工具类
 * 
 * @author Administrator
 * 
 */
public class FileOperatorUtil {
	private static final int BUFFER_SIZE = 16 * 1024;

	/**
	 * 自己封装的一个把源文件对象复制成目标文件对象
	 * 
	 * @param src
	 *            File
	 * @param dst
	 *            File
	 * @throws Exception
	 */
	public static void copy(File src, File dst) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 把一个文件输出到一个流中
	 * 
	 * @param src
	 *            File
	 * @param os
	 *            OutputStream
	 */
	public static void copyFileAsStream(File src, OutputStream os) {
		if (src == null || os == null) {
			return;
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(src);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
		} catch (Exception e) {
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}
}
