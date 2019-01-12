package com.self.service.system;

import java.util.List;

public interface RolePermissionManager {

	List<String> findPermissionByRoleId(String roleId);

}
