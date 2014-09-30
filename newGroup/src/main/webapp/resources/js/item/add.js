addItem = function(){
	return {
		
		//增加商品
		addItem : function (){
			$('#item_fm').form('submit',{
				url:"/pc/item/add.htm",
				onSubmit: function(){
					return $('#item_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("保存成功",function(){
							$('#item_fm').form('clear');
							Ict.closeWin();
							$('#item_table').datagrid('reload');
						});
					}else if(data == "ERROR PHOTO"){
						Ict.error("请选择正确的图片!");
					}else if(data == "code"){
						Ict.error("编号已存在,请重新输入!");
					}else if(data == "name"){
						Ict.error("商品名称已存在,请重新输入!");
					}else {
						Ict.error("保存失败!");
					}
				}
			});
		},
		
		//清空
		clear : function(){
			$('#item_fm').form('clear');
		},
		
		//获取随即树
		randomNum :function(){
			$.ajax({
				type : "POST",
				url  :  "/pc/item/randomNum.htm",
				success : function(data){
					$('#itemCode').val(data);
				}
			});
		}
	};
}();

$(function(){
	$("#itemSort").combobox({
		url : '/pc/item/bigSort.htm',
		valueField : "item_sort_id",
		textField : "item_sort_name",
		editable : false,
		panelHeight : 200,
		onSelect: function(value){
			$('#itemSmallSortId').combobox('setValue',"");
			$('#itemSmallSortId').combobox({
				url:'/pc/item/smallSort.htm?itemSortId='+value.item_sort_id
			});
		}
	});
	$("#itemSmallSortId").combobox({
		valueField : "item_sort_id",
		textField : "item_sort_name",
		editable : false,
		panelHeight : 200
	});
	
});