package org.appsys.service;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.BackendUser;

public interface BackendUserService {
	/**
	 * 登陆后台管理平台
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public BackendUser selectUserByNameAndPwd(@Param("userCode") String userCode,@Param("userPassword") String userPassword);
}
