Message = function(){
	return {
	//弹出选择人员框
	openWin:function(title,width,height,href){
	   if($('#msg_company').combobox('getValue')==null ||$('#msg_company').combobox('getValue')==""){
		   $.messager.alert("提示",'请选择公司','info');
		   return;
	   }
	   $('#choseUser').window({
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
			 top:(document.body.clientHeight-height)/2 ,   
	         left:(document.body.scrollWidth-width)/2-100,
			 modal:true
		});	
	},
	//点击删除对象里面的人员
	deletee:function(obj){
		$.messager.confirm('提示','确定删除',function(r){
			if(r){
				var name = $(obj).attr("name");
				$("#"+name).remove();
				$(obj).remove();
			}
		});
	},
 	//点击页面上面的取消按钮
 	cancelEdit:function(){
 		$("#save_form")[0].reset();
 		$('#empNameD').text('');
		$('#empIdD').text('');
		$('#checkboxName').attr("checked",false);
		$('#SelectedEmp').css({"background":"white"});
 	},
 	//点击页面上面的保存按钮
 	save:function(){
 		var flag=$('#checkboxName').attr("checked");
 		if($('#checkboxName').attr("checked")==undefined){ 
 			flag=false;
 		}else{
 			flag=true;
 		}
 		//表单验证
 		if($('#msg_company').combobox('getValue')==null ||$('#msg_company').combobox('getValue')=="")
 		{
 			 $.messager.alert("提示",'请选择公司','info');
 		}
 		else if($("#empIdD").text() == '' || $("#empIdD").text() == null || $("#empIdD").text() == undefined)
 		{
 			$.messager.alert('提示','请选择要推送的用户','info');
 		}
 		else if ($("#msg_type").combobox('getValue') == '' || $("#msg_type").combobox('getValue') == null)
 		{
 			$.messager.alert('提示','请选择消息类型','info');
 		}
 		else if($("#msg_type").combobox('getValue') == '084' && $("#msg_text").val() == '')
 		{
 			$.messager.alert('提示','请输入消息内容','info');
 		}else{
 			//验证通过后，
	 		$.messager.confirm('提示','确定推送消息',function(r){
	 			if(r){
					$.ajax({
						url : '/pc/message/sendMessage.htm',
						data : {
							'companyId':$('#msg_company').combobox('getValue'),
							'empIdD' :$('#empIdD').text(),
							'msg_text':$('#msg_text').val(),
							'checkboxName' :flag,
							'msg_type' :$("#msg_type").combobox('getValue')
						},
						type : 'post',
						success : function(data) {
							if (data == "SUCCESS") {
								$.messager.alert("提示",'推送成功','info');
								window.location.href = "/pc/message/main.htm";
							}else if(data == "FALSE"){
								$.messager.alert("提示",'推送失败,请稍后重试','error');									
							}else{
								$.messager.alert("提示",data,'warning');	
							}
						}
					});
				}else{
					return;
				}
	 		});
 		}
 	},
	disableObj:function(){
//		alert($('#msg_text').val());
		if($('#checkboxName').attr("checked")){
			$('#SelectedEmp').css({"background":"#a9bed5"});
			$('#empNameD').text('');
			$('#empIdD').text('');
			$('#empSelectButton').hide();
		}else{
			$('#SelectedEmp').css({"background":"white"});
			$('#empSelectButton').show();
		}
	}
	};
}();

$(function(){
	$('#msg_company').combobox({
		url :'/pc/message/company.htm',
		valueField : "company_id",
		textField  : "company_name"
	});
	$('#msg_type').combobox({
		url :'/pc/message/massageType.htm',
		valueField : "data_id",
		textField  : "data_content",
		onSelect : function(txt){
			if(txt.data_id=="085"||txt.data_id=="086"){
				$('#msg_text').val("");
				$('#msg_text').attr({"disabled":true});
			}else{
				$('#msg_text').attr({"disabled":false});
			}
		}
	});
});


