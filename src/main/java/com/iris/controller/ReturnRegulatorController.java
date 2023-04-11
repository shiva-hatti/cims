package com.iris.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ReturnRegulatorMappingDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.Frequency;
import com.iris.model.Regulator;
import com.iris.model.Return;
import com.iris.model.ReturnGroupMapping;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.model.UserMaster;
import com.iris.service.GenericService;
import com.iris.service.impl.ReturnRegulatorMappingService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/returnRegulatorController")
public class ReturnRegulatorController {

	private static final Logger Logger = LogManager.getLogger(ReturnRegulatorController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private ReturnRegulatorMappingService returnRegulatorMappingService;

	// @Autowired
	// private GenericService<UserRoleReturnMapping, Long>
	// userRoleReturnMappingService;

	/**
	 * This method returns list of Returns which are Mapped to Particular department with The List of Returns which are not mapped to any of the department based on User Role.
	 * 
	 * @param jobProcessId
	 * @param returnListRequest
	 * @return
	 */
	@PostMapping(value = "/getMappedAndUnMappedReturnList")
	public ServiceResponse getMappedAndUnMappedReturnList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupMappingRequest returnListRequest) {
		List<Return> returnList = new ArrayList<>();
		List<ReturnGroupMappingDto> returnGroupMappingDtoList = new ArrayList<>();
		try {
			UserMaster userMaster = userMasterService.getDataById(returnListRequest.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null || userMaster.getRoleType().getRoleTypeId() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			Regulator reg = userMaster.getDepartmentIdFk();

			if (!reg.getIsMaster()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.E015.getConstantVal()).setStatusMessage(ErrorConstants.E015_MSG.getConstantVal()).build();
			}

			Map<String, Object> columneMap = new HashMap<>();
			columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnListRequest.getIsActive());
			columneMap.put(ColumnConstants.ROLEID.getConstantVal(), returnListRequest.getRoleId());
			columneMap.put(ColumnConstants.REGULATORID.getConstantVal(), returnListRequest.getDeptId());
			columneMap.put(ColumnConstants.LANG_ID.getConstantVal(), returnListRequest.getLangId());

			List<Return> mappedReturnList = returnRegulatorMappingService.getReturnList(columneMap, MethodConstants.GET_MAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal());
			Logger.info("request completed to get mapped return list for job processingid" + jobProcessId);
			if (CollectionUtils.isEmpty(mappedReturnList)) {
				Logger.info("Empty com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) mappedReturnList " + mappedReturnList + "" + jobProcessId);
			} else {
				Logger.info("with data com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) mappedReturnList " + mappedReturnList + "" + jobProcessId);
			}

			// for (ReturnRegulatorMapping returnRegulatorMapping : mappedReturnList) {
			//
			// Return returnDt = returnRegulatorMapping.getReturnIdFk();
			// returnDt.setDeptMapped(Boolean.TRUE);
			// returnDt.setEmailIds(returnRegulatorMapping.getEmailIds());
			returnList.addAll(mappedReturnList);
			System.out.println(new Gson().toJson(mappedReturnList));
			// }

			List<Return> unMappedReturnList = returnRegulatorMappingService.getReturnList(columneMap, MethodConstants.GET_UNMAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal());
			Logger.info("request completed to get unmapped return list for job processingid" + jobProcessId);

			// for (UserRoleReturnMapping userRoleReturnMapping : unMappedReturnList) {
			//
			// Return returnDt = userRoleReturnMapping.getReturnIdFk();
			// returnDt.setDeptMapped(Boolean.FALSE);
			returnList.addAll(unMappedReturnList);
			if (CollectionUtils.isEmpty(unMappedReturnList)) {
				Logger.info("Empty com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) unMappedReturnList " + unMappedReturnList + "" + jobProcessId);
			} else {
				Logger.info("with data com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) unMappedReturnList " + unMappedReturnList + "" + jobProcessId);
			}
			System.out.println(new Gson().toJson(unMappedReturnList));
			// }

			Set<ReturnGroupMapping> returnGroupMappings = returnList.stream().map(f -> f.getReturnGroupMapIdFk()).collect(Collectors.toSet());
			if (returnList == null || returnList.isEmpty()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
			}
			returnGroupMappings = addReturnListToReturnGroup(returnListRequest.getLangId(), returnList, returnGroupMappings, jobProcessId);
			if (CollectionUtils.isEmpty(returnGroupMappings)) {
				Logger.info("Empty com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) returnGroupMappings " + returnGroupMappings + "" + jobProcessId);
			} else {
				Logger.info("with data com.iris.controller.ReturnRegulatorController.getMappedAndUnMappedReturnList(String, ReturnGroupMappingRequest) returnGroupMappings " + returnGroupMappings + "" + jobProcessId);
			}
			ReturnGroupMappingDto returnGroupMappingDto;
			UserMaster user;
			List<ReturnDto> returnDtoList;
			ReturnDto returnDto;
			Frequency frequency;
			// prepare Response in form of List of Return Groups including the List of
			// Returns
			for (ReturnGroupMapping returnGroupMapping : returnGroupMappings) {
				if (returnGroupMapping == null) {
					continue;
				}
				returnGroupMappingDto = new ReturnGroupMappingDto();
				returnGroupMappingDto.setDefaultGroupName(returnGroupMapping.getDefaultGroupName());
				returnGroupMappingDto.setIsActive(returnGroupMapping.getIsActive());
				returnGroupMappingDto.setReturnGroupMapId(returnGroupMapping.getReturnGroupMapId());
				//// returnGroupMappingDto.setOrderNo(returnGroupMapping.getOrderNo());
				//
				// user = new UserMaster();
				// user.setUserId(returnGroupMapping.getCreatedBy().getUserId());
				// user.setUserName(returnGroupMapping.getCreatedBy().getUserName());
				// user.setFirstName(returnGroupMapping.getCreatedBy().getFirstName());
				// user.setLastName(returnGroupMapping.getCreatedBy().getLastName());
				// returnGroupMappingDto.setCreatedBy(user);
				//
				returnDtoList = new ArrayList<>();
				for (Return returnDt : returnGroupMapping.getReturnList()) {
					if (returnDt == null) {
						continue;
					}
					returnDto = new ReturnDto();
					returnDto.setReturnId(returnDt.getReturnId());
					returnDto.setReturnName(returnDt.getReturnName());
					returnDto.setDeptMapped(returnDt.isDeptMapped());
					returnDto.setEmailIds(returnDt.getEmailIds());
					returnDto.setReturnCode(returnDt.getReturnCode());
					returnDtoList.add(returnDto);
				}
				returnGroupMappingDto.setReturnList(returnDtoList);
				returnGroupMappingDto.setReturnCount(returnDtoList.size());
				returnGroupMappingDtoList.add(returnGroupMappingDto);
			}
			Logger.info("return list prepared for job processingid" + jobProcessId);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response;
		if (returnGroupMappingDtoList == null || returnGroupMappingDtoList.isEmpty()) {
			response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			Logger.info("request completed to get return list for job processingid returnGroupMappingDtoList " + returnGroupMappingDtoList + " " + jobProcessId);
			sortData(returnGroupMappingDtoList);
			response.setResponse(returnGroupMappingDtoList);
		}
		Logger.info("request completed to get return list for job processingid" + jobProcessId);
		return response;

	}

