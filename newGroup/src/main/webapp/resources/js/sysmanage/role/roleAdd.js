saveRole = function() {
		$('#company').combotree('enable',true);
		$('#fm').form('submit', {
			url : "/pc/role/save.htm",
			onSubmit : function() {
				return $("#fm").form("validate");
			},
			success : function(data) {
				if (data == 1) {
					Ict.info("添加成功!", function() {
						Ict.closeWin();
						$("#roletable").datagrid("reload");
					});
				} else {
					Ict.error("添加失败!稍后重试。");
				}
			}
		});
};

$(function(){
	$("#statusId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'status',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
		}
	});
	if($('#roleCodeForAdd').val()!='0'){
		$("#forAdmin").hide();
		$("#company").combotree({
			required : false
		});
	}
});
