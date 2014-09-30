<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录-角色权限设置</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/right/edit.js"></script>
<style type="text/css">
	.navBar{
		padding: 5px;
		border-bottom: solid 1px silver;
	}
	.rightContainer{
		padding:10px;
		border:solid 1px #CCCCCC;
		width:100%;
		margin:10px;
		height:80px;
	}
	.dvset{
		float:right;
		right:5px;
		cursor:pointer;
		padding:5px;
	}
	.dvedit{
		padding:10px;
		float:right;
		cursor:pointer;
	}
	.dvcont{padding:5px;}
</style>
<script language="JavaScript">
<!--
	function winShow(id){
		var w=Hg.get(id),b=document.body, cw=b.clientWidth,ch=b.scrollTop + 120;
		$(w).css('left',(cw -500)/2).css('top',parseInt(ch)).css('display','block')
	}
	function winHide(id){
		var w=Hg.get(id);
		w.style.display="none";
	}
	// onload事件里添加代码
	window.onload=function(){
		tabView("tab_head","tab_body","on","onclick");		
	}

-->
</script>
</head>

<body>



<!--body正文-->
<div class="bodyqj bodybg fn_clear" style="height:500px">
	<div class="ck_right1">
		<div class="ck_rightbg">
			“${master.masterName }”权限设置
		</div>
		<div id="dvButton" class="margintop" style="width:100%;text-align:right;">
			<input id="updatebtn" name="" type="button" class="bottom_01" value="添加条件组" onclick="addCondition('${master.masterId}');"/>
			<input id="backbtn" name="" type="button" class="bottom_01" value="返回" onclick="location.href='master_list.htm'"/>
		</div>
		<div class="contentDiv" id="mbImportDiv" style="padding:5px;">
			<c:set var="index" value="1"></c:set>
			<c:forEach items="${pagination.result}" var="result">
				<div class="rightContainer" id="conditions${index }">
					<div class="dvset" id="delete1" onclick="delCondition(${index},'${result.rules_id }')">
						<img src="/resources/images/icon_delete.gif" style="padding:4px;width:16px;height:16px;" alt="删除" />
					</div>
					<div class="dvedit" id="edit1" onclick="editCondition('${result.rules_id}')">
						<img style="width:16px;height:16px;" src="/resources/images/icon_top01.png" alt="编辑" />
					</div>
					${result.mark1 }  条件${index }：
					<div class="dvcont" id="dvContent${index }">
						<table width="100%" border="0" cellpadding="1" cellspacing="1" id="tab${index }">
							<tbody></tbody>
						</table>
					</div>
					<script type="text/javascript">
						revertJson('${result.rules_content}',${index});
					</script>
				</div>
				<c:set var="index" value="${index+1 }"></c:set>
			 </c:forEach>
		</div>
		
		
	</div>
</div>
</body>
</html>
