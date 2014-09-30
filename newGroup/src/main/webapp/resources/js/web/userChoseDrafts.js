$(function(){
	$("#addOneD").click(function() {
	    var $options = $('#select1D option:selected'); //获取选中的选项
	    if(hasSameOption($options,"select2D"))
	    {
	    	$.messager.alert('错误', '存在相同的已选人员','error');
	    }
	    else{
	    	$options.appendTo('#select2D');
	    }
	});
	$("#select1D").dblclick(function() {
	    var $options = $('#select1D option:selected'); //获取选中的选项
	    if(hasSameOption($options,"select2D"))
	    {
	    	$.messager.alert('错误', '存在相同的已选人员','error');
	    }
	    else{
	    	$options.appendTo('#select2D');
		}
	});
	$("#addAllD").click(function() {
	    var $options = $('#select1D option');
	});
	$("#select2D").dblclick(function() {
		var $options = $('#select2D option:selected'); //获取选中的选项
		if(hasSameOption($options,"select1D"))
	    {
			$('#select2D option:selected').remove();
	    }
		else{
	    	$options.appendTo('#select1D');
		}
	});
	$("#removeOneD").click(function() {
	    var $option = $('#select2D option:selected');
	    $option.appendTo('#select1D');
	});
	$("#removeAllD").click(function() {
	    var $option = $('#select2D option');
	    $option.appendTo('#select1D');
	});
	//关闭窗口
	function closeWin(){
	    $('#choseUser').window('close');
	};
});

//检测右侧是否已经存在相同的选项
function hasSameOption(option,select)
{
	var retValue = false;
	$("select[id="+select+"] option").each(function(){
		var thisOpt = $(this).val();
		if(option.val()==thisOpt)
		{
			retValue = true;
		}
	});
	return retValue;
}

function save() {
	var val_1 = $("#select2D").val();
	if (val_1 == null) {
		$.messager.alert('警告', '请选择人员', 'warning');
		return false;
	} else {
		val_1 += ",";
		var val_2 = val_1.split(",");
		var val_4 = "";
		for ( var i = 0; i < val_2.length - 1; i++) {
			val_3 = val_2[i];
			val_4 = val_3.split(" ");

			$("#empIdD").append("<span id="+val_4[0]+"_"+val_4[1]+" >'"+val_4[0]+"',</span>");
			$("#empNameD").append("<span onclick='javascript:Message.deletee(this);' name="+val_4[0]+"_"+val_4[1]+" >"+val_4[1]+";</span>");

		}	

		closeWin();
	};
};

$(function(){
	 //公司部门树
//	alert($('#compId').val());
	var dept = $("#departmentId")!=null?$("#departmentId").val():"0";
	$('#employeeDeptTreeD').tree({
		//url:'/pc/systemUser/companyDeptTree.htm',
		url:'/pc/message/msgTreeByCompanyIdAndDeptID.htm?companyId='+$('#compId').val() + '&department_id=' + dept,
		onClick: function(node){
			$("#select1D").empty();
			var idIcon = node.id;
//			alert(idIcon);
			$.ajax({
				url:'/pc/message/empBydepartmentId.htm?id='+idIcon,
				type: 'POST',
				success : function(data){
						var html = "";
						for(var i = 0; i < data.length; i ++){
							var value = data[i];
							html = '<option value="'+value.userId + " " + value.usreName +'">'+value.usreName+'</option>';
							$("#select1D").append(html);
						}
				}
			});
		}
	});
});