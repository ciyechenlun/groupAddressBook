/**
 * homecommon.js
 * 
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2013-1-22 下午4:10:57
 */

$(function() {

			$(document).ready(function() {
				Highcharts.setOptions({
							lang : {
								months : ['01','02','03','04','05','06','07','08','09','10','11','12'],
								weekdays : ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
								downloadJPEG:'下载为JPEG图片',
								downloadPDF :'下载为PDF文件',
								downloadPNG :'下载为PNG图片',
								downloadSVG :'下载为SVG图片',
								exportButtonTitle:'下载',
								loading:'加载中……',
								resetZoom:'重置Zoom',
								resetZoomTitle:'重置Zoom比例为1:1'
							}
						});
			});
		});