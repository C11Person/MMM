package org.appsys.service;


import org.apache.ibatis.annotations.Param;
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
