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
<script type="text/javascript" src="/resources/js/web/companyMap.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />		
 </head>
 
<body>
	<%@include file="/WEB-INF/views/top.jsp" %>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
		
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/companyMap/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
	</form>
	
	
	<div class="fulltd" >
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
   	    <tr>
     	 <td class="conts">
		<div class="nowbg">您当前位置 -- 系统维护 -- 组织地图</div>
			
			<form id="comp_map" method="post" enctype="multipart/form-data" autocomplete = "off" >
				<input id="companyIds" name="companyIds" type="hidden"/>
				<input id="map" name="map" readOnly value="" type="text">
				<input id="mapZip" name="mapZip" type="file" class="file" onchange="document.getElementById('map').value=this.value"/>
                <input id="allCompany" value="--请选择--" name="allCompany" type="text" style="width: 230px"/>
                <div class="f_r lis_btn lis_O"><a href="javascript:void(0);" onclick="CompanyMap.addRecord();"><img src="/resources/images/btn_tjbm.png" />添加地图</a></div>
			</form>
		
	        <div class="tabsI">
	          <table width="100%" border="0" cellpadding="0" cellspacing="0">
	             <tr>
	                <th width="120">企业</th>
	                <th width="70">更新时间</th>
	                <th width="30">版本号</th>
	              </tr>
	               <c:forEach items="${pagination.result}" var="result">
			      <tr>
			         <td align="center" class="ck_table4" title="${result.company_name}">
			        	 ${result.company_name}
			        </td>
			        <td align="center">${result.updateDate}</td>
			        <td align="center">${result.version}</td>
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