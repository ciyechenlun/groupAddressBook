/**
 * 系统用户管理
 */

$(function(){
	//系统用户列表
    $('#systemUserGrid').datagrid({  
        url:'/pc/systemUser/systemUsers.htm', 
        fitColumns:true,
        striped:true,
        fit:true,
        rownumbers:true,
        singleSelect:true,
        autoRowHeight:true,
        nowrap:true,
        pagination:true,
        pageSize:20,
        loadMsg:"载入中...",
        columns:[[  
            {field:'id',title:'id',hidden:true,width:100},  
            {field:'companyName',title:'公司',width:100},  
            {field:'roleName',title:'角色',width:100},
            {field:'employeeCode',title:'员工编码',width:100},
            {field:'realName',title:'真实姓名',width:100},
            {field:'userName',title:'登录帐号',width:100},
            {field:'mobile',title:'手机号',width:100},
            {field:'loginTime',title:'最近登录时间',width:100}
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
    			Ict.openWin("系统用户管理","600","500","/pc/systemUser/edit/.htm");
    		}
    	},'-',{
    		iconCls: 'edit',
    		text:'修改',
    		handler: function(){
    			var record = $("#systemUserGrid").datagrid("getSelected");
    			if(record){
    				Ict.openWin("系统用户编辑",600,400,"/pc/systemUser/edit/"+record.id+".htm");
    			}else{
    				Ict.alert("没有选中记录！");
    			}
    			
    		}
    	},'-',{
    		iconCls: 'remove',
    		text:'删除',
    		handler: function(){
    			var record = $("#systemUserGrid").datagrid("getSelected");
    			if(record){
    				Ict.confirm("确定要删除所选记录？",function(r){
    					if(r){
    						$.post("/pc/systemUser/delete/"+record.id+".htm",{typeId:record.id},function(data){
    							if(data == "SUCCESS"){
    								Ict.info("删除成功!",function(){
    									$("#systemUserGrid").datagrid("reload");
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
	
    //公司部门树
	$('#companyDeptTree').tree({
		url:'/pc/systemUser/companyDeptTree.htm',
		onClick: function(node){
			var idIcon = node.id+"_"+node.iconCls;
			$('#systemUserGrid').datagrid({
				url:'/pc/systemUser/systemUsers.htm', 
				queryParams:{
					idIcon:idIcon
				}
			}).load();
			
		}
	});
});

