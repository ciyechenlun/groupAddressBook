<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<script type="text/javascript" src="/resources/js/sysmanage/metaData/metaData.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false,title:'元数据列表'">
			<div class="easyui-layout" data-options="fit:true">
				<!--start:表格头部button-->
				<!-- <div data-options="region:'north',border:false" style="height:38px;overflow:hidden;">
         
                <div class="finder-action">
                    <div class="finder-button">
                        <a href="javascript:toDict()" class="easyui-linkbutton" plain="true" menu="#mm4" iconCls="starit-batch-del">指定区域</a>
                        <a href="javascript:deleteToDict()" class="easyui-linkbutton" plain="true" menu="#mm4" iconCls="starit-batch-del">批量取消</a>
                        <a href="javascript:updaterow()" class="easyui-linkbutton" plain="true" menu="#mm2" iconCls="icon-edit">刷新</a>
                    </div>
                    
                    <div class="finder-search">
                         <input type="text"  style="width:150px;float:left;_margin-right:-3px;" value="关键字..."/><a href="#"><img src="resources/icon/icon-search1.png" alt="搜索" /></a>
                    </div>
                    
                </div>
            </div>  -->

				<div data-options="region:'center',border:false" style="margin-left: -1px;">
					<table id="metaGrid"></table>
				</div>
			</div>
		</div>

		<div data-options="title:'元数据类型列表',region:'west',border:false"
			style="width: 200px; border-right: 1px #ccc solid; margin-left: -1px;">
			<table id="metaConfigGrid"></table>
		</div>
	</div>
</body>
</html>
