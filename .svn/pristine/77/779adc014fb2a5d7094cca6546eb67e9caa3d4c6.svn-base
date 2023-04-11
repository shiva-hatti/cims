package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnGroupLabelMapping;
import com.iris.model.ReturnGroupLblMod;
import com.iris.repository.ReturnGroupLabelModRepo;
import com.iris.repository.ReturnGroupLabelRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class ReturnGroupLabelServiceV2 implements GenericService<ReturnGroupLabelMapping, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnGroupLabelServiceV2.class);

	@Autowired
	private ReturnGroupLabelRepo returnGroupLabelRepo;

	@Autowired
	private ReturnGroupLabelModRepo returnGroupLabelModRepo;

	@Override
	public ReturnGroupLabelMapping add(ReturnGroupLabelMapping returnGroupLabelMapping) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ReturnGroupLabelMapping returnGroupLabelMappingBean) throws ServiceException {
		boolean flag = false;
		try {
			ReturnGroupLabelMapping oldRtnGroupLabel = returnGroupLabelRepo.getDataById(returnGroupLabelMappingBean.getReturnGroupLabelMapId());
			if (oldRtnGroupLabel == null) {
				return false;
			}
			ReturnGroupLblMod returnGroupLblMod = new ReturnGroupLblMod();
			returnGroupLblMod.setRtnGroupLabel(oldRtnGroupLabel);
			returnGroupLblMod.setRtnGroupLabelName(oldRtnGroupLabel.getGroupLabel());
			returnGroupLblMod.setLangIdFk(oldRtnGroupLabel.getLangIdFk());
			if (oldRtnGroupLabel.getLastModifiedOn() == null) {
				returnGroupLblMod.setActionIdFK(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal());
				returnGroupLblMod.setUserModify(oldRtnGroupLabel.getReturnGroupMapIdFk().getCreatedBy());
				returnGroupLblMod.setModifiedOn(oldRtnGroupLabel.getReturnGroupMapIdFk().getCreatedOn());
			} else {
				returnGroupLblMod.setActionIdFK(GeneralConstants.ACTIONID_EDITION.getConstantIntVal());
				returnGroupLblMod.setUserModify(oldRtnGroupLabel.getUserModify());
				returnGroupLblMod.setModifiedOn(oldRtnGroupLabel.getLastModifiedOn());
			}
			returnGroupLabelModRepo.save(returnGroupLblMod);
			LOGGER.debug("Request served to add returnGroupLabel Mod");
			oldRtnGroupLabel.setLastModifiedOn(returnGroupLabelMappingBean.getLastModifiedOn());
			oldRtnGroupLabel.setGroupLabel(returnGroupLabelMappingBean.getGroupLabel());
			oldRtnGroupLabel.setLastUpdateOn(returnGroupLabelMappingBean.getLastUpdateOn());
			oldRtnGroupLabel.setUserModify(returnGroupLabelMappingBean.getUserModify());
			returnGroupLabelRepo.save(oldRtnGroupLabel);
			flag = true;
			LOGGER.debug("Request served to update returnGroupLabelMapping.");
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return flag;
	}

	@Override
	public List<ReturnGroupLabelMapping> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnGroupLabelMapping getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLabelMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLabelMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLabelMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLabelMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLabelMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnGroupLabelMapping bean) throws ServiceException {

	}

	public Boolean checkRetrunGroupLabelExist(String returnGroupName, String langCode, String methodName) {
		if (methodName.equalsIgnoreCase(MethodConstants.CHECK_RETURN_GROUP_NAME_EXIST.getConstantVal())) {
			List<ReturnGroupLabelMapping> returnGroupLabelMappingList = returnGroupLabelRepo.checkReturnGroupNameExist(returnGroupName.toUpperCase());
			if (!CollectionUtils.isEmpty(returnGroupLabelMappingList)) {
				return true;
			}
		} else if (methodName.equalsIgnoreCase(MethodConstants.CHECK_RETURN_GROUP_LABEL_EXIST.getConstantVal())) {
			List<ReturnGroupLabelMapping> returnGroupLabelMappingList = returnGroupLabelRepo.checkReturnGroupLabelExist(returnGroupName.toUpperCase(), langCode);
			if (!CollectionUtils.isEmpty(returnGroupLabelMappingList)) {
				return true;
			}
		}
		return false;
	}

	public List<ReturnGroupLabelMapping> getReturnGroupLabelList(String languageCode) {
		return returnGroupLabelRepo.getReturnGroupLabelList(languageCode);
	}
}
