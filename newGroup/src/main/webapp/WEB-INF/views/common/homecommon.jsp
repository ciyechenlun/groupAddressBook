<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="/resources/js/jquery.js"></script> 
<link href="/resources/js/common/loadmask/jquery.loadmask.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resources/js/common/loadmask/jquery.loadmask.min.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/resources/js/common/easyui-portal/portal.css">
<script type="text/javascript" src="/resources/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/common/easyui-portal/jquery.portal.js"></script>
<script type="text/javascript" src="/resources/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/resources/js/common/common.js"></script>
<script type="text/javascript" src="/resources/js/common/pushlet/ajax-pushlet-client.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/index/home.css">

<input id="basePath" type="hidden" value="<%=basePath%>"/>
<script type="text/javascript">
   	var basePath = document.getElementById("basePath").value;
</script> 
<div id="commonWindow" class="easyui-window" data-options="closed:'true'"></div>
