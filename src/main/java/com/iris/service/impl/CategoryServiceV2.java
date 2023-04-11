package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.dto.CategoryDto;
import com.iris.dto.EntityDto;
import com.iris.dto.SubCategoryDto;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.Category;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorCode;

@Service
public class CategoryServiceV2 implements GenericService<Category, Long> {

	@Autowired
	private UserMasterServiceV2 userMasterServiceV2;

	private static final Logger LOGGER = LogManager.getLogger(CategoryServiceV2.class);

	@Override
	public Category add(Category entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(Category entity) throws ServiceException {
		return false;
	}

	@Override
	public List<Category> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public Category getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Category> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Category> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Category> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Category> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Category> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(Category bean) throws ServiceException {

	}

	public List<CategoryDto> getApplicableCategoryForUser(UserDto userDto, String jobProcessId) throws ApplicationException {

		UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

		if (Objects.isNull(userDetailsDto)) {
			LOGGER.error("User not found for job processigid" + jobProcessId);
			throw new ApplicationException(ErrorCode.E0638.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString()));
		}

		List<CategoryDto> categoryDtos = new ArrayList<>();
		CategoryDto categoryDto;
		if (!CollectionUtils.isEmpty(userDetailsDto.getEntityDtos())) {
			for (EntityDto entityDto : userDetailsDto.getEntityDtos()) {
				categoryDto = new CategoryDto();
				categoryDto.setCategoryId(entityDto.getCategoryDto().getCategoryId());
				categoryDto.setCategoryName(entityDto.getCategoryDto().getCategoryName());
				categoryDto.setCategoryCode(entityDto.getCategoryDto().getCategoryCode());
				if (categoryDtos.indexOf(categoryDto) != -1) {
					categoryDto = categoryDtos.get(categoryDtos.indexOf(categoryDto));
					if (categoryDto.getSubCategory().indexOf(entityDto.getSubCategoryDto()) == -1) {
						categoryDto.getSubCategory().add(entityDto.getSubCategoryDto());
					}
				} else {
					List<SubCategoryDto> subCategoryDtos = new ArrayList<>();
					subCategoryDtos.add(entityDto.getSubCategoryDto());
					categoryDto.setSubCategory(subCategoryDtos);
					categoryDtos.add(categoryDto);
				}
			}

			// Sort Category DTO
			categoryDtos.sort((o1, o2) -> o1.getCategoryName().compareTo(o2.getCategoryName()));

			// Sort SubcategoryDto
			categoryDtos.stream().forEach(f -> {
				f.getSubCategory().sort((o1, o2) -> o1.getSubCategoryName().compareTo(o2.getSubCategoryName()));
			});

			return categoryDtos;
		} else {
			return null;
		}

	}

}
