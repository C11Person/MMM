package org.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppCategory;

public interface AppCategoryService {
	/**
	 * 一级列表
	 */
	public List<AppCategory> categoryLevel1List();
	
	/**
	 * 二三级列表
	 */
	public List<AppCategory> categoryLevel2List(@Param("id") int id);
}
