package com.iris.sdmx.sdmxDataModelCodesDownloadService;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnPreviewEntity;
import com.iris.service.GenericService;

@Service
public class SdmxFillingDocumentDownload implements GenericService<SdmxReturnPreviewEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxFillingDocumentDownload.class);

	@Override
	public SdmxReturnPreviewEntity add(SdmxReturnPreviewEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxReturnPreviewEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxReturnPreviewEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxReturnPreviewEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
