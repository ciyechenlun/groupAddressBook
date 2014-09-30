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
		pedometer.initSearch('${keyword}');
	});
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
	<div id="choseUser"></div>
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="./movementUsers.htm?movement_id=${movement.movementId }">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="10" />
	</form>
	<h1>"${movement.movementName }"活动用户列表</h1>
	<div class="rightContMain">
	    <div class="searchBar">
            <div>
            	<table cellpadding="0" cellspacing="0" >
                	<tr>
                    	<td><label>关键字：</label></td><td>
                    		<input onclick="pedometer.keyword_focus(this)" id="keyword" value="姓名、手机号码" name="keyword" type="text" style="width:160px;color:#CCCCCC" class="bm_input"/>
                    	</td>
                       <td><button class="cxbtn" onClick="pedometer.search('${movement.movementId}')">查 询</button></td>
                       <td><button class="cxbtn" onClick="pedometer.openWin('${company_id}','${movement.movementId }')">用户维护</button></td>
                       <td><button class="cxbtn" onClick="history.back();"> 返回  </button></td>
                    </tr>
                </table>
            </div>
	    </div>
	    
	    <p class="line"></p>
	    
	    <div class="gridWrap">
	        <div class="gridHead"><b>"${movement.movementName }"活动用户列表</b></div>
	        <div class="gridCont">
	            <table class="gridTable" style="width:100%;" cellpadding="0" cellspacing="0">
	                <tr>
	                	<th>姓名</th>
	                	<th>部门</th>
	                	<th>设置</th>
	                </tr>
	                
	                <c:forEach var="searchResult" varStatus="status" items="${pagination.result }">
	                	<tr>
		                	<td>${searchResult.employee_name }</td>
		                	<td>${searchResult.department_name }</td>
		                	<td>
		                		<a href="javascript:pedometer.deleteUsers('${searchResult.movement_id}','${searchResult.employee_id }')" onclick="void(0)">
		                			删除
		                		</a>
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
