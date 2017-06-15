package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppVersion;

public interface AppVersionService {
	public List<AppVersion> getAppVersionById(@Param("id") int id);
}