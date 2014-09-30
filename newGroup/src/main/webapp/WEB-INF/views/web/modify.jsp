<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/modify.js"></script>
<script language="JavaScript">
	$(function() {
		/* var companyType = window.parent.document.getElementById("companyType").value;
		if (companyType == "bnewleft01 bnewleft01a liGetSelected"
				|| companyType == "bnewleft01 bnewleft01a bnewlefton liGetSelected") {
			$("#grid").removeClass("ck_left2");
			$("#grid").addClass("ck_left1");
			$("#tree").show();
		} */
		$("#companyId").val(window.parent.document.getElementById('companyId').value);
	});
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />

	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/userModify/list.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="8" />
		<input id="companyId" name="companyId" type="hidden" />
	</form>
	<div class="nowbg">您当前位置 -- 审核修改</div>
        <div class="tabsI">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <th>提交人</th>
                <th>手机号码</th>
                <th width="200">操作</th>
              </tr>
               <c:forEach items="${pagination.result}" var="result">
		      <tr>
		        <td align="center">${result.employee_name}</td>
		        <td align="center">${result.mobile}</td>
		        <td align="center">
			        <div class="tab_btn">
				        <a href="javascript:void(0)" onclick="Modify.modify('${result.user_modify_id}');">
				        	<img src="/resources/images/btn_sh.png" />审 核
				        </a>
			        </div>
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
<div id="modifyAudit"></div>
</body>
</html>
