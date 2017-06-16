/**
 * 
 */
package org.appsys.service;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.BackendUserMapper;
import org.appsys.pojo.BackendUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("backendUserService")
public class BackendUserServiceImpl implements BackendUserService {
	@Autowired
	BackendUserMapper backendUserMapper;
	
	/* (non-Javadoc)
	 * @see org.appsys.service.BackendUserService#selectUserByNameAndPwd(java.lang.String, java.lang.String)
	 */
	public void setBackendUserMapper(BackendUserMapper backendUserMapper) {
		this.backendUserMapper = backendUserMapper;
	}

	@Override
	public BackendUser selectUserByNameAndPwd(
			@Param("userCode") String userCode,
			@Param("userPassword") String userPassword) {
		return backendUserMapper.selectUserByNameAndPwd(userCode, userPassword);
	}

}
