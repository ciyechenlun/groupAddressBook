addSort = function(){
	return {
		//打开增加商品大类窗口
		addBigSort : function(){
			itemSort_Win = art.dialog({
				id: 'itemSort_Win',
				content: document.getElementById('itemSort_Win'),
				title: '增加大类',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				lock : true,
				ok: function () {
					$.ajax({
						type : "POST",
						url  :  "/pc/itemSort/addSort.htm?itemSortName="+encodeURI($('#bigSortName').val()),
						success : function(data){
							if(data == "SUCCESS"){
								Ict.info("保存成功",function(){
									$('#bigSortName').val("");
									$('#itemSort').combobox({
										url:'/pc/item/bigSort.htm'
									});
								});
							}
						}
					});
			    },
			    cancelVal: '关闭',
			    cancel: true
			});
		},
		
		//打开增加小类窗口
		addSmallSort : function(){
			var bigSortId = $('#itemSort').combobox('getValue');
			var bigSortName = $('#itemSort').combobox('getText');
			$('#bigSortNameForSmall').val(bigSortName);
			$('#bigSortIdForSmall').val(bigSortId);
			if(bigSortId == ""){
				Ict.info("请先选择大类!");
				return;
			}
			smallItemSort_Win = art.dialog({
				id: 'smallItemSort_Win',
				content: document.getElementById('smallItemSort_Win'),
				title: '增加小类',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				lock : true,
				ok: function () {
					$.ajax({
						type : "POST",
						url  :  "/pc/itemSort/addSmallSort.htm?smallSortName="+encodeURI($('#smallSortName').val())+"&bigSortId="+$("#bigSortIdForSmall").val(),
						success : function(data){
							if(data == "SUCCESS"){
								Ict.info("保存成功",function(){
									$('#bigSortNameForSmall').val("");
									$('#smallSortName').val("");
									$('#itemSmallSortId').combobox({
										url:'/pc/item/smallSort.htm?itemSortId='+$('#bigSortIdForSmall').val()
									});
								});
							}
						}
					});
			    },
			    cancelVal: '关闭',
			    cancel: true
			});
		}
	};
}();

$(function(){
	
});