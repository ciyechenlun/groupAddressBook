RoadDraftbox = function(){
	return {
		//删除公告
		deleteInfo:function(noticeId){
			$.messager.confirm('提示信息','是否删除该记录?',function(result){
				if(result){
					$.ajax({
						type: "POST",
					    url: "/pc/publicRoadManager/deleteDraft.htm",
					    data: {'noticeId' :noticeId},
					    success: function(msg){
						  if(msg){
							  $.messager.alert('提示',"删除成功",'info',function(){
								  window.location.href = "/pc/publicRoadManager/toDraftbox.htm?" + $("#searchForm").serialize();
							  });
						  }else{
							   $.messager.alert('提示',"删除失败",'error');
						   }
					   }
					});
				}
			});
		},
		toDetailInfo:function(noticeId){
			window.location.href="/pc/notice/main.htm?noticeId="+noticeId;
		}
	};
	
	
}();