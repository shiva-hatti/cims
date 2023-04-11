/**
 * 
 */
package com.iris.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.EntityDto;
import com.iris.dto.RequestApprovalInputBeanV2;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.RevisonRequestQueryOutputBean;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.dto.UserRoleDto;
import com.iris.dto.ViewRevisionRequestInputBeanV2;
import com.iris.exception.ApplicationException;
import com.iris.model.Category;
import com.iris.model.EntityBean;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.RevisionRequest;
import com.iris.model.SubCategory;
import com.iris.model.UserMaster;
import com.iris.service.impl.RevisionRequestService;
import com.iris.service.impl.UserMasterService;
import com.iris.service.impl.UserMasterServiceV2;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author apagaria
 *
 */
@Service
public class ViewRevisionRequestControllerServiceV2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewRevisionRequestControllerServiceV2.class);

	@Autowired
	private RevisionRequestService revisionRequestService;

	@Autowired
	private UserMasterServiceV2 userMasterServiceV2;

	@Autowired
	private UserMasterService userMasterService;

	public List<RevisonRequestQueryOutputBean> fetchViewRevisionRequest(String jobProcessId, ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2) throws Exception {
		LOGGER.debug(jobProcessId + " Fetch pending approval Request service start");
		UserDto userDto = createUserDto(viewRevisionRequestInputBeanV2);
		UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

		String startDateStr = DateManip.formatAppDateTime(new Date(viewRevisionRequestInputBeanV2.getStartDateLong()), "yyyy-MM-dd", "en");
		String endDateStr = DateManip.formatAppDateTime(new Date(viewRevisionRequestInputBeanV2.getEndDateLong()), "yyyy-MM-dd", "en");
		Date startDate = DateManip.convertStringToDate(startDateStr, DateConstants.YYYY_MM_DD.getDateConstants());
		Date endDate = DateManip.convertStringToDate(endDateStr, DateConstants.YYYY_MM_DD.getDateConstants());
		List<Long> roleIdList = validateRoles(viewRevisionRequestInputBeanV2, userDetailsDto);
		List<String> entityCodeList = new ArrayList<>();
		setEntityCodeList(entityCodeList, viewRevisionRequestInputBeanV2, userDetailsDto);
		// Fetch Return list by entity
		if (CollectionUtils.isEmpty(entityCodeList)) {
			throw new ApplicationException(ErrorCode.E0646.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0646.toString()));
		}
		Boolean isEntityUser = false;
		if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().intValue() == userDetailsDto.getRoleTypeId()) {
			Set<Long> roleCreatedByUserSet = fetchUserIdListforRoleCreateByUsers(userDetailsDto.getUserRoleDtos(), jobProcessId, roleIdList);
			if (!CollectionUtils.isEmpty(roleCreatedByUserSet)) {
				// check the role type is regulator for roles created by users
				Boolean isRegulatorUserCreatedRole = isRegulatorForRolesCreatedByUsers(roleCreatedByUserSet, jobProcessId);
				if (!isRegulatorUserCreatedRole) {
					isEntityUser = true;
				}
			}
		}
		List<UserRoleDto> userRoleDtoList = userDetailsDto.getUserRoleDtos();
		List<RevisonRequestQueryOutputBean> revisonRequestQueryOutputBeanList = revisionRequestService.fetchViewRevisionRequest(viewRevisionRequestInputBeanV2, startDate, endDate, entityCodeList, roleIdList, isEntityUser);
		LOGGER.debug(jobProcessId + " Fetch pending approval Request service End");
		return revisonRequestQueryOutputBeanList;
	}

	/**
	 * @param roleCreatedByUserSet
	 * @param jobProcessId
	 * @return
	 * @throws ApplicationException
	 */
	private Boolean isRegulatorForRolesCreatedByUsers(Set<Long> roleCreatedByUserSet, String jobProcessId) throws ApplicationException {
		return userMasterService.isRegulatorForRolesCreatedByUsers(GeneralConstants.REGULATOR_ROLE_TYPE_ID.getConstantLongVal(), roleCreatedByUserSet);
	}

	/**
	 * @param userRoleDtoList
	 * @param jobProcessId
	 * @return
	 * @throws ApplicationException
	 */
	private Set<Long> fetchUserIdListforRoleCreateByUsers(List<UserRoleDto> userRoleDtoList, String jobProcessId, List<Long> roleIdList) throws ApplicationException {
		Set<Long> roleCreatedByUserList = null;
		if (!CollectionUtils.isEmpty(userRoleDtoList)) {
			roleCreatedByUserList = new HashSet<>();
			for (UserRoleDto userRoleDto : userRoleDtoList) {
				if (roleIdList.contains(userRoleDto.getUserRoleId())) {
					roleCreatedByUserList.add(userRoleDto.getCreatedByRole());
				}
			}
		} else {
			throw new ApplicationException("ER002", "User role not found");
		}
		return roleCreatedByUserList;
	}

	private void setEntityCodeList(List<String> entityCodeList, ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2, UserDetailsDto userDetailsDto) throws ApplicationException {
		if (!StringUtils.isEmpty(viewRevisionRequestInputBeanV2.getEntityCode())) {
			entityCodeList.add(viewRevisionRequestInputBeanV2.getEntityCode());
		} else {
			List<EntityDto> entityDtoList = userDetailsDto.getEntityDtos();
			if (!CollectionUtils.isEmpty(entityDtoList)) {
				for (EntityDto entityDto : entityDtoList) {
					entityCodeList.add(entityDto.getEntityCode());
				}
			} else {
				throw new ApplicationException(ErrorCode.E0639.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString()));
			}
		}

	}

	private List<Long> validateRoles(ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2, UserDetailsDto userDetailsDto) throws ApplicationException {
		Long roleId = viewRevisionRequestInputBeanV2.getRoleId();
		List<Long> roleIdList = new ArrayList<>();
		if (roleId != null) {
			List<UserRoleDto> userRoleDtoList = userDetailsDto.getUserRoleDtos();
			Boolean isValidRoleId = false;
			for (UserRoleDto userRoleDto : userRoleDtoList) {
				if (userRoleDto.getUserRoleId().equals(roleId)) {
					isValidRoleId = true;
					break;
				}
			}
			if (!isValidRoleId.equals(Boolean.TRUE)) {
				throw new ApplicationException(ErrorCode.E0481.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString()));
			} else {
				roleIdList.add(roleId);
			}
		} else {
			List<UserRoleDto> userRoleDtoList = userDetailsDto.getUserRoleDtos();
			for (UserRoleDto userRoleDto : userRoleDtoList) {
				roleIdList.add(userRoleDto.getUserRoleId());
			}
		}
		return roleIdList;
	}

	/**
	 * @param returnChannelMapReqDto
	 * @return
	 */
	private UserDto createUserDto(ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2) {
		UserDto userDto = new UserDto();
		userDto.setUserId(viewRevisionRequestInputBeanV2.getUserId());
		userDto.setIsActive(viewRevisionRequestInputBeanV2.getIsActive());
		userDto.setLangCode(viewRevisionRequestInputBeanV2.getLangCode());

		// Sub Category
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getSubCategoryCodeSet())) {
			List<String> subCategoryCodeList = new ArrayList<String>();
			subCategoryCodeList.addAll(viewRevisionRequestInputBeanV2.getSubCategoryCodeSet());
			userDto.setSubCategoryCodes(subCategoryCodeList);
		}

		return userDto;
	}

	/**
	 * @param revisonRequestQueryOutputBeans
	 * @return
	 */
	public List<RevisionRequest> convertQueryOutputToBean(String jobProcessId, List<RevisonRequestQueryOutputBean> revisonRequestQueryOutputBeans) {
		LOGGER.debug(jobProcessId + " Fetch pending approval Request convertQueryOutputToBean service start");
		List<RevisionRequest> revisionRequestList = null;
		if (!CollectionUtils.isEmpty(revisonRequestQueryOutputBeans)) {
			revisionRequestList = new ArrayList<>();
			for (RevisonRequestQueryOutputBean revisonRequestQueryOutputBean : revisonRequestQueryOutputBeans) {
				RevisionRequest revisionRequest = new RevisionRequest();

				// Return
				Return returns = new Return();
				returns.setReturnId(revisonRequestQueryOutputBean.getReturnId());
				returns.setReturnCode(revisonRequestQueryOutputBean.getReturnCode());
				returns.setReturnName(revisonRequestQueryOutputBean.getReturnName());
				revisionRequest.setReturns(returns);

				// Entity
				// Sub Category
				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategoryId(revisonRequestQueryOutputBean.getSubCategoryId());
				subCategory.setSubCategoryCode(revisonRequestQueryOutputBean.getSubCategoryCode());
				subCategory.setSubCategoryName(revisonRequestQueryOutputBean.getSubCategoryName());

				// Category
				Category category = new Category();
				category.setCategoryId(revisonRequestQueryOutputBean.getCategoryId());
				category.setCategoryCode(revisonRequestQueryOutputBean.getCategoryCode());
				category.setCategoryName(revisonRequestQueryOutputBean.getCategoryName());

				EntityBean entityBean = new EntityBean();
				entityBean.setEntityId(revisonRequestQueryOutputBean.getEntityId());
				entityBean.setEntityCode(revisonRequestQueryOutputBean.getEntityCode());
				entityBean.setEntityName(revisonRequestQueryOutputBean.getEntityName());
				entityBean.setCategory(category);
				entityBean.setSubCategory(subCategory);
				revisionRequest.setEntity(entityBean);

				// Frequency
				FrequencyDescription frequencyDesc = new FrequencyDescription();
				frequencyDesc.setFinYrFrquencyDesc(revisonRequestQueryOutputBean.getFinYrFrquencyDesc());
				revisionRequest.setFrequencyDesc(frequencyDesc);

				// Return Properties
				ReturnPropertyValue returnPropertyVal = new ReturnPropertyValue();
				returnPropertyVal.setReturnProValue(revisonRequestQueryOutputBean.getReturnPropValue());
				revisionRequest.setReturnPropertyVal(returnPropertyVal);

				// User
				UserMaster createdBy = new UserMaster();
				createdBy.setUserId(revisonRequestQueryOutputBean.getUserId());
				createdBy.setUserName(revisonRequestQueryOutputBean.getUserName());
				revisionRequest.setCreatedBy(createdBy);

				// Other details
				revisionRequest.setCreatedOn(revisonRequestQueryOutputBean.getCreatedOn());
				revisionRequest.setYear(revisonRequestQueryOutputBean.getYear());
				revisionRequest.setMonth(revisonRequestQueryOutputBean.getMonth());
				revisionRequest.setReportingDate(revisonRequestQueryOutputBean.getReportingDate());
				revisionRequest.setStartDate(revisonRequestQueryOutputBean.getStartDate());
				revisionRequest.setEndDate(revisonRequestQueryOutputBean.getEndDate());
				revisionRequest.setReasonForRequest(revisonRequestQueryOutputBean.getReasonForRequest());
				revisionRequest.setReasonForRejection(revisonRequestQueryOutputBean.getReasonForRejection());
				revisionRequest.setRevisionRequestId(revisonRequestQueryOutputBean.getRevisionRequestId());
				revisionRequest.setAdminStatusIdFk(revisonRequestQueryOutputBean.getAdminStatusIdFk());

				// add in list
				revisionRequestList.add(revisionRequest);
			}
		}
		LOGGER.debug(jobProcessId + " Fetch pending approval Request convertQueryOutputToBean service end");
		return revisionRequestList;
	}

}
