package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ErrorCodeDetail;
import com.iris.model.ErrorCodeLabelMapping;
import com.iris.model.ErrorCodeLabelRequest;
import com.iris.model.LanguageMaster;
import com.iris.model.UserMaster;
import com.iris.repository.ErrorCodeDetailRepo;
import com.iris.repository.ErrorCodeLabelMappingRepository;
import com.iris.service.GenericService;

@Service
public class ErrorCodeLabelMappingService implements GenericService<ErrorCodeLabelMapping, Long> {

	@Autowired
	private ErrorCodeLabelMappingRepository errorCodeLabelMappingRepository;

	@Autowired
	private ErrorCodeDetailRepo errorCodeDetailRepo;

	static final Logger LOGGER = LogManager.getLogger(ErrorCodeLabelMappingService.class);

	@Override
	public ErrorCodeLabelMapping add(ErrorCodeLabelMapping entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ErrorCodeLabelMapping entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ErrorCodeLabelMapping> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorCodeLabelMapping getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorCodeLabelMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorCodeLabelMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorCodeLabelMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorCodeLabelMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorCodeLabelMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ErrorCodeLabelMapping bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<ErrorCodeLabelMapping> getErrorCodeLabelByErrorCodeIds(ErrorCodeLabelRequest errorCodeLabelRequest) {

		List<ErrorCodeDetail> errorCodeDetailList = new ArrayList<>();
		for (Long errorCodeDetailId : errorCodeLabelRequest.getErrorCodeIdList()) {
			ErrorCodeDetail errorCodeDetail = new ErrorCodeDetail();
			errorCodeDetail.setErrorCodeDetailId(errorCodeDetailId);
			errorCodeDetailList.add(errorCodeDetail);
		}
		List<ErrorCodeLabelMapping> errorCodeLabelMappingList = errorCodeLabelMappingRepository.getErrorCodeLabelByErrorCodeIds(errorCodeDetailList);
		List<ErrorCodeLabelMapping> returnErrorCodeLabelMappingsObj = new ArrayList<ErrorCodeLabelMapping>();
		for (ErrorCodeLabelMapping errorCodeLabelMapping : errorCodeLabelMappingList) {
			ErrorCodeLabelMapping returnErrorCodeLabelMappingObj = new ErrorCodeLabelMapping();
			returnErrorCodeLabelMappingObj.setErrorCodeLabelMappingId(errorCodeLabelMapping.getErrorCodeLabelMappingId());
			returnErrorCodeLabelMappingObj.setErrorCodeDetailIdFk(errorCodeLabelMapping.getErrorCodeDetailIdFk());
			if (errorCodeLabelRequest.getIsFileBasedLabelOnly()) {
				returnErrorCodeLabelMappingObj.setErrorKeyLabelForFileBased(errorCodeLabelMapping.getErrorKeyLabelForFileBased());
			}
			if (errorCodeLabelRequest.getIsWebBasedLabelOnly()) {
				returnErrorCodeLabelMappingObj.setErrorKeyLabelForWebBased(errorCodeLabelMapping.getErrorKeyLabelForWebBased());
			}
			LanguageMaster languageMaster = new LanguageMaster();
			languageMaster.setLanguageId(errorCodeLabelMapping.getLanguageIdFk().getLanguageId());
			returnErrorCodeLabelMappingObj.setLanguageIdFk(languageMaster);
			returnErrorCodeLabelMappingObj.setLastUpdatedOn(errorCodeLabelMapping.getLastUpdatedOn());
			returnErrorCodeLabelMappingObj.setCreatedOn(errorCodeLabelMapping.getCreatedOn());
			returnErrorCodeLabelMappingObj.setModifiedOn(errorCodeLabelMapping.getModifiedOn());
			//UserMaster userMaster = new UserMaster();
			//userMaster.setUserId(errorCodeLabelMapping.getCreatedBy().getUserId());
			returnErrorCodeLabelMappingObj.setCreatedBy(null);
			//userMaster.setUserId(errorCodeLabelMapping.getModifiedBy().getUserId());
			returnErrorCodeLabelMappingObj.setModifiedBy(null);
			returnErrorCodeLabelMappingsObj.add(returnErrorCodeLabelMappingObj);
		}
		return returnErrorCodeLabelMappingsObj;
	}

	public List<ErrorCodeDetail> getStaticErrorCodeLabels(List<String> staticErrorCodeList) {

		List<ErrorCodeDetail> errorCodeDetailList = errorCodeDetailRepo.getStaticErrorCodeLabels(staticErrorCodeList);
		List<ErrorCodeDetail> returnErrorCodeLabelMappingsObj = new ArrayList<>();
		ErrorCodeDetail errorCodeDetailBean = null;
		for (ErrorCodeDetail errorCodeDetail : errorCodeDetailList) {
			errorCodeDetailBean = new ErrorCodeDetail();
			errorCodeDetailBean.setTechnicalErrorCode(errorCodeDetail.getTechnicalErrorCode());
			errorCodeDetailBean.setBusinessErrorCode(errorCodeDetail.getBusinessErrorCode());
			errorCodeDetailBean.setErrorDescription(errorCodeDetail.getErrorDescription());
			returnErrorCodeLabelMappingsObj.add(errorCodeDetailBean);
		}

		return returnErrorCodeLabelMappingsObj;
	}

}
