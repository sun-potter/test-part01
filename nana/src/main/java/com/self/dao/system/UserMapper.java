package com.self.dao.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.User;

import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
	
	List<User> selectAllWithDeptAndRole(Map<String, Object> conditionMap);
}
