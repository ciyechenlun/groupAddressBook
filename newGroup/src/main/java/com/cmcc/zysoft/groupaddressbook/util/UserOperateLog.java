package com.cmcc.zysoft.groupaddressbook.util;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cmcc.zysoft.groupaddressbook.controller.IndexController;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

public class UserOperateLog  implements
		HandlerInterceptor {
	/**
	 * 日志.
	 */
	private static Logger logger = Logger.getLogger("useroperatorlog"); 

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String logStr ="";
		User user = SecurityContextUtil.getCurrentUser();
	    if(user!=null){
	    	logStr +=user.getUsername()+"-"+request.getRemoteAddr();
	    }else{
	    	return true;
	    }
	    logStr += " 执行操作:"+request.getRequestURI();
		String ctext="";
		Map<String, String[]> map = request.getParameterMap();
		for (String key : map.keySet()) {
				ctext+="&"+key+"=";
			   String[] temp = map.get(key);
			   for(int i=0;i<temp.length;i++){
				   ctext+=temp[i]+" ";
			   }
		}
		if(ctext.length()>900){
			ctext=ctext.substring(1,900);
		}else if(ctext.length()>0){
			ctext=ctext.substring(1);
		}
		logStr += " 参数:"+ctext;
		logger.info(logStr);
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
