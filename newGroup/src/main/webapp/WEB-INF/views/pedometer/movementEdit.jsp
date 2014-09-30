<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/pedometer/pedometer.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
</head>

<body>
	<form name="fmMovement" id="fmMovement" enctype="multipart/form-data"  action="" method="post">
		<input type="hidden" name="movementId" id="movementId" value="${movement.movementId }" />
		<input type="hidden" name="movementStatus" id="movementStatus" value="${movement.movementStatus }" />
		<input type="hidden" name="eadmin" id="eadmin" value="${eadmin }" />
		<table class="gridTable" style="width:100%;" cellpadding="0" cellspacing="0">
	       <tr>
	       	  <th colspan="3">活动${typeName }</th>
	       </tr>
	       <tr>
	       		<td style="width:30%">活动名称</td>
	       		<td style="width:160px;">
	       			<input value="${movement.movementName }" id="movementName" name="movementName" type="text" style="width:160px;" class="bm_input"/>
	       		</td>
	       		<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       		<td>活动开始时间</td>
	       		<td>
	       			<input id="movementStartDate" value="${movement.movementStartDate }" name="movementStartDate" type="text"  style="width:160px;"  class="easyui-datetimebox"  />
	       		</td>
	       		<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       		<td>活动结束时间</td>
	       		<td>
	       			<input id="movementEndDate" name="movementEndDate" value="${movement.movementEndDate }" type="text" style="width:160px;"  class="easyui-datetimebox"  />
	       		</td>
	       		<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       	<c:if test="${eadmin=='1' }">
	       	<td>所属分公司</td>
	       	<td>
	       		<span>${deptName }</span>
	       		<input type="hidden" id="departmentId" name="departmentId" value="${department_id }" type="text" style="width:160px;" class="bm_input"/>
	       	</td>
	       	<td>
	       			<span style="color:red">*</span>
	       		</td>
	       	</c:if>
	       	<c:if test="${eadmin!='1' }">
	       	<td>所属企业</td>
	       	<td>
	       		<input id="companyId" name="companyId" value="${movement.companyId }" type="text" style="width:160px;" class="bm_input"/>
	       	</td>
	       	<td>
	       			<span style="color:red">*</span>
	       		</td>
	       	</c:if>
	       	
	       </tr>
	       <tr>
	       	<td>勋章系统</td>
	       	<td>
	       		<input id="medalSysId" name="medalSysId" value="${movement.medalSysId }" type="text" style="width:160px;" class="bm_input"/>
	       	</td>
	       	<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       	<td>排名方式</td>
	       	<td>
	       		<select name="orderType" id="orderType" style="width:160px;">
	       			<option value="0" <c:if test="${movement.orderType=='0' }"> selected="selected"</c:if>>按步数</option>
	       			<option value="1" <c:if test="${movement.orderType=='1' }"> selected="selected"</c:if>>按里程</option>
	       		</select>
	       	</td>
	       	<td>
	       			<span style="color:red">*</span>
	       		</td>
	       </tr>
	       <tr>
	       	<td>所属赛季</td>
	       	<td>
	       		<input id="parentMovementId" value="${movement.parentMovementId }" name="parentMovementId" type="text" style="width:160px;" class="bm_input"/>
	       	</td>
	       	<td>
	       		<span style="color:#CCCCCC">
	       			如果不选择赛季成绩只在活动内有效
	       		</span>
	       	</td>
	       </tr>
	       <tr>
	       	<td>活动宣传图片</td>
	       	<td>
	       		<input type="file" style="width:160px" class="bm_input" id="pictureZip" name="pictureZip" />
	       	</td>
	       	<td>
	       		<span style="color:#CCCCCC">请以zip格式上传活动宣传图片。图片名称必须叫movement.png。</span>
	       	</td>
	       </tr>
	       <tr>
	       		<td colspan="3" style="text-align:center">
	       			<button style="width:100px;" class="cxbtn" onclick="return pedometer.saveMovement('${type}')">提交</button>
	       			<button class="cxbtn" style="width:100px;" onclick="pedometer.cancel()">取消</button>
	       		</td>
	       </tr>
		</table>
	</form>
</body>
</html>
