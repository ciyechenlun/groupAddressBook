<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团通讯录查看通讯录</title>
<link rel="stylesheet" type="text/css" href="/resources/css/css.css"/>
<link rel="stylesheet" type="text/css" href="/resources/css/starit.base.css"/>
<script type="text/javascript" src="/resources/scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/resources/js/index/header.js"></script>
<!--js-->
<link rel="stylesheet" href="/resources/css/jquery.treeview.css"/>
<script type="text/javascript" src="/resources/scripts/jquery.min.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery.cookie.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery.treeview.js"></script>
<!--js-->
<script type="text/javascript">
<!--

$(function(){
	
	$(".bnewleft01").mouseover(function(){
		$(this).addClass("bnewlefton");
	});
	$(".bnewleft01").mouseout(function(){
		$(this).removeClass("bnewlefton");
	});

	$("#browser").treeview({
		collapsed: true,
		unique: true,
		persist: "location"
	});

});
//-->
</script>
</head>

<body>
<!--top-->
<%@include file="../top.jsp" %>

<!--body-->
<div class="bodyqj newbody">
    <ul>
        <li>集团通讯录</li>
    </ul>
</div>

<!--body正文-->
<div class="bodyqj bodybg fn_clear">

<!--left-->
<%@include file="../left.jsp"%>
<!--树-->
<div class="ck_left">
    <div class="margintop">
    <ul id="browser" class="filetree">
  		   <li><span class="folder">安徽省移动公司</span>
			<ul>
				<li><span class="folder">信息系统部</span>
					<ul id="folder21">
						<li><span class="file">ICT支撑中心</span></li>
						<li><span class="file">运用管理部</span></li>
                        <li><span class="file">信息安全部</span></li>
					</ul>
				</li>
				<li><span class="file">市场部</span></li>
                <li><span class="file">网络部</span></li>
			</ul>
		</li>
	</ul>
    </div>
</div>
<!--树end-->
<!--right-->
<div class="ck_right">
    <div class="ck_rightbg">ICT支撑中心</div>
	<div class="margintop ">
        <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="41%" class="ck_rwenzi">产品支持部</td>
            <td width="20%"><input name="" type="submit" class="bottom_01" value="编辑用户" /></td>
            <td width="20%"><input name="" type="submit" class="bottom_01" value="增加用户" /></td>
            <td width="19%"><input name="input" type="submit" class="bottom_01" value="删除用户" /></td>
          </tr>
        </table>
    </div>
    <div class="margintop ck_top">
    <table class="ck_table margintop">
      <tr>
        <td width="60" class="ck_table1"><input name="" type="radio" value="" /></td>
        <td width="132" class="ck_table2"><img src="images/images_01.png" width="100" height="94" /></td>
        <td width="397" class="ck_table4">
        <table class="ck_table5">
          <tr>
            <td class="ck_table1"><img src="images/ck_cion1.png" width="15" height="18" /></td>
            <td class="ck_rwenzi ck_table2">刘洪峰</td>
            <td class="ck_table1"><img src="images/ck_cion1.png" width="15" height="18" /></td>
            <td class="ck_rwenzi">刘洪峰</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion2.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">0551-66788888</td>
            <td><img src="images/ck_cion2.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">66666</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion3.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">13919536482</td>
            <td><img src="images/ck_cion3.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">8010</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion4.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">yanxiang202@163.com</td>
          </tr>
        </table></td>
      </tr>
      </table>
    </div>
     <div class="margintop ck_top">
    <table class="ck_table margintop">
      <tr>
        <td class="ck_table1"><input name="" type="radio" value="" /></td>
        <td class="ck_table2"><img src="images/images_01.png" width="100" height="94" /></td>
        <td class="ck_table4"><table class="ck_table3">
          <tr>
            <td class="ck_table1"><img src="images/ck_cion1.png" width="15" height="18" /></td>
            <td class="ck_rwenzi ck_table2">刘洪峰</td>
            <td class="ck_table1"><img src="images/ck_cion1.png" width="15" height="18" /></td>
            <td class="ck_rwenzi">刘洪峰</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion2.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">0551-66788888</td>
            <td><img src="images/ck_cion2.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">66666</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion3.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">13919536482</td>
            <td><img src="images/ck_cion3.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">8010</td>
          </tr>
          <tr>
            <td><img src="images/ck_cion4.png" width="15" height="18" /></td>
            <td class="ck_rwenzi1">yanxiang202@163.com</td>
          </tr>
        </table></td>
      </tr>
      </table>
    </div>

<!--翻页-->
<div class="fn_clear">
<div class="pagination f-right" >
    <ul class="margintop30 maginright">
        <li class="prev disabled">
        <a href="#">&lt; 上一页</a>
        </li>
        <li class="active">
        <a href="#">1</a>
        </li>
        <li>
        <a href="#">2</a>
        </li>
        <li>
        <a href="#">3</a>
        </li>
        <li>
        <a href="#">4</a>
        </li>
        <li class="next">
        <a href="#">下一页 &gt; </a>
        </li>
    </ul>
</div>
</div>
<!--翻页end-->
</div>

</div>
<!--bottom-->
<div class="bottombg">
<ul>
<li>Copyright © 2013-2015  安徽移动通信 All Rights Reserved 版权所有   维护电话：0551-66666666</li>
</ul>
</div>
</body>
</html>
