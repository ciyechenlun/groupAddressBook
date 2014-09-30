package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * 文件上传工具类
 * 
 * @author Administrator
 * 
 */
public class FileUploadUtil {
	private static Logger log = Logger.getLogger(FileUploadUtil.class);

	/**
	 * 创建文件路径
	 * 
	 * @param path
	 *            String
	 */
	public static boolean createFolder(String path) {
		try {
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdir();
			}
		} catch (Exception e) {
			log.error("error occur when creating folder", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fullName
	 *            String
	 * @return
	 */
	public static boolean newFile(String fullName) {
		try {
			File file = new File(fullName);
			if (!file.exists()) {
				if (!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (Exception e) {
			log.error("error occur when creating folder", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断文件是否存在，并写入文本
	 * 
	 * @param fullName
	 *            String
	 * @param text
	 *            String
	 * @return
	 */
	public static boolean newFile(String fullName, String text) {
		try {
			newFile(fullName);
			FileWriter out = new FileWriter(fullName, false);
			out.write(text);
			out.close();
		} catch (Exception e) {
			log.error("error occur when creating file", e);
			return false;
		}
		return true;

	}

	/**
	 * 追加文件内容
	 * 
	 * @param fullName
	 *            String
	 * @param text
	 *            String
	 * @return
	 */
	public static boolean appendFile(String fullName, String text) {
		try {
			newFile(fullName);
			FileWriter out = new FileWriter(fullName, true);
			out.write(text);
			out.close();
		} catch (Exception e) {
			log.error("error occur when creating file", e);
			return false;
		}
		return true;

	}

	/**
	 * 删除文件
	 * 
	 * @param fullName
	 *            String
	 * @return
	 */
	public static boolean delFile(String fullName) {
		try {
			File file = new File(fullName);
			if (file.exists()) {
				return file.delete();
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * 删除文件
	 * 
	 * @param fullName
	 *            String
	 */
	public static void delFileOnExit(String fullName) {
		try {
			File file = new File(fullName);
			if (file.exists()) {
				file.deleteOnExit();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 文件重命名
	 * 
	 * @param fileName
	 *            String
	 * @param distFile
	 *            String
	 * @return
	 */
	public static boolean rename(String fileName, String distFile) {
		synchronized (fileName) {
			File oldFile = new File(fileName);
			if (oldFile.exists()) {
				try {
					oldFile.renameTo(new File(distFile));
					return true;
				} catch (Exception e) {
					return false;
				}
			} else
				return true;
		}
	}

	/**
	 * 
	 * @param pathFolder
	 *            String
	 * @param recursiveRemove
	 *            boolean
	 * @return
	 */
	public static boolean removeFolder(String pathFolder,
			boolean recursiveRemove) {
		File folder = new File(pathFolder);
		if (folder.isDirectory()) {
			return removeFolder(folder, recursiveRemove);
		}
		return false;
	}

	/**
	 * 
	 * @param folder
	 *            File
	 * @param removeRecursivly
	 *            boolean
	 * @return
	 */
	public static boolean removeFolder(File folder, boolean removeRecursivly) {
		if (removeRecursivly) {
			for (File current : folder.listFiles()) {
				if (current.isDirectory()) {
					removeFolder(current, true);
				} else {
					current.delete();
				}
			}
		}
		return folder.delete();
	}

	/**
	 * 删除目录
	 * 
	 * @param path
	 *            String
	 * @return
	 */
	public static boolean delFolder(String path) {
		return removeFolder(path, true);
	}

	/**
	 * 复制文件
	 * 
	 * @param source
	 *            String
	 * @param target
	 *            String
	 * @return
	 */
	public static boolean copyFile(String source, String target) {
		boolean isSuc = false;
		InputStream in = null;
		FileOutputStream out = null;
		try {
//			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(source);
			newFile(target);
			if (oldfile.exists()) {
				in = new FileInputStream(source);
				out = new FileOutputStream(target, false);// no append,
															// overwrite old.
				byte[] buffer = new byte[4096];
				while ((byteread = in.read(buffer)) != -1) {
//					bytesum += byteread;
					out.write(buffer, 0, byteread);
				}
				isSuc = true;
			} else {
				System.err.println("File " + source + " not exists");
			}

		} catch (Exception e) {
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
		return isSuc;
	}

	/**
	 * 
	 * @param source
	 *            String
	 * @param target
	 *            String
	 * @return
	 */
	public static boolean copyFileFilter(String source, String target) {
		String folderPath = source.substring(0, source.lastIndexOf("/"));
		String fileName = source.substring(source.lastIndexOf("/") + 1);
		String prefixFileName = fileName
				.substring(0, fileName.lastIndexOf("*"));
		File fullDir = new File(folderPath);
		File[] allFile = fullDir.listFiles(new PagingFileFilter(fileName));
		if (allFile != null && allFile.length > 0) {
			for (File file : allFile) {
				InputStream in = null;
				FileOutputStream out = null;
				try {
//					int bytesum = 0;
					int byteread = 0;
					String fileSuffix = file.getName().substring(
							prefixFileName.length());
					String targetFilePath = target.substring(0,
							target.lastIndexOf("."))
							+ fileSuffix;
					newFile(targetFilePath);
					in = new FileInputStream(file);
					out = new FileOutputStream(targetFilePath, false); // no
																		// append,
					// overwrite old.
					byte[] buffer = new byte[4096];
					while ((byteread = in.read(buffer)) != -1) {
//						bytesum += byteread;
						out.write(buffer, 0, byteread);
					}
				} catch (Exception e) {
					return false;
				} finally {
					try {
						if (in != null)
							in.close();
						if (out != null)
							out.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param oldfile
	 *            String
	 * @param newfilepath
	 *            String
	 * @param newfile
	 *            String
	 * @return
	 */
	public static boolean copyFileNoOverWrite(String oldfile,
			String newfilepath, String newfile) {
		return copyFileNoOverWrite(oldfile, newfilepath + newfile);
	}

	/**
	 * 
	 * @param oldfile
	 *            String
	 * @param newfile
	 *            String
	 * @return
	 */
	public static boolean copyFileNoOverWrite(String oldfile, String newfile) {
		if (FileUtil.checkFileExists(oldfile)
				&& !FileUtil.checkFileExists(newfile)) { // 文件存在时
			return copyFile(oldfile, newfile);
		}
		return true;
	}

	/**
	 * 
	 * @param source
	 *            String
	 * @param target
	 *            String
	 */
	public static void copyFolder(String source, String target) {

		if (target.indexOf(source) != -1) {
			System.err.println("target is source's sub directory");
			return;
		}

		try {
			File dir = new File(source);
			File[] listFiles = dir.listFiles();
			File fileSource = null;
			for (int i = 0; i < listFiles.length; i++) {
				fileSource = listFiles[i];
				if (fileSource.isDirectory()) {
					createFolder(target + "/" + fileSource.getName());
					copyFolder(fileSource.getAbsolutePath() + "/", target + "/"
							+ fileSource.getName() + "/");
				} else {
					copyFile(fileSource.getAbsolutePath(), target + "/"
							+ fileSource.getName());
				}
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param oldPath
	 *            String
	 * @param newFolderPath
	 *            String
	 * @param fileName
	 *            String
	 * @return
	 */
	public static boolean moveFile(String oldPath, String newFolderPath,
			String fileName) {
		return copyFile(oldPath, newFolderPath + fileName) && delFile(oldPath);
	}

	/**
	 * 
	 * @param oldPath
	 *            String
	 * @param newPath
	 *            String
	 * @return
	 */
	public static boolean moveFile(String oldPath, String newPath) {
		return copyFile(oldPath, newPath) && delFile(oldPath);
	}


	/**
	 * 
	 * @param oldPath
	 *            String
	 * @param newPath
	 *            String
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 
	 * @param filename
	 *            String
	 * @return
	 */
	public static String getExtName(String filename) {
		int index = filename.lastIndexOf('.');
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	/**
	 * 
	 * @param path
	 *            String
	 * @return
	 */
	public static String replaceSeparator(String path) {
		return (path == null) ? null : path.replaceAll("\\\\", "/").replaceAll(
				"//", "/");
	}

	/**
	 * 得到文件后缀
	 * 
	 * @param name
	 *            String
	 * @return
	 */
	public static String getFileType(String name) {
		String type = "";
		if (name != null && !name.endsWith(".") && name.lastIndexOf(".") > 0) {
			type = name.substring(name.lastIndexOf(".") + 1);
		}
		return type;
	}
}
