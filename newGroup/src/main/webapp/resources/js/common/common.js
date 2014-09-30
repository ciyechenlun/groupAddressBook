/**
 * common.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2012-12-1 上午12:09:48
 */
Ict = function(){
	return {
		//添加Tab
		addTab:function(title,href,iconCls,closable){
			var exist = $('#mainPanel').tabs('exists',title);
			if(exist){
				$('#mainPanel').tabs('select',title);
			}else{
				var content = '<iframe id="frame'+title+'" scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';  
		        $('#mainPanel').tabs('add',{  
		            title:title,  
		            content:content,
		            iconCls:iconCls,
		            closable:closable,
		            tools:[
		            	{
		            		iconCls:'icon-mini-refresh',
		            		handler:function(){
		            			$('#frame'+title).attr('src',href);
		            		}
		           		}
		            ]
		        });  
			}
		},
		
		//取得所有可以关闭的tab
		getClosableTabs :function(){
			var tabs = $('#mainPanel').tabs('tabs');
			var closableTabs = new Array();
			for (var i = 0; i < tabs.length; i++) {
				var panel = tabs[i];
				var closable = panel.panel('options').closable;
				if(closable==true){
					closableTabs.push(i);
				}
			}
			return closableTabs;
		},
		
		//移除可以关闭的所有tab
		removeTabs:function(){
			var closableTabs = this.getClosableTabs();
			if(closableTabs.length>0){
				var tab = closableTabs.shift();
				$('#mainPanel').tabs('close',tab);
				if(closableTabs.length>0){
					this.removeTabs();
				}
			}
		},
		
		//关闭选中标签页
		closeSelectedTab:function(){
			var panel = $('#mainPanel').tabs('getSelected');
			var closable = panel.panel('options').closable;
			var title = panel.panel('options').title;
			if(closable==true){
				$('#mainPanel').tabs('close',title);
			}
		},
		
		//关闭所有标签页
		closeAllTab:function(){
			this.removeTabs();
		},
		
		//关闭除选中之外的所有tab
		removeOtherTab:function(){
			var panel = $('#mainPanel').tabs('getSelected');
			var title = panel.panel('options').title;
			
			var tabs = $('#mainPanel').tabs('tabs');
			var closableTabs = new Array();
			for (var i = 0; i < tabs.length; i++) {
				var p = tabs[i];
				var closable = p.panel('options').closable;
				var tt = p.panel('options').title;
				if(title!=tt && closable==true){
					closableTabs.push(i);
				}
			}
			
			if(closableTabs.length>0){
				var tab = closableTabs.shift();
				$('#mainPanel').tabs('close',tab);
				if(closableTabs.length>0){
					this.removeOtherTab();
				}
			}
		},
		//关闭其他标签
		closeOtherTab:function(){
			this.removeOtherTab();
		},
		
		//提示消息
		alert:function(msg,fn){
			if(fn){
				$.messager.alert('消息',msg,'',fn);
			}else{
				$.messager.alert('消息',msg);
			}
		},
		
		//消息提示
		info:function(msg,fn){
			if(fn){
				$.messager.alert('消息',msg,'info',fn);
			}else{
				$.messager.alert('消息',msg,'info');
			}
		},
		
		//警告消息
		warn:function(msg,fn){
			if(fn){
				$.messager.alert('警告',msg,'warning',fn);
			}else{
				$.messager.alert('警告',msg,'warning');
			}
		},
		
		//错误消息
		error:function(msg,fn){
			if(fn){
				$.messager.alert('错误',msg,'error',fn);
			}else{
				$.messager.alert('错误',msg,'error');
			}
		},
		
		
		/**
		 * 确认消息
		 * demo:
		 * Ict.confirm('yes or no?',function(r){
		 *		if(r){
		 *			Ict.alert('yes');
		 *		}else{
		 *			Ict.alert('no');
		 *		}
		 *	});
		 */
		confirm : function(msg,callback){  
            $.messager.confirm('确认消息', msg, callback);  
        },
        
		//右下角滑动提示框
		slideMsg : function(msg,timeout){
            $.messager.show({  
                title:'提示消息',  
                msg:msg, 
                timeout:timeout?timeout:3000,
                showType:'show'  
            });  
        },
        
        //打开窗口（默认使用）
        openWin : function(title,width,height,href){
//           var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';  
		   $('#commonWindow').window({
			     title: title,
			     width: width,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: height,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:href,
//				 content:content,
				 top:(document.body.clientHeight-height)/2 ,   
		         left:(document.body.scrollWidth-width)/2,
				 modal:true
			});	
		},
		
		//弹出窗口（特殊情况下选择使用）
		openWin2 : function(title,width,height,href){
          var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';  
          parent.$('#commonWindow').window({
			     title: title,
			     width: width,
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: height,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 content:content,
				 top:(parent.document.body.clientHeight-height)/2 ,   
		         left:(parent.document.body.scrollWidth-width)/2,
				 modal:true
			});	
		},
		//打开对话框
     
		//关闭窗口
		closeWin : function (){
			$('#commonWindow').window('close');
	 	},
	 	
	 	//关闭弹窗
	 	closeWin2 : function(){
	 		parent.$('#commonWindow').window('close');
	 	}
	};
}();

$(function(){
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

});
