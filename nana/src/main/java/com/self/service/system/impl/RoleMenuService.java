package com.self.service.system.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.self.dao.system.RoleMenuMapper;
import com.self.dao.system.RolePermissionMapper;
import com.self.entity.system.Menu;
import com.self.entity.system.RoleMenu;
import com.self.entity.system.RolePermission;
import com.self.service.system.RoleMenuManager;
import com.self.util.UUIDUtil;

@Service("roleMenuService")
public class RoleMenuService implements RoleMenuManager{

	
	@Resource(name="roleMenuMapper")
	private RoleMenuMapper roleMenuMapper;
	@Resource(name="rolePermissionMapper")
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	public List<Menu> findMenuByRole(String roleId) throws Exception{
		return roleMenuMapper.findListMenuByRoleId(roleId);
	}

	@Override
	public void compareAndChangeRoleRightMenu(String roleId, String ids) throws Exception{
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(roleId);
		List<RoleMenu> oldRoleMenuList = roleMenuMapper.select(roleMenu);
		String[] newIdArray = StringUtils.isBlank(ids)?new String[]{}:ids.split(",");
		List<String> newIdList = Arrays.asList(newIdArray);
		List<String> oldIdList = new ArrayList<String>();
		for(RoleMenu rm :oldRoleMenuList){
			String mi = rm.getMenuId();
			oldIdList.add(mi);
			if(!newIdList.contains(mi)){
				roleMenuMapper.delete(rm);
			}
		}
		for(String nid:newIdList){
			if(!oldIdList.contains(nid)){
				RoleMenu nrm = new RoleMenu();
				nrm.setId(UUIDUtil.createNotLink());
				nrm.setMenuId(nid);
				nrm.setRoleId(roleId);
				nrm.setStatus(1);
				roleMenuMapper.insert(nrm);
			}
		}
	}

	@Override
	public void compareAndChangeRolePermission(String roleId, String ids) throws Exception {
		RolePermission rolePermission = new RolePermission();
		rolePermission.setRoleId(roleId);
		List<RolePermission> oldRolePermissionList = rolePermissionMapper.select(rolePermission);
		String[] newIdArray = StringUtils.isBlank(ids)?new String[]{}:ids.split(",");
		List<String> newIdList = Arrays.asList(newIdArray);
		List<String> oldIdList = new ArrayList<String>();
		for(RolePermission rm :oldRolePermissionList){
			String mi = rm.getPermissionId();
			oldIdList.add(mi);
			if(!newIdList.contains(mi)){
				rolePermissionMapper.delete(rm);
			}
		}
		for(String nid:newIdList){
			if(!oldIdList.contains(nid)){
				RolePermission nrm = new RolePermission();
				nrm.setId(UUIDUtil.createNotLink());
				nrm.setPermissionId(nid);
				nrm.setRoleId(roleId);
				nrm.setStatus(1);
				rolePermissionMapper.insert(nrm);
			}
		}
	}
	

}
