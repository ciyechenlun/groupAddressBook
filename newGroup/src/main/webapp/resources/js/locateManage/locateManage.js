/**
 * locateManage.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2012-12-12 下午8:01:14
 */

LocateManage = function(){
	
	//全局地图对象
	var locateMapObj = null;
	//全局的zoom级别
	var zoomLevel = 6;

	var address_static = '';
	var content_static = '';
	var id_static = '';
	var locations = [];
	var overlays = [];
	var all = 0;
	var count = 0;

	//basePath
	var basePath = $('#basePath').val();
	
	//提示图片
	var imageURL = basePath + 'resources/img/icon/marker/blank.png';
	
	var autoZoom = false;

	return {
		
		//创建tipOption
		createTipOption : function(title,content){
			
			var fontstyle=new MFontStyle(); 
		    fontstyle.size=14; 
		    fontstyle.color=0xFFFFFF; 
		    var fillstyle=new MFillStyle(); 
		    fillstyle.color=0x145697; 
		    var fontstyle1=new MFontStyle();
		    fontstyle1.size=13; 
		    //fontstyle1.color=0xFF0066; 
		    var fillstyle1=new MFillStyle(); 
		    fillstyle1.color=0xFFFFCC; 
		    var linestyle=new MLineStyle();//创建线样式对象 
		    linestyle.color=0x145697;//线的颜色，16进制整数，默认为0x005890（蓝色）
			var tipOption=new MTipOptions(); 
			tipOption.title=title; 
		    tipOption.titleFontStyle=fontstyle; 
		    tipOption.titleFillStyle=fillstyle; 
		    tipOption.content=content; 
		    tipOption.contentFontStyle=fontstyle1; 
		    tipOption.fillStyle=fillstyle1; 
		    tipOption.borderStyle=linestyle; 
		    tipOption.tipType=MConstants.HTML_BUBBLE_TIP; 
		    tipOption.tipHeight=230; 
		    tipOption.tipWidth=300; 
		    return tipOption;
		},
		
		//生成tip内容
		createTipContent : function(address,zoom){
			var tipContent = "";
			$.ajax({
				url : "/pc/locate/manage/getTipContent",
				data:{
					address:address,
					zoom:zoom
				},
				type:'post',
				async:false,
				context : document.body,
				dataType:'json',
				success : function(result) {
					var datas = result.data;
					tipContent = '<div style="width:300;height:230;overflow:auto;color:green;">';
					for ( var i = 0; i < datas.length; i++) {
						var data = datas[i];
						tipContent += '姓名:'+data.empName + '  手机:' + data.telephone + '<br />';
					}
					tipContent += '</div>';
				},
				error : function(){
					tipContent = '获取人员列表失败';  
				}
			});
			return tipContent;
		},
		
		//创建Marker
		createMarker : function(id,content,address,lng,lat,zoom){
			var tipContent = LocateManage.createTipContent(address,zoom);
			var tipOption = LocateManage.createTipOption("人员详细列表",tipContent);
		    var markerOption = new MMarkerOptions(); 
		    markerOption.tipOption = tipOption; 
		    markerOption.canShowTip= true; 
		    ////参数说明：标注上要加载图片的url|标注上显示的文字|文字相对于图片的位置（0表示在上方，1表示在图片下方）|文字的字体大小|文字的颜色|标注的背景色|标注的边框颜色|标注的宽度|以及标注的高度。
		    markerOption.imageUrl="http://api.mapabc.com/flashmap/res/GIS1.swf?swfp="+imageURL+"|"+content+"|0|12|0x046788|0xffffff|0x046788|135|15";//气泡标注 
		    markerOption.picAgent=false; 
		    var marker = new MMarker(new MLngLat(lng,lat),markerOption); 
		    marker.id= id; 
		    return marker;
		},
		
		//模糊定位函数
		mLocateFunc : function(zoom) {
			if(typeof(zoom)=='undefined'){
				zoom = LocateManage.checkZoomLevel(zoomLevel);
			}
			address_static = '';
			content_static = '';
			id_static = '';
			
			//zoom:当前地图的缩放级别，范围为3到18，数值越大，代表地图内容越详细
			//3<=zoom<5显示省份级别；5<=zoom<8显示市级别；9<=zoom<13显示区级别；13<=zoom<18显示详细地址级别；
			$.ajax({
				url : "/pc/locate/manage/mLocate/"+zoom,
				type:'post',
				context : document.body,
				dataType:'json',
				success : function(result) {
				    locateMapObj.removeAllOverlays(); 
				    overlays = [];
				    callBackArr = [];
				    locations = result.data;
				    all = locations.length;
				    
				    for (var i = 0; i < all; i++) { 
		            	var d = locations[i];
		            	var id = "marker"+i;
		            	var content = d.detailLocation+':'+d.counter+'人';
		            	var mk = LocateManage.createMarker(id,content,d.detailLocation,d.lng,d.lat,zoom);
		            	overlays.push(mk);
		            } 
		            locateMapObj.addOverlays(overlays,false); 
				},
				error : function(){
					$.messager.alert('消息提示','定位失败，请稍候再试！','error');  
				}
			});
		},
		
		initMap : function() {
			var mapoption = new MMapOptions();
			mapoption.toolbar = MConstants.ROUND; //设置地图初始化工具条，ROUND:新版圆工具条
			mapoption.toolbarPos = new MPoint(20, 20); //设置工具条在地图上的显示位置
			mapoption.overviewMap = MConstants.SHOW; //设置鹰眼地图的状态，SHOW:显示，HIDE:隐藏（默认）
			mapoption.scale = MConstants.SHOW; //设置地图初始化比例尺状态，SHOW:显示（默认），HIDE:隐藏。
			mapoption.zoom = 6;//要加载的地图的缩放级别
			mapoption.center = new MLngLat(117.259482, 31.855592);//要加载的地图的中心点经纬度坐标
			mapoption.language = MConstants.MAP_CN;//设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图
			mapoption.fullScreenButton = MConstants.SHOW;//设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏
			mapoption.centerCross = MConstants.SHOW;//设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏
			mapoption.mapComButton = MConstants.SHOW_NO;
			mapoption.requestNum = 100;//设置地图切片请求并发数。默认100。
			locateMapObj = new MMap("map", mapoption); //地图初始化
			//注册事件
			locateMapObj.addEventListener(locateMapObj,MConstants.MAP_READY,LocateManage.addMenuItem);
			//默认显示的zoom级别是到市级别
			locateMapObj.addEventListener(locateMapObj,MConstants.MAP_READY,LocateManage.mLocateFunc(LocateManage.checkZoomLevel(zoomLevel)));
			//注册地图缩放事件
			locateMapObj.addEventListener(locateMapObj,MConstants.ZOOM_CHANGED,LocateManage.zoomChange);
		},
		
		//添加邮件菜单
		addMenuItem : function(){
			//构造模糊定位菜单
			var mLocateItem = new MMenuItem();
			mLocateItem.id = "mLocateItem";
			mLocateItem.order=1; 
			mLocateItem.menuText="模糊定位"; 
			mLocateItem.isEnabled=true; 
			mLocateItem.functionName = LocateManage.mLocateFunc;
			
			//添加邮件菜单
			var menuArray = new Array(); 
			menuArray.push(mLocateItem); 
			menuArray.push(jLocateItem); 
			locateMapObj.addMenuItems(menuArray); 
		},
		
		//根据zoomLevel获取要显示的级别
		//1:3<=zoomLevel<7显示省份级别；2:7<=zoomLevel<9显示市级别；3:9<=zoomLevel<13显示区级别；4:13<=zoomLevel<19显示详细地址级别；
		checkZoomLevel : function(n){
			var result = 2;
			switch (n) {
				case 3:
					result = 1;
					break;
				case 4:
					result = 1;
					break;
				case 5:
					result = 1;
					break;
				case 6:
					result = 1;
					break;
				case 7:
					result = 2;
					break;
				case 8:
					result = 2;
					break;
				case 9:
					result = 3;
					break;
				case 10:
					result = 3;
					break;
				case 11:
					result = 3;
					break;
				case 12:
					result = 3;
					break;
				case 13:
					result = 4;
					break;
				case 14:
					result = 4;
					break;
				case 15:
					result = 4;
					break;
				case 16:
					result = 4;
					break;
				case 17:
					result = 4;
					break;
				case 18:
					result = 4;
					break;
				default:
					result = 2;
					break;
			}
			return result;
		},
		
		//zoom级别发生改变以后
		zoomChange : function(param){
				//1:3<=zoomLevel<7显示省份级别；2:7<=zoomLevel<11显示市级别；3:11<=zoomLevel<16显示区级别；4:16<=zoomLevel<19显示详细地址级别；
				//检查改变后的zoom级别，如果zoom级别不等于当前要显示的级别，重新定位
				var zoom = param.zoom;
				var level = LocateManage.checkZoomLevel(zoom);
				var static_level = LocateManage.checkZoomLevel(zoomLevel);
				zoomLevel = zoom;
				if(static_level!=level){
					LocateManage.mLocateFunc(level);
				}else{
					locateMapObj.removeAllOverlays(); 
					locateMapObj.addOverlays(overlays,false); 
		            locateMapObj.setContinuousZoom(true);
				}
		}
		
	};
}();

$(function(){
	// 初始化地图
	LocateManage.initMap();
});
