package com.iris.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.sql.DataSource;
import com.iris.dto.DropDownGroup;
import com.iris.dto.DropDownObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.MenuRoleMapDto;
import com.iris.dto.RoleMapHistory;
import com.iris.dto.UserInfoDTO;
import com.iris.dto.UserModDetailsJsonBean;
import com.iris.dto.UserModifiedDto;
import com.iris.dto.UserRoleDto;
import com.iris.dto.UserRoleEntityMappingDto;
import com.iris.dto.UserRoleLabelDto;
import com.iris.dto.UserRoleMgmtBean;
import com.iris.dto.UserRolePlatformDto;
import com.iris.dto.UserRoleReturnMappingDto;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.Menu;
import com.iris.model.MenuRoleMap;
import com.iris.model.Return;
import com.iris.model.RoleEntityMappingMod;
import com.iris.model.RoleMenuMappingMod;
import com.iris.model.RoleReturnMappingMod;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.model.UserModified;
import com.iris.model.UserRole;
import com.iris.model.UserRoleActivityMap;
import com.iris.model.UserRoleEntityMapping;
import com.iris.model.UserRoleLabel;
import com.iris.model.UserRoleLabelModified;
import com.iris.model.UserRoleModified;
import com.iris.model.UserRolePlatFormMap;
import com.iris.model.UserRolePlatformMapModified;
import com.iris.model.UserRoleReturnMapping;
import com.iris.model.WorkFlowActivity;
import com.iris.repository.EntityRepo;
import com.iris.repository.LanguageMasterRepo;
import com.iris.repository.MenuRepo;
import com.iris.repository.MenuRoleMapRepo;
import com.iris.repository.ReturnRepo;
import com.iris.repository.RoleEntityMapModRepo;
import com.iris.repository.RoleMenuMapModRepo;
import com.iris.repository.RolePlatMapModRepo;
import com.iris.repository.RoleReturnMapModRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.repository.UserModifiedRepo;
import com.iris.repository.UserRoleActivityMapRepo;
import com.iris.repository.UserRoleEntityMappingRepo;
import com.iris.repository.UserRoleLabelModifiedRepo;
import com.iris.repository.UserRoleLabelRepo;
import com.iris.repository.UserRoleModifiedRepo;
import com.iris.repository.UserRolePortalMapRepo;
import com.iris.repository.UserRoleRepo;
import com.iris.repository.UserRoleReturnMappingRepo;
import com.iris.repository.WorkFlowActivityRepo;
import com.iris.util.AESV2;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;

@Service
public class RoleMapService {
	static final Logger LOGGER = LogManager.getLogger(RoleMapService.class);

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	UserRoleRepo userRoleRepo;

	@Autowired
	RoleTypeService roleTypeService;

	@Autowired
	UserRoleActivityMapRepo userRoleActivityMapRepo;

	@Autowired
	EntityManager em;

	@Autowired
	UserRoleLabelModifiedRepo userRoleLabelModifiedRepo;

	@Autowired
	UserRoleModifiedRepo userRoleModifiedRepo;

	@Autowired
	RoleEntityMapModRepo roleEntityMapModRepo;

	@Autowired
	PortalRoleService portalRoleService;

	@Autowired
	RolePlatMapModRepo rolePlatMapModRepo;

	@Autowired
	RoleReturnMapModRepo roleReturnMapModRepo;
	@Autowired
	PortalService portalService;

	@Autowired
	UserMasterService userMasterService;

	@Autowired
	UserRoleLabelRepo userRoleLabelRepo;

	@Autowired
	LanguageMasterRepo languageMasterRepo;

	@Autowired
	UserRoleActivityMapService userRoleActivityMapService;

	@Autowired
	UserRoleReturnMappingRepo usserRoleReturnMappingRepo;

	@Autowired
	ReturnRepo returnRepo;

	@Autowired
	MenuRepo menuRepo;

	@Autowired
	MenuRoleMapService menuRoleMapService;

	@Autowired
	MenuRoleMapRepo menuRoleMapRepo;

	@Autowired
	RoleMenuMapModRepo roleMenuMapModRepo;

	@Autowired
	UserRolePortalMapRepo userRolePortalMapRepo;

	@Autowired
	EntityRepo entityRepo;

	@Autowired
	UserRoleEntityMappingRepo userRoleEntityMappingRepo;

	@Autowired
	WorkFlowActivityRepo workFlowActivityRepo;
	@Autowired
	DataSource datasource;

	@Autowired
	UserMasterRepo userMasterRepo;

	@Autowired
	UserModifiedRepo userModifiedRepo;

