package com.iris.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.iris.dto.CategoryDto;
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.NbfcAndOtherCategoryDto;
import com.iris.dto.PageableEntityV2;
import com.iris.dto.SubCategoryDto;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.service.GenericService;

@Service
public class EntityServiceV2 implements GenericService<EntityBean, Long> {

	private static final Logger LOGGER = LogManager.getLogger(EntityServiceV2.class);

	@Autowired
	private UserMasterServiceV2 userMasterServiceV2;

	@Override
	public EntityBean add(EntityBean entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(EntityBean entity) throws ServiceException {
		return false;
	}

	@Override
	public List<EntityBean> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public EntityBean getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<EntityBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(EntityBean bean) throws ServiceException {

	}

	public List<EntityDto> getFlatEntityList(EntityMasterDto entityMasterDto) throws ApplicationException {

		UserDto userDto = prepareUserDto(entityMasterDto);
		UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);
		if (!ObjectUtils.isEmpty(userDetailsDto)) {
			List<EntityDto> entityDtos = userDetailsDto.getEntityDtos();
			sortEntityDtos(entityDtos);
			return entityDtos;
		}

		return null;
	}

	public PageableEntityV2 getPagableEntityList(EntityMasterDto entityMasterDto) throws ApplicationException {
		UserDto userDto = prepareUserDto(entityMasterDto);
		PageableEntityV2 pageableEntityV2 = userMasterServiceV2.getPagableEntityDetails(userDto);

		if (!CollectionUtils.isEmpty(pageableEntityV2.getContent())) {
			sortEntityDtos(pageableEntityV2.getContent());
		}

		return pageableEntityV2;
	}

	private UserDto prepareUserDto(EntityMasterDto entityMasterDto) {
		UserDto userDto = new UserDto();
		userDto.setUserId(entityMasterDto.getUserId());
		userDto.setLangCode(entityMasterDto.getLanguageCode());
		userDto.setIsActive(entityMasterDto.getIsActive());
		userDto.setSubCategoryCodes(entityMasterDto.getSubCategoryCodeList());
		userDto.setCategoryCodes(entityMasterDto.getCategoryCodeList());
		userDto.setLangCode(entityMasterDto.getLanguageCode());
		userDto.setEntityNameLike(entityMasterDto.getEntityNameLike());
		userDto.setRoleId(entityMasterDto.getRoleId());
		userDto.setPage(entityMasterDto.getPage());
		userDto.setFetchSize(entityMasterDto.getFetchSize());
		return userDto;
	}

	private void sortEntityDtos(List<EntityDto> entityDtos) {
		if (!CollectionUtils.isEmpty(entityDtos)) {
			entityDtos.sort((o1, o2) -> o1.getEntityName().compareTo(o2.getEntityName()));
		}
	}

	public List<CategoryDto> getCategoryWiseEntityList(EntityMasterDto entityMasterDto, String jobProcessId) throws ApplicationException {
		UserDto userDto = new UserDto();
		userDto.setUserId(entityMasterDto.getUserId());
		userDto.setLangCode(entityMasterDto.getLanguageCode());
		userDto.setIsActive(entityMasterDto.getIsActive());
		userDto.setSubCategoryCodes(entityMasterDto.getSubCategoryCodeList());
		userDto.setCategoryCodes(entityMasterDto.getCategoryCodeList());
		userDto.setLangCode(entityMasterDto.getLanguageCode());
		userDto.setRoleId(entityMasterDto.getRoleId());
		userDto.setEntityNameLike(entityMasterDto.getEntityNameLike());

		UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

		if (ObjectUtils.isEmpty(userDetailsDto)) {
			LOGGER.error(jobProcessId + " User Details Not found");
			return null;
		}

		List<EntityDto> entityDtoList = userDetailsDto.getEntityDtos();

		if (!CollectionUtils.isEmpty(entityDtoList)) {
			return arrangeEntityDataCategoryWise(entityDtoList);
		} else {
			LOGGER.error(jobProcessId + " Entity DTO list is empty");
		}

		return null;
	}

