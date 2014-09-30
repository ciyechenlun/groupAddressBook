<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/lookGroupBook/batchAddEmp.js"></script>

   <div class="openwarp">
     <div class="optab">
       <table width="100%" border="0" cellpadding="0" cellspacing="0" id="batch_tab">
              <tr>
                <th width="30" align="center">&nbsp;</th>
                <th>姓名</th>
                <th>手机号码</th>
                <th>所在部门</th>
                <th>职位</th>
                <th>部门内排序</th>
              </tr>
              <tr>
                <td align="center">1</td>
                <td><input name="employeeName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="mobile" type="text" class="opand" value="" size="15" /></td>
                <td><input  name="departmentId" type="text" class="opand" size="10" style="width:150px"/>
               </td>
                <td><input  id="headshipCombox" name="headshipId" type="text" class="opand" size="10" style="width:120px"/>
                 <input id="headshipName2"  name="headshipName" type="hidden"/></td>
                <td><input  name="displayOrder" type="hidden"  />
            	<input type="text" name="relativeOrder" value='1' class="opand" size="5" style="width:80px"/></td>
              </tr>
              <tr>
                <td align="center">2</td>
                <td><input name="employeeName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="mobile" type="text" class="opand" value="" size="15" /></td>
                <td>
                  <input  name="departmentId" type="text" value="同上" class="opand" size="10" style="width:150px"/>
                </td>
                <td><input id="headshipCombox" name="headshipId" type="text" value="同上" class="opand" size="10" style="width:120px"/>
                 <input id="headshipName2"  name="headshipName" type="hidden"/></td>
                <td><input  name="displayOrder" type="hidden"  />
            	<input type="text" name="relativeOrder" value="1" class="opand" size="5" style="width:80px"/></td>
              </tr>
              <tr>
                 <td align="center">3</td>
                <td><input name="employeeName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="mobile" type="text" class="opand" value="" size="15" /></td>
               <td>
                  <input  name="departmentId" type="text" value="同上" class="opand" size="10" style="width:150px"/>
                </td>
                <td><input id="headshipCombox" name="headshipId" type="text" value="同上" class="opand" size="10" style="width:120px"/>
                 <input id="headshipName2"  name="headshipName" type="hidden"/></td>
                <td><input  name="displayOrder" type="hidden"  />
            	<input type="text" name="relativeOrder" value="1" class="opand" size="5" style="width:80px"/></td>
              </tr>
              <tr>
                 <td align="center">4</td>
                <td><input name="employeeName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="mobile" type="text" class="opand" value="" size="15" /></td>
                <td>
                  <input  name="departmentId" type="text" value="同上" class="opand" size="10" style="width:150px"/>
                </td>
                <td><input id="headshipCombox" name="headshipId" type="text" value="同上" class="opand" size="10" style="width:120px"/>
                 <input id="headshipName2"  name="headshipName" type="hidden"/></td>
                <td><input  name="displayOrder" type="hidden"  />
            	<input type="text" name="relativeOrder" value="1" class="opand" size="5" style="width:80px"/></td>
              </tr>
              <tr>
                <td align="center">5</td>
                <td><input name="employeeName" type="text" class="opand" value="" size="10" /></td>
                <td><input name="mobile" type="text" class="opand" value="" size="15" /></td>
                <td>
                  <input  name="departmentId" type="text" value="同上" class="opand" size="10" style="width:150px"/>
                </td>
                <td><input id="headshipCombox" name="headshipId" type="text" value="同上" class="opand" size="10" style="width:120px"/>
                 <input id="headshipName2"  name="headshipName" type="hidden"/></td>
                <td><input  name="displayOrder" type="hidden"  />
            	<input type="text" name="relativeOrder" value="1" class="opand" size="5" style="width:80px"/></td>
              </tr>
           </table>
     </div>     
   </div>
   <div class="opennav">
     <div class="tab_btn"><a href="#" onclick="batchAddEmp.addTr()"><img src="/resources/images/btn_tj.png" />添 加&nbsp;&nbsp;</a></div>
   </div>
   <div class="fulltd" style="height:50px; display:block; overflow:hidden">
      <div class="f_r opbtns" style="cursor:pointer" onclick="$('#addEmployee').window('close')"><img src="/resources/images/qx.png" />取　消</div>
      <div class="f_r opbtns" style="cursor:pointer" onclick="batchAddEmp.batchAddUser()"><img src="/resources/images/bc.png" />保　存</div>
   </div>
