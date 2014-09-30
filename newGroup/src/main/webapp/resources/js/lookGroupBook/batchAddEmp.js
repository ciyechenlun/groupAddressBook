batchAddEmp = function(){
	return {
		//部门树
		initCombo : function(){
			//部门下拉框
			$("[name='departmentId']").combotree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + companyId,
				editable:false,
				panelHeight:200,
				onClick : function(node){
					//$("#relativeOrder").combobox('setValue',1);
				}
			});
			//岗位下拉框
			$("[name='headshipId']").combobox({
				url : '/pc/lookGroup/findHeadship.htm',
				valueField : "data_code",
				textField : "data_content",
				//editable : false,
				panelHeight : 120,
				onSelect : function(record){
					$("#headshipName2").val($('#headshipCombox').combobox("getText"));
				}
			});
			$("[name='relativeOrder']").combobox({
				editable:false,
				valueField:'id',
				textField:'text',
				panelHeight:120,
				onShowPanel:function(){
					var pNodeId = $(this).parent().parent().find("[name='departmentId']").val();
					var index = $(this).parent().parent().index();
					if(pNodeId == "同上"){
						var trs = $("#batch_tab tr");
						for(var i=index-1;i>=1;i--){
							var departmentId = trs.eq(i).find("[name='departmentId']").val();
							if(departmentId !="同上"){
								pNodeId = departmentId;
								break;
							}
						}
					}
					var _this=this;
					if(pNodeId){
						$.ajax({
							type: "POST",
							async:false,
							url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+companyId,
							dataType: "JSON",
							success: function(data){
								/*var deptArray = $("#batch_tab tr").map(function() { return $(this).find("[name='departmentId']").val(); }).get();
								var number =0;
								for(var i=0;i<deptArray.length;i++){
									if(deptArray[i] == pNodeId || deptArray[i]=="同上"){
										number++;
									}else{
										break;
									}
								}*/
								var relativeList = new Array();
								for(var i=1;i<=Number(data)+index;i++){
									relativeList.push({"id":i,"text":i+""});
								}
								$(_this).combobox("loadData",relativeList);
							}
						});
						
					}
				}
			});
				
		},
		addTr:function(){
			var Con=document.getElementById("batch_tab");
			var row = Con.insertRow(Con.rows.length);
		    var cell = row.insertCell(0);
		    cell.innerHTML =  row.rowIndex.toString();
		    cell = row.insertCell(1);
		    cell.innerHTML = '<input name="employeeName" type="text" class="opand" value="" size="10" />';
		    cell = row.insertCell(2);
		    cell.innerHTML = '<input name="mobile" type="text" class="opand" value="" size="15" />';
		    cell = row.insertCell(3);
		    cell.innerHTML = '<input  name="departmentId" type="text" value="同上" class="opand" size="10" style="width:150px"/>';
		    cell = row.insertCell(4);
		    cell.innerHTML = '<input id="headshipCombox" name="headshipId" type="text" value="同上" class="opand" size="10" style="width:120px"/>'
		    				+'<input id="headshipName2"  name="headshipName" type="hidden"/>';
		    cell = row.insertCell(5);
		    cell.innerHTML = '<input  name="displayOrder" type="hidden"  />'+
            		'<input type="text" name="relativeOrder" value="1" class="opand" size="5" style="width:80px"/>';
		    $("#batch_tab tr").eq(row.rowIndex).find("[name='departmentId']").combotree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + companyId,
				editable:false,
				panelHeight:200,
				onClick : function(node){
					//$("#relativeOrder").combobox('setValue',1);
				}
			});
		  //岗位下拉框
		    $("#batch_tab tr").eq(row.rowIndex).find("[name='headshipId']").combobox({
				url : '/pc/lookGroup/findHeadship.htm',
				valueField : "data_code",
				textField : "data_content",
				//editable : false,
				panelHeight : 120,
				onSelect : function(record){
				}
			});
		    $("#batch_tab tr").eq(row.rowIndex).find("[name='relativeOrder']").combobox({
				editable:false,
				valueField:'id',
				textField:'text',
				panelHeight:120,
				onShowPanel:function(){
					var pNodeId = $(this).parent().parent().find("[name='departmentId']").val();
					var index = $(this).parent().parent().index();
					if(pNodeId == "同上"){
						var trs = $("#batch_tab tr");
						for(var i=index-1;i>=1;i--){
							var departmentId = trs.eq(i).find("[name='departmentId']").val();
							if(departmentId !="同上"){
								pNodeId = departmentId;
								break;
							}
						}
					}
					var _this=this;
					if(pNodeId){
						$.ajax({
							type: "POST",
							async:false,
							url: "/pc/lookGroup/getRelativeCount.htm?departmentId="+pNodeId+"&companyId="+companyId,
							dataType: "JSON",
							success: function(data){
								var relativeList = new Array();
								for(var i=1;i<=Number(data)+index;i++){
									relativeList.push({"id":i,"text":i+""});
								}
								$(_this).combobox("loadData",relativeList);
							}
						});
						
					}
				}
			});
		    
		},
		batchAddUser:function(){
			var trs = $("#batch_tab tr");
			var result = [];
			var flag =true;
			var recordCount=0;
			for(var i=1;i<trs.length;i++){
				var empName = trs.eq(i).find("[name='employeeName']").val();
				if(empName == null || empName == ""){
					 continue;
				}else{
					recordCount++;
					flag = false;
				}
				var mobileLong = trs.eq(i).find("[name='mobile']").val();
				var departmentId = trs.eq(i).find("[name='departmentId']").val();
				var headshipId = trs.eq(i).find("[name='headshipId']").val();
				trs.eq(i).find("[id='headshipName2']").val(trs.eq(i).find("[id='headshipCombox']").combobox("getText"));
				var headshipName =trs.eq(i).find("[id='headshipName2']").val();
				if(mobileLong == null || mobileLong == ""){
					$.messager.alert('提示','手机长号不能为空!','info');
					return;
				}
				if(mobileLong.length>12){
					$.messager.alert('提示','手机长号不能超过12位','info');
					return;
				}
				if(departmentId == null || departmentId == ""){
					 $.messager.alert('提示','部门不能为空!','info');
					 return;
				}
				if(headshipName == null || headshipName == ""){
					 $.messager.alert('提示','职位不能为空!','info');
					 return;
				}
				var lastdeptId = "";
				if(i>1){
					lastdeptId = trs.eq(i-1).find("[name='departmentId']").val();
				}
				if(departmentId=="同上" && lastdeptId !=""){
					departmentId = lastdeptId;
					trs.eq(i).find("[name='departmentId']").val(departmentId);
				}
				
			
				
				var lastHeadshipName = "";
				if(i>1){
					lastHeadshipName = trs.eq(i-1).find("[id='headshipName2']").val();
				}
				if(headshipName=="同上" && lastHeadshipName !=""){
					headshipName = lastHeadshipName;
					trs.eq(i).find("[id='headshipName2']").val(lastHeadshipName);
				}
				var pNodeId = departmentId;
				var this_displayOrder = 0;
				if(pNodeId){
					
					var leftOrder = 0;//相对较小的displayorder
					var rightOrder = 0;//相对较大的displayorder
					
					var this_relativeOrder = Number(trs.eq(i).find("[name='relativeOrder']").val());
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
							this_displayOrder = Number(leftOrder)+Math.round((rightOrder-leftOrder)/2);
							trs.eq(i).find("[name='displayOrder']").val(this_displayOrder);
						
						}
					});
				}
				$.ajax({
					type: "POST",
					async:false,
					url: "/pc/userCompany/addOrUpdate.htm",
					data:{"companyId":companyId,"employeeName":empName,"mobile":mobileLong,
						"departmentId":departmentId,"headshipName":headshipName,"displayOrder":this_displayOrder},
					dataType: "JSON",
					success: function(data){
						if(data!='SUCCESS'){
							result.push([i,data]);
						}
					}
				
				});
			}
			if(flag){
				$.messager.alert('提示','没有添加任何成员','info');
				return;
			}
			if(result.length == 0){
				$.messager.alert('提示','保存成功','info',function(){
					$('#addEmployee').window('close');
					//window.location.href="/pc/lookGroup/forward.htm?companyId="+companyId;
					LookGroup.refreshMain();
				});
			}else{
				var message = "";
				for(var x=0;x<result.length;x++){
					if(result[x][1] == 'SAME'){
						message += "第"+result[x][0]+"行：保存失败，当前部门存在相同的用户<br/>";
					}else if(result[x][1] == '号码重复,请重新输入手机号码！'){
						message += "第"+result[x][0]+"行：号码重复,请重新输入手机号码！<br/>";
					}else{
						message +="第"+result[x][0]+"行：保存失败：未知原因<br/>";
					}
				}
				if(recordCount>result.length){
					message += "其他行保存成功";
				}
				$.messager.alert('提示',message,'info',function(){
					$('#addEmployee').window('close');
					//window.location.href="/pc/lookGroup/forward.htm?companyId="+companyId;
					LookGroup.refreshMain();
				});
			}
		}
		
	};
}();

$(function(){	
	companyId =  window.parent.document.getElementById("companyId").value;
	batchAddEmp.initCombo();
			
});