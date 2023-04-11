/**
 * 
 */
package com.iris.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.controller.EntityMasterController;
import com.iris.controller.FileMetaDataValidateController;
import com.iris.controller.ReturnGroupController;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.model.EntityBean;
import com.iris.util.constant.GeneralConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class MasterDataAuthenticationService {

	@Autowired
	private EntityMasterController entityMasterController;

	@Autowired
	private ReturnGroupController returnGroupController;

	private static final Logger LOGGER = LogManager.getLogger(MasterDataAuthenticationService.class);

	public boolean isAuthorisedRequestWithRespectToEntityIfscCode(Long userId, Long roleId, String langCode, List<String> ifscCodeList, Map<String, String> entityIfscCodeAndLabelMap, String jobProcessId) {

		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setUserId(userId);
		entityMasterDto.setRoleId(roleId);
		if (langCode == null) {
			entityMasterDto.setLanguageCode(GeneralConstants.ENG_LANG.getConstantVal());
		} else {
			entityMasterDto.setLanguageCode(langCode);
		}
		entityMasterDto.setIsActive(true);

		ServiceResponse serviceResponse = entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto);

		List<String> mappedIfscCode = new ArrayList<>();
		if (serviceResponse.isStatus()) {
			@SuppressWarnings("unchecked")
			List<EntityBean> entityBeans = (List<EntityBean>) serviceResponse.getResponse();
			entityBeans.forEach(f -> {
				mappedIfscCode.add(f.getIfscCode());
				entityIfscCodeAndLabelMap.put(f.getIfscCode(), f.getEntityName());
			});
		}

		if (!CollectionUtils.isEmpty(ifscCodeList)) {
			List<String> unauthorizedIfscCodeList = ifscCodeList.stream().filter(i -> !mappedIfscCode.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedIfscCodeList)) {
				LOGGER.error("UNAUTHORISED ENTITY CODE LISE : " + unauthorizedIfscCodeList.toString());

				return false;
			}
		}

		return true;
	}

	public boolean isAuthorisedRequestWithRespectToEntityCode(Long userId, Long roleId, String langCode, List<String> entityCodeList, String jobProcessId) {

		if (!CollectionUtils.isEmpty(entityCodeList)) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setUserId(userId);
			entityMasterDto.setRoleId(roleId);
			if (langCode == null) {
				entityMasterDto.setLanguageCode(GeneralConstants.ENG_LANG.getConstantVal());
			} else {
				entityMasterDto.setLanguageCode(langCode);
			}
			entityMasterDto.setIsActive(true);

			ServiceResponse serviceResponse = entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto);

			List<String> mappedEntityCodes = new ArrayList<>();
			if (serviceResponse.isStatus()) {
				@SuppressWarnings("unchecked")
				List<EntityBean> entityBeans = (List<EntityBean>) serviceResponse.getResponse();
				entityBeans.forEach(f -> {
					mappedEntityCodes.add(f.getEntityCode());
				});
			}

			List<String> unauthorizedEntityCodeList = entityCodeList.stream().filter(i -> !mappedEntityCodes.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedEntityCodeList)) {
				return false;
			}
		}

		return true;
	}

	public boolean isAuthorisedRequestWithRespectToEntityEntityIds(Long userId, Long roleId, String langCode, List<Long> entityids, String jobProcessId) {

		if (!CollectionUtils.isEmpty(entityids)) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setUserId(userId);
			entityMasterDto.setRoleId(roleId);
			if (langCode == null) {
				entityMasterDto.setLanguageCode(GeneralConstants.ENG_LANG.getConstantVal());
			} else {
				entityMasterDto.setLanguageCode(langCode);
			}
			entityMasterDto.setIsActive(true);

			ServiceResponse serviceResponse = entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto);

			List<Long> mappedEntityIds = new ArrayList<>();
			if (serviceResponse.isStatus()) {
				@SuppressWarnings("unchecked")
				List<EntityBean> entityBeans = (List<EntityBean>) serviceResponse.getResponse();
				entityBeans.forEach(f -> {
					mappedEntityIds.add(f.getEntityId());
				});
			}

			List<Long> unauthorizedEntityCodeList = entityids.stream().filter(i -> !mappedEntityIds.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedEntityCodeList)) {
				return false;
			}
		}

		return true;
	}

	public boolean isAuthorisedRequestWithRespectToReturnId(Long userId, Long roleId, Long langId, List<String> returnCodes, Map<String, String> returnCodeAndLabelMap, String jobProcessId) {

		ReturnGroupMappingRequest returnGroupMappingReq = new ReturnGroupMappingRequest();
		returnGroupMappingReq.setRoleId(roleId);
		returnGroupMappingReq.setUserId(userId);
		returnGroupMappingReq.setIsActive(true);
		if (langId == null) {
			returnGroupMappingReq.setLangId(GeneralConstants.ENG_LANG_ID.getConstantLongVal());
		} else {
			returnGroupMappingReq.setLangId(langId);
		}

		ServiceResponse returnGroupServiceResponse = returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingReq);
		List<String> dbReturnCodes = new ArrayList<>();

		if (returnGroupServiceResponse.isStatus()) {
			@SuppressWarnings("unchecked")
			List<ReturnGroupMappingDto> returnGroupMappingRtos = (List<ReturnGroupMappingDto>) returnGroupServiceResponse.getResponse();
			returnGroupMappingRtos.forEach(f -> {
				List<ReturnDto> returnList = f.getReturnList();
				returnList.forEach(r -> {
					dbReturnCodes.add(r.getReturnCode());
					returnCodeAndLabelMap.put(r.getReturnCode(), r.getReturnName());
				});
			});
		}

		if (!CollectionUtils.isEmpty(returnCodes)) {
			List<String> unauthorizedReturnIdList = returnCodes.stream().filter(i -> !dbReturnCodes.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedReturnIdList)) {
				LOGGER.error("UNAUTHORISED RETURN CODE LISE : " + unauthorizedReturnIdList.toString());
				return false;
			}
		}
		return true;
	}
}
