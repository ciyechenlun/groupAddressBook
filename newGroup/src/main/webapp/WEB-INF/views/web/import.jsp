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
<link href="/resources/css/jquery-ui-1.10.4.css" rel="stylesheet"/>
<script src="/resources/public/jquery.blockUI.js"></script>
<script type="text/javascript" src="/resources/public/jupload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/resources/public/jupload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="/resources/public/jupload/jquery.fileupload.js"></script>
<script type="text/javascript" src="/resources/js/web/import.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery-ui-1.10.4.js"></script>
<style type="text/css">

.file {
    height: 32px;
    left: 180px;
    opacity: 0;
    position: absolute;
    top: 116px;
    width: 110px;
}
.table_border{  
    border: solid 1px #697A83;  
    border-collapse: collapse;     --折叠样式.  
}  
.table_border tr th{  
    border: solid 1px #697A83;  
}  
.table_border tr td{  
    border: solid 1px #697A83;  
} 
 .ui-progressbar {
position: relative;
}
.progress-label {
position: absolute;
left: 50%;
top: 4px;
font-weight: bold;
text-shadow: 1px 1px 0 #fff;
}
.loading_div{display:none;position: absolute; top:  0%; left:0%; width: 100%; height: 100%;background-color: white; z-index:1002;  -moz-opacity: 0.7;opacity:.70; filter: alpha(opacity=70);overflow: auto;}
</style>
</head>

<body >
 <div id="importDivs" class="loading_div">
 <div id="progressbar" style="width:500px">
 <div class="progress-label">正在导入...</div>
 </div>
 </div>
        <div class="nowbg">您当前位置 -- 导入通讯录</div>
        <div style="">
        <div class="drwj">
        <form id="importForm" method="post" enctype="multipart/form-data" action="">
		  <input type="hidden" id="companyId" name="companyId"/>
          <table border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="50" colspan="2"  class="t_l"><div class="lis_btn"><a href="#"><img src="/resources/images/btn_xzwj.png" />选择文件</a></div></td>
              </tr>
              <tr>
                <td width="400" class="t_l"><input id="textfield" name="textfield" type="text" class="opcek" />
                <input id="importFile" name="importExcel" type="file" class="file" onchange="document.getElementById('textfield').value=this.value"  /></td>
                <td class="t_l"><a href="javascript:Import.excelImport();"><span class="f_l opbtns"><img src="/resources/images/dr.png" />导　入</span></a></td>
              </tr>
              <tr>
                <td colspan="2" class="t_l drwj_fot">注意：请选择大小3M以内，格式为xls的文件</td>
              </tr>
          </table>
          </form>
        </div>
         <div style="display: none">
		    	<form id="excelExport" method="post" action="">
		    	</form>
		    </div>
        <div class="drsm">
          <span>试用说明</span>　1，请将组织架构与联系人资料按模板内格式填写上传，姓名，手机长号，部门，职位为必填项。<a href="/resources/excel/test.xls"> [点击下载模板]</a>
          <br />　2，第一次导入较多的信息可能耗时较长，请耐心等候。
          <br />　3，更新部分联系人信息时，建议您仅上传本次修改的内容。<a href="javascript:Import.excelExport();">[点击此处下载当前联系人资料]</a>
          <br />　4，信息每次上传会替代原本的信息，请在上传前检查您的输入是否完全正确！
        </div>
        </div>
		<div style="height:650px;overflow:auto;display:none">
			<div class="drwj">
				<div id="step1">
					<form id="smartUploadForm" name="smartUploadForm" method="post" enctype="multipart/form-data">
						<table  border="0" cellpadding="0" cellspacing="0">
						  <tr>
			                <td height="50" colspan="2"  class="t_l"><div class="lis_btn"><a href="#"><img src="/resources/images/btn_xzwj.png" />选择文件</a></div></td>
			              </tr>
			              <tr>
			                <td width="400" class="t_l"><input id="textfield1" name="textfield1" type="text" class="opcek" />
			                <input id="uploadFile" name="excel" type="file" class="file" onchange="document.getElementById('textfield1').value=this.value"  /></td>
			                <td class="t_l"><a id="smartUploadBtn" href="javascript:void(0)"><span class="f_l opbtns"><img src="/resources/images/dr.png" />上传</span></a></td>
			              </tr>
				          <tr>
				          	<td id="msg" style="text-align: left;padding-top: 20px;display: none;color:#697A83" colspan="2">
				          		
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
				    <div id="previewDivTitle" style="margin: 0 10px 10px 10px;display: none;text-align:left;">结果预览:</div>
				    <div id="previewDiv" style="height:240px;overflow:auto;margin: 0 10px 0 -60px;display: none;" >
				    </div>
				</div>
			</div>
			
			<div class="drsm">
		  	    <span>使用说明</span>
		        <ul class="bnewzih">
		            <li>1.智能导入能够智能识别excel文件的列内容，进行导入自动匹配。</li>
		            <li>2.第一次导入较多的信息可能耗时较长，请您耐心等待。</li>
		            <li>3.更新部分联系人信息时，建议您仅仅上传本次修改的内容。点击此处下载<a href="#" onclick="Import.excelExport();">当前联系人资料</a>。</li>
		            <li>4.信息每次上传会替代原本的信息，请在上传前检查您的输入是否完全正确！</li>
		        </ul>
		    </div>
		    
		    <div id="columnNameList" style="width:250px;height:300px; overflow: auto;display: none;"> </div>
		</div>
</body>
</html>
