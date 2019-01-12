package com.self.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.self.entity.system.Menu;
import com.self.service.system.MenuManager;

@Service
@RequestMapping("/menu")
public class MenuController {

	@Value("#{configProperties['deploymentAddress']}")
	private String ip;
	@Resource(name="menuService")
	private MenuManager menuService;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("system/menu/menu_list");
		String superior = request.getParameter("superior");
		Menu superiorMenu = null;
		if(superior!=null&&!"0".equals(superior)){
			superiorMenu = menuService.findMenuById(superior);
		}else{
			superiorMenu = new Menu();
			superiorMenu.setId("0");
			superiorMenu.setName("无");
		}
		mv.addObject("superiorMenu", superiorMenu);
		mv.addObject("ip", ip);
		return mv;
	}
	
	@RequestMapping("/listPageAjax")
	@ResponseBody
	public Map<String,Object> ListPageAjax(HttpServletRequest request) throws Exception{
		String page = request.getParameter("page");//当前第几页
		String rows = request.getParameter("rows");//每页显示的纪录条数
		String queryName = request.getParameter("queryName");
		String queryStatus = request.getParameter("queryStatus");
		String superior = request.getParameter("superior");
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		conditionMap.put("page", page);
		conditionMap.put("rows", rows);
		conditionMap.put("queryName", queryName);
		conditionMap.put("queryStatus", queryStatus);
		conditionMap.put("superior", superior);
		Map<String, Object> resultMap = menuService.pageList(conditionMap);
		return resultMap;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(Menu menu)throws Exception{
		menuService.saveOrUpdate(menu);
		return "success";
	}
	
	@RequestMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id)throws Exception{
		String info = menuService.delete(id);
		ResponseEntity<String> result = new ResponseEntity<String>(info,HttpStatus.OK);
		return result;
	}
	
	
	
	@RequestMapping("/zTreeForRole")
	@ResponseBody
	public List<Map<String, Object>> zTreeForRole(HttpServletRequest request)throws Exception{
		String roleId =  request.getParameter("roleId");
		List<Map<String, Object>> zNodes = menuService.turnzTreeNodes(roleId);
		return zNodes;
	}
}
