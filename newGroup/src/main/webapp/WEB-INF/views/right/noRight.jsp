<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
</head>

<body>
<!--body正文-->
 <div class="nowbg">您当前位置 -- 权限管理 -- 暂无权限</div>
        <div class="qxred">权限规则设定/编辑</div>
        <div class="drwj2 fotsm">当前企业没有设定任何权限，请点击设定权限按钮进行设置<span class="lis_btn f_r"><a href="/pc/right/toSetMaster.htm?companyId=${companyId}"><img src="/resources/images/btn_tjbm.png" />设定权限</a></span></div>
</body>
</html>
