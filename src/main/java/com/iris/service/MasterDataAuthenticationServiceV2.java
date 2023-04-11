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

import com.iris.controller.EntityMasterControllerV2;
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.exception.ApplicationException;
import com.iris.service.impl.EntityServiceV2;
import com.iris.service.impl.ReturnGroupServiceV2;

/**
 * @author sajadhav
 *
 */
@Service
public class MasterDataAuthenticationServiceV2 {

	@Autowired
	private EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	private EntityServiceV2 entityServiceV2;

	@Autowired
	private ReturnGroupServiceV2 returnGroupServiceV2;

	private static final Logger LOGGER = LogManager.getLogger(MasterDataAuthenticationServiceV2.class);

	public boolean isAuthorisedRequestWithRespectToEntityIfscCode(Long userId, Long roleId, String langCode, List<String> ifscCodeList, Map<String, String> entityIfscCodeAndLabelMap, String jobProcessId) throws ApplicationException {

		//		if (!CollectionUtils.isEmpty(ifscCodeList)) {
		EntityMasterDto entityMasterDto = new EntityMasterDto();
		entityMasterDto.setUserId(userId);
		entityMasterDto.setRoleId(roleId);
		entityMasterDto.setLanguageCode(langCode);
		entityMasterDto.setIsActive(true);

		List<EntityDto> entityDtos = entityServiceV2.getFlatEntityList(entityMasterDto);

		List<String> mappedIfscCode = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entityDtos)) {
			entityDtos.forEach(f -> {
				mappedIfscCode.add(f.getIfscCode());
				entityIfscCodeAndLabelMap.put(f.getIfscCode(), f.getEntityName());
			});
		}

		if (!CollectionUtils.isEmpty(ifscCodeList)) {
			List<String> unauthorizedIfscCodeList = ifscCodeList.stream().filter(i -> !mappedIfscCode.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedIfscCodeList)) {
				LOGGER.error("UNAUTHORISED IFSC CODE LISE : " + unauthorizedIfscCodeList.toString());
				return false;
			}
		}
		//		}

		return true;
	}

	public boolean isAuthorisedRequestWithRespectToEntityCode(Long userId, Long roleId, String langCode, List<String> entityCodeList, String jobProcessId) throws ApplicationException {

		if (!CollectionUtils.isEmpty(entityCodeList)) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setUserId(userId);
			entityMasterDto.setRoleId(roleId);
			entityMasterDto.setLanguageCode(langCode);
			entityMasterDto.setIsActive(true);

			List<EntityDto> entityDtos = entityServiceV2.getFlatEntityList(entityMasterDto);

			List<String> mappedEntityCodes = new ArrayList<>();
			if (!CollectionUtils.isEmpty(entityDtos)) {
				entityDtos.forEach(f -> {
					mappedEntityCodes.add(f.getEntityCode());
				});
			}

			List<String> unauthorizedEntityCodeList = entityCodeList.stream().filter(i -> !mappedEntityCodes.contains(i)).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(unauthorizedEntityCodeList)) {
				LOGGER.error("UNAUTHORISED ENTITY CODE LISE : " + unauthorizedEntityCodeList.toString());
				return false;
			}
		}

		return true;
	}

	public boolean isAuthorisedRequestWithRespectToEntityEntityIds(Long userId, Long roleId, String langCode, List<Long> entityids, String jobProcessId) throws ApplicationException {

		if (!CollectionUtils.isEmpty(entityids)) {
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setUserId(userId);
			entityMasterDto.setRoleId(roleId);
			entityMasterDto.setLanguageCode(langCode);
			entityMasterDto.setIsActive(true);

			List<EntityDto> entityDtos = entityServiceV2.getFlatEntityList(entityMasterDto);

			List<Long> mappedEntityIds = new ArrayList<>();
			if (!CollectionUtils.isEmpty(entityDtos)) {
				entityDtos.forEach(f -> {
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

	public boolean isAuthorisedRequestWithRespectToReturnId(Long userId, Long roleId, String langCode, List<String> returnCodes, Map<String, String> returnCodeAndLabelMap, String jobProcessId) throws ApplicationException {

		ReturnByRoleInputDto returnByRoleInputDto = new ReturnByRoleInputDto();
		returnByRoleInputDto.setUserId(userId);
		returnByRoleInputDto.setIsActive(true);
		returnByRoleInputDto.setRoleId(roleId);
		returnByRoleInputDto.setLangCode(langCode);

		List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);

		List<String> dbReturnCodes = new ArrayList<>();

		if (!CollectionUtils.isEmpty(returnByRoleOutputDtoList)) {
			returnByRoleOutputDtoList.forEach(f -> {
				dbReturnCodes.add(f.getReturnCode());
				returnCodeAndLabelMap.put(f.getReturnCode(), f.getReturnName());
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
