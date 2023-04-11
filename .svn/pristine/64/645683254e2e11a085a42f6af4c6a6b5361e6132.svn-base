/**
 * 
 */
package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.caching.ObjectCache;
import com.iris.dto.DataListDto;
import com.iris.dto.EntityReturnChanneMapAppDto;
import com.iris.dto.ReturnChannelDetailsDto;
import com.iris.dto.ReturnDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.model.Category;
import com.iris.model.EntityBean;
import com.iris.model.EntityReturnChanMapApproval;
import com.iris.model.Return;
import com.iris.model.ReturnEntityChannelMapModification;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.SubCategory;
import com.iris.model.UserMaster;
import com.iris.repository.EntityReturnChanMapAppRepo;
import com.iris.repository.ModuleApprovalDeptWiseRepo;
import com.iris.repository.ReturnRepo;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class EntityReturnChannelMappingApprovalService {

	@Autowired
	private EntityReturnChanMapAppRepo returnEntityChanMapAppRepo;

	@Autowired
	private ReturnEntityMapServiceNew returnEntityMapServiceNew;

	@Autowired
	private ReturnEntityChannelMapModificationService returnEntityChannelMapModificationService;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private ModuleApprovalDeptWiseRepo moduleApprovalDeptWiseRepo;

	@Autowired
	private EntityReturnChanMapAppRepo entityReturnChanMapAppRepo;

	@Autowired
	private UserMasterService userService;

	private static final Logger LOGGER = LogManager.getLogger(EntityReturnChannelMappingApprovalService.class);

	public List<EntityReturnChanneMapAppDto> getEntityReturnChannelMappingApprovalData(Long adminStatusId, boolean isActive, Long userId, String langCode) {

		List<EntityReturnChanneMapAppDto> entityReturnChannleMaps = returnEntityChanMapAppRepo.getDataForApproval(adminStatusId, isActive, userId, langCode);

		if (!CollectionUtils.isEmpty(entityReturnChannleMaps)) {
			entityReturnChannleMaps.forEach(entityReturnChannleMap -> {
				if (!StringUtils.isEmpty(entityReturnChannleMap.getEntityReturnChannelMapJson())) {
					entityReturnChannleMap.setReturnChannelMapping(JsonUtility.getGsonObject().fromJson(entityReturnChannleMap.getEntityReturnChannelMapJson(), DataListDto.class));
					entityReturnChannleMap.setEntityReturnChannelMapJson(null);
				}
			});
		}

		return entityReturnChannleMaps;
	}

	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse approveAndRejectEntityReturnChannelMappingData(EntityReturnChanneMapAppDto entityReturnChanneMapAppDto, String jobProcessingId) {
		EntityReturnChanMapApproval entityReturnChanMapApproval = returnEntityChanMapAppRepo.getDataByReturnEntityChanMapAppId(entityReturnChanneMapAppDto.getEntityReturnChanneMapAppId());
		if (entityReturnChanMapApproval.getApprovalStatus().getAdminStatusId().equals(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal().longValue())) {
			AdminStatus approvalStatus = new AdminStatus();
			approvalStatus.setAdminStatusId(entityReturnChanneMapAppDto.getAdminStatusId());
			entityReturnChanMapApproval.setApprovalStatus(approvalStatus);

			if (entityReturnChanneMapAppDto.getAdminStatusId().equals(GeneralConstants.REJECTED_ADMIN_STATUS_ID.getConstantIntVal().longValue())) {
				// Reject Record
				UserMaster userMaster = new UserMaster();
				userMaster.setUserId(entityReturnChanneMapAppDto.getApprovedBy().getUserId());
				entityReturnChanMapApproval.setApprovedBy(userMaster);
				entityReturnChanMapApproval.setComment(entityReturnChanneMapAppDto.getComment());
				entityReturnChanMapApproval.setApprovedOn(new Date());
			} else {
				// Approve record
				DataListDto dataListDto = JsonUtility.getGsonObject().fromJson(entityReturnChanMapApproval.getReturnEntityChanMapJson(), DataListDto.class);
				insertReturnEntityChannelMappingData(dataListDto, jobProcessingId);

				UserMaster userMaster = new UserMaster();
				userMaster.setUserId(entityReturnChanneMapAppDto.getApprovedBy().getUserId());
				entityReturnChanMapApproval.setApprovedBy(userMaster);
				entityReturnChanMapApproval.setApprovedOn(new Date());

			}
			returnEntityChanMapAppRepo.save(entityReturnChanMapApproval);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0014.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0014.toString())).build();
		} else {
			// Record already approved
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse insertReturnEntityChannelMappingData(DataListDto dataListDto, String jobProcessingId) {
		List<String> returnIdList = getReturnIdList(dataListDto);
		ReturnEntityMappingNew returnEntityMappingNew = null;
		ReturnEntityChannelMapModification returnEntityChannelMapModification = null;
		EntityBean entityBean = null;
		Return returnBean = null;
		List<ReturnEntityChannelMapModification> listOfModBean = new ArrayList<>();
		String jsonDataString = null;
		if (dataListDto.getEntityId() != null) {
			entityBean = new EntityBean();
			entityBean.setEntityId(dataListDto.getEntityId());

			for (ReturnChannelDetailsDto returnChannelDetailsDto : dataListDto.getChannelDetails()) {
				returnEntityChannelMapModification = new ReturnEntityChannelMapModification();
				returnEntityMappingNew = getEntityReturnChannelMapping(dataListDto.getEntityId(), returnChannelDetailsDto.getReturnId());
				returnBean = new Return();
				returnBean.setReturnId(returnChannelDetailsDto.getReturnId());
				returnEntityChannelMapModification.setEntityIdFk(entityBean);
				returnEntityChannelMapModification.setReturnIdFk(returnBean);
				if (!Objects.isNull(returnEntityMappingNew)) {
					returnEntityChannelMapModification.setModifiedByFk(returnEntityMappingNew.getModifiedbyFkChannel());
					returnEntityChannelMapModification.setModifiedOn(returnEntityMappingNew.getModifiedOnChannel());
				}

				jsonDataString = JsonUtility.getGsonObject().toJson(returnEntityMappingNew);
				returnEntityChannelMapModification.setEntReturnChannelMapJsonData(jsonDataString);
				listOfModBean.add(returnEntityChannelMapModification);
			}
			if (!CollectionUtils.isEmpty(returnIdList)) {
				try {
					boolean flag = returnEntityMapServiceNew.addUpdateReturnEntityChannel(dataListDto.getUserId(), dataListDto.getEntityId(), returnIdList.toArray(new String[returnIdList.size()]));

					if (flag) {
						for (ReturnEntityChannelMapModification ReturnEntityChannelMapModification : listOfModBean) {
							returnEntityChannelMapModificationService.add(ReturnEntityChannelMapModification);
						}
						return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0014.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0014.toString())).build();
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
					}
				} catch (Exception e) {
					LOGGER.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessingId + "Exception is", e);
				}
			}

		} else {
			try {
				boolean flag = returnEntityMapServiceNew.addUpdateReturnEntityChannelWithCategoryCode(dataListDto.getCateId(), dataListDto.getSubCatId(), returnIdList.toArray(new String[returnIdList.size()]), dataListDto.getUserId());

				if (flag) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0014.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0014.toString())).build();
				} else {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
				}
			} catch (Exception e) {
				LOGGER.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessingId + "Exception is", e);
			}
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse updateEntityReturnChannelMappingData(DataListDto dataListDto, String jobProcessId) {
		UserDto userDto = new UserDto();
		userDto.setUserId(dataListDto.getUserId());
		userDto.setIsActive(true);
		userDto.setLangCode(dataListDto.getLangCode());

		UserDetailsDto userDetailsDto = userService.getUserDetails(userDto);
		EntityReturnChanneMapAppDto entityReturnChanMapApproval = null;
		if (dataListDto.getEntityId() != null) {
			entityReturnChanMapApproval = entityReturnChanMapAppRepo.isRecordPendingForApproval(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal().longValue(), true, dataListDto.getCateId(), dataListDto.getSubCatId(), dataListDto.getEntityId());
		} else {
			entityReturnChanMapApproval = entityReturnChanMapAppRepo.isRecordPendingForApproval(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal().longValue(), true, dataListDto.getCateId(), dataListDto.getSubCatId());
		}

		if (entityReturnChanMapApproval != null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1686.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1686.toString())).build();
		}

		boolean moduleApprovalFlag = false;
		if (userDetailsDto.getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal().intValue())) {
			if (moduleApprovalDeptWiseRepo.fetchModuleApprovalDeptwiseForDept(dataListDto.getMenuId(), userDetailsDto.getRegulatorDto().getRegulatorId().longValue(), userDetailsDto.getRoleTypeId().longValue()) != null) {
				moduleApprovalFlag = true;
			}
		} else {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1575.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1575.toString())).build();
		}

		if (CollectionUtils.isEmpty(dataListDto.getChannelDetails())) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
		}

		if (moduleApprovalFlag) {
			return saveDataForApproval(dataListDto);
		} else {
			return insertReturnEntityChannelMappingData(dataListDto, jobProcessId);
		}
	}

	public ServiceResponse saveDataForApproval(DataListDto dataListDto) {

		List<ReturnDto> returnDtos = returnRepo.getReturnCodeByReturnIds(dataListDto.getChannelDetails().stream().map(f -> f.getReturnId()).collect(Collectors.toList()));

		Map<Long, String> returnCodeMap = returnDtos.stream().collect(Collectors.toMap(ReturnDto::getReturnId, ReturnDto::getReturnCode));

		dataListDto.getChannelDetails().forEach(f -> {
			f.setReturnCode(returnCodeMap.get(f.getReturnId()));
		});

		EntityReturnChanMapApproval entityReturnChanMapApproval = new EntityReturnChanMapApproval();

		Category category = new Category();
		category.setCategoryId(dataListDto.getCateId());
		entityReturnChanMapApproval.setCategory(category);

		SubCategory subCategory = new SubCategory();
		subCategory.setSubCategoryId(dataListDto.getSubCatId());
		entityReturnChanMapApproval.setSubCategory(subCategory);

		UserMaster userMaster = new UserMaster();
		userMaster.setUserId(dataListDto.getUserId());
		entityReturnChanMapApproval.setCreatedBy(userMaster);

		if (dataListDto.getEntityId() != null) {
			EntityBean entityBean = new EntityBean();
			entityBean.setEntityId(dataListDto.getEntityId());
			entityReturnChanMapApproval.setEntity(entityBean);
		}

		entityReturnChanMapApproval.setReturnEntityChanMapJson(JsonUtility.getGsonObject().toJson(dataListDto));
		entityReturnChanMapApproval.setCreatedOn(new Date());

		AdminStatus adminStatus = new AdminStatus();
		adminStatus.setAdminStatusId(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal().longValue());
		entityReturnChanMapApproval.setApprovalStatus(adminStatus);

		entityReturnChanMapApproval.setIsActive(true);

		returnEntityChanMapAppRepo.save(entityReturnChanMapApproval);

		return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0015.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0015.toString())).build();
	}

	private List<String> getReturnIdList(DataListDto dataListDto) {
		List<String> returnLists = null;
		if (!CollectionUtils.isEmpty(dataListDto.getChannelDetails())) {
			returnLists = new ArrayList<>();
			for (ReturnChannelDetailsDto returnChannelDetailsDto : dataListDto.getChannelDetails()) {
				if (returnChannelDetailsDto.isApiChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_API_CHANNEL");
				}
				if (returnChannelDetailsDto.isEmailChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_EMAIL_CHANNEL");
				}
				if (returnChannelDetailsDto.isStsChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_STS_CHANNEL");
				}
				if (returnChannelDetailsDto.isUploadChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_UPLOAD_CHANNEL");
				}
				if (returnChannelDetailsDto.isWebChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_WEB_CHANNEL");
				}
				if (!returnChannelDetailsDto.isApiChannel() && !returnChannelDetailsDto.isEmailChannel() && !returnChannelDetailsDto.isStsChannel() && !returnChannelDetailsDto.isUploadChannel() && !returnChannelDetailsDto.isWebChannel()) {
					returnLists.add(returnChannelDetailsDto.getReturnId().toString() + "_NO_CHANNEL_MAP");
				}
			}
		}
		return returnLists;
	}

	private ReturnEntityMappingNew getEntityReturnChannelMapping(Long entityId, Long returnId) {
		ReturnEntityMappingNew returnEntityMappingNew = null;
		Map<String, Object> columnValueMap = new HashMap<>();

		columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), entityId);
		columnValueMap.put(ColumnConstants.RETURNID.getConstantVal(), returnId);

		List<ReturnEntityMappingNew> returnEntityMappingNewList = returnEntityMapServiceNew.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_ID_AND_RETURN_ID.getConstantVal());
		UserMaster um = null;
		for (ReturnEntityMappingNew returnEntityMapingNew : returnEntityMappingNewList) {
			um = new UserMaster();
			returnEntityMappingNew = new ReturnEntityMappingNew();
			returnEntityMappingNew.setApiChannel(returnEntityMapingNew.isApiChannel());
			returnEntityMappingNew.setEmailChannel(returnEntityMapingNew.isEmailChannel());
			returnEntityMappingNew.setStsChannel(returnEntityMapingNew.isStsChannel());
			returnEntityMappingNew.setUploadChannel(returnEntityMapingNew.isUploadChannel());
			returnEntityMappingNew.setWebChannel(returnEntityMapingNew.isWebChannel());
			if (returnEntityMapingNew.getModifiedbyFkChannel() != null) {
				um.setUserId(returnEntityMapingNew.getModifiedbyFkChannel().getUserId());
				returnEntityMappingNew.setModifiedbyFkChannel(um);
			}
			returnEntityMappingNew.setModifiedOnChannel(returnEntityMapingNew.getModifiedOnChannel());
			returnEntityMappingNew.setChannelUpdatedViaModule(returnEntityMapingNew.getChannelUpdatedViaModule());
		}

		return returnEntityMappingNew;
	}

}
