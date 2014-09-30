
Menu = function(){
	return {
		
		//在toolbar上面添加搜索框
		addSearchBox:function(width){
			$('.searchbox').css({'width': width+'px'});
			$('.searchbox-text').css({'width': eval(width-20)+'px'});
			$('.datagrid-toolbar').append($('.searchbox'));
//			$('.datagrid-toolbar').append($('#tbar'));
		},
		
		//搜索函数
		doSearch:function(value){
			if($.trim(value) ==""){
				Ict.info("请输入查询条件!");
				return;
			}
			$.ajax({
				url : '/pc/menu/search.htm?menuName=' + encodeURI(value),
				type : 'post',
				success : function(data) {
					$("#menu").treegrid('unselectAll');
					for(var i = 0;i < data.length;i++){
						$("#menu").treegrid('select',data[i].menu_id);
					}
				}
			});
		},
		
		//删除方法
		remove : function(){
			var rows = $('#menu').treegrid('getSelections');
			var menuIds = null;
			if (rows.length == 0) {
				Ict.info("请选择你要删除的菜单!");
				return;
			} else{
				Ict.confirm('确定要删除选中的菜单吗?<br>若选中的菜单中包含父菜单，将删除菜单下所有子菜单', function(result) {
					if (result) {
						for ( var i = 0; i < rows.length; i++) {
							if (null == menuIds || i == 0) {
								menuIds = rows[i].id;
							} else {
								menuIds = menuIds + "," + rows[i].id;
							}
						}
						$.ajax({
							url : '/pc/menu/delete.htm?menuIds=' + menuIds,
							type : 'post',
							success : function(data) {
								if (data == "SUCCESS") {
									Ict.info('删除成功',function(){
										$('#menu').treegrid('reload');
									});
								} else {
									Ict.error("删除失败!");
								}
							}
						});
					}
				});
			}
		}
	};
}();

$(function(){
	//初始化表格
	$('#menu').treegrid({
		nowrap: false,
		rownumbers: true,
		animate:true,
		checkbox:true,
		collapsible:true,
		url :'/pc/menu/tree.htm',
		singleSelect:false,
		fit:true,
		fitColumns:true,
		idField:'id',
		treeField:'text',
		columns:[[
		    {field:'id',title:'菜单Id',width:50,checkbox : true},
			{field:'text',title:'菜单名称',width:150},
			{field:'companyName',title:'所属公司',width:80},
			{field:'path',title:'菜单路径',width:250}
		]],
		toolbar : [{
			text : '添加',
			iconCls : 'add',
			handler: function(){
				Ict.openWin("新增菜单",400,400,"/pc/menu/addPage.htm");
			}
		},{
			text : '修改',
			iconCls : 'edit',
			handler: function(){
				var row = $('#menu').treegrid('getSelections');
				if(row.length == 0) {
					Ict.info("请选择你要修改的菜单!");
					return;
				}else if(row.length > 1){
					Ict.info("请选择一条记录进行修改!");
					return;
				}else{
					Ict.openWin("修改菜单",400,400,"/pc/menu/updatePage.htm?menuId="+ row[0].id);
				}
			}
		},{
			text : '查看',
			iconCls : 'detail',
			handler: function(){
				var row = $('#menu').treegrid('getSelections');
				if(row.length == 0) {
					Ict.info("请选择你要查看的菜单!");
					return;
				}else if(row.length > 1){
					Ict.info("请选择一条记录进行查看!");
					return;
				}else{
					Ict.openWin("查看菜单",400,400,"/pc/menu/detailPage.htm?menuId="+ row[0].id);
				}
			}
		},{
			text : '删除',
			iconCls : 'remove',
			handler : function(){
				Menu.remove();
			}
		},{
			text : '刷新',
			iconCls : 'reload',
			handler : function() {
				$('#menu').treegrid('reload');
			}
		},{
			text : '全部展开',
			iconCls : 'expand',
			handler : function() {
				$('#menu').treegrid('expandAll');
			}
		},{
			text : '全部收起',
			iconCls : 'collapse',
			handler : function() {
				$('#menu').treegrid('collapseAll');
			}
		}]
	});
	Menu.addSearchBox(250);
});