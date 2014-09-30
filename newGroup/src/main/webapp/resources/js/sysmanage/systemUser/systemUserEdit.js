/**
  *系统用户编辑
  **/
//确认密码
$.extend($.fn.validatebox.defaults.rules, {  
    equals: {  
        validator: function(value,param){  
            return value == $(param[0]).val();  
        },  
        message: '密码不一致!'  
    }  
});  

SystemUser = function(){
	return {
		//编辑操作初始化
		editInit:function(){
			var employeeId = $("#employeeIdHidden").val();
			var companyId = $('#companyIdHidden').val();
			SystemUser.companyTree("0");
			SystemUser.roleCombo(companyId);
			if(employeeId){
				$(".isnotEmpoyeHide").css("display","none");
				$(".isEmpoyeHide").show();
				$("#isEmpCheckbox").attr("checked",true);
				SystemUser.departmentTree(companyId);
				$("#employeeIdComboGrid").combogrid({
		    		url:'/pc/employee/employees/'+$('#departmentComboTree').val()+'.htm'
		    	}).load(); 
			}
		},
		//初始化
		init:function(){
			var isAdmin = $("#admin").val();
			var companyId = "0";
			if(isAdmin != "isAdmin"){//超级管理员
				companyId = isAdmin;
			}
			SystemUser.companyTree(companyId);
		},
		//公司树初始化
		companyTree:function(companyId){
			$('#companyComboTree').combotree({  
			    url: '/pc/company/companyTree/'+companyId+'.htm',  
			    required: true,
			    onClick: function(node){
			    	var companyId = node.id;
			    	SystemUser.roleCombo(companyId);//初始化角色
			    	var flag = $("#isEmpCheckbox").is(":checked");//是否从员工表中查询获取数据
			    	if(flag){
			    		SystemUser.departmentTree(companyId);
			    	}
				}
			}); 
		},
		//是否从员工中获取数据操作
		isEmployeeOpt:function(){
			$("#isEmpCheckbox").click(function(){
				var flag = $(this).is(":checked");
				if(flag){
					$(".isnotEmpoyeHide").hide("slow",function(){
						$(".isEmpoyeHide").show("slow");
					});
				}else{
					$(".isEmpoyeHide").hide("slow",function(){
						$(".isnotEmpoyeHide").show("slow");
					});
				}
				//$("#systemUserFrom").form("clear");
				SystemUser.cancelUpLoadPhoto();//图片取消操作
				
			});
		},
		//初始化角色
		roleCombo:function(companyId){
			$("#roleCombo").combobox({
				url:'/pc/role/roleCombo/'+companyId+'.htm',
				valueField:'id',
				textField:'text',
				//multiple:true,
				panelHeight:'auto',
				required:true
			});
		},
		//部门树
		departmentTree:function(companyId){
			$('#departmentComboTree').combotree({  
				url:'/pc/department/departmentTree/'+companyId+'.htm',
			    required: true,
			    onClick: function(node){
			    	var deptId = node.id;
			    	$("#employeeIdComboGrid").combogrid({
			    		url:'/pc/employee/employees/'+deptId+'.htm'
			    	}).load(); 
				}
			}); 
		},
		
		//员工选择
		employeeSelect:function(){
		    $("#employeeIdComboGrid").combogrid({  
		        panelWidth:500,  
		        pagination:true,
		        fitColumns:true,
		        editable:false,
		        striped:true,
		        rownumbers:true,
		        singleSelect:true,
		        autoRowHeight:true,
		        nowrap:true,
		        loadMsg:"载入中...",
		        idField:'employeeId',  
		        textField:'employeeName',  
		        columns:[[  
		            {field:'employeeId',title:'employeeId',hidden:true},  
		            {field:'employeeCode',title:'员工编码',width:150},  
		            {field:'employeeName',title:'真实姓名',width:100},  
		            {field:'idCard',title:'身份证号码',width:150},  
		            {field:'mobile',title:'手机',width:90}  
		        ]]  
		    });  
		},
		
		//打开人员选择
		openEmployeeWindow:function(companyId){
			$('#empSelectWindow').window({
			     title: "人员选择",
			     href:"/pc/systemUser/departmentTree/"+companyId+".htm",
			     height: "500",
			     width: "700",
			     shadow: false,
				 cache:  false,
				 collapsible:false,
				 minimizable:false,
				 maximizable:false,
				 resizable:false,
				 top:(document.body.clientHeight-500)/2 ,   
		         left:(document.body.scrollWidth-700)/2,
				 modal:true
			}).window("open");
		},
		//取消上传图片操作
		cancelUpLoadPhoto:function(){
			var photoAddrHidden = $('#photoAddrHidden').val();
			if(photoAddrHidden){
				$.post('/pc/systemUser/deleteUpLoadedPhoto.htm',{photoAddr:photoAddrHidden},function(){
					$('#photoAddrHidden').val("");
					$("#systemUserImg").attr("src","");
				});
			}
		},
		//图片上传
		upLoadPhoto:function(){
			if(uploadCount > 0){//删除先前上传的照片
				SystemUser.cancelUpLoadPhoto();
			}
			uploadCount++;
			$("#systemUserPhotoForm").form("submit",{
				url:"/pc/systemUser/uploadPhoto.htm",
				onSubmit:function(){//验证上传内容是否为空
					var flag = true;
					if(!$('#systemUserPhotoFile').val()){
						Ict.alert("上传图片为空");
						flag = false;
					}
					return flag;
				},
				success:function(data){
					if(data == "ERROR"){
						Ict.error("上传失败，请上传正确的图片，或稍后再试。");
					}else{
						$('#photoAddrHidden').val(data);
						$("#systemUserImg").attr("src",data);
					}
				}
			});
		},
		//保存操作
		saveSystemUser:function(){
			$('#systemUserFrom').form('submit', {  
			    url:"/pc/systemUser/save.htm",  
			    onSubmit: function(){  
			    	return $("#systemUserFrom").form("validate");
			    },  
			    success:function(data){  
			    	if(data == "SUCCESS"){
			    		Ict.info("保存成功!",function(){
			    			Ict.closeWin();
			    			$("#systemUserGrid").datagrid("reload");
			    		});
			    	}else{
			    		Ict.error("保存失败!稍后重试。");
			    	}
			    }
			});  
		}
	};
}();

$(function(){
	SystemUser.employeeSelect();//初始化员工数据表格
	var userRoleId = $("#userRoleIdHidden").val();
	if(userRoleId){
		SystemUser.editInit();
	}else{
		SystemUser.init();
	}
	SystemUser.isEmployeeOpt();
	uploadCount=0;
});


