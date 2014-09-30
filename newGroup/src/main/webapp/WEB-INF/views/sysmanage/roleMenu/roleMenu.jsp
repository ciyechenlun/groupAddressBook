<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<script type="text/javascript" src="/resources/js/sysmanage/roleMenu/roleMenu.js"></script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false,title:'角色列表'">
	    	<div class="easyui-layout" data-options="fit:true">
	            <div data-options="region:'center',border:false" style="margin-left:-1px;">
	                 <table id="roletable"></table>
	            </div>
	        </div>
    	</div>
	
		<div id="menuTree" data-options="title:'菜单树',region:'east',border:false" style="width:300px;border-right:1px #ccc solid;margin-left:-1px;">
        	<ul id="role_menu"></ul>
        </div>
    </div>
</body>
</html>