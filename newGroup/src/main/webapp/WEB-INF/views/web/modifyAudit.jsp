<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">

$(function(){
	$.ajax({
		type: "POST",
		url: "/pc/userModify/get.htm",
		data: {'userModifyId' :'${userModifyId}'},
		dataType: "JSON",
		success: function(data){
			$("#userModifyId").val(data[0].user_modify_id);
			$("#employeeName").val(data[0].employee_name);
			$("#userCompany").val(data[0].user_company);
			$("#departmentName").val(data[0].department_name);
			$("#headshipName").val(data[0].headship_name);
			$("#mobile").val(data[0].mobile);
			$("#mobileShort").val(data[0].mobile_short);
			$("#telephone2").val(data[0].telephone2);
			$("#home_telephone").val(data[0].telephone);
			$("#telShort").val(data[0].tel_short);
			$("#weibo").val(data[0].weibo);
			$("#weixin").val(data[0].weixin);
			$("#school").val(data[0].school);
			$("#userMajor").val(data[0].user_major);
			$("#userGrade").val(data[0].user_grade);
			$("#userClass").val(data[0].user_class);
			$("#studentId").val(data[0].student_id);
			$("#email").val(data[0].email);
			$("#qq").val(data[0].qq);
			$("#nativePlace").val(data[0].native_place);
			$("#address").val(data[0].address);
			$("#homeAddress").val(data[0].home_address);
			$("#remark").val(data[0].remark);
			$("#mood").val(data[0].mood);
			$("#employeeName2").val(data[0].employee_name2);
			$("#userCompany2").val(data[0].user_company2);
			$("#departmentName2").val(data[0].department_name2);
			$("#headshipName2").val(data[0].headship_name2);
			$("#mobile2").val(data[0].mobile2);
			$("#mobileShort2").val(data[0].mobile_short2);
			$("#telephone22").val(data[0].telephone22);
			$("#home_telephone2").val(data[0].home_telephone2);
			$("#telShort2").val(data[0].tel_short2);
			$("#weibo2").val(data[0].weibo2);
			$("#weixin2").val(data[0].weixin2);
			$("#school2").val(data[0].school2);
			$("#userMajor2").val(data[0].user_major2);
			$("#userGrade2").val(data[0].user_grade2);
			$("#userClass2").val(data[0].user_class2);
			$("#studentId2").val(data[0].student_id2);
			$("#email2").val(data[0].email2);
			$("#qq2").val(data[0].qq2);
			$("#nativePlace2").val(data[0].native_place2);
			$("#address2").val(data[0].address2);
			$("#homeAddress2").val(data[0].home_address2);
			$("#remark2").val(data[0].remark2);
			$("#mood2").val(data[0].mood2);
		}
	});
});
</script>
 <div class="openwarp"  >
       <div class="opentabs" >
       			<form id="form_modify" method="post" action="">
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr class="t_r">
						<td width="100px"></td>
						<td  align="center">修改前</td>
						<td  align="center">修改后</td>
					</tr>
					<tr class="t_r">
						<td class="t_l">姓名：</td>
						<td align="center">
							<input id="employeeName" name="employeeName" type="text" size="19" class="opcek4"  readonly="readonly" />
						</td>
						<td align="center">
							<input id="employeeName2" type="text" size="19" class="opcek4"  readonly="readonly"   />
							<input type="hidden" id="userModifyId"/>
						</td>
					</tr>
					<tr class="t_r">
						<td class="t_l">单位：</td>
						<td align="center">
							<input id="userCompany" name="userCompany" type="text" size="19" class="opcek4" readonly="readonly"   />
						</td>
						<td align="center">
							<input id="userCompany2" type="text" size="19" class="opcek4" readonly="readonly"   />
						</td>
					</tr>
					<tr class="t_r">
						<td class="t_l">所属部门：</td>
						<td align="center"><input id="departmentName" name="departmentName" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="departmentName2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">职位：</td>
						<td align="center"><input id="headshipName" name="headshipName" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="headshipName2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">手机长号：</td>
						<td align="center"><input id="mobile" name="mobile" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="mobile2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">手机短号：</td>
						<td align="center"><input id="mobileShort" name="mobileShort" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="mobileShort2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">办公室长号：</td>
						<td align="center"><input id="telephone2" name="telephone2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="telephone22" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">办公室短号：</td>
						<td align="center"><input id="telShort" name="telShort" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="telShort2"  type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">微博：</td>
						<td align="center"><input id="weibo" name="weibo" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="weibo2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">微信：</td>
						<td align="center"><input id="weixin" name="weixin" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="weixin2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">学校：</td>
						<td align="center"><input id="school" name="school" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="school2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">专业：</td>
						<td align="center"><input id="userMajor" name="userMajor" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="userMajor2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">年级：</td>
						<td align="center"><input id="userGrade" name="userGrade" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="userGrade2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">班级：</td>
						<td align="center"><input id="userClass" name="userClass" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="userClass2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">学号：</td>
						<td align="center"><input id="studentId" name="studentId" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="studentId2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">籍贯：</td>
						<td align="center"><input id="nativePlace" name="nativePlace" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="nativePlace2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">常住地址：</td>
						<td align="center"><input id="address" name="address" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="address2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">家庭地址：</td>
						<td align="center"><input id="homeAddress" name="homeAddress" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="homeAddress2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">宅电：</td>
						<td align="center"><input id="home_telephone" name="telephone" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="home_telephone2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">QQ：</td>
						<td align="center"><input id="qq" name="qq" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="qq2" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">显示顺序：</td>
						<td align="center"><input id="displayOrder" name="displayOrder" type="text" size="19" class="opcek4" readonly="readonly"   /></td>
						<td align="center"><input id="displayOrder2" type="text" size="19" class="opcek4" readonly="readonly" /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">备注：</td>
						<td align="center"><input id="remark" name="remark" type="text" size="19" class="opcek4" readonly="readonly"  /></td>
						<td align="center"><input id="remark2"  type="text" size="19" class="opcek4" readonly="readonly"  /></td>
					</tr>
					<tr class="t_r">
						<td class="t_l">心情：</td>
						<td align="center"><input id="mood" name="mood" type="text" size="19" class="opcek4" readonly="readonly"  /></td>
						<td align="center"><input id="mood2"  type="text" size="19" class="opcek4" readonly="readonly"  /></td>
					</tr>
				</table>
			</form>
       </div>
       <div class="openbtn">
         <a href="#" onclick="Modify.cancelModify();"><span class="f_l opbtns"><img src="/resources/images/qx.png" />取消</span></a>
        <a href="#" onclick="Modify.saveModify();"> <span class="f_l opbtns"><img src="/resources/images/opsave.png" />同意</span></a>
       <a href="#" onclick="Modify.refuseModify();"> <span class="f_l opbtns"><img src="/resources/images/opsave.png" />拒绝</span></a>
       </div>
   </div>
