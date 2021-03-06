package org.appsys.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppInfo;

public interface AppInfoMapper {

	/**
	 * app信息列表
	 */
	public List<AppInfo> appList(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3,@Param("index") int index,@Param("pageSize") int pageSize);
	
	/**
	 * 查询总记录数
	 */
	
	public int selectAppCount(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3);
	

	/**
	 * 查看apk名是否存在
	 */
	
	public AppInfo selectDevByName(@Param("APKName") String APKName);
	
	/**
	 * 添加App
	 */
	public int addAppInfo(AppInfo appInfo);
	
	/**
	 * 修改App
	 */
	public int updateAppInfo(AppInfo appInfo);
	
	
	/**
	 * 删除App
	 */
	
	public int deleteAppInfo (@Param("id") int id);
	
	
	/**
	 * 根据id查询
	 */
	
	public AppInfo selectAppById(@Param("id") int id);
	
	
	/**
	 * 显示详细信息
	 */
	public AppInfo getInfoById(@Param("id") int id);
	
	/**
	 * 修改版本id
	 */
	
	public int upAppInfo(@Param("versionId") int versionId,@Param("id") int id);
	
	
	/**
	 * 上架
	 * @return
	 */
	public int appOnSale(@Param("status") int status,@Param("onSaleDate") Date onSaleDate,@Param("id") int id);
	
	
	/**
	 * 下架
	 */
	public int appOffSale(@Param("status") int status,@Param("offSaleDate") Date offSaleDate,@Param("id") int id);
	
	
	/**
	 * 后台管理列表
	 */
	public List<AppInfo> backendList(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3,@Param("index") int index,@Param("pageSize") int pageSize);
	
	/**
	 * 后台列表总记录数
	 */
	
	public int selectBackendCount(@Param("softwareName") String softwareName,@Param("STATUS") int STATUS,@Param("flatformId") int flatformId,@Param("categoryLevel1") int categoryLevel1,@Param("categoryLevel2") int categoryLevel2,@Param("categoryLevel3") int categoryLevel3);
	
	/**
	 * 审核
	 */
	public int updateStatus(@Param("status") int status,@Param("id") int id);
}
