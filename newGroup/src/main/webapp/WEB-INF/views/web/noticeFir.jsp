<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择公告号</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<script type="text/javascript" src="/resources/js/web/noticeFir.js"></script>
</head>

<body id="bodyDv">
   <div class="step"><span class="on"><img src="/resources/images/step1.gif">选择公告号</span> <span><img src="/resources/images/step2.gif">编辑和预览</span></div>
<ul class="gglist clearfix">
 <c:forEach items="${publicRoads}" var="result">
	<li>
    	<a>
        	<em <c:if test="${result.public_id ne publicId}">class="hide"</c:if> value="${result.public_id}" text="${result.user_company_id}"></em>
            <p >
             <c:choose>
            <c:when test="${result.picture !=''&& result.picture!=null}">
            	<img src="/pc/notice/images/${result.picture}" width="106" height="106" />
				</c:when>
				<c:otherwise>
            			<img src="/resources/images/img1.jpg" width="106" height="106" />
            		</c:otherwise>
				 </c:choose>
            </p>
            ${result.public_name}
        </a>
    </li>
 </c:forEach>
</ul>
<p class="btn">
    <span class="lis_btn">
        <a href="/pc/notice/main.htm"><img src="/resources/images/icon_cancel.png">取消</a>
    </span>
    <span class="lis_btn">
        <a href="javascript:NoticeFir.nextStep()"><img src="/resources/images/icon_sure.png">下一步</a>
    </span>
</p>
</body>
</html>
