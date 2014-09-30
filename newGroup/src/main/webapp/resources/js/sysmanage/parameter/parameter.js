Parameter = function(){
	return {
		
	};
}();


$(function(){
	$('#parameter_table').datagrid({
		nowrap: false,
		rownumbers: true,
		animate:true,
		checkbox:true,
		collapsible:true,
		url:'/pc/parameter/list.htm',
		singleSelect:false,
		fit:true,
		fitColumns:true,
		idField:'parameterConfigId',
		columns:[[
		    {field:'parameterConfigId',title:'参数配置Id',width:50,checkbox : true},
		    {field:'parameterName',title:'参数名称',width:80},
		    {field:'parameterCode',title:'参数编码',width:60},
		    {field:'parameterValue',title:'参数值'},
		    {field:'companyName',title:'所属公司',width:60}
		]],
		pagination : true,
		toolbar : [{
			text : '添加',
			iconCls : 'add',
			handler : function() {
				Ict.openWin("添加参数",400,250,"/pc/parameter/addPage.htm");
			}
		},{
			text : '修改',
			iconCls : 'edit',
			handler : function() {
				var row = $('#parameter_table').datagrid('getSelections');
				if (row.length == 0) {
					Ict.info("请选择你要修改的参数!");
					return;
				}else if(row.length > 1){
					Ict.info("请选择一条记录进行修改!");
					return;
				}else if(row.length == 1)
					Ict.openWin("修改参数",400,250,"/pc/parameter/updatePage.htm?parameterConfigId="+ row[0].parameterConfigId);
			}
		},{
			text : '删除',
			iconCls : 'remove',
			handler : function() {
				var rows = $('#parameter_table').datagrid('getSelections');
				var paramIds = null;
				if (rows.length == 0) {
					Ict.info("请选择你要删除的参数!");
					return;
				}else{
					Ict.confirm('确定要删除选中的参数吗?', function(result){
						if (result){
							for ( var i = 0; i < rows.length; i++) {
								if (null == paramIds || i == 0) {
									paramIds = rows[i].parameterConfigId;
								} else {
									paramIds = paramIds + "," + rows[i].parameterConfigId;
								}
							}
						}
						$.ajax({
							url : '/pc/parameter/delete.htm?paramIds=' + paramIds,
							type : 'post',
							success : function(data) {
								if (data == "SUCCESS") {
									Ict.info('删除成功',function(){
										$('#parameter_table').datagrid('reload');
										$('#parameter_table').datagrid('clearSelections');
									});
								} else {
									Ict.error("删除失败!");
								}
							}
						});
					});
				}

			}
		}]
	});
});