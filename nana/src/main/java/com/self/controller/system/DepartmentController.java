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

import com.self.entity.system.Department;
import com.self.service.system.DepartmentManager;

@Controller
@RequestMapping("/department")
public class DepartmentController{

	@Value("#{configProperties['deploymentAddress']}")
	private String ip;
	@Resource(name="departmentService")
	private DepartmentManager departmentService;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("system/department/department_list");
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
		String queryCode = request.getParameter("queryCode");
		String queryLevel = request.getParameter("queryLevel");
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		conditionMap.put("page", page);
		conditionMap.put("rows", rows);
		conditionMap.put("queryName", queryName);
		conditionMap.put("queryStatus", queryStatus);
		conditionMap.put("queryCode", queryCode);
		conditionMap.put("queryLevel", queryLevel);
		Map<String, Object> resultMap = departmentService.pageList(conditionMap);
		return resultMap;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(Department department)throws Exception{
		departmentService.saveOrUpdate(department);
		return "success";
	}
	
	@RequestMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id)throws Exception{
		String info = departmentService.delete(id);
		ResponseEntity<String> result = new ResponseEntity<String>(info,HttpStatus.OK);
		return result;
	}
}
