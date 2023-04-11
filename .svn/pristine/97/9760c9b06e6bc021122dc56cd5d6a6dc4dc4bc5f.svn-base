package com.iris.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.RequestApprovalInputBeanV2;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.RevisionRequestDto;
import com.iris.dto.RevisonRequestQueryOutputBean;
import com.iris.dto.ViewRevisionRequestInputBeanV2;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.RevisionRequest;
import com.iris.model.UserMaster;
import com.iris.repository.ReturnPropertyValueRepository;
import com.iris.repository.RevisionRequestRepository;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class RevisionRequestService implements GenericService<RevisionRequest, Long> {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private RevisionRequestRepository revisionRequestRepo;

	@Autowired
	private ReturnPropertyValueRepository returnPropertyValueRepository;

	@Override
	public RevisionRequest add(RevisionRequest entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(RevisionRequest entity) throws ServiceException {

		try {
			revisionRequestRepo.save(entity);
			return true;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<RevisionRequest> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RevisionRequest> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> entityIds = null;
			List<String> returnIds = null;
			Date startDate = null;
			Date endDate = null;
			List<String> returnPropertyValIds = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
						entityIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURNID.getConstantVal())) {
						returnIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.STARTDATE.getConstantVal())) {
						startDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), DateConstants.DD_MM_YYYY.getDateConstants());
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENDDATE.getConstantVal())) {
						endDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), DateConstants.DD_MM_YYYY.getDateConstants());
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal())) {
						returnPropertyValIds = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_REVISION_REAQUEST.getConstantVal()) && returnIds != null && !returnIds.isEmpty()) {
				return revisionRequestRepo.getNonRejectedRevisionRequest(Long.parseLong(returnIds.get(0)), Long.parseLong(entityIds.get(0)), endDate);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_REVISION_REAQUEST_BY_RETURN_STATUS.getConstantVal()) && returnIds != null && !returnIds.isEmpty() && returnPropertyValIds != null && !returnPropertyValIds.isEmpty()) {
				return revisionRequestRepo.getNonRejectedRevisionRequest(Long.parseLong(returnIds.get(0)), Long.parseLong(entityIds.get(0)), endDate, Integer.parseInt(returnPropertyValIds.get(0)));
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<RevisionRequest> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return null;
	}

	@Override
	public List<RevisionRequest> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RevisionRequest> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(RevisionRequest bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public RevisionRequest getDataById(Long id) throws ServiceException {
		try {
			// TODO Auto-generated method stub
			return revisionRequestRepo.findAllById(Arrays.asList(id)).get(0);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<RevisionRequest> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long langId = null;
			Long regulatorId = null;
			Long entityId = null;
			List<Long> returnList = null;
			String langCode = null;
			Long userId = null;
			Date startDate = null;
			Date endDate = null;
			List<Integer> revisionIdList = null;
			List<Long> entityList = null;
			List<Long> subCatIdList = null;
			Long subCategoryId = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.LANG_ID.getConstantVal())) {
					langId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.REGULATORID.getConstantVal())) {
					regulatorId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_CODE.getConstantVal())) {
					langCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_ID_LIST.getConstantVal())) {
					returnList = (List<Long>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
					userId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.STARTDATE.getConstantVal())) {
					startDate = DateManip.convertStringToDate(columnValueMap.get(columnName).toString(), DateConstants.YYYY_MM_DD.getDateConstants());
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENDDATE.getConstantVal())) {
					endDate = DateManip.convertStringToDate(columnValueMap.get(columnName).toString(), DateConstants.YYYY_MM_DD.getDateConstants());
				} else if (columnName.equalsIgnoreCase(ColumnConstants.REVISION_STATUS_ID_LIST.getConstantVal())) {
					revisionIdList = (List<Integer>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_ID_LIST.getConstantVal())) {
					entityList = (List<Long>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_ID.getConstantVal())) {
					if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName) != "") {
						subCategoryId = Long.parseLong(columnValueMap.get(columnName) + "");
					} else {
						subCategoryId = null;
					}
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal())) {
					subCatIdList = (List<Long>) columnValueMap.get(columnName);
				}

			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_REVISION_REQUEST.getConstantVal())) {
				return revisionRequestRepo.getDataByLangIdAndRegId(langId, regulatorId, userId);
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_REVISION_REQUEST_FOR_APPROVAL.getConstantVal())) {
				if (subCatIdList == null) {
					return revisionRequestRepo.getDataByLangIdAndRegId(langId, regulatorId, userId, returnList, entityList, startDate, endDate);
				} else {
					return revisionRequestRepo.getDataByLangIdAndRegId(langId, regulatorId, userId, returnList, entityList, startDate, endDate, subCatIdList);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_REVISION_REQUEST_DATA.getConstantVal())) {
				if (subCatIdList == null) {
					if (regulatorId == null) {
						return revisionRequestRepo.getRevisionRequestDataByStartDateAndEndDate(returnList, entityList, langCode);
					} else {
						return revisionRequestRepo.getRevisionRequestDataByStartDateAndEndDate(returnList, entityList, langCode, startDate, endDate, revisionIdList, regulatorId);
					}
				} else {
					return revisionRequestRepo.getRevisionRequestDataByStartDateAndEndDateSubCatId(returnList, entityList, langCode, startDate, endDate, revisionIdList, regulatorId, subCatIdList);
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public Boolean saveRevisionRequest(RevisionRequestDto revisionRequest, EntityBean entityObj, Return returnObj, FrequencyDescription frequencyDescription, RetUploadDetBean retUploadBean, UserMaster user) {
		Date currentDate = new Date();
		RevisionRequest entity = new RevisionRequest();
		entity.setEndDate(revisionRequest.getEndDate());
		entity.setEntity(entityObj);
		entity.setReasonForRequest(revisionRequest.getReasonOfNotProcessed());
		entity.setReportingDate(currentDate);
		if (revisionRequest.getReturnPropertyValId() != null) {
			ReturnPropertyValue returnPropertyVal = returnPropertyValueRepository.getPropValInfo(revisionRequest.getReturnPropertyValId());

			entity.setReturnPropertyVal(returnPropertyVal);
		}
		entity.setRevisionStatus("OPEN");
		entity.setFrequencyDesc(frequencyDescription);
		entity.setReturns(returnObj);
		entity.setStartDate(revisionRequest.getReportingPeriodStartDate());
		entity.setEndDate(revisionRequest.getReportingPeriodEndDate());
		entity.setYear(Long.valueOf(retUploadBean.getFinancialYear()));
		entity.setMonth(Integer.valueOf(retUploadBean.getFinancialMonth()));
		entity.setActionIdFk(1);
		entity.setAdminStatusIdFk(1);
		entity.setCreatedBy(user);
		entity.setCreatedOn(currentDate);
		update(entity);
		revisionRequest.setRevisionReqId(entity.getRevisionRequestId());
		return true;

	}

	public List<RevisonRequestQueryOutputBean> fetchPendingRequest(RequestApprovalInputBeanV2 requestApprovalInputBeanV2, Date startDate, Date endDate) {
		String returnQuerySql = "SELECT DISTINCT new com.iris.dto.RevisonRequestQueryOutputBean(revisionRequest.entity.entityId, " + " revisionRequest.entity.entityCode, revisionRequest.entity.entityName, " + " revisionRequest.entity.category.categoryId, revisionRequest.entity.category.categoryCode, " + " revisionRequest.entity.category.categoryName, revisionRequest.entity.subCategory.subCategoryId, " + " revisionRequest.entity.subCategory.subCategoryCode, revisionRequest.entity.subCategory.subCategoryName, " + " revisionRequest.returns.returnId, revisionRequest.returns.returnCode, " + " revisionRequest.returns.returnName, revisionRequest.createdBy.userId, " + " revisionRequest.createdBy.userName, revisionRequest.createdOn, " + " revisionRequest.frequencyDesc.frequency.frequencyId, revisionRequest.frequencyDesc.frequency.frequencyName, " + " revisionRequest.frequencyDesc.frequency.description, revisionRequest.year, " + " revisionRequest.month, revisionRequest.reportingDate, revisionRequest.startDate, " + " revisionRequest.endDate, revisionRequest.reasonForRequest, revisionRequest.reasonForRejection, " + " revisionRequest.revisionRequestId,revisionRequest.frequencyDesc.finYrFrquencyDesc, revisionRequest.adminStatusIdFk) " + " FROM RevisionRequest revisionRequest, DeptUserEntityMapping deptUserEntMap, " + " ReturnRegulatorMapping retRegMap, " + " Return returns, ReturnLabel returnLabel, " + " EntityBean entityBean, " + " EntityLabelBean entityLabelBean, LanguageMaster langMaster," + " UserRoleReturnMapping userRoleReturnMapping, UserRoleMaster userRoleMaster ";

		returnQuerySql = returnQuerySql + " WHERE langMaster.languageCode =:langCode ";
		// Entity Code
		if (!StringUtils.isBlank(requestApprovalInputBeanV2.getEntityCode())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.entity.entityCode =:entityCode ";
		}
		returnQuerySql = returnQuerySql + " and revisionRequest.adminStatusIdFk = 1  " + "and  revisionRequest.endDate between date(:startDate) and (:endDate) ";

		returnQuerySql = returnQuerySql + " and revisionRequest.createdBy.userId != :userId ";
		// Return Code List
		if (!CollectionUtils.isEmpty(requestApprovalInputBeanV2.getReturnCodeList())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.returns.returnCode IN(:returnCodeList) ";
		}

		returnQuerySql = returnQuerySql + " and revisionRequest.returns.returnId = returns.returnId " + " and returns.isActive =:isActive " + " and returns.returnId = returnLabel.returnIdFk.returnId " + " and returnLabel.langIdFk.languageId = langMaster.languageId " + " and retRegMap.returnIdFk.returnId = returns.returnId " + " and retRegMap.isActive =:isActive " + " and deptUserEntMap.userIdFk.userId=:userId " + " and deptUserEntMap.isActive=:isActive " + " and deptUserEntMap.entity.entityId = entityBean.entityId " + " and entityBean.isActive =:isActive ";
		if (!CollectionUtils.isEmpty(requestApprovalInputBeanV2.getSubCategoryCodeList())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.entity.subCategory.subCategoryCode IN(:subCategoryCodeList) ";
		}

		returnQuerySql = returnQuerySql + " and revisionRequest.entity.entityId = entityBean.entityId " + " and entityBean.entityId = entityLabelBean.entityBean.entityId " + " and entityLabelBean.languageMaster.languageId = langMaster.languageId ";

		returnQuerySql = returnQuerySql + " and userRoleMaster.userMaster.userId = deptUserEntMap.userIdFk.userId " + " and userRoleMaster.userRole.userRoleId = userRoleReturnMapping.roleIdFk.userRoleId " + " and userRoleReturnMapping.returnIdFk.returnId = retRegMap.returnIdFk.returnId ";

		if (requestApprovalInputBeanV2.getRoleId() != null) {
			returnQuerySql = returnQuerySql + " and userRoleReturnMapping.roleIdFk.userRoleId =:roleId ";
		}

		returnQuerySql = returnQuerySql + " ORDER BY revisionRequest.revisionRequestId ASC ";
		Query query = entityManager.createQuery(returnQuerySql, RevisonRequestQueryOutputBean.class);
		query.setParameter("userId", requestApprovalInputBeanV2.getUserId());
		query.setParameter("isActive", requestApprovalInputBeanV2.getIsActive());
		query.setParameter("langCode", requestApprovalInputBeanV2.getLangCode());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		// entity Code
		if (!StringUtils.isBlank(requestApprovalInputBeanV2.getEntityCode())) {
			query.setParameter("entityCode", requestApprovalInputBeanV2.getEntityCode());
		}
		// Return code list
		if (!CollectionUtils.isEmpty(requestApprovalInputBeanV2.getReturnCodeList())) {
			query.setParameter("returnCodeList", requestApprovalInputBeanV2.getReturnCodeList());
		}
		// Sub category list
		if (!CollectionUtils.isEmpty(requestApprovalInputBeanV2.getSubCategoryCodeList())) {
			query.setParameter("subCategoryCodeList", requestApprovalInputBeanV2.getSubCategoryCodeList());
		}

		// RoleId
		if (requestApprovalInputBeanV2.getRoleId() != null) {
			query.setParameter("roleId", requestApprovalInputBeanV2.getRoleId());
		}
		return query.getResultList();
	}

	public List<RevisonRequestQueryOutputBean> fetchViewRevisionRequest(ViewRevisionRequestInputBeanV2 viewRevisionRequestInputBeanV2, Date startDate, Date endDate, List<String> entityCodeList, List<Long> roleIdList, Boolean isEntityUser) {
		String returnQuerySql = "SELECT DISTINCT new com.iris.dto.RevisonRequestQueryOutputBean(revisionRequest.entity.entityId, " + " revisionRequest.entity.entityCode, revisionRequest.entity.entityName, " + " revisionRequest.entity.category.categoryId, revisionRequest.entity.category.categoryCode, " + " revisionRequest.entity.category.categoryName, revisionRequest.entity.subCategory.subCategoryId, " + " revisionRequest.entity.subCategory.subCategoryCode, revisionRequest.entity.subCategory.subCategoryName, " + " revisionRequest.returns.returnId, revisionRequest.returns.returnCode, " + " revisionRequest.returns.returnName, revisionRequest.createdBy.userId, " + " revisionRequest.createdBy.userName, revisionRequest.createdOn, " + " revisionRequest.frequencyDesc.frequency.frequencyId, revisionRequest.frequencyDesc.frequency.frequencyName, " + " revisionRequest.frequencyDesc.frequency.description, revisionRequest.year, " + " revisionRequest.month, revisionRequest.reportingDate, revisionRequest.startDate, " + " revisionRequest.endDate, revisionRequest.reasonForRequest, revisionRequest.reasonForRejection, " + " revisionRequest.revisionRequestId,revisionRequest.frequencyDesc.finYrFrquencyDesc, revisionRequest.adminStatusIdFk) " + " FROM RevisionRequest revisionRequest, " + " Return returns, ReturnLabel returnLabel, " + " EntityBean entityBean, " + " EntityLabelBean entityLabelBean, " + " LanguageMaster langMaster," + " UserRoleReturnMapping userRoleReturnMapping, UserRoleMaster userRoleMaster ";

		returnQuerySql = returnQuerySql + " WHERE langMaster.languageCode =:langCode ";
		// Entity Code
		if (!CollectionUtils.isEmpty(entityCodeList)) {
			returnQuerySql = returnQuerySql + " and revisionRequest.entity.entityCode IN(:entityCodeList) ";
		}
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getStatusIdSet())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.adminStatusIdFk IN(:adminStatusIdSet)  ";
		}

		returnQuerySql = returnQuerySql + "and  revisionRequest.endDate between date(:startDate) and (:endDate) ";

		//returnQuerySql = returnQuerySql + " and revisionRequest.createdBy.userId != :userId ";
		// Return Code List
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getReturnCodeSet())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.returns.returnCode IN(:returnCodeList) ";
		}

		returnQuerySql = returnQuerySql + " and revisionRequest.returns.returnId = returns.returnId " + " and returns.isActive =:isActive " + " and returns.returnId = returnLabel.returnIdFk.returnId " + " and returnLabel.langIdFk.languageId = langMaster.languageId " + " and entityBean.isActive =:isActive ";
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getSubCategoryCodeSet())) {
			returnQuerySql = returnQuerySql + " and revisionRequest.entity.subCategory.subCategoryCode IN(:subCategoryCodeList) ";
		}

		returnQuerySql = returnQuerySql + " and revisionRequest.entity.entityId = entityBean.entityId " + " and entityBean.entityId = entityLabelBean.entityBean.entityId " + " and entityLabelBean.languageMaster.languageId = langMaster.languageId ";
		returnQuerySql = returnQuerySql + " and userRoleMaster.userMaster.userId =:userId ";
		if (isEntityUser) {
			returnQuerySql = returnQuerySql + " and userRoleMaster.userRole.userRoleId = userRoleReturnMapping.roleIdFk.userRoleId " + "and userRoleReturnMapping.returnIdFk.returnId = revisionRequest.returns.returnId " + " and userRoleReturnMapping.isActive=:isActive ";
		}

		if (viewRevisionRequestInputBeanV2.getRoleId() != null) {
			returnQuerySql = returnQuerySql + " and userRoleReturnMapping.roleIdFk.userRoleId =:roleId ";
		}

		returnQuerySql = returnQuerySql + " ORDER BY revisionRequest.revisionRequestId ASC ";
		Query query = entityManager.createQuery(returnQuerySql, RevisonRequestQueryOutputBean.class);

		query.setParameter("userId", viewRevisionRequestInputBeanV2.getUserId());

		query.setParameter("isActive", viewRevisionRequestInputBeanV2.getIsActive());
		query.setParameter("langCode", viewRevisionRequestInputBeanV2.getLangCode());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getStatusIdSet())) {
			query.setParameter("adminStatusIdSet", viewRevisionRequestInputBeanV2.getStatusIdSet());
		}

		// entity Code
		if (!CollectionUtils.isEmpty(entityCodeList)) {
			query.setParameter("entityCodeList", entityCodeList);
		}
		// Return code list
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getReturnCodeSet())) {
			query.setParameter("returnCodeList", viewRevisionRequestInputBeanV2.getReturnCodeSet());
		}
		// Sub category list
		if (!CollectionUtils.isEmpty(viewRevisionRequestInputBeanV2.getSubCategoryCodeSet())) {
			query.setParameter("subCategoryCodeList", viewRevisionRequestInputBeanV2.getSubCategoryCodeSet());
		}

		// RoleId
		if (viewRevisionRequestInputBeanV2.getRoleId() != null) {
			query.setParameter("roleId", viewRevisionRequestInputBeanV2.getRoleId());
		}
		return query.getResultList();
	}

}
