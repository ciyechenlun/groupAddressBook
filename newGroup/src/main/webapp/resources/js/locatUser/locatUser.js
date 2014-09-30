/**
 * 定位任务设置
 */

LocatUser = function(){
	return {
		init:function(){
			LocatUser.gridInit();
			LocatUser.treeInit();
			LocatUser.searchBoxInit();
		},
		edit:function(){
			var rows = $('#locatUserGrid').datagrid("getSelections");
			if(rows.length == 1){
				var employee_id = rows[0].employee_id+"_edit";
				Ict.openWin("关联模板",580,400,"/pc/locatUser/edit/"+employee_id+".htm");
			}else{
				Ict.alert("请选择一条记录!");
			}
		},
		deleteRecords:function(){
			var rows = $('#locatUserGrid').datagrid("getSelections");
			var empIds = "";
			for(var i=0;i<rows.length;i++){
				empIds += rows[i].employee_id + ",";
			}
			if(empIds){
				Ict.confirm("请否删除所选记录？",function(r){
					if(r){
						$.post("/pc/locatUser/delete.htm",{empIds:empIds},function(data){
							if(data == "SUCCESS"){
								Ict.alert("删除成功",function(){
									$('#locatUserGrid').datagrid("reload");
								});
							}else{
								Ict.error("删除失败，稍后重试！");
							}
						});
					}
				});
			}else{
				Ict.alert("没有选择记录!");
			}
		},
		searchBoxInit:function(){
			$('#searchbox').searchbox({  
		        searcher:function(value,name){  
	        		var node = $("#empTree").tree("getSelected");
	        		var idIcon = "";
	        		if(node){
	        			if(node.iconCls == "employee"){
							idIcon = node.attributes+"_employee";
						}else{
							idIcon = node.id;
						}
	        		}
	        		$('#locatUserGrid').datagrid({
						url:'/pc/locatUser/locatUsers.htm',
						queryParams:{
							idIcon:idIcon,
							keyword:value
						}
					}).reload;
		        },  
		        width:250,
		        prompt:'关键字(用户名或手机号码)'  
		    });  
		},
		//应用定位模板
		locatSetting:function(){
			var nodes = $('#empTree').tree('getChecked');
			var empIds = "";
			for(var i=0;i<nodes.length;i++){
				if(nodes[i].iconCls == "employee"){
					empIds  += nodes[i].attributes + ",";
				}
			}
			if(empIds == ""){
				Ict.alert("请选择人员!");
				return;
			}
			$.post("/pc/locatUser/validate.htm",{empIds:empIds},function(data){
				if(data.msg == "Y"){
					Ict.confirm(data.result+"&nbsp;已经指定模板，若继续会把之前模板重置，是否继续?",function(r){
						if(r){
							Ict.openWin("关联模板",580,400,"/pc/locatUser/edit/"+empIds+".htm");
						}
					});
				}else{
					Ict.openWin("关联模板",580,400,"/pc/locatUser/edit/"+empIds+".htm");
				}
			});
		},
		gridInit:function(){
			$('#locatUserGrid').datagrid({
				url:'/pc/locatUser/locatUsers.htm', 
		        fitColumns:true,
		        striped:true,
		        fit:true,
		        rownumbers:true,
		        autoRowHeight:true,
		        nowrap:true,
		        pagination:true,
		        loadMsg:"载入中...",
		        columns:[[  
		            {field:"ck",checkbox:true},
		            {field:'employee_id',title:'employee_id',hidden:true,width:100},  
		            {field:'employee_code',title:'人员编号',width:100},  
		            {field:'employee_name',title:'人员姓名',width:80},
		            {field:'mobile',title:'手机号',width:80},
		            {field:'cycle_starttime',title:'开始时间(时：分)',width:80},
		            {field:'cycle_endtime',title:'结束时间(时：分)',width:80},
		            {field:'cycle_stationfreq',title:'基站采集频率(分钟)',width:100},
		            {field:'weekCode',title:'星期数',width:220}
			    ]]
			 });
		},
		treeInit:function(){
			$("#empTree").tree({
				url:'/pc/immeUserInfo/tree/.htm',
				checkbox:true,
				animate:true,
				onClick: function(node){
					var idIcon = "";
					if(node.iconCls == "employee"){
						idIcon = node.attributes+"_employee";
					}else{
						idIcon = node.id;
					}
					$('#locatUserGrid').datagrid({
						url:'/pc/locatUser/locatUsers.htm',
						queryParams:{
							idIcon:idIcon
						}
					}).reload;
				}
			});
		}
	};
}();

$(function(){
	LocatUser.init();
});