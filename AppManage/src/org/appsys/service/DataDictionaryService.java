package org.appsys.service;

import java.util.List;

import org.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	/**
	 * app状态
	 */
	
	public List<DataDictionary> statusList();
	
	/**
	 * 审核状态
	 */
	public List<DataDictionary> flatFormList();
}
