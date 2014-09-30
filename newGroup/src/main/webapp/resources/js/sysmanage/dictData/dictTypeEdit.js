/**
  *元数据类型编辑
  **/

savedictType=function(){
	$('#dictTypeForm').form('submit', {  
	    url:"/pc/dictData/type/save.htm",  
	    onSubmit: function(){  
	    	return $("#dictTypeForm").form("validate");
	    },  
	    success:function(data){  
	    	if(data == "SUCCESS"){
	    		Ict.info("保存成功!",function(){
	    			Ict.closeWin();
	    			$("#dictTypeGrid").datagrid("reload");
	    		});
	    	}else{
	    		Ict.error("保存失败!稍后重试。");
	    	}
	    }  
	});  
};