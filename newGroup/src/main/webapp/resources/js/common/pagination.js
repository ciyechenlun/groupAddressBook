pagination = function(){
	return {
		
	};
}();

$(function(){
//	$("input[name='toTargetPage']").numberbox({
//		min : 1
//	});
	
	// 创建分页
	$("div[name='Pagination']").pagination($("#totalRecords").val(), {
		num_edge_entries: 1, //边缘页数
		items_per_page: $("#pageSize").val(),
		num_display_entries: 4, //主体页数
		current_page: $("#currPage").val() - 1, //主体页数
		callback: pageselectCallback
	});
	 
	function pageselectCallback(page_index, jq){
		$("#toPage").val(page_index + 1);
		var url = $("#searchForm").attr("action");
		if(url.indexOf('?')>-1)
		{
			url = url + "&" + $("#searchForm").serialize();
		}
		else{
			url = url + "?" + $("#searchForm").serialize();
		}
		location = url;
	}
	$("input[name='toTargetPage_btn']").click(function(){
		var page = parseInt($(this).siblings("input").val());
		if(page <= 0){
			return;
		}
		var currPage = parseInt($("#currPage").val());
		var totalPages = parseInt($("#totalPages").val());
		if(page == currPage){
			return;
		}
		if(page >= totalPages){
			$("#toPage").val(totalPages);
		} else {
			$("#toPage").val(page);
		}
		var url = $("#searchForm").attr("action");
		location = url + "?" + $("#searchForm").serialize();
	});
});