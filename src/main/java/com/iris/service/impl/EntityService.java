package com.iris.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.BankTypeBean;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.CompanyType;
import com.iris.model.CompanyTypeLabel;
import com.iris.model.DeptUserEntityMapping;
import com.iris.model.EntUsrInfoMapping;
import com.iris.model.EntityBean;
import com.iris.model.EntityInfo;
import com.iris.model.FinYrFormat;
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.repository.BankTypeRepo;
import com.iris.repository.DeptUserEntityMappingRepo;
import com.iris.repository.EntUserInfoMappingRepo;
import com.iris.repository.EntityRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class EntityService implements GenericService<EntityBean, Long> {
	static final Logger LOGGER = LogManager.getLogger(EntityService.class);
	@Autowired
	private EntityRepo entityRepo;
	@Autowired
	DataSource datasource;
	@Autowired
	private DeptUserEntityMappingRepo deptUserEntityMappingRepo;
	@Autowired
	private EntUserInfoMappingRepo entUserInfoMappingRepo;
	@Autowired
	private BankTypeRepo bankTypeRepo;

	@Override
	public EntityBean add(EntityBean entity) throws ServiceException {
		try {
			return entityRepo.save(entity);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public boolean update(EntityBean entity) throws ServiceException {
		return false;
	}

	@Override
	public List<EntityBean> getDataByIds(Long[] ids) throws ServiceException {
		try {
			return entityRepo.fetchDataByEntityIds(ids);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<EntityBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> entCodeList = null;
			List<String> ifscCodeList = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_CODE.getConstantVal())) {
						entCodeList = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.IFSC_CODE.getConstantVal())) {
						ifscCodeList = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal())) {
				return entityRepo.getDataByEntityCodeInIgnoreCaseAndIsActiveTrue(entCodeList);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_DATA_BY_IFSC_CODE.getConstantVal())) {
				return entityRepo.getDataByIfscCodeInIgnoreCaseAndIsActiveTrue(ifscCodeList);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_DATA_BY_ENTITY_CODE_CONTAINING.getConstantVal())) {
				if (entCodeList != null) {
					return entityRepo.getDataByEntityCodeStartingWithAndIsActiveTrue(entCodeList.get(0));
				}
			}
		} catch (Exception e) {
			throw new ServiceException("Exception : ", e);
		}
		return null;
	}

	@Override
	public List<EntityBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		if (bean == EntityBean.class && id == null) {
			return entityRepo.fetchAllNonNBFCEntityData();
		}
		return null;
	}

	@Override
	public List<EntityBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		if (bean == EntityBean.class && id == null) {
			return entityRepo.fetchAllActiveEntityData();
		}
		return null;
	}

	@Override
	public void deleteData(EntityBean bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<EntityBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> categoryIds = null;
			List<Long> subCategoryIds = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase("categoryId")) {
						categoryIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase("subCategoryId")) {
						subCategoryIds = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase("getDataByCategoryIdAndSubCategoryId")) {
				return entityRepo.fetchAllActiveEntityDataByCategoryAndSubCategoryId(categoryIds, subCategoryIds);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public EntityBean getDataById(Long id) throws ServiceException {
		try {
			return entityRepo.findByEntityId(id);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * Fetch data with pagination
	 * 
	 * @param columnValueMap
	 * @param methodName
	 * @return
	 * @throws ServiceException
	 */
	public Page<EntityBean> getPagedDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long roleId = null;
			Long userId = null;
			Boolean isActive = false;
			String langCode = null;
			String likeString = null;
			Boolean pageAble = columnValueMap.get(ColumnConstants.PAGEABLE.getConstantVal()) != null ? true : false;

			if (columnValueMap.containsKey(ColumnConstants.ROLEID.getConstantVal())) {
				roleId = (Long) columnValueMap.get(ColumnConstants.ROLEID.getConstantVal());
			}
			if (columnValueMap.containsKey(ColumnConstants.USER_ID.getConstantVal())) {
				userId = (Long) columnValueMap.get(ColumnConstants.USER_ID.getConstantVal());
			}
			if (columnValueMap.containsKey(ColumnConstants.IS_ACTIVE.getConstantVal())) {
				isActive = (Boolean) columnValueMap.get(ColumnConstants.IS_ACTIVE.getConstantVal());
			}
			if (columnValueMap.containsKey(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
				langCode = (String) columnValueMap.get(ColumnConstants.LANGUAGE_CODE.getConstantVal());
			}
			if (columnValueMap.containsKey(ColumnConstants.LIKE_STRING.getConstantVal())) {
				likeString = (String) columnValueMap.get(ColumnConstants.LIKE_STRING.getConstantVal());
			}
			Pageable pageable;
			if (pageAble) {
				pageable = PageRequest.of(((Integer) columnValueMap.get(ColumnConstants.PAGEABLE.getConstantVal())).intValue(), ((Integer) columnValueMap.get(ColumnConstants.FETCHSIZE.getConstantVal())).intValue());
			} else {
				pageable = Pageable.unpaged();
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_RET_ENT_MAP_BY_ROLE_ID.getConstantVal())) {

				return entityRepo.getDataByRoleIdAndLangCodeAndIsActive(roleId, isActive, langCode, likeString, pageable);

			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ENT_MAP_BY_USER_ID.getConstantVal())) {
				return entityRepo.getDataByUserIdAndLangCode(userId, isActive, langCode, likeString, pageable);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal())) {

				return entityRepo.getDataByLangCodeUserIdAndIsActive(langCode, isActive, likeString, pageable, userId);
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<EntityBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long roleId = null;
			Boolean isActive = false;
			String langCode = null;
			String categoryCode = null;
			String subCategoryCode = null;
			Long userId = null;
			String likeString = null;
			String username = null;
			List<String> subCateCodeList = null;
			List<String> ifscCodeList = null;
			String entityName = null;
			List<String> entityCodeList = null;
			Boolean pageAble = columnValueMap.get(ColumnConstants.PAGEABLE.getConstantVal()) != null ? true : false;
			UserMaster userMaster = null;
			Long menuId = null;
			List<String> catCodeList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ROLEID.getConstantVal())) {
					roleId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = (Boolean) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
					langCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.CAT_CODE.getConstantVal())) {
					categoryCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CAT_CODE.getConstantVal())) {
					subCategoryCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
					userId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LIKE_STRING.getConstantVal())) {
					likeString = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_NAME.getConstantVal())) {
					username = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal())) {
					subCateCodeList = (List<String>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.IFSC_CODE.getConstantVal())) {
					ifscCodeList = (List<String>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_NAME.getConstantVal())) {
					entityName = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENT_CODE_LIST.getConstantVal())) {
					entityCodeList = (List<String>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_MASTER_OBJ.getConstantVal())) {
					userMaster = (UserMaster) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.MENU_ID.getConstantVal())) {
					menuId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.CAT_CODE_LIST.getConstantVal())) {
					catCodeList = (List<String>) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_RET_ENT_MAP_BY_ROLE_ID.getConstantVal())) {

				if (StringUtils.isBlank(categoryCode) && CollectionUtils.isEmpty(subCateCodeList)) {
					return entityRepo.getDataByRoleIdAndLangCodeAndIsActive(roleId, isActive, langCode, likeString);
				} else if (CollectionUtils.isEmpty(subCateCodeList) && !StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByRoleIdAndCatCodes(roleId, isActive, langCode, categoryCode, likeString);

				} else if (!CollectionUtils.isEmpty(subCateCodeList) && StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByRoleIdAndLangCodesAndSubCateCodes(roleId, isActive, langCode, subCateCodeList, likeString);
				} else if (!CollectionUtils.isEmpty(subCateCodeList) && !StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByRoleIdAndSubCateCodeAndCatCode(roleId, isActive, langCode, categoryCode, subCateCodeList, likeString);
				}

			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ENT_MAP_BY_USER_ID.getConstantVal())) {
				if (StringUtils.isBlank(categoryCode) && CollectionUtils.isEmpty(subCateCodeList)) {
					return entityRepo.getDataByUserIdAndLangCode(userId, isActive, langCode, likeString);
				} else if (CollectionUtils.isEmpty(subCateCodeList) && !StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByUserIdAndLangCodeAndCategoryCode(userId, isActive, langCode, categoryCode, likeString);
				} else if (!CollectionUtils.isEmpty(subCateCodeList) && StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByUserIdAndLangCodeAndSubCateCode(userId, isActive, langCode, subCateCodeList, likeString);
				} else if (!CollectionUtils.isEmpty(subCateCodeList) && !StringUtils.isBlank(categoryCode)) {
					return entityRepo.getDataByUserIdAndLangCodeAndCatCodeAndSubCateCode(userId, isActive, langCode, categoryCode, subCateCodeList, likeString);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal())) {

				if (CollectionUtils.isEmpty(catCodeList) && CollectionUtils.isEmpty(subCateCodeList) && userMaster != null && userMaster.getDepartmentIdFk().getIsMaster() && roleId.equals(GeneralConstants.RBI_SUPER_USER.getConstantLongVal()) && menuId == null && "".equals(likeString)) {
					return entityRepo.getDataByLangCodeAndIsActive(langCode, isActive);
				} else if (CollectionUtils.isEmpty(catCodeList) && CollectionUtils.isEmpty(subCateCodeList)) {
					return entityRepo.getDataByLangCodeUserIdAndIsActive(langCode, isActive, likeString, userId);
				}

				else if (CollectionUtils.isEmpty(subCateCodeList) && !CollectionUtils.isEmpty(catCodeList)) {
					return entityRepo.getDataByLangCodeUserIdAndIsActiveAndCategoryCode(langCode, isActive, catCodeList, likeString, userId);
				} else if (!CollectionUtils.isEmpty(subCateCodeList) && CollectionUtils.isEmpty(catCodeList)) {
					return entityRepo.getDataByLangCodeUserIdAndIsActiveAndSubCateCode(langCode, isActive, subCateCodeList, likeString, userId);
				} else if (!CollectionUtils.isEmpty(subCateCodeList) && !CollectionUtils.isEmpty(catCodeList)) {
					return entityRepo.getDataByLangCodeUserIdAndIsActiveAndSubCateCodeAndCateCode(langCode, isActive, catCodeList, subCateCodeList, likeString, userId);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_ENTITY_USER.getConstantVal())) {
				if (subCategoryCode != null) {
					return entityRepo.getEntityDataWithSubCategory(userId, categoryCode, langCode, subCategoryCode, isActive);
				} else {
					return entityRepo.getEntityData(userId, categoryCode, langCode, isActive);
				}
			}

			else if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_USERNAME.getConstantVal())) {
				return entityRepo.getEntityDataByUserName(username);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_IFSC_CODE_ENTITY_LIST_WITH_LABEL.getConstantVal())) {
				return entityRepo.getDataByLangCodeAndIsActiveAndIfscCode(langCode, isActive, ifscCodeList);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_ENTITY_NAME.getConstantVal())) {
				return entityRepo.getDataByEntityName(entityName);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_IFSC_CODE_ENTITY_LIST_WITH_LABEL_BY_ENTITY_CODE.getConstantVal())) {
				return entityRepo.getDataByLangCodeAndIsActiveAndIfscCodeByEntityCode(langCode, isActive, entityCodeList);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public List<EntityBean> getActiveEntityList(List<Long> entityBeanList) throws ServiceException {
		try {
			return entityRepo.getActiveEntityList(entityBeanList, true, true, true);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * This method is used to check whether the Firm Registration Number entered by user exists in the system.
	 * 
	 */
	public EntityBean checkIfscCodeExists(String ifscCode) {
		LOGGER.info("Checking whether the IFSC code exists in the system: EntityService.checkIfscCodeExists");
		try {
			return entityRepo.getEntityByIfscCode(ifscCode);

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

	/**
	 * This method is used to check whether the Firm Registration Number entered by user exists in the system.
	 * 
	 */
	public EntityBean checkEntityCodeExists(String entityCode) {
		LOGGER.info("Checking whether the IFSC code exists in the system: EntityService.checkEntityCodeExists");
		try {
			return entityRepo.getEntityByEntityCode(entityCode.toUpperCase());

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

	/**
	 * This method is used to add entity.
	 * 
	 */
	public ServiceResponse addEntity(EntityInfo entityInfo) {
		LOGGER.info("Adding entity: EntityService.addEntity");
		try {
			EntityBean entityBean = new EntityBean();
			Category category = new Category();
			category.setCategoryId(entityInfo.getCategoryId());
			entityBean.setCategory(category);

			SubCategory subCategory = new SubCategory();
			subCategory.setSubCategoryId(entityInfo.getSubCategoryId());
			entityBean.setSubCategory(subCategory);

			BankTypeBean bankType = new BankTypeBean();
			bankType.setId(entityInfo.getBankTypeId());
			entityBean.setBankTypeIdFk(bankType);

			CompanyType compType = new CompanyType();
			if (entityInfo.getSubCategoryId() != null) {
				if (entityInfo.getSubCategoryId().equals(GeneralConstants.FOREIGN_SUB_CATEGORY_ID.getConstantLongVal())) {
					compType.setCompTypeId(2L);
				} else {
					compType.setCompTypeId(1L);
				}
			}
			entityBean.setCompType(compType);
			entityBean.setEntityCode(entityInfo.getEntityCode());
			entityBean.setIfscCode(entityInfo.getIfscCode());
			entityBean.setEntityName(entityInfo.getEntityName());
			entityBean.setEntityNameBil(entityInfo.getEntityNameBil());
			entityBean.setEntityShortName(entityInfo.getEntityShortName());
			entityBean.setEntityShortNameBil(entityInfo.getEntityShortNameBil());
			entityBean.setEntityEmailId(entityInfo.getEntityEmailId());
			entityBean.setEntityPhoneNo(entityInfo.getEntityPhoneNo());

			FinYrFormat finYrFormat = new FinYrFormat();
			finYrFormat.setFinYrFormatId(entityInfo.getFinYrFormatId());
			entityBean.setFinYrFormat(finYrFormat);

			UserMaster userObj = new UserMaster();
			userObj.setUserId(entityInfo.getCreatedByUserId());
			entityBean.setCreatedBy(userObj);
			entityBean.setCreatedOn(DateManip.getCurrentDateTime());
			entityBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
			entityBean.setIsActive(Boolean.TRUE);
			entityBean.setIsNBFCEntity(entityInfo.getIsNBFCEntity());
			/*
			 * entityBean.setOpLevel1(entityInfo.getOpLevel1());
			 * entityBean.setOpLevel2(entityInfo.getOpLevel2());
			 * entityBean.setOpLevel3(entityInfo.getOpLevel3());
			 * entityBean.setOpLevel4(entityInfo.getOpLevel4());
			 * entityBean.setOpLevel5(entityInfo.getOpLevel5());
			 */
			entityRepo.save(entityBean);

			boolean flag = addEntityLabel(entityBean.getEntityId());

			if (flag) {
				UserMaster userMasterObj = new UserMaster();
				userMasterObj.setUserId(GeneralConstants.DEPT_USER_ID.getConstantLongVal());
				DeptUserEntityMapping deptUserEntMap = new DeptUserEntityMapping();
				deptUserEntMap.setUserIdFk(userMasterObj);
				deptUserEntMap.setEntity(entityBean);
				deptUserEntMap.setActive(Boolean.TRUE);
				deptUserEntityMappingRepo.save(deptUserEntMap);

				EntUsrInfoMapping entUserInfoMap = new EntUsrInfoMapping();
				entUserInfoMap.setEntityIdFk(entityBean);
				entUserInfoMap.setMaxUserBank(entityInfo.getMaxUserBank());
				entUserInfoMappingRepo.save(entUserInfoMap);

				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			} else {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

			}

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}

	}

	/**
	 * This method is used to execute procedure to add entity label.
	 * 
	 */
	public boolean addEntityLabel(Long entityId) throws SQLException {
		LOGGER.info("Executing procedure to add entity label: EntityService.addEntityLabel");
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.ENTITY_LABEL_PROCEDURE.getConstantVal());) {

			stmt.setLong(1, entityId);

			stmt.registerOutParameter(2, Types.INTEGER);

			stmt.executeQuery();
			int number = stmt.getInt(2);
			if (number > 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * This method is used to fetch the Entity Details List.
	 * 
	 */
	public List<EntityInfo> getEntityDetailsList(String langCode) {
		LOGGER.info("Fetching the Entity Details List: EntityService.getEntityDetailsList");
		List<EntityInfo> entityList = new ArrayList<>();
		List<EntityBean> entityBeanList = new ArrayList<>();

		try {
			EntityInfo entityObj;
			entityBeanList = entityRepo.getEntityDetailsList();

			if (!CollectionUtils.isEmpty(entityBeanList)) {
				for (EntityBean entityBean : entityBeanList) {
					entityObj = new EntityInfo();
					entityObj.setEntityId(entityBean.getEntityId());
					entityObj.setEntityCode(entityBean.getEntityCode());
					entityObj.setIfscCode(entityBean.getIfscCode());
					entityObj.setEntityName(entityBean.getEntityName());
					entityObj.setEntityNameBil(entityBean.getEntityNameBil());
					entityObj.setEntityShortName(entityBean.getEntityShortName());
					entityObj.setEntityShortNameBil(entityBean.getEntityShortNameBil());
					entityObj.setEntityEmailId(entityBean.getEntityEmailId());
					entityObj.setEntityPhoneNo(entityBean.getEntityPhoneNo());
					entityObj.setCreatedByUserName(entityBean.getCreatedBy().getUserName());
					entityObj.setCreatedOn(entityBean.getCreatedOn());
					entityObj.setIsActive(entityBean.getIsActive());
					entityObj.setOpLevel1(entityBean.getOpLevel1());
					entityObj.setOpLevel2(entityBean.getOpLevel2());
					entityObj.setOpLevel3(entityBean.getOpLevel3());
					entityObj.setOpLevel4(entityBean.getOpLevel4());
					entityObj.setOpLevel5(entityBean.getOpLevel5());

					if (entityBean.getBankTypeIdFk() != null) {
						entityObj.setBankTypeName(entityBean.getBankTypeIdFk().getBankTypeName());
					}
					if (entityBean.getModifiedBy() != null) {
						entityObj.setLastModifiedByUserName(entityBean.getModifiedBy().getUserName());
					}
					entityObj.setLastModifiedOn(entityBean.getLastModifiedOn());
					CategoryLabel catLabelBean = entityBean.getCategory().getCatLblSet().stream().filter(cl -> cl.getLangIdFk().getLanguageCode().equals(langCode)).findAny().orElse(null);
					if (catLabelBean != null) {
						entityObj.setCategoryName(catLabelBean.getCategoryLabel());
					} else {
						entityObj.setCategoryName(entityBean.getCategory().getCategoryName());
					}
					SubCategoryLabel subCatLabelBean = entityBean.getSubCategory().getSubCatLblSet().stream().filter(sc -> sc.getLangIdFk().getLanguageCode().equals(langCode)).findAny().orElse(null);
					if (subCatLabelBean != null) {
						entityObj.setSubCategoryName(subCatLabelBean.getSubCategoryLabel());
					} else {
						entityObj.setCategoryName(entityBean.getSubCategory().getSubCategoryName());
					}

					CompanyTypeLabel compTypeLabel = entityBean.getCompType().getComTypeLblSet().stream().filter(c -> c.getLangIdFk().getLanguageCode().equals(langCode)).findAny().orElse(null);
					if (compTypeLabel != null) {
						entityObj.setCompanyTypeName(compTypeLabel.getCompTypeLabel());
					}
					EntUsrInfoMapping entUserInfoMap = entUserInfoMappingRepo.getEntUserInfoMappingByEntId(entityObj.getEntityId());
					if (entUserInfoMap != null) {
						entityObj.setMaxUserBank(entUserInfoMap.getMaxUserBank());
					}
					entityList.add(entityObj);
				}

			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return entityList;
	}

	/**
	 * This method is used to fetch the Entity Details List.
	 * 
	 */
	public List<EntityInfo> getEntityDetailsListV2(String langCode) {
		LOGGER.info("Fetching the Entity Details List: EntityService.getEntityDetailsListV2");
		List<EntityInfo> entityList = null;
		try {
			entityList = entityRepo.getEntityDetailsListV2(langCode);
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return entityList;
	}

	/**
	 * This method is used to update Entity.
	 * 
	 */
	public ServiceResponse updateEntity(EntityInfo entityInfo) {
		LOGGER.info("Updating entity: EntityService.updateEntity");
		try {
			EntityBean entityBean = new EntityBean();
			EntityBean entityObj = entityRepo.findByEntityId(entityInfo.getEntityId());
			entityBean.setEntityCode(entityObj.getEntityCode());
			entityBean.setIfscCode(entityObj.getIfscCode());
			entityBean.setCreatedBy(entityObj.getCreatedBy());
			entityBean.setCreatedOn(entityObj.getCreatedOn());
			entityBean.setFinYrFormat(entityObj.getFinYrFormat());
			entityBean.setIsNBFCEntity(entityInfo.getIsNBFCEntity());
			entityBean.setEntityId(entityInfo.getEntityId());

			Category category = new Category();
			category.setCategoryId(entityInfo.getCategoryId());
			entityBean.setCategory(category);

			SubCategory subCategory = new SubCategory();
			subCategory.setSubCategoryId(entityInfo.getSubCategoryId());
			entityBean.setSubCategory(subCategory);
			CompanyType compType = new CompanyType();
			if (entityInfo.getSubCategoryId() != null) {
				if (entityInfo.getSubCategoryId().equals(GeneralConstants.FOREIGN_SUB_CATEGORY_ID.getConstantLongVal())) {
					compType.setCompTypeId(2L);
				} else {
					compType.setCompTypeId(1L);
				}
			}
			entityBean.setCompType(compType);

			BankTypeBean bankTypeObj = bankTypeRepo.getBankTypeByName(entityInfo.getBankTypeName());
			entityBean.setBankTypeIdFk(bankTypeObj);

			entityBean.setEntityName(entityInfo.getEntityName());
			entityBean.setEntityNameBil(entityInfo.getEntityNameBil());
			entityBean.setEntityShortName(entityInfo.getEntityShortName());
			entityBean.setEntityShortNameBil(entityInfo.getEntityShortNameBil());
			entityBean.setEntityEmailId(entityInfo.getEntityEmailId());
			entityBean.setEntityPhoneNo(entityInfo.getEntityPhoneNo());

			UserMaster userObj = new UserMaster();
			userObj.setUserId(entityInfo.getLastModifiedByUserId());
			entityBean.setModifiedBy(userObj);
			entityBean.setLastModifiedOn(DateManip.getCurrentDateTime());
			entityBean.setLastUpdatedOn(DateManip.getCurrentDateTime());
			entityBean.setIsActive(entityInfo.getIsActive());
			/*
			 * entityBean.setOpLevel1(entityInfo.getOpLevel1());
			 * entityBean.setOpLevel2(entityInfo.getOpLevel2());
			 * entityBean.setOpLevel3(entityInfo.getOpLevel3());
			 * entityBean.setOpLevel4(entityInfo.getOpLevel4());
			 * entityBean.setOpLevel5(entityInfo.getOpLevel5());
			 */
			entityRepo.save(entityBean);

			EntUsrInfoMapping entUserInfoMap = entUserInfoMappingRepo.getEntUserInfoMappingByEntId(entityInfo.getEntityId());
			entUserInfoMap.setMaxUserBank(entityInfo.getMaxUserBank());
			entUserInfoMappingRepo.save(entUserInfoMap);

			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();

		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}
	}

	/**
	 * This method is used to fetch the Entity Details list.
	 */
	public EntityInfo getEntityByEntityCode(EntityInfo entity, String langCode) {
		LOGGER.info("Retreiving entity by entity code: EntityService.getEntityByEntityCode");
		EntityInfo entityObj = null;
		try {
			EntityBean entityBean = entityRepo.getDataByLangCodeAndIsActiveAndEntityCode(langCode, true, entity.getEntityCode().toUpperCase());
			if (entityBean != null) {
				entityObj = new EntityInfo();
				entityObj.setEntityCode(entityBean.getEntityCode());
				entityObj.setEntityName(entityBean.getEntityName());
				entityObj.setEntityId(entityBean.getEntityId());
			}
		} catch (Exception e) {
			LOGGER.error(ErrorCode.EC0033.toString());
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return entityObj;
	}
}
