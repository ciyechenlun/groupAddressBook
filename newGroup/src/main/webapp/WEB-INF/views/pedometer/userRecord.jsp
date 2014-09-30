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
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<script type="text/javascript">
	$(function(){
		var id = '${ID}';
		$('#department_look').tree({
			url :'/pc/message/msgTreeByCompanyIdAndDeptID.htm?companyId=${company_id}&department_id=' + id,
			onClick : function(node){
				window.location.href = "./userRecord.htm?departmentId=" + node.id + "&movement_id=${movementId}";
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
	function showDetail(employee_id)
	{
		document.location.href = "./userDetail.htm?employee_id=" + employee_id;
	}
	//活动改变
	function movementCombobox_Changed(movement_id)
	{
		location.href = 'userRecord.htm?department_id=${department_id}&movement_id=' + movement_id;
	}
	//导出
	function export2Excel()
	{
		var movement_id = $("#movement_id").val();
		$('#exportForm').form('submit',{
			url:'./exportUserRecord.htm?department_id=${department_id}&movement_id=' + movement_id,
			success:function(ret){
					$.messager.alert(data);
				}
			});
	}
</script>
<style type="text/css">
	.ck_left{ float:left; width:160px; border-left:1px solid #e5e5e5; height:100%; }
</style>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
	
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="./userRecord.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="4" />
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
		<input id="companyId" name="companyId" type="hidden" />
		<input id="movement_id" name="movement_id" type="hidden" value="${movementId }" />
	</form>
	
	<form id="exportForm" method="post" action="">
	</form>
	
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
		<h1>活动：
			<select onchange="movementCombobox_Changed(this.value)" name="movementCombobox" id="movementCombobx">
				<c:forEach var="movementList" varStatus="status" items="${list }">
      				<option 
      				${movementList.movementId == movementId?"selected='selected'":""} 
      				value="${movementList.movementId }">${movementList.movementName }</option>
      			</c:forEach>
			</select>
			<button class="cxbtn" onclick="export2Excel()">导出</button>
		</h1>
	    <div class="rightContMain">
	    	<div class="userList">
	    	<c:forEach var="searchResult" items="${pagination.result }">
	    		<!--用户信息-->
	            <div class="userInfo">
	                <img src="/resources/pedometer/img/user.png" class="left">
	                <dl>
	                    <dt>${searchResult.employee_name }</dt>
	                    <dd>总次数：${searchResult.all_sport_times }</dd>
	                    <dd>当前等级：${searchResult.sport_level }</dd>
	                    <dd>总用时：${searchResult.all_sport_elpse_time }</dd>
	                    <dd>平均速度：${searchResult.avg_sport_speed } km/h</dd>
	                    <dd>总步数：${searchResult.all_sport_steps }</dd>
	                    <dd>平均步数：${searchResult.avg_sport_steps }</dd>
	                </dl>
	                <p>
	                    <input type="button" value="查 看" class="ckbtn" onclick="showDetail('${searchResult.employee_id}')" />
	                    <!-- <input type="button" value="设为管理员" class="ckbtn" /> -->
	                </p>
	                
	            </div>            
	            <p class="lineFull"></p>
	            <!--用户信息 end-->
    		</c:forEach>
	        	
        	<!--start:分页-->
			<div class="bottom-pagination">
				<div class="pagination-box">
					<div class="pagination">
						<ul>
							<li>
								<div class="paginations" name="Pagination"></div> <span>共${pagination.totalRecords}条记录</span>
								<span> 到第 <input type="text" name="toTargetPage"
									style="width: 25px;" value="${pagination.currPage}" /> <span>页</span>
									<input type="button" value="确定" id="J_JumpTo"
									name="toTargetPage_btn" />
							</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<!--end：分页-->
	            
	        </div>
	    </div>
	</div>
</body>
</html>
