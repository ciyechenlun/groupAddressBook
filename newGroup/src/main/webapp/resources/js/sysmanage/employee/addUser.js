AddUser = function(){
	return {
		saveUser : function (){
			$('#user_fm').form('submit',{
				url:"/pc/employee/saveUser.htm",
				onSubmit: function(){
					return $('#user_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("启用成功",function(){
							$('#user_fm').form('clear');
							Ict.closeWin();
							$('#employeetable').datagrid('reload');
						});
					} else if(data=='NAME'){
						Ict.error("用户名已存在!");
					} else {
						Ict.error("保存失败!");
					}
				}
			});
		}
	};
}();

$(function(){
	$("#roleCombo").combobox({
		url:'/pc/employee/roleList.htm',
		valueField:'roleId',
		textField:'roleName',
		panelHeight:200,
		required:true,
		editable:false
	});
});

$.extend($.fn.validatebox.defaults.rules, {  
    equals: {  
        validator: function(value,param){  
            return value == $(param[0]).val();  
        },  
        message: '密码不一致!'  
    }  
});  