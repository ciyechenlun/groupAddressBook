addFormatter = function(value, row, index) {
		var date = new Date(value);
		return date.format('Y-m-d');
	};

	saveHeadship = function (){
		$('#companyIdId').combotree('enable',true);
		$('#fm').form('submit', {
			url : "/pc/headship/save.htm",
			onSubmit : function() {
				return $("#fm").form("validate");
			},
			success : function(data) {
				if (data == 1) {
					Ict.info("添加成功!", function() {
						Ict.closeWin();
						$("#headshiptable").datagrid("reload");
					});
				} else {
					Ict.error("添加失败!稍后重试。");
				}
			}
		});
};  	

$(function(){
	$("#headshipLevelId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'headship_level',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
			//add.setCity(value.provinceid);
			//$('#city').combobox('setValue',"");
			//$('#area').combobox('setValue',"");
		}
	});
	
	$("#statusId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'status',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
			//add.setCity(value.provinceid);
			//$('#city').combobox('setValue',"");
			//$('#area').combobox('setValue',"");
		}
	});
});
