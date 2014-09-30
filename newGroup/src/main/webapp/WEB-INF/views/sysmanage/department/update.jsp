<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/department/update.js"></script>

	<div class="easyui-layout" fit="true"> 
	    <div region="center" border="false" style="overflow-x:hidden">
	        <div class="pd10">
	            <div class="tableform">
	             <form id="dept_fm" method="post" name="fm" > 
	                <table cellpadding="0" cellspacing="0">
	                	<tr>
	                    	<th width="100"><font color="red">*</font>上级部门：</th>
	                        <td>
	                        	<input id="parentDepartmentId" name="parentDepartment" style="width:160px;" value="${dept.parentDepartmentId}" disabled="disabled"/>
	                        	<input id="parentDepartmentForAdd" type="hidden" name="parentDepartmentId" value="${dept.parentDepartmentId}"/>
	                        	<input id="departmentArea" type="hidden"  value="${dept.departmentArea}"/>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>所属公司：</th>
	                        <td>
	                        	<input id="company" class="easyui-combotree" name="companyTest" style="width:160px;" value="${dept.company.companyId}"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true,disabled:true" />
	        					<input id="companyForAdd" type="hidden" name="company" value="${dept.company.companyId}"/>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th width="100"><font color="red">*</font>部门名称：</th>
							<td>
								<input name="departmentName" style="width:160px;" class="easyui-validatebox" data-options="required:true" value="${dept.departmentName}" />
								<input id="departmentId" name="departmentId" type="hidden" value="${dept.departmentId }" /> 
						   </td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>地址：</th>
	                        <td><input name="departmentAddress" style="width:160px;" value="${dept.departmentAddress }" class="easyui-validatebox" data-options="required:true"/></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>联系电话：</th>
	                        <td><input name="telephone" style="width:160px;" value="${dept.telephone }" class="easyui-validatebox" data-options="required:true"/></td>
	                    </tr>
	                    <tr>
	                    	<th>传真：</th>
	                        <td><input name="fax" style="width:160px;" value="${dept.fax }" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>部门类型：</th>
	                        <td><input id="type" name="departmentType" style="width:160px;" value="${dept.departmentType }" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>部门区域：</th>
	                        <td><input id="area" name="departmentArea" style="width:160px;" value="${dept.departmentArea }" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
                    		<th>销售任务：</th>
							<td>
								<input name="saleTask" style="width:160px;" value="${dept.saleTask }"  class="easyui-numberbox" data-options="precision:2" /><a>&nbsp;&nbsp;&nbsp;万元</a>
							</td>
                    	</tr>
	                    <tr>
	                    	<th>显示顺序：</th>
	                        <td><input name="displayOrder" style="width:160px;" value="${dept.displayOrder }" class="easyui-numberbox"/></td>
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
	                        <a href="javascript:void(0)" onClick="updateDepartment.update()" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
	                        <a href="javascript:void(0)" onClick="Ict.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	    <!--end：button区域-->
	    
	</div>
