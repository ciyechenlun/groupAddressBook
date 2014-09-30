/**
 * jquery.overvide.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2013-1-23 下午3:21:54
 */
 
(function($){
	//备份jquery的ajax方法
	var _ajax=$.ajax;
	
	//重写jquery的ajax方法
	$.ajax=function(opt){
		//备份opt中error和success方法
		var fn = {
			error:function(XMLHttpRequest, textStatus, errorThrown){},
			success:function(data, textStatus){}
		};
		if(opt.error){
			fn.error=opt.error;
		}
		if(opt.success){
			fn.success=opt.success;
		}
		
		//扩展增强处理
		var _opt = $.extend(opt,{
			//覆盖状态吗处理函数，当session过期的时候，能够让ajax请求处理
			statusCode: {401: function() {
				//如果是顶层窗口
				if (window.top != window.self) {
					window.parent.location.href = "/security/sessionTimeoutHandler";
				} else {
					window.location.href = "/security/sessionTimeoutHandler";
				}
			}},
			
			error:function(XMLHttpRequest, textStatus, errorThrown){
				//错误方法增强处理
				fn.error(XMLHttpRequest, textStatus, errorThrown);
			},
			success:function(data, textStatus){
				//成功回调方法增强处理
				fn.success(data, textStatus);
			}
		});
		_ajax(_opt);
	};
})(jQuery);