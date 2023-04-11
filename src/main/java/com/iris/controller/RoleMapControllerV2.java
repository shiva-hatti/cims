package com.iris.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.CategoryDto;
import com.iris.dto.DropDownGroup;
import com.iris.dto.DropDownObject;
import com.iris.dto.EditViewRoleDto;
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.LogedInUser;
import com.iris.dto.RegulatorDto;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.dto.SubCategoryDto;
import com.iris.dto.UserAndBrowserInfoInputBean;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserRoleDto;
import com.iris.dto.UserRoleMgmtBean;
import com.iris.dto.UserRoleModDetailsDto;
import com.iris.dto.UserRoleModJsonDto;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.ActivityApplicableMenu;
import com.iris.model.EntityBean;
import com.iris.model.Frequency;
import com.iris.model.LanguageMaster;
import com.iris.model.Menu;
import com.iris.model.MenuLabel;
import com.iris.model.MenuRoleMap;
import com.iris.model.Platform;
import com.iris.model.Regulator;
import com.iris.model.RegulatorLabel;
import com.iris.model.Return;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.model.RoleType;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.UserRoleActivityMap;
import com.iris.model.UserRoleEntityMapping;
import com.iris.model.UserRoleMaster;
import com.iris.model.UserRoleModified;
import com.iris.model.UserRolePlatFormMap;
import com.iris.model.UserRoleReturnMapping;
import com.iris.model.WebServiceComponentUrl;
import com.iris.model.WorkFlowActivity;
import com.iris.repository.ActivityApplicableMenuRepo;
import com.iris.repository.DeptUserEntityMappingRepo;
import com.iris.repository.MenuRepo;
import com.iris.repository.MenuRoleMapRepo;
import com.iris.repository.PortalRepo;
import com.iris.repository.ReturnRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.repository.UserRoleEntityMappingRepo;
import com.iris.repository.UserRoleModifiedRepo;
import com.iris.repository.UserRoleReturnMappingRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.DeptUserEntityMappingService;
import com.iris.service.impl.EntityService;
import com.iris.service.impl.LanguageMasterService;
import com.iris.service.impl.MenuRoleMapService;
import com.iris.service.impl.PortalRoleService;
import com.iris.service.impl.PortalService;
import com.iris.service.impl.RoleMapService;
import com.iris.service.impl.RoleTypeService;
import com.iris.service.impl.UserMasterService;
import com.iris.service.impl.UserRoleActivityMapService;
import com.iris.service.impl.UserRoleMasterService;
import com.iris.service.impl.UserRoleModifiedService;
import com.iris.service.impl.UserRoleReturnMappingService;
import com.iris.service.impl.UserRoleService;
import com.iris.util.AESV2;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

@RestController
@RequestMapping("/service/rolemap/V2")
public class RoleMapControllerV2 {
	private static final Logger Logger = LoggerFactory.getLogger(RoleMapControllerV2.class);
	private static final String SELECTED = "selected";

	@Autowired
	PlatFormController platFormController;

	@Autowired
	PortalRoleService portalRoleService;

	@Autowired
	RoleTypeController roleTypeController;

	@Autowired
	EntityMasterController entityMasterController;

	@Autowired
	ReturnGroupController returnGroupController;

	@Autowired
	private UserRoleModifiedService userRoleModifiedService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	PortalService portalService;

	@Autowired
	LanguageMasterService languageMasterService;

	@Autowired
	EntityService entityService;

	@Autowired
	RoleTypeService roleTypeService;

	@Autowired
	MenuRoleController menuRoleController;

	@Autowired
	MenuRoleMapService menuService;

	@Autowired
	UserRoleReturnMappingService userRoleReturnMappingService;

	@Autowired
	UserRoleReturnMappingRepo usserRoleReturnMappingRepo;

	@Autowired
	UserRoleEntityMappingRepo userRoleEntityMappingRepo;

	@Autowired
	RoleMapService roleMapService;

	@Autowired
	UserMasterService userMasterService;

	@Autowired
	WorkFlowActivityController workFlowActivityController;

	@Autowired
	ActivityApplicableMenuController activityApplicableMenuController;

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	UserRoleActivityMapService userRoleActivityMapService;

	@Autowired
	UserRoleModifiedRepo userRoleModifiedRepo;

	@Autowired
	private GenericService<Return, Long> returnService;

	@Autowired
	MenuRoleMapRepo menuRoleMapRepo;

	@Autowired
	UserRoleMasterService userRoleMasterService;

	@Autowired
	UserMasterRepo userMasterRepo;

	@Autowired
	DeptUserEntityMappingService deptUserEntityMappingService;

	@Autowired
	DeptUserEntityMappingRepo deptUserEntityMappingRepo;

	@Autowired
	ActivityApplicableMenuRepo activityApplicableMenuRepo;

	@Autowired
	MenuRepo menuRepo;

	@Autowired
	EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	ReturnGroupControllerV2 returnGroupControllerV2;

	@Autowired
	ReturnRepo returnRepo;

	@Autowired
	PortalRepo portalRepo;

