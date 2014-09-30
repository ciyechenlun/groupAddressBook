<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/role/roleAdd.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
						<table cellpadding="0" cellspacing="0">
							<tr>
                    			<th width="100">&nbsp;</th>
                        		<td>&nbsp;</td>
                    		</tr>
                    		<!-- 是否是超级管理员 -->
							<tr style="display: none">
								<th>角色Id：</th>
								<td><input id="roleIdId" name="roleId" style="width: 150px"
									class="easyui-validatebox"></td>
							</tr>
							<tr Id="forAdmin">
								<th>公司名称：</th>
								<td>
									<input id="company" class="easyui-combotree" name="company" style="width:152px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
							</tr>
							<tr>
								<th>角色名称：</th>
								<td><input id="roleNameId" name="roleName"
									style="width: 150px" class="easyui-validatebox"
									data-options="required:true"> <span style="color: red">*</span></td>
							</tr>
							<tr>
								<th>角色类型：</th>
								<td>
								<input id="roleCodeId" class="easyui-combotree" name="roleCode" style="width:152px;"
	        					data-options="url:'/pc/role/roleTree.htm',editable:false,required:true" />	
									 <span style="color: red">*</span></td>
							</tr>
							<!-- <tr>
								<th>状态：</th>
								<td><input id="statusId" name="status" style="width:152px;"/>
								<span style="color: red">*</span></td>
							</tr> -->
							<!-- <tr>
								<th>显示顺序：</th>
								<td><input name="displayOrder" id="displayOrderId" style="width: 150px"
									precision="0" class="easyui-numberbox" min="0" data-options="required:true"/> <span
									style="color: red">数字类型</span>
							</tr> -->
							<tr>
								<th>描述：</th>
								<td><textarea id="descriptionId" name="description"
										style="width: 150px"></textarea></td>
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
						<td><a href="javascript:void(0)" onClick="saveRole();"
							class="easyui-linkbutton" plain="true" iconCls="save">保存</a> <a
							href="javascript:void(0)" onClick="javascript:Ict.closeWin()"
							class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>