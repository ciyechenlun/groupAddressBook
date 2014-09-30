<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
<script type="text/javascript" src="/resources/js/sysmanage/dictData/dictTypeEdit.js"></script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',border:false" style="overflow-x:hidden">
        <div class="pd10">
            <div class="tableform">
             <form id="dictTypeForm" method="post"  > 
            	<input type="hidden" name="typeId" value="${dictType.typeId }" />
                <table cellpadding="0" cellspacing="0">
                	<tr>
                    	<th width="100">&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>公司：</th>
                        <td>
                        	<input class="easyui-combotree" name="company.companyId" value="${dictType.company.companyId}" style="width:300;"
        					data-options="url:'/pc/company/companyTree/0.htm',editable:false" />
                        </td>
                    </tr>
                    <tr>
                    	<th>字典类型名称：</th>
						<td>
							<input name="typeName" value="${dictType.typeName }" style="width:300;" class="easyui-validatebox" data-options="required:true" />
							<font color="red">*</font>
					   </td>
                    </tr>
                      <tr>
                    	<th>字典类型编码：</th>
                        <td>
                        	<input name="typeCode" value="${dictType.typeCode }" style="width:300;"  class="easyui-validatebox" data-options="required:true" ></input>
                        	<font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                    	<th>描述：</th>
                        <td>
                        	<textarea name="description" rows="6" cols="40">${dictType.description }</textarea>
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
                        <a href="javascript:void(0)" onClick="javascript:savedictType();" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
                        <a href="javascript:void(0)" onClick="javascript:Ict.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--end：button区域-->
</div>
