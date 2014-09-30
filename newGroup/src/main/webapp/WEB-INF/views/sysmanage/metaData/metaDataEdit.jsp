<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
<script type="text/javascript" src="/resources/js/sysmanage/metaData/metaDataEdit.js"></script>

<div class="easyui-layout" fit="true"> 
    <div region="center" border="false" style="overflow-x:hidden">
        <div class="pd10">
            <div class="tableform">
             <form id="metaDataFrom" method="post" > 
             	<input name="metaId" type="hidden" value="${metaData.metaId }" /> 
                <table cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="100">&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                      <tr>
                    	<th>配置类型：</th>
                        <td>
                        	<input id="cc" class="easyui-combobox" name="typeId" value="${metaData.typeId }" style="width:250;"
        					data-options="valueField:'typeId',textField:'typeName',editable:false,url:'/pc/metaData/configTypes',required:true" />
        					<font color="red">*</font>  
                        </td>
                    </tr>
                    <tr>
                    	<th>元数据名称：</th>
						<td><input name="metaName" style="width:250;" value="${metaData.metaName }" class="easyui-validatebox" data-options="required:true"/>
						<font color="red">*</font>  
					   </td>
                    </tr>
                    <tr>
                    	<th>元数据编码：</th>
                        <td><input name="metaCode" style="width:250;" value="${metaData.metaCode }" class="easyui-validatebox" data-options="required:true" ></input>
                        <font color="red">*</font></td>
                    </tr>
                    <tr>
                    	<th>描述：</th>
                        <td>
                        	<textarea name="description" rows="6" cols="40">${metaData.description}</textarea>
                        </td>
                    </tr>
                </table>
                </form>
            </div>
        </div>
    </div>
    <!--start：button区域-->
    <div region="south" style="height:36px;overflow:hidden;" border="false">
        <div class="WinBtnBody pdt5">
            <table align="center"border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <a href="javascript:void(0)" onClick="javascript:saveMetaData();" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
                        <a href="javascript:void(0)" onClick="javascript:Ict.closeWin()" class="easyui-linkbutton" plain="true" iconCls="close">关闭</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--end：button区域-->
</div>

