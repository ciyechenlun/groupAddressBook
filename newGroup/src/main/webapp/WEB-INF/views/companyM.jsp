<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>

<%@include file="common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<style type="text/css">
.lis_O {
    line-height: 29px !important;
}
</style>
</head>

<body>
<!--top-->
<%@include file="top.jsp" %>

<input type="hidden" value="${org_list.totalRecords}" id="totalRecords" />
<input type="hidden" id="totalPages" value="${org_list.totalPages}" />
<input type="hidden" id="currPage" value="${org_list.currPage}" />
	
<!-- 表单数据提交发送请求 -->
<form id="searchForm" method="post" action="/toCompanyM.htm">
	<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
	<input name="key" type="hidden" value="${key}" />
	<input id="pageSize" name="pageSize" type="hidden" value="18" />
	<input type="hidden" name="allCount" value="${allCount}" />
</form>
<!--body-->
<div class="fulltd" >
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="conts">
      <div class="nowbg">
          <div class="f_l">您当前位置 -- 系统维护 -- <a href="/toCompanyM.htm">企业维护</a></div>
          <c:if test="${user.username=='s_admin'}">
          <div class="f_r lis_btn lis_O"><a href="javascript:void(0);" onclick="Ict.openWin('新增企业',580,480,'/pc/company/add.htm');"><img src="/resources/images/btn_tjbm.png" />新建企业</a></div>
        </c:if>
        </div>
        <div class="nowsm"></div>
        <div class="qylist">
        <c:forEach items="${org_list.result}" var="result">
       <div class="qyas">
            <a href="/index.htm?companyId=${result.company_id}">
             <c:choose>
            <c:when test="${result.index_pictrue !=''&& result.index_pictrue!=null && fn:indexOf(result.index_pictrue,'.zip')>0 }">
            	<img src="/pc/company/images/${result.index_pictrue}" class="qypic" />
				</c:when>
				<c:otherwise>
            		<img src="/resources/images/imga.png" class="qypic" />
            		</c:otherwise>
				 </c:choose>
            
            <div title="${result.company_name}">${result.company_name}</div></a>${result.pCount}人
            <c:if test="${manager=='1' || result.manage_flag=='1'}">
            <a href="#" onclick="Ict.openWin('修改企业信息',580,480,'/pc/company/update.htm?companyId=${result.company_id}');"><img src="/resources/images/ico_gly.png" /></a>
            </c:if><br />
            <c:choose>
            <c:when test="${result.create_date != null}">
            <fmt:formatDate value="${result.create_date}" pattern="yyyy-MM-dd "/>
            </c:when>
            <c:otherwise>
            <br />
            </c:otherwise>
            </c:choose>
          </div>
    	</c:forEach>
        </div>
        	<!--start:分页-->
			<div class="xw_paga">
				<div class="paginations" name="Pagination"  style="margin-left: 80px;"></div>
				<span>共${org_list.totalRecords}条记录</span>
				<span>
					到第
					<input type="text" name="toTargetPage" style="width: 25px;" value="${org_list.currPage}" />
					<span>页</span>
					<input  class="di_an" type="button" value="确定" id="J_JumpTo" name="toTargetPage_btn" />
				</span>
			</div>
			<!--end：分页--> 
      </td>
    </tr>
  </table>
</div>
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>

<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>

</body>
</html>
