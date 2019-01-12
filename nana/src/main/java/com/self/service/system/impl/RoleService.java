package com.self.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.self.dao.system.RoleMapper;
import com.self.dao.system.RoleMenuMapper;
import com.self.dao.system.RolePermissionMapper;
import com.self.dao.system.UserMapper;
import com.self.entity.system.Role;
import com.self.entity.system.RoleMenu;
import com.self.entity.system.RolePermission;
import com.self.entity.system.User;
import com.self.service.system.RoleManager;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("roleService")
public class RoleService implements RoleManager{
	
	@Resource(name="roleMapper")
	private RoleMapper roleMapper;
	@Resource(name="userMapper")
	private UserMapper userMapper;
	@Resource(name="rolePermissionMapper")
	private RolePermissionMapper rolePermissionMapper;
	@Resource(name="roleMenuMapper")
	private RoleMenuMapper roleMenuMapper;

	@Override
	public Map<String, Object> pageList(Map<String, Object> conditionMap) throws Exception{
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
		Example ex = new Example(Role.class);
		Criteria criteria = ex.createCriteria();
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryName"))){
			criteria = criteria.andLike("name", "%"+conditionMap.get("queryName")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryStatus"))){
			criteria = criteria.andEqualTo("status", Integer.parseInt((String)conditionMap.get("queryStatus")));
		}
		List<Role> roleList = roleMapper.selectByExample(ex);
		PageInfo<Role> pageInfo = new PageInfo<Role>(roleList);
		for(Role role:roleList){
			if(role.getStatus()==1){
				role.setStatusName("∆Ù”√");
			}else{
				role.setStatusName("≤ª∆Ù”√");
			}
		}
		int pages = pageInfo.getPages();
		resultMap.put("rows", roleList);
		resultMap.put("total", pages);
		return resultMap;
	}

	@Override
	public void saveOrUpdate(Role role)throws Exception {
		if(StringUtils.isBlank(role.getId())){
			role.setId(null);
			roleMapper.insert(role);
		}else{
			roleMapper.updateByPrimaryKey(role);
		}
	}

	@Override
	public String delete(String id) throws Exception{
		String result = "success";
		User user = new User();
		user.setRoleId(id);
		List<User> userlist = userMapper.select(user);
		RoleMenu rm = new RoleMenu();
		rm.setRoleId(id);
		List<RoleMenu> rmlist = roleMenuMapper.select(rm);
		RolePermission rp = new RolePermission();
		rp.setRoleId(id);
		List<RolePermission> rpList = rolePermissionMapper.select(rp);
		if(!userlist.isEmpty()){
			result = "hasUser";
		}else if(!rmlist.isEmpty()){
			result = "hasMenu";
		}else if(!rpList.isEmpty()){
			result = "hasPermission";
		}else{
			roleMapper.deleteByPrimaryKey(id);
		}
		return result;
	}

	@Override
	public Role findRoleById(String id)throws Exception {
		Role role = new Role();
		role.setId(id);
		return roleMapper.selectOne(role);
	}

	@Override
	public void update(Role role) throws Exception{
		roleMapper.updateByPrimaryKey(role);
	}
 
}
