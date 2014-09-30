NoticeSec = function(){
	return {
		nextStep : function(){
			var publicId = $("em:visible").attr('value');
			var userCompanyId = $("em:visible").attr('text');
			if(publicId){
				$('#noticeFir').hide();
				$('#noticeSec').show();
				
				$('#publicId').val(publicId);
				$('#userCompanyId').val(userCompanyId);
				NoticeSec.addScrollBar();

			}else{
				$.messager.alert("提示","请选择公告号","info");
			}
		},
		addScrollBar:function(){
			if(!$('.phone').is(":hidden")&&!$('#s1').hasClass("mCustomScrollbar")){
				$("#s1").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}else if(!$('.phone').is(":hidden")&&$('#s1').hasClass("mCustomScrollbar")
					&&$('#s1').find('.mCSB_container').height()>$('#s1').height()){
				$("#s1").mCustomScrollbar("destroy");
				$("#s1").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}else if(!$('.phone2').is(":hidden")&&!$('#s2').hasClass("mCustomScrollbar")){
				$("#s2").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}else if(!$('.phone2').is(":hidden")&&$('#s2').hasClass("mCustomScrollbar")
					&&$('#s2').find('.mCSB_container').height()>$('#s2').height()){
				$("#s2").mCustomScrollbar("destroy");
				$("#s2").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}else if(!$('.phone4').is(":hidden")&&!$('#s4').hasClass("mCustomScrollbar")){
				$("#s4").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}else if(!$('.phone4').is(":hidden")&&$('#s4').hasClass("mCustomScrollbar")
					&&$('#s4').find('.mCSB_container').height()>$('#s4').height()){
				$("#s4").mCustomScrollbar("destroy");
				$("#s4").mCustomScrollbar({
					autoHideScrollbar:true,
					theme:"light-thin"
				});
			}
		},
		preStep : function(){
			$('#noticeSec').hide();
			$('#noticeFir').show();
		},
		onBlur:function(){
			if($('#noticeTitle').val()==""){
				$('#noticeTitle').val("请输入标题");
			}
		},
		
		onfocus:function(){
			if($('#noticeTitle').val()=="请输入标题"){
				$('#noticeTitle').val("");
			}
		},
		sendMsg:function(){
			var publicId = $('#publicId').val();
			$.ajax({
				type: "POST",
			    url: "/pc/notice/checkInfo.htm",
			    data: {'publicId' :publicId},
			    success: function(msg){
				  if(msg=="01"){
					  $.messager.alert('提示',"此公告号已达发送上限",'info');
				  }else if(msg=="02"){
					   $.messager.alert('提示',"此公告号发送消息频率过快，请稍后重试",'info');
				  }else{
					  $('#notice_form').form('submit',{
							url:"/pc/notice/sendMsg.htm",
							onSubmit: function(){
								return $('#notice_form').form('validate');
							},
							success:function(data){
								if(data){
									$.messager.alert("提示","发送成功","info",function(){
										window.location.href="/pc/notice/main.htm";
									});
									$.post("/pc/notice/autoSendMessage.htm?noticeId="+data);
								}else{
									
								}
							}
						});
				  }
			   }
			});
		},
		saveDraft:function(){
			$('#notice_form').form('submit',{
				url:"/pc/notice/saveDraft.htm",
				onSubmit: function(){
					return $('#notice_form').form('validate');
				},
				success:function(data){
					if(data){
						$.messager.alert("提示","保存成功","info",function(){
							$('#noticeId').val(data);
						});
					}else{
						
					}
				}
			});
		},
		show:function(value){
			if(value=='1'){
				$('.phone4').show();
				$('.phone2').hide();
				$('.phone').hide();
				$('#phone_txt').text("当前选择机型：S4-1920*1080");
				NoticeSec.addScrollBar();
			}else if(value=='2'){
				$('.phone2').show();
				$('.phone4').hide();
				$('.phone').hide();
				$('#phone_txt').text("当前选择机型：S3-1280*720");
				NoticeSec.addScrollBar();
			}else if(value=='3'){
				$('.phone').show();
				$('.phone4').hide();
				$('.phone2').hide();
				$('#phone_txt').text("当前选择机型：9180-800*480");
				NoticeSec.addScrollBar();
			}
		}
	};
}();
$(function(){	
	$("li").click(function(){
		$(this).parent().find("em").addClass("hide");
		$(this).find("em").removeClass("hide");
	});
	KindEditor.create('textarea[name="noticeContent"]', {
			resizeType : 1,
			urlType : 'domain',
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			afterCreate : function() {
	         	this.sync();
	        },
	        uploadJson: '/resources/scripts/kindeditor/jsp/upload_json.jsp',
	        afterBlur:function(){
	            this.sync();
	        },
	        afterUpload : function(data) {
	        	var html='<img alt=""  src="'+data+'">';
	        	$('.content').prepend(html);
	        }, 
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'image', 'link']
		});
	$("#noticeTitle").keyup(function(){
		var title = $(this).val();
		var time = new Date().format("yyyy年MM月dd hh:mm"); 
		$('.title').html(title);
		$('.time').html(time);
	});
	$(document.getElementById('edit_frame').contentWindow.document.body).keyup(function(){
		$('.content').html($(this).html());
		NoticeSec.addScrollBar();
	});
	
});