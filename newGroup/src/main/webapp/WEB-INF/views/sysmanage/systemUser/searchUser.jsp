<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>" />
	<script type="text/javascript" src="/resources/js/sysmanage/systemUser/searchUser.js"></script>
</head>

<body>
	<div  class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false,title:'用户查询'"
					style="margin-left: -1px;">
					<table id="searchUser"></table>
				</div>
			</div>
		</div>
        
		<div id="toolBar">
		开通时间:从 <input id="createStartDate" type="text" class="easyui-datebox"  /> 
		到 <input id="createEndDate" type="text" class="easyui-datebox" />&nbsp;&nbsp;
		停用时间:从<input id="stopStartDate" type="text" class="easyui-datebox"  /> 
		到<input id="stopEndDate" type="text" class="easyui-datebox" />
		<a href="javascript:void(0);" onClick="SearchUser.doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a>
		</div>
		
    </div>
</body>
</html>