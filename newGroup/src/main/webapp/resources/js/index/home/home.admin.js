/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */

var mapObj;
Home = function() {
	
	//图片路径
	var leftImgSrc = '/resources/img/left.png';
	var leftsImgSrc = '/resources/img/left_s.png';
	var rightImgSrc = '/resources/img/right.png';
	var rightsImgSrc = '/resources/img/right_s.png';
	
	
	var qyxsqsChart = null;
	var QYXSEZBChart = null;
	var QYXSDCLChart = null;
	var XSTBChart = null;
	
	return {
		
		init: function(){
		
			Home.initPortal();
			Home.initStyle();
			Home.initListner();
			Home.initWorkplan();
			Home.initXSQSChart();
			Home.initGoodsCombo();
			Home.initMap();
			
			Home.initQYXSEZBChart();
			Home.initQYXSDCLChart();
			Home.initXSTBChart();
			Home.loadXSTBChart();
			
			Home.loadQYXSBLData();
			Home.loadQYXSEDCLData();
		},
		
		
		//初始化portal
		initPortal : function(){
			
			$.pp.portal({
				border:false,
				fit:true
			});
			
		},
		
		//初始化事件绑定
		initListner:function(){
		
			$.selectImg = $('.selectImg');
			
			$.selectImg.on({
				mouseover:function(){
					if($(this).attr('id')=='leftImg'){
						$(this).attr('src',leftsImgSrc);				
					}else{
						$(this).attr('src',rightsImgSrc);	
					}
				},
				mouseout:function(){
					if($(this).attr('id')=='leftImg'){
						$(this).attr('src',leftImgSrc);				
					}else{
						$(this).attr('src',rightImgSrc);	
					}			
				},
				
				click:function(){
					//var prefix = '#chart_index_';
					$.chart_index = $('#chart_index');
					var chartIndex = $.chart_index.val();
					if($(this).attr('id')=='leftImg'){
						if(chartIndex=='2'){
							$('#chart_index_2').hide('slow');
							$('#chart_index_1').show('slow');
							$.chart_index.val('1');
							$('#titleTd').html('销售趋势图');
							$('#topTd').show();
						}else if(chartIndex=='3'){
							$('#chart_index_3').hide('slow');
							$('#chart_index_2').show('slow');
							$('#titleTd').html('区域销售图');
							$.chart_index.val('2');
						}
					}else{
						if(chartIndex=='1'){
							$('#chart_index_1').hide('slow');
							$('#chart_index_2').show('slow');
							$.chart_index.val('2');
							$('#titleTd').html('销售趋势图');
							$('#topTd').hide();
						}else if(chartIndex=='2'){
							$('#chart_index_2').hide('slow');
							$('#chart_index_3').show('slow');
							$.chart_index.val('3');
							$('#titleTd').html('销售同比图');
						}
					}
				}
			});
			
			$.sellType.on({
				click:function(){
					//1：销量 2：销售额
					if($(this).val()==0){
						qyxsqsChart.series[1].setVisible(false);
						qyxsqsChart.series[0].setVisible(true);
					}else{
						qyxsqsChart.series[0].setVisible(false);
						qyxsqsChart.series[1].setVisible(true);
					}
				}
			});
		},
		
		//初始化数据拉取器
		startPushlet : function(){
			PL._init(); 
			PL.joinListen('/pushlet/workdynamic'); 
		},
		
		//初始化地图
		initMap:function(){
			var mapoption = new MMapOptions();
			//mapoption.toolbar = MConstants.ROUND; // 设置地图初始化工具条，ROUND:新版圆工具条
			//mapoption.toolbarPos = new MPoint(20, 20); // 设置工具条在地图上的显示位置
			//mapoption.overviewMap = MConstants.SHOW; // 设置鹰眼地图的状态，SHOW:显示，HIDE:隐藏（默认）
			mapoption.scale = MConstants.HIDE; // 设置地图初始化比例尺状态，SHOW:显示（默认），HIDE:隐藏。
			mapoption.zoom = 13;// 要加载的地图的缩放级别
			mapoption.center = new MLngLat(117.24249, 31.85189);// 要加载的地图的中心点经纬度坐标
			mapoption.language = MConstants.MAP_CN;// 设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图
			//mapoption.fullScreenButton = MConstants.SHOW;// 设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏
			mapoption.fullScreenButton= MConstants.HIDE;
			//mapoption.centerCross = MConstants.SHOW;// 设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏
			mapoption.centerCross= MConstants.HIDE;
			mapoption.requestNum = 100;// 设置地图切片请求并发数。默认100。
			mapObj = new MMap("map", mapoption); // 地图初始化
			mapObj.addEventListener(mapObj, MConstants.MAP_READY,Home.startPushlet);
		},
		
		//将字符数组转换成数字数组
		transferArray : function(array){
			var newArr = new Array();
			for (var index = 0; index < array.length; index++) {
				newArr.push(parseFloat(array[index]));
			}
			return newArr;
		},
		
		//查看通知公告
		showNewDetail : function(type,newsId, readNum){
			if('0'==type){
				Ict.openWin('通知公告',800,400,"/pc/notice/noticeDetail/"+newsId+".htm");
			}else if('1'==type){
				Ict.openWin('工作报告',800,400,"/pc/marketinformation/detail.htm?newsId="+newsId);
			}else if('2'==type){
				Ict.openWin('信息反馈', 800, 400, "/pc/marketInfo/marketInfoDetail/"+newsId+".htm");
			}
		},
		
		//我的待办被点击
		//type=0:通知公告；1：工作报告；2： 市场反馈；3：订单审核
		workPlanClicked:function(id,type){
			if(type=='3'){
				Ict.openWin('订单审核','800','400','/pc/record/main.htm');
			}else{
				Home.showNewDetail(type,id,0);
			}
			//重载待办列表
			Home.initWorkplan();
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
			  				html += '<li><a style="text-decoration: none;color:black;" href="javascript:Home.workPlanClicked(\''+ data[i].id +'\',\''+ data[i].type +'\');">' + data[i].plan + '</a></li>';
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
			  	}
			});
		},
		
		//初始化商品下拉框
		initGoodsCombo : function(){
			$.goods.combobox({
				url : '/home/itemList.htm',
				valueField : "item_id",
				textField : "item_name",
				editable : false,
				panelHeight : 120,
				onLoadSuccess:function(){
					var data = $.goods.combobox('getData');
					if(data[0]){
			 			$.goods.combobox('setValue',data[0].item_id);
					}
				},
				onChange : function(newValue, oldValue){
					//商品Id
					itemId = newValue;
					//查询某商品的销量和销售额
					Home.loadXSQSData(itemId);
				}
			});
			
		},
		
		//加载趋势图数据
		loadXSQSData:function(itemId){
			$.ajax({
			 	url: "/home/salenum.htm",
			 	data:{
			 		'itemId' : itemId
			 	},
			  	context: document.body,
			  	success: function(data){
			    	if(qyxsqsChart){
			    		qyxsqsChart.showLoading('加载中……');
			    		qyxsqsChart.series[0].setData(Home.transferArray(data.saleNum),true);
			    		qyxsqsChart.series[1].setData(Home.transferArray(data.salePrice),true);
			    		qyxsqsChart.hideLoading();
			    	}
			  	},
			  	error:function(){
			  		qyxsqsChart.hideLoading();
			  		//Ict.error('趋势图数据加载出错！');
			  	}
			});
		},
		
		//加载区域销售占比图数据 2013年1月30日 15:11:03
		loadQYXSBLData:function(){
			$.ajax({
			 	url: "/home/saleZoneProp.htm",
			  	context: document.body,
			  	success: function(data){
			  		if(QYXSEZBChart) {
			  			QYXSEZBChart.showLoading('加载中……');
			  			QYXSEZBChart.series[0].setData(data);
			  			QYXSEZBChart.hideLoading();
			  		}
//			    	if(qyxsqsChart){
//			    		qyxsqsChart.showLoading('加载中……');
//			    		qyxsqsChart.series[0].setData(Home.transferArray(data.saleNum),true);
//			    		qyxsqsChart.series[1].setData(Home.transferArray(data.salePrice),true);
//			    		qyxsqsChart.hideLoading();
//			    	}
			  	},
			  	error:function(){
			  		QYXSEZBChart.hideLoading();
			  		//Ict.error('趋势图数据加载出错！');
			  	}
			});
		},
		
		//加载区域销售额达成率数据 2013年1月31日 10:10:17
		loadQYXSEDCLData:function(){
			$.ajax({
			 	url: "/home/saleSuccProp.htm",
			  	context: document.body,
			  	success: function(data){
			  		if(QYXSDCLChart) {
			  			QYXSDCLChart.showLoading('加载中……');
			  			QYXSDCLChart.series[0].setData(data);
			  			QYXSDCLChart.hideLoading();
			  		}
			  	},
			  	error:function(){
			  		QYXSDCLChart.hideLoading();
			  	}
			});
		},
		
		//初始化样式
		initStyle : function(){
			var height = eval($.pp.height() - 110);
			var width = $('#chartDiv').width()-10;
			//var halfHeight = eval(height/2-25);
			$.chart.height(height);
			$.chart.width(eval(width-5));
		},
		
		//初始化区域销售报表
		initXSQSChart : function(){
			qyxsqsChart = new Highcharts.Chart({
	            chart: {
	                height: $.chart.height(),
	        	   	width:$.chart.width(),
	                renderTo: 'chart_index_1',
	                marginRight: 30,
	                marginBottom: 30,
	                borderColor:'#6699FF',
	                shadow:true,
	                zoomType: 'x'
	            },
	            exporting: {
		            filename: '年销量-年销售额趋势图',
		            type	: 'image/png',
		            buttons : {
		            	exportButton:{
		            		x:-110,
		            		y:11,
		            		menuItems:[{
		                        text: '导出为PNG图片(小尺寸)',
		                        onclick: function() {
		                            this.exportChart({
		                                width: 250
		                            });
		                        }
		                    }, {
		                        text: '导出为PNG图片(大尺寸)',
		                        onclick: function() {
		                            this.exportChart(); // 800px by default
		                        }
		                    },null,null]
		            	},
		            	printButton:{
		            		enabled:false
		            	}
		            }
		        },
          	 	title: {
                	text: ' ',
                	style:{
						color: '#3E576F',
						fontSize: '10px'
					}
	            },
	            xAxis: {
	                type: 'datetime',
	                dateTimeLabelFormats: {
	                	day		: '%Y-%m-%d',
		                month	: '%Y-%m',
						year	: '%Y' 
		            },
	                maxZoom: 14 * 24 * 3600000, // fourteen days
	                title: {
	                    text: null
	                }
	            },
	            yAxis: {
	            	min:0,
	                title: {
	                    text: ''
	                },
	                showFirstLabel: false
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
	            	xDateFormat: '%Y-%m-%d',
	            	formatter:function(){
	            		return Highcharts.dateFormat('%Y-%m-%d',this.x) + ' ' + this.series.name + ':' + this.y + (this.series.name=='销量'?'件':'元');
	            	},
	            	style: {
						color: '#FF0000'
					}
	            },
	            legend: {
	                enabled: true,
	                align: 'center',
	                verticalAlign: 'top',
	                //x: 200,
	                //y: 2,
	                floating: true,
	                shadow: true
	            },
	            plotOptions: {
	                area: {
	                    fillColor: {
	                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                        stops: [
	                            [0, Highcharts.getOptions().colors[0]],
	                            [1, 'rgba(0,0,255,0)']
	                        ]
	                    },
	                    lineWidth: 2,
	                    marker: {
	                        enabled: false,
	                        states: {
	                            hover: {
	                                enabled: true,
	                                radius: 5
	                            }
	                        }
	                    },
	                    shadow: false,
	                    states: {
	                        hover: {
	                            lineWidth: 2
	                        }
	                    },
	                    threshold: null
	                }
	            },
	    
	            series: [{
	                type: 'area',
	                name: '销量',
	                pointInterval: 24 * 3600 * 1000,
	                pointStart: Date.UTC(new Date().getFullYear(), 0, 01),
	                data: []
	            },{
	                type: 'area',
	                name: '销量额',
	                pointInterval: 24 * 3600 * 1000,
	                pointStart: Date.UTC(new Date().getFullYear(), 0, 01),
	                data: [],
	                visible:false
	            }]
	        });
		},
	
		//初始化区域销售额占比图表
		initQYXSEZBChart:function(){
			 QYXSEZBChart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'qyxsezbChart',
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                height: $.chart.height(),
	        	   	width:	eval($.chart.width())/2,
	                marginRight: 30,
	                marginBottom: 30
	            },
	            title: {
	                text: '区域销售额占比'
	            },
	            tooltip: {
	        	    pointFormat: '{series.name}: <b>{point.percentage}%</b>',
	            	percentageDecimals: 1
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
	            exporting: {
		            filename: '年销量-年销售额趋势图',
		            type	: 'image/png',
		            buttons : {
		            	exportButton:{
		            		x:-110,
		            		y:11,
		            		menuItems:[{
		                        text: '导出为PNG图片(小尺寸)',
		                        onclick: function() {
		                            this.exportChart({
		                                width: 250
		                            });
		                        }
		                    }, {
		                        text: '导出为PNG图片(大尺寸)',
		                        onclick: function() {
		                            this.exportChart(); // 800px by default
		                        }
		                    },null,null]
		            	},
		            	printButton:{
		            		enabled:false
		            	}
		            }
		        },
	            plotOptions: {
	                pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: true,
	                        color: '#000000',
	                        connectorColor: '#000000',
	                        formatter: function() {
	                            return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
	                        }
	                    }
	                }
	            },
	            series: [{
	                type: 'pie',
	                name: '销售额占比',
	                data: [
	                    ['合肥办事处',   45.0],
	                    ['芜湖办事处',   26.8],
	                    {
	                        name: '阜阳办事处',
	                        y: 12.8,
	                        sliced: true,
	                        selected: true
	                    },
	                    ['六安办事处',    8.5],
	                    ['安庆办事处',     6.2],
	                    ['其他',   0.7]
	                ]
	            }]
	        });
		},
		
		//初始化区域销售达成率图表
		initQYXSDCLChart:function(){
			 QYXSDCLChart = new Highcharts.Chart({
			    chart: {
			        renderTo: 'qyxsedclChart',
			        type: 'gauge',
			        plotBackgroundColor: null,
			        plotBackgroundImage: null,
			        plotBorderWidth: 0,
			        plotShadow: false,
			        height: $.chart.height(),
	        	   	width:	$.chart.width()/2,
	                marginRight: 30,
	                marginBottom: 30
			    },
			    
			    title: {
			        text: '区域销售额达成率'
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
			    exporting: {
		            filename: '年销量-年销售额趋势图',
		            type	: 'image/png',
		            buttons : {
		            	exportButton:{
		            		x:-110,
		            		y:11,
		            		menuItems:[{
		                        text: '导出为PNG图片(小尺寸)',
		                        onclick: function() {
		                            this.exportChart({
		                                width: 250
		                            });
		                        }
		                    }, {
		                        text: '导出为PNG图片(大尺寸)',
		                        onclick: function() {
		                            this.exportChart(); // 800px by default
		                        }
		                    },null,null]
		            	},
		            	printButton:{
		            		enabled:false
		            	}
		            }
		        },
			    
			    pane: {
			        startAngle: -150,
			        endAngle: 150,
			        background: [{
			            backgroundColor: {
			                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
			                stops: [
			                    [0, '#FFF'],
			                    [1, '#333']
			                ]
			            },
			            borderWidth: 0,
			            outerRadius: '109%'
			        }, {
			            backgroundColor: {
			                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
			                stops: [
			                    [0, '#333'],
			                    [1, '#FFF']
			                ]
			            },
			            borderWidth: 1,
			            outerRadius: '107%'
			        }, {
			            // default background
			        }, {
			            backgroundColor: '#DDD',
			            borderWidth: 0,
			            outerRadius: '105%',
			            innerRadius: '103%'
			        }]
			    },
			       
			    // the value axis
			    yAxis: {
			        min: 0,
			        max: 160,
			        
			        minorTickInterval: 'auto',
			        minorTickWidth: 1,
			        minorTickLength: 10,
			        minorTickPosition: 'inside',
			        minorTickColor: '#666',
			
			        tickPixelInterval: 30,
			        tickWidth: 2,
			        tickPosition: 'inside',
			        tickLength: 10,
			        tickColor: '#666',
			        labels: {
			            step: 2,
			            rotation: 'auto'
			        },
			        title: {
			            text: '销售额达成率'
			        },
			        plotBands: [{
			            from: 0,
			            to: 80,
			            color: '#DF5353' // red
			        }, {
			            from: 80,
			            to: 100,
			            color: '#DDDF0D' // yellow
			        }, {
			            from: 100,
			            to: 120,
			            color: '#55BF3B' // green
			        },{
			            from: 120,
			            to: 160,
			            color: '#0000FF' //blue
			        }]        
			    },
			
			    series: [{
			        name: '达成率',
			        data: [80],
			        tooltip: {
			            valueSuffix: '%'
			        }
			    }]
			});
		},
		
		//初始化销售同比图
		initXSTBChart:function(){
			XSTBChart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'chart_index_3',
	                type: 'area',
	                height: $.chart.height(),
	        	   	width:$.chart.width(),
	                marginRight: 30,
	                marginBottom: 30,
	                borderColor:'#6699FF',
	                shadow:true,
	                zoomType: 'x'
	            },
	            title: {
	                text: '  '
	            },
	            legend: {
	                enabled: true,
	                align: 'center',
	                verticalAlign: 'top',
	                //x: 200,
	                //y: 2,
	                floating: true,
	                shadow: true
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
	            exporting: {
		            filename: '年销量-年销售额趋势图',
		            type	: 'image/png',
		            buttons : {
		            	exportButton:{
		            		x:-110,
		            		y:11,
		            		menuItems:[{
		                        text: '导出为PNG图片(小尺寸)',
		                        onclick: function() {
		                            this.exportChart({
		                                width: 250
		                            });
		                        }
		                    }, {
		                        text: '导出为PNG图片(大尺寸)',
		                        onclick: function() {
		                            this.exportChart(); // 800px by default
		                        }
		                    },null,null]
		            	},
		            	printButton:{
		            		enabled:false
		            	}
		            }
		        },
	            xAxis: {
	            	type: 'datetime',
	                dateTimeLabelFormats: {
	                	//day		: '%Y-%m-%d',
		                month	: '%m'
						//year	: '%Y' 
		            }
	            },
	            yAxis: {
	                title: {
	                    text: '销售额(元)'
	                },
	                labels: {
	                    formatter: function() {
	                        return this.value;
	                    }
	                }
	            },
	            tooltip: {
	            	crosshairs: true,
                	shared: true
//	                formatter: function() {
//	                    return this.series.name +' produced <b>'+
//	                        Highcharts.numberFormat(this.y, 0) +'</b><br/>warheads in '+ this.x;
//	                }
	            },
	            plotOptions: {
	                area: {
	                	pointInterval: 24 *30 * 3600 * 1000,
	                	pointStart: Date.UTC(new Date().getFullYear(), 0, 01),
	                    marker: {
	                        enabled: false,
	                        symbol: 'circle',
	                        radius: 2,
	                        states: {
	                            hover: {
	                                enabled: true
	                            }
	                        }
	                    }
	                }
	            },
	            series: [{
	                name: '今年',
//	                data: [1020, 4000, 1600, 1500, 3200, 1060 , 1100, 3020, 1100, 2350, 3690, 4040,2000]
	                data: []
	            }, {
	                name: '去年',
//	                data: [1000, 8000, 6000, 2000, 2490, 2600 , 3100, 1320, 1000, 2000, 3790, 3000,1000]
	                data: []
	            }]
	        });
		},
		
		//加载销售同比图数据
		loadXSTBChart : function(){
			$.ajax({
			 	url: "/home/year.htm",
			  	context: document.body,
			  	success: function(data){
			    	if(XSTBChart){
			    		XSTBChart.showLoading('加载中……');
			    		XSTBChart.series[0].setData(Home.transferArray(data.thisYear),true);
			    		XSTBChart.series[1].setData(Home.transferArray(data.lastYear),true);
			    		XSTBChart.hideLoading();
			    	}
			  	},
			  	error:function(){
			  		XSTBChart.hideLoading();
			  		//Ict.error('趋势图数据加载出错！');
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
	$.chart = $('.chart');
	//商品下拉框
	$.goods = $('#goods');
	
	$.sellType = $('input[name="sellType"]');
	
	Home.init();
	
	//提示图片
	var imageURL = basePath + '/resources/img/blank.png';
	var longititude = null;
	var latitude = null;
	var content = null;

	//事件回调函数
	onData = function (event){
		var result = decodeURIComponent(event.get('workdynamic')).replace(/\+/g,' ');
		result = $.parseJSON(result);
		var type = result.type;
		var cont = result.content;
		if($('#workdynamicList').text()!=cont){
			$('#workdynamicList').html('<marquee behavior=alternate scrollamount=2>'+cont+'</marquee>');
		}
		if(type=='2'){
			var lng = result.longitude;
			var lat = result.latitude;
			if(lng!=longititude||lat!=latitude||content!=cont){
				overlays = [];
				var temp = cont.substring(19);
				var mk = createMarker(temp,lng,lat);
				overlays.push(mk);
				mapObj.addOverlays(overlays,true); 
				longititude = lng;
				latitude = lat;
				content = cont;
			};
		};
	};
		
	//创建Marker
	createMarker = function(content,lng,lat){
		var markerOption = new MMarkerOptions(); 
	    ////参数说明：标注上要加载图片的url|标注上显示的文字|文字相对于图片的位置（0表示在上方，1表示在图片下方）|文字的字体大小|文字的颜色|标注的背景色|标注的边框颜色|标注的宽度|以及标注的高度。
	    markerOption.imageUrl="http://api.mapabc.com/flashmap/res/GIS1.swf?swfp="+imageURL+"|"+content+"|0|12|0xFF3300|0xffffff|0x046788|260|10";//气泡标注 
	    markerOption.picAgent=false; 
	    var marker = new MMarker(new MLngLat(lng,lat),markerOption); 
	    marker.id = ''+lng+lat;
	    return marker;
	};
	
	
});

