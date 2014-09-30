<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/starit.base.css"/>
<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>
<script type="text/javascript" src="/resources/js/index/index.js"></script>

</head>
<body>
<div id="indexRightDivs" class="bnewright">
	<c:if test="${company!=null }">
		<b>当前公司：${company.companyName }</b>
	    <div class="margintop30">
	      	  <table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
		        <tr>
	      		<c:if test="${manager=='1' }">
		        	<td><img src="/resources/images/icon_r01.png" width="176" height="85" onclick="Index.imports();"/></td>
	      		</c:if>
		            <td><img src="/resources/images/icon_r02.png" width="176" height="85" onclick="Index.lookGroup();"/></td>
		        <c:if test="${manager=='1' }">
		            <td><img src="/resources/images/icon_r02_clear.png" width="176" height="85" onclick="Index.clearGroup();"/></td>
		        </c:if>
		        </tr>
		      </table>
	    </div>
    </c:if>
	
    <div class="margintop30"> <span class="bnewzi">使用说明</span>
      <ul class="bnewzih">
        <li>1.查看通讯录允许您按组织架构查询联系人信息，系统管理员可以在通讯录内设置部门管理员和修改联系人信息。</li>
        <li>2.导入信息时请保持列表表头（列名）统一。或者按照模板填写信息。</li>
        <li>3.单个联系人信息修改立即生效。组织架构修改需确认后才可生效。</li>
        <li>4.请在左侧选择企业进行管理</li>
      </ul>
    </div>
</div>
</body>
</html>
