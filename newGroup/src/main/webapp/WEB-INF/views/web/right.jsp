<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录权限设置</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/baseIncludeJs.jsp" %>
<script type="text/javascript" src="/resources/js/web/right.js"></script>
<script type="text/javascript">
<!--

$(function(){
	$("p").addClass("selected");
	$(".bnewleft01").mouseover(function(){
		$(this).addClass("bnewlefton");
	});
	$(".bnewleft01").mouseout(function(){
		$(this).removeClass("bnewlefton");
	});
});
//-->
</script>
</head>

<body>
<!--top-->
<%@include file="../top.jsp" %>

<!--body-->
<div class="bodyqj newbody">
    <ul>
        <li>集团通讯录</li>
    </ul>
</div>

<!--body正文-->
<div class="bodyqj bodybg fn_clear">
<!--left-->
<%@include file="../left.jsp" %>

<!--right-->
<div class="bnewright">
 <div class="margintop30">
  	    <span class="bnewzi">权限设置说明：</span>
        <ul class="bnewzih">
            <li>1.查询权限，决定用户能够查询到的组织架构的层级。</li>
            <li>2.系统管理员由业务开通时中国移动生成。</li>
        </ul>
    </div>
 <div class="ck_rightbg margintop30">普通用户</div>
    <div class="margintop30 marginbottom">
        <table class="qx_table" align="center">
          <tr>
            <td>
            <table align="left" >
            	<tr>
            		<td width="7%"></td>
            		<td width="20%" height="29" align="left" class="qx_tablewzLeft">查询权限：</td>
              	</tr>
              		<c:forEach items="${list2}" var="right" varStatus="i">
              	<tr align="right">
	            	<td width="7%"><input id="${right.configId}" name="${right.configId}" type="checkbox" value="${right.configId}" <c:if test="${right.rightCheck==1}">checked="checked"</c:if>></input></td>
                	<td width="20%" class="zw_zi1">${right.configName}</td>
              	</tr>
	            	</c:forEach>
	            	<c:forEach items="${list1}" var="right1" varStatus="i">
              	<tr align="right">
	            	<td width="7%"><input id="${right1.configId}" name="${right1.configId}" type="checkbox" value="${right1.configId}" <c:if test="${right1.rightCheck==1}">checked="checked"</c:if>></input></td>
                	<td width="20%" class="zw_zi1">${right1.configName}</td>
              	</tr>
	            	</c:forEach>
	            	<c:forEach items="${list0}" var="right0" varStatus="i">
              	<tr align="right">
	            	<td width="7%"><input id="${right0.configId}" name="${right0.configId}" type="checkbox" value="${right0.configId}" <c:if test="${right0.rightCheck==1}">checked="checked"</c:if>></input></td>
                	<td width="20%" class="zw_zi1">${right0.configName}</td>
              	</tr>
	            	</c:forEach>
             </table>

            </td>
          </tr>
          <tr>
            <td colspan="2">&nbsp;</td>
          </tr>
        </table>
<table width="40%" border="0" cellspacing="0" cellpadding="0" align="center" class=" margintop30">
  <tr>
    <td><input name="" type="button" class="bottom_01" value="确认" onclick="Right.save();"/></td>
    <td><input name="" type="button" class="bottom_01" value="取消" onclick="Right.cancel();"/></td>
  </tr>
</table>


    </div>
   
</div>
</div>
<!--bottom-->
<div class="bottombg">
<ul>
<li>Copyright © 2013-2015  安徽移动通信 All Rights Reserved 版权所有   维护电话：0551-66666666</li>
</ul>
</div>
</body>
</html>
