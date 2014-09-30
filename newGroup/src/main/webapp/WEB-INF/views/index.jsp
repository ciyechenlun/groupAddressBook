<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="common/baseIncludeJs.jsp" %>
 <script type="text/javascript" src="/resources/js/selectmenu.js"></script>
</head>

<body class="menu_body">
<!--top-->
<%@include file="top.jsp" %>

<!--body-->
<div class="mid">
 <input type="hidden" value="${manager }" id="manager" />
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="navtd">
        <div class="menu" id="nav">
            <ul> 
            <c:if test="${manager=='1' || manager=='2' }">
                <li><a href="#" title="" id="curMenu" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/lookGroup/forward.htm?companyId=${companyId }');"><span><img src="/resources/images/navz.png" />查看通讯录</span></a></li>
                
                <c:if test="${manager=='1'}">
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/import/main.htm');"><span><img src="/resources/images/nava.png" />导入通讯录</span></a></li>
                
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/right/getMasterRule.htm');"><span><img src="/resources/images/navc.png" />权限管理</span></a></li>
                </c:if>
                 <c:if test="${manager=='1'}">
                 <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/userModify/list.htm?companyId=${companyId }');"><span><img src="/resources/images/navd.png" />审核修改</span></a></li>
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/message/sms.htm');"><span><img src="/resources/images/nave.png" />短信推广</span></a></li> 
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/recycle/getDelElement.htm');"><span><img src="/resources/images/navf.png" />回收站</span></a></li> 
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/pheadship/main.htm');"><span><img src="/resources/images/navb.png" />职位管理</span></a></li>
                 <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/userManager/main.htm');"><span><img src="/resources/images/navb.png" />成员管理</span></a></li>
                 </c:if>
                <%-- <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/lookGroup/forward.htm?companyId=${companyId }');"><span><img src="/resources/images/navg.png" />用户管理</span></a></li>  --%>
                <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/deptMag/main.htm');"><span><img src="/resources/images/navg.png" />部门管理</span></a></li>  
                </c:if>
                 <c:if test="${user.username=='s_admin' || publicMan eq true }">
                  <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/publicRoadManager/main.htm?manager=${manager}');"><span><img src="/resources/images/navg.png" />公告管理</span></a></li> 
                 </c:if>
                <c:if test="${publicMan eq true }">
                  <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/notice/main.htm');"><span><img src="/resources/images/navg.png" />新公告</span></a></li> 
          			 <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/publicRoadManager/toDraftbox.htm');"><span><img src="/resources/images/navg.png" />草稿箱</span></a></li> 
          			 <li><a href="#" title="" onclick="changeId('curMenu',this);javascript:$('#iFrame').attr('src','/pc/publicRoadManager/toHistory.htm');"><span><img src="/resources/images/navg.png" />历史公告</span></a></li> 
          		</c:if>
          </ul>
        </div>
      </td>
      <td>
		<div style="height:700px" >
		  <iframe id="iFrame" src="/pc/lookGroup/forward.htm?companyId=${companyId }" width="100%" height="100%" frameborder="0" scrolling="no"/>
		  </div>
      </td>
    </tr>
  </table>
</div>

</body>
</html>
