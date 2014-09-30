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
<script type="text/javascript" src="/resources/js/web/noticeSec.js"></script>
<script charset="utf-8" type="text/javascript" src="/resources/scripts/kindeditor/kindeditor-min.js"></script>
<script charset="utf-8" type="text/javascript" src="/resources/scripts/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="/resources/scripts/dateformat.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/jquery.mCustomScrollbar.css" />
<!-- the mousewheel plugin -->
<script type="text/javascript" src="/resources/scripts/jquery.mCustomScrollbar.concat.min.js"></script>
<style type="text/css">
.title{font-size:1.0em;font-weight:bold}
.time{font-size:0.9em;margin-top:10px;margin-bottom:10px}
	img,image{max-width:100%}
	p,h1,h2,h3,strong,li{font-size:1.0em}
</style>
</head>

<body id="bodyDv">
<div id="noticeFir">
 <div class="step"><span class="on"><img src="/resources/images/step1.gif">选择公告号</span> <span><img src="/resources/images/step2.gif">编辑和预览</span></div>
<ul class="gglist clearfix">
 <c:forEach items="${publicRoads}" var="result">
	<li>
    	<a>
        	<em <c:if test="${result.public_id ne publicId}">class="hide"</c:if> value="${result.public_id}" text="${result.user_company_id}"></em>
            <p>
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
        <a href="/pc/notice/main.htm"><img src="/resources/images/icon_cancel.png"/>取消</a>
    </span>
    <span class="lis_btn">
        <a href="javascript:NoticeSec.nextStep()"><img src="/resources/images/icon_sure.png"/>下一步</a>
    </span>
</p>
</div>
<div id="noticeSec" style="display:none">
   <div class="step"><span><img src="/resources/images/step1.gif" />选择公告号</span> <span class="on"><img src="/resources/images/step2.gif" />编辑和预览</span></div>
<div class="edit clearfix">
	<div class="f_l">
		<form id="notice_form" method="post">
		<input id="publicId" name="publicId" type="hidden" value="${publicId}"/>
		<input id="noticeId" name="noticeId" type="hidden" value="${noticeId}"/>
		<input id="userCompanyId" name="userCompanyId" type="hidden" value="${userCompanyId}"/>
    	<p class="sdgg_p">标题：<input id="noticeTitle" name="noticeTitle" value="${noticeTitle==null?'请输入标题':noticeTitle}" type="text" class="opcek" onblur="NoticeSec.onBlur();" onfocus="NoticeSec.onfocus();"/></p>
        <textarea style="width: 400px;height:400px;" id="noticeContent"  name="noticeContent">${noticeContent}</textarea>
        <p class="btn mt10">
            <span class="lis_btn">
                <a href="/pc/notice/main.htm"><img src="/resources/images/icon_cancel.png" />取消</a>
            </span>
            <span class="lis_btn">
                <a href="javascript:NoticeSec.preStep()"><img src="/resources/images/icon_goback.png" />上一步</a>
            </span>
            <span class="lis_btn">
                <a href="javascript:NoticeSec.sendMsg()"><img src="/resources/images/icon_sure.png" />发送</a>
            </span>
            <span class="lis_btn">
                <a href="javascript:NoticeSec.saveDraft()"><img src="/resources/images/icon_sure.png" />暂存</a>
            </span>
        </p>
        </form>
    </div>
  <div class="f_r">
    	<p>预览公告</p>
        <select class="selecttext mt10" onchange="NoticeSec.show(this.value)">
           <!--  <option value="0">选择预览机型</option> -->
            <option value="1">高：S4-1920*1080</option>
            <option value="2">中：S3-1280*720</option>
            <option value="3">低：9180-800*480</option>
        </select>
	   <!--  <div class="mt10">
		    <div class="back">
		    <div  class="preview">
		    </div>
		    </div>
		    
	    </div> -->
	     <div class="mt10 phone" style="display:none;">
       	  <div class="phonebg">
          	<div id="s1" class="phonetext">
            	<p class="title">${noticeTitle}</p>
          		<p class="time">${sendTime}</p>
            	<div class="content">
            		${noticeContent}
            	</div>
            </div>
            </div>
        </div>
        <div class="mt10 phone2" style="display:none;">
       	  <div class="phonebg">
          	<div id="s2" class="phonetext">
            	<p class="title">${noticeTitle}</p>
          		<p class="time">${sendTime}</p>
            	<div class="content">
            		${noticeContent}
            	</div>
            </div>
            </div>
        </div>
        <div class="mt10 phone4" >
       	  <div  class="phonebg">
          	<div id="s4" class="phonetext">
          		<p class="title">${noticeTitle}</p>
          		<p class="time">${sendTime}</p>
          		<div class="content">
            		${noticeContent}
            	</div>
            </div>
            </div>
        </div>
        <p class="mt10" id="phone_txt">当前选择机型：S4-1920*1080</p>
    </div>
</div>
</div>

</body>
</html>
