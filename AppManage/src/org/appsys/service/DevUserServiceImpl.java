/**
 * 
 */
package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.DevMapper;
import org.appsys.pojo.AppCategory;
import org.appsys.pojo.AppInfo;
import org.appsys.pojo.DataDictionary;
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
	@Override
	public List<DataDictionary> statusList() {
		return devMapper.statusList();
	}

	@Override
	public List<DataDictionary> flatFormList() {
		return devMapper.flatFormList();
	}

	@Override
	public List<AppCategory> categoryLevel1List() {
		return devMapper.categoryLevel1List();
	}

	@Override
	public List<AppCategory> categoryLevel2List(@Param("id") int id) {
		// TODO Auto-generated method stub
		return devMapper.categoryLevel2List(id);
	}
	@Override
	public List<AppInfo> appList(@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3,
			@Param("index") int index, @Param("pageSize") int pageSize) {
		return devMapper.appList(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3,(index-1)*pageSize, pageSize);
	}
	@Override
	public int selectAppCount(@Param("softwareName") String softwareName,
			@Param("STATUS") int STATUS, @Param("flatformId") int flatformId,
			@Param("categoryLevel1") int categoryLevel1,
			@Param("categoryLevel2") int categoryLevel2,
			@Param("categoryLevel3") int categoryLevel3) {
		return devMapper.selectAppCount(softwareName, STATUS, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}



}
