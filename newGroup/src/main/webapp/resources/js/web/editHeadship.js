editHeadship = function(){
	return {
		addTr:function(){
			var Con=document.getElementById("addHeadship");
			var row = Con.insertRow(Con.rows.length);
		    var cell0 = row.insertCell(0);
		    cell0.innerHTML =  row.rowIndex.toString();
		    var cell1 = row.insertCell(1);
		    cell1.innerHTML = '<input  name="headshipName" type="text" class="opand" value="" size="10" />';
		    var cell2 = row.insertCell(2);
		    cell2.innerHTML = '<input  name="headshipLevel" type="text" class="opand" value="" size="15" />';
		    var cell2 = row.insertCell(3);
		    cell2.innerHTML = '<input name="description" type="text" class="opand" value="" size="40" />';
		    
		},
		//打开增加岗位窗口
		addHeadship : function(){
			Header.winShow('window_headship');
			$('#toggleDelEdite').text('新增岗位');
			$("#headshipId").val("");
			$("#headshipName").val("");
			$("#headshipLevel").val("");
			$("#description").val("");
		},
		
		//打开编辑岗位信息窗口并显示岗位信息
		updateHeadship : function(headshipId){
			Header.winShow('window_headship');
		    $('#toggleDelEdite').text('编辑岗位信息');	
		    $.ajax({
				   type: "POST",
				   url: "/pc/pheadship/detail.htm",
				   data: {'headshipId' :headshipId},
				   dataType: "JSON",
				   success: function(data){
					   $("#headshipId").val(data[0].headshipId);
					   $("#headshipName").val(data[0].headshipName);
					   $("#headshipLevel").val(data[0].headshipLevel);
					   $("#description").val(data[0].description);
					   }
				});
		},
		
		//保存信息
		saveHeadship : function(){
			/*var headshipNameStr = document.getElementsByName('headshipName');
			var headshipLevelStr = document.getElementsByName('headshipLevel');
			if(headshipName == null || headshipName == ""){
				$.messager.alert('提示','岗位名字不能为空!','info');
				return;
			}
			if(headshipLevel == null || headshipLevel == ""){
				$.messager.alert('提示','岗位级别不能为空!','info');
				return;
			}*/
			$('#hs_form').form('submit',{
				url:"/pc/pheadship/plSaveHeadship.htm",
				onSubmit: function(){
					return $('#hs_form').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#hs_form').form('clear');
							//Header.winHide('window_headship');
							window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
						});				
					}else if(data=='NAME'){
						$.messager.alert('提示','岗位名字重复','error');	
					}else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});
		},
		//保存信息
		updateHeadship : function(){
			var headshipName = $("#headshipName").val();
			var headshipLevel = $("#headshipLevel").val();
			if(headshipName == null || headshipName == ""){
				$.messager.alert('提示','职位名不能为空!','info');
				return;
			}
			if(headshipLevel == null || headshipLevel == ""){
				$.messager.alert('提示','职位级别不能为空!','info');
				return;
			}
			$('#hs_form').form('submit',{
				url:"/pc/pheadship/saveHeadship.htm",
				onSubmit: function(){
					return $('#hs_form').form('validate');
				},
				success:function(data){
					if(data=='SUCCESS'){
						$.messager.alert('提示','保存成功','info',function(){
							$('#hs_form').form('clear');
							window.location.href = "/pc/pheadship/main.htm?" + $("#searchForm").serialize();
						});				
					}else if(data=='NAME'){
						$.messager.alert('提示','岗位名字重复','error');	
					}else {
						$.messager.alert('提示','保存失败','error');	
					}
				}
			});
		}

	};
}();

$(function(){
	
});