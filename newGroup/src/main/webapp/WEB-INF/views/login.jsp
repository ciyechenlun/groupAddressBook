<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>集团通讯录-登录</title>
  <link href="/resources/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/resources/js/login/login.js"></script>
<script type="text/javascript">
if (window != top)
top.location.href = location.href;
</script>
</head>
 
<body scroll='no'>
<input type="hidden" name="to" id="to" value="${to }" />
<div class="lowarp">
    <div class="login_top"><img src="/resources/images/loname.png" /></div>
</div>

  <div style="margin:80px auto">
    <form name="loginForm" id="loginForm" action="" method="post"> 
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
        <td width="853" height="324" align="right" valign="middle" class="login_contain">
         <table width="357" border="0" cellpadding="0" cellspacing="0">
          <tr><td colspan="4" height="60" align="right" valign="bottom" style="padding-right: 30px;color:red">
           <input id="loginFailureMustValCode" type="hidden" value="${sessionScope.loginFailureMustValCode}" />
            <div id="error" class="error divClearMargin15" style="display:none;margin-top: 50px;">
                <p>登录信息错误提示，默认隐藏，包含用户名和密码</p>
            </div>
            <c:if test="${not empty ERROR_MESSAGE}">
             	<div id="errorMsg" class="error divClearMargin15">${ERROR_MESSAGE}</div>
            </c:if>
          </td></tr>
          <tr>
            <td width="50" height="60" align="left" valign="top"><span class="t_l">用户名</span></td>
            <td colspan="3" align="left" valign="top"><span class="t_l">
              <input type="text" class="locek"  name="j_username" id="j_username" value="${COOKIE_J_USER_NAME}"/>
            </span></td>
           </tr>
          <tr>
            <td height="60" align="left" valign="top">密　码</td>
            <td colspan="3" align="left" valign="top"><span class="t_l">
              <input type="password" class="locek" name="j_password" id="j_password" value="${COOKIE_J_PASS_WORD}"/>
            </span></td>
           </tr>
          <tr id="jcapatch_code_div" style="display:none">
            <td height="66" align="left" valign="top">验证码</td>
            <td width="100" align="left" valign="top"><span class="t_l">
              <input type="text" class="locekyzm" id="jcapatch_code"  name="jcapatch_code"/>
            </span></td>
            <td width="102" align="left" valign="top"><span class="t_l"><img id="jcaptchaId"  alt="" /></span>
            </td>
             <td width="105" align="left" valign="top"><span class="lofot"><a href="javascript:Login.refreshCode();">换一张</a></span></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
           <td height="70" colspan="3" align="left" valign="top"><a href="#" onclick="Login.forget();"><img id="passwordImg" src="/resources/images/getpassword.gif" /></a>
           <a id="loginBtn" href="javascript:void(0);" ><img id="loginImg" src="/resources/images/lobtn.gif" /></a></td>
           </tr>
         </table>
         
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>
    </form>
  </div>



  <div class="lowarp">
    <div class="login_copyright">中国移动通信集团安徽有限公司  版权所有</div>
  </div>
</body>

</html>