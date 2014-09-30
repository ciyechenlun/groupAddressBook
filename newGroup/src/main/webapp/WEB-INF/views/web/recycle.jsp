<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<script type="text/javascript" src="/resources/js/web/recycle.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script language="JavaScript">
	// onload事件里添加代码
	window.onload = function() {
		tabView("tab_head", "tab_body", "on", "onclick");
	};

	$(function() {
		//如果不是s_admin&&查看的是企业组织，提示没有权限
		if('${org_flag}' == '1' && '${manager}' != '1')
		{
			$("#dvCover").show();
			$.messager.alert('提示','你没有权限执行此操作','info',function(){history.back();});
			
		}else{
			var companyType = window.parent.document.getElementById("companyType").value;
			if (companyType == "bnewleft01 bnewleft01a liGetSelected"
					|| companyType == "bnewleft01 bnewleft01a bnewlefton liGetSelected") {
				$("#tree").show();
			}
		}
		
	});
</script>
</head>

<body>
	<div id="dvCover" style="display:none;width:785px;height:500px;backgroud-color:#CCCCCC;">
	</div>
	<input id="companyId" name="companyId" type="hidden" />

	<!--body正文-->
	<div class="bnewright ">
	<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
		<tr>
			<td >
			<!--树-->
			<div id="tree" class="ck_left" style="overflow: auto; display: none;">
				<div class="margintop">
					<ul id="recycle_department"></ul>
				</div>
			</div>
			<!--树end-->
			</td>
			<td >
			<div style="width:620px;height:420px">
			  <iframe name="recycleFrame" id="recycleFrame" src="/pc/recycle/getDelElement.htm?companyId=${companyId}" width="100%" height="100%" frameborder="0" scrolling="no"/>
			  </div>
			</td>
		</tr>
	</table>
	</div>

</body>
</html>
