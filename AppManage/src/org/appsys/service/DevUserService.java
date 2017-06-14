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
	


	
}
