/**
 * 登录页面JS
 * login.js
 * @author lisanlai
 * @email li.sanlai@ustcinfo.com
 * @date 2013-1-4 下午3:15:28
 */

Login = function(){
	
	//标记是否需要验证码
	var loginFailureMustValCode = false;
	
	return {
		
		//初始化
		init : function() {
			//输入框获取焦点
			$("#j_username").focus();
			
			Login.bindListners();
			
    		if($("#loginFailureMustValCode").val() == "yes")
    			Login._loginHande();
    			
    		document.onkeydown = function(e){   
    		    var ev = document.all ? window.event : e; 
    		    if(ev.keyCode == 13) { 
    		    	Login.login();
			 	}
    		};
    	},
    	
    	//绑定事件
    	bindListners:function(){
    		$("#loginBtn").on({
    			'click' : Login.login
    		});
    	},
    	
    	//刷新验证码
    	refreshCode : function() {
    		$("#jcaptchaId").attr("src", "/security/securityCode?" + (new Date()).getTime());
    	},
    	
    	//登录
    	login : function() {
    		if(!Login.validationForm())
    			return;
    		
    		$("#error").hide();
    		$("#loginBtn").attr("disabled", true)
    		$("#loginImg").attr("src", '/resources/images/lobtn_load.gif');
    		$.post("j_spring_security_check", $("#loginForm").serialize(), function(data) {
    			if(data.type == "BadCapatch") {
    				$("#error").show().html("验证码错误，请重新输入 ");
    				$("#jcapatch_code").val("");
    				Login._loginHande();
    			}else if(data.type == "userlock") {
    				$("#error").show().html("该账号已被锁定");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				Login._loginHande();
    			} else if(data.type && data.type.search("BadCredentials")!=-1) {
    				var count = 5-data.type.split('_')[1];
    				$("#error").show().html("密码错误,还可以输入"+count+"次");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				Login._loginHande();
    			} else if(data.type == "UsernameNotFound") {
    				$("#error").show().html("账号或密码错误");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				Login._loginHande();
    			} else if(data.type == "NoAuthority") {
    				$("#error").show().html("您无权限登录网页版");
    				$("#j_password").val("");
    				$("#jcapatch_code").val("");
    				Login._loginHande();
    			} else {
    				$("#error").hide();
    				//if ($("#to").val() == 'ped'){
        			//	window.location.href = "/pedmeter/pedometer/index.htm";
    				//}
    				//else
					//{
    					window.location.href = "/index.htm";
					//}
    			}
    		}).error(function() { $("#loginBtn").attr("disabled", false); 
    		$("#loginImg").attr("src", '/resources/images/lobtn.gif');});
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
    		Login.changeLoginBodyStyle();
    		$("#loginBtn").removeAttr("disabled");
    		$("#loginImg").attr("src", '/resources/images/lobtn.gif');
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
    			Login.changeLoginBodyStyle();
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
    			url : '/mobile/testMsg/sendMsg.htm',
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
					}else if(data=="0"){
						$('#errorMsg').hide();
		    			$("#error").show().html("发送失败,请确认输入的用户名!");
		    			return;
					}else if(data=="2"){
						$('#errorMsg').hide();
		    			$("#error").show().html("忘记密码提示短信间隔不足一分钟，请稍候尝试!");
		    			return;
					}else if(data=="3"){
						$('#errorMsg').hide();
		    			$("#error").show().html("用户不存在!");
		    			return;
					}
				}
    		});
    	}
		
	};
	
}();

$(function(){
	$.loginForm = $('#loginForm');
	Login.init();
});