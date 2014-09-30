SearchUser = function(){
	return {
		initData : function(){
			$('#searchUser').datagrid({  
		        url:'/pc/systemUser/checkUser.htm', 
		        fitColumns:true,
		        striped:true,
		        fit:true,
		        rownumbers:true,
		        singleSelect:true,
		        autoRowHeight:true,
		        nowrap:true,
		        pagination:true,
		        pageSize:20,
		        queryParams : {
					'createStartDate':"",
					'createEndDate' : "",
					'stopStartDate' : "",
					'stopEndDate':""
				},
		        loadMsg:"载入中...",
		        columns:[[  
		            {field:'user_id',title:'user_id',hidden:true,width:100},  
		            {field:'user_name',title:'登陆账号',width:100,align:'center'},
		            {field:'create_time',title:'创建时间',width:100,align:'center'},
		            {field:'modify_time',title:'停用时间',width:100,align:'center',formatter:function(value,rowData){
		            	if(rowData.del_flag=="1"){
		            		return value;
		            	}else{
		            		return "";
		            	}
		            }}
		        ]],
		        toolbar : '#toolBar'
		    }); 
		},
		
		doSearch : function(){
			var createStartDate = $('#createStartDate').datebox('getValue');
			var createEndDate = $('#createEndDate').datebox('getValue');
			var stopStartDate = $('#stopStartDate').datebox('getValue');
			var stopEndDate = $('#stopEndDate').datebox('getValue');
			if (createStartDate != '' && createEndDate != '') {
				if (createStartDate > createEndDate) {
					Ict.info('开始时间不能大于结束时间!');
					return;
				}
			}
			if (stopStartDate != '' && stopEndDate != '') {
				if (stopStartDate > stopEndDate) {
					Ict.info('开始时间不能大于结束时间!');
					return;
				}
			}
			$('#searchUser').datagrid({
				url:'/pc/systemUser/checkUser.htm', 
				pageNumber:1,
				queryParams : {
					'createStartDate':createStartDate,
					'createEndDate' : createEndDate,
					'stopStartDate' : stopStartDate,
					'stopEndDate':stopEndDate
				},
				toolbar : '#toolBar'
			});
		}
	};
}();

$(function(){
	SearchUser.initData();
});