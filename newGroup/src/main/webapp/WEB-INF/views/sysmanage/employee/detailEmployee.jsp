<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
	<link rel="stylesheet" type="text/css" href="/resources/css/style.css">
	<script type="text/javascript" src="/resources/js/common/DateFormat.js"></script>
	<script type="text/javascript" src="/resources/js/sysmanage/employee/employeeDetail.js"></script>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="overflow-x: hidden">
			<div class="pd10">
				<div class="tableform">
					<form id="fm" method="post" name="fm">
					<input id="graduationDateHidden" value="${employee.graduationDate}" type="hidden"></input>
					<input id="birthdayHidden" value="${employee.birthday}"" type="hidden"></input>
					<input id="joinDateHidden" value="${employee.joinDate}" type="hidden"></input>
					<input id="leaveDateHidden" value="${employee.leaveDate}" type="hidden"></input>
		<table cellpadding="0" cellspacing="0">
						<tr>
                    		<th width="80px">&nbsp;</th>
                       	 	<td width="140px">&nbsp;</td>
                       	 	<td>&nbsp;</td>
                       	 	<th width="80px">&nbsp;</th>
                       	 	<td width="140px">&nbsp;</td>
                   		</tr>
							<tr>
								<th>公司名称：</th>
								<td>
                        			<input value="${company.companyName}" id="companyNameId" name="companyName" style="width:121px;" readonly />
                        		</td>
                        		<td>&nbsp;</td>
                				<th>部门：</th>
								<td>
									<input value="${dept.departmentName}" id="departmentNameId"  name="departmentName" style="width:121px;" readonly/>
								</td>
							</tr>
							<tr>
                				<th>岗位：</th>
								<td>
									<input value="${headship.headshipName}" id="headshipNameId"  name="headshipName" style="width:121px;" readonly/>
								</td>
								<td>&nbsp;</td>
								<th>员工编码：</th>
								<td><input id="employeeCodeId"
									value="${employee.employeeCode}" name="employeeCode" readonly
									style="width: 121px" class="easyui-validatebox" >
									</td>
							</tr>
							<tr>
								<th>真实姓名：</th>
								<td><input id="employeeNameId"
									value="${employee.employeeName}" name="employeeName"
									style="width: 121px" class="easyui-validatebox" readonly></td>
								<td>&nbsp;</td>
								<th>身份证号码：</th>
								<td><input id="idCardId" value="${employee.idCard}"
									name="idCard" style="width: 121px"
									readonly>
									</td>
							</tr>

							<tr>
								<th>个人简历：</th>
								<td><input id="resume" value="${employee.resume}"
									name="resume" style="width: 121px"
									readonly>
									</td>
								<td>&nbsp;</td>
								<th>年龄：</th>
								<td><input id="ageId" value="${employee.age}" name="age"
									style="width: 121px"
									readonly >
									</td>
							</tr>
							<tr>
								<th>性别：</th>
								<td><input value="${employee.sex }" disabled="disabled" id="sexId" name="sex" style="width:125px;"/></td>
								<td>&nbsp;</td>
								<th>政治面貌：</th>
								<td>
									<input value="${employee.politicsStatus }" disabled="disabled" id="politicsStatusId" name="politicsStatus" style="width:125px;"/>
								</td>
							</tr>
							<tr>
								<th>籍贯：</th>
								<td>
									<input  value="${employee.nativePlace }"  type="text" id="nativePlaceId" name="nativePlace"
										style="width: 121px" readonly>
								</td>
								<td>&nbsp;</td>
								<th>毕业学校：</th>
								<td><input id="schoolId" value="${employee.school}"
									name="school" style="width: 121px" readonly>
									</td>
							</tr>
							<tr>
								<th>学历：</th>
								<td>
									<input value="${employee.degree }" disabled="disabled" id="degreeId" name="degree" style="width:125px;"/>
								</td>
								<td>&nbsp;</td>
								<th>毕业时间：</th>
								<td><input id="graduationDateId"
									value="${employee.graduationDate}" name="graduationDate"
									type="text" style="width: 121px" readonly></input>
									<td>
							</tr>
							<tr>
								<th>手机：</th>
								<td><input id="mobileId" value="${employee.mobile}" readonly
									name="mobile" style="width: 121px" class="easyui-validatebox"
									>
									</td>
									<td>&nbsp;</td>
								<th>备用手机：</th>
								<td><input id="backupMobileId" readonly
									value="${employee.backupMobile}" name="backupMobile"
									style="width: 121px" class="easyui-validatebox">
									</td>
							</tr>
							<tr>
								<th>宅电：</th>
								<td><input id="telephoneId" value="${employee.telephone}" readonly
									name="telephone" style="width: 121px" class="easyui-validatebox"
									>
									</td>
									<td>&nbsp;</td>
								<th>邮箱：</th>
								<td><input id="emailId" value="${employee.email}" readonly
									name="email" style="width: 121px" class="easyui-validatebox"
									>
									</td>
							</tr>
							<tr>
								<th>QQ：</th>
								<td><input id="qqId" value="${employee.qq}" name="qq" readonly
									style="width: 121px" class="easyui-validatebox">
									</td>
									<td>&nbsp;</td>
								<th>生日：</th>
								<td><input id="birthdayId" value="${employee.birthday}"
									name="birthday" type="text" readonly
									style="width: 121px"></input></td>
							</tr>
							<tr>
								<th>个人照片：</th>
								<td><input id="pictureId" value="${employee.picture}" readonly
									name="picture" style="width: 121px" class="easyui-validatebox"
									>
									</td>
									<td>&nbsp;</td>
								<th>直属领导：</th>
								<td><input id="higherUpId" value="${employee.higherUp}" readonly
									name="higherUp" style="width: 121px" class="easyui-validatebox"
									>
									</td>
							</tr>
							<tr>
								<th>入职时间：</th>
								<td><input id="joinDateId" value="${employee.joinDate}"
									name="joinDate" type="text" readonly
									style="width: 121px"></input></td>
									<td>&nbsp;</td>
								<th>状态：</th>
								<td><input value="${employee.status }" id="statusId" disabled="disabled" name="status" style="width:125px;"/></td>
							</tr>
							<tr>
								<th>离职时间：</th>
								<td><input id="leaveDateId" value="${employee.leaveDate}"
									name="leaveDate" type="text"  readonly
									style="width: 121px"></input></td>
									<td>&nbsp;</td>
								<th hidden="true">删除标志：</th>
								<td><input id="delFlagId" value="${employee.delFlag}" hidden="true"
									name="delFlag" style="width: 121px" class="easyui-validatebox"
									readonly>
								</td>
							</tr>
							<tr>
								<th>离职原因：</th>
								<td><textarea id="leaveReasonId" name="leaveReason" readonly
										style="width: 121px">${employee.leaveReason}</textarea></td>
										<td>&nbsp;</td>
								<th>备注：</th>
								<td><textarea id="commentId" name="comment" readonly
										style="width: 121px">${employee.comment}</textarea></td>
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
						<td><a href="javascript:void(0)" onClick="Ict.closeWin()"
							class="easyui-linkbutton" plain="true" iconCls="cancel">关闭</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--end：button区域-->
	</div>