	public List<CategoryDto> getCategoryWiseEntityListForMultipleUsers(EntityMasterDto entityMasterDto, String jobProcessId) throws ApplicationException {
		List<Long> userIds = entityMasterDto.getUserIds();

		List<EntityDto> finalEntityDtoList = new ArrayList<>();
		for (Long userId : userIds) {
			UserDto userDto = new UserDto();
			userDto.setUserId(userId);
			userDto.setLangCode(entityMasterDto.getLanguageCode());
			userDto.setIsActive(entityMasterDto.getIsActive());
			userDto.setSubCategoryCodes(entityMasterDto.getSubCategoryCodeList());
			userDto.setCategoryCodes(entityMasterDto.getCategoryCodeList());
			userDto.setLangCode(entityMasterDto.getLanguageCode());
			userDto.setRoleId(entityMasterDto.getRoleId());
			userDto.setEntityNameLike(entityMasterDto.getEntityNameLike());

			UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

			if (ObjectUtils.isEmpty(userDetailsDto)) {
				LOGGER.error(jobProcessId + " User Details Not found");
				return null;
			}

			List<EntityDto> entityDtoList = userDetailsDto.getEntityDtos();

			if (!CollectionUtils.isEmpty(entityDtoList)) {

				entityDtoList.forEach(f -> {
					try {
						if (!CollectionUtils.isEmpty(finalEntityDtoList)) {
							if (finalEntityDtoList.indexOf(f) != -1) {
								f = finalEntityDtoList.get(finalEntityDtoList.indexOf(f));
								f.getMappedUserIds().add(userId);
							} else {
								List<Long> mappedUserIds = new ArrayList<>();
								mappedUserIds.add(userId);
								f.setMappedUserIds(mappedUserIds);
								finalEntityDtoList.add(f);
							}
						} else {
							List<Long> mappedUserIds = new ArrayList<>();
							mappedUserIds.add(userId);
							f.setMappedUserIds(mappedUserIds);
							finalEntityDtoList.add(f);
						}
					} catch (Exception e) {
						LOGGER.error(jobProcessId + " Exception occured while iterating entity list ", e);
					}
				});
			}
		}

		if (!CollectionUtils.isEmpty(finalEntityDtoList)) {
			return arrangeEntityDataCategoryWise(finalEntityDtoList);
		} else {
			LOGGER.error(jobProcessId + " Entity DTO list is empty");
		}

		return null;
	}

	public List<CategoryDto> getCategoryWiseEntityListForMultipleUsersV2(EntityMasterDto entityMasterDto, String jobProcessId) throws ApplicationException {
		List<Long> userIds = entityMasterDto.getUserIds();

		List<EntityDto> finalEntityDtoList = new ArrayList<>();
		for (Long userId : userIds) {
			UserDto userDto = new UserDto();
			userDto.setUserId(userId);
			userDto.setLangCode(entityMasterDto.getLanguageCode());
			userDto.setIsActive(entityMasterDto.getIsActive());
			userDto.setSubCategoryCodes(entityMasterDto.getSubCategoryCodeList());
			userDto.setCategoryCodes(entityMasterDto.getCategoryCodeList());
			userDto.setLangCode(entityMasterDto.getLanguageCode());
			userDto.setRoleId(entityMasterDto.getRoleId());
			userDto.setEntityNameLike(entityMasterDto.getEntityNameLike());

			UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetailsV2(userDto);

			if (ObjectUtils.isEmpty(userDetailsDto)) {
				LOGGER.error(jobProcessId + "getCategoryWiseEntityListForMultipleUsersV2- User Details Not found");
				return null;
			}

			List<EntityDto> entityDtoList = userDetailsDto.getEntityDtos();

			if (!CollectionUtils.isEmpty(entityDtoList)) {

				entityDtoList.forEach(f -> {
					try {
						if (!CollectionUtils.isEmpty(finalEntityDtoList)) {
							if (finalEntityDtoList.indexOf(f) != -1) {
								f = finalEntityDtoList.get(finalEntityDtoList.indexOf(f));
								f.getMappedUserIds().add(userId);
							} else {
								List<Long> mappedUserIds = new ArrayList<>();
								mappedUserIds.add(userId);
								f.setMappedUserIds(mappedUserIds);
								finalEntityDtoList.add(f);
							}
						} else {
							List<Long> mappedUserIds = new ArrayList<>();
							mappedUserIds.add(userId);
							f.setMappedUserIds(mappedUserIds);
							finalEntityDtoList.add(f);
						}
					} catch (Exception e) {
						LOGGER.error(jobProcessId + " getCategoryWiseEntityListForMultipleUsersV2-Exception occured while iterating entity list ", e);
					}
				});
			}
		}

		if (!CollectionUtils.isEmpty(finalEntityDtoList)) {
			return arrangeEntityDataCategoryWise(finalEntityDtoList);
		} else {
			LOGGER.error(jobProcessId + " Entity DTO list is empty");
		}

		return null;
	}

	public NbfcAndOtherCategoryDto arrangeEntityDataNbfcAndOtherCategoryWise(List<CategoryDto> categoryDtoList) {
		NbfcAndOtherCategoryDto nbfcAndOtherCategoryDto = new NbfcAndOtherCategoryDto();

		List<SubCategoryDto> nbfcCategoryDtos = new ArrayList<>();

		List<SubCategoryDto> otherCateDtos = new ArrayList<>();

		if (!CollectionUtils.isEmpty(categoryDtoList)) {
			categoryDtoList.forEach(f -> {
				if (f.getCategoryId().equals(2)) {
					nbfcCategoryDtos.addAll(f.getSubCategory());
				} else {
					otherCateDtos.addAll(f.getSubCategory());
				}
			});
			nbfcCategoryDtos.sort((SubCategoryDto s1, SubCategoryDto s2) -> s1.getSubCategoryName().compareTo(s2.getSubCategoryName()));
			otherCateDtos.sort((SubCategoryDto s1, SubCategoryDto s2) -> s1.getSubCategoryName().compareTo(s2.getSubCategoryName()));

			nbfcAndOtherCategoryDto.setNbfcCatDtos(nbfcCategoryDtos);
			nbfcAndOtherCategoryDto.setOtherCateDtos(otherCateDtos);
			return nbfcAndOtherCategoryDto;
		} else {
			return null;
		}

	}

