package com.self.interceptor.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.self.util.Const;

/**
 * �Զ����shiro��֤������
 * Ŀ�ģ�ʵ����֤ǰͨ����֤��У��
 * @author Sunny
 *
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter{

	//��֤����������У����֤�뷽��
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession session = httpServletRequest.getSession();
		//��֤����ȷ��ֵ,����������֤��ʱ����
		String validateCode = (String)session.getAttribute(Const.VALIDATECODE);
		//randomCode ǰ̨�û������ֵ
		String randomCode = httpServletRequest.getParameter("randomCode");
		if(randomCode!=null && validateCode !=null && !randomCode.toUpperCase().equals(validateCode)){
			//�����֤��У��ʧ�ܣ�ͨ��shiroLoginFailure���õ�request��
			httpServletRequest.setAttribute("shiroLoginFailure", "validateCodeError");
			return true;//�ܾ�����
		}
		return super.onAccessDenied(request, response);
		
	}

}
