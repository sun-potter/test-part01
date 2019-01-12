package com.self.dao.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.RolePermission;

import tk.mybatis.mapper.common.Mapper;

public interface RolePermissionMapper extends Mapper<RolePermission> {

	List<String> findPermissionNameByRoleId(String roleId);

	List<Map<String, Object>> findListPermissionByRoleId(String roleId);

}
