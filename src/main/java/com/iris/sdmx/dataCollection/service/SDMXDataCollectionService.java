package com.iris.sdmx.dataCollection.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.dataCollection.bean.SDMXDataCollectionLimitedFieldBean;
import com.iris.sdmx.dataCollection.entity.SDMXDataCollection;
import com.iris.sdmx.dataCollection.repo.SDMXDataCollectionRepo;
import com.iris.service.GenericService;

@Service
public class SDMXDataCollectionService implements GenericService<SDMXDataCollection, Long> {

	static final Logger LOGGER = LogManager.getLogger(SDMXDataCollectionService.class);

	@Autowired
	private SDMXDataCollectionRepo sdmxDataCollectionRepo;

	@Override
	public SDMXDataCollection add(SDMXDataCollection entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SDMXDataCollection entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SDMXDataCollection> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDMXDataCollection getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SDMXDataCollection> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SDMXDataCollection> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SDMXDataCollection> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SDMXDataCollection> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SDMXDataCollection> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SDMXDataCollection bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param processCode
	 * @param categoryLable
	 * @return
	 */
	public List<SDMXDataCollectionLimitedFieldBean> getSdmxDataCollectionByProcessIdNCategory(String processCode) {
		List<SDMXDataCollectionLimitedFieldBean> sdmxDataCollectionLimitedFieldBeanList = null;
		sdmxDataCollectionLimitedFieldBeanList = sdmxDataCollectionRepo.getSdmxDataCollectionByProcessId(processCode);
		return sdmxDataCollectionLimitedFieldBeanList;
	}

}
