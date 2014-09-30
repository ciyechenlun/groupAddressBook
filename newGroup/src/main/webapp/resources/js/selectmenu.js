//头部菜单选择

function $fun(objId){
 return document.getElementById(objId);
}
function changeId(idName,obj){
 if($fun(idName)){
	$fun(idName).id="";
 }
 obj.id=idName;
 obj.blur();
}