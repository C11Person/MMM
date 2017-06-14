package org.appsys.dao;

import java.util.List;

import org.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	/**
	 * app状态
	 */
	
	public List<DataDictionary> statusList();
	
	/**
	 * 审核状态
	 */
	public List<DataDictionary> flatFormList();
}
