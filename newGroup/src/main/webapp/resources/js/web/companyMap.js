CompanyMap = function(){
	return {
		initCompanyComboBox:function(){
			$('#allCompany').combobox({
				editable:true,
				url:'/orgAllList.htm?companyId='+ companyId,
				valueField:'company_id',
				textField:'company_name',
				panelHeight:200,
				onSelect:function(record){
					$("#companyIds").val(record.company_id);
				}
			});
		},
	
		addRecord:function(){
			if(!$('#companyIds').val()){
				Ict.error("未选择企业");
				return;
			};
			if(!$('#mapZip').val()){
				Ict.error("上传文件为空");
				return;
			};
			$("#comp_map").form("submit",{
				url:"/pc/companyMap/add.htm",
				onSubmit:function(){//验证上传内容是否为空
					return $("#comp_map").form("validate");
				},
				success:function(data){
					if(data == "01"){
						Ict.error("添加失败，或稍后再试。");
					}else if(data=='02'){
						Ict.error("文件格式不正确。请输入.zip或者.ZIP格式文件");
					}else if(data=='03'){
						Ict.error("文件为空");
					}else{
						Ict.alert("添加成功",function(){
								window.location.reload();
						});
						
					}
				}
			});
		}
	}
	
}();

$(function(){	
	CompanyMap.initCompanyComboBox();
});