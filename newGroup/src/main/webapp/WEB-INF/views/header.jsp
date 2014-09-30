<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link href="/resources/js/common/loadmask/jquery.loadmask.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/js/common/loadmask/jquery.loadmask.min.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<div class="header">
	<div class="logo">
		<c:choose>
			<c:when test="${empty SESSION_COMPANY_LOGO or SESSION_COMPANY_LOGO=='' or SESSION_COMPANY_LOGO=='ERROR'}">
				<img src="/resources/img/logo.png" />
			</c:when>
			<c:otherwise>
				<img width="300px" height="75px" src="/pc/company/images/${SESSION_COMPANY_LOGO}" />
			</c:otherwise>
		</c:choose>
	</div>
	<div class="topMenu" id="topMenu">
		<img src="/resources/img/loading.gif"/>
	</div>
	<div class="topLink">
		<ul>
			<li><font style="font-weight: bolder;">${sessionScope.SESSION_USER_NAME}</font>&#12288;欢迎您！</li>
			<li><a href="javascript:Header.changePwd();">修改密码</a></li>
			<li><a href="javascript:Header.logout();">安全退出</a></li>
		</ul>
	</div>
</div>

