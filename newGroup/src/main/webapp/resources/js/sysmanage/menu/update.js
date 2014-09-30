updateMenu = function(){
	return {
		
		//更新方法
		update : function(){
			var parentId = $('#parentId').combotree('getValue');
			if(parentId == $('#menuId').val()){
				Ict.error("请勿将上级菜单修改为当前修改的菜单!");
				return;
			}
			$('#company').combotree('enable',true);
			$('#menu_fm').form('submit',{
				url:"/pc/menu/update.htm",
				onSubmit: function(){
					return $('#menu_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("修改成功",function(){
							$('#menu_fm').form('clear');
							Ict.closeWin();
							$('#menu').treegrid('reload');
						});
					} else if(data=='ERROR PHOTO'){
						Ict.error("请上传正确格式的图片!");
					} else{
						Ict.error("修改失败!");
					}
				}
			});
		}
	};
}();

$(function(){
	//初始化新增菜单里的路径
	$('#path').combobox({
		url :'/pc/menu/path.htm',
		valueField:'path',
		textField:'path',
		required : true,
		editable : true,
		panelHeight:200,
		onSelect : function(value){
			$('#menuName').val(value.menuName);
		}
	});
	
	//初始化新增菜单里的上级菜单
	$('#parentId').combotree({
		url:'/pc/menu/comboTree.htm',
		editable:false,
		required:true,
		onSelect : function(node){
			if(node.id != 0){
				$.ajax({
					type : "POST",
					url  :  "/pc/menu/findCompanyByMenuId.htm?menuId="+node.id,
					success : function(data){
						$('#company').combotree('setValue',data);
						$('#company').combotree('disable',true);
					}
				});
			}else{
				$('#company').combotree('enable',true);
			}
		}
	});
});