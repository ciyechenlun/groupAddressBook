// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.

package com.cmcc.zysoft.sellmanager.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.cmcc.zysoft.framework.common.support.ResponseData;
import com.cmcc.zysoft.framework.utils.AjaxUtil;

/**
 * @author 李三来
 * <br />邮箱： li.sanlai@ustcinfo.com
 * <br />描述：控制器基础类
 * <br />版本:1.0.0
 * <br />日期： 2013-1-11 下午7:51:03
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
abstract public class BaseController {

	/**
	 * 日志
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 异常消息key
	 */
	public static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";
	
	/**
	 * mapper
	 */
	protected final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 初始化版定
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	/**
	 * 处理异常
	 * @param exception
	 * @param request
	 * @param response
	 */
	@ExceptionHandler()
	public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		//服务端处理失败
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		request.setAttribute(EXCEPTION_MESSAGE, exception);
		
		logger.error(exception.getMessage(), exception);
		
		if(!AjaxUtil.isAjaxRequest(request)) {
			RequestDispatcher rd   =  request.getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp"); 
            try {
				rd.forward(request, response);
			} catch (Exception e) {
				//
			}
		} else {
			ResponseData data = new ResponseData(false, exception.getClass() + ": " + exception.getMessage());
			data.setRequestURI(request.getRequestURI());
			data.setExecptionTrace(ExceptionUtils.getFullStackTrace(exception));
			
			try {
				String json = mapper.writeValueAsString(data);
				response.setContentType("application/json;charset=UTF-8");
				response.getOutputStream().print(json);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}