/**
 * 模块使用统计
 * by:李三来
 */

StatisticsConfig = function(){
	
	return {
		
		//初始化菜单树
		initMenuTree : function(){
			$('#menu_tree').tree({
				url :'/pc/menu/tree.htm',
				checkbox:true,
				onCheck: function(node,checked){
					if(node.attributes == "Y"){
						if(checked){
//							alert("保存叶子节点");
							$.ajax({
								type : "POST",
								url : '/pc/statistics/save.htm',
								data:{
									  'menuId':node.id,
									  'menuName' :node.text,
									  'path' : node.path
								 },
								success : function(data){
								}
							});
						}else{
//							alert("删除叶子节点");
							$.ajax({
								type : "POST",
								url : '/pc/statistics/delete.htm',
								data:{
									  'menuId':node.id
								 },
								success : function(data){				
								}
							});
						}
					}else{
						if(checked){
//							alert("保存非叶子节点");
							$.ajax({
								type : "POST",
								url : '/pc/statistics/saveAll.htm',
								data:{
									  'menuId':node.id,
									  'menuName' :node.text,
									  'path' : node.path
								 },
								success : function(data){
								}
							});
						}else{
//							alert("删除非叶子节点");
							$.ajax({
								type : "POST",
								url : '/pc/statistics/deleteAll.htm',
								data:{
									  'menuId':node.id
								 },
								success : function(data){				
								}
							});
						}
					}
				},
				onLoadSuccess:function(node, data){
					$("#menu_tree").tree('expandAll');
					$('#menu_tree').mask('加载中 ……');
					$.ajax({
						type : "POST",
						url : '/pc/statistics/list.htm',
						success : function(data){
							for(var i = 0;i < data.length;i++){
								var roleMenu = $("#menu_tree").tree('find',data[i].menu_id);
								$("#menu_tree").tree('check',roleMenu.target);
							}
							$('#menu_tree').unmask();
						},
						error:function(){
							$('#menu_tree').unmask();
						}
					});
				}
			});
		}
	};
	
}();


$(function(){
	StatisticsConfig.initMenuTree();
});