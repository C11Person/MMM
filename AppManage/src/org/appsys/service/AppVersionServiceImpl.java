/**
 * 
 */
package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.AppVersionMapper;
import org.appsys.pojo.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {
	@Autowired
	AppVersionMapper appVersionMapper;
	/* (non-Javadoc)
	 * @see org.appsys.service.AppVersionService#getAppVersionById(int)
	 */
	@Override
	public List<AppVersion> getAppVersionById(@Param("id") int id) {
		// TODO Auto-generated method stub
		return appVersionMapper.getAppVersionById(id);
	}
	@Override
	public boolean addVersion(AppVersion appVersion) {
		int row = appVersionMapper.addVersion(appVersion);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}

}
