<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/employee/addUser.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="user_fm" method="post" enctype="multipart/form-data">
						<table cellpadding="0" cellspacing="0">
						<tr >
                        	<th width="100px"><font color="red">*</font>登录帐号：</th>
							<td> 
								<input name='employeeId' type="hidden" value="${ employeeId}"/>
								<input name="userName" type="hidden" value="${mobile}"/>
								<input name="userId" type="hidden" value="${userId}"/>
								<input id="userName" style="width:125px;" value="${mobile}" class="easyui-validatebox" data-options="required:true" disabled="disabled"/>
					   		</td>
					   		<td></td>
                    	</tr>
						<tr>
                        	<th><font color="red">*</font>密码：</th>
	                        <td>
	                        	<input id="password" name="password" type="password" style="width:125px;" class="easyui-validatebox" data-options="required:true" ></input>
	                        </td>
                    	</tr>
                    	<tr>
					   		<th><font color="red">*</font>密码确认：</th>
	                        <td>
	                        	<input name="passwordConfirm" type="password" style="width:125px;" class="easyui-validatebox" 
	                        		 required="required" validType="equals['#password']" ></input>
	                        </td>
                    	</tr>	
						<tr>
							<th><font color="red">*</font>角色：</th>
							<td>
								<input id="roleCombo" name="roleId" value="${role.roleId }" class="easyui-combobox" value="${metaData.roleId}" style="width:125px;"data-options="editable:false" />
							</td>
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
						<td>
						<a href="javascript:void(0)" onClick="AddUser.saveUser()" class="easyui-linkbutton" plain="true" iconCls="save">保存</a> 
						<a href="javascript:void(0)" onClick="Ict.closeWin()" class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>