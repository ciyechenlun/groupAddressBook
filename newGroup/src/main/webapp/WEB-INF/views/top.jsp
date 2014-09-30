<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/common/common.js"></script>
<script src="/resources/js/common/artDialog/artDialog.js?skin=default"></script>
<script src="/resources/js/common/artDialog/plugins/iframeTools.js"></script>
<%@include file="common/baseIncludeJs.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<style type="text/css">
.com-item {
    font-size: 15px;
    padding: 5px 0 3px 3px;
}
.setting-item {
    font-size: 12px;
    padding: 3px 0 3px 3px;
}
.com-panel {
	background: none repeat scroll 0 0 #FFFFFF;
	overflow: auto;
   border: 1px solid #E9E9E9;
}
.com-item-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
.com-search {
    border: 1px solid #D8DADE;
    color: #333333;
    font-size: 14px;
    height: 25px;
    line-height: 25px;
    padding-left: 5px;
    width: 180px;
}
</style>
<div class="warp">
  <!--头部：start-->
  <div class="top">
   <div class="f_l name"><a href="/index.htm"><img src="/resources/images/topname.png" /></a></div>
   <div class="f_r user">
     <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><a href="#"><img src="/resources/images/btn_cjgr.png" /></a></td>
        <td><a href="#"><img src="/resources/images/ico_xtwh.png" /></a></td>
        <td><a href="#" onclick="outMenu()">系统维护</a></td>
        <td><a href="#"><img src="/resources/images/ico_gly.png" /></a></td>
        <td>${user.realName }</td>
        <td width="60" align="right"><a href="#"><img src="/resources/images/top_pic.png" width="55" height="55" /></a></td>
      </tr>
     </table>
   </div>
  </div>
  <div class="level">
   <div class="f_l level_cek">
     <table border="0" cellspacing="0" cellpadding="2">
      <tr>
        <td><a href="#" onclick="loadAllCompany()"><img src="/resources/images/ico_level.png" /></a></td>
        <td class="caname">${companyName eq null ? "选择企业" : companyName }
        </td>
        <td>
        <input type="hidden" value="${companyId }" id="companyId" />
        <input type="hidden" value="${pageTye}" id="pageType" />
        <c:if test="${manager=='1'}">
        <a href="javascript:void(0)" onclick="editCompany('${companyId}')"><img src="/resources/images/ico_level_edit.png" /></a>
        </c:if></td>
      </tr>
     </table>
   </div>
   <div class="f_r lever_search">
     <table border="0" cellspacing="0" cellpadding="0">
       <tr>
        <td width="200"><input id="search_input" onkeydown="onEnter(event,'${companyId}')"  type="text" /></td>
        <td><a href="#" onClick="searchData('${companyId}')"><img src="/resources/images/btn_search.gif" /></a></td>
       </tr>
     </table>     
   </div>
  </div>
  <!--头部：end-->
</div>
<script type="text/javascript">
function editCompany(companyId){
	if(companyId){
		Ict.openWin('修改企业信息',580,480,'/pc/company/update.htm?companyId='+companyId);
	}else{
		$.messager.alert('提示','没有企业可以编辑','info');
	}
}
function onEnter(event,companyId){
	 var keyCode = event.keyCode?event.keyCode:event.which?event.which:event.charCode;
	 if (keyCode ==13){
	   // 此处处理回车动作
	   if($('#pageType').val()=="companyM"){//企业选择页面，搜索企业
		   window.location.href="/toCompanyM.htm?key="+$('#search_input').val();
	   }else if($('#pageType').val()=="index"){//通讯录显示页面，搜索员工
			$('#iFrame').attr('src','/pc/lookGroup/searchEmployee.htm?key='+$('#search_input').val()+'&companyId='+ companyId);
	   }else{
		   //
	   }
	 }
	}
function searchData(companyId){
	   // 此处处理回车动作
	 if($('#pageType').val()=="companyM"){//企业选择页面，搜索企业
		 window.location.href="/toCompanyM.htm?key="+$('#search_input').val();
	   }else if($('#pageType').val()=="index"){//通讯录显示页面，搜索员工
			$('#iFrame').attr('src','/pc/lookGroup/searchEmployee.htm?key='+$('#search_input').val()+'&companyId='+ companyId);
	   }else{
		   //
	   }
	 
	}
	function forwardCompany(companyId){
		//$('#company_div').attr("style","display:none");
		window.location.href="/index.htm?companyId="+companyId;
	}
	function loadAllCompany(){
		if($("#company_div").is(":hidden")){
			$('#company_div').show();
			if($.trim($('#company_combo').html())==""){
				loadNextCompany();
			}else{
				$('.com-item').show();
			}
			$("#company_combo div").each(function(i,val){
				if($.trim($(val).text()) == '${companyName}'){
					$(this).addClass("com-item-selected");
				}
			});
		}else{
			$('#company_div').hide();
		}
	}
	function loadNextCompany(){
		$.ajax({
			type: "POST",
			async:false,
			url: "/orgAllList.htm",
			dataType: "JSON",
			success: function(data){
				var array = data;
				var deptDiv ="";
				for(var i=0;i<array.length;i++){
					deptDiv +='<div class="com-item" >'+
					'<ul>'+
		        	'<li href="javascript:void(0)" onclick=\'forwardCompany("'+array[i].company_id+'")\' style="cursor:pointer" >'+
		        	array[i].company_name+'</li>'+
			      	'</ul>'+
					'</div>';
				}
				$('#company_combo').append(deptDiv);
			}
		});
	}
	function toCompanyM(){
		window.location.href="/toCompanyM.htm";
	}
	function toAdvise(){
		window.location.href="/pc/advise/main.htm";
	}
	
	function toComMap(){
		window.location.href="/pc/companyMap/main.htm";
	}
	
	function outMenu(){
		if($("#setting_div").is(":hidden")){
			$('#setting_div').show();
		}else{
			$('#setting_div').hide();
		}
	}

