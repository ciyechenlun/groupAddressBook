searchRe= function(){
	return {
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
		}
		
	};
}();

$(function(){	
	companyId =  window.parent.document.getElementById("companyId").value;
});