	public Boolean saveOrUpdate(UserRoleMgmtBean userRoleMgmt, Boolean eidt) throws JsonProcessingException {
		Date auditDate = new Date();
		if (userRoleMgmt.getRoleIdExisting() != null && userRoleMgmt.getRoleIdExisting() > 0) {
			editData(userRoleMgmt, auditDate);
			return true;
		}
		UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
		//save into user role,role Type
		UserRole role = prepareAndSaveRole(userRoleMgmt, auditDate, userMaster);
		//save into user role label
		if (role != null) {
			prepareAndSaveRoleLabel(role.getUserRoleId());
		}
		//save user role platform mapping
		prepareAndSavePlarForm(userRoleMgmt, auditDate, userMaster, role);
		if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
			//save user role return mapping
			prepareAndSaveRoleReturnMap(userRoleMgmt, auditDate, userMaster, role);
			//save user role menu mapping
			prepareAndRoleMenuMap(userRoleMgmt, auditDate, userMaster, role);
			//Save user role Entity mapping
			prepareAndRoleEntityuMap(userRoleMgmt, auditDate, userMaster, role);
			//Save user role Activity mapping
			prepareAndSaveActivity(userRoleMgmt, auditDate, userMaster, role);
		}
		return true;
	}

	public Boolean saveOrUpdateInApproval(UserRoleMgmtBean userRoleMgmt, UserMaster createdBy, Date createdOn, UserMaster approvedBy, Date approvedOn, UserMaster modifiedBy, Date modifiedOn, Boolean eidt) throws JsonProcessingException {
		Date auditDate = new Date();
		if (userRoleMgmt.getRoleIdExisting() != null && userRoleMgmt.getRoleIdExisting() > 0) {
			editDataInApproval(userRoleMgmt, auditDate, approvedBy, modifiedBy, modifiedOn);
			return true;
		}
		UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
		//save into user role,role Type
		UserRole role = prepareAndSaveRoleInApproval(userRoleMgmt, auditDate, userMaster, createdBy, createdOn, approvedBy, approvedOn);
		//save into user role label
		if (role != null) {
			prepareAndSaveRoleLabel(role.getUserRoleId());
		}
		//save user role platform mapping
		prepareAndSavePlarForm(userRoleMgmt, auditDate, userMaster, role);
		if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
			//save user role return mapping
			prepareAndSaveRoleReturnMapModified(userRoleMgmt, auditDate, userMaster, role);
			//save user role menu mapping
			prepareAndRoleMenuMapModified(userRoleMgmt, auditDate, userMaster, role);
			//Save user role Entity mapping
			prepareAndRoleEntityMapModified(userRoleMgmt, auditDate, userMaster, role);
			//Save user role Activity mapping
			prepareAndSaveActivityModified(userRoleMgmt, auditDate, userMaster, role);
		}
		return true;
	}

	private void editData(UserRoleMgmtBean userRoleMgmt, Date auditDate) throws JsonProcessingException {
		UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
		//save into user role,role Type
		UserRole role = userRoleService.getDataById(userRoleMgmt.getRoleIdExisting());
		//saveRoleHistory(role,auditDate,userMaster);
		role = updateRole(role, userRoleMgmt, auditDate, userMaster);
		//save into user role label
		saveRoleLabelHistory(role, auditDate, userMaster);
		updateRoleLabel(userRoleMgmt, auditDate, userMaster, role);
		//prepareAndSaveRoleLabel(userRoleMgmt,auditDate,userMaster,role);
		//save user role platform mapping
		savePlatFormHistor(role, auditDate, userMaster);
		//cancelPlarForm(role);
		updateOrSavePlarForm(userRoleMgmt, auditDate, userMaster, role);
		//prepareAndSavePlarForm(userRoleMgmt,auditDate,userMaster,role);
		if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
			//save user role return mapping
			saveRoleReturnMapHistory(role, auditDate, userMaster);
			cancelRoleReturnMap(role);
			updateOrSaveRoleReturnMap(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndSaveRoleReturnMap(userRoleMgmt,auditDate,userMaster,role);
			//save user role menu mapping
			saveRoleMenuHistory(role, auditDate, userMaster);
			cancelRoleMenuMap(role);
			updateOrSaveRoleMenuMap(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndRoleMenuMap(userRoleMgmt,auditDate,userMaster,role);
			//Save user role Entity mapping
			saveRoleEntityHistory(role, auditDate, userMaster);
			cancelRoleEntityuMap(role);
			updateOrSaveRoleEntityuMap(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndRoleEntityuMap(userRoleMgmt,auditDate,userMaster,role);

			cancelRoleActivity(role);
			updateOrSaveActivity(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndSaveActivity(userRoleMgmt,auditDate,userMaster,role);
		}
	}

	private void editDataInApproval(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserMaster modifiedBy, Date modifiedOn) throws JsonProcessingException {
		//UserMaster userMaster = userMasterService.getDataById(userRoleMgmt.getUserId());
		//save into user role,role Type
		UserRole role = userRoleService.getDataById(userRoleMgmt.getRoleIdExisting());
		//saveRoleHistory(role,auditDate,userMaster);---------------------------------to maintain modify history
		role = updateRoleInApproval(role, userRoleMgmt, auditDate, userMaster, modifiedBy, modifiedOn);
		//save into user role label
		//saveRoleLabelHistory(role,auditDate,userMaster);
		updateRoleLabel(userRoleMgmt, auditDate, userMaster, role);
		//prepareAndSaveRoleLabel(userRoleMgmt,auditDate,userMaster,role);
		//save user role platform mapping
		//savePlatFormHistor(role,auditDate,userMaster);
		//cancelPlarForm(role);
		updateOrSavePlarForm(userRoleMgmt, auditDate, userMaster, role);
		//prepareAndSavePlarForm(userRoleMgmt,auditDate,userMaster,role);
		//save user role return mapping
		//saveRoleReturnMapHistory(role,auditDate,userMaster);
		if (userRoleMgmt.getPortal().equals(GeneralConstants.SADP_PORTAL_ID.getConstantLongVal())) {
			cancelRoleReturnMap(role);
			updateOrSaveRoleReturnMapModified(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndSaveRoleReturnMap(userRoleMgmt,auditDate,userMaster,role);
			//save user role menu mapping
			//saveRoleMenuHistory(role,auditDate,userMaster);
			cancelRoleMenuMap(role);
			updateOrSaveRoleMenuMapModified(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndRoleMenuMap(userRoleMgmt,auditDate,userMaster,role);
			//Save user role Entity mapping
			//saveRoleEntityHistory(role,auditDate,userMaster);
			cancelRoleEntityuMap(role);
			updateOrSaveRoleEntityuMapModified(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndRoleEntityuMap(userRoleMgmt,auditDate,userMaster,role);

			cancelRoleActivity(role);
			updateOrSaveActivityModified(userRoleMgmt, auditDate, userMaster, role);
			//prepareAndSaveActivity(userRoleMgmt,auditDate,userMaster,role);
		}
	}

	private void prepareAndSaveActivity(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedActivityList = new ArrayList<>();
		String[] allotedActivity = userRoleMgmt.getSelectedActivity();
		if (StringUtils.equals(allotedActivity[0], "null") || StringUtils.equals(allotedActivity[0], "[]") || StringUtils.equals(allotedActivity[0], "")) {
			return;
		}
		Arrays.stream(allotedActivity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedActivityList.add(Long.valueOf(innerItem))));
		List<UserRoleActivityMap> allotedList = new ArrayList<>();
		allotedActivityList.forEach(item -> {
			UserRoleActivityMap entity = new UserRoleActivityMap();
			entity.setIsActive(true);
			entity.setRole(role);
			entity.setCreatedBy(userMaster);
			WorkFlowActivity activity = new WorkFlowActivity();
			activity.setActivityId(item);
			entity.setWorkFlowActivity(activity);
			entity.setCreatedOn(auditDate);
			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);

			allotedList.add(entity);
		});

		userRoleActivityMapRepo.saveAll(allotedList);

	}

	private void updateOrSaveActivity(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedActivityList = new ArrayList<>();
		String[] allotedActivity = userRoleMgmt.getSelectedActivity();
		if (StringUtils.equals(allotedActivity[0], "null") || StringUtils.equals(allotedActivity[0], "[]") || StringUtils.equals(allotedActivity[0], "")) {
			return;
		}
		Arrays.stream(allotedActivity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedActivityList.add(Long.valueOf(innerItem))));
		List<UserRoleActivityMap> allotedList = new ArrayList<>();
		List<UserRoleActivityMap> existing = userRoleActivityMapRepo.findByRoleUserRoleId(role.getUserRoleId());
		allotedActivityList.forEach(item -> {
			UserRoleActivityMap entity = new UserRoleActivityMap();
			entity.setIsActive(true);
			entity.setRole(role);

			WorkFlowActivity activity = new WorkFlowActivity();
			activity.setActivityId(item);
			entity.setWorkFlowActivity(activity);

			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);
			if (existing != null) {
				Optional<UserRoleActivityMap> present = existing.stream().filter(inner -> inner.getWorkFlowActivity().getActivityId().compareTo(item) == 0).findAny();
				if (present.isPresent()) {
					entity.setUserRoleActivityMapId(present.get().getUserRoleActivityMapId());
				}
			} else {
				entity.setCreatedBy(userMaster);
				entity.setCreatedOn(auditDate);
			}
			allotedList.add(entity);
		});

		userRoleActivityMapRepo.saveAll(allotedList);

	}

	private void cancelRoleActivity(UserRole role) {
		userRoleActivityMapService.cancelUserRoleActivityMapping(role.getUserRoleId());
	}

	private void saveRoleLabelHistory(UserRole role, Date auditDate, UserMaster userMaster) throws JsonProcessingException {
		UserRoleLabelModified entity = new UserRoleLabelModified();
		entity.setLastModifiedOn(auditDate);
		entity.setUserRole(role);
		entity.setModifiedBy(userMaster);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		List<UserRoleLabelDto> jsoValue = role.getUsrRoleLabelSet().stream().map(item -> {
			UserRoleLabelDto dto = new UserRoleLabelDto();
			dto.setLangIdFk(item.getLangIdFk().getLanguageId());
			//	dto.setLastApprovedBy(item.getLastApprovedBy().getUserId());
			dto.setLastModifiedOn(item.getLastModifiedOn());
			dto.setLastUpdateOn(item.getLastUpdateOn());
			dto.setRoleIdKey(item.getRoleIdKey());
			//	dto.setUserModify(item.getUserModify().getUserId());
			dto.setUserRoleLabel(item.getUserRoleLabel());
			dto.setUserRoleLabelId(item.getUserRoleLabelId());
			return dto;
		}).collect(Collectors.toList());

		JsonNode node = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(jsoValue));
		entity.setPrevData(node);
		userRoleLabelModifiedRepo.save(entity);

	}

	private void saveRoleHistory(UserRole role, Date auditDate, UserMaster userMaster) throws JsonProcessingException {
		UserRoleModified entity = new UserRoleModified();
		//entity.setLastModifiedOn(auditDate);
		//entity.setUserRole(role);
		//entity.setModifiedBy(userMaster);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		UserRoleDto dto = new UserRoleDto();
		BeanUtils.copyProperties(role, dto);
		JsonNode node = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(dto));
		///entity.setPrevData(node);
		userRoleModifiedRepo.save(entity);
	}

	private void savePlatFormHistor(UserRole role, Date auditDate, UserMaster userMaster) {
		List<UserRolePlatFormMap> platFpormList = portalRoleService.UserRolePlatFormMapByUser(role.getUserRoleId());
		if (platFpormList == null || (platFpormList != null && platFpormList.size() == 0)) {
			return;
		}
		List<UserRolePlatformMapModified> prevData = platFpormList.stream().map(item -> {
			UserRolePlatformMapModified dto = new UserRolePlatformMapModified();
			dto.setUserRoleIdFk(role);
			dto.setLastModifiedByFk(userMaster);
			dto.setLastModifiedOn(auditDate);
			dto.setUserRolePlatMapModId(item.getUserRolePlatFormMapId());
			dto.setUserRolePlatMapping(String.valueOf(item.getPlatForm().getPlatFormId()));
			return dto;
		}).collect(Collectors.toList());

		rolePlatMapModRepo.saveAll(prevData);

	}

	private void saveRoleMenuHistory(UserRole role, Date auditDate, UserMaster userMaster) throws JsonProcessingException {
		List<MenuRoleMap> roleMenuList = menuRoleMapRepo.findByUserRoleIdFkUserRoleIdAndIsActiveTrueOrderByMenuIDFkParentMenuMenuIdAsc(role.getUserRoleId());
		if (roleMenuList == null || !roleMenuList.isEmpty()) {
			return;
		}
		List<MenuRoleMapDto> prevData = roleMenuList.stream().map(item -> {
			MenuRoleMapDto dto = new MenuRoleMapDto();
			dto.setMenuIDFk(item.getMenuIDFk().getMenuId());
			dto.setMenuRoleId(item.getMenuRoleId());
			Long modifiedBy = item.getModifiedBy() != null ? item.getModifiedBy().getUserId() : null;
			dto.setModifiedBy(modifiedBy);
			dto.setIsActive(item.getIsActive());
			dto.setUserRoleIdFk(item.getUserRoleIdFk().getUserRoleId());
			return dto;
		}).collect(Collectors.toList());
		RoleMenuMappingMod entity = new RoleMenuMappingMod();
		entity.setLastModifiedOn(auditDate);
		entity.setModifiedBy(userMaster);
		entity.setUserRole(role);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(prevData));
		entity.setPrevData(node);
		roleMenuMapModRepo.save(entity);

	}

	private void saveRoleReturnMapHistory(UserRole role, Date auditDate, UserMaster userMaster) throws JsonProcessingException {
		List<UserRoleReturnMapping> roleReturnList = usserRoleReturnMappingRepo.findByRoleIdFkUserRoleIdAndIsActiveTrue(role.getUserRoleId());
		if (roleReturnList == null || (roleReturnList != null && roleReturnList.size() == 0)) {
			return;
		}
		List<UserRoleReturnMappingDto> prevData = roleReturnList.stream().map(item -> {
			UserRoleReturnMappingDto dto = new UserRoleReturnMappingDto();
			dto.setReturnIdFk(item.getReturnIdFk().getReturnId());
			dto.setIsActive(item.getIsActive());
			dto.setRoleIdFk(item.getRoleIdFk().getUserRoleId());
			dto.setRoleReturnId(item.getRoleReturnId());
			return dto;
		}).collect(Collectors.toList());
		RoleReturnMappingMod entity = new RoleReturnMappingMod();
		entity.setLastModifiedOn(auditDate);
		entity.setModifiedBy(userMaster);
		entity.setUserRole(role);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(prevData));
		entity.setPrevData(node);
		roleReturnMapModRepo.save(entity);

	}

	private void saveRoleEntityHistory(UserRole role, Date auditDate, UserMaster userMaster) throws JsonProcessingException {
		List<UserRoleEntityMapping> roleentityList = userRoleEntityMappingRepo.findByUserRoleUserRoleIdAndIsActiveTrue(role.getUserRoleId());
		if (roleentityList == null || (roleentityList != null && roleentityList.size() == 0)) {
			return;
		}
		List<UserRoleEntityMappingDto> prevData = roleentityList.stream().map(item -> {
			UserRoleEntityMappingDto dto = new UserRoleEntityMappingDto();
			dto.setEntity(item.getEntity().getEntityId());
			dto.setActive(item.isActive());
			dto.setUserRole(item.getUserRole().getUserRoleId());
			dto.setUserRoleEntMapId(item.getUserRoleEntMapId());
			return dto;
		}).collect(Collectors.toList());
		RoleEntityMappingMod entity = new RoleEntityMappingMod();
		entity.setLastModifiedOn(auditDate);
		entity.setModifiedBy(userMaster);
		entity.setUserRole(role);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(prevData));
		entity.setPrevData(node);
		roleEntityMapModRepo.save(entity);

	}

	private void cancelRoleEntityuMap(UserRole role) {
		userRoleEntityMappingRepo.cancelUserRoleEntityMapping(role.getUserRoleId());
	}

	private void cancelRoleMenuMap(UserRole role) {
		menuRoleMapRepo.cancelMenuRoleMap(role.getUserRoleId());

	}

	private void cancelRoleReturnMap(UserRole role) {
		usserRoleReturnMappingRepo.cancelUserRoleReturnMapping(role.getUserRoleId());

	}

	/*
	 * private void cancelPlarForm(UserRole role) { UserRolePlatFormMap bean = new
	 * UserRolePlatFormMap(); bean.setUserRole(role);
	 * portalRoleService.deleteData(bean);
	 * 
	 * }
	 */

	private void updateRoleLabel(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		Set<UserRoleLabel> enLabels = role.getUsrRoleLabelSet();
		enLabels.forEach(enLabel -> {
			enLabel.setUserRoleLabel(userRoleMgmt.getRollName());
			enLabel.setUserModify(userMaster);
			enLabel.setLastUpdateOn(auditDate);
			enLabel.setLastModifiedOn(auditDate);
			enLabel.setUserRoleIdFk(role);
			userRoleLabelRepo.save(enLabel);
		});

	}

	private UserRole updateRole(UserRole role, UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster) {
		UserRole entity = role;
		entity.setCreatedOn(role.getCreatedOn());
		entity.setIsActive(true);
		entity.setRoleName(userRoleMgmt.getRollName());
		entity.setLastUpdateOn(auditDate);
		if (userRoleMgmt.getIsActive() != null && userRoleMgmt.getIsActive().equals(1l)) {
			entity.setIsActive(true);
		} else {
			entity.setIsActive(false);
		}
		//entity.setLastApprovedBy(userMaster);
		//entity.setLastApprovedOn(auditDate);
		entity.setRoleDesc(userRoleMgmt.getRoleDesc());
		entity.setRoleType(roleTypeService.getDataById(userRoleMgmt.getRoleType()));
		if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(2)) == 0) {
			entity.setLinkToEntity(true);
			entity.setLinkToAuditor(false);
		} else if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(3)) == 0) {
			entity.setLinkToEntity(false);
			entity.setLinkToAuditor(true);
		} else {
			entity.setLinkToAuditor(false);
			entity.setLinkToEntity(false);

		}
		if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin()) && StringUtils.equals("1", userRoleMgmt.getDeptAdmin())) {
			entity.setDeptAdmin(userRoleMgmt.getDeptAdmin());
		} else {
			entity.setDeptAdmin("0");
		}
		entity.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
		entity.setRolePriority(12l);
		entity.setUserModify(userMaster);
		entity.setUser(userMaster);
		entity.setLastModifiedOn(auditDate);
		return userRoleService.add(entity);
	}

	private UserRole updateRoleInApproval(UserRole role, UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserMaster modifiedBy, Date modifiedOn) {
		UserRole entity = role;
		entity.setLastApprovedBy(userMaster);
		entity.setCreatedOn(role.getCreatedOn());
		//entity.setIsActive(true);
		entity.setRoleName(userRoleMgmt.getRollName());
		entity.setLastUpdateOn(auditDate);
		if (userRoleMgmt.getIsActive() != null && userRoleMgmt.getIsActive().equals(1l)) {
			entity.setIsActive(true);
		} else {
			entity.setIsActive(false);
		}

		entity.setLastApprovedOn(auditDate);
		entity.setRoleDesc(userRoleMgmt.getRoleDesc());
		entity.setRoleType(roleTypeService.getDataById(userRoleMgmt.getRoleType()));
		if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(2)) == 0) {
			entity.setLinkToEntity(true);
			entity.setLinkToAuditor(false);
		} else if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(3)) == 0) {
			entity.setLinkToEntity(false);
			entity.setLinkToAuditor(true);
		} else {
			entity.setLinkToAuditor(false);
			entity.setLinkToEntity(false);

		}
		if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin()) && StringUtils.equals("1", userRoleMgmt.getDeptAdmin())) {
			entity.setDeptAdmin(userRoleMgmt.getDeptAdmin());
		} else {
			entity.setDeptAdmin("0");
		}
		entity.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
		entity.setRolePriority(12l);
		entity.setUserModify(modifiedBy);
		entity.setLastModifiedOn(modifiedOn);
		//entity.setUser(userMaster);

		return userRoleService.add(entity);

	}

	private void updateOrSaveRoleEntityuMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedEntityList = new ArrayList<>();
		String[] allotedEntity = userRoleMgmt.getSelectedEntity();
		if (StringUtils.equals(allotedEntity[0], "null") || StringUtils.equals(allotedEntity[0], "[]") || StringUtils.equals(allotedEntity[0], "")) {
			return;
		}
		Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedEntityList.add(Long.valueOf(innerItem))));
		List<EntityBean> menuList = entityRepo.findAllById(allotedEntityList);
		List<UserRoleEntityMapping> existing = userRoleEntityMappingRepo.findByUserRoleUserRoleId(role.getUserRoleId());
		List<UserRoleEntityMapping> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			UserRoleEntityMapping entity = new UserRoleEntityMapping();
			entity.setActive(true);
			entity.setEntity(item);
			entity.setUserRole(role);
			if (existing != null) {
				Optional<UserRoleEntityMapping> present = existing.stream().filter(roleEntity -> roleEntity.getEntity().getEntityId().compareTo(item.getEntityId()) == 0).findAny();
				if (present.isPresent()) {
					entity.setUserRoleEntMapId(present.get().getUserRoleEntMapId());
				}
			} else {
				entity.setCreatedBy(userMaster);
				entity.setCreatedOn(auditDate);
			}
			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);
			allotedList.add(entity);
		});

		userRoleEntityMappingRepo.saveAll(allotedList);

	}

	private void prepareAndRoleEntityuMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedEntityList = new ArrayList<>();
		String[] allotedEntity = userRoleMgmt.getSelectedEntity();
		if (StringUtils.equals(allotedEntity[0], "null") || StringUtils.equals(allotedEntity[0], "[]") || StringUtils.equals(allotedEntity[0], "")) {
			return;
		}
		Arrays.stream(allotedEntity).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedEntityList.add(Long.valueOf(innerItem))));
		List<EntityBean> menuList = entityRepo.findAllById(allotedEntityList);
		List<UserRoleEntityMapping> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			UserRoleEntityMapping entity = new UserRoleEntityMapping();
			entity.setActive(true);
			entity.setEntity(item);
			entity.setUserRole(role);
			entity.setLastmMdifiedOn(auditDate);
			entity.setCreatedOn(auditDate);
			entity.setCreatedBy(userMaster);
			entity.setLastModifiedBy(userMaster);
			allotedList.add(entity);
		});

		userRoleEntityMappingRepo.saveAll(allotedList);

	}

	private void updateOrSaveRoleMenuMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedMenuList = new ArrayList<>();
		String[] allotedMenu = userRoleMgmt.getSelectedMenu();
		if (StringUtils.equals(allotedMenu[0], "null") || StringUtils.equals(allotedMenu[0], "") || StringUtils.equals(allotedMenu[0], "[]")) {
			return;
		}

		Arrays.stream(allotedMenu).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedMenuList.add(Long.valueOf(innerItem))));
		List<Menu> menuList = menuRepo.findAllById(allotedMenuList);
		List<MenuRoleMap> existing = menuRoleMapRepo.findByUserRoleIdFkUserRoleId(role.getUserRoleId());
		List<MenuRoleMap> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			MenuRoleMap entity = new MenuRoleMap();
			entity.setIsActive(true);
			entity.setMenuIDFk(item);
			entity.setModifiedOn(auditDate);
			entity.setUserRoleIdFk(role);
			entity.setMenuRoleId(role.getRoleIdKey());
			if (existing != null) {
				Optional<MenuRoleMap> present = existing.stream().filter(menuRole -> menuRole.getMenuIDFk().getMenuId().compareTo(item.getMenuId()) == 0).findAny();
				if (present.isPresent()) {
					entity.setMenuRoleId(present.get().getMenuRoleId());
				}
			}
			allotedList.add(entity);
		});

		menuRoleMapRepo.saveAll(allotedList);

	}

	private void prepareAndRoleMenuMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedMenuList = new ArrayList<>();
		String[] allotedMenu = userRoleMgmt.getSelectedMenu();
		if (StringUtils.equals(allotedMenu[0], "null") || StringUtils.equals(allotedMenu[0], "[]") || StringUtils.equals(allotedMenu[0], "")) {
			return;
		}
		Arrays.stream(allotedMenu).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedMenuList.add(Long.valueOf(innerItem))));
		List<Menu> menuList = menuRepo.findAllById(allotedMenuList);
		List<MenuRoleMap> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			MenuRoleMap entity = new MenuRoleMap();
			entity.setIsActive(true);
			entity.setMenuIDFk(item);
			entity.setModifiedOn(auditDate);
			entity.setModifiedBy(userMaster);
			entity.setUserRoleIdFk(role);
			entity.setMenuRoleId(role.getRoleIdKey());
			allotedList.add(entity);
		});

		menuRoleMapRepo.saveAll(allotedList);

	}

	private void updateOrSaveRoleReturnMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<String> allotedReturnsList = new ArrayList<>();
		String[] allotedReturn = userRoleMgmt.getSelectedReturn();
		if (StringUtils.equals(allotedReturn[0], "null") || StringUtils.equals(allotedReturn[0], "") || StringUtils.equals(allotedReturn[0], "[]")) {
			return;
		}
		Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedReturnsList.add(innerItem)));

		List<Return> returns = returnRepo.getDataByReturnCodeIn(allotedReturnsList);
		List<UserRoleReturnMapping> existingRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(role.getUserRoleId(), false);
		List<UserRoleReturnMapping> entityList = new ArrayList<>();
		returns.forEach(item -> {
			UserRoleReturnMapping entity = new UserRoleReturnMapping();

			entity.setUserModify(userMaster);
			entity.setIsActive(true);

			entity.setLastModifiedOn(auditDate);
			entity.setReturnIdFk(item);
			entity.setRoleIdFk(role);
			if (existingRole != null) {
				Optional<UserRoleReturnMapping> rolePresent = existingRole.stream().filter(roleItem -> roleItem.getReturnIdFk().getReturnId().compareTo(item.getReturnId()) == 0).findAny();
				if (rolePresent.isPresent()) {
					entity.setRoleReturnId(rolePresent.get().getRoleReturnId());
				}
			} else {
				entity.setCreatedOn(auditDate);
				entity.setCreatedBy(userMaster);
			}

			entityList.add(entity);
		});

		usserRoleReturnMappingRepo.saveAll(entityList);

	}

	private void prepareAndSaveRoleReturnMap(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<String> allotedReturnsList = new ArrayList<>();
		String[] allotedReturn = userRoleMgmt.getSelectedReturn();
		if (StringUtils.equals(allotedReturn[0], "null") || StringUtils.equals(allotedReturn[0], "[]") || StringUtils.equals(allotedReturn[0], "")) {
			return;
		}
		Arrays.stream(allotedReturn).forEach(item -> Arrays.stream(item.substring(1, item.length() - 1).replace("\"", "").split(",")).forEach(innerItem -> allotedReturnsList.add(innerItem)));

		List<Return> returns = returnRepo.getDataByReturnCodeIn(allotedReturnsList);
		List<UserRoleReturnMapping> entityList = new ArrayList<>();
		returns.forEach(item -> {
			UserRoleReturnMapping entity = new UserRoleReturnMapping();
			entity.setCreatedBy(userMaster);
			entity.setUserModify(userMaster);
			entity.setIsActive(true);
			entity.setCreatedOn(auditDate);
			entity.setLastModifiedOn(auditDate);
			entity.setReturnIdFk(item);
			entity.setRoleIdFk(role);
			entityList.add(entity);
		});

		usserRoleReturnMappingRepo.saveAll(entityList);

	}

	private void prepareAndSaveRoleLabel(Long userRoleId) {
		LOGGER.info("Executing procedure to add role label: RoleMapService.prepareAndSaveRoleLabel");
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.USER_ROLE_LABEL_PROCEDURE.getConstantVal());) {

			stmt.setLong(1, userRoleId);

			stmt.executeQuery();

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	private void updateOrSavePlarForm(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<UserRolePlatFormMap> existing = userRolePortalMapRepo.findAllByUserRoleUserRoleId(role.getUserRoleId());
		UserRolePlatFormMap rolePlat = new UserRolePlatFormMap();
		if (existing != null) {
			Optional<UserRolePlatFormMap> present = existing.stream().filter(item -> item.getPlatForm().getPlatFormId().compareTo(userRoleMgmt.getPortal()) == 0).findAny();
			if (present.isPresent()) {
				rolePlat.setUserRolePlatFormMapId(present.get().getUserRolePlatFormMapId());
			}
			;
		}

		rolePlat.setIsActive(true);
		rolePlat.setUserRole(role);
		rolePlat.setPlatForm(portalService.getDataById(userRoleMgmt.getPortal()));
		portalRoleService.add(rolePlat);

	}

	private void prepareAndSavePlarForm(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		UserRolePlatFormMap rolePlat = new UserRolePlatFormMap();
		rolePlat.setIsActive(true);
		rolePlat.setUserRole(role);
		rolePlat.setPlatForm(portalService.getDataById(userRoleMgmt.getPortal()));
		portalRoleService.add(rolePlat);

	}

	private UserRole prepareAndSaveRole(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster) {
		UserRole entity = new UserRole();
		entity.setCreatedOn(auditDate);
		entity.setIsActive(true);
		entity.setRoleName(userRoleMgmt.getRollName());
		entity.setLastUpdateOn(auditDate);
		//entity.setLastApprovedBy(userMaster);
		//entity.setLastApprovedOn(auditDate);
		entity.setRoleDesc(userRoleMgmt.getRoleDesc());
		entity.setRoleType(roleTypeService.getDataById(userRoleMgmt.getRoleType()));
		if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(2)) == 0) {
			entity.setLinkToEntity(true);
			entity.setLinkToAuditor(false);
		} else if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(3)) == 0) {
			entity.setLinkToEntity(false);
			entity.setLinkToAuditor(true);
		} else {
			entity.setLinkToAuditor(false);
			entity.setLinkToEntity(false);

		}
		if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin())) {
			entity.setDeptAdmin(userRoleMgmt.getDeptAdmin());
		} else {
			entity.setDeptAdmin("0");
		}
		entity.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
		entity.setRolePriority(12l);
		entity.setUserModify(userMaster);
		entity.setUser(userMaster);
		return userRoleService.add(entity);

	}

	private UserRole prepareAndSaveRoleInApproval(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserMaster createdBy, Date createdOn, UserMaster approvedBy, Date approvedOn) {
		UserRole entity = new UserRole();
		entity.setLastApprovedBy(approvedBy);
		entity.setLastApprovedOn(approvedOn);
		entity.setCreatedOn(createdOn);
		entity.setIsActive(userRoleMgmt.getIsActive() == 1L ? true : false);
		entity.setRoleName(userRoleMgmt.getRollName());
		entity.setLastUpdateOn(auditDate);
		//entity.setLastApprovedBy(userMaster);
		//entity.setLastApprovedOn(auditDate);
		entity.setRoleDesc(userRoleMgmt.getRoleDesc());
		entity.setRoleType(roleTypeService.getDataById(userRoleMgmt.getRoleType()));
		if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(2)) == 0) {
			entity.setLinkToEntity(true);
			entity.setLinkToAuditor(false);
		} else if (userRoleMgmt.getRoleType().compareTo(Long.valueOf(3)) == 0) {
			entity.setLinkToEntity(false);
			entity.setLinkToAuditor(true);
		} else {
			entity.setLinkToAuditor(false);
			entity.setLinkToEntity(false);

		}
		if (StringUtils.isNotBlank(userRoleMgmt.getDeptAdmin())) {
			entity.setDeptAdmin(userRoleMgmt.getDeptAdmin());
		} else {
			entity.setDeptAdmin("0");
		}
		entity.setCreatedByRole(userRoleService.getDataById(userRoleMgmt.getCreatedByRole()));
		entity.setRolePriority(12l);
		entity.setUserModify(userMaster);
		entity.setUser(createdBy);
		return userRoleService.add(entity);

	}

	public List<UserRoleLabel> getEditView(UserRoleMgmtBean userRoleMgmt) {
		Optional<UserRole> role = userRoleRepo.findById(userRoleMgmt.getCreatedByRole());
		if (role.isPresent()) {
			return userRoleLabelRepo.getAllUserRoleForLoggedInUser(role.get().getRoleType().getRoleTypeId(), role.get().getUser().getDepartmentIdFk().getRegulatorId(), userRoleMgmt.getCurrentLocale());
		}
		return null;
		// userRoleService.findByCreatedByRoleuserRoleIdAndIsActiveTrue(userRoleMgmt.getCreatedByRole());
	}

	public List<RoleMapHistory> getRoleMapHistory(UserRoleMgmtBean userRoleMgmtBean) {
		StringBuilder sb = new StringBuilder();
		sb.append(" with languageMaster as ( ").append(" select tbl_language_master.language_id as language_id from tbl_language_master where language_code='en' ").append(" ), ").append(" roleView as ( ").append(" select * from tbl_user_role where user_role_id=" + userRoleMgmtBean.getRoleIdExisting()).append(" ), ").append(" roleEntityView as(  ").append(" SELECT   a.role_id_fk,a.modified_on,group_concat(DISTINCT label.entity_name_label  ORDER BY label.entity_name_label ASC  separator ';') as label ").append(" FROM     tbl_role_entity_mapping_mod a, languageMaster, ").append(" json_table(prev_data_json, '$[*]' columns (  mapid int(100) path '$.userRoleEntMapId',  entityid int(100) path '$.entity' ) )modDetails left join ").append(" tbl_entity_label label on modDetails.entityid=label.entity_id_fk ").append(" where languageMaster.language_id=label.language_id_fk ").append(" group by modified_on ").append(" ), ").append(" menuRoleView as( ").append(" SELECT   a.role_id_fk,a.modified_on,group_concat(DISTINCT label.menu_label  ORDER BY label.menu_label ASC  separator ';')  as label ").append(" FROM     tbl_role_menu_mapping_mod a, languageMaster, ").append(" json_table(prev_data_json, '$[*]' columns (    menuIDFk int(100) path '$.menuIDFk' ) )modDetails left join ").append(" tbl_menu_label label on modDetails.menuIDFk=label.menu_id_fk ").append(" where languageMaster.language_id=label.language_id_fk ").append(" group by modified_on ").append(" ), ").append(" returnRoleView as( ").append(" SELECT   label.lang_id_fk as lang_id,a.role_id_fk,a.modified_on,group_concat(DISTINCT label.return_label  ORDER BY label.return_label ASC  separator ';')  as label ").append(" FROM     tbl_role_return_mapping_mod a, languageMaster, ").append(" json_table(prev_data_json, '$[*]' columns (    returnIdFk int(100) path '$.returnIdFk' ) ) modDetails left join ").append(" tbl_return_label label on modDetails.returnIdFk=label.return_id_fk ").append(" where languageMaster.language_id=label.lang_id_fk ").append(" group by modified_on ").append(" ) ").append(" select roleView.role_name,menuRoleView.role_id_fk,menuRoleView.label as menu,roleEntityView.label as entity,returnRoleView.label as returnlabel from menuRoleView left join roleEntityView on menuRoleView.role_id_fk=roleEntityView.role_id_fk ").append(" left join returnRoleView on  menuRoleView.role_id_fk=returnRoleView.role_id_fk ,roleView ").append(" order by menuRoleView.modified_on ");

		Query query = em.createNativeQuery(sb.toString(), Tuple.class);
		List<Tuple> result = query.getResultList();
		List<RoleMapHistory> list = result.stream().map(item -> {
			RoleMapHistory dto = new RoleMapHistory();
			dto.setRoleName((String) item.get("role_name"));
			dto.setEntityLabel((String) item.get("entity"));
			dto.setMenuLabel((String) item.get("menu"));
			dto.setReturnLabel((String) item.get("returnlabel"));
			return dto;

		}).collect(Collectors.toList());
		return list;

	}

	private void prepareAndSaveRoleReturnMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<String> allotedReturnsList = new ArrayList<>();
		String[] allotedReturn = userRoleMgmt.getSelectedReturn();
		if (allotedReturn == null || StringUtils.equals(allotedReturn[0], "null") || StringUtils.equals(allotedReturn[0], "[]") || StringUtils.equals(allotedReturn[0], "")) {
			return;
		}
		allotedReturnsList = Arrays.asList(userRoleMgmt.getSelectedReturn());
		List<Return> returns = returnRepo.getDataByReturnCodeIn(allotedReturnsList);
		List<UserRoleReturnMapping> entityList = new ArrayList<>();
		returns.forEach(item -> {
			UserRoleReturnMapping entity = new UserRoleReturnMapping();
			entity.setCreatedBy(userMaster);
			entity.setUserModify(userMaster);
			entity.setIsActive(true);
			entity.setCreatedOn(auditDate);
			entity.setLastModifiedOn(auditDate);
			entity.setReturnIdFk(item);
			entity.setRoleIdFk(role);
			entityList.add(entity);
		});

		usserRoleReturnMappingRepo.saveAll(entityList);

	}

	private void prepareAndRoleMenuMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedMenuList = new ArrayList<>();
		String[] allotedMenu = userRoleMgmt.getSelectedMenu();
		if (allotedMenu == null || StringUtils.equals(allotedMenu[0], "null") || StringUtils.equals(allotedMenu[0], "[]") || StringUtils.equals(allotedMenu[0], "")) {
			return;
		}
		List<String> menuIds = Arrays.asList(userRoleMgmt.getSelectedMenu());
		allotedMenuList = menuIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<Menu> menuList = menuRepo.findAllById(allotedMenuList);
		List<MenuRoleMap> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			MenuRoleMap entity = new MenuRoleMap();
			entity.setIsActive(true);
			entity.setMenuIDFk(item);
			entity.setModifiedOn(auditDate);
			entity.setModifiedBy(userMaster);
			entity.setUserRoleIdFk(role);
			entity.setMenuRoleId(role.getRoleIdKey());
			allotedList.add(entity);
		});

		menuRoleMapRepo.saveAll(allotedList);

	}

	private void prepareAndRoleEntityMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedEntityList = new ArrayList<>();
		String[] allotedEntity = userRoleMgmt.getSelectedEntity();
		if (allotedEntity == null || StringUtils.equals(allotedEntity[0], "null") || StringUtils.equals(allotedEntity[0], "[]") || StringUtils.equals(allotedEntity[0], "")) {
			return;
		}
		List<String> entityIds = Arrays.asList(userRoleMgmt.getSelectedEntity());
		allotedEntityList = entityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<EntityBean> menuList = entityRepo.findAllById(allotedEntityList);
		List<UserRoleEntityMapping> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			UserRoleEntityMapping entity = new UserRoleEntityMapping();
			entity.setActive(true);
			entity.setEntity(item);
			entity.setUserRole(role);
			entity.setLastmMdifiedOn(auditDate);
			entity.setCreatedOn(auditDate);
			entity.setCreatedBy(userMaster);
			entity.setLastModifiedBy(userMaster);
			allotedList.add(entity);
		});

		userRoleEntityMappingRepo.saveAll(allotedList);

	}

	private void prepareAndSaveActivityModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedActivityList = new ArrayList<>();
		String[] allotedActivity = userRoleMgmt.getSelectedActivity();
		if (allotedActivity == null || StringUtils.equals(allotedActivity[0], "null") || StringUtils.equals(allotedActivity[0], "[]") || StringUtils.equals(allotedActivity[0], "")) {
			return;
		}
		List<String> activityIds = Arrays.asList(userRoleMgmt.getSelectedActivity());
		allotedActivityList = activityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<WorkFlowActivity> workflowActivityList = workFlowActivityRepo.findAllById(allotedActivityList);
		List<UserRoleActivityMap> allotedList = new ArrayList<>();
		workflowActivityList.forEach(item -> {
			UserRoleActivityMap entity = new UserRoleActivityMap();
			entity.setIsActive(true);
			entity.setRole(role);
			entity.setCreatedBy(userMaster);
			entity.setWorkFlowActivity(item);
			entity.setCreatedOn(auditDate);
			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);

			allotedList.add(entity);
		});

		userRoleActivityMapRepo.saveAll(allotedList);

	}

	private void updateOrSaveRoleReturnMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<String> allotedReturnsList = new ArrayList<>();
		String[] allotedReturn = userRoleMgmt.getSelectedReturn();
		if (allotedReturn == null || StringUtils.equals(allotedReturn[0], "null") || StringUtils.equals(allotedReturn[0], "[]") || StringUtils.equals(allotedReturn[0], "")) {
			return;
		}
		allotedReturnsList = Arrays.asList(userRoleMgmt.getSelectedReturn());
		List<Return> returns = returnRepo.getDataByReturnCodeIn(allotedReturnsList);
		List<UserRoleReturnMapping> existingRole = usserRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(role.getUserRoleId(), false);
		List<UserRoleReturnMapping> entityList = new ArrayList<>();
		returns.forEach(item -> {
			UserRoleReturnMapping entity = new UserRoleReturnMapping();

			entity.setUserModify(userMaster);
			entity.setIsActive(true);

			entity.setLastModifiedOn(auditDate);
			entity.setReturnIdFk(item);
			entity.setRoleIdFk(role);
			if (existingRole != null) {
				Optional<UserRoleReturnMapping> rolePresent = existingRole.stream().filter(roleItem -> roleItem.getReturnIdFk().getReturnId().compareTo(item.getReturnId()) == 0).findAny();
				if (rolePresent.isPresent()) {
					entity.setRoleReturnId(rolePresent.get().getRoleReturnId());
				}
			} else {
				entity.setCreatedOn(auditDate);
				entity.setCreatedBy(userMaster);
			}

			entityList.add(entity);
		});

		usserRoleReturnMappingRepo.saveAll(entityList);

	}

	private void updateOrSaveRoleMenuMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedMenuList = new ArrayList<>();
		String[] allotedMenu = userRoleMgmt.getSelectedMenu();
		if (allotedMenu == null || StringUtils.equals(allotedMenu[0], "null") || StringUtils.equals(allotedMenu[0], "[]") || StringUtils.equals(allotedMenu[0], "")) {
			return;
		}
		List<String> menuIds = Arrays.asList(userRoleMgmt.getSelectedMenu());
		allotedMenuList = menuIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<Menu> menuList = menuRepo.findAllById(allotedMenuList);
		List<MenuRoleMap> existing = menuRoleMapRepo.findByUserRoleIdFkUserRoleId(role.getUserRoleId());
		List<MenuRoleMap> allotedList = new ArrayList<>();
		menuList.forEach(item -> {
			MenuRoleMap entity = new MenuRoleMap();
			entity.setIsActive(true);
			entity.setMenuIDFk(item);
			entity.setModifiedOn(auditDate);
			entity.setUserRoleIdFk(role);
			entity.setMenuRoleId(role.getRoleIdKey());
			if (existing != null) {
				Optional<MenuRoleMap> present = existing.stream().filter(menuRole -> menuRole.getMenuIDFk().getMenuId().compareTo(item.getMenuId()) == 0).findAny();
				if (present.isPresent()) {
					entity.setMenuRoleId(present.get().getMenuRoleId());
				}
			}
			allotedList.add(entity);
		});

		menuRoleMapRepo.saveAll(allotedList);

	}

	private void updateOrSaveRoleEntityuMapModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedEntityList = new ArrayList<>();
		String[] allotedEntity = userRoleMgmt.getSelectedEntity();
		if (allotedEntity == null || StringUtils.equals(allotedEntity[0], "null") || StringUtils.equals(allotedEntity[0], "[]") || StringUtils.equals(allotedEntity[0], "")) {
			return;
		}
		List<String> entityIds = Arrays.asList(userRoleMgmt.getSelectedEntity());
		allotedEntityList = entityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<EntityBean> entityList = entityRepo.findAllById(allotedEntityList);
		List<UserRoleEntityMapping> existing = userRoleEntityMappingRepo.findByUserRoleUserRoleId(role.getUserRoleId());
		List<UserRoleEntityMapping> allotedList = new ArrayList<>();
		entityList.forEach(item -> {
			UserRoleEntityMapping entity = new UserRoleEntityMapping();
			entity.setActive(true);
			entity.setEntity(item);
			entity.setUserRole(role);
			if (existing != null) {
				Optional<UserRoleEntityMapping> present = existing.stream().filter(roleEntity -> roleEntity.getEntity().getEntityId().compareTo(item.getEntityId()) == 0).findAny();
				if (present.isPresent()) {
					entity.setUserRoleEntMapId(present.get().getUserRoleEntMapId());
				}
			} else {
				entity.setCreatedBy(userMaster);
				entity.setCreatedOn(auditDate);
			}
			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);
			allotedList.add(entity);
		});

		userRoleEntityMappingRepo.saveAll(allotedList);

	}

	private void updateOrSaveActivityModified(UserRoleMgmtBean userRoleMgmt, Date auditDate, UserMaster userMaster, UserRole role) {
		List<Long> allotedActivityList = new ArrayList<>();
		String[] allotedActivity = userRoleMgmt.getSelectedActivity();
		if (allotedActivity == null || StringUtils.equals(allotedActivity[0], "null") || StringUtils.equals(allotedActivity[0], "[]") || StringUtils.equals(allotedActivity[0], "")) {
			return;
		}
		List<String> activityIds = Arrays.asList(userRoleMgmt.getSelectedActivity());
		allotedActivityList = activityIds.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
		List<WorkFlowActivity> workflowActivityList = workFlowActivityRepo.findAllById(allotedActivityList);
		List<UserRoleActivityMap> allotedList = new ArrayList<>();
		List<UserRoleActivityMap> existing = userRoleActivityMapRepo.findByRoleUserRoleId(role.getUserRoleId());
		workflowActivityList.forEach(item -> {
			UserRoleActivityMap entity = new UserRoleActivityMap();
			entity.setIsActive(true);
			entity.setRole(role);
			entity.setWorkFlowActivity(item);
			entity.setLastmMdifiedOn(auditDate);
			entity.setLastModifiedBy(userMaster);
			if (existing != null) {
				Optional<UserRoleActivityMap> present = existing.stream().filter(inner -> inner.getWorkFlowActivity().getActivityId().compareTo(item.getActivityId()) == 0).findAny();
				if (present.isPresent()) {
					entity.setUserRoleActivityMapId(present.get().getUserRoleActivityMapId());
				}
			} else {
				entity.setCreatedBy(userMaster);
				entity.setCreatedOn(auditDate);
			}
			allotedList.add(entity);
		});

		userRoleActivityMapRepo.saveAll(allotedList);

	}

	/**
	 * This method is used to get user role list on basis of logged in user role type id Tested
	 */
	public List<UserRoleDto> getEditUserRoleList(UserInfoDTO userInfo) {
		List<UserRoleDto> userRoleDTOList = new ArrayList<>();
		try {
			List<UserRoleLabel> userRoleLabelList;
			List<UserRoleLabel> userAssignedRoleLabelList;
			List<Long> roleTypeId = new ArrayList<>();
			UserMaster userMaster = userMasterRepo.findByUserId(userInfo.getLoggedInUserId());

			if (userInfo.getRoleTypeId() != null) {
				roleTypeId.add(userInfo.getRoleTypeId());//	roleTypeId : Grid user role type / Drodown role type
			}

			if (roleTypeId.contains(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
				if (userInfo.getLoggedInUserDeptId() != null) { // userInfo.getLoggedInUserDeptId() : Grid user department Id
					List<Long> loggedInUserDeptId = new ArrayList<>();
					loggedInUserDeptId.add(userInfo.getLoggedInUserDeptId());
					loggedInUserDeptId.add(userMaster.getDepartmentIdFk().getRegulatorId());
					// 					This code is used while editing department user in the grid by main department user
					//					userRoleLabelList = userRoleLabelRepo.getDeptActiveUserRoleForEditUser(loggedInUserDeptId, roleTypeId, userInfo.getLangCode());
					return getDeptActiveUserRoleForEditUserV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), loggedInUserDeptId);
				} else {
					if (userMaster.getDepartmentIdFk().getIsMaster().equals(Boolean.TRUE)) {
						// 						This code is used to populate user role when logged in by main department user and selected Department in the role Type drop down
						//						userRoleLabelList = userRoleLabelRepo.getAllActiveRoles(roleTypeId, userInfo.getLangCode());
						return getAllActiveRolesV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId());
					} else {
						// This code is used to populate user role when logged in by non main department user and selected Department type in the role Type drop down
						//						// This code is used to edit the non main depat user in the grid by non main department user
						//						userRoleLabelList = userRoleLabelRepo.getDeptActiveUserRoleForAddUser(userMaster.getDepartmentIdFk().getRegulatorId(), roleTypeId, userInfo.getLangCode());
						return getDeptActiveUserRoleForAddUserV2(userMaster.getDepartmentIdFk().getRegulatorId(), roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId());
					}

				}
			} else if (roleTypeId.contains(GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal())) {
				userRoleLabelList = userRoleLabelRepo.getAudActiveUserRoleForAddUser(roleTypeId, userInfo.getLangCode());
				userAssignedRoleLabelList = userRoleLabelRepo.getAllAssignedRoles(roleTypeId, userInfo.getLoggedInUserId(), userInfo.getLangCode());
				userRoleLabelList.addAll(userAssignedRoleLabelList);
				Map<Long, UserRoleLabel> roleLblMap = new LinkedHashMap<>();
				UserRoleDto userRoleDTO;

				if (!CollectionUtils.isEmpty(userRoleLabelList)) {
					for (UserRoleLabel roleLblDto : userRoleLabelList) {
						String rolePlatforms = "";
						if (!roleLblMap.containsKey(roleLblDto.getUserRoleLabelId())) {
							roleLblMap.put(roleLblDto.getUserRoleLabelId(), roleLblDto);
							List<UserRolePlatFormMap> userRolePlatformMappingList = roleLblDto.getUserRoleIdFk().getUserRolePlatFormMap().stream().filter(cl -> cl.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
							userRoleDTO = new UserRoleDto();
							userRoleDTO.setUserRoleId(roleLblDto.getUserRoleIdFk().getUserRoleId());

							for (UserRolePlatFormMap userRolePlatformMapping : userRolePlatformMappingList) {
								if (rolePlatforms != null) {
									if (!StringUtils.isEmpty(rolePlatforms)) {
										rolePlatforms += ", ";
									}

									rolePlatforms += ObjectCache.getLabelKeyValue(userInfo.getLangCode(), userRolePlatformMapping.getPlatForm().getPlatFormKey());
								}
							}

							userRoleDTO.setRoleName(roleLblDto.getUserRoleLabel() + " (" + rolePlatforms + ")");
							userRoleDTOList.add(userRoleDTO);
						}
					}
					userRoleLabelList = new ArrayList<>();
					userRoleLabelList.addAll(roleLblMap.values());
				}
			} else if (roleTypeId.contains(GeneralConstants.AGENCY_ROLE_TYPE_ID.getConstantLongVal())) {
				userRoleLabelList = userRoleLabelRepo.getAudActiveUserRoleForAddUser(roleTypeId, userInfo.getLangCode());
				Map<Long, UserRoleLabel> roleLblMap = new LinkedHashMap<>();
				UserRoleDto userRoleDTO;

				if (!CollectionUtils.isEmpty(userRoleLabelList)) {
					for (UserRoleLabel roleLblDto : userRoleLabelList) {
						String rolePlatforms = "";
						if (!roleLblMap.containsKey(roleLblDto.getUserRoleLabelId())) {
							roleLblMap.put(roleLblDto.getUserRoleLabelId(), roleLblDto);
							List<UserRolePlatFormMap> userRolePlatformMappingList = roleLblDto.getUserRoleIdFk().getUserRolePlatFormMap().stream().filter(cl -> cl.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
							userRoleDTO = new UserRoleDto();
							userRoleDTO.setUserRoleId(roleLblDto.getUserRoleIdFk().getUserRoleId());

							for (UserRolePlatFormMap userRolePlatformMapping : userRolePlatformMappingList) {
								if (rolePlatforms != null) {
									if (!StringUtils.isEmpty(rolePlatforms)) {
										rolePlatforms += ", ";
									}

									rolePlatforms += ObjectCache.getLabelKeyValue(userInfo.getLangCode(), userRolePlatformMapping.getPlatForm().getPlatFormKey());
								}
							}

							userRoleDTO.setRoleName(roleLblDto.getUserRoleLabel() + " (" + rolePlatforms + ")");
							userRoleDTOList.add(userRoleDTO);
						}
					}
					userRoleLabelList = new ArrayList<>();
					userRoleLabelList.addAll(roleLblMap.values());
				}
			} else {
				if (userMaster.getRoleType().getRoleTypeId().longValue() == GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) {
					// logging by main department user and select entity type in the drop down then this method is used to populate the role
					// logging by main department user and select entity type user in the grid then this method is used to populate the role
					//					userRoleLabelList = userRoleLabelRepo.getEntActiveUserRoleForAddUserMainDept(roleTypeId, userInfo.getLangCode(), userMaster.getDepartmentIdFk().getRegulatorId(), userInfo.getEntityCode());
					return getEntActiveUserRoleForAddUserMainDeptV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), userInfo.getEntityCode(), userMaster.getDepartmentIdFk().getRegulatorId());
				} else {
					// logged in by entity user and populate role drop down for entity type
					//					userRoleLabelList = userRoleLabelRepo.getEntActiveUserRoleForAddUserEntUsr(roleTypeId, userInfo.getLangCode(), userInfo.getEntityCode());
					return getEntActiveUserRoleForAddUserEntUsrV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), userInfo.getEntityCode());
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return userRoleDTOList;
	}

	/**
	 * This method is used to get user role list on basis of logged in user role type id Tested
	 */
	public List<UserRoleDto> getAddUserRoleList(UserInfoDTO userInfo) {
		List<UserRoleDto> userRoleDTOList = new ArrayList<>();
		try {
			List<UserRoleLabel> userRoleLabelList;
			List<UserRoleLabel> userAssignedRoleLabelList;
			List<Long> roleTypeId = new ArrayList<>();
			UserMaster userMaster = userMasterRepo.findByUserId(userInfo.getLoggedInUserId());

			if (userInfo.getRoleTypeId() != null) {
				roleTypeId.add(userInfo.getRoleTypeId());//	roleTypeId : Grid user role type / Drodown role type
			}

			if (roleTypeId.contains(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
				if (userInfo.getLoggedInUserDeptId() != null) { // userInfo.getLoggedInUserDeptId() : Grid user department Id
					List<Long> loggedInUserDeptId = new ArrayList<>();
					loggedInUserDeptId.add(userInfo.getLoggedInUserDeptId());
					loggedInUserDeptId.add(userMaster.getDepartmentIdFk().getRegulatorId());
					// 					This code is used while editing department user in the grid by main department user
					//					userRoleLabelList = userRoleLabelRepo.getDeptActiveUserRoleForEditUser(loggedInUserDeptId, roleTypeId, userInfo.getLangCode());
					return getDeptActiveUserRoleForEditUserV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), loggedInUserDeptId);
				} else {
					if (userMaster.getDepartmentIdFk().getIsMaster().equals(Boolean.TRUE)) {
						// 						This code is used to populate user role when logged in by main department user and selected Department in the role Type drop down
						//						userRoleLabelList = userRoleLabelRepo.getAllActiveRoles(roleTypeId, userInfo.getLangCode());
						return getAllActiveRolesV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId());
					} else {
						// This code is used to populate user role when logged in by non main department user and selected Department type in the role Type drop down
						//						// This code is used to edit the non main depat user in the grid by non main department user
						//						userRoleLabelList = userRoleLabelRepo.getDeptActiveUserRoleForAddUser(userMaster.getDepartmentIdFk().getRegulatorId(), roleTypeId, userInfo.getLangCode());
						return getDeptActiveUserRoleForAddUserV2(userMaster.getDepartmentIdFk().getRegulatorId(), roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId());
					}

				}
			} else if (roleTypeId.contains(GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal())) {
				userRoleLabelList = userRoleLabelRepo.getAudActiveUserRoleForAddUser(roleTypeId, userInfo.getLangCode());
				userAssignedRoleLabelList = userRoleLabelRepo.getAllAssignedRoles(roleTypeId, userInfo.getLoggedInUserId(), userInfo.getLangCode());
				userRoleLabelList.addAll(userAssignedRoleLabelList);
				Map<Long, UserRoleLabel> roleLblMap = new LinkedHashMap<>();
				UserRoleDto userRoleDTO;

				if (!CollectionUtils.isEmpty(userRoleLabelList)) {
					for (UserRoleLabel roleLblDto : userRoleLabelList) {
						String rolePlatforms = "";
						if (!roleLblMap.containsKey(roleLblDto.getUserRoleLabelId())) {
							roleLblMap.put(roleLblDto.getUserRoleLabelId(), roleLblDto);
							List<UserRolePlatFormMap> userRolePlatformMappingList = roleLblDto.getUserRoleIdFk().getUserRolePlatFormMap().stream().filter(cl -> cl.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
							userRoleDTO = new UserRoleDto();
							userRoleDTO.setUserRoleId(roleLblDto.getUserRoleIdFk().getUserRoleId());

							for (UserRolePlatFormMap userRolePlatformMapping : userRolePlatformMappingList) {
								if (rolePlatforms != null) {
									if (!StringUtils.isEmpty(rolePlatforms)) {
										rolePlatforms += ", ";
									}

									rolePlatforms += ObjectCache.getLabelKeyValue(userInfo.getLangCode(), userRolePlatformMapping.getPlatForm().getPlatFormKey());
								}
							}

							userRoleDTO.setRoleName(roleLblDto.getUserRoleLabel() + " (" + rolePlatforms + ")");
							userRoleDTOList.add(userRoleDTO);
						}
					}
					userRoleLabelList = new ArrayList<>();
					userRoleLabelList.addAll(roleLblMap.values());
				}
			} else {
				if (userMaster.getRoleType().getRoleTypeId().longValue() == GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal()) {
					// logging by main department user and select entity type in the drop down then this method is used to populate the role
					// logging by main department user and select entity type user in the grid then this method is used to populate the role
					//					userRoleLabelList = userRoleLabelRepo.getEntActiveUserRoleForAddUserMainDept(roleTypeId, userInfo.getLangCode(), userMaster.getDepartmentIdFk().getRegulatorId(), userInfo.getEntityCode());
					return getEntActiveUserRoleForAddUserMainDeptV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), userInfo.getEntityCode(), userMaster.getDepartmentIdFk().getRegulatorId());
				} else {
					// logged in by entity user and populate role drop down for entity type
					//					userRoleLabelList = userRoleLabelRepo.getEntActiveUserRoleForAddUserEntUsr(roleTypeId, userInfo.getLangCode(), userInfo.getEntityCode());
					return getEntActiveUserRoleForAddUserEntUsrV2(roleTypeId, userInfo.getLangCode(), userInfo.getLoggedInUserId(), userInfo.getEntityCode());
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return userRoleDTOList;
	}

	private List<UserRoleDto> getDeptActiveUserRoleForEditUserV2(List<Long> roleTypeId, String langCode, Long loggedInUserId, List<Long> loggedInUserDeptId) {
		List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getDeptActiveUserRoleAndPlatFormForEditUser(loggedInUserDeptId, roleTypeId, langCode);

		List<UserRolePlatformDto> userRolePlatformDtos1 = userRoleLabelRepo.getAllAssignedRolesAndPlatForm(roleTypeId, loggedInUserId, langCode);

		userRolePlatformDtos.addAll(userRolePlatformDtos1);

		return transformObject(userRolePlatformDtos, langCode);
	}

	private List<UserRoleDto> getAllActiveRolesV2(List<Long> roleTypeId, String langCode, Long loggedInUserId) {
		List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getRolePlatFormMapping(roleTypeId, langCode);

		List<UserRolePlatformDto> userRolePlatformDtos1 = userRoleLabelRepo.getAllAssignedRolesAndPlatForm(roleTypeId, loggedInUserId, langCode);

		userRolePlatformDtos.addAll(userRolePlatformDtos1);

		return transformObject(userRolePlatformDtos, langCode);
	}

	private List<UserRoleDto> getEntActiveUserRoleForAddUserEntUsrV2(List<Long> roleTypeId, String langCode, Long loggedInUserId, String entityCode) {

		List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getEntActiveUserRoleAndPlatformForAddUserEntUsr(roleTypeId, langCode, entityCode);
		List<UserRolePlatformDto> userRolePlatformDtos1 = userRoleLabelRepo.getAllAssignedRolesAndPlatForm(roleTypeId, loggedInUserId, langCode);

		userRolePlatformDtos.addAll(userRolePlatformDtos1);

		return transformObject(userRolePlatformDtos, langCode);
	}

	private List<UserRoleDto> getEntActiveUserRoleForAddUserMainDeptV2(List<Long> roleTypeId, String langCode, Long loggedInUserId, String entityCode, Long regulatorId) {
		List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getEntActiveUserRoleAndPlatformForAddUserMainDept(roleTypeId, langCode, regulatorId, entityCode);

		List<UserRolePlatformDto> userRolePlatformDtos1 = userRoleLabelRepo.getAllAssignedRolesAndPlatForm(roleTypeId, loggedInUserId, langCode);

		userRolePlatformDtos.addAll(userRolePlatformDtos1);

		return transformObject(userRolePlatformDtos, langCode);
	}

	private List<UserRoleDto> getDeptActiveUserRoleForAddUserV2(Long regulatorId, List<Long> roleTypeId, String langCode, Long loggedInUserId) {
		List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getDeptActiveUserRoleAndPlatformForAddUser(regulatorId, roleTypeId, langCode);

		List<UserRolePlatformDto> userRolePlatformDtos1 = userRoleLabelRepo.getAllAssignedRolesAndPlatForm(roleTypeId, loggedInUserId, langCode);

		userRolePlatformDtos.addAll(userRolePlatformDtos1);

		return transformObject(userRolePlatformDtos, langCode);
	}

	private List<UserRoleDto> transformObject(List<UserRolePlatformDto> userRolePlatformDtos, String langCode) {
		if (!CollectionUtils.isEmpty(userRolePlatformDtos)) {
			userRolePlatformDtos.sort((o1, o2) -> o1.getUserRoleName().compareTo(o2.getUserRoleName()));

			List<UserRoleDto> userRoleDtos = new ArrayList<>();
			Map<Long, Map<String, Set<String>>> roleIdAndPlatFormList = new LinkedHashMap<>();

			for (UserRolePlatformDto userRolePlatformDto : userRolePlatformDtos) {
				if (roleIdAndPlatFormList.containsKey(userRolePlatformDto.getUserRoleId())) {
					Map<String, Set<String>> platFormMap = roleIdAndPlatFormList.get(userRolePlatformDto.getUserRoleId());
					platFormMap.get(userRolePlatformDto.getUserRoleName()).add(ObjectCache.getLabelKeyValue(langCode, userRolePlatformDto.getPlatformName()));
				} else {
					Map<String, Set<String>> platFormMap = new LinkedHashMap<>();
					Set<String> platformList = new HashSet<>();
					platformList.add(ObjectCache.getLabelKeyValue(langCode, userRolePlatformDto.getPlatformName()));
					platFormMap.put(userRolePlatformDto.getUserRoleName(), platformList);
					roleIdAndPlatFormList.put(userRolePlatformDto.getUserRoleId(), platFormMap);
				}
			}

			StringBuilder rolePlatforms;
			List<String> list;
			for (Map.Entry<Long, Map<String, Set<String>>> roleIdAndPlatformList : roleIdAndPlatFormList.entrySet()) {
				UserRoleDto userRoleDto = new UserRoleDto();
				userRoleDto.setUserRoleId(roleIdAndPlatformList.getKey());

				Map<String, Set<String>> platFormMaps = roleIdAndPlatformList.getValue();
				rolePlatforms = new StringBuilder("");
				for (Map.Entry<String, Set<String>> mapEntry : platFormMaps.entrySet()) {
					if (!CollectionUtils.isEmpty(mapEntry.getValue())) {
						list = new ArrayList<String>(mapEntry.getValue());
						list.sort((s1, s2) -> s1.compareTo(s2));
						for (String platForm : list) {
							if (!StringUtils.isEmpty(rolePlatforms.toString())) {
								rolePlatforms.append(", ");
							}
							rolePlatforms.append(platForm);
						}
					}
					userRoleDto.setRoleName(mapEntry.getKey() + " (" + rolePlatforms + ")");
				}

				userRoleDtos.add(userRoleDto);
			}

			return userRoleDtos;
		}
		return null;
	}

	public List<UserModifiedDto> checkPendingStatus(Long editUserId, Integer actionId, Integer adminStatus) {
		List<UserModifiedDto> userModifiedDtoList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		UserModifiedDto userMod;
		try {
			sb.append(" SELECT urm.USER_MODIFIED_ID,urm.USER_ID_FK,urm.USER_NAME,urm.ADMIN_STATUS_ID_FK,urm.ACTION_ID_FK,urm.CREATED_ON,urm.MODIFIED_ON,urm.APPROVED_ON, ");
			sb.append(" (SELECT USER_NAME FROM TBL_USER_MASTER WHERE USER_ID = urm.CREATED_BY_FK) as createdByUser,(SELECT USER_NAME FROM TBL_USER_MASTER WHERE USER_ID = urm.MODIFIED_BY_FK) as modifiedByUser,(SELECT USER_NAME FROM TBL_USER_MASTER WHERE USER_ID = urm.APPROVED_BY_FK) as approvedByUser, ");
			sb.append(" urm.ROLE_TYPE_FK,urm.USER_DETAILS FROM TBL_USER_MODIFIED urm where urm.USER_ID_FK = :editUserId and urm.ADMIN_STATUS_ID_FK = :adminStatus and urm.ACTION_ID_FK = :actionId ");

			Query query = em.createNativeQuery(sb.toString(), Tuple.class);
			query.setParameter("editUserId", editUserId).setParameter("actionId", actionId).setParameter("adminStatus", adminStatus);
			List<Tuple> result = query.getResultList();
			for (Tuple tuple : result) {
				userMod = new UserModifiedDto();
				userMod.setUserName(tuple.get("USER_NAME").toString());
				userMod.setUserId(Long.parseLong(tuple.get("USER_ID_FK").toString()));
				userMod.setUserId(Long.parseLong(tuple.get("USER_MODIFIED_ID").toString()));
				userMod.setAction(Integer.parseInt(tuple.get("ACTION_ID_FK").toString()));
				userMod.setAdminStatus(Integer.parseInt(tuple.get("ADMIN_STATUS_ID_FK").toString()));
				userMod.setCreatedBy(AESV2.getInstance().decrypt(tuple.get("createdByUser").toString()));
				if (tuple.get("modifiedByUser") != null) {
					userMod.setModifiedBy(AESV2.getInstance().decrypt(tuple.get("modifiedByUser").toString()));
				}
				if (tuple.get("approvedByUser") != null) {
					userMod.setApprovedBy(AESV2.getInstance().decrypt(tuple.get("approvedByUser").toString()));
				}
				userMod.setCreatedOn((Date) tuple.get("CREATED_ON"));
				userMod.setLastModifiedOn((Date) tuple.get("MODIFIED_ON"));
				userMod.setApprovedOn((Date) tuple.get("APPROVED_ON"));
				userMod.setUserDetailsJson(tuple.get("USER_DETAILS").toString());
				userModifiedDtoList.add(userMod);
			}

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return userModifiedDtoList;
	}

	public List<DropDownGroup> getModifiedEntitySelectListV2(Long userModId, String langCode) {
		List<DropDownGroup> entityDropDownList = new ArrayList<>();
		LOGGER.info("This is getEntitySelectList method");
		try {
			UserModified userMod = userModifiedRepo.findByUserModifiedId(userModId);
			UserModDetailsJsonBean data = new Gson().fromJson(userMod.getUserDetailsJson(), UserModDetailsJsonBean.class);
			List<EntityBean> entityListSelected = null;
			if (!StringUtils.isEmpty(data.getSelectedBank())) {
				List<String> newEntityList = Arrays.asList(data.getSelectedBank().split("\\s*,\\s*"));
				List<Long> longEntityIds = newEntityList.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				Long longArray[] = longEntityIds.toArray(new Long[0]);
				entityListSelected = entityRepo.fetchDataByEntityIds(longArray);
			}

			Map<String, DropDownGroup> entityMap = new HashMap<>();
			if (entityListSelected != null && !entityListSelected.isEmpty()) {
				for (EntityBean ent : entityListSelected) {
					Optional<SubCategoryLabel> subCategoryLabel = ent.getSubCategory().getSubCatLblSet().stream().filter(i -> i.getLangIdFk().getLanguageCode().equals(langCode)).findAny();
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
					entityDropDownList.add(entityMapValue.getValue());
				}

			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
		}
		return entityDropDownList;
	}

	public List<DropDownGroup> getModifiedNBFCEntitySelectListV2(Long userModId, String langCode) {
		List<DropDownGroup> entityDropDownList = new ArrayList<>();
		LOGGER.info("This is getModifiedNBFCEntitySelectListV2 method");
		try {
			UserModified userMod = userModifiedRepo.findByUserModifiedId(userModId);
			UserModDetailsJsonBean data = new Gson().fromJson(userMod.getUserDetailsJson(), UserModDetailsJsonBean.class);
			List<EntityBean> entityListSelected = null;
			if (!StringUtils.isEmpty(data.getSelectedUcbOrNbfc())) {
				List<String> newEntityList = Arrays.asList(data.getSelectedUcbOrNbfc().split("\\s*,\\s*"));
				List<Long> longEntityIds = newEntityList.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				Long longArray[] = longEntityIds.toArray(new Long[0]);
				entityListSelected = entityRepo.fetchDataByEntityIds(longArray);
			}

			Map<String, DropDownGroup> entityMap = new HashMap<>();
			if (entityListSelected != null && !entityListSelected.isEmpty()) {
				for (EntityBean ent : entityListSelected) {
					Optional<SubCategoryLabel> subCategoryLabel = ent.getSubCategory().getSubCatLblSet().stream().filter(i -> i.getLangIdFk().getLanguageCode().equals(langCode)).findAny();
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
					entityDropDownList.add(entityMapValue.getValue());
				}

			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
		}
		return entityDropDownList;
	}

	public List<UserRoleDto> getModifiedRoleSelectListV2(Long userModId, String langCode) {
		LOGGER.info("This is getModifiedRoleSelectListV2 method");
		try {
			UserModified userMod = userModifiedRepo.findByUserModifiedId(userModId);
			UserModDetailsJsonBean data = new Gson().fromJson(userMod.getUserDetailsJson(), UserModDetailsJsonBean.class);
			if (!StringUtils.isEmpty(data.getSelectedRole())) {
				List<String> roleList = Arrays.asList(data.getSelectedRole().split("\\s*,\\s*"));
				List<Long> roleIds = roleList.stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
				List<UserRolePlatformDto> userRolePlatformDtos = userRoleLabelRepo.getAlottedRolesAndPlatForm(roleIds, langCode);
				return transformObject(userRolePlatformDtos, langCode);
			}

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString(), e);
		}
		return new ArrayList<>();
	}
}
