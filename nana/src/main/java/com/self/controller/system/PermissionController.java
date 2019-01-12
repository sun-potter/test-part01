package com.self.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.self.entity.system.Permission;
import com.self.service.system.PermissionManager;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	@Value("#{configProperties['deploymentAddress']}")
	private String ip;
	@Resource(name="permissionService")
	private PermissionManager permissionService;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("system/permission/permission_list");
		mv.addObject("ip", ip);
		return mv;
	}
	
	@RequestMapping("/listPageAjax")
	@ResponseBody
	public Map<String,Object> ListPageAjax(HttpServletRequest request) throws Exception{
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		String page = request.getParameter("page");//当前第几页
		String rows = request.getParameter("rows");//每页显示的纪录条数
		String queryName = request.getParameter("queryName");
		String queryPercode = request.getParameter("queryPercode");
		String queryStatus = request.getParameter("queryStatus");
		conditionMap.put("page", page);
		conditionMap.put("rows", rows);
		conditionMap.put("queryName", queryName);
		conditionMap.put("queryPercode", queryPercode);
		conditionMap.put("queryStatus", queryStatus);
		Map<String, Object> resultMap = permissionService.pageList(conditionMap);
		return resultMap;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(Permission permission)throws Exception{
		permissionService.saveOrUpdate(permission);
		return "success";
	}
	
	@RequestMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id)throws Exception{
		String info = permissionService.delete(id);
		ResponseEntity<String> result = new ResponseEntity<String>(info,HttpStatus.OK);
		return result;
	}
	
	@RequestMapping("/zTreeForRole")
	@ResponseBody
	public List<Map<String, Object>> zTreeForRole(HttpServletRequest request)throws Exception{
		String roleId =  request.getParameter("roleId");
		return permissionService.turnzTreeNodes(roleId);
	}
}
