Modify = function(){
	return {
		modify : function(userModifyId){
			$('#modifyAudit').window({
			     title: "请审核",
			     width: 750,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 600,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/userModify/toModifyAudit.htm?userModifyId="+userModifyId,
				 onMove:function(left,top){
					   	if(top<0){
					   		$(this).window('move',{left:left,top:0});
					   	}
					   	if(left<0){
					   		$(this).window('move',{left:0,top:top});
					   	}
					   },
				 modal:true
			});
			$('#modifyAudit').window('center');
		},
		
		saveModify : function(){
			$.ajax({
				type: "POST",
				url: "/pc/userModify/saveModify.htm",
				data: {'userModifyId' :$('#userModifyId').val()},
				success: function(data){
					if(data == "SUCCESS"){
						$.messager.alert('提示','审核通过!','info',function(){
							window.location.href = "/pc/userModify/list.htm";
						});
					}else if(data == "MOBILE"){
						$.messager.alert('提示','号码重复,号码不通过!','error');
					}else{
						$.messager.alert('提示','审核失败,请稍后再试!','error');
					}
				}
			});
		},
		
		refuseModify : function(){
			$.ajax({
				type: "POST",
				url: "/pc/userModify/refuseModify.htm",
				data: {'userModifyId' :$('#userModifyId').val()},
				success: function(data){
					if(data == "SUCCESS"){
						$.messager.alert('提示','保存成功!','info',function(){
							$('#form_modify').form('clear');
							window.location.href = "/pc/userModify/list.htm";
						});
					}else{
						$.messager.alert('提示','操作失败,请稍后再试!','error');
					}
				}
			});
		},
		
		cancelModify : function(){
			$('#form_modify').form('clear');
			window.location.href = "/pc/userModify/list.htm";
		}
	};
}();

$(function(){
	
});