	/**
	 * @param langId
	 * @param returnlist
	 * @param returnGrouplist
	 * @param returnGroupIds
	 */
	private Set<ReturnGroupMapping> addReturnListToReturnGroup(Long langId, List<Return> returnlist, Set<ReturnGroupMapping> returnGrouplist, String jobProcessId) {
		Map<Long, ReturnGroupMapping> returnGroupMap = new HashMap<>();
		try {
			for (ReturnGroupMapping returnGroupMapping : returnGrouplist) {
				if (returnGroupMapping == null) {
					continue;
				}
				List<Return> returnSet = new ArrayList<>();
				for (Return returnDt : returnlist) {
					if (returnDt == null) {
						continue;
					}
					if (returnDt.getReturnGroupMapIdFk() != null && returnDt.getReturnGroupMapIdFk().getReturnGroupMapId() != null && returnDt.getReturnGroupMapIdFk().getReturnGroupMapId().equals(returnGroupMapping.getReturnGroupMapId())) {
						// ReturnLabel returnLabel = returnDt.getReturnLblSet().stream().filter(f ->
						// f.getLangIdFk().getLanguageId().equals(langId)).findAny().orElse(null);
						// if (returnLabel != null) {
						// returnDt.setReturnName(returnLabel.getReturnLabel());
						// }
						returnSet.add(returnDt);
					}
					returnDt = null;
				}
				returnGroupMapping.setReturnList(returnSet);
				returnGroupMap.put(returnGroupMapping.getReturnGroupMapId(), returnGroupMapping);
				// ReturnGroupLabelMapping returnGroupLabelMapping =
				// returnGroupMapping.getRtnGroupLblSet().stream().filter(f ->
				// f.getLangIdFk().getLanguageId().equals(langId)).findAny().orElse(null);
				//
				// if (returnGroupLabelMapping != null) {
				// returnGroupMapping.setDefaultGroupName(returnGroupLabelMapping.getGroupLabel());
				// }
			}
			returnGrouplist = new HashSet<>(returnGroupMap.values());
		} catch (Exception e) {
			Logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			throw e;
		}
		return returnGrouplist;
	}

