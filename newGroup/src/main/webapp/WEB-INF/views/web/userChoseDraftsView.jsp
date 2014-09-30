<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/web/userChoseDrafts.js"></script>
	
<div class="easyui-layout"  fit="true">
	<input id="compId" value="${companyId}" type="hidden"/>
	<div region="center" title="选择人员" border="false">
		<form id="form1" runat="server">
			<table>
				<tr>
					<th style="font-size:10pt;color:blue;">可选人员：</th>
					<th>&nbsp;</th>
					<th style="font-size:10pt;color:blue;">已选人员：</th>
				</tr>
				<tr>
					<td>
        				 <select multiple ="multiple" id ="select1D" style=" width:190px; height:280px;">
        				</select>
       				</td>  
       				<td>
	        			<input type="button" id="addOneD" value=" > " style="width:50px;" /><br />
	        			<input type="button" id="removeOneD" value="&lt;" style="width:50px;" /><br />
	        			<input type="button" id="addAllD" value=" >> " style="width:50px;" /><br />
	        			<input type="button" id="removeAllD" value="&lt;&lt;" style="width:50px;" /><br />
      			 	</td>
       			 	<td>
        				<select multiple="multiple" id = "select2D" style="width:190px;height:280px;">
           
        				</select>
       				</td>  
    			</tr>
   			 </table>
    	</form>
    
    	<!--start：button区域-->
		<div data-options="border:false"  style="height: 36px; overflow: hidden;">
			<div class="WinBtnBody pdt5">
				<table align="center" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><a href="javascript:void(0)" onClick="save();"
							class="easyui-linkbutton" plain="true" iconCls="save">确定</a> <a
							href="javascript:void(0)" onClick="closeWin();"
							class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>
	
	
	<div region="west" title="可选组织" style="width:200px;border-right:1px #ccc solid;margin-left:-1px;">
		<ul id="employeeDeptTreeD"  class="easyui-tree"></ul>
	</div> 
</div>
