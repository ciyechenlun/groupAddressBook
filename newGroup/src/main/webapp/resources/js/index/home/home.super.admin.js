/**
 * 欢迎页面对应的JS
 * 
 * @author li.sanlai@ustcinfo.com
 */


Home = function() {
	
	return {
		
		init:function(){
			Home.initPortal();
			Home.initStyle();
			Home.initServReggrid();
		},
		
		//初始化portal
		initPortal : function(){
			
			$.pp.portal({
				border:false,
				fit:true
			});
			
		},
		
		//初始化样式
		initStyle : function(){
			var height = eval($.pp.height());
			var width = eval($.pp.width()-50);
//			var halfHeight = eval(height/2);
//			$.qydd.width(width);
//			$.qydd.height(height);
//			$.qyddTab.width(width);
//			$.qyddTab.height(height);
		},
		
		//服务开通
		servReg:function(id,ecName,ecCode,priority,adminUser,adminUfid){
			$('body').mask('正在开通……');
			$.ajax({
				  url: "/interface/registServ.htm",
				  type:'post',
				  timeout:300000,
				  data:{
				  	'ecName':ecName,
				  	'ecCode':ecCode,
				  	'priority':priority,
				  	'adminUser':adminUser,
				  	'adminUfid':adminUfid
				  },
				  context: document.body,
				  success: function(data){
				  	$('body').unmask();
				  	if(data.success==true){
				  		Ict.slideMsg('<font style="color:green;">' + ecName + "&nbsp;&nbsp;已经开通服务 </font>",1500);
				  		//设置订单状态
			  			$.ajax({
							  url: "/interface/updateServOrder.htm",
							  type:'post',
							  data:{
							  	'id':id,
							  	'ecName':ecName
							  },
							  context: document.body,
							  success:function(data){
					  	  		$('#servReggrid').datagrid('reload');
							  }
			  			});
				  		
				  		/*Ict.info('服务开通完成',function(){
				  			//设置订单状态
				  			$.ajax({
								  url: "/interface/updateServOrder.htm",
								  type:'post',
								  data:{
								  	'id':id,
								  	'ecName':ecName
								  },
								  context: document.body,
								  success:function(data){
						  	  		$('#servReggrid').datagrid('reload');
								  }
				  			});
				  	  	});*/
				  	}else{
				  		Ict.error(data.msg);
				  	}
				  	  	
				  },
				  error:function(){
				  		$('body').unmask();
				  		Ict.info('服务开通失败');
				  }
			});
		},
		
		//初始化服务开通订单列表
		initServReggrid : function(){
			$('#servReggrid').datagrid({
				url:'/interface/servOrderList.htm?optType=0',
				fit:true,
				idField:'id',
				striped:true,
				nowrap:true,
				loadMsg:'加载中 ……',
				animate:true,
				checkbox:true,
				pagination:true,
				rownumbers:true,
				fitColumns:true,
				border:true,
				columns:[[
					{field:'id',title:'',width:50,checkbox : true},
					{field:'ecName',title:'企业名称',width:120},
				    {field:'ecCode',title:'企业编码',width:100},
//				    {field:'siName',title:'SI名称',width:80},
//				    {field:'siCode',title:'SI编码',width:80},
//				    {field:'serviceName',title:'服务名称',width:120},
//				    {field:'serviceCode',title:'服务编码',width:100},
				    {field:'priority',title:'管理员手机',width:80},
				    {field:'adminUser',title:'管理员用户名',width:80,formatter: function(value,row,index){
				    	return value + '@' + row.ecCode;
					}},
				    {field:'accessNo',title:'登录地址',width:180,formatter: function(value,row,index){
				    	if(value && value!=''){
					    	var url = basePath + 'login/' + value;
							return url;
				    	}else{
				    		return '';
				    	}
					}},
				    {field:'adminUfid',title:'管理员密码',width:60},
				    {field:'state',title:'订单状态',width:50,formatter: function(value,row,index){
						if (value=='1'){
							return '<font style="color:green;">已处理</font>';
						} else {
							return '<font style="color:red;">未处理</font>';
						}
					}},
					{field:'opt',title:'操作',width:30,formatter: function(value,row,index){
						return row.state=='1'?'<font style="color:green;">已开通</font>':'<a href="javascript:Home.servReg(\''+row.id+'\',\''+row.ecName+'\',\''+row.ecCode+'\',\''+row.priority+'\',\''+row.adminUser + '@' + row.ecCode + '\',\''+row.adminUfid+'\')">开通</a>';
					}}
				]]
			});
			
		}
	};
}();

$(function(){
	$.pp = $('#portal');
	$.qydd = $('#qydd');
	$.grid = $('#grid');
	$.qyddTab = $('#qyddTab');
	$.servReggrid = $('#servReggrid');
	//
	Home.init();
});
