package com.self.service.system;

import java.util.Map;

import com.self.entity.system.User;

public interface UserManager {

	User findUserByUsername(String username)throws Exception;

	Map<String, Object> pageList(Map<String, Object> conditionMap)throws Exception;

	void saveOrUpdate(User user)throws Exception;

	void delete(String id)throws Exception;

	Map<String,Object> initPageList()throws Exception;


}
