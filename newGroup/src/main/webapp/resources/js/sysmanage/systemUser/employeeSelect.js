/**
 * 系统用户管理 人员选择窗口
 */

$(function(){
	
	alert($("#companyIdHidden").val());
    //公司部门树
	$('#detpartmentTree').tree({
		url:'/pc/department/departmentTree/'+$("#companyIdHidden").val()+'.htm',
		onClick: function(node){
		}
	});
	
	//元数据类型列表
    $('#systemUserGrid').datagrid({  
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
    			Ict.openWin("系统用户管理","600","500","/pc/systemUser/edit/''.htm");
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
});

