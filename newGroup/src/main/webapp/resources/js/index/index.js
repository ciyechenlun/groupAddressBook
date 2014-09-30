Index = function() {

	return {
		imports : function(){
			window.location.href = "/pc/import/main.htm";
		},
		
		lookGroup : function(){
			window.location.href = "/pc/lookGroup/forward.htm?companyId="+window.parent.document.getElementById("companyId").value;
		},
		f_inputOnblur : function(){
			$('#searchValue').val("");
		},
		
		search : function(){
			window.location.href = "/pc/lookGroup/search.htm?" + $("#searchForm").serialize();
		},
		
		addCompany : function(){
			$("#companyId").val("");
			Header.winShow1('window_company');
		},
		
		updateCompany : function(){
			var companyId = window.parent.document.getElementById("companyId").value;
			if(companyId == null || companyId == ""){
				$.messager.alert('提示','请选择通讯录!','info');
				return;
			}
			var companyType = window.parent.document.getElementById("companyType").value;
			if (companyType == "bnewleft01 bnewleft01a liGetSelected"
					|| companyType == "bnewleft01 bnewleft01a bnewlefton liGetSelected") {
				$.messager.alert('提示','企业通讯录不能修改!','info');
				return;
			}
			$("#companyId").val(companyId);
			Header.winShow1('window_company');
			$('#toggleDelEdite1').text('请输入新名称');
		},
		
		deleteCompany : function(){
			var companyId = window.parent.document.getElementById("companyId").value;
			if(companyId == null || companyId == ""){
				$.messager.alert('提示','请选择通讯录!','info');
				return;
			}
			var companyType = window.parent.document.getElementById("companyType").value;
			if (companyType == "bnewleft01 bnewleft01a liGetSelected"
					|| companyType == "bnewleft01 bnewleft01a bnewlefton liGetSelected") {
				$.messager.alert('提示','企业通讯录不能删除!','info');
				return;
			}
			$.messager.confirm('提示信息','确认是否删除该群组?',function(r){ 		
				if (r){ 								
					$.ajax({
						type: "POST",
						url: "/pc/pcompany/delete.htm",
						data: {'companyId' :companyId},
						success: function(data){
							if(data == "SUCCESS"){
								$.messager.alert('提示',"删除群组成功",'info',function(){
									window.location.href = "/index.htm";
								});
							}else{
								$.messager.alert('提示','删除失败,请稍后再试!','error');
							}
						}
					}); 
				}
			});
		},
		
		saveCompany : function(){
			var companyName = $.trim($("#companyName").val());
			if(companyName == null || companyName == ""){
				$.messager.alert('提示','通讯录名称不能为空!','info');
				return;
			}
			$.ajax({
				url:"/pc/pcompany/save.htm",
				data: {
					'companyName' : companyName,
					'companyId' : $("#companyId").val()
					},
				dataType: "json",
				success: function(data){
					if(data.success=='true'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#comp_form').form('clear');
							Header.winHide('window_company');
							window.location.href = "/index.htm";
						});				
					}else {
						$.messager.alert('提示',data.msg,'error');	
					}
				  }
				});
		}
		
	};
}();

$(document).ready(function() {
});