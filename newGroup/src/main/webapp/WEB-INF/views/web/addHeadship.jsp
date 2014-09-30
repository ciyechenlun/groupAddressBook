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
                <td align="center">1</td>
                <td><input name="headshipName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="headshipLevel" type="text" class="opand" value="" size="15" /></td>
                <td><input name="description" type="text" class="opand" value="" size="40" /></td>
              </tr>
              <tr>
                <td align="center">2</td>
                <td><input name="headshipName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="headshipLevel" type="text" class="opand" value="" size="15" /></td>
                <td><input name="description" type="text" class="opand" value="" size="40" /></td>
              </tr>
              <tr>
                <td align="center">3</td>
                 <td><input name="headshipName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="headshipLevel" type="text" class="opand" value="" size="15" /></td>
                <td><input name="description" type="text" class="opand" value="" size="40" /></td>
              </tr>
              <tr>
                <td align="center">4</td>
                 <td><input name="headshipName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="headshipLevel" type="text" class="opand" value="" size="15" /></td>
                <td><input name="description" type="text" class="opand" value="" size="40" /></td>
              </tr>
              <tr>
                <td align="center">5</td>
                 <td><input name="headshipName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="headshipLevel" type="text" class="opand" value="" size="15" /></td>
                <td><input name="description" type="text" class="opand" value="" size="40" /></td>
              </tr>
           </table>
           </form>
     </div>  
        <div class="opennav">
     <div class="tab_btn"><a href="#" onclick="editHeadship.addTr()"><img src="/resources/images/btn_tj.png" />添 加&nbsp;&nbsp;</a></div>
   </div>
   <div class="fulltd" style="height:50px; display:block; overflow:hidden">
      <a href="/pc/pheadship/main.htm"><span class="f_r opbtns"><img src="/resources/images/qx.png" />取　消</span></a>
      <a href="#" onclick="editHeadship.saveHeadship()"><span class="f_r opbtns"><img src="/resources/images/bc.png" />保　存</span></a>
   </div>   
   </div>
