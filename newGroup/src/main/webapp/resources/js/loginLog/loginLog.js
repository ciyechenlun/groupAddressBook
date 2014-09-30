LoginLog = function(){
	return {
		
		initGrid : function(){
			$('#loginLog').datagrid({  
		        url:'/pc/loginLog/list.htm', 
		        fitColumns:true,
		        striped:true,
		        fit:true,
		        rownumbers:true,
		        singleSelect:false,
		        autoRowHeight:true,
		        nowrap:true,
		        pagination:true,
		        pageSize:20,
		        queryParams : {
					'startTime':"",
					'endTime' : ""
				},
		        loadMsg:"载入中...",
		        columns:[[  
		            {field:'id',title:'id',hidden:true,width:100},  
		            {field:'user_name',title:'用户名',width:100,align:'center'},
		            {field:'operate_type',title:'操作类型',width:50,align:'center',formatter:function(value){
		            	if(value=="1"){
		            		return "登录";
		            	}else if(value=="2"){
		            		return "注销";
		            	}
		            }},
		            {field:'operate_time',title:'操作时间',width:100,align:'center'},
		            {field:'login_ip',title:'登录IP',width:100,align:'center'},
		            {field:'client_type',title:'客户端类型',width:50,align:'center',formatter:function(value){
		            	if(value=="1"){
		            		return "电脑";
		            	}else if(value=="2"){
		            		return "手机";
		            	}
		            }},
		            {field:'login_result',title:'登录结果',width:60,align:'center',formatter:function(value){
		            	if(value=="1"){
		            		return "成功";
		            	}else if(value=="0"){
		            		return "失败";
		            	}
		            }},
		            {field:'log_content',title:'日志内容',width:200,align:'center'},
		            {field:'time_consuming',title:'登录耗时',width:60,align:'center'}
		        ]],
		        toolbar : '#toolBar'
		    }); 
		},
		
		doSearch : function(){
			var startTime = $('#startTime').datebox('getValue');
			var endTime = $('#endTime').datebox('getValue');
			if (startTime != '' && endTime != '') {
				if (startTime > endTime) {
					Ict.info('开始时间不能大于结束时间!');
					return;
				}
			}
			$('#loginLog').datagrid({
				url:'/pc/loginLog/list.htm', 
				pageNumber:1,
				queryParams : {
					'startTime':startTime,
					'endTime' : endTime
				},
				toolbar : '#toolBar'
			});
		},
		
		exportLog : function(){
			$('#export_startTime').val($('#startTime').datebox('getValue'));
			$('#export_endTime').val($('#endTime').datebox('getValue'));
			$('#exportForm_log').form('submit',{
				url:'/pc/loginLog/export.htm',
				success:function(data){
					$.messager.alert(data);
				}
			});
		},
		
		deleteLog : function(){
			var rows = $('#loginLog').datagrid('getSelections');
			var logIds = null;
			if (rows.length == 0) {
				Ict.info("请选择你要删除的记录!");
				return;
			}else{
				Ict.confirm('确定要删除选中的记录吗?', function(result){
					if (result){
						for ( var i = 0; i < rows.length; i++) {
							if (null == logIds || i == 0) {
								logIds = rows[i].id;
							} else {
								logIds = logIds + "," + rows[i].id;
							}
						}
					}
					$.ajax({
						url : '/pc/loginLog/delete.htm',
						data:{
							  'logIds':logIds
						  },
						type : 'post',
						success : function(data) {
							if (data == "SUCCESS") {
								$('#loginLog').datagrid('reload');
								$('#loginLog').datagrid('clearSelections');
								Ict.slideMsg('操作成功，共删除'+rows.length+'条记录');
							} else {
								Ict.error("删除失败!");
							}
						}
					});
					});
				}
		}
	};
}();

$(function(){
	LoginLog.initGrid();
});