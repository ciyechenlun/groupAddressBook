SetRoadBasicInfo=function(){
	return{
		//跳转至第二步设置发送对象
		toStepTwo:function(){
			var img = $("#picture").val();
			if(img){
				$('#comp_fm').form('submit',{
					url:"/pc/publicRoadCreate/uploadPhoto.htm",
					onSubmit: function(){
						return $('#comp_fm').form('validate');
					},
					success:function(data){
						if(data=="01"){
							$.messager.alert('提示',"添加失败",'error');
						} else if(data=="02"){
							$.messager.alert('提示',"导入格式错误",'error');
						}else{
							var publicName = $("#publicName").val();
							if(publicName=="输入公告名称，不超过8个字"){
								publicName = "";
							}
							var toRange = $(":checked").val();
							var picture =data;
							window.location.href = "/pc/publicRoadCreate/toSetSendTarget?publicName="+publicName+"&picture="+picture+"&toRange="+toRange;
						}
					}
					
				});
			}else{
				var publicName = $("#publicName").val();
				if(publicName=="输入公告名称，不超过8个字"){
					publicName = "";
				}
				var toRange = $(":checked").val();
				var picture =img;
				window.location.href = "/pc/publicRoadCreate/toSetSendTarget?publicName="+publicName+"&picture="+picture+"&toRange="+toRange;
			}
			
			
		},
		onBlur:function(){
			if($('#publicName').val()==""){
				$('#publicName').val("输入公告名称，不超过8个字");
			}
		},
		
		onfocus:function(){
			if($('#publicName').val()=="输入公告名称，不超过8个字"){
				$('#publicName').val("");
			}
		},
		//取消
		cancle:function(){
			$.messager.confirm('提示信息','是否取消添加?',function(result){
				if(result){
					var manager =  window.parent.document.getElementById("manager").value;
					window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
				}
				
			});
		}
	};
	
	
}();