EditPublicRoad=function(){
	return{
		//跳转至第二步设置发送对象
		toStepTwo:function(){
			var publicName = $("#publicName").val();
			if(publicName.trim()=="输入公告名称，不超过8个字"){
				publicName = "";
			}
			if(publicName==null||publicName==""){
				 $.messager.alert('提示','公告名称不能为空!','info');
				 return;
			}
			var img = $("#picture").val();
			var toRange = $("#toRange").val();
			var toRangeNew = $(":checked").val();
			if(img){
				$('#comp_fm').form('submit',{
					url:"/pc/publicRoadCreate/uploadPhoto.htm",
					onSubmit: function(){
						return $('#comp_fm').form('validate');
					},
					success:function(data){
						if(data=="01"){
							$.messager.alert('提示',"图片添加失败",'error');
						} else if(data=="02"){
							$.messager.alert('提示',"导入格式错误",'error');
						}else{
							$("#picture").val(data);
							$("#step1").hide();
							$("#step2").show();
							$("#step3").hide();
							//window.location.href = "/pc/publicRoadCreate/toSetSendTarget?publicName="+publicName+"&picture="+picture+"&toRange="+toRange;
						}
					}
					
				});
			}else{
				$("#step1").hide();
				$("#step2").show();
				$("#step3").hide();
			}
			var companyId =  window.parent.document.getElementById("companyId").value;
			if(toRangeNew!=toRange){
				$("#targetMember").empty();
			}
			if(toRangeNew=='1'){
				EditPublicRoad.departmentTree(companyId);
				$(".othChoice").hide();
				$(".comChoice").show();
			}else if(toRangeNew=='2'){
				EditPublicRoad.userTree(companyId);
				$(".othChoice").hide();
				$(".comChoice").show();
			}else{
				$(".comChoice").hide();
				$(".othChoice").show();
			}
			
		},
		onBlur:function(){
			if($('#publicName').val()==""){
				$('#publicName').val("输入公告名称，不超过8个字");
			}
		},
		
		onfocus:function(){
			if($('#publicName').val()=="输入公告名称，不超过8个字"){
				$('#publicName').val("");
			}
		},
		//取消
		cancle:function(){
			$.messager.confirm('提示信息','是否取消编辑?',function(result){
				if(result){
					var manager = $("#manager").val();
					window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
				}
				
			});
		},
		//添加部门树
		departmentTree:function(companyId){
			$('#targetTree').tree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + companyId,
				checkbox:true,
				onLoadSuccess:function(node,param){
					var rootNode = $("#targetTree").tree('getRoot');
					if(rootNode!=null&&node!=null){
						if(rootNode.id!=node.id&&node.checked){
							var childrenNode = $("#targetTree").tree('getChildren', node.target);  
	                        if (childrenNode != null&&childrenNode!="") {  
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		$("#targetTree").tree('check', childrenNode[i].target);
	                        		   
	                            }  
	                        }
	                        $("#targetTree").tree("collapseAll",node.target);
//	                        node.target.nextSibling.style.display=none;
						}
							
					}
					
			    },
				onCheck:function(node,checked){
					if(checked){
						var childrenNode = $("#targetTree").tree('getChildren', node.target);  
                        if (childrenNode != null&&childrenNode!="") {  
                        	for (var i = 0; i < childrenNode.length; i++) {  
                        		 $("#targetTree").tree('check', childrenNode[i].target);    
                            }  
                        }else{
                        	if(!$('#targetTree').tree('isLeaf',node.target)){
	                        	$('#targetTree').tree("reload",node.target);
                        	}
                        }
                        EditPublicRoad.addMember(node.id, node.text);
					}else{
						if(!$('#targetTree').tree('isLeaf',node.target)){
							var childrenNode = $("#targetTree").tree('getChildren', node.target);  
	                        if (childrenNode != null) {
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		 $("#targetTree").tree('uncheck', childrenNode[i].target);    
	                            }  
	                           
	                        }  
						}   
						EditPublicRoad.delMember(node.id);
					}			
				}
			});
			
		},
		//添加人员树
		userTree:function(companyId){
			$('#targetTree').tree({
				url :'/pc/publicRoadCreate/getUserTree.htm?companyId=' + companyId,
				checkbox:true,
				onLoadSuccess:function(node,param){
					var rootNode = $("#targetTree").tree('getRoot');
					if(rootNode!=null&&node!=null){
						if(rootNode.id!=node.id&&node.checked){
							var childrenNode = $("#targetTree").tree('getChildren', node.target);  
	                        if (childrenNode != null&&childrenNode!="") {  
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		if($('#targetTree').tree('isLeaf',childrenNode[i].target)){
	                        			$("#targetTree").tree('check', childrenNode[i].target);
	                        		}    
	                            }  
	                        }
	                        $("#targetTree").tree("collapseAll",node.target);
//	                        node.target.nextSibling.style.display=none;
						}
							
					}
					
			    },
				onCheck:function(node,checked){
					if(checked){
						var childrenNode = $("#targetTree").tree('getChildren', node.target);  
                        if (childrenNode != null&&childrenNode!="") {  
                        	for (var i = 0; i < childrenNode.length; i++) {  
                        		 $("#targetTree").tree('check', childrenNode[i].target);    
                            }  
                        }else{
                        	if(!$('#targetTree').tree('isLeaf',node.target)){
	                        	$('#targetTree').tree("expand",node.target);
                        	}
                        }
					if($('#targetTree').tree('isLeaf',node.target)){
						EditPublicRoad.addMember(node.id, node.text);
					} 
					
				}else{
					if(!$('#targetTree').tree('isLeaf',node.target)){
						var childrenNode = $("#targetTree").tree('getChildren', node.target);  
                        if (childrenNode != null) {
                        	for (var i = 0; i < childrenNode.length; i++) {  
                        		 $("#targetTree").tree('uncheck', childrenNode[i].target);    
                            }  
                           
                        }  
					}  
					EditPublicRoad.delMember(node.id);
				}
				}
			});
		},
		
		//添加选定对象
		addMember:function(id,name){
			var d=document.getElementById("targetMember");
		       var a=d.getElementsByTagName("li");
		       for(var i=0;i<a.length;i++)
		       {
		           if(a[i].id==id){
		        	   return;
		           }
		       } 
			var txt = '<li id='+id+'><a href="#" onclick="EditPublicRoad.delMemberImg(\''+id+'\');"> <img src="/resources/images/icon_cancel.png"/></a>'+name+'</li>';
			$("#targetMember").append(txt);
			
		},
		
		//删除选定对象 checkBox删除
		delMember:function(id){
			var selector = "ul[id='targetMember'] li[id='"+id+"']";
			$(selector).remove();
		},
		//删除图标删除
		delMemberImg:function(id){
			var selectNode = $('#targetTree').tree('find',id);
			if(selectNode!=null){
				$('#targetTree').tree('uncheck',selectNode .target);
			}
		    var selector = "ul[id='targetMember'] li[id='"+id+"']";
			$(selector).remove();
		},
		
		toStepThree:function(){
			
			var toName = $("#toName").val();
			if(toName==null||toName.trim()==""){
				 $.messager.alert('提示','公告对象群组名称不能为空!','info');
				 return;
			}
			
			 var toRange = $(":checked").val();
		    if(toRange!=0){
		    	var target = "";
				var targetMember=document.getElementById("targetMember");
			    var targetInfo=targetMember.getElementsByTagName("li");
			    for(var i=0;i<targetInfo.length;i++){
			    	target+=targetInfo[i].id+",";
			    } 
		    	target = target.substring(0, target.length-1);
				if(target==null||target==""){
					 $.messager.alert('提示','公告发送对象不能为空!','info');
					 return;
				}
				$("#target").val(target);
		    }
			$("#step1").hide();
			$("#step2").hide();
			$("#step3").show();
			var companyId =  window.parent.document.getElementById("companyId").value;
			EditPublicRoad.managerTree(companyId);
		},
		
		//添加人员树
		managerTree:function(companyId){
			$('#managerTree').tree({
				url :'/pc/publicRoadCreate/getUserTree.htm?companyId=' + companyId,
				checkbox:true,
//				onlyLeafCheck:true,
				onLoadSuccess:function(node,param){
					var rootNode = $("#managerTree").tree('getRoot');
					if(rootNode!=null&&node!=null){
						if(rootNode.id!=node.id&&node.checked){
							var childrenNode = $("#managerTree").tree('getChildren', node.target);  
	                        if (childrenNode != null&&childrenNode!="") {  
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		 $("#managerTree").tree('check', childrenNode[i].target);    
	                            }  
	                        }
	                        $("#managerTree").tree("collapseAll",node.target);
//	                        node.target.nextSibling.style.display=none;
						}
							
					}
					
			    },
				onCheck:function(node,checked){
					if(checked){
						var childrenNode = $("#managerTree").tree('getChildren', node.target);  
                        if (childrenNode != null&&childrenNode!="") {  
                        	for (var i = 0; i < childrenNode.length; i++) {  
                        		 $("#managerTree").tree('check', childrenNode[i].target);    
                            }  
                        }else{
                        	if(!$('#managerTree').tree('isLeaf',node.target)){
	                        	$('#managerTree').tree("expand",node.target);
                        	}
                        }
					if($('#managerTree').tree('isLeaf',node.target)){
						EditPublicRoad.addManagerMember(node.id, node.text);
					} 
						
					}else{
						if(!$('#managerTree').tree('isLeaf',node.target)){
							var childrenNode = $("#managerTree").tree('getChildren', node.target);  
	                        if (childrenNode != null) {
	                        	for (var i = 0; i < childrenNode.length; i++) {  
	                        		 $("#managerTree").tree('uncheck', childrenNode[i].target);    
	                            }  
	                           
	                        }  
						}  
						EditPublicRoad.delManagerMember(node.id);
						
					}	
				}
			});
		},
		
		//添加选定对象
		addManagerMember:function(id,name){
			var d=document.getElementById("managerMember");
		    var a=d.getElementsByTagName("li");
		    for(var i=0;i<a.length;i++){
	           if(a[i].id==id){
	        	   return;
	           }
	       } 
			var txt = '<li id='+id+'><a href="#" onclick="EditPublicRoad.delManagerMemberImg(\''+id+'\');"> <img src="/resources/images/icon_cancel.png"/></a>'+name+'</li>';
			$("#managerMember").append(txt);
		},
		//删除选定对象
		delManagerMember:function(id){
			var selector = "ul[id='managerMember'] li[id='"+id+"']";
			$(selector).remove();
		},
		
		//删除图标删除
		delManagerMemberImg:function(id){
			var selectNode = $('#managerTree').tree('find',id);
			if(selectNode!=null){
				$('#managerTree').tree('uncheck',selectNode .target);
			}
		    var selector = "ul[id='managerMember'] li[id='"+id+"']";
			$(selector).remove();
			
		},
		
		complete:function(){
			var companyId =  window.parent.document.getElementById("companyId").value;
			var publicId = $("#publicId").val();
			var publicName = $("#publicName").val();
			var toRange = $(":checked").val();
			var picture = $("#picture").val();
			var toName = $("#toName").val();
			var manager = $("#manager").val();
			//获取发送对象id字符串
		    target = $("#target").val();
		  //获取发送对象id字符串
			var managerMember="";
			var d=document.getElementById("managerMember");
		    var a=d.getElementsByTagName("li");
		    for(var i=0;i<a.length;i++){
		    	managerMember+=a[i].id+",";
		    	} 
			 managerMember = managerMember.substring(0,managerMember.length-1);
			 
			 if(managerMember==""){
				 $.messager.alert('提示','公告管理员不能为空!','info');
				 return;
			}
			 
			$.ajax({
				type: "POST",
				url: "/pc/publicRoadCreate/addPublicRoad.htm",
				dataType: "JSON",
				data : {'publicId':publicId,'publicName':publicName,'toRange':toRange,
					'picture':picture,'toName':toName,'target':target,'managerMember':managerMember,'companyId':companyId},
				success: function(data){
					if(data){
						 $.messager.alert('提示',"修改成功",'info',function(){
							 window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
						 });
					}else{
						$.messager.alert('提示',"修改失败",'error',function(){
							window.location.href = "/pc/publicRoadManager/main.htm?manager="+manager;
						});
					}
				}
			});
		},
		
		
	};
}();


$(function(){
	$("#step2").hide();
	$("#step3").hide();
	var toRange = $("#toRange").val();
	var manager = $("#manager").val();
	if(manager!="1"){
		$(".cover_div").show();
		$(".cover_div2").show();
	}else{
		$(".cover_div").hide();
		$(".cover_div2").hide();
	}
	if(toRange=='1'){
		$("#selDepartment").attr("checked","checked");
	}else if(toRange=='2'){
		$("#selEmployee").attr("checked","checked");
	}else{
		$("#allCompany").attr("checked","checked");
	}
	
});