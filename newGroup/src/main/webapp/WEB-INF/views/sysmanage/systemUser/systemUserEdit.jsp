<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
<script type="text/javascript" src="/resources/js/sysmanage/systemUser/systemUserEdit.js"></script>
 
<div class="easyui-layout" fit="true"> 
    <div region="center" border="false" style="overflow-x:hidden">
        <div class="pd10">
            <div class="tableform">
             <form id="systemUserFrom" method="post" > 
				<!-- 是否是超级管理员 -->
             	<input id="admin" type="hidden" value="${admin}" />
             	<input name="userId" type="hidden" value="${systemUser.userId}" />
             	<input id="userRoleIdHidden" name="userRoleId" type="hidden" value="${userRoleId }" />
             	<input id="employeeIdHidden" type="hidden" value="${employee.employeeId }" />
             	<input id="companyIdHidden" type="hidden" value="${systemUser.company.companyId}"  /> 
             	<input id="photoAddrHidden" name="photo" type="hidden" value="${systemUser.photo }" /> 
             	
                <table cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="80px">&nbsp;</th>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>是否员工：</th>
                        <td><input id="isEmpCheckbox" type="checkbox" /></td>
                        <th>登录帐号：</th>
						<td> 
							<input name="userName" value="${systemUser.userName}" style="width:125px;" class="easyui-validatebox" data-options="required:true" />
							<font color="red">*</font>  
					   </td>
                    </tr>
                    <tr>
                    	<th>公司：</th>
                        <td>
                        	<input id="companyComboTree" value="${systemUser.company.companyId}" name="company.companyId" style="width:125px;" />
                        	<font color="red">*</font>
                        </td>
                        <th>密码：</th>
                        <td>
                        	<input id="password" name="password" type="password" style="width:125px;" value="${metaData.metaCode }" class="easyui-validatebox" data-options="required:true" ></input>
                        	<font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                    	<th>角色：</th>
						<td>
							<input id="roleCombo" name="roleId" value="${role.roleId }" class="easyui-combobox" value="${metaData.roleId}" style="width:125px;"data-options="editable:false" />
							<font color="red">*</font>
					   </td>
					   <th>密码确认：</th>
                        <td>
                        	<input name="passwordConfirm" type="password" style="width:125px;" class="easyui-validatebox" 
                        		 required="required" validType="equals['#password']" ></input>
                        	<font color="red">*</font>
                        </td>
                    </tr>
                </table>
                
                <table class="isEmpoyeHide" cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="80px">&nbsp;</th>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                	<tr>
                		<th>部门：</th>
						<td>
							<input id="departmentComboTree" value="${employee.departmentId }" class="easyui-combotree" style="width:125px;" />
						</td>
						<th>员工：</th>
						<td>
							<input id="employeeIdComboGrid" value="${employee.employeeId }" name="employeeId" class="easyui-combogrid" style="width:125px;" />  
					   </td>
                	</tr>
                </table>
                
                <table  class="isnotEmpoyeHide" cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="80px">&nbsp;</th>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                	<tr>
                		<th>真实姓名：</th>
                        <td>
                        	<input name="realName" style="width:125px;" value="${systemUser.realName }" class="easyui-validatebox"></input>
                        </td>
                        <th>手机号：</th>
                        <td>
                        	<input name="mobile" style="width:125px;" value="${systemUser.mobile }" class="easyui-numberbox"></input>
                        </td>
                	</tr>
                	<tr>
                		<th>邮箱：</th>
                        <td>
                        	<input name=email style="width:125px;" value="${systemUser.email }" class="easyui-validatebox" data-options="validType:'email'"></input>
                        </td>
                        <th>性别：</th>
                        <td>
                   			&nbsp;&nbsp;
                   			<input id="man" type="radio" name="sex" value="男" <c:if test="${systemUser.sex == '男'}">checked</c:if> /><label for="man">男</label>&nbsp;&nbsp;
                   			<input id="woman" type="radio" name="sex" value="女" <c:if test="${systemUser.sex == '女'}">checked</c:if>/><label for="woman">女</label>
                        </td>
                	</tr>
                </table>
                </form>
                
                <form id="systemUserPhotoForm" method="post" enctype="multipart/form-data" autocomplete = "off" >
	               	 <table cellpadding="0" cellspacing="0">
		               	<tr>
		               		<th width="80px">&nbsp;</th>
	                       <td colspan="2">&nbsp;</td>
	                   </tr>
	                   <tr>
							<th>上传照片：</th>
							<td>
								<input id="systemUserPhotoFile" name="photoFile" type="file" style="width:125px;">
							</td>
							<th></th>
							<td>
								<a href="javascript:SystemUser.upLoadPhoto();" class="easyui-linkbutton" plain="true">上传预览</a>
								<a href="javascript:SystemUser.cancelUpLoadPhoto();" class="easyui-linkbutton" plain="true">取消</a>
							</td>
						</tr>
						<tr>
	                    	<th width="80px">&nbsp;</th>
	                        <td colspan="3">&nbsp;</td>
                   		</tr>
						<tr>
							<td colspan="4" align="center"> 
								<div id="systemUserPhotoshow" style="width:510px;padding: 5px;overflow: auto;">
									<img id="systemUserImg"  src="${systemUser.photo }">
								</div>
							</td>
						</tr>
	                  </table>
                </form>
            </div>
        </div>
    </div>
    <!--start：button区域-->
    <div region="south" style="height:36px;overflow:hidden;" border="false">
        <div class="WinBtnBody pdt5">
            <table align="center"border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <a href="javascript:void(0)" onClick="javascript:SystemUser.saveSystemUser();" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
                        <a href="javascript:void(0)" onClick="javascript:Ict.closeWin()" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--end：button区域-->
    
</div>
