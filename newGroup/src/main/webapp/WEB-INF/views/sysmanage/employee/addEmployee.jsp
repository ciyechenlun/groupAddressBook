<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
<script type="text/javascript"
	src="/resources/js/sysmanage/employee/employeeAdd.js"></script>
<div class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow-x: hidden">
		<div class="pd10">
			<div class="tableform">
				<form id="fm" method="post" enctype="multipart/form-data">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<th width="100">工号：</th>
							<td><input name="employeeCode" style="width: 150px"
								class="easyui-validatebox" data-options="required:true" /></td>
						</tr>
						<tr>
							<th>姓名：</th>
							<td><input name="employeeName" style="width: 150px"
								class="easyui-validatebox" data-options="required:true" /></td>
						</tr>
						<tr>
							<th>所属部门：</th>
							<td><input id="departmentName" style="width: 150px"
								class="easyui-validatebox" disabled="disabled" /> <input
								type="hidden" id="departmentId" name="departmentId" /></td>
						</tr>
						<tr>
							<th>是否部门领导：</th>
							<td><input id="isDeptLeader" name="isDeptLeader"
								style="width: 150px" data-options="required:true" /></td>
						</tr>
						<tr>
							<th>手机号码：</th>
							<td><input name="mobile" style="width: 150px"
								class="easyui-validatebox" validType="validateTel['#mobile']"
								data-options="required:true" /></td>
						</tr>
						<tr>
							<th>备用号码：</th>
							<td><input name="backupMobile" style="width: 150px"
								class="easyui-validatebox"
								validType="validateTel['#backupMobile']" /></td>
						</tr>
						<tr>
							<th>入职时间：</th>
							<td><input name="joinDate" class="easyui-datebox"
								style="width: 150px" /></td>
						</tr>
						<tr>
							<th>离职时间：</th>
							<td><input name="leaveDate" class="easyui-datebox"
								style="width: 150px" /></td>
						</tr>
						<tr>
							<th>状态：</th>
							<td><input id="status" name="status"
								class="easyui-validatebox" value="0" style="width: 150px" /></td>
						</tr>
						<tr>
							<th>照片：</th>
							<td><input id="picture" name="photo" type="file"
								style="width: 150px;"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<!--start：button区域-->
	<div region="south" style="height: 36px; overflow: hidden;"
		border="false">
		<div class="WinBtnBody pdt5">
			<table align="center" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><a href="javascript:void(0)" onClick="AddEmployee.save();"
						class="easyui-linkbutton" plain="true" iconCls="save">保存</a> <a
						href="javascript:void(0)" onClick="javascript:Ict.closeWin()"
						class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
				</tr>
			</table>
		</div>
	</div>
	<!--end：button区域-->
</div>