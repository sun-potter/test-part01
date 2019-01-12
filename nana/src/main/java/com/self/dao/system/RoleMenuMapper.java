package com.self.dao.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.Menu;
import com.self.entity.system.RoleMenu;

import tk.mybatis.mapper.common.Mapper;

public interface RoleMenuMapper extends Mapper<RoleMenu> {

	public List<Menu> findListMenuByRoleId(String roleId);
	
	public List<Map<String,Object>>  findListMenuRightByRoleId(String roleId);
}
