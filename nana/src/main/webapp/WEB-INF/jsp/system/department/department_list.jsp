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
<title>部门列表</title>
</head>
	<table id="dg" class="easyui-datagrid"
		style="width:90%;min-height:500px" 
		data-options="
		url:'<%=basePath%>department/listPageAjax.do',
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
				<th data-options="field:'name',width:120,align:'center',sortable:false">部门名称</th>
				<th data-options="field:'code',width:80,align:'center',sortable:false">编码</th>
				<th data-options="field:'level',width:80,align:'center',sortable:false">等级</th>
				<th data-options="field:'statusName',width:80,align:'center',sortable:false">启用</th>
				<th data-options="field:'tip',width:300,align:'center',sortable:false">说明</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<table>
			<tr>
				<td>部门名称:</td>
				<td>
					<input id="queryName" class="easyui-textbox" style="width:180px">
				</td>
				<td>编码:</td>
				<td>
					<input id="queryCode" class="easyui-textbox" style="width:180px">
				</td>
				<td>等级:</td>
				<td>
					<input id="queryLevel" class="easyui-textbox" style="width:180px">
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
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="initAdd()">新建部门</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="initEdit()">修改部门</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="initDel()">删除部门</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		data-options="iconCls:'icon-save',resizable:true,modal:true"
		style="width:400px;height:320px;padding:10px 20px;top:50px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm"  method="post"
			action="" data-options="novalidate:true" enctype="multipart/form-data">
			<input type="hidden" id="id" name="id">
			<table>
				<tr>
					<td>部门名称:</td>
					<td><input id="name" name="name" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>编码:</td>
					<td><input id="code" name="code" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>等级:</td>
					<td><input id="level" name="level" class="easyui-numberbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>说明:</td>
					<td><input id="tip" name="tip" class="easyui-textbox" style="width:180px"></td>
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
		queryCode:$("#queryCode").val(),
		queryLevel:$("#queryLevel").val()
	});
}
function initAdd(){
	$("#id").val("");
	$("#name").textbox("setValue","");
	$("#code").textbox("setValue","");
	$("#level").numberbox("setValue","");
	$("#tip").textbox("setValue","");
	$("input[name='status']").attr("checked",false);
	$("#dlg").dialog({
		title:"新增部门",
	}).dialog("open");
}
function save(){
	$('#fm').form('submit',{
        url:"department/save.do",
        onSubmit: function(){
        	return $("#fm").form('validate');
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
		$.get("department/delete/"+row.id,function(data){
			if(data=="success"){
				alert("删除成功");
				$("#dg").datagrid('load');
			}else if(data=="false"){
				alert("请先删除使用此部门的用户");
			}
		});
	}
}
function initEdit(){
	var row = $('#dg').datagrid('getSelected');
	if(row){
		$("#id").val(row.id);
		$("#name").textbox("setValue",row.name);
		$("#code").textbox("setValue",row.code);
		$("#level").numberbox("setValue",row.level);
		$("#tip").textbox("setValue",row.tip);
		$("input[name='status'][value='"+row.status+"']").attr("checked",true);
		$("#dlg").dialog({
			title:"编辑部门",
		}).dialog("open");
	}
}
</script>
</body>
</html>