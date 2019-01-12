package com.self.interceptor.shiro;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.entity.system.User;
import com.self.service.system.RolePermissionManager;
import com.self.service.system.UserManager;
import com.self.util.CookieUtils;

import redis.clients.jedis.Jedis;

public class ShiroRealm extends AuthorizingRealm {

	private Logger log = LoggerFactory.getLogger(ShiroRealm.class);

	@Resource(name = "userService")
	private UserManager userService;

	@Resource(name = "rolePermissionService")
	private RolePermissionManager rolePermissionService;

	private String tokenName = "TOKEN";

	private String host = "127.0.0.1";

	private int port = 6379;

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		String roleId = user.getRoleId();
		SimpleAuthorizationInfo saz = new SimpleAuthorizationInfo();
		List<String> permissionNameList = rolePermissionService.findPermissionByRoleId(roleId);
		saz.addStringPermissions(permissionNameList);
		return saz;
	}

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		String tokenKey = CookieUtils.getCookieValue(request, tokenName);
		if(tokenKey!=null){
			Jedis jedis = new Jedis(host,port);
			String tokenValue = jedis.get(tokenKey);
			jedis.close();
			if(StringUtils.isNoneBlank(tokenValue)){
				try {
					User user = new ObjectMapper().readValue(tokenValue,User.class);
					SimpleAuthenticationInfo sac = new SimpleAuthenticationInfo(user,"freeToken","shiro");
					return sac;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String username = (String) token.getPrincipal();
		User user = null;
		try {
			user = userService.findUserByUsername(username);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		if (user == null) {
			return null;
		}
		String password = user.getPassword();
		user.setPassword(null);
		SimpleAuthenticationInfo sac = new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(username),
				"shiro");
		return sac;
	}

	// 清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}

}
