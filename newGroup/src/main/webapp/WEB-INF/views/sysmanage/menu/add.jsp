<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/sysmanage/menu/add.js"></script>

	<div class="easyui-layout" fit="true"> 
	    <div region="center" border="false" style="overflow-x:hidden">
	        <div class="pd10">
	            <div class="tableform">
	             <form id="menu_fm" method="post" name="fm" enctype="multipart/form-data" autocomplete = "off"> 
	                <table cellpadding="0" cellspacing="0">
	                	<tr>
	                    	<th width="100">&nbsp;</th>
	                        <td>&nbsp;</td>
	                    </tr>
	                    <tr>
	                    	<th>菜单路径：</th>
	                        <td>
	                        	<input id="path" name="path" style="width:150px;"/>
	                        	<font color="red">*</font>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>菜单名称：</th>
							<td>
								<input id="menuName" name="menuName" style="width:150px;" class="easyui-validatebox" data-options="required:true" />
								<font color="red">*</font>
						   </td>
	                    </tr>
	                    <tr>
	                    	<th>上级菜单：</th>
	                        <td>
	                        	<input id="parentId" name="parentId" style="width:150px;"/>
	                        	<font color="red">*</font>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>所属公司：</th>
	                        <td>
	                        	<input id="company" class="easyui-combotree" name="company" style="width:150px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>状态：</th>
	                        <td>
	                        	<select id="status" name="status"  style="width:150px;" class="easyui-validatebox" required="true">
	                              <option value="">--请选择--</option>
	                              <option value="0">禁用</option>
	                              <option value="1">启用</option>
	                           </select>
	                           <font color="red">*</font>
	                        </td>
	                    </tr>
	                    <tr>
	                    	<th>显示顺序：</th>
	                        <td><input name="displayOrder" style="width:150px;" class="easyui-numberbox"/></td>
	                    </tr>
	                    <tr>
	                    	<th>菜单图标：</th>
	                        <td><input id="menu_icon" name="menu_icon" style="width:150px;" type="file" style="width:150px;"/></td>
	                    </tr>
	                    <tr>
	                    	<th>备注：</th>
	                        <td>
	                        	<textarea name="comment" rows="6" cols="30"></textarea>
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
	                        <a href="javascript:void(0)" onClick="addMenu.save()" class="easyui-linkbutton" plain="true" iconCls="add">保存</a>
	                        <a href="javascript:void(0)" onClick="Ict.closeWin();" class="easyui-linkbutton" plain="true" iconCls="remove">关闭</a>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	    <!--end：button区域-->
	    
	</div>
