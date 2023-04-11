package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.exception.ServiceException;
import com.iris.model.EntityLabelBean;
import com.iris.repository.EntityLabelRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author siddique
 *
 */

@Service
public class EntityLabelService implements GenericService<EntityLabelBean, Long> {

	@Autowired
	private EntityLabelRepo entityLabelRepo;

	@Override
	public EntityLabelBean add(EntityLabelBean entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(EntityLabelBean entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EntityLabelBean> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityLabelBean getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityLabelBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			//	List<String> labelKeys  = null;
			List<String> languageCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
						languageCode = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal())) {
				return entityLabelRepo.getDataLangCode(languageCode);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<EntityLabelBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<EntityLabelBean> getPaginatedDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			String languageCode = null;
			Long entityId = null;
			Object isActive = null;
			String likeString = null;

			if (columnValueMap != null) {
				if (columnValueMap.containsKey(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
					languageCode = (String) columnValueMap.get(ColumnConstants.LANGUAGE_CODE.getConstantVal());
				}
				if (columnValueMap.containsKey(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(ColumnConstants.ENTITYID.getConstantVal());
				}
				if (columnValueMap.containsKey(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = columnValueMap.get(ColumnConstants.IS_ACTIVE.getConstantVal());
				}
				if (columnValueMap.containsKey(ColumnConstants.LIKE_STRING.getConstantVal())) {
					likeString = (String) columnValueMap.get(ColumnConstants.LIKE_STRING.getConstantVal());
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_ENTITY_USER.getConstantVal()) && columnValueMap != null && !columnValueMap.isEmpty()) {
				Boolean isActives = (Boolean) isActive;

				Pageable pageable = PageRequest.of(((Long) columnValueMap.get(ColumnConstants.PAGEABLE.getConstantVal())).intValue(), ((Long) columnValueMap.get(ColumnConstants.FETCHSIZE.getConstantVal())).intValue());
				return entityLabelRepo.getEntityData(entityId, languageCode, isActives, likeString, pageable);

			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<EntityLabelBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			Object entityIdList = null;
			String languageCode = null;
			Object categoryCode = null;
			Long entityId = null;
			Object isActive = null;
			String subCategoryCodes = null;
			String categoryCodes = null;
			String likeString = null;
			Object subCategoryList = null;

			if (columnValueMap != null) {
				for (String columnName : columnValueMap.keySet()) {
					if (columnValueMap.get(columnName) != null) {
						if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_ID_LIST.getConstantVal())) {
							entityIdList = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
							languageCode = (String) columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
							entityId = (Long) columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.CAT_CODE.getConstantVal())) {
							categoryCode = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
							isActive = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.LIKE_STRING.getConstantVal())) {
							likeString = (String) columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal())) {
							subCategoryList = columnValueMap.get(columnName);
						}
					}
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_REGULATOR_USER.getConstantVal())) {
				List<Long> entityIdLists = (List<Long>) entityIdList;
				return entityLabelRepo.getEntityDataByEntityIdAndLangCode(entityIdLists, languageCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_FOR_ENTITY_USER.getConstantVal())) {
				Boolean isActives = (Boolean) isActive;
				List<String> subCategoryLists = (List<String>) subCategoryList;

				if (categoryCode != null && !CollectionUtils.isEmpty(subCategoryLists)) {
					categoryCodes = categoryCode.toString();
					return entityLabelRepo.getEntityDataWithSubCategoryAndCateCode(entityId, categoryCodes, languageCode, subCategoryLists, isActives, likeString);
				}

				else if (categoryCode == null && !CollectionUtils.isEmpty(subCategoryLists)) {
					return entityLabelRepo.getEntityDataWithSubCategory(entityId, languageCode, subCategoryLists, isActives, likeString);
				} else if (categoryCode != null && CollectionUtils.isEmpty(subCategoryLists)) {
					categoryCodes = categoryCode.toString();
					return entityLabelRepo.getEntityDataWithCateCode(entityId, categoryCodes, languageCode, isActives, likeString);
				} else if (categoryCode == null && CollectionUtils.isEmpty(subCategoryLists)) {
					return entityLabelRepo.getEntityData(entityId, languageCode, isActives, likeString);
				}

			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<EntityLabelBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityLabelBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(EntityLabelBean bean) throws ServiceException {

	}

}
