/**
 * 定位任务设置编辑
 */


LocatUserEdit = function(){
	var times = [];
	return {
		init:function(){
			LocatUserEdit.timeInit();
			LocatUserEdit.timeCombox();
			LocatUserEdit.cycleModuleInit();
			LocatUserEdit.weekCodeCheck();
			//修改操作 
			var weeks = $("#weeksHidden").val();
			if(weeks){
				var weekCodes = weeks.split(",");
				for(var i=0;i<weekCodes.length;i++){
	    			$("#weekcode"+weekCodes[i]).attr("checked",true).next().css("color","red");
	    		}
			}
		},
		save:function(){
			$("#locatUserEditForm").form("submit",{
				url:'/pc/locatUser/save.htm',
				onSubmit:function(){
					return $("#locatUserEditForm").form("validate");
				},
				success:function(data){
					if(data == "SUCCESS"){
						Ict.alert("保存成功!",function(){
							Ict.closeWin();
							$('#locatUserGrid').datagrid("reload");
						});
					}else{
						Ict.error("保存失败，稍后重试!");
					}
				}
			});
		},
		cycleModuleInit:function(){
			$('#cycleModule').combobox({  
			    url:'/pc/cycleModule/cycleModulesCombox.htm',  
			    valueField:'id', 
			    textField:'text',
			    editable:false,
			    panelHeight:'auto',
			    onSelect:function(record){
			    	$("input[name='weekCode']").attr("checked",false).next().css("color","black");
			    	$.post("/pc/cycleModule/cycleModulesCombox.htm",{cycleModuleId:record.id},function(data){
			    		$("input[name='cycleModuleName']").val(data[0].cycleModuleName);
			    		$("#cycleStarttime").combobox("setValue",data[0].cycleStarttime);
			    		$("#cycleEndtime").combobox("setValue",data[0].cycleEndtime);
			    		$("#cycleStationfreq").numberbox("setValue",data[0].cycleStationfreq);
			    		var weekCodes = data[0].weeks;
			    		for(var i=0;i<weekCodes.length;i++){
			    			$("#weekcode"+weekCodes[i].week_code).attr("checked",true).next().css("color","red");
			    		}
			    	});
			    }
			});  
		},
		timeCombox:function(){
			$("#cycleStarttime").combobox({
				valueField: 'id',  
		        textField: 'text',  
				data:times,
				editable:false,
				required:true,
			});
			$("#cycleEndtime").combobox({
				valueField: 'id',  
		        textField: 'text',  
				data:times,
				editable:false,
				required:true,
			});
		},
		weekCodeCheck:function(){
			$("input[name='weekCode']").click(function(){
				if($(this).is(":checked")){
					$(this).next().css("color","red");
				}else{
					$(this).next().css("color","black");
				}
			});
		},
		timeInit:function(){
			for(var i=0;i<24;i++){
				var data = {id:"",text:""};
				var timeValue = i+":00";
				if(i < 10){
					timeValue = "0"+timeValue;
				}
				data.id = timeValue;
				data.text = timeValue;
				times.push(data);
			}
		}
	};
}();

$(function(){
	LocatUserEdit.init();
});
