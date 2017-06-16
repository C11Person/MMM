package org.appsys.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	
	/**
	 * 根据id查询版本
	 * @param id
	 * @return
	 */
	public List<AppVersion> getappVersionById(@Param("id")int id);
	
	
	/**
	 * 添加版本
	 */
	public int addVersion(AppVersion appVersion);
	
	/**
	 * 获取上次添加的id
	 */
	public int addVerId();
	
	
	/**
	 * 修改版本信息
	 */
	public int updateVersion(AppVersion appVersion);
	
	/**
	 * id查询最新版本
	 */
	public  AppVersion selectVerById(@Param("id")int id,@Param("appId")int appId);
}
