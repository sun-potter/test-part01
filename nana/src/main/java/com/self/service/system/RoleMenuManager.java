package com.self.service.system;

import java.util.List;

import com.self.entity.system.Menu;

public interface RoleMenuManager {

	public List<Menu> findMenuByRole(String roleId) throws Exception;

	public void compareAndChangeRoleRightMenu(String roleId, String ids)throws Exception;

	public void compareAndChangeRolePermission(String roleId, String ids)throws Exception;
}
