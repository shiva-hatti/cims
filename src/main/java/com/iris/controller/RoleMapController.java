package com.iris.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.iris.dto.DropDownGroup;
import com.iris.dto.DropDownObject;
import com.iris.dto.EditViewRoleDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.LogedInUser;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserInfoDTO;
import com.iris.dto.UserModifiedDto;
import com.iris.dto.UserRoleDto;
import com.iris.dto.UserRoleMgmtBean;
import com.iris.dto.UserRoleModDetailsDto;
import com.iris.dto.UserRoleModJsonDto;
import com.iris.exception.ServiceException;
import com.iris.model.ActivityApplicableMenu;
import com.iris.model.Category;
import com.iris.model.EntityBean;
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
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.UserRoleActivityMap;
import com.iris.model.UserRoleEntityMapping;
import com.iris.model.UserRoleLabel;
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
import com.iris.repository.UserMasterRepo;
import com.iris.repository.UserRoleEntityMappingRepo;
import com.iris.repository.UserRoleModifiedRepo;
import com.iris.repository.UserRolePortalMapRepo;
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
import com.iris.util.UtilMaster;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * This class is responsible for User Role Creation, save,update related activities
 * 
 * @author svishwakarma
 *
 */
@RestController
@RequestMapping("/service/rolemap")
public class RoleMapController {
	private static final Logger Logger = LoggerFactory.getLogger(RoleMapController.class);
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
	UserRolePortalMapRepo userRolePortalMapRepo;

	private static final Object lock1 = new Object();

