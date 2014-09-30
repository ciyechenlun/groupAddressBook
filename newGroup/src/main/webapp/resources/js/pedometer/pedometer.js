var pedometer = function(){
	return {
		//禁用活动
		disableMovement:function(movement_id)
		{
			$.get(
				"movementConfig.htm?movement_id=" + movement_id,
				function(ret){
					if(ret == 'SUCCESS')
						window.location.reload();
					else
					{
						//禁用失败
						$.messager.alert('禁用失败');
					}
				}
			);
		},
		
		//用户明细搜索初始化（默认填充文本框）
		initSearch:function(key)
		{
			if(key!='')
			{
				$("#keyword").css("color","#000000");
				$("#keyword").val(key);
			}
		},
		
		//用户搜索
		search:function(movement_id)
		{
			var txt = $("#keyword");
			if(txt.val() == '' || txt.val() == '姓名、手机号码')
			{
				$.messager.alert('错误','请输入搜索关键司','info');
			}
			else
			{
				window.location.href = 'movementUsers.htm?movement_id='+movement_id+'&keyword=' + txt.val();
			}
		},
		

		//更改搜索文本框的样式
		keyword_focus:function(txt)
		{
			if(txt.value == '姓名、手机号码'){
				txt.value = '';
				$("#keyword").css('color','#000000');
			}
		},
		
		//添加新的活动
		btnAdd_OnClick:function(type,movement_id)
		{
			var title = "活动管理";
			var width = 640;
			var height = 400;
			var href = "movementEdit.htm?type=" + type;
			if(movement_id!='')
			{
				href += "&movement_id=" + movement_id;
			}
			else
			{
				href += "&movement_id=";
			}
			var win = $('#movementDialog').window({
			     title: title,
			     width: width,
			  // shadow: false,
			     closed: false,
				 cache:  false,
			     height: height,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:href,
				 top:50 ,   
		         left:(document.body.scrollWidth-width)/2-100,
				 modal:true,
				 onLoad:function(){
					 //所属公司
					 $('#companyId').combobox({
							url :'/pc/message/getAllCompanyByUser.htm',
							valueField : "company_id",
							textField  : "company_name"
					});
					//勋章系统
					 $('#medalSysId').combobox({
						 url:'/pedmeter/pedometer/getMedalSystem.htm',
						 valueField : 'medal_sys_id',
						 textField : 'medal_sys_name'
					 });
					 //计步方式
					 $("#orderType").combobox();
					 //所属赛季
					 $("#parentMovementId").combobox({
						 url:'/pedmeter/pedometer/getParentMovement.htm',
						 valueField:'movement_id',
						 textField:'movement_name'
					 });
				 }
			});	
		},

		//删除用户
		deleteUsers:function(movement_id,employee_id)
		{
			$.messager.confirm('提示','确实要删除活动下的用户吗？',function(b){
				if(b){
					$.get("./movementUserDelete.htm?movement_id="+movement_id+"&employee_id="+employee_id,function(ret){
						if(ret == "SUCCESS")
						{
							window.location.reload();
						}
						else
						{
							$.messager.alert('删除失败');
						}
					});
				}
			});
		},
		
		//表单提交
		saveMovement:function(type)
		{
			//先进行表单验证
			var eadmin = $("#eadmin").val();
			var movementName = $("#movementName").val();
			var startDate = $("#movementStartDate").datetimebox('getValue');
			var endDate = $("#movementEndDate").datetimebox('getValue');
			var companyId = eadmin == '1'?$("#departmentId").val():$("#companyId").combobox('getValue');
			var medalSysId = $('#medalSysId').combobox('getValue');
			var orderType = $('#orderType').combobox('getValue');
			if(movementName == '')
			{
				$.messager.alert('提示','活动名称不能为空','info');
				return false;
			}
			else if(startDate == '')
			{
				$.messager.alert('提示','活动开始时间不能为空','info');
				return false;
			}
			else if(endDate == '')
			{
				$.messager.alert('提示','活动结束时间不能为空','info');
				return false;
			}
			else if(companyId == '')
			{
				$.messager.alert('提示','所属公司不能为空','info');
				return false;
			}
			else if(medalSysId == '')
			{
				$.messager.alert('提示','请选择勋章系统','info');
				return false;
			}
			else if(orderType == '')
			{
				$.messager.alert('提示','请选择排名方式','info');
				return false;
			}
			else
			{
				//表单验证通过，提交至服务器
				$('#fmMovement').form(
					'submit',
					{
						onSubmit: function(){
							return $('#fmMovement').form('validate');
						},
						url:"./movementEditSubmit.htm?type=" + type,
						success:function(ret){
							if(ret == 'SUCCESS')
							{
								window.location.reload();
							}
							else
							{
								if(ret == "SAME DATE")
								{
									$.messager.alert("提示","开始和结束日期相同","error");
								}
								else if(ret == 'MORE')
								{
									$.messager.alert("提示","结束日期早于开始日期","error");
								}
								else if(ret == 'COMPANY')
								{
									$.messager.alert("提示","所属企业不能为空","error");
								}
								else if(ret == 'MEDAL')
								{
									$.messager.alert("提示","勋章系统不能为空","error");
								}
								else if(ret == 'ORDER')
								{
									$.messager.alert("提示","排名方式不能为空","error");
								}
								else{
									$.messager.alert("提示","添加失败，请检查必填项等属性","error");
								}
							}
						}
					}
				);
			}
			return false;
		},
		
		//表单取消
		cancel:function()
		{
			$("#movementDialog").window('close');
		},
		
		//选择用户
		openWin:function(company_id,movement_id)
		{
			var title = "用户选择";
			var width = 640;
			var height = 410;
			var href = "userChoseDraftsView.htm?company_id=" + company_id + "&movement_id=" + movement_id + "";
			$('#choseUser').window({
			     title: title,
			     width: width,
			  // shadow: false,
			     closed: false,
				 cache:  false,
			     height: height,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 href:href,
				 top:50 ,   
		         left:(document.body.scrollWidth-width)/2-100,
				 modal:true
			});	
		}
	};
}();