<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<script type="text/javascript" src="/resources/js/lookGroupBook/lookGroupBook.js"></script>
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<script language="JavaScript">
<!--
	// onload事件里添加代码
	window.onload = function() {
		tabView("tab_head", "tab_body", "on", "onclick");
	};

	$(function() {
		//如果不是s_admin&&查看的是企业组织，提示没有权限
		if('${org_flag}' == '1' && '${manager}' != '1')
		{
			$("#dvCover").show();
			$.messager.alert('提示','你没有权限执行此操作','info',function(){history.back();});
			
		}else{
			var companyType = window.parent.document.getElementById("companyType").value;
			if (companyType == "bnewleft01 bnewleft01a liGetSelected"
					|| companyType == "bnewleft01 bnewleft01a bnewlefton liGetSelected") {
				$("#grid").removeClass("ck_left2");
				$("#grid").addClass("ck_left1");
				$("#tree").show();
			}
			$("#companyId").val(window.parent.document.getElementById('companyId').value);
		}
	});
//-->
</script>
</head>

<body>
	<div id="dvCover" style="display:none;width:785px;height:500px;backgroud-color:#CCCCCC;">
	</div>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />

	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="/pc/lookGroup/main.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="${search.pageSize }" />
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
		<input id="companyId" name="companyId" type="hidden" />
	</form>




	<!--body正文-->
	<div class="bnewright bodybg fn_clear">

		<!--树-->
		<div id="tree" class="ck_left" style="overflow: auto; display: none;">
			<div class="margintop">
				<ul id="department_look"></ul>
			</div>
		</div>
		<!--树end-->
		<div id="grid" class="ck_left2">
			<div class="ck_rightbg">${parentDepartmentName}</div>
			<div class="margintop ">
				<table width="90%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td width="41%" class="ck_rwenzi">${departmentName}</td>
						<td width="20%"><input id="editeUser" name="input"
							type="submit" class="bottom_01" value="编辑用户"
							onclick="LookGroup.editeUser();" /></td>
						<td width="20%"><input id="addUser" name="input"
							type="submit" class="bottom_01" value="增加用户"
							onclick="LookGroup.addUser();" /></td>
						<td width="19%"><input id="deleteUser" name="input"
							type="submit" class="bottom_01" value="删除用户"
							onclick="LookGroup.deleteUser();" /></td>
					</tr>
				</table>
			</div>
			<c:forEach items="${pagination.result}" var="searchResult">
				<div class="margintop ck_top">
					<table class="ck_table margintop" id="table1">
						<tr>
							<td class="ck_table1"><input name="radioName" type="radio"
								value='${searchResult.user_company_id}' /></td>
							<td class="ck_table4"><img
								src="/resources/images/images_01.png" width="100" height="94" /></td>
							<td><table class="ck_table5" id="table2">
									<tr>
										<td width="30"><img src="/resources/images/ck_cion1.png"
											width="15" height="18" /></td>
										<td width="200" class="ck_rwenzi">${searchResult.employee_name}</td>
										<td width="30"><img src="/resources/images/ck_cion1.png"
											width="15" height="18" /></td>
										<td width="150" class="ck_rwenzi">${searchResult.employee_name}</td>

									</tr>
									<tr>
										<td><img src="/resources/images/ck_cion2.png" width="15"
											height="18" /></td>
										<td>${searchResult.telephone2}</td>
										<td><img src="/resources/images/ck_cion2.png" width="15"
											height="18" /></td>
										<td class="ck_rwenzi1">${searchResult.tel_short}</td>
									</tr>
									<tr>
										<td><img src="/resources/images/ck_cion3.png" width="15"
											height="18" /></td>
										<td>${searchResult.mobile}</td>
										<td><img src="/resources/images/ck_cion3.png" width="15"
											height="18" /></td>
										<td>${searchResult.mobile_short}</td>
									</tr>
									<tr>
										<td><img src="/resources/images/ck_cion4.png" width="15"
											height="18" /></td>
										<td>${searchResult.email}</td>
									</tr>
								</table></td>
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
								<div class="paginations" name="Pagination"></div> <span>共${pagination.totalRecords}条记录</span>
								<span> 到第 <input type="text" name="toTargetPage"
									style="width: 25px;" value="${pagination.currPage}" /> <span>页</span>
									<input type="button" value="确定" id="J_JumpTo"
									name="toTargetPage_btn" />
							</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<!--end：分页-->
		</div>

	</div>


	<!--弹出框  个人通讯录 新增用户-->
	<div class="bg2 win" id="window_lookGroup" style="display: none;">
		<!--导航-->
		<div id="toggleDelEdite" class="dh_01 zw_top">请添加联系人</div>
		<!--导航结束-->
		<!-- 标签页开始 -->
		<div class="zw_cen2" style="overflow-x: hidden; overflow-y: scroll;">
			<form id="form_lookGroup" method="post" enctype="multipart/form-data"
				action="">
				<table width="90%" border="0" cellspacing="0" cellpadding="0"
					align="center">
					<tr>
						<td class="xz_zi tc_w"><span class="tc_red">*</span>姓名：</td>
						<td>
							<input id="employeeName" name="employeeName" type="text" size="20" class="inputzw02" />
							<input id="userCompanyId" name="userCompanyId" type="hidden" />
							<input id="companyId2" name="companyId" type="hidden"  />
						</td>
					</tr>
					<tr id="for_not_org_uc" style="display: none;">
						<td class="xz_zi tc_w">单位：</td>
						<td>
							<input id="userCompany" name="userCompany" type="text" size="20" class="inputzw02" />
						</td>
					</tr>
					<tr id="for_org_dept" style="display: none">
						<td class="xz_zi"><span class="tc_red">*</span>所属部门：</td>
						<td colspan="2">
							<input id="departmentCombox" name="departmentId" type="text" style="width: 200px" />
							<input id="departmentName2" name="departmentName" type="hidden"/>
						</td>
					</tr>
					<tr id="for_not_org_dept" style="display: none">
						<td class="xz_zi tc_w">所属部门：</td>
						<td><input id="departmentName" name="departmentName" type="text" size="20" class="inputzw02" /></td>
					</tr>
					<tr id="for_org_hs" style="display: none">
		           		<td class="xz_zi tc_w"><span class="tc_red">*</span>职位：</td>
		            	<td>
		            		<input id="headshipCombox" name="headshipId" type="text" style="width:200px"/>
		            		<input id="headshipName2" name="headshipName" type="hidden"/>
		            	</td>
	           		</tr>
					<tr id="for_not_org_hs" style="display: none">
						<td class="xz_zi tc_w">职位：</td>
						<td><input id="headshipName" name="headshipName"
							type="text" size="20" class="inputzw02" /></td>
					</tr>
							<tr>
								<td class="xz_zi"><span class="tc_red">*</span>手机长号：</td>
								<td><input id="mobile" name="mobile" type="text" size="20"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">手机短号：</td>
								<td><input id="mobileShort" name="mobileShort" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">办公室长号：</td>
								<td><input id="telephone2" name="telephone2" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">办公室短号：</td>
								<td><input id="telShort2" name="telShort" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">微博：</td>
								<td><input id="weibo" name="weibo" type="text" size="20"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">微信：</td>
								<td><input id="weixin" name="weixin" type="text" size="20"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">学校：</td>
								<td><input id="school" name="school" type="text" size="20"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">专业：</td>
								<td><input id="userMajor" name="userMajor" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">年级：</td>
								<td><input id="userGrade" name="userGrade" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">班级：</td>
								<td><input id="userClass" name="userClass" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">学号：</td>
								<td><input id="studentId" name="studentId" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">籍贯：</td>
								<td><input id="nativePlace" name="nativePlace" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">常住地址：</td>
								<td><input id="address" name="address" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">家庭地址：</td>
								<td><input id="homeAddress" name="homeAddress" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">宅电：</td>
								<td><input id="telephone" name="telephone" type="text"
									size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">QQ：</td>
								<td><input id="qq" name="qq" type="text" size="20"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">显示顺序：</td>
								<td><input id="displayOrder" name="displayOrder"
									type="text" size="20" class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi">图片：</td>
								<td><input id="apicture" name="apicture" type="file"
									class="inputzw02" /></td>
							</tr>
							<tr>
								<td class="xz_zi tc_w">备注：</td>
								<td colspan="4"><textarea id="remark" name="remark"
										rows="4" cols="29"></textarea></td>
							</tr>
							<tr>
								<td class="xz_zi tc_w">心情：</td>
								<td colspan="4"><textarea id="mood" name="mood" rows="4"
										cols="29"></textarea></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="2"><div align="center">
										<input type="button" class="bottom_01" value="确定"
											onclick="LookGroup.saveUser();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
											type="button" class="bottom_01" value="取消"
											onclick="LookGroup.cancelEdit();" />
									</div></td>
							</tr>
				</table>
			</form>
		</div>
		<div class="zw_bottom"></div>
	</div>

</body>
</html>
