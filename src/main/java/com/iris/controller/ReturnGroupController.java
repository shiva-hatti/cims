package com.iris.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnGroupListForRegulatorRequest;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.Frequency;
import com.iris.model.Regulator;
import com.iris.model.RegulatorLabel;
import com.iris.model.Return;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnGroupLabelMapping;
import com.iris.model.ReturnGroupMapping;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.model.ReturnReturnTypeMapping;
import com.iris.model.RoleType;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.model.UserRoleReturnMapping;
import com.iris.repository.UserRoleReturnMappingRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityAuditorMappingService;
import com.iris.service.impl.ReturnGroupMappingService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/returnGroupController")
public class ReturnGroupController {

	private static final Logger Logger = LogManager.getLogger(ReturnGroupController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private ReturnGroupMappingService returnGroupMappingService;

	@Autowired
	private GenericService<UserRoleReturnMapping, Long> userRoleReturnMappingService;

	@Autowired
	private GenericService<ReturnEntityMappingNew, Long> returnEntityMapServiceNew;

	@Autowired
	private GenericService<Return, Long> returnService;

	@Autowired
	private UserRoleReturnMappingRepo userRoleReturnMappingRepo;

	@Autowired
	private EntityAuditorMappingService entityAuditorMappingService;

	/**
	 * Gets the ReturnGroup. This method is to get all ReturnGroups .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getReturnGroups")
	public ServiceResponse getReturnGroups(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupMappingRequest returnGroupMappingRequest) {
		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();
		try {
			UserMaster userMaster = userMasterService.getDataById(returnGroupMappingRequest.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}
			RoleType roleType = userMaster.getRoleType();
			Boolean isRegulator = false;
			Boolean isEntity = false;
			Boolean isAuditor = false;
			if (GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isRegulator = true;
			} else if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isEntity = true;
			} else if (GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isAuditor = true;
			}
			List<ReturnGroupMapping> returnGroupMappingList = new ArrayList<>();

			if (Boolean.TRUE.equals(isRegulator)) {

				Map<String, Object> columneMap = new HashMap<>();
				columneMap.put(ColumnConstants.ROLEID.getConstantVal(), returnGroupMappingRequest.getRoleId());
				columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnGroupMappingRequest.getIsActive());

				List<UserRoleReturnMapping> userRoleReturnMappingList = userRoleReturnMappingService.getDataByObject(columneMap, "getUserRoleReturnMappingByUserId");
				ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest = new ReturnGroupListForRegulatorRequest();
				returnGroupListForRegulatorRequest.setJobProcessId(jobProcessId);
				returnGroupListForRegulatorRequest.setReturnGroupMappingList(returnGroupMappingList);
				returnGroupListForRegulatorRequest.setReturnGroupMappingRequest(returnGroupMappingRequest);
				returnGroupListForRegulatorRequest.setUserRoleReturnMappingList(userRoleReturnMappingList);
				//				prepareReturnGroupMappingListRegulatorUser(returnGroupListForRegulatorRequest);
				List<ReturnGroupMappingDto> returnGroupMappDtoList = returnGroupMappingService.prepareReturnGroupMappingListRegulatorUserModified(returnGroupMappingRequest);
				sortData(returnGroupMappDtoList);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(returnGroupMappDtoList).build();
			} else if (Boolean.TRUE.equals(isEntity)) {
				Set<UserRoleMaster> userRoleMasterList = userMaster.getUsrRoleMstrSet();

				UserRoleMaster userRoleMaster = userRoleMasterList.stream().filter(f -> f.getUserRole().getUserRoleId().equals(returnGroupMappingRequest.getRoleId())).findAny().orElse(null);
				Long userRoleMasterId = userRoleMaster.getUserRoleMasterId();

				Set<UserEntityRole> userEntityRoleSet = userRoleMaster.getUserEntityRole();
				UserEntityRole userEntityRole = userEntityRoleSet.stream().filter(f -> f.getUserRoleMaster().getUserRoleMasterId() == userRoleMasterId).findAny().orElse(null);

				Map<String, Object> columneValMap = new HashMap<>();
				columneValMap.put(ColumnConstants.ENTITYID.getConstantVal(), userEntityRole.getEntityBean().getEntityId());
				List<ReturnEntityMappingNew> returnEntityMappingList = returnEntityMapServiceNew.getDataByObject(columneValMap, MethodConstants.GET_ACTIVE_MAPPING_BY_ENTITY_ID.getConstantVal());

				int mappedReturnCount = userRoleReturnMappingRepo.getMappedReturnCountForUserRole(returnGroupMappingRequest.getRoleId());

				if (mappedReturnCount > 0) {
					Map<String, Object> columneMap = new HashMap<>();
					columneMap.put(ColumnConstants.ROLEID.getConstantVal(), returnGroupMappingRequest.getRoleId());
					List<UserRoleReturnMapping> userRoleReturnMappingList = userRoleReturnMappingService.getDataByObject(columneMap, MethodConstants.GET_USER_ROLE_RETURN_MAPP_DATA_BY_ROLE_ID.getConstantVal());

					prepareReturnGroupMappingListForEntityUser(returnEntityMappingList, userRoleReturnMappingList, returnGroupMappingList, returnGroupMappingRequest, jobProcessId);
				} else {
					prepareReturnGroupMappingListForEntityUser(returnEntityMappingList, null, returnGroupMappingList, returnGroupMappingRequest, jobProcessId);

				}
			} else if (Boolean.TRUE.equals(isAuditor)) {

				Map<String, Object> columneMap = new HashMap<>();
				columneMap.put(ColumnConstants.USER_ID.getConstantVal(), returnGroupMappingRequest.getUserId());
				columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnGroupMappingRequest.getIsActive());
				columneMap.put(ColumnConstants.LANG_ID.getConstantVal(), returnGroupMappingRequest.getLangId());

				List<Return> commonReturnList = entityAuditorMappingService.getReturnMappingForAuditor(columneMap);
				Set<ReturnGroupMapping> returnGroupMappings = commonReturnList.stream().map(f -> f.getReturnGroupMapIdFk()).collect(Collectors.toSet());
				ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest = new ReturnGroupListForRegulatorRequest();
				returnGroupListForRegulatorRequest.setLangId(returnGroupMappingRequest.getLangId());
				returnGroupListForRegulatorRequest.setReturnlist(commonReturnList);
				returnGroupListForRegulatorRequest.setReturnGrouplist(returnGroupMappings);
				returnGroupListForRegulatorRequest.setReturnGroupIds(returnGroupMappingRequest.getReturnGroupIds());
				returnGroupListForRegulatorRequest.setJobProcessId(jobProcessId);

				addReturnListToReturnGroup(returnGroupListForRegulatorRequest);

				returnGroupMappingList.addAll(returnGroupMappings);
			}
			ReturnGroupMappingDto returnGroupMappingDto;
			UserMaster user;
			List<ReturnDto> returnList;
			ReturnDto returnDto;
			Frequency frequency;
			for (ReturnGroupMapping returnGroupMapping : returnGroupMappingList) {
				returnGroupMappingDto = new ReturnGroupMappingDto();
				returnGroupMappingDto.setDefaultGroupName(returnGroupMapping.getDefaultGroupName());
				returnGroupMappingDto.setIsActive(returnGroupMapping.getIsActive());
				returnGroupMappingDto.setReturnGroupMapId(returnGroupMapping.getReturnGroupMapId());
				returnGroupMappingDto.setOrderNo(returnGroupMapping.getOrderNo());

				user = new UserMaster();
				user.setUserId(returnGroupMapping.getCreatedBy().getUserId());
				user.setUserName(returnGroupMapping.getCreatedBy().getUserName());
				user.setFirstName(returnGroupMapping.getCreatedBy().getFirstName());
				user.setLastName(returnGroupMapping.getCreatedBy().getLastName());
				returnGroupMappingDto.setCreatedBy(user);

				returnList = new ArrayList<>();
				for (Return returnDt : returnGroupMapping.getReturnList()) {
					if (returnDt.getReturnRegulatorMapping() == null) {
						continue;
					}
					returnDto = new ReturnDto();
					returnDto.setReturnId(returnDt.getReturnId());
					returnDto.setReturnName(returnDt.getReturnName());
					returnDto.setAllowRevision(returnDt.getAllowRevision());

					frequency = new Frequency();
					frequency.setFrequencyId(returnDt.getFrequency().getFrequencyId());
					frequency.setFrequencyName(returnDt.getFrequency().getFrequencyName());
					frequency.setDescription(returnDt.getFrequency().getDescription());
					frequency.setIsActive(returnDt.getFrequency().getIsActive());

					returnDto.setFrequency(frequency);
					returnDto.setIsActive(returnDt.getIsActive());
					returnDto.setIsParent(returnDt.getIsParent());
					returnDto.setReturnCode(returnDt.getReturnCode());

					List<Long> returnTypeIds = new ArrayList<>();

					for (ReturnReturnTypeMapping returnReturnTypeMap : returnDt.getReturnReturnTypeMapping()) {
						returnTypeIds.add(returnReturnTypeMap.getReturnTypeIdFk().getReturnTypeId());
					}

					returnDto.setReturnTypeIds(returnTypeIds);

					if (!CollectionUtils.isEmpty(returnDt.getReturnRegulatorMapping())) {
						ReturnRegulatorMapping returnRegulatorMapping = returnDt.getReturnRegulatorMapping().stream().filter(f -> f.getIsActive()).findAny().orElse(null);
						if (returnRegulatorMapping != null && returnRegulatorMapping.getReturnIdFk().getIsActive() && returnRegulatorMapping.getRegulatorIdFk().getIsActive()) {
							Regulator regulator = new Regulator();
							regulator.setRegulatorId(returnRegulatorMapping.getRegulatorIdFk().getRegulatorId());
							RegulatorLabel regulatorlabel = returnRegulatorMapping.getRegulatorIdFk().getRegulatorLblSet().stream().filter(f -> f.getLangIdFk().getLanguageId().equals(returnGroupMappingRequest.getLangId())).findAny().orElse(null);
							if (regulatorlabel != null) {
								regulator.setRegulatorName(regulatorlabel.getRegulatorLabel());
							} else {
								regulator.setRegulatorName(returnRegulatorMapping.getRegulatorIdFk().getRegulatorName());
							}
							returnDto.setRegulator(regulator);
						}
					}
					returnList.add(returnDto);
				}
				returnGroupMappingDto.setReturnList(returnList);
				returnGroupMappingDto.setReturnCount(returnList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching return group info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching return group info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		sortData(returnGroupMappingDtoList);
		response.setResponse(returnGroupMappingDtoList);
		Logger.info("request completed to get return group list for job processingid" + jobProcessId);
		return response;
	}

	/**
	 * Gets the ReturnGroup. This method is to get all ReturnGroups .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getReturnGroupsUserRole")
	public ServiceResponse getReturnGroupsUserRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupMappingRequest returnGroupMappingRequest) {
		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();
		try {
			UserMaster userMaster = userMasterService.getDataById(returnGroupMappingRequest.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}
			RoleType roleType = userMaster.getRoleType();
			Boolean isRegulator = false;
			Boolean isEntity = false;
			Boolean isAuditor = false;
			if (GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isRegulator = true;
			} else if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isEntity = true;
			} else if (GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				isAuditor = true;
			}
			List<ReturnGroupMapping> returnGroupMappingList = new ArrayList<>();

			if (Boolean.TRUE.equals(isRegulator)) {

				/*
				 * List<UserRoleReturnMapping> userRoleReturnMappingList =
				 * userRoleReturnMappingService.getDataByIds(roleArray);
				 * ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest = new
				 * ReturnGroupListForRegulatorRequest();
				 * returnGroupListForRegulatorRequest.setJobProcessId(jobProcessId);
				 * returnGroupListForRegulatorRequest.setReturnGroupMappingList(
				 * returnGroupMappingList);
				 * returnGroupListForRegulatorRequest.setReturnGroupMappingRequest(
				 * returnGroupMappingRequest);
				 * returnGroupListForRegulatorRequest.setUserRoleReturnMappingList(
				 * userRoleReturnMappingList);
				 */
				//				prepareReturnGroupMappingListRegulatorUser(returnGroupListForRegulatorRequest);
				List<ReturnGroupMappingDto> returnGroupMappDtoList = returnGroupMappingService.prepareReturnGroupMappingListUserModified(returnGroupMappingRequest);
				sortData(returnGroupMappDtoList);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(returnGroupMappDtoList).build();
			} else if (Boolean.TRUE.equals(isEntity)) {
				Set<UserRoleMaster> userRoleMasterList = userMaster.getUsrRoleMstrSet();

				UserRoleMaster userRoleMaster = userRoleMasterList.stream().filter(f -> f.getUserRole().getUserRoleId().equals(returnGroupMappingRequest.getRoleId())).findAny().orElse(null);
				Long userRoleMasterId = userRoleMaster.getUserRoleMasterId();

				Set<UserEntityRole> userEntityRoleSet = userRoleMaster.getUserEntityRole();
				UserEntityRole userEntityRole = userEntityRoleSet.stream().filter(f -> f.getUserRoleMaster().getUserRoleMasterId() == userRoleMasterId).findAny().orElse(null);

				/*
				 * Map<String, Object> columneValMap = new HashMap<>();
				 * columneValMap.put(ColumnConstants.ENTITYID.getConstantVal(),
				 * userEntityRole.getEntityBean().getEntityId()); List<ReturnEntityMappingNew>
				 * returnEntityMappingList =
				 * returnEntityMapServiceNew.getDataByObject(columneValMap,
				 * MethodConstants.GET_ACTIVE_MAPPING_BY_ENTITY_ID.getConstantVal());
				 */
				boolean returnNotMappedToRole = false;
				int mappedReturnCount;
				for (UserRoleMaster userRoleMasterObj : userRoleMasterList) {
					if (userRoleMasterObj != null && userRoleMasterObj.getUserRole() != null && userRoleMasterObj.getIsActive()) {
						mappedReturnCount = userRoleReturnMappingRepo.getMappedReturnCountForUserRole(userRoleMasterObj.getUserRole().getUserRoleId());
						if (mappedReturnCount == 0) {
							returnNotMappedToRole = true;
							break;
						}
					}
				}
				if (userEntityRole != null && userEntityRole.getEntityBean() != null) {
					returnGroupMappingRequest.setEntityId(userEntityRole.getEntityBean().getEntityId());
				}
				List<ReturnGroupMappingDto> returnGroupMappDtoList = null;
				if (returnNotMappedToRole) {
					returnGroupMappDtoList = returnGroupMappingService.prepareReturnGroupMappingListEntUserModified(returnGroupMappingRequest);

				} else {
					returnGroupMappDtoList = returnGroupMappingService.prepareReturnGroupMappingListUserModified(returnGroupMappingRequest);

				}
				sortData(returnGroupMappDtoList);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(returnGroupMappDtoList).build();
				/*
				 * if (mappedReturnCount > 0) { Map<String, Object> columneMap = new
				 * HashMap<>(); columneMap.put(ColumnConstants.ROLEID.getConstantVal(),
				 * returnGroupMappingRequest.getRoleId()); List<UserRoleReturnMapping>
				 * userRoleReturnMappingList =
				 * userRoleReturnMappingService.getDataByObject(columneMap,
				 * MethodConstants.GET_USER_ROLE_RETURN_MAPP_DATA_BY_ROLE_ID.getConstantVal());
				 * 
				 * prepareReturnGroupMappingListForEntityUser(returnEntityMappingList,
				 * userRoleReturnMappingList, returnGroupMappingList, returnGroupMappingRequest,
				 * jobProcessId); } else {ull
				 * prepareReturnGroupMappingListForEntityUser(returnEntityMappingList, n,
				 * returnGroupMappingList, returnGroupMappingRequest, jobProcessId);
				 * 
				 * }
				 */
			} else if (Boolean.TRUE.equals(isAuditor)) {

				Map<String, Object> columneMap = new HashMap<>();
				columneMap.put(ColumnConstants.USER_ID.getConstantVal(), returnGroupMappingRequest.getUserId());
				columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnGroupMappingRequest.getIsActive());
				columneMap.put(ColumnConstants.LANG_ID.getConstantVal(), returnGroupMappingRequest.getLangId());

