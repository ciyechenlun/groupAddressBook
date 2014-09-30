/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */

Home = function() {
	
	var QGXSChart;
	var QYXSChart;
	
	return {
		
		init: function(){
		
			Home.initPortal();
			Home.initStyle();
			Home.initWorkplan();
			Home.initQGXSChart();
			Home.initQYXSChart();
			Home.initGoodsCombo();
			Home.initGrid();
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
//			  		Ict.error('数据加载出错！');
			  	}
			});
		},
		
		//初始化商品下拉框
		initGoodsCombo : function(){
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
			
			$('#goods_2').combobox({
				url : '/home/itemList.htm',
				valueField : "item_id",
				textField : "item_name",
				editable : false,
				panelHeight : 120,
				onLoadSuccess:function(){
					var data = $('#goods_2').combobox('getData');
					if(data[0]){
			 			$("#goods_2").combobox('setValue',data[0].item_id);
					}
				},
				onChange : function(newValue, oldValue){
					//商品Id
//					itemId = record.item_id;
					itemId = newValue;
					//查询某商品的销量和销售额
					$.ajax({
					 	url: "/home/saleinfoReport.htm",
					 	data:{
					 		'itemId' : itemId
					 	},
					  	context: document.body,
					  	success: function(data){
					    	if(QYXSChart){
					    		QYXSChart.showLoading('加载中……');
					    		QYXSChart.xAxis[0].setCategories(data.departments);
					    		QYXSChart.series[0].setData(Home.transferArray(data.saleNum),false);
					    		QYXSChart.series[1].setData(Home.transferArray(data.salePrice),false);
					    		QYXSChart.redraw();
					    		QYXSChart.hideLoading();
					    	}
					  	},
					  	error:function(){
					  		Ict.error('趋势图数据加载出错！');
					  	}
					});
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
			var halfHeight = eval(height/2-25);
			$.qgxsqs.height(halfHeight);
			$.qgxsqs.width(eval(width-5));
			$.qyxsbb.height(halfHeight);
			$.qyxsbb.width(eval(width-5));
			$.bmgk.height(halfHeight);
		},
		
		//初始化全国销售趋势图
		initQGXSChart : function(itemId){
			QGXSChart = new Highcharts.Chart({
		           chart: {
		        	   	height: $.qgxsqs.height()-20,
		        	   	width:$.qgxsqs.width(),
		                renderTo: 'qgxsqs',
		                type: 'line',
		                marginRight: 10,
		                marginBottom: 30,
		                shadow:true,
		                zoomType: 'xy'
		            },
		            title: {
		                text: '全国销售趋势',
		                x: -20, //center
		                style: {
	 		        		fontFamily: '"Microsoft YaHei"', // default font
	 		        		fontSize: '12px'
	 		        	}
		            },
		            credits:{
		            	text: '安徽移动销售管家',
		            	position: {
		            		align: 'right',
		            		x: -20,
		            		verticalAlign: 'top',
		            		y: 20
		            	}
 		            },
 		            style: {
 		        		fontFamily: '微软雅黑', // default font
 		        		fontSize: '12px'
 		        	},
		            xAxis: {
		            	tickPixelInterval:30,
		                categories: ['1月', '2月', '3月', '4月', '5月', '6月',
		                    '7月', '8月', '9月', '10月', '11月', '12月']
		            },
		            yAxis: {
		                title: {
		                    text: '销售额：(元);销量：(件)'
		                },
		                min:0,
		                tickPixelInterval:50,
		                plotLines: [{
		                    value: 0,
		                    width: 1,
		                    color: '#808080'
		                }]
		            },
		            tooltip: {
		                formatter: function() {
		                        return '<b>'+ this.series.name +'</b><br/>'+
		                        this.x +': '+ this.y + (this.series.name=='销售额'?'元':'件');
		                }
		            },
		            legend: {
		                layout: 'vertical',
		                align: 'right',
		                verticalAlign: 'top',
		                x: -50,
		                y: 30,
		                floating: true,
		                borderWidth: 0
		            },
		            series: [{
		                name: '销量',
		                data: []
		            }, {
		                name: '销售额',
		                data: []
		            }]
		        });
		},
		
		//初始化区域销售报表
		initQYXSChart : function(){
			QYXSChart = new Highcharts.Chart({
	            chart: {
	                height: $.qyxsbb.height()-20,
	        	   	width:$.qyxsbb.width(),
	                renderTo: 'qyxsbb',
	                type: 'column',
	                marginRight: 10,
	                marginBottom: 45,
	                shadow:true,
	                zoomType: 'xy'
	            },
          	 	title: {
	                text: '区域销售报表',
	                x: -20, //center
	                style: {
 		        		fontFamily: '"Microsoft YaHei"', // default font
 		        		fontSize: '12px'
 		        	}
	            },
	            style: {
	        		fontFamily: '微软雅黑', // default font
	        		fontSize: '12px'
	        	},
	        	credits:{
	            	text: '安徽移动销售管家',
	            	position: {
	            		align: 'right',
	            		x: -20,
	            		verticalAlign: 'top',
	            		y: 20
	            	}
	            },
	            tooltip: {
		                formatter: function() {
		                    return ''+
		                        this.series.name +': '+ this.y + (this.series.name=='销售额'?'元':'件');
		                }
	            },
	            legend: {
		                layout: 'vertical',
		                align: 'right',
		                verticalAlign: 'top',
		                x: -50,
		                y: 30,
		                floating: true,
		                shadow: true
	            },
	            xAxis: {
	                categories: []
	            },
	            yAxis: {
	            	tickPixelInterval:30,
	                min: 0,
	                title: {
	                	color: '#00009F',
	                    text: '销售额：(元);销量：(件)'
	                }
	            },
	            plotOptions: {
	                column: {
	                    pointPadding: 0.2,
	                    borderWidth: 0
	                }
	            },
	            series: [{
		                name: '销量',
		                data: []
		            }, {
		                name: '销售额',
		                data: []
		         	}
		        ]
	        });
		},
		
		//初始化部门概况表格树
		initGrid : function(){
			$('#bmgrid').treegrid({
						url : "",
						idField:'department_id',  
					    treeField:'department_name', 
					    animate:true,
					    striped:true,
					    fit : true,
					    fitColumns:true,
					    checkbox:true,
					    nowrap: false,//内容不换行   
                		rownumbers: true,//行号  
                		singleSelect : false,
                		border:0,
					    columns:[[  
					        {field:'department_id',title:'部门ID',width:100,hidden:true},
					        {field:'parent_department_id',title:'上级部门ID',width:100,hidden:true},
					        {field:'department_name',title:'部门名称',width:150},
					        {field:'companyName',title:'部门领导',width:100},
					        {field:'department_address',title:'员工数',width:100},
					        {field:'telephone',title:'客户数',width:60},
					        {field:'fax',title:'门店数',width:60},
					        {field:'departmentType',title:'累计收入',width:90},
					        {field:'departmentType',title:'收入进度',width:90}
					    ]],
					    onContextMenu : function(e, node) {
							e.preventDefault();
							$('#grid').treegrid('select', node.target);
							$('#mm').menu('show', {
								left : e.pageX,
								top : e.pageY
							});
						}
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
	//全国销售趋势
	$.qgxsqs = $('#qgxsqs');
	//区域销售报表
	$.qyxsbb = $('#qyxsbb');
	//部门概况
	$.bmgk = $('#bmgk');
	
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

