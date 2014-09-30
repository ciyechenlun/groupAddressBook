<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/pedometer/pedometer.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
</head>

<body>
	<form name="fmMedal" id="fmMedal" enctype="multipart/form-data"  action="" method="post">
		<input type="hidden" name="medalSysId" id="medalSysId" value="${medalSys.medalSysId }" />
		<table class="gridTable" style="width:100%;" cellpadding="0" cellspacing="0">
	       <tr>
	       	  <th colspan="3">勋章系统${typeName }</th>
	       </tr>
	       <tr>
	       		<td style="width:30%">勋章系统名称</td>
	       		<td style="width:160px;">
	       			<input value="${medalSys.medalSysName }" id="medalSysName" name="medalSysName" type="text" style="width:160px;" class="bm_input"/>
	       		</td>
	       		<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       	<td>勋章图片</td>
	       	<td>
	       		<input id="medalPicture"  name="medalPicture" type="file" style="width:160px;" class="bm_input"/>
	       	</td>
	       	<td>
	       		<span style="color:#CCCCCC">
	       			请将勋章图片以zip格式上传
	       		</span>
	       	</td>
	       </tr>
	       <tr>
	       		<td colspan="3" style="text-align:center">
	       			<button style="width:100px;" class="cxbtn" onclick="return medal.saveMedal('${type}')">提交</button>
	       			<button class="cxbtn" style="width:100px;" onclick="medal.cancel()">取消</button>
	       		</td>
	       </tr>
		</table>
	</form>
</body>
</html>
