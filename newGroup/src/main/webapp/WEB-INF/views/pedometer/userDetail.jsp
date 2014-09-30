<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户详情</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<script type="text/javascript">

function myrefresh(){
	window.location.reload();
}

function showUserDetail()
{
	window.location.href = './walkRecord.htm?usercode=${employee_id}';
}

$(function(){
	
	function autoSize(){
		var newH = $(window).height() - $("h1").height();
		var newW = $(window).width();
		$(".main").height( newH );
		$(".main").width( newW );
		$(".rightContMain").height( newH );
	}
	autoSize();
	
	$(window).on("resize",function(){
		autoSize();
	});

})

</script>
<style type="text/css">
	.btncont{position:absolute;width:100%;}
</style>
</head>

<body>

<h1>用户记录<span class="blue">${user.employee_name}</span></h1>

<div class="main">
    <div class="rightContMain" style="padding:20px;text-align:center;">
       <table align="center" class="gridTable2" border="0" cellpadding="0" cellspacing="0">
       	<tr>
       		<th>姓名</th>
       		<td>&nbsp;&nbsp;${user.employee_name}</td>
       		<th>运动次数</th>
       		<td>${user.all_sport_times}</td>
       		<th>第一次使用时间</th>
       		<td>${user.sport_first_date}</td>
       	</tr>
       	<tr>
       		<th>性别</th>
       		<td>${user.sex}</td>
       		<th>总步数</th>
       		<td>${user.all_sport_steps}</td>
       		<th>最近一次使用时间</th>
       		<td>${user.sport_nearest_time}</td>
       	</tr>
       	<tr>
       		<th>出生日期</th>
       		<td>${user.birthday}</td>
       		<th>总用时</th>
       		<td>${user.all_sport_elpse_time}</td>
       		<th>GPS统计次数</th>
       		<td>${user.gps_times}</td>
       	</tr>
       	<tr>
       		<th>工号</th>
       		<td>${user.employee_code}</td>
       		<th>总里程</th>
       		<td>${user.all_sport_distence}</td>
       		<th>GPS记录总里程</th>
       		<td>${user.all_gps_distence}</td>
       	</tr>
       	<tr>
       		<th>手机</th>
       		<td>${user.mobile}</td>
       		<th>平均速度</th>
       		<td>${user.avg_sport_speed}</td>
       		<th>GPS记录总步数</th>
       		<td>${user.all_gps_steps}</td>
       	</tr>
       	<tr>
       		<th>公司</th>
       		<td>${user.parent_department_name}</td>
       		<th>平均步数</th>
       		<td>${user.avg_sport_steps}</td>
       		<th>活动勋章数量</th>
       		<td>${user.movement_number}</td>
       	</tr>
       	<tr>
       		<th>部门</th>
       		<td>${user.department_name}</td>
       		<th>平均用时</th>
       		<td>${user.avg_sport_time}</td>
       		<th>次数勋章数量</th>
       		<td>${user.times_number}</td>
       	</tr>
       	<tr>
       		<th>身高</th>
       		<td>${user.employee_height} cm</td>
       		<th>最快速度</th>
       		<td>${user.single_max_speed}</td>
       		<th>步长</th>
       		<td>${user.steps}</td>
       	</tr>
       	<tr>
       		<th>体重</th>
       		<td>${user.employee_weight}</td>
       		<th>最大步数</th>
       		<td>${user.single_max_steps}</td>
       		<th>当前排名</th>
       		<td>${user.current_rank}</td>
       	</tr>
       	<tr>
       		<td colspan="2"></td>
       		<th>最长用时</th>
       		<td>${user.single_max_time}</td>
       		<th>等级</th>
       		<td>${user.sport_level}</td>
       	</tr>
       </table>
       <div class="btncont">
	       <input type="button" value="  返回   " class="ckbtn" onclick="history.back();"  />
	       <input type="button" value="查看个人健步记录" class="ckbtn" onclick="showUserDetail()"  />
       </div>
    </div>
</div>

</body>
</html>
