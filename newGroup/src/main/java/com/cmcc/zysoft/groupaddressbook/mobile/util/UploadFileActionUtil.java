package com.cmcc.zysoft.groupaddressbook.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.management.modelmbean.XMLParseException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.cmcc.zysoft.sellmanager.model.UgcUploadFile;

/**
 * 
 * 
 * @author <a href="mailto:yushenke@wondertek.com.cn">Ken Yu</a>
 * @version 1.0
 * 
 *          Create on 2009-12-23 下午03:44:15
 */

public class UploadFileActionUtil {
	protected static final Log log = LogFactory
			.getLog(UploadFileActionUtil.class);

	/**
	 * 校验MD5码 NOTE:如果客户端没有上传md5码，那么不校验，默认校验通过
	 * 
	 * @param uploadFile
	 *            String
	 * @param path
	 *            String
	 * @return 0成功，非0失败
	 */
	public static int checkMD5(UgcUploadFile uploadFile, String path) {
		if (uploadFile == null) {
			throw new NullPointerException();
		}
		if (!StringUtils.hasText(uploadFile.getFileMd5())) {
			return 0;
		}
		String digestStr = getMD5OfFile(path + uploadFile.getFileLocalName());
		if (digestStr != null && uploadFile.getFileMd5().equals(digestStr)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 得到一个文件的MD5码
	 * 
	 * @param fileName
	 *            String
	 * @return
	 */
	public static String getMD5OfFile(String fileName) {
		if (fileName == null)
			throw new NullPointerException();
		File file = new File(fileName);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.error("", e);
			return null;
		}
		int off = 0;
		int len = (int) file.length();
		int readBytes = 0;
		byte[] fileBytes = new byte[len];
		try {
			while ((readBytes = is.read(fileBytes, off, len)) != -1) {
				off += readBytes;
				len -= readBytes;
				if (len == 0) {
					break;
				}
			}
		} catch (IOException e) {
			log.error("", e);
			return null;
		}
		MessageDigest md = null;
		try {
			md = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
			return null;
		}
		md.update(fileBytes);
		byte[] digestBytes = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
		String digestStr = UgcStringUtil.byteToHexString(digestBytes);
		return digestStr;
	}

	/**
	 * 如果上传文件、属性文件不存在，则创建；如果存在则需判断是否已上传完毕 实现： 1.判断是否已上传 2.创建属性文件并初始化
	 * 
	 * @param path
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @return 0 成功；1 文件已经上传完毕,-1 上传文件未创建
	 */
	public static int createUploadFile(HttpServletRequest request, String path) {
		// 1.判断是否已上传
		int status = isUpload(request, path);
		if (status != 0) {
			return status;
		}
		return 0;
	}

	/**
	 * 判断文件是否已上传
	 * 
	 * @param path
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @return 0 文件已初始化；1 文件已经上传完毕,2 客户端上传文件超过参数指定长度,-1 上传文件未创建
	 */
	public static int isUpload(HttpServletRequest request, String path) {
		// 1.判断是否已上传
		String fileLocalName = RequestUtil.getParam(request, "fileLocalName");
		long fileLen = RequestUtil.getLongParam(request, "fileSize");
		File oldUploadFile = new File(path + fileLocalName);
		if (oldUploadFile.exists()) {
			if (oldUploadFile.length() == fileLen) {
				return 1;
			} else if (oldUploadFile.length() > fileLen) {
				return 2;
			} else {
				return 0;
			}
		} else {
			log.error("uploadFile is not created.[fileName=" + path
					+ fileLocalName + "]");
			return -1;
		}
	}

	/**
	 * 判断文件上传位置,如果位置正确，修改uploadFile里的相关字段
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param uploadFile
	 *            String
	 * @param path
	 *            String
	 * @return 0 位置正确，非0，位置不正确
	 */
	public static int checkPosParam(HttpServletRequest request,
			UgcUploadFile uploadFile, String path) {
		Long clientStart = RequestUtil.getLongParam(request, "P_START");
		Long clientEnd = RequestUtil.getLongParam(request, "P_END");

		String fileLocalPropertiesName = RequestUtil.getParam(request,
				"fileLocalPropertiesName");
		if (clientStart == null || clientEnd == null) {
			return -1;
		} else {
			String propName = path + fileLocalPropertiesName;
			Document propDoc = DomUtil.parserXmlFromFile(propName);
			Element phaseNode = (Element) propDoc.getElementsByTagName("PHASE")
					.item(0);
			NamedNodeMap attributes = phaseNode.getAttributes();
			Long serverStart = Long.parseLong(attributes.getNamedItem("START")
					.getNodeValue());
			Long serverEnd = Long.parseLong(attributes.getNamedItem("END")
					.getNodeValue());
			if ((!clientStart.equals(serverStart)) || (clientEnd > serverEnd)) {
				return -2;
			} else {
				uploadFile.setStartOffset(serverStart);
				uploadFile.setEndOffset(serverEnd);
				return 0;
			}
		}
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param path
	 *            String
	 * @return
	 */
	public static int readRequest(HttpServletRequest request, String path) {
		InputStream is = null;
		OutputStream os = null;
		int readSize = 0;// 读取字节数
		int pStart = RequestUtil.getIntParam(request, "P_START");
		int pEnd = RequestUtil.getIntParam(request, "P_END");
		int tType = RequestUtil.getIntParam(request, "T_TYPE");
		int id = RequestUtil.getIntParam(request, "ID");
		String fileLocalName = RequestUtil.getParam(request, "fileLocalName");
		log.debug("===write=upload==pStart=" + pStart + " , pEnd : " + pEnd);
		int expectSize = pEnd - pStart;// 期望读取字节数
		if (expectSize < 0) {
			log.debug("===upload==expectSize < 0 ERROR return :" + expectSize);
			return -2;
		} else {
			log.debug("===upload==file size=" + expectSize);
		}
		try {
			String localNamePath = path + fileLocalName;
			localNamePath = FileUploadUtil.replaceSeparator(localNamePath);
			log.debug("======upload=====" + localNamePath);
			os = new FileOutputStream(localNamePath, true);
			is = request.getInputStream();
			if (is != null) {
				log.debug("======upload==request.getInputStream() is not null===");
			} else {
				log.debug("======upload==request.getInputStream() is null===");
			}

			log.debug("======upload====tType=" + tType);
			int cacheSize = 0;
			/**
			 * 针对不同的上传客户端使用不同的读取缓冲大小 RMS:2 和 SUP:1
			 */
			if (tType == PortalConstants.CLIENT_TYPE_RMS) {
				cacheSize = PortalConstants.UGC_UPLOAD_READ_CACHE_RMS;
			} else {
				cacheSize = PortalConstants.UGC_UPLOAD_READ_CACHE_DEFAULT;
			}
			log.debug("======upload====cacheSize=" + cacheSize);
			// byte[] bytes = new byte[cacheSize];//读取用的缓冲字节数组
			// int bytesRead = -1;//单次读取的字节数
			/*
			 * while ((bytesRead = is.read(bytes, 0, cacheSize)) != -1){
			 * os.write(bytes, 0, bytesRead); readSize += bytesRead;
			 * log.debug("======upload==bytesRead:"
			 * +bytesRead+",readSize:"+readSize); }
			 */

			int byteread = 0;
			byte[] buffer = new byte[1];
			while ((byteread = is.read(buffer, 0, 1)) != -1) {
				readSize += byteread;
				os.write(buffer, 0, byteread);
			}
			log.debug("==upload==read strean sum : " + readSize
					+ ",file size : " + expectSize);
			// 比较读取的字节数和上传参数
			if (readSize < expectSize) {
				log.debug("=========================================");
				log.debug("====upload_ERROR return, because of expectSize:"
						+ expectSize + " != readSize:" + readSize + "===");
				return -2;
			} else {
				readSize = expectSize;
				log.debug("======upload====readSize = expectSize=" + readSize);
			}
		} catch (IOException e) {
			log.debug("======upload=read request inputStream error:[uploadFile.id="
					+ id + "]" + e.getMessage());
			readSize = -1;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.debug("======upload=close inputStream error:"
							+ e.getMessage());
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					log.debug("======upload=close outputStream error:"
							+ e.getMessage());
				}
			}
		}
		return readSize;
	}

