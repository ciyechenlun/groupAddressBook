<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录</title>
<%@include file="../common/baseIncludeJs.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/pagination.css" />
<script type="text/javascript" src="/resources/js/common/jquery.pagination.js"></script>
<script type="text/javascript" src="/resources/js/common/pagination.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/pedometer/css/sub.css" />
<!-- 自动补全 -->
<link rel="stylesheet" type="text/css" href="/resources/css/jquery.autocomplete.css"></link>
<script type="text/javascript" src="/resources/js/common/jquery.autocomplete.js"></script>
<script type="text/javascript">
	$(function(){
		$('#departmentCombobox').combotree({
				url:'/pc/deptMag/tree.htm',
				editable:false,
				panelHeight:200,
				onSelect:function(node){
					if(node.id!='0'){
						$("#hidDept").val(node.id);
					}
					else
					{
						$("#hidDept").val('');
					}
				}
			});
		//活动下拉框
		$('#movementCombobox').combobox();
		//auto complete
		$("#txtUserName").autocomplete("/pedometer/getEmployee.htm",{
			max:5,
			minChars:1,
			matchContains:1,
			cacheLength:10,
			matchContains:true,
			scrollHeight:250,
			width:250,
			dataType:'json',//返回的数据类型为JSON类型
			parse: function(data) {
			    return $.map(data, function(item) {
			      return { data: item, value: item.employee_id, result: item.employee_name };
			    });
			  },
			formatItem: function (row, i, max) {
	             return  row.employee_name;
	         },
	         formatMatch: function (row, i, max) {
	             return row.employee_name + " ";
	         },
	 
	         formatResult: function (row) {
	             return row.name;
	         }
		});	
		//日历控件
		$('#txtStartDate').datebox({
		    
		});
		$('#txtEndDate').datebox({
		    
		});
		
	    $(".gridCont").find("tr:even").find("td").css({ background:"#eee" });
		
		function autoSize(){
			var newH = $(window).height();
			$(".rightContMain").height( newH - $("h1").height() );
		}
		autoSize();
		
		$(window).on("resize",function(){
			autoSize();
		});
		
		//初始化默认值
		initDefaultValue();
	});
	//初始值
	function initDefaultValue()
	{
		//默认选中部门
		$("#departmentCombobox").combotree('setValue','${department_id}');
		$("#txtUserName").val('${user_name}');
		$("#txtStartDate").datebox('setValue','${startDate}');
		$("#txtEndDate").datebox('setValue','${endDate}');
	}
	//搜索
	function search()
	{
		var department_id = $("#hidDept").val();
		var userName = $("#txtUserName").val();
		var startDate = $("#txtStartDate").datebox('getValue');
		var endDate = $("#txtEndDate").datebox('getValue');
		var movementId = $("#movementCombobox").combobox('getValue');
		window.location.href = './walkRecord.htm?movement_id=' + movementId + '&department_id=' + department_id + '&user_name=' + userName
				+ '&startDate=' + startDate + '&endDate=' + endDate;
	}
	//导出
	function exportRecord()
	{
		var department_id = $("#hidDept").val();
		var userName = $("#txtUserName").val();
		var startDate = $("#txtStartDate").datebox('getValue');
		var endDate = $("#txtEndDate").datebox('getValue');
		var movementId = $("#movementCombobox").combobox('getValue');
		$('#exportForm').form('submit',{
			url:'./exportWalkRecord.htm?movement_id=' + movementId + '&department_id=' + department_id + '&user_name=' + userName
			+ '&startDate=' + startDate + '&endDate=' + endDate,
			success:function(ret)
				{
					$.messager.alert(data);
				}
			});
	}
</script>
</head>