	private List<CategoryDto> arrangeEntityDataCategoryWise(List<EntityDto> entityDtoList) {
		if (!CollectionUtils.isEmpty(entityDtoList)) {
			sortEntityDtos(entityDtoList);
			List<CategoryDto> categoryDtos = new ArrayList<>();

			for (EntityDto entityDto : entityDtoList) {

				if (categoryDtos.indexOf(entityDto.getCategoryDto()) != -1) {

					CategoryDto categoryDto = categoryDtos.get(categoryDtos.indexOf(entityDto.getCategoryDto()));

					if (categoryDto.getSubCategory().indexOf(entityDto.getSubCategoryDto()) != -1) {
						SubCategoryDto subCategoryDto = categoryDto.getSubCategory().get(categoryDto.getSubCategory().indexOf(entityDto.getSubCategoryDto()));
						EntityDto newEntityDto = prepareEntityDto(entityDto);
						subCategoryDto.getEntityDtoList().add(newEntityDto);
						if (!CollectionUtils.isEmpty(subCategoryDto.getEntityDtoList())) {
							subCategoryDto.setEntityCount(subCategoryDto.getEntityDtoList().size());
						}
					} else {
						List<SubCategoryDto> subCategoryDtos = new ArrayList<>();

						SubCategoryDto subCategoryDto = prepareSubCategoryDto(entityDto);
						categoryDto.getSubCategory().add(subCategoryDto);

						List<EntityDto> entityDtos = new ArrayList<>();

						EntityDto newEntityDto = prepareEntityDto(entityDto);
						entityDtos.add(newEntityDto);

						subCategoryDto.setEntityDtoList(entityDtos);
						if (!CollectionUtils.isEmpty(subCategoryDto.getEntityDtoList())) {
							subCategoryDto.setEntityCount(subCategoryDto.getEntityDtoList().size());
						}

						subCategoryDtos.add(subCategoryDto);
					}
				} else {
					List<EntityDto> entityDtos = new ArrayList<>();

					EntityDto newEntityDto = prepareEntityDto(entityDto);
					entityDtos.add(newEntityDto);

					List<SubCategoryDto> subCategoryDtos = new ArrayList<>();

					SubCategoryDto subCategoryDto = prepareSubCategoryDto(entityDto);
					subCategoryDto.setEntityDtoList(entityDtos);

					if (!CollectionUtils.isEmpty(subCategoryDto.getEntityDtoList())) {
						subCategoryDto.setEntityCount(subCategoryDto.getEntityDtoList().size());
					}

					subCategoryDtos.add(subCategoryDto);

					CategoryDto categoryDto = prepareCategoryDto(entityDto);

					categoryDto.setSubCategory(subCategoryDtos);
					categoryDtos.add(categoryDto);
				}
			}

			if (!CollectionUtils.isEmpty(categoryDtos)) {
				categoryDtos.sort((CategoryDto c1, CategoryDto c2) -> c1.getCategoryName().compareTo(c2.getCategoryName()));
				categoryDtos.forEach(f -> {
					if (!CollectionUtils.isEmpty(f.getSubCategory())) {
						f.getSubCategory().sort((SubCategoryDto s1, SubCategoryDto s2) -> s1.getSubCategoryName().compareTo(s2.getSubCategoryName()));
					}
				});
				return categoryDtos;
			}
		}

		return null;
	}

	private SubCategoryDto prepareSubCategoryDto(EntityDto entityDto) {
		SubCategoryDto subCategoryDto = new SubCategoryDto();
		subCategoryDto.setSubCategoryCode(entityDto.getSubCategoryDto().getSubCategoryCode());
		subCategoryDto.setSubCategoryId(entityDto.getSubCategoryDto().getSubCategoryId());
		subCategoryDto.setSubCategoryName(entityDto.getSubCategoryDto().getSubCategoryName());
		return subCategoryDto;
	}

	private CategoryDto prepareCategoryDto(EntityDto entityDto) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryCode(entityDto.getCategoryDto().getCategoryCode());
		categoryDto.setCategoryId(entityDto.getCategoryDto().getCategoryId());
		categoryDto.setCategoryName(entityDto.getCategoryDto().getCategoryName());
		return categoryDto;
	}

	private EntityDto prepareEntityDto(EntityDto entityDto) {
		EntityDto newEntityDto = new EntityDto();
		newEntityDto.setEntityCode(entityDto.getEntityCode());
		newEntityDto.setEntityName(entityDto.getEntityName());
		newEntityDto.setIfscCode(entityDto.getIfscCode());
		newEntityDto.setEntityId(entityDto.getEntityId());
		newEntityDto.setMappedUserIds(entityDto.getMappedUserIds());
		return newEntityDto;
	}
}
