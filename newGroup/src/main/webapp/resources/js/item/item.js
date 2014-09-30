Item = function(){
	return {
		
		//查看图片
		photo : function(value){
			value = "/pc/item/images/" + value;
			art.dialog({
				content : '<img src='+value+'>',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				left: '20%',
			    top: '20%',
			    lock : true,
			    cancelVal: '关闭',
			    cancel: true
			});
		},
		
		//添加搜索框
		addSearchBox:function(width){
			$('.searchbox').css({'width': width+'px'});
			$('.searchbox-text').css({'width': eval(width-20)+'px'});
			$('.datagrid-toolbar').append($('.serarchbox'));
			$('.datagrid-toolbar').append($('#tbar'));
		},
		
		doSearch : function(value){
			$('#item_table').datagrid({
				url:'/pc/item/queryList.htm',
				queryParams : {
					'key' : value
				},
				toolbar : '#toolBar'
			});
		}
	};
}();

$(function(){
	$('#item_table').datagrid({
		nowrap: false,
		rownumbers: true,
		animate:true,
		checkbox:true,
		collapsible:true,
		url:'/pc/item/list.htm',
		singleSelect:false,
		fit:true,
		fitColumns:true,
		idField:'item_id',
		columns:[[
		    {field:'item_id',title:'商品Id',width:50,checkbox : true},
		    {field:'item_code',title:'商品编码',width:150,hidden:true},
		    {field:'item_name',title:'商品名称',width:80},
		    {field:'model',title:'规格型号',width:80},
		    {field:'item_unit',title:'计量单位',width:80},
		    {field:'item_price',title:'商品价格(元)',width:80},
		    {field:'parentSort',title:'大类',width:80},
		    {field:'childSort',title:'小类',width:80},
		    {field:'bar_code',title:'条码',width:80},
		    {field:'brand',title:'品牌',width:80},
		    {field:'picture_url',title:'照片',width:50,formatter:function(value,rowData){
		    	if(value == null || value == ""){
		    		return "";
		    	}else{
		    		return "<div><a href=\'javascript:Item.photo(\""+value+"\")\'><font color='blue'>查看</font></a></div>";
		    	}	
		    }}
		]],
		pagination : true,
		toolbar : [{
			text : '添加',
			iconCls : 'add',
			id : 'itemadd',
			handler : function() {
				if($('#roleCode').val()=='6'){
					Ict.info("您的角色为超级管理员,但是您无权添加商品!");
					return;
				}
				Ict.openWin("添加商品",400,350,"/pc/item/addPage.htm");
			}
		},{
			text : '修改',
			iconCls : 'edit',
			handler : function() {
				if($('#roleCode').val()=='6'){
					Ict.info("您的角色为超级管理员,但是您无权修改商品信息!");
					return;
				}
				var row = $('#item_table').datagrid('getSelections');
				if (row.length == 0) {
					Ict.info("请选择你要修改的记录!");
					return;
				}else if(row.length > 1){
					Ict.info("请选择一条记录进行修改!");
					return;
				}else if(row.length == 1)
					Ict.openWin("修改商品信息",400,350,"/pc/item/updatePage.htm?itemId="+ row[0].item_id);
			}
		},{
			text : '删除',
			iconCls : 'remove',
			handler : function() {
				if($('#roleCode').val()=='6'){
					Ict.info("您的角色为超级管理员,但是您无权删除商品信息!");
					return;
				}
				var rows = $('#item_table').datagrid('getSelections');
				var itemIds = null;
				if (rows.length == 0) {
					Ict.info("请选择你要删除的记录!");
					return;
				}else{
					Ict.confirm('确定要删除选中的记录吗?', function(result){
						if (result){
							for ( var i = 0; i < rows.length; i++) {
								if (null == itemIds || i == 0) {
									itemIds = rows[i].item_id;
								} else {
									itemIds = itemIds + "," + rows[i].item_id;
								}
							}
						}
						$.ajax({
							url : '/pc/item/delete.htm?itemIds=' + itemIds,
							type : 'post',
							success : function(data) {
								if (data == "SUCCESS") {
									Ict.info('删除成功',function(){
										$('#item_table').datagrid('reload');
										$('#item_table').datagrid('clearSelections');
									});
									art.dialog({
										content : '已删除'+rows.length+'行',
										width:'auto',
										height:'auto',
										zIndex : "20000",
										time: 3,
										title: '三秒后自动关闭',
										left: '100%',
									    top: '100%'
									});
								} else {
									Ict.error("删除失败!");
								}
							}
						});
					});
				}

			}
		},{
			text : '导出',
			iconCls : 'export',
			handler : function() {
				var key = $('#search').searchbox('getValue');
				$('#search_key').val(key);
				$('#exportForm').form('submit',{
					url:'/pc/item/excelExport.htm',
					success:function(data){
						$.messager.alert(data);
					}
				});
			}
		},{
			text : '导入',
			iconCls : 'import',
			handler : function() {
				if($('#roleCode').val()=='6'){
					Ict.info("您的角色为超级管理员,但是您无权导入商品信息!");
					return;
				}
				art.dialog({
					title: '选择导入的文件',
					content: document.getElementById('importForm'),
					width:'auto',
					height:'auto',
					zIndex : "20000",
					lock : true,
					ok: function () {
						$('#importForm').form('submit',{
							url:'/pc/item/excelImport.htm',
							async:false,
							success:function(data){
								art.dialog({
									content: data,
									title: '导入结果',
									width:'auto',
									height:50,
									zIndex : "20000",
									lock : true,
									cancelVal: '关闭',
								    cancel: true
								});
								$('#item_table').datagrid('reload');
							}
						});
				    },
				    cancelVal: '关闭',
				    cancel: true
				});
			}
		},{
			text : '刷新',
			iconCls : 'reload',
			handler : function() {
				$('#item_table').datagrid('reload');
			}
		}]
	});
	Item.addSearchBox(250);
});