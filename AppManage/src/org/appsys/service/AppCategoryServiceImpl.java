/**
 * 
 */
package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.dao.AppCategoryMapper;
import org.appsys.pojo.AppCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {
	@Autowired
	AppCategoryMapper appCategoryMapper;
	/* (non-Javadoc)
	 * @see org.appsys.service.AppCategoryService#categoryLevel1List()
	 */
	@Override
	public List<AppCategory> categoryLevel1List() {
		return appCategoryMapper.categoryLevel1List();
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.AppCategoryService#categoryLevel2List(int)
	 */
	@Override
	public List<AppCategory> categoryLevel2List(@Param("id") int id) {
		return appCategoryMapper.categoryLevel2List(id);
	}

}
