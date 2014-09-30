<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@include file="../../common/baseIncludeJs.jsp"%>
	<link  type="text/css" rel="stylesheet" href="/resources/js/common/showLoading/css/showLoading.css"/>
	<!-- <script type="text/javascript" src="/resources/js/sysmanage/company/company.js"></script> -->
	<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.js"></script>
	<script type="text/javascript" src="/resources/js/common/showLoading/js/jquery.showLoading.min.js"></script>

</head>

<body>
 <div class="nowbg">您当前置位 -- 系统维护 -- 企业维护</div>        
        <div class="listmenu f_r">
          <div class="tab_btn"><a href="#"><img src="/resources/images/btn_tjbm.png" />添加</a></div>
          <div class="tab_btn"><a href="#"><img src="/resources/images/btn_bj.png" />修改</a></div>
          <div class="tab_btn"><a href="#"><img src="/resources/images/btn_sccy.png" />删除</a></div>
          <div class="tab_btn"><a href="#"><img src="/resources/images/btn_sx.png" />刷新</a></div>
          <div class="f_r lever_search">
             <table border="0" cellspacing="0" cellpadding="0">
               <tr>
                <td><input name="" type="text" /></td>
                <td><a href="#"><img src="/resources/images/btn_search.gif" /></a></td>
               </tr>
             </table>     
          </div>
        </div>
        <div class="tabsI" >          
          <table width="100%" border="0" cellpadding="0" cellspacing="0" >
              <tr>
                <th width="50">序号</th>
                <th width="60">&nbsp;</th>
                <th>企业编号</th>
                <th>企业名称</th>
                <th width="200">联系人</th>
                <th>联系电话</th>
                <th>企业标识</th>
              </tr>
              <c:forEach items="${list}" var="item" varStatus="s">
              <tr>
                <td align="center">${s.index + 1 }</td>
                <td align="center"><input type="checkbox" name="checkbox" id="checkbox" />
                  <label for="checkbox"></label></td>
                <td align="center">${item.company_code}</td>
                <td align="center">${item.company_name}</td>
                <td align="center">${item.contact_man}</td>
                <td align="center">${item.telephone}</td>
                <td align="center">
               <c:if test="${item.index_logo !=''&& item.index_logo!=null }">
				   <div><a href="javascript:Company.photo(${item.index_logo})"><font color='blue'>查看</font></a></div>
				</c:if>
                </td>
              </tr>
              </c:forEach>
           </table>
        </div> 
</body>
</html>