<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<script type="text/javascript" src="/resources/js/lookGroupBook/editEmp.js"></script>
<style type="text/css">

.opbtns2{margin:8px}
.comp_a_click{
	color:red !important;
	background:url(/resources/images/lis_btnbg2.png) no-repeat !important;
	display:block !important;
	padding-left:10px !important; 
}
.dept_a_click{
	color:blue !important;
	background:url(/resources/images/lis_btnbg2.png) no-repeat !important;
	display:block !important;
	padding-left:10px !important; 
}
</style>
<div class="openwarp">
		<form id="form_lookGroup" method="post" enctype="multipart/form-data"
				action="">
		<input id="userCompanyId" name="userCompanyId" value="${empInfo.user_company_id}" type="hidden" />
		<input id="companyId2" name="companyId" value="${empInfo.company_id}" type="hidden"  />
		<input id="manage_flag" name="manage_flag" value="${empInfo.manage_flag}" type="hidden" />
		<input id="manage_dept_id" name="manage_dept_id" value="${manage_dept_id}" type="hidden" />
       <div class="openuser">
       	 <div class="f_l"><a href="#"><img src="/resources/images/open_pic.png" /></a></div>
         <div class="f_l"><span style="margin-left:28px">姓名：<input style="width:330px" value="${empInfo.employee_name}"  id="employeeName" name="employeeName" type="text" class="opcek3" /></span>
         <br />个性签名：<input style="margin-top:5px;width:330px"  id="mood" name="mood" type="text" class="opcek3"  value="${empInfo.mood }"/></div>
       </div>
       <div class="openbq">
         <ul>
           <li id="important_li" class="f_l openbg_on"><span id="important_span" class="openbqa"><a href="javascript:editEmp.changeToImportant()">重要信息</a></span></li>
           <li id="other_li" class="f_l openbg_out"><span id="other_span" class="openbqb"><a href="javascript:editEmp.changeToOther()">其他信息</a></span></li>
         </ul>
       </div>
       <div class="opentabs">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <thead id="important">
          <tr>
            <td class="t_r" width="80">手机长号：</td>
            <td width="150" class="t_l"><input value="${empInfo.mobile}" type="text"  id="mobile" name="mobile" class="opcek3" /></td>
            <td width="100" class="t_r"> 手机短号：</td>
            <td class="t_l"><input value="${empInfo.mobile_short}" type="text" id="mobileShort" name="mobileShort"  class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">办公固话：</td>
            <td class="t_l"><input value="${empInfo.telephone2}"  type="text"  id="telephone2" name="telephone2" class="opcek3" /></td>
            <td class="t_r">办公短号：</td>
            <td class="t_l"><input value="${empInfo.tel_short}"  type="text" id="telShort2" name="telShort" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">部门：</td>
            <td colspan="3" class="t_l">
            <input  id="departmentCombox" value="${empInfo.department_id}"  name="departmentId" type="text" style="width: 426px" />
            <input id="lastDepartmentId" value="${empInfo.department_id}" type="hidden"/>
            <input id="departmentName2" value="${empInfo.department_name}" name="departmentName" type="hidden"/>
            </td>
           </tr>
          <tr>
            <td class="t_r">职位：</td>
            <td colspan="3" class="t_l">
            <input id="headshipCombox" value="${empInfo.headship_id}"  name="headshipId" type="text" style="width:426px"/>
		     <input id="headshipName2" value="${empInfo.headship_name}" name="headshipName" type="hidden"/>
            </td>
           </tr>
          <tr>
            <td class="t_r">V网ID：</td>
            <td class="t_l"><input type="text" value="${empInfo.grid_number}" id="gridNumber" name="gridNumber"  class="opcek3" /></td>
            <td class="t_r">部门显示顺序：</td>
            <td class="t_l">
            <input id="displayOrder" value="${empInfo.display_order}" name="displayOrder" type="hidden"  />
            <input type="text" value="<fmt:formatNumber type="number" value="${empInfo.row}" maxFractionDigits="0"/>" id="relativeOrder" name="relativeOrder" style="width: 160px" />
            <input id="relativeOrderHidden" value="<fmt:formatNumber type="number" value="${empInfo.row}" maxFractionDigits="0"/>" name="relativeOrderHidden" type="hidden" /></td>
          </tr>
          <tr>
            <td class="t_r">邮箱：</td>
            <td colspan="3" class="t_l"><input type="text" value="${empInfo.email}" id="email" name="email"  class="opcekbig" /></td>
           </tr>
          <tr>
            <td class="t_r">办公地址：</td>
            <td colspan="3" class="t_l"><input type="text" value="${empInfo.address}" id="address" name="address"  class="opcekbig" /></td>
           </tr>
           </thead>
           
           
           <tbody id="other" style="display: none">
           <tr>
            <td class="t_r" width="80">微博：</td>
            <td width="150" class="t_l"><input value="${empInfo.weibo}" id="weibo" name="weibo" type="text" class="opcek3" /></td>
            <td width="100" class="t_r"> 学校：</td>
            <td class="t_l"><input value="${empInfo.school}" id="school" name="school" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">QQ：</td>
            <td class="t_l"><input value="${empInfo.qq}" id="qq" name="qq" type="text" class="opcek3" /></td>
            <td class="t_r">专业：</td>
            <td class="t_l"><input value="${empInfo.user_major}" id="userMajor" name="userMajor" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">微信：</td>
            <td class="t_l"><input value="${empInfo.weixin}" id="weixin" name="weixin" type="text" class="opcek3" /></td>
            <td class="t_r"> 班级：</td>
            <td class="t_l"><input value="${empInfo.user_class}" id="userClass" name="userClass" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">年级：</td>
            <td class="t_l"><input value="${empInfo.user_grade}" id="userGrade" name="userGrade" type="text" class="opcek3" /></td>
            <td class="t_r"> 学号：</td>
            <td class="t_l"><input value="${empInfo.student_id}" id="studentId" name="studentId" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">籍贯：</td>
            <td class="t_l"><input value="${empInfo.native_place}" id="nativePlace" name="nativePlace" type="text" class="opcek3" /></td>
            <td class="t_r">家庭电话：</td>
            <td class="t_l"><input value="${empInfo.telephone}" id="telephone" name="telephone" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">家庭住址：</td>
            <td colspan="3" class="t_l"><input value="${empInfo.home_address}" id="homeAddress" name="homeAddress" type="text" class="opcekbig" /></td>
           </tr>
           </tbody>
        </table>
       </div>     
       </form>
   </div>
   <div class="opennav">
      <div class="f_l listmenu">
        <div id="comp_man_div" style="display:none" class="lis_btn"><a href="#" onclick="editEmp.setManagerComp()"><img src="/resources/images/btn_cyjy.png" />企业管理员</a></div>
        <div id="dept_man_div" style="display:none" class="lis_btn"><a href="#" onclick="editEmp.setManagerDept('${empInfo.employee_id}','${empInfo.company_id}')"><img src="/resources/images/btn_cyjy.png" />部门管理员</a></div>
        <div class="lis_btn"><a href="javascript:editEmp.deleteUser('${empInfo.user_company_id}','${empInfo.department_id}')"><img src="/resources/images/btn_sccy.png" />删除成员</a></div>
      </div>
      <a href="javascript:editEmp.saveUser()"><span class="f_l opbtns2"><img src="/resources/images/opsave.png" />保存并退出</span></a>
   </div>