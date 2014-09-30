<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/department/add.js"></script>

	<div class="easyui-layout" fit="true"> 
	    <div region="center" border="false" style="overflow-x:hidden">
	        <div class="pd10">
	            <div class="tableform">
	             <form id="dept_fm" method="post" name="fm" > 
	                <table cellpadding="0" cellspacing="0">
	                	<tr>
	                    	<th width="100">&nbsp;</th>
	                        <td>&nbsp;</td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>上级部门：</th>
	                        <td>
	                        	<input id="parentDepartmentId" name="parentDepartment" style="width:160px;" disabled="disabled"/>
	                        	<input id="parentDepartmentForAdd" type="hidden" name="parentDepartmentId"/>
	                        	<input id=parentDepartmentArea type="hidden" name="parentDepartmentArea"/>
	                        </td>
	                    </tr>
	                    <!-- <tr>
	                    	<th>所属公司：</th>
	                        <td>
	                        	<input id="company" class="easyui-combotree" name="companyTest" style="width:160px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true,onSelect:function(node){$('#companyForAdd').val(node.id);}" />
	        					<font color="red">*</font>
	        					<input id="companyForAdd" type="hidden" name="company"/>
	                        </td>
	                    </tr> -->
	                    <tr>
	                    	<th><font color="red">*</font>部门名称：</th>
							<td>
								<input name="departmentName" style="width:160px;" class="easyui-validatebox" data-options="required:true" />
						   </td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>地址：</th>
	                        <td><input name="departmentAddress" style="width:160px;" class="easyui-validatebox" data-options="required:true"/></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>联系电话：</th>
	                        <td><input name="telephone" style="width:160px;" class="easyui-validatebox" data-options="required:true"/></td>
	                    </tr>
	                    <tr>
	                    	<th>传真：</th>
	                        <td><input name="fax" style="width:160px;" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>部门类型：</th>
	                        <td><input id="type" name="departmentType" style="width:160px;" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
	                    	<th><font color="red">*</font>部门区域：</th>
	                        <td><input id="area" name="departmentArea" style="width:160px;" class="easyui-validatebox" /></td>
	                    </tr>
	                    <tr>
                    		<th>销售任务：</th>
							<td>
								<input name="saleTask" style="width:160px;" class="easyui-numberbox" data-options="precision:2" /><a>&nbsp;&nbsp;&nbsp;万元</a>
							</td>
                    	</tr>
	                    <tr>
	                    	<th>显示顺序：</th>
	                        <td><input name="displayOrder" style="width:160px;" class="easyui-numberbox"/></td>
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
	                        <a href="javascript:void(0)" onClick="addDepartment.save()" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
	                        <a href="javascript:void(0)" onClick="Ict.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	    <!--end：button区域-->
	    
	</div>
