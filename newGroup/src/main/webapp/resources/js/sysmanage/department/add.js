addDepartment = function(){
	return {
		
		//初始化 上级部门树
		intcombotree : function(){
			$('#parentDepartmentId').combotree({
				url:'/pc/department/getDept.htm',
				editable:false,
				required:true,
				panelHeight:200,
				onSelect : function(node){
					$('#area').combobox('setValue',"");
					$('#parentDepartmentForAdd').val(node.id);
					if(node.id != 0){
						$.ajax({
							type : "POST",
							url  :  "/pc/department/findCompanyByDeptId.htm?departmentId="+node.id,
							success : function(data){
								$('#company').combotree('setValue',data);
								$('#company').combotree('disable',true);
								$('#companyForAdd').val(data);
							}
						});
					}else{
						$('#company').combotree('enable',true);
					}
				},
				onLoadSuccess : function(){
					var row = $('#grid').treegrid('getSelections');
					if(row.length==0){
						$('#parentDepartmentId').combotree('setValue',"");
						$('#parentDepartmentId').combotree('enable',true);
					}else{
						$('#parentDepartmentId').combotree('setValue',row[0].department_id);
						$('#parentDepartmentForAdd').val(row[0].department_id);
					}
				}
			});
			
		},
		
		//增加方法
		save : function(){
			$('#dept_fm').form('submit',{
				url:"/pc/department/add.htm",
				onSubmit: function(){
					return $('#dept_fm').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						Ict.info("保存成功",function(){
							$('#dept_fm').form('clear');
							Ict.closeWin();
							$('#grid').treegrid('reload');
						});
					} else {
						Ict.error("保存失败!");
					}
				}
			});
		}
	};
}();

$(function(){
	addDepartment.intcombotree();
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
		onSelect : function(record){
			if($('#parentDepartmentId').combotree('getValue')==""){
				Ict.info("请先选择上级部门！");
				$('#area').combobox('setValue',"");
			}else if($('#parentDepartmentId').combotree('getValue') !="0"){
				$.ajax({
					type : "POST",
					url  :  "/pc/department/findDepartmentAreaByDeptId.htm?departmentId="+$('#parentDepartmentId').combotree('getValue'),
					success : function(data){
						if(parseInt(data)<parseInt(record.data_code)){
							Ict.info("所选择的区域不能高于上级部门的区域！");
							$('#area').combobox('setValue',"");
						}
					}
				});
			}
		}
	});
});