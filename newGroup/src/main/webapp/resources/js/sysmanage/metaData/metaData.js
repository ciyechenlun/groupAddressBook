/**
 * 元数据管理对应js
 */

$(function(){
	//元数据类型列表
    $('#metaConfigGrid').datagrid({  
        url:'/pc/metaData/configTypes', 
        fitColumns:true,
        striped:true,
        fit:true,
        rownumbers:true,
        singleSelect:true,
        autoRowHeight:true,
        nowrap:true,
        loadMsg:"载入中...",
        columns:[[  
            {field:'typeId',title:'id',hidden:true,width:100},  
            {field:'typeName',title:'元数据类型名字',width:100},  
            {field:'typeCode',title:'类型编码',width:100},
            {field:'processClass',title:'操作类',hidden:true,width:100},  
            {field:'description',title:'描述',hidden:true,width:100} 
        ]],
        onClickRow:function(rowIndex,rowData){
        	var typeId = rowData.typeId;
        	$('#metaGrid').datagrid({ 
        		url:'/pc/metaData/metaDatas', 
                queryParams:{
                	typeId:typeId
                }}).load();
        },
        toolbar: [{
    		iconCls: 'add',
    		text:'添加',
    		handler: function(){
    			Ict.openWin("元数据类型添加",600,400,"/pc/metaData/type/configTypeEdit.htm");
    		}
    	},'-',{
    		iconCls: 'edit',
    		text:'修改',
    		handler: function(){
    			var record = $("#metaConfigGrid").datagrid("getSelected");
    			if(record){
    				Ict.openWin("元数据类型编辑",600,400,"/pc/metaData/type/configTypeEdit.htm?typeId="+record.typeId);
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    			
    		}
    	},'-',{
    		iconCls: 'remove',
    		text:'删除',
    		handler: function(){
    			var record = $("#metaConfigGrid").datagrid("getSelected");
    			if(record){
    				Ict.confirm("确定要删除所选记录？",function(r){
    					if(r){
    						$.post("/pc/metaData/type/detele.htm",{typeId:record.typeId},function(data){
    							if(data == "SUCCESS"){
    								Ict.info("删除成功!",function(){
    									$("#metaConfigGrid").datagrid("reload");
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
	
    //元数据列表
    $('#metaGrid').datagrid({  
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
            {field:'metaId',title:'元数据ID',hidden:true,width:100},  
            {field:'typeId',title:'配置类型ID',hidden:true,width:100},  
            {field:'metaName',title:'元数据名称',width:100},  
            {field:'metaCode',title:'元数据编码',width:100},  
            {field:'isAtom',title:'是否原子功能',width:100},  
            {field:'description',title:'描述',width:100},
            {field:'status',title:'状态',width:100}
        ]],
        toolbar: [{
    		iconCls: 'add',
    		text:'添加',
    		handler: function(){
    			Ict.openWin("元数据添加",600,400,"/pc/metaData/meta/metaDataEdit.htm");
        	}
    	},'-',{
    		iconCls: 'edit',
    		text:'修改',
    		handler: function(){
    			var record = $("#metaGrid").datagrid("getSelected");
    			if(record){
    				Ict.openWin("元数据编辑",600,400,"/pc/metaData/meta/metaDataEdit.htm?metaId="+record.metaId);
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    			
    		}
    	},'-',{
    		iconCls: 'remove',
    		text:'删除',
    		handler: function(){
    			var records = $("#metaGrid").datagrid("getSelections");
    			var len = records.length;
    			if(len == 1){
    				Ict.confirm("确定要删除所选记录？",function(r){
    					if(r){
    						var metaIds = "";
    						for(var i=0;i<len;i++){
    							metaIds += records[i].metaId + ",";
    						}
    						$.post("/pc/metaData/meta/detele.htm",{metaIds:metaIds},function(data){
    							if(data == "SUCCESS"){
    								Ict.info("删除成功!",function(){
    									$("#metaGrid").datagrid("reload",{});
    					    		});
    							}else{
    								Ict.error("删除失败！稍后重试。");
    							}
    						});
    					}
    				});
    			}else if(len > 1){
    				Ict.alert("请确定一条记录！");
    			}else if(len == 0){
    				Ict.alert("没有选中记录！");
    			}
    		}
    	}]  
    }); 
});

