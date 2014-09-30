editDept = function(){
	return {
		intcombotree : function(){
			$('#departmentCombobox').combotree({
				url:'/pc/deptMag/treeTest.htm',
				editable:false,
				required:false,
				panelHeight:200,
				onClick :function(node){
					$("#deptLevel").val(node.attributes.deptLevel);
					$("#relativeOrder").combobox('setValue',1);
				},
				onLoadSuccess:function(){
					var parentDeptName = $('#parentDeptName').val();
					if(parentDeptName){
						$('#departmentCombobox').combotree('setText',parentDeptName);
					}
				}
			});
			
		},
		intcombobox : function(){
			$('#relativeOrder').combobox({
				editable:false,
				required:false,
				valueField:'id',
				textField:'text',
				panelHeight:200,
				onLoadSuccess:function(){
					$('#relativeOrder').combobox('setValue',$('#orderHidden').val());
				},
				onShowPanel:function(){
					var pNodeId = $('#departmentCombobox').combotree('getValue');
					if(pNodeId){
						$.ajax({
							type: "POST",
							contentType :'application/json', 
							url: '/pc/deptMag/treeTest.htm?id='+pNodeId+'&companyId='+companyId,
							success: function(data){
								var size = data.length;
								var relativeList = new Array();
								var departmentId = $("#departmentId").val();
								if(!departmentId){
									for(var i=1;i<=size+1;i++){
										relativeList.push({"id":i,"text":i+""});
									}
								}else{
									var lastParentId= $('#pdId').val();
									if(lastParentId == pNodeId){
										for(var i=1;i<=size;i++){
											relativeList.push({"id":i,"text":i+""});
										}
									}else{
										for(var i=1;i<=size+1;i++){
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
		
		save : function(){
			var name = $.trim($("#departmentName").val());
			//var displayOrder = $.trim($("#displayOrder").val());
			if(name == ""){
				$.messager.alert("提示",'请输入部门名称!','info');
				return;
			}
			
			var pNodeId = $('#departmentCombobox').combotree('getValue');
			if(!pNodeId){
				$.messager.alert("提示",'请选择上级部门!','info');
				return;
			}
			var deptlevel = $('#deptLevel').val();
			if(deptlevel == '4'){
				$.messager.alert("提示",'上级部门不能是五级部门!','info');
				return;
			}
			var departmentId = $("#departmentId").val();
			if(departmentId == pNodeId){
				$.messager.alert("提示",'不能移动到本部门下！','info');
				return;
			}
			var leftOrder = 0;//相对较小的displayorder
			var rightOrder = 0;//相对较大的displayorder
			$.ajax({
				type: "POST",
				contentType :'application/json', 
				url: '/pc/deptMag/treeTest.htm?id='+pNodeId+'&companyId='+companyId,
				success: function(data){
					var childrenList = data;
					var this_relativeOrder = Number($('#relativeOrder').combobox('getValue'));
					
					if(!departmentId){
						if(this_relativeOrder > childrenList.length){
							if(childrenList.length == 0){
								rightOrder = 200;
							}else{
								rightOrder = Number(childrenList[childrenList.length-1].attributes.displayOrder)+20;
							}
						}
						if(this_relativeOrder-1 == 0){
							leftOrder = 0;
						}
						for(var i=0;i<childrenList.length;i++){
							var relativeOrder = Number(childrenList[i].attributes.relativeOrder);
							var dOrder = Number(childrenList[i].attributes.displayOrder);
							if(relativeOrder==this_relativeOrder-1){
								leftOrder =dOrder;
								
							}else if(relativeOrder==this_relativeOrder){
								rightOrder =dOrder;
							}
						}
						if(rightOrder-leftOrder<=1){
							$.post("/pc/deptMag/updateDisplayOrder.htm?parentDepartmentId="+pNodeId+"&displayOrder="+Number(rightOrder));
							rightOrder=rightOrder+10;
						}
						var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
						$("#displayOrder").val(this_displayOrder);
					}else{
						
						//var tree = $('#departmentCombobox').combotree('tree');
						//var node = tree.tree('find',departmentId);
						var last_relativeOrder = $('#orderHidden').val();//修改之前的相对顺序
						var lastParentId= $('#pdId').val();
						/*var nodechildren = tree.tree('getChildren',node.target);
						var isReturn = false;
						$.each(nodechildren, function(i,item){ 
							if(pNodeId == item.id){
								isReturn = true;
							}
						  });
						if(isReturn){
							$.messager.alert("提示",'不能移动到本部门的子部门下！','info');
							return;
						}*/
						if(lastParentId == pNodeId){
							if(last_relativeOrder>this_relativeOrder){//相对顺序变小
								if(this_relativeOrder-1==0){
									leftOrder = 0;
								}
								for(var i=0;i<childrenList.length;i++){
									var relativeOrder = Number(childrenList[i].attributes.relativeOrder);
									var dOrder = Number(childrenList[i].attributes.displayOrder);
									if(relativeOrder==this_relativeOrder-1){
										leftOrder =dOrder;
									}else if(relativeOrder==this_relativeOrder){
										rightOrder =dOrder;
									}
									
								}
								if(rightOrder-leftOrder<=1){
									$.post("/pc/deptMag/updateDisplayOrder.htm?parentDepartmentId="+pNodeId+"&displayOrder="+Number(rightOrder));
									rightOrder=rightOrder+10;
								}
								var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
								$("#displayOrder").val(this_displayOrder);
							}else if(last_relativeOrder < this_relativeOrder){//相对顺序变大
								if(this_relativeOrder+1>childrenList.length){
									rightOrder = Number(childrenList[childrenList.length-1].attributes.displayOrder)+20;
								}
								for(var i=0;i<childrenList.length;i++){
									var relativeOrder = Number(childrenList[i].attributes.relativeOrder);
									var dOrder = Number(childrenList[i].attributes.displayOrder);
									if(relativeOrder==this_relativeOrder){
										leftOrder =dOrder;
										
									}else if(relativeOrder==this_relativeOrder+1){
										rightOrder =dOrder;
									}
								}
								if(rightOrder-leftOrder<=1){
									$.post("/pc/deptMag/updateDisplayOrder.htm?parentDepartmentId="+pNodeId+"&displayOrder="+Number(rightOrder));
									rightOrder=rightOrder+10;
								}
								var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
								$("#displayOrder").val(this_displayOrder);
								
							}else{//相对顺序不变
								//do nothing
							}
						}else{
							if(this_relativeOrder > childrenList.length){
								if(childrenList.length == 0){
									rightOrder = 200;
								}else{
									rightOrder = Number(childrenList[childrenList.length-1].attributes.displayOrder)+20;
								}
							}
							if(this_relativeOrder-1 == 0){
								leftOrder = 0;
							}
							for(var i=0;i<childrenList.length;i++){
								var relativeOrder = Number(childrenList[i].attributes.relativeOrder);
								var dOrder = Number(childrenList[i].attributes.displayOrder);
								if(relativeOrder==this_relativeOrder-1){
									leftOrder =dOrder;
									
								}else if(relativeOrder==this_relativeOrder){
									rightOrder =dOrder;
								}
							}
							if(rightOrder-leftOrder<=1){
								$.post("/pc/deptMag/updateDisplayOrder.htm?parentDepartmentId="+pNodeId+"&displayOrder="+Number(rightOrder));
								rightOrder=rightOrder+10;
							}
							var this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
							$("#displayOrder").val(this_displayOrder);
						}
				}
			
				
					
					/*if(displayOrder == ""){
						$.messager.alert("提示",'请输入显示顺序!','info');
						return;
					}*/
					$('#save_form').form('submit',{
						url:"/pc/deptMag/save.htm",
						onSubmit: function(){
							return $('#save_form').form('validate');
						},
						success:function(data){
							if(data=='SUCCESS'){
								$.messager.alert("提示",'保存成功!','info',
									function(){
										//window.location.href = "/pc/deptMag/main.htm";
									$('#save_form').form('clear');
									$('#deptWin').window('close');
									DeptMag.refreshMain();
								});
							}else if(data=='NAME'){
								$.messager.alert("提示",'部门名称重复!','error');
							}else {
								$.messager.alert("提示",'保存失败!','error');
							}
						}
					});
				}
			});
		}
	};
}();

$(function(){
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
	companyId =  window.parent.document.getElementById("companyId").value;
	editDept.intcombotree();
	editDept.intcombobox();
});