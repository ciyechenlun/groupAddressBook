<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<script type="text/javascript" src="/resources/js/echarts/doc/asset/js/esl/esl.js"></script>
<script type="text/javascript" src="/resources/js/echarts/doc/asset/js/codemirror.js"></script>
<script type="text/javascript" src="/resources/js/echarts/doc/asset/js/javascript.js"></script>
<script type="text/javascript">
	$(function(){
		$('#department_look').tree({
			url :'/pc/message/msgTreeByCompanyIdAndDeptID.htm?companyId=${companyId}&department_id=${department_id}',
			onClick : function(node){
				//window.location.href = "./userAnalyze.htm?departmentId=" + node.id;
				$("#hidDept").val(node.id);
				$("#txtDept").val(node.text);
				cxbtn_onclick();
			}
		});
		//日历控件
		$('#txtStartDate').datebox({
		    
		});
		$('#txtEndDate').datebox({
		    
		});
		
		function autoSize(){
			var newH = $(window).height();
			var newW = $(window).width() - 181;
			
			$(".leftCont").height( newH );
			$(".treeWrap").height( newH - $("h1").height() );
			$(".rightCont").width( newW );
			$(".rightContMain").height( newH - $("h1").height() );
		}
		autoSize();
		
		$(window).on("resize",function(){
			autoSize();
		});
	});
	
	//点击查询按钮
	function cxbtn_onclick()
	{
		var startDate = $("#txtStartDate").datebox('getValue');
		var endDate = $("#txtEndDate").datebox('getValue');
		var departmentId = $("#hidDept").val();
		var type = $("#selType").val();
		var tips = $("#selType").find("option:selected").text();
		var movement_id = $("#movementCombobox").val();
		$.get("getUserReport.htm?movement_id="+movement_id+"&departmentId="+departmentId+"&type="+type+"&tips="+tips+"&startDate="+startDate+"&endDate="+endDate,function(ret){
			refresh(eval("(" + ret.json + ")"));
		});
	}
</script>
<style type="text/css">
	.ck_left{ float:left; width:160px; border-left:1px solid #e5e5e5; height:100%; }
</style>
</head>

<body scroll="no">
	<div class="left leftCont w180">
		<h1>公司选择</h1>
	   <!--树-->
		<div id="tree" class="ck_left" style="overflow: auto;">
			<div class="margintop">
				<ul id="department_look"></ul>
			</div>
		</div>
		<!--树end-->
	</div>
	<div class="left rightCont">
		<h1></h1>
	    <div class="rightContMain">
	        <div class="searchBar">
	            <div>
	            	<table cellpadding="0" cellspacing="0" width="100%">
	                	<tr>
	                		<td>活动：</td>
	                		<td>
								<select name="movementCombobox" id="movementCombobox">
									<c:forEach var="movementList" varStatus="status" items="${list }">
					      				<option 
					      				${movementList.movementId == movementId?"selected='selected'":""} 
					      				value="${movementList.movementId }">${movementList.movementName }</option>
					      			</c:forEach>
								</select>
	                		</td>
	                    	<td style="display:none"><label>部门：</label></td><td>
	                    		<input type="text" value="${department.departmentName }" id="txtDept" class="inputText" readonly="readonly" />
	                    		<input type="hidden" value="${department.departmentId }" id="hidDept" name="hidDept" />
	                    	</td>
	                        <td><label>统计维度：</label></td><td>
	                        	<select name="selType" id="selType">
	                        		<option value="all_sport_times">总次数</option>
	                        		<option value="all_sport_elpse_time">总耗时</option>
	                        		<option value="all_sport_steps">总步数</option>
	                        		<option value="all_sport_distence">总里程</option>
	                        		<option value="avg_sport_speed">平均速度</option>
	                        		<option value="avg_sport_steps">平均步数</option>
	                        		<option value="avg_sport_time">平均耗时</option>
	                        	</select>
							</td>
	                        <td><label>起始时间：</label></td><td><input id="txtStartDate" name="txtStartDate" type="text" class="easyui-datebox"  /></td>
	                        <td><label>结束时间：</label></td><td><input id="txtEndDate" name="txtEndDate" type="text" class="easyui-datebox"  /></td>
	                        <td class="w80"><button class="cxbtn" onclick="cxbtn_onclick()"> 查 询  </button></td>
	                    </tr>
	                </table>
	            </div>
	        </div>
	        <p class="line"></p>
	        <!--图表-->
	        <div class="chartWrap" id="main">
				正在加载报表数据，请稍候...
	        </div>
	        <!--图表 end-->
	        <p class="clearFix"></p>
	    </div>
	</div>
	<script type="text/javascript">
		var rptOpt = "${data}";
		var option = eval("("+rptOpt+")");
	</script>
	<script src="/resources/js/echarts/doc/asset/js/echartsExample.js" type="text/javascript"></script>
</body>
</html>
