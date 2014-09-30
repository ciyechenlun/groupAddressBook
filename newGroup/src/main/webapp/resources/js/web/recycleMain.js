recycle = function(){
	return {
		recycleElement : function(departmentId,parentDepartmentId,userCompanyId,type){
			$.messager.confirm("提示","确定恢复信息?",function(r){
				if(r){
					if(type=='部门'){
						recycle.recycleDepartment(departmentId,parentDepartmentId);
					}else{
						recycle.recycleEmployee(departmentId,userCompanyId);
					}
					
				}
				
			});
		},
		recycleDepartment : function(departmentId,parentDepartmentId){
			if(parentDepartmentId&&parentDepartmentId!='0'){
				$.ajax({
					type: "POST",
					url: "/pc/recycle/isDepartmentDelete.htm?departmentId="+parentDepartmentId,
					dataType: "JSON",
					success: function(data){
						if(data == true)
						{
							$.messager.alert('提示',"请先恢复上级部门",'info');
						}
						else
						{
							$.ajax({
								type: "POST",
								url: "/pc/recycle/recycleDepartment.htm?departmentId="+departmentId,
								dataType: "JSON",
								success: function(data){
									if(data==true){
										$.messager.alert('提示',"部门已恢复",'info',function(){
											window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize();
										});
									}
									else
									{
										$.messager.alert('提示','操作失败','error');
									}
								}
							});
						}
							
					}
				});
			}else{
				$.ajax({
					type: "POST",
					url: "/pc/recycle/recycleDepartment.htm?departmentId="+departmentId,
					dataType: "JSON",
					success: function(data){
						if(data==true){
							$.messager.alert('提示',"部门已恢复",'info',function(){
								window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize();
							});
						}
						else
						{
							$.messager.alert('提示','操作失败','error');
						}
					}
				});
			}
			
		},
		recycleEmployee : function(departmentId,userCompanyId){
			$.ajax({
				type: "POST",
				url: "/pc/recycle/isDepartmentDelete.htm?departmentId="+departmentId,
				dataType: "JSON",
				success: function(data){
					if(data == true)
					{
						$.messager.alert('提示',"请先恢复该员工所属部门",'info');
					}
					else
					{
						$.ajax({
							type: "POST",
							url: "/pc/recycle/recycleEmployee.htm?departmentId=" + departmentId+"&userCompanyId="+userCompanyId,
							dataType: "JSON",
							success: function(data){
								if(data=="success"){
									$.messager.alert('提示',"员工已恢复",'info',function(){
										window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize();
									});
									
									}else if (data=="exist"){
										$.messager.alert('提示',"当前部门存在相同手机号的用户",'info');
									}
									else
									{
										$.messager.alert('提示','操作失败','error');
									}
							}
						});
					}
						
				}
			});
			
		},
		delRecycleElement : function(departmentId,userCompanyId,type){
			$.messager.confirm("提示","删除后将无法恢复。确认删除?",function(result){
				if(result){
					if(type=='部门'){
						recycle.delRecycleDepartment(departmentId);
					}else{
						recycle.delRecycleEmployee(departmentId,userCompanyId);
					}
				}
			});
			
		},
		delRecycleDepartment : function(departmentId){
			$.ajax({
				type: "POST",
				url: "/pc/recycle/delRecycleDepartment.htm?departmentId="+departmentId,
				dataType: "JSON",
				success: function(data){
					if(data==true){
						$.messager.alert('提示',"部门已删除",'info',function(){
							window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize();
						});
					}
					else
					{
						$.messager.alert('提示','操作失败','error');
					}
				}
			});
		},
		delRecycleEmployee : function(departmentId,userCompanyId){
			$.ajax({
				type: "POST",
				url: "/pc/recycle/delRecycleEmployee.htm?departmentId=" + departmentId+"&userCompanyId="+userCompanyId,
				dataType: "JSON",
				success: function(data){
					if(data==true){
						$.messager.alert('提示',"员工已删除",'info',function(){
							window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize();
						});
						
					}
					else
					{
						$.messager.alert('提示','操作失败','error');
					}
				}
			});
			
		}
	};
}();
