   /**
	添加条件
	**/
	function addCondition(masterId)
	{
		location.href='/pc/right/add_master_rules.htm?master_id=' + masterId;
	}
	/**
	删除条件组
	*/
	function delCondition(index,id)
	{
		if(confirm('确实要删除此条件吗？'))
		{
			$.get(
				"/pc/right/deleteMasterRules?rules_id=" + id,	//url
				function(ret){
					//删除成功后的回调函数，将当前的div隐藏rhj
					$("#conditions"+index).hide(300);
				}
			);
		}
	}
	/**
	编辑条件组
	*/
	function editCondition(master_id,rules_id)
	{
		//编辑条件组
		location.href = "/pc/right/add_master_rules.htm?type=edit&master_id=" + master_id + "&rules_id=" + rules_id;
	}
	/**
	json解析
	**/
	function revertJson(jsonStr,index)
	{
		var tableBody = $("#tab"+index+" tbody").html();
		var obj = $.parseJSON(jsonStr);
		for ( var i = 0;i<obj.rules.length;i++)
		{
			var bodyContent = "<tr><td>";
			bodyContent += revertField(obj.rules[i].field);
			bodyContent += "&nbsp;&nbsp;" + revertOperator(obj.rules[i].op);
			bodyContent += "&nbsp;&nbsp;" + obj.rules[i].value;
			if(obj.rules[i].field == 'headship_level')
				bodyContent += "&nbsp;&nbsp;(可以查看上"+obj.rules[i].value+"级领导)";
			else if(obj.rules[i].field=="department_level")
				bodyContent += "&nbsp;&nbsp;(可以查看上"+obj.rules[i].value+"级部门用户)";
			bodyContent += "</tr></td>";
			$("#tab"+index+" tbody").html(tableBody + "<tr><td>" + bodyContent + "</td></tr>");
		}
	}
	
	/**
	字段解释器
	**/
	function revertField(field)
	{
		var ret = "";
		switch(field)
		{
			case "headship_level":
				ret = "岗位级别";
				break;
			case "department_level":
				ret = "部门级别";
				break;
			case "upleader":
				ret = "分管领导";
				break;
		}
		return ret;
	}
	/**
	操作符解释
	**/
	function revertOperator(op)
	{
		var ret = "";
		switch(op)
		{
			case "more":
				ret = "大于";
				break;
			case "morethen":
				ret = "大于等于";
				break;
			case "less":
				ret = "小于";
				break;
			case "lessthen":
				ret = "小于等于";
				break;
			case "equal":
				ret = "等于";
				break;
			case "notequal":
				ret = "不等于";
				break;
		}
		return ret;
	}