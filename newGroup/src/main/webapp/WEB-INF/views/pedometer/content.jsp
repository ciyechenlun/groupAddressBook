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
	<h1>欢迎使用安徽移动计步器管理平台！</h1>
	<div class="main">
		<div id="dvUsers">
			<h1></h1>
		</div>
	</div>	
</body>
</html>
