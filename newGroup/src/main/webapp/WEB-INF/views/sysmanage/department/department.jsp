<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="<%=basePath%>" />
<script type="text/javascript" src="/resources/js/sysmanage/department/department.js"></script>
</head>
<body>
	<div id="grid" style="width:100%;height:100%;">
		
	</div>
	
	<!-- 工具条 -->
	<div id="tbar" style="margin-top:3px;width:99%;text-align: right;">
		<input id="serarch" class="easyui-searchbox" data-options="prompt:'请输入部门名称查询 ',searcher:Department.doSearch" /> 
	</div>
	
	<!-- 菜单树右键菜单 -->
	<div style="display: none;">
		<div id="mm" class="easyui-menu" style="width: 120px">
			<div onclick="Department.reload()" iconCls="icon-reload">刷新</div>
		</div>
	</div>
</body>
</html>