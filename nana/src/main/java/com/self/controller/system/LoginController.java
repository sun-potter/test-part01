package com.self.controller.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.entity.system.Menu;
import com.self.entity.system.User;
import com.self.service.system.RoleMenuManager;
import com.self.util.Const;
import com.self.util.CookieUtils;

import redis.clients.jedis.Jedis;


@Controller
public class LoginController {

	private Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Value("#{configProperties['deploymentAddress']}")
	private String ip;
	@Resource(name="roleMenuService")
	private RoleMenuManager roleMenuService;
	
	@RequestMapping("/toLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = new ModelAndView("system/main/login");
		mv.addObject("ip", ip);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public String login(HttpServletRequest request)throws Exception{
		String reStr = null;
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		if(UnknownAccountException.class.getName().equals(exceptionClassName)){
			reStr = "loginFailure";
			log.info("登入系统失败，账号不存在");
		}else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)){
			reStr = "loginFailure";
			log.info("登入系统失败，用户名/密码错误");
		}else if("validateCodeError".equals(exceptionClassName)){
			reStr = "validateCodeError";
			log.info("验证码错误");
		}
		if(reStr==null){
			reStr = "regain";
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
		}
		return reStr;
	}
	
	@RequestMapping("/index")
	public ModelAndView index()throws Exception{
		ModelAndView mv = new ModelAndView("system/main/main");
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		String roleId = user.getRoleId();
		List<Menu> menuList = null;
		try {
			menuList = roleMenuService.findMenuByRole(roleId);
		} catch (Exception e) {
			log.error("查询菜单错误："+e.getMessage());
			e.printStackTrace();
		}
		Map<Menu,List<Menu>> mapMenuList = new HashMap<Menu,List<Menu>>();
		for(Menu mu1:menuList){
			if("0".equals(mu1.getSuperior())){
				List<Menu> mu2List = new ArrayList<Menu>();
				for(Menu mu2:menuList){
					if(mu1.getId().equals(mu2.getSuperior())){
						mu2List.add(mu2);
					}
				}
				mapMenuList.put(mu1,mu2List);
			}
		}
		mv.addObject("mapMenuList", mapMenuList);
		mv.addObject("name", user.getName());
		mv.addObject("ip", ip);
		return mv;
		
	}
	
	@ResponseBody
	@RequestMapping("/successUrl")
	public String successUrl(HttpServletRequest request,HttpServletResponse response)throws Exception{
		return "loginSuccess";
	}
	
	@RequestMapping("/code")
	public void generate(HttpServletResponse response)throws Exception{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String code = drawImg(output);
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		session.setAttribute(Const.VALIDATECODE, code);
		try {
			ServletOutputStream out = response.getOutputStream();
			output.writeTo(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String drawImg(ByteArrayOutputStream output){
		String code = "";
		for(int i=0; i<4; i++){
			code += randomChar();
		}
		int width = 70;
		int height = 25;
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman",Font.PLAIN,20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66,2,82);
		g.setColor(color);
		g.setBackground(new Color(226,226,240));
		g.clearRect(0, 0, width, height);
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int)x, (int)baseY);
		g.dispose();
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	private char randomChar(){
		Random r = new Random();
		String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
}
