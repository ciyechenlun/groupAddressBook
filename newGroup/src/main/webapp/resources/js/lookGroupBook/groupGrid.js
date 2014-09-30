LookGroup = function(){
	return {
		//部门树
		initTree : function(){
			//部门下拉框
			$('#departmentCombox').combotree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + window.parent.parent.document.getElementById("companyId").value,
				editable:false,
				required:true,
				panelHeight:200,
				onClick : function(node){
					var departmentName = node.text;
					$("#departmentName2").val(departmentName);
					$("#relativeOrder").combobox('setValue',1);
				} 
			});
			//岗位下拉框
			$('#headshipCombox').combobox({
				url : '/pc/lookGroup/findHeadship.htm',
				valueField : "data_code",
				textField : "data_content",
				editable : false,
				panelHeight : 120,
				onSelect : function(record){
					//alert($('#headshipCombox').combobox("getText"));
					$("#headshipName2").val($('#headshipCombox').combobox("getText"));
				}
			});
			$('#relativeOrder').combobox({
				editable:false,
				required:true,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onShowPanel:function(){
					var pNodeId = $('#departmentCombox').combotree('getValue');
					if(pNodeId){
						
						$.ajax({
							type: "POST",
							async:false,
							url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val(),
							dataType: "JSON",
							success: function(data){
								var relativeList = new Array();
								var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
								var winTitle = $('#toggleDelEdite').text();
								if(winTitle!="编辑联系人"){//新增
									for(var i=1;i<=Number(data)+1;i++){
										relativeList.push({"id":i,"text":i+""});
									}
								}else{
									var Ids = userCompanyIdAndDepartmentId.split('|');
									var lastParentId = Ids[1];
									if(lastParentId == pNodeId){
										for(var i=1;i<=Number(data);i++){
											relativeList.push({"id":i,"text":i+""});
										}
									}else{
										for(var i=1;i<=Number(data)+1;i++){
											relativeList.push({"id":i,"text":i+""});
										}
									}
								}
								$("#relativeOrder").combobox("loadData",relativeList);
							}
						});
						
					}
				}
			});
				
		},
		
		//修改联系人
		editeUser : function(managerType){
			
			var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
			if(userCompanyIdAndDepartmentId == null || userCompanyIdAndDepartmentId == ""){
				$.messager.alert('提示','请选择单选按钮!','info');
				return;
			}
			var Ids = userCompanyIdAndDepartmentId.split('|');
			var userCompanyId = Ids[0];
			var current_departmentId = Ids[1];
			var departmentId = $("#departmentId").val();
			if(userCompanyId == null || userCompanyId == ""){
				$.messager.alert('提示','请选择人员!','info');
				return;
			}
			if(managerType =="3"){
				var sDepartmentId ="";
				var children =  parent.$('#department_look').tree('getChildren',parent.$('#department_look').tree('getRoot').target);
				if(children){
					sDepartmentId= children[0].id;
				}
				var osDepartmentId = "";
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/lookGroup/getUserDepartments.htm?userCompanyId="+userCompanyId,
					dataType: "JSON",
					success: function(data){
						osDepartmentId = data;
					}
				});
				if(sDepartmentId != osDepartmentId){
					$.messager.alert('提示','对不起,您无权进行此操作!','info');
					return;
				}
			}
			var type = window.parent.parent.document.getElementById("companyType").value;
			if(type == "bnewleft01 bnewleft01a liGetSelected" || 
					type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
				LookGroup.initTree();
				Header.winShow('window_lookGroup');
				$('#departmentName').attr('disabled',true);
				$('#headshipName').attr('disabled',true);
				$('#departmentName2').attr('disabled',false);
				$('#headshipName2').attr('disabled',false);
				$('#form_lookGroup').form('clear');
				$("#for_org_dept").show();
				$("#for_org_hs").show();
				$('#toggleDelEdite').text('编辑联系人');
				$.ajax({
					type: "POST",
					url: "/pc/lookGroup/getUserCompanyById.htm",
					data: {'userCompanyId' :userCompanyId,'current_departmentId':current_departmentId},
					dataType: "JSON",
					success: function(data){
						$('#userCompanyId').val(userCompanyId);
						$("#employeeName").val(data[0].employee_name);
						$("#userCompany").val(data[0].user_company);
						$("#departmentName2").val(data[0].department_name);
						$("#departmentCombox").combotree('setValue',data[0].department_id);
						$("#departmentCombox").combotree('setText',data[0].department_name);
						$("#headshipCombox").combobox('setValue',data[0].headship_id);
						$("#headshipName2").val(data[0].headship_name);
						$("#mobile").val(data[0].mobile);
						$("#mobileShort").val(data[0].mobile_short);
						$("#telephone2").val(data[0].telephone2);
						$("#telephone").val(data[0].telephone);
						$("#telShort2").val(data[0].tel_short);
						$("#weibo").val(data[0].weibo);
						$("#weixin").val(data[0].weixin);
						$("#school").val(data[0].school);
						$("#userMajor").val(data[0].user_major);
						$("#userGrade").val(data[0].user_grade);
						$("#userClass").val(data[0].user_class);
						$("#studentId").val(data[0].student_id);
						$("#email").val(data[0].email);
						$("#gridNumber").val(data[0].grid_number);
						$("#qq").val(data[0].qq);
						$("#nativePlace").val(data[0].native_place);
						$("#address").val(data[0].address);
						$("#homeAddress").val(data[0].home_address);
						$("#remark").val(data[0].remark);
						$("#mood").val(data[0].mood);
						$("#displayOrder").val(data[0].display_order);
						$('#relativeOrder').combobox('setValue',data[0].row);
						$('#relativeOrderHidden').val(data[0].row);
					}
				});
			}else{
				$.ajax({
					type: "POST",
					url: "/pc/userCompany/beforeAdd.htm",
					data: {'companyId' :window.parent.parent.document.getElementById("companyId").value},
					dataType: "JSON",
					success: function(data){
						if(data=="YES"){
							$('#userCompanyId').val(userCompanyId);
							$('#companyId').val(window.parent.parent.document.getElementById("companyId").value);
							Header.winShow('window_lookGroup');
							$('#departmentName2').attr('disabled',true);
							$('#headshipName2').attr('disabled',true);
							$('#departmentName').attr('disabled',false);
							$('#headshipName').attr('disabled',false);
							$('#form_lookGroup').form('clear');
							$("#for_not_org_dept").show();
							$("#for_not_org_hs").show();
							$("#for_not_org_uc").show();
							$('#toggleDelEdite').text('编辑联系人');
							$.ajax({
								type: "POST",
								url: "/pc/lookGroup/getUserCompanyById.htm",
								data: {'userCompanyId' :userCompanyId,'current_departmentId':departmentId},
								dataType: "JSON",
								success: function(data){
									$('#userCompanyId').val(userCompanyId);
									$("#employeeName").val(data[0].employee_name);
									$("#userCompany").val(data[0].user_company);
									$("#departmentName").val(data[0].department_name);
									$("#headshipName").val(data[0].headship_name);
									$("#mobile").val(data[0].mobile);
									$("#mobileShort").val(data[0].mobile_short);
									$("#telephone2").val(data[0].telephone2);
									$("#telephone").val(data[0].telephone);
									$("#telShort").val(data[0].tel_short);
									$("#weibo").val(data[0].weibo);
									$("#weixin").val(data[0].weixin);
									$("#school").val(data[0].school);
									$("#userMajor").val(data[0].user_major);
									$("#userGrade").val(data[0].user_grade);
									$("#userClass").val(data[0].user_class);
									$("#studentId").val(data[0].student_id);
									$("#email").val(data[0].email);
									$("#gridNumber").val(data[0].grid_number);
									$("#qq").val(data[0].qq);
									$("#nativePlace").val(data[0].native_place);
									$("#address").val(data[0].address);
									$("#homeAddress").val(data[0].home_address);
									$("#remark").val(data[0].remark);
									$("#mood").val(data[0].mood);
									$("#displayOrder").val(data[0].display_order);
								}
							});
						}else{
							$.messager.alert('提示','对不起,您无权进行此操作!','info');
						}
					}
				});
			}
		},
		
		//增加联系人
		addUser : function(){
			var type = window.parent.parent.document.getElementById("companyType").value;
			$("#displayOrder").val(9999);
			if(type == "bnewleft01 bnewleft01a liGetSelected" || 
					type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
				LookGroup.initTree();
				Header.winShow('window_lookGroup');
				$('#departmentName').attr('disabled',true);
				$('#headshipName').attr('disabled',true);
				$('#departmentName2').attr('disabled',false);
				$('#headshipName2').attr('disabled',false);
				$('#form_lookGroup').form('clear');
				$("#for_org_dept").show();
				$("#for_org_hs").show();
				$('#toggleDelEdite').text('请添加联系人');
			}else{
				$.ajax({
					type: "POST",
					url: "/pc/userCompany/beforeAdd.htm",
					data: {'companyId' :window.parent.parent.document.getElementById("companyId").value},
					dataType: "JSON",
					success: function(data){
						if(data=="YES"){
							Header.winShow('window_lookGroup');
							$('#departmentName2').attr('disabled',true);
							$('#headshipName2').attr('disabled',true);
							$('#departmentName').attr('disabled',false);
							$('#headshipName').attr('disabled',false);
							$('#form_lookGroup').form('clear');
							$("#for_not_org_dept").show();
							$("#for_not_org_hs").show();
							$("#for_not_org_uc").show();
							$('#toggleDelEdite').text('请添加联系人');
						}else{
							$.messager.alert('提示','对不起,您无权进行此操作!','info');
						}
					}
				});
			}
		},
		
		//管理员设定弹出
		managerMan:function(){
			var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
			if(userCompanyIdAndDepartmentId == null || userCompanyIdAndDepartmentId == ""){
				$.messager.alert('提示','请选择单选按钮!','info');
				return;
			}
			var Ids = userCompanyIdAndDepartmentId.split('|');
			var userCompanyId = Ids[0];
			var current_departmentId = Ids[1];
			if(userCompanyId == null || userCompanyId == ""){
				$.messager.alert('提示','请选择单选按钮!','info');
				return;
			}
			Header.winShow('window_setManager');
			
		},
		//管理员设定
		setManagerMan:function(){
			var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
			var Ids = userCompanyIdAndDepartmentId.split('|');
			var userCompanyId = Ids[0];
			var current_departmentId = Ids[1];
			var managerType = $("[name='managerset']:checked").val();//管理员类型
			$.ajax({
				type:"POST",
				url:"/pc/userCompany/manageEdit.htm?managerType="+managerType,
				data:{"userCompanyId" :userCompanyId,"departmentId":current_departmentId},
				success:function(msg)
				{
					if(msg == 'SUCCESS')
					{
						Header.winHide('window_setManager');
						//管理员配置成功
						window.location.reload();
					}
					else
					{
						$.messager.alert('提示','对不起,操作失败!','info');
					}
				}
			});
		},
		//删除联系人
		deleteUser : function(managerType){
			var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
			if(userCompanyIdAndDepartmentId == null || userCompanyIdAndDepartmentId == ""){
				$.messager.alert('提示','请选择单选按钮!','info');
				return;
			}
			var Ids = userCompanyIdAndDepartmentId.split('|');
			var userCompanyId = Ids[0];
			var current_departmentId = Ids[1];
			if(userCompanyId == null || userCompanyId == ""){
				$.messager.alert('提示','请选择单选按钮!','info');
				return;
			}
			if(managerType =="3"){
				var sDepartmentId ="";
				var children =  parent.$('#department_look').tree('getChildren',parent.$('#department_look').tree('getRoot').target);
				if(children){
					sDepartmentId= children[0].id;
				}
				var osDepartmentId = "";
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/lookGroup/getUserDepartments.htm?userCompanyId="+userCompanyId,
					dataType: "JSON",
					success: function(data){
						osDepartmentId = data;
					}
				});
				if(sDepartmentId != osDepartmentId){
					$.messager.alert('提示','对不起,您无权进行此操作!','info');
					return;
				}
			}
			$.ajax({
				type: "POST",
				url: "/pc/userCompany/beforeAdd.htm",
				data: {'companyId' :window.parent.parent.document.getElementById("companyId").value},
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
												window.location.reload();
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
		
		saveUser : function(treeId){
			var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
			var companyId = window.parent.parent.document.getElementById("companyId").value;
			$('#companyId').val(window.parent.parent.document.getElementById("companyId").value);
			$('#companyId2').val(window.parent.parent.document.getElementById("companyId").value);
			var empName = $("#employeeName").val();
			var mobileLong = $("#mobile").val();
			var departmentName = $("#departmentName2").val();
			var type = window.parent.parent.document.getElementById("companyType").value;
			//var displayOrder = $("#displayOrder").val();
			/*if(displayOrder == '' ||  displayOrder=='')
			{
				$.messager.alert('提示','排序不能为空','info');
				return;
			}*/
			if(type == "bnewleft01 bnewleft01a liGetSelected" || 
					type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
				if(departmentName == null || departmentName == ""){
					 $.messager.alert('提示','部门不能为空!','info');
					 return;
				}
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
					url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val(),
					dataType: "JSON",
					success: function(data){
						var userCompanyIdAndDepartmentId = $("[name='radioName']:checked").val();
						var winTitle = $('#toggleDelEdite').text();
						if(winTitle!="编辑联系人"){//新增
							if(this_relativeOrder > Number(data)){
								if(Number(data) == 0){
									leftOrder = 0;
									rightOrder = 200;
								}else{
									$.ajax({
										type: "POST",
										async:false,
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+Number(data),
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
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+(this_relativeOrder-1),
										dataType: "JSON",
										success: function(result){
											leftOrder = result.display_order;
										}
									});	
								}
								
								$.ajax({
									type: "POST",
									async:false,
									url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+this_relativeOrder,
									dataType: "JSON",
									success: function(result){
										rightOrder = result.display_order;
									}
								});	
							}
							if(rightOrder-leftOrder<=1){
								$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&displayOrder="+Number(rightOrder));
								rightOrder=rightOrder+10;
							}
							var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
							$("#displayOrder").val(this_displayOrder);
						}else{
							var Ids = userCompanyIdAndDepartmentId.split('|');
							var lastParentId = Ids[1];
							if(lastParentId == pNodeId){
								var last_relativeOrder = $('#relativeOrderHidden').val();
								if(last_relativeOrder>this_relativeOrder){//相对顺序变小
									if(this_relativeOrder-1==0){
										leftOrder = 0;
									}else{
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+(this_relativeOrder-1),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
									}
										
									$.ajax({
										type: "POST",
										async:false,
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+this_relativeOrder,
										dataType: "JSON",
										success: function(result){
											rightOrder = result.display_order;
										}
									});	
									if(rightOrder-leftOrder<=1){
										$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&displayOrder="+Number(rightOrder));
										rightOrder=rightOrder+10;
									}
									var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
									$("#displayOrder").val(this_displayOrder);
								}else if(last_relativeOrder < this_relativeOrder){//相对顺序变大
									if(this_relativeOrder+1>Number(data)){
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+Number(data),
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
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+this_relativeOrder,
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
										$.ajax({
											type: "POST",
											async:false,
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+(this_relativeOrder+1),
											dataType: "JSON",
											success: function(result){
												rightOrder = result.display_order;
											}
										});
									}
									if(rightOrder-leftOrder<=1){
										$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&displayOrder="+Number(rightOrder));
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
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+Number(data),
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
											url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+(this_relativeOrder-1),
											dataType: "JSON",
											success: function(result){
												leftOrder = result.display_order;
											}
										});	
									}
									
									$.ajax({
										type: "POST",
										async:false,
										url: "/pc/lookGroup/getUserByOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&relativeOrder="+this_relativeOrder,
										dataType: "JSON",
										success: function(result){
											rightOrder = result.display_order;
										}
									});	
								}
								if(rightOrder-leftOrder<=1){
									$.post("/pc/lookGroup/updateDisplayOrder.htm?departmentId="+pNodeId+"&companyId="+$('#companyId').val()+"&displayOrder="+Number(rightOrder));
									rightOrder=rightOrder+10;
								}
								var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
								$("#displayOrder").val(this_displayOrder);
							}
						}
					}
				});
			}
			$('#form_lookGroup').form('submit',{
				url:"/pc/userCompany/addOrUpdate.htm?userCompanyIdAndDepartmentId=" + userCompanyIdAndDepartmentId,
				onSubmit: function(){
					return $('#form_lookGroup').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#form_lookGroup').form('clear');
							Header.winHide('window_lookGroup');
							//mod by zhangjun 2013/11/21
							window.location.href = "/pc/lookGroup/main.htm?"+ $("#searchForm").serialize()+"&treeId="+treeId;					
						});				
					}
					else if(data=='SAME')
					{
						$.messager.alert('提示','保存失败，当前部门存在相同的用户','error')
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
		
		cancelEdit : function(){
			Header.winHide('window_lookGroup');
			$('#form_lookGroup').form('clear');
		}
		
		
		
	};
}();

$(function(){	
	var type = window.parent.parent.document.getElementById("companyType").value;
	if(type == "bnewleft01 bnewleft01a liGetSelected" || 
			type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
			LookGroup.initTree();
			
	}
});