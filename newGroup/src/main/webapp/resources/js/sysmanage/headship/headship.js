Headship = function(){
	return {
		//设置body的高度为文档的高度
		doLayout : function(){
			$('body').height($(document).height());
		},
		
	
	//列表
	initGrid : function(){
		$('#headshiptable').datagrid({
    	nowrap : false,
		striped : true,
		url : '/pc/headship/all.htm',
//		queryParams : {
//		},
		fit : true,
		fitColumns : true,
		method : 'POST',
		loadMsg : '数据载入中，请稍等...',
        columns:[  [ {
				field : 'headship.rolebox',
				title : 'headship.rolebox',
				checkbox : true
			}, 
            {field:'headshipId',title:'岗位Id',hidden:true,width:100},  
            {field:'companyId',title:'公司Id',hidden:true,width:100},  
            {field:'headshipName',title:'岗位名称',width:100},
            {field:'dataContent',title:'岗位级别',width:100},
            {field:'headshipLevel',title:'岗位级别',hidden:true,width:100},
            {field:'createMan',title:'创建人',hidden:true,width:100},
            {field:'createTime',title:'创建时间',hidden:true,width:100,formatter:function(value,rows,index){
            	if(value != '' && value != null){
					value = value.substr(0,10);
				}
				return value;
            }
            },  
            {field:'modifyMan',title:'修改人',hidden:true,width:100},
            {field:'modifyTime',title:'修改时间',hidden:true,width:100,formatter:function(value,rows,index){
            	if(value != '' && value != null){
					value = value.substr(0,10);
				}
				return value;
            }
            },  
            {field:'status',title:'状态',width:100,formatter:function(value,rows,index){
				if(value=='0'){
					return "启用";
				}
				if(value=='1'){
					return "禁用";
				}
            	}},
            {field:'description',title:'描述',width:100},
            {field:'companyName',title:'公司名称',width:100},
            {field:'delFlag',title:'删除标志',hidden:true,width:100,formatter:function(value,rows,index){
				if(value=='0'){
					return "未删除";
				}
				if(value=='1'){
					return "已删除";
				}
            	}},
        ]],
        toolbar : [{
			text : '添加',
			iconCls : 'add',
			handler : function() {
				Ict.openWin('添加岗位',400,300,'/pc/headship/add.htm');
			}
		}, {
			text : '修改',
			iconCls : 'edit',
			handler : function() {
				var row = $('#headshiptable').datagrid('getSelections');
				if (row.length == 0) {
					Ict.error("请选择你要修改的岗位",function(){
						return;
					});
				}else if(row.length > 1){
					Ict.error("请选择一条记录进行修改",function(){
						return;
					});
				}else if(row.length == 1)
					Ict.openWin('编辑岗位',400,300,'/pc/headship/edit.htm?headshipId='+ row[0].headshipId);
				
			}
		}, {
			text : '删除',
			iconCls : 'remove',
			handler : function() {
				var rows = $('#headshiptable').datagrid('getSelections');
				var num = rows.length;
				var headships = null;
				if (rows.length == 0) {
					Ict.error("请选择你要删除的岗位",function(){
						return;
					});
				} else {
					Ict.confirm("确定要删除选中的岗位吗?",	
						function(result) {
								if (result) {
									for ( var i = 0; i < num; i++) {
										if (null == headships || i == 0) {
											headships = rows[i].headshipId;
										} else {
											headships = headships + ","
													+ rows[i].headshipId;
										}
									}

									$
											.ajax({
												url : "/pc/headship/del.htm",
												type : 'get',
												context : document.body,
												data : {
													"headships" : headships
												},
												dataType : 'json',
												success : function(data) {
													if (data == 1) {
														//$.messager.alert('提示', "删除成功!",'info');
														Ict.info("删除成功!",function(){
															$('#headshiptable').datagrid('reload');
														});
														
													} else {
														//$.messager.alert('提示',"删除失败!",'info');
														Ict.error("删除失败!",function(){
															$('#headshiptable').datagrid('reload');
														});
													}
												}
											});
								}
							});
				}
			}
		} ,{
			text : '刷新',
			iconCls : 'reload',
			handler : function() {
				$('#headshiptable').datagrid('reload');
			}
		},'->'],
        pagination : true,
		rownumbers : true
    });
	},
	//初始化
	init : function(){
		this.doLayout();
		this.initGrid();
		//this.addSearchBox(250);
	},
	//在toolbar上面添加搜索框
	addSearchBox:function(width){
		$('.searchbox').css({'width': width+'px'}); 
		$('.searchbox-text').css({'width': eval(width-20)+'px'}); 
		$('.datagrid-toolbar').append($('.serarchbox')); 
		$('.datagrid-toolbar').append($('#tbar')); 
	},
		//搜索函数
		doSearch:function(value){
			$('#headshiptable').datagrid({
				nowrap : false,
				striped : true,
				url : '/pc/headship/allByCondition.htm',
				queryParams : {
					'headshipName':value
				},
				loadMsg : '数据载入中，请稍等...',
				fitColumns : true,
				striped : true,
				pagination : true,
				rownumbers : true,
				method : 'post',
				fit : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ]
			});
		}
	};
//});
}();

$(function(){
	 //公司部门树
	$('#headshipDeptTree').tree({
		url:'/pc/company/tree.htm',
		onClick: function(node){
			var idIcon = node.id+"_"+node.iconCls;
			$('#headshiptable').datagrid({
				url:'/pc/headship/all.htm', 
				queryParams:{
					idIcon:idIcon
				}
			}).load();
		}
	});
	
	Headship.init();
});