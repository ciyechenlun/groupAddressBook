<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>集团通讯录节假日皮肤管理</title>
		<%@include file="../common/baseIncludeJs.jsp" %>
		<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
		<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
		<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
		<script type="text/javascript" src="/resources/js/holidayskin/list.js"></script>
		<script type="text/javascript" src="/resources/scripts/common.js"></script>
		<script type="text/javascript" src="/resources/js/index/header.js"></script>
	</head>
	
	<body>
		<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
		<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
		<input type="hidden" id="currPage" value="${pagination.currPage}" />
			
		<!-- 表单数据提交发送请求 -->
		<form id="searchForm" method="post" action="/pc/holidaySkin/main.htm">
			<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="7" />
		</form>
	
		<!--body正文-->
		<div class="bodyqj bodybg fn_clear" style="height:500px;">
			<div class="ck_left2">
				<div class="ck_rightbg">节假日皮肤管理</div>
				<div class="margintop ">
					<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td width="41%" class="ck_rwenzi"></td>
							<td width="20%">
								<input type="submit" class="bottom_01" value="编辑皮肤" onclick="Holidayskin.eidtSkin();" />
							</td>
							<td width="20%">
								<input type="submit" class="bottom_01" value="增加皮肤" onclick="Holidayskin.addSkin();" />
							</td>
							<td width="19%">
								<input type="submit" class="bottom_01" value="删除皮肤" onclick="Holidayskin.deleteSkin();" />
							</td>
						</tr>
					</table>
				</div>
				<div class="margintop ck_top">
				    <table class="ck_table5 margintop" border="1" cellspacing="0" cellpadding="0" rules="all" bordercolor="#e6e6e6">
				    	<tr style="height: 40px">
				    		<td class="ck_table1"></td>
				    		<td align="center" style="width:20%"><strong>皮肤名称</strong></td>
			                <td align="center" style="width:25%"><strong>节假日名称</strong></td>
			                <td align="center" style="width:18%"><strong>开始日期</strong></td>
			                <td align="center" style="width:18%"><strong>结束日期</strong></td>
			                <td align="center" style="width:14%"><strong>是否循环使用</strong></td>
		                </tr>
					    <c:forEach items="${pagination.result}" var="skin">
					    	<tr style="height: 40px">
						        <td class="ck_table1">
						        	<input name="radioName" type="radio" value='${skin.holidayskinId}' />
					        	</td>
						        <td align="center" class="ck_table4">${skin.holidayskinName}</td>
						        <td align="center" class="ck_table4">${skin.holidayName}</td>
						        <td align="center" class="ck_table4">
						        	<fmt:formatDate value="${skin.holidayskinStartDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						        </td>
						        <td align="center" class="ck_table4">
						        	<fmt:formatDate value="${skin.holidayskinEndDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						        </td>
						        <td align="center" class="ck_table4">
						        	<c:choose>
						        		<c:when test="${skin.holidayskinRepeat == '1'}">是</c:when>
						        		<c:when test="${skin.holidayskinRepeat == '0'}">否</c:when>
						        		<c:otherwise></c:otherwise>
						        	</c:choose>
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
									<div class="paginations" name="Pagination"></div>
									<span>共${pagination.totalRecords}条记录</span>
									<span>
										到第
										<input type="text" name="toTargetPage" style="width: 25px;" value="${pagination.currPage}" />
										<span>页</span>
										<input type="button" value="确定" id="J_JumpTo" name="toTargetPage_btn" />
									</span>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!--end：分页-->
			</div>
		</div>
	
		<!-- 添加皮肤 start -->
		<div class="bg2 win" id="skinWindow" style="display: none;">
			<!--导航-->
			<div id="toggleDelEdite" class="dh_01 zw_top">添加节假日皮肤</div>
			<!--导航结束-->
			<!-- 标签页开始 -->
			<div class="zw_cen2" style="overflow-x:hidden; overflow-y:auto;">
				<form id="skinForm" method="post" enctype="multipart/form-data" action="">
					<input id="holidayskinId" name="holidayskinId" type="hidden"/>
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td class="xz_zi tc_w">
								<span class="tc_red">*</span>皮肤名称:
							</td>
							<td>
								<input id="holidayskinName" name="holidayskinName" type="text" size="20" class="inputzw02" />
							</td>
						</tr>
						<tr>
							<td class="xz_zi tc_w">
								<span class="tc_red">*</span>节假日名称:
							</td>
							<td>
								<input id="holidayName" name="holidayName" type="text" size="20" class="inputzw02" />
							</td>
						</tr>
						<tr>
							<td class="xz_zi tc_w">
								<span class="tc_red">*</span>开始日期:
							</td>
							<td>
								<input id="holidayskinStartDate" name="holidayskinStartDate" style="width:200px;" class="easyui-datetimebox"/>
							</td>
						</tr>
						<tr>
							<td class="xz_zi tc_w">
								<span class="tc_red">*</span>结束日期:
							</td>
							<td>
								<input id="holidayskinEndDate" name="holidayskinEndDate" style="width:200px;" class="easyui-datetimebox"/>
							</td>
						</tr>
						<tr>
							<td class="xz_zi tc_w">
								<span class="tc_red">*</span>皮肤地址:
							</td>
							<td>
								<input id="holidayskinPath" name="holidayskinPath" type="hidden"/>
								<input id="skinFile" name="skinFile" type="file" class="inputzw02" />
							</td>
						</tr>
						<tr>
							<td class="xz_zi tc_w">是否循环使用:</td>
							<td>
								<select id="holidayskinRepeat" name="holidayskinRepeat" class="easyui-combobox" style="width:200px;" panelHeight="auto">
									<option value="">请选择</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div align="center">
									<input type="button" class="bottom_01" value="确定" onclick="Holidayskin.saveSkin();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" class="bottom_01" value="取消" onclick="Holidayskin.closeWin();" />
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="zw_bottom"></div>
		</div>
		<!-- 添加皮肤   end  -->
	</body>
</html>