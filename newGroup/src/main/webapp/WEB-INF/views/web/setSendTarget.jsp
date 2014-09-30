<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设定发送对象</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/setSendTarget.js"></script>
</head>
<body>
<div class="step"><span><img src="/resources/images/step1.gif">设置公告号基本信息</span> <span class="on"><img src="/resources/images/step2.gif">设定公告对象</span> <span><img src="/resources/images/step3.gif">设定公告管理员</span> <img src="/resources/images/step4.gif">完成</div>
<div class="send">
	<input value="${toRange}"  name="toRange" id="toRange" type="hidden"/>
	<input value="${publicName}"  name="publicName" id="publicName" type="hidden"/>
	<input value="${target}"  name="target" id="target" type="hidden"/>
	<input value="${picture}"  name="picture" id="picture" type="hidden"/>
	<p class="sdgg_p">2.1设定公告对象群组名称：<input name="toName" id ="toName" type="text" class="opcek"></p>
    <p class="sdgg_p comChoice">2.2选择公告对象：</p>
	<div class="clearfix comChoice">
        <div class="f_l">
            <p class="tit hui">可选对象</p>
            <div class="treebg" style="overflow:auto">
            	<div id="targetTree">	
            	</div>
            </div>
            <p class="btn">
                <span class="lis_btn">
                    <a href="#"  onclick="SetSendTarget.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
                </span>
            </p>
        </div>
        <div class="f_r">
            <p class="tit red">已选对象</p>
            <div class="treebg" style="overflow:auto">
                <ul class="yixuan" id="targetMember"></ul>
            </div>
            <p class="btn">
                <span class="lis_btn">
                    <a href="#" onclick="SetSendTarget.toStepThree();"><img src="/resources/images/icon_sure.png">下一步</a>
                </span>
            </p>
        </div>
        

        
    </div>
    <p class="btn textl mt30 othChoice">
        <span class="lis_btn">
        <a href="#"  onclick="SetSendTarget.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
	    </span>
	    <span class="lis_btn mt30">
	        <a href="#" onclick="SetSendTarget.toStepThree();"><img src="/resources/images/icon_sure.png">下一步</a>
	    </span>
	</p>
</div>



</body>

</html>