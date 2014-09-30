//var cTime = document.getElementById('createTimeId').value;
//if(cTime != ''){
//	 $("#createTimeId").attr("value",cTime.substring(0,10));
//}else{
//	$("#createTimeId").attr("value",cTime);
//}

editFormatter = function(value, row, index) {
	var date = new Date(value);
	return date.format('Y-m-d');
};

function saveHeadship() {
		$('#fm').form('submit', {
			url : "/pc/headship/editHeadship.htm",
			onSubmit : function() {
				return $("#fm").form("validate");
			},
			success : function(data) {
				if (data == 1) {
					Ict.info("更新成功!", function() {
						Ict.closeWin();
						$("#headshiptable").datagrid("reload");
					});
				} else {
					Ict.error("更新失败!稍后重试。");
				}
			}
		});
}

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
		}
	});
});