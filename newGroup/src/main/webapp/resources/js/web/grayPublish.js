GrayPublish = function() {
	return {
		/**
		 * 打开选择部门弹窗.
		 * @param width 弹窗宽度
		 * @param height 弹窗高度
		 * @returns
		 */
		openWin : function(width, height) {
			var companyId = $('#msg_company').combobox('getValue');
			if(null == companyId || "" == companyId) {
				$.messager.alert("提示", '请选择公司', 'info');
				   return false;
			}
			$('#chooseDept').window({
				title       : '部门选择',
			    width       : width,
			    closed      : false,
				cache       : false,
			    height      : height,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				resizable   : false,
				href        : '/pc/gray/chooseDept.htm?companyId='+ $('#msg_company').combobox('getValue'),
				top         : (document.body.clientHeight - height)/2 ,   
		        left        : (document.body.scrollWidth - width)/2 - 100,
				modal       : true
			});	
		},
		
		/**
		 * 判断是否同步所有人(true-同步所有人;false-同步选择部门下的人员).
		 * @returns
		 */
		disableObj : function() {
			if($('#checkboxName').attr("checked")) {
				$('#SelectedDept').css({"background":"#a9bed5"});
				$('#deptSelectButton').hide();
				$("#syncAllFlag").val("true");
			} else {
				$('#SelectedDept').css({"background":"white"});
				$('#deptSelectButton').show();
				$("#syncAllFlag").val("false");
			}
		},
		
		/**
		 * 取消编辑.
		 * @returns
		 */
		cancelEdit : function() {
			window.location.href = "/pc/gray/main.htm";
	 	},
	 	
	 	/**
	 	 * 确认选择部门下的所有人员进行灰度发布.
	 	 * @returns
	 	 */
	 	confirmSync : function() {
	 		var companyId = $('#msg_company').combobox('getValue');
			if(null == companyId || "" == companyId) {
				$.messager.alert("提示", '请选择公司', 'info');
				return false;
			}
			var syncAllFlag = $("#syncAllFlag").val();
			var SelectedDeptId = $.trim($("#SelectedDeptId").val());
			if("false" == syncAllFlag && (null == SelectedDeptId || "" == SelectedDeptId)) {
				$.messager.alert("提示", '请选择部门', 'info');
				return false;
			}
			var empIds = $.trim($("#empIds").val());
			var deptName = $.trim($("#SelectedDept").val());
			if("false" == syncAllFlag && (null == empIds || "" == empIds)) {
				$.messager.alert("提示", '【' + deptName + '】下无可同步人员,请重新选择部门', 'info');
				return false;
			}
			var empNum = $("#empNum").val();
			var companyName = $('#msg_company').combobox('getText');
			if("true" == syncAllFlag && 0 == empNum) {
				$.messager.alert('提示', '公司【' + companyName + '】下无可同步人员,请重新选择!', 'info');
				return false;
			}
			$.messager.confirm('提示', '确定同步所选对象吗?', function(btn) {
				if(btn) {
					$.ajax({
						url     : '/pc/gray/syncEmps.htm',
						data    : {
							'companyId'   : companyId,
							'empIds'      : $("#empIds").val(),
							'syncAllFlag' : syncAllFlag
						},
						type    : 'POST',
						success : function(data) {
							if(data.code == '0') {
								if(data.success) {
									$.messager.alert('提示', '灰度用户发布成功!', 'info', function() {
										window.location.href = "/pc/gray/main.htm";
									});
								} else {
									$.messager.alert('提示', '灰度用户发布失败!', 'info');
								}
							} else if(data.code == '1') {
								$.messager.alert('提示', '成员订购失败!', 'info');
							} else {
								$.messager.alert('提示', '灰度用户发布失败!', 'info');
							}
						}
					});
				}
			});
	 	}
	};
}();

$(function(){
	$('#msg_company').combobox({
		url        : '/pc/message/company.htm',
		valueField : 'company_id',
		textField  : 'company_name',
		editable   : false,
		onSelect   : function(record) {
			$.ajax({
				url     : '/pc/gray/getEmpsByCompId.htm',
				data    : {
					'companyId'   : record.company_id,
				},
				type    : 'POST',
				success : function(data) {
					if(data.empNum == '0') {
						$.messager.alert('提示', '公司【' + record.company_name + '】下无可同步人员,请重新选择!', 'info');
						$("#syncObject").val("");
					} else {
						$("#syncObject").val(data.empNames);
					}
					$("#empNum").val(data.empNum);
				}
			});
		}
	});
});