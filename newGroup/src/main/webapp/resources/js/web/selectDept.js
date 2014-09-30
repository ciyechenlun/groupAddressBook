SelectDept = function() {
	return {
		//关闭窗口
		closeWin : function() {
		    $('#chooseDept').window('close');
		},
		
		save : function() {
			var deptId = $.trim($("#deptId").val());
			if(null == deptId || "" == deptId) {
				$.messager.alert("提示", '请选择部门', 'info');
				return false;
			}
			var deptName = $.trim($("#SelectedDept").val());
			var empsNum = $("#empsNum").val();
			if(0 == empsNum) {
				$.messager.alert("提示", '【' + deptName + '】下无可同步人员,请重新选择部门', 'info');
				return false;
			}
			$('#chooseDept').window('close');
		}
	};
}();

$(function(){
	$('#employeeDeptTreeD').tree({
		url     : '/pc/message/msgTreeByCompanyId.htm?companyId=' + $('#compId').val(),
		onClick : function(node) {
			$("#deptId").val(node.id);
			$("#SelectedDept").val(node.text);
			$("#SelectedDeptId").val(node.id);
			$("#empSelected").empty();
			$("#syncObject").val("");
			var empIdArray = "";
			var empNameArray = "";
			$.ajax({
				url     : '/pc/gray/empBydepartmentId.htm?id=' + node.id,
				type    : 'POST',
				success : function(data) {
					var html = "";
					for(var i = 0; i < data.length; i++) {
						empIdArray += "'" + $.trim(data[i].userId) + "',";
						empNameArray += $.trim(data[i].userName) + ",";
						html = '<option value="' + data[i].userId + " " + data[i].userName + '">' + data[i].userName + '</option>';
						$("#empSelected").append(html);
					}
					empIdArray = empIdArray.substring(0, empIdArray.length - 1);
					empNameArray = empNameArray.substring(0, empNameArray.length - 1);
					$("#empIds").val(empIdArray);
					$("#syncObject").val(empNameArray);
					//人员个数
					$("#empsNum").val(data.length);
				}
			});
		}
	});
});