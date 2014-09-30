<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<script type="text/javascript" src="/resources/js/sysmanage/systemUser/password.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden;height: 200px;">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
						<table cellpadding="0" cellspacing="0">
							<tr>
                    			<th style="color: red;" id="message" width="100" colspan="2"></th>
                    		</tr>
							<tr>
								<th style="text-align: left;height: 30px;"><font color="red">*</font>旧密码：</th>
								<td style="text-align: left;height: 30px;"><input type="password" id="oldPassword" name="oldPassword" style="width: 150px" class="easyui-validatebox"></td>
							</tr>
							<tr>
								<th style="text-align: left;height: 30px;"><font color="red">*</font>新密码：</th>
								<td style="text-align: left;height: 30px;"><input type="password" id="newPassword" name="newPassword" style="width: 150px" class="easyui-validatebox"></td>
							</tr>
								
							<tr>
								<th style="text-align: left;height: 30px;"><font color="red">*</font>确认密码：</th>
								<td style="text-align: left;height: 30px;"><input type="password" id="confirmPassword" name="confirmPassword" style="width: 150px" class="easyui-validatebox"></td>
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
						<td><a href="javascript:void(0)" onClick="Password.savePassword();"
							class="easyui-linkbutton" plain="true" iconCls="save">保存</a> <a
							href="javascript:void(0)" onClick="javascript:Ict.closeWin()"
							class="easyui-linkbutton" plain="true" iconCls="cancel">取消</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>