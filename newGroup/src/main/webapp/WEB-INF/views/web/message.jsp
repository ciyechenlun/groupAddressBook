<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录部门维护</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<script type="text/javascript" src="/resources/js/web/message.js"></script>
</head>

<body>
<div id="choseUser"></div>
<!--body正文-->
<div class="bodyqj bodybg fn_clear">

<div class="ck_right1" style="height:500px;">
	<div class="ck_rightbg">
		消息推送
	</div>
    <div class="margintop ck_top">
    <form id="save_form" action="">
    <table  align="center" style="width:100%;">
      <tr>
        <td width="25%" align="right">选择公司：</td>
        <td width="35%" align="left"><input id="msg_company" style="width: 270px"/></td>
      	<td width="15%" align="right"><input  id="checkboxName" type="checkbox" onclick="Message.disableObj();"/></td>
      	<td width="25%" align="left">发送给所有人</td>
      </tr>
      <tr>
         <td width="25%"  align="right">对象：</td>
         <td width="35%"  align="left">
           	<span id="empIdD" style="display:none;" ><c:forEach items="${list }" var="user"><span id="${user.userId }">,'${user.userId }'</span></c:forEach></span>
            <div style="height: 20px;border: 1px solid #A9A9F5;background:white" id="SelectedEmp">
              	<span id="empNameD">
             		<c:forEach items="${list }" var="user">
               			<span name="${user.userId }_${user.userName }" onclick='javascript:Message.deletee(this);'>${user.userName };</span>
					</c:forEach>
				</span>
             </div>
		</td>
        <td width="15%" align="left"><font color="red">*点击可删除</font></td>
        <td width="25%" id="empSelectButton" align="left">  
			<a id="SelectEmp" href="javascript:void(0)" onClick="javascript:Message.openWin('人员选择',660,410,'/pc/message/userChoseView?companyId='+ $('#msg_company').combobox('getValue'))" plain="true" iconCls="add">选择人员</a>
		</td>
	  </tr>
      <tr>
        <td width="25%" align="right">类型：</td>
        <td width="35%" align="left"><input id="msg_type" style="width: 270px"/></td>
         <td width="15%" align="right"></td>
        <td width="25%" align="left"></td>
      </tr>
      <tr>
      	<td width="25%" align="right">内容：</td>
        <td align="left" colspan="3"><textarea id="msg_text" style="width:100%;height:130px;"></textarea></td>
      </tr>
      <tr style="height: 20px">
        <td width="25%" align="right"></td>
        <td width="35%" align="left"></td>
      	<td width="15%" align="right"></td>
      	<td width="25%" align="left"></td>
      </tr>
      <tr>
      	<td width="25%"  align="right"></td>
        <td colspan="3">
        	<input name="" type="button" class="bottom_01" value="确认" onclick="Message.save();"/>
        	<input name="" type="button" class="bottom_01" value="取消" onclick="Message.cancelEdit();"/></td>
      </tr>
    </table>
    </form>
    </div>
</div>
</div>
</html>
