/**
 * 头部页面JS
 * 
 * @author li.sanlai@ustcinfo.com
 */

Header = function() {
	

	return {

		//注销
		logout : function(){
			$.messager.confirm('提示','<span style="color:#000">确定要注销吗？</span>',function(b){
				if(b){
					window.location.href = "/logout";
				}
			});
		},
		
		//打开修改密码窗口
		changePwd :function(){
			//Header.winShow('window_password');
			$('#window_password').window({
			     width: 450,
			     title:'修改密码',
			     shadow: false,
			     closed: false,
				 cache:  false,
			     height: 310,
			     href :'/toChangePwd.htm',
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
		         onMove:function(left,top){
					   	if(top<0){
					   		$(this).window('move',{left:left,top:0});
					   	}
					   	if(left<0){
					   		$(this).window('move',{left:0,top:top});
					   	}
					   },
				 modal:true
			});
			$('#window_password').window('open');
		},
		
		savePassword : function(){
			var oldPassword = $.trim($("#oldPassword").val());
			var newPassword = $.trim($("#newPassword").val());
			var newPassword2 = $.trim($("#newPassword2").val());
			if(oldPassword == ""){
				$('#message').html('请输入旧密码');
				return;
			}
			if(newPassword == ""){
				$('#message').html('密码为空,请勿输入空格！');
				return;
			}
			if(newPassword2 == ""){
				$('#message').html('密码为空,请勿输入空格！');
				return;
			}
			if(newPassword != newPassword2){
				$('#message').html('两次输入的密码不相同');
				return;
			}
			$.ajax({
				  url: "/pc/user/changePassword.htm",
				  type:'post',
				  data:{
				  	'oldPassword' : oldPassword,
				  	'newPassword' : newPassword
				  },
				  dataType:'json',
				  success: function(data){
				  	if(data.success=='true'){
				  		$("#oldPassword").val("");
				  		$("#newPassword").val("");
				  		$("#newPassword2").val("");
				  		$.messager.alert('提示','密码修改成功,请重新登陆!','info',function(){
				  			window.location.href="/logout";
				  		});
				  	}else{
				  		if(data.msg){
				  			$.messager.alert('提示',data.msg,'error');
				  		}else{
				  			$.messager.alert('提示','密码修改失败,请稍后重试!','error');
				  		}
				  		
				  	}
				  },
				  error:function(){
					  $.messager.alert('提示','系统出错，密码修改失败！','error');
				  }
			});
			
		},
		photo:function(value){
			value = "/pc/company/images/" + value;
			art.dialog({
				content : '<img src='+value+'>',
				width:'auto',
				height:'auto',
				zIndex : "20000",
				left: '40%',
			    top: '30%',
			    lock : true,
			    cancelVal: '关闭',
			    cancel: true
			});
		},
		winShow : function(id){
			var w=Hg.get(id),b=document.body, cw=b.clientWidth,ch=b.scrollTop;
			$(w).css('left',(cw -500)/2).css('top',parseInt(ch)).css('display','block');
		},
		
		winShow1 : function(id){
			var w=Hg.get(id),b=document.body, cw=b.clientWidth,ch=b.scrollTop + 300;
			$(w).css('left',(cw -500)/2).css('top',parseInt(ch)).css('display','block');
		},
		
		winHide : function(id){
			var w=Hg.get(id);
			w.style.display="none";
		}
		
	};

}();

