Holidayskin = function(){
	return {
		/**
		 * 打开添加节假日皮肤弹窗.
		 * @returns
		 */
		addSkin : function() {
			Header.winShow('skinWindow');
		},
		
		/**
		 * 打开编辑节假日皮肤弹窗,并填充皮肤信息.
		 * @returns
		 */
		eidtSkin : function() {
			var skinId = $("[name='radioName']:checked").val();
			if(skinId == null || skinId == "") {
				$.messager.alert('提示', '请选择皮肤!', 'info');
				return false;
			}
			$('#toggleDelEdite').text('编辑节假日皮肤');	
			$.ajax({
				type: "POST",
				url: "/pc/holidaySkin/getSkinById.htm",
				data: {'skinId' : skinId},
				dataType: "JSON",
				success : function(data) {
					$('#holidayskinId').val(skinId);
					$("#holidayskinName").val(data[0].holidayskin_name);
					$("#holidayName").val(data[0].holiday_name);
					$("#holidayskinStartDate").datetimebox('setValue',data[0].holidayskin_start_date);
					$("#holidayskinEndDate").datetimebox('setValue',data[0].holidayskin_end_date);
					$("#holidayskinPath").val(data[0].holidayskin_path);
					$("#holidayskinRepeat").combobox('setValue',data[0].holidayskin_repeat);
				}
			});
			Header.winShow('skinWindow');
		},
		
		/**
		 * 关闭弹窗.
		 * @returns
		 */
		closeWin : function() {
			Header.winHide('skinWindow');
			$('#skinForm').form('clear');
		},
		
		/**
		 * 保存节假日皮肤信息.
		 * @returns
		 */
		saveSkin : function() {
			var holidayskinId = window.document.getElementById("holidayskinId").value;
			var holidayskinName = window.document.getElementById("holidayskinName").value;
			var holidayName = window.document.getElementById("holidayName").value;
			var holidayskinRepeat = $("#holidayskinRepeat").combobox("getValue");
			if($.trim(holidayskinName) == '') {
				$.messager.alert('提示', '请输入皮肤名称!', 'info');
				return false;
			}
			if($.trim(holidayName) == '') {
				$.messager.alert('提示', '请输入节假日名称!', 'info');
				return false;
			}
			if(!$('#skinForm').form('validate')) {
				return false;
			}
			if($.trim(holidayskinId) == '') {
				var path = $.trim($("#skinFile").val());
				var suffix = path.substring(path.lastIndexOf('.') + 1, path.length); //suffix:文件后缀
				suffix = suffix.toLowerCase();
				if(path == "") {  //没有选择文件
					$.messager.alert('提示', '请选择皮肤包!', 'info');
					return false;
				} else if(suffix != "" && suffix != "zip" && suffix != "rar" && suffix != "tar" && suffix != "war" && suffix != "jar" && suffix != "7z") {
					$.messager.alert('提示', '皮肤包类型错误,请重新选择!', 'info');
					return false;
				}
			}
			if($.trim(holidayskinRepeat) == '') {
				$.messager.alert('提示', '请选择是否循环使用!', 'info');
				return false;
			}
			$('#skinForm').form('submit', {
				url      : "/pc/holidaySkin/saveSkin.htm",
				onSubmit : function() {
					return $('#skinForm').form('validate');
				},
				success  : function(data) {
					if(data == "SUCCESS") {
						$.messager.alert('提示', '保存成功!', 'info', function() {
							$('#skinForm').form('clear');
							Header.winHide('skinWindow');
							window.location.href = "/pc/holidaySkin/main.htm";	
						});
					} else if(data == "0") {
						$.messager.alert('提示', '开始时间应小于结束时间!', 'info');
					} else if(data == "1") {
						$.messager.alert('提示', '该段时间内已有皮肤包正在使用,请确认!', 'info');
					} else if(data == "FAIL") {
						$.messager.alert('提示', '皮肤包上传失败!', 'info');
					} else {
						$.messager.alert('提示', '保存失败!', 'error');
					}
				}
			});
		},
		
		/**
		 * 删除皮肤.
		 * @returns
		 */
		deleteSkin : function() {
			var skinId = $("[name='radioName']:checked").val();
			if(skinId == null || skinId == "") {
				$.messager.alert('提示', '请选择皮肤!', 'info');
				return false;
			}
			$.messager.confirm('确认', '确定要删除吗?', function(btn) {
				if(btn) {
					$.post('/pc/holidaySkin/delete.htm', {'skinId' : skinId}, function(data) {
						if(data == "SUCCESS") {
							$.messager.alert('提示', '删除成功!', 'info', function() {
								window.location.href = "/pc/holidaySkin/main.htm";	
							});
						} else {
							$.messager.alert('提示', '删除失败!', 'error');
						}
					});
				}
			});
		}
	};
}();

$(function(){
	$('#holidayskinStartDate').datetimebox({
		editable    : false,
		showSeconds : true,
		required    : true
	});
	$('#holidayskinEndDate').datetimebox({
		editable    : false,
		showSeconds : true,
		required    : true
	});
});