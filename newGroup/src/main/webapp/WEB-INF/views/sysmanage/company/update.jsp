<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css">

<style type="text/css">

.file {
    height: 32px;
    left: 267px;
    opacity: 0;
    position: absolute;
    top: 306px;
    width: 110px;
}
.combo-text{
 font-size: 12px;
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
    font-size: 12px;
    padding: 5px 0 3px 3px;
}
.combo-panel {
   border: 1px solid #E9E9E9;
}
.combobox-item-selected {
    background: none repeat scroll 0 0 #88AEC2;
}
.logo_css {
    height: 72px;
    width: 72px;
}
</style>
<script type="text/javascript" src="/resources/js/sysmanage/company/update.js"></script>
  <div class="openwarp">
   <form id="comp_fm" method="post" enctype="multipart/form-data" autocomplete = "off" > 
   		<input type="hidden" name="pedometer" id="pedometer" value="${pedometer}"/>
   		<input type="hidden" name="pedometerFlag" id="pedometerFlag" value="${pedometerFlag}"/>
       <div class="openbz">
         <div class="f_l">
         <c:choose>
            <c:when test="${comp.indexPictrue !=''&& comp.indexPictrue!=null && fn:indexOf(comp.indexPictrue,'.zip')>0}">
            	<a href="javascript:Header.photo('${comp.indexPictrue}')"><img src="/pc/company/images/${comp.indexPictrue}" class="logo_css" /></a>
				</c:when>
				<c:otherwise>
            			<img src="/resources/images/imga.png" class="logo_css"/>
            		</c:otherwise>
				 </c:choose>
         </div>
         <div class="f_l opname"><span>${comp.companyName}</span></div>
       </div>       
       <div class="opentabs">
         <input id="photoAddrHidden" name="indexLogo" type="hidden" value="${comp.indexLogo}"/>
         <input type="hidden" id="companyId" name="companyId" value="${comp.companyId}" />
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="t_r" width="80">企业名：</td>
            <td width="150" class="t_l"><input id="comName_txt" name="companyName" required="true"  value="${comp.companyName}" type="text" class="opcek3" /></td>
            <td width="120" class="t_r"> 企业编号：</td>
            <td class="t_l"><input name="code" disabled="disabled" value="${comp.companyCode}"   type="text" class="opcek3" />
            <input name="companyCode"  type="hidden" value="${comp.companyCode}"/></td>
          </tr>
          <tr>
            <td class="t_r">地市：</td>
            <td class="t_l"><input id="city"  name="city" value="${comp.city}" type="text" class="opcek3" style="width:160px"/></td>
            <td class="t_r">通讯录上限：</td>
            <td class="t_l"><input name="companyUsersLimit" value="${comp.companyUsersLimit}" type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">联系人：</td>
            <td class="t_l"><input name="contactMan"  value="${comp.contactMan}" type="text" class="opcek3" /></td>
            <td class="t_r"> 使用成员上限：</td>
            <td class="t_l"><input name="usersLimit" id="usersLimit"  value="${comp.usersLimit }"  type="text" class="opcek3" /></td>
          </tr>
          <tr>
            <td class="t_r">手机号码：</td>
            <td class="t_l"><input id="valTel" name="telephone"  class="easyui-numberbox opcek3" validType="validateTel['#valTel']" value="${comp.telephone}" type="text" /></td>
            <td class="t_r"> 异网许可：</td>
            <td class="t_l">　
            <input  type="radio" name="otherNetFlag" id="radio" value="0"  <c:if test="${comp.otherNetFlag=='0' }">checked="checked"</c:if>  />
            	允许　
              　		<input type="radio" name="otherNetFlag" id="radio2" value="1" <c:if test="${comp.otherNetFlag!='0' }">checked="checked"</c:if> />
            	不允许</td>
          </tr>
          <tr>
            <td class="t_r">更换皮肤：</td>
            <td class="t_l"><input id="textfield" name="textfield" readOnly value="${comp.indexPictrue}"   type="text" class="opcek3" />
            <input id="logo" name="logo" type="file" class="file" onchange="document.getElementById('textfield').value=this.value"  /></td>
            <td class="t_r"><div class="lis_btn"><a href="#"><img src="/resources/images/btn_xzwj.png" />选择文件</a></div></td>
            <c:if test="${pedometerFlag=='1'}">
            	<td class="t_l"><span>计步器：</span> <span style="margin-top:10px"><input type="checkbox" id="pedometerSwitch"/></span>&nbsp;&nbsp;<span>开</span></td>
          	</c:if>
          </tr>
         </table>
       </div>     
        </form>
   </div>
   <div class="opennav pd70">
      <a href="javascript:void(0)" onClick="Ict.closeWin();"><div class="f_l opbtns"><img src="/resources/images/qx.png" />取　消</div></a>
      <a href="javascript:void(0)" onClick="update.updateCompany()"><div class="f_l opbtns"  ><img src="/resources/images/bc.png" />保　存</div></a>
   </div>