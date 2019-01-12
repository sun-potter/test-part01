package com.self.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.self.dao.system.DepartmentMapper;
import com.self.dao.system.RoleMapper;
import com.self.dao.system.UserMapper;
import com.self.entity.system.Department;
import com.self.entity.system.Role;
import com.self.entity.system.User;
import com.self.service.system.UserManager;

@Service("userService")
public class UserService implements UserManager{

	@Resource(name="userMapper")
	private UserMapper userMapper;
	@Resource(name="roleMapper")
	private RoleMapper roleMapper;
	@Resource(name="departmentMapper")
	private DepartmentMapper departmentMapper;
	
	
	@Override
	public Map<String,Object> initPageList() throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Role role = new Role();
		role.setStatus(1);
		List<Role> roleList = roleMapper.select(role);
		Department dept = new Department();
		List<Department> deptList = departmentMapper.select(dept);
		resultMap.put("roleList", roleList);
		resultMap.put("deptList", deptList);
		return resultMap;
	}

	@Override
	public User findUserByUsername(String username)throws Exception{
		User user = new User();
		user.setUsername(username);
		user = userMapper.selectOne(user);
		return user;
	}

	@Override
	public Map<String, Object> pageList(Map<String, Object> conditionMap) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int page = 1;
		int rows = 10;
		if(conditionMap.containsKey("page")){
			page = Integer.parseInt((String) conditionMap.get("page"));
		}
		if(conditionMap.containsKey("rows")){
			rows = Integer.parseInt((String) conditionMap.get("rows"));
		}
		PageHelper.startPage(page, rows);
		List<User> userList = userMapper.selectAllWithDeptAndRole(conditionMap);
		PageInfo<User> pageInfo = new PageInfo<User>(userList);
		int pages = pageInfo.getPages();
		for(User user:userList){
			if(user.getStatus()==1){
				user.setStatusName("∆Ù”√");
			}else{
				user.setStatusName("≤ª∆Ù”√");
			}
		}
		resultMap.put("rows", userList);
		resultMap.put("total", pages);
		return resultMap;
	}

	@Override
	public void saveOrUpdate(User user) throws Exception {
		String password = user.getPassword();
		String username = user.getUsername();
		password = new Md5Hash(password, username, 2).toString();
		user.setPassword(password);
		if(StringUtils.isBlank(user.getId())){
			user.setId(null);
			userMapper.insert(user);
		}else{
			userMapper.updateByPrimaryKey(user);
		}
	}

	@Override
	public void delete(String id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	}

}
