package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.SdmxWebserviceUrl;
import com.iris.repository.SdmxWebServiceUrlRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class SdmxWebService implements GenericService<SdmxWebserviceUrl, Long> {

	static final Logger LOGGER = LogManager.getLogger(SdmxWebService.class);

	@Autowired
	private SdmxWebServiceUrlRepo sdmxWebServiceUrlRepo;

	@Override
	public SdmxWebserviceUrl add(SdmxWebserviceUrl entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxWebserviceUrl entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxWebserviceUrl> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxWebserviceUrl getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxWebserviceUrl> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxWebserviceUrl> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxWebserviceUrl> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxWebserviceUrl> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxWebserviceUrl> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return sdmxWebServiceUrlRepo.findByIsActiveTrue();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal());
		}
	}

	@Override
	public void deleteData(SdmxWebserviceUrl bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
