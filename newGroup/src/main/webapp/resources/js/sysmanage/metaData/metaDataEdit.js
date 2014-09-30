/**
  *元数据类型编辑
  **/

saveMetaData=function(){
	$('#metaDataFrom').form('submit', {  
	    url:"/pc/metaData/meta/save.htm",  
	    onSubmit: function(){  
	    	return $("#metaDataFrom").form("validate");
	    },  
	    success:function(data){  
	    	if(data == "SUCCESS"){
	    		Ict.info("保存成功!",function(){
	    			Ict.closeWin();
	    			$("#metaGrid").datagrid("reload");
	    		});
	    	}else{
	    		Ict.error("保存失败!稍后重试。");
	    	}
	    }  
	});  
};