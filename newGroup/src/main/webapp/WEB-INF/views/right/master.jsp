<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<script language="JavaScript">
$(function(){
	$('#selReleation').numberbox({
		min:0
	});
});
	//保存权限设置
		function saveRight()
	{
		
		//提交表单
		$('#form1').form('submit',{
			url:"/pc/right/addMasterRule.htm",
			success:function(data){
				if(data=='SUCCESS'){
					$.messager.alert('提示','保存成功','info',function(){
						window.location.href = "/pc/right/getMasterRule.htm";					
					});
				}else {
					$.messager.alert('提示','保存失败','error');	
				}
			}
		});
	}
		//删除权限设置
		function delSubmit()
	{
		$.messager.confirm('提示','确定删除吗？',function(ok){
			if(ok){
				$.ajax({
					type:"POST",
					url:"/pc/right/delMasterRule.htm?txtMasterRulesId="+$('#txtMasterRulesId').val(),
					success:function(msg)
					{
						if(msg == 'SUCCESS')
						{
							window.location.href = "/pc/right/getMasterRule.htm";			
						}
						else
						{
							$.messager.alert('提示','删除失败','error');	
						}
					}
				});
			}
		});
	}
</script>
</head>

<body>
 <div class="nowbg">您当前位置 -- 权限管理</div>
        <div class="qxred">权限规则设定/编辑</div>
        <div class="drwj2">
        <form id="form1" action="" method="post">
			<input type="hidden" name="companyId" id="companyId" value="${companyId}" />
			<input type="hidden" name="txtMasterId" id="txtMasterId" value="${masterRule.master_id }" />
			<input type="hidden" name="txtMasterRulesId" id="txtMasterRulesId" value="${masterRule.rules_id}" />
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="90">我希望我的单位通讯录里，每个人只能看到岗位级别比自己高<input class="opcekbig" id="selReleation" name="selReleation" value="${masterRule.rules_sql}"  type="text" style="width: 50px"  />级的成员。</td>
              </tr>
              <tr>
                <td style="padding-left:110px"><a href="#" onclick="delSubmit();"><span class="f_l opbtns"><img src="/resources/images/qx.png" />删除</span></a>
                <a href="#" onclick="saveRight();"><span class="f_l opbtns"><img src="/resources/images/bc.png" />保　存</span></a></td>
              </tr>
          </table>
           </form>
      </div>
</body>
</html>
