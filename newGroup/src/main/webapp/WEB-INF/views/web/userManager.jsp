<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成员管理</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/userManager.js"></script>
<style type="text/css">
</style>
</head>
<body>
<div class="send clearfix">
    <div class="f_l">
        <p class="tit hui">可选对象</p>
        <div class="treebg" style="overflow:auto">
        	<div id="userTree"></div>
        </div>
        <p class="btn">
            <span class="lis_btn">
                <a href="#" onclick="SetManager.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
            </span>
        </p>
    </div>
    <div class="f_r">
        <p class="tit red">禁用对象</p>
        <div class="treebg" style="overflow:auto">
            <ul class="yixuan" id="forbiddenMember">
            	 <c:forEach items="${forbiddenMember}" var="searchResult">
	                		<li id="${searchResult.id }"> 
	                			<a href="#" onclick="UserManager.delManagerMemberImg('${searchResult.id }');"> 
	                			<img src="/resources/images/icon_cancel.png"/></a>${searchResult.name}</li>
	              </c:forEach>
            </ul>
        </div>
        <p class="btn">
            <span class="lis_btn">
                <a href="#" onclick="UserManager.saveInfo();"><img src="/resources/images/icon_sure.png">保存</a>
            </span>
        </p>
    </div>
</div>



</body>
</html>