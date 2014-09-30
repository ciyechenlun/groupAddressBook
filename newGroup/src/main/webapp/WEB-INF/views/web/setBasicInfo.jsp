<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设定基本信息</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/setRoadBasicInfo.js"></script>
<style type="text/css">
.file {
    height: 40px;
    left: 485px;
    opacity: 0;
    position: absolute;
    top: 84px;
    width: 150px;
}
</style>
</head>

<body>
<form id="comp_fm" method="post" enctype="multipart/form-data" autocomplete = "off" > 
<div class="step"><span class="on"><img src="/resources/images/step1.gif">设置公告号基本信息</span> <span><img src="/resources/images/step2.gif">设定公告对象</span> <span><img src="/resources/images/step3.gif">设定公告管理员</span> <img src="/resources/images/step4.gif">完成</div>
<div class="send">
  <p class="sdgg_p">名称：<input id="publicName" name="publicName" value="输入公告名称，不超过8个字" type="text" class="opcek" onblur="SetRoadBasicInfo.onBlur();" onfocus="SetRoadBasicInfo.onfocus();" maxlength="8"></p>
   <p class="sdgg_p">头像：<input id="picture" name="picture" readOnly value="" type="text" class="opcek">
   <input id="logo" name="logo" type="file" class="file" onchange="document.getElementById('picture').value=this.value"/>
    <a href="#"><span class="opbtns"><img src="/resources/images/icon_save.png">选择图片</span></a></p>
  
  <p class="sdgg_p">
  	发送范围：<input type="radio" name="toRange" value="0" checked="checked"/> 全企业   
  			 <input type="radio" name="toRange" value="1" /> 自选部门 
  	         <input type="radio" name="toRange" value="2" /> 自选成员
  </p>
  
 <p class="btn textl mt30">
    <span class="lis_btn">
        <a href="#"  onclick="SetRoadBasicInfo.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
    </span>
    <span class="lis_btn">
        <a href="#" onclick="SetRoadBasicInfo.toStepTwo();"><img src="/resources/images/icon_sure.png">下一步</a>
    </span>
</p>

	
</div>
</form>
</body>
</html>