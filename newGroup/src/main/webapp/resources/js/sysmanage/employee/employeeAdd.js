$.extend($.fn.validatebox.defaults.rules, {  
	validateTel: {  
        validator: function(value, param){  
        	return (/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/g.test(value));
        },  
        message: '请输入正确的手机号码'  
    }  
});  

AddEmployee = function(){
	return {
		
		//保存
		save: function(){
			$('#fm').form('submit', {  
			    url:"/pc/employee/save.htm",  
			    onSubmit: function(){  
			    	return $("#fm").form("validate");
			    },  
			    success:function(data){  
			    	if(data == "SUCCESS"){
			    		Ict.info("添加成功!",function(){
			    			Ict.closeWin();
		    				$("#employeetable").datagrid("load");
			    		});
			    	}else if(data == "ERROR PHOTO"){
			    		Ict.error("请选择正确格式的图片！");
			    	}else if(data == "MOBILE"){
			    		Ict.error("手机号码重复,请重新输入！");
			    	}else{
			    		Ict.error("添加失败！稍后重试！");
			    	}
			    }  
				}); 
			}
	};
}();

$(function(){
	var selectDept=$('#employeeDeptTree').tree('getSelected');
	if(null != selectDept){
		$('#departmentId').val(selectDept.id);
		$('#departmentName').val(selectDept.text);
	}
	$("#status").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'status',
		valueField : "dataCode",
		textField : "dataContent",
		editable : false,
		panelHeight : 100
	});
	$('#isDeptLeader').combobox({
		url:'/pc/employee/isDeptLeader.htm',
		textField:'data_content',
		valueField:'data_code',
		required : true,
		editable:false,
		panelHeight:120
	});
});