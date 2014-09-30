<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<script type="text/javascript" src="/resources/pedometer/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=8150a48c5fce9d1fd4970342dbe93ef9"></script>
<script type="text/javascript">
	function myrefresh(){
		window.location.reload();
	}
	
	$(function(){
		function autoSize(){
			var newH = $(window).height() - $("h1").height();
			var newW = $(window).width();
			$(".main").height( newH );
			$(".main").width( newW );
		}
		autoSize();
		
		$(window).on("resize",function(){
			autoSize();
		});
		init();
	});
	//初始化地图
	function init()
	{
		$.get('./getGPS.htm?employee_record_id=${employee_record_id}',function(data){
			//轨迹信息
			// 百度地图API功能
			var map = new BMap.Map("dvMap");
			map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
			map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));  //右上角，仅包含平移和缩放按钮
			map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT, type: BMAP_NAVIGATION_CONTROL_PAN}));  //左下角，仅包含平移按钮
			map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}));  //右下角，仅包含缩放按钮
			var point = new BMap.Point(data.list[0].lon/1000000, data.list[0].lat/1000000);
			var points = new Array(data.list.size);
			for(var i = 0; i < data.list.length; i++)
			{
				points[i] = new BMap.Point(data.list[i].lon/1000000, data.list[i].lat/1000000);
			}
			map.centerAndZoom(point, 15);
			var polyline = new BMap.Polyline(
				points, {strokeColor:"blue", strokeWeight:6, strokeOpacity:0.5});
			map.addOverlay(polyline);
			map.enableScrollWheelZoom(true);
		});
	}
</script>
<style type="text/css">
	#dvUserInfo table td {padding:5px;font-size:14px;font-style: italic;}
	#dvUserInfo table th {font-weight: bold;}
</style>
</head>

<body>
	<h1>${userName }用户轨迹查看&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.back();">返回</a></h1>
	<div class="main">
		<div id="dvUserInfo">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<th>姓名</th>
					<td>${employee_name }</td>
					<th>运动日期</th>
					<td>${sport_date }</td>
					<th>开始时间</th>
					<td>${sport_start_time }</td>
					<th>结束时间</th>
					<td>${sport_end_time }</td>
				</tr>
				<tr>
					<th>速度</th>
					<td>${sport_speed }</td>
					<th>步数</th>
					<td>${sport_step }</td>
					<th>里程</th>
					<td>${sport_distence }</td>
					<th>总用时</th>
					<td>${sport_elapse_time }</td>
				</tr>
			</table>
		</div>
		<div id="dvMap" style="width:100%;height:300px;">
			正在加载地图，请稍候...
		</div>
	</div>
	<script type="text/javascript">
		
</script>
	
</body>
</html>
