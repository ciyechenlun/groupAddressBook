Import = function(){
	return {
		excelImport : function(){
			if($("#importFile").val()==""){
				$.messager.alert("提示","<br><center>请选择文件</center>");
				return;
			}
			$('#importDivs').show();//打开遮罩
			$("#progressbar").progressbar( "option", "value", false );//初始化进度条的值
			$( ".progress-label" ).text("正在导入...");//初始化进度显示
			$('#importForm').form('submit',{
				url:'/pc/import/excelImport.htm',
				success:function(data){
					clearInterval(timer);//关闭定时器
					$.post("/pc/import/init.htm");//初始化操作行与总行数
					$('#importDivs').hide();//关闭遮罩
					var result = data;
					if(result.indexOf("导入成功")>-1){
						path = result.substr(4);
						result = "导入成功";
						//$.post("/pc/userCompany/autoUpdateShortNum.htm?path="+path);
					}
					$.messager.show({
						title:'导入结果',
						width: 300,
						height: 400,
						timeout:5000000,
						msg:result
					});
				}
			});
			//定时获取后台进度
			  timer=setInterval(function(){
				  $.ajax({
						type: "POST",
						url: "/pc/import/getOperateRate.htm",
						success: function(data){
							if(data!="NaN"&&data!='0'){
								$("#progressbar").progressbar( "value",data);
							}
						}
					}); 
				  
			},500);
			
		},
		
		excelExport : function(){
			$('#excelExport').form('submit',{
				url:"/pc/import/export.htm",
				success:function(data){
					$.messager.alert(data);
				}
			});
			
		},
		//参数obj为input file对象
		excelImport2 : function(){
			if($("#importFile2").val()==""){
				$.messager.alert("提示","<br><center>请选择文件</center>");
				return;
			}
			$('#importDivs').showLoading();
			$('#importForm').form('submit',{
				url:'/pc/userCompany/import.htm',
				async:false,
				success:function(data){
					$('#importDivs').hideLoading();
					var result = data;
					$.messager.show({
						title:'导入结果',
						width: 300,
						height: 400,
						timeout:5000000,
						msg:result
					});
				}
			});
		}
	};
}();

Ict = function(){
	
	return {
		
		//提示消息
		alert:function(msg,fn){
			if(fn){
				$.messager.alert('消息',msg,'',fn);
			}else{
				$.messager.alert('消息',msg);
			}
		},
		
		//消息提示
		info:function(msg,fn){
			if(fn){
				$.messager.alert('消息',msg,'info',fn);
			}else{
				$.messager.alert('消息',msg,'info');
			}
		},
		
		//警告消息
		warn:function(msg,fn){
			if(fn){
				$.messager.alert('警告',msg,'warning',fn);
			}else{
				$.messager.alert('警告',msg,'warning');
			}
		},
		
		//错误消息
		error:function(msg,fn){
			if(fn){
				$.messager.alert('错误',msg,'error',fn);
			}else{
				$.messager.alert('错误',msg,'error');
			}
		},
		
		
		/**
		 * 确认消息
		 * demo:
		 * Ict.confirm('yes or no?',function(r){
		 *		if(r){
		 *			Ict.alert('yes');
		 *		}else{
		 *			Ict.alert('no');
		 *		}
		 *	});
		 */
		confirm : function(msg,callback){  
            $.messager.confirm('确认消息', msg, callback);  
        },
        
		//右下角滑动提示框
		slideMsg : function(msg,timeout){
            $.messager.show({  
                title:'提示消息',  
                msg:msg, 
                timeout:timeout?timeout:3000,
                showType:'show'  
            });  
        }
	};
}();

