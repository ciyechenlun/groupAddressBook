<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/web/advise.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript">
	/**
	提交规则信息至数据库
	**/
	function submit()
	{
		var selReleation = $("#selReleation").val();//与前一个条件的关系
		var selType = $("#selType").val();//条件
		var selOperator = $("#selOperator").val();//操作符
		var selGroups = $("#selGroups").val();//分组
		var txtValue = $("#txtValue").val();//值
		if(selType == "1" || selType == "2")
		{
			//此情况，值不能为空
			if(txtValue == '' || txtValue == undefined)
			{
				alert('比较值不能为空');
				$("#txtValue").focus();
				return;
			}
		}
		else if(selType == "3")
		{
			//分组要不能为空
			if(selGroups == '' || selGroups == undefined)
			{
				alert('分组必须选择');
				return;
			}
		}
		//提交表单
		$('#form1').form('submit',{
			url:"/pc/right/add_master_rules_save.htm",
			onSubmit: function(){
				return $('#form1').form('validate');
			},
			success:function(data){
				if(data=='SUCCESS'){
					$.messager.alert('提示','保存成功','info',function(){
						window.location.href = "/pc/right/master_right_set?type=${type}&master_id=${master_id}&rules_id=${rules_id}";					
					});				
				}else {
					$.messager.alert('提示','保存失败','error');	
				}
			}
		});
	}
	/**
	根据选择框内容动态展示相关信息
	**/
	function selType_OnSelected(opt)
	{
		var value = opt.options[opt.selectedIndex].value;
		switch(value)
		{
			case "1"://部门级别，显示、比较符、比较值
				showOrHide('','','none');
				break;
			case "2"://岗位级别
				showOrHide('','','none');
				break;
			case "3"://分组
				showOrHide('none','none','');
				break;
			case "4"://分管领导
				showOrHide('none','none','none');
				break;
		}
	}
	/**
	设置条件区域展示与否
	**/
	function showOrHide(operator,value,groups)
	{
		var trvalue = document.getElementById('trvalue');
		var trgroup = document.getElementById('trgroups');
		var troperator = document.getElementById('troperator');
		trvalue.style.display = value;
		trgroup.style.display = groups;
		troperator.style.display = operator;
		
	}
</script>
</head>

<body>	

<!--body-->

<!--body正文-->
<div class="bodyqj bodybg fn_clear" style="height:500px;">
<div class="ck_right1">
	<div class="ck_rightbg">
		权限规则管理
	</div>
	<div id="dvButton" class="margintop" style="width:100%;text-align:right;">
		<input id="submitBtn" name="" type="button" class="bottom_01" value="提交" onclick="submit();"/>
		<input id="backBtn" name="" type="button" class="bottom_01" value="取消" onclick="history.back();"/>
	</div>
	<div class="margintop ck_top">
		<form id="form1" action="" method="post">
			<input type="hidden" name="txtMasterId" id="txtMasterId" value="${master_id }" />
			<input type="hidden" name="txtMasterRulesId" id="txtMasterRulesId" value="${rules_id}" />
		   <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:10px;">
		   <tr>
		   		<td class="xz_zi tc_w" style="width:200px;">与前一条件关系</td>
		   		<td>
		   			<select id="selReleation" name="selReleation" style="width:100px;">	
		   				<option value="AND">AND</option>
		   				<option value="OR">OR</option>
		   			</select>
		   		</td>
		   </tr>
		   	<tr>
		   		<td class="xz_zi tc_w">当前用户的 &nbsp;&nbsp;&nbsp;&nbsp;条件类型</td>
		   		<td>
		   			<select id="selType" name="selType" style="width:100px;" onchange="selType_OnSelected(this)">
		   				<option value="1">部门级别</option>
		   				<option value="2">岗位级别</option>
		   				<option value="3">分组</option>
		   				<option value="4">分管领导</option>
		   			</select>
		   		</td>
		   	</tr>
	   		<tr id="troperator">
		   		<td class="xz_zi tc_w">比较条件</td>
		   		<td>
		   			<select id="selOperator" name="selOperator" style="width:100px;">
		   				<option value="=">=</option>
		   				<option value=">">></option>
		   				<option value=">=">>=</option>
		   				<option value="<"><</option>
		   				<option value="<="><=</option>
		   				<option value="!=">!=</option>
		   			</select>
		   		</td>
	   		</tr>
	   		<tr id="trgroups" style="display:none;">
		   		<td class="xz_zi tc_w">分组</td>
		   		<td>
		   			<select id="selGroups" name="selGroups" style="width:100px;">
		   			</select>
		   		</td>
		   	</tr>
	   		<tr id="trvalue">
		   		<td class="xz_zi tc_w">比较值</td>
		   		<td>
		   			<input type="text" id="txtValue" name="txtValue" style="width:100px;" />
		   		</td>
		   	</tr>
		   </table>
	   </form>
	</div>
</div>

</div>
</body>
</html>
