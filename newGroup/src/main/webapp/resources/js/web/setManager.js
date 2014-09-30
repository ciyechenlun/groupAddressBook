SetManager=function(){
	return{
		//添加人员树
		userTree:function(companyId){
			$('#managerTree').tree({
				url :'/pc/publicRoadCreate/getUserTree.htm?companyId=' + companyId,
				checkbox:true,
				onlyLeafCheck:true,
				onCheck:function(node,checked){
					if(checked){
						SetManager.addMember(node.id, node.text);
					}else{
						SetManager.delMember(node.id);
					}	
				}
			});
		},
		
		//添加选定对象
		addMember:function(id,name){
			var txt = "<li value="+id+">"+name+"</li>";
			$("#managerMember").append(txt);
		},
		//删除选定对象
		delMember:function(id){
			var selector = "li[value='"+id+"']";
			$(selector).remove();
		},
		
		complete:function(){
			var companyId =  window.parent.document.getElementById("companyId").value;
			
			var publicName = $("#publicName").val();
			var toRange = $("#toRange").val();
			var picture = $("#picture").val();
			var toName = $("#toName").val();
			var target = $("#target").val();
			
			var nodes = $('#managerTree').tree("getChecked");
			
			var managerMember="";
			 for (var i = 0; i < nodes.length; i++) {
                 managerMember += nodes[i].id+",";
             }
			 managerMember = managerMember.substring(0,managerMember.length-1);
			$.ajax({
				type: "POST",
				url: "/pc/publicRoadCreate/addPublicRoad.htm?companyId="+companyId,
				dataType: "JSON",
				data : {'publicName':publicName,'toRange':toRange,'picture':picture,'toName':toName,'target':target,'managerMember':managerMember},
				success: function(data){
					if(data){
						 $.messager.alert('提示',"添加成功",'info',function(){
							 var manager =  window.parent.document.getElementById("manager").value;
							 window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
						 });
					}else{
						$.messager.alert('提示',"添加失败",'error',function(){
							var manager =  window.parent.document.getElementById("manager").value;
							window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
						});
					}
				}
			});
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



$(function(){
	var companyId =  window.parent.document.getElementById("companyId").value;
	SetManager.userTree(companyId);
	
});