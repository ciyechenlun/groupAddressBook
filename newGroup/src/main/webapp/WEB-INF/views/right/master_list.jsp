<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/web/advise.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script language="JavaScript">
<!--
	function winShow(id){
		var w=Hg.get(id),b=document.body, cw=b.clientWidth,ch=b.scrollTop + 120;
		$(w).css('left',(cw -500)/2).css('top',parseInt(ch)).css('display','block')
	}
	function winHide(id){
		var w=Hg.get(id);
		w.style.display="none";
	}
	// onload事件里添加代码
	window.onload=function(){
		tabView("tab_head","tab_body","on","onclick");		
	}
	//添加角色
	function addMaster()
	{
		Header.winShow('window_01');
	}
	//删除确认对话框
	function del_confirm(master_id)
	{
		if(confirm('确实要删除此角色吗？'))
		{
			$.get(
					'/pc/right/master_del.htm?master_id=' + master_id,
					function(ret)
					{
						if(ret == 'SUCCESS')
						{
							//删除成功
							window.location.reload();
						}
						else
						{
							$.messager.alert('提示','删除失败!','error');
						}
					}
				);
		}
	}
	//保存角色
	function saveMaster()
	{
		//表单验证
		if($('#masterName').val() == '')
		 	$.messager.alert('提示','角色名称不能为空!','info');
		else if($('#masterTaxis').val() == '')
			$.messager.alert('提示','排序不能为空，且必须为数字');
		else{
			//提交表单
			$('#addForm2').form('submit',{
				url:"/pc/right/master_add.htm",
				onSubmit: function(){
					return $('#addForm2').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#addForm2').form('clear');
							Header.winHide('window_01');
							window.location.href = "/pc/right/master_list.htm";					
						});				
					}else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});
		}
	}
//-->
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
		
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/right/master_list.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
		<input id="pageSize" name="pageSize" type="hidden" value="6" />
	</form>
	
<!--top-->


<!--body-->


<!--body正文-->
<div class="bodyqj bodybg fn_clear" style="height:500px;">
<div class="ck_right1">
	<div class="ck_rightbg">
		权限管理
	</div>
	<div id="dvButton" class="margintop" style="width:100%;text-align:right;">
		<input id="update" name="" type="button" class="bottom_01" value="添加角色" onclick="addMaster()"/>
	</div>
	<div class="margintop ck_top">
	    <table class="ck_table5 margintop" border="1" cellspacing="0" cellpadding="0" rules="all" bordercolor="#e6e6e6">
	    	<tr style="height: 40px">
	                <td align="center" style="width:60%"><strong>角色名称</strong></td>
	                <td align="center" style="width:20%"><strong>排序</strong></td>
	                <td align="center" style="width:20%"><strong>操作</strong></td>
	                <!-- <td align="center" style="width:10%"><strong>操作</strong></td> -->
	           </tr>
		    <c:forEach items="${pagination.result}" var="result">
		      <tr style="height: 40px">
		        <td align="center" class="ck_table4">${result.master_name}</td>
		        <td align="center" class="ck_table4">
		        	${result.taxis}
		        </td>
		        <td align="center" class="ck_table4">
		        	<a href="master_right_set?master_id=${result.master_id }">权限设置</a>|
		        	<a onclick="javascript:del_confirm('${result.master_id}');" href="#">删除</a>
		        </td>
		        
		        <%-- <td align="center" class="ck_table4"><a href="javascript:Advise.deleteAd('${result.id}')">删除</a></td> --%>
		      </tr>
		     </c:forEach>
	    </table>
	</div>
   
	<!--start:分页-->
	<div class="bottom-pagination">
		<div class="pagination-box">
			<div class="pagination">
				<ul>
					<li>
						<div class="paginations" name="Pagination"></div>
						<span>共${pagination.totalRecords}条记录</span>
						<span>
							到第
							<input type="text" name="toTargetPage" style="width: 25px;" value="${pagination.currPage}" />
							<span>页</span>
							<input type="button" value="确定" id="J_JumpTo" name="toTargetPage_btn" />
						</span>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!--end：分页-->
</div>

</div>

<!-- 添加角色 -->
<div class="bg2 win" id="window_01" style="display:none;" >
<!--导航-->
	<div id="toggleDelEdite" class="dh_01 zw_top">
	角色维护
	</div>
	<!--导航结束-->
	<!-- 标签页开始 -->
		<div class="zw_cen2" style="overflow-x:hidden; overflow-y:auto;">
			<form id="addForm2" method="post" action="">
			<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi tc_w"><span class="tc_red">*</span>角色名称：</td>
	            <td>
	            	<input id="masterName" name="masterName" type="text" size="20" class="inputzw02"/>
	            </td>
	          </tr>
	          <tr>
	            <td class="xz_zi tc_w"><span class="tc_red">*</span>排序：</td>
	            <td>
	            	<input id="masterTaxis" name="masterTaxis" type="text" size="20" class="inputzw02"/>
	            </td>
	          </tr>
			 <tr>
	            <td style="height:60px;">&nbsp;</td>
	            <td colspan="2"><div align="center"><input type="button" class="bottom_01" value="确定" onclick="saveMaster();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="bottom_01" value="取消" onclick="Header.winHide('window_01')"/></div></td>
	         </tr>
	        </table>
			</form>
		</div>
</div>

</body>
</html>
