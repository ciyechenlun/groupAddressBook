<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>集团通讯录部门维护</title>
		<%@include file="../common/baseIncludeJs.jsp" %>
		<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
		<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
		<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>
		<script type="text/javascript" src="/resources/js/web/sms.js"></script>
	</head>
	<body id="dvContainer">
		 <div class="nowbg">您当前位置 -- 短信推广</div>
        <div class="qxred">短信推广内容</div>
        <div class="dxsearch">
            <div><strong>选择发送范围</strong><span class="f_r">许可倒计时：N天N时N分</span></div>            
              <input name="target" type="radio" value="1" checked="checked" />本网用户  <input name="target" type="radio" value="2" />未注册用户  
              <input name="target" type="radio" value="3" />异网用户  <input name="target" type="radio" value="4" />所有用户           
        </div>
        <div class="drwj3">
        <form id="save_form" action="">
					<input type="hidden" id="companyId" name="companyId" value="${companyId}"/>
					<input type="hidden" id="companyName" name="companyName" value="${companyName}"/>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="170"><label for="textarea"></label>
                <textarea id="content" name="content" cols="45" rows="5" class="dxcekbig" ></textarea></td>
              </tr>
              <tr>
                <td style="padding-right:20px"><a href="#" onclick="Sms.sendMsg();"><span class="f_r opbtns"><img src="/resources/images/fsdx.png" />发送短信</span></a></td>
              </tr>
          </table>
          </form>
      </div>
	</body>
</html>