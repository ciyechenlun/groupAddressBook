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
<script type="text/javascript" src="/resources/js/pedometer/medal.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
</head>

<body>
	<div id="medalDialog"></div>
	<h1>${ms.medalSysName }勋章列表</h1>
	<div class="rightContMain">
	    <div class="searchBar">
            <div>
            	<table cellpadding="0" cellspacing="0" >
                	<tr>
                    	<td class="w80"><button class="cxbtn" onclick="medal.btnAddMedal_OnClick('add','')"> 添加勋章 </button></td>
                    	<td class="w80"><button class="cxbtn" onclick="medal.btnEditMedal_OnClick()"> 提交修改 </button></td>
                    	<td class="w80"><button class="cxbtn" onclick="window.location.href='medal.htm'"> 返回 </button></td>
                    </tr>
                </table>
            </div>
	    </div>
	    
	    <p class="line"></p>
	    
	    <div class="gridWrap">
	        <div class="gridHead"><b>勋章系统</b></div>
	        <div class="gridContStaticHeight">
	            <table class="gridTable" style="width:100%;" cellpadding="0" cellspacing="0">
	                <tr>
	                	<th>勋章名称</th>
	                	<th>级别(步数/里程)</th>
	                	<th>图片</th>
	                	<th>设置</th>
	                </tr>
	                <form name="fmMedal" id="fmMedal" method="post">
	                <input type="hidden" id="medal_sys_id" name="medal_sys_id" value="${ms.medalSysId }" />
	                <c:forEach var="searchResult" varStatus="status" items="${list }">
	                	<tr>
	                		<td>
	                			<input type="hidden" name="medalId" id="medalId" value="${searchResult.medal_id }" />
		                		<input type="text" id="medalName" name="medalName"
		                		 value="${searchResult.medal_name }"  style="width:160px;" class="bm_input" />
		                	</td>
		                	<td>
		                		<input type="text" name="medalLevel" id="medalLevel"
		                		 style="width:160px;" class="bm_input" value="${searchResult.medal_level }" />
		                	</td>
		                	<td><img width="80" height="80" src="/resources/medal/${searchResult.medal_sys_id }/${searchResult.medal_picture}" /></td>
		                	<td>
		                		删除
							</td>
	                	</tr>
	                </c:forEach>
	                </form>
	            </table>
	        </div>
	      </div>
	</div>
</body>
</html>
