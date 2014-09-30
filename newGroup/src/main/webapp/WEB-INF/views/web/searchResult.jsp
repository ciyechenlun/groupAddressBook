<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/js/web/searchResult.js"></script>
<style type="text/css">
.combo-text{
 font-size: 15px;
 height: 30px;
 color: #313C50;
 font-family: "微软雅黑";
}
.combo {
    border: 1px solid #E9E9E9;
}
.combo-arrow {
    background: url("/resources/scripts/easyui/themes/default/images/combo_arrow.gif") no-repeat scroll 7px 8px #E9E9E9;
    cursor: pointer;
    display: inline-block;
    height: 30px;
    opacity: 0.6;
    overflow: hidden;
    vertical-align: top;
    width: 25px;
}
.combobox-item {
   color: #313C50;
    font-family: "微软雅黑";
    font-size: 15px;
    padding: 5px 0 3px 3px;
}
.combo-panel {
   border: 1px solid #E9E9E9;
}
.combobox-item-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
</style>
</head>

<body style="padding:0 15px 10px 15px">
	 <div class="nowbg">您当前位置 -- 搜索结果页</div>
	 	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
		<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
		<input type="hidden" id="currPage" value="${pagination.currPage}" />

	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/lookGroup/searchEmployee.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="4" />
		<input id="companyId" name="companyId" type="hidden" value="${company.companyId}"/>
		<input id="key" name="key" type="hidden" value="${key}"/>
	</form>
	   <c:choose>
          <c:when test="${empty pagination.result}">
          	 <div class="sechnone">
			          抱歉！暂无相关内容<img src="/resources/images/nonepic.gif" />
			 </div>
		</c:when>
		<c:otherwise>
          		<c:forEach items="${pagination.result}" var="searchResult">
        <div class="sechlist">
          <div class="sechadd">
            <div class="f_l"><span><strong>
            <c:set var="keyRe" value="${'<font color=red>'}${key}${'</font>'}"></c:set>   
            ${fn:replace(searchResult.employee_name,key,keyRe)}</strong></span><span>${searchResult.headshipName}</span>${searchResult.department_name}</div>
            <div class="f_r sechbtn"><div style="cursor:pointer" class="f_l bianji" onclick="searchRe.editUser('${searchResult.user_company_id}','${searchResult.department_id}')"></div>
            <div style="cursor:pointer"  class="f_l dels" onclick="searchRe.deleteUser('${searchResult.user_company_id}','${searchResult.department_id}')"></div></div>
          </div>
          <div class="sechtop">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="80" rowspan="2"><img src="/resources/images/open_pic.png" /></td>
                <td width="130"><img src="/resources/images/mobil.png" />${fn:replace(searchResult.mobile,key,keyRe)}</td>
                <td width="140"><img src="/resources/images/phone.png" />${fn:replace(searchResult.telephone2,key,keyRe)}</td>
                <td><img src="/resources/images/company.png" />部门显示顺序：${searchResult.relative_order}</td>
              </tr>
              <tr>
                <td><img src="/resources/images/mobil.png" />${fn:replace(searchResult.mobile_short,key,keyRe)}</td>
                <td><img src="/resources/images/phone.png" />${fn:replace(searchResult.tel_short,key,keyRe)}</td>
                <td><img src="/resources/images/id.png" />V网id：${searchResult.grid_number}</td>
              </tr>
            </table>
          </div>          
        </div>
	</c:forEach>
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
        </c:otherwise>
	</c:choose>
	 
        <div id="addEmployee"></div>
        <div id="setManager">
        	<ul id="man_tree" style="margin-left:30px;margin-top:20px"></ul>
        </div>
</body>
</html>
