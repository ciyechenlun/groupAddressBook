/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */


Home = function() {
	
	
	return {
		
		init: function(){
		
			Home.initPortal();
			Home.initStyle();
			Home.initWorkplan();
		},
		
		
		//初始化portal
		initPortal : function(){
			
			$.pp.portal({
				border:false,
				fit:true
			});
			
		},
		
		//将字符数组转换成数字数组
		transferArray : function(array){
			var newArr = new Array();
			for (var index = 0; index < array.length; index++) {
				newArr.push(parseFloat(array[index]));
			}
			return newArr;
		},
		
		//初始化我的待办
		initWorkplan:function(){
			$.worlPlanList = $('#worlPlanList');
			$.worlPlanList.mask('加载中……');
			//查询某商品的销量和销售额
			$.ajax({
			 	url: "/home/workPlan.htm",
			  	context: document.body,
			  	success: function(data){
			  		var html = '<ul>';
			  		if(data&&data.length>0){
			  			for (var i = 0; i < data.length; i++) {
			  				html += '<li>' + data[i] + '</li>';
			  			}
			  		}else{
			  			html += '<li>没有待办事项 </li>';
			  		}
			  		html += '</ul>';
			  		$.worlPlanList.html(html);
			    	$.worlPlanList.unmask();
			  	},
			  	error:function(){
			  		$.worlPlanList.unmask();
			  		//Ict.error('数据加载出错！');
			  	}
			});
		},
		
		
		//初始化数据拉取器
		initPushlet : function(){
			PL._init(); 
			PL.joinListen('/pushlet/workdynamic'); 
		},
		
		//初始化样式
		initStyle : function(){
//			var height = eval($.pp.height() - 10);
//			var width = $('#chartDiv').width();
//			var halfHeight = eval(height/2-25);
		}
		
	};
		
}();

$(function(){
	
	//portal画布
	$.pp = $('#portal');
	//工作动态
	$.gzdt = $('#gzdt');
	//我的待办
	$.wddb = $('#wddb');
	
	Home.init();
	
	$(document).ready(function(){
		Home.initPushlet();
		PL._init(); 
		PL.joinListen('/pushlet/workdynamic'); 
		//事件回调函数
		onData = function (event){
			$('#workdynamicList').html(decodeURIComponent(event.get('workdynamic').replace(/\+/g,' ')));
		};
	});
});
