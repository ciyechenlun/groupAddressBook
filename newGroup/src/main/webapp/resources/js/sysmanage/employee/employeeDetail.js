var gTime = document.getElementById('graduationDateHidden').value;
if(gTime != ''){
	 $("#graduationDateId").attr("value",gTime.substring(0,10));
}else{
	$("#graduationDateId").attr("value",gTime);
}

var bTime = document.getElementById('birthdayHidden').value;
if(bTime != ''){
	 $("#birthdayId").attr("value",bTime.substring(0,10));
}else{
	$("#birthdayId").attr("value",bTime);
}

var jTime = document.getElementById('joinDateHidden').value;
if(jTime != ''){
	 $("#joinDateId").attr("value",jTime.substring(0,10));
}else{
	$("#joinDateId").attr("value",jTime);
}

var lTime = document.getElementById('leaveDateHidden').value;
if(lTime != ''){
	 $("#leaveDateId").attr("value",lTime.substring(0,10));
}else{
	$("#leaveDateId").attr("value",lTime);
}

$(function(){
	$("#sexId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'sex',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
		}
	});
	
	$("#politicsStatusId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'politics_status',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
		}
	});
	
	$("#degreeId").combobox({
		url : '/pc/dictData/comoTree.htm?typeCode='+'degree',
		valueField : "dataCode",
		textField : "dataContent",
		required : true,
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
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