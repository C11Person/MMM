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
import org.appsys.dao.AppVersionMapper;
import org.appsys.pojo.AppCategory;
import org.appsys.pojo.AppInfo;
import org.appsys.pojo.AppVersion;
import org.appsys.pojo.DataDictionary;
import org.appsys.pojo.DevUser;
import org.appsys.service.AppCategoryService;
import org.appsys.service.AppInfoService;
import org.appsys.service.AppVersionService;
import org.appsys.service.DataDictionaryService;
import org.appsys.service.DevUserService;
import org.appsys.tool.Constans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private Logger logger = Logger.getLogger("DevUserController.class");
	@Autowired
	DevUserService devUserService;
	@Autowired
	AppInfoService appInfoService;
	@Autowired
	DataDictionaryService dataDictionaryService;
	@Autowired
	AppCategoryService appCategoryService;
	@Autowired
	AppVersionService appVersionService;

	// 跳转到登陆界面
	@RequestMapping(value = "/login.html")
	public String lo() {
		return "devlogin";
	}

	// 登陆
	@RequestMapping(value = "/dologin.html", method = RequestMethod.POST)
	public String login(String devCode, String devPassword, HttpSession session) {
		/*
		 * ApplicationContext context = new
		 * ClassPathXmlApplicationContext("SpringConfig.xml"); DevUserService
		 * devUserService = (DevUserService) context.getBean("devUserService");
		 */
		DevUser devUser = devUserService.selectUserByNameAndPwd(devCode,
				devPassword);
		if (devUser != null) {
			session.setAttribute("devUserSession", devUser);
			return "developer/main";
		} else {
			session.setAttribute("error", "用户名或密码错误");
			return "devlogin";
		}
	}

	// 注销
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

		int count = appInfoService.selectAppCount(querySoftwareName,
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
		List<AppInfo> appInfoList = appInfoService.appList(querySoftwareName,
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

		// 一级分类
		List<AppCategory> categoryLevel1List = appCategoryService
				.categoryLevel1List();
		session.setAttribute("appInfoList", appInfoList);
		session.setAttribute("statusList", statusList);
		session.setAttribute("flatFormList", flatFormList);
		session.setAttribute("categoryLevel1List", categoryLevel1List);
		return "developer/appinfolist";
	}

	// 跳转到添加App页面
	@RequestMapping(value = "/appinfoadd.html")
	public String appinfoadd() {
		return "developer/appinfoadd";
	}

	// 所属平台
	@RequestMapping(value = "/datadictionarylist", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String datadictionarylist() {
		List<DataDictionary> datadictionarylist = dataDictionaryService
				.flatFormList();
		return JSONArray.toJSONString(datadictionarylist);
	}

	// 一级分类
	@RequestMapping(value = "/categoryLevel1List", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String categoryLevel1List() {
		List<AppCategory> categoryLevel1List = appCategoryService
				.categoryLevel1List();
		return JSONArray.toJSONString(categoryLevel1List);
	}

	// 判断apk名称是否存在
	@RequestMapping(value = "/apkexist", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String selectDevByName(String APKName) {
		AppInfo appInfo = appInfoService.selectDevByName(APKName);
		HashMap<String, String> resutlt = new HashMap<String, String>();
		if (APKName.isEmpty()) {
			resutlt.put("APKName", "empty");
		} else if (appInfo != null) {
			resutlt.put("APKName", "exist");
		} else if (appInfo == null) {
			resutlt.put("APKName", "noexist");
		}

		return JSONArray.toJSONString(resutlt);
	}

	// 添加
	@RequestMapping(value = "/appinfoaddsave.html", method = RequestMethod.POST)
	public String useradd(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
		String hid_logoPicPath = request.getParameter("hid_logoPicPath");
		System.out.println(hid_logoPicPath);
		String logoLocPath = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 512000;

			if (attach.getSize() > filesize) {// 上传大小不得超过 500k
				request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
				return "useradd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
				logger.debug("new fileName======== " + attach.getName());
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", " * 上传失败！");
					return "developer/appinfoadd";
				}
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("uploadFileError", " * 上传图片格式不正确");
				return "developer/appinfoadd";
			}
		}
		int devId = ((DevUser) session.getAttribute("devUserSession")).getId();
		appInfo.setDevId(devId);
		appInfo.setCreatedBy(devId);
		appInfo.setCreationDate(new Date());
		appInfo.setStatus(1);
		appInfo.setLogoPicPath(hid_logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		boolean flag = appInfoService.addAppInfo(appInfo);
		if (flag) {
			System.out.println("添加成功！");
			return "redirect:/list.html";
		} else {
			System.out.println("添加失败！");
			return "false";
		}
	}

	// 修改
	@RequestMapping(value = "/appinfomodify")
	public String updateAppinfo(Integer id, Model model, HttpSession session) {
		AppInfo appInfo = appInfoService.selectAppById(id);
		session.setAttribute("appInfo", appInfo);
		model.addAttribute("pid", id);
		return "developer/appinfomodify";
	}

	// 一二三级分类
	@RequestMapping(value = "/categoryLevelAll", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String categoryLevelAll(String pid) {
		if (pid == null || pid.equals("")) {
			pid = "0";
		}
		List<AppCategory> categoryLevel2List = appCategoryService
				.categoryLevel2List(Integer.parseInt(pid));
		return JSONArray.toJSONString(categoryLevel2List);
	}

	// 修改界面删除图片
	@RequestMapping(value = "/delfile", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object deletephoto() {
		HashMap<String, String> resutlt = new HashMap<String, String>();
		resutlt.put("result", "success");
		return JSONArray.toJSONString(resutlt);
	}

	// 修改App信息
	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String updateApp(
			AppInfo appInfo,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {
		String hid_logoPicPath = request.getParameter("hid_logoPicPath");
		System.out.println(hid_logoPicPath);
		String logoLocPath = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 512000;

			if (attach.getSize() > filesize) {// 上传大小不得超过 500k
				request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
				return "useradd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
				logger.debug("new fileName======== " + attach.getName());
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", " * 上传失败！");
					return "developer/appinfomodify";
				}
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("uploadFileError", " * 上传图片格式不正确");
				return "developer/appinfomodify";
			}
		}
		int devId = ((DevUser) session.getAttribute("devUserSession")).getId();
		appInfo.setDevId(devId);
		appInfo.setModifyBy(devId);
		appInfo.setModifyDate(new Date());
		appInfo.setLogoPicPath(hid_logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		boolean flag = appInfoService.updateAppInfo(appInfo);
		if (flag) {
			logger.info("修改成功");
			return "redirect:/list.html";
		} else {
			logger.info("修改失败");
			return "false";
		}
	}

	// 显示view
	@RequestMapping(value = "/appview/{appinfoid}")
	public String appView(@PathVariable int appinfoid, HttpSession session) {
		AppInfo appInfo = appInfoService.getInfoById(appinfoid);
		List<AppVersion> appVersionList = appVersionService
				.getappVersionById(appinfoid);
		session.setAttribute("appInfo", appInfo);
		session.setAttribute("appVersionList", appVersionList);
		return "developer/appinfoview";
	}

	// 删除App
	@RequestMapping(value = "/delapp", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object deleteApp(int id) {
		HashMap<String, String> resutlt = new HashMap<String, String>();
		AppInfo appInfo = appInfoService.selectAppById(id);
		if (appInfo == null) {
			resutlt.put("delResult", "notexist");
		}
		boolean flag = appInfoService.deleteAppInfo(id);
		if (flag) {
			resutlt.put("delResult", "true");
		} else {
			resutlt.put("delResult", "false");
		}

		return JSONArray.toJSONString(resutlt);
	}

	// 跳转到新增版本页面
	@RequestMapping(value = "/appversionadd")
	public String appversionadd(int id, HttpSession session,HttpServletRequest request) {
		List<AppVersion> appVersionList = appVersionService
				.getappVersionById(id);
		Map map = new HashMap();
		map.put("appId",id);
		session.setAttribute("appVersion",map);
		request.setAttribute("id",id);
		session.setAttribute("appVersionList", appVersionList);
		return "developer/appversionadd";
	}
	//新增版本
	@RequestMapping(value = "addversionsave",method=RequestMethod.POST)
	public String addversionsave(AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach) {
		String hid_downloadLink = request.getParameter("hid_downloadLink");
		String logoLocPath = null;
		String FileNameee=null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			FileNameee=oldFileName;
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 999999999;

			if (attach.getSize() > filesize) {// 上传大小不得超过 500k
				request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
				return "useradd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")
					|| prefix.equalsIgnoreCase("apk")) {// 上传文件格式不正确
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + oldFileName;
				logger.debug("new fileName======== " + attach.getName());
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", " * 上传失败！");
					return "developer/appversionadd";
				}
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("uploadFileError", " * 上传图片格式不正确");
				return "developer/appversionadd";
			}
		}
		int devId = ((DevUser) session.getAttribute("devUserSession")).getId();
		appVersion.setPublishStatus(3);
		appVersion.setCreatedBy(devId);
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(hid_downloadLink);
		appVersion.setApkFileName(FileNameee);
		appVersion.setApkLocPath(logoLocPath);
		boolean flag= appVersionService.addVersion(appVersion);
		if (flag) {
			logger.info("添加成功");
			int versionid = appVersionService.addVerId();
			System.out.println();
			System.out.println(appVersion.getId());
			appInfoService.upAppInfo(versionid,appVersion.getAppId());
			return "redirect:/list.html";
		} else {
			logger.info("添加失败");
			return "false";
		}
	}
	
	
	//修改版本信息
	@RequestMapping(value="/appversionmodify")
	public String appversionmodify(Integer vid,Integer aid,HttpServletRequest request){
		List<AppVersion> vers = appVersionService.getappVersionById(aid);
		request.setAttribute("appVersionList", vers);
		if(vid != null){
			AppVersion appVersion =appVersionService.selectVerById(vid, aid);
			request.setAttribute("appVersion", appVersion);
		}else{
			Map m = new HashMap();
			m.put("id", vid);
			m.put("appId", aid);
			request.setAttribute("appVersion", m);
		}
		return "developer/appversionmodify";
	}
	
	
	@RequestMapping(value="/delfile")
	@ResponseBody
	public Object  delVer(String id){
		Map m = new HashMap();
		m.put("result", "success");
		return JSONArray.toJSONString(m);

	}
	
	
	@RequestMapping(value="appversionmodifysave",method=RequestMethod.POST)
	public String updateVersion(AppVersion appVersion,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach){
		String hid_downloadLink = request.getParameter("hid_downloadLink");
		String logoLocPath = null;
		String FileNameee=null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			FileNameee=oldFileName;
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 999999999;

			if (attach.getSize() > filesize) {// 上传大小不得超过 500k
				request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
				return "useradd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")
					|| prefix.equalsIgnoreCase("apk")) {// 上传文件格式不正确
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + oldFileName;
				logger.debug("new fileName======== " + attach.getName());
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", " * 上传失败！");
					return "developer/appversionadd";
				}
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("uploadFileError", " * 上传图片格式不正确");
				return "developer/appversionadd";
			}
		}
		int devId = ((DevUser) session.getAttribute("devUserSession")).getId();
		appVersion.setModifyBy(devId);
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(hid_downloadLink);
		appVersion.setApkLocPath(logoLocPath);
		boolean flag = appVersionService.updateVersion(appVersion);
		if (flag) {
			logger.info("添加成功");
			return "redirect:/list.html";
		} else {
			logger.info("添加失败");
			return "false";
		}
		
		
		
	}

}