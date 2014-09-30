<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript"
	src="/resources/js/sysmanage/role/role.js"></script>
	<style>
.tree-file{background: url(/resources/img/icons/icon_nav.png) no-repeat;}
.tree-icon{background: url(/resources/img/icons/icon_nav.png) no-repeat;}
</style>
</head>
<body>
	<!-- 当前登陆人员角色 -->
	<input id="roleCodeForAdd" type="hidden"  value="${roleCode}"/>

	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false,title:'角色信息列表'">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false"
					style="margin-left: -1px;">
					<table id="roletable"></table>
				</div>
			</div>
		</div>

		<!-- 工具条 -->
		<div id="tbar" style="margin-top: 3px; width: 99%; text-align: right;">
			<input id="serarch" class="easyui-searchbox"
				data-options="prompt:'输入关键字查询  例如：角色名称',searcher:Role.doSearch"/>
		</div>
	</div>
</body>
</html>