	/**
	 * 修改属性文件的PHASE节点的START字段
	 * 
	 * @param uploadFile
	 *            UgcUploadFile
	 * @param start
	 *            long
	 * @param path
	 *            String
	 * @return 0成功，-1，start值超过end值
	 */
	public static int modifyProp(UgcUploadFile uploadFile, long start,
			String path) {
		String propertiesName = path + uploadFile.getFileLocalPropertiesName();
		Document propertiesDoc = DomUtil.parserXmlFromFile(propertiesName);
		Element phaseNode = (Element) propertiesDoc.getElementsByTagName(
				"PHASE").item(0);
		NamedNodeMap attributes = phaseNode.getAttributes();
		long end = Long
				.parseLong(attributes.getNamedItem("END").getNodeValue());
		if (start > end) {
			return -1;
		} else {
			attributes.getNamedItem("START").setNodeValue(start + "");
		}
		FileUtil.writeFileByString(propertiesName,
				DomUtil.createXmlString(propertiesDoc), "UTF-8");
		return 0;
	}

	/**
	 * 上传成功后续处理：删除属性文件
	 * 
	 * @param uploadFile
	 *            UgcUploadFile
	 * @param path
	 *            String
	 * @param convertFileQueueManager
	 */
	public static void processSuccess(UgcUploadFile uploadFile, String path) {
//		String currTime = new Timestamp(System.currentTimeMillis()).toString();
		/**
		 * 删除属性文件
		 */
		String filePathAbs = path + uploadFile.getFileLocalPropertiesName();
		FileUtil.deleteFile(new File(filePathAbs));
		/**
		 * 修改上传文件表记录
		 */
		/*
		 * UploadFile oldUploadFile = uploadFileManager.get(uploadFile.getId());
		 * oldUploadFile.setUpdateTime(currTime);
		 * oldUploadFile.setStatus(UploadFile.STATUS_UPLOADED);
		 * uploadFileManager.update(oldUploadFile);
		 */
	}

