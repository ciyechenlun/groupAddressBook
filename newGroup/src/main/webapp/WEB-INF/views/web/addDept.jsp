<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<script type="text/javascript" src="/resources/scripts/common.js"></script>
<script type="text/javascript" src="/resources/js/web/editDept.js"></script>
<style type="text/css">
.combo-text{
 color: #697A83;
 padding: 0 0 0 8px;
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
     color: #697A83;
     padding: 3px 0 3px 8px;
}
.combo-panel {
   border: 1px solid #E9E9E9;
}
.combobox-item-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
.tree-node-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
</style>
   <div class="openwarp">
       <div class="opentabs">
        <input id="deptLevel" type="hidden"/>
          <form id="save_form" action="">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100" class="t_r">部门名称：</td>
            <td class="t_l">
            <input id="departmentId" name="departmentId" type="hidden"/>
        	<input id="departmentName" name="departmentName" type="text" class="opcek" />
            </td>
          </tr>
          <tr>
            <td class="t_r">上级部门：</td>
            <td class="t_l"> 
             <input id="departmentCombobox" name="parentDepartmentId" type="text" style="width:230px;"/>
             </td>
          </tr>
          <tr>
            <td class="t_r">同级别顺序：</td>
            <td class="t_l">  
            <input id="displayOrder" name="displayOrder" type="hidden"/>
        	<span>第<input id="relativeOrder" name="relativeOrder" type="text" style="width:100px;" />位</span></td>
          </tr>
          <tr>
            <td class="t_r">V网编号：</td>
            <td class="t_l"> <input id="fax" name="fax" type="text" class="opcek"/></td>
          </tr>
        </table>
        </form>
       </div>
       <div class="openbtn">
         <a href="#" onclick="$('#deptWin').window('close')"><span class="f_l opbtns"><img src="/resources/images/qx.png" />取消</span></a>
        <a href="#" onclick="editDept.save()"> <span class="f_l opbtns"><img src="/resources/images/opsave.png" />保存并退出</span></a>
       </div>
   </div>

