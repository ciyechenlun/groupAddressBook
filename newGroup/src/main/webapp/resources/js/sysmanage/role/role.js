Role = function() {
	return {
		// 设置body的高度为文档的高度
		doLayout : function() {
			$('body').height($(document).height());
		},

		// 初始化表格
		initGrid : function() {
			$('#roletable')
					.datagrid(
							{
								nowrap : false,
								striped : true,
								url : '/pc/role/all.htm',
								queryParams : {
								// 'roleName':'',
								},
								fit : true,
								pageSize : 5,
								fitColumns : true,
								method : 'POST',
								loadMsg : '数据载入中，请稍等...',
								columns : [ [ {
									field : 'role.rolebox',
									title : 'role.rolebox',
									checkbox : true
								}, {
									field : 'roleId',
									title : '角色Id',
									width : 50,
									sortable : true,
									hidden : true
								}, {
									field : 'roleName',
									title : '角色名称',
									width : 50,
									sortable : true
								}, {
									field : 'dataContent',
									title : '角色类型',
									width : 50,
									sortable : true
								}, {
									field : 'status',
									title : '状态',
									width : 50,
									sortable : true,
									formatter:function(value,rows,index){
						            	if(value == 0){
											return "启用";
										}else if(value == 1){
											return "禁用";
										}else{
											return value;
										}
										
						            }
								}, {
									field : 'displayOrder',
									title : '显示顺序',
									width : 50,
									sortable : true,
									hidden : true
								}, {
									field : 'description',
									title : '描述',
									width : 50,
									sortable : true
								}, {
									field : 'companyId',
									title : '公司Id',
									width : 50,
									sortable : true,
									hidden : true
								}, {
									field : 'companyName',
									title : '公司名称',
									width : 50,
									sortable : true
								}] ],
								toolbar : [
										{
											text : '添加',
											iconCls : 'add',
											handler : function() {
												Ict.openWin('添加角色', 400, 300,
														'/pc/role/add.htm');
											}
										},
										{
											text : '修改',
											iconCls : 'edit',
											handler : function() {
												var row = $('#roletable')
														.datagrid(
																'getSelections');
												if (row.length == 0) {
													Ict.error('请选择你要修改的角色',
															function() {
																return;
															});
												} else if (row.length > 1) {
													Ict.error('请选择一条记录进行修改',
															function() {
																return;
															});
												} else if (row.length == 1)
													Ict
															.openWin(
																	'角色修改',
																	400,
																	300,
																	'/pc/role/edit.htm?roleId='
																			+ row[0].roleId);
											}
										},
										{
											text : '删除',
											iconCls : 'remove',
											handler : function() {
												var rows = $('#roletable')
														.datagrid(
																'getSelections');
												var num = rows.length;
												var roles = null;
												if (rows.length == 0) {
													Ict.error('请选择你要删除的角色',
															function() {
																return;
															});
												} else {
													Ict
															.confirm(
																	"确定要删除所选记录？",
																	function(
																			result) {
																		if (result) {
																			for ( var i = 0; i < num; i++) {
																				if (null == roles
																						|| i == 0) {
																					roles = rows[i].roleId;
																				} else {
																					roles = roles
																							+ ","
																							+ rows[i].roleId;
																				}
																			}

																			$
																					.ajax({
																						url : "/pc/role/delRole.htm",
																						type : 'get',
																						context : document.body,
																						data : {
																							"roles" : roles
																						},
																						dataType : 'json',
																						success : function(
																								data) {
																							if (data == 1) {
																								// $.messager.alert('提示',
																								// "删除成功!",'info');
																								Ict.info("删除成功!",function() {
																									$("#roletable").datagrid("reload");});
																							} else {
																								// $.messager.alert('提示',"删除失败!");
																								// $('#roletable').datagrid('reload');
																								Ict
																										.error(
																												"删除失败！稍后重试。",
																												function() {
																													return;
																												});
																							}
																						}
																					});
																		}
																	});
												}
											}
										},
										{
											text : '刷新',
											iconCls : 'reload',
											handler : function() {
												$('#roletable').datagrid(
														'reload');
											}
										}
										, '->' ],
								pagination : true,
								rownumbers : true
							});
		},
		// 初始化
		init : function() {
			this.doLayout();
			this.initGrid();
			this.addSearchBox(250);
		},
		// 在toolbar上面添加搜索框
		addSearchBox : function(width) {
			$('.searchbox').css({
				'width' : width + 'px'
			});
			$('.searchbox-text').css({
				'width' : eval(width - 20) + 'px'
			});
			$('.datagrid-toolbar').append($('.serarchbox'));
			$('.datagrid-toolbar').append($('#tbar'));
		},
		// 搜索函数
		doSearch : function(value) {
			$('#roletable').datagrid({
				nowrap : false,
				striped : true,
				url : '/pc/role/allByCondition.htm',
				queryParams : {
					'roleName' : value
				},
				loadMsg : '数据载入中，请稍等...',
				fitColumns : true,
				striped : true,
				pagination : true,
				rownumbers : true,
				method : 'post',
				fit : true,
				toolbar : '#toolBar',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ]
			});
		}
	};
}();

$(function() {
	Role.init();
});
