/**
 * 
 */
package com.iris.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DataListDto;
import com.iris.dto.DynamicContent;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.model.EntityBean;
import com.iris.model.EntityLabelBean;
import com.iris.model.Return;
import com.iris.model.ReturnEntityChannelMapModification;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnEntityMappingNewMod;
import com.iris.model.UserMaster;
import com.iris.repository.ModuleApprovalDeptWiseRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityReturnChannelMappingApprovalService;
import com.iris.service.impl.ReturnEntityChannelMapModificationService;
import com.iris.service.impl.ReturnEntityMapNewModService;
import com.iris.service.impl.ReturnEntityMapServiceNew;
import com.iris.service.impl.ReturnService;
import com.iris.service.impl.UserMasterService;
import com.iris.util.JsonUtility;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 *
 */

@RestController
@RequestMapping("/service/returnEntityMappingService")
public class ReturnEntityMappingController {

	@Autowired
	private ReturnEntityMapServiceNew returnEntityMapServiceNew;

	@Autowired
	private ReturnEntityMapNewModService returnEntityMapNewModService;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private GenericService<EntityBean, Long> entityService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private EntityReturnChannelMappingApprovalService entutyReturnChannelMappingApprovalService;

	@Autowired
	private ReturnEntityChannelMapModificationService returnEntityChannelMapModificationService;

	private static final Logger logger = LogManager.getLogger(ReturnEntityMappingController.class);

	private static final String GROUP = "GROUP";
	private static final String INDIVIDUAL = "INDIVIDUAL";
	private static final Integer MOD_HISTORY_RECORD_COUNT = 10;

	@PostMapping(value = "/addEditReturnEntityMapping")
	public ServiceResponse addEditReturnEntityMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {

		logger.info("Request received for jobProcessingId" + jobProcessId);

		try {
			validateRequestDto(dataListDto, jobProcessId);
			String[] returnCodeArray = null;

			if (!CollectionUtils.isEmpty(dataListDto.getReturnCodeList())) {
				returnCodeArray = dataListDto.getReturnCodeList().toArray(new String[dataListDto.getReturnCodeList().size()]);
			}

			Long[] entityArray = dataListDto.getEntityIdList().toArray(new Long[dataListDto.getEntityIdList().size()]);

			UserMaster userMaster = userMasterService.getDataById(dataListDto.getUserId());

			if (StringUtils.isEmpty(userMaster)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0610.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0610.toString())).build();
			}

			String moduleName;

			if (dataListDto.getIsGroupMapp().equals(Boolean.TRUE)) {
				moduleName = GROUP;
			} else {
				moduleName = INDIVIDUAL;
			}

