/**
 * 
 */
package org.appsys.service;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.DevMapper;
import org.appsys.pojo.DevUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
	
	@Autowired
	DevMapper devMapper;
	/* (non-Javadoc)
	 * @see org.appsys.service.DevUserService#selectUserByNameAndPwd(java.lang.String, java.lang.String)
	 */
	
	@Override
	public DevUser selectUserByNameAndPwd(@Param("devCode") String devCode,@Param("devPassword") String devPassword) {
		return devMapper.selectUserByNameAndPwd(devCode, devPassword);

	}

}
