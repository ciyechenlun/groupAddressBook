<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/lookGroupBook/lookGroupBook.js"></script>
<script type="text/javascript">
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

$(function(){
	$(".bnewleft01").mouseover(function(){
		$(this).addClass("bnewlefton");
	});
	$(".bnewleft01").mouseout(function(){
		$(this).removeClass("bnewlefton");
	});

	$("#browser").treeview({
		collapsed: true,
		unique: true,
		persist: "location"
	});

});
//-->
</script>
</head>

<body>
<!--top-->
<div class="bodyqj fn_clear">
<div class="bodylogo margintopjji f-left"></div>
<div class="f-right fn_clear">
    <div class="logor"></div>
    <div class="logoa">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>欢迎您！${sessionScope.SESSION_USER_NAME}</td>
        <td><img src="/resources/images/icon_top01.png" width="17" height="17" /></td>
        <td><a href="javascript:Header.changePwd();">修改密码</a></td>
        <td><img src="/resources/images/icon_top02.png" width="17" height="17" /></td>
        <td><a href="javascript:Header.logout();">安全退出</a></td>
      </tr>
    </table>
    </div>
    <div class="logob"></div>
</div>
</div>

<!--body-->
<div class="bodyqj newbody">
    <ul>
        <li>集团通讯录</li>
    </ul>
</div>

<!--body正文-->
<div class="bodyqj bodybg fn_clear">
<!--left-->
<%@include file="../left.jsp" %>

<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
<input type="hidden" id="currPage" value="${pagination.currPage}" />

<form id="searchForm" method="post" action="/pc/lookGroup/search.htm">
	<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" />
	<input id="pageSize" name="pageSize" type="hidden" value="${search.pageSize }" />
	<input  name="key" type="hidden" value="${key}" />
</form>

