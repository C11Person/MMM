/**
 * 
 */
package org.appsys.service;

import java.util.List;

import org.appsys.dao.DataDictionaryMapper;
import org.appsys.pojo.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 时光与你皆薄凉
 *
 */
@Service("dataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {
	@Autowired
	DataDictionaryMapper dataDictionaryMapper;
	public void setDataDictionaryMapper(DataDictionaryMapper dataDictionaryMapper) {
		this.dataDictionaryMapper = dataDictionaryMapper;
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.DataDictionaryService#statusList()
	 */
	@Override
	public List<DataDictionary> statusList() {
		return dataDictionaryMapper.statusList();
	}

	/* (non-Javadoc)
	 * @see org.appsys.service.DataDictionaryService#flatFormList()
	 */
	@Override
	public List<DataDictionary> flatFormList() {
		return dataDictionaryMapper.flatFormList();
	}

}
