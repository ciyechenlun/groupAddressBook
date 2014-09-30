/**
  *元数据类型编辑
  **/

saveDictData=function(){
	$('#dictDataForm').form('submit', {  
	    url:"/pc/dictData/data/save.htm",  
	    onSubmit: function(){  
	    	return $("#dictDataForm").form("validate");
	    },  
	    success:function(data){  
	    	if(data == "SUCCESS"){
	    		Ict.info("保存成功!",function(){
	    			Ict.closeWin();
	    			$("#dictDataGrid").datagrid("reload");
	    		});
	    	}else{
	    		Ict.error("保存失败!稍后重试。");
	    	}
	    }  
	});  
};