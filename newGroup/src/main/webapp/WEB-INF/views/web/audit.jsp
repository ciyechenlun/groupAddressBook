<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录审核设置</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<script type="text/javascript" src="/resources/js/web/audit.js"></script>
</head>

<body>
<div class="bnewright bodybg fn_clear">
	<input type="hidden" id="num" value="${num}"/>
	<table style="padding-top: 20px;">
		<tr>
			<td><input id="modify" name="1" type="radio" value="1" /></td>
			<td>允许自行修改个人信息</td>
		</tr>
		<tr>
			<td><input id="audit" name="1" type="radio" value="2" /></td>
			<td>修改后需要审核</td>
		</tr>
	</table>
<div id="admin_table_btn">
    <ul>
    <li id="ok"><a href="javascript:void(0)" onclick="Audit.saveA()">确定</a></li>
    <li id="cancel"><a href="javascript:void(0)" onclick="Audit.cancelA()">取消</a></li>
    </ul>
    
    </div>
</div>
</body>
</html>
