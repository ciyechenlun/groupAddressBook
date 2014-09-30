package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 文件处理工具类
 * 
 * @author John Tang
 * 
 */
public class FileUtil {

	// 日志
	private static final Logger log = Logger.getLogger(FileUtil.class);

	/**
	 * 获取当前系统下的换行符
	 * 
	 * @return String
	 */
	public static String getLineChar() {
		String newLine = System.getProperty("line.separator");
		if (newLine == null) {
			newLine = "\n";
		}
		return newLine;
	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @return
	 */
	public static boolean checkFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 读取文件的所有内容并返回
	 * 
	 * @param fileName
	 *            文件名
	 * @return String 文件内容,返回null则读取错误
	 */
	public static String getContent(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			log.warn(fileName + " 请输入正确的文件名");
			return null;
		}
		DataInputStream dis = null;
		BufferedReader reader = null;
		try {
			String newline = getLineChar();
			File f = new File(fileName);
			if (!f.exists() || f.isDirectory()) {
				log.warn(fileName + " 文件不存在");
				return null;
			}
			dis = new DataInputStream(new FileInputStream(f));
			reader = new BufferedReader(new InputStreamReader(dis));
			String tmp = "";
			StringBuffer result = new StringBuffer();
			while ((tmp = reader.readLine()) != null) {
				result.append(tmp + newline);
			}

			return result.toString();
		} catch (Exception e) {
			log.error("文件读取错误:" + e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 将文件内容读取,并返回每行内容的一个数组.
	 * 
	 * @param f
	 *            文件名
	 * @return lineContent[],返回null则读取错误
	 */
	public static String[] getLines(File f) {
		DataInputStream dis = null;
		BufferedReader reader = null;
		try {
			if (!f.exists() || f.isDirectory()) {
				return null;
			}
			dis = new DataInputStream(new FileInputStream(f));
			reader = new BufferedReader(new InputStreamReader(dis));
			String tmp = "";
			Vector<String> result = new Vector<String>();
			while ((tmp = reader.readLine()) != null) {
				result.add(tmp);
			}
			return result.toArray(new String[result.size()]);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
				}
			}
		}
		return null;

	}

	/**
	 * 将文件内容写入文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            内容
	 */
	public static void saveContent(String fileName, String content) {
		try {
			File f = findOrCreateFile(fileName);
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write(content);
			osw.close();
			fos.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 判断该文件是否存在,不存在则创建文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return File
	 */
	public static File findOrCreateFile(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			log.warn(fileName + " 不是一个正确的文件名");
			return null;
		}
		File f = new File(fileName);
		if (f.exists() && f.isFile()) {
			return f;
		}
		return createFile(f);
	}

	/**
	 * 创建文件，如果存在，删除后，新建
	 * 
	 * @param f
	 *            File
	 * @throws IOException
	 */
	public static void createNewFile(File f) throws IOException {
		if (f.exists()) {
			f.delete();
		}
		f.getParentFile().mkdirs();
		f.createNewFile();
	}

	/**
	 * 创建一个文件,如果存在,则先删除,再创建
	 * 
	 * @param fileName
	 *            文件名
	 * @return File
	 */
	public static File createFile(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			log.warn(fileName + " 不是一个正确的文件名");
			return null;
		}
		File f = new File(fileName);
		return createFile(f);
	}

	/**
	 * 创建一个新的文件,存在则先删除
	 * 
	 * @param f
	 *            文件名
	 * @return File
	 */
	public static File createFile(File f) {
		if (f.exists() && f.isFile()) {
			delete(f);
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			createDir(f.getParent());
			try {
				f.createNewFile();
			} catch (IOException e1) {
				log.error(e.getMessage(), e1);
			}
		}
		return f;
	}

	/**
	 * 判断该目录是否存在,存在则返回该目录不存在则创建目录
	 * 
	 * @param dir
	 *            目录名
	 * @return File
	 */
	public static File findOrCreateDir(String dir) {
		if (dir == null || dir.length() == 0) {
			log.warn(dir + " 不是一个正确的目录名");
			return null;
		}
		File f = new File(dir);
		if (f.exists() && f.isDirectory()) {
			return f;
		}
		return createDir(f);
	}

	/**
	 * 创建一个空目录
	 * 
	 * @param dir
	 *            目录名
	 * @return File
	 */
	public static File createDir(String dir) {
		if (dir == null) {
			return null;
		}
		File d = new File(dir);
		return createDir(d);
	}

	/**
	 * 创建空目录
	 * 
	 * @param f
	 *            目录
	 * @return 文件对象
	 */
	public static File createDir(File f) {
		if (f.exists() && f.isDirectory()) {
			return f;
		}
		if (f.exists()) {
			delete(f);

		}
		if (!f.mkdir()) {
			createDir(f.getParentFile());
			f.mkdir();
		}

		return f;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static void delete(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			log.warn(fileName + " 不是一个正确的目录或文件名");
			return;
		}
		File f = new File(fileName);
		delete(f);
	}

	/**
	 * 删除目录或文件
	 * 
	 * @param file
	 *            文件名
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isFile()) {
					fs[i].delete();
				} else {
					delete(fs[i]);
				}
			}
		}
		file.delete();
	}

	/**
	 * 获取文件的前缀名
	 * 
	 * @param fileName
	 *            文件名
	 * @return String
	 */
	public static String getShortName(String fileName) {
		File f = new File(fileName);
		return f.getName();
	}

	/**
	 * 将一个大文件切分到指定目录的小文件,返回切分后的文件名数组,切分后的文件按count的顺序命名,并添加指定扩展名
	 * 
	 * @param orgDir
	 *            原始文件所在目录,空则为当前目录
	 * @param originFile
	 *            原始文件
	 * @param destDir
	 *            目标目录,空则为当前目录
	 * @param ext
	 *            重名名后的文件后缀
	 * @param count
	 *            切分后单个文件行数
	 * @return String[] 文件名数组,null表示处理异常
	 */
	public static String[] splitFileByEachFileLines(String orgDir,
			String originFile, String destDir, String ext, int count) {
		if (originFile == null || originFile.length() == 0) {
			return null;
		}
		// 原始文件
		if (orgDir == null || orgDir.length() == 0) {
			orgDir = null;
		}
		File orgFile = new File(orgDir, originFile);
		if (orgFile == null || !orgFile.exists()) {
			log.warn("没有要分割的原始文件");
			return null;
		}

		// 读取文件内容
		DataInputStream dis = null;
		BufferedReader reader = null;
		try {
			dis = new DataInputStream(new FileInputStream(orgFile));
			reader = new BufferedReader(new InputStreamReader(dis));

			// 读取成功,进行分割
			// 每个小文件中的文件行数
			int lineCountInEveryFile = count;
			int fileStep = 0;
			int line = 0;
			String newLine = getLineChar();
			StringBuilder tmpContent = new StringBuilder();

			Vector<String> fileResult = new Vector<String>();

			String tmpLine = "";
			// 遍历文件内容的每一行
			while ((tmpLine = reader.readLine()) != null) {
				if (line < lineCountInEveryFile // 每个文件的行数还未达到
				) {
					if (tmpLine.length() > 0) {
						tmpContent.append(tmpLine + newLine);
						line++;
					}
				}
				if (line == lineCountInEveryFile // 每个文件的行数到了
				) {
					// 将内容写入文件
					String filename = destDir + System.currentTimeMillis()
							+ "_" + fileStep + ext;
					saveContent(filename, tmpContent.toString());
					fileResult.add(filename);
					// 重置行数计数器
					line = 0;
					// 清空内容
					tmpContent.setLength(0);
					fileStep++;
				}
			}

			// 所有内容处理完毕,将最后一个文件的内容写入
			if (tmpContent.length() > 0) {
				// 将内容写入文件
				String lastFileName = destDir + System.currentTimeMillis()
						+ "_" + fileStep + ext;
				saveContent(lastFileName, tmpContent.toString());
				fileResult.add(lastFileName);
			}

			return fileResult.toArray(new String[fileResult.size()]);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 重名名文件到指定目录
	 * 
	 * @param srcFile
	 *            原始文件
	 * @param destDir
	 *            目标目录
	 * @param destfile
	 *            目标文件
	 * @return true成功或false失败
	 */
	public static Boolean renameTo(String srcFile, String destDir,
			String destfile) {
		findOrCreateDir(destDir);
		return new File(srcFile).renameTo(new File(destDir, destfile));
	}

	/**
	 * 列出该目录下的指定后缀的文件
	 * 
	 * @param dir
	 *            目录名
	 * @param ext
	 *            后缀
	 * @return String[]
	 */
	public static String[] list(String dir, String ext) {
		if (dir == null || dir.length() == 0) {
			return null;
		}

		return new File(dir).list(new FileFilter(ext));
	}

	/**
	 * 文件名过滤器
	 * 
	 * @author John Tang
	 * 
	 */
	static class FileFilter implements FilenameFilter {

		String suffix;

		/**
		 * 默认构造方法
		 */
		public FileFilter() {

		}

		/**
		 * 带参构造方法
		 * 
		 * @param suffix
		 *            文件后缀
		 */
		public FileFilter(String suffix) {
			this.suffix = suffix;
		}

		/**
		 * Tests if a specified file should be included in a file list.
		 * 
		 * @param dir
		 *            the directory in which the file was found.
		 * @param name
		 *            the name of the file.
		 * @return <code>true</code> if and only if the name should be included
		 *         in the file list; <code>false</code> otherwise.
		 */
		public boolean accept(File dir, String name) {
			if (new File(dir, name).isFile()) {
				return name.endsWith(suffix);
			}
			return false;
		}
	}

	/**
	 * 将字符串写入到文件中，
	 * 
	 * @param filePath
	 *            文件路径+文件名
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            字符串编码格式 默认系统编码
	 * @throws Exception
	 */
	public static void writeFileByString(String filePath, String fileContent,
			String encoding) {
		FileUtil.writeFileByString(filePath, fileContent, encoding, false);
	}

	/**
	 * 将字符串写入到文件中，
	 * 
	 * @param filePath
	 *            文件路径+文件名
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            字符串编码格式 默认系统编码
	 * @param append
	 *            如果为true表示将fileContent中的内容添加到文件file末尾处
	 * @throws Exception
	 * 
	 * @author duguocheng
	 */
	public static void writeFileByString(String filePath, String fileContent,
			String encoding, boolean append) {
		PrintWriter out = null;
		try {
			if (filePath == null || fileContent == null
					|| fileContent.length() <= 0) {
				log.error("into writeFileByString [filePath=" + filePath
						+ ",filePath=" + fileContent + "] is null return!!!");
				return;
			}

			if (append) {
				File tempFile = new File(filePath);
				if (!tempFile.exists()) {
					tempFile.getParentFile().mkdirs();
					tempFile.createNewFile();
				}
			} else
				createNewFile(new File(filePath));

			if (encoding == null || encoding.trim().length() <= 0) {
				out = new PrintWriter(new FileWriter(filePath));
			} else {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath,
								append), encoding)), true);
			}
			out.print(fileContent);
			out.close();
		} catch (Exception e) {
			log.error("writeFileByString Exception:" + e, e);
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 将字符串写入到文件中，
	 * 
	 * @param filePath
	 *            文件路径+文件名
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            字符串编码格式 默认系统编码
	 * @param append
	 *            如果为true表示将fileContent中的内容添加到文件file末尾处
	 * @throws Exception
	 * 
	 * @author duguocheng
	 * @throws Exception
	 */
	public static void writeFileToString(String filePath, String fileContent,
			String encoding, boolean append) throws Exception {
		PrintWriter out = null;
		try {
			if (filePath == null || fileContent == null
					|| fileContent.length() <= 0) {
				log.error("into writeFileByString [filePath=" + filePath
						+ ",filePath=" + fileContent + "] is null return!!!");
				return;
			}

			File tempFile = new File(filePath);
			if (!tempFile.exists()) {
				tempFile.getParentFile().mkdirs();
				tempFile.createNewFile();
			}

			if (encoding == null || encoding.trim().length() <= 0) {
				out = new PrintWriter(new FileWriter(filePath));
			} else {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath,
								append), encoding)), true);
			}
			out.print(fileContent);
			out.close();
		} catch (Exception e) {
			log.error("writeFileByString Exception:" + e, e);
			throw e;
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 将字符串写入到文件中，
	 * 
	 * @param filePath
	 *            文件路径+文件名
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            字符串编码格式 默认系统编码
	 * @param append
	 *            如果为true表示将fileContent中的内容添加到文件file末尾处
	 * @param isNewLine
	 *            boolean
	 * @throws Exception
	 * @author duguocheng
	 */
	public static void writeFileByString(String filePath, String fileContent,
			String encoding, boolean append, boolean isNewLine) {
		PrintWriter out = null;
		try {
			if (filePath == null || fileContent == null
					|| fileContent.length() <= 0) {
				log.error("into writeFileByString [filePath=" + filePath
						+ ",filePath=" + fileContent + "] is null return!!!");
				return;
			}

			if (append) {
				File tempFile = new File(filePath);
				if (!tempFile.exists()) {
					tempFile.getParentFile().mkdirs();
					tempFile.createNewFile();
				}
			} else
				createNewFile(new File(filePath));

			if (encoding == null || encoding.trim().length() <= 0) {
				out = new PrintWriter(new FileWriter(filePath));
			} else {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath,
								append), encoding)), true);
			}
			if (isNewLine) {
				out.println();
			}
			out.print(fileContent);
			out.close();
		} catch (Exception e) {
			log.error("writeFileByString Exception:" + e, e);
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 
	 * @param f
	 *            File
	 * @return
	 */
	public static String loadAFileToString(File f) {
		InputStream is = null;
		String ret = null;
		try {
			is = new BufferedInputStream(new FileInputStream(f));
			long contentLength = f.length();
			ByteArrayOutputStream outstream = new ByteArrayOutputStream(
					contentLength > 0 ? (int) contentLength : 1024);
			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) > 0) {
				outstream.write(buffer, 0, len);
			}
			outstream.close();
			ret = new String(outstream.toByteArray(), "utf-8");
		} catch (IOException e) {
			ret = "";
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param f
	 *            File
	 * @return
	 */
	public static String loadAFileToString2(File f) {
		InputStream is = null;
		String ret = "";
		if (f.exists()) {
			BufferedReader br = null;
			try {
				String line;
				is = new FileInputStream(f);
				InputStreamReader read = new InputStreamReader(is, "utf-8");
				br = new BufferedReader(read);
				while ((line = br.readLine()) != null) {
					ret += line + "\r\n";
				}
			} catch (Exception e) {
				ret = "";
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 删除指定文件或目录
	 * 
	 * @param file
	 *            需要被删除的文件
	 * @return 0成功,非0失败
	 */
	public static int deleteFile(File file) {
		if (file == null) {
			return -1;
		}
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (subFiles != null) {
					for (int i = 0; i < subFiles.length; i++) {
						deleteFile(subFiles[i]);
					}
				}
				file.delete();
			}
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * @param filePath
	 *            String
	 */
	public static void deleteFile(String filePath) {
		String path = filePath.toString();
		File delFile = new File(path);
		deleteFile(delFile);
	}

}
