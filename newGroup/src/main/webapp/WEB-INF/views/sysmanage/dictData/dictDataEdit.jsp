<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
<script type="text/javascript" src="/resources/js/sysmanage/dictData/dictDataEdit.js"></script>

<div class="easyui-layout" fit="true"> 
    <div region="center" border="false" style="overflow-x:hidden">
        <div class="pd10">
            <div class="tableform">
             <form id="dictDataForm" method="post" > 
             	<input name="dataId" type="hidden" value="${dictData.dataId }" /> 
                <table cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="100">&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                      <tr>
                    	<th>类型：</th>
                        <td>
                        	<input class="easyui-combobox" name="dictType.typeId" value="${dictData.dictType.typeId}" style="width:250;"
        					data-options="valueField:'typeId',textField:'typeName',editable:false,url:'/pc/dictData/type/dictTypesForCombo',required:true" />
        					<font color="red">*</font>  
                        </td>
                    </tr>
                    <tr>
                    	<th>数据编码：</th>
						<td><input name="dataCode" style="width:250;" value="${dictData.dataCode }" class="easyui-validatebox" data-options="required:true"/>
						<font color="red">*</font>  
					   </td>
                    </tr>
                    <tr>
                    	<th>数据内容：</th>
                        <td><input name="dataContent" style="width:250;" value="${dictData.dataContent }" class="easyui-validatebox" data-options="required:true" ></input>
                        <font color="red">*</font></td>
                    </tr>
                    <tr>
                    	<th>数据描述：</th>
                        <td>
                        	<textarea name="description" rows="6" cols="40">${dictData.description}</textarea>
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
                        <a href="javascript:void(0)" onClick="javascript:saveDictData();" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
                        <a href="javascript:void(0)" onClick="javascript:Ict.closeWin()" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--end：button区域-->
</div>

