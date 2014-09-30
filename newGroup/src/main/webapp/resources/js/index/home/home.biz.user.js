/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */


Home = function() {
	
	var top3Chart;
	
	return {
		
		init: function(){
		
			Home.initPortal();
			Home.initStyle();
			Home.initWorkplan();
			Home.initChart();
			Home.initGoodscombo();
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
			  		var html = '<ul style="padding: 2px;list-style: outside;">';
			  		if(data&&data.length>0){
			  			for (var i = 0; i < data.length; i++) {
			  				html += '<li>' + (i+1) + '、' + data[i] + '</li>';
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
			var height = eval($.pp.height() - 10);
			var width = $('#chartDiv').width();
			var halfHeight = eval(height-60);
			$.chart.height(halfHeight);
			$.chart.width(eval(width-5));
		},
		
		//初始化商品下拉框
		initGoodscombo:function(){
			$('#goods_1').combobox({
				url : '/home/itemList.htm',
				valueField : "item_id",
				textField : "item_name",
				editable : false,
				panelHeight : 120,
				onLoadSuccess:function(){
					var data = $('#goods_1').combobox('getData');
					if(data[0]){
			 			$("#goods_1").combobox('setValue',data[0].item_id);
					}
				},
				onChange : function(newValue, oldValue){
					//商品Id
//					itemId = record.item_id;
					itemId = newValue;
					//查询某商品的销量和销售额
					$.ajax({
					 	url: "/home/saleinfo.htm",
					 	data:{
					 		'itemId' : itemId
					 	},
					  	context: document.body,
					  	success: function(data){
					    	if(QGXSChart){
					    		QGXSChart.showLoading('加载中……');
					    		QGXSChart.series[0].setData(Home.transferArray(data.saleNum),false);
					    		QGXSChart.series[1].setData(Home.transferArray(data.salePrice),false);
					    		QGXSChart.redraw();
					    		QGXSChart.hideLoading();
					    	}
					  	},
					  	error:function(){
					  		Ict.error('趋势图数据加载出错！');
					  	}
					});
				}
			});
		},
		
		//初始化报表
		initChart : function(){
	       top3Chart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'chart',
	                zoomType: 'xy'
	            },
	            title: {
	                text: '商品-门店销售统计'
	            },
	            xAxis: {
	                categories: ['国购店','步行街店','万达店','西二环店','北一环店']
	            },
	            yAxis: [{ // Primary yAxis
	            	min: 0,
	                labels: {
	                    formatter: function() {
	                        return this.value +'件';
	                    }
	                },
	                title: {
	                    text: '销量/库存量'
	                }
	            }, { // Secondary yAxis
	            	min: 0,
	                title: {
	                    text: '销售额'
	                },
	                labels: {
	                    formatter: function() {
	                        return this.value +' 元';
	                    }
	                },
	                opposite: true
	            }],
	            credits:{
	            	text: '安徽移动销售管家',
	            	position: {
	            		align: 'right',
	            		x: -20,
	            		verticalAlign: 'top',
	            		y: 20
	            	}
	            },
	            legend: {
	                layout: 'vertical',
	                backgroundColor: '#FFFFFF',
	                align: 'left',
	                verticalAlign: 'top',
	                x: 90,
	                y: 50,
	                floating: true,
	                shadow: true
	            },
	            tooltip: {
	                formatter: function() {
	                    return ''+
	                       this.x +'-'+ this.series.name + ':'+ this.y + (this.series.name=='销售额'?'元':'件');
	                }
	            },
	            plotOptions: {
	                column: {
	                    pointPadding: 0.2,
	                    borderWidth: 0
	                },
	                line: {
	                    dataLabels: {
	                        enabled: true
	                    }
	                }
	            },
	            series: [{
	                name: '销量',
	                type: 'column',
	                yAxis: 1,
	                data: [49.9, 71.5, 106.4, 129.2, 144.0],
	                dataLabels: {
	                    enabled: true,
	                    rotation: -90,
	                    color: '#FFFFFF',
	                    align: 'right',
	                    x: 4,
	                    y: 10,
	                    style: {
	                        fontSize: '13px',
	                        fontFamily: 'Verdana, sans-serif'
	                    }
	                }
	            }, {
	                name: '库存量',
	                yAxis: 1,
	                type: 'column',
	                data: [48.9, 38.8, 39.3, 41.4, 47.0],
	                dataLabels: {
	                    enabled: true,
	                    rotation: -90,
	                    color: '#FFFFFF',
	                    align: 'right',
	                    x: 4,
	                    y: 10,
	                    style: {
	                        fontSize: '13px',
	                        fontFamily: 'Verdana, sans-serif'
	                    }
	                }
                },
               	{
	                name: '销售额',
	                type: 'line',
	                data: [83.6, 78.8, 98.5, 93.4, 106.0]
	             }
	            ]
	        });
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
	
	$.chart = $('#chart');
	
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