	/**
	 * 得到加密的key
	 * 
	 * @param id
	 *            String
	 * @param createTime
	 *            String
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getEncryptKey(String id, String createTime) {
		StringBuffer key = new StringBuffer();
		key.append(
				UgcStringUtil.getStringByAppointLen(
						PortalConstants.FILE_UPLOAD_ENCRYPT_KEY, 8, true))
				.append(UgcStringUtil.getStringByAppointLen(id, 8, true))
				.append(UgcStringUtil
						.getStringByAppointLen(createTime, 8, true));
		return key.toString();
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param uploadFile
	 *            UgcUploadFile
	 * @return
	 */
	public static String checkUrl(HttpServletRequest request,
			UgcUploadFile uploadFile) {
		String currTime = new Timestamp(System.currentTimeMillis()).toString();
		if (uploadFile == null) {
			return PortalConstants.ERROR_CLIENT_WRONG_PARAMS;
		}

		String fileTitle = RequestUtil.getParam(request, "C_TITLE");// 内容标题
		String fileDesc = RequestUtil.getParam(request, "C_DESC");// 内容简述
		String fileMd5 = RequestUtil.getParam(request, "C_MDC");// 视频文件MD5码
		String fileExtName = RequestUtil.getParam(request, "C_TYPE");// 视频文件类型 *
		Long fileSize = RequestUtil.getLongParam(request, "C_LEN");// 文件长度
		Long thread = RequestUtil.getLongParam(request, "THREADS");// 上传线程数
		Long clientType = RequestUtil.getLongParam(request, "T_TYPE");// 接入客户端类型
																		// *
		String fileName = RequestUtil.getParam(request, "FILE_NAME"); // 上传文件名*
		String fileCreatetime = RequestUtil.getParam(request,
				"FILE_CREATE_TIME"); // 文件创建时间*

		log.debug(" , fileTitle:" + fileTitle + " , fileDesc:" + fileDesc
				+ " , fileMd5:" + fileMd5 + " , fileType:" + fileExtName
				+ " , fileSize:" + fileSize + " , clientType:" + clientType
				+ " , fileName:" + fileName + " , fileCreatetime:"
				+ fileCreatetime);

		if (fileSize == null || clientType == null || thread == null) {
			return PortalConstants.ERROR_CLIENT_WRONG_PARAMS;// 参数不正确
		}

		if (thread > 1) {
			return PortalConstants.ERROR_CLIENT_UPLOAD_THREAD_TOO_MUCH;// 参数不正确：线程数过大
		}
		// 数据组装
		uploadFile.setProgramTitle(fileTitle);
		uploadFile.setProgramDesc(fileDesc);
		uploadFile.setFileMd5(fileMd5);
		uploadFile.setFileType(fileExtName);
		uploadFile.setFileSize(fileSize);
		uploadFile.setFileName(fileName);
		uploadFile.setFileCreatetime(fileCreatetime);
		uploadFile.setSegmentAmount(thread);
		uploadFile.setCreateTime(currTime);
		return PortalConstants.CORRECT_REQUEST_HANDLED;
	}

