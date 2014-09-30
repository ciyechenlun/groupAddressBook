/**
 * 数据字典管理。js
 */

$(function(){
	//数据字典类型列表
    $('#dictTypeGrid').datagrid({  
        url:'/pc/dictData/type/dictTypes.htm', 
        fitColumns:true,
        striped:true,
        fit:true,
        rownumbers:true,
        singleSelect:true,
        autoRowHeight:true,
        nowrap:true,
        pagination:true,
        loadMsg:"载入中...",
        columns:[[  
            {field:'type_id',title:'typeId',hidden:true,width:100},  
            {field:'company_id',title:'companyId',hidden:true,width:100},  
            {field:'company_name',title:'公司名称',hidden:true,width:100},  
            {field:'type_name',title:'字典类型名称',width:100},  
            {field:'type_code',title:'字典类型编码',width:100},
            {field:'description',title:'描述',hidden:true,width:100} 
        ]],
        onClickRow:function(rowIndex,rowData){
        	var typeId = rowData.type_id;
        	$('#dictDataGrid').datagrid({ 
        		url:'/pc/dictData/data/metaDatas.htm', 
                queryParams:{
                	typeId:typeId
                }}).load();
        },
        toolbar: [{
    		iconCls: 'add',
    		text:'添加',
    		handler: function(){
    			Ict.openWin("数据字典类型添加",600,400,"/pc/dictData/type/dictTypeEdit.htm");
    		}
    	},'-',{
    		iconCls: 'edit',
    		text:'修改',
    		handler: function(){
    			var record = $("#dictTypeGrid").datagrid("getSelected");
    			if(record){
    				Ict.openWin("数据字典类型编辑",600,400,"/pc/dictData/type/dictTypeEdit.htm?typeId="+record.type_id);
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    			
    		}
    	},'-',{
    		iconCls: 'remove',
    		text:'删除',
    		handler: function(){
    			var record = $("#dictTypeGrid").datagrid("getSelected");
    			if(record){
    				Ict.confirm("确定要删除所选记录？",function(r){
    					if(r){
    						$.post("/pc/dictData/type/detele.htm",{typeId:record.type_id},function(data){
    							if(data == "SUCCESS"){
    								Ict.info("删除成功!",function(){
    									$("#dictTypeGrid").datagrid("reload");
    									$("#dictDataGrid").datagrid("reload");
    					    		});
    							}else{
    								Ict.error("删除失败！稍后重试。");
    							}
    						});
    					}
    				});
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    		}
    	}]
    });  
	
    //数据字典数据
    $('#dictDataGrid').datagrid({  
        fitColumns:true,
        striped:true,
        fit:true,
        rownumbers:true,
        //singleSelect:true,
        autoRowHeight:true,
        nowrap:true,
        pagination:true,
        loadMsg:"载入中...",
        columns:[[  
            {field:'data_id',title:'ID',hidden:true,width:100},  
            {field:'data_code',title:'数据编码',width:100},  
            {field:'data_content',title:'数据内容',width:100},  
            {field:'description',title:'数据描述',width:100},  
            {field:'status',title:'状态',width:100}
        ]],
        toolbar: [{
    		iconCls: 'add',
    		text:'添加',
    		handler: function(){
    			Ict.openWin("数据字典数据添加",600,400,"/pc/dictData/data/dictDataEdit.htm");
        	}
    	},'-',{
    		iconCls: 'edit',
    		text:'修改',
    		handler: function(){
    			var record = $("#dictDataGrid").datagrid("getSelected");
    			if(record){
    				Ict.openWin("数据字典数据编辑",600,400,"/pc/dictData/data/dictDataEdit.htm?dataId="+record.data_id);
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    			
    		}
    	},'-',{
    		iconCls: 'remove',
    		text:'删除',
    		handler: function(){
    			var records = $("#dictDataGrid").datagrid("getSelections");
    			var len = records.length;
    			if(len > 0){
    				Ict.confirm("确定要删除所选记录？",function(r){
    					if(r){
    						var dataIds = "";
    						for(var i=0;i<len;i++){
    							dataIds += records[i].data_id + ",";
    						}
    						$.post("/pc/dictData/data/detele.htm",{dataIds:dataIds},function(data){
    							if(data == "SUCCESS"){
    								Ict.info("删除成功!",function(){
    									$("#dictDataGrid").datagrid("reload");
    					    		});
    							}else{
    								Ict.error("删除失败！稍后重试。");
    							}
    						});
    					}
    				});
    			}else if(len == 0){
    				Ict.alert("没有选中记录！");
    			}
    		}
    	}]  
    }); 
});

