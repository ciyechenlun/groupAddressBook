<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑公告号</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/editPublicRoad.js"></script>
<style type="text/css">
.file {
    height: 40px;
    left: 485px;
    opacity: 0;
    position: absolute;
    top: 84px;
    width: 150px;
}
.cover_div{display:none;position: absolute; top:75px; left:20px; width: 435px;height: 391px;background-color: white; z-index:1002;  -moz-opacity: 0.7;opacity:.20; filter: alpha(opacity=70);overflow: auto;}
.cover_div2{display:none;position: absolute; top:75px; left:620px; width: 435px;height: 391px;background-color: white; z-index:1002;  -moz-opacity: 0.7;opacity:.20; filter: alpha(opacity=70);overflow: auto;}
</style>
</head>

<body>
<form id="comp_fm" method="post" enctype="multipart/form-data" autocomplete = "off" > 
	<input value="${publicId}"  name="publicId" id="publicId" type="hidden"/>
	<input value="${toRange}"  name="toRange" id="toRange" type="hidden"/>
	<input value="${target}"  name="target" id="target" type="hidden"/>
	<input value="${manager}"  name="manager" id="manager" type="hidden"/>
<div id="step1">
	<div class="step"><span class="on"><img src="/resources/images/step1.gif">设置公告号基本信息</span> <span><img src="/resources/images/step2.gif">设定公告对象</span> <span><img src="/resources/images/step3.gif">设定公告管理员</span> <img src="/resources/images/step4.gif">完成</div>
	<div class="send">
	  <p class="sdgg_p">名称：<input id="publicName" name="publicName" value="${publicName}" type="text" class="opcek" onblur="EditPublicRoad.onBlur();" onfocus="EditPublicRoad.onfocus();" maxlength="8"></p>
	   <p class="sdgg_p">头像：<input id="picture" name="picture" readOnly value="${picture}" type="text" class="opcek">
	   <input id="logo" name="logo" type="file" class="file" onchange="document.getElementById('picture').value=this.value"/>
	    <a href="#"><span class="opbtns"><img src="/resources/images/icon_save.png">选择图片</span></a></p>
	  
	  <p class="sdgg_p">
	  	发送范围：<input type="radio" name="toRangeCheck" value="0" id="allCompany"/> 全企业   
	  			 <input type="radio" name="toRangeCheck" value="1" id="selDepartment"/> 自选部门 
	  	         <input type="radio" name="toRangeCheck" value="2" id="selEmployee"/> 自选成员
	  </p>
	 <p class="btn textl mt30">
	    <span class="lis_btn">
	        <a href="#"  onclick="EditPublicRoad.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
	    </span>
	    <span class="lis_btn">
	        <a href="#" onclick="EditPublicRoad.toStepTwo();"><img src="/resources/images/icon_sure.png">下一步</a>
	    </span>
	</p>
	</div>
</div>
<div id="step2">
	<div class="step"><span><img src="/resources/images/step1.gif">设置公告号基本信息</span> <span class="on"><img src="/resources/images/step2.gif">设定公告对象</span> <span><img src="/resources/images/step3.gif">设定公告管理员</span> <img src="/resources/images/step4.gif">完成</div>
	<div class="send">
		<p class="sdgg_p">2.1设定公告对象群组名称：<input name="toName" id ="toName" type="text" class="opcek" value="${toName}"></p>
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
	                    <a href="#"  onclick="EditPublicRoad.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
	                </span>
	            </p>
	        </div>
	        <div class="f_r">
	            <p class="tit red">已选对象</p>
	            <div class="treebg" style="overflow:auto">
	                <ul class="yixuan" id="targetMember">
	                	<c:forEach items="${targetMap}" var="searchResult" varStatus="status">
	                		<li id="${searchResult.id }">
	                			<a href="#" onclick="EditPublicRoad.delMemberImg('${searchResult.id }');"> 
	                			<img src="/resources/images/icon_cancel.png"/></a>${searchResult.name} </li>
	                	</c:forEach>
	                </ul>
	            </div>
	            <p class="btn">
	                <span class="lis_btn">
	                    <a href="#" onclick="EditPublicRoad.toStepThree();"><img src="/resources/images/icon_sure.png">下一步</a>
	                </span>
	            </p>
	        </div>
	    </div>
	    <p class="btn textl mt30 othChoice">
	        <span class="lis_btn">
	        <a href="#"  onclick="EditPublicRoad.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
		    </span>
		    <span class="lis_btn mt30">
		        <a href="#" onclick="EditPublicRoad.toStepThree();"><img src="/resources/images/icon_sure.png">下一步</a>
		    </span>
		</p>
	</div>
</div>

<div id="step3">
	<div class="step"><span><img src="/resources/images/step1.gif">设置公告号基本信息</span> <span><img src="/resources/images/step2.gif">设定公告对象</span> <span class="on"><img src="/resources/images/step3.gif">设定公告管理员</span> <img src="/resources/images/step4.gif">完成</div>
	<div class="send clearfix">
	    <div class="f_l">
	    	<div class="cover_div" > </div>
	        <p class="tit hui">可选对象</p>
	        <div class="treebg" style="overflow:auto">
	        	<div id="managerTree"></div>
	        </div>
	        <p class="btn">
	            <span class="lis_btn">
	                <a href="#" onclick="EditPublicRoad.cancle();"><img src="/resources/images/icon_cancel.png">取消</a>
	            </span>
	        </p>
	    </div>
	    <div class="f_r">
	    	<div class="cover_div2" > </div>
	        <p class="tit red">已选对象</p>
	        <div class="treebg" style="overflow:auto">
	            <ul class="yixuan" id="managerMember">
	          	 <c:forEach items="${managerMap}" var="searchResult" varStatus="status">
	                		<li id="${searchResult.id }"> 
	                			<a href="#" onclick="EditPublicRoad.delManagerMemberImg('${searchResult.id }');"> 
	                			<img src="/resources/images/icon_cancel.png"/></a>${searchResult.name}</li>
	              </c:forEach>
	            </ul>
	        </div>
	        <p class="btn">
	            <span class="lis_btn">
	                <a href="#" onclick="EditPublicRoad.complete();"><img src="/resources/images/icon_sure.png">完成</a>
	            </span>
	        </p>
	    </div>
	</div>
</div>


</form>
</body>
</html>