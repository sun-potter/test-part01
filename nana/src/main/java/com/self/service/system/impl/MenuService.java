package com.self.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.self.dao.system.MenuMapper;
import com.self.dao.system.RoleMenuMapper;
import com.self.entity.system.Menu;
import com.self.entity.system.RoleMenu;
import com.self.service.system.MenuManager;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service("menuService")
public class MenuService implements MenuManager{

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MenuService.class);
	
	@Resource(name="menuMapper")
	private MenuMapper menuMapper;
	@Resource(name="roleMenuMapper")
	private RoleMenuMapper roleMenuMapper;
	
	@Override
	public Map<String,Object> pageList(Map<String,Object> conditionMap)throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Object superior = conditionMap.get("superior");
		if(superior!=null && !"0".equals(superior)){
			Menu parentMenu = new Menu();
			parentMenu.setId((String)superior);
			parentMenu = menuMapper.selectOne(parentMenu);
			String susuper = parentMenu.getSuperior();
			resultMap.put("susuper", susuper);
		}
		int page = 1;
		int rows = 10;
		if(conditionMap.containsKey("page")){
			page = Integer.parseInt((String) conditionMap.get("page"));
		}
		if(conditionMap.containsKey("rows")){
			rows = Integer.parseInt((String) conditionMap.get("rows"));
		}
		PageHelper.startPage(page, rows);
		Example ex = new Example(Menu.class);
		ex.setOrderByClause("sort");
		Criteria criteria = ex.createCriteria().andEqualTo("superior", superior==null?"0":superior);
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryName"))){
			criteria = criteria.andLike("name", "%"+conditionMap.get("queryName")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryStatus"))){
			criteria = criteria.andEqualTo("status", Integer.parseInt((String)conditionMap.get("queryStatus")));
		}
		List<Menu> menuList = menuMapper.selectByExample(ex);
		PageInfo<Menu> pageInfo = new PageInfo<Menu>(menuList);
		int pages = pageInfo.getPages();
		for(Menu menu:menuList){
			if(menu.getStatus()==1){
				menu.setStatusName("∆Ù”√");
			}else{
				menu.setStatusName("≤ª∆Ù”√");
			}
		}
		resultMap.put("rows", menuList);
		resultMap.put("total", pages);
		return resultMap;
	}

	@Override
	public Menu findMenuById(String id) throws Exception{
		Menu menu = new Menu();
		menu.setId(id);
		return menuMapper.selectOne(menu);
	}

	@Override
	public void saveOrUpdate(Menu menu) throws Exception {
		if(StringUtils.isBlank(menu.getId())){
			menu.setId(null);
			menuMapper.insert(menu);
		}else{
			menuMapper.updateByPrimaryKey(menu);
		}
	}

	@Override
	public String delete(String id) throws Exception {
		String result = "hasRight";
		RoleMenu rm = new RoleMenu();
		rm.setMenuId(id);
		List<RoleMenu> list = roleMenuMapper.select(rm);
		if(list.isEmpty()){
			Menu menuChild = new Menu();
			menuChild.setSuperior(id);
			List<Menu> menuList = menuMapper.select(menuChild);
			if(menuList.isEmpty()){
				menuMapper.deleteByPrimaryKey(id);
				result = "success";
			}else{
				result = "hasChild";
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> turnzTreeNodes(String roleId) throws Exception {
		List<Map<String, Object>> MenuList = roleMenuMapper.findListMenuRightByRoleId(roleId);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : MenuList){
			if(map.get("checked")!=null){
				map.put("checked", true);
			}
			if("0".equals(map.get("pId"))){
				List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> childMap : MenuList){
					if(map.get("id").equals(childMap.get("pId"))){
						childList.add(childMap);
					}
				}
				map.put("children", childList);
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	
}
