package org.appsys.controller;

import javax.servlet.http.HttpSession;

import org.appsys.pojo.DevUser;
import org.appsys.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DevUserController {
	
	@Autowired
	DevUserService devUserService;
	
	@RequestMapping(value="/login.html")
	public String lo(){
		return "devlogin";
	}
	
	
	@RequestMapping(value="/dologin.html",method=RequestMethod.POST)
	public String login(String devCode,String devPassword ,HttpSession session){
		/*ApplicationContext context = new ClassPathXmlApplicationContext("SpringConfig.xml");
		DevUserService devUserService = (DevUserService) context.getBean("devUserService");*/
		DevUser devUser = devUserService.selectUserByNameAndPwd(devCode, devPassword);
		if(devUser!=null){
			session.setAttribute("devUserSession", devUser);
			return "developer/main";
		}else{
			session.setAttribute("error","用户名或密码错误");
			return "devlogin";
		}
	}
	
	@RequestMapping(value="/dev/logout.html")
	public String loginout(HttpSession session){
		session.invalidate();
		return "devlogin";
	}
	
	//APP维护
	@RequestMapping(value="/dev/flatform/app/list.html")
	public String list(){
		return "developer/appinfolist";
	}
}
