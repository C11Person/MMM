package org.appsys.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppVersion;

public interface AppVersionService {
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public List<AppVersion> getAppVersionById(@Param("id") int id);
	
	/**
	 * 添加版本
	 */
	public boolean addVersion(AppVersion appVersion);
}
