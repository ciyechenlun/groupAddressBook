NoticeFir = function(){
	return {
	
		nextStep : function(){
			var publicId = $("em:visible").attr('value');
			var userCompanyId = $("em:visible").attr('text');
			if(publicId){
				window.location.href="/pc/notice/newNoticeMain.htm?publicId="+publicId+"&userCompanyId="+userCompanyId;
			}else{
				$.messager.alert("提示","请选择公告号","info");
			}
		}
	};
}();
$(function(){	
	$("li").click(function(){
		$(this).parent().find("em").addClass("hide");
		$(this).find("em").removeClass("hide");
	});
});