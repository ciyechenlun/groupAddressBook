SetSendTarget=function(){
	return{
		//添加部门树
		departmentTree:function(companyId){
			$('#targetTree').tree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + companyId,
				checkbox:true,
				onlyLeafCheck:true,
				onCheck:function(node,checked){
					if(checked){
						SetSendTarget.addMember(node.id, node.text);
					}else{
						SetSendTarget.delMember(node.id);
					}				
				}
			});
		},
		//添加人员树
		userTree:function(companyId){
			$('#targetTree').tree({
				url :'/pc/publicRoadCreate/getUserTree.htm?companyId=' + companyId,
				checkbox:true,
				onlyLeafCheck:true,
				onCheck:function(node,checked){
					if(checked){
						SetSendTarget.addMember(node.id, node.text);
					}else{
						SetSendTarget.delMember(node.id);
					}	
				}
			});
		},
		
		//添加选定对象
		addMember:function(id,name){
			var txt = "<li id="+id+">"+name+"</li>";
			$("#targetMember").append(txt);
			
		},
		
		//删除选定对象
		delMember:function(id){
			var selector = "li[id='"+id+"']";
			$(selector).remove();
		},
		
		toStepThree:function(){
			var publicName = $("#publicName").val();
			var toRange = $("#toRange").val();
			var picture = $("#picture").val();
			var toName = $("#toName").val();
			var nodes = $('#targetTree').tree("getChecked");
			var targetMember="";
			 for (var i = 0; i < nodes.length; i++) {
                 targetMember += nodes[i].id+",";
             }
			targetMember = targetMember.substring(0,targetMember.length-1);
			window.location.href = "/pc/publicRoadCreate/toSetManager?publicName="+publicName+"&picture="
				+picture+"&toRange="+toRange+"&toName="+toName+"&target="+targetMember;
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
	var toRange = $("#toRange").val();
	if(toRange=='1'){
		SetSendTarget.departmentTree(companyId);
		$(".othChoice").hide();
	}else if(toRange=='2'){
		SetSendTarget.userTree(companyId);
		$(".othChoice").hide();
	}else{
		$(".comChoice").hide();
	}
});