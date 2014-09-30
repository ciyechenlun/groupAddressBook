<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="/resources/js/sysmanage/menu/menu.js"></script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false" style="margin-left:-1px;">
        	<table id="menu"></table>
        </div>
    </div>
    
    <!-- 工具条 -->
	<div id="tbar" style="margin-top:3px;width:99%;text-align: right;">
		<input id="serarch" class="easyui-searchbox" data-options="prompt:'请输入菜单名称 查询',searcher:Menu.doSearch" /> 
	</div>
</body>
</html>