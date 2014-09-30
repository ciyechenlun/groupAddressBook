var medal = function(){
	return {
		//添加或编辑对话框
		btnAdd_OnClick:function(type,medal_sys_id){
			var title = "勋章系统管理";
			var width = 640;
			var height = 200;
			var href = "medalEdit.htm?type=" + type;
			if(medal_sys_id!='')
			{
				href += "&medal_sys_id=" + medal_sys_id;
			}
			else
			{
				href += "&medal_sys_id=";
			}
			$('#medalDialog').window({
			     title: title,
			     width: width,
			  // shadow: false,
			     closed: false,
				 cache:  false,
			     height: height,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:href,
				 top:50 ,   
		         left:(document.body.scrollWidth-width)/2-100,
				 modal:true,
				 onLoad:function(){
				 }
			});	
		},
		
		//保存编辑或添加
		saveMedal:function(type)
		{
			//表单验证
			var medalSysName = $('#medalSysName').val();
			var medalPicture = $("#medalPicture").val(); 
			if(medalSysName == '')
			{
				$.messager.alert('提示','勋章名称不能为空','info');
				return false;
			}
			else if(medalPicture == '')
			{
				$.messager.alert('提示','勋章图片不能为空','info');
				return false;
			}
			else
			{
				//图片格式判断
				var fileType = medalPicture.substring(medalPicture.lastIndexOf(".") + 1);
				if(fileType!='zip')
				{
					$.messager.alert('提示','勋章图片必须以zip格式包进行上传','error');
					return false;
				}
				else{
					//提交表单
					$('#fmMedal').form(
						'submit',
						{
							url:"./medalSysEditSubmit.htm?type=" + type,
							success:function(ret){
								ret = eval("("+ret+")")
								var code = ret.code;
								if(code == '0')
								{
									window.location.href = './medalList.htm?medal_sys_id=' + ret.msg;
								}
								else
								{
									$.messager.alert('提示',ret.msg,'error');
									return false;
								}
							}
						}
					);
				}
				
			}
			return false;
		},
		
		//表单取消
		cancel:function()
		{
			$("#medaltDialog").window('close');
		},
		
		//提交勋章详情修改
		btnEditMedal_OnClick:function()
		{
			var medal_sys_id = $("#medal_sys_id").val();
			//提交表单
			$('#fmMedal').form(
				'submit',
				{
					url:"./medalEditSubmit.htm?medal_sys_id=" + medal_sys_id,
					success:function(ret){
						ret = eval("("+ret+")");
						var code = ret.code;
						if(code == '0')
						{
							window.location.reload();
						}
						else
						{
							$.messager.alert('提示',ret.msg,'error');
							return false;
						}
					}
				});
		}
	};
}();

$(function(){
	
});