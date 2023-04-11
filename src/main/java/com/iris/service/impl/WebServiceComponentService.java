package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.caching.ObjectCache;
import com.iris.exception.ServiceException;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.WebServiceComponentUrlRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class WebServiceComponentService implements GenericService<WebServiceComponentUrl, Long> {

	static final Logger LOGGER = LogManager.getLogger(WebServiceComponentService.class);

	@Autowired
	private WebServiceComponentUrlRepo webServiceComponentUrlRepo;

	@Override
	public WebServiceComponentUrl add(WebServiceComponentUrl entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(WebServiceComponentUrl entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WebServiceComponentUrl> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WebServiceComponentUrl> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		try {
			List<String> componentTypeList = null;
			List<String> methodTypeList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && !CollectionUtils.isEmpty(columnValueMap.get(columnName))) {
					if (columnName.equalsIgnoreCase(ColumnConstants.COMPONENTTYPE.getConstantVal())) {
						componentTypeList = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.METHODTYPE.getConstantVal())) {
						methodTypeList = columnValueMap.get(columnName);
					}
				}
			}

			if (componentTypeList != null && !CollectionUtils.isEmpty(componentTypeList) && methodTypeList != null && !CollectionUtils.isEmpty(methodTypeList)) {
				if (ObjectCache.getCacheMap().containsKey(componentTypeList.get(0) + "~" + methodTypeList.get(0))) {
					return new ArrayList<>(Arrays.asList((WebServiceComponentUrl) ObjectCache.getCacheMap().get(componentTypeList.get(0) + "~" + methodTypeList.get(0))));
				} else {
					if (methodName.equalsIgnoreCase(MethodConstants.GET_WEBSERVICE_COMPONENT_DATA.getConstantVal())) {
						return webServiceComponentUrlRepo.findByComponentTypeInAndUrlHttpMethodTypeInAndIsActiveTrue(componentTypeList, methodTypeList);
					}
				}
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<WebServiceComponentUrl> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<WebServiceComponentUrl> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<WebServiceComponentUrl> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return webServiceComponentUrlRepo.findAll();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal());
		}
	}

	@Override
	public void deleteData(WebServiceComponentUrl bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public WebServiceComponentUrl getDataById(Long id) throws ServiceException {
		return webServiceComponentUrlRepo.getDataById(id);
	}

	@Override
	public List<WebServiceComponentUrl> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	//	public WebServiceComponentUrl findByComponentTypeAndUrlHttpMethodTypeAndIsActiveTrue(String constantVal,
	//			String httpMethodTypePost) {
	//		return webServiceComponentUrlRepo.findByComponentTypeAndUrlHttpMethodTypeAndIsActiveTrue(constantVal, httpMethodTypePost);
	//	}

}