	@PostMapping("/initUserRole/{rollId}/{userid}/{langCode}")
	@SuppressWarnings("unchecked")
	public ServiceResponse getRoleMapUseRoleV2(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollId") Long rollId, @PathVariable("userid") Long userId, @RequestBody UserRoleMgmtBean userRoleMgmtBean, @PathVariable("langCode") String localeCode) throws Exception {
		Logger.info("Requseting getRoleMap {}", jobProcessId);
		try {
			UserRoleMgmtBean responseData = new UserRoleMgmtBean();
			responseData.setCurrentLocale(userRoleMgmtBean.getCurrentLocale());
			//LanguageMaster localeCode = languageMasterService.getDataById(userRoleMgmtBean.getCurrentLocale());
			UserMaster userMaster = userMasterService.getDataById(userId);
			UserRole userRole = userRoleService.getDataById(rollId);
			if (userRole.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {

				// step 1: check if role id is RBI super user then prepare the bean and send
				// response
				if (userMaster.getDepartmentIdFk() != null) {
					responseData.setDepartment(userMaster.getDepartmentIdFk().getRegulatorId());
					if (Boolean.TRUE.equals(userMaster.getDepartmentIdFk().getIsMaster())) {
						responseData.setDeptAdminFlag(true);
					}
				}
				responseData.setDeptFlag(true);
			} else {
				responseData.setEntityFlag(true);
			}

			responseData.setUserId(userId);
			responseData.setRoleType(userMaster.getRoleType().getRoleTypeId());

			// List<UserRolePlatFormMap> poralRoleMap =
			// portalRoleService.getActiveDataFor(null, null);
			/*
			 * List<DropDownObject> portalRoleList =
			 * poralRoleMap.stream().filter(f->f.getPlatForm().getIsActive()&&f.getUserRole(
			 * ).getIsActive()).map(item->{ DropDownObject dto = new DropDownObject();
			 * UserRoleLabel dispaly =
			 * item.getUserRole().getUsrRoleLabelSet().stream().filter(label->label.
			 * getLangIdFk().getLanguageId().compareTo(localeCode.getLanguageId())==0).
			 * findFirst().orElse(null); dto.setDisplay(dispaly.getUserRoleLabel());
			 * dto.setKey(item.getUserRole().getUserRoleId()); return dto;
			 * }).collect(Collectors.toList()); if(portalRoleList!=null) {
			 * responseData.setPortalRoleList(portalRoleList); } else {
			 * responseData.setPortalRoleList(new ArrayList<>()); }
			 */
			ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			Collection<Platform> platformList = (Collection<Platform>) platFormController.getPortalMultipleRole(jobProcessId, userId).getResponse();

			List<Platform> newPlatformList = new ArrayList<>();

			for (Platform platform : platformList) {
				Platform newPlatForm = new Platform();
				newPlatForm.setPlatFormAppId(platform.getPlatFormAppId());
				newPlatForm.setPlatFormCode(platform.getPlatFormCode());
				newPlatForm.setPlatFormId(platform.getPlatFormId());
				newPlatForm.setPlatFormKey(platform.getPlatFormKey());
				newPlatForm.setPlatFormUrl(platform.getPlatFormUrl());
				newPlatformList.add(newPlatForm);
			}

			responseData.getPortalInputList().addAll(newPlatformList);
			// logic for role population

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ROLE_PLATFORM_LIST.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);
			responseData.setEntityCode(userRoleMgmtBean.getEntityCode());
			responseData.setUserName(AESV2.getInstance().encrypt(userMaster.getUserName()));
			headerMap.put(GeneralConstants.UUID.getConstantVal(), jobProcessId);
			responseData.setPlatFormCodeList(responseData.getPortalInputList().stream().map(item -> item.getPlatFormCode()).collect(Collectors.toList()));
			responseData.setLangCode(localeCode);
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, responseData, null, headerMap);
			// ServiceResponse serviceResponse = (ServiceResponse)
			// webServiceResponseReader.readServiceResponse(ServiceResponse.class,
			// responsestring, componentUrl.getUrlProduceType());
			ObjectMapper mapper = new ObjectMapper();

			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			String resp = JsonUtility.extractResponseValueFromServiceResponseString(responsestring);
			if (StringUtils.isNotBlank(resp)) {
				EditViewRoleDto res = mapper.readValue(resp, EditViewRoleDto.class);
				List<Long> excludeRoles = res.getUserRoleDtoList().stream().filter(item -> StringUtils.equalsIgnoreCase(item.getPlatformMasterDTO().getPlatFormCode(), "IFILE")).map(item -> item.getUserRoleId()).collect(Collectors.toList());
				List<DropDownObject> portalRoleList = res.getUserRoleDtoList().stream().filter(item -> !excludeRoles.contains(item.getUserRoleId()) && Boolean.TRUE.equals(item.getIsActive())).map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setDisplay(item.getRoleName());
					dto.setKey(item.getUserRoleId());
					return dto;
				}).collect(Collectors.toList());
				if (portalRoleList != null) {
					responseData.setPortalRoleList(portalRoleList);
				} else {
					responseData.setPortalRoleList(new ArrayList<>());
				}
			}
			responseData.setRoleTypeInputList((List<RoleType>) roleTypeController.getRoleTypeByUserId(jobProcessId, userId).getResponse());
			//responseData.setRoleTypeInputList(responseData.getRoleTypeInputList().stream().filter(item -> item.getRoleTypeId() != 4).collect(Collectors.toList()));

			// remove nbfc role type
			responseData.setRoleTypeInputList(responseData.getRoleTypeInputList().stream().filter(item -> item.getRoleTypeId() != 4 && item.getRoleTypeId() != 5).collect(Collectors.toList()));
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setActive(true);
			entityMasterDto.setRoleId(rollId);
			entityMasterDto.setUserId(userId);
			entityMasterDto.setIsCategoryWiseResponse(true);
			entityMasterDto.setLanguageCode(localeCode);
			List<CategoryDto> categoryList = (List<CategoryDto>) entityMasterControllerV2.getCategoryWiseEntityList(jobProcessId, entityMasterDto).getResponse();
			Logger.info("Entity fetch complete {}", jobProcessId);
			List<DropDownGroup> entityDropDown = new ArrayList<>();
			if (categoryList != null && !categoryList.isEmpty()) {
				for (CategoryDto item : categoryList) {
					if (item.getSubCategory() != null) {
						for (SubCategoryDto subCat : item.getSubCategory()) {
							DropDownGroup grp = new DropDownGroup();
							grp.setDisplay(subCat.getSubCategoryName());
							for (EntityDto ent : subCat.getEntityDtoList()) {
								DropDownObject dropItem = new DropDownObject();
								dropItem.setDisplay(ent.getEntityName());
								dropItem.setKey(ent.getEntityId());
								grp.getDropDownList().add(dropItem);
							}
							entityDropDown.add(grp);
						}
					}

				}
			}

			responseData.setEntityInputGroupList(entityDropDown);
			ReturnByRoleInputDto returnByRoleInputDto = new ReturnByRoleInputDto();
			returnByRoleInputDto.setIsActive(true);
			returnByRoleInputDto.setUserId(userId);
			returnByRoleInputDto.setLangCode(localeCode);
			returnByRoleInputDto.setRoleId(rollId);

			responseData.setReturnRoleList((List<RegulatorDto>) returnGroupControllerV2.getRegulatorAndReturnListByRole(jobProcessId, returnByRoleInputDto).getResponse());
			Logger.info("ReturnGroup fetch complete {}", jobProcessId);
			List<Long> userRoleIds = new ArrayList<>();
			Set<UserRoleMaster> usrRoleMstrSet = userMaster.getUsrRoleMstrSet();
			for (UserRoleMaster userRoleMastObj : usrRoleMstrSet) {
				if (userRoleMastObj.getIsActive()) {
					userRoleIds.add(userRoleMastObj.getUserRole().getUserRoleId());
				}
			}
			List<MenuRoleMap> menulist = (List<MenuRoleMap>) menuRoleController.getAllMenuUserRole(userRoleIds).getResponse();
			Map<Long, MenuRoleMap> menuRoleMap = new LinkedHashMap<>();
			for (MenuRoleMap menuRole : menulist) {
				menuRoleMap.put(menuRole.getMenuIDFk().getMenuId(), menuRole);
			}
			if (!CollectionUtils.isEmpty(menuRoleMap)) {
				menulist = new ArrayList<>();
				menulist.addAll(menuRoleMap.values());
			}
			Logger.info("Menu fetch complete {}", jobProcessId);
			List<MenuRoleMap> filterd;
			if (responseData.isDeptFlag()) {
				filterd = menulist.stream().filter(item -> item.getMenuIDFk().getIsDept() && item.getMenuIDFk().getIsActive()).collect(Collectors.toList());
			} else {
				filterd = menulist.stream().filter(item -> item.getMenuIDFk().getIsEntity() && item.getMenuIDFk().getIsActive()).collect(Collectors.toList());
			}

			List<DropDownObject> menuInList = new ArrayList<>();
			if (filterd != null) {
				menuInList = filterd.stream().map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setKey(item.getMenuIDFk().getMenuId());
					item.getMenuIDFk().getMenuLabelSet().forEach(inner -> {
						if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
							dto.setDisplay(inner.getMenuLabel());
						}
					});

					return dto;
				}).collect(Collectors.toList());

				menuInList.sort((DropDownObject a1, DropDownObject a2) -> {
					if (!StringUtils.isBlank(a1.getDisplay()) && !StringUtils.isBlank(a2.getDisplay())) {
						return a1.getDisplay().compareTo(a2.getDisplay());
					} else {
						return a1.getKey().compareTo(a2.getKey());
					}
				});
			}
			responseData.setMenuInputList(menuInList);

			// prepare for Workflow Activity
			responseData.setRoleIdExisting(userRoleMgmtBean.getRoleIdExisting());
			prepareForWorkFlowActivity(responseData);
			if (userRoleMgmtBean.getRoleIdExisting() != null) {
				List<UserRoleEntityMapping> roleentityList = userRoleEntityMappingRepo.findByUserRoleUserRoleIdAndIsActiveTrue(userRoleMgmtBean.getRoleIdExisting());
				Logger.info("Selected Entity fetch complete {} ", jobProcessId);
				List<DropDownObject> entityoutList = roleentityList.stream().map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setKey(item.getEntity().getEntityId());
					return dto;
				}).collect(Collectors.toList());
				responseData.setEntityOutList(entityoutList);
				Logger.info(" Selected Entity Set complete {}", jobProcessId);
				ReturnGroupMappingRequest returnGroupMappingRequestAlloted = new ReturnGroupMappingRequest();
				returnGroupMappingRequestAlloted.setIsActive(true);
				returnGroupMappingRequestAlloted.setUserId(userId);
				returnGroupMappingRequestAlloted.setLangId(15l);
				returnGroupMappingRequestAlloted.setRoleId(userRoleMgmtBean.getRoleIdExisting());

				Map<String, Object> columnValueMap = new HashMap<>();
				columnValueMap.put(ColumnConstants.ROLEID.getConstantVal(), userRoleMgmtBean.getRoleIdExisting());
				List<UserRoleReturnMapping> roleReturnMap = userRoleReturnMappingService.getDataByObject(columnValueMap, "getUserRoleReturnMappingByUserId");
				if (roleReturnMap != null && responseData.getReturnRoleList() != null) {
					responseData.getReturnRoleList().forEach(item -> {
						item.getReturnList().forEach(returnItem -> {
							roleReturnMap.forEach(outItem -> {
								if (returnItem.getReturnCode().equals(outItem.getReturnIdFk().getReturnCode())) {
									returnItem.setSelected(SELECTED);
								}
							});
						});

					});
				}
				Logger.info("Selected returnGroup fetch complete {} ", jobProcessId);
				List<MenuRoleMap> menuOutList = (List<MenuRoleMap>) menuRoleController.getAllMenu(jobProcessId, userRoleMgmtBean.getRoleIdExisting()).getResponse();
				Logger.info(" Selected Menu fetch complete {}", jobProcessId);
				List<DropDownObject> menuoutDropList = menuOutList.stream().map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setKey(item.getMenuIDFk().getMenuId());
					item.getMenuIDFk().getMenuLabelSet().forEach(inner -> {
						if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
							dto.setDisplay(inner.getMenuLabel());
						}
					});
					return dto;
				}).collect(Collectors.toList());
				responseData.setMenuOutList(menuoutDropList);
				Logger.info(" Selected Menu Set complete {}", jobProcessId);
				UserRole role = userRoleService.getDataById(userRoleMgmtBean.getRoleIdExisting());
				responseData.setRoleDesc(role.getRoleDesc());
				responseData.setRollName(role.getRoleName());
				responseData.setDeptAdmin(role.getDeptAdmin());
				Long isActive = Boolean.TRUE.equals(role.getIsActive()) ? 1l : 0l;
				responseData.setIsActive(isActive);
				responseData.setRoleType(role.getRoleType().getRoleTypeId());
				if (role.getUserRolePlatFormMap() != null) {
					Optional<UserRolePlatFormMap> portaMap = role.getUserRolePlatFormMap().stream().filter(item -> org.apache.commons.lang.StringUtils.equals(item.getPlatForm().getPlatFormCode(), userRoleMgmtBean.getPortalCode())).findFirst();
					if (portaMap.isPresent()) {
						responseData.setPortal(portaMap.get().getPlatForm().getPlatFormId());
					}
				}
				/*
				 * List<UserRoleLabel> hindiLabel =
				 * role.getUsrRoleLabelSet().stream().filter(item ->
				 * StringUtils.equals(item.getLangIdFk().getLanguageCode(),
				 * "hi")).collect(Collectors.toList()); if (hindiLabel != null &&
				 * !hindiLabel.isEmpty()) {
				 * responseData.setRollNameHindi(hindiLabel.get(0).getUserRoleLabel()); }
				 */
			}
			// super admin menu list
			if (responseData.isDeptAdminFlag() || userRole.getUserRoleId().equals(GeneralConstants.RBI_SUPER_USER.getConstantLongVal())) {
				List<Menu> deptMenu = menuService.getMenuForDept(); /*menulist.stream()
																	.filter(item -> item.getMenuIDFk().getIsDept() && item.getMenuIDFk().getIsActive())
																	.collect(Collectors.toList());*/
				StringBuilder deptMenuOption = new StringBuilder();
				if (deptMenu != null && !deptMenu.isEmpty()) {
					deptMenu.sort((Menu a1, Menu a2) -> {
						for (MenuLabel inner : a1.getMenuLabelSet()) {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								for (MenuLabel inner2 : a2.getMenuLabelSet()) {
									if (inner2.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
										return inner.getMenuLabel().compareTo(inner2.getMenuLabel());
									}
								}
							}
						}
						return a1.getMenuDesc().compareTo(a2.getMenuDesc());
					});
					for (Menu item : deptMenu) {
						item.getMenuLabelSet().forEach(inner -> {
							String selected = StringUtils.EMPTY;
							for (DropDownObject menuOut : responseData.getMenuOutList()) {
								if (menuOut.getKey().compareTo(item.getMenuId()) == 0) {
									selected = SELECTED;
								}

							}
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								deptMenuOption.append("<option value='" + item.getMenuId() + "'" + selected + " >" + inner.getMenuLabel() + "</option>");
							}
						});

					}
				}
				List<Menu> entityMenu = menuService.getMenuForEntity();
				StringBuilder entMenuOption = new StringBuilder();
				if (entityMenu != null && !entityMenu.isEmpty()) {
					entityMenu.sort((Menu a1, Menu a2) -> {
						for (MenuLabel inner : a1.getMenuLabelSet()) {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								for (MenuLabel inner2 : a2.getMenuLabelSet()) {
									if (inner2.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
										return inner.getMenuLabel().compareTo(inner2.getMenuLabel());
									}
								}
							}
						}
						return a1.getMenuDesc().compareTo(a2.getMenuDesc());
					});

					for (Menu item : entityMenu) {
						item.getMenuLabelSet().forEach(inner -> {
							String selected = StringUtils.EMPTY;
							for (DropDownObject menuOut : responseData.getMenuOutList()) {
								if (menuOut.getKey().compareTo(item.getMenuId()) == 0) {
									selected = SELECTED;
								}

							}
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								entMenuOption.append("<option value='" + item.getMenuId() + "'" + selected + " >" + inner.getMenuLabel() + "</option>");
							}
						});

					}
				}

				responseData.setDeptMenuOption(deptMenuOption.toString());
				responseData.setEntMenuOption(entMenuOption.toString());
				responseData.setAutMenuOption(getAuditorMenu(responseData));
			}
			/*
			 * List<LanguageMaster> languegeList =
			 * languageMasterService.getAllActiveLanguage(); List<DropDownObject>
			 * languageInDropDown = languegeList.stream().map(item -> { DropDownObject dto =
			 * new DropDownObject(); dto.setKey(item.getLanguageId());
			 * dto.setDisplay(item.getLanguageName()); return dto;
			 * }).collect(Collectors.toList());
			 * responseData.setLanguageInDropDown(languageInDropDown);
			 */
			response.setResponse(responseData);
			Logger.info("init complete {}", jobProcessId);
			return response;
		} catch (Exception e) {
			Logger.error("Exception while getRoleMap request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private WebServiceComponentUrl getWebServiceComponentURL(String componentName, String methodType) {
		Map<String, List<String>> valueMap = new HashMap<>();

		List<String> valueList = new ArrayList<>();
		valueList.add(componentName);
		valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(methodType);
		valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

		WebServiceComponentUrl componentUrl = null;
		try {
			componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			Logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

	/**
	 * Prepares for WorkFlow Activity
	 * 
	 * @param responseData
	 */
	@SuppressWarnings("unchecked")
	private void prepareForWorkFlowActivity(UserRoleMgmtBean responseData) {
		List<WorkFlowActivity> deptDropDownAct = (List<WorkFlowActivity>) workFlowActivityController.getWorkFlowActivity(true).getResponse();
		List<DropDownObject> deptDropActivity = deptDropDownAct.stream().map(item -> {
			DropDownObject dto = new DropDownObject();
			dto.setDisplay(item.getActivityDesc());
			dto.setKey(item.getActivityId());
			return dto;
		}).collect(Collectors.toList());
		responseData.setActivityDeptInDropDown(deptDropActivity);
		List<WorkFlowActivity> entDropDownAct = (List<WorkFlowActivity>) workFlowActivityController.getWorkFlowActivity(false).getResponse();

		List<DropDownObject> entDropActivity = entDropDownAct.stream().map(item -> {
			DropDownObject dto = new DropDownObject();
			dto.setDisplay(item.getActivityDesc());
			dto.setKey(item.getActivityId());
			return dto;
		}).collect(Collectors.toList());
		responseData.setActivityEntInDropDown(entDropActivity);
		List<ActivityApplicableMenu> aplicableDept = (List<ActivityApplicableMenu>) activityApplicableMenuController.getActivityApplicableMenu(true).getResponse();
		List<Long> applicableDeptList = aplicableDept.stream().map(item -> {
			return item.getMenuIdFk();
		}).collect(Collectors.toList());
		responseData.setApprvRejectActivityDept(applicableDeptList);
		List<ActivityApplicableMenu> aplicableEnt = (List<ActivityApplicableMenu>) activityApplicableMenuController.getActivityApplicableMenu(false).getResponse();
		List<Long> applicableEntList = aplicableEnt.stream().map(item -> {
			if (!item.getActivityIdFk().getActivityId().equals(4l)) {
				return item.getMenuIdFk();
			}
			return null;

		}).collect(Collectors.toList());
		responseData.setApprvRejectActivityEnt(applicableEntList);
		List<Long> applicablUploadList = aplicableEnt.stream().map(item -> {
			if (item.getActivityIdFk().getActivityId().equals(4l) || item.getActivityIdFk().getActivityId().equals(5l)) {
				return item.getMenuIdFk();
			}
			return null;

		}).collect(Collectors.toList());
		responseData.setUploadActivity(applicablUploadList);
		if (responseData.getRoleIdExisting() != null) {
			List<UserRoleActivityMap> userRoleActivity = userRoleActivityMapService.findByRoleUserRoleId(responseData.getRoleIdExisting());
			if (userRoleActivity != null) {
				userRoleActivity.forEach(item -> {
					responseData.getActivityDeptInDropDown().forEach(dropitem -> {
						if (dropitem.getKey().equals(item.getWorkFlowActivity().getActivityId())) {
							dropitem.setSelected(SELECTED);
						}
					});

					responseData.getActivityEntInDropDown().forEach(dropitem -> {
						if (dropitem.getKey().equals(item.getWorkFlowActivity().getActivityId())) {
							dropitem.setSelected(SELECTED);
						}
					});

				});
			}
		}
	}

	/**
	 * Prepares Menu option for Auditor
	 * 
	 * @param responseData
	 * @return
	 */
	private String getAuditorMenu(UserRoleMgmtBean responseData) {
		List<Menu> audMenu = menuService.getMenuForAuditor();
		StringBuilder autMenuOption = new StringBuilder();
		if (audMenu != null && !audMenu.isEmpty()) {
			audMenu.sort((Menu a1, Menu a2) -> {
				for (MenuLabel inner : a1.getMenuLabelSet()) {
					if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
						for (MenuLabel inner2 : a2.getMenuLabelSet()) {
							if (inner2.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								return inner.getMenuLabel().compareTo(inner2.getMenuLabel());
							}
						}
					}
				}
				return a1.getMenuDesc().compareTo(a2.getMenuDesc());
			});

			for (Menu item : audMenu) {
				item.getMenuLabelSet().forEach(inner -> {
					String selected = StringUtils.EMPTY;
					for (DropDownObject menuOut : responseData.getMenuOutList()) {
						if (menuOut.getKey().compareTo(item.getMenuId()) == 0) {
							selected = SELECTED;
						}

					}
					if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
						autMenuOption.append("<option value='" + item.getMenuId() + "'" + selected + " >" + inner.getMenuLabel() + "</option>");
					}
				});

			}
		}
		return autMenuOption.toString();

	}

	@PostMapping("/initRollModified/{rollModifiedId}/{userid}")
	@SuppressWarnings("unchecked")
	public ServiceResponse getRoleMapModifiedV2(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollModifiedId") Long rollId, @PathVariable("userid") Long userId, @RequestBody UserRoleMgmtBean userRoleMgmtBean) throws Exception {
		Logger.info("Requseting getRoleMapModified{}", jobProcessId);
		try {
			UserRoleMgmtBean responseData = new UserRoleMgmtBean();
			responseData.setCurrentLocale(userRoleMgmtBean.getCurrentLocale());
			LanguageMaster localeCode = languageMasterService.getDataById(userRoleMgmtBean.getCurrentLocale());
			UserMaster userMaster = userMasterService.getDataById(userId);
			// UserRole userRole = userRoleService.getDataById(rollId);
			UserRoleModified userRole = userRoleModifiedService.getDataById(rollId);
			Gson gson = new Gson();
			UserRoleModJsonDto userRoleJson = gson.fromJson(userRole.getRoleDetailsJson(), UserRoleModJsonDto.class);

			if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
				// step 1: check if role id is RBI super user then prepare the bean and send
				// response
				if (userMaster.getDepartmentIdFk() != null) {
					responseData.setDepartment(userMaster.getDepartmentIdFk().getRegulatorId());
					if (Boolean.TRUE.equals(userMaster.getDepartmentIdFk().getIsMaster())) {
						responseData.setDeptAdminFlag(true);
					}
				}
				responseData.setDeptFlag(true);
			} else {
				responseData.setEntityFlag(true);
			}

			responseData.setUserId(userId);
			responseData.setRoleType(userRoleJson.getRoleTypeId());

			ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();

			// responseData.getPortalInputList().addAll((Collection<Platform>)
			// platFormController.getAllPortal(jobProcessId,userId,rollId).getResponse());
			responseData.getPortalInputList().addAll((Collection<Platform>) platFormController.getAllPortal(jobProcessId).getResponse());
			// logic for role population

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ROLE_PLATFORM_LIST.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);
			responseData.setEntityCode(userRoleMgmtBean.getEntityCode());
			responseData.setUserName(AESV2.getInstance().encrypt(userMaster.getUserName()));
			headerMap.put(GeneralConstants.UUID.getConstantVal(), jobProcessId);
			responseData.setPlatFormCodeList(responseData.getPortalInputList().stream().filter(item -> item.getPlatFormId().equals(userRoleJson.getPortalId())).map(item -> item.getPlatFormCode()).collect(Collectors.toList()));
			responseData.setLangCode(localeCode.getLanguageCode());
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, responseData, null, headerMap);
			// ServiceResponse serviceResponse = (ServiceResponse)
			// webServiceResponseReader.readServiceResponse(ServiceResponse.class,
			// responsestring, componentUrl.getUrlProduceType());
			ObjectMapper mapper = new ObjectMapper();

			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			String resp = JsonUtility.extractResponseValueFromServiceResponseString(responsestring);
			if (StringUtils.isNotBlank(resp)) {
				EditViewRoleDto res = mapper.readValue(resp, EditViewRoleDto.class);
				List<Long> excludeRoles = res.getUserRoleDtoList().stream().filter(item -> StringUtils.equalsIgnoreCase(item.getPlatformMasterDTO().getPlatFormCode(), "IFILE")).map(item -> item.getUserRoleId()).collect(Collectors.toList());
				List<DropDownObject> portalRoleList = res.getUserRoleDtoList().stream().filter(item -> !excludeRoles.contains(item.getUserRoleId()) && Boolean.TRUE.equals(item.getIsActive())).map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setDisplay(item.getRoleName());
					dto.setKey(item.getUserRoleId());
					return dto;
				}).collect(Collectors.toList());
				if (portalRoleList != null) {
					responseData.setPortalRoleList(portalRoleList);
				} else {
					responseData.setPortalRoleList(new ArrayList<>());
				}
			}
			responseData.setRoleTypeInputList((List<RoleType>) roleTypeController.getRoleTypeByUserId(jobProcessId, userId).getResponse());
			// remove nbfc role type
			responseData.setRoleTypeInputList(responseData.getRoleTypeInputList().stream().filter(item -> item.getRoleTypeId() != 4).collect(Collectors.toList()));
			//		EntityMasterDto entityMasterDto = new EntityMasterDto();
			//		entityMasterDto.setActive(true);
			//		entityMasterDto.setRoleId(rollId);
			//		entityMasterDto.setUserId(userId);
			//		entityMasterDto.setIsCategoryWiseResponse(true);
			//		entityMasterDto.setLanguageCode(localeCode.getLanguageCode());
			//		List<Category> categoryList = (List<Category>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
			List<EntityBean> entityList = new ArrayList<>();
			if (!ArrayUtils.isEmpty(userRoleJson.getSelectedEntity()) && !userRoleJson.getSelectedEntity()[0].equals("null") && !userRoleJson.getSelectedEntity()[0].equals("") && !userRoleJson.getSelectedEntity()[0].equals("[]")) {
				List<String> entityIds = Arrays.asList(userRoleJson.getSelectedEntity());
				List<Long> longEntityIds = entityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				Long longArray[] = longEntityIds.toArray(new Long[0]);
				entityList = entityService.getDataByIds(longArray);
			}
			Logger.info("Entity fetch complete {}", jobProcessId);
			List<DropDownGroup> entityDropDown = new ArrayList<>();
			Map<String, DropDownGroup> entityMap = new HashMap<>();
			if (entityList != null && !entityList.isEmpty()) {
				for (EntityBean ent : entityList) {
					Optional<SubCategoryLabel> subCategoryLabel = ent.getSubCategory().getSubCatLblSet().stream().filter(i -> i.getLangIdFk().getLanguageCode().equals(localeCode.getLanguageCode())).findAny();
					if (subCategoryLabel.isPresent()) {
						if (entityMap.containsKey(subCategoryLabel.get().getSubCategoryLabel())) {
							DropDownObject obj = new DropDownObject();
							obj.setDisplay(ent.getEntityName());
							obj.setKey(ent.getEntityId());
							DropDownGroup grp = entityMap.get(subCategoryLabel.get().getSubCategoryLabel());
							grp.getDropDownList().add(obj);
							entityMap.put(subCategoryLabel.get().getSubCategoryLabel(), grp);
						} else {
							DropDownObject obj = new DropDownObject();
							obj.setDisplay(ent.getEntityName());
							obj.setKey(ent.getEntityId());
							DropDownGroup grp = new DropDownGroup();
							grp.setDisplay(subCategoryLabel.get().getSubCategoryLabel());
							grp.getDropDownList().add(obj);
							entityMap.put(subCategoryLabel.get().getSubCategoryLabel(), grp);
						}
					}
				}
				for (Map.Entry<String, DropDownGroup> entityMapValue : entityMap.entrySet()) {
					entityDropDown.add(entityMapValue.getValue());
				}
			}
			responseData.setEntityInputGroupList(entityDropDown);
			//		ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			//		returnGroupMappingRequest.setIsActive(true);
			//		returnGroupMappingRequest.setUserId(userId);
			//		returnGroupMappingRequest.setLangId(localeCode.getLanguageId());
			//		returnGroupMappingRequest.setRoleId(rollId);

			//		responseData.setReturnInputList((List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse());
			List<RegulatorDto> regulatorDtos = new ArrayList<>();
			if (!ArrayUtils.isEmpty(userRoleJson.getSelectedReturn()) && !userRoleJson.getSelectedReturn()[0].equals("null") && !userRoleJson.getSelectedReturn()[0].equals("") && !userRoleJson.getSelectedReturn()[0].equals("[]")) {
				List<String> returnCodeList = Arrays.asList(userRoleJson.getSelectedReturn());
				//List<Long> longReturnIds = returnIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				//Long longArray[] = longReturnIds.toArray(new Long[0]);

				List<Return> returnList = returnRepo.getDataByReturnCodeIn(returnCodeList);

				for (Return x : returnList) {

					RegulatorDto regulatorDto = new RegulatorDto();

					if (!CollectionUtils.isEmpty(x.getReturnRegulatorMapping())) {
						ReturnRegulatorMapping returnRegulatorMapping = x.getReturnRegulatorMapping().stream().filter(f -> f.getIsActive()).findAny().orElse(null);
						if (returnRegulatorMapping != null && returnRegulatorMapping.getReturnIdFk().getIsActive() && returnRegulatorMapping.getRegulatorIdFk().getIsActive()) {
							regulatorDto.setRegulatorCode(returnRegulatorMapping.getRegulatorIdFk().getRegulatorCode());

							RegulatorLabel regulatorlabel = returnRegulatorMapping.getRegulatorIdFk().getRegulatorLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equals(localeCode.getLanguageCode())).findAny().orElse(null);
							if (regulatorlabel != null) {
								regulatorDto.setRegulatorName(regulatorlabel.getRegulatorLabel());
							} else {
								regulatorDto.setRegulatorName(returnRegulatorMapping.getRegulatorIdFk().getRegulatorName());
							}

						}
					}

					ReturnDto returnDto = new ReturnDto();
					returnDto.setReturnCode(x.getReturnCode());
					returnDto.setReturnName(x.getReturnName());

					Frequency frequency = new Frequency();
					frequency.setFrequencyCode(x.getFrequency().getFrequencyCode());
					frequency.setFrequencyName(x.getFrequency().getFreqLbl());

					returnDto.setFrequency(frequency);

					if (regulatorDtos.indexOf(regulatorDto) != -1) {
						regulatorDto = regulatorDtos.get(regulatorDtos.indexOf(regulatorDto));
						regulatorDto.getReturnList().add(returnDto);
						if (!CollectionUtils.isEmpty(regulatorDto.getReturnList())) {
							regulatorDto.setReturnCount(regulatorDto.getReturnList().size());
						}
					} else {
						List<ReturnDto> returnDtoList = new ArrayList<>();
						returnDtoList.add(returnDto);
						regulatorDto.setReturnList(returnDtoList);
						if (!CollectionUtils.isEmpty(regulatorDto.getReturnList())) {
							regulatorDto.setReturnCount(regulatorDto.getReturnList().size());
						}
						regulatorDtos.add(regulatorDto);
					}
				}
			}
			responseData.setReturnRoleList(regulatorDtos);
			Logger.info("ReturnGroup fetch complete {}", jobProcessId);

			List<Menu> filterd = new ArrayList<>();
			List<DropDownObject> menuInList = new ArrayList<>();
			if (!ArrayUtils.isEmpty(userRoleJson.getSelectedMenu()) && !userRoleJson.getSelectedMenu()[0].equals("null") && !userRoleJson.getSelectedMenu()[0].equals("") && !userRoleJson.getSelectedMenu()[0].equals("[]")) {
				//			List<MenuRoleMap> menulist =(List<MenuRoleMap>) menuRoleController.getAllMenu(jobProcessId, rollId).getResponse();
				List<String> stringIds = Arrays.asList(userRoleJson.getSelectedMenu());
				List<Long> longIds = stringIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				List<Menu> menulist = menuService.getMenuFromJsonIds(longIds);
				Logger.info("Menu fetch complete {}", jobProcessId);

				//			if(responseData.isDeptFlag()) {
				//				filterd = menulist.stream().filter(item->item.getIsActive()).collect(Collectors.toList());
				//			}else {
				//				filterd = menulist.stream().filter(item->item.getMenuIDFk().getIsEntity()&&item.getMenuIDFk().getIsActive()).collect(Collectors.toList());
				//			}
				filterd = menulist.stream().filter(item -> item.getIsActive()).collect(Collectors.toList());
				if (filterd != null) {
					menuInList = filterd.stream().map(item -> {
						DropDownObject dto = new DropDownObject();
						dto.setKey(item.getMenuId());
						item.getMenuLabelSet().forEach(inner -> {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								dto.setDisplay(inner.getMenuLabel());
							}
						});

						return dto;
					}).collect(Collectors.toList());

					menuInList.sort((DropDownObject a1, DropDownObject a2) -> {
						if (!StringUtils.isBlank(a1.getDisplay()) && !StringUtils.isBlank(a2.getDisplay())) {
							return a1.getDisplay().compareTo(a2.getDisplay());
						} else {
							return a1.getKey().compareTo(a2.getKey());
						}
					});
				}
			}
			responseData.setMenuInputList(menuInList);

			// prepare for Workflow Activity
			responseData.setRoleIdExisting(userRoleMgmtBean.getRoleIdExisting());
			prepareForWorkFlowActivityModified(responseData, userRoleJson);
			if (userRoleMgmtBean.getRoleIdExisting() != null) {
				List<UserRoleEntityMapping> roleentityList = userRoleEntityMappingRepo.findByUserRoleUserRoleIdAndIsActiveTrue(userRoleMgmtBean.getRoleIdExisting());
				Logger.info("Selected Entity fetch complete {} ", jobProcessId);
				List<DropDownObject> entityoutList = roleentityList.stream().map(item -> {
					DropDownObject dto = new DropDownObject();
					dto.setKey(item.getEntity().getEntityId());
					return dto;
				}).collect(Collectors.toList());
				responseData.setEntityOutList(entityoutList);
				Logger.info(" Selected Entity Set complete {}", jobProcessId);
				ReturnGroupMappingRequest returnGroupMappingRequestAlloted = new ReturnGroupMappingRequest();
				returnGroupMappingRequestAlloted.setIsActive(true);
				returnGroupMappingRequestAlloted.setUserId(userId);
				returnGroupMappingRequestAlloted.setLangId(15l);
				returnGroupMappingRequestAlloted.setRoleId(userRoleMgmtBean.getRoleIdExisting());

				Map<String, Object> columnValueMap = new HashMap<>();
				columnValueMap.put(ColumnConstants.ROLEID.getConstantVal(), userRoleMgmtBean.getRoleIdExisting());
				List<UserRoleReturnMapping> roleReturnMap = userRoleReturnMappingService.getDataByObject(columnValueMap, "getUserRoleReturnMappingByUserId");
				if (roleReturnMap != null && responseData.getReturnRoleList() != null) {
					responseData.getReturnRoleList().forEach(item -> {
						item.getReturnList().forEach(returnItem -> {
							roleReturnMap.forEach(outItem -> {
								if (returnItem.getReturnCode().equals(outItem.getReturnIdFk().getReturnCode())) {
									returnItem.setSelected(SELECTED);
								}
							});
						});

					});
				}
				Logger.info("Selected returnGroup fetch complete {} ", jobProcessId);

				List<Menu> menulist1;
				List<DropDownObject> menuoutDropList = new ArrayList<>();
				if (userRoleJson.getSelectedMenu() != null) {
					// List<MenuRoleMap> menuOutList = (List<MenuRoleMap>)
					// menuRoleController.getAllMenu(jobProcessId,
					// userRoleMgmtBean.getRoleIdExisting()).getResponse();
					List<String> stringIds = Arrays.asList(userRoleJson.getSelectedMenu());
					List<Long> longIds = stringIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
					menulist1 = menuService.getMenuFromJsonIds(longIds);
					Logger.info(" Selected Menu fetch complete {}", jobProcessId);
					menuoutDropList = menulist1.stream().map(item -> {
						DropDownObject dto = new DropDownObject();
						dto.setKey(item.getMenuId());
						item.getMenuLabelSet().forEach(inner -> {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								dto.setDisplay(inner.getMenuLabel());
							}
						});
						return dto;
					}).collect(Collectors.toList());
				}

				responseData.setMenuOutList(menuoutDropList);
				Logger.info(" Selected Menu Set complete {}", jobProcessId);
				// UserRole role =
				// userRoleService.getDataById(userRoleMgmtBean.getRoleIdExisting());
				responseData.setRoleDesc(userRoleJson.getRoleDesc());
				responseData.setRollName(userRoleJson.getRoleName());

				/*
				 * if (userRoleJson.getRoleNameDto() != null) { Optional<RoleNameDto> portaMap =
				 * userRoleJson.getRoleNameDto().stream() .filter(item ->
				 * item.getLangCode().equals(localeCode.getLanguageCode())).findFirst(); if
				 * (portaMap.isPresent()) {
				 * responseData.setRollName(portaMap.get().getRoleName()); } }
				 */
				responseData.setDeptAdmin(userRoleJson.getDeptAdmin());
				Long isActive = Boolean.TRUE.equals(userRoleJson.getIsActive()) ? 1l : 0l;
				responseData.setIsActive(isActive);
				responseData.setRoleType(userRoleJson.getRoleTypeId());
				responseData.setPortal(userRoleJson.getPortalId());
				// List<UserRoleLabel> hindiLabel =
				// role.getUsrRoleLabelSet().stream().filter(item->StringUtils.equals(item.getLangIdFk().getLanguageCode(),"hi")).collect(Collectors.toList());
				//			if(hindiLabel!=null && !hindiLabel.isEmpty()) {
				//				responseData.setRollNameHindi(hindiLabel.get(0).getUserRoleLabel());
				//			}
				/*
				 * if (userRoleJson.getRoleNameDto() != null) { Optional<RoleNameDto> portaMap =
				 * userRoleJson.getRoleNameDto().stream() .filter(item ->
				 * item.getLangCode().equals("hi")).findFirst(); if (portaMap.isPresent()) {
				 * responseData.setRollNameHindi(portaMap.get().getRoleName()); } }
				 */
			}
			// super admin menu list
			if (responseData.isDeptAdminFlag()) {
				List<Menu> deptMenu = filterd.stream().filter(item -> item.getIsDept() && item.getIsActive()).collect(Collectors.toList());
				StringBuilder deptMenuOption = new StringBuilder();
				if (deptMenu != null && !deptMenu.isEmpty()) {
					deptMenu.sort((Menu a1, Menu a2) -> {
						for (MenuLabel inner : a1.getMenuLabelSet()) {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								for (MenuLabel inner2 : a2.getMenuLabelSet()) {
									if (inner2.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
										return inner.getMenuLabel().compareTo(inner2.getMenuLabel());
									}
								}
							}
						}
						return a1.getMenuDesc().compareTo(a2.getMenuDesc());
					});
					for (Menu item : deptMenu) {
						item.getMenuLabelSet().forEach(inner -> {
							String selected = StringUtils.EMPTY;
							for (DropDownObject menuOut : responseData.getMenuOutList()) {
								if (menuOut.getKey().compareTo(item.getMenuId()) == 0) {
									selected = SELECTED;
								}

							}
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								deptMenuOption.append("<option value='" + item.getMenuId() + "'" + selected + " >" + inner.getMenuLabel() + "</option>");
							}
						});

					}
				}
				List<Menu> entityMenu = menuService.getMenuForEntity();
				StringBuilder entMenuOption = new StringBuilder();
				if (entityMenu != null && !entityMenu.isEmpty()) {
					entityMenu.sort((Menu a1, Menu a2) -> {
						for (MenuLabel inner : a1.getMenuLabelSet()) {
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								for (MenuLabel inner2 : a2.getMenuLabelSet()) {
									if (inner2.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
										return inner.getMenuLabel().compareTo(inner2.getMenuLabel());
									}
								}
							}
						}
						return a1.getMenuDesc().compareTo(a2.getMenuDesc());
					});

					for (Menu item : entityMenu) {
						item.getMenuLabelSet().forEach(inner -> {
							String selected = StringUtils.EMPTY;
							for (DropDownObject menuOut : responseData.getMenuOutList()) {
								if (menuOut.getKey().compareTo(item.getMenuId()) == 0) {
									selected = SELECTED;
								}

							}
							if (inner.getLanguageIdFk().getLanguageId().equals(responseData.getCurrentLocale())) {
								entMenuOption.append("<option value='" + item.getMenuId() + "'" + selected + " >" + inner.getMenuLabel() + "</option>");
							}
						});

					}
				}

				responseData.setDeptMenuOption(deptMenuOption.toString());
				responseData.setEntMenuOption(entMenuOption.toString());
				responseData.setAutMenuOption(getAuditorMenu(responseData));
			}
			/*
			 * List<LanguageMaster> languegeList =
			 * languageMasterService.getAllActiveLanguage(); List<DropDownObject>
			 * languageInDropDown = languegeList.stream().map(item -> { DropDownObject dto =
			 * new DropDownObject(); dto.setKey(item.getLanguageId());
			 * dto.setDisplay(item.getLanguageName()); return dto;
			 * }).collect(Collectors.toList());
			 */
			responseData.setCreatedByRoleType(userRole.getCreatedBy().getRoleType().getRoleTypeId());
			//responseData.setLanguageInDropDown(languageInDropDown);
			response.setResponse(responseData);
			Logger.info("init complete {}", jobProcessId);
			return response;
		} catch (Exception e) {
			Logger.error("Exception while getRoleMapModified request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Prepares for WorkFlow Activity
	 * 
	 * @param responseData
	 */
	@SuppressWarnings("unchecked")
	private void prepareForWorkFlowActivityModified(UserRoleMgmtBean responseData, UserRoleModJsonDto userRoleJson) {
		List<WorkFlowActivity> deptDropDownAct = (List<WorkFlowActivity>) workFlowActivityController.getWorkFlowActivity(true).getResponse();
		List<DropDownObject> deptDropActivity = deptDropDownAct.stream().map(item -> {
			DropDownObject dto = new DropDownObject();
			dto.setDisplay(item.getActivityDesc());
			dto.setKey(item.getActivityId());
			return dto;
		}).collect(Collectors.toList());
		responseData.setActivityDeptInDropDown(deptDropActivity);
		List<WorkFlowActivity> entDropDownAct = (List<WorkFlowActivity>) workFlowActivityController.getWorkFlowActivity(false).getResponse();

		List<DropDownObject> entDropActivity = entDropDownAct.stream().map(item -> {
			DropDownObject dto = new DropDownObject();
			dto.setDisplay(item.getActivityDesc());
			dto.setKey(item.getActivityId());
			return dto;
		}).collect(Collectors.toList());
		responseData.setActivityEntInDropDown(entDropActivity);
		List<ActivityApplicableMenu> aplicableDept = (List<ActivityApplicableMenu>) activityApplicableMenuController.getActivityApplicableMenu(true).getResponse();
		List<Long> applicableDeptList = aplicableDept.stream().map(item -> {
			return item.getMenuIdFk();
		}).collect(Collectors.toList());
		responseData.setApprvRejectActivityDept(applicableDeptList);
		List<ActivityApplicableMenu> aplicableEnt = (List<ActivityApplicableMenu>) activityApplicableMenuController.getActivityApplicableMenu(false).getResponse();
		List<Long> applicableEntList = aplicableEnt.stream().map(item -> {
			if (!item.getActivityIdFk().getActivityId().equals(4l)) {
				return item.getMenuIdFk();
			}
			return null;

		}).collect(Collectors.toList());
		responseData.setApprvRejectActivityEnt(applicableEntList);
		List<Long> applicablUploadList = aplicableEnt.stream().map(item -> {
			if (item.getActivityIdFk().getActivityId().equals(4l)) {
				return item.getMenuIdFk();
			}
			return null;

		}).collect(Collectors.toList());
		responseData.setUploadActivity(applicablUploadList);
		if (userRoleJson.getSelectedActivity() != null && !userRoleJson.getSelectedActivity()[0].equals("null") && !userRoleJson.getSelectedActivity()[0].equals("") && !userRoleJson.getSelectedActivity()[0].equals("[]")) {
			List<String> activityIds = Arrays.asList(userRoleJson.getSelectedActivity());
			List<Long> finalActivityIds = activityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
			List<WorkFlowActivity> userRoleActivity = userRoleActivityMapService.getActivityFromJsonIds(finalActivityIds);
			if (userRoleActivity != null) {
				userRoleActivity.forEach(item -> {
					responseData.getActivityDeptInDropDown().forEach(dropitem -> {
						if (dropitem.getKey().equals(item.getActivityId())) {
							dropitem.setSelected(SELECTED);
						}
					});

					responseData.getActivityEntInDropDown().forEach(dropitem -> {
						if (dropitem.getKey().equals(item.getActivityId())) {
							dropitem.setSelected(SELECTED);
						}
					});

				});
			}
		}

	}

	@PostMapping("/getModifiedRoleList")
	public ServiceResponse getModifiedRoleList(@RequestHeader(name = "AppId") String jobProcessingId, @RequestHeader(name = "UUID") String uuid, @RequestBody UserRoleMgmtBean userRoleMgmtBean) throws JsonProcessingException {
		Logger.info("request received to get getModifiedRoleList for job processingid" + jobProcessingId);
		List<UserRoleModDetailsDto> dtoList = null;
		Long userId = userRoleMgmtBean.getUserId();
		ServiceResponse response = null;
		try {
			List<Long> roleTypeId = null;

			List<UserRoleModified> modifiedList = userRoleModifiedRepo.getDataByStatus(userRoleMgmtBean.getStatus());

			if (CollectionUtils.isEmpty(modifiedList)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			dtoList = new ArrayList<>();
			Gson gson = new Gson();

			Platform platform;

			List<RoleType> roleTypeList = roleTypeService.findByIsActiveTrue();
			List<Long> platformIdList = portalRepo.getDataByPortalCode(userRoleMgmtBean.getPlatFormCodeList());
			UserMaster userMaster = new UserMaster();
			userMaster = userMasterService.getDataById(userId);
			//UserRole loggedInUserRole = userRoleService.getDataById(userRoleMgmtBean.getRoleId());
			roleTypeId = new ArrayList<>();
			if (userMaster.getRoleType().getRoleTypeId() == GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal() && userMaster.getDepartmentIdFk().getIsMaster()) {
				roleTypeId.add(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal());
				roleTypeId.add(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal());
				roleTypeId.add(GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal());
				roleTypeId.add(GeneralConstants.AGENCY_ROLE_TYPE_ID.getConstantLongVal());
			} else if (userMaster.getRoleType().getRoleTypeId() == GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal() && !userMaster.getDepartmentIdFk().getIsMaster()) {
				roleTypeId.add(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal());
			} else if (userMaster.getRoleType().getRoleTypeId() == GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal()) {
				roleTypeId.add(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal());
			}
			UserRoleModDetailsDto dto = null;
			for (UserRoleModified userMod : modifiedList) {
				UserRoleModJsonDto userRoleJson = gson.fromJson(userMod.getRoleDetailsJson(), UserRoleModJsonDto.class);

				platform = portalService.getDataById(userRoleJson.getPortalId());

				if (!platformIdList.contains(userRoleJson.getPortalId())) {
					continue;
				}

				if (userRoleJson.getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
					if (!roleTypeId.contains(userRoleJson.getRoleTypeId())) {
						continue;
					}
					if (userMod.getCreatedBy().getDepartmentIdFk() != null && userMod.getCreatedBy().getDepartmentIdFk().getRegulatorId() != userMaster.getDepartmentIdFk().getRegulatorId()) {
						continue;
					}
					/*
					 * if
					 * (userRoleJson.getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.
					 * getConstantLongVal()) &&
					 * userMod.getCreatedBy().getDepartmentIdFk().getRegulatorId() !=
					 * userMaster.getDepartmentIdFk().getRegulatorId()) { continue; }
					 */
				} else if (userRoleJson.getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal())) {

					if (!roleTypeId.contains(userRoleJson.getRoleTypeId())) {
						continue;
					}

					if (roleTypeId.contains(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
						if (userMod.getCreatedBy().getDepartmentIdFk() == null) {
							continue;
						}
					}

					if (userRoleMgmtBean.getEntityCode() != null) {
						Map<String, Object> columnMap = new HashMap<>();
						columnMap.put(ColumnConstants.USER_ID.getConstantVal(), userMod.getCreatedBy().getUserId());
						columnMap.put(ColumnConstants.ROLEID.getConstantVal(), userMod.getCreatedByRole().getUserRoleId());

						List<UserRoleMaster> userRoleMaster = new ArrayList<>();
						UserRoleMaster userRoleMast = null;
						userRoleMaster = userRoleMasterService.getDataByObject(columnMap, MethodConstants.FIND_BY_USERROLEIDIN_AND_USERIDIN.getConstantVal());
						if (!CollectionUtils.isEmpty(userRoleMaster)) {
							userRoleMast = userRoleMaster.get(0);
						}
						Map<String, List<String>> columnValueMap1 = new HashMap<>();
						List<String> entityCodes = new ArrayList<>();
						entityCodes.add(userRoleMgmtBean.getEntityCode());
						columnValueMap1.put(ColumnConstants.ENTITY_CODE.getConstantVal(), entityCodes);

						List<EntityBean> entityBeanList = entityService.getDataByColumnValue(columnValueMap1, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());
						EntityBean userMappedEntity = null;
						EntityBean loggedInEntity = null;

						if (userRoleMast != null && !CollectionUtils.isEmpty(userRoleMast.getUserEntityRole())) {
							userMappedEntity = userRoleMast.getUserEntityRole().iterator().next().getEntityBean();

						} else {
							continue;
						}
						if (userMappedEntity != null && !userRoleMgmtBean.getEntityCode().equalsIgnoreCase(userMappedEntity.getEntityCode())) {
							continue;
						}
					}

				}
				dto = new UserRoleModDetailsDto();
				dto.setUserRoleModDetailsId(userMod.getUserRoleModifiedId());
				dto.setPortalName(platform.getPlatFormKey());
				for (RoleType roleType : roleTypeList) {
					if (roleType.getRoleTypeId().equals(userRoleJson.getRoleTypeId())) {
						dto.setRoleTypeName(roleType.getRoleTypeDesc());
						dto.setRoleTypeId(userRoleJson.getRoleTypeId());

					}
				}
				dto.setPortalId(userRoleJson.getPortalId());
				if (userRoleJson.getDeptAdmin() != null && userRoleJson.getDeptAdmin().equalsIgnoreCase("1")) {
					dto.setIsDeptAdmin(Boolean.TRUE);
				} else {
					dto.setIsDeptAdmin(Boolean.FALSE);

				}
				dto.setDeptAdmin(userRoleJson.getDeptAdmin());
				if (!StringUtils.isBlank(userRoleJson.getRoleDesc())) {
					dto.setRoleDesc(userRoleJson.getRoleDesc());
				} else {
					dto.setRoleDesc(null);

				}
				dto.setRoleName(userRoleJson.getRoleName());

				dto.setCreatedByUserName(userMod.getCreatedBy().getUserName());
				dto.setCreatedByOn(userMod.getCreatedOn());
				if (userMod.getUserModify() != null) {
					dto.setModifiedByUserName(userMod.getUserModify().getUserName());
					dto.setModifiedByOn(userMod.getModifiedOn());
				} else {
					dto.setModifiedByUserName(GeneralConstants.EMPTY_DATA.getConstantVal());
					dto.setModifiedByOn(null);
				}

				if (userMod.getApprovedBy() != null) {
					dto.setApprovedByUserName(userMod.getApprovedBy().getUserName());
					dto.setApprovedOn(userMod.getApprovedOn());
				} else {
					dto.setApprovedByUserName(GeneralConstants.EMPTY_DATA.getConstantVal());
					dto.setApprovedOn(null);
				}
				if (userMod.getRejectComment() != null) {
					dto.setRejectComment(userMod.getRejectComment());
				}
				dto.setActionIdFk(userMod.getActionId_FK());
				dto.setIsActive(userRoleJson.getIsActive());

				dto.setOtherPortalJSON(userMod.getRoleDetailsJson());
				dtoList.add(dto);

			}

			if (CollectionUtils.isEmpty(dtoList)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(dtoList);

		} catch (Exception e) {
			Logger.error("Exception while fetching Pending User Role info for job processingid " + jobProcessingId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		Logger.info("request completed to get user role list for job processingid" + jobProcessingId);
		return response;
	}
}
