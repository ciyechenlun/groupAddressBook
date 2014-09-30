<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<link href="/resources/css/plogin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/resources/js/login/login.js"></script>
<script type="text/javascript">
$(function(){
	$("#j_password").focus(function(){
	  $(this).siblings("#passtips").hide();								 
	});
	$("#j_password").blur(function(){
	  var thistext = $(this).val();
	  if( thistext=="" ){
	    $(this).siblings("#passtips").show();	
	  }
	});
	$("#passtips").click(function(){
		$(this).hide();
		$("#j_password").focus();
	});
	
});
</script>
</head>

<body>
<div class="wrap">
<div class="head">
	<div class="w1024">
    	<div class="logo"><img src="/resources/images/plogin/logo.png" width="419" height="59" /></div>
    </div>
</div>
<div class="main">
	<div class="w1024">
    	<div class="loginform">
    		<input id="loginFailureMustValCode" type="hidden" value="${sessionScope.loginFailureMustValCode}" />
            <div id="error" class="error divClearMargin15" style="display:none;">
                <p>登录信息错误提示，默认隐藏，包含用户名和密码</p>
            </div>
            <c:if test="${not empty ERROR_MESSAGE}">
             	<div id="errorMsg" class="error divClearMargin15">${ERROR_MESSAGE}</div>
            </c:if>
        	<form name="loginForm" id="loginForm" action="" method="post">
            	<div class="box">
                	<input type="text" value="请输入用户名" id="j_username" name="j_username" onfocus="if(value=='请输入用户名'){value='';}" onblur="if(value==''){value='请输入用户名';}" />
                    <p class="wrongtip">用户名不能为空</p>
                </div>
                <div class="box pos">
                    <input type="password" id="j_password" name="j_password" />
                    <p id="passtips">请输入密码</p>
                    <p class="wrongtip">密码不能为空</p>
                </div>
                <div class="forget" id="jcapatch_code_div" style="display:none;">
                	<table width="100" border="0" cellpadding="0" cellspacing="0">
                		<tr>
                			<td><input onfocus="if(value=='验证码'){value='';}" onblur="if(value==''){value='验证码';}" value="验证码" id="jcapatch_code" type="text" class="input5" name="jcapatch_code" /></td>
                			<td><a href="javascript:Login.refreshCode();" style="padding-left:5px;"><img id="jcaptchaId" alt="验证码" width="65" height="25"/></a></td>
                			<td>&nbsp;</td>
                		</tr>
                	</table>
                </div>
                <p class="btn clearfix">
                		<a class="tijiao" id="loginBtn" href="javascript:void(0)"></a>
                	<a href="javascript:void();" onclick="Login.forget();" class="forgetpwd">忘记密码？</a>
                </p>
            </form>
        </div>
    </div>
</div>
<div class="foot">
	<div class="w1024">版权所有&copy;中国移动通信安徽分公司</div>
</div>
</div>
</body>
</html>
