$.extend($.fn.validatebox.defaults.rules, {  
	validateTel: {  
        validator: function(value, param){  
        	return (/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/g.test(value));
        },  
        message: '请输入正确的手机号码'  
    }  
});

update = function(){
	return {
		//更新方法	
		updateCompany: function(){
//			var parentId = $('#parentCompanyId').combotree('getValue');
//			if(parentId == $('#companyId').val()){
//				Ict.error("请勿将上级企业修改为当前企业!");
//				return;
//			}
			if($("#pedometerFlag").val()=='1'){
				if($("#pedometerSwitch").attr('checked')){
					$("#pedometer").val("1");
				}else{
					$("#pedometer").val("0");
				}
			}	
			$('#comp_fm').form('submit',{
				url:"/pc/company/updateCompany.htm",
				onSubmit: function(){
					return $('#comp_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("修改成功",function(){
							$('#comp_fm').form('clear');
							Ict.closeWin();
							//$('#company_tt').treegrid('reload');
						});
					} else if(data=='ERROR PHOTO'){
						Ict.error("请选择zip格式的企业皮肤文件!");
					} else {
						Ict.error("修改失败!");
					}
				}
			});
		}
	};
	
}();


$(function(){
//	$("#parentCompanyId").combotree({
//		url:'/pc/company/tree.htm',
//		required : true,
//		editable : false,
//		panelHeight : 200
//	});
	var pedometer = $("#pedometer").val();
	if(pedometer=="1"){
		$("#pedometerSwitch").attr('checked',true);
	}
	var json =[
				{"id":"A","text":"合肥"},
				{"id":"B","text":"芜湖"},
				{"id":"C","text":"蚌埠"},
				{"id":"D","text":"淮南"},
				{"id":"E","text":"马鞍山"},
				{"id":"F","text":"淮北"},
				{"id":"G","text":"铜陵"},
				{"id":"H","text":"安庆"},
				{"id":"J","text":"黄山"},
				{"id":"K","text":"阜阳"},
				{"id":"L","text":"宿州"},
				{"id":"M","text":"滁州"},
				{"id":"N","text":"六安"},
				{"id":"P","text":"宣城"},
				{"id":"Q","text":"巢湖"},
				{"id":"R","text":"贵池"},
				{"id":"S","text":"亳州"}
			];
	$('#city').combobox({
		valueField : "id",
		textField : "text",
		editable : false,
		panelHeight : 120,
		data:json
	});

});
