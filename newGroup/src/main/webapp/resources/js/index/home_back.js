/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */


Home = function() {
	
	var mapObj = null;

	return {
		
		initMap : function() {
			var mapoption = new MMapOptions();
			mapoption.toolbar = MConstants.ROUND; // 设置地图初始化工具条，ROUND:新版圆工具条
			mapoption.toolbarPos = new MPoint(20, 20); // 设置工具条在地图上的显示位置
			mapoption.overviewMap = MConstants.SHOW; // 设置鹰眼地图的状态，SHOW:显示，HIDE:隐藏（默认）
			mapoption.scale = MConstants.SHOW; // 设置地图初始化比例尺状态，SHOW:显示（默认），HIDE:隐藏。
			mapoption.zoom = 13;// 要加载的地图的缩放级别
			mapoption.center = new MLngLat(117.24249, 31.85189);// 要加载的地图的中心点经纬度坐标
			mapoption.language = MConstants.MAP_CN;// 设置地图类型，MAP_CN:中文地图（默认），MAP_EN:英文地图
			mapoption.fullScreenButton = MConstants.SHOW;// 设置是否显示全屏按钮，SHOW:显示（默认），HIDE:隐藏
			mapoption.centerCross = MConstants.SHOW;// 设置是否在地图上显示中心十字,SHOW:显示（默认），HIDE:隐藏
			mapoption.requestNum = 100;// 设置地图切片请求并发数。默认100。
			mapObj = new MMap("map", mapoption); // 地图初始化
		}
	};
}();

$(function(){
	// 初始化地图
	Home.initMap();
});
