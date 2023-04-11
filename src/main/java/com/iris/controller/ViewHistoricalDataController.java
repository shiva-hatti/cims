package com.iris.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.ViewHistoricalDto;
import com.iris.model.Category;
import com.iris.model.EntityBean;
import com.iris.model.SubCategory;
import com.iris.model.UserMaster;
import com.iris.repository.EntityRepo;
import com.iris.repository.UserRoleEntityMapRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author svishwakarma
 *
 */
@RestController
@RequestMapping("/service/viewHistoricalDataController")
public class ViewHistoricalDataController {

	private static final Logger Logger = LogManager.getLogger(ReturnGroupController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private EntityService entityService;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private UserRoleEntityMapRepo userRoleEntityMapRepo;

	@RequestMapping(value = "/getEntityList", method = RequestMethod.POST)
	public ServiceResponse getEntityMasterList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ViewHistoricalDto viewHistoricalDto) {
		Logger.info("request received to get entity list for job processigid {}", jobProcessId);
		Map<String, Object> columnValueMap = null;

		List<EntityBean> entityBeanList = null;

		String langCode = null;
		try {

			if (StringUtils.isBlank(viewHistoricalDto.getLanguageCode()) || viewHistoricalDto.getRoleId() == null || viewHistoricalDto.getUserId() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			langCode = viewHistoricalDto.getLanguageCode();
			UserMaster userMaster = userMasterService.getDataById(viewHistoricalDto.getUserId());

			// code changes to work with list of sub category
			List<String> subCategoryList = new ArrayList<>();
			if (!StringUtils.isBlank(viewHistoricalDto.getSubCategoryCode())) {
				subCategoryList.add(viewHistoricalDto.getSubCategoryCode());
				viewHistoricalDto.setSubCategoryCodeList(subCategoryList);
			}

			if (userMaster != null) {

				if (userMaster.getRoleType().getRoleTypeId().equals(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal())) {

					// step 1: check if role id is RBI super user then prepare the bean and send
					// Now department super user also has the entities based on the user id so now there is no difference between department super user and department non super user
					// response
					if (userMaster.getDepartmentIdFk() != null) {

						entityBeanList = getEntityListForSuperUser(viewHistoricalDto);
						if (!CollectionUtils.isEmpty(entityBeanList)) {
							if (viewHistoricalDto.getIsCategoryWiseResponse()) {
								return new ServiceResponseBuilder().setStatus(true).setResponse(reArrangeEntityCategoryAndSubCategoryWise(entityBeanList)).build();
							} else {
								return new ServiceResponseBuilder().setStatus(true).setResponse(entityBeanList).build();
							}
						} else {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0639.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
						}
					}
				}

			} else {
				Logger.info("request completed to get entity list for job processigid {}", jobProcessId);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

		} catch (Exception e) {
			Logger.error("Exception in load Entity list for JobProcessingId {} Exception is ", jobProcessId, e);

		}
		return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

	}

	private List<EntityBean> getEntityListForSuperUser(ViewHistoricalDto viewHistoricalDto) {

		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), viewHistoricalDto.getCategoryCode());
		columnValueMap.put(ColumnConstants.LANGUAGE_CODE.getConstantVal(), viewHistoricalDto.getLanguageCode());
		columnValueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), viewHistoricalDto.getIsActive());
		if (!CollectionUtils.isEmpty((viewHistoricalDto.getSubCategoryCodeList()))) {
			columnValueMap.put(ColumnConstants.SUB_CAT_CODE_LIST.getConstantVal(), viewHistoricalDto.getSubCategoryCodeList());
		}
		if (!StringUtils.isBlank(viewHistoricalDto.getCategoryCode())) {
			columnValueMap.put(ColumnConstants.CAT_CODE.getConstantVal(), viewHistoricalDto.getCategoryCode());
		}

		if (!StringUtils.isBlank(viewHistoricalDto.getEntityNameLike())) {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), viewHistoricalDto.getEntityNameLike().toUpperCase());
		} else {
			columnValueMap.put(ColumnConstants.LIKE_STRING.getConstantVal(), GeneralConstants.EMPTY_STRING.getConstantVal());
		}
		if (StringUtils.isBlank(viewHistoricalDto.getReturnCode())) {
			return entityService.getDataByObject(columnValueMap, MethodConstants.GET_ENTITY_FOR_SUPER_USER.getConstantVal());
		}
		return entityRepo.getDataByLangCodeAndIsActiveAndByReturn(viewHistoricalDto.getLanguageCode(), viewHistoricalDto.getIsActive(), viewHistoricalDto.getEntityNameLike().toUpperCase(), viewHistoricalDto.getReturnCode());

	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
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

}
