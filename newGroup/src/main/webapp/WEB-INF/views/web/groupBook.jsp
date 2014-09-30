<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
<script type="text/javascript" src="/resources/js/common/common.js"></script>
<script type="text/javascript" src="/resources/js/lookGroupBook/lookGroupBook.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery.dragsort-0.5.1.min.js"></script>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>
<style type="text/css">
.nodata{
color: #8BA3AA;
font-size: 20px;
padding-top: 200px;
text-align: center;
}
.combo-text{
 font-size: 15px;
 height: 30px;
 color: #313C50;
 font-family: "微软雅黑";
}
.combo {
    border: 1px solid #E9E9E9;
}
.combo-arrow {
    background: url("/resources/scripts/easyui/themes/default/images/combo_arrow.gif") no-repeat scroll 7px 8px #E9E9E9;
    cursor: pointer;
    display: inline-block;
    height: 30px;
    opacity: 0.6;
    overflow: hidden;
    vertical-align: top;
    width: 25px;
}
.combobox-item {
   color: #313C50;
    font-family: "微软雅黑";
    font-size: 15px;
    padding: 5px 0 3px 3px;
}
.combo-panel {
   border: 1px solid #E9E9E9;
}
.combobox-item-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
.placeHolder div { background-color:white !important; border:dashed 1px gray !important; }
.lpart{width:201px;display:block;height:44px; background:url(/resources/images/listbg2.png) no-repeat;text-align:center; line-height:44px;margin:5px 0 0 10px;color:#607a83;font-size:14px;font-weight:bold}
.cover_div{display:none;position: absolute; top:  0%; left:0%; width: 100%; height: 100%;background-color: white; z-index:1002;  -moz-opacity: 0.7;opacity:.70; filter: alpha(opacity=70);overflow: auto;}
</style>
<script language="JavaScript">

	 	$(function() {
		//如果不是s_admin&&查看的是企业组织，提示没有权限
		if('${org_flag}' == '1' && '${manager}' == '')
		{
			$("#dvCover").show();
			//$.messager.alert('提示','你没有权限执行此操作','info',function(){history.back();});
			
		}
		
	}); 

</script>
</head>

<body id="bookMask">
	<div id="dvCover" class="cover_div">
	</div>
	<form id="order_form" method="post" action="">
		<input name="list1SortOrder" type="hidden" />
		<input id="manageFlag" name="manageFlag" value="${manageFlag}" type="hidden" />
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="4" class="rignav">
              <div class="listmenu f_r">
              <c:if test="${user.username=='s_admin'}">
              <div class="lis_btn"><a href="#" onclick="LookGroup.clearGroup()"><img src="/resources/images/btn_plsc.png" />清空通讯录</a></div>
               </c:if>
                <div class="lis_btn"><a href="#" onclick="LookGroup.batchDelete()"><img src="/resources/images/btn_plsc.png" />批量删除</a></div>
                <div class="lis_btn"><a href="javascript:void(0)" onclick="LookGroup.toAddEmployee()"><img src="/resources/images/btn_tjcy.png" />添加成员</a></div>
                <div class="lis_btn_big"><a href="#" onclick="LookGroup.toBatchAddEmp()"><img src="/resources/images/btn_pltj.png" />批量添加成员</a></div>
                <div class="f_r"><a href="#" onclick="LookGroup.saveOrder()"><img src="/resources/images/btn_bc.png" /></a></div>
              </div>
            </td>
          </tr>
          <tr>
            <td class="rignav">
              <div  class="lnav">
                <div>
                <input id="firstDepartment" value="--请选择--" name="firstDepartment" type="text" style="width: 230px"  />
				</div>
                  <div id="first" class="scrolls">
                  <c:if test="${manageFlag ne '3'}">
                    <c:forEach items="${seDepList.result}" var="result">
		                  <div title="${result.department_name}">
		                    	<a name="dept_a" href="javascript:LookGroup.getDeptByPId('${result.company_id}','${result.department_id}','${result.department_level+1 }','1','second',true)"  class="lpart">
		                    		<c:choose>
										<c:when test="${fn:length(result.department_name)>12}">
											<c:out value ="${fn:substring(result.department_name,0,11)}..." />
										</c:when>
										<c:otherwise>
											<c:out value="${result.department_name}"/>
										</c:otherwise>
									</c:choose>
		                    	</a>
		                   </div>
              	    </c:forEach>
              	    <c:if test="${fn:length(seDepList.result)>=10 }">
              	 <div style="cursor:pointer" onclick="javascript:LookGroup.getDeptByPId('${seDepList.result[0].company_id}','','${seDepList.result[0].department_level}','2','first')" class="pages"></div>
              	 </c:if>
              	 </c:if>
                  </div>
              </div>
            </td>
            <td class="rignav"><div  class="lnav">
              <div class="lselcet">
                  <input id="secondDepartment" value="--请选择--" name="secondDepartment" type="text" style="width: 230px"  />
              </div>
              <div id="second" class="scrolls">
              <div class="nodata">【请选择三级部门】</div>
              </div>
            </div></td>
            <td class="rignav"><div  class="lnav">
              <div class="lselcet">
                  <input id="thirdDepartment" value="--请选择--" name="thirdDepartment" type="text" style="width: 230px"  />

              </div>
              <div id="third" class="scrolls">
					<div class="nodata">【请选择四级部门】</div>
              </div>
            </div></td>
            <td class="rignav"><div  class="lnav">
              <div class="lselcet">
                 <input id="fourthDepartment" value="--请选择--" name="fourthDepartment" type="text" style="width: 230px"  />
              </div>
              <div id="fourth" class="scrolls">
                <!--单独一个：start-->
                <div class="nodata">【请选择五级部门】</div>
              </div>
            </div></td>
          </tr>          
        </table>
        </form>
        <div id="addEmployee"></div>
        <div id="setManager">
        	<ul id="man_tree" style="margin-left:30px;margin-top:20px"></ul>
        </div>
</body>
</html>
