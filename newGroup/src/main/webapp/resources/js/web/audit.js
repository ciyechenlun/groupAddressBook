Audit = function(){
	return {
		saveA : function(){
			$.ajax({
				type: "POST",
				url: "/pc/userModify/save.htm",
				data: {
					'value' :$("input[name='1']:checked").val()
				 },
				success: function(data){
					if(data == "SUCCESS"){
						$.messager.alert('提示','保存成功!','info',function(){
							window.location.href = "/pc/userModify/main.htm";
						});
					}else{
						$.messager.alert('提示','保存失败!','error');
					}
				}
			});
		},
		
		cancelA : function(){
			window.location.href = "/pc/userModify/main.htm";
		}
	};
}();

$(function(){
	if($("#num").val() > 0){
		document.getElementById("audit").checked=true;
	}else{
		document.getElementById("modify").checked=true;
	}
});