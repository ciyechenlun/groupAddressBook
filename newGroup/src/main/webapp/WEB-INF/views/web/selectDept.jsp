<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/web/selectDept.js"></script>
	
<div class="easyui-layout"  fit="true">
	<input id="deptId" name="deptId" type="hidden"/>
	<input id="empsNum" name="empsNum" type="hidden"/>
	<input id="compId" value="${companyId}" type="hidden"/>
	<div region="center" title="人员列表" border="false">
		<form id="form1" runat="server">
			<table>
				<tr>
					<th style="font-size:10pt;color:blue;"></th>
				</tr>
				<tr>
					<td>
        				<select multiple="multiple" id="empSelected" style=" width:300px; height:300px;">
        				</select>
       				</td>  
    			</tr>
   			 </table>
    	</form>
    
    	<!--start：button区域-->
		<div data-options="border:false" style="height: 36px; overflow: hidden;">
			<div class="WinBtnBody pdt5">
				<table align="center" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<a href="javascript:void(0)" onClick="SelectDept.save();" class="easyui-linkbutton" plain="true" iconCls="save">确定</a>
							<a href="javascript:void(0)" onClick="SelectDept.closeWin();" class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>
	
	<div region="west" title="可选部门" style="width:220px;border-right:1px #ccc solid;margin-left:-1px;">
		<ul id="employeeDeptTreeD" class="easyui-tree"></ul>
	</div> 
</div>