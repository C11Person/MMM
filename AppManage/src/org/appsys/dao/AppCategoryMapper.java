package org.appsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	/**
	 * 一级列表
	 */
	public List<AppCategory> categoryLevel1List();
	
	/**
	 * 二三级列表
	 */
	public List<AppCategory> categoryLevel2List(@Param("id") int id);
}