$(function(){
	$('#commonWindow').window({closed:true});
		$(".com-item").click(function(){
			$(this).parent().find("div").removeClass("com-item-selected");
			$(this).addClass("com-item-selected");
		}).hover(function(){
			$(this).addClass("combobox-item-hover");
		},function(){
			$(this).removeClass("combobox-item-hover");
		});
		$("#company_div").mouseleave(function(){
			$('#company_div').hide(500);
		});
		$("#setting_div").mouseleave(function(){
			$('#setting_div').hide(500);
		});
		//获取class为caname的元素 
		$(".caname").click(function() { 
			var td = $(this); 
			var txt = $.trim(td.text()); 
			var input = $("<input class='com-search' type='text' value='" + txt + "'/>"); 
			td.html(input); 
			input.click(function() { return false; }); 
			//获取焦点 
			input.trigger("focus").select(); 
			//文本框失去焦点后提交内容，重新变为文本 
			input.blur(function() { 
				var newtxt = $(this).val(); 
				//判断文本有没有修改 
				if (newtxt != txt) { 
					//td.html(newtxt); 
				} 
				else 
				{ 
					td.html(newtxt); 
				} 
			});
			input.keyup(function(){
				var newtxt = $.trim($(this).val()); 
				$('#company_div').show();
				if($.trim($('#company_combo').html())==""){
					loadNextCompany();
				}else{
					$('.com-item').show();
				}
				$("#company_combo div").each(function(i,val){
					if($.trim($(val).text()).indexOf(newtxt)==-1){
						$(this).hide();
					}
				});
			});
	}); 
	});
</script>


<!--弹出框 新建组织-->
<div class="bg3 win" id="window_company" style="display:none">
	<!--导航-->
	<div id="toggleDelEdite1" class="dh_01 zw_top">
	新建通讯录
	</div>
	<!--导航结束-->
	<!-- 标签页开始 -->
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
</div>
<div id="commonWindow" ></div>
<div id="window_password" ></div>
<!--下拉框 显示企业-->
<div id="company_div"  class="panel" style="display:none;position: absolute; z-index: 9002; width: 230px; height:700px;left: 60px; top: 112px;">
	<div id="company_combo" class="com-panel" title="" style="width: 228px; height: 690px;">
	<%-- <c:forEach items="${all_list.result}" var="result">
		<div  class="com-item" >
		<ul>
        	<li href="javascript:void(0)" onclick="forwardCompany('${result.company_id}')" style="cursor:pointer">${result.company_name}</li>
      	</ul>
		</div>
   	</c:forEach> --%>
<%--    	<c:if test="${fn:length(org_list.result)>=20 }">
      	 <a href="#" onclick="loadNextCompany(2)">
      	 <div class="pages"></div>
      	 </a>
     </c:if> --%>
    </div>
    
</div>
<!--下拉框，显示系统维护-->
<div id="setting_div" class="panel" style="display:none;position: absolute; z-index: 9002; width: 100px; height:110px;right: 105px; top: 50px;">
	<div class="com-panel" title="" style="width: 85px; 
		<c:choose>
			<c:when test="${flag=='1'}">height: 100px;</c:when>
			<c:otherwise>height: 80px;</c:otherwise>
		</c:choose>>
	">
		<div  class="setting-item" >
		<ul>
        	<li onclick="toCompanyM()" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">企业维护</li>
      	</ul>
		</div>
		<div  class="setting-item" >
		<ul>
        	<li  onclick="Header.changePwd()" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">修改密码</li>
      	</ul>
		</div>
	<!-- 	
		<div  class="setting-item" >
		<ul>
        	<li  onclick="window.location.href='http://120.209.138.173:8080/index.htm'" href="" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">使用旧版后台</li>
      	</ul>
		</div> -->
		<c:if test="${flag=='1'}">
		<div  class="setting-item" >
		<ul>
        	<li  onclick="toAdvise()" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">使用反馈</li>
      	</ul>
		</div>
		</c:if>
		<c:if test="${user.username=='s_admin'}">
		<div  class="setting-item" >
		<ul>
        	<li  onclick="toComMap()" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">组织地图</li>
      	</ul>
		</div>
		</c:if>
		<div  class="setting-item" >
		<ul>
        	<li  onclick="Header.logout()" style="cursor:pointer;color: #313C50;font-family: '微软雅黑'">安全退出</li>
      	</ul>
		</div>
    </div>
    
</div>