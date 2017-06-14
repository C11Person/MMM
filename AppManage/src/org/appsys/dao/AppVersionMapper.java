package org.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	public List<AppVersion> getAppVersionById(@Param("id") int id);
}
