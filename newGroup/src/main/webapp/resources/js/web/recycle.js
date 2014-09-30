recycle = function(){
	return {
		//部门树
		initTree : function(){
			$('#recycle_department').tree({
				url :'/pc/deptMag/treeTest.htm?companyId=' + window.parent.document.getElementById("companyId").value+'&isRecycle=true',
				persist:'cookie',
				cookieId: 'treeview-black',
				onClick : function(node){
					//$("#departmentId").val(node.id);
					//$("#toPage").val(1);
					//window.location.href = "/pc/recycle/getDelElement.htm?" + $("#searchForm").serialize()+"&treeId="+node.id;
					$('#recycleFrame').attr('src',"/pc/recycle/getDelElement.htm?pageNo=1&departmentId="+node.id+"&companyId="+window.parent.document.getElementById("companyId").value+"&treeId="+node.id);
				}
			});
		}
	};
}();

$(function(){	
	var type = window.parent.parent.document.getElementById("companyType").value;
	if(type == "bnewleft01 bnewleft01a liGetSelected" || 
			type == "bnewleft01 bnewleft01a bnewlefton liGetSelected"){
		recycle.initTree();
	}
});