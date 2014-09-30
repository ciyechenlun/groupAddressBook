detailDepartment = function(){
	return {
		
		//初始化 上级部门树
		intcombotree : function(){
			$('#parentDepartmentId').combotree({
				url:'/pc/department/getDept.htm',
				editable:false,
				required:true,
				panelHeight:200,
				onSelect : function(node){
					if(node.id != 0){
						$.ajax({
							type : "POST",
							url  :  "/pc/department/findCompanyByDeptId.htm?departmentId="+node.id,
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
			
		}
	};
}();

$(function(){
	detailDepartment.intcombotree();
});