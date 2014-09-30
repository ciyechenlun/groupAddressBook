/**
 * password.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2013-1-17 上午12:27:32
 */
 
Password = function(){

	return {
	
		//保存密码
		savePassword:function(){
			
			var oldPassword = $.trim($('#oldPassword').val());
			var newPassword = $.trim($('#newPassword').val());
			var confirmPassword = $.trim($('#confirmPassword').val());
			
			if(oldPassword==''){
				$('#message').html('请输入旧密码');
				$('#oldPassword').focus();
				return;
			}
			if(newPassword==''){
				$('#message').html('请输入新密码');
				$('#newPassword').focus();
				return;
			}
			if(newPassword.length<6){
				$('#message').html('新密码最小长度6位');
				$('#newPassword').focus();
				return;
			}
			
			if(confirmPassword==''){
				$('#message').html('请确认新密码');
				$('#confirmPassword').focus();
				return;
			}
			if(newPassword!=confirmPassword){
				$('#message').html('新密码两次输入不一致');
				$('#confirmPassword').focus();
				return;
			}
		
			$('body').mask('正在修改密码……');
			
			$.ajax({
				  url: "/pc/systemUser/savePassword.htm",
				  type:'post',
				  data:{
				  	'oldPassword' : oldPassword,
				  	'newPassword' : newPassword
				  },
				  dataType:'json',
				  context: document.body,
				  success: function(data){
				  	$('body').unmask();
				  	if(data.success=='true'){
				  		Ict.info('密码修改成功,请您重新登录!',function(){
				  			window.location.href="/logout";
				  		});
				  	}else{
				  		if(data.msg){
					  		Ict.error(data.msg);
				  		}else{
				  			Ict.error('密码修改失败！');
				  		}
				  		
				  	}
				  },
				  error:function(){
				  	$('body').unmask();
				  	Ict.error('系统出错，密码修改失败！');
				  }
			});
		}
		
	};

}();