$(function(){
	//进度条显示与操作
	 var progressbar = $( "#progressbar" ),
	 progressLabel = $( ".progress-label" );
	 progressbar.progressbar({
		 value: false,
		 change: function() {
			 progressLabel.text( progressbar.progressbar( "value" ) + "%" );
		 },
		 complete: function() {
			 progressLabel.text( "导入完成!" );
		 }
	 });
	 $('.ui-progressbar').css("margin-top",document.body.clientHeight/2);
	 $('.ui-progressbar').css("margin-left",(document.body.scrollWidth-600)/ 2);

	 
	var currentColumn = '';
	var fileSaveName = '';
	
	//$("#importButton").on({
	//	'click' : Import.excelImport
	//});
	//$("#importButton2").on({
	//	'click' : Import.excelImport2
	//});
	//var type = window.parent.document.getElementById("companyType").value;
	//if(type == "bnewleft01 bnewleft01b liGetSelected" || 
	//		type == "bnewleft01 bnewleft01b bnewlefton liGetSelected"){
		$("#org").hide();
		$("#not_org").show();
	//}
	$("#companyId").val(window.parent.document.getElementById("companyId").value);
	
	/**
	 * 格式化文件大小
	 */
	formatFileSize = function(fileSize){
		if(fileSize<500){
			return fileSize + 'B';
		}else if(eval(fileSize/1024)<500){
			return toDecimal2(eval(fileSize/1024)) + 'KB';
		}else{
			return toDecimal2(eval(fileSize/1024/1024)) + 'MB';
		}
	};
	
	//保留两位小数   
    //功能：将浮点数四舍五入，取小数点后2位  
	toDecimal = function (x) {  
        var f = parseFloat(x);  
        if (isNaN(f)) {  
            return;  
        }  
        f = Math.round(x*100)/100;  
        return f;  
    };


    //制保留2位小数，如：2，会在2后面补上00.即2.00  
	toDecimal2 = function (x) {  
        var f = parseFloat(x);  
        if (isNaN(f)) {  
            return false;  
        }  
        var f = Math.round(x*100)/100;  
        var s = f.toString();  
        var rs = s.indexOf('.');  
        if (rs < 0) {  
            rs = s.length;  
            s += '.';  
        }  
        while (s.length <= rs + 2) {  
            s += '0';  
        }  
        return s;  
    };
	
	//资源文件
	$.uploadFile = $('#uploadFile');
	$.smartUploadBtn = $('#smartUploadBtn'); 
	$.progressInfo = $('#progressInfo');
	//发布资源保存路径
	var PUBLISH_SAVE_URL = "/pc/import/smart/upload.htm";
	
	// 初始化上传组件
	initUpload = function(){
		
		$.uploadFile.fileupload({
			dataType: 'json',
			type	: 'POST',
			maxFileSize : 52428800,
			minFileSize	: 1024,
			paramName : 'Filedata',
			acceptFileTypes : /(\.|\/)(xls|xlsx|csv)$/i,
		    url: PUBLISH_SAVE_URL,
		    limitMultiFileUploads:1,
		    sequentialUploads: true,
		    add:function (e,data) {
		        $.each(data.files, function (index, file) {
		            $('#msg').html(file.size<1024?'文件大小不能小于1KB':'文件名：' + file.name + '&#12288;&#12288;文件大小：' + formatFileSize(file.size))
		            	.css({'padding':'5px 0px 0px 0px'})
		            	.slideDown('fast');
		        });
		        $.smartUploadBtn.on({
		        	'click' : function(){
		        		data.submit();
		        	}
		        });
		        FILE_QUEUE_SIZE = 1;
		    },
		    submit:function(e,data){
			},
		    start:function (e) {
		    	$.blockUI({ message: $('#progressInfo') }); 
		    },
		    progressall:function (e, data) {
		        var progress = parseInt(data.loaded / data.total * 100, 10);
		        $.progressInfo.html('&#12288;<img src="/resources/img/icons/waiting.gif"/>&nbsp;&nbsp;正在上传文件，请稍等……' + progress + '%')
		        	.css({'color':'#68C6E4'})
		        	.show();
		    },
		    done:function (e, data) {
		    	$.unblockUI();
		    	fileSaveName = data.result.savename;
		    	var nextBtn = '<div class="lis_btn"><a id="step2Btn" href="javascript:praseExcel(\''+fileSaveName+'\')">&nbsp;&nbsp;&nbsp;开始解析</a></div>';
		    	if(data.result.success){
		    		$.progressInfo.html('&#12288;' + data.result.message)
	        		.css({'color':'#68C6E4'})
	        		.show();
		    	}else{
		    		$.progressInfo.html('&#12288;' + data.result.message)
	        		.css({'color':'red'})
	        		.show();
		    	}
		    	
		    	$('#nextStepBtnDiv').html(nextBtn).slideDown();
		    	
		    },
		    fail:function (e, data) {
		    	$.unblockUI();
		    	$.progressInfo.html('&#12288;' + data.result.message)
        		.css({'color':'red'})
        		.show();
		    },
		    always:function(e,data){
		    	FILE_QUEUE_SIZE = 0;
		    }
		});
	};
	
	var parseTimes = 0;
	
	/**
	 * 智能上传第二步，解析
	 */
	praseExcel = function(excelName){
		$.progressInfo.html('&#12288;<img src="/resources/img/icons/waiting.gif"/>&nbsp;&nbsp;正在智能识别表格信息……')
		.css({'color':'#68C6E4'});
		$.blockUI({ message: $('#progressInfo') }); 
		$.ajax({
			type: "POST",
			dataType : "JSON",
			url: "/pc/import/smart/parse.htm",
			data: { 'excelName': excelName},
			success:function(data,textStatus,jqXHR){
				parseTimes ++;
				$.unblockUI();
				var columnNames = data.columnNames;
				var ul = '<ul style="list-style: none;">';
				for ( var i=0;i<columnNames.length;i++) {
					var columnName = columnNames[i];
					ul += '<li><input onclick="changeColName(this);" name="columnName" type="radio" value="'+columnName+'"/>'+columnName+'</li>';
				}
				ul += '</ul>';
				$('#columnNameList').html(ul).show();
				
				if(parseTimes==1){
					$('#columnNameList').dialog({  
						title: '列名称',  
						width: 250,  
						height: 300,  
						closed: true,  
						modal: true  
					});
				}
				//
				var excelRows = data.excelRows;
				var excelValues = data.excel.sort(function(x,y){
					if(x.columnIndex>y.columnIndex){
						return 1;
					}
				});
				var length = excelValues.length;
				var columnValueArr = new Array(length);
				var html = '<table id="previewTable" style="width:'+ 100*length +'px;" class="table_border" cellspacing="0" cellpadding="5px">';
				html += '<tr>';
				for ( var i=0;i<length;i++) {
					html += '<th style="width:130px;"><span class="titleSpan" id="col'+i+'">'+ excelValues[i].columnName +'</span>&#12288;<span><img onclick="showNameList(this,\''+i+'\');" style="width:10px;height:8px;cursor: pointer;" src="/resources/img/icons/down.png"/></span></th>';
					columnValueArr[i] = excelValues[i].columnValues;
				}
				html += '</tr>';
				for (var j=0;j<excelRows;j++) {
					html += '<tr>';
					for ( var k=0;k<length;k++) {
						var v = columnValueArr[k][j];
						v =	(typeof(v)=='undefined')?'':v;
						html += '<td style="width:130px;">'+v+'</td>';
					}
					html += '</tr>';
				}
				
				html += '</table>';
				$('#previewDivTitle').show().attr("style","margin: 0 10px 10px 10px;text-align:left;color:#697A83");
				console.log(html);
				$('#previewDiv').html(html).slideDown('fast');
				//更换下一步按钮事件
		    	var nextBtn = '<div class="lis_btn" style="padding-left:10px"><a id="step3Btn" href="javascript:step3()">&nbsp;&nbsp;&nbsp;导入数据</a></div>';
		    	$('#step2Btn').html('&nbsp;&nbsp;&nbsp;重新解析');
		    	if(parseTimes==1){
		    		$('#nextStepBtnDiv').html($('#nextStepBtnDiv').html()+nextBtn);
		    	}
			},
			error:function(jqXHR,textStatus,errorThrown){
				$.unblockUI();
				Ict.error('智能解析失败,请重试！');
			}
		});
	};
	
	/**
	 *显示表头名称列表
	 */
	showNameList = function(el,id){
		currentEl = el;
		currentColumn = 'col'+id;
		$('#columnNameList').dialog('open');
	};
	
	/**
	 * 改变列名
	 */
	changeColName = function(el){
		var colName = $(el).val();
		$('#'+currentColumn).html(colName);
		$('#columnNameList').dialog('close');
	};
	
	/**
	 * 第三步
	 */
	step3 = function(){
		$.messager.confirm('提示信息','确认结果预览中列名与数据对应完全正确并按此导入?',function(result){ 		
			if (result){
				var paramStr = '<columns>';
				$('.titleSpan').each(function( index ) {
					paramStr += '<column>';
					paramStr += '<index>'+ index + '</index>';
					paramStr += '<title>'+ $(this).text() + '</title>';
					paramStr += '</column>';
				});
				paramStr += '</columns>';
				
				$.progressInfo.html('&#12288;<img src="/resources/img/icons/waiting.gif"/>&nbsp;&nbsp;正在导入,请稍等…………')
				.css({'color':'#68C6E4'});
				$.blockUI({ message: $('#progressInfo') }); 
				//提交解析结果到后台，例如上传的文件内容，重写一份符合模板导入的excel文件
				$.ajax({
					url: "/pc/import/smart/writeTemplateWorksheet.htm",
					type:'post',
					context: document.body,
					data:{
						'excelName':fileSaveName,
						'columns' : paramStr
					},
					dataType : 'json',
					success:function(data,textStatus,jqXHR){
						$.unblockUI();
						var success = data.success;
						var msg = data.msg;
						if(success==true){
							Ict.alert(msg);
						}else{
							Ict.error(msg);
						}
					},
					error:function(jqXHR,textStatus,errorThrown){
						$.unblockUI();
						Ict.error('导入失败，请尝试用模板导入！');
					}
				});
			}
		}); 
	};
	
	initUpload();
});