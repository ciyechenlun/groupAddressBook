UserManager = function(){
	return{
		//添加人员树
		userTree:function(companyId){
			$('#userTree').tree({
				url :'/pc/publicRoadCreate/getUserTree.htm?companyId=' + companyId,
				checkbox:true,
//				onlyLeafCheck:true,
				onLoadSuccess:function(node,param){
					var rootNode = $("#userTree").tree('getRoot');
					if(rootNode!=null&&node!=null){
						if(rootNode.id!=node.id&&node.checked){
							var childrenNode = $("#userTree").tree('getChildren', node.target);  
	                        if (childrenNode != null&&childrenNode!="") {  
	                        	for (var i = 0; i < childrenNode.length; i++) { 
	                        		if($('#userTree').tree('isLeaf',childrenNode[i].target)){
	                        			$("#userTree").tree('check', childrenNode[i].target);
	                        		}    
	                            }  
	                        }
//	                        $("#managerTree").tree("collapseAll",node.target);
//	                        node.target.nextSibling.style.display=none;
						}
							
					}
					
			    },
				onCheck:function(node,checked){
					if(checked){
						var childrenNode = $("#userTree").tree('getChildren', node.target);  
                        if (childrenNode != null&&childrenNode!="") {  
                        	for (var i = 0; i < childrenNode.length; i++) {  
                        		 $("#userTree").tree('check', childrenNode[i].target);    
                            }  
                        }else{
                        	if(!$('#userTree').tree('isLeaf',node.target)){
	                        	$('#userTree').tree("expand",node.target);
                        	}
                        }
					if($('#userTree').tree('isLeaf',node.target)){
						UserManager.addMember(node.id, node.text);
					} 
						
					}else{
						if(!$('#userTree').tree('isLeaf',node.target)){
							var childrenNode = $("#userTree").tree('getChildren', node.target);  
	                        if (childrenNode != null) {
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		 $("#userTree").tree('uncheck', childrenNode[i].target);    
	                            }  
	                           
	                        }  
						}  
						UserManager.delMember(node.id);
						
					}	
				}
			});
		},
		
		//添加选定对象
		addMember:function(id,name){
			var d=document.getElementById("forbiddenMember");
		    var a=d.getElementsByTagName("li");
		    for(var i=0;i<a.length;i++){
	           if(a[i].id==id){
	        	   return;
	           }
	       } 
			var txt = '<li id='+id+'><a href="#" onclick="UserManager.delManagerMemberImg(\''+id+'\');"> <img src="/resources/images/icon_cancel.png"/></a>'+name+'</li>';
			$("#forbiddenMember").append(txt);
		},
		//删除选定对象
		delMember:function(id){
			var selector = "li[id='"+id+"']";
			$(selector).remove();
		},
		
		//删除图标删除
		delManagerMemberImg:function(id){
			var selectNode = $('#userTree').tree('find',id);
			if(selectNode!=null){
				$('#userTree').tree('uncheck',selectNode .target);
			}
		    var selector = "ul[id='forbiddenMember'] li[id='"+id+"']";
			$(selector).remove();
			
		},
		
		saveInfo:function(){
			var forbiddenMember="";
			var d=document.getElementById("forbiddenMember");
		    var a=d.getElementsByTagName("li");
		    for(var i=0;i<a.length;i++){
		    	forbiddenMember+=a[i].id+",";
		    	} 
		    forbiddenMember = forbiddenMember.substring(0,forbiddenMember.length-1);
			$.ajax({
				type: "POST",
				url: "/pc/userManager/saveInfo.htm",
				dataType: "JSON",
				data : {'forbiddenMember':forbiddenMember},
				success: function(data){
					if(data){
						 $.messager.alert('提示',"保存成功",'info');
					}else{
						$.messager.alert('提示',"保存失败",'error');
					}
				}
			});
			
		}
		
		
	};
	
}();

$(function(){
	var companyId =  window.parent.document.getElementById("companyId").value;
	UserManager.userTree(companyId);
});