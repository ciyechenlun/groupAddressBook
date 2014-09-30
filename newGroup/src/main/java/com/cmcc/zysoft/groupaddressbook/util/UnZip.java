/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:UnZip.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-8
 */
package com.cmcc.zysoft.groupaddressbook.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.util
 * 创建时间：2013-12-8
 */
public class UnZip {
	/**
	 * 解压缩zip文件
	 * @param zipPath
	 * @param targetPath
	 * @return
	 * @throws IOException 
	 */
	public static String unzipFile(String zipFilePath,String targetPath) throws IOException
	{
		String retFileName = "";
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = null;
		try
		{
            zipFile = new ZipFile(zipFilePath);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf("."));
            } else {
                directoryPath = targetPath;
            }
            Enumeration entryEnum = zipFile.getEntries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                        directoryPath = directoryPath + File.separator
                                + zipEntry.getName();
                        System.out.println(directoryPath);
                        continue;
                    }
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = buildFile(directoryPath
                                + File.separator + zipEntry.getName(), false);
                        String imgName = zipEntry.getName();
                        retFileName += imgName + "|";
                        os = new BufferedOutputStream(new FileOutputStream(
                                targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }
                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        buildFile(directoryPath + File.separator
                                + zipEntry.getName(), true);
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if(null != zipFile){
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
		return retFileName;
	}
	
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * 
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author yayagepei
     * @date 2008-8-27
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    } 
}
