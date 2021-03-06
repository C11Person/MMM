/**
 * 
 */
package org.appsys.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.AppInfoMapper;
import org.appsys.pojo.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService {
	@Autowired
	AppInfoMapper appInfoMapper;
	/* (non-Javadoc)
	 * @see org.appsys.service.AppInfoService#appList(java.lang.String, int, int, int, int, int, int, int)
	 */
	public void setAppInfoMapper(AppInfoMapper appInfoMapper) {
		this.appInfoMapper = appInfoMapper;
	}

	@Override
	public List<AppInfo> appList(@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3,
			@Param("index") int index, @Param("pageSize") int pageSize) {
		return appInfoMapper.appList(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3,(index-1)*pageSize, pageSize);
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.AppInfoService#selectAppCount(java.lang.String, int, int, int, int, int)
	 */
	@Override
	public int selectAppCount(@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3) {
		return appInfoMapper.selectAppCount(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.AppInfoService#selectDevByName(java.lang.String)
	 */
	@Override
	public AppInfo selectDevByName(@Param("APKName") String APKName) {
		return appInfoMapper.selectDevByName(APKName);
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.AppInfoService#addAppInfo(org.appsys.pojo.AppInfo)
	 */
	@Override
	public boolean addAppInfo(AppInfo appInfo) {
		int row = appInfoMapper.addAppInfo(appInfo);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.AppInfoService#selectAppById(int)
	 */
	@Override
	public AppInfo selectAppById(@Param("id") int id) {
		return appInfoMapper.selectAppById(id);
	}

	@Override
	public AppInfo getInfoById(@Param("id") int id) {
		return appInfoMapper.getInfoById(id);
	}

	@Override
	public boolean updateAppInfo(AppInfo appInfo) {
		int row = appInfoMapper.updateAppInfo(appInfo);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteAppInfo(@Param("id") int id) {
		int row = appInfoMapper.deleteAppInfo(id);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int upAppInfo(@Param("versionId") int versionId, @Param("id") int id) {
		return appInfoMapper.upAppInfo(versionId, id);
	}

	@Override
	public int appOnSale(@Param("status") int status,
			@Param("onSaleDate") Date onSaleDate, @Param("id") int id) {
		return  appInfoMapper.appOnSale(status, onSaleDate, id);

	}

	@Override
	public int appOffSale(@Param("status") int status,
			@Param("offSaleDate") Date offSaleDate, @Param("id") int id) {
		return  appInfoMapper.appOffSale(status, offSaleDate, id);

	}

	@Override
	public List<AppInfo> backendList(
			@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3,
			@Param("index") int index, @Param("pageSize") int pageSize) {
		return appInfoMapper.backendList(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3,(index-1)*pageSize, pageSize);
	}

	@Override
	public int selectBackendCount(@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3) {
		return appInfoMapper.selectBackendCount(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}

	@Override
	public boolean updateStatus(@Param("status") int status, @Param("id") int id) {
		int row =  appInfoMapper.updateStatus(status, id);
		if(row==1){
			return true;
		}else{
			return false;
		}
	}


}
