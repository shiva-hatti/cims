/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.LabelDto;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.PageableEntity;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.BankTypeBean;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.CompanyType;
import com.iris.model.CountryMaster;
import com.iris.model.DynamicDropDownBean;
import com.iris.model.EntityBean;
import com.iris.model.EntityInfo;
import com.iris.model.EntityLabelBean;
import com.iris.model.Return;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnLabel;
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.BankTypeRepo;
import com.iris.repository.UserRoleReturnMappingRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityAuditorMappingService;
import com.iris.service.impl.EntityLabelService;
import com.iris.service.impl.EntityService;
import com.iris.service.impl.ReturnEntityMapServiceNew;
import com.iris.service.impl.UserMasterService;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 * 
 * @author psahoo
 *
 */

@RestController
@RequestMapping("/service/entityMasterService")
public class EntityMasterController {
	private static final Logger logger = LogManager.getLogger(EntityMasterController.class);

	@Autowired
	private EntityLabelService entityLabelService;

	@Autowired
	private EntityService entityService;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	private ReturnEntityMapServiceNew returnEntityMapService;

	@Autowired
	private GenericService<Return, Long> returnService;

	@Autowired
	private UserRoleReturnMappingRepo userRoleReturnMappingRepo;

	@Autowired
	private BankTypeRepo bankTypeRepo;

	@Autowired
	private EntityAuditorMappingService entityAuditorMappingService;