	@PostMapping("/init/{rollId}/{userid}")
	@SuppressWarnings("unchecked")
	public ServiceResponse getRoleMap(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollId") Long rollId, @PathVariable("userid") Long userId, @RequestBody UserRoleMgmtBean userRoleMgmtBean) throws Exception {
		Logger.info("Requseting getRoleMap {}", jobProcessId);
		try {
			UserRoleMgmtBean responseData = new UserRoleMgmtBean();
			responseData.setCurrentLocale(userRoleMgmtBean.getCurrentLocale());
			LanguageMaster localeCode = languageMasterService.getDataById(userRoleMgmtBean.getCurrentLocale());
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
			Collection<Platform> platformList = (Collection<Platform>) platFormController.getAllPortal(jobProcessId, userId, rollId).getResponse();

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
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setActive(true);
			entityMasterDto.setRoleId(rollId);
			entityMasterDto.setUserId(userId);
			entityMasterDto.setIsCategoryWiseResponse(true);
			entityMasterDto.setLanguageCode(localeCode.getLanguageCode());
			List<Category> categoryList = (List<Category>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
			Logger.info("Entity fetch complete {}", jobProcessId);
			List<DropDownGroup> entityDropDown = new ArrayList<>();
			if (categoryList != null && !categoryList.isEmpty()) {
				for (Category item : categoryList) {
					if (item.getSubCategory() != null) {
						for (SubCategory subCat : item.getSubCategory()) {
							DropDownGroup grp = new DropDownGroup();
							grp.setDisplay(subCat.getSubCategoryName());
							for (EntityBean ent : subCat.getEntity()) {
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
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(userId);
			returnGroupMappingRequest.setLangId(localeCode.getLanguageId());
			returnGroupMappingRequest.setRoleId(rollId);

			responseData.setReturnInputList((List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse());
			Logger.info("ReturnGroup fetch complete {}", jobProcessId);
			List<MenuRoleMap> menulist = (List<MenuRoleMap>) menuRoleController.getAllMenu(jobProcessId, rollId).getResponse();
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
				if (roleReturnMap != null && responseData.getReturnInputList() != null) {
					responseData.getReturnInputList().forEach(item -> {
						item.getReturnList().forEach(returnItem -> {
							roleReturnMap.forEach(outItem -> {
								if (returnItem.getReturnId().equals(outItem.getReturnIdFk().getReturnId())) {
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
				List<UserRoleLabel> hindiLabel = role.getUsrRoleLabelSet().stream().filter(item -> StringUtils.equals(item.getLangIdFk().getLanguageCode(), "hi")).collect(Collectors.toList());
				if (hindiLabel != null && !hindiLabel.isEmpty()) {
					responseData.setRollNameHindi(hindiLabel.get(0).getUserRoleLabel());
				}
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
			List<LanguageMaster> languegeList = languageMasterService.getAllActiveLanguage();
			List<DropDownObject> languageInDropDown = languegeList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(item.getLanguageId());
				dto.setDisplay(item.getLanguageName());
				return dto;
			}).collect(Collectors.toList());
			responseData.setLanguageInDropDown(languageInDropDown);
			response.setResponse(responseData);
			Logger.info("init complete {}", jobProcessId);
			return response;
		} catch (Exception e) {
			Logger.error("Exception while getRoleMap request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
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

	@PostMapping("/saveOrModify")
	public ServiceResponse saveOrModify(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleMgmtBean userRoleMgmt, Boolean add) throws JsonProcessingException {
		Logger.info("Requseting saveOrModify {}", jobProcessId);
		try {
			ServiceResponse response = null;
			String userRoleJson;
			Boolean approvalFlag = add;
			if (userRoleMgmt.getRoleIdExisting() != null && userRoleMgmt.getRoleIdExisting() > 0) {
				approvalFlag = false;
			} else {
				approvalFlag = true;
			}

			if (approvalFlag) {
				if (userRoleMgmt.isApprovalFlag()) {// Approval On
					if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
						UserRoleModJsonDto modifiedJsonDto = new UserRoleModJsonDto();// Json Start
						modifiedJsonDto.setRoleTypeId(userRoleMgmt.getRoleType());
						modifiedJsonDto.setPortalId(userRoleMgmt.getPortal());
						// userRoleModDetailsDto.setLangCode(userRoleMgmt.getLangCode());

						/*
						 * List<RoleNameDto> nameDtoList = new ArrayList<RoleNameDto>(); RoleNameDto
						 * roleNameDto = new RoleNameDto();
						 * 
						 * roleNameDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
						 * roleNameDto.setRoleName(userRoleMgmt.getRollName());
						 * nameDtoList.add(roleNameDto);
						 * 
						 * roleNameDto = new RoleNameDto();
						 * roleNameDto.setLangCode(GeneralConstants.HIN_LANG.getConstantVal());
						 * roleNameDto.setRoleName(userRoleMgmt.getRollNameHindi());
						 * nameDtoList.add(roleNameDto);
						 */
						if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin())) {
							modifiedJsonDto.setDeptAdmin(userRoleMgmt.getDeptAdmin());
						} else {
							modifiedJsonDto.setDeptAdmin("0");
						}

						modifiedJsonDto.setIsActive(true);
						modifiedJsonDto.setRoleName(userRoleMgmt.getRollName());
						modifiedJsonDto.setRoleDesc(userRoleMgmt.getRoleDesc());

						List<String> returnList = new ArrayList<String>();
						List<String> entityList = new ArrayList<String>();
						List<String> activityList = new ArrayList<String>();
						List<String> menuList = new ArrayList<String>();
						List<String> tempList = new ArrayList<String>();

						if (!userRoleMgmt.getSelectedReturn()[0].equals("null")) {
							String retArray = userRoleMgmt.getSelectedReturn()[0];
							retArray = retArray.replace("[", "");
							retArray = retArray.replace("]", "");
							for (String str : retArray.trim().split(",")) {
								str = str.replace("\"", "");
								returnList.add(str.trim());
							}
							String[] arr = new String[returnList.size()];
							arr = returnList.toArray(arr);
							modifiedJsonDto.setSelectedReturn(arr);
						}

						if (!userRoleMgmt.getSelectedEntity()[0].equals("null")) {
							String entArray = userRoleMgmt.getSelectedEntity()[0];
							entArray = entArray.replace("[", "");
							entArray = entArray.replace("]", "");
							for (String str : entArray.trim().split(",")) {
								str = str.replace("\"", "");
								entityList.add(str.trim());
							}
							String[] arrEnt = new String[entityList.size()];
							arrEnt = entityList.toArray(arrEnt);
							modifiedJsonDto.setSelectedEntity(arrEnt);
						}

						if (!userRoleMgmt.getSelectedMenu()[0].equals("null")) {
							String menuArray = userRoleMgmt.getSelectedMenu()[0];
							menuArray = menuArray.replace("[", "");
							menuArray = menuArray.replace("]", "");
							for (String str : menuArray.trim().split(",")) {
								str = str.replace("\"", "");
								menuList.add(str.trim());
							}
							String[] arrMenu = new String[menuList.size()];
							arrMenu = menuList.toArray(arrMenu);
							modifiedJsonDto.setSelectedMenu(arrMenu);
						}

						if (!userRoleMgmt.getSelectedActivity()[0].equals("null")) {
							String actArray = userRoleMgmt.getSelectedActivity()[0];
							actArray = actArray.replace("[", "");
							actArray = actArray.replace("]", "");
							for (String str : actArray.trim().split(",")) {
								str = str.replace("\"", "");
								activityList.add(str.trim());
							}
							String[] arrAct = new String[activityList.size()];
							arrAct = activityList.toArray(arrAct);
							modifiedJsonDto.setSelectedActivity(arrAct);
						}

						if (!userRoleMgmt.getSelectedTemplate()[0].equals("null")) {
							String tempArray = userRoleMgmt.getSelectedTemplate()[0];
							tempArray = tempArray.replace("[", "");
							tempArray = tempArray.replace("]", "");
							for (String str : tempArray.trim().split(",")) {
								str = str.replace("\"", "");
								tempList.add(str.trim());
							}
							String[] arrTemp = new String[tempList.size()];
							arrTemp = tempList.toArray(arrTemp);
							modifiedJsonDto.setSelectedTemplate(arrTemp);
						}

						Gson gson = new Gson();
						userRoleJson = gson.toJson(modifiedJsonDto);// Json end
					} else {
						userRoleJson = userRoleMgmt.getOtherPortalJSON();
					}
					UserRoleModified roleModified = new UserRoleModified();
					roleModified.setAdminStatusId_FK(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal());
					roleModified.setActionId_FK(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal());
					UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
					// roleModified.setUserModify(userMaster);
					Date auditDate = new Date();
					// roleModified.setModifiedOn(auditDate);
					roleModified.setCreatedBy(userMaster);
					roleModified.setCreatedOn(auditDate);
					roleModified.setLastUpdateOn(auditDate);
					roleModified.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
					roleModified.setRoleDetailsJson(userRoleJson);

					UserRoleModified status = userRoleModifiedRepo.save(roleModified); // Add in User Role Modified
																						// Table
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					if (status != null) {
						response.setStatus(true);
					} else {
						response.setStatus(false);
					}
				} else {// Approval Off

					boolean status = roleMapService.saveOrUpdate(userRoleMgmt, add); // Add in User Role Master Table
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					response.setStatus(status);
				}
			} else {// edit

				if (userRoleMgmt.isApprovalFlag()) {// Approval On
					if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
						UserRoleModJsonDto modifiedJsonDto = new UserRoleModJsonDto();// Json Start
						modifiedJsonDto.setRoleTypeId(userRoleMgmt.getRoleType());
						modifiedJsonDto.setPortalId(userRoleMgmt.getPortal());
						// userRoleModDetailsDto.setLangCode(userRoleMgmt.getLangCode());

						/*
						 * List<RoleNameDto> nameDtoList = new ArrayList<RoleNameDto>(); RoleNameDto
						 * roleNameDto = new RoleNameDto();
						 * 
						 * roleNameDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
						 * roleNameDto.setRoleName(userRoleMgmt.getRollName());
						 * nameDtoList.add(roleNameDto);
						 * 
						 * roleNameDto = new RoleNameDto();
						 * roleNameDto.setLangCode(GeneralConstants.HIN_LANG.getConstantVal());
						 * roleNameDto.setRoleName(userRoleMgmt.getRollNameHindi());
						 * nameDtoList.add(roleNameDto);
						 */

						if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin())) {
							modifiedJsonDto.setDeptAdmin(userRoleMgmt.getDeptAdmin());
						} else {
							modifiedJsonDto.setDeptAdmin("0");
						}

						if (userRoleMgmt.getIsActive() != null && userRoleMgmt.getIsActive().equals(1l)) {
							modifiedJsonDto.setIsActive(true);
						} else {
							modifiedJsonDto.setIsActive(false);
						}
						modifiedJsonDto.setRoleName(userRoleMgmt.getRollName());
						//modifiedJsonDto.setRoleNameDto(nameDtoList);
						modifiedJsonDto.setRoleDesc(userRoleMgmt.getRoleDesc());

						List<String> returnList = new ArrayList<String>();
						List<String> entityList = new ArrayList<String>();
						List<String> activityList = new ArrayList<String>();
						List<String> menuList = new ArrayList<String>();
						List<String> tempList = new ArrayList<String>();

						if (!userRoleMgmt.getSelectedReturn()[0].equals("null")) {
							String retArray = userRoleMgmt.getSelectedReturn()[0];
							retArray = retArray.replace("[", "");
							retArray = retArray.replace("]", "");
							for (String str : retArray.trim().split(",")) {
								str = str.replace("\"", "");
								returnList.add(str.trim());
							}
							String[] arr = new String[returnList.size()];
							arr = returnList.toArray(arr);
							modifiedJsonDto.setSelectedReturn(arr);
						}

						if (!userRoleMgmt.getSelectedEntity()[0].equals("null")) {
							String entArray = userRoleMgmt.getSelectedEntity()[0];
							entArray = entArray.replace("[", "");
							entArray = entArray.replace("]", "");
							for (String str : entArray.trim().split(",")) {
								str = str.replace("\"", "");
								entityList.add(str.trim());
							}
							String[] arrEnt = new String[entityList.size()];
							arrEnt = entityList.toArray(arrEnt);
							modifiedJsonDto.setSelectedEntity(arrEnt);
						}

						if (!userRoleMgmt.getSelectedMenu()[0].equals("null")) {
							String menuArray = userRoleMgmt.getSelectedMenu()[0];
							menuArray = menuArray.replace("[", "");
							menuArray = menuArray.replace("]", "");
							for (String str : menuArray.trim().split(",")) {
								str = str.replace("\"", "");
								menuList.add(str.trim());
							}
							String[] arrMenu = new String[menuList.size()];
							arrMenu = menuList.toArray(arrMenu);
							modifiedJsonDto.setSelectedMenu(arrMenu);
						}

						if (!userRoleMgmt.getSelectedActivity()[0].equals("null")) {
							String actArray = userRoleMgmt.getSelectedActivity()[0];
							actArray = actArray.replace("[", "");
							actArray = actArray.replace("]", "");
							for (String str : actArray.trim().split(",")) {
								str = str.replace("\"", "");
								activityList.add(str.trim());
							}
							String[] arrAct = new String[activityList.size()];
							arrAct = activityList.toArray(arrAct);
							modifiedJsonDto.setSelectedActivity(arrAct);
						}

						if (!userRoleMgmt.getSelectedTemplate()[0].equals("null")) {
							String tempArray = userRoleMgmt.getSelectedTemplate()[0];
							tempArray = tempArray.replace("[", "");
							tempArray = tempArray.replace("]", "");
							for (String str : tempArray.trim().split(",")) {
								str = str.replace("\"", "");
								tempList.add(str.trim());
							}
							String[] arrTemp = new String[tempList.size()];
							arrTemp = tempList.toArray(arrTemp);
							modifiedJsonDto.setSelectedTemplate(arrTemp);
						}

						Gson gson = new Gson();
						userRoleJson = gson.toJson(modifiedJsonDto);// Json end

					} else {
						userRoleJson = userRoleMgmt.getOtherPortalJSON();
					}

					UserRole role = userRoleService.getDataById(userRoleMgmt.getRoleIdExisting());
					UserRoleModified roleModified = new UserRoleModified();
					roleModified.setAdminStatusId_FK(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal());
					roleModified.setActionId_FK(GeneralConstants.ACTIONID_EDITION.getConstantIntVal());
					UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
					roleModified.setUserModify(userMaster);
					Date auditDate = new Date();
					roleModified.setCreatedBy(role.getUser());
					roleModified.setCreatedOn(role.getCreatedOn());
					roleModified.setModifiedOn(auditDate);
					roleModified.setLastUpdateOn(auditDate);
					roleModified.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
					roleModified.setRoleDetailsJson(userRoleJson);
					UserRole existRoleModified = new UserRole();
					existRoleModified.setUserRoleId(userRoleMgmt.getRoleIdExisting());
					roleModified.setUserRoleIdFk(existRoleModified);

					UserRoleModified status = userRoleModifiedRepo.save(roleModified); // Add in User Role Modified
																						// Table
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					if (status != null) {
						response.setStatus(true);
					} else {
						response.setStatus(false);
					}
				} else {// Approval Off

					UserRole role = userRoleService.getDataById(userRoleMgmt.getRoleIdExisting());
					if (role.getLastApprovedOn() == null) {

						if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
							UserRoleModJsonDto userRoleModJsonDto = new UserRoleModJsonDto();// Json Start
							userRoleModJsonDto.setRoleTypeId(role.getRoleType().getRoleTypeId());

							for (UserRolePlatFormMap userRolePlatform : role.getUserRolePlatFormMap()) {
								Long platformId = userRolePlatform.getPlatForm().getPlatFormId();
								userRoleModJsonDto.setPortalId(platformId);
							}

							/*
							 * List<RoleNameDto> nameDtoList = new ArrayList<RoleNameDto>(); RoleNameDto
							 * roleNameDto = new RoleNameDto();
							 * roleNameDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
							 * roleNameDto.setRoleName(role.getUsrRoleLabelSet().stream().filter(i ->
							 * i.getLangIdFk().getLanguageCode().equals(GeneralConstants.ENG_LANG.
							 * getConstantVal())).findAny().get().getUserRoleLabel());
							 * nameDtoList.add(roleNameDto); roleNameDto = new RoleNameDto();
							 * roleNameDto.setLangCode(GeneralConstants.HIN_LANG.getConstantVal());
							 * roleNameDto.setRoleName(role.getUsrRoleLabelSet().stream().filter(i ->
							 * i.getLangIdFk().getLanguageCode().equals(GeneralConstants.HIN_LANG.
							 * getConstantVal())).findAny().get().getUserRoleLabel());
							 * nameDtoList.add(roleNameDto);
							 */
							if (StringUtils.isNotBlank(role.getDeptAdmin())) {
								userRoleModJsonDto.setDeptAdmin(role.getDeptAdmin());
							} else {
								userRoleModJsonDto.setDeptAdmin("0");
							}

							if (role.getIsActive() != null && role.getIsActive() == true) {
								userRoleModJsonDto.setIsActive(true);
							} else {
								userRoleModJsonDto.setIsActive(false);
							}

							List<String> returnList = new ArrayList<String>();
							List<String> entityList = new ArrayList<String>();
							List<String> activityList = new ArrayList<String>();
							List<String> menuList = new ArrayList<String>();

							if (!role.getUserRoleActivityMapping().isEmpty()) {
								for (UserRoleActivityMap activity : role.getUserRoleActivityMapping()) {
									activityList.add(activity.getWorkFlowActivity().getActivityId().toString());
								}
								String[] arr = new String[activityList.size()];
								arr = activityList.toArray(arr);
								userRoleModJsonDto.setSelectedActivity(arr);
							}

							if (!role.getUserRoleReturnMapping().isEmpty()) {
								for (UserRoleReturnMapping userReturn : role.getUserRoleReturnMapping()) {
									returnList.add(userReturn.getReturnIdFk().getReturnId().toString());
								}
								String[] arrReturn = new String[returnList.size()];
								arrReturn = returnList.toArray(arrReturn);
								userRoleModJsonDto.setSelectedReturn(arrReturn);
							}

							if (!role.getUserRoleEntityMapping().isEmpty()) {
								for (UserRoleEntityMapping userEntity : role.getUserRoleEntityMapping()) {
									entityList.add(userEntity.getEntity().getEntityId().toString());
								}
								String[] arrEntity = new String[entityList.size()];
								arrEntity = entityList.toArray(arrEntity);
								userRoleModJsonDto.setSelectedEntity(arrEntity);
							}

							List<MenuRoleMap> existingMenuIdList = menuRoleMapRepo.findByUserRoleIdFkUserRoleIdAndIsActiveTrue(role.getUserRoleId());

							if (!existingMenuIdList.isEmpty()) {
								for (MenuRoleMap menu : existingMenuIdList) {
									menu.getMenuIDFk().getMenuId();
								}

								for (MenuRoleMap menuRole : existingMenuIdList) {
									menuList.add(menuRole.getMenuIDFk().getMenuId().toString());
								}
								String[] arrMenu = new String[menuList.size()];
								arrMenu = menuList.toArray(arrMenu);
								userRoleModJsonDto.setSelectedMenu(arrMenu);
							}
							userRoleModJsonDto.setRoleName(userRoleMgmt.getRollName());
							//userRoleModJsonDto.setRoleNameDto(nameDtoList);
							userRoleModJsonDto.setRoleDesc(role.getRoleDesc());

							Gson gson = new Gson();
							userRoleJson = gson.toJson(userRoleModJsonDto);// Json end

						} else {
							userRoleJson = userRoleMgmt.getOtherPortalJSON();
						}

						UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());

						UserRoleModified roleModified = new UserRoleModified();
						Date auditDate = new Date();

						roleModified.setAdminStatusId_FK(GeneralConstants.APPROVED_ADMIN_STATUS_ID.getConstantIntVal());
						if (role.getLastModifiedOn() == null) {
							roleModified.setActionId_FK(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal());
						} else {
							roleModified.setActionId_FK(GeneralConstants.ACTIONID_EDITION.getConstantIntVal());
							roleModified.setUserModify(userMaster);
							roleModified.setModifiedOn(auditDate);
						}
						roleModified.setCreatedBy(role.getUser());
						roleModified.setCreatedOn(role.getCreatedOn());
						roleModified.setLastUpdateOn(auditDate);
						UserRole createdRole = new UserRole();
						createdRole.setUserRoleId(role.getCreatedByRole().getUserRoleId());
						roleModified.setCreatedByRole(createdRole);
						roleModified.setRoleDetailsJson(userRoleJson);
						UserRole existRoleId = new UserRole();
						existRoleId.setUserRoleId(userRoleMgmt.getRoleIdExisting());
						roleModified.setUserRoleIdFk(existRoleId);

						UserRoleModified newUserRoleModified = userRoleModifiedRepo.save(roleModified); // Add in User
																										// Role Modified
																										// Table
					}

					List<UserRoleReturnMapping> existingRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(role.getUserRoleId(), true);

					boolean status = roleMapService.saveOrUpdate(userRoleMgmt, add); // Add in User Role Master Table

					List<UserRoleReturnMapping> newRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(role.getUserRoleId(), true);

					if (status) {
						boolean checkReturnFlag = false;
						if (existingRole.isEmpty() == false && newRole.isEmpty() == true) {
							checkReturnFlag = true;
						}

						if (checkReturnFlag) {
							List<Long> userMasterIdList = userMasterRepo.getUserMasterList(role.getUserRoleId());
							Long[] arrId = new Long[userMasterIdList.size()];
							int i = 0;
							for (Long userId : userMasterIdList) {
								arrId[i] = userId;
								i++;
							}

							List<UserMaster> list = userMasterRepo.findByUserIdIn(arrId);
							boolean retMappingFlag = false;
							List<Long> updateUserList = new ArrayList<Long>();
							for (UserMaster user : list) {
								if (user.getUsrRoleMstrSet().size() == 1) {
									retMappingFlag = false;
									updateUserList.add(user.getUserId());
								} else {
									for (UserRoleMaster roleM : user.getUsrRoleMstrSet()) {
										if (!roleM.getUserRole().getUserRoleId().equals(role.getUserRoleId())) {

											List<UserRoleReturnMapping> chkRoleRetMapping = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(roleM.getUserRole().getUserRoleId(), true);
											if (chkRoleRetMapping.isEmpty() == false) {
												retMappingFlag = true;
												break;
											}
										}
									}

									if (!retMappingFlag) {
										updateUserList.add(user.getUserId());
									}
								}
							}

							if (updateUserList.isEmpty() == false) {
								Long[] arrUserId = new Long[updateUserList.size()];
								int j = 0;
								for (Long userId : userMasterIdList) {
									arrUserId[j] = userId;
									j++;
								}

								int updateDeptFlag = deptUserEntityMappingRepo.updateEntityOfUser(arrUserId);

							}
						}
					}

					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					response.setStatus(status);
				}

			}

			Logger.info("Requset saveOrModify complete {}", jobProcessId);
			return response;
		} catch (Exception e) {
			Logger.error("Exception while saveOrModify request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping("/approveRejectUserRole")
	public ServiceResponse approveRejectUserRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleModDetailsDto userRoleModDetailsDto) throws JsonProcessingException {
		try {
			Date currentDate = new Date();
			Gson gson = new Gson();
			List<UserRoleReturnMapping> existingRole = new ArrayList<>();

			if (userRoleModDetailsDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}
			synchronized (lock1) {
				UserRoleModified mod = userRoleModifiedService.getDataById(userRoleModDetailsDto.getUserRoleModDetailsId());
				Integer modAdminStatusIdFK = mod.getAdminStatusId_FK();
				Integer userAdminStatusIdFK = userRoleModDetailsDto.getAdminStatusIdFk();

				if (modAdminStatusIdFK != 1) {
					if (modAdminStatusIdFK == 2) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0095.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0095.toString())).build();
					} else if (modAdminStatusIdFK == 3) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0096.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0096.toString())).build();
					}
				}
				if (userRoleModDetailsDto.getApprovalFlag() && userAdminStatusIdFK == 2) {
					UserMaster um = userMasterService.getDataById(userRoleModDetailsDto.getApprovedByFk().getUserId());
					mod.setApprovedBy(um);
					mod.setApprovedOn(currentDate);
					mod.setAdminStatusId_FK(2);

					boolean modUpdateFlag = userRoleModifiedService.update(mod);

					//Update in User Role Master Table
					if (modUpdateFlag) {

						UserRoleModJsonDto userRoleJson = gson.fromJson(mod.getRoleDetailsJson(), UserRoleModJsonDto.class);
						//um
						UserRoleMgmtBean userRoleBean = new UserRoleMgmtBean();
						userRoleBean.setRollName(userRoleJson.getRoleName());
						userRoleBean.setRoleDesc(userRoleJson.getRoleDesc());
						userRoleBean.setRoleType(userRoleJson.getRoleTypeId());
						userRoleBean.setDeptAdmin(userRoleJson.getDeptAdmin());
						userRoleBean.setCreatedByRole(mod.getCreatedBy().getUserId());

						//userRoleBean.setRollNameHindi(userRoleJson.getRoleNameDto().get(1).getRoleName());

						userRoleBean.setPortal(userRoleJson.getPortalId());
						if (userRoleBean.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
							userRoleBean.setSelectedReturn(userRoleJson.getSelectedReturn());
							userRoleBean.setSelectedMenu(userRoleJson.getSelectedMenu());
							userRoleBean.setSelectedEntity(userRoleJson.getSelectedEntity());
							userRoleBean.setSelectedActivity(userRoleJson.getSelectedActivity());
							userRoleBean.setSelectedTemplate(userRoleJson.getSelectedTemplate());
						}
						userRoleBean.setIsActive(userRoleJson.getIsActive() == true ? 1L : 0L);
						if (mod.getUserRoleIdFk() != null) {
							userRoleBean.setRoleIdExisting(mod.getUserRoleIdFk().getUserRoleId());
							existingRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(mod.getUserRoleIdFk().getUserRoleId(), true);
						}

						boolean status = roleMapService.saveOrUpdateInApproval(userRoleBean, mod.getCreatedBy(), mod.getCreatedOn(), mod.getApprovedBy(), mod.getApprovedOn(), mod.getUserModify(), mod.getModifiedOn(), false); //Add in User Role Master Table

						if (status && mod.getUserRoleIdFk() != null) {
							List<UserRoleReturnMapping> newRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(mod.getUserRoleIdFk().getUserRoleId(), true);

							boolean checkReturnFlag = false;
							if (existingRole.isEmpty() == false && newRole.isEmpty() == true) {
								checkReturnFlag = true;
							}

							if (checkReturnFlag) {
								List<Long> userMasterIdList = userMasterRepo.getUserMasterList(mod.getUserRoleIdFk().getUserRoleId());
								Long[] arrId = new Long[userMasterIdList.size()];
								int i = 0;
								for (Long userId : userMasterIdList) {
									arrId[i] = userId;
									i++;
								}

								List<UserMaster> list = userMasterRepo.findByUserIdIn(arrId);
								boolean retMappingFlag = false;
								List<Long> updateUserList = new ArrayList<Long>();
								for (UserMaster user : list) {
									if (user.getUsrRoleMstrSet().size() == 1) {
										retMappingFlag = false;
										updateUserList.add(user.getUserId());
									} else {
										for (UserRoleMaster roleM : user.getUsrRoleMstrSet()) {
											if (!roleM.getUserRole().getUserRoleId().equals(mod.getUserRoleIdFk().getUserRoleId())) {

												List<UserRoleReturnMapping> chkRoleRetMapping = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(roleM.getUserRole().getUserRoleId(), true);
												if (chkRoleRetMapping.isEmpty() == false) {
													retMappingFlag = true;
													break;
												}
											}
										}

										if (!retMappingFlag) {
											updateUserList.add(user.getUserId());
										}
									}
								}

								if (updateUserList.isEmpty() == false) {
									Long[] arrUserId = new Long[updateUserList.size()];
									int j = 0;
									for (Long userId : userMasterIdList) {
										arrUserId[j] = userId;
										j++;
									}

									int updateDeptFlag = deptUserEntityMappingRepo.updateEntityOfUser(arrUserId);

								}
							}
						}
						ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
						response.setStatus(status);

						Map<String, List<String>> valueMap = new HashMap<>();
						List<String> roleNameList = new ArrayList<>();
						roleNameList.add(userRoleJson.getRoleName());
						valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), roleNameList);
						List<UserRole> userRoleList = userRoleService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
						if (!CollectionUtils.isEmpty(userRoleList)) {
							UserRole role = userRoleList.get(0);
							mod.setUserRoleIdFk(role);
							userRoleModifiedService.update(mod);
						}

					}

				} else if (userAdminStatusIdFK == 3) {
					UserMaster uMaster = userMasterService.getDataById(userRoleModDetailsDto.getApprovedByFk().getUserId());
					mod.setApprovedBy(uMaster);
					mod.setApprovedOn(currentDate);
					mod.setAdminStatusId_FK(userRoleModDetailsDto.getAdminStatusIdFk());
					mod.setRejectComment(userRoleModDetailsDto.getRejectComment());

					userRoleModifiedService.update(mod);

				}

			}
		} catch (Exception e) {
			Logger.error("Exception while approve or reject unlock request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

		return new ServiceResponseBuilder().setStatus(true).build();

	}

	@PostMapping("/getPendingUserRole")
	public ServiceResponse getPendingUserRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody LogedInUser LoggedInUser) throws JsonProcessingException {
		Logger.info("request received to get getPendingUserRole for job processingid" + jobProcessId);
		List<UserRoleModDetailsDto> dtoList = null;
		Long userId = LoggedInUser.getUserId();
		boolean deptAdminFlag = false;
		ServiceResponse response = null;
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			List<String> platFormMapCodeList = new ArrayList<>();

			List<UserRoleModified> modifiedList = userRoleModifiedService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_USER_ROLE_DATA.getConstantVal());
			List<UserRolePlatFormMap> platFormMap = userRolePortalMapRepo.findBydPlatformLByUserName(AESV2.getInstance().encrypt(LoggedInUser.getUserName()));
			for (UserRolePlatFormMap userRolePlatformMapping : platFormMap) {
				if (!platFormMapCodeList.contains(userRolePlatformMapping.getPlatForm().getPlatFormCode())) {
					platFormMapCodeList.add(userRolePlatformMapping.getPlatForm().getPlatFormCode());
				}
			}
			if (CollectionUtils.isEmpty(modifiedList)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			dtoList = new ArrayList<UserRoleModDetailsDto>();
			UserRoleModDetailsDto dto = null;
			Gson gson = new Gson();

			Platform platform;

			List<RoleType> roleTypeList = roleTypeService.findByIsActiveTrue();

			UserMaster userMaster = new UserMaster();
			userMaster = userMasterService.getDataById(userId);
			UserRole loggedInUserRole = userRoleService.getDataById(LoggedInUser.getRoleId());
			if (loggedInUserRole.getDeptAdmin().equals("1")) {
				deptAdminFlag = true;
			} else {
				deptAdminFlag = false;
			}
			for (UserRoleModified userMod : modifiedList) {
				UserRoleModJsonDto userRoleJson = gson.fromJson(userMod.getRoleDetailsJson(), UserRoleModJsonDto.class);

				platform = portalService.getDataById(userRoleJson.getPortalId());
				if (!platFormMapCodeList.contains(platform.getPlatFormCode())) {
					continue;
				}
				if (userMod.getCreatedBy().getUserId() == userMaster.getUserId() && userMod.getUserModify() == null) {
					continue;
				}
				if (userMod.getUserModify() != null && userMod.getUserModify().getUserId() == userMaster.getUserId()) {
					continue;
				}
				if (userMod.getUserRoleIdFk() != null && LoggedInUser.getRoleId() == userMod.getUserRoleIdFk().getUserRoleId()) {
					continue;
				}
				if (LoggedInUser.getRoleTypeId() == GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) {
					if (!userMaster.getDepartmentIdFk().getIsMaster() && userMod.getCreatedBy().getRoleType().getRoleTypeId() != userMaster.getRoleType().getRoleTypeId()) {
						continue;
					} else if (userMaster.getDepartmentIdFk().getIsMaster() && !deptAdminFlag && userMod.getCreatedBy().getRoleType().getRoleTypeId() != userMaster.getRoleType().getRoleTypeId()) {
						continue;
					}
					if (!userMaster.getDepartmentIdFk().getIsMaster() && userMod.getCreatedBy().getDepartmentIdFk().getRegulatorId() != userMaster.getDepartmentIdFk().getRegulatorId()) {
						continue;
					} else if (!deptAdminFlag && userMod.getCreatedBy().getDepartmentIdFk().getRegulatorId() != userMaster.getDepartmentIdFk().getRegulatorId()) {
						continue;
					}
				} else if (LoggedInUser.getRoleTypeId() == GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal()) {

					if (userMod.getCreatedBy().getRoleType().getRoleTypeId() != userMaster.getRoleType().getRoleTypeId()) {
						continue;
					}
					Map<String, Object> columnMap = new HashMap<>();
					columnMap.put(ColumnConstants.USER_ID.getConstantVal(), userMod.getCreatedBy().getUserId());
					columnMap.put(ColumnConstants.ROLEID.getConstantVal(), userMod.getCreatedByRole().getUserRoleId());

					UserRoleMaster userRoleMaster = new UserRoleMaster();
					userRoleMaster = userRoleMasterService.getDataByObject(columnMap, MethodConstants.FIND_BY_USERROLEIDIN_AND_USERIDIN.getConstantVal()).get(0);

					Map<String, List<String>> columnValueMap1 = new HashMap<>();
					List<String> entityCodes = new ArrayList<>();
					entityCodes.add(LoggedInUser.getEntityCode());
					columnValueMap1.put(ColumnConstants.ENTITY_CODE.getConstantVal(), entityCodes);

					List<EntityBean> entityBeanList = entityService.getDataByColumnValue(columnValueMap1, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());

					if (userRoleMaster.getUserEntityRole().stream().findFirst().get().getEntityBean().getEntityId() != entityBeanList.stream().findFirst().get().getEntityId()) {
						continue;
					}
				}
				//if (platform.getPlatFormId().equals(userRoleJson.getPortalId())) {
				dto = new UserRoleModDetailsDto();
				dto.setUserRoleModDetailsId(userMod.getUserRoleModifiedId());
				dto.setPortalName(platform.getPlatFormKey());

				for (RoleType roleType : roleTypeList) {
					if (roleType.getRoleTypeId().equals(userRoleJson.getRoleTypeId())) {
						dto.setRoleTypeName(roleType.getRoleTypeDesc());
					}
				}
				dto.setPortalId(userRoleJson.getPortalId());
				dto.setDeptAdmin(userRoleJson.getDeptAdmin());
				dto.setRoleDesc(userRoleJson.getRoleDesc());
				dto.setRoleName(userRoleJson.getRoleName());
				//dto.setEnglishRoleName(userRoleJson.getRoleNameDto().get(0).getRoleName());
				//dto.setHindiRoleName(userRoleJson.getRoleNameDto().get(1).getRoleName());
				dto.setCreatedByUserName(userMod.getCreatedBy().getUserName());
				dto.setCreatedByOn(userMod.getCreatedOn());
				if (userMod.getUserModify() != null) {
					dto.setModifiedByUserName(userMod.getUserModify().getUserName());
					dto.setModifiedByOn(userMod.getModifiedOn());
				} else {
					dto.setModifiedByUserName(GeneralConstants.EMPTY_DATA.getConstantVal());
					dto.setModifiedByOn(null);
				}
				dto.setActionIdFk(userMod.getActionId_FK());
				dto.setIsActive(userRoleJson.getIsActive());

				dto.setOtherPortalJSON(userMod.getRoleDetailsJson());
				dtoList.add(dto);
				//}
			}
			if (CollectionUtils.isEmpty(dtoList)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(dtoList);
		} catch (Exception e) {
			Logger.error("Exception while fetching Pending User Role info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		Logger.info("request completed to get user role list for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping("/editView")
	public ServiceResponse getEditView(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleMgmtBean userRoleMgmt, Boolean add) {
		Logger.debug("Requset editView {} with id {}", userRoleMgmt, jobProcessId);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		List<UserRoleLabel> roles = roleMapService.getEditView(userRoleMgmt);
		Logger.info("fetched roles editView {} with size  {}", roles, roles.size());
		List<UserRoleDto> roleDto = roles.stream().map(item -> {
			UserRoleDto dto = new UserRoleDto();
			BeanUtils.copyProperties(item.getUserRoleIdFk(), dto);
			Set<String> platForm = new HashSet<>();
			item.getUserRoleIdFk().getUserRolePlatFormMap().forEach(itemm -> {
				platForm.add(itemm.getPlatForm().getPlatFormCode());
			});
			dto.setPlatformCode(platForm.toString());
			return dto;
		}).collect(Collectors.toList());
		response.setResponse(roleDto);
		response.setStatus(true);
		Logger.info("Requset editView complete {}", jobProcessId);
		return response;

	}

	@PostMapping("/getHistory")
	public ServiceResponse getHistory(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleMgmtBean userRoleMgmt) {
		Logger.info("Requset getHistory {}", jobProcessId);
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(roleMapService.getRoleMapHistory(userRoleMgmt));
		response.setStatus(true);
		Logger.info("Requset getHistory complete {}", jobProcessId);
		return response;

	}

	@PostMapping("/checkNameExists")
	public ServiceResponse checkNameExists(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleMgmtBean userRoleMgmt) {
		Logger.info("Requset checkNameExists {}", jobProcessId);
		List<UserRoleLabel> rowCount = new ArrayList<>();
		if (!StringUtils.isNotBlank(userRoleMgmt.getOldRoleName()) || (!StringUtils.equals(userRoleMgmt.getOldRoleName(), userRoleMgmt.getRollName()))) {
			List<UserRoleLabel> roles = roleMapService.getEditView(userRoleMgmt);
			rowCount = roles.stream().filter(item -> StringUtils.equals(item.getUserRoleIdFk().getRoleName(), userRoleMgmt.getRollName())).collect(Collectors.toList());
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(rowCount.size());
		response.setStatus(true);
		Logger.info("Requset checkNameExists complete {}", jobProcessId);
		return response;
	}

	@PostMapping("/addReturnDeptMapping")
	public ServiceResponse addReturnMappingForDeptSuperUser(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserRoleMgmtBean userRoleMgmt) {
		Logger.info("Requseting addReturnMappingForDeptSuperUser {}", jobProcessId);
		ServiceResponse response = null;
		UserRoleReturnMapping entity = null;
		try {
			entity = new UserRoleReturnMapping(userRoleMgmt.getRoleId(), userRoleMgmt.getReturnId(), null, Boolean.TRUE);
			UserMaster user = new UserMaster();
			user.setUserId(userRoleMgmt.getUserId());
			entity.setCreatedBy(user);
			entity.setCreatedOn(new Date());
			// entity.setLastModifiedOn(new Date());
			// entity.setUserModify(user);
			entity = userRoleReturnMappingService.add(entity);

		} catch (Exception e) {
			Logger.error("Exception while mapping return to Dept Super User for job processingid {} ", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (entity.getRoleReturnId() != null) {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setStatus(true);
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0772.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0772.toString())).build();
			response.setStatus(false);
		}
		Logger.info("Requset addReturnMappingForDeptSuperUser complete {}", jobProcessId);
		return response;

	}

	@PostMapping("/initRollModified/{rollModifiedId}/{userid}")
	@SuppressWarnings("unchecked")
	public ServiceResponse getRoleMapModified(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollModifiedId") Long rollId, @PathVariable("userid") Long userId, @RequestBody UserRoleMgmtBean userRoleMgmtBean) throws Exception {
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
			List<ReturnGroupMappingDto> unFinalizedReturnGroupMappingDtos = new ArrayList<>();
			if (!ArrayUtils.isEmpty(userRoleJson.getSelectedReturn()) && !userRoleJson.getSelectedReturn()[0].equals("null") && !userRoleJson.getSelectedReturn()[0].equals("") && !userRoleJson.getSelectedReturn()[0].equals("[]")) {
				List<String> returnIds = Arrays.asList(userRoleJson.getSelectedReturn());
				List<Long> longReturnIds = returnIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				Long longArray[] = longReturnIds.toArray(new Long[0]);

				Map<String, List<ReturnDto>> map = new HashMap<>();

				List<Return> returnList = returnService.getDataByIds(longArray);
				List<ReturnDto> returnList1;
				for (Return x : returnList) {

					if (!CollectionUtils.isEmpty(x.getReturnRegulatorMapping())) {
						ReturnRegulatorMapping returnRegulatorMapping = x.getReturnRegulatorMapping().stream().filter(f -> f.getIsActive()).findAny().orElse(null);
						if (returnRegulatorMapping != null && returnRegulatorMapping.getReturnIdFk().getIsActive() && returnRegulatorMapping.getRegulatorIdFk().getIsActive()) {
							Regulator regulator = new Regulator();
							regulator.setRegulatorId(returnRegulatorMapping.getRegulatorIdFk().getRegulatorId());
							RegulatorLabel regulatorlabel = returnRegulatorMapping.getRegulatorIdFk().getRegulatorLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equals(localeCode.getLanguageCode())).findAny().orElse(null);
							if (regulatorlabel != null) {
								x.setReturnGroupName(regulatorlabel.getRegulatorLabel());
							} else {
								x.setReturnGroupName(returnRegulatorMapping.getRegulatorIdFk().getRegulatorName());
							}

						}
					}
					if (map.containsKey(x.getReturnGroupMapIdFk().getReturnGroupMapId() + "_" + x.getReturnGroupName())) {
						returnList1 = map.get(x.getReturnGroupMapIdFk().getReturnGroupMapId() + "_" + x.getReturnGroupName());
						ReturnDto returnDto = new ReturnDto();
						returnDto.setReturnId(x.getReturnId());
						returnDto.setReturnCode(x.getReturnCode());
						returnDto.setReturnName(x.getReturnName());
						returnList1.add(returnDto);
						map.put(x.getReturnGroupMapIdFk().getReturnGroupMapId() + "_" + x.getReturnGroupName(), returnList1);
					} else {
						returnList1 = new ArrayList<>();
						ReturnDto returnDto = new ReturnDto();
						returnDto.setReturnId(x.getReturnId());
						returnDto.setReturnCode(x.getReturnCode());
						returnDto.setReturnName(x.getReturnName());
						returnList1.add(returnDto);
						map.put(x.getReturnGroupMapIdFk().getReturnGroupMapId() + "_" + x.getReturnGroupName(), returnList1);
					}
				}

				for (Map.Entry<String, List<ReturnDto>> x : map.entrySet()) {
					ReturnGroupMappingDto returnGroupMappingDto = new ReturnGroupMappingDto();
					returnGroupMappingDto.setReturnGroupMapId(Long.valueOf(x.getKey().split("_")[0]));
					returnGroupMappingDto.setDefaultGroupName((x.getKey().split("_")[1]));
					returnGroupMappingDto.setReturnList(x.getValue());
					unFinalizedReturnGroupMappingDtos.add(returnGroupMappingDto);
				}
			}
			responseData.setReturnInputList(unFinalizedReturnGroupMappingDtos);
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
				if (roleReturnMap != null && responseData.getReturnInputList() != null) {
					responseData.getReturnInputList().forEach(item -> {
						item.getReturnList().forEach(returnItem -> {
							roleReturnMap.forEach(outItem -> {
								if (returnItem.getReturnId().equals(outItem.getReturnIdFk().getReturnId())) {
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
			List<LanguageMaster> languegeList = languageMasterService.getAllActiveLanguage();
			List<DropDownObject> languageInDropDown = languegeList.stream().map(item -> {
				DropDownObject dto = new DropDownObject();
				dto.setKey(item.getLanguageId());
				dto.setDisplay(item.getLanguageName());
				return dto;
			}).collect(Collectors.toList());
			responseData.setCreatedByRoleType(userRole.getCreatedBy().getRoleType().getRoleTypeId());
			responseData.setLanguageInDropDown(languageInDropDown);
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

	@PostMapping("/getPendingUserRoleModified")
	public ServiceResponse getPendingUserRoleModified(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody LogedInUser LoggedInUser) throws JsonProcessingException {
		Logger.info("request received to getPendingUserRoleModified for job processingid" + jobProcessId);
		List<UserRoleModDetailsDto> dtoList = null;
		ServiceResponse response = null;
		try {
			Map<String, Object> columnValueMap = new HashMap<>();

			List<UserRoleModified> modifiedList = userRoleModifiedService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_USER_ROLE_DATA.getConstantVal());

			if (CollectionUtils.isEmpty(modifiedList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			dtoList = new ArrayList<UserRoleModDetailsDto>();
			UserRoleModDetailsDto dto = null;
			Gson gson = new Gson();

			List<Platform> platformList = portalService.getActiveDataFor(Platform.class, null);

			List<RoleType> roleTypeList = roleTypeService.findByIsActiveTrue();

			for (UserRoleModified userMod : modifiedList) {
				UserRoleModJsonDto userRoleJson = gson.fromJson(userMod.getRoleDetailsJson(), UserRoleModJsonDto.class);

				dto = new UserRoleModDetailsDto();

				dto.setUserRoleModDetailsId(userMod.getUserRoleModifiedId());

				for (Platform platform : platformList) {
					if (platform.getPlatFormId().equals(userRoleJson.getPortalId())) {
						dto.setPortalName(platform.getPlatFormCode());
					}
				}

				for (RoleType roleType : roleTypeList) {
					if (roleType.getRoleTypeId().equals(userRoleJson.getRoleTypeId())) {
						dto.setRoleTypeName(roleType.getRoleTypeDesc());
					}
				}
				dto.setPortalId(userRoleJson.getPortalId());
				dto.setDeptAdmin(userRoleJson.getDeptAdmin());
				dto.setRoleDesc(userRoleJson.getRoleDesc());
				dto.setRoleName(userRoleJson.getRoleName());
				/*
				 * dto.setEnglishRoleName(userRoleJson.getRoleNameDto().get(0).getRoleName());
				 * dto.setHindiRoleName(userRoleJson.getRoleNameDto().get(1).getRoleName());
				 */

				dto.setCreatedByUserName(userMod.getCreatedBy().getUserName());
				dto.setCreatedByOn(userMod.getCreatedOn());
				if (userMod.getUserModify() != null) {
					dto.setModifiedByUserName(userMod.getUserModify().getUserName());
					dto.setModifiedByOn(userMod.getModifiedOn());
				} else {
					dto.setModifiedByUserName(GeneralConstants.EMPTY_DATA.getConstantVal());
					dto.setModifiedByOn(null);
				}
				dto.setRoleTypeId(userRoleJson.getRoleTypeId());
				dto.setIsActive(userRoleJson.getIsActive());
				dtoList.add(dto);
			}

			if (CollectionUtils.isEmpty(dtoList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(dtoList);
		} catch (Exception e) {
			Logger.error("Exception while fetching userRoleModified list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		Logger.info("request completed to getPendingUserRoleModified list for job processingid" + jobProcessId);
		return response;

	}

	@GetMapping("/getPreviousUserRole/{userRoleId}")
	public ServiceResponse getPreviousUserRole(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "JobProcessingId") String uuid, @PathVariable Long userRoleId) throws JsonProcessingException {
		Logger.info("request received to getPreviousUserRole for job processingid" + jobProcessId);
		ServiceResponse response = null;
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.ROLEID.getConstantVal(), userRoleId);

			List<UserRoleModified> modifiedList = userRoleModifiedService.getDataByObject(columnValueMap, MethodConstants.GET_PREVIOUS_USER_ROLE_DATA.getConstantVal());

			if (CollectionUtils.isEmpty(modifiedList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			UserRoleModDetailsDto dto = null;
			Gson gson = new Gson();

			List<Platform> platformList = portalService.getActiveDataFor(Platform.class, null);

			List<RoleType> roleTypeList = roleTypeService.findByIsActiveTrue();

			for (UserRoleModified userMod : modifiedList) {
				UserRoleModJsonDto userRoleJson = gson.fromJson(userMod.getRoleDetailsJson(), UserRoleModJsonDto.class);

				dto = new UserRoleModDetailsDto();

				dto.setUserRoleModDetailsId(userMod.getUserRoleModifiedId());

				for (Platform platform : platformList) {
					if (platform.getPlatFormId().equals(userRoleJson.getPortalId())) {
						dto.setPortalName(platform.getPlatFormCode());
					}
				}

				for (RoleType roleType : roleTypeList) {
					if (roleType.getRoleTypeId().equals(userRoleJson.getRoleTypeId())) {
						dto.setRoleTypeName(roleType.getRoleTypeDesc());
					}
				}
				dto.setPortalId(userRoleJson.getPortalId());
				dto.setDeptAdmin(userRoleJson.getDeptAdmin());
				dto.setRoleDesc(userRoleJson.getRoleDesc());
				dto.setRoleName(userRoleJson.getRoleName());
				/*
				 * dto.setEnglishRoleName(userRoleJson.getRoleNameDto().get(0).getRoleName());
				 * dto.setHindiRoleName(userRoleJson.getRoleNameDto().get(1).getRoleName());
				 */

				dto.setCreatedByUserName(userMod.getCreatedBy().getUserName());
				dto.setCreatedByOn(userMod.getCreatedOn());
				if (userMod.getUserModify() != null) {
					dto.setModifiedByUserName(userMod.getUserModify().getUserName());
					dto.setModifiedByOn(userMod.getModifiedOn());
				} else {
					dto.setModifiedByUserName(GeneralConstants.EMPTY_DATA.getConstantVal());
					dto.setModifiedByOn(null);
				}
				dto.setRoleTypeId(userRoleJson.getRoleTypeId());
				dto.setIsActive(userRoleJson.getIsActive());
				if (userRoleJson.getSelectedReturn() != null) {
					List<Return> list = new ArrayList<>();
					List<Long> longList = Stream.of(userRoleJson.getSelectedReturn()).map(Long::valueOf).collect(Collectors.toList());
					Long[] ids = new Long[longList.size()];
					for (int i = 0; i < longList.size(); i++) {
						ids[i] = longList.get(i);
					}
					list = returnService.getDataByIds(ids);
					dto.setSelectedReturnText(list.stream().map(Return::getReturnCode).collect(Collectors.joining(",")));
				}
				if (userRoleJson.getSelectedEntity() != null) {
					List<EntityBean> list = new ArrayList<>();
					List<Long> longList = Stream.of(userRoleJson.getSelectedEntity()).map(Long::valueOf).collect(Collectors.toList());
					Long[] ids = new Long[longList.size()];
					for (int i = 0; i < longList.size(); i++) {
						ids[i] = longList.get(i);
					}
					list = entityService.getDataByIds(ids);
					dto.setSelectedEntityText(list.stream().map(EntityBean::getEntityCode).collect(Collectors.joining(",")));
				}
				if (userRoleJson.getSelectedActivity() != null) {
					List<ActivityApplicableMenu> list = new ArrayList<>();
					List<Long> longList = Stream.of(userRoleJson.getSelectedActivity()).map(Long::valueOf).collect(Collectors.toList());
					Long[] ids = new Long[longList.size()];
					for (int i = 0; i < longList.size(); i++) {
						ids[i] = longList.get(i);
					}
					list = activityApplicableMenuRepo.findAllById(Arrays.asList(ids));
					dto.setSelectedActivityText(list.stream().map(i -> i.getActivityIdFk().getActivityDesc()).collect(Collectors.joining(",")));
				}
				if (userRoleJson.getSelectedMenu() != null) {
					List<Menu> list = new ArrayList<>();
					List<Long> longList = Stream.of(userRoleJson.getSelectedMenu()).map(Long::valueOf).collect(Collectors.toList());
					Long[] ids = new Long[longList.size()];
					for (int i = 0; i < longList.size(); i++) {
						ids[i] = longList.get(i);
					}
					list = menuRepo.findAllById(Arrays.asList(ids));
					dto.setSelectedMenuText(list.stream().map(Menu::getDefaultMenu).collect(Collectors.joining(",")));
				}
			}

			if (dto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
			}

			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(dto);
		} catch (Exception e) {
			Logger.error("Exception in getPreviousUserRole{} for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		Logger.info("request completed to getPreviousUserRole{} for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping("/initUserRole/{rollId}/{userid}")
	@SuppressWarnings("unchecked")
	public ServiceResponse getRoleMapUseRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("rollId") Long rollId, @PathVariable("userid") Long userId, @RequestBody UserRoleMgmtBean userRoleMgmtBean) throws Exception {
		Logger.info("Requseting getRoleMap {}", jobProcessId);
		try {
			UserRoleMgmtBean responseData = new UserRoleMgmtBean();
			responseData.setCurrentLocale(userRoleMgmtBean.getCurrentLocale());
			LanguageMaster localeCode = languageMasterService.getDataById(userRoleMgmtBean.getCurrentLocale());
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
			Collection<Platform> platformList = (Collection<Platform>) platFormController.getAllPortal(jobProcessId, userId, rollId).getResponse();
			//Collection<Platform> platformList = (Collection<Platform>) platFormController.getPortalMultipleRole(jobProcessId, userId).getResponse();

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
			EntityMasterDto entityMasterDto = new EntityMasterDto();
			entityMasterDto.setActive(true);
			entityMasterDto.setRoleId(rollId);
			entityMasterDto.setUserId(userId);
			entityMasterDto.setIsCategoryWiseResponse(true);
			entityMasterDto.setLanguageCode(localeCode.getLanguageCode());
			List<Category> categoryList = (List<Category>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
			Logger.info("Entity fetch complete {}", jobProcessId);
			List<DropDownGroup> entityDropDown = new ArrayList<>();
			if (categoryList != null && !categoryList.isEmpty()) {
				for (Category item : categoryList) {
					if (item.getSubCategory() != null) {
						for (SubCategory subCat : item.getSubCategory()) {
							DropDownGroup grp = new DropDownGroup();
							grp.setDisplay(subCat.getSubCategoryName());
							for (EntityBean ent : subCat.getEntity()) {
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
			ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
			returnGroupMappingRequest.setIsActive(true);
			returnGroupMappingRequest.setUserId(userId);
			returnGroupMappingRequest.setLangId(localeCode.getLanguageId());
			returnGroupMappingRequest.setRoleId(rollId);

			responseData.setReturnInputList((List<ReturnGroupMappingDto>) returnGroupController.getReturnGroupsUserRole(jobProcessId, returnGroupMappingRequest).getResponse());
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
				if (roleReturnMap != null && responseData.getReturnInputList() != null) {
					responseData.getReturnInputList().forEach(item -> {
						item.getReturnList().forEach(returnItem -> {
							roleReturnMap.forEach(outItem -> {
								if (returnItem.getReturnId().equals(outItem.getReturnIdFk().getReturnId())) {
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

	/**
	 * This method is used to get user role list on basis of logged in user role type id Tested
	 */
	@PostMapping("/getEditUserRoleList")
	public ServiceResponse getEditUserRoleList(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @RequestBody UserInfoDTO userInfo) {
		Logger.info("This is getEditUserRoleList method", jobProcessId);
		List<UserRoleDto> userRoleDTOList = null;
		try {
			if (UtilMaster.isEmpty(userInfo.getLangCode())) {
				userInfo.setLangCode(GeneralConstants.DEFAULT_LANG_CODE.getConstantVal());
			}

			userRoleDTOList = roleMapService.getEditUserRoleList(userInfo);

			if (CollectionUtils.isEmpty(userRoleDTOList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0312.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0312.toString())).build();
			}

		} catch (Exception e) {
			Logger.error("Exception in getEditUserRoleList{} for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
		Logger.info("This is getEditUserRoleList method end", jobProcessId);
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(userRoleDTOList).build();

	}

	/**
	 * This method is used to get user role list on basis of logged in user role type id Tested
	 */
	@PostMapping("/getAddUserRoleList")
	public ServiceResponse getAddUserRoleList(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @RequestBody UserInfoDTO userInfo) {
		Logger.info("This is getAddUserRoleList method", jobProcessId);
		List<UserRoleDto> userRoleDTOList = null;
		try {

			if (UtilMaster.isEmpty(userInfo.getLangCode())) {
				userInfo.setLangCode(GeneralConstants.DEFAULT_LANG_CODE.getConstantVal());
			}

			userRoleDTOList = roleMapService.getAddUserRoleList(userInfo);

			if (CollectionUtils.isEmpty(userRoleDTOList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0312.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0312.toString())).build();
			}

		} catch (Exception e) {
			Logger.error("Exception in getAddUserRoleList{} for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
		Logger.info("This is getAddUserRoleList method end", jobProcessId);
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(userRoleDTOList).build();

	}

	@PostMapping("/checkPendingStatus/{editUserId}/{actionId}/{adminStatus}")
	public ServiceResponse checkPendingStatus(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("editUserId") Long editUserId, @PathVariable("actionId") Integer actionId, @PathVariable("adminStatus") Integer adminStatus) {
		Logger.info("This is checkPendingStatus method", jobProcessId);
		List<UserModifiedDto> userModifiedDtoList = null;

		try {

			userModifiedDtoList = roleMapService.checkPendingStatus(editUserId, actionId, adminStatus);

			if (!CollectionUtils.isEmpty(userModifiedDtoList)) {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(userModifiedDtoList).build();
			}
		} catch (Exception e) {
			Logger.error("Exception in checkPendingStatus{} for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
		Logger.info("This is checkPendingStatus method end", jobProcessId);
		return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();

	}

	@PostMapping("/getModifiedEntitySelectListV2/{userModifiedId}/{langCode}")
	public ServiceResponse getModifiedEntitySelectListV2(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("userModifiedId") Long userModifiedId, @PathVariable("langCode") String langCode) {
		Logger.info("This is getModifiedEntitySelectListV2 API: " + uuid);
		List<DropDownGroup> entityDropDownList = null;
		try {
			entityDropDownList = roleMapService.getModifiedEntitySelectListV2(userModifiedId, langCode);

			if (CollectionUtils.isEmpty(entityDropDownList)) {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
		Logger.info("This is getModifiedEntitySelectListV2 API end");
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(entityDropDownList).build();
	}

	@PostMapping("/getModifiedNBFCEntitySelectListV2/{userModId}/{langCode}")
	public ServiceResponse getModifiedNBFCEntitySelectListV2(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("userModId") Long userModId, @PathVariable("langCode") String langCode) {
		Logger.info("This is getModifiedNBFCEntitySelectListV2 API: " + uuid);
		List<DropDownGroup> entityDropDownList = null;
		try {
			entityDropDownList = roleMapService.getModifiedNBFCEntitySelectListV2(userModId, langCode);

			if (CollectionUtils.isEmpty(entityDropDownList)) {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
		Logger.info("This is getModifiedNBFCEntitySelectListV2 API end");
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(entityDropDownList).build();
	}

	@PostMapping("/getModifiedRoleSelectListV2/{userModifiedId}/{langCode}")
	public ServiceResponse getModifiedRoleSelectListV2(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("userModifiedId") Long userModifiedId, @PathVariable("langCode") String langCode) {
		Logger.info("This is getModifiedRoleSelectListV2 API: " + uuid);

		List<UserRoleDto> userRoleDtoList = null;
		try {
			userRoleDtoList = roleMapService.getModifiedRoleSelectListV2(userModifiedId, langCode);

			if (CollectionUtils.isEmpty(userRoleDtoList)) {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
			}

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		Logger.info("This is getModifiedRoleSelectListV2 API end");
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(userRoleDtoList).build();
	}

}
