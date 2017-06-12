package org.appsys.test;

import org.appsys.pojo.DevUser;
import org.appsys.service.DevUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	@org.junit.Test	
	public void show(){
		ApplicationContext context = new ClassPathXmlApplicationContext("SpringConfig.xml");
		DevUserService devUserService = (DevUserService) context.getBean("devUserService");
		
		DevUser devUser = devUserService.selectUserByNameAndPwd("test001","123456");
		if(devUser!=null){
			System.out.println("登陆成功");
		}else{
			System.out.println("登陆失败");
		}
	}
}
