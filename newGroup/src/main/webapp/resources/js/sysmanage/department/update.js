updateDepartment = function(){
	return {
		
		//初始化 上级部门树
		intcombotree : function(){
			$('#parentDepartmentId').combotree({
				url:'/pc/department/getDept.htm',
				editable:false,
				required:true,
				panelHeight:200,
				onLoadSuccess : function(){
					$('#company').combotree('disable',true);
				}
			});
			
		},
		
		//修改方法
		update : function(){
			$('#company').combotree('enable',true);
			var parentId = $('#parentDepartmentId').combotree('getValue');
			if(parentId == $('#departmentId').val()){
				Ict.error("请勿将上级部门修改为当前修改的部门!");
				return;
			}
			$('#dept_fm').form('submit',{
				url:"/pc/department/update.htm",
				onSubmit: function(){
					return $('#dept_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("修改成功",function(){
							$('#dept_fm').form('clear');
							Ict.closeWin();
							$('#grid').treegrid('reload');
						});
					} else {
						Ict.error("修改失败!");
					}
				}
			});
		}
	};
}();

$(function(){
	updateDepartment.intcombotree();
	$('#type').combobox({
		url:'/pc/department/type.htm',
		textField:'data_content',
		valueField:'data_code',
		required : true,
		editable:false,
		panelHeight:120
	});
	$('#area').combobox({
		url:'/pc/department/area.htm',
		textField:'data_content',
		valueField:'data_code',
		required : true,
		editable:false,
		panelHeight:150,
		onSelect:function(record){
			if($('#parentDepartmentForAdd').val() != "0"){
				if(parseInt($('#departmentArea').val())<parseInt(record.data_code)){
					Ict.info("所选择的区域不能高于上级部门的区域！");
					$('#area').combobox('setValue',"");
				}
			}
		}
	});
});