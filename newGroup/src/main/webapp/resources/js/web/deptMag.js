DeptMag = function(){
	return {
		getDeptByPId : function(companyId,parentDeptId,departmentLevel,pageNo,divId){
			$.ajax({
				type: "POST",
				async:false,
				url: "/pc/deptMag/getDepartment.htm?parentDeptId="+parentDeptId+"&companyId="+companyId+"&pageNo="+pageNo+"&departmentLevel="+departmentLevel,
				dataType: "JSON",
				success: function(data){
					var array = data.rows;
					if(pageNo==1){
						$('#'+divId).children().remove();
						if(divId == "1"){
							array_first = [];
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].department_id);
							}
						}else if(divId == "2"){
							array_second = [];
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].department_id);
							}
						}else if(divId == "3"){
							array_third = [];
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].department_id);
							}
						}else if(divId == "4"){
							array_fourth = [];
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].department_id);
							}
						}
					}else{
						$('#'+divId).children().remove('.pages');
						if(divId == "1"){
							for(var i=0;i<array.length;i++){
								array_first.push(array[i].department_id);
							}
						}else if(divId == "2"){
							for(var i=0;i<array.length;i++){
								array_second.push(array[i].department_id);
							}
						}else if(divId == "3"){
							for(var i=0;i<array.length;i++){
								array_third.push(array[i].department_id);
							}
						}else if(divId == "4"){
							for(var i=0;i<array.length;i++){
								array_fourth.push(array[i].department_id);
							}
						}
					}
					
					
					var deptDiv ="";
					for(var i=0;i<array.length;i++){
						deptDiv +=' <div class="lname">'+
		                  '<div title="'+array[i].department_name+'" class="f_l lna">'+
		                    '<input type="checkbox" name="checkbox" value="'+array[i].department_id+'"/><a href=\'javascript:DeptMag.editDepartment("'+array[i].department_id+'")\'><img title="编辑" src="/resources/images/list_edit.png" /></a>'+
		                    '<a name="dept_a" href=\'javascript:DeptMag.getDeptByPId("'+ array[i].company_id +'","'+ array[i].department_id +'","'+ (Number(array[i].department_level)+1) +'","1","'+ (Number(array[i].department_level)+1) +'")\'>'+array[i].department_name+
		                    '</a>'+
		                  '</div>'+
		                  '<div class="f_r lclose"><a href=\'javascript:DeptMag.del("'+array[i].department_id+'")\'><img src="/resources/images/list_close.png" /></a></div>'+
		                '</div>';
					}
					if(array.length>=10){
						deptDiv +='<div style="cursor:pointer" onclick=\'javascript:DeptMag.getDeptByPId("'+ companyId +'","'+parentDeptId+'","'+departmentLevel+'","'+(Number(pageNo)+1)+'","'+divId+'")\' class="pages"></div>';
					}
					$('#'+divId).append(deptDiv);
					$("a[name=dept_a]").click(function(){
						$(this).parent().parent().parent().find(".lname").removeClass("dept-selected");
						$(this).parent().parent().addClass("dept-selected");
					});
					if(pageNo==1){
						if(divId == "1"){
							$("#1").dragsort("destroy");
							$('#1').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: false, dragEnd: DeptMag.dragCall});
						}else if(divId == "2"){
							$('#3').children().remove();
							$('#3').append('<div class="nodata">【请选择三级部门】</div>');
							$('#4').children().remove();
							$('#4').append('<div class="nodata">【请选择四级部门】</div>');
							$("#1").dragsort("destroy");
							$("#2").dragsort("destroy");
							$('#1,#2').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
						}else if(divId == "3"){
							$('#4').children().remove();
							$('#4').append('<div class="nodata">【请选择四级部门】</div>');
							$("#1").dragsort("destroy");
							$("#2").dragsort("destroy");
							$("#3").dragsort("destroy");
							$('#1,#2,#3').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
						}else if(divId == "4"){
							$("#1").dragsort("destroy");
							$("#2").dragsort("destroy");
							$("#3").dragsort("destroy");
							$("#4").dragsort("destroy");
							$('#1,#2,#3,#4').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
						}
					}
				}
			});
			
		},
		getDeptByPIdForManage : function(children,companyId,parentDeptId,departmentLevel,pageNo,divId){
			if(!children){
				DeptMag.getDeptByPId(companyId,parentDeptId,departmentLevel,pageNo,divId);
			}else{
				children=children.replace(/\*/g,"\"");
				var array = eval(children);
				if(pageNo==1){
					$('#'+divId).children().remove();
					if(divId == "1"){
						array_first = [];
						for(var i=0;i<array.length;i++){
							array_first.push(array[i].department_id);
						}
					}else if(divId == "2"){
						array_second = [];
						for(var i=0;i<array.length;i++){
							array_second.push(array[i].department_id);
						}
					}else if(divId == "3"){
						array_third = [];
						for(var i=0;i<array.length;i++){
							array_third.push(array[i].department_id);
						}
					}else if(divId == "4"){
						array_fourth = [];
						for(var i=0;i<array.length;i++){
							array_fourth.push(array[i].department_id);
						}
					}
				}else{
					$('#'+divId).children().remove('.pages');
					if(divId == "1"){
						for(var i=0;i<array.length;i++){
							array_first.push(array[i].department_id);
						}
					}else if(divId == "2"){
						for(var i=0;i<array.length;i++){
							array_second.push(array[i].department_id);
						}
					}else if(divId == "3"){
						for(var i=0;i<array.length;i++){
							array_third.push(array[i].department_id);
						}
					}else if(divId == "4"){
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
					deptDiv +=' <div class="lname">'+
	                  '<div title="'+array[i].text+'" class="f_l lna">'+
	                    '<input type="checkbox" name="checkbox" value="'+array[i].id+'"/><a href=\'javascript:DeptMag.editDepartment("'+array[i].id+'")\'><img title="编辑" src="/resources/images/list_edit.png" /></a>'+
	                    '<a name="dept_a" href=\'javascript:DeptMag.getDeptByPIdForManage("'+ children +'","'+ array[i].company_id +'","'+ array[i].id +'","'+ (Number(array[i].department_level)+1) +'","1","'+ (Number(array[i].department_level)+1) +'")\'>'+array[i].text+
	                    '</a>'+
	                  '</div>'+
	                  '<div class="f_r lclose"><a href=\'javascript:DeptMag.del("'+array[i].id+'")\'><img src="/resources/images/list_close.png" /></a></div>'+
	                '</div>';
				}
				$('#'+divId).append(deptDiv);
				$("a[name=dept_a]").click(function(){
					$(this).parent().parent().parent().find(".lname").removeClass("dept-selected");
					$(this).parent().parent().addClass("dept-selected");
				});
				if(pageNo==1){
					if(divId == "1"){
						$("#1").dragsort("destroy");
						$('#1').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages",scrollContainer:'.scrolls',  dragBetween: false, dragEnd: DeptMag.dragCall});
					}else if(divId == "2"){
						$('#3').children().remove();
						$('#3').append('<div class="nodata">【请选择三级部门】</div>');
						$('#4').children().remove();
						$('#4').append('<div class="nodata">【请选择四级部门】</div>');
						$("#1").dragsort("destroy");
						$("#2").dragsort("destroy");
						$('#1,#2').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
					}else if(divId == "3"){
						$('#4').children().remove();
						$('#4').append('<div class="nodata">【请选择四级部门】</div>');
						$("#1").dragsort("destroy");
						$("#2").dragsort("destroy");
						$("#3").dragsort("destroy");
						$('#1,#2,#3').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
					}else if(divId == "4"){
						$("#1").dragsort("destroy");
						$("#2").dragsort("destroy");
						$("#3").dragsort("destroy");
						$("#4").dragsort("destroy");
						$('#1,#2,#3,#4').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: true, dragEnd: DeptMag.dragCall});
					}
				}
			}
		},
		dragCall :function () {
	 		/*var divId = $(this).parent().attr('id');
	 		if(divId =='1'){
	 			array_first.push($(this).find('input').val());
	 		}else if(divId =='2'){
	 			array_second.push($(this).find('input').val());
	 		}else if(divId =='3'){
	 			array_third.push($(this).find('input').val());
	 		}else if(divId =='4'){
	 			array_fourth.push($(this).find('input').val());
	 		}*/
	 		
			/*var data = $("#first div").map(function() { return $(this).children('input').val(); }).get();
			$("input[name=list1SortOrder]").val(data.join("-"));
			alert($("input[name=list1SortOrder]").val());*/
		},
		saveOrder:function(){
			$('#bodyDv').showLoading();
			var lastOrder1=$("#1 div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder2=$("#2 div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder3=$("#3 div").map(function() { return $(this).children('input').val(); }).get();
			var lastOrder4=$("#4 div").map(function() { return $(this).children('input').val(); }).get();
			var firstDeptId = $("#1").find('.dept-selected input').val();
			var secondDeptId = $("#2").find('.dept-selected input').val();
			var thirdDeptId = $("#3").find('.dept-selected input').val();
			var fourthDeptId = $("#4").find('.dept-selected input').val();
			$.ajax({
				type: "POST",
				contentType :'application/json', 
				url: "/pc/deptMag/saveOrder.htm",
				data: JSON.stringify({'firstMove' :array_first,'secondMove' :array_second,
					'thirdMove' :array_third,'fourthMove' :array_fourth,
					'lastOrder1' :lastOrder1,'lastOrder2' :lastOrder2,
					'lastOrder3' :lastOrder3,'lastOrder4' :lastOrder4,
					'firstDeptId' :firstDeptId,'secondDeptId' :secondDeptId,
					'thirdDeptId' :thirdDeptId,'fourthDeptId' :fourthDeptId
					}),
				success: function(data){
					$('#bodyDv').hideLoading();
					if(data=="SUCCESS"){
						$.messager.alert('提示',"保存成功",'info',function(){
							DeptMag.refreshMain();
						});
					}else{
						$.messager.alert('提示',"保存失败",'info');
					}
				}});
		},
		refreshMain:function(){
			var firstDeptId = $("#1").find('.dept-selected input').val();
			var secondDeptId = $("#2").find('.dept-selected input').val();
			var thirdDeptId = $("#3").find('.dept-selected input').val();
			DeptMag.getDeptByPId(companyId,'0','1',1,'1');
			if(firstDeptId){
				$('#1').find('input').filter("[value='"+firstDeptId+"']").parent().parent().addClass("dept-selected");
				DeptMag.getDeptByPId(companyId,firstDeptId,'2',1,'2');
			}
			if(secondDeptId){
				$('#2').find('input').filter("[value='"+secondDeptId+"']").parent().parent().addClass("dept-selected");
				DeptMag.getDeptByPId(companyId,secondDeptId,'3',1,'3');
			}
			if(thirdDeptId){
				$('#3').find('input').filter("[value='"+thirdDeptId+"']").parent().parent().addClass("dept-selected");
				DeptMag.getDeptByPId(companyId,thirdDeptId,'4',1,'4');
			}
			
		},
		//修改联系人
		editDepartment : function(departmentId){
			var ok = DeptMag.checkRole(departmentId);
			if(!ok){
				$.messager.alert("提示",'权限不足','info');
				return;
			}
			$('#deptWin').window({
			     title: "修改部门",
			     width: 600,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 380,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href: "/pc/deptMag/toEditDept.htm?departmentId="+departmentId,
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
			$('#deptWin').window('center');
		},
		toAddDept:function(){
			$('#deptWin').window({
			     title: "添加部门",
			     width: 600,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 380,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:"/pc/deptMag/toAddDept.htm",
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
			$('#deptWin').window('center');
		},
		checkRole:function(departmentId){
			var ok=true;
			var manageFlag=$('#manageFlag').val();
			if(manageFlag=='3'){
				$.ajax({
					url : '/pc/deptMag/checkRole.htm',
					data : {
						'departmentId' :departmentId 
					},
					type : 'post',
					async:false,
					success : function(data) {
						if(!data){
							ok=false;
						}
					}
				});
			}
			return ok;
		},
		del : function(departmentId){
				var ok = DeptMag.checkRole(departmentId);
				if(!ok){
					$.messager.alert("提示",'权限不足','info');
					return;
				}
				$.messager.confirm("提示","删除部门会删除部门及子部门下所有用户，可能需要一点时间。确认删除?",function(result){
					if(result){
						$('#bodyDv').showLoading();
						$.ajax({
							url : '/pc/deptMag/del.htm',
							data : {
								'departmentId' :departmentId 
							},
							type : 'post',
							success : function(data) {
								$('#bodyDv').hideLoading();
								if (data == "SUCCESS") {
									$.messager.alert("提示",'删除成功','info',
											function(){
										//window.location.reload();
										DeptMag.refreshMain();
									});
								} else {
									$.messager.alert("提示",'删除失败','error');									
								}
							}
						});
					}else{
						return;
					}
				});

		},
		batchDelete : function(){
			var temp =true;
			$('input[name="checkbox"]:checked').each(function(){
				   var departmentId = $(this).val();
				   temp = DeptMag.checkRole(departmentId);
					if(!temp){
						return;
					}
			});
			if(!temp){
				$.messager.alert("提示",'无权删除非管理部门','info');
				return;
			}
			$.messager.confirm("提示","删除部门会删除部门及子部门下所有用户，可能需要一点时间。确认删除勾选部门?",function(result){
				if(result){
					$('#bodyDv').showLoading();
					var flag = false;//是否有勾选
					var message = true;//结果成功
					$('input[name="checkbox"]:checked').each(function(){
			 		       flag =true;
						   var departmentId = $(this).val();
						   $.ajax({
								url : '/pc/deptMag/del.htm',
								async:false,
								data : {
									'departmentId' :departmentId 
								},
								type : 'post',
								success : function(data) {
									if(data != "SUCCESS"){
										message = false;
									}
								}
							});
						   if(!message){
							   return false;
						   }
					});
					$('#bodyDv').hideLoading();
					if(flag){
						if(message){
						 	$.messager.alert('提示',"删除成功",'info',function(){
								//window.location.reload();
						 		DeptMag.refreshMain();
							});
						}else{
							$.messager.alert("提示",'删除失败','error');
						}
			 		}else{
			 			$.messager.alert("提示",'没有勾选项','info');
			 		}
					
					
				}else{
					return;
				}
			});
		}
	};
}();
var array_first = [];
var array_second = [];
var array_third = [];
var array_fourth = [];
$(function(){
	companyId =  window.parent.document.getElementById("companyId").value;
	$('#1').dragsort({ dragSelector: ".lname",dragSelectorExclude:".f_l,.f_r,.pages,.dept-selected",scrollContainer:".scrolls", dragBetween: false, dragEnd: DeptMag.dragCall});
	array_first=$("#1 div").map(function() { return $(this).children('input').val(); }).get();
	/*var type = window.parent.document.getElementById("companyType").value;
	if(type == "bnewleft01 bnewleft01a liGetSelected" || 
			type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
		DeptMag.initTree();
		DeptMag.intcombotree();
		DeptMag.intcombobox();
		$("#departmentName").val("");
		$("#departmentId").val("");
		$("#displayOrder").val("");
		$("#fax").val("");
		$("#departmentCombobox").combotree('setValue',"");
		DeptMag.disableButton();
	}else{
		$.messager.alert("提示",'个人通讯录无需维护部门','info',function(){
			window.location.href = "/index_right.htm";
		});
	}*/
	var manageFlag=$('#manageFlag').val();
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
					deptDiv +=' <div class="lname">'+
	                  '<div title="'+array[i].text+'" class="f_l lna">'+
	                    '<input type="checkbox" name="checkbox" value="'+array[i].id+'"/><a href=\'javascript:DeptMag.editDepartment("'+array[i].id+'")\'><img title="编辑" src="/resources/images/list_edit.png" /></a>'+
	                    '<a name="dept_a" href=\'javascript:DeptMag.getDeptByPIdForManage("'+children+'","'+ array[i].company_id +'","'+ array[i].id +'","'+ (Number(array[i].department_level)+1) +'","1","'+ (Number(array[i].department_level)+1) +'")\'>'+array[i].text+
	                    '</a>'+
	                  '</div>'+
	                  '<div class="f_r lclose"><a href=\'javascript:DeptMag.del("'+array[i].id+'")\'><img src="/resources/images/list_close.png" /></a></div>'+
	                '</div>';
				}
				$('#1').append(deptDiv);
			}
		});
	}
	$("a[name=dept_a]").click(function(){
		$(this).parent().parent().parent().find(".lname").removeClass("dept-selected");
		$(this).parent().parent().addClass("dept-selected");
	});
});