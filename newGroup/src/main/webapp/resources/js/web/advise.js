Advise = function(){
	return {
		deleteAd : function(adviseId){
			$.messager.confirm("提示","确认删除?",function(result){
				if(result){
					$.ajax({
						url : '/pc/advise/delete.htm',
						data : {
							'adviseId' :adviseId
						},
						type : 'post',
						success : function(data) {
							if (data == "SUCCESS") {
								$.messager.alert("提示",'删除成功','info');
								window.location.href = "/pc/advise/main.htm?" + $("#searchForm").serialize();
							} else {
								$.messager.alert("提示",'删除失败','error');									
							}
						}
					});
				}else{
					return;
				}
			});
		},
	
	toReply : function(adviseId){
			$('#toReply').window({
			     title: "回复",
			     width: 580,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 500,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/reply/main.htm?adviseId="+adviseId,
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
		}
	};
}();