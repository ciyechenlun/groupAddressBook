<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>集团通讯录</title>
  <%@include file="../common/baseIncludeJs.jsp" %>
  <link href="/resources/css/main.css" rel="stylesheet" type="text/css" />
  <link href="/resources/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/js/web/advise.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />		
 </head>
 
<body>
	<%@include file="/WEB-INF/views/top.jsp" %>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
		
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/advise/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
	</form>
	<div class="fulltd" >
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
   	    <tr>
     	 <td class="conts">
		<div class="nowbg">您当前位置 -- 系统维护 -- 使用反馈</div>
	        <div class="tabsI">
	          <table width="100%" border="0" cellpadding="0" cellspacing="0">
	             <tr>
	                <th width="75">反馈人</th>
	                <th width="90">单位</th>
	                <th width="100">手机号码</th>
	                <th>反馈内容</th>
	                <th width="150">反馈时间</th>
	                <th width="60">状态</th>
	                <th width="70">操作</th>
	              </tr>
	               <c:forEach items="${pagination.result}" var="result">
			      <tr>
			        <td align="center">${result.adviseMan}</td>
			       
			         <td align="center" class="ck_table4" title="${result.company_name}">
			        	<c:choose>
							<c:when test="${fn:length(result.company_name)>5}">
								<c:out value ="${fn:substring(result.company_name,0,4)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${result.company_name}"/>
							</c:otherwise>
						</c:choose>
			        </td>
			        <td align="center">${result.mobile}</td>
			          <td align="center" class="ck_table4" title="${result.content}">
			        	<c:choose>
							<c:when test="${fn:length(result.content)>25}">
								<c:out value ="${fn:substring(result.content,0,25)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${result.content}"/>
							</c:otherwise>
						</c:choose>
			        </td>
			        <td align="center">${result.adviseDate}</td>
			       <!-- <c:choose>
							<c:when test="${fn:length(result.content)>25}">
								<td align="center"><span>未回复</span></td>
							</c:when>
							<c:otherwise>
								<td>已回复</td>
							</c:otherwise>
						</c:choose>
					--> 
					<td align="center"><span>未回复</span></td>
			        <td align="center">
				         <div><a href="#" onclick="javascript:Advise.toReply('${result.adviseId}','${result.content}')"; class="f_l mails"></a></div>
				         
				         
				         <div ><a href="#" onclick="javascript:Advise.deleteAd('${result.adviseId}')" class="f_l dels"></a></div>
			        </td>
			      </tr>
			     </c:forEach>
	           </table>
	        </div>
			<!--start:分页-->
			<div class="xw_paga">
					<div class="paginations" name="Pagination"  style="margin-left: 80px;"></div>
					<span>共${pagination.totalRecords}条记录</span>
					<span>
						到第
						<input type="text" name="toTargetPage" style="width: 25px;" value="${pagination.currPage}" />
						<span>页</span>
						<input  class="di_an" type="button" value="确定" id="J_JumpTo" name="toTargetPage_btn" />
					</span>
				</div>
			</div>
		<!--end：分页-->
		 </td>
    </tr>
  </table>
  </div>
<div id="toReply"></div>
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>

<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
</body>

</html>