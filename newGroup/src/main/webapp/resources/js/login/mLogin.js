/**
 * 登录页面JS
 * mLogin.js
 * @author yuanfengjian
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-14 下午4:42:18
 */

MLogin = function(){
	
	//标记是否需要验证码
	var loginFailureMustValCode = false;
	
	return {
		
		//初始化
		init : function() {
			//输入框获取焦点
			$("#j_username").focus();
			
			MLogin.bindListners();
			
    		if($("#loginFailureMustValCode").val() == "yes")
    			MLogin._loginHande();
    			
    		document.onkeydown = function(e){   
    		    var ev = document.all ? window.event : e; 
    		    if(ev.keyCode == 13) { 
    		    	MLogin.login();
			 	}
    		};
    	},
    	
    	//绑定事件
    	bindListners:function(){
    		$("#loginBtn").on({
    			'click' : MLogin.login
    		});
    	},
    	
    	//刷新验证码
    	refreshCode : function() {
    		$("#jcaptchaId").attr("src", "/security/securityCode?" + (new Date()).getTime());
    	},
    	
    	//登录
    	login : function() {
    		if(!MLogin.validationForm())
    			return;
    		
    		$("#error").hide();
    		$("#loginBtn").attr("disabled", true).val("登录中...");
    		$.post("j_spring_security_check", $("#loginForm").serialize(), function(data) {
    			if(data.type == "BadCapatch") {
    				$("#error").show().html("验证码错误，请重新输入 ");
    				$("#jcapatch_code").val("");
    				MLogin._loginHande();
    			} else if(data.type == "BadCredentials") {
    				$("#error").show().html("密码错误，请重新输入 ");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				MLogin._loginHande();
    			} else if(data.type == "UsernameNotFound") {
    				$("#error").show().html("该用户不存在");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				MLogin._loginHande();
    			} else if(data.type == "NoAuthority") {
    				$("#error").show().html("您无权限登录网页版");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				MLogin._loginHande();
    			} else {
    				$("#error").hide();
    				window.location.href = "/pedmeter/pedometer/index.htm";
    			}
    		}).error(function() { $("#loginBtn").attr("disabled", false).val("登  录"); });
    	},
    	
    	//改变登录框样式
    	changeLoginBodyStyle : function(){
    		$.loginBoxBody =  $('#loginBox-body');
    		$.loginBoxBody.css({
    			'padding':'2px 26px 0 26px',
    			'height': 'auto'
    		});
    	},
    	
    	//登录回调函数
    	_loginHande : function() {
    		$('#errorMsg').hide();
    		MLogin.changeLoginBodyStyle();
    		$("#loginBtn").removeAttr("disabled").val("登  录");
    		$("#jcaptchaId").attr("src", "/security/securityCode?" + (new Date()).getTime());
    		$("#jcapatch_code_div").show();
    		loginFailureMustValCode = true;
    	},
    	
    	//验证表单
    	validationForm : function() {
    		if($("#j_username").val() == "") {
    			$('#errorMsg').hide();
    			$("#error").show().html("请输入用户名");
    			return false;
    		} else if($("#j_password").val() == "") {
    			$('#errorMsg').hide();
    			$("#error").show().html("请输入密码");
    			return false;
    		} else if($("#jcapatch_code").val() == "" && loginFailureMustValCode) {
    			MLogin.changeLoginBodyStyle();
    			$('#errorMsg').hide();
    			$("#error").show().html("请输入验证码");
    			return false;
    		}
    		
    		return true;
    	},
    	
    	//忘记密码
    	forget : function(){
    		if($("#j_username").val() == "") {
    			$('#errorMsg').hide();
    			$("#error").show().html("请输入用户名,新密码将发送到您的手机！");
    			return;
    		}
    		$.ajax({
    			url : '/mobile/testMsg/send',
				data : {
					'userName' :$("#j_username").val() 
				},
				type : 'post',
				async : false,
				success : function(data) {
					if(data=="1"){
						$('#errorMsg').hide();
		    			$("#error").show().html("短信发送成功,请注意查收!");
		    			return;
					}else{
						$('#errorMsg').hide();
		    			$("#error").show().html("发送失败,请确认输入的用户名!");
		    			return;
					}
				}
    		});
    	}
		
	};
	
}();

$(function(){
	$.loginForm = $('#loginForm');
	MLogin.init();
});