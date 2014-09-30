<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

  <!--弹出框 修改密码-->
	  <div class="openwarp">
       <div class="opentabs">
		<form id="addForm" method="post" action="">
	        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="t_r"></td>
	            <td  colspan="2" id="message" style="color: red;"></td>
	          </tr>
	          <tr>
	            <td class="t_r">旧密码：</td>
	            <td class="t_l" colspan="2"><input id="oldPassword" name="oldPassword" type="password" size="20" class="opcek3" required="required"/></td>
	          </tr>
	           <tr>
	            <td class="t_r">新密码：</td>
	            <td class="t_l" colspan="2"><input id="newPassword" name="newPassword" type="password" size="20" class="opcek3" required="required"/></td>
	          </tr>
	          <tr>
	            <td class="t_r">确认新密码：</td>
	            <td class="t_l" colspan="2"><input id="newPassword2" name="newPassword2" type="password" size="20" class="opcek3" required="required"/></td>
	          </tr>
	        </table>
        </form>
           </div>
       <div class="fulltd">
         <a href="#" onclick="$('#window_password').window('close')"><span class="f_l opbtns3"><img src="/resources/images/qx.png" />取消</span></a>
        <a href="#" onclick="Header.savePassword();"> <span class="f_l opbtns3"><img src="/resources/images/opsave.png" />确定</span></a>
       </div>
   </div>
</div>