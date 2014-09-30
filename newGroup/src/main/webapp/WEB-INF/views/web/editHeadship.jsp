<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/web/editHeadship.js"></script>
   <div class="openwarp">
     <div class="optab">
     	<form id="hs_form" method="post" enctype="multipart/form-data" action="">
       <table id="addHeadship" width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <th width="30" align="center">&nbsp;</th>
                <th>职位名</th>
                <th>职位级别</th>
                <th>职位描述</th>
              </tr>
              <tr>
                <td align="center">1<input type="hidden" name="headshipId" id="headshipId" value="${headship.headshipId}"/></td>
                <td><input id="headshipName" name="headshipName" value="${headship.headshipName}" type="text" class="opand" value="" size="10" /></td>
                <td><input id="headshipLevel" name="headshipLevel" value="${headship.headshipLevel}" type="text" class="opand" value="" size="15" /></td>
                <td><input id="description" name="description" value="${headship.description}" type="text" class="opand" value="" size="40" /></td>
              </tr>
           </table>
           </form>
     </div>   
     <div class="fulltd" style="height:50px; display:block; overflow:hidden">
      <a href="javascript:void(0)" onclick="$('#headshipWin').window('close')"><span class="f_r opbtns"><img src="/resources/images/qx.png" />取　消</span></a>
      <a href="#" onclick="editHeadship.updateHeadship()"><span class="f_r opbtns"><img src="/resources/images/bc.png" />保　存</span></a>
   </div>  
   </div>