	private void sortData(List<ReturnGroupMappingDto> returnGroupMappingDtoList) {
		if (!CollectionUtils.isEmpty(returnGroupMappingDtoList)) {
			// Sorting by default group name
			Collections.sort(returnGroupMappingDtoList, new Comparator<ReturnGroupMappingDto>() {
				@Override
				public int compare(ReturnGroupMappingDto o1, ReturnGroupMappingDto o2) {
					return o1.getDefaultGroupName().toUpperCase().compareTo(o2.getDefaultGroupName().toUpperCase());
				}
			});

			// Sorting by return name
			for (ReturnGroupMappingDto returnGroupMappingDto : returnGroupMappingDtoList) {
				if (!CollectionUtils.isEmpty(returnGroupMappingDto.getReturnList())) {
					Collections.sort(returnGroupMappingDto.getReturnList(), new Comparator<ReturnDto>() {
						@Override
						public int compare(ReturnDto o1, ReturnDto o2) {
							return o1.getReturnName().toUpperCase().compareTo(o2.getReturnName().toUpperCase());
						}
					});
				}
			}
		}
	}

	/**
	 * This method adds Mapping Between Return and Department.
	 * 
	 * @param jobProcessId
	 * @param returnRegulatorMappingDto
	 * @return
	 */
	@PostMapping(value = "/addReturnDepartmentMapping")
	public ServiceResponse addReturnDepartmentMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnRegulatorMappingDto returnRegulatorMappingDto) {
		ReturnRegulatorMapping returnRegulatorMapping = new ReturnRegulatorMapping();
		try {
			Logger.debug("Request for Return mapping to Department for job processingid" + jobProcessId);
			UserMaster user = new UserMaster();
			user.setUserId(returnRegulatorMappingDto.getCreatedBy());
			returnRegulatorMapping.setCreatedBy(user);
			Regulator regulator = new Regulator();
			regulator.setRegulatorId(returnRegulatorMappingDto.getRegulatorIdFk());
			returnRegulatorMapping.setRegulatorIdFk(regulator);
			returnRegulatorMapping.setIsActive(returnRegulatorMappingDto.getIsActive());
			returnRegulatorMapping.setRoleId(returnRegulatorMappingDto.getRoleIdFk());
			returnRegulatorMapping.setLangCode(returnRegulatorMappingDto.getLangCode());

			Map<Long, String> returnEmailMap = returnRegulatorMappingDto.getReturnEmailIdMap();
			if (!CollectionUtils.isEmpty(returnEmailMap)) {
				Set<Long> returnIdSet = returnEmailMap.keySet();
				int totalReturns = returnIdSet.size();
				String emailIdString = "";
				int count = 0;
				int maxCount = 0;
				int elementArrs = (totalReturns / 10) + 1;
				Map<String, String> returnEmailStringMap = new HashMap<>();
				StringBuilder eAlert = new StringBuilder();
				List<Long> returnIdList = new ArrayList<>();
				for (Long returnId : returnIdSet) {
					if (maxCount < 10) {
						if (count >= 1 && maxCount <= 9 && count <= returnIdSet.size() - 1) {
							eAlert = eAlert.append(",");
						}
						eAlert = eAlert.append(returnId);
						if (count >= 1 && count <= returnIdSet.size() - 1) {
							emailIdString += ";";
						}
						emailIdString = emailIdString + returnEmailMap.get(returnId);
						count += 1;
						maxCount += 1;
					} else if (count == returnIdSet.size() - 1 || maxCount == 10) {
						returnEmailStringMap.put(eAlert.toString(), emailIdString);
						eAlert = new StringBuilder();
						emailIdString = returnEmailMap.get(returnId);
						eAlert = eAlert.append(returnId);
						maxCount = 1;
						count += 1;
					}
					returnIdList.add(returnId);
				}
				if (count == returnIdSet.size()) {
					returnEmailStringMap.put(eAlert.toString(), emailIdString);
				}
				returnRegulatorMapping.setReturnIdArray(returnIdList);
				returnRegulatorMapping.setReturnArrs(elementArrs);
				returnRegulatorMapping.setReturnEmailIdStringMap(returnEmailStringMap);
			}

			returnRegulatorMapping = returnRegulatorMappingService.add(returnRegulatorMapping);
		} catch (ServiceException e) {
			Logger.error("ServiceException while mapping return to deparment for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0805.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0805.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while mapping return to deparment for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response;
		if (returnRegulatorMapping == null) {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			Logger.debug("Return mapping to Department Failed. for job processingid" + jobProcessId);
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(returnRegulatorMapping).build();
			Logger.info("request completed to add Return Department mapping for job processingid" + jobProcessId);
		}
		return response;
	}

	/**
	 * @param jobProcessId
	 * @param returnRegulatorMappingDto
	 * @return
	 */
	@PostMapping("/updateReturnRegulatorMapping")
	public ServiceResponse updateReturnRegulatorMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnRegulatorMappingDto returnRegulatorMappingDto) {
		Logger.info("This is updateReturnRegulatorMapping method");
		ReturnRegulatorMapping returnRegulatorMapping = new ReturnRegulatorMapping();
		ServiceResponse response;
		try {
			Logger.debug("Request for Updating Return mapping to Department for job processingid" + jobProcessId);
			UserMaster user = new UserMaster();
			user.setUserId(returnRegulatorMappingDto.getCreatedBy());
			returnRegulatorMapping.setCreatedBy(user);
			returnRegulatorMapping.setUpdatedBy(user);
			Regulator regulator = new Regulator();
			regulator.setRegulatorId(returnRegulatorMappingDto.getRegulatorIdFk());
			returnRegulatorMapping.setRegulatorIdFk(regulator);
			returnRegulatorMapping = returnRegulatorMappingService.add(returnRegulatorMapping);
		} catch (ServiceException e) {
			Logger.error("Request object not proper to update Return mapping to Department ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while Updating Return mapping to Department info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0903.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0903.toString())).build();
		}
		response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		Logger.info("request completed to update Return mapping to Department for job processingid" + jobProcessId);
		return response;
	}
}
