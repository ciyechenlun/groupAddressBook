LookGroup = function(){
	return {
		//部门下拉框数据加载
		initDepartmentComboBox : function(){
			$('#firstDepartment').combobox({
				editable:true,
				url:'/pc/deptMag/load1LevelDepartment.htm?companyId='+ companyId,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					LookGroup.getEmployeeBydepartment(record.id,"first",1);
					$('#secondDepartment').combobox('setValue','--请选择--');
					$('#thirdDepartment').combobox('loadData','');
					$('#thirdDepartment').combobox('setValue','--请选择--');
					$('#fourthDepartment').combobox('loadData','');
					$('#fourthDepartment').combobox('setValue','--请选择--');
					//$('#second').children().remove();
					//$('#second').append('<div class="nodata">【请选择三级部门】</div>');
					$('#third').children().remove();
					$('#third').append('<div class="nodata">【请选择四级部门】</div>');
					$('#fourth').children().remove();
					$('#fourth').append('<div class="nodata">【请选择五级部门】</div>');
					$('#secondDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='
							+$(this).combobox('getValue'));
					LookGroup.getDeptByPId(companyId,record.id,2,1,"second");
				}
			});
			$('#secondDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					LookGroup.getEmployeeBydepartment(record.id,"second",1);
					$('#thirdDepartment').combobox('setValue','--请选择--');
					$('#fourthDepartment').combobox('loadData','');
					$('#fourthDepartment').combobox('setValue','--请选择--');
					//$('#third').children().remove();
					//$('#third').append('<div class="nodata">【请选择四级部门】</div>');
					$('#fourth').children().remove();
					$('#fourth').append('<div class="nodata">【请选择五级部门】</div>');
					$('#thirdDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='
							+$(this).combobox('getValue'));
					LookGroup.getDeptByPId(companyId,record.id,3,1,"third");
				}
			});
			$('#thirdDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					LookGroup.getEmployeeBydepartment(record.id,"third",1);
					$('#fourthDepartment').combobox('setValue','--请选择--');
					//$('#fourth').children().remove();
					//$('#fourth').append('<div class="nodata">【请选择五级部门】</div>');
					$('#fourthDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='
							+$(this).combobox('getValue'));
					LookGroup.getDeptByPId(companyId,record.id,4,1,"fourth");
				}
			});
			$('#fourthDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					LookGroup.getEmployeeBydepartment(record.id,"fourth",1);
				}
			});
				$('.combo-text').click(function(){$(this).select();});
		},
		//部门管理员下拉框数据加载
		initDepartmentComboBoxForManage : function(){
			$('#firstDepartment').combobox({
				editable:true,
				url:'/pc/lookGroup/manageDeptList.htm',
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					$('#secondDepartment').combobox('setValue','--请选择--');
					$('#thirdDepartment').combobox('loadData','');
					$('#thirdDepartment').combobox('setValue','--请选择--');
					$('#fourthDepartment').combobox('loadData','');
					$('#fourthDepartment').combobox('setValue','--请选择--');
					//$('#second').children().remove();
					//$('#second').append('<div class="nodata">【请选择三级部门】</div>');
					$('#third').children().remove();
					$('#third').append('<div class="nodata">【请选择四级部门】</div>');
					$('#fourth').children().remove();
					$('#fourth').append('<div class="nodata">【请选择五级部门】</div>');
					if(record.children){
						$('#first').empty();
						var children =JSON.stringify(record.children).replace( /\"/g,"*");
						$('#secondDepartment').combobox('loadData',record.children);
						LookGroup.getDeptByPIdForManage(children,companyId,record.id,2,1,"second");
					}else{
						LookGroup.getEmployeeBydepartment(record.id,"first",1);
						$('#secondDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
								+ companyId+'&id='
								+$(this).combobox('getValue'));
						LookGroup.getDeptByPId(companyId,record.id,2,1,"second");
					}
				}
			});
			$('#secondDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					$('#thirdDepartment').combobox('setValue','--请选择--');
					$('#fourthDepartment').combobox('loadData','');
					$('#fourthDepartment').combobox('setValue','--请选择--');
					//$('#third').children().remove();
					//$('#third').append('<div class="nodata">【请选择四级部门】</div>');
					$('#fourth').children().remove();
					$('#fourth').append('<div class="nodata">【请选择五级部门】</div>');
					if(record.children){
						$('#second').empty();
						var children =JSON.stringify(record.children).replace( /\"/g,"*");
						$('#thirdDepartment').combobox('loadData',record.children);
						LookGroup.getDeptByPIdForManage(children,companyId,record.id,3,1,"third");
					}else{
						LookGroup.getEmployeeBydepartment(record.id,"second",1);
						
						$('#thirdDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
								+ companyId+'&id='
								+$(this).combobox('getValue'));
						LookGroup.getDeptByPId(companyId,record.id,3,1,"third");
					}
				}
			});
			$('#thirdDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					$('#fourthDepartment').combobox('setValue','--请选择--');
					if(record.children){
						$('#third').empty();
						var children =JSON.stringify(record.children).replace( /\"/g,"*");
						$('#fourthDepartment').combobox('loadData',record.children);
						LookGroup.getDeptByPIdForManage(children,companyId,record.id,4,1,"fourth");
					}else{
						LookGroup.getEmployeeBydepartment(record.id,"third",1);
						$('#fourthDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
								+ companyId+'&id='
								+$(this).combobox('getValue'));
						LookGroup.getDeptByPId(companyId,record.id,4,1,"fourth");
					}
				}
			});
			$('#fourthDepartment').combobox({
				editable:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onSelect:function(record){
					LookGroup.getEmployeeBydepartment(record.id,"fourth",1);
				}
			});
				$('.combo-text').click(function(){$(this).select();});
		},
		getEmployeeBydepartment : function(departmentId,employeeDivId,pageNo){
			$.ajax({
				type: "POST",
				async:false,
				url: "/pc/lookGroup/loadEmployeeByDepartment.htm?departmentId="+departmentId+"&companyId="+companyId+"&pageNo="+pageNo,
				dataType: "JSON",
				success: function(data){
					var array = data.rows;
					if(pageNo==1){
						$('#'+employeeDivId).children().remove();
						if(employeeDivId == "first"){
							array_first = [];
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "second"){
							array_second = [];
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "third"){
							array_third = [];
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "fourth"){
							array_fourth = [];
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].user_company_id);
							}
						}
					}else{
						$('#'+employeeDivId).children().remove('.pages');
						if(employeeDivId == "first"){
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "second"){
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "third"){
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].user_company_id);
							}
						}else if(employeeDivId == "fourth"){
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].user_company_id);
							}
						}
					}
					var employeeDiv ="";
					for(var i=0;i<array.length;i++){
						employeeDiv +=' <div class="lname">';
		                 if(array[i].manage_flag =='1'){
		                	 employeeDiv +='<div class="f_l lna" style="color:red">';
		                 }else if(array[i].manage_flag =='3'){
		                	 employeeDiv +='<div class="f_l lna" style="color:blue">';
		                 }else{
		                	 employeeDiv +='<div class="f_l lna">';
		                 }
		                 var name = array[i].employee_name+"";
							if(name.length>9){
								name = name.slice(0,7)+'...';
							}
		                  employeeDiv +=  '<input type="checkbox" name="checkbox" value="'+array[i].user_company_id+'"/><span title="'+array[i].employee_name+'">'+name+'</span>'+
		                    '<a href=\'javascript:LookGroup.editUser("'+ array[i].user_company_id +'","'+array[i].department_id+'")\'><img src="/resources/images/list_edit.png" /></a>'+
		                  '</div>'+
		                  '<div class="f_r lclose"><a href=\'javascript:LookGroup.deleteUser("'+ array[i].user_company_id +'","'+array[i].department_id+'")\'><img src="/resources/images/list_close.png" /></a></div>'+
		                '</div>';
					}
					if(array.length>=10){
						employeeDiv +='<div style="cursor:pointer" onclick=\'javascript:LookGroup.getEmployeeBydepartment("'+ departmentId +'","'+employeeDivId+'","'+(Number(pageNo)+1)+'")\' class="pages"></div>';
					}
					$('#'+employeeDivId).append(employeeDiv);
					if(pageNo==1){
						if(employeeDivId == "first"){
							$("#first").dragsort("destroy");
							$('#first').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: false, dragEnd: LookGroup.dragCall});
						}else if(employeeDivId == "second"){
							$("#first").dragsort("destroy");
							$("#second").dragsort("destroy");
							if($.trim($("#first").html())==""){
								$('#second').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: true, dragEnd: LookGroup.dragCall});
							}else{
								$('#first,#second').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: true, dragEnd: LookGroup.dragCall});
							}
						}else if(employeeDivId == "third"){
							$("#first").dragsort("destroy");
							$("#second").dragsort("destroy");
							$("#third").dragsort("destroy");
							$('#first,#second,#third').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: true, dragEnd: LookGroup.dragCall});
						}else if(employeeDivId == "fourth"){
							$("#first").dragsort("destroy");
							$("#second").dragsort("destroy");
							$("#third").dragsort("destroy");
							$("#fourth").dragsort("destroy");
							$('#first,#second,#third,#fourth').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: true, dragEnd: LookGroup.dragCall});
						}
					}
				}
			});
		},
		getDeptByPId : function(companyId,parentDeptId,departmentLevel,pageNo,divId,flg){
			if(flg){
				if(divId=="second"){
					$('#secondDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='+parentDeptId);
				}else if(divId=="third"){
					$('#thirdDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='+parentDeptId);
				}else if(divId=="fourth"){
					$('#fourthDepartment').combobox('reload','/pc/deptMag/load1LevelDepartment.htm?companyId=' 
							+ companyId+'&id='+parentDeptId);
				}
			}
			var employeeDivId = "";
			if(divId == "second"){
				employeeDivId = "first";
			}else if(divId == "third"){
				employeeDivId = "second";
			}else if(divId == "fourth"){
				employeeDivId = "third";
			}
			if(employeeDivId !=""&&flg){
				LookGroup.getEmployeeBydepartment(parentDeptId,employeeDivId,1);
				$("#"+employeeDivId+"Department").combobox('setValue',parentDeptId);
			}
			if(divId==""){
				LookGroup.getEmployeeBydepartment(parentDeptId,"fourth",1);
				$("#fourthDepartment").combobox('setValue',parentDeptId);
			}
			if(divId&&divId!=""){
				var nextDivId = "";
				if(divId == "second"){
					nextDivId = "third";
				}else if(divId == "third"){
					nextDivId = "fourth";
				}else if(divId == "first"){
					nextDivId = "second";
				}
				
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/deptMag/getDepartment.htm?parentDeptId="+parentDeptId+"&companyId="+companyId+"&pageNo="+pageNo+"&departmentLevel="+departmentLevel,
					dataType: "JSON",
					success: function(data){
						var array = data.rows;
						if(pageNo==1){
							$('#'+divId).children().remove();
							if(divId == "first"){
								array_first = [];
								for(var i=0;i<array.length;i++){
									array_first.push(array[i].department_id);
								}
							}else if(divId == "second"){
								array_second = [];
								for(var i=0;i<array.length;i++){
									array_second.push(array[i].department_id);
								}
							}else if(divId == "third"){
								array_third = [];
								for(var i=0;i<array.length;i++){
									array_third.push(array[i].department_id);
								}
							}else if(divId == "fourth"){
								array_fourth = [];
								for(var i=0;i<array.length;i++){
									array_fourth.push(array[i].department_id);
								}
							}
						}else{
							$('#'+divId).children().remove('.pages');
							if(divId == "first"){
								for(var i=0;i<array.length;i++){
									array_first.push(array[i].department_id);
								}
							}else if(divId == "second"){
								for(var i=0;i<array.length;i++){
									array_second.push(array[i].department_id);
								}
							}else if(divId == "third"){
								for(var i=0;i<array.length;i++){
									array_third.push(array[i].department_id);
								}
							}else if(divId == "fourth"){
								for(var i=0;i<array.length;i++){
									array_fourth.push(array[i].department_id);
								}
							}
						}
						
						
						var deptDiv ="";
						for(var i=0;i<array.length;i++){
							var name = array[i].department_name+"";
							if(name.length>12){
								name = name.slice(0,10)+'...';
							}
							deptDiv +=
			                  '<div title="'+array[i].department_name+'">'+
			                    '<a name="dept_a" href=\'javascript:LookGroup.getDeptByPId("'+ array[i].company_id +'","'+ array[i].department_id +'","'+ (Number(array[i].department_level)+1) +'","1","'+ nextDivId+'",true)\' class="lpart">'+
			                    name+
			                    '</a>'+
			                  '</div>';
						}
						if(array.length>=10){
							deptDiv +='<div style="cursor:pointer" onclick=\'javascript:LookGroup.getDeptByPId("'+ companyId +'","'+parentDeptId+'","'+departmentLevel+'","'+(Number(pageNo)+1)+'","'+divId+'")\' class="pages"></div>';
						}
						$('#'+divId).append(deptDiv);
					}
				});
			}
			
		},
		dragCall :function () {
	 		/*var divId = $(this).parent().attr('id');
	 		if(divId =='first'){
	 			array_first.push($(this).find('input').val());
	 		}else if(divId =='second'){
	 			array_second.push($(this).find('input').val());
	 		}else if(divId =='third'){
	 			array_third.push($(this).find('input').val());
	 		}else if(divId =='fourth'){
	 			array_fourth.push($(this).find('input').val());
	 		}*/
	 		
			/*var data = $("#first div").map(function() { return $(this).children('input').val(); }).get();
			$("input[name=list1SortOrder]").val(data.join("-"));
			alert($("input[name=list1SortOrder]").val());*/
		},
		saveOrder:function(){
			$('#bookMask').showLoading();
			var lastOrder1=$("#first div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder2=$("#second div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder3=$("#third div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder4=$("#fourth div").map(function() { return $(this).children('input').val(); }).get();
			var firstDept = $('#firstDepartment').combobox('getValue');
			var secondDept = $('#secondDepartment').combobox('getValue');
			var thirdDept = $('#thirdDepartment').combobox('getValue');
			var fourthDept = $('#fourthDepartment').combobox('getValue');
			
			$.ajax({
				type: "POST",
				contentType :'application/json', 
				url: "/pc/userCompany/saveOrder.htm",
				data: JSON.stringify({'firstMove' :array_first,'secondMove' :array_second,
					'thirdMove' :array_third,'fourthMove' :array_fourth,
					'lastOrder1' :lastOrder1,'lastOrder2' :lastOrder2,
					'lastOrder3' :lastOrder3,'lastOrder4' :lastOrder4,
					'firstDept':firstDept,'secondDept':secondDept,
					'thirdDept':thirdDept,'fourthDept':fourthDept
					}),
				success: function(data){
					$('#bookMask').hideLoading();
					if(data=="SUCCESS"){
						$.messager.alert('提示',"保存成功",'info',function(){
							LookGroup.refreshMain();
						});
					}else{
						$.messager.alert('提示',"保存失败",'info');
					}
				}});
		},
		//修改联系人
		editUser : function(userCompanyId,current_departmentId){
			$('#addEmployee').window({
			     title: "修改成员",
			     width: 580,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 600,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/lookGroup/toEditEmployee.htm?userCompanyId="+userCompanyId+"&current_departmentId="+current_departmentId,
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
			//$('#addEmployee').window('center');
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
												//window.location.reload();
												LookGroup.refreshMain();
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
		refreshMain:function(){
			var firstDepartmentId = $('#firstDepartment').combobox('getValue');
			var secondDepartmentId = $('#secondDepartment').combobox('getValue');
			var thirdDepartmentId = $('#thirdDepartment').combobox('getValue');
			var fourthtDepartmentId = $('#fourthDepartment').combobox('getValue');
			if(firstDepartmentId && firstDepartmentId !='--请选择--'&&$.trim($("#first").html())!=""){
				LookGroup.getEmployeeBydepartment(firstDepartmentId,"first",1);
			}
			if(secondDepartmentId && secondDepartmentId !='--请选择--'&&$.trim($("#second").html())!=""){
				LookGroup.getEmployeeBydepartment(secondDepartmentId,"second",1);
			}
			if(thirdDepartmentId && thirdDepartmentId !='--请选择--'&&$.trim($("#third").html())!=""){
				LookGroup.getEmployeeBydepartment(thirdDepartmentId,"third",1);
			}
			if(fourthtDepartmentId && fourthtDepartmentId !='--请选择--'&&$.trim($("#fourth").html())!=""){
				LookGroup.getEmployeeBydepartment(fourthtDepartmentId,"fourth",1);
			}
			
		},
		toAddEmployee : function(){
			 $('#addEmployee').window({
			     title: "添加成员",
			     width: 580,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 600,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/lookGroup/toAddEmployee.htm",
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
			 //$('#addEmployee').window('center');
		},
		toBatchAddEmp:function(){
			$('#addEmployee').window({
			     title: "批量添加成员",
			     width: 680,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 500,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/lookGroup/toBatchAddEmp.htm",
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
			 //$('#addEmployee').window('center');
		},
		//删除联系人
		batchDelete : function(){
			$.ajax({
				type: "POST",
				url: "/pc/userCompany/beforeAdd.htm",
				data: {'companyId' :companyId},
				dataType: "JSON",
				success: function(data){
					if(data=="YES"){
						$.messager.confirm('提示信息','确认是否删除勾选成员?',function(r){ 		
							if (r){
								var flag =false;//是否有勾选
								var message = true;//结果成功
							 	$('input[name="checkbox"]:checked').each(function(){
							 		       flag =true;
										   var userCompanyId = $(this).val();
										   var departmentId = "";
										   var deptDivId = $(this).parent().parent().parent().attr("id");
										   if(deptDivId == 'first'){
											   departmentId = $('#firstDepartment').combobox('getValue');
										   }else if(deptDivId == 'second'){
											   departmentId = $('#secondDepartment').combobox('getValue');
										   }else if(deptDivId == 'third'){
											   departmentId = $('#thirdDepartment').combobox('getValue');
										   }else if(deptDivId == 'fourth'){
											   departmentId = $('#fourthDepartment').combobox('getValue');
										   }
										   $.ajax({
												type: "POST",
												async:false,
												url: "/pc/userCompany/delete.htm",
												data: {'userCompanyId' :userCompanyId,'departmentId':departmentId},
												success: function(msg){
													if(msg != "SUCCESS"){
														message = false;
													}
												}
											});
										   if(!message){
											   return false;
										   }
									});
							 	if(flag){
									if(message){
									 	$.messager.alert('提示',"删除成功",'info',function(){
									 		LookGroup.refreshMain();
										});
									}else{
										$.messager.alert("提示",'删除失败','error');
									}
						 		}else{
						 			$.messager.alert("提示",'没有勾选项','info');
						 		}
								
								}
							}); 
					}
					else
					{
						$.messager.alert('提示','对不起,您无权进行此操作!','info');
					}
				}});
		},
		clearGroup : function(){
			$.messager.confirm('提示信息','确认是否清空通讯录?',function(r){ 	
				if (r){ 
					$('#bookMask').showLoading();
					$.ajax({
						type: "POST",
						url: "/pc/clearGroup/clearByCompany.htm?companyId="+companyId,
						success: function(data){
							$('#bookMask').hideLoading();
							if(data == "SUCCESS"){
								$.messager.alert('提示',"通讯录已清空",'info',function(){
									window.location.reload();
								});
							}else{
								$.messager.alert('提示','清空失败,请稍后再试!','error');
							}
						}
					}); 
				}
			});
		},
		getDeptByPIdForManage : function(children,companyId,parentDeptId,departmentLevel,pageNo,divId,flg){
			if(!children){
				LookGroup.getDeptByPId(companyId,parentDeptId,departmentLevel,pageNo,divId,flg);
			}else{
				children=children.replace(/\*/g,"\"");
				var array = eval(children);
				if(flg){
					if(divId=="second"){
						$('#secondDepartment').combobox('loadData',array);
					}else if(divId=="third"){
						$('#thirdDepartment').combobox('loadData',array);
					}else if(divId=="fourth"){
						$('#fourthDepartment').combobox('loadData',array);
					}
				}
				if(divId&&divId!=""){
					var nextDivId = "";
					if(divId == "second"){
						nextDivId = "third";
					}else if(divId == "third"){
						nextDivId = "fourth";
					}else if(divId == "first"){
						nextDivId = "second";
					}
					if(pageNo==1){
						$('#'+divId).children().remove();
						if(divId == "first"){
							array_first = [];
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].department_id);
							}
						}else if(divId == "second"){
							array_second = [];
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].department_id);
							}
						}else if(divId == "third"){
							array_third = [];
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].department_id);
							}
						}else if(divId == "fourth"){
							array_fourth = [];
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].department_id);
							}
						}
					}else{
						$('#'+divId).children().remove('.pages');
						if(divId == "first"){
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].department_id);
							}
						}else if(divId == "second"){
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].department_id);
							}
						}else if(divId == "third"){
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].department_id);
							}
						}else if(divId == "fourth"){
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].department_id);
							}
						}
					}
					
					
					var deptDiv ="";
					for(var i=0;i<array.length;i++){
						var children ="";
						if(array[i].children){
							children =JSON.stringify(array[i].children).replace( /\"/g,"*");
						}else{
							children ="";
						}
						var name = array[i].text+"";
						if(name.length>12){
							name = name.slice(0,10)+'...';
						}
						deptDiv +=
		                  '<div title="'+array[i].text+'">'+
		                    '<a name="dept_a" href=\'javascript:LookGroup.getDeptByPIdForManage("'+ children +'","'+ array[i].company_id +'","'+ array[i].id +'","'+ (Number(array[i].department_level)+1) +'","1","'+ nextDivId+'",true)\' class="lpart">'+
		                    name+
		                    '</a>'+
		                  '</div>';
					}
					$('#'+divId).append(deptDiv);
				}
			}
		}
		
	};
}();
var array_first = [];
var array_second = [];
var array_third = [];
var array_fourth = [];
$(function(){	
	companyId =  window.parent.document.getElementById("companyId").value;
	var manageFlag=$('#manageFlag').val();
	if(manageFlag=='3'){
		LookGroup.initDepartmentComboBoxForManage();
	}else{
		LookGroup.initDepartmentComboBox();
	}
	if(manageFlag=='3'){
		$.ajax({
			type: "POST",
			url: "/pc/lookGroup/manageDeptList.htm",
			success: function(data){
				var array =data;
				var deptDiv ="";
				for(var i=0;i<array.length;i++){
					if(array[i].children){
						children =JSON.stringify(array[i].children).replace( /\"/g,"*");
					}else{
						children ="";
					}
					var name = array[i].text+"";
					if(name.length>12){
						name = name.slice(0,10)+'...';
					}
					deptDiv +=
	                  '<div title="'+array[i].text+'">'+
	                    '<a name="dept_a" href=\'javascript:LookGroup.getDeptByPIdForManage("'+ children +'","'+ array[i].company_id +'","'+ array[i].id +'","'+ (Number(array[i].department_level)+1) +'","1","second",true)\' class="lpart">'+
	                    name+
	                    '</a>'+
	                  '</div>';
				}
				$('#first').append(deptDiv);
			}
		});
	}		
});