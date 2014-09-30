/**
  *元数据类型编辑
  **/

saveMetaConfigType=function(){
	$('#metaConfigTypeForm').form('submit', {  
	    url:"/pc/metaData/type/save.htm",  
	    onSubmit: function(){  
	    	return $("#metaConfigTypeForm").form("validate");
	    },  
	    success:function(data){  
	    	if(data == "SUCCESS"){
	    		Ict.info("保存成功!",function(){
	    			Ict.closeWin();
	    			$("#metaConfigGrid").datagrid("reload");
	    		});
	    	}else{
	    		Ict.error("保存失败!稍后重试。");
	    	}
	    }  
	});  
};