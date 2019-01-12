package com.self.service.system;

import java.util.Map;

import com.self.entity.system.Department;

public interface DepartmentManager {

	Map<String, Object> pageList(Map<String, Object> conditionMap)throws Exception;

	void saveOrUpdate(Department department)throws Exception;

	Department findDepartmentById(String id)throws Exception;

	String delete(String id)throws Exception;
	
}
