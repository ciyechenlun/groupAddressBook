<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录部门维护</title>
<%@include file="../common/baseIncludeJs.jsp" %>
<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
<script type="text/javascript" src="/resources/scripts/jquery.dragsort-0.5.1.min.js"></script>
<style type="text/css">
.nodata{
color: #8BA3AA;
font-size: 20px;
padding-top: 200px;
text-align: center;
}
a:link, a:visited, a:active {
    color: #000000;
    text-decoration: none;
}
.f_l {float:left;width:130px;overflow: hidden;white-space:nowrap;text-overflow:ellipsis;}
.placeHolder div { background-color:white !important; border:dashed 1px gray !important; }
.dept-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
</style>
</head>

<body id="bodyDv">
<input id="manageFlag" name="manageFlag" value="${manageFlag}" type="hidden" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td colspan="4" class="rignav">
          <div class="listmenu f_r">
            <div class="lis_btn"><a href="#" onclick="DeptMag.batchDelete()"><img src="/resources/images/btn_plsc.png" />批量删除</a></div>
            <div class="lis_btn"><a href="javascript:void(0)" onclick="DeptMag.toAddDept()"><img src="/resources/images/btn_tjbm.png" />添加部门</a></div>
            <div class="f_r"><a href="javascript:void(0)" onclick="DeptMag.saveOrder()"><img src="/resources/images/btn_bc.png" /></a></div>
          </div>
        </td>
      </tr>
      <tr>
        <td class="rignav">
        	<div style="font-size: 17px;text-align: center ;color: #313C50;font-family: '微软雅黑'">
            	二级部门
			</div>
          <div  class="lnav">
            
              <div id="1" class="scrolls">
              <c:if test="${manageFlag ne '3'}">
              	 <c:forEach items="${seDepList.result}" var="result">
              	 	<div class="lname">
                  <div title="${result.department_name}" class="f_l lna">
                    <input type="checkbox" name="checkbox" value="${result.department_id}"/>
                    	<a href="javascript:DeptMag.editDepartment('${result.department_id}')"><img title="编辑" src="/resources/images/list_edit.png" /></a>
                    	<a name="dept_a" href="javascript:DeptMag.getDeptByPId('${result.company_id}','${result.department_id}','${result.department_level+1 }','1','${result.department_level+1 }')">${result.department_name}</a>
                    </div>
                  <div class="f_r lclose"> <a href="javascript:DeptMag.del('${result.department_id}')"><img src="/resources/images/list_close.png" /></a> </div>
                </div>
              	 </c:forEach>
              	 <c:if test="${fn:length(seDepList.result)>=10 }">
              	 <div style="cursor:pointer" onclick="javascript:DeptMag.getDeptByPId('${seDepList.result[0].company_id}','','${seDepList.result[0].department_level}','2','${seDepList.result[0].department_level}')" class="pages"></div>
              	 </c:if>
              	 </c:if>
              </div>
          </div>
        </td>
        <td class="rignav">
        <div style="font-size: 17px;text-align: center ;color: #313C50;font-family: '微软雅黑'">
            	三级部门
			</div>
        <div  class="lnav">
          <div id="2" class="scrolls">
          <%--  <c:forEach items="${thDepList.result}" var="result">
              	 	<div class="lname">
                  <div  title="${result.department_name}" class="f_l lna">
                    <input type="checkbox" />
                    <a href="javascript:DeptMag.editDepartment('${result.department_id}')"><img title="编辑" src="/resources/images/list_edit.png" /></a>
                    	<a href="javascript:DeptMag.getDeptByPId('${result.company_id}','${result.department_id}','${result.department_level+1 }','1','${result.department_level+1 }')">${result.department_name}</a> </div>
                  <div class="f_r lclose"> <a href="javascript:DeptMag.del('${result.department_id}')"><img src="/resources/images/list_close.png" /></a> </div>
                </div>
              	 </c:forEach>
              	 <c:if test="${fn:length(thDepList.result)>=10 }">
              	 <a href="javascript:DeptMag.getDeptByPId('${thDepList.result[0].company_id}','','${thDepList.result[0].department_level}','2','${thDepList.result[0].department_level}')">
              	 <div class="pages"></div>
              	 </a>
              	 </c:if> --%>
              	 <div class="nodata">【请选择二级部门】</div>
          </div>
        </div></td>
        <td class="rignav">
        <div style="font-size: 17px;text-align: center ;color: #313C50;font-family: '微软雅黑'">
            	四级部门
			</div>
        <div  class="lnav">

          <div id="3" class="scrolls">
 			<%-- <c:forEach items="${foDepList.result}" var="result">
              	 	<div class="lname">
                  <div  title="${result.department_name}" class="f_l lna">
                    <input type="checkbox" />
                    <a href="javascript:DeptMag.editDepartment('${result.department_id}')"><img title="编辑" src="/resources/images/list_edit.png" /></a> 
                    	<a href="javascript:DeptMag.getDeptByPId('${result.company_id}','${result.department_id}','${result.department_level+1 }','1','${result.department_level+1 }')">${result.department_name}</a></div>
                  <div class="f_r lclose"> <a href="javascript:DeptMag.del('${result.department_id}')"><img src="/resources/images/list_close.png" /></a> </div>
                </div>
              	 </c:forEach>
              	 <c:if test="${fn:length(foDepList.result)>=10 }">
              	 <a href="javascript:DeptMag.getDeptByPId('${foDepList.result[0].company_id}','','${foDepList.result[0].department_level}','2','${foDepList.result[0].department_level}')">
              	 <div class="pages"></div>
              	 </a>
              	 </c:if> --%>
              	 <div class="nodata">【请选择三级部门】</div>
          </div>
        </div></td>
        <td class="rignav">
        <div style="font-size: 17px;text-align: center ;color: #313C50;font-family: '微软雅黑'">
            	五级部门
			</div>
        <div  class="lnav">

          <div id="4" class="scrolls">
            <%--  <c:forEach items="${fiDepList.result}" var="result">
              	 	<div class="lname">
                  <div  title="${result.department_name}" class="f_l lna">
                    <input type="checkbox" />
                    <a href="javascript:DeptMag.editDepartment('${result.department_id}')"><img title="编辑" src="/resources/images/list_edit.png" /></a> 
                    	${result.department_name}</div>
                  <div class="f_r lclose"> <a href="javascript:DeptMag.del('${result.department_id}')"><img src="/resources/images/list_close.png" /></a> </div>
                </div>
              	 </c:forEach>
              	 <c:if test="${fn:length(fiDepList.result)>=10 }">
              	 <a href="javascript:DeptMag.getDeptByPId('${fiDepList.result[0].company_id}','','${fiDepList.result[0].department_level}','2','${fiDepList.result[0].department_level}')">
              	 <div class="pages"></div>
              	 </a>
              	 </c:if> --%>
              	 <div class="nodata">【请选择四级部门】</div>
          </div>
        </div></td>
      </tr>          
    </table>
    <div id="deptWin"></div>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>
<script type="text/javascript" src="/resources/js/web/deptMag.js"></script>
</body>
</html>
