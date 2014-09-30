<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="/WEB-INF/tld/spring.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="<%=basePath%>resources/img/favicon.ico" mce_href="<%=basePath%>resources/img/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=basePath%>resources/img/favicon.ico" mce_href="<%=basePath%>resources/img/favicon.ico" type="image/x-icon" />
<title>权限不足</title>
</head>
<body>
	<h1>您的权限不足，无权访问该资源</h1> 
<p> 
 您访问的资源页面被拒绝: <strong>${errorDetails}</strong>. 
</p> 
<em>详细信息:</em><br /> 
<blockquote> 
  <pre>${errorTrace}</pre> 
</blockquote> 
</body>
</html>