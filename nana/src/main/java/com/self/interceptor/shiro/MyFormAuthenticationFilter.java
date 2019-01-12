package com.self.interceptor.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.self.util.Const;

/**
 * 自定义的shiro认证拦截器
 * 目的：实现认证前通过验证码校验
 * @author Sunny
 *
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter{

	//认证方法，插入校验验证码方法
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession session = httpServletRequest.getSession();
		//验证码正确的值,在生成验验证码时传入
		String validateCode = (String)session.getAttribute(Const.VALIDATECODE);
		//randomCode 前台用户输入的值
		String randomCode = httpServletRequest.getParameter("randomCode");
		if(randomCode!=null && validateCode !=null && !randomCode.toUpperCase().equals(validateCode)){
			//如果验证码校验失败，通过shiroLoginFailure设置到request中
			httpServletRequest.setAttribute("shiroLoginFailure", "validateCodeError");
			return true;//拒绝访问
		}
		return super.onAccessDenied(request, response);
		
	}

}
