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
			url :'/pc/deptMag/treeTest.htm?companyId=${companyId}',
			onClick : function(node){
				//window.location.href = "./userAnalyze.htm?departmentId=" + node.id;
				$("#hidDept").val(node.id);
				$("#txtDept").val(node.text);
				cxbtn_onclick();
			}
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
	
	function cxbtn_onclick()
	{
		var departmentId = $("#hidDept").val();
		var type = $("#selType").val();
		var tips = $("#selType").find("option:selected").text();
		$.get("/pedometer/getDeptReport.htm?departmentId="+departmentId+"&type="+type+"&tips="+tips+"&startDate="+startDate+"&endDate="+endDate,function(ret){
			refresh(eval("(" + ret.json + ")"));
		});
	}
</script>
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
	            	<table cellpadding="0" cellspacing="0">
	                	<tr>
	                    	<td><label>部门：</label></td><td>
	                    		<input id="txtDept" name="txtDept" type="text" class="inputText" />
	                    		<input type="hidden" id="hidDept" name="hidDept" />
	                    	</td>
	                        <td><label>统计维度：</label></td>
	                        <td>
	                        	<select name="selType" id="selType">
	                        		<option value="avg_times">平均次数</option>
	                        		<option value="max_times">最多次数</option>
	                        		<option value="avg_steps">平均步数</option>
	                        		<option value="max_steps">最多步数</option>
	                        		<option value="avg_use_time">平均用时</option>
	                        		<option value="max_use_time">最多用时</option>
	                        		<option value="max_level">成员最高等级</option>
	                        		<option value="max_order">成员最高排名</option>
	                        	</select>
							</td>
	                        <td class="w80 tr"><button class="cxbtn" onclick="cxbtn_onclick">查 询</button></td>
	                    </tr>
	                </table>
	            </div>
	        	<p class="lineFull"></p>
	            <div>
	            	<b>部门高级查询：</b>
	                <table cellpadding="0" cellspacing="0" width="100%">
	                	<tr>
	                    	<td><label>部门 1：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 2：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 3：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 4：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 5：</label></td><td><input type="text" class="inputText" /></td>
	                        <td rowspan="2" class="w80 tr"><button class="cxbtn">对比查询</button></td>
	                    </tr>
	                    <tr>
	                        <td><label>部门 6：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 7：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 8：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门 9：</label></td><td><input type="text" class="inputText" /></td>
	                        <td><label>部门10：</label></td><td><input type="text" class="inputText" /></td>
	                    </tr>
	                    
	                </table>
	            </div>
	        </div>
	        <p class="line"></p>
	        <!--图表-->
	        <div class="chartWrap">
	            图表区域，高度随内容自动,min-height:400px;
	        </div>
	        <!--图表 end-->
	        <p class="clearFix"></p>
	    </div>
	</div>
</body>
</html>
