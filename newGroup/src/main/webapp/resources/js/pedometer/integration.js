var integration = function(){
	return {
		test:function()
		{
			return "";
		},
		btnEdit:function()
		{
			//更新积分
			$.get(
				"./updateIntegration.htm",
				function(ret)
				{
					if(ret == "SUCCESS")
					{
						$.messager.alert('提示','更新成功','info');
					}
					else
					{
						$.messager.alert('错误',ret,'error');
					}
				}
			);
		}
	};
}();

$(function(){
	
});