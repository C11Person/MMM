package org.appsys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppCategory;
import org.appsys.pojo.AppInfo;
import org.appsys.pojo.DataDictionary;
import org.appsys.pojo.DevUser;
import org.appsys.service.DevUserService;
import org.appsys.tool.Constans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;


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
	@RequestMapping(value="/list.html")
	public String list(HttpSession session,@RequestParam(required=false) String querySoftwareName,@RequestParam(required=false) String queryStatus,@RequestParam(required=false) String queryFlatformId,@RequestParam(required=false) String queryCategoryLevel1,@RequestParam(required=false) String queryCategoryLevel2,@RequestParam(required=false) String queryCategoryLevel3,@RequestParam(required=false) String pageIndex){
		if(pageIndex==null){
			pageIndex="1";
		}
		if(queryStatus==null || queryStatus.equals("")){
			queryStatus="0";
		}
		if(queryFlatformId==null || queryFlatformId.equals("")){
			queryFlatformId="0";
		}
		if(queryCategoryLevel1==null || queryCategoryLevel1.equals("")){
			queryCategoryLevel1="0";
		}
		if(queryCategoryLevel2==null || queryCategoryLevel2.equals("")){
			queryCategoryLevel2="0";
		}
		if(queryCategoryLevel3==null || queryCategoryLevel3.equals("")){
			queryCategoryLevel3="0";
		}
		
		
		int count = devUserService.selectAppCount(querySoftwareName,Integer.parseInt(queryStatus),Integer.parseInt(queryFlatformId), Integer.parseInt(queryCategoryLevel1), Integer.parseInt(queryCategoryLevel2), Integer.parseInt(queryCategoryLevel3));
		int pageCount = count % Constans.PAGE_SIZE == 0 ?   count / Constans.PAGE_SIZE :  count / Constans.PAGE_SIZE  +1;
		System.out.println("================"+count);
		Map map = new HashMap();
		map.put("totalCount", count);
		map.put("totalPageCount", pageCount);
		map.put("currentPageNo", pageIndex);
		session.setAttribute("pages", map);
		//显示所有
		List<AppInfo> appInfoList = devUserService.appList(querySoftwareName,Integer.parseInt(queryStatus),Integer.parseInt(queryFlatformId), Integer.parseInt(queryCategoryLevel1), Integer.parseInt(queryCategoryLevel2), Integer.parseInt(queryCategoryLevel3),Integer.parseInt(pageIndex),Constans.PAGE_SIZE);
		
		//app状态
		List<DataDictionary> statusList = devUserService.statusList();
		
		//所属平台
		List<DataDictionary> flatFormList = devUserService.flatFormList();
		
		//一级分类
		List<AppCategory> categoryLevel1List = devUserService.categoryLevel1List();
		session.setAttribute("appInfoList", appInfoList);
		session.setAttribute("statusList",statusList);
		session.setAttribute("flatFormList",flatFormList);
		session.setAttribute("categoryLevel1List",categoryLevel1List);
		return "developer/appinfolist";
	}
	
	
	@RequestMapping(value="/categorylevel2list",produces="application/json;charset=utf-8")
	@ResponseBody
	public String li2(String pid){
		
		System.out.println("哈哈");
		System.out.println(pid);
		List<AppCategory> categoryLevel2List = devUserService.categoryLevel2List(Integer.parseInt(pid));
		return JSONArray.toJSONString(categoryLevel2List);
	}
	
	
}
