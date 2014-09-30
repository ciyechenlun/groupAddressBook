/** ~ CopyRight © 2012 China Mobile Group Anhui CO.,LTD All Rights Reserved. */
package com.cmcc.zysoft.HttpClientUtil;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;

/**
 * 
* @ClassName: HttpClientUtil
* @author shanxiusheng
* @date 2013-2-19 上午11:36:57
*
 */
public class HttpClientUtil {
	
	/**
	 * 默认构造方法.
	 */
	public HttpClientUtil() {
	}
	
	/**
	 * 客户端请求url.
	 * @param url 传入url参数
	 * @return String 返回请求信息
	 * @throws HttpException 客户端请求异常
	 */
	public static String get(String url) throws HttpException {
		HttpClient httpClient = new HttpClient();
		//httpClient.getHostConfiguration().setProxy("proxy.ah.cmcc", 8080);
		//httpClient.getParams().setAuthenticationPreemptive(true);
		//创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);
		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		    new DefaultHttpMethodRetryHandler());
		String result = null;//初始化返回结果（String类型）
		byte[] responseBody = null;//初始化返回结果（byte[]类型）
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
		    if (statusCode != HttpStatus.SC_OK) {
			    System.err.println("Method failed: "
			      + postMethod.getStatusLine());
		    }
		    //返回结果（byte[]类型） 
		    responseBody = postMethod.getResponseBody();
		    //返回结果（String类型，GBK格式）
		    result = new String(responseBody,"GBK");
		} catch (IOException e) {
		   e.printStackTrace();
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 客户端请求url.
	 * @param url 传入url参数
	 * @param connectionTimeoutTimers：连接超时时间
	 * @param mobile 短信发送的手机号
	 * @return String 返回请求信息
	 * @throws HttpException 客户端请求异常
	 */
	public static String get(String url,int connectionTimeoutTimers,String mobile) throws HttpException {
		HttpClient httpClient = new HttpClient();
//		httpClient.getHostConfiguration().setProxy("proxy.ah.cmcc", 8080);
//		httpClient.getParams().setAuthenticationPreemptive(true);
		//创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);
		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		    new DefaultHttpMethodRetryHandler());
		postMethod.setParameter("phone",mobile);
		String result = null;//初始化返回结果（String类型）
		byte[] responseBody = null;//初始化返回结果（byte[]类型）
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
		    if (statusCode != HttpStatus.SC_OK) {
			    System.err.println("Method failed: "
			      + postMethod.getStatusLine());
		    }
		    //返回结果（byte[]类型） 
		    responseBody = postMethod.getResponseBody();
		    //返回结果（String类型，GBK格式）
		    result = new String(responseBody,"GBK");
		} catch (IOException e) {
		   e.printStackTrace();
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		return result;
	}
	public static String post(String url,String mobiles,String msg,String retry) throws HttpException {
		HttpClient httpClient = new HttpClient();
		//httpClient.getHostConfiguration().setProxy("proxy.ah.cmcc", 8080);
		//httpClient.getParams().setAuthenticationPreemptive(true);
		//创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);
		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		    new DefaultHttpMethodRetryHandler());
		 postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		postMethod.setParameter("user",mobiles);
		postMethod.setParameter("msg",msg);
		//是否为重发：0：首次发送，1:对失败的重新发送
		postMethod.setParameter("retry",retry);
		String result = null;//初始化返回结果（String类型）
		byte[] responseBody = null;//初始化返回结果（byte[]类型）
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
		    if (statusCode != HttpStatus.SC_OK) {
			    System.err.println("Method failed: "
			      + postMethod.getStatusLine());
		    }
		    //返回结果（byte[]类型） 
		    responseBody = postMethod.getResponseBody();
		    //返回结果（String类型，GBK格式）
		    result = new String(responseBody,"GBK");
		} catch (IOException e) {
		   e.printStackTrace();
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		return result;
	}
	public static String postForShort(String url,String mobile)throws HttpException {
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setProxy("proxy.ah.cmcc", 8080);
		httpClient.getParams().setAuthenticationPreemptive(true); 
		//创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);
		//使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		    new DefaultHttpMethodRetryHandler());
		 postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
		postMethod.setParameter("mobile",mobile);
		String result = null;//初始化返回结果（String类型）
		byte[] responseBody = null;//初始化返回结果（byte[]类型）
		try {
			//执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
		    if (statusCode != HttpStatus.SC_OK) {
			    System.err.println("Method failed: "
			      + postMethod.getStatusLine());
		    }
		    //返回结果（byte[]类型） 
		    responseBody = postMethod.getResponseBody();
		    //返回结果（String类型，GBK格式）
		    result = new String(responseBody,"GBK");
		} catch (IOException e) {
		   e.printStackTrace();
		} finally {
			//释放连接
			postMethod.releaseConnection();
		}
		return result;
	}
}
