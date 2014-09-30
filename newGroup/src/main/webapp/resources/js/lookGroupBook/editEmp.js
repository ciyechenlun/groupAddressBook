editEmp = function(){
	return {
		//部门树
		initCombo : function(){
			//部门下拉框
			$('#departmentCombox').combotree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + companyId,
				editable:false,
				panelHeight:200,
				onClick : function(node){
					var departmentName = node.text;
					$("#departmentName2").val(departmentName);
					$("#relativeOrder").combobox('setValue',1);
				},
				onLoadSuccess:function(){
					$('#departmentCombox').combotree('setText',$('#departmentName2').val());
				}
			});
			//岗位下拉框
			$('#headshipCombox').combobox({
				url : '/pc/lookGroup/findHeadship.htm',
				valueField : "data_code",
				textField : "data_content",
				//editable : false,
				panelHeight : 120,
				onSelect : function(record){
					$("#headshipName2").val($('#headshipCombox').combobox("getText"));
				}
			});
			$('#relativeOrder').combobox({
				editable:false,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onShowPanel:function(){
					var pNodeId = $('#departmentCombox').combotree('getValue');
					if(pNodeId){
						$.ajax({
							type: "POST",
							async:false,
							url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+companyId,
							dataType: "JSON",
							success: function(data){
								var relativeList = new Array();
								var lastParentId = $('#lastDepartmentId').val();
								if(lastParentId == pNodeId){
									for(var i=1;i<=Number(data);i++){
										relativeList.push({"id":i,"text":i+""});
									}
								}else{
									for(var i=1;i<=Number(data)+1;i++){
										relativeList.push({"id":i,"text":i+""});
									}
								}
								$("#relativeOrder").combobox("loadData",relativeList);
							}
						});
						
					}
				}
			});
				
		},
		changeToImportant:function(){
			$('#important').attr('style','');
			$('#other').attr('style','display:none');
			$('#important_li').removeClass('openbg_out');
			$('#important_li').addClass('openbg_on');
			$('#important_span').removeClass('openbqb');
			$('#important_span').addClass('openbqa');
			$('#other_li').removeClass('openbg_on');
			$('#other_li').addClass('openbg_out');
			$('#other_span').removeClass('openbqa');
			$('#other_span').addClass('openbqb');
		},
		changeToOther:function(){
			$('#important').attr('style','display:none');
			$('#other').attr('style','');
			$('#other_li').removeClass('openbg_out');
			$('#other_li').addClass('openbg_on');
			$('#other_span').removeClass('openbqb');
			$('#other_span').addClass('openbqa');
			$('#important_li').removeClass('openbg_on');
			$('#important_li').addClass('openbg_out');
			$('#important_span').removeClass('openbqa');
			$('#important_span').addClass('openbqb');
		},
		saveUser : function(){
			var empName = $("#employeeName").val();
			var mobileLong = $("#mobile").val();
			var departmentName = $("#departmentName2").val();
			$("#headshipName2").val($('#headshipCombox').combobox("getText"));
			//var type = window.parent.parent.document.getElementById("companyType").value;
			//if(type == "bnewleft01 bnewleft01a liGetSelected" || 
			//		type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
				if(departmentName == null || departmentName == ""){
					 $.messager.alert('提示','部门不能为空!','info');
					 return;
				}
			//}
			if(empName == null || empName == ""){
				 $.messager.alert('提示','姓名不能为空!','info');
				 return;
			}
			if(mobileLong == null || mobileLong == ""){
				$.messager.alert('提示','手机长号不能为空!','info');
				return;
			}
			if(mobileLong.length>12){
				$.messager.alert('提示','手机长号不能超过12位','info');
				return;
			}
			var pNodeId = $('#departmentCombox').combotree('getValue');
			if(pNodeId){
				
				var leftOrder = 0;//相对较小的displayorder
				var rightOrder = 0;//相对较大的displayorder
				
				var this_relativeOrder = Number($('#relativeOrder').combobox('getValue'));
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+companyId,
					dataType: "JSON",
					success: function(data){
							var lastParentId = $('#lastDepartmentId').val();
							if(lastParentId == pNodeId){
								var last_relativeOrder = $('#relativeOrderHidden').val();
								if(last_relativeOrder>this_relativeOrder){//相对顺序变小
									if(this_relativeOrder-1==0){
										leftOrder = 0;
									}else{
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+(this_relativeOrder-1),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
									}
										
									$.ajax({
										type: "POST",
										async:false,
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+this_relativeOrder,
										dataType: "JSON",
										success: function(result){
											rightOrder = result.display_order;
										}
									});	
									if(rightOrder-leftOrder<=1){
										$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&displayOrder="+Number(rightOrder));
										rightOrder=rightOrder+10;
									}
									var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
									$("#displayOrder").val(this_displayOrder);
								}else if(last_relativeOrder < this_relativeOrder){//相对顺序变大
									if(this_relativeOrder+1>Number(data)){
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+Number(data),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
												rightOrder = Number(result.display_order)+20;
											}
										});
									}else{
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+this_relativeOrder,
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+(this_relativeOrder+1),
											dataType: "JSON",
											success: function(result){
												rightOrder = result.display_order;
											}
										});
									}
									if(rightOrder-leftOrder<=1){
										$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&displayOrder="+Number(rightOrder));
										rightOrder=rightOrder+10;
									}
									var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
									$("#displayOrder").val(this_displayOrder);
									
								}else{//相对顺序不变
									//do nothing
								}
								
							}else{
								if(this_relativeOrder > Number(data)){
									if(Number(data) == 0){
										leftOrder = 0;
										rightOrder = 200;
									}else{
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+Number(data),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
												rightOrder = Number(result.display_order)+20;
											}
										});
									}
								}else{
									if(this_relativeOrder-1 == 0){
										leftOrder = 0;
									}else{
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+(this_relativeOrder-1),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
									}
									
									$.ajax({
										type: "POST",
										async:false,
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+this_relativeOrder,
										dataType: "JSON",
										success: function(result){
											rightOrder = result.display_order;
										}
									});	
								}
								if(rightOrder-leftOrder<=1){
									$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&displayOrder="+Number(rightOrder));
									rightOrder=rightOrder+10;
								}
								var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
								$("#displayOrder").val(this_displayOrder);
							}
					}
				});
			}

			var userCompanyIdAndDepartmentId = $('#userCompanyId').val()+'|'+$('#lastDepartmentId').val();
			$('#form_lookGroup').form('submit',{
				url:"/pc/userCompany/addOrUpdate.htm?userCompanyIdAndDepartmentId=" + userCompanyIdAndDepartmentId,
				onSubmit: function(){
					return $('#form_lookGroup').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						var managerType ="";
						if($('#comp_man_div a').hasClass('comp_a_click')){
							managerType = "1";
						}else if($('#dept_man_div a').hasClass('dept_a_click')){
							managerType ="3";
						}else{
							managerType="";
						}
						if($('#manageFlag').val() !="3"){//登录人的级别不是部门管理员才有设置管理员的权限
							var managerDept = $("#manage_dept_id").val();
							$.ajax({
								type:"POST",
								url:"/pc/userCompany/manageEdit.htm?managerType="+managerType+"&managerDept="+managerDept,
								data:{"userCompanyId" :$('#userCompanyId').val()},
								success:function(msg)
								{
									if(msg == 'SUCCESS')
									{
										$.messager.alert('提示','保存成功','info',function(){
											$('#form_lookGroup').form('clear');
											$('#addEmployee').window('close');
											try{
												LookGroup.refreshMain();
											}catch(e){
												window.location.reload();
											}
										});	
									}
									else
									{
										$.messager.alert('提示','保存成功,管理员设置失败!','info');
									}
								}
							});
						}else{
							$.messager.alert('提示','保存成功','info',function(){
								$('#form_lookGroup').form('clear');
								$('#addEmployee').window('close');
								try{
									LookGroup.refreshMain();
								}catch(e){
									window.location.reload();
								}
							});	
						}
					}
					else if(data=='SAME')
					{
						$.messager.alert('提示','保存失败，当前部门存在相同的用户','error');
					}
					else if(data == '号码重复,请重新输入手机号码！')
					{
						$.messager.alert('提示','号码重复,请重新输入手机号码！');
					}
					else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});

		},
		addUser:function(){
			var empName = $("#employeeName").val();
			var mobileLong = $("#mobile").val();
			var departmentName = $("#departmentName2").val();
			$("#headshipName2").val($('#headshipCombox').combobox("getText"));
			var headshipName =$("#headshipName2").val();
			$('#companyId2').val(companyId);
			//var type = window.parent.parent.document.getElementById("companyType").value;
			//if(type == "bnewleft01 bnewleft01a liGetSelected" || 
			//		type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
				if(departmentName == null || departmentName == ""){
					 $.messager.alert('提示','部门不能为空!','info');
					 return;
				}
			//}
			if(headshipName == null || headshipName == ""){
				 $.messager.alert('提示','职位不能为空!','info');
				 return;
			}
			if(empName == null || empName == ""){
				 $.messager.alert('提示','姓名不能为空!','info');
				 return;
			}
			if(mobileLong == null || mobileLong == ""){
				$.messager.alert('提示','手机长号不能为空!','info');
				return;
			}
			//add by zhangjun 2013/11/20
			if(mobileLong.length>12){
				$.messager.alert('提示','手机长号不能超过12位','info');
				return;
			}
			//add by zhangjun 2013/11/20
			
			var pNodeId = $('#departmentCombox').combotree('getValue');
			if(pNodeId){
				
				var leftOrder = 0;//相对较小的displayorder
				var rightOrder = 0;//相对较大的displayorder
				
				var this_relativeOrder = Number($('#relativeOrder').combobox('getValue'));
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+companyId,
					dataType: "JSON",
					success: function(data){
						if(this_relativeOrder > Number(data)){
							if(Number(data) == 0){
								leftOrder = 0;
								rightOrder = 200;
							}else{
								$.ajax({
									type: "POST",
									async:false,
									url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+Number(data),
									dataType: "JSON",
									success: function(result){
										leftOrder = result.display_order;
										rightOrder = Number(result.display_order)+20;
									}
								});
							}
						}else{
							if(this_relativeOrder-1 == 0){
								leftOrder = 0;
							}else{
								$.ajax({
									type: "POST",
									async:false,
									url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+(this_relativeOrder-1),
									dataType: "JSON",
									success: function(result){
										leftOrder = result.display_order;
									}
								});	
							}
							
							$.ajax({
								type: "POST",
								async:false,
								url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&relativeOrder="+this_relativeOrder,
								dataType: "JSON",
								success: function(result){
									rightOrder = result.display_order;
								}
							});	
						}
						if(rightOrder-leftOrder<=1){
							$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+companyId+"&displayOrder="+Number(rightOrder));
							rightOrder=rightOrder+10;
						}
						var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
						$("#displayOrder").val(this_displayOrder);
					
					}
				});
			}
			$('#form_lookGroup').form('submit',{
				url:"/pc/userCompany/addOrUpdate.htm",
				onSubmit: function(){
					return $('#form_lookGroup').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#form_lookGroup').form('clear');
							$('#addEmployee').window('close');
							LookGroup.refreshMain();
						});				
					}
					else if(data=='SAME')
					{
						$.messager.alert('提示','保存失败，当前部门存在相同的用户','error');
					}
					else if(data == '号码重复,请重新输入手机号码！')
					{
						$.messager.alert('提示','号码重复,请重新输入手机号码！');
					}
					else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});
		
		},
		//删除联系人
		deleteUser : function(userCompanyId,current_departmentId){
			$.ajax({
				type: "POST",
				url: "/pc/userCompany/beforeAdd.htm",
				data: {'companyId' :companyId},
				dataType: "JSON",
				success: function(data){
					if(data=="YES"){
						$.messager.confirm('提示信息','确认是否删除该用户信息?',function(r){ 		
							if (r){ 								
								$.ajax({
										type: "POST",
										url: "/pc/userCompany/delete.htm",
										data: {'userCompanyId' :userCompanyId,'departmentId':current_departmentId},
										success: function(msg){
											$.messager.alert('提示',"删除用户成功",'info',function(){
												$('#form_lookGroup').form('clear');
												$('#addEmployee').window('close');
												try{
													LookGroup.refreshMain();
												}catch(e){
													window.location.reload();
												}
											});
										}
									}); 
								}
							}); 
					}
					else
					{
						$.messager.alert('提示','对不起,您无权进行此操作!','info');
					}
				}});
		},
		//管理员设定
		setManagerComp:function(){
			if($('#comp_man_div a').hasClass('comp_a_click')){
				$('#comp_man_div a').removeClass('comp_a_click');
			}else{
				$('#dept_man_div a').removeClass('dept_a_click');
				$('#comp_man_div a').addClass('comp_a_click');
			}
		},
		getTreeValue:function(){
				var manage_dept_id="";
			 	var checkList = $("#man_tree").tree("getChecked");
			 	for(var i=0;i<checkList.length;i++){
			 		var flag=false;
			 		for(var j=0;j<checkList.length;j++){
				 		if(checkList[j].id==checkList[i].attributes.pid){
				 			flag=true;
				 			break;
				 		}
				 	}
			 		if(flag){
			 			continue;
			 		}
			 		if(manage_dept_id!=""){
			 			manage_dept_id+=",";
					}
			 		manage_dept_id +=checkList[i].id;
			 	}
			 	$("#manage_dept_id").val(manage_dept_id);
			 	$('#setManager').dialog('close');
			 	if(checkList.length==0){
			 		$("#manage_dept_id").val("");
			 		$('#dept_man_div a').removeClass('dept_a_click');
			 	}else{
				 	$('#comp_man_div a').removeClass('comp_a_click');
					$('#dept_man_div a').addClass('dept_a_click');
			 	}
			 	
		},
		//管理员设定
		setManagerDept:function(employeeId,company_id){
			$('#setManager').dialog({
			     title: "部门管理员设置",
			     width: 300,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 300,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
		         onMove:function(left,top){
					   	if(top<0){
					   		$(this).window('move',{left:left,top:0});
					   	}
					   	if(left<0){
					   		$(this).window('move',{left:0,top:top});
					   	}
					   },
				 modal:true,
				  buttons:[{
						text:'确定',
						iconCls:'icon-ok',
						handler:function(){
							editEmp.getTreeValue();
						}
					},{
						text:'取消',
						iconCls:'icon-cancel',
						handler:function(){
							$('#setManager').dialog('close');
						}
					}],
					onOpen:function(){
						$('#man_tree').tree( {
							url:'/pc/deptMag/mTree.htm?employeeId='+employeeId+'&companyId='+companyId,
							disabled:true,
							animate:true,
							checkbox:true,
							cascadeCheck:false,
							expand:true,
							onLoadSuccess:function(){
								var manage_dept_id = $('#manage_dept_id').val();
								var modelist = manage_dept_id.split(",");
								  for(var i=0;i<modelist.length;i++){
								    	var node = $('#man_tree').tree('find',modelist[i]);
								    	if(node){
									 		$('#man_tree').tree('check',node.target);
									 		$('#man_tree').tree('expandTo',node.target);
									 	}
									 }
							}
						});
					}
			});
		}
		
	};
}();

$(function(){	
	companyId =  window.parent.document.getElementById("companyId").value;
	var manageFlag = $('#manageFlag').val();//登录人员的级别
	var manageType=$('#manage_flag').val();//管理员类型（1：企业3：部门）
	if(manageFlag != '3'){
		$('#comp_man_div').show();
		$('#dept_man_div').show();
		if(manageType=="1"){
			$('#comp_man_div a').addClass('comp_a_click');
		}else if(manageType=="3"){
			$('#dept_man_div a').addClass('dept_a_click');
		}else{
			//
		}
	}
	editEmp.initCombo();
			
			
});