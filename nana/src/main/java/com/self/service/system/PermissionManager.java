package com.self.service.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.Permission;

public interface PermissionManager {

	Map<String, Object> pageList(Map<String, Object> conditionMap)throws Exception;

	void saveOrUpdate(Permission permission)throws Exception;

	String delete(String id)throws Exception;

	Permission findPermissionById(String id)throws Exception;

	List<Map<String, Object>> turnzTreeNodes(String roleId)throws Exception;

}
