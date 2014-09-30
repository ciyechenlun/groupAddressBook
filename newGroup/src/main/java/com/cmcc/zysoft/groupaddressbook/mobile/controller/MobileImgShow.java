package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.util.UnZip;
import com.cmcc.zysoft.sellmanager.common.BaseController;

/**
 * @author 闫豆
 * @mail yan.dou@ustcinfo.com
 * @date 2013-1-28 上午10:44:19
 */
@Controller
@RequestMapping("/mobile")
public class MobileImgShow extends BaseController {

	@Value("${upload.mobile.img.path}")
	private String uploadPath;
	
	@Value("${project.mobile.img.path}")
	private String projectPath;

	private static Logger _logger = LoggerFactory
			.getLogger(MobileImgShow.class);

	/**
	 * 手机侧查看图片.
	 * @param fileCode 
	 * @param suffix 
	 * @param request 
	 * @return 
	 * @throws IOException  
	 * 返回类型：String
	 */
	@RequestMapping(value="/image/{fileCode}.{suffix}")
	@ResponseBody
	public String showImageToPhone(@PathVariable String fileCode,
			@PathVariable String suffix,String zipName,HttpServletRequest request) throws IOException{
		if(zipName==null||!StringUtils.hasText(zipName)){
			File file = null;
			file = new File(uploadPath + File.separator + fileCode + "."
					+ suffix);
			File destFile = new File("/opt/application/jttxl/tomcat/webapps/ROOT/resources/mobileImg/"+ fileCode + "."
					+ suffix);
			/*File destFile = new File(projectPath+ File.separator + fileCode + "."
					+ suffix);*/
			_logger.info("服务器上图片拷贝到工程下面");
			if(!destFile.exists())
				FileUtils.copyFile(file, destFile);
			return "resources/mobileImg/"+ fileCode + "." + suffix;
		}else{
			String name = zipName.substring(0,zipName.lastIndexOf("."));
			File file = null;
			file = new File(uploadPath + File.separator + name+ File.separator+fileCode + "."
					+ suffix);
			if(!file.canRead()){
				UnZip.unzipFile(uploadPath +File.separator+ zipName, uploadPath +File.separator+ name);
			}
	//		File destFile = new File("/opt/application/jttxl/tomcat/webapps/ROOT/resources/mobileImg/"+ fileCode + "."
	//				+ suffix);
			File destFile = new File(projectPath+ File.separator+ name+File.separator +fileCode + "."
					+ suffix);
			_logger.info("服务器上图片拷贝到工程下面");
			if(!destFile.exists())
				FileUtils.copyFile(file, destFile);
			return "resources/mobileImg/"+ name+"/"+fileCode + "." + suffix;
		}
	}
	
	
	
}
