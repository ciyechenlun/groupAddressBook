updateItem = function(){
	return {
		
		//修改商品信息
		updateItem : function (){
			$('#item_fm').form('submit',{
				url:"/pc/item/update.htm",
				onSubmit: function(){
					return $('#item_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("保存成功",function(){
							$('#item_fm').form('clear');
							Ict.closeWin();
							$('#item_table').datagrid('clearSelections');
							$('#item_table').datagrid('reload');
						});
					}else if(data == "name"){
						Ict.error("商品名称已存在,请重新输入!");
					}else if(data=='ERROR PHOTO'){
						Ict.error("请上传正确格式的图片!");
					}else {
						Ict.error("保存失败!");
					}
				}
			});
		},
		
		//初始化小类
		setSmallSort : function(value){
			$('#itemSmallSortId').combobox({
				url:'/pc/item/smallSort.htm?itemSortId='+value
			});
		}
	};
}();

$(function(){
	updateItem.setSmallSort($("#itemSort").val());
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