package com.self.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.self.dao.system.RolePermissionMapper;
import com.self.service.system.RolePermissionManager;

@Service("rolePermissionService")
public class RolePermissionService implements RolePermissionManager{

	@Resource(name="rolePermissionMapper")
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	public List<String> findPermissionByRoleId(String roleId) {
		return rolePermissionMapper.findPermissionNameByRoleId(roleId);
	}

	
}
