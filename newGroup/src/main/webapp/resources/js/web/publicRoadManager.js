PublicRoadManager = function(){
	return {
		//变更状态：0：正常，1：暂停
		changeStatus : function(publicId,status){
			var newStatus = "";
			var info = "";
			if(status=="0"){
				newStatus = "1";
				info = "是否暂停该公告?";
			}else{
				newStatus = "0";
				info = "是否开始该公告?";
			}
			$.messager.confirm('提示信息',info,function(result){
				if(result){
					$.ajax({
						type: "POST",
					    url: "/pc/publicRoadManager/changeStatus.htm",
					    data: {'publicId' :publicId,'status':newStatus},
					    success: function(msg){
						  if(msg){
							  $.messager.alert('提示',"修改成功",'info',function(){
								  window.location.href = "/pc/publicRoadManager/main.htm?" + $("#searchForm").serialize();
							  });
						  }else{
							  $.messager.alert('提示',"修改失败",'error');
						  }
					   }
					});
				}
			});
			
		},
		//删除公告
		deleteInfo:function(publicId){
			$.messager.confirm('提示信息','是否删除该公告?',function(result){
				if(result){
					$.ajax({
						type: "POST",
					    url: "/pc/publicRoadManager/delete.htm",
					    data: {'publicId' :publicId},
					    success: function(msg){
						  if(msg){
							  $.messager.alert('提示',"公告删除成功",'info',function(){
								  window.location.href = "/pc/publicRoadManager/main.htm?" + $("#searchForm").serialize();
							  });
						  }else{
							   $.messager.alert('提示',"删除失败",'error');
						   }
					   }
					});
				}
			});
		},
		
		addPublicRoad:function(){
			 window.location.href = "/pc/publicRoadCreate/toBasic";
		},
		
		toEdit:function(id){
			 window.location.href = "/pc/publicRoadCreate/editPublicRoad.htm?publicId="+id+"&manager="+$("#manager").val();
		},
		
	};
}();