<body>
	<input type="hidden" value="${pagination.totalRecords}" id="totalRecords" />
	<input type="hidden" id="totalPages" value="${pagination.totalPages}" />
	<input type="hidden" id="currPage" value="${pagination.currPage}" />
	
	<!-- 表单数据提交发送请求 -->
	<form id="searchForm" method="post" action="./walkRecord.htm">
		<input id="toPage" name="pageNo" type="hidden" value="${search.pageNo }" /> 
		<input id="pageSize" name="pageSize" type="hidden" value="20" />
		<input id="department_id" name="department_id" type="hidden" value="${department_id}" />
		<input id="user_name" name="user_name" type="hidden" value="${user_name }" />
		<input id="startDate" name="startDate" type="hidden" value="${startDate }" />
		<input id="endDate" name="endDate" type="hidden" value="${endDate }" />
		<input id="companyId" name="companyId" type="hidden" />
		<input id="movementId" name="movement_id" type="hidden" value="${movementId }" />
	</form>
	<h1>健步记录</h1>
	<div class="rightContMain">
	    <div class="searchBar">
            <div>
            	<form name="exportForm" id="exportForm" method="post" action="">
                </form>
	            	<table cellpadding="0" cellspacing="0" >
	                	<tr>
	                		<td><label>活动：</label></td>
	                		<td>
		                		<select id="movementCombobox" name="movementCombobox">
		                			<c:forEach var="movementList" varStatus="status" items="${list }">
		                				<option 
		                				${movementList.movementId == movementId?"selected='selected'":""} 
		                				value="${movementList.movementId }">${movementList.movementName }</option>
		                			</c:forEach>
		                		</select>
	                		</td>
	                    	<td><label>部门：</label></td><td>
	                    		<input id="departmentCombobox" name="parentDepartmentId" type="text" style="width:160px;" class="bm_input"/>
	                    		<input type="hidden" name="hidDept" id="hidDept" />
	                    	</td>
	                        <td><label>姓名：</label></td><td><input type="text" id="txtUserName" name="txtUserName" class="inputText" /></td>
	                        <td><label>起始时间：</label></td><td><input type="text" class="inputText" id="txtStartDate" name="txtStartDate" /></td>
	                        <td><label>结束时间：</label></td><td><input type="text" id="txtEndDate" name="txtEndDate" class="inputText" /></td>
	                        <td class="w80"><button class="cxbtn" onClick="search()">查 询</button></td>
	                        <td class="w80"><button class="cxbtn" onClick="exportRecord()">导出</button></td>
	                    </tr>
	                </table>
            </div>
	    </div>
	    
	    <p class="line"></p>
	    
	    <div class="gridWrap">
	        <div class="gridHead"><b>用户列表</b></div>
	        <div class="gridCont">
	            <table class="gridTable" cellpadding="0" cellspacing="0">
	                <tr><th>编号</th><th>姓名</th>
	                <th>轨迹</th>
	                <th>所属部门</th><th>手机号</th><th>体重/kg</th><th>身高/cm</th><th>上传日期</th><th>运动日期</th><th>起始时间</th><th>结束时间</th><th>耗时</th><th>步数</th><th>里程/米</th><th>速度km/h</th><th>耗能/千卡</th><th>GPS状态</th><th>GPS里程</th><th>计步方式切换次数</th>
	                <!-- <th>时间倒计</th><th>步数倒计</th> -->
	                <th>步长推算/cm</th><th>步长误差/cm</th></tr>
	                
	                <c:forEach var="searchResult" varStatus="status" items="${pagination.result }">
	                	<tr><td><c:out value="${status.index+1}"/></td>
		                	<td>${searchResult.employee_name }</td>
		                	<td>
		                		<c:if test="${searchResult.gps_status == '1' }">
		                			<a href="showMap.htm?employee_record_id=${searchResult.employee_record_id }&employee_name=${searchResult.employee_name}&sport_date=${searchResult.sport_date }&sport_start_time=${searchResult.sport_start_time }&sport_end_time=${searchResult.sport_end_time }&sport_elapse_time=${searchResult.sport_elapse_time }&sport_step=${searchResult.sport_step }&sport_distence=${searchResult.sport_distence }&sport_speed=${searchResult.sport_speed }">
		                				轨迹
		                			</a>
		                		</c:if>
		                	</td>
		                	<td>${searchResult.department_name }</td>
		                	<td>${searchResult.mobile }</td>
		                	<td>${searchResult.employee_weight }</td>
		                	<td>${searchResult.employee_height }</td>
		                	<td>${searchResult.update_date }</td>
		                	<td>${searchResult.sport_date }</td>
		                	<td>${searchResult.sport_start_time }</td>
		                	<td>${searchResult.sport_end_time }</td>
		                	<td>${searchResult.sport_elapse_time }</td>
		                	<td>${searchResult.sport_step }</td>
		                	<td>${searchResult.sport_distence }</td>
		                	<td>${searchResult.sport_speed }</td>
		                	<td>${searchResult.sport_calories }</td>
		                	<td>
		                		<c:if test="${searchResult.gps_status=='0' }">
		                			关
		                		</c:if>
		                		<c:if test="${searchResult.gps_status=='1' }">
		                			开
		                		</c:if>
							</td>
		                	<td>${searchResult.gps_distence }</td>
		                	<td>${searchResult.mode_change_times }</td>
		                	<!-- <td></td>
		                	<td></td> -->
		                	<td>${searchResult.step_calc }</td>
		                	<td>${searchResult.step_error }</td>
	                	</tr>
	                </c:forEach>
	                
	            </table>
	        </div>
	        
			<!--start:分页-->
			<div class="bottom-pagination">
				<div class="pagination-box">
					<div class="pagination">
						<ul>
							<li>
								<div class="paginations" name="Pagination"></div> <span>共${pagination.totalRecords}条记录</span>
								<span> 到第 <input type="text" name="toTargetPage"
									style="width: 25px;" value="${pagination.currPage}" /> <span>页</span>
									<input type="button" value="确定" id="J_JumpTo"
									name="toTargetPage_btn" />
							</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<!--end：分页-->

	    </div>
	</div>
</body>
</html>
