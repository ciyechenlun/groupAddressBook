<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/index/index.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	div {white-space:nowrap;}
</style>
<div class="bnewleft">
  <div class="margintop" style="height: 420px;overflow-y:auto;overflow-x:hidden;">
    <c:forEach items="${org_list}" var="org_list">
    <div class="bnewleft01 bnewleft01a" id="${org_list.company_id}">
      <ul>
        <li href="javascript:void(0)" onclick="javascript:$('#iFrame').attr('src','/index_right.htm?company_id=${org_list.company_id}');">${org_list.company_name}</li>
      </ul>
    </div>
    </c:forEach>
    <c:forEach items="${list}" var="list">
    <div class="bnewleft01 bnewleft01b" id="${list.company_id}">
      <ul>
        <li href="javascript:void(0)" onclick="javascript:$('#iFrame').attr('src','/index_right.htm?company_id=${list.company_id}');">${list.company_name}</li>
      </ul>
    </div>
    </c:forEach>
  </div>
 <!--  <div class="index-btn2"><a href="javascript:void(0)" onclick="Index.deleteCompany()">删除组织机构</a></div>
  <div class="index-btn"><a href="javascript:void(0)" onclick="Index.addCompany()">新建组织</a></div>
  <div class="index-btn1"><a href="javascript:void(0)" onclick="Index.updateCompany()">修改组织名称</a></div> -->
</div>


<!--弹出框 新建组织-->
<!-- <div class="bg3 win" id="window_company" style="display:none">
	导航
	<div id="toggleDelEdite1" class="dh_01 zw_top">
	新建通讯录
	</div>
	导航结束
	标签页开始
	<div class="zw_cen3">
		<form id="comp_form" method="post" action="">
			<input type="hidden" id="companyId" name="companyId"/>
			
	        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi">通讯录名称：</td>
	            <td><input id="companyName" maxlength="10" name="companyName" type="text" size="20" class="inputzw02"/></td>
	          </tr>
	          <tr>
	            <td >&nbsp;</td>
	            <td><div align="left"><input id="addUserInfo" type="button" class="bottom_01" value="确定" onclick="Index.saveCompany();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="" type="button" class="bottom_01" value="取消" onclick="Header.winHide('window_company')"/></div></td>
	          </tr>
	        </table>
	       </form>
	<div class="zw_bottom"></div>
    </div>
</div> -->

<!-- <script type="text/javascript">
<!--
$(function(){
	$(".bnewleft01 ul>li").click(function(){
		var id= $(this).attr("id");
		location.replace(id);
	});
});
</script> -->
