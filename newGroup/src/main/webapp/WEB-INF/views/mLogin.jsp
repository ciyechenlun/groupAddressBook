<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<link href="/resources/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/resources/js/login/mLogin.js"></script>
<script type="text/javascript">
$(function(){
	$('#username').focus(function(){
		$(this).removeClass("input2");			 
		$(this).addClass("input1");
	});
	$('#username').blur(function(){
		$(this).removeClass("input1");			 
		$(this).addClass("input2");
   });
   $('#userpw').focus(function(){
		$(this).removeClass("input2");			 
		$(this).addClass("input1");
	});
	$('#userpw').blur(function(){
		$(this).removeClass("input1");			 
		$(this).addClass("input2");
   });
   $('#userpw1').focus(function(){
		$(this).removeClass("input3");			 
		$(this).addClass("input4");
	});
	$('#userpw1').blur(function(){
		$(this).removeClass("input4");			 
		$(this).addClass("input3");
   });
});
</script>
</head>

<body>
<div class="wrap">
	<div class="logo"><img src="/resources/pedometer/img/logo.png" width="508" height="86" /></div>
</div>
<div class="wrap fn_clear">
    <div class="col-left">
    	 <div class="banner"></div>
    </div>
    <div class="col-right loginArea">
    	<div class="login-top">
    	</div>
         <div class="login-middle">
         	<input id="loginFailureMustValCode" type="hidden" value="${sessionScope.loginFailureMustValCode}" />
            <div id="error" class="error divClearMargin15" style="display:none;">
                <p>登录信息错误提示，默认隐藏，包含用户名和密码</p>
            </div>
            <c:if test="${not empty ERROR_MESSAGE}">
             	<div id="errorMsg" class="error divClearMargin15">${ERROR_MESSAGE}</div>
            </c:if>
            <form name="loginForm" id="loginForm" action="" method="post">
            <table align="left" class="table_01"> 
                <tr>                       
                    <td colspan="2" class="td1">&nbsp;</td>                    
                </tr> 
                <tr>
                    <td class="td1">用户名：</td>
                    <td><input class="input2" type="text" name="j_username" id="j_username" value="${COOKIE_J_USER_NAME}"/></td>
                </tr>
                <tr>
                    <td class="td1">密&nbsp;&nbsp;码：</td>
                    <td><input type="password" class="input2" name="j_password" id="j_password" value="${COOKIE_J_PASS_WORD}" /></td>
                </tr>
                <tr id="jcapatch_code_div" style="display: none">
                	<td class="td1">验证码：</td>
                	<td>
                		<input id="jcapatch_code" type="text" class="input5" name="jcapatch_code" />
	                	<span style="padding-left:5px; padding-top:10px;">
		               		<img id="jcaptchaId" alt="验证码" width="65" height="25"/>
		               	</span>
		               	<a href="javascript:Login.refreshCode();" style="padding-left:5px;">换一张</a>
                	</td>
                </tr>                            
            </table>
            <table align="left" class="table_01">
            	<tr>
                	<td width="40">&nbsp;</td>
                    <td height="46" colspan="1">&nbsp;&nbsp;
						<input name="按钮" id="loginBtn" type="button" class="anniu" value="登&nbsp;&nbsp;&nbsp;录" />&nbsp;&nbsp;
                 	</td>
                   <td>
                   		<img src="/resources/images/login/ban_wj.png" width="14" height="14" />
                   		<a href="javaScript:void(0)" onclick="MLogin.forget();">忘记密码？</a>
                   </td>
                </tr>
            </table>
            </form>
</div>
          <div class="login-bottom">
            <table>
            </table>
        </div>   
    </div>
<div class="col-right1"><img src="/resources/images/login/newsimg_r.jpg" width="55" height="278" /></div>
</div>

<div class="col-footer fn_clear">
    <center>
        <p class="p" face="verdana" >版权所有©中国移动通信安徽分公司<br/>
          
        </p>
    </center>
</div> 


</body>
</html>
