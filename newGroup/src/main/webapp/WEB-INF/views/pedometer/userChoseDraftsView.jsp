<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/web/userChoseDrafts.js"></script>
<script type="text/javascript">
	$(function(){
		//加载默认用户
		$.get("./getDefaultUsers.htm?movement_id=${movement_id}",function(ret){
			var data = eval(ret);
			//$("#select2D").remove();
			for(var i = 0; i<data.length;i++)
			{
				var value = data[i];
				html = '<option value="' + value.employee_id + " " + value.employee_name + '">'+value.employee_name+'</option>';
				$("#select2D").append(html);
			}
			$("#select2D").children().each(function(){$(this).attr("selected","selected")});
		});
	});
	function addUsers()
	{
		$("#select2D").children().each(function(){$(this).attr("selected","selected")});
		var val_1 = $("#select2D").val();
		if (val_1 == null) {
			$.messager.alert('警告', '请选择人员', 'warning');
			return false;
		} else {
			$.post("./addUsers.htm",{user:"" + val_1,movement_id:'${movement_id}'},function(ret){
				if(ret == 'SUCCESS')
				{
					window.location.reload();
				}
				else
				{
					$.messager.alert('操作失败');
				}
			});
		};
	}
</script>

<div class="easyui-layout"  fit="true">
	<input id="compId" value="${company_id}" type="hidden"/>
	<input id="departmentId" value="${department_id }" type="hidden" />
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
        				 <select multiple ="multiple" id ="select1D" name="select1D" style=" width:190px; height:280px;">
        				</select>
       				</td>  
       				<td>
	        			<input type="button" id="addOneD" value=" > " style="width:50px;" /><br />
	        			<input type="button" id="removeOneD" value="&lt;" style="width:50px;" /><br />
	        			<input type="button" id="addAllD" value=" >> " style="width:50px;" /><br />
	        			<input type="button" id="removeAllD" value="&lt;&lt;" style="width:50px;" /><br />
      			 	</td>
       			 	<td>
        				<select multiple="multiple" id = "select2D" name="select2D" style="width:190px;height:280px;">
           
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
						<td><a href="javascript:void(0)" onClick="addUsers();"
							class="easyui-linkbutton" plain="true" iconCls="save">确定</a> <a
							href="javascript:void(0)" onClick="$('#movementDialog').window('close');"
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
