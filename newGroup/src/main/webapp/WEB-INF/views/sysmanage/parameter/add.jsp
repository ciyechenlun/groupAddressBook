<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/sysmanage/parameter/add.js"></script> 

	<div class="easyui-layout" fit="true"> 
	    <div region="center" border="false" style="overflow-x:hidden">
	        <div class="pd10">
	            <div class="tableform">
	             <form id="param_fm" method="post" name="fm" > 
	                	<table cellpadding="0" cellspacing="0">
		                 	<tr>
	                    		<th width="100">参数名称：</th>
								<td>
									<input name="parameterName" style="width:150px;" class="easyui-validatebox" data-options="required:true" />
									<font color="red">*</font>
							   </td>
	                    	</tr>
	                    	<tr>
	                    		<th width="100">参数编码：</th>
								<td>
									<input name="parameterCode" style="width:150px;" class="easyui-validatebox" data-options="required:true" />
									<font color="red">*</font>
							   </td>
	                    	</tr>
	                    	<tr>
	                    		<th width="100">参数值：</th>
								<td>
									<input name="parameterValue" style="width:150px;" class="easyui-validatebox" data-options="required:true" />
									<font color="red">*</font>
							   </td>
	                    	</tr>
	                    	<tr>
	                    	<th>所属公司：</th>
	                        <td>
	                        	<input class="easyui-combotree" name="companyId" style="width:150px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
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
	                        <a href="javascript:void(0)" onClick="addParameter.save()" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
	                        <a href="javascript:void(0)" onClick="Ict.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	    <!--end：button区域-->
	    
	</div>
