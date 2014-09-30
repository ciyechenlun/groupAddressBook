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
<script type="text/javascript">
	$(function(){
		
	});
	
	
	
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
	<div id="movementDialog"></div>
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="./movement.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="10" />
	</form>
	<h1>活动列表</h1>
	<div class="rightContMain">
	    <div class="searchBar">
            <div>
            	<table cellpadding="0" cellspacing="0" >
                	<tr>
                    	<td class="w80"><button class="cxbtn" onclick="pedometer.btnAdd_OnClick('add')"> 添加活动 </button></td>
                    </tr>
                </table>
            </div>
	    </div>
	    
	    <p class="line"></p>
	    
	    <div class="gridWrap">
	        <div class="gridHead"><b>活动列表</b></div>
	        <div class="gridCont">
	            <table class="gridTable" style="width:100%;" cellpadding="0" cellspacing="0">
	                <tr>
	                	<th>活动名称</th>
	                	<th>起始时间</th>
	                	<th>结束时间</th>
	                	<th>状态</th>
	                	<th>设置</th>
	                </tr>
	                
	                <c:forEach var="searchResult" varStatus="status" items="${pagination.result }">
	                	<tr>
		                	<td>${searchResult.movement_name }</td>
		                	<td>${searchResult.movement_start_date }</td>
		                	<td>${searchResult.movement_end_date }</td>
		                	<td>
		                		<c:if test="${searchResult.movement_status=='1' }">进行中</c:if>
		                		<c:if test="${searchResult.movement_status=='0' }">未开始</c:if>
		                		<c:if test="${searchResult.movement_status=='2' }">已过期</c:if>
		                		<c:if test="${searchResult.movement_status=='3' }">已禁用</c:if>
		                	</td>
		                	<td>
		                		<a href="movementUsers.htm?movement_id=${searchResult.movement_id }">
		                			用户设置
		                		</a>
		                		|
		                		<a href="javascript:pedometer.disableMovement('${searchResult.movement_id}')" onclick="void(0)">
		                			<c:if test="${searchResult.movement_status=='1' }">禁用</c:if>
		                			<c:if test="${searchResult.movement_status=='2' }">启用</c:if>
		                		</a>
		                		|
		                		<a href="javascript:pedometer.btnAdd_OnClick('edit','${searchResult.movement_id }')">编辑</a>
							</td>
	                	</tr>
	                </c:forEach>
	                
	            </table>
	        </div>
	        
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
</body>
</html>
