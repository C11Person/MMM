package org.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
public class DevUserController {
	private Logger logger =Logger.getLogger("DevUserController.class");
	@Autowired
	DevUserService devUserService;

	@RequestMapping(value = "/login.html")
	public String lo() {
		return "devlogin";
	}

	@RequestMapping(value = "/dologin.html", method = RequestMethod.POST)
	public String login(String devCode, String devPassword, HttpSession session) {
		/*
		 * ApplicationContext context = new
		 * ClassPathXmlApplicationContext("SpringConfig.xml"); DevUserService
		 * devUserService = (DevUserService) context.getBean("devUserService");
		 */
		DevUser devUser = devUserService.selectUserByNameAndPwd(devCode,
				devPassword);
		if(devUser != null){
			session.setAttribute("devUserSession", devUser);
			return "developer/main";
		}else{
			session.setAttribute("error", "用户名或密码错误");
			return "devlogin";
		}
	}

	@RequestMapping(value = "/dev/logout.html")
	public String loginout(HttpSession session) {
		session.invalidate();
		return "devlogin";
	}

	// APP维护
	@RequestMapping(value = "/list.html")
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

		int count = devUserService.selectAppCount(querySoftwareName,
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
		List<AppInfo> appInfoList = devUserService.appList(querySoftwareName,
				Integer.parseInt(queryStatus),
				Integer.parseInt(queryFlatformId),
				Integer.parseInt(queryCategoryLevel1),
				Integer.parseInt(queryCategoryLevel2),
				Integer.parseInt(queryCategoryLevel3),
				Integer.parseInt(pageIndex), Constans.PAGE_SIZE);

		// app状态
		List<DataDictionary> statusList = devUserService.statusList();

		// 所属平台
		List<DataDictionary> flatFormList = devUserService.flatFormList();

		// 一级分类
		List<AppCategory> categoryLevel1List = devUserService
				.categoryLevel1List();
		session.setAttribute("appInfoList", appInfoList);
		session.setAttribute("statusList", statusList);
		session.setAttribute("flatFormList", flatFormList);
		session.setAttribute("categoryLevel1List", categoryLevel1List);
		return "developer/appinfolist";
	}

	@RequestMapping(value = "/categorylevel2list", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String li2(String pid) {
		List<AppCategory> categoryLevel2List = devUserService.categoryLevel2List(Integer.parseInt(pid));
		return JSONArray.toJSONString(categoryLevel2List);
	}

	@RequestMapping(value = "/appinfoadd.html")
	public String appinfoadd(){
		return "developer/appinfoadd";
	}
	
	
	@RequestMapping(value = "/datadictionarylist", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String datadictionarylist() {
		List<DataDictionary> datadictionarylist = devUserService.flatFormList();
		return JSONArray.toJSONString(datadictionarylist);
	}
	
	@RequestMapping(value = "/categoryLevel1List", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String categoryLevel1List() {
		List<AppCategory> categoryLevel1List = devUserService.categoryLevel1List();
		return JSONArray.toJSONString(categoryLevel1List);
	}
	
	@RequestMapping(value = "/apkexist", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String selectDevByName(String APKName) {
		AppInfo appInfo= devUserService.selectDevByName(APKName);
		HashMap<String,String> resutlt = new HashMap<String, String>();
		if(APKName.isEmpty()){
			resutlt.put("APKName","empty");
		}else if(appInfo!=null){
			resutlt.put("APKName","exist");
		}else if(appInfo==null){
			resutlt.put("APKName","noexist");
		}
		
		return JSONArray.toJSONString(resutlt);
	}
	
	
	//添加
	@RequestMapping(value="/appinfoaddsave.html",method=RequestMethod.POST)
	public String useradd( AppInfo appInfo,HttpSession session,HttpServletRequest request,
			 @RequestParam(value ="a_logoPicPath", required = false) MultipartFile attach){
		String hid_logoPicPath = request.getParameter("hid_logoPicPath");
		System.out.println(hid_logoPicPath);
/*			@Valid
 * 			BindingResult bindingResult
 * 			if(bindingResult.hasErrors()){  
			return "developer/appinfoadd";			
		}*/
		String logoLocPath = null;
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");			
			String oldFileName = attach.getOriginalFilename();//原文件名		
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀   
			int filesize = 512000;
			
	        if(attach.getSize() >  filesize){//上传大小不得超过 500k
           	request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
	        	return "useradd";
           }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
           		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
           	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";  
               logger.debug("new fileName======== " + attach.getName());
               File targetFile = new File(path, fileName);  
               if(!targetFile.exists()){  
                   targetFile.mkdirs();  
               }  
               //保存  
               try {  
               	attach.transferTo(targetFile);  
               } catch (Exception e) {  
                   e.printStackTrace();  
                   request.setAttribute("uploadFileError", " * 上传失败！");
                   return "developer/appinfoadd";
               }  
               logoLocPath = path+File.separator+fileName;
           }else{
           	request.setAttribute("uploadFileError", " * 上传图片格式不正确");
           	return "developer/appinfoadd";
           }
		}
		int devId=((DevUser)session.getAttribute("devUserSession")).getId();
		appInfo.setDevId(devId);
		appInfo.setCreatedBy(devId);
		appInfo.setCreationDate(new Date());
		appInfo.setStatus(1);
/*		hid_logoPicPath=request.getParameter("hid_logoPicPath");
		System.out.println("==========================================================="+hid_logoPicPath);*/
		appInfo.setLogoPicPath(hid_logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		boolean flag = devUserService.addAppInfo(appInfo);
		if(flag){
			System.out.println("添加成功！");
			return "redirect:/list.html";
		}else{
			System.out.println("添加失败！");
			return "false";
		}
	}
	
	
	//修改
	@RequestMapping(value="/appinfomodify/{appinfoid}")
	public String updateAppinfo(@PathVariable int appinfoid,HttpSession session){
		
		AppInfo appInfo=devUserService.selectAppById(appinfoid);
		session.setAttribute("appInfo", appInfo);
		return "developer/appinfomodify";
	}
	
	
}