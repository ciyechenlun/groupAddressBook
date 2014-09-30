Right = function(){
	return {
		
		save : function(){
			var ids = "";
			var inputs = document.getElementsByTagName("input");
			for(var i=0; i< inputs.length; i++){
				if(inputs[i].type == "checkbox"){
					if(inputs[i].checked==true){
						if(i==0){
							ids = inputs[i].id;
						}else{
							ids += "_" + inputs[i].id;
						}
					}
				}
			}
			$.ajax({
    			url : '/pc/right/save.htm',
				data : {
					'ids' :ids
				},
				type : 'post',
				async : false,
				success : function(data) {
					if(data=="SUCCESS"){
						$.messager.alert("提示",'保存成功!','info');
					}else{
						$.messager.alert("提示",'保存失败,请稍后再试!','error');
					}
				}
    		});
		},
		
		cancel : function(){
			var inputs = document.getElementsByTagName("input");
			for(var i=0; i< inputs.length; i++){
				if(inputs[i].type == "checkbox"){
					if(inputs[i].checked==true){
						inputs[i].checked = false;
					}
				}
			}
		}
		
	};
}();

$(function(){
	
});