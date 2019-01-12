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
<title>菜单列表</title>
</head>
<body>
	<table id="dg" class="easyui-datagrid"
		style="width:90%;min-height:500px" 
		data-options="
		url:'<%=basePath%>menu/listPageAjax.do?superior=${superiorMenu.id}',
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
				<th data-options="field:'name',width:120,align:'center',sortable:false">菜单名称</th>
				<th data-options="field:'sort',width:80,align:'center',sortable:false">排序</th>
				<c:if test="${superiorMenu.id ne '0'}">
					<th data-options="field:'url',width:180,align:'center',sortable:false">URL</th>
				</c:if>	
				<th data-options="field:'statusName',width:80,align:'center',sortable:false">启用</th>
				<th data-options="field:'tip',width:200,align:'center',sortable:false">说明 </th>
				<th data-options="field:'opertion',width:150,align:'center',sortable:false,formatter:opertion">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<table>
			<tr>
				<td>菜单名称:</td>
				<td>
					<input id="queryName" class="easyui-textbox" style="width:180px">
				</td>
				<td>是否启用:</td>
				<td>
					<select id="queryStatus" class="easyui-combobox" data-options="required:true,panelHeight: 'auto',editable:false" style="width:180px">
					    <option value="">全部</option>
						<option value="1" <c:if test="${queryStatus eq '1' }">selected</c:if>>启用</option>
						<option value="2" <c:if test="${queryStatus eq '2' }">selected</c:if>>未启用</option>
					</select>
				</td>
				<td>
					<a href="javascript:void(0)" onclick="searchData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>
				</td>
			</tr>
		</table>
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="initAdd()">新建菜单</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="initEdit()">修改菜单</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="initDel()">删除菜单</a>
			<c:if test="${superiorMenu.id ne '0'}">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="enter('${superiorMenu.superior}')">返回</a>
			</c:if>
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
					<td>上级菜单:</td>
					<td>
						${superiorMenu.name}
						<input type="hidden" name="superior" value="${superiorMenu.id}">
					</td>
				</tr>
				<tr>
					<td>菜单名称:</td>
					<td><input id="name" name="name" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>排序:</td>
					<td><input id="sort" name="sort" class="easyui-numberbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<c:if test="${superiorMenu.id ne '0'}">
					<tr>
						<td>URL地址:</td>
						<td><input id="url" name="url" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
					</tr>
				</c:if>
				<tr>
					<td>备注:</td>
					<td><input id="tip" name="tip" class="easyui-textbox easyui-validatebox" data-options="required:true" style="width:180px"></td>
				</tr>
				<tr>
					<td>是否启用:</td>
					<td>
						<input type="radio" name="status" class="easyui-validatebox" data-options="validType:'requireRadio[\'#fm input[name=status]\', \'Yes or no\']'" value="1" >启用
						<input type="radio" name="status" class="easyui-validatebox" value="2" >不启用
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
			queryStatus:$("#queryStatus").val()
		});
	}
	//操作栏
	function opertion(val, row, index){
		var id = row.id;
		var str = '<input type = "button" value = "进入" onclick = "enter(\''+id+'\')"/>';
		return str;
	}
	function initAdd(){
		$("#id").val("");
		$("#name").textbox("setValue","");
		$("#sort").textbox("setValue","");
		$("#tip").textbox("setValue","");
		$("#url").textbox("setValue","");
		$("input[name='status']").attr("checked",false);
		$("#dlg").dialog({
			title:"新增菜单",
		}).dialog("open");
	}
	function save(){
		$('#fm').form('submit',{
	        url:"menu/save.do",
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
			$.get("menu/delete/"+row.id,function(data){
				if(data=="success"){
					alert("删除成功");
					$("#dg").datagrid('load');
				}else if(data=="hasChild"){
					alert("请先删除其子菜单");
				}else if(data=="hasRight"){
					alert("请先删除使用此菜单的权限");
				}
			});
		}
	}
	function initEdit(){
		var row = $('#dg').datagrid('getSelected');
		if(row){
			$("#id").val(row.id);
			$("#name").textbox("setValue",row.name);
			$("#sort").textbox("setValue",row.sort);
			$("#tip").textbox("setValue",row.tip);
			$("#url").textbox("setValue",row.url);
			$("input[name='status'][value='"+row.status+"']").attr("checked",true);
			$("#dlg").dialog({
				title:"编辑菜单",
			}).dialog("open");
		}
	}
	function enter(id){
		window.location.href="menu/list.do?superior="+id;
	}
</script>
</body>
</html>