Sms = function(){
	return {
		closeWin : function() {
			window.location.href = "/pc/message/sms.htm";
		},
		
		sendMsg : function() {
			var content = $("#content").val();
			var sendObj =$("[name='target']:checked").val();
			var companyId = $("#companyId").val();
			var companyName = $("#companyName").val();
			if(null == $.trim(content) || '' == $.trim(content)) {
				$.messager.alert("提示", '请填写消息内容你!', 'info');
				return false;
			}
			$.messager.confirm('提示', '确认发送短信进行推广吗?', function(btn) {
				if(btn) {
					$('#dvContainer').showLoading('tips');
					$.post('/pc/message/sendMsg.htm', {'companyId' : companyId, 'content' : content, 'sendObj':sendObj}, function(data) {
						if(data.userNum == '0') {
							if(sendObj =='1'){
								$.messager.alert("提示", '【' + companyName + '】暂无本网用户,不可进行短信推广,请确认!', 'info');
							}else if(sendObj =='2'){
								$.messager.alert("提示", '【' + companyName + '】暂无未注册用户,不可进行短信推广,请确认!', 'info');
							}else if(sendObj =='3'){
								$.messager.alert("提示", '【' + companyName + '】暂无异网用户,不可进行短信推广,请确认!', 'info');
							}else if(sendObj =='4'){
								$.messager.alert("提示", '【' + companyName + '】暂无用户,不可进行短信推广,请确认!', 'info');
							}
						} else {
							if(data.code == '0') {
								if(data.result == 'SUCCESS') {
									$.messager.alert('提示', '短信推广成功!', 'info', function() {
										window.location.href = "/pc/message/sms.htm";
									});
								} else {
									$.messager.alert('提示', '短信推广失败!', 'info');
								}
							} else if(data.code == '1') {
								$.messager.alert('提示', '距离上次推广不到三天时间,暂不能进行推广,请稍后再试!', 'info');
							} else {
								$.messager.alert('提示', '短信推广失败!', 'info');
							}
						}
						$('#dvContainer').hideLoading();
					});
				}
			});
		}
	};
}();

$(function(){
	var companyName = $("#companyName").val();
	var content = "各位同事,[我局/我司]与移动合作开发了在手机上查询同事电话,来电显示同事名片的集团通讯录,个人免费使用.安卓手机下载  http://t.cn/z8G8Nho 【" + companyName + "】";
	$("#content").val(content);
});