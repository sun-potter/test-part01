package com.self.service.system;

import java.util.List;
import java.util.Map;

import com.self.entity.system.Menu;


public interface MenuManager {

	/**
	 * �˵��б�
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> pageList(Map<String,Object> conditionMap)throws Exception;

	/**
	 * ����id������
	 * @param id
	 * @return
	 */
	Menu findMenuById(String id)throws Exception;

	/**
	 * ����
	 * @param menu
	 * @throws Exception
	 */
	void saveOrUpdate(Menu menu)throws Exception;

	/**
	 * ɾ��
	 * @param id
	 * @throws Exception
	 */
	String delete(String id)throws Exception;

	/**
	 * ���˵�ת����zTree�ڵ�
	 */
	List<Map<String, Object>> turnzTreeNodes(String roleId)throws Exception;
}
