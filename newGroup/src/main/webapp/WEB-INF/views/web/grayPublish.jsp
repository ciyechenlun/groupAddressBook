<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>集团通讯录部门维护</title>
	<%@include file="../common/baseIncludeJs.jsp" %>
	<script type="text/javascript" src="/resources/js/web/grayPublish.js"></script>
</head>

<body>
	<div id="chooseDept"></div>
	<!--body正文-->
	<div class="bodyqj bodybg fn_clear">
		<div class="ck_right1" style="height:500px;">
			<div class="ck_rightbg">灰度发布</div>
   			<div class="margintop ck_top">
    			<form id="save_form" action="">
					<input id="empIds" name="empIds" type="hidden" />
					<input id="empNum" name="empNum" type="hidden" />
    				<table align="center" style="width:100%;">
						<tr>
					        <td width="25%" align="right"><span class="tc_red">*</span>选择公司：</td>
					        <td width="35%" align="left">
					        	<input id="msg_company" style="width: 270px"/>
					       	</td>
					      	<td width="15%" align="right">
					      		<input id="syncAllFlag" name="syncAllFlag" type="hidden" value="false"/>
					     		<input id="checkboxName" type="checkbox" onclick="GrayPublish.disableObj();"/>
					   		</td>
					      	<td width="25%" align="left">同步所有人</td>
     		 			</tr>
						<tr>
							<td width="25%"  align="right"><span class="tc_red">*</span>部门名称：</td>
							<td width="35%"  align="left">
								<input id="SelectedDeptId" name="SelectedDeptId" type="hidden"/>
								<input id="SelectedDept" style="width: 270px;height: 20px;border: 1px solid #A9A9F5;background:white" readonly="readonly"/>
							</td>
							<td width="10%" align="right"></td>
							<td width="30%" id="deptSelectButton" align="left" colspan="2">  
								<a href="javascript:void(0)" onclick="javascript:GrayPublish.openWin('550', '410')">选择部门</a>
							</td>
						</tr>
						<tr>
					      	<td width="25%" align="right"><span class="tc_red">*</span>同步对象：</td>
					        <td align="left" colspan="3">
					        	<textarea id="syncObject" style="width:100%;height:180px;" readonly="readonly"></textarea>
					       	</td>
						</tr>
						<tr style="height: 20px">
					        <td width="25%" align="right"></td>
					        <td width="35%" align="left"></td>
					      	<td width="15%" align="right"></td>
					      	<td width="25%" align="left"></td>
						</tr>
						<tr>
							<td width="25%" align="right"></td>
							<td colspan="3">
					        	<input name="" type="button" class="bottom_01" value="确认" onclick="GrayPublish.confirmSync();"/>
					        	<input name="" type="button" class="bottom_01" value="取消" onclick="GrayPublish.cancelEdit();"/>
				        	</td>
			        	</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>