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
	public List<AppVersion> getAppVersionById(@Param("id") int id);
	
	
	/**
	 * 添加版本
	 */
	public int addVersion(AppVersion appVersion);
}
