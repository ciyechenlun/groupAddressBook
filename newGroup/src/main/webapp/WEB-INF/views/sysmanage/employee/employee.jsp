<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<style>
.tree-file{background: url(/resources/img/icons/icon_nav.png) no-repeat;}
.tree-icon{background: url(/resources/img/icons/icon_nav.png) no-repeat;}
</style>
<script type="text/javascript" src="/resources/js/sysmanage/employee/employee.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false,title:'员工信息'">
					<table id="employeetable"></table>
		</div>

		<!-- 工具条 -->
		<!-- <div id="tbar" style="margin-top: 3px; width: 99%; text-align: right;">
			<input id="serarch" class="easyui-searchbox" data-options="prompt:'输入关键字查询  例如：员工名称',searcher:Employee.doSearch"/>
		</div> -->
		
		<div data-options="title:'部门树',region:'west',border:false" style="width:200px;border-right:1px #ccc solid;margin-left:-1px;">
			<ul id="employeeDeptTree"  class="easyui-tree"></ul>
		</div>
	</div>
</body>
</html>