			if (dataListDto.getIsGroupMapp().equals(Boolean.TRUE)) {
				List<EntityBean> entityList = entityService.getDataByIds(entityArray);
				List<ReturnEntityMappingNewMod> returnEntityMappingModList = getReturnEntMappingList(entityList, userMaster);

				returnEntityMapServiceNew.addUpdateReturnEntityMap(dataListDto.getUserId(), returnCodeArray, entityArray, moduleName);

				if (!CollectionUtils.isEmpty(returnEntityMappingModList)) {
					returnEntityMapNewModService.addListObject(returnEntityMappingModList);
				}

				List<DynamicContent> dynamicContentList = getDyamicListForGroupModification(userMaster);

				sendMail(dataListDto.getAlertId(), dataListDto.getMenuId(), dataListDto.getRoleId(), dataListDto.getUserId(), dynamicContentList);

				return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).build();
			} else {
				List<EntityBean> entityList = entityService.getDataByIds(entityArray);

				boolean exceptionOccured = false;
				List<String> exceptionError = new ArrayList<>();

				boolean isRequiredUpdate = false;
				for (EntityBean entityBean : entityList) {
					try {
						List<ReturnEntityMappingNew> returnEntityMappingList = entityBean.getReturnEntityMappingNew();
						ReturnEntityMappingNewMod returnEntityMappingNewMod = prepareModHistoryRecord(returnEntityMappingList, entityBean, userMaster);

						if (returnCodeArray != null) {
							List<Return> activeReturnList = returnService.getDataByCode(returnCodeArray);
							for (Return returnObj : activeReturnList) {
								ReturnEntityMappingNew returnEntityMappingNew = returnEntityMappingList.stream().filter(f -> f.getReturnObj().getReturnId().equals(returnObj.getReturnId())).findAny().orElse(null);
								if (returnEntityMappingNew == null) {
									isRequiredUpdate = true;
									ReturnEntityMappingNew newReturnEntityMappingNew = prepareReturnEntityObject(entityBean, returnObj, moduleName, userMaster);
									returnEntityMappingList.add(newReturnEntityMappingNew);
								} else {
									isRequiredUpdate = true;
									returnEntityMappingNew.setActive(true);
									returnEntityMappingNew.setModifiedBy(userMaster);
									returnEntityMappingNew.setModifiedOn(new Date());
									returnEntityMappingNew.setRetEntMapUpdatedViaModule(moduleName);
								}
							}
						}

						if (!CollectionUtils.isEmpty(dataListDto.getDeActiveReturnCodeList())) {
							for (String deactiveateReturnCode : dataListDto.getDeActiveReturnCodeList()) {
								ReturnEntityMappingNew returnEntityMappingNew = returnEntityMappingList.stream().filter(f -> f.getReturnObj().getReturnCode().equals(deactiveateReturnCode)).findAny().orElse(null);
								if (returnEntityMappingNew != null) {
									isRequiredUpdate = true;
									returnEntityMappingNew.setActive(false);
									returnEntityMappingNew.setEmailChannel(false);
									returnEntityMappingNew.setApiChannel(false);
									returnEntityMappingNew.setUploadChannel(false);
									returnEntityMappingNew.setWebChannel(false);
									returnEntityMappingNew.setStsChannel(false);
									returnEntityMappingNew.setModifiedBy(userMaster);
									returnEntityMappingNew.setModifiedOn(new Date());
									returnEntityMappingNew.setRetEntMapUpdatedViaModule(moduleName);
								}
							}
						}
						if (isRequiredUpdate) {
							returnEntityMapNewModService.updateEntityReturnMappingAndSaveModificationHistory(entityBean, returnEntityMappingNewMod);

							List<DynamicContent> dynamicContentList = getDyamicListForIndvidualModification(userMaster, entityBean);

							sendMail(dataListDto.getAlertId(), dataListDto.getMenuId(), dataListDto.getRoleId(), dataListDto.getUserId(), dynamicContentList);
						}
					} catch (Exception e) {
						exceptionError.add(entityBean.getEntityId() + "");
						exceptionOccured = true;
						logger.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessId + "Exception is", e);
					}
				}

				if (exceptionOccured) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).setResponse(exceptionError).build();
				} else {
					return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).build();
				}
			}
		} catch (ApplicationException e) {
			logger.error("Request object not proper");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(e.getMessage()).build();
		} catch (Exception e) {
			logger.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessId + "Exception is" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	@PostMapping(value = "/addEditReturnEntityChannelMapping")
	public ServiceResponse addEditReturnEntityChannelMapping(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {
		try {
			validateMethodForEntityReturnChannelMapping(dataListDto);
			return entutyReturnChannelMappingApprovalService.updateEntityReturnChannelMappingData(dataListDto, jobProcessId);
		} catch (ApplicationException e) {
			logger.error("Request object not proper in addEditReturnEntityChannelMapping for job processing ID :  " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(e.getMessage()).build();
		} catch (Exception e) {
			logger.error("Exception in addEditReturnEntityChannelMapping for JobProcessingId " + jobProcessId + "Exception is" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	private ReturnEntityMappingNew prepareReturnEntityObject(EntityBean entityBean, Return returnObj, String moduleName, UserMaster userMaster) {
		ReturnEntityMappingNew newReturnEntityMappingNew = new ReturnEntityMappingNew();
		newReturnEntityMappingNew.setReturnObj(returnObj);
		newReturnEntityMappingNew.setEntity(entityBean);
		newReturnEntityMappingNew.setActive(true);
		newReturnEntityMappingNew.setCreatedBy(userMaster);
		newReturnEntityMappingNew.setCreatedOn(new Date());
		newReturnEntityMappingNew.setRetEntMapCreatedViaModule(moduleName);
		newReturnEntityMappingNew.setApiChannel(false);
		newReturnEntityMappingNew.setUploadChannel(false);
		newReturnEntityMappingNew.setEmailChannel(false);
		newReturnEntityMappingNew.setStsChannel(false);
		newReturnEntityMappingNew.setWebChannel(false);
		return newReturnEntityMappingNew;
	}

	private ReturnEntityMappingNewMod prepareModHistoryRecord(List<ReturnEntityMappingNew> returnEntityMappingList, EntityBean entityBean, UserMaster userMaster) {
		ReturnEntityMappingNewMod returnEntityMappingNewMod = null;
		if (!CollectionUtils.isEmpty(returnEntityMappingList)) {
			returnEntityMappingNewMod = new ReturnEntityMappingNewMod();
			returnEntityMappingNewMod.setEntity(entityBean);
			returnEntityMappingNewMod.setCreatedBy(userMaster);
			returnEntityMappingNewMod.setCreatedOn(new Date());
			returnEntityMappingNewMod.setRetEntMapJsonData(getJsonObject(returnEntityMappingList));
			returnEntityMappingNewMod.setModule("Individual");
		}

		return returnEntityMappingNewMod;
	}

	private List<DynamicContent> getDyamicListForIndvidualModification(UserMaster userMaster, EntityBean entityBean) {
		String languageCode = "en";
		List<DynamicContent> dynamicContentList = new ArrayList<>();

		DynamicContent dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityName"));
		dynamicContent.setValue(entityBean.getEntityName());
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.modifiedBy"));
		dynamicContent.setValue(userMaster.getUserName());
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.modifiedOn"));
		dynamicContent.setValue(DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()));
		dynamicContentList.add(dynamicContent);

		return dynamicContentList;
	}

	private List<DynamicContent> getDyamicListForGroupModification(UserMaster userMaster) {
		List<DynamicContent> dynamicContentList = new ArrayList<>();

		DynamicContent dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "grid.modifiedBy"));
		dynamicContent.setValue(userMaster.getUserName());
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "grid.modifiedOn"));
		dynamicContent.setValue(DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()));
		dynamicContentList.add(dynamicContent);

		return dynamicContentList;
	}

	void sendMail(Long alertId, Long menuId, Long roleId, Long userId, List<DynamicContent> dynamicContentList) {
		try {
			logger.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(alertId);
			mailServiceBean.setMenuId(menuId);
			mailServiceBean.setRoleId(roleId);
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setUserId(userId);
			mailServiceBean.setDynamicContentsList(dynamicContentList);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				logger.info("Mail sent successfully");
			}
		} catch (Exception e) {
			logger.error("Exception while sending email", e);
		}

	}

	private List<ReturnEntityMappingNewMod> getReturnEntMappingList(List<EntityBean> entityList, UserMaster userMaster) {
		List<ReturnEntityMappingNewMod> returnEntityMappingNewMods = new ArrayList<>();
		for (EntityBean entityBean : entityList) {
			List<ReturnEntityMappingNew> returnEntityMappingList = entityBean.getReturnEntityMappingNew();

			if (!CollectionUtils.isEmpty(returnEntityMappingList)) {
				ReturnEntityMappingNewMod returnEntityMappingNewMod = new ReturnEntityMappingNewMod();
				returnEntityMappingNewMod.setEntity(entityBean);

				returnEntityMappingNewMod.setCreatedBy(userMaster);
				returnEntityMappingNewMod.setCreatedOn(new Date());
				returnEntityMappingNewMod.setRetEntMapJsonData(getJsonObject(returnEntityMappingList));
				returnEntityMappingNewMod.setModule(GROUP);
				returnEntityMappingNewMods.add(returnEntityMappingNewMod);
			}
		}
		return returnEntityMappingNewMods;
	}

	private String getJsonObject(List<ReturnEntityMappingNew> returnEntityMappingList) {
		List<ReturnEntityMappingNew> returnEntityMappingNews = new ArrayList<>();
		for (ReturnEntityMappingNew returnEntityMappingNew : returnEntityMappingList) {
			ReturnEntityMappingNew newReturnEntMap = new ReturnEntityMappingNew();

			Return returnObj = new Return();
			returnObj.setReturnId(returnEntityMappingNew.getReturnObj().getReturnId());
			returnObj.setReturnName(returnEntityMappingNew.getReturnObj().getReturnName());
			newReturnEntMap.setReturnObj(returnObj);

			EntityBean entity = new EntityBean();
			entity.setEntityId(returnEntityMappingNew.getEntity().getEntityId());
			entity.setEntityName(returnEntityMappingNew.getEntity().getEntityName());
			newReturnEntMap.setEntity(entity);

			newReturnEntMap.setActive(returnEntityMappingNew.isActive());

			if (returnEntityMappingNew.getCreatedBy() != null) {
				UserMaster createdBy = new UserMaster();
				createdBy.setUserName(returnEntityMappingNew.getCreatedBy().getUserName());
				newReturnEntMap.setCreatedBy(createdBy);
				newReturnEntMap.setCreatedOnString(DateManip.convertDateToString(returnEntityMappingNew.getCreatedOn(), "dd-MM-yyyy HH:mm:ss"));
			}

			if (returnEntityMappingNew.getModifiedBy() != null) {
				UserMaster modifiedBy = new UserMaster();
				modifiedBy.setUserName(returnEntityMappingNew.getModifiedBy().getUserName());
				newReturnEntMap.setModifiedBy(modifiedBy);
				newReturnEntMap.setModifiedOnString(DateManip.convertDateToString(returnEntityMappingNew.getModifiedOn(), "dd-MM-yyyy HH:mm:ss"));
			}

			newReturnEntMap.setRetEntMapCreatedViaModule(returnEntityMappingNew.getRetEntMapCreatedViaModule());
			newReturnEntMap.setRetEntMapUpdatedViaModule(returnEntityMappingNew.getRetEntMapUpdatedViaModule());

			returnEntityMappingNews.add(newReturnEntMap);
		}

		return JsonUtility.getGsonObject().toJson(returnEntityMappingNews);
	}

	private void validateRequestDtoForModHistory(DataListDto dataListDto, String jobProcessingId) throws ApplicationException {
		String errorMessage = "";

		if (UtilMaster.isEmpty(jobProcessingId)) {
			errorMessage = ErrorConstants.REQUEST_TRANSACTION_ID_NOT_FOUND.getConstantVal();
		}

		if (Objects.isNull(dataListDto.getEntityId()) || StringUtils.isEmpty(dataListDto.getEntityId())) {
			if (StringUtils.isEmpty(errorMessage)) {
				errorMessage = ErrorConstants.ENTITY_ID_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.ENTITY_ID_NOT_FOUND.getErrorMessage();
			}
		}

		if (Objects.isNull(dataListDto.getLangCode()) || StringUtils.isEmpty(dataListDto.getLangCode())) {
			if (StringUtils.isEmpty(errorMessage)) {
				errorMessage = ErrorConstants.LANG_CODE_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.LANG_CODE_NOT_FOUND.getErrorMessage();
			}
		}

		if (Objects.isNull(dataListDto.getDateFormat()) || StringUtils.isEmpty(dataListDto.getDateFormat())) {
			if (StringUtils.isEmpty(errorMessage)) {
				errorMessage = ErrorConstants.DATE_FORMAT_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.DATE_FORMAT_NOT_FOUND.getErrorMessage();
			}
		}

		if (!StringUtils.isEmpty(errorMessage)) {
			throw new ApplicationException(ErrorCode.E0768.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0768.toString()));
		}
	}

	private void validateRequestDto(DataListDto dataListDto, String jobProcessingId) throws ApplicationException {
		String errorMessage = "";

		if (UtilMaster.isEmpty(jobProcessingId)) {
			errorMessage = ErrorConstants.REQUEST_TRANSACTION_ID_NOT_FOUND.getConstantVal();
		}

		if (dataListDto.getUserId() == null) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.USER_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.USER_NOT_FOUND.getConstantVal();
			}
		}
		if (dataListDto.getIsGroupMapp().equals(Boolean.TRUE)) {
			if ((dataListDto.getReturnCodeList() == null || CollectionUtils.isEmpty(dataListDto.getReturnCodeList()))) {
				if (errorMessage.equals("")) {
					errorMessage = ErrorConstants.RETURN_ID_NOT_FOUND.getErrorMessage();
				} else {
					errorMessage = errorMessage + ", " + ErrorConstants.RECORD_COUNT_NOT_FOUND.getErrorMessage();
				}
			}
		} else {
			if ((dataListDto.getReturnIdList() == null || CollectionUtils.isEmpty(dataListDto.getReturnIdList())) && (dataListDto.getDeActiveReturnIdList() != null && CollectionUtils.isEmpty(dataListDto.getDeActiveReturnIdList()))) {
				if (errorMessage.equals("")) {
					errorMessage = ErrorConstants.RETURN_ID_OR_DEACTIVATE_RETURN_ID_NOT_FOUND.getErrorMessage();
				} else {
					errorMessage = errorMessage + ", " + ErrorConstants.RETURN_ID_OR_DEACTIVATE_RETURN_ID_NOT_FOUND.getErrorMessage();
				}
			}
		}

		if (dataListDto.getEntityIdList() == null || CollectionUtils.isEmpty(dataListDto.getEntityIdList())) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.ENTITY_ID_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.ENTITY_ID_NOT_FOUND.getErrorMessage();
			}
		}

		if (!errorMessage.equals("")) {
			logger.error("Error Code : " + ErrorCode.EC0391.toString() + ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()) + " --> " + errorMessage);
			throw new ApplicationException(ErrorCode.EC0391.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()));
		}
	}

	@PostMapping(value = "/getEntityReturnMappModHistory")
	public ServiceResponse getEntityReturnMappModHistory(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {

		logger.info("Request received for jobProcessingId" + jobProcessId);

		try {
			validateRequestDtoForModHistory(dataListDto, jobProcessId);

			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), dataListDto.getEntityId());
			valueMap.put(ColumnConstants.TOTAL_RECORD_COUNT.getConstantVal(), MOD_HISTORY_RECORD_COUNT);

			List<ReturnEntityMappingNewMod> returnEntityMappingNewMods = returnEntityMapNewModService.getDataByObject(valueMap, MethodConstants.GET_ENT_RETURN_MAPPING_MOD_HISTORY_BY_ENTITY_ID.getConstantVal());

			List<ReturnEntityMappingNewMod> responseObj = null;
			if (!CollectionUtils.isEmpty(returnEntityMappingNewMods)) {
				responseObj = prepareResponseObject(returnEntityMappingNewMods, dataListDto.getLangCode(), dataListDto.getDateFormat(), dataListDto.getCalendarFormat());
			}

			return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(responseObj).build();
		} catch (ApplicationException e) {
			logger.error("Request object not proper");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(e.getMessage()).build();
		} catch (Exception e) {
			logger.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessId + "Exception is" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ErrorCode.EC0033.toString()).build();
		}
	}

	private List<ReturnEntityMappingNewMod> prepareResponseObject(List<ReturnEntityMappingNewMod> returnEntityMappingNewMods, String langCode, String dateFormat, String calendarFormat) {

		List<String> strList = returnEntityMappingNewMods.stream().map(f -> f.getRetEntMapJsonData()).collect(Collectors.toList());

		Type listToken = new TypeToken<List<ReturnEntityMappingNew>>() {
		}.getType();

		List<ReturnEntityMappingNew> returnEntMapingNewList = null;
		Set<Long> returnIds = new HashSet<>();
		for (String string : strList) {
			returnEntMapingNewList = JsonUtility.getGsonObject().fromJson(string, listToken);
			returnIds.addAll(returnEntMapingNewList.stream().map(m -> m.getReturnObj().getReturnId()).collect(Collectors.toSet()));
		}

		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put(ColumnConstants.RETURN_ID_ARRAY.getConstantVal(), new ArrayList<>(returnIds));
		valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), true);
		valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), langCode);

		List<Return> dbReturnList = returnService.getDataByObject(valueMap, MethodConstants.GET_RETURN_LIST_WITH_LABEL.getConstantVal());

		List<ReturnEntityMappingNewMod> responseObj = new ArrayList<>();

		for (ReturnEntityMappingNewMod returnEntityMappingNewMod : returnEntityMappingNewMods) {
			ReturnEntityMappingNewMod returnEntityMappingNewMod2 = new ReturnEntityMappingNewMod();

			EntityBean entitybean = new EntityBean();
			entitybean.setEntityId(returnEntityMappingNewMod.getEntity().getEntityId());

			EntityLabelBean entityLabelBean = returnEntityMappingNewMod.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			entitybean.setEntityId(returnEntityMappingNewMod.getEntity().getEntityId());

			if (entityLabelBean != null) {
				entitybean.setEntityName(entityLabelBean.getEntityNameLabel());
			} else {
				entitybean.setEntityName(entitybean.getEntityName());
			}
			returnEntityMappingNewMod2.setEntity(entitybean);

			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(returnEntityMappingNewMod.getCreatedBy().getUserId());
			userMaster.setUserName(returnEntityMappingNewMod.getCreatedBy().getUserName());
			returnEntityMappingNewMod2.setCreatedBy(userMaster);

			returnEntMapingNewList = JsonUtility.getGsonObject().fromJson(returnEntityMappingNewMod.getRetEntMapJsonData(), listToken);

			for (ReturnEntityMappingNew returnEntityMappingNew : returnEntMapingNewList) {
				if (dbReturnList.indexOf(returnEntityMappingNew.getReturnObj()) != -1) {
					Return dbReturn = dbReturnList.get(dbReturnList.indexOf(returnEntityMappingNew.getReturnObj()));
					returnEntityMappingNew.getReturnObj().setReturnName(dbReturn.getReturnName());
				}
				try {
					if (returnEntityMappingNew.getCreatedOn() != null) {
						returnEntityMappingNew.setCreatedOnString(DateManip.formatAppDateTime(returnEntityMappingNew.getCreatedOn(), dateFormat, calendarFormat));
					}
				} catch (Exception e) {
					logger.error("Exception while parsing returnEntityMappingNew.getCreatedOn() date :", e);
				}

				try {
					if (returnEntityMappingNew.getModifiedOn() != null) {
						returnEntityMappingNew.setModifiedOnString(DateManip.formatAppDateTime(returnEntityMappingNew.getModifiedOn(), dateFormat, calendarFormat));
					}
				} catch (Exception e) {
					logger.error("Exception while parsing returnEntityMappingNew.getModifiedOn() date :", e);
				}
			}

			returnEntityMappingNewMod2.setRetEntMapJsonData(JsonUtility.getGsonObject().toJson(returnEntMapingNewList));
			returnEntityMappingNewMod2.setModule(returnEntityMappingNewMod.getModule());
			responseObj.add(returnEntityMappingNewMod2);
		}

		return responseObj;
	}

	@PostMapping(value = "/getEntityReturnChannelMappModHistory")
	public ServiceResponse getEntityReturnChannelModHistory(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {
		try {
			if (dataListDto.getEntityId() == null || dataListDto.getReturnId() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}
			Map<String, Object> valueMap = new HashMap<String, Object>();
			valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), dataListDto.getEntityId());
			valueMap.put(ColumnConstants.RETURNID.getConstantVal(), dataListDto.getReturnId());

			List<ReturnEntityMappingNew> returnEntityMapCurrentList = returnEntityMapServiceNew.getDataByObject(valueMap, MethodConstants.GET_DATA_BY_ENTITY_ID_AND_RETURN_ID.getConstantVal());
			JSONArray currentRecordArray = new JSONArray();
			for (ReturnEntityMappingNew returnEntityMappingNew : returnEntityMapCurrentList) {
				JSONObject currentRecordJsonObj = new JSONObject();
				Date date = returnEntityMappingNew.getModifiedOnChannel();
				if (returnEntityMappingNew.getModifiedOnChannel() != null) {
					currentRecordJsonObj.put("modifiedOn", DateManip.formatAppDateTime(returnEntityMappingNew.getModifiedOnChannel(), dataListDto.getDateFormat(), dataListDto.getCalendarFormat()));
				}
				if (returnEntityMappingNew.getModifiedbyFkChannel() != null) {
					currentRecordJsonObj.put("modifiedBy", returnEntityMappingNew.getModifiedbyFkChannel().getUserName());
				}
				currentRecordJsonObj.put("entityName", returnEntityMappingNew.getEntity().getEntityName());
				currentRecordJsonObj.put("returnName", returnEntityMappingNew.getReturnObj().getReturnName());
				if (returnEntityMappingNew.isWebChannel()) {
					currentRecordJsonObj.put("webChannel", "active");
				} else {
					currentRecordJsonObj.put("webChannel", "deactive");
				}

				if (returnEntityMappingNew.isUploadChannel()) {
					currentRecordJsonObj.put("uploadChannel", "active");
				} else {
					currentRecordJsonObj.put("uploadChannel", "deactive");
				}

				if (returnEntityMappingNew.isApiChannel()) {
					currentRecordJsonObj.put("apiChannel", "active");
				} else {
					currentRecordJsonObj.put("apiChannel", "deactive");
				}

				if (returnEntityMappingNew.isEmailChannel()) {
					currentRecordJsonObj.put("emailChannel", "active");
				} else {
					currentRecordJsonObj.put("emailChannel", "deactive");
				}

				if (returnEntityMappingNew.isStsChannel()) {
					currentRecordJsonObj.put("stsChannel", "active");
				} else {
					currentRecordJsonObj.put("stsChannel", "deactive");
				}
				currentRecordJsonObj.put("period", "current");
				currentRecordArray.put(currentRecordJsonObj);
			}

			List<ReturnEntityChannelMapModification> returnEntityMappingNewMods = returnEntityChannelMapModificationService.getDataByObject(valueMap, MethodConstants.GET_MOD_HIST_BY_ENTITY_ID_AND_RETURN_ID.getConstantVal());
			Boolean first = false;
			JSONObject previousRecordJsonObj = null;
			if (!CollectionUtils.isEmpty(returnEntityMappingNewMods)) {
				for (ReturnEntityChannelMapModification returnEntityChannelMapModification : returnEntityMappingNewMods) {

					String stringArr = returnEntityChannelMapModification.getEntReturnChannelMapJsonData();
					try {
						JSONObject objects = new JSONObject(stringArr.trim());
						JSONArray o = objects.names();
						previousRecordJsonObj = new JSONObject();
						for (int i = 0; i < o.length(); i++) {

							// iterate over the array
							if (!first) {
								if (returnEntityChannelMapModification.getModifiedByFk() != null) {
									previousRecordJsonObj.put("modifiedBy", returnEntityChannelMapModification.getModifiedByFk().getUserName());
								}
								//								Date date = returnEntityChannelMapModification.getModifiedOn();
								if (returnEntityChannelMapModification.getModifiedOn() != null) {
									previousRecordJsonObj.put("modifiedOn", DateManip.formatAppDateTime(returnEntityChannelMapModification.getModifiedOn(), dataListDto.getDateFormat(), dataListDto.getCalendarFormat()));
								}
								first = true;
							}
							previousRecordJsonObj.put("period", "previous");

							String keys = o.getString(i);
							String value = objects.getString(keys);

							if (keys.equalsIgnoreCase("webChannel")) {
								if (value.equalsIgnoreCase("true")) {
									previousRecordJsonObj.put("webChannel", "active");
								} else {
									previousRecordJsonObj.put("webChannel", "deactive");
								}
							}

							if (keys.equalsIgnoreCase("uploadChannel")) {
								if (value.equalsIgnoreCase("true")) {
									previousRecordJsonObj.put("uploadChannel", "active");
								} else {
									previousRecordJsonObj.put("uploadChannel", "deactive");
								}
							}

							if (keys.equalsIgnoreCase("apiChannel")) {
								if (value.equalsIgnoreCase("true")) {
									previousRecordJsonObj.put("apiChannel", "active");
								} else {
									previousRecordJsonObj.put("apiChannel", "deactive");
								}
							}

							if (keys.equalsIgnoreCase("emailChannel")) {
								if (value.equalsIgnoreCase("true")) {
									previousRecordJsonObj.put("emailChannel", "active");
								} else {
									previousRecordJsonObj.put("emailChannel", "deactive");
								}
							}

							if (keys.equalsIgnoreCase("stsChannel")) {
								if (value.equalsIgnoreCase("true")) {
									previousRecordJsonObj.put("stsChannel", "active");
								} else {
									previousRecordJsonObj.put("stsChannel", "deactive");
								}
							}
						}

						currentRecordArray.put(previousRecordJsonObj);

					} catch (Exception e) {
						logger.error("Exception :", e);
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ErrorCode.EC0033.toString()).build();
					}
				}
			}

			return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(currentRecordArray.toString()).build();
		} catch (Exception e) {
			logger.error("Exception in addEditReturnEntityMapping for JobProcessingId " + jobProcessId + "Exception is" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ErrorCode.EC0033.toString()).build();
		}
	}

	public void validateMethodForEntityReturnChannelMapping(DataListDto dataListDto) throws ApplicationException {
		if (ObjectUtils.isEmpty(dataListDto.getUserId())) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (dataListDto.getCateId() == null) {
			throw new ApplicationException(ErrorCode.E0004.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0004.toString()));
		} else if (dataListDto.getSubCatId() == null) {
			throw new ApplicationException(ErrorCode.E0009.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0009.toString()));
		} else if (dataListDto.getLangCode() == null) {
			dataListDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
		}
	}

}
