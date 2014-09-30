<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/headship.js"></script>
</head>

<body >
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
		
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/pheadship/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
		<input id="key" name="key" type="hidden" value="${key}"/>
	</form>
	



<!--body-->

<!--body正文-->
<div class="nowbg">您当前位置 -- 职位管理</div>
        <div class="f_l lever_search" style="margin:9px 72px">
		     <table border="0" cellspacing="0" cellpadding="0" >
		       <tr>
		        <td width="200" ><input id="headship_name" name="headship_name" type="text" class="opand" value="${key}" size="20" onkeydown="Headship.onkeydown(event)"/></td>
		        <td><a href="#" onClick="Headship.search()"><img src="/resources/images/btn_search.gif" /></a></td>
		       <td width="480"></td>
		       <td><div class="lis_btn"><a href="#" onclick="Headship.batchDelete()"><img src="/resources/images/btn_plsc.png" />批量删除</a></div></td>
		      	<td><div class="lis_btn"><a href="javascript:void(0)" onclick="Headship.toAddHeadship()" ><img src="/resources/images/btn_tjbm.png" />添加职位</a></div></td>
		       </tr>
		     </table>     
   		</div>
 
        <div class="tabsI">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <th width="60">&nbsp;</th>
                <th>职位</th>
                <th>职位级别</th>
                <th width="200">操作</th>
              </tr>
              <c:forEach items="${pagination.result}" var="result">
              <tr>
                <td align="center"><input type="checkbox" name="checkbox" id="checkbox" value= '${result.headship_id}'/>
                <label for="checkbox"></label></td>
                <td align="center">${result.headship_name}</td>
                <td align="center">${result.headship_level}</td>
                <td>
                  <div class="tab_btn"><a href="javascript:Headship.updateHeadship('${result.headship_id}')"><img src="/resources/images/btn_bj.png" />编 辑</a></div>
                  <div class="tab_btn"><a href="javascript:Headship.deleteHeadship('${result.headship_id}')"><img src="/resources/images/btn_sccy.png" />删 除</a></div>
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
<div id="headshipWin"></div>
</body>
</html>
