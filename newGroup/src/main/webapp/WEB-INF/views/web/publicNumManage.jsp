<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>回收站</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/publicRoadManager.js"></script>
<style type="text/css">
.obj_long{float:left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; width: 280px;}
.dept_long{float:left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; width: 200px;}
</style>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
	<input type="hidden" id="manager" value="${manager}" />

	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/publicRoadManager/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
		<input id="companyId" name="companyId" type="hidden" />
		<input type="hidden" id="manager" name="manager" value="${manager}" />
	</form>
	<c:if test="${manager eq '1' }">
     <div style="padding-right:81px;float:right;">
     	 <div class="lis_btn"><a href="javascript:void(0)" onclick="PublicRoadManager.addPublicRoad();"><img src="/resources/images/btn_tjcy.png" />添加公告号</a></div>
     </div>
     </c:if>
     <div class="tabsI" style="padding-top:20px;">
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
           <tr>
             <th width="80">序号</th>
             <th width="200">名称</th>
             <th width="280">公告对象</th>
             <th width="280">管理员</th>
             <th>操作</th>
           </tr>
           <c:forEach items="${pagination.result}" var="searchResult" varStatus="status">
           <tr>
             <td align="center"><div title="${status.index + 1+8*(pagination.currPage-1)}">${ status.index + 1+8*(pagination.currPage-1)}</div></td>
             <td align="center"><div title="${searchResult.public_name}" class="dept_long">${searchResult.public_name}</div></td>
             <td align="center"><div title="${searchResult.to_name}" class="obj_long">${searchResult.to_name}</div></td>
             <td align="center"><div title="${searchResult.manager}" class="obj_long">${searchResult.manager}</div></td>
             <td align="center">
             	<a href="#" onclick="PublicRoadManager.toEdit('${searchResult.public_id}');"> <img src="/resources/images/icon_edit.png"/></a>
	            	<c:if test="${manager eq '1' }">
		             <a href="#" onclick="PublicRoadManager.changeStatus('${searchResult.public_id}','${searchResult.status}');" title="修改状态"> <img src="/resources/images/icon_tjggh.png"/></a>
		       		 <a href="#" onclick="PublicRoadManager.deleteInfo('${searchResult.public_id}');" title="删除"><img src="/resources/images/icon_delete.gif"/></a>
	       	 	</c:if>
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
	<!--end：分页--> 
</body>
</html>
