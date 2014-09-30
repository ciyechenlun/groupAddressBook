Company = function(){
	return {
		
		//添加搜索框
		addSearchBox:function(width){
			$('.searchbox').css({'width': width+'px'});
			$('.searchbox-text').css({'width': eval(width-20)+'px'});
			$('.datagrid-toolbar').append($('.serarchbox'));
			$('.datagrid-toolbar').append($('#tbar'));
		},
		
		//查询
		doSearch:function(value){
			if($('#roleCode').val()!='0'){
				Ict.info("您无权进行此操作!");
				return;
			}
			if($.trim(value) ==""){
				Ict.info("请输入查询条件!");
				return;
			}
			$.ajax({
				url : '/pc/company/search.htm?companyName=' + encodeURI(value),
				type : 'post',
				success : function(data) {
					$("#company_tt").treegrid('unselectAll');
					for(var i = 0;i < data.length;i++){
						$("#company_tt").treegrid('select',data[i].company_id);
					}
				}
			});
		},
		
		//查看图片
		photo:function(value){
			value = "/pc/company/images/" + value;
			art.dialog({
				content : '<img src='+value+'>',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				left: '40%',
			    top: '30%',
			    lock : true,
			    cancelVal: '关闭',
			    cancel: true
			});
		},
		
		//初始化treegrid
		initGrid : function(){
			$('#company_tt').treegrid({
				nowrap: false,
				rownumbers: true,
				animate:true,
				checkbox:true,
				collapsible:true,
				url:'/pc/company/getAll.htm',
				singleSelect:false,
				fit:true,
				fitColumns:true,
				idField:'company_id',
				treeField:'company_name',
				columns:[[
				    {field:'company_id',title:'企业Id',width:50,checkbox : true},
				    {field:'company_code',title:'企业编号',width:50},
					{field:'company_name',title:'企业名称',width:140},
					{field:'contact_man',title:'联系人',width:60},
					{field:'telephone',title:'联系电话',width:60},
					{field:'index_logo',title:'企业标识',width:25,formatter:function(value,rowData){
				    	if(value == null || value == ""){
				    		return "";
				    	}else{
				    		return "<div><a href=\'javascript:Company.photo(\""+value+"\")\'><font color='blue'>查看</font></a></div>";
				    	}	
				    }}
				]],
				toolbar : [{
					text : '添加',
					iconCls : 'add',
					handler : function() {
						if($('#roleCode').val()=='0'){
							Ict.openWin("新增企业",600,300,"/pc/company/add.htm");
						}else{
							Ict.info("您无权进行此操作!");
						}
					}
				}, {
					text : '修改',
					iconCls : 'edit',
					handler : function() {
						if($('#roleCode').val()!='0'&&$('#roleCode').val()!='4'){
							Ict.info("您无权进行此操作!");
							return;
						}
						var row = $('#company_tt').treegrid('getSelections');
						if (row.length == 0) {
							Ict.info("请选择你要修改的企业!");
							return;
						}else if(row.length > 1){
							Ict.info("请选择一条记录进行修改!");
							return;
						}else if(row.length == 1)
							Ict.openWin('修改企业信息',420,330,"/pc/company/update.htm?companyId="+ row[0].company_id);
					}
				}, {
					text : '删除',
					iconCls : 'remove',
					handler : function(){
						if($('#roleCode').val()!='0'){
							Ict.info("您无权进行此操作!");
							return;
						}
						var rows = $('#company_tt').treegrid('getSelections');
						var companyIds = null;
						if (rows.length == 0) {
							Ict.info("请选择你要删除的企业!");
							return;
						}
						for ( var i = 0; i < rows.length; i++){
							if(rows[i].have_child_company == "Y"){
								Ict.info("请先删除子企业!");
								return;
							}
						}
						Ict.confirm('确定要删除选中的公司吗?', function(result) {
							if (result) {
								for ( var i = 0; i < rows.length; i++) {
									if (null == companyIds || i == 0) {
										companyIds = rows[i].company_id;
									} else {
										companyIds = companyIds + "," + rows[i].company_id;
									}
								}
								$.ajax({
									url : '/pc/company/delete.htm?companyIds=' + companyIds,
									type : 'post',
									success : function(data) {
										if (data == "SUCCESS") {
											$('#company_tt').treegrid('reload');
											Ict.slideMsg('操作成功，共删除'+rows.length+'条记录');
										} else {
											Ict.error("删除失败!");
										}
									}
								});
							}
						});
					}
				} ,{
					text : '刷新',
					iconCls : 'reload',
					handler : function() {
						$('#company_tt').treegrid('reload');
					}
				} 
				/*,
				'-',{
					text : '全部展开',
					iconCls : 'expand',
					handler : function() {
						$('#company_tt').treegrid('expandAll');
					}
				},{
					text : '全部收起',
					iconCls : 'collapse',
					handler : function() {
						$('#company_tt').treegrid('collapseAll');
					}
				}*/
				]
		});
	},
	// 初始化
	init : function() {
		this.initGrid();
		this.addSearchBox(250);
	}
	};
}();
		

$(function(){
	Company.init();
});