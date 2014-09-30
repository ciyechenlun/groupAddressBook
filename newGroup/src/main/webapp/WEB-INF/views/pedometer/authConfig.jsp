<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<script type="text/javascript" src="/resources/pedometer/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	function myrefresh(){
		window.location.reload();
	}
	
	$(function(){
		function autoSize(){
			var newH = $(window).height() - $("h1").height();
			var newW = $(window).width();
			$(".main").height( newH );
			$(".main").width( newW );
		}
		autoSize();
		
		$(window).on("resize",function(){
			autoSize();
		});
	
	});
</script>
</head>

<body>
	<h1>权限设置</h1>
	<div class="main">
		<div class="setting">
	    	<h2>普通用户</h2>
	        <div class="checkBoxWrap">
	        	<p><input type="checkbox"><label>上1级</label></p>
	        	<p><input type="checkbox"><label>上2级</label></p>
	        	<p><input type="checkbox"><label>上3级</label></p>
	        	<p><input type="checkbox"><label>平级</label></p>
	        	<p><input type="checkbox"><label>下1级</label></p>
	        	<p><input type="checkbox"><label>下2级</label></p>
	        	<p><input type="checkbox"><label>下3级</label></p>
	        </div>
	    </div>
	
		<!--提示-->
		<div class="tips">
	    	<div class="line"></div>
	        <div class="tipsCont">
	            <b>权限设置说明：</b>
	            <p>1.查询权限，决定用户能够查询、查看到的组织架构的层级。<br/>2.系统管理员由业务开通时中国移动生成。</p>
	        </div>
	    </div>
		<!--提示 end-->
	</div>
</body>
</html>
