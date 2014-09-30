<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/lookGroupBook/editEmp.js"></script>

<div class="openwarp">
		<form id="form_lookGroup" method="post" enctype="multipart/form-data"
				action="">
		<input id="userCompanyId" name="userCompanyId"  type="hidden" />
		<input id="companyId2" name="companyId"  type="hidden"  />
       <div class="openuser">
         <div class="f_l"><a href="#"><img src="/resources/images/open_pic.png" /></a></div>
         <div class="f_l"><span style="margin-left:28px">姓名：<input style="width:330px"  id="employeeName" name="employeeName" type="text" class="opcek3" /></span>
         <br />个性签名：<input style="margin-top:5px;width:330px"  id="mood" name="mood" type="text" class="opcek3" /></div>
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
            <td width="150" class="t_l"><input  type="text"  id="mobile" name="mobile" class="opcek3" /></td>
            <td width="100" class="t_r"> 手机短号：</td>
            <td class="t_l"><input  type="text" id="mobileShort" name="mobileShort"  class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">办公固话：</td>
            <td class="t_l"><input  type="text"  id="telephone2" name="telephone2" class="opcek3" /></td>
            <td class="t_r">办公短号：</td>
            <td class="t_l"><input   type="text" id="telShort2" name="telShort" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">部门：</td>
            <td colspan="3" class="t_l">
            <input id="departmentCombox"  name="departmentId" type="text" style="width: 426px" />
            <input id="lastDepartmentId"  type="hidden"/>
            <input id="departmentName2"  name="departmentName" type="hidden"/>
            </td>
           </tr>
          <tr>
            <td class="t_r">职位：</td>
            <td colspan="3" class="t_l">
            <input id="headshipCombox"   name="headshipId" type="text" style="width:426px"/>
		     <input id="headshipName2"  name="headshipName" type="hidden"/>
            </td>
           </tr>
          <tr>
            <td class="t_r">V网ID：</td>
            <td class="t_l"><input type="text"  id="gridNumber" name="gridNumber"  class="opcek3" /></td>
            <td class="t_r">部门显示顺序：</td>
            <td class="t_l">
            <input id="displayOrder"  name="displayOrder" type="hidden"  />
            <input type="text"  id="relativeOrder" name="relativeOrder" style="width: 160px" />
            <input id="relativeOrderHidden"  name="relativeOrderHidden" type="hidden" /></td>
          </tr>
          <tr>
            <td class="t_r">邮箱：</td>
            <td colspan="3" class="t_l"><input type="text"  id="email" name="email"  class="opcekbig" /></td>
           </tr>
          <tr>
            <td class="t_r">办公地址：</td>
            <td colspan="3" class="t_l"><input type="text"  id="address" name="address"  class="opcekbig" /></td>
           </tr>
           </thead>
           
           
           <tbody id="other" style="display: none">
           <tr>
            <td class="t_r" width="80">微博：</td>
            <td width="150" class="t_l"><input  id="weibo" name="weibo" type="text" class="opcek3" /></td>
            <td width="100" class="t_r"> 学校：</td>
            <td class="t_l"><input  id="school" name="school" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">QQ：</td>
            <td class="t_l"><input  id="qq" name="qq" type="text" class="opcek3" /></td>
            <td class="t_r">专业：</td>
            <td class="t_l"><input  id="userMajor" name="userMajor" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">微信：</td>
            <td class="t_l"><input id="weixin" name="weixin" type="text" class="opcek3" /></td>
            <td class="t_r"> 班级：</td>
            <td class="t_l"><input  id="userClass" name="userClass" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">年级：</td>
            <td class="t_l"><input  id="userGrade" name="userGrade" type="text" class="opcek3" /></td>
            <td class="t_r"> 学号：</td>
            <td class="t_l"><input  id="studentId" name="studentId" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">籍贯：</td>
            <td class="t_l"><input  id="nativePlace" name="nativePlace" type="text" class="opcek3" /></td>
            <td class="t_r">家庭电话：</td>
            <td class="t_l"><input  id="telephone" name="telephone" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">家庭住址：</td>
            <td colspan="3" class="t_l"><input id="homeAddress" name="homeAddress" type="text" class="opcekbig" /></td>
           </tr>
           </tbody>
        </table>
       </div>     
       </form>
   </div>
   <div class="opennav pd70">
       <a href="javascript:void(0)" onClick="$('#addEmployee').window('close');"><div class="f_l opbtns"><img src="/resources/images/qx.png" />取　消</div></a>
      <a href="javascript:editEmp.addUser()"><span class="f_l opbtns"><img src="/resources/images/opsave.png" />保存并退出</span></a>
   </div>
