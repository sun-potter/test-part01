<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%> 
<!DOCTYPE html >
<html>
<head>
<base href="<%=basePath%>" >
<meta charset="UTF-8">
<title>管理系统</title>
<link rel="shortcut icon" href="static/images/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.3/themes/icon.css">
<script type="text/javascript" src="static/jquery-easyui-1.5.3/jquery.min.js"></script>
<script type="text/javascript" src="static/jquery-easyui-1.5.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:60px;background:#B3DFDA;padding:10px">
		<div style="display:inline-block;float:left;margin-left:50px;font-size: 21px;line-height: 40px;color:white;">管理系统</div>
			<div style="display:inline-block;float:right;margin-right:20px;height: 40px;line-height: 40px;font-size:16px; ">
				<a href="javascript:void(0)" style="text-decoration:none;" onclick="logout()">退出</a>
			</div>
			<div style="display:inline-block;float:right;font-size: 18px;margin-right:20px;line-height: 40px;color:#338ACC;">您好，${name}</div>
	</div>
	<div  data-options="region:'west',split:true,title:'菜单'" style="width:150px;">
		<div class="easyui-accordion" data-options="fit:true,border:false,selected:false">
			<c:forEach items="${mapMenuList}" var="groupMenu">
				<div title="${groupMenu.key.name}" data-options="iconCls:'icon-ok'" style="overflow:auto;">
					<c:forEach items="${groupMenu.value}" var="secMenu">
						<div style="color:blue;border-bottom: 1px solid #95B8E7;height:30px;text-align: center;line-height: 30px;">
							<a href="javascript:void(0)" onclick="addTabs('${secMenu.name }','${secMenu.url }');">${secMenu.name }</a>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
	</div>
	<div data-options="region:'south',border:false" style="height:30px;background:#A9FACD;padding:5px;text-align: center;">版权所有，盗版必究</div>
	<div data-options="region:'center',fit:true,border:false" class="easyui-tabs" id="content">
	</div>
		<script>
			function addTabs(title,url){
				if ($('#content').tabs('exists', title)){
					$('#content').tabs('select', title);
				} else {
					 var content = "<iframe scrolling='auto' frameborder='0' src='<%=basePath%>"+url+"' style='width:100%;height:100%;'></iframe>";
					 $('#content').tabs('add',{
					    title:title,
					    content:content,
					    closable:true
					});
				}
			}
			function logout(){
				window.location.href="logout.action";
			}
		</script>
	</body>
</html>