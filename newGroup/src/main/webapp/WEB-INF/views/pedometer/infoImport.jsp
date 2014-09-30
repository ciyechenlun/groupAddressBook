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
	<h1>信息导入</h1>
	<div class="main">
		<div class="boxWrap openFile">
	    	<table>
	        	<tr><th><label>选择导入文件：</label></th><td><label><input type="file"/></label></td></tr>
	        	<tr><td></td><td><button class="ckbtn">导入</button></td></tr>
	        </table>
	    </div>
	
		<!--提示-->
		<div class="tips">
	    	<div class="line"></div>
	        <div class="tipsCont">
	            <b>使用说明：</b>
	            <p>1.请将用户资料按模板内格式填写上传，姓名，手机长号，部门，身高，体重为必填。点击下载模板。<br />2.第一次导入较多的信息可能耗时较长，请您耐心等待。<br />3.更新部分用户信息时，建议您仅仅上传本次修改的内容。点击此处下载当前用户资料。<br />4.信息每次上传会替代原本的信息，请在上传前检查您的输入是否完全正确！</p>
	        </div>
	    </div>
		<!--提示 end-->
	</div>
</body>
</html>
