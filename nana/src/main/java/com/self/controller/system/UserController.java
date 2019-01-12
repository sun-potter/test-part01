package com.self.controller.system;

import java.util.HashMap;
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

import com.self.entity.system.User;
import com.self.service.system.UserManager;

@Controller
@RequestMapping("/user")
public class UserController {

	@Value("#{configProperties['deploymentAddress']}")
	private String ip;
	@Resource(name="userService")
	private UserManager userService;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("system/user/user_list");
		Map<String,Object> resultMap = userService.initPageList();
		mv.addObject("ip", ip);
		mv.addObject("roleList", resultMap.get("roleList"));
		mv.addObject("deptList", resultMap.get("deptList"));
		return mv;
	}
	
	@RequestMapping("/listPageAjax")
	@ResponseBody
	public Map<String,Object> ListPageAjax(HttpServletRequest request) throws Exception{
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		String page = request.getParameter("page");//当前第几页
		String rows = request.getParameter("rows");//每页显示的纪录条数
		String queryName = request.getParameter("queryName");
		String queryStatus = request.getParameter("queryStatus");
		String queryDept = request.getParameter("queryDept");
		String queryRole = request.getParameter("queryRole");
		conditionMap.put("page", page);
		conditionMap.put("rows", rows);
		conditionMap.put("queryName", queryName);
		conditionMap.put("queryStatus", queryStatus);
		conditionMap.put("queryDept", queryDept);
		conditionMap.put("queryRole", queryRole);
		Map<String, Object> resultMap = userService.pageList(conditionMap);
		return resultMap;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(User user)throws Exception{
		userService.saveOrUpdate(user);
		return "success";
	}
	
	@RequestMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id)throws Exception{
		userService.delete(id);
		ResponseEntity<String> result = new ResponseEntity<String>("success",HttpStatus.OK);
		return result;
	}
	
}
