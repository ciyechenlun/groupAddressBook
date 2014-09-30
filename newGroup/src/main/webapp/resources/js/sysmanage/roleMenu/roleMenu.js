roleMenu = function(){
	return {
		
	};
}();



$(function(){
	$('#role_menu').tree({
		url :'/pc/menu/tree.htm',
		checkbox:true,
		onCheck: function(node,checked){
			var selectRole=$('#roletable').datagrid('getSelected');
			//增加或删除叶子节点菜单
			if(roleMenu.add_flag){
				if(node.attributes == "Y"){
					if(checked){
//						alert("保存叶子节点");
						$.ajax({
							type : "POST",
							url : '/pc/roleMenu/save.htm?roleId='+selectRole.roleId+'&menuId='+node.id,
							success : function(data){				
							}
						});
					}else{
//						alert("删除叶子节点");
						$.ajax({
							type : "POST",
							url : '/pc/roleMenu/delete.htm?roleId='+selectRole.roleId+'&menuId='+node.id,
							success : function(data){				
							}
						});
					}
				}else{
					if(checked){
//						alert("保存非叶子节点");
						$.ajax({
							type : "POST",
							url : '/pc/roleMenu/saveAll.htm?roleId='+selectRole.roleId+'&menuId='+node.id,
							success : function(data){
							}
						});
					}else{
//						alert("删除非叶子节点");
						$.ajax({
							type : "POST",
							url : '/pc/roleMenu/deleteAll.htm?roleId='+selectRole.roleId+'&menuId='+node.id,
							success : function(data){				
							}
						});
					}
				}
			}
		},
		onLoadSuccess:function(node, data){
			$("#role_menu").tree('expandAll');
			roleMenu.add_flag=false;
		}
	});
	$('#roletable').datagrid({
    	nowrap : false,
		striped : true,
		url : '/pc/role/all.htm',
		fit : true,
		fitColumns : true,
		checkbox : true,
		singleSelect:true,
		method : 'POST',
		loadMsg : '数据载入中，请稍等...',
        columns:[[ 
            {field:'roleId',title:'角色Id',hidden:true,width:100},  
            {field:'roleName',title:'角色名称',width:100},
            {field:'status',title:'状态',width:100,formatter:function(value,rows,index){
				if(value=='0'){
					return "启用";
				}
				if(value=='1'){
					return "禁用";
				}
            	}}
        ]],
        pagination : true,
		rownumbers : true,
		onClickRow : function(rowIndex, rowData){
			$('#menuTree').mask('加载中 ……');
			roleMenu.add_flag=false;
			var checkedNodes=$("#role_menu").tree('getChecked');
			for(var i = 0;i < checkedNodes.length;i++) {
				$("#role_menu").tree('uncheck',checkedNodes[i].target);
			}
			$.ajax({
				type : "POST",
				url : '/pc/roleMenu/findRoleRightByRoleId.htm?roleId='+rowData.roleId,
				success : function(data){
					for(var i = 0;i < data.length;i++){
						var roleMenu = $("#role_menu").tree('find',data[i].menu_id);
						$("#role_menu").tree('check',roleMenu.target);
					}
					$('#menuTree').unmask();
				},
				error:function(){
					$('#menuTree').unmask();
				}
			});
			roleMenu.add_flag=true;
		}
	    });
	
});