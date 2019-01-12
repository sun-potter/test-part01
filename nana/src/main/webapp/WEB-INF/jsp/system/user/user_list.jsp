<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%> 
<!DOCTYPE html >
<html>
<head>
<base href="<%=basePath%>" >
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.3/themes/icon.css">
<script type="text/javascript" src="static/jquery-easyui-1.5.3/jquery.min.js"></script>
<script type="text/javascript" src="static/jquery-easyui-1.5.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"></script>
<meta charset="UTF-8">
<title>用户列表</title>
</head>
<body>
	<table id="dg" class="easyui-datagrid"
		style="width:90%;min-height:500px" 
		data-options="
		url:'<%=basePath%>user/listPageAjax.do',
		iconCls:'icon-save',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		toolbar:'#toolbar',
		sortName:'sort',
		sortOrder:'asc'
		">
		<thead>
			<tr>
				<th data-options="field:'name',width:120,align:'center',sortable:false">用户名称</th>
				<th data-options="field:'username',width:100,align:'center',sortable:false">登录名</th>
				<th data-options="field:'rights',width:80,align:'center',sortable:false">权限标志</th>
				<th data-options="field:'deptName',width:100,align:'center',sortable:false">部门</th>
				<th data-options="field:'roleName',width:100,align:'center',sortable:false">角色</th>
				<th data-options="field:'statusName',width:80,align:'center',sortable:false">启用</th>
				<th data-options="field:'tip',width:300,align:'center',sortable:false">说明</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<table>
			<tr>
				<td>用户名称:</td>
				<td>
					<input id="queryName" class="easyui-textbox" style="width:180px">
				</td>
				<td>部门:</td>
				<td>
					<select id="queryDept" class="easyui-combobox" data-options="required:true,panelHeight: 'auto',editable:false" style="width:180px">
					    <option value="">全部</option>
						<c:forEach items="${deptList }" var="dept">
							<option value="${dept.id }">${dept.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>角色:</td>
				<td>
					<select id="queryRole" class="easyui-combobox" data-options="required:true,panelHeight: 'auto',editable:false" style="width:180px">
					    <option value="">全部</option>
						<c:forEach items="${roleList }" var="role">
							<option value="${role.id }">${role.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>是否启用:</td>
				<td>
					<select id="queryStatus" class="easyui-combobox" data-options="required:true,panelHeight: 'auto',editable:false" style="width:180px">
					    <option value="">全部</option>
						<option value="1" >启用</option>
						<option value="2" >未启用</option>
					</select>
				</td>
				<td>
					<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
				</td>
			</tr>
		</table>
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="initAdd()">新建用户</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="initEdit()">修改用户</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="initDel()">删除用户</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		data-options="iconCls:'icon-save',resizable:true,modal:true"
		style="width:400px;height:400px;padding:10px 20px;top:50px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm"  method="post"
			action="" data-options="novalidate:true" enctype="multipart/form-data">
			<input type="hidden" id="id" name="id">
			<table>
				<tr>
					<td>用户名称:</td>
					<td><input id="name" name="name" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>登录名:</td>
					<td><input id="username" name="username" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>密码:</td>
					<td><input id="password" type="password" name="password" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input id="rpwd" name="rpwd" type="password" class="easyui-textbox easyui-validatebox" required="required" validType="equals['#password']" style="width:180px"></td>
				</tr>
				<tr>
					<td>部门:</td>
					<td>
						<select id="deptId" name="deptId" class="easyui-combobox easyui-validatebox" data-options="required:true,editable:false" style="width:180px">
							<c:forEach items="${deptList }" var="dept">
								<option value="${dept.id }">${dept.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>角色:</td>
					<td>
						<select id="roleId" name="roleId" class="easyui-combobox easyui-validatebox" data-options="required:true,editable:false" style="width:180px">
							<c:forEach items="${roleList }" var="role">
								<option value="${role.id }">${role.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>说明:</td>
					<td><input id="tip" name="tip" class="easyui-textbox" style="width:180px"></td>
				</tr>
				<tr>
					<td>权限标志:</td>
					<td><input id="rights" name="rights" class="easyui-textbox" style="width:180px"></td>
				</tr>
				<tr>
					<td>是否启用:</td>
					<td>
						<input type="radio" name="status" class="easyui-validatebox" data-options="validType:'requireRadio[\'#fm input[name=status]\', \'Yes or no\']'" value="1" >启用
						<input type="radio" name="status" class="easyui-validatebox" value="2">不启用
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'" onclick="save()" style="width:90px">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'" onclick="javascript:$('#dlg').dialog('close')"
			style="width:90px">取消</a>
	</div>
<script type="text/javascript">
$(function(){
	$.extend($.fn.validatebox.defaults.rules, {
	    equals: {
			validator: function(value,param){
				return value == $(param[0]).val();
			},
			message: '填写相同的密码'
	    }
	});
	$.extend($.fn.validatebox.defaults.rules, {
        requireRadio: {  
            validator: function(value, param){  
                var input = $(param[0]);
                input.off('.requireRadio').on('click.requireRadio',function(){
                    $(this).focus();
                });
                return $(param[0] + ':checked').val() != undefined;
            },  
            message: '请选择'  
        }  
    });
});
function searchData(){
	$("#dg").datagrid('load',{
		queryName:$("#queryName").val(),
		queryStatus:$("#queryStatus").val(),
		queryDept:$("#queryDept").val(),
		queryRole:$("#queryRole").val()
	});
}
function initAdd(){
	$("#id").val("");
	$("#name").textbox("setValue","");
	$("#username").textbox("setValue","");
	$("#password").textbox("setValue","");
	$("#rpwd").textbox("setValue","");
	$("#deptId").combobox("select","");
	$("#roleId").combobox("select","");
	$("#tip").textbox("setValue","");
	$("#rights").textbox("setValue","");
	$("input[name='status']").attr("checked",false);
	$("#dlg").dialog({
		title:"新增用户",
	}).dialog("open");
}
function save(){
	$('#fm').form('submit',{
        url:"user/save.do",
        onSubmit: function(){
        	return $(this).form('validate');
        },
        success:function(data){
        	if(data=="success"){
				alert("保存成功");
				$('#dlg').dialog('close');
				$("#dg").datagrid('load');
			}else{
				alert("保存出错");
				$("#dg").datagrid('load');
			}
        },
        error:function(){
			alert("保存失败");
			$("#dg").datagrid('load');
		}
	});
}
function initDel(){
	var row = $('#dg').datagrid('getSelected');
	if(row){
		$.get("user/delete/"+row.id,function(data){
			if(data=="success"){
				alert("删除成功");
				$("#dg").datagrid('load');
			}
		});
	}
}
function initEdit(){
	var row = $('#dg').datagrid('getSelected');
	if(row){
		$("#id").val(row.id);
		$("#name").textbox("setValue",row.name);
		$("#username").textbox("setValue",row.username);
		$("#password").textbox("setValue","");
		$("#rpwd").textbox("setValue","");
		$("#deptId").combobox("select",row.deptId);
		$("#roleId").combobox("select",row.roleId);
		$("#rights").textbox("setValue",row.rights);
		$("#tip").textbox("setValue",row.tip);
		$("input[name='status'][value='"+row.status+"']").attr("checked",true);
		$("#dlg").dialog({
			title:"编辑用户",
		}).dialog("open");
	}
}

</script>
</body>
</html>