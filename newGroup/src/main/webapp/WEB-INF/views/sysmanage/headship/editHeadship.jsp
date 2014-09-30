<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/common/DateFormat.js"></script>
	<script type="text/javascript" src="/resources/js/sysmanage/headship/headshipEdit.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
						<input type="hidden" name="role" value="${headship.headshipId}" />
						<table cellpadding="0" cellspacing="0">
							<tr>
                    			<th width="100">&nbsp;</th>
                        		<td>&nbsp;</td>
                    		</tr>
							<tr style="display: none">
								<th>岗位Id：</th>
								<td><input id="headshipIdId" value="${headship.headshipId}"
									name="headshipId" style="width: 150px" class="easyui-validatebox"></td>
							</tr>
							<tr>
								<th>公司名称：</th>
								<td>
									<input  value="${headship.companyId}" class="easyui-combotree" name="companyId" style="width:153px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
	        					</td>
							</tr>
							<tr>
								<th>岗位名称：</th>
								<td><input id="headshipNameId"
									value="${headship.headshipName}" name="headshipName"
									style="width: 150px" class="easyui-validatebox"
									data-options="required:true"> <span style="color: red">*</span></td>
							</tr>
							<tr>
								<th>岗位级别：</th>
								<td><input value="${headship.headshipLevel }" id="headshipLevelId" name="headshipLevel" style="width:153px;"/></td>
							</tr>
							<%-- <tr style="display:none">
								<th>创建人：</th>
								<td><input id="createManId" value="${headship.createMan}"
									name="createMan" style="width: 150px" class="easyui-validatebox"
									data-options="required:true"> <span style="color: red">*</span></td>
							</tr>

							<tr style="display:none">
								<th>创建时间：</th>
								<td><input id="createTimeId"
									value="${headship.createTime }" name="createTime" type="text"
									class="easyui-datebox"
									data-options="formatter:editFormatter,required:true"
									style="width: 154px"></input> <span style="color: red">*</span>
								</td>
							</tr>
							<tr style="display: none">
								<th>修改人：</th>
								<td><input id="modifyManId" value="${headship.modifyMan}"
									name="modifyMan" style="width: 150px" class="easyui-validatebox">
							</tr>
							<tr style="display: none">
								<th>修改时间：</th>
								<td><input id="modifyTimeId" value="${headship.modifyTime}"
									name="modifyTime" style="width: 150px" class="easyui-datebox">
							</tr> --%>
							<tr>
								<th>状态：</th>
								<td><input value="${headship.status }" id="statusId" name="status" style="width:153px;"/>
								<span style="color: red">*</span></td>
							</tr>
							<tr>
								<th>描述：</th>
								<td><textarea id="descriptionId" name="description"
										style="width: 150px">${headship.description}</textarea></td>
							</tr>
							<%-- <tr style="display: none">
								<th>删除标志：</th>
								<td><input id="delFlagId" value="${headship.delFlag}"
									name="delFlag" style="width: 150px" class="easyui-validatebox">
							</tr> --%>
						</table>
					</form>
				</div>
			</div>
		</div>
		<!--start：button区域-->
		<div region="south" style="height: 36px; overflow: hidden;"
			border="false">
			<div class="WinBtnBody pdt5">
				<table align="center" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><a href="javascript:void(0)" onClick="saveHeadship();"
							class="easyui-linkbutton" plain="true" iconCls="save">保存</a> <a
							href="javascript:void(0)" onClick="Ict.closeWin()"
							class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>