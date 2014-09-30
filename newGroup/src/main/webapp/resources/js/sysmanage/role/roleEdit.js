editRole = function () {
		$('#fm').form('submit', {
			url : "/pc/role/editRole.htm",
			onSubmit : function() {
				return $("#fm").form("validate");
			},
			success : function(data) {
				if (data == 1) {
					Ict.info("更新成功!", function() {
						Ict.closeWin();
						$("#roletable").datagrid("reload");
					});
				} else {
					Ict.error("更新失败!稍后重试。");
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