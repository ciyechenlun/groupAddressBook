Headship = function(){
	return {
		
		//打开增加岗位窗口
		addHeadship : function(){
			Header.winShow('window_headship');
			$('#toggleDelEdite').text('新增岗位');
			$("#headshipId").val("");
			$("#headshipName").val("");
			$("#headshipLevel").val("");
			$("#description").val("");
		},
		
		//打开编辑岗位信息窗口并显示岗位信息
		updateHeadship : function(headshipId){
			$('#headshipWin').window({
			     title: "编辑职位",
			     width: 680,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 300,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/pheadship/toEditHeadship.htm?headshipId="+headshipId,
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
			$('#headshipWin').window('center');
		},
		search:function(){
			var key = $('#headship_name').val();
			window.location.href="/pc/pheadship/main.htm?key="+key;
		},
		onkeydown:function(event){
			 var keyCode = event.keyCode?event.keyCode:event.which?event.which:event.charCode;
			 if (keyCode ==13){
				 var key = $('#headship_name').val();
					window.location.href="/pc/pheadship/main.htm?key="+key;
			 }
		},
		//保存信息
		saveHeadship : function(){
			var headshipName = $("#headshipName").val();
			var headshipLevel = $("#headshipLevel").val();
			if(headshipName == null || headshipName == ""){
				$.messager.alert('提示','岗位名字不能为空!','info');
				return;
			}
			if(headshipLevel == null || headshipLevel == ""){
				$.messager.alert('提示','岗位级别不能为空!','info');
				return;
			}
			$('#hs_form').form('submit',{
				url:"/pc/pheadship/saveHeadship.htm",
				onSubmit: function(){
					return $('#hs_form').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#hs_form').form('clear');
							Header.winHide('window_headship');
							window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
						});				
					}else if(data=='NAME'){
						$.messager.alert('提示','岗位名字重复','error');	
					}else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});
		},
		//添加职位弹出框
		toAddHeadship:function(){
			$('#headshipWin').window({
			     title: "添加职位",
			     width: 680,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 450,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/pheadship/toAddHeadship.htm",
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
			$('#headshipWin').window('center');
		},
		//单条删除岗位
		deleteHeadship : function(headshipId){
			$.messager.confirm('提示信息','确认是否删除该岗位信息?',function(result){ 		
				if (result){
					$.ajax({
						   type: "POST",
						   url: "/pc/pheadship/delete.htm",
						   data: {'headshipId' :headshipId},
						   success: function(msg){
							   if(msg == "SUCCESS"){
								   $.messager.alert('提示',"删除岗位成功",'info');
								   $("#toPage").val(1);
								   window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
							   }else if(msg=="USE"){
								   $.messager.alert('提示',"该职位被使用，无法删除",'info');
							   }else{
								   $.messager.alert('提示',"删除失败",'error');
							   }
						   }
						}); 
				}
			}); 
		},
		batchDelete:function(){
			$.messager.confirm('提示信息','确认是否删除勾选岗位?',function(result){ 		
				if (result){
					var flag =false;//是否有勾选
					var result = [];//结果
					var recordCount=0;
				 	$('input[name="checkbox"]:checked').each(function(i){
				 		       flag =true;
				 		      recordCount++;
							   var headshipId = $(this).val();
							   $.ajax({
								   type: "POST",
								   async:false,
								   url: "/pc/pheadship/delete.htm",
								   data: {'headshipId' :headshipId},
								   success: function(msg){
									   if(msg != "SUCCESS"){
										   result.push([i+1,msg]);
									   }
								   }
								});
						});
					 	if(flag){
					 		if(result.length == 0){
					 			$.messager.alert('提示',"删除成功",'info',function(){
							 		window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
								});
							}else{
								var message = "";
								for(var x=0;x<result.length;x++){
									if(result[x][1] == 'USE'){
										message += "第"+result[x][0]+"笔：删除失败，该职位被使用<br/>";
									}else{
										message +="第"+result[x][0]+"笔：删除失败：未知原因<br/>";
									}
								}
								if(recordCount>result.length){
									message += "其他删除成功";
								}
								$.messager.alert('提示',message,'info',function(){
									window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
								});
							}
				 		}else{
				 			$.messager.alert("提示",'没有勾选项','info');
				 		}
					}
			}); 
		}
	};
}();

$(function(){
	
});