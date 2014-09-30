<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/views/common/common.jsp"%>
	<base href="<%=basePath%>" />
	<script type="text/javascript" src="/resources/js/sysmanage/monitor/loginLog.js"></script>
</head>

<body>

	<!-- 当前登录人员的角色 -->
	<input id="roleCode" type="hidden"  value="${roleCode}"/>
	
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false" style="margin-left:-1px;">
        	<table id="company_tt"></table>
        </div>
    </div>
    
    <!-- 工具条 -->
	<div id="tbar" style="margin-top:3px;width:99%;text-align: right;">
		<input id="search" type="text" class="easyui-searchbox" data-options="prompt:'请输入公司名称',searcher:Company.doSearch" style="width:300px" /> 
	</div>
</body>
</html>