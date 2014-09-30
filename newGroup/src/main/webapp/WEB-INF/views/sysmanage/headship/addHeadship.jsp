<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/common/DateFormat.js"></script>
	<script type="text/javascript" src="/resources/js/sysmanage/headship/headshipAdd.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
						<table cellpadding="0" cellspacing="0">
							<tr>
                    			<th width="100">&nbsp;</th>
                        		<td><input type="hidden" id="headshipIdId" name="headshipId"/></td>
                        		
                    		</tr>
							<tr>
								<th>公司名称：</th>
								<td>
									<input id="companyIdId" class="easyui-combotree" name="companyId" style="width:150px;"
	        					data-options="url:'/pc/company/companyTree/0.htm',editable:false,required:true" />
	        					<font color="red">*</font>
	        					</td>
							</tr>
							<tr>
								<th>岗位名称：</th>
								<td>
									<input id="headshipNameId" name="headshipName" style="width: 150px" 
									class="easyui-validatebox" data-options="required:true"> 
									<font color="red">*</font>
								</td>
							</tr>
							<tr>
								<th>岗位级别：</th>
								<td>
	        						<input id="headshipLevelId" name="headshipLevel" style="width:150px;"/>
	        					<font color="red">*</font>
								</td>
							</tr>
							<!-- <tr style="display: none">
								<th>创建人：</th>
								<td><input id="createManId" name="createMan"
									style="width: 150px" class="easyui-validatebox">
								</td>	
							</tr> -->

							<!-- <tr style="display: none">
								<th>创建时间：</th>
								<td><input id="createTimeId" name="createTime" type="text"
									class="easyui-datebox" data-options="formatter:addFormatter"
									style="width: 150px"/>
								<td>
							</tr> -->
							<!-- <tr style="display: none">
								<th>修改人：</th>
								<td><input id="modifyManId" name="modifyMan"
									style="width: 150px" class="easyui-validatebox">
								</td>
							</tr> -->
							<!-- <tr style="display: none">
								<th>修改时间：</th>
								<td><input id="modifyTimeId" name="modifyTime" type="text"
									class="easyui-datebox" data-options="formatter:addFormatter"
									style="width: 150px"/>
								</td>
							</tr> -->
							<tr>
								<th>状态：</th>
								<td>
									<input id="statusId" name="status" style="width:150px;"/>
									<font color="red">*</font>
								</td>
							</tr>
							<tr>
								<th>描述：</th>
								<td><textarea id="descriptionId" name="description"
										style="width: 150px"></textarea>
								</td>
							</tr>
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