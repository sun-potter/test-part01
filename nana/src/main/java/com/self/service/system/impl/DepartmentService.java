package com.self.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.self.dao.system.DepartmentMapper;
import com.self.dao.system.UserMapper;
import com.self.entity.system.Department;
import com.self.entity.system.User;
import com.self.service.system.DepartmentManager;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("departmentService")
public class DepartmentService implements DepartmentManager {
	
	@Resource(name="departmentMapper")
	private DepartmentMapper departmentMapper;
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
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
		Example ex = new Example(Department.class);
		Criteria criteria = ex.createCriteria();
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryName"))){
			criteria = criteria.andLike("name", "%"+conditionMap.get("queryName")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryStatus"))){
			criteria = criteria.andEqualTo("status", Integer.parseInt((String)conditionMap.get("queryStatus")));
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryCode"))){
			criteria = criteria.andLike("code", "%"+conditionMap.get("queryCode")+"%");
		}
		if(StringUtils.isNoneBlank((String)conditionMap.get("queryLevel"))){
			criteria = criteria.andEqualTo("level", Integer.parseInt((String)conditionMap.get("queryLevel")));
		}
		List<Department> departmentList = departmentMapper.selectByExample(ex);
		PageInfo<Department> pageInfo = new PageInfo<Department>(departmentList);
		int pages = pageInfo.getPages();
		for(Department department:departmentList){
			if(department.getStatus()==1){
				department.setStatusName("∆Ù”√");
			}else{
				department.setStatusName("≤ª∆Ù”√");
			}
		}
		resultMap.put("rows", departmentList);
		resultMap.put("total", pages);
		return resultMap;
	}

	@Override
	public void saveOrUpdate(Department department) throws Exception {
		if(StringUtils.isBlank(department.getId())){
			department.setId(null);
			departmentMapper.insert(department);
		}else{
			departmentMapper.updateByPrimaryKey(department);
		}
	}

	@Override
	public Department findDepartmentById(String id) throws Exception {
		Department department = new Department();
		department.setId(id);
		return departmentMapper.selectOne(department);
	}

	@Override
	public String delete(String id) throws Exception {
		String result = "false";
		User user = new User();
		user.setDeptId(id);
		List<User> list = userMapper.select(user);
		if(list.isEmpty()){
			departmentMapper.deleteByPrimaryKey(id);
			result = "success";
		}
		return result;
	}

}