<!--right-->
<div class="bnewright">
	<div class="margintop ">
        <table class="sh_table">
          <tr>
          	<td width="60%" class="ck_rwenzi">&nbsp;&nbsp;</td>
            <td width="20%"><input name="" type="submit" class="bottom_01" value="编辑用户" onclick="LookGroup.editeUser();" /></td>
            <td width="20%"><input name="" type="submit" class="bottom_01" value="增加用户" onclick="LookGroup.addUser();"/></td>
            <td width="19%"><input name="input" type="submit" class="bottom_01" value="删除用户" onclick="LookGroup.deleteUser();"/></td>
          </tr>
        </table>
    </div>
    
	<c:forEach items="${pagination.result}" var="searchResult">
    
		<div class="margintop ck_top">
	      <table class="ck_table margintop">
	        <tr>
	          <td class="ck_table1"><input name="radioName" type="radio" value="${searchResult.empId}"/></td>
	          <td class="ck_table4"><img src="/resources/images/images_01.png" width="100" height="94" /></td>
	          <td ><table class="ck_table5">
	            <tr>
	              <td width="30"><img src="/resources/images/ck_cion1.png" width="15" height="18" /></td>
	              <td width="300" class="ck_rwenzi">${searchResult.empName}</td>
	              <td width="30"><img src="/resources/images/ck_cion1.png" width="15" height="18" /></td>
	              <td width="150" class="ck_rwenzi">${searchResult.empName}</td>
	            </tr>
	            <tr>
	              <td><img src="/resources/images/ck_cion2.png" width="15" height="18" /></td>
	              <td>${searchResult.telLong}</td>
	              <td><img src="/resources/images/ck_cion2.png" width="15" height="18" /></td>
	              <td class="ck_rwenzi1">${searchResult.telShort}</td>
	            </tr>
	            <tr>
	              <td><img src="/resources/images/ck_cion3.png" width="15" height="18" /></td>
	              <td>${searchResult.mobileLong}</td>
	              <td><img src="/resources/images/ck_cion3.png" width="15" height="18" /></td>
	              <td>${searchResult.mobileShot}</td>
	            </tr>
	            <tr>
	              <td><img src="/resources/images/ck_cion4.png" width="15" height="18" /></td>
	              <td>${searchResult.email}</td>
	            </tr>
	          </table></td>
	        </tr>
	      </table>
    	</div>
    	
    	<div class="margintop ">
	        <table class="sh_table">
	          <tr>
	            <td width="60%" class="ck_rwenzi">&nbsp;&nbsp;${searchResult.parentDeptName}-${searchResult.deptName}</td>
	            <td width="21%">&nbsp;</td>
	            <td width="19%">&nbsp;</td>
	          </tr>
	        </table>
    	</div>
    	
  </c:forEach>
  
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
<!--bottom-->
<div class="bottombg">
<ul>
<li>Copyright © 2013-2015  安徽移动通信 All Rights Reserved 版权所有   维护电话：0551-66666666</li>
</ul>
</div>
<!--弹出框 新增用户-->
<div class="bg win" id="window_01" style="display:none">
	<!--导航-->
	<div id="toggleDelEdite" class="dh_01 zw_top">
	请添加联系人
	</div>
	<!--导航结束-->
	<!-- 标签页开始 -->
		<div class="zw_cen">
		<form id="addForm" method="post" enctype="multipart/form-data" action="">
	        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi tc_w"><span class="tc_red">*</span>姓名：</td>
	            <td>
	            	<input name="employeeName" type="text" size="20" class="inputzw02"/>
	            	<input id="employeeId" name="employeeId" type="hidden"/>
	            </td>
	          </tr>
	           <tr>
	           <td class="xz_zi"><span class="tc_red">*</span>所属部门：</td>
	            <td colspan="2"> <input id="departmentCombox" name="departmentId" type="text" style="width:200px"/>
	            </td>
	          </tr>
	          <tr>
	           <td class="xz_zi tc_w"><span class="tc_red">*</span>职位：</td>
	            <td> <input id="headshipCombox" name="headshipId" type="text" style="width:200px"/>
	            </td>
	           </tr>
	          <tr>
	            <td class="xz_zi"><span class="tc_red">*</span>手机长号：</td>
	            <td colspan="2"><input name="mobile" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	          <tr>
	            <td class="xz_zi">手机短号：</td>
	            <td colspan="2"><input name="mobileShort" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	           <tr>
	            <td class="xz_zi">办公室长号：</td>
	            <td colspan="2"><input name="telephone2" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	           <tr>
	            <td class="xz_zi">办公室短号：</td>
	            <td colspan="2"><input name="telShort" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	          <tr>
	            <td height="98" valign="top" class="xz_zi">工作邮箱：</td>
	            <td colspan="2" valign="top"><input name="email" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	          <tr>
	            <td height="98" valign="top" class="xz_zi">V网编号：</td>
	            <td colspan="2" valign="top"><input id="gridNumber" name="gridNumber" type="text" size="20" class="inputzw02" /></td>
	          </tr>
	          <tr>
	            <td height="98" valign="top" class="xz_zi">显示顺序：</td>
	            <td colspan="2" valign="top"><input id="displayOrder" name="displayOrder" type="text" style="width:200px" class="easyui-numberbox" /></td>
	          </tr>
	          <tr>
		          <td height="98" valign="top" class="xz_zi">图片：</td>
		          <td><input id="apicture" name="apicture" type="file" class="inputzw02"/></td>
	          </tr>
	           <tr>
	            <td>&nbsp;</td>
	            <td colspan="2"><div align="center"><a href="#"><input id="addUserInfo" type="button" class="bottom_01" value="确定" onclick="LookGroup.saveUser();"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><input name="" type="button" class="bottom_01" value="取消" onclick="winHide('window_01')"/></a></div></td>
	             </tr>
	        </table>
        </form>
	    </div>
        <div class="zw_bottom"></div>
	</div>
	
	<!--弹出框 修改密码-->
	<div class="bg2 win" id="window_password" style="display:none">
		<!--导航-->
		<div id="toggleDelEdite" class="dh_01 zw_top">修改密码</div>
		<!--导航结束-->
		<!-- 标签页开始 -->
		<div class="zw_cen1">
		<form id="addForm" method="post" action="">
	        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
	            <td class="xz_zi"></td>
	            <td colspan="2" id="message" style="color: red;"></td>
	          </tr>
	          <tr>
	            <td class="xz_zi">旧密码：</td>
	            <td colspan="2"><input id="oldPassword" name="oldPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td class="xz_zi">新密码：</td>
	            <td colspan="2"><input id="newPassword" name="newPassword" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	          <tr>
	            <td class="xz_zi">确认新密码：</td>
	            <td colspan="2"><input id="newPassword2" name="newPassword2" type="password" size="20" class="inputzw01" required="required"/></td>
	          </tr>
	           <tr>
	            <td >&nbsp;</td>
	            <td colspan="2"><div align="left"><input id="addUserInfo" type="button" class="bottom_01" value="确定" onclick="Header.savePassword();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="" type="button" class="bottom_01" value="取消" onclick="Header.winHide('window_password')"/></div></td>
	             </tr>
	        </table>
        </form>
	    </div>
	</div>
</body>
</html>
