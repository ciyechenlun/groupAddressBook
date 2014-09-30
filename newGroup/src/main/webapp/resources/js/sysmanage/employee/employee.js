Employee = function(){
	return{
		//设置body的高度为文档的高度
		doLayout : function(){
			$('body').height($(document).height());
		},
		
		//查看图片
		photo : function(value){
			value = "/pc/item/images/" + value;
			art.dialog({
				content : '<img src='+value+'>',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				title: '员工图片',
				left: '20%',
			    top: '20%',
			    lock : true,
			    cancelVal: '关闭',
			    cancel: true
			});
		},
		
		//员工信息列表
		initGrid : function(){
			$('#employeetable').datagrid({
				nowrap : false,
				striped : true,
				queryParams : {
					'deptId':""
				},
				idField:'employee_id',
				url : '/pc/employee/getList.htm',
				fit : true,
				fitColumns : true,
				method : 'POST',
				loadMsg : '数据载入中，请稍等...',
				columns : [[{field:'employee_id',title:'员工Id',hidden:true,width:100},
							{field:'employee_code',title:'工号',width:60},
							{field:'employee_name',title:'姓名',width:60},  
							{field:'mobile',title:'手机号',width:50},
							{field:'deptName',title:'部门',width:60},
							{field:'status',title:'状态',width:30,align:'center',formatter:function(value,rows,index){
								if(value=='0'){
									return "在职";
								}else if(value=='1'){
									return "离职";
								}else{
									return "";
								}
							}},
							{field:'is_dept_leader',title:'是否部门领导',align:'center',width:40,formatter:function(value){
								if(value=='0'){
									return "是";
								}else{
									return "";
								}
							}},
							{field:'picture',title:'照片',width:30,align:'center',formatter:function(value,rowData){
						    	if(value == null || value == ""){
						    		return "";
						    	}else{
						    		return "<div><a href=\'javascript:Employee.photo(\""+value+"\")\'><font color='blue'>查看</font></a></div>";
						    	}	
						    }},
						    {field:'isUser',title:'禁用/启用账号',width:40,align:'center',formatter:function(value,rowData){
						    	if(value == 0){
						    		return "<div><a href=\'javascript:Employee.open(\""+rowData.employee_id+"\")\'><font color='blue'>启用</font></a></div>";
						    	}else{
						    		return "<div><a href=\'javascript:Employee.close(\""+rowData.employee_id+"\")\'><font color='red'>禁用</font></a></div>";
						    	}	
						    }}
							]],
					        toolbar : [{
								text : '添加',
								iconCls : 'add',
								handler : function() {
									var selectDept=$('#employeeDeptTree').tree('getSelected');
									if(null == selectDept){
										Ict.info("请先在左侧部门树中选择部门");
									}else{
										if(selectDept.iconCls == "company"){
											Ict.info("请选择公司下的部门!");
										}else{
											Ict.openWin('添加员工',400,350,'/pc/employee/add.htm');
										}
									}
								}
							},{
								text : '修改',
								iconCls : 'edit',
								handler : function() {
									var row = $('#employeetable').datagrid('getSelections');
									if (row.length == 0) {
										Ict.error("请选择你要修改的员工",function(){
											return;
										});
									}else if(row.length > 1){
										Ict.error("请选择一条记录进行修改",function(){
											return;
										});
									}else if(row.length == 1)
										Ict.openWin('编辑员工',400,350,'/pc/employee/edit.htm?employeeId='+ row[0].employee_id);
									
								}
							}, {
								text : '删除',
								iconCls : 'remove',
								handler : function() {
									var rows = $('#employeetable').datagrid('getSelections');
									var num = rows.length;
									var employees = null;
									if (rows.length == 0) {
										Ict.error("请选择你要删除的员工",function(){
											return;
										});
									} else {
										Ict.confirm("确定要删除选中的员工吗?",	
											function(result) {
													if (result) {
														for ( var i = 0; i < num; i++) {
															if (null == employees || i == 0) {
																employees = rows[i].employee_id;
															} else {
																employees = employees + ","
																		+ rows[i].employee_id;
															}
														}
														$.ajax({
															url : "/pc/employee/del.htm",
															type : 'get',
															context : document.body,
															data : {
																"employees" : employees
															},
															dataType : 'json',
															success : function(data) {
																if (data == 1) {
																	Ict.info("删除成功!",function(){
																		$('#employeetable').datagrid('reload');
																	});
																} else {
																	Ict.error("删除失败!",function(){
																		$('#employeetable').datagrid('reload');
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
									$('#employeetable').datagrid('reload');
								}
							},'->'],
				pagination : true,
				pageSize:20,
				rownumbers : true
			});
		},
		
//		//添加搜索框
//		addSearchBox:function(width){
//			$('.searchbox').css({'width': width+'px'});
//			$('.searchbox-text').css({'width': eval(width-20)+'px'});
//			$('.datagrid-toolbar').append($('.serarchbox'));
//			$('.datagrid-toolbar').append($('#tbar'));
//		},
//		
		//搜索函数
		doSearch:function(value){
			$('#employeetable').datagrid({
				nowrap : false,
				striped : true,
				url : '/pc/employee/allByCondition.htm',
				queryParams : {
					'employeeName':value
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
		},
		//初始化
		init : function(){
			this.doLayout();
			this.initGrid();
//			this.addSearchBox(250);
		},
		
		//启用账号
		open : function(value){
			Ict.openWin('启用账号',350,300,'/pc/employee/addUser.htm?employeeId='+ value);
		},
		
		//禁用账号
		close : function(value){
			Ict.confirm("确定要禁用当前员工的账号吗?",	
					function(result) {
							if (result) {
								$.ajax({
									url : "/pc/employee/deleteUser.htm",
									data : {
										"employeeId" : value
									},
									dataType : 'json',
									success : function(data) {
										if (data == "SUCCESS") {
											Ict.info("禁用成功!",function(){
												$('#employeetable').datagrid('reload');
											});
										} else {
											Ict.error("禁用失败!",function(){
												$('#employeetable').datagrid('reload');
											});
										}
									}
								});
							}
						});
		}
		};
}();

$(function(){
	Employee.init();
	//部门树
	$('#employeeDeptTree').tree({
		url:'/pc/employee/deptTree.htm',
		onClick : function(node){
			$('#employeetable').datagrid({
				url : '/pc/employee/getList.htm',
				queryParams : {
					'deptId':node.iconCls +"_"+node.id
				},
				toolbar : '#toolBar'
			});
		},
		onDblClick : function(node){
			$('#employeeDeptTree').tree('toggle',node.target);
		},
		onLoadSuccess : function(){
			$('#employeeDeptTree').tree('expandAll');
		}
	});
});