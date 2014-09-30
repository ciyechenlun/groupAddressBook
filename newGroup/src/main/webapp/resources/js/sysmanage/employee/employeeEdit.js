$.extend($.fn.validatebox.defaults.rules, {  
	validateTel: {  
        validator: function(value, param){  
        	return (/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/g.test(value));
        },  
        message: '请输入正确的手机号码'  
    }  
});  

updateEmployee = function() {
	return {
	edit: function(){
		$('#fm').form('submit', {
			url : "/pc/employee/editEmployee.htm",
			onSubmit : function() {
				return $("#fm").form("validate");
			},
			success : function(data) {
				if(data == "SUCCESS"){
		    		Ict.info("添加成功!",function(){
		    			Ict.closeWin();
		    			$("#employeetable").datagrid("reload");
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
	$("#status").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'status',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
		}
	});
	$('#isDeptLeader').combobox({
		url:'/pc/employee/isDeptLeader.htm',
		textField:'data_content',
		valueField:'data_code',
		required : true,
		editable:false,
		panelHeight:120
	});
	$('#departmentName').combotree({
		url:'/pc/client/deptTree.htm',
		editable:false,
		required:true,
		panelHeight:200
	});
});