package com.self.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.self.dao.system.PermissionMapper;
import com.self.dao.system.RolePermissionMapper;
import com.self.entity.system.Permission;
import com.self.entity.system.RolePermission;
import com.self.service.system.PermissionManager;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("permissionService")
public class PermissionService implements PermissionManager{
	
	
	@Resource(name="permissionMapper")
	private PermissionMapper permissionMapper;	
	@Resource(name="rolePermissionMapper")
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	public Map<String, Object> pageList(Map<String, Object> conditionMap) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int page = 1;
		int rows = 10;
		if(conditionMap.containsKey("page")){
			page = Integer.parseInt((String) conditionMap.get("page"));
		}
		if(conditionMap.containsKey("rows")){
			rows = Integer.parseInt((String) conditionMap.get("rows"));
		}
		PageHelper.startPage(page, rows);
		Example ex = new Example(Permission.class);
		Criteria criteria = ex.createCriteria();
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryName"))){
			criteria = criteria.andLike("name", "%"+conditionMap.get("queryName")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryPercode"))){
			criteria = criteria.andLike("percode", "%"+conditionMap.get("queryPercode")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryStatus"))){
			criteria = criteria.andEqualTo("status", Integer.parseInt((String)conditionMap.get("queryStatus")));
		}
		List<Permission> permissionList = permissionMapper.selectByExample(ex);
		PageInfo<Permission> pageInfo = new PageInfo<Permission>(permissionList);
		int pages = pageInfo.getPages();
		for(Permission permission:permissionList){
			if(permission.getStatus()==1){
				permission.setStatusName("∆Ù”√");
			}else{
				permission.setStatusName("≤ª∆Ù”√");
			}
		}
		resultMap.put("rows", permissionList);
		resultMap.put("total", pages);
		return resultMap;
	}

	@Override
	public void saveOrUpdate(Permission permission) throws Exception{
		if(StringUtils.isBlank(permission.getId())){
			permission.setId(null);
			permissionMapper.insert(permission);
		}else{
			permissionMapper.updateByPrimaryKey(permission);
		}
	}

	@Override
	public String delete(String id) throws Exception{
		String result = "false";
		RolePermission rp = new RolePermission();
		rp.setPermissionId(id);
		List<RolePermission> list = rolePermissionMapper.select(rp);
		if(list.isEmpty()){
			permissionMapper.deleteByPrimaryKey(id);
			result = "success";
		}
		return result;
	}

	@Override
	public Permission findPermissionById(String id) throws Exception{
		Permission permission = new Permission();
		permission.setId(id);
		return permissionMapper.selectOne(permission);
	}

	@Override
	public List<Map<String, Object>> turnzTreeNodes(String roleId) throws Exception{
		List<Map<String, Object>> MenuList = rolePermissionMapper.findListPermissionByRoleId(roleId);
		for(Map<String, Object> map : MenuList){
			if(map.get("checked")!=null){
				map.put("checked", true);
			}
		}
		return MenuList;
	}

}
