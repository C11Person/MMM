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
	private AppVersionMapper appVersionMapper;
	/* (non-Javadoc)
	 * @see org.appsys.service.AppVersionService#getAppVersionById(int)
	 */

	@Override
	public boolean addVersion(AppVersion appVersion) {
		int row = appVersionMapper.addVersion(appVersion);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}
	public void setAppVersionMapper(AppVersionMapper appVersionMapper) {
		this.appVersionMapper = appVersionMapper;
	}
	@Override
	public int addVerId() {
		return appVersionMapper.addVerId();
	}
	@Override
	public boolean  updateVersion(AppVersion appVersion) {
		int row = appVersionMapper.updateVersion(appVersion);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public List<AppVersion> getappVersionById(@Param("id")int id) {
		return appVersionMapper.getappVersionById(id);
	}
	@Override
	public AppVersion selectVerById(@Param("id") int id,
			@Param("appId") int appId) {
		return appVersionMapper.selectVerById(id, appId);
	}

}
