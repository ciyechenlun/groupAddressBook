<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>草稿箱</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/roadDraftbox.js"></script>
<style type="text/css">
.obj_long{float:left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; width: 380px;}
.dept_long{float:left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; width: 180px;}
</style>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />

	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/publicRoadManager/toDraftbox.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
		<input id="companyId" name="companyId" type="hidden" />
	</form>
	<div class="nowbg">您当前位置 -- 草稿箱</div>
     <div class="tabsI">
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
           <tr>
             <th width="80">序号</th>
             <th width="180">名称</th>
             <th width="150">保存时间</th>
             <th width="380">公告标题</th>
             <th>操作</th>
           </tr>
           <c:forEach items="${pagination.result}" var="searchResult" varStatus="status">
           <tr>
             <td align="center"><div title="${status.index + 1+8*(pagination.currPage-1)}">${ status.index + 1+8*(pagination.currPage-1)}</div></td>
             <td align="center"><div title="${searchResult.public_name}" class="dept_long">${searchResult.public_name}</div></td>
             <td align="center"><div title="${searchResult.save_time}">${searchResult.save_time}</div></td>
             <td align="center"><div title="${searchResult.notice_title}" class="obj_long">${searchResult.notice_title}</div></td>
             <td align="center">
	             <a href="#" onclick="RoadDraftbox.toDetailInfo('${searchResult.notice_id}');" title="显示详情"> <img src="/resources/images/icon_tip.gif"/></a>
	             <a href="#" onclick="RoadDraftbox.deleteInfo('${searchResult.notice_id}');" title="删除"><img src="/resources/images/icon_delete.gif"/></a>
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
