package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppCategory;
import org.appsys.pojo.AppInfo;
import org.appsys.pojo.DataDictionary;
import org.appsys.pojo.DevUser;

public interface DevUserService {
	/**
	 * 登陆开发者平台
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser selectUserByNameAndPwd(@Param("devCode") String devCode,@Param("devPassword") String devPassword );
	
	
	/**
	 * app列表
	 */
	public List<AppInfo> appList(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3,@Param("index") int index,@Param("pageSize") int pageSize);
	
	/**
	 * 查询总记录数
	 */
	
	public int selectAppCount(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3);
	
	/**
	 * app状态
	 */
	
	public List<DataDictionary> statusList();
	
	/**
	 * 审核状态
	 */
	public List<DataDictionary> flatFormList();
	
	
	
	/**
	 * 一级列表
	 */
	public List<AppCategory> categoryLevel1List();
	
	/**
	 * 二三级列表
	 */
	public List<AppCategory> categoryLevel2List(@Param("id") int id);
	
	/**
	 * 一二三
	 */
	public List<AppCategory> categoryLevelAll(@Param("id") int id);
	
	/**
	 * 查看apk名是否存在
	 */
	
	public AppInfo selectDevByName(@Param("APKName") String APKName);
	
	/**
	 * 添加App
	 */
	public boolean addAppInfo(AppInfo appInfo);
	
	/**
	 * 根据id查询
	 */
	
	public AppInfo selectAppById(@Param("id") int id);
	
}
