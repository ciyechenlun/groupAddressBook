<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录导入信息</title>
<%@include file='../common/baseIncludeJs.jsp' %>
<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
<link href="/resources/public/jupload/css/jquery.fileupload-ui.css" rel="stylesheet"/>
<noscript><link href="/resources/public/jupload/css/jquery.fileupload-ui-noscript.css" rel="stylesheet"/></noscript>
<style type="text/css">

.navBar{
	padding: 5px;
	border-bottom: solid 1px silver;
}

.navBar a{
	text-decoration: none;
	font-size: 12px;
}

.navBar a:HOVER{
	text-decoration: none;
	font-size: 12px;
}
.navLink{
 text-decoration: none;
}

.navLink:HOVER{
 text-decoration: none;
}

.redFont{
	color:red;
}

.previewTable{
	width:auto;
	border: 1px solid silver;
}

.previewTable th{
	width:130px;
	height:40px;
	background-color: #5CACEE;
}

.previewTable td{
	width:130px;
	border: 1px solid silver;
}
body{backgroud-color:#FFF;background-image:url();}
</style>
</head>

<body>

<!--body正文-->
	<!--right-->
	<div id="importDivs" class="bnewright bodybg fn_clear">
		<div class="navBar">
			<a class="navLink" name="mbImportDiv" href="javascript:void(0);" style="color:blue;">模板导入</a>|
		</div>
		<div class="contentDiv" id="mbImportDiv">
			<div class="margintop30 bnewrightbottom paddingbottom">
			    <form id="importForm" method="post" enctype="multipart/form-data" action="">
			    	<input type="hidden" id="companyId" name="companyId"/>
			        <table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
			          <tr id="org">
			            <td width="100"><input id="importFile" name="importExcel" type="file" class="bnewrightip1" value="导入" /></td>
			            <td><input id="importBtn" onclick="excelImport()" value="导入" type="button" class="bottom_01"/></td>
			          </tr>
			          <tr id="not_org" style="display: none;">
			            <td width="100"><input id="importFile2" name="importExcel2" type="file" class="bnewrightip1" value="导入个人通讯录" /></td>
			            <td><input id="importButton2" value="导入个人通讯录" type="button" class="bottom_01"/></td>
			          </tr>
			        </table>
			    </form>
		    </div>
		    <div class="margintop30">
		  	    <span class="bnewzi">使用说明</span>
		        <ul class="bnewzih">
		            <li>1.请将组织架构与联系人资料按模板内格式填写上传。</li>
		            <li>2.第一次导入较多的信息可能耗时较长，请您耐心等待。</li>
		            <li>3.更新部分联系人信息时，建议您仅仅上传本次修改的内容。点击此处下载。</li>
		            <li>4.信息每次上传会替代原本的信息，请在上传前检查您的输入是否完全正确！</li>
		            
		        </ul>
		    </div>
		    <div style="display: none">
		    	<form id="excelExport" method="post" action="">
		    	</form>
		    </div>
		</div>
		
		<div class="contentDiv"  id="smartImportDiv" style="display: none;">
			<div class="margintop30 bnewrightbottom paddingbottom">
				<div id="step1">
					<form id="smartUploadForm" name="smartUploadForm" method="post" enctype="multipart/form-data">
						<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
				          <tr>
				            <td width="100"><input id="uploadFile" name="excel" type="file" class="bnewrightip1" value="导入" /></td>
				            <td><input id="smartUploadBtn" value="上传" type="button" class="bottom_01"/></td>
				          </tr>
				          <tr>
				          	<td id="msg" style="text-align: left;padding-top: 20px;display: none;" colspan="2">
				          		
				          	</td>
				          </tr>
				          <tr>
				          	<td id="progressInfo" style="text-align: left;padding-top: 20px;padding-bottom: 20px;display: none;" colspan="2">
				          		
				          	</td>
				          </tr>
				          <tr>
				          	<td id="nextStepBtnDiv" style="text-align: left;padding-top: 20px;padding-bottom: 20px;display: none;" colspan="2">
				          	</td>
				          </tr>
				        </table>
				    </form>
				    <div id="previewDivTitle" style="margin: 0 10px 0 10px;display: none;">结果预览:</br></br></div>
				    <div id="previewDiv" style="height:240px;overflow-x:scroll;overflow-y:scroll;margin: 0 10px 0 10px;display: none;" >
				    </div>
				</div>
			</div>
			
			<div class="margintop30">
		  	    <span class="bnewzi">使用说明</span>
		        <ul class="bnewzih">
		            <li>1.智能导入能够智能识别excel文件的列内容，进行导入自动匹配。</li>
		            <li>2.第一次导入较多的信息可能耗时较长，请您耐心等待。</li>
		            <li>3.更新部分联系人信息时，建议您仅仅上传本次修改的内容。点击此处下载<a href="#" onclick="Import.excelExport();">当前联系人资料</a>。</li>
		            <li>4.信息每次上传会替代原本的信息，请在上传前检查您的输入是否完全正确！</li>
		        </ul>
		    </div>
		    
		    <div id="columnNameList" style="width:250px;height:300px; overflow-y: scroll;display: none;"> </div>
		</div>
	</div>

<script src="/resources/public/jquery.blockUI.js"></script>
<script type="text/javascript" src="/resources/public/jupload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/resources/public/jupload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="/resources/public/jupload/jquery.fileupload.js"></script>
<!--[if gte IE 8]><script type="text/javascript" src="/resources/public/jupload/cors/jquery.xdr-transport.js"></script><![endif]-->
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>
<script type="text/javascript" src="/resources/js/web/import.js"></script>
<script type="text/javascript">
<!--
$(function(){
	$('.navLink').click(function(){
		$('.navLink').css({'color':'#0066cc'});
		$(this).css({'color':'blue'});
		var id = $(this).attr("name");
		$('.contentDiv').hide();
		$('#'+id).show();
	});
});

function excelImport (){
	if($("#importFile").val()==""){
		$.messager.alert("提示","<br><center>请选择文件</center>");
		return;
	}
	$('#importDivs').showLoading();
	$('#importForm').form('submit',{
		url:'/mobile/pedometer/excelImport.htm',
		async:false,
		success:function(data){
			$('#importDivs').hideLoading();
			var result = data;
			$.messager.show({
				title:'导入结果',
				width: 300,
				height: 400,
				timeout:5000000,
				msg:result
			});
		}
	});
}


//-->
</script>
</body>
</html>