	@RequestMapping(value = "/getEntityList", method = RequestMethod.POST)
	public ServiceResponse getEntityMasterList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody EntityMasterDto entityMasterDto) {
		Boolean pageAble = entityMasterDto.getPage() != null ? true : false;
		logger.info("request received to get entity list for job processigid" + jobProcessId);
		Map<String, Object> columnValueMap = null;

		List<EntityBean> entityBeanList = null;
		Page<EntityBean> entityPagedBeanList = null;

		String langCode = null;
		try {

			if (StringUtils.isBlank(entityMasterDto.getLanguageCode()) || entityMasterDto.getRoleId() == null || entityMasterDto.getUserId() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			langCode = entityMasterDto.getLanguageCode();
			UserMaster userMaster = userMasterService.getDataById(entityMasterDto.getUserId());

			// code changes to work with list of sub category
			if (entityMasterDto.getSubCategoryCodeList() != null && !entityMasterDto.getSubCategoryCodeList().isEmpty()) {
				entityMasterDto.setSubCategoryCodeList(entityMasterDto.getSubCategoryCodeList());
			} else {
				List<String> subCategoryList = new ArrayList<>();
				if (!StringUtils.isBlank(entityMasterDto.getSubCategoryCode())) {
					subCategoryList.add(entityMasterDto.getSubCategoryCode());
					entityMasterDto.setSubCategoryCodeList(subCategoryList);
				}
			}

			if (userMaster != null) {

				if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {

					// step 1: check if role id is RBI super user or any department user then prepare the bean and send
					// Now department super user also has the entities based on the user id so now there is no difference between department super user and department non super user
					// response

					if (pageAble) {
						entityPagedBeanList = getPaginatedEntityListForSuperUser(entityMasterDto);
						PageableEntity result = new PageableEntity();
						result.setContent(entityPagedBeanList.getContent());
						result.setTotalCount(entityPagedBeanList.getTotalElements());
						return new ServiceResponseBuilder().setStatus(true).setResponse(result).build();

					} else {
						entityBeanList = getEntityListForSuperUser(entityMasterDto);
						if (!CollectionUtils.isEmpty(entityBeanList)) {
							if (entityMasterDto.getIsCategoryWiseResponse()) {
								return new ServiceResponseBuilder().setStatus(true).setResponse(reArrangeEntityCategoryAndSubCategoryWise(entityBeanList)).build();
							} else {
								return new ServiceResponseBuilder().setStatus(true).setResponse(entityBeanList).build();
							}
						} else {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0639.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
						}
					}

				}

				// Step 4 : if user is entity user
				if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE.getConstantLongVal())) {

					// UserEntityRole userEntityRole =
					// userEntityRoleService.getDataById(entityMasterDto.getUserId());

					EntityBean userMappedEntity = null;
					UserRoleMaster userRoleMaster = userMaster.getUsrRoleMstrSet().stream().filter(userRole -> userRole.getUserRole().getUserRoleId().equals(entityMasterDto.getRoleId())).findAny().orElse(null);
					if (userRoleMaster != null) {
						if (!CollectionUtils.isEmpty(userRoleMaster.getUserEntityRole())) {
							// Entity user always mapped to only one entity..hence getting first record from
							// set
							userMappedEntity = userRoleMaster.getUserEntityRole().iterator().next().getEntityBean();
						}
					}
					if (userMappedEntity != null) {
						if (pageAble) {
							return new ServiceResponseBuilder().setStatus(true).setResponse(getPagedEntityListForEntityUser(userMappedEntity, entityMasterDto)).build();
						} else {
							entityBeanList = getEntityListForEntityUser(userMappedEntity, entityMasterDto);
							if (CollectionUtils.isEmpty(entityBeanList)) {
								logger.info("request completed to get entity list for job processigid" + jobProcessId);

								return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0807.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0807.toString())).build();
							}

							logger.info("request completed to get entity list for job processigid" + jobProcessId);

							if (entityMasterDto.getIsCategoryWiseResponse()) {
								return new ServiceResponseBuilder().setStatus(true).setResponse(reArrangeEntityCategoryAndSubCategoryWise(entityBeanList)).build();
							} else {
								return new ServiceResponseBuilder().setStatus(true).setResponse(entityBeanList).build();
							}
						}
					} else {
						logger.info("request completed to get entity list for job processigid" + jobProcessId);

						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0807.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0807.toString())).build();
					}

				}
				if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal())) {
					//Fetch entity list mapped to auditor
					entityBeanList = getEntityListForAuditorUser(entityMasterDto);
					if (!CollectionUtils.isEmpty(entityBeanList)) {
						if (entityMasterDto.getIsCategoryWiseResponse()) {
							return new ServiceResponseBuilder().setStatus(true).setResponse(reArrangeEntityCategoryAndSubCategoryWise(entityBeanList)).build();
						} else {
							return new ServiceResponseBuilder().setStatus(true).setResponse(entityBeanList).build();
						}
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0639.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
					}

				} else {
					logger.info("request completed to get entity list for job processigid" + jobProcessId);

					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0656.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0656.toString())).build();
				}
			} else {
				logger.info("request completed to get entity list for job processigid" + jobProcessId);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

		} catch (Exception e) {
			logger.error("Exception in load Entity list for JobProcessingId " + jobProcessId + "Exception is" + e);

		}
		return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

	}

	@PostMapping(value = "/getReturnChannelMappingList")
	public ServiceResponse getEntityReturnChannelMapp(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnEntityMapDto returnChannelMapReqDto) {
		logger.info("request received to get entity return channel map list for job processigid : " + jobProcessId);

		try {
			validateReturnChannelMapReqDto(returnChannelMapReqDto);

			UserMaster userMaster = userMasterService.getDataById(returnChannelMapReqDto.getUserId());

			if (userMaster == null) {
				logger.error("User not found for job processigid" + jobProcessId);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {
				// Regulator User
				logger.info("Request for regulator User for job procesing Id : " + jobProcessId);
				if (returnChannelMapReqDto.getEntCodeList() != null) {
					return loadEntityReturnChannelMappingBasedUponEntCodeList(returnChannelMapReqDto);
				} else {
					return loadEntityReturnChannelMappingBasedUponCatAndSubCat(returnChannelMapReqDto);
				}
			} else {
				// Entity User
				logger.info("Request for entity User for job procesing Id : " + jobProcessId);
				EntityBean userMappedEntity = null;
				UserRoleMaster userRoleMaster = userMaster.getUsrRoleMstrSet().stream().filter(userRole -> userRole.getUserRole().getUserRoleId().equals(returnChannelMapReqDto.getRoleId())).findAny().orElse(null);
				if (userRoleMaster != null) {
					if (!CollectionUtils.isEmpty(userRoleMaster.getUserEntityRole())) {
						// Entity user always mapped to only one entity..hence getting first record from
						userMappedEntity = userRoleMaster.getUserEntityRole().iterator().next().getEntityBean();
						return loadEntityReturnChannelMappingForEntityUser(returnChannelMapReqDto, userMappedEntity);
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).setStatusCode(ErrorCode.E0639.toString()).build();
					}
				} else {
					return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).setStatusCode(ErrorCode.E0639.toString()).build();
				}
			}
		} catch (ApplicationException applicationException) {
			logger.error("Eception occured for job processigid" + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private PageableEntity getPagedEntityListForEntityUser(EntityBean userMappedEntity, EntityMasterDto entityMasterDto) {
		PageableEntity result = new PageableEntity();
		List<EntityLabelBean> entityLabelBeanList = null;
		Page<EntityLabelBean> entityPaginatedLabelBeanList = null;
		List<EntityBean> entityBeanList = null;
		Long entityId = userMappedEntity.getEntityId();

		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), entityId);
		columnValueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), entityMasterDto.getLanguageCode());
		columnValueMap.put(ColumnConstants.PAGEABLE.getConstantVal(), entityMasterDto.getPage());
		columnValueMap.put(ColumnConstants.FETCHSIZE.getConstantVal(), entityMasterDto.getFetchSize());
		columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), entityMasterDto.getIsActive());

		if (entityMasterDto.getCategoryCode() != null) {
			columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		}

		if (!CollectionUtils.isEmpty((entityMasterDto.getSubCategoryCodeList()))) {
			columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), entityMasterDto.getSubCategoryCodeList());
		}

		if (!StringUtils.isBlank(entityMasterDto.getEntityNameLike())) {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), entityMasterDto.getEntityNameLike().toUpperCase());
		} else {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
		}

		entityPaginatedLabelBeanList = entityLabelService.getPaginatedDataByObject(columnValueMap, MethodConstants.GET_ENTITY_FOR_ENTITY_USER.getConstantVal());
		entityLabelBeanList = entityPaginatedLabelBeanList.toList();
		if (!CollectionUtils.isEmpty(entityLabelBeanList)) {
			entityBeanList = new ArrayList<>();
			EntityBean entityBean = null;
			for (EntityLabelBean entityLabelBean : entityLabelBeanList) {
				entityBean = new EntityBean();
				entityBean.setEntityCode(entityLabelBean.getEntityBean().getEntityCode());
				entityBean.setEntityId(entityLabelBean.getEntityBean().getEntityId());
				entityBean.setEntityName(entityLabelBean.getEntityNameLabel());

				Category category = new Category();
				category.setCategoryId(entityLabelBean.getEntityBean().getCategory().getCategoryId());
				category.setCategoryCode(entityLabelBean.getEntityBean().getCategory().getCategoryCode());

				CategoryLabel catLabel = entityLabelBean.getEntityBean().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(entityMasterDto.getLanguageCode())).findAny().orElse(null);
				if (catLabel != null) {
					category.setCategoryName(catLabel.getCategoryLabel());
				} else {
					category.setCategoryName(entityLabelBean.getEntityBean().getCategory().getCategoryName());
				}

				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryId());
				subCategory.setSubCategoryCode(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryCode());
				SubCategoryLabel subCatLabel = entityLabelBean.getEntityBean().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(entityMasterDto.getLanguageCode())).findAny().orElse(null);
				if (subCatLabel != null) {
					subCategory.setSubCategoryName(subCatLabel.getSubCategoryLabel());
				} else {
					subCategory.setSubCategoryName(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryName());
				}

				entityBean.setCategory(category);
				entityBean.setSubCategory(subCategory);

				entityBeanList.add(entityBean);
			}

		}
		result.setContent(entityBeanList);
		result.setTotalCount(entityPaginatedLabelBeanList.getTotalElements());

		return result;
	}

	private List<EntityBean> getEntityListForEntityUser(EntityBean userMappedEntity, EntityMasterDto entityMasterDto) {
		List<EntityLabelBean> entityLabelBeanList = null;
		List<EntityBean> entityBeanList = null;
		Long entityId = userMappedEntity.getEntityId();

		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), entityId);
		columnValueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), entityMasterDto.getLanguageCode());

		columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), entityMasterDto.getIsActive());

		if (entityMasterDto.getCategoryCode() != null) {
			columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		}

		if (!CollectionUtils.isEmpty((entityMasterDto.getSubCategoryCodeList()))) {
			columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), entityMasterDto.getSubCategoryCodeList());
		}

		if (!StringUtils.isBlank(entityMasterDto.getEntityNameLike())) {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), entityMasterDto.getEntityNameLike().toUpperCase());
		} else {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
		}

		entityLabelBeanList = entityLabelService.getDataByObject(columnValueMap, MethodConstants.GET_ENTITY_FOR_ENTITY_USER.getConstantVal());

		if (!CollectionUtils.isEmpty(entityLabelBeanList)) {
			entityBeanList = new ArrayList<>();
			EntityBean entityBean = null;
			for (EntityLabelBean entityLabelBean : entityLabelBeanList) {
				entityBean = new EntityBean();
				entityBean.setEntityCode(entityLabelBean.getEntityBean().getEntityCode());
				entityBean.setIfscCode(entityLabelBean.getEntityBean().getIfscCode());

				entityBean.setEntityId(entityLabelBean.getEntityBean().getEntityId());
				entityBean.setEntityName(entityLabelBean.getEntityNameLabel());

				Category category = new Category();
				category.setCategoryId(entityLabelBean.getEntityBean().getCategory().getCategoryId());
				category.setCategoryCode(entityLabelBean.getEntityBean().getCategory().getCategoryCode());

				CategoryLabel catLabel = entityLabelBean.getEntityBean().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(entityMasterDto.getLanguageCode())).findAny().orElse(null);
				if (catLabel != null) {
					category.setCategoryName(catLabel.getCategoryLabel());
				} else {
					category.setCategoryName(entityLabelBean.getEntityBean().getCategory().getCategoryName());
				}

				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryId());
				subCategory.setSubCategoryCode(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryCode());
				subCategory.setCategory(category);
				SubCategoryLabel subCatLabel = entityLabelBean.getEntityBean().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(entityMasterDto.getLanguageCode())).findAny().orElse(null);
				if (subCatLabel != null) {
					subCategory.setSubCategoryName(subCatLabel.getSubCategoryLabel());
				} else {
					subCategory.setSubCategoryName(entityLabelBean.getEntityBean().getSubCategory().getSubCategoryName());
				}

				entityBean.setCategory(category);
				entityBean.setSubCategory(subCategory);
				if (entityLabelBean.getEntityBean().getCompType() != null) {
					CompanyType compType = new CompanyType();
					compType.setCompTypeId(entityLabelBean.getEntityBean().getCompType().getCompTypeId());
					compType.setCompTypeCode(entityLabelBean.getEntityBean().getCompType().getCompTypeCode());
					entityBean.setCompType(compType);
				}
				entityBeanList.add(entityBean);
			}

		}

		return entityBeanList;
	}

	private List<EntityBean> getEntityListForSuperUser(EntityMasterDto entityMasterDto) {

		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		columnValueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), entityMasterDto.getLanguageCode());
		columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), entityMasterDto.getIsActive());
		columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), entityMasterDto.getUserId());
		columnValueMap.put(ColumnConstants.ROLEID.getConstantVal(), entityMasterDto.getRoleId());
		UserMaster userMaster = userMasterService.getDataById(entityMasterDto.getUserId());

		columnValueMap.put(ColumnConstants.USER_MASTER_OBJ.getConstantVal(), userMaster);
		columnValueMap.put(ColumnConstants.MENU_ID.getConstantVal(), entityMasterDto.getMenuId());

		//changes done for Accepting category list 
		List<String> catCodeList = new ArrayList<>();
		if (entityMasterDto.getCategoryCode() != null && !entityMasterDto.getCategoryCode().isEmpty()) {
			catCodeList.add(entityMasterDto.getCategoryCode());
		} else {
			catCodeList = entityMasterDto.getCategoryCodeList();
		}
		columnValueMap.put(ColumnConstants.CAT_CODE_LIST.getConstantVal(), catCodeList);

		if (!CollectionUtils.isEmpty((entityMasterDto.getSubCategoryCodeList()))) {
			columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), entityMasterDto.getSubCategoryCodeList());
		}

		if (!StringUtils.isBlank(entityMasterDto.getCategoryCode())) {
			columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		}

		if (!StringUtils.isBlank(entityMasterDto.getEntityNameLike())) {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), entityMasterDto.getEntityNameLike().toUpperCase());
		} else {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
		}

		return entityService.getDataByObject(columnValueMap, MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal());

	}

	private Page<EntityBean> getPaginatedEntityListForSuperUser(EntityMasterDto entityMasterDto) {

		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		columnValueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), entityMasterDto.getLanguageCode());
		columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), entityMasterDto.getIsActive());
		columnValueMap.put(ColumnConstants.PAGEABLE.getConstantVal(), entityMasterDto.getPage());
		columnValueMap.put(ColumnConstants.FETCHSIZE.getConstantVal(), entityMasterDto.getFetchSize());
		columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), entityMasterDto.getUserId());

		//changes done for Accepting category list 
		List<String> catCodeList = new ArrayList<>();
		if (entityMasterDto.getCategoryCode() != null && !entityMasterDto.getCategoryCode().isEmpty()) {
			catCodeList.add(entityMasterDto.getCategoryCode());
		} else {
			catCodeList = entityMasterDto.getCategoryCodeList();
		}
		columnValueMap.put(ColumnConstants.CAT_CODE_LIST.getConstantVal(), catCodeList);

		if (!CollectionUtils.isEmpty((entityMasterDto.getSubCategoryCodeList()))) {
			columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), entityMasterDto.getSubCategoryCodeList());
		}
		if (!StringUtils.isBlank(entityMasterDto.getCategoryCode())) {
			columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
		}

		if (!StringUtils.isBlank(entityMasterDto.getEntityNameLike())) {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), entityMasterDto.getEntityNameLike().toUpperCase());
		} else {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
		}

		return entityService.getPagedDataByObject(columnValueMap, MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal());

	}

	private void validateReturnChannelMapReqDto(ReturnEntityMapDto returnChannelMapReqDto) throws ApplicationException {
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
		}

		if (!errorMessage.equals("")) {
			throw new ApplicationException(ErrorCode.EC0391.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()));
		}
	}

	private ServiceResponse loadEntityReturnChannelMappingBasedUponEntCodeList(ReturnEntityMapDto returnChannelMapReqDto) {

		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnChannelMapReqDto.getRoleId());
		valueMap.put(ColumnConstants.ENT_CODE_LIST.getConstantVal(), returnChannelMapReqDto.getEntCodeList());
		valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnChannelMapReqDto.getIsActive());
		valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnChannelMapReqDto.getLangCode());
		valueMap.put(ColumnConstants.API_CHANNEL.getConstantVal(), returnChannelMapReqDto.getApiChannel());
		valueMap.put(ColumnConstants.UPLOAD_CHANNEL.getConstantVal(), returnChannelMapReqDto.getUploadChannel());
		valueMap.put(ColumnConstants.WEB_CHANNEL.getConstantVal(), returnChannelMapReqDto.getWebChannel());
		valueMap.put(ColumnConstants.STS_CAHNNEL.getConstantVal(), returnChannelMapReqDto.getStsChannel());
		valueMap.put(ColumnConstants.EMAIL_CHANNEL.getConstantVal(), returnChannelMapReqDto.getEmailChannel());
		valueMap.put(ColumnConstants.IS_CHANNEL_TO_CONSIDER.getConstantVal(), returnChannelMapReqDto.getIsChanelToConsider());

		List<ReturnEntityMappingNew> returnChannelMappingNew = returnEntityMapService.getDataByObject(valueMap, MethodConstants.GET_RET_ENT_MAP_BY_ROLE_ID_ENT_CODE.getConstantVal());

		List<EntityBean> entityList = new ArrayList<>();

		for (ReturnEntityMappingNew returnEntityMappingNew : returnChannelMappingNew) {
			EntityBean entityBean = entityList.stream().filter(s -> s.getEntityId().equals(returnEntityMappingNew.getEntityId())).findAny().orElse(null);
			if (entityBean != null) {
				List<Return> returnList = entityBean.getReturnList();
				if (!CollectionUtils.isEmpty(returnList)) {
					Return returnObjFromList = returnList.stream().filter(f -> f.getReturnId().equals(returnEntityMappingNew.getReturnId())).findAny().orElse(null);
					if (returnObjFromList != null) {
						if (returnObjFromList.isApiChannel() != returnEntityMappingNew.isApiChannel()) {
							returnObjFromList.setMultipleChannelFound(true);
						}
						if (returnObjFromList.isWebChannel() != returnEntityMappingNew.isWebChannel()) {
							returnObjFromList.setMultipleChannelFound(true);
						}
						if (returnObjFromList.isEmailChannel() != returnEntityMappingNew.isEmailChannel()) {
							returnObjFromList.setMultipleChannelFound(true);
						}
						if (returnObjFromList.isStsChannel() != returnEntityMappingNew.isStsChannel()) {
							returnObjFromList.setMultipleChannelFound(true);
						}
						if (returnObjFromList.isUploadChannel() != returnEntityMappingNew.isUploadChannel()) {
							returnObjFromList.setMultipleChannelFound(true);
						}
						returnObjFromList.setUploadChannel(returnEntityMappingNew.isUploadChannel());
						returnObjFromList.setWebChannel(returnEntityMappingNew.isWebChannel());
						returnObjFromList.setApiChannel(returnEntityMappingNew.isApiChannel());
						returnObjFromList.setEmailChannel(returnEntityMappingNew.isEmailChannel());
						returnObjFromList.setStsChannel(returnEntityMappingNew.isStsChannel());
						returnObjFromList.setReturnPropertyIdFk(returnEntityMappingNew.getReturnPropertyIdFk());

					} else {
						// Return not present
						Return newReturnObj = new Return();
						newReturnObj.setReturnId(returnEntityMappingNew.getReturnId());
						newReturnObj.setReturnCode(returnEntityMappingNew.getReturnCode());
						newReturnObj.setReturnName(returnEntityMappingNew.getReturnName());
						newReturnObj.setUploadChannel(returnEntityMappingNew.isUploadChannel());
						newReturnObj.setWebChannel(returnEntityMappingNew.isWebChannel());
						newReturnObj.setApiChannel(returnEntityMappingNew.isApiChannel());
						newReturnObj.setEmailChannel(returnEntityMappingNew.isEmailChannel());
						newReturnObj.setStsChannel(returnEntityMappingNew.isStsChannel());
						newReturnObj.setReturnPropertyIdFk(returnEntityMappingNew.getReturnPropertyIdFk());
						returnList.add(newReturnObj);
						entityBean.setReturnList(returnList);
					}
				}
			} else {
				EntityBean newEntityBean = new EntityBean();
				newEntityBean.setEntityId(returnEntityMappingNew.getEntityId());
				newEntityBean.setEntityName(returnEntityMappingNew.getEntityName());

				// Return not present
				List<Return> returnList = new ArrayList<>();

				Return newReturnObj = new Return();
				newReturnObj.setReturnId(returnEntityMappingNew.getReturnId());
				newReturnObj.setReturnCode(returnEntityMappingNew.getReturnCode());
				newReturnObj.setReturnName(returnEntityMappingNew.getReturnName());
				newReturnObj.setUploadChannel(returnEntityMappingNew.isUploadChannel());
				newReturnObj.setWebChannel(returnEntityMappingNew.isWebChannel());
				newReturnObj.setApiChannel(returnEntityMappingNew.isApiChannel());
				newReturnObj.setEmailChannel(returnEntityMappingNew.isEmailChannel());
				newReturnObj.setStsChannel(returnEntityMappingNew.isStsChannel());
				newReturnObj.setReturnPropertyIdFk(returnEntityMappingNew.getReturnPropertyIdFk());
				returnList.add(newReturnObj);
				newEntityBean.setReturnList(returnList);
				entityList.add(newEntityBean);
			}
		}

		return new ServiceResponseBuilder().setStatus(true).setResponse(entityList).build();

	}

	private ServiceResponse loadEntityReturnChannelMappingBasedUponCatAndSubCat(ReturnEntityMapDto returnChannelMapReqDto) {

		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnChannelMapReqDto.getRoleId());
		valueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), returnChannelMapReqDto.getCategoryId());
		valueMap.put(ColumnConstants.SUB_CATEGORY_ID.getConstantVal(), returnChannelMapReqDto.getSubCategoryId());
		valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnChannelMapReqDto.getIsActive());
		valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnChannelMapReqDto.getLangCode());

		//changes done for Accepting category list 
		List<Long> catIdList = new ArrayList<>();
		if (returnChannelMapReqDto.getCategoryId() != null && returnChannelMapReqDto.getCategoryId() != 0) {
			catIdList.add(returnChannelMapReqDto.getCategoryId());
		} else {
			catIdList = returnChannelMapReqDto.getCategoryIdList();
		}
		valueMap.put(ColumnConstants.CATEGORY_ID_LIST.getConstantVal(), catIdList);

		//changes done for Accepting subcategory list 
		List<Long> subCatIdList = new ArrayList<>();
		if (returnChannelMapReqDto.getSubCategoryId() != null && returnChannelMapReqDto.getSubCategoryId() != 0) {
			subCatIdList.add(returnChannelMapReqDto.getSubCategoryId());
		} else {
			subCatIdList = returnChannelMapReqDto.getSubCategoryIdList();
		}
		valueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), subCatIdList);

		List<ReturnEntityMappingNew> returnChannelMappNew = returnEntityMapService.getDataByObject(valueMap, MethodConstants.GET_RET_ENT_MAP_BY_ROLE_CAT_SUBCAT_ID.getConstantVal());

		if (CollectionUtils.isEmpty(returnChannelMappNew)) {
			return new ServiceResponseBuilder().setStatus(true).setStatusMessage(ErrorCode.E0637.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0637.toString())).build();
		}

		List<SubCategory> subCategoryList = new ArrayList<>();

		for (ReturnEntityMappingNew returnEntMapNew : returnChannelMappNew) {
			SubCategory subCategory = subCategoryList.stream().filter(s -> s.getSubCategoryId().equals(returnEntMapNew.getSubCategoryId())).findAny().orElse(null);
			if (subCategory != null) {
				List<Return> returnList = subCategory.getReturnList();
				if (!CollectionUtils.isEmpty(returnList)) {
					Return returnObjFromList = returnList.stream().filter(f -> f.getReturnId().equals(returnEntMapNew.getReturnId())).findAny().orElse(null);
					if (returnObjFromList != null) {
						if (!returnObjFromList.isMultipleChannelFound()) {
							if (returnObjFromList.isApiChannel() != returnEntMapNew.isApiChannel()) {
								returnObjFromList.setMultipleChannelFound(true);
							}
							if (returnObjFromList.isWebChannel() != returnEntMapNew.isWebChannel()) {
								returnObjFromList.setMultipleChannelFound(true);
							}
							if (returnObjFromList.isEmailChannel() != returnEntMapNew.isEmailChannel()) {
								returnObjFromList.setMultipleChannelFound(true);
							}
							if (returnObjFromList.isStsChannel() != returnEntMapNew.isStsChannel()) {
								returnObjFromList.setMultipleChannelFound(true);
							}
							if (returnObjFromList.isUploadChannel() != returnEntMapNew.isUploadChannel()) {
								returnObjFromList.setMultipleChannelFound(true);
							}
							if (returnObjFromList.isMultipleChannelFound()) {
								returnObjFromList.setUploadChannel(false);
								returnObjFromList.setWebChannel(false);
								returnObjFromList.setApiChannel(false);
								returnObjFromList.setEmailChannel(false);
								returnObjFromList.setStsChannel(false);
							} else {
								returnObjFromList.setUploadChannel(returnEntMapNew.isUploadChannel());
								returnObjFromList.setWebChannel(returnEntMapNew.isWebChannel());
								returnObjFromList.setApiChannel(returnEntMapNew.isApiChannel());
								returnObjFromList.setEmailChannel(returnEntMapNew.isEmailChannel());
								returnObjFromList.setStsChannel(returnEntMapNew.isStsChannel());
							}
						}
					} else {
						// Return not present
						Return newReturnObj = new Return();
						newReturnObj.setReturnId(returnEntMapNew.getReturnId());
						newReturnObj.setReturnCode(returnEntMapNew.getReturnCode());
						newReturnObj.setReturnName(returnEntMapNew.getReturnName());
						newReturnObj.setUploadChannel(returnEntMapNew.isUploadChannel());
						newReturnObj.setWebChannel(returnEntMapNew.isWebChannel());
						newReturnObj.setApiChannel(returnEntMapNew.isApiChannel());
						newReturnObj.setEmailChannel(returnEntMapNew.isEmailChannel());
						newReturnObj.setStsChannel(returnEntMapNew.isStsChannel());
						returnList.add(newReturnObj);
						subCategory.setReturnList(returnList);
					}
				}
			} else {
				SubCategory newSubCategory = new SubCategory();
				newSubCategory.setSubCategoryId(returnEntMapNew.getSubCategoryId());
				newSubCategory.setSubCategoryName(returnEntMapNew.getSubCategoryName());

				// Return not present
				List<Return> returnList = new ArrayList<>();

				Return newReturnObj = new Return();
				newReturnObj.setReturnId(returnEntMapNew.getReturnId());
				newReturnObj.setReturnCode(returnEntMapNew.getReturnCode());
				newReturnObj.setReturnName(returnEntMapNew.getReturnName());
				newReturnObj.setUploadChannel(returnEntMapNew.isUploadChannel());
				newReturnObj.setWebChannel(returnEntMapNew.isWebChannel());
				newReturnObj.setApiChannel(returnEntMapNew.isApiChannel());
				newReturnObj.setEmailChannel(returnEntMapNew.isEmailChannel());
				newReturnObj.setStsChannel(returnEntMapNew.isStsChannel());
				returnList.add(newReturnObj);
				newSubCategory.setReturnList(returnList);
				subCategoryList.add(newSubCategory);
			}
		}

		return new ServiceResponseBuilder().setStatus(true).setResponse(subCategoryList).build();
	}

	private ServiceResponse loadEntityReturnChannelMappingForEntityUser(ReturnEntityMapDto returnChannelMapReqDto, EntityBean userMappedEntity) {
		if (returnChannelMapReqDto.getEntCodeList() != null) {
			if (returnChannelMapReqDto.getEntCodeList().contains(userMappedEntity.getEntityCode())) {
				List<EntityBean> entityList = new ArrayList<>();
				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(userMappedEntity.getEntityId());

				EntityLabelBean entityLabel = userMappedEntity.getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equalsIgnoreCase(returnChannelMapReqDto.getLangCode())).findAny().orElse(null);

				if (entityLabel != null) {
					entityBean.setEntityName(entityLabel.getEntityNameLabel());
				} else {
					entityBean.setEntityName(userMappedEntity.getEntityName());
				}
				entityBean.setReturnList(loadEntityReturnChannelMappingForEntityUserInternal(userMappedEntity, returnChannelMapReqDto));
				entityList.add(entityBean);

				return new ServiceResponseBuilder().setStatus(true).setResponse(entityList).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0635.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0635.toString())).build();
			}
		} else {
			if (userMappedEntity.getCategory().getCategoryId().equals(returnChannelMapReqDto.getCategoryId()) && userMappedEntity.getSubCategory().getSubCategoryId().equals(returnChannelMapReqDto.getSubCategoryId())) {

				List<SubCategory> subCategoryList = new ArrayList<>();

				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(userMappedEntity.getSubCategory().getSubCategoryId());
				SubCategoryLabel subCategoryLabel = userMappedEntity.getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(returnChannelMapReqDto.getLangCode())).findAny().orElse(null);
				if (subCategoryLabel != null) {
					subCategory.setSubCategoryName(subCategoryLabel.getSubCategoryLabel());
				} else {
					subCategory.setSubCategoryName(userMappedEntity.getSubCategory().getSubCategoryName());
				}

				subCategory.setReturnList(loadEntityReturnChannelMappingForEntityUserInternal(userMappedEntity, returnChannelMapReqDto));

				subCategoryList.add(subCategory);
				return new ServiceResponseBuilder().setStatus(true).setResponse(subCategoryList).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).setStatusCode(ErrorCode.EC0391.toString()).build();
			}
		}
	}

	private List<Return> loadEntityReturnChannelMappingForEntityUserInternal(EntityBean userMappedEntity, ReturnEntityMapDto returnChannelMapReqDto) {
		List<ReturnEntityMappingNew> returnEntityMappingList = userMappedEntity.getReturnEntityMappingNew();

		if (returnChannelMapReqDto.getIsChanelToConsider().equals(Boolean.TRUE)) {
			returnEntityMappingList = returnEntityMappingList.stream().filter(item -> item.isApiChannel() == returnChannelMapReqDto.getApiChannel() && item.isEmailChannel() == returnChannelMapReqDto.getEmailChannel() && item.isStsChannel() == returnChannelMapReqDto.getStsChannel() && item.isUploadChannel() == returnChannelMapReqDto.getUploadChannel() && item.isWebChannel() == returnChannelMapReqDto.getWebChannel()).collect(Collectors.toList());
		}

		Comparator<Return> returnComparator = (Return r1, Return r2) -> r1.getReturnName().compareTo(r2.getReturnName());
		List<Return> returnList = prepareReturnListWithChannel(returnEntityMappingList, returnChannelMapReqDto.getLangCode());

		int mappedReturnCount = userRoleReturnMappingRepo.getMappedReturnCountForUserRole(returnChannelMapReqDto.getRoleId());

		if (mappedReturnCount > 0) {
			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.ROLEID.getConstantVal(), returnChannelMapReqDto.getRoleId());
			valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), returnChannelMapReqDto.getIsActive());
			valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnChannelMapReqDto.getLangCode());
			List<Return> mappedReturnList = returnService.getDataByObject(valueMap, MethodConstants.GET_RETURNS_BY_ROLE_ID.getConstantVal());

			List<Return> activeReturnList = mappedReturnList.stream().filter(f -> f.isRoleReturnMappingActive()).collect(Collectors.toList());
			List<Return> intersectReturnList = returnList.stream().filter(activeReturnList::contains).collect(Collectors.toList());
			Collections.sort(intersectReturnList, returnComparator);
			return intersectReturnList;
		} else {
			Collections.sort(returnList, returnComparator);
			return returnList;
		}

	}

	private List<Return> prepareReturnListWithChannel(List<ReturnEntityMappingNew> returnEntityMappingList, String langCode) {

		List<Return> returnList = new ArrayList<>();

		returnEntityMappingList.forEach(f -> {
			if (f.isActive() && f.getReturnObj().getIsActive().equals(Boolean.TRUE) && f.getReturnObj().getReturnGroupMapIdFk() != null && f.getReturnObj().getReturnGroupMapIdFk().getIsActive().equals(Boolean.TRUE)) {
				Return returnObj = new Return();
				returnObj.setReturnId(f.getReturnObj().getReturnId());

				ReturnLabel returnLabel = f.getReturnObj().getReturnLblSet().stream().filter(g -> g.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);

				if (returnLabel != null) {
					returnObj.setReturnName(returnLabel.getReturnLabel());
				} else {
					returnObj.setReturnName(f.getReturnObj().getReturnName());
				}

				returnObj.setReturnCode(f.getReturnObj().getReturnCode());
				returnObj.setWebChannel(f.isWebChannel());
				returnObj.setApiChannel(f.isApiChannel());
				returnObj.setUploadChannel(f.isUploadChannel());
				returnObj.setStsChannel(f.isStsChannel());
				returnObj.setEmailChannel(f.isEmailChannel());
				returnList.add(returnObj);
			}
		});
		return returnList;
	}

	private List<Category> reArrangeEntityCategoryAndSubCategoryWise(List<EntityBean> entityBeanList) {
		List<Category> categoryList = entityBeanList.stream().map(f -> f.getCategory()).collect(Collectors.toList());
		List<SubCategory> subCategoryList = entityBeanList.stream().map(f -> f.getSubCategory()).collect(Collectors.toList());

		// Get unique category list
		categoryList = categoryList.stream().filter(distinctByKeys(Category::getCategoryId, Category::getCategoryCode)).collect(Collectors.toList());

		// Get unique subcategory list
		subCategoryList = subCategoryList.stream().filter(distinctByKeys(SubCategory::getSubCategoryId, SubCategory::getSubCategoryCode)).collect(Collectors.toList());

		categoryList.sort((Category o1, Category o2) -> o1.getCategoryName().compareTo(o2.getCategoryName()));
		subCategoryList.sort((SubCategory o1, SubCategory o2) -> o1.getSubCategoryName().compareTo(o2.getSubCategoryName()));
		return prepareCategoryObject(categoryList, subCategoryList, entityBeanList);
	}

	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> {
			final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}

	private List<Category> prepareCategoryObject(List<Category> categoryList, List<SubCategory> subCategoryList, List<EntityBean> entitBeanList) {
		List<Category> finalCategoryList = new ArrayList<>();

		for (Category category : categoryList) {
			Category newCategory = new Category();
			newCategory.setCategoryId(category.getCategoryId());
			newCategory.setCategoryName(category.getCategoryName());
			newCategory.setCategoryCode(category.getCategoryCode());

			Set<SubCategory> subcategorySet = new LinkedHashSet<>();
			if (!CollectionUtils.isEmpty(subCategoryList)) {
				for (SubCategory subCategory : subCategoryList) {
					if (subCategory.getCategory().getCategoryId().equals(category.getCategoryId())) {
						SubCategory newSubcateCategory = new SubCategory();

						List<EntityBean> entityList = entitBeanList.stream().filter(f -> f.getSubCategory().getSubCategoryId().equals(subCategory.getSubCategoryId())).collect(Collectors.toList());

						newSubcateCategory.setSubCategoryCode(subCategory.getSubCategoryCode());
						newSubcateCategory.setSubCategoryId(subCategory.getSubCategoryId());
						newSubcateCategory.setSubCategoryName(subCategory.getSubCategoryName());

						newSubcateCategory.setEntity(new LinkedHashSet<EntityBean>(entityList));
						newSubcateCategory.setEntityCount(entityList.size());
						subcategorySet.add(newSubcateCategory);
					}
				}
				newCategory.setSubCategory(subcategorySet);
			}

			finalCategoryList.add(newCategory);
		}

		return finalCategoryList;
	}

	// this method would use to fetch the entity bean with the help of entity name is the input parameter
	@PostMapping(value = "/getEntityInfoForNonXBRLReturns")
	public ServiceResponse getEntityInfoForNonXBRLReturns(@RequestHeader(name = "JobProcessingId") String JobProcessingId, @RequestBody LabelDto labelDto) {
		logger.info("Request received to get Entity info for non xbrl return: " + JobProcessingId);

		try {
			if (!CollectionUtils.isEmpty(labelDto.getLabelKeyMap())) {
				Set<String> entityNameSet = new HashSet<>();

				labelDto.getLabelKeyMap().forEach((k, v) -> {
					if (StringUtils.isNotBlank(v)) {
						Map<String, Object> columnValueMap = new HashMap<>();
						columnValueMap.put(ColumnConstants.ENTITY_NAME.getConstantVal(), v);
						List<EntityBean> entiyBeanList = entityService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_NAME.getConstantVal());

						if (!CollectionUtils.isEmpty(entiyBeanList)) {
							entiyBeanList.forEach(x -> {
								entityNameSet.add(x.getEntityName().toUpperCase());
							});
						}
					}
				});

				if (!CollectionUtils.isEmpty(entityNameSet)) {
					logger.info("Request completed to get entity info for non xbrl return: entityMasterList" + entityNameSet.size());
					return new ServiceResponseBuilder().setStatus(Boolean.TRUE).setResponse(new Gson().toJson(entityNameSet)).build();
				}
			}
		} catch (Exception e) {
			logger.info("Exception occoured while getting pan info for non xbrl return" + e);
		}
		logger.info("Request completed to get pan info for non xbrl return: Blank response");
		return new ServiceResponseBuilder().setStatus(Boolean.TRUE).build();

	}

	//This method is for getting Non NBFC Entity List
	@GetMapping(value = "/getNonNBFCEntityList/{languageCode}")
	public ServiceResponse getNonNBFCEntityList(@PathVariable String languageCode) {
		logger.info("getNonNBFCEntityList data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		//List<EntityBean> entityBean ;
		List<EntityBean> listNonNbfcData = null;
		List<EntityBean> listNonNbfcDataNew = null;

		listNonNbfcDataNew = new ArrayList<>();
		listNonNbfcData = entityService.getActiveDataFor(EntityBean.class, null);

		for (EntityBean entityDto : listNonNbfcData) {
			EntityBean entityDtoNew = new EntityBean();

			EntityLabelBean entityLabelBean = entityDto.getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equalsIgnoreCase(languageCode)).findAny().orElse(null);
			entityDtoNew.setEntityId(entityDto.getEntityId());
			entityDtoNew.setEntityCode(entityDto.getEntityCode());
			entityDtoNew.setIfscCode(entityDto.getIfscCode());
			if (entityLabelBean != null) {
				entityDtoNew.setEntityName(entityLabelBean.getEntityNameLabel());
			} else {
				entityDtoNew.setEntityName(entityDto.getEntityName());
			}
			listNonNbfcDataNew.add(entityDtoNew);
		}
		//return new ServiceResponseBuilder().setStatus(true).setResponse(listNonNbfcData).build();
		return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listNonNbfcDataNew)).build();
	}

	private List<EntityBean> getEntityListForAuditorUser(EntityMasterDto entityMasterDto) {
		logger.info("Fetching Entity List mapped to auditor: EntityAuditorMapProcessor.getEntityListForAuditorUser");
		List<EntityBean> entityBeanList = null;
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), entityMasterDto.getLanguageCode());
			columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), entityMasterDto.getIsActive());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), entityMasterDto.getUserId());

			if (!CollectionUtils.isEmpty((entityMasterDto.getSubCategoryCodeList()))) {
				columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), entityMasterDto.getSubCategoryCodeList());
			}
			if (!StringUtils.isBlank(entityMasterDto.getCategoryCode())) {
				columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), entityMasterDto.getCategoryCode());
			}

			if (!StringUtils.isBlank(entityMasterDto.getEntityNameLike())) {
				columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), entityMasterDto.getEntityNameLike().toUpperCase());
			} else {
				columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
			}

			entityBeanList = entityAuditorMappingService.getEntityListForAuditorUser(columnValueMap);

		} catch (Exception e) {
			logger.info("Exception occured while getting Entity List mapped to auditor:getEntityListForAuditorUser" + e);
		}
		return entityBeanList;
	}

	/**
	 * This method is used to check whether the IFSC Code entered by user exists in the system.
	 */
	@PostMapping(value = "/checkIfscCodeExists/{ifscCode}")
	public ServiceResponse checkIfscCodeExists(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("ifscCode") String ifscCode) {
		logger.info("Checking whether the Ifsc Code exists in the system: checkIfscCodeExists");
		try {
			if (UtilMaster.isEmpty(ifscCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0183.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0183.toString())).build();
			}
			EntityBean entityObj = entityService.checkIfscCodeExists(ifscCode);
			if (entityObj != null) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E1572.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1572.toString())).build();
			}
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1572.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1572.toString())).build();
		}
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
	}

	/**
	 * This method is used to check whether the Entity code entered by user exists in the system.
	 */
	@PostMapping(value = "/checkEntityCodeExists/{entityCode}")
	public ServiceResponse checkEntityCodeExists(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable("entityCode") String entityCode) {
		logger.info("Checking whether the Entity code exists in the system: checkEntityCodeExists");
		try {
			if (UtilMaster.isEmpty(entityCode)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0183.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0183.toString())).build();
			}
			EntityBean entityObj = entityService.checkEntityCodeExists(entityCode);
			if (entityObj != null) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0124.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0124.toString())).build();
			}
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0124.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0124.toString())).build();
		}
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
	}

	/**
	 * This method is used to add new Entity.
	 */
	@PostMapping(value = "/addEntity")
	public ServiceResponse addEntity(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @RequestBody EntityInfo entityInfo) {
		logger.info("Add Entity: addEntity");
		ServiceResponse serviceResponse = null;
		try {
			serviceResponse = entityService.addEntity(entityInfo);
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

		return serviceResponse;
	}

	/**
	 * This method is used to fetch the Entity Details list.
	 */
	@GetMapping(value = "/getEntityDetailsList/{langCode}")
	public ServiceResponse getEntityDetailsList(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @PathVariable String langCode) {
		try {
			logger.info("Fetching the Entity Details List: getEntityDetailsList for jobProcessId: " + jobProcessId + " and UUID: " + uuid);
			//List<EntityInfo> entityList = entityService.getEntityDetailsList(langCode);
			List<EntityInfo> entityList = entityService.getEntityDetailsListV2(langCode);
			if (CollectionUtils.isEmpty(entityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
			String jsonResult = new Gson().toJson(entityList);
			if (!UtilMaster.isEmpty(jsonResult)) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(jsonResult).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * This method is used to update Entity.
	 */
	@PostMapping(value = "/updateEntity")
	public ServiceResponse updateEntity(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @RequestBody EntityInfo entityInfo) {
		logger.info("Update Entity: updateEntity");
		ServiceResponse serviceResponse = null;
		try {

			serviceResponse = entityService.updateEntity(entityInfo);

		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchBankTypeList")
	public ServiceResponse fetchBankTypeList(@RequestHeader(name = "AppId") String jobProcessingId, @RequestHeader(name = "UUID") String uuid) {
		ServiceResponse serviceResponse = null;
		try {
			logger.info("API call start of fetch Bank Type DropdownList");

			DynamicDropDownBean option = new DynamicDropDownBean();

			List<DynamicDropDownBean> optionList = null;

			optionList = new ArrayList<>();

			List<BankTypeBean> bankTypeList = bankTypeRepo.getBankTypeList();
			if (CollectionUtils.isEmpty(bankTypeList)) {
				logger.error("Exception while fetching Bank Type drop down , if drop down type list is empty");
				serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
				serviceResponse.setResponse(null);
				return serviceResponse;
			}
			for (BankTypeBean bankTypeObj : bankTypeList) {
				option = new DynamicDropDownBean();
				option.setKey(bankTypeObj.getId());
				option.setValue(bankTypeObj.getBankTypeName());
				optionList.add(option);
			}

			String jsonResult = new Gson().toJson(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(jsonResult);

		} catch (Exception e) {
			logger.error("Error in API call fetch Bank Type DropdownList" + e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}

	/**
	 * This method is used to fetch the Entity Details list.
	 */
	@PostMapping(value = "/getEntityByEntityCode/{langCode}")
	public ServiceResponse getEntityByEntityCode(@RequestHeader(name = "AppId") String jobProcessId, @RequestHeader(name = "UUID") String uuid, @RequestBody EntityInfo entityInfo, @PathVariable String langCode) {
		try {
			logger.info("Fetching the Entity Details By Entity Code: getEntityByEntityCode");
			EntityInfo entity = entityService.getEntityByEntityCode(entityInfo, langCode);
			if (UtilMaster.isEmpty(entity)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
			String jsonResult = new Gson().toJson(entity);
			if (!UtilMaster.isEmpty(jsonResult)) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(jsonResult).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
		} catch (Exception e) {
			logger.error(ErrorCode.EC0033.toString());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Added By psahoo
	 */
	@GetMapping("/getEntityMasterDetails/{langCode}/{entityName}")
	public ServiceResponse getEntityMasterDetails(@PathVariable(name = "langCode") String langCode, @PathVariable(name = "entityName") String entityName) {
		logger.info("fetch getEntityMasterDetails controller started. Language Code : " + langCode + " Entity Code/Name : " + entityName);
		ServiceResponse response = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<EntityBean> listOfActiveEntityData = null;

		try {
			listOfActiveEntityData = new ArrayList<>();
			optionList = new ArrayList<>();
			options = new Options();

			if (Validations.isEmpty(langCode) && Validations.isEmpty(entityName)) {
				logger.error("Language Code and Entity Code/Name is Empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			if (entityName.equals(GeneralConstants.ENTITY_COD.getConstantVal()) || entityName.equals(GeneralConstants.ENTITY_NAME.getConstantVal()) || entityName.equals(GeneralConstants.ENTITY_COD_ENTITY_NAME.getConstantVal())) {
				listOfActiveEntityData = entityService.getAllDataFor(EntityBean.class, null);
			}
			if (CollectionUtils.isEmpty(listOfActiveEntityData)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			if (langCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
				logger.info("Language is English");
				if (entityName.equals(GeneralConstants.ENTITY_COD.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityCode());
						option.setValue(entityBean.getEntityCode());
						optionList.add(option);
					}
				} else if (entityName.equals(GeneralConstants.ENTITY_NAME.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityName());
						option.setValue(entityBean.getEntityName());
						optionList.add(option);
					}
				} else if (entityName.equals(GeneralConstants.ENTITY_COD_ENTITY_NAME.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityCode());
						option.setValue(entityBean.getEntityName());
						optionList.add(option);
					}
				}
			} else if (langCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
				logger.info("Language is Hindi");
				if (entityName.equals(GeneralConstants.ENTITY_COD.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityCode());
						option.setValue(entityBean.getEntityCode());
						optionList.add(option);
					}
				} else if (entityName.equals(GeneralConstants.ENTITY_NAME.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityName());
						option.setValue(entityBean.getEntityName());
						optionList.add(option);
					}
				} else if (entityName.equals(GeneralConstants.ENTITY_COD_ENTITY_NAME.getConstantVal())) {
					for (EntityBean entityBean : listOfActiveEntityData) {
						option = new Option();
						option.setKey(entityBean.getEntityCode());
						option.setValue(entityBean.getEntityName());
						optionList.add(option);
					}
				}
			}

			options.setOptionList(optionList);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;
		} catch (Exception e) {
			logger.error("Exception occoured while featch Entity Details list" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}

	}
}
