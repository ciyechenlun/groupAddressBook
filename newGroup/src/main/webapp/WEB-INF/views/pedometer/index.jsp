<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<link rel="stylesheet" type="text/css" href="/resources/css/css.css"/>
<link rel="stylesheet" type="text/css" href="/resources/scripts/easyui/themes/default/easyui.css"/>
<link href="/resources/pedometer/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/pedometer/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/resources/scripts/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/add.css"/>
<style  type="text/css">
	.leftd {padding-left:25px;color:#000;}
</style>
<script type="text/javascript">
	$(function(){
		function autoSize(){
			var newH = $(window).height() - $(".head").height();
			var newW = $(".main").width() - $(".mainLeft").width();
			$(".main").height( newH );
			$(".mainLeft").height( newH );
			$(".mainRight").height( newH );
			$(".mainRight").width( newW );
			$("#window_password").hide();
		}
		autoSize();
		
		$(window).on("resize",function(){
			autoSize();
		});
		
		/*导航*/
		
		$(document).on("click",".nav>dt>a",function(){
			$(this).parent().parent().find(".itemSelected").removeClass("itemSelected");
			$(this).parent().addClass("itemSelected");
			
			var obj = $(".mainRightDiv").eq($(this).parent().index());
			$(".mainRightDiv").hide();
			obj.show();
			
			if(obj.find("iframe").attr("src")==""){
				obj.find("iframe").attr("src", $(this).attr("openURL"));
			}
			else{
				obj.find("iframe").attr("src", $(this).attr("openURL"));
			}
		});
		
		$(document).on("click",".nav>dd>a",function(){
			$(this).parent().parent().find(".itemSelected").removeClass("itemSelected");
			$(this).parent().addClass("itemSelected");
			
			var obj = $(".mainRightDiv").eq($(this).parent().index());
			$(".mainRightDiv").hide();
			obj.show();
			
			if(obj.find("iframe").attr("src")==""){
				obj.find("iframe").attr("src", $(this).attr("openURL"));
			}
			else{}
		});
	});
	
	function chgPwd()
	{
		$("#window_password").show();
		$win = $("#window_password").window({
			title:"修改密码",
			width:500,
			height:200,
			border:false,
		    modal:true,
		    minimizable: false,
		    maximizable: false,
		    collapsible: false
		});
		//$win.window('open');
	}
	function cancelChangePassword()
	{
		if($win!=null)
		{
			$("#window_password").hide();
			$win.window('close');
		}
	}
	 function savePassword(){
		var oldPassword = $.trim($("#oldPassword").val());
		var newPassword = $.trim($("#newPassword").val());
		var newPassword2 = $.trim($("#newPassword2").val());
		if(oldPassword == ""){
			$('#message').html('请输入旧密码');
			return;
		}
		if(newPassword == ""){
			$('#message').html('密码为空,请勿输入空格！');
			return;
		}
		if(newPassword2 == ""){
			$('#message').html('密码为空,请勿输入空格！');
			return;
		}
		if(newPassword != newPassword2){
			$('#message').html('两次输入的密码不相同');
			return;
		}
		$.ajax({
			  url: "/pc/user/changePassword.htm",
			  type:'post',
			  data:{
			  	'oldPassword' : oldPassword,
			  	'newPassword' : newPassword
			  },
			  dataType:'json',
			  success: function(data){
			  	if(data.success=='true'){
			  		$("#oldPassword").val("");
			  		$("#newPassword").val("");
			  		$("#newPassword2").val("");
			  		$.messager.alert('提示','密码修改成功,请重新登陆!','info',function(){
			  			window.location.href="/logout";
			  		});
			  	}else{
			  		if(data.msg){
			  			$.messager.alert('提示',data.msg,'error');
			  		}else{
			  			$.messager.alert('提示','密码修改失败,请稍后重试!','error');
			  		}
			  		
			  	}
			  },
			  error:function(){
				  $.messager.alert('提示','系统出错，密码修改失败！','error');
			  }
		});
		
	}
	
</script>
</head>

<body scroll="no">
	<div class="full">
		<!--head-->
		<div class="head">
	    	<a href="index.htm" class="logo">
	    		<img src="/resources/pedometer/img/logo.png" />
	    	</a>
	        <div class="userInfo">
	        	<img src="/resources/pedometer/img/icon1.png" />欢迎您，<b>${user.username }</b>
	        	<a href="javascript:chgPwd()" class="headA">修改密码</a>
	        	<a href="javascript:Header.logout();" class="logout">安全退出</a>
	        </div>
	    </div>
		<!--head end-->
		<!--main-->
	    <div class="main">
			<!--mainLeft-->
	    	<div class="mainLeft">
	        	<dl class="nav">
	            	<dt class="itemSelected"><a href="index.htm" openURL="">首页</a></dt>
	            	<dt><a href="javascript:void(0)" openURL="walkRecord.htm">健步记录</a></dt>
	            	<dt><a href="javascript:void(0)" openURL="userRecord.htm">用户记录</a></dt>
	            	<dt>统计分析</dt>
	                <dd><a href="javascript:void(0)" openURL="userAnalyze.htm">用户分析</a></dd>
	                <!-- <dd><a href="javascript:void(0)" openURL="deptAnalyze.htm">部门分析</a></dd> -->
	            	<!-- <dt><a href="javascript:void(0)" openURL="infoImport.htm">信息导入</a></dt> -->
	            	<dt><a href="javascript:void(0)" openURL="movement.htm">活动管理</a></dt>
	            	<c:if test="${user.username=='s_admin' }">
		            	<dt><a href="javascript:void(0)" openURL="medal.htm">勋章管理</a></dt>
		            	<dt><a href="javascript:void(0)" openURL="integration.htm">积分查询</a></dt>
	            	</c:if>
	            </dl>
	        </div>
			<!--mainLeft end-->
			<!--mainRight-->
	    	<div class="mainRight">
	        	<div class="mainRightDiv" style="display:block">
	            	<iframe class="iframeIndex" src="content.htm" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	            
	        	<div class="mainRightDiv"></div>
	            
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	            <div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        	<div class="mainRightDiv">
	            	<iframe class="iframeIndex" src="" width="100%" height="100%" scrolling="auto" frameborder="no" border="0" framespacing="0" ></iframe>
	            </div>
	        </div>
			<!--mainRight end-->
	        <div class="clearFix"></div>
	    </div>
		<!--main end-->
	</div>

	<!--bottom-->
	<div class="bottombg">
		Copyright © 2013-2015  安徽移动通信 All Rights Reserved 版权所有   维护电话：15156892727
	</div>
	
	<!--弹出框 修改密码-->
<div  id="window_password" style="width: 300px; height: 180px;">
	
	<!-- 标签页开始 -->
		<div class="zw_cen1">
		<form id="addForm" method="post" action="">
	        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi"></td>
	            <td colspan="2" id="message" style="color: red;"></td>
	          </tr>
	          <tr>
	            <td class="leftd">旧密码：</td>
	            <td colspan="2"><input id="oldPassword" name="oldPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td class="leftd">新密码：</td>
	            <td colspan="2"><input id="newPassword" name="newPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	          <tr>
	            <td class="leftd">确认新密码：</td>
	            <td colspan="2"><input id="newPassword2" name="newPassword2" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td >&nbsp;</td>
	            <td colspan="2"><div align="left">
	            <input id="addUserInfo" type="button" class="bottom_01" value="确定" onclick="Header.savePassword();"/>
	            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="" type="button" class="bottom_01" value="取消" onclick="cancelChangePassword()"/></div></td>
	             </tr>
	        </table>
        </form>
	    </div>
</div>
	
</body>
</html>
