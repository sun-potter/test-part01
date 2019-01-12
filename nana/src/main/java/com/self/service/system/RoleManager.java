package com.self.service.system;

import java.util.Map;

import com.self.entity.system.Role;

public interface RoleManager {

	Map<String, Object> pageList(Map<String, Object> conditionMap)throws Exception;

	void saveOrUpdate(Role role)throws Exception;

	String delete(String id)throws Exception;

	Role findRoleById(String id)throws Exception;

	void update(Role role)throws Exception;

}
