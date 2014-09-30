<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/department/detail.js"></script>

	<div class="easyui-layout" fit="true"> 
	    <div region="center" border="false" style="overflow-x:hidden">
	        <div class="pd10">
	            <div class="tableform">
	             <form id="dept_fm" method="post" name="fm" > 
	                <table cellpadding="0" cellspacing="0">
	                    <tr>
	                    	<th width="100">部门名称：</th>
							<td>
								<input name="departmentName" style="width:150px;" class="easyui-validatebox" data-options="required:true" value="${dept.departmentName}" disabled="disabled"/>
								<input name="departmentId" type="hidden" value="${dept.departmentId }" /> 
						   </td>
	                    </tr>
	                    <tr>
	                    	<th>上级部门：</th>
	                        <td>
	                        	<input id="parentDepartmentId"  name="parentDepartmentId" style="width:150px;" value="${dept.parentDepartmentId}" disabled="disabled"/>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>所属公司：</th>
	                        <td>
	                        	<input class="easyui-combotree" name="company" style="width:150px;" value="${dept.company.companyId}"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true,disabled:true" />
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>部门经理：</th>
	                        <td><input name="departmentMaster" style="width:150px;"  class="easyui-validatebox" value="${dept.departmentMaster}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>部门电话：</th>
	                        <td><input name="telephone" style="width:150px;" class="easyui-validatebox" value="${dept.telephone}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>显示顺序：</th>
	                        <td><input name="displayOrder" style="width:150px;" class="easyui-numberbox" value="${dept.displayOrder}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>创建时间：</th>
	                        <td><input name="createTime" style="width:150px;" class="easyui-datebox" value="${dept.createTime}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>创建人：</th>
	                        <td><input name="createMan" style="width:150px;" class="easyui-validatebox" value="${dept.createMan}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>修改时间：</th>
	                        <td><input name="modifyTime" style="width:150px;" class="easyui-datebox" value="${dept.modifyTime}" disabled="disabled"/></td>
	                    </tr>
	                    <tr>
	                    	<th>创建人：</th>
	                        <td><input name="modifyMan" style="width:150px;" class="easyui-validatebox" value="${dept.modifyMan}" disabled="disabled"/></td>
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
	                        <a href="javascript:void(0)" onClick="Department.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	    <!--end：button区域-->
	    
	</div>
