package com.self.service.system.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.self.interceptor.shiro.ShiroRealm;
import com.self.service.system.ShiroManager;

@Service("shiroService")
public class ShiroService implements ShiroManager{

	@Resource(name="shiroRealm")
	private ShiroRealm shiroRealm;
	
	//µ÷ÓÃÇå³ý»º´æ
	@Override
	public void clearShiroCached() throws Exception{
		shiroRealm.clearCached();
	}
}
