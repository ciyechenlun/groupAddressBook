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
<script type="text/javascript" src="/resources/js/web/advise.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script language="JavaScript">
<!--
	function winShow(id){
		var w=Hg.get(id),b=document.body, cw=b.clientWidth,ch=b.scrollTop + 120;
		$(w).css('left',(cw -500)/2).css('top',parseInt(ch)).css('display','block')
	}
	function winHide(id){
		var w=Hg.get(id);
		w.style.display="none";
	}
	// onload事件里添加代码
	window.onload=function(){
		tabView("tab_head","tab_body","on","onclick");		
	}
//-->
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
		
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/interface/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
		<input id="pageSize" name="pageSize" type="hidden" value="10" />
	</form>
	
<!--top-->
<div class="bodyqj fn_clear">
<div class="bodylogo margintopjji f-left"></div>
<div class="f-right fn_clear">
    <div class="logor"></div>
    <div class="logoa">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>欢迎您！${sessionScope.SESSION_USER_NAME}</td>
        <td><img src="/resources/images/icon_top01.png" width="17" height="17" /></td>
        <td><a href="javascript:Header.changePwd();">修改密码</a></td>
        <td><img src="/resources/images/icon_top02.png" width="17" height="17" /></td>
        <td><a href="javascript:Header.logout();">安全退出</a></td>
      </tr>
    </table>
    </div>
    <div class="logob"></div>
</div>
</div>

<!--body-->
<div class="bodyqj newbody">
    <ul>
        <li>集团通讯录</li>
    </ul>
</div>

<!--body正文-->
<div class="bodyqj bodybg fn_clear">
<!--left-->
<%@include file="../left.jsp"%>
<div class="ck_right1">
	<div class="margintop ck_top">
	    <table class="ck_table5 margintop" border="1" cellspacing="0" cellpadding="0" rules="all" bordercolor="#e6e6e6">
	    	<tr style="height: 40px">
	                <td align="center" style="width:15%"><strong>调用人</strong></td>
	                <td align="center" style="width:55%"><strong>接口名称</strong></td>
	                <td align="center" style="width:20%"><strong>调用时间</strong></td>
	                <!-- <td align="center" style="width:10%"><strong>操作</strong></td> -->
	           </tr>
		    <c:forEach items="${pagination.result}" var="result">
		      <tr style="height: 40px">
		        <td align="center" class="ck_table4">${result.employee_name}</td>
		        <td align="center" class="ck_table4" title="${result.interface_name}">
		        	<c:choose>
						<c:when test="${fn:length(result.interface_name)>25}">
							<c:out value ="${fn:substring(result.interface_name,0,25)}..." />
						</c:when>
						<c:otherwise>
							<c:out value="${result.interface_name}"/>
						</c:otherwise>
					</c:choose>
		        </td>
		        <td align="center" class="ck_table4">${result.operateTime}</td>
		        
		        <%-- <td align="center" class="ck_table4"><a href="javascript:Advise.deleteAd('${result.id}')">删除</a></td> --%>
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
<!--bottom-->
<div class="bottombg">
    <ul>
        <li>Copyright © 2013-2015  安徽移动通信 All Rights Reserved 版权所有   维护电话：0551-66666666</li>
    </ul>
</div>

<div id="console"></div>

<!--弹出框 修改密码-->
<div class="bg2 win" id="window_password" style="display:none">
	<!--导航-->
	<div id="toggleDelEdite" class="dh_01 zw_top">
	修改密码
	</div>
	<!--导航结束-->
	<!-- 标签页开始 -->
		<div class="zw_cen1">
		<form id="addForm" method="post" action="">
	        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi"></td>
	            <td colspan="2" id="message" style="color: red;"></td>
	          </tr>
	          <tr>
	            <td class="xz_zi">旧密码：</td>
	            <td colspan="2"><input id="oldPassword" name="oldPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td class="xz_zi">新密码：</td>
	            <td colspan="2"><input id="newPassword" name="newPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	          <tr>
	            <td class="xz_zi">确认新密码：</td>
	            <td colspan="2"><input id="newPassword2" name="newPassword2" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td >&nbsp;</td>
	            <td colspan="2"><div align="left"><input id="addUserInfo" type="button" class="bottom_01" value="确定" onclick="Header.savePassword();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="" type="button" class="bottom_01" value="取消" onclick="Header.winHide('window_password')"/></div></td>
	             </tr>
	        </table>
        </form>
	    </div>
</div>
</body>
</html>
