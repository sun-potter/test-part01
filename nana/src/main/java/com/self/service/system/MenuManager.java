package com.self.service.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.Menu;


public interface MenuManager {

	/**
	 * 菜单列表
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> pageList(Map<String,Object> conditionMap)throws Exception;

	/**
	 * 根据id查数据
	 * @param id
	 * @return
	 */
	Menu findMenuById(String id)throws Exception;

	/**
	 * 保存
	 * @param menu
	 * @throws Exception
	 */
	void saveOrUpdate(Menu menu)throws Exception;

	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	String delete(String id)throws Exception;

	/**
	 * 将菜单转换成zTree节点
	 */
	List<Map<String, Object>> turnzTreeNodes(String roleId)throws Exception;
}
