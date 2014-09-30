/**
 * 组织机构管理
 * department.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2012-12-1 上午11:40:42
 */

Department = function(){
	return {
		//加载部门树的URL
		DEPARTMENT_TREE_URL : "/pc/department/deptTree.htm",
		
		//设置body的高度为文档的高度
		doLayout : function(){
			$('body').height($(document).height());
		},
		
		closeWin: function(){
			Ict.closeWin();
			$('#grid').treegrid('reload');
		},
		
		
		
		//初始化部门表格树
		initGrid : function(){
			$('#grid').treegrid({
						url : Department.DEPARTMENT_TREE_URL,
						idField:'department_id',  
					    treeField:'department_name', 
					    animate:true,
					    striped:true,
					    fit : true,
					    fitColumns:true,
					    checkbox:true,
					    nowrap: false,//内容不换行   
                		rownumbers: true,//行号  
                		singleSelect : false,
					    columns:[[  
					        {field:'department_id',title:'部门ID',width:100,checkbox : true},
					        {field:'parent_department_id',title:'上级部门ID',width:100,hidden:true},
					        {field:'department_name',title:'部门名称',width:150},
					        {field:'companyName',title:'所属公司',width:100},
					        {field:'department_address',title:'地址',width:50},
					        {field:'telephone',title:'联系电话',width:60},
					        {field:'fax',title:'传真',width:60},
					        {field:'departmentType',title:'部门类型',width:90},
					        {field:'sale_task',title:'销售任务(万元)',width:80},
					        {field:'empNum',title:'员工数量',width:80,hidden:true},
					        {field:'clientNum',title:'客户数量',width:80,hidden:true},
					        {field:'storeNum',title:'门店数量',width:80,hidden:true}
					    ]],
					    onContextMenu : function(e, node) {
							e.preventDefault();
							$('#grid').treegrid('select', node.target);
							$('#mm').menu('show', {
								left : e.pageX,
								top : e.pageY
							});
						},
					    toolbar : [{
							text : '添加',
							iconCls : 'add',
							handler : function() {
								var row = $('#grid').treegrid('getSelections');
								if(row.length > 1){
									Ict.info("只能选择一个部门!");
									return;
								} else {
									Ict.openWin("新增部门",400,350,"/pc/department/addPage.htm");
								}
							}
						}, {
							text : '修改',
							iconCls : 'edit',
							handler : function() {
								var row = $('#grid').treegrid('getSelections');
								if (row.length == 0) {
									Ict.info("请选择你要修改的部门!");
									return;
								}else if(row.length > 1){
									Ict.info("请选择一条记录进行修改!");
									return;
								}else if(row.length == 1)
									Ict.openWin("修改部门信息",400,350,"/pc/department/updatePage.htm?deptId="+ row[0].department_id);
							}
						}, {
							text : '删除',
							iconCls : 'remove',
							handler : function() {
								Department.remove();
							}
						}
//						,{
//							text : '查看',
//							iconCls : 'detail',
//							handler : function() {
//								var row = $('#grid').treegrid('getSelections');
//								if (row.length == 0) {
//									Ict.info("请选择你要查看的部门!");
//									return;
//								}else if(row.length > 1){
//									Ict.info("请选择一条记录查看!");
//									return;
//								}else if(row.length == 1)
//									Ict.openWin("查看部门信息",400,400,"/pc/department/detailPage.htm?deptId="+ row[0].department_id);
//							}
//						} 
						,{
							text : '刷新',
							iconCls : 'reload',
							handler : function() {
								Department.reload();
							}
						} ,
						'-',{
							text : '全部展开',
							iconCls : 'expand',
							handler : function() {
								$('#grid').treegrid('expandAll');
							}
						},{
							text : '全部收起',
							iconCls : 'collapse',
							handler : function() {
								$('#grid').treegrid('collapseAll');
							}
						}]
					});  
		},
		
		//初始化
		init : function(){
			this.doLayout();
			this.initGrid();
			this.addSearchBox(250);
		},
		
		//删除方法
		remove : function(){
			var rows = $('#grid').treegrid('getSelections');
			var deptIds = null;
			if (rows.length == 0) {
				Ict.info("请选择你要删除的部门!");
				return;
			} else{
				for ( var i = 0; i < rows.length; i++){
					if(rows[i].have_child_deparment=="Y"){
						Ict.info("请先删除子部门!");
						return;
					}
				}
				for ( var i = 0; i < rows.length; i++){
					if(rows[i].empNum>0){
						Ict.info("请先删除部门下的员工!");
						return;
					}
				}
				for ( var i = 0; i < rows.length; i++){
					if(rows[i].clientNum>0){
						Ict.info("请先删除部门下的客户!");
						return;
					}
				}
				for ( var i = 0; i < rows.length; i++){
					if(rows[i].storeNum>0){
						Ict.info("请先删除部门下的门店!");
						return;
					}
				}
				Ict.confirm('确定要删除选中的部门吗?', function(result) {
					if (result) {
						for ( var i = 0; i < rows.length; i++) {
							if (null == deptIds || i == 0) {
								deptIds = rows[i].department_id;
							} else {
								deptIds = deptIds + "," + rows[i].department_id;
							}
						}
						$.ajax({
							url : '/pc/department/delete.htm?deptIds=' + deptIds,
							type : 'post',
							success : function(data) {
								if (data == "SUCCESS") {
									$('#grid').treegrid('reload');
									Ict.slideMsg('操作成功，共删除'+rows.length+'条记录');
								} else {
									Ict.error("删除失败!");
								}
							}
						});
					}
				});
			}
		},
		
		//在toolbar上面添加搜索框
		addSearchBox:function(width){
			$('.searchbox').css({'width': width+'px'});
			$('.searchbox-text').css({'width': eval(width-20)+'px'});
			$('.datagrid-toolbar').append($('.searchbox'));
//			$('.datagrid-toolbar').append($('#tbar'));
		},
		
		// 刷新菜单树（重新加载菜单配置信息）
		reload : function() {
			$('#grid').treegrid('reload');
		},
		
		//全部展开
		expandAll:function(){
			$('#grid').treegrid('expandAll');
		},
		//全部收起
		collapseAll:function(){
			$('#grid').treegrid('collapseAll');
		},
		
		//搜索函数
		doSearch:function(value){
			if($.trim(value)==""){
				Ict.info("请输入查询条件!");
				return;
			}
			$.ajax({
				url : '/pc/department/search.htm?deptName=' + encodeURI(value),
				type : 'post',
				success : function(data) {
					$("#grid").treegrid('unselectAll');
					for(var i = 0;i < data.length;i++){
						$("#grid").treegrid('select',data[i].department_id);
					}
				}
			});
		}
		
	};
}();

$(function(){
	Department.init();
});