	/**
	 * 设置文件路径
	 * 
	 * @param uploadFile
	 *            UgcUploadFile
	 */
	public static void setFilePath(UgcUploadFile uploadFile) {
		if (uploadFile == null || uploadFile.getId() == null) {
			throw new NullPointerException();
		}
		Long id = uploadFile.getId();
		String filePath = new StringBuffer().append("/")
				.append(UgcStringUtil.getFileDirPathById(id))
				.append(id.toString()).append(".").toString();
		uploadFile.setFileLocalName(new StringBuffer().append(filePath)
				.append(uploadFile.getFileType()).toString());
		uploadFile.setFileLocalPropertiesName(new StringBuffer()
				.append(filePath).append("properties").toString());

	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	public static String assembleUploadUrl(HttpServletRequest request) {
		return PortalUtil.getDomain(request) + "/uploadFile/";
	}

	/**
	 * 
	 * @param uploadFile
	 *            UgcUploadFile
	 * @return
	 */
	public static String assembleParams(UgcUploadFile uploadFile) {
		String id = uploadFile.getId().toString();
		String createTime = UgcDateUtil.getDateTime(uploadFile.getCreateTime());
		// 1.抽取uploadFile里的字段（键），以等号链接字段（键）和值，以逗号链接键值对
		StringBuffer sb = new StringBuffer();
		sb.append("fileLocalName=").append(uploadFile.getFileLocalName())
				.append("&");
		sb.append("fileLocalPropertiesName=")
				.append(uploadFile.getFileLocalPropertiesName()).append("&");
		sb.append("fileMd5=").append(uploadFile.getFileMd5()).append("&");
		sb.append("fileSize=").append(uploadFile.getFileSize().toString())
				.append("&");
		sb.append("segmentAmount=")
				.append(uploadFile.getSegmentAmount().toString()).append("&");
		sb.append("fileName=").append(uploadFile.getFileName().toString())
				.append("&");
		sb.append("encode=true");
		String encryptStr = "";// EncryptionUtils.encrypt(getEncryptKey(id,createTime),
								// sb.toString());
		try {
			encryptStr = URLEncoder.encode(encryptStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
			return null;
		}
		sb.append(encryptStr);
		sb.append("&ID=").append(id);
		sb.append("&CREATE_TIME=").append(createTime);
		return sb.toString();

	}

	/**
	 * 
	 * @param uploadFile
	 *            UgcUploadFile
	 * @param path
	 *            String
	 * @return
	 */
	public static int initVideoAndProp(UgcUploadFile uploadFile, String path) {
		int flag = 0;
		try {
			// 创建文件
			String absUploadfilePath = path;
			String localNamePath = absUploadfilePath
					+ uploadFile.getFileLocalName();
			String propNamePath = absUploadfilePath
					+ uploadFile.getFileLocalPropertiesName();
			FileUtil.createNewFile(new File(localNamePath));
			FileUtil.createNewFile(new File(propNamePath));

			// 写入初始化xml内容到属性文件
			Document propertiesDoc = DomUtil.getDocument();
			DomUtil.addElementToDocument(propertiesDoc, "RESULT", null);
			try {
				DomUtil.addElementToElement(propertiesDoc, "RESULT", "LIST",
						null);
				DomUtil.addElementToElement(propertiesDoc, "LIST", "PHASE",
						null);
				Map<String, String> keyValueMap = new HashMap<String, String>();
				keyValueMap.put("ID", "1");
				keyValueMap.put("START", "0");
				keyValueMap.put("END", uploadFile.getFileSize().toString());
				keyValueMap.put("SIZE", uploadFile.getFileSize().toString());
				DomUtil.addAttributeToElement(propertiesDoc, "PHASE",
						keyValueMap);
				DomUtil.addElementToElement(propertiesDoc, "RESULT",
						"CREATE_TIME",
						UgcDateUtil.getDateTime(uploadFile.getCreateTime()));
				FileUtil.writeFileByString(
						absUploadfilePath
								+ uploadFile.getFileLocalPropertiesName(),
						DomUtil.createXmlString(propertiesDoc), "UTF-8");
			} catch (XMLParseException e) {
				flag = -1;
			}
		} catch (Exception e) {
			flag = -1;
		}
		return flag;
	}

}
