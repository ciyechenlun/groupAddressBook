<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/role/roleEdit.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
						<input type="hidden" name="role" value="${role.roleId}" />
						<table cellpadding="0" cellspacing="0">
							<tr>
                    			<th width="100">&nbsp;</th>
                        		<td>&nbsp;</td>
                    		</tr>
							<tr style="display: none">
								<th>角色Id：</th>
								<td><input id="roleIdId" value="${role.roleId}"
									name="roleId" style="width: 150px" class="easyui-validatebox"></td>
							</tr>
							<tr Id="forAdmin">
								<th>公司名称：</th>
								<td>
									<input value="${role.company.companyId}" id="company" class="easyui-combotree" name="company" style="width:150px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
							</tr>
							<tr>
								<th>角色名称：</th>
								<td><input id="roleNameId" value="${role.roleName}"
									name="roleName" style="width: 150px" class="easyui-validatebox"
									data-options="required:true"> <span style="color: red">*</span></td>
							</tr>
							<tr>
								<th>角色类型：</th>
								<td>
								<input  value="${role.roleCode}" id="roleCodeId" class="easyui-combotree" name="roleCode" style="width:150px;"
	        					data-options="url:'/pc/role/roleTree.htm',editable:false,required:true" />	
	        					 <span style="color: red">*</span></td>
							</tr>
							<%-- <tr>
								<th>状态：</th>
								<td><input value="${role.status }" id="statusId" name="status" style="width:153px;"/> <span style="color: red">*</span></td>
							</tr> --%>
							<%-- <tr>
								<th>显示顺序：</th>
								<td><input name="displayOrder" id="displayOrderId" style="width: 150px"
									value="${role.displayOrder}" precision="0"
									class="easyui-numberbox" min="0" /> <span style="color: red">数字类型</span>
								</td>
							</tr> --%>
							<tr>
								<th>描述：</th>
								<td><textarea id="descriptionId" name="description"
										style="width: 150px">${role.description}</textarea></td>
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
						<td><a href="javascript:void(0)" onClick="editRole();"
							class="easyui-linkbutton" plain="true" iconCls="save">保存</a> <a
							href="javascript:void(0)" onClick="Ict.closeWin()"
							class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>