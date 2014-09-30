<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script src="/resources/scripts/jquery.autocomplete.js" type="text/javascript"></script>
<script type="text/javascript">
	function addText(){
		var text = document.getElementById("textarea").value;
		if(text==null||$.trim(text).length==0){
			document.getElementById("textarea").value="此处填写回复信息";
		}
	}
	
	function clearText(){
		var text = document.getElementById("textarea").value;
		if($.trim(text)=="此处填写回复信息"){
			document.getElementById("textarea").value="";
		}
	}
</script>

   <div class="openwarp2"  style="overflow:auto;height:420px;">
   	 <div style=" text-align:left;line-height:30px;font-size:14px;color:#5c8495;height:30px;margin-left:10px;" >
   	 反馈人：${adviseMan}
   	 </div>
   	  <div style=" text-align:left;line-height:30px;font-size:14px;color:#5c8495;margin-left:10px;" >
   	 单位：${company_name}
   	 </div>
   	  <div style=" text-align:left;line-height:30px;font-size:14px;color:#5c8495;height:30px;margin-left:10px;" >
   	 手机号码：${mobile}</div>
      <div class="open_title">反馈信息</div>
      <div class="open_old">${content }</div>
      <div class="open_title">回复内容</div>
      <div class="open_old2"><textarea style="width:95%;height:90px;resize:none;"name="textarea" cols="45" rows="5" class="oldcek" id="textarea"onblur="addText();" onfocus="clearText();">此处填写回复信息
      </textarea></div>
      <div class="f_r opbtns"><img src="/resources/images/fsdx.png" />发送回复</div>
   </div>   

