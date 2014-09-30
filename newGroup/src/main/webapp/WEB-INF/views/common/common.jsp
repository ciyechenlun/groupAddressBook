<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="icon" href="/resources/img/favicon.ico" mce_href="/resources/img/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="/resources/img/favicon.ico" mce_href="/resources/img/favicon.ico" type="image/x-icon" />

<script type="text/javascript" src="/resources/js/jquery.js"></script>
<link href="/resources/js/common/loadmask/jquery.loadmask.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/js/common/loadmask/jquery.loadmask.min.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/common/icons.css">
<link rel="stylesheet" type="text/css" href="/resources/css/common/starit.base.css">

<script src="/resources/js/common/artDialog/artDialog.js?skin=default"></script>
<script src="/resources/js/common/artDialog/plugins/iframeTools.js"></script>


<link rel="stylesheet" type="text/css" href="/resources/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/js/easyui/themes/icon.css">
<script type="text/javascript" src="/resources/js/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="/resources/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/resources/js/common/common.js"></script>

<input id="basePath" type="hidden" value="<%=basePath%>"/>
<script type="text/javascript">
   	var basePath = document.getElementById("basePath").value;
</script> 
<div id="commonWindow" class="easyui-window" data-options="closed:'true'"></div>