				List<Return> commonReturnList = entityAuditorMappingService.getReturnMappingForAuditor(columneMap);
				Set<ReturnGroupMapping> returnGroupMappings = commonReturnList.stream().map(f -> f.getReturnGroupMapIdFk()).collect(Collectors.toSet());
				ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest = new ReturnGroupListForRegulatorRequest();
				returnGroupListForRegulatorRequest.setLangId(returnGroupMappingRequest.getLangId());
				returnGroupListForRegulatorRequest.setReturnlist(commonReturnList);
				returnGroupListForRegulatorRequest.setReturnGrouplist(returnGroupMappings);
				returnGroupListForRegulatorRequest.setReturnGroupIds(returnGroupMappingRequest.getReturnGroupIds());
				returnGroupListForRegulatorRequest.setJobProcessId(jobProcessId);

				addReturnListToReturnGroup(returnGroupListForRegulatorRequest);

				returnGroupMappingList.addAll(returnGroupMappings);
			}
			ReturnGroupMappingDto returnGroupMappingDto;
			UserMaster user;
			List<ReturnDto> returnList;
			ReturnDto returnDto;
			Frequency frequency;
			for (ReturnGroupMapping returnGroupMapping : returnGroupMappingList) {
				returnGroupMappingDto = new ReturnGroupMappingDto();
				returnGroupMappingDto.setDefaultGroupName(returnGroupMapping.getDefaultGroupName());
				returnGroupMappingDto.setIsActive(returnGroupMapping.getIsActive());
				returnGroupMappingDto.setReturnGroupMapId(returnGroupMapping.getReturnGroupMapId());
				returnGroupMappingDto.setOrderNo(returnGroupMapping.getOrderNo());

				user = new UserMaster();
				user.setUserId(returnGroupMapping.getCreatedBy().getUserId());
				user.setUserName(returnGroupMapping.getCreatedBy().getUserName());
				user.setFirstName(returnGroupMapping.getCreatedBy().getFirstName());
				user.setLastName(returnGroupMapping.getCreatedBy().getLastName());
				returnGroupMappingDto.setCreatedBy(user);

				returnList = new ArrayList<>();
				for (Return returnDt : returnGroupMapping.getReturnList()) {
					if (returnDt.getReturnRegulatorMapping() == null) {
						continue;
					}
					returnDto = new ReturnDto();
					returnDto.setReturnId(returnDt.getReturnId());
					returnDto.setReturnName(returnDt.getReturnName());
					returnDto.setAllowRevision(returnDt.getAllowRevision());

					frequency = new Frequency();
					frequency.setFrequencyId(returnDt.getFrequency().getFrequencyId());
					frequency.setFrequencyName(returnDt.getFrequency().getFrequencyName());
					frequency.setDescription(returnDt.getFrequency().getDescription());
					frequency.setIsActive(returnDt.getFrequency().getIsActive());

					returnDto.setFrequency(frequency);
					returnDto.setIsActive(returnDt.getIsActive());
					returnDto.setIsParent(returnDt.getIsParent());
					returnDto.setReturnCode(returnDt.getReturnCode());

					List<Long> returnTypeIds = new ArrayList<>();

					for (ReturnReturnTypeMapping returnReturnTypeMap : returnDt.getReturnReturnTypeMapping()) {
						returnTypeIds.add(returnReturnTypeMap.getReturnTypeIdFk().getReturnTypeId());
					}

					returnDto.setReturnTypeIds(returnTypeIds);

					if (!CollectionUtils.isEmpty(returnDt.getReturnRegulatorMapping())) {
						ReturnRegulatorMapping returnRegulatorMapping = returnDt.getReturnRegulatorMapping().stream().filter(f -> f.getIsActive()).findAny().orElse(null);
						if (returnRegulatorMapping != null && returnRegulatorMapping.getReturnIdFk().getIsActive() && returnRegulatorMapping.getRegulatorIdFk().getIsActive()) {
							Regulator regulator = new Regulator();
							regulator.setRegulatorId(returnRegulatorMapping.getRegulatorIdFk().getRegulatorId());
							RegulatorLabel regulatorlabel = returnRegulatorMapping.getRegulatorIdFk().getRegulatorLblSet().stream().filter(f -> f.getLangIdFk().getLanguageId().equals(returnGroupMappingRequest.getLangId())).findAny().orElse(null);
							if (regulatorlabel != null) {
								regulator.setRegulatorName(regulatorlabel.getRegulatorLabel());
							} else {
								regulator.setRegulatorName(returnRegulatorMapping.getRegulatorIdFk().getRegulatorName());
							}
							returnDto.setRegulator(regulator);
						}
					}
					returnList.add(returnDto);
				}
				returnGroupMappingDto.setReturnList(returnList);
				returnGroupMappingDto.setReturnCount(returnList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
		} catch (ServiceException e) {
			Logger.error("Exception while fetching return group info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching return group info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		sortData(returnGroupMappingDtoList);
		response.setResponse(returnGroupMappingDtoList);
		Logger.info("request completed to get return group list for job processingid" + jobProcessId);
		return response;
	}

	private void sortData(List<ReturnGroupMappingDto> returnGroupMappingDtoList) {
		if (!CollectionUtils.isEmpty(returnGroupMappingDtoList)) {
			// Sorting by default group name
			Collections.sort(returnGroupMappingDtoList, new Comparator<ReturnGroupMappingDto>() {
				@Override
				public int compare(ReturnGroupMappingDto o1, ReturnGroupMappingDto o2) {
					return o1.getDefaultGroupName().compareTo(o2.getDefaultGroupName());
				}
			});

			// Sorting by return name
			for (ReturnGroupMappingDto returnGroupMappingDto : returnGroupMappingDtoList) {
				if (!CollectionUtils.isEmpty(returnGroupMappingDto.getReturnList())) {
					Collections.sort(returnGroupMappingDto.getReturnList(), new Comparator<ReturnDto>() {
						@Override
						public int compare(ReturnDto o1, ReturnDto o2) {
							return o1.getReturnName().compareTo(o2.getReturnName());
						}
					});
				}
			}
		}
	}

	private void sortReturnGroupData(List<ReturnGroupMapping> returnGroupMappingList) {
		if (!CollectionUtils.isEmpty(returnGroupMappingList)) {
			// Sorting by default group name
			Collections.sort(returnGroupMappingList, new Comparator<ReturnGroupMapping>() {
				@Override
				public int compare(ReturnGroupMapping o1, ReturnGroupMapping o2) {
					return o1.getDefaultGroupName().compareTo(o2.getDefaultGroupName());
				}
			});

			// Sorting by return name
			for (ReturnGroupMapping returnGroupMappingDto : returnGroupMappingList) {
				if (!CollectionUtils.isEmpty(returnGroupMappingDto.getReturnList())) {
					Collections.sort(returnGroupMappingDto.getReturnList(), new Comparator<Return>() {
						@Override
						public int compare(Return o1, Return o2) {
							return o1.getReturnName().compareTo(o2.getReturnName());
						}
					});
				}
			}
		}
	}

	/**
	 * returnGroupMappingDtoList prepares ReturnGroupMapping List including Return Set from the UserRoleReturnMappingList for Entity user
	 * 
	 * @param LangId
	 * @param returnEntityMappingList
	 * @param userRoleReturnMappingList
	 */
	private void prepareReturnGroupMappingListForEntityUser(List<ReturnEntityMappingNew> returnEntityMappingList, List<UserRoleReturnMapping> userRoleReturnMappingList, List<ReturnGroupMapping> returnGroupMappingList, ReturnGroupMappingRequest returnGroupMappingRequest, String jobProcessId) {
		Long langId = returnGroupMappingRequest.getLangId();
		try {
			List<Return> returnListMappedToRole = null;
			List<Return> returnListMappedToEntity = null;
			List<Return> commonReturnList = null;

			if (!CollectionUtils.isEmpty(userRoleReturnMappingList)) {
				if (returnGroupMappingRequest.getFrequencyId() != null) {
					returnListMappedToRole = userRoleReturnMappingList.stream().filter(f -> f.getIsActive() && f.getReturnIdFk().getIsActive() && f.getReturnIdFk().getReturnGroupMapIdFk() != null && f.getReturnIdFk().getFrequency().getFrequencyId().equals(returnGroupMappingRequest.getFrequencyId())).map(m -> m.getReturnIdFk()).collect(Collectors.toList());
					returnListMappedToEntity = returnEntityMappingList.stream().filter(f -> f.isActive() && f.getReturnObj().getIsActive() && f.getReturnObj().getReturnGroupMapIdFk() != null && f.getReturnObj().getFrequency().getFrequencyId().equals(returnGroupMappingRequest.getFrequencyId())).map(m -> m.getReturnObj()).collect(Collectors.toList());
				} else {
					returnListMappedToRole = userRoleReturnMappingList.stream().filter(f -> f.getIsActive() && f.getReturnIdFk().getIsActive() && f.getReturnIdFk().getReturnGroupMapIdFk() != null).map(m -> m.getReturnIdFk()).collect(Collectors.toList());
					returnListMappedToEntity = returnEntityMappingList.stream().filter(f -> f.isActive() && f.getReturnObj().getIsActive() && f.getReturnObj().getReturnGroupMapIdFk() != null).map(m -> m.getReturnObj()).collect(Collectors.toList());
				}
				commonReturnList = returnListMappedToRole.stream().filter(returnListMappedToEntity::contains).collect(Collectors.toList());
			} else {
				// Load return for entity user who does not find entry in role return mapping table
				if (returnGroupMappingRequest.getFrequencyId() != null) {
					commonReturnList = returnEntityMappingList.stream().filter(f -> f.isActive() && f.getReturnObj().getIsActive() && f.getReturnObj().getReturnGroupMapIdFk() != null && f.getReturnObj().getFrequency().getFrequencyId().equals(returnGroupMappingRequest.getFrequencyId())).map(m -> m.getReturnObj()).collect(Collectors.toList());
				} else {
					commonReturnList = returnEntityMappingList.stream().filter(f -> f.isActive() && f.getReturnObj().getIsActive() && f.getReturnObj().getReturnGroupMapIdFk() != null).map(m -> m.getReturnObj()).collect(Collectors.toList());
				}
			}

			Set<ReturnGroupMapping> returnGroupMappings = commonReturnList.stream().map(f -> f.getReturnGroupMapIdFk()).collect(Collectors.toSet());
			ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest = new ReturnGroupListForRegulatorRequest();
			returnGroupListForRegulatorRequest.setLangId(langId);
			returnGroupListForRegulatorRequest.setReturnlist(commonReturnList);
			returnGroupListForRegulatorRequest.setReturnGrouplist(returnGroupMappings);
			returnGroupListForRegulatorRequest.setReturnGroupIds(returnGroupMappingRequest.getReturnGroupIds());
			returnGroupListForRegulatorRequest.setJobProcessId(jobProcessId);
			addReturnListToReturnGroup(returnGroupListForRegulatorRequest);

			returnGroupMappingList.addAll(returnGroupMappings);
		} catch (Exception e) {
			Logger.error("Exception while fetching return group info in method prepareReturnGroupMappingList() for job processingid " + jobProcessId, e);
			throw e;
		}
	}

	/**
	 * Method prepareReturnGroupMappingList prepares ReturnGroupMapping List including Return Set from the UserRoleReturnMappingList for Regulator.
	 * 
	 * @param returnGroupMappingList
	 * @param userRoleReturnMappingList
	 */
	private void prepareReturnGroupMappingListRegulatorUser(ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest) {
		List<Return> returnlist = new ArrayList<>();
		Set<ReturnGroupMapping> returnGrouplist = new HashSet<>();
		Set<Long> returnIdlist = new HashSet<>();
		Set<Long> returnGroupIdlist = new HashSet<>();
		List<Long> returnGroupIds = returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getReturnGroupIds();
		Long langId = returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getLangId();
		try {
			for (UserRoleReturnMapping userRoleReturnMapping : returnGroupListForRegulatorRequest.getUserRoleReturnMappingList()) {
				if ((userRoleReturnMapping.getReturnIdFk() == null || !userRoleReturnMapping.getReturnIdFk().getIsActive() || userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk() == null) || (returnGroupIds != null && !returnGroupIds.isEmpty() && !returnGroupIds.contains(userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk().getReturnGroupMapId()))) {
					continue;
				}
				/**
				 * if(returnGroupIds != null && !returnGroupIds.isEmpty() && !returnGroupIds.contains(userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk() .getReturnGroupMapId())) { continue; }
				 */

				if (returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getFrequencyId() != null && returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getFrequencyId().compareTo(userRoleReturnMapping.getReturnIdFk().getFrequency().getFrequencyId()) == 0) {
					prepareReturnGroupListWithFreqIdFilter(returnlist, returnGrouplist, returnIdlist, returnGroupIdlist, userRoleReturnMapping);
				} else {
					prepareReturnGroupListWithoutFreqIdFilter(returnGroupListForRegulatorRequest, returnlist, returnGrouplist, returnIdlist, returnGroupIdlist, userRoleReturnMapping);
				}

			}
			returnGroupListForRegulatorRequest.setLangId(langId);
			returnGroupListForRegulatorRequest.setReturnlist(returnlist);
			returnGroupListForRegulatorRequest.setReturnGrouplist(returnGrouplist);
			returnGroupListForRegulatorRequest.setReturnGroupIds(returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getReturnGroupIds());
			returnGroupListForRegulatorRequest.setJobProcessId(returnGroupListForRegulatorRequest.getJobProcessId());
			addReturnListToReturnGroup(returnGroupListForRegulatorRequest);
		} catch (Exception e) {
			Logger.error("Exception while fetching return group info in method prepareReturnGroupMappingList() for job processingid " + returnGroupListForRegulatorRequest.getJobProcessId(), e);
			throw e;
		}
		returnGroupListForRegulatorRequest.getReturnGroupMappingList().addAll(returnGrouplist);
	}

	/**
	 * @param returnlist
	 * @param returnGrouplist
	 * @param returnIdlist
	 * @param returnGroupIdlist
	 * @param userRoleReturnMapping
	 */
	private void prepareReturnGroupListWithFreqIdFilter(List<Return> returnlist, Set<ReturnGroupMapping> returnGrouplist, Set<Long> returnIdlist, Set<Long> returnGroupIdlist, UserRoleReturnMapping userRoleReturnMapping) {
		if (!returnIdlist.contains(userRoleReturnMapping.getReturnIdFk().getReturnId())) {
			returnlist.add(userRoleReturnMapping.getReturnIdFk());
			returnIdlist.add(userRoleReturnMapping.getReturnIdFk().getReturnId());
		}

		if (!returnGroupIdlist.contains(userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk().getReturnGroupMapId())) {
			returnGroupIdlist.add(userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk().getReturnGroupMapId());
			returnGrouplist.add(userRoleReturnMapping.getReturnIdFk().getReturnGroupMapIdFk());
		}
	}

	/**
	 * @param returnGroupListForRegulatorRequest
	 * @param returnlist
	 * @param returnGrouplist
	 * @param returnIdlist
	 * @param returnGroupIdlist
	 * @param userRoleReturnMapping
	 */
	private void prepareReturnGroupListWithoutFreqIdFilter(ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest, List<Return> returnlist, Set<ReturnGroupMapping> returnGrouplist, Set<Long> returnIdlist, Set<Long> returnGroupIdlist, UserRoleReturnMapping userRoleReturnMapping) {
		if (returnGroupListForRegulatorRequest.getReturnGroupMappingRequest().getFrequencyId() == null) {
			prepareReturnGroupListWithFreqIdFilter(returnlist, returnGrouplist, returnIdlist, returnGroupIdlist, userRoleReturnMapping);
		}
	}

	/**
	 * @param langId
	 * @param returnlist
	 * @param returnGrouplist
	 * @param returnGroupIds
	 */
	private void addReturnListToReturnGroup(ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest) {
		try {
			for (ReturnGroupMapping returnGroupMapping : returnGroupListForRegulatorRequest.getReturnGrouplist()) {
				if (returnGroupListForRegulatorRequest.getReturnGroupIds() != null && !returnGroupListForRegulatorRequest.getReturnGroupIds().contains(returnGroupMapping.getReturnGroupMapId())) {
					//if(!returnGroupListForRegulatorRequest.getReturnGroupIds().contains(returnGroupMapping.getReturnGroupMapId())) {
					continue;
					//}
				}
				List<Return> returnSet = new ArrayList<>();
				for (Return returnDt : returnGroupListForRegulatorRequest.getReturnlist()) {
					if (returnDt.getReturnGroupMapIdFk().getReturnGroupMapId().equals(returnGroupMapping.getReturnGroupMapId())) {
						setReturnLabel(returnGroupListForRegulatorRequest, returnDt);
						returnSet.add(returnDt);
					}
				}
				returnGroupMapping.setReturnList(returnSet);
				ReturnGroupLabelMapping returnGroupLabelMapping = returnGroupMapping.getRtnGroupLblSet().stream().filter(f -> f.getLangIdFk().getLanguageId().equals(returnGroupListForRegulatorRequest.getLangId())).findAny().orElse(null);

				if (returnGroupLabelMapping != null) {
					returnGroupMapping.setDefaultGroupName(returnGroupLabelMapping.getGroupLabel());
				}
			}
		} catch (Exception e) {
			Logger.error("Exception while fetching return group info in method addReturnListToReturnGroup() for job processingid " + returnGroupListForRegulatorRequest.getJobProcessId(), e);
			throw e;
		}
	}

	/**
	 * @param returnGroupListForRegulatorRequest
	 * @param returnDt
	 */
	private void setReturnLabel(ReturnGroupListForRegulatorRequest returnGroupListForRegulatorRequest, Return returnDt) {
		ReturnLabel returnLabel = returnDt.getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageId().equals(returnGroupListForRegulatorRequest.getLangId())).findAny().orElse(null);
		if (returnLabel != null) {
			returnDt.setReturnName(returnLabel.getReturnLabel());
		}
	}

	@PostMapping(value = "/getReturnGroupsByEntityCode")
	public ServiceResponse getReturnGroupsByEntityCode(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody ReturnEntityMapDto returnEntityMapDto) {

		Logger.info("request received to get entity return channel map list for job processigid : " + jobProcessingId);

		try {
			validateReturnEntityMapDto(returnEntityMapDto);

			UserMaster userMaster = userMasterService.getDataById(returnEntityMapDto.getUserId());

			if (userMaster == null) {
				Logger.error("User not found for job processigid" + jobProcessingId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
				// Regulator User
				Logger.info("Request for regulator User for job procesing Id : " + jobProcessingId);

				Map<String, Object> valueMap = new HashMap<>();
				valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnEntityMapDto.getRoleId());
				valueMap.put(ColumnConstants.USER_ID.getConstantVal(), returnEntityMapDto.getUserId());
				valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnEntityMapDto.getLangCode());
				valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnEntityMapDto.getIsActive());
				valueMap.put(ColumnConstants.ENT_CODE.getConstantVal(), returnEntityMapDto.getEntityCode());

				List<Return> returnList = returnService.getDataByObject(valueMap, MethodConstants.GET_REGULATOR_RETURN_LIST_BASED_UPON_ENTITY.getConstantVal());

				List<ReturnGroupMapping> returnGroupList = getApplicableReturnGroupList(returnList);
				return new ServiceResponseBuilder().setStatus(true).setResponse(returnGroupList).build();
			} else {
				// Entity User
				Logger.info("Request for entity User for job procesing Id : " + jobProcessingId);
				EntityBean userMappedEntity = null;
				UserRoleMaster userRoleMaster = userMaster.getUsrRoleMstrSet().stream().filter(userRole -> userRole.getUserRole().getUserRoleId().equals(returnEntityMapDto.getRoleId())).findAny().orElse(null);
				if (userRoleMaster != null) {
					if (!CollectionUtils.isEmpty(userRoleMaster.getUserEntityRole())) {
						// Entity user always mapped to only one entity..hence getting first record from set
						userMappedEntity = userRoleMaster.getUserEntityRole().iterator().next().getEntityBean();

						Map<String, Object> valueMap = new HashMap<>();
						valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnEntityMapDto.getRoleId());
						valueMap.put(ColumnConstants.USER_ID.getConstantVal(), returnEntityMapDto.getUserId());
						valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnEntityMapDto.getLangCode());
						valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnEntityMapDto.getIsActive());
						valueMap.put(ColumnConstants.ENT_CODE.getConstantVal(), userMappedEntity.getEntityCode());

						List<Return> returnList = returnService.getDataByObject(valueMap, MethodConstants.GET_ENTITY_RETURN_LIST_BASED_UPON_ENTITY.getConstantVal());

						if (CollectionUtils.isEmpty(returnList)) {
							return new ServiceResponseBuilder().setStatus(true).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0810.toString())).setStatusCode(ErrorCode.E0810.toString()).build();
						} else {
							int mappedReturnCount = userRoleReturnMappingRepo.getMappedReturnCountForUserRole(returnEntityMapDto.getRoleId());

							if (mappedReturnCount > 0) {
								valueMap = new HashMap<>();
								valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnEntityMapDto.getRoleId());
								valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnEntityMapDto.getIsActive());

								List<UserRoleReturnMapping> userRoleReturnMappings = userRoleReturnMappingService.getDataByObject(valueMap, MethodConstants.GET_USER_ROLE_RETURN_DATA_BY_ROLE_ID_ACTIVE_FLAG.getConstantVal());
								List<Return> intersectReturnList = getIntersectReturnList(returnList, userRoleReturnMappings);
								return new ServiceResponseBuilder().setStatus(true).setResponse(getApplicableReturnGroupList(intersectReturnList)).build();
							} else {
								return new ServiceResponseBuilder().setStatus(true).setResponse(getApplicableReturnGroupList(returnList)).build();
							}
						}
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0807.toString())).setStatusCode(ErrorCode.E0807.toString()).build();
					}
				} else {
					return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0807.toString())).setStatusCode(ErrorCode.E0807.toString()).build();
				}
			}
		} catch (ApplicationException applicationException) {
			Logger.error("Exception occured for job processigid : " + jobProcessingId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			Logger.error("Exception occured for job processigid : " + jobProcessingId);
			Logger.error(ErrorCode.EC0033.toString(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<Return> getIntersectReturnList(List<Return> returnList, List<UserRoleReturnMapping> userRoleReturnMapping) {
		if (!CollectionUtils.isEmpty(userRoleReturnMapping)) {
			List<Return> finalReturnList = new ArrayList<>();
			List<Return> compareReturnList = userRoleReturnMapping.stream().map(userRole -> userRole.getReturnIdFk()).collect(Collectors.toList());
			for (Return returnObj : returnList) {
				Return compareReturn = compareReturnList.stream().filter(f -> f.getReturnId().equals(returnObj.getReturnId())).findAny().orElse(null);
				if (compareReturn != null) {
					finalReturnList.add(returnObj);
				}
			}
			return finalReturnList;
		} else {
			return returnList;
		}
	}

	private List<ReturnGroupMapping> getApplicableReturnGroupList(List<Return> returnList) {

		Map<String, List<Return>> returnMap = new HashMap<>();

		for (Return return1 : returnList) {
			if (returnMap.containsKey(return1.getReturnGroupId() + "~" + return1.getReturnGroupName())) {
				returnMap.get(return1.getReturnGroupId() + "~" + return1.getReturnGroupName()).add(return1);
			} else {
				List<Return> newlist = new ArrayList<Return>();
				newlist.add(return1);
				returnMap.put(return1.getReturnGroupId() + "~" + return1.getReturnGroupName(), newlist);
			}
		}

		List<ReturnGroupMapping> returnGroupMapps = new ArrayList<>();
		for (String key : returnMap.keySet()) {
			ReturnGroupMapping returnGroupMapping = new ReturnGroupMapping();
			returnGroupMapping.setReturnGroupMapId(Long.parseLong(key.split("~")[0]));
			returnGroupMapping.setDefaultGroupName(key.split("~")[1]);
			returnGroupMapping.setReturnList(returnMap.get(key));
			returnGroupMapping.setReturnCount((long) returnMap.get(key).size());
			returnGroupMapps.add(returnGroupMapping);
		}
		// Sort by return group name then return name asc
		sortReturnGroupData(returnGroupMapps);
		return returnGroupMapps;
	}

	private void validateReturnEntityMapDto(ReturnEntityMapDto returnChannelMapReqDto) throws ApplicationException {
		String errorMessage = "";
		if (returnChannelMapReqDto.getUserId() == null) {
			if (!errorMessage.equals("")) {
				errorMessage = ErrorConstants.USR_ID_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ErrorConstants.USR_ID_NOT_FOUND.getConstantVal();
			}
		} else if (returnChannelMapReqDto.getRoleId() == null) {
			if (!errorMessage.equals("")) {
				errorMessage = ErrorConstants.USER_ROLE_ID_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ErrorConstants.USER_ROLE_ID_NOT_FOUND.getConstantVal();
			}
		} else if (returnChannelMapReqDto.getIsActive() == null) {
			if (!errorMessage.equals("")) {
				errorMessage = ErrorConstants.ACTIVE_FLAG_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ErrorConstants.ACTIVE_FLAG_NOT_FOUND.getConstantVal();
			}
		} else if (returnChannelMapReqDto.getLangCode() == null) {
			if (!errorMessage.equals("")) {
				errorMessage = ErrorConstants.LANG_CODE_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ErrorConstants.LANG_CODE_NOT_FOUND.getConstantVal();
			}
		} else if (returnChannelMapReqDto.getEntityCode() == null) {
			if (!errorMessage.equals("")) {
				errorMessage = ErrorConstants.ENTITY_CODE_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ErrorConstants.ENTITY_CODE_NOT_FOUND.getConstantVal();
			}
		}

		if (!errorMessage.equals("")) {
			throw new ApplicationException(ErrorCode.EC0391.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()));
		}
	}

	@PostMapping(value = "/getReturnListWithFrequency/{languageCode}")
	public ServiceResponse fetchReturnListWithFrequency(@PathVariable String languageCode, @RequestBody List<Long> returnIdList) {
		ServiceResponse serviceResponse = null;

		if (languageCode != null) {
			List<Return> returnList = getReturnListWithFrequency(returnIdList, languageCode);
			//return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(returnList)).build();
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(prepareResponseOfReturnList(returnList));

		}
		return serviceResponse;
	}

	private List<Return> prepareResponseOfReturnList(List<Return> returnList) {
		List<Return> returnListNew = new ArrayList<>();
		for (Return returnObj : returnList) {
			Return tempReturnObj = new Return();
			tempReturnObj.setReturnId(returnObj.getReturnId());
			tempReturnObj.setReturnCode(returnObj.getReturnCode());
			tempReturnObj.setReturnLblSet(returnObj.getReturnLblSet());
			//			ReturnProperty returnProperty =new ReturnProperty();
			//			returnProperty.setReturnProprtyId(tempReturnObj.getReturnPropertyIdFk().getReturnProprtyId());
			//			returnProperty.setReturnProperty(tempReturnObj.getReturnPropertyIdFk().getReturnProperty());
			//			tempReturnObj.setReturnPropertyIdFk(returnProperty);
			Frequency frequency = new Frequency();
			frequency.setFrequencyId(returnObj.getFrequency().getFrequencyId());
			frequency.setFrequencyCode(returnObj.getFrequency().getFrequencyCode());
			frequency.setFrequencyName(returnObj.getFrequency().getFrequencyName());
			frequency.setFreqLbl(returnObj.getFrequency().getFreqLbl());
			tempReturnObj.setFrequency(frequency);
			returnListNew.add(tempReturnObj);
		}
		return returnListNew;
	}

	public List<Return> getReturnListWithFrequency(List<Long> returnIdList, String langCode) {
		try {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), true);
			dataMap.put(ColumnConstants.LANG_CODE.getConstantVal(), langCode);
			dataMap.put(ColumnConstants.RETURN_ID_ARRAY.getConstantVal(), returnIdList);
			return returnService.getDataByObject(dataMap, MethodConstants.GET_RETURN_LIST_WITH_FREQUENCY.getConstantVal());

		} catch (Exception e) {
			Logger.error("Exception while fetching entity list", e);
		}
		return null;
	}

	public List<Return> getReturnListWithFrequencyByReturnCodes(List<String> returnCodeList, String langCode) {
		try {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), true);
			dataMap.put(ColumnConstants.LANG_CODE.getConstantVal(), langCode);
			dataMap.put(ColumnConstants.RETURN_CODE_LIST.getConstantVal(), returnCodeList);
			return returnService.getDataByObject(dataMap, MethodConstants.GET_RETURN_LIST_WITH_FREQUENCY_BY_RETURN_CODE.getConstantVal());

		} catch (Exception e) {
			Logger.error("Exception while fetching Return list with frequency by return codes", e);
		}
		return null;
	}

}
