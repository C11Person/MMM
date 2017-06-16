package org.appsys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.appsys.pojo.AppCategory;
import org.appsys.pojo.AppInfo;
import org.appsys.pojo.AppVersion;
import org.appsys.pojo.BackendUser;
import org.appsys.pojo.DataDictionary;
import org.appsys.service.AppCategoryService;
import org.appsys.service.AppInfoService;
import org.appsys.service.AppVersionService;
import org.appsys.service.BackendUserService;
import org.appsys.service.DataDictionaryService;
import org.appsys.tool.Constans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

@Controller
public class BackendController {
	
	@Autowired
	BackendUserService backendUserService;
	@Autowired
	AppInfoService appInfoService;
	@Autowired
	DataDictionaryService dataDictionaryService;
	@Autowired
	AppCategoryService appCategoryService;
	@Autowired
	AppVersionService appVersionService;
	// 跳转到登陆界面
	@RequestMapping(value = "/backendLogin.html")
	public String lo() {
		return "backendlogin";
	}
	
	
	//登陆
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String login(@RequestParam String userCode,@RequestParam String userPassword,HttpSession session){
		BackendUser backendUser = backendUserService.selectUserByNameAndPwd(userCode, userPassword);
		if(backendUser!=null){
			session.setAttribute("userSession", backendUser);
			return "backend/main";
		}else{
			session.setAttribute("error", "用户名或密码错误");
			return "backendlogin";
		}
	}
	// 注销
	@RequestMapping(value = "/manager/logout")
	public String loginout(HttpSession session) {
		session.invalidate();
		return "backendlogin";
	}
	
	
	// APP维护
	@RequestMapping(value = "/list")
	public String list(HttpSession session,
			@RequestParam(required = false) String querySoftwareName,
			@RequestParam(required = false) String queryStatus,
			@RequestParam(required = false) String queryFlatformId,
			@RequestParam(required = false) String queryCategoryLevel1,
			@RequestParam(required = false) String queryCategoryLevel2,
			@RequestParam(required = false) String queryCategoryLevel3,
			@RequestParam(required = false) String pageIndex) {

		if (pageIndex == null) {
			pageIndex = "1";
		}
		if (queryStatus == null || queryStatus.equals("")) {
			queryStatus = "0";
		}
		if (queryFlatformId == null || queryFlatformId.equals("")) {
			queryFlatformId = "0";
		}
		if (queryCategoryLevel1 == null || queryCategoryLevel1.equals("")) {
			queryCategoryLevel1 = "0";
		}
		if (queryCategoryLevel2 == null || queryCategoryLevel2.equals("")) {
			queryCategoryLevel2 = "0";
		}
		if (queryCategoryLevel3 == null || queryCategoryLevel3.equals("")) {
			queryCategoryLevel3 = "0";
		}

		int count = appInfoService.selectBackendCount(querySoftwareName,
				Integer.parseInt(queryStatus),
				Integer.parseInt(queryFlatformId),
				Integer.parseInt(queryCategoryLevel1),
				Integer.parseInt(queryCategoryLevel2),
				Integer.parseInt(queryCategoryLevel3));
		int pageCount = count % Constans.PAGE_SIZE == 0 ? count
				/ Constans.PAGE_SIZE : count / Constans.PAGE_SIZE + 1;
		System.out.println("================" + count);
		Map map = new HashMap();
		map.put("totalCount", count);
		map.put("totalPageCount", pageCount);
		map.put("currentPageNo", pageIndex);
		session.setAttribute("pages", map);
		// 显示所有
		List<AppInfo> appInfoList = appInfoService.backendList(querySoftwareName,
				Integer.parseInt(queryStatus),
				Integer.parseInt(queryFlatformId),
				Integer.parseInt(queryCategoryLevel1),
				Integer.parseInt(queryCategoryLevel2),
				Integer.parseInt(queryCategoryLevel3),
				Integer.parseInt(pageIndex), Constans.PAGE_SIZE);

		// app状态
		List<DataDictionary> statusList = dataDictionaryService.statusList();

		// 所属平台
		List<DataDictionary> flatFormList = dataDictionaryService
				.flatFormList();
		session.setAttribute("appInfoList", appInfoList);
		session.setAttribute("statusList", statusList);
		session.setAttribute("flatFormList", flatFormList);
		List<AppCategory> categoryLevel1List = appCategoryService.categoryLevel1List();

		session.setAttribute("categoryLevel1List", categoryLevel1List);
		return "backend/applist";
	}
	
	
	// 一二三级分类
	@RequestMapping(value = "/categoryLevelABC", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String categoryLevelABC(String pid) {
		if (pid == null || pid.equals("")) {
			pid = "0";
		}
		System.out.println(pid);
		List<AppCategory> categoryLevel2List = appCategoryService.categoryLevel2List(Integer.parseInt(pid));
		System.out.println(categoryLevel2List.size());
		return JSONArray.toJSONString(categoryLevel2List);
	}
	
	
	@RequestMapping(value="/check")
	public String check(Integer vid,Integer aid,HttpServletRequest request){
		AppInfo appInfo = appInfoService.getInfoById(aid);
		request.setAttribute("appInfo", appInfo);
		AppVersion appVersion =appVersionService.selectVerById(vid, aid);
		request.setAttribute("appVersion", appVersion);
		return "backend/appcheck";
	}
	
	@RequestMapping(value="/checksave",method=RequestMethod.POST)
	public String checksave(@RequestParam String status,@RequestParam String id){
		boolean flag = appInfoService.updateStatus(Integer.parseInt(status),Integer.parseInt(id));
		if(flag){
			return "redirect:/list";
		}else{
			return "backend/appcheck";
		}
	}
}
