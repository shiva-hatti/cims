package com.iris.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iris.caching.ObjectCache;
import com.iris.controller.NotificationController;
import com.iris.controller.PrepareSendMailController;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.Action;
import com.iris.dto.DynamicContent;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ReturnApprovalDataDto;
import com.iris.dto.ReturnUploadNBusinessResult;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.WorkflowJsonBean;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.BusinessValidationFailed;
import com.iris.model.ETLAuditLog;
import com.iris.model.EntityLabelBean;
import com.iris.model.FilingStatus;
import com.iris.model.ReturnApprovalDetail;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnProperty;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.ReturnUploadNBusinessData;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.WorkFlowActivity;
import com.iris.repository.FilingStatusRepo;
import com.iris.repository.ReturnPropertyValueRepository;
import com.iris.repository.ReturnUploadDetailsRepository;
import com.iris.service.GenericService;
import com.iris.util.FileManager;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.workflow.WorkflowUtility;

@Service
public class ReturnUploadDetailsService implements GenericService<ReturnsUploadDetails, Long> {

	@Autowired
	private ReturnUploadDetailsRepository returnUploadDetailsRepository;

	@Autowired
	private FilingStatusRepo filingStatusRepo;

	@Autowired
	private NotificationController notificationController;

	@Autowired
	private ETLAuditLogService eTLAuditLogService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private ReturnApprovalDetailsService returnApprovalDetailService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private ReturnPropertyValueRepository returnPropertyValueRepo;

	private static final String APPROVED = "APPROVED";

	private static final String REJECTED = "REJECTED";

	private static final String AUTO_APPROVED = "AUTO_APPROVED";

	private static final Object lock1 = new Object();

	static final Logger LOGGER = LogManager.getLogger(ReturnUploadDetailsService.class);

	@Override
	public ReturnsUploadDetails add(ReturnsUploadDetails entity) throws ServiceException {
		return returnUploadDetailsRepository.save(entity);
	}

	@Override
	public boolean update(ReturnsUploadDetails entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnsUploadDetails> getDataByIds(Long[] ids) throws ServiceException {
		try {
			return returnUploadDetailsRepository.getReturnUploadDetailsByUploadIdInIsActiveTrue(ids);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ReturnsUploadDetails> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> entityIds = new ArrayList<>();
			List<String> returnIds = new ArrayList<>();
			List<String> finYearFreqDescIds = null;
			List<Integer> statusIds = null;
			Date startDate = null;
			Date endDate = null;
			String filedetailsId = null;
			List<String> returnIdList = null;
			List<String> entityIdList = null;
			List<String> subCatIdList = null;
			List<Long> conRetIdList = new ArrayList<>();
			List<Long> conEntIdList = new ArrayList<>();
			List<Date> endDateList = new ArrayList<>();
			List<Long> conSubCatIdList = new ArrayList<>();
			List<Integer> conReturnPropertyIdList = new ArrayList<>();
			int returnPropertyId = 0;
			String misReportType = null;
			String returnCode = null;
			String entityCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && !columnValueMap.get(columnName).isEmpty()) {
					if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
						entityIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURNID.getConstantVal())) {
						returnIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.FIN_YEAR_FREQ_DESC_ID.getConstantVal())) {
						finYearFreqDescIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.STARTDATE.getConstantVal())) {
						startDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), DateConstants.DD_MM_YYYY.getDateConstants());
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENDDATE.getConstantVal())) {
						endDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), DateConstants.DD_MM_YYYY.getDateConstants());
					} else if (columnName.equalsIgnoreCase(ColumnConstants.STARTDT.getConstantVal())) {
						startDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), "yyyy-MM-dd");
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENDDT.getConstantVal())) {
						endDate = DateManip.convertStringToDate(columnValueMap.get(columnName).get(0), "yyyy-MM-dd");
					} else if (columnName.equalsIgnoreCase(ColumnConstants.FILE_DETAILS.getConstantVal())) {
						filedetailsId = columnValueMap.get(columnName).get(0);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.STATUS.getConstantVal())) {
						List<String> list = columnValueMap.get(columnName);
						statusIds = list.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_ID_LIST.getConstantVal())) {
						returnIdList = columnValueMap.get(columnName);
						for (String str : returnIdList) {
							conRetIdList.add(Long.valueOf(str));
						}
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_ID_LIST.getConstantVal())) {
						entityIdList = columnValueMap.get(columnName);
						for (String str : entityIdList) {
							conEntIdList.add(Long.valueOf(str));
						}
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal())) {
						returnPropertyId = Integer.parseInt(columnValueMap.get(columnName).get(0));
						if (returnPropertyId == 0) {
							conReturnPropertyIdList.add(1);
							conReturnPropertyIdList.add(3);
						} else if (returnPropertyId == 1) {
							conReturnPropertyIdList.add(2);
							conReturnPropertyIdList.add(4);
						}
					} else if (columnName.equalsIgnoreCase(ColumnConstants.MIS_REPORT_TYPE.getConstantVal())) {
						misReportType = columnValueMap.get(columnName).get(0);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.END_DATE_LIST.getConstantVal())) {
						List<String> tempEndDateList = columnValueMap.get(columnName);

						for (String endDates : tempEndDateList) {
							endDateList.add(DateManip.convertStringToDate(endDates, DateConstants.DD_MM_YYYY.getDateConstants()));
						}
					} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal())) {
						subCatIdList = columnValueMap.get(columnName);
						for (String str : subCatIdList) {
							conSubCatIdList.add(Long.valueOf(str));
						}
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_CODE.getConstantVal())) {
						returnCode = columnValueMap.get(columnName).get(0);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_CODE.getConstantVal())) {
						entityCode = columnValueMap.get(columnName).get(0);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_EXISTING_UPLOAD_DATA.getConstantVal()) && returnIds != null && entityIds != null && !CollectionUtils.isEmpty(returnIds) && !CollectionUtils.isEmpty(entityIds)) {
				return returnUploadDetailsRepository.getExistingUploadInfo(Long.parseLong(returnIds.get(0)), Long.parseLong(entityIds.get(0)), startDate, endDate);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_EXISTING_UPLOAD_DATA_WITHOUT_BUSINESS_VALIDATION_FAILED.getConstantVal()) && returnIds != null && entityIds != null && !CollectionUtils.isEmpty(returnIds) && !CollectionUtils.isEmpty(entityIds)) {
				return returnUploadDetailsRepository.getExistingUploadInfoData(Long.parseLong(returnIds.get(0)), Long.parseLong(entityIds.get(0)), endDate, statusIds);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_FILE_DETAILS_ID.getConstantVal())) {
				return returnUploadDetailsRepository.getReturnUploadDetailsByFileDetailsId(Long.parseLong(filedetailsId));
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_ENTITY_WISE.getConstantVal())) {

				if (returnPropertyId == 0 && entityIds != null) {
					return returnUploadDetailsRepository.getSubmitedDataEntityWiseAudited(conRetIdList, conEntIdList, startDate, endDate, conSubCatIdList, conReturnPropertyIdList);
				} else if (returnPropertyId == 1 && entityIds != null) {
					return returnUploadDetailsRepository.getSubmitedDataEntityWiseUnAudited(conRetIdList, conEntIdList, startDate, endDate, conSubCatIdList, conReturnPropertyIdList);
				}

			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_RETURN_WISE.getConstantVal())) {
				if (returnPropertyId == 0) {
					return returnUploadDetailsRepository.getSubmitedDataReturnWiseAudited(conRetIdList, conEntIdList, startDate, endDate, conReturnPropertyIdList);
				} else if (returnPropertyId == 1) {
					return returnUploadDetailsRepository.getSubmitedDataReturnWiseUnAudited(conRetIdList, conEntIdList, startDate, endDate, conReturnPropertyIdList);
				}

			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_FILING_RETURN_ENTITY_WISE.getConstantVal()) && entityIds != null && returnIds != null) {
				return returnUploadDetailsRepository.getFilingReturnEntityWise(Long.parseLong(returnIds.get(0)), Long.parseLong(entityIds.get(0)), startDate, endDate);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_SUCCESSFULL_RETURN_FILING_BY_RETURN_AND_ENTITY.getConstantVal())) {
				return returnUploadDetailsRepository.fetchReturnUploadDetailsByReturnCodeAndEntityCodeAndStatus(returnCode, entityCode);
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<ReturnsUploadDetails> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnsUploadDetails> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnsUploadDetails> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnsUploadDetails bean) throws ServiceException {
	}

	@Override
	public ReturnsUploadDetails getDataById(Long id) throws ServiceException {
		try {
			return returnUploadDetailsRepository.getReturnUploadDetailsByUploadIdNActive(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public List<ReturnsUploadDetails> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long filedetailsId = null;
			List<Integer> fillingStatusIdList = null;
			List<String> fileTypeList = null;
			Integer changedFIllingStatus = null;
			Integer totalRecordCount = null;
			List<String> returnCodeList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null) {
					if (columnName.equalsIgnoreCase(ColumnConstants.FILE_DETAILS.getConstantVal())) {
						filedetailsId = (Long) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.FILLING_STATUS_ID.getConstantVal())) {
						fillingStatusIdList = (List<Integer>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.CHANGED_FILLING_STATUS_ID.getConstantVal())) {
						changedFIllingStatus = (Integer) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.TOTAL_RECORD_COUNT.getConstantVal())) {
						totalRecordCount = (Integer) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.FILE_TYPE.getConstantVal())) {
						fileTypeList = (List<String>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_CODE.getConstantVal())) {
						returnCodeList = (List<String>) columnValueMap.get(columnName);
					}
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_FILE_DETAILS_ID.getConstantVal())) {
				return returnUploadDetailsRepository.getReturnUploadDetailsByFileDetailsId(filedetailsId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURNS_UPLOAD_DETALS_RECORD_BY_STATUS_AND_TYPE.getConstantVal())) {
				if (CollectionUtils.isEmpty(returnCodeList)) {
					return returnUploadDetailsRepository.getReturnUploadDetailsRecordByStatus(fillingStatusIdList, fileTypeList, PageRequest.of(0, totalRecordCount));
				} else {
					return returnUploadDetailsRepository.getReturnUploadDetailsRecordByStatus(fillingStatusIdList, fileTypeList, returnCodeList, PageRequest.of(0, totalRecordCount));
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURNS_UPLOAD_DETALS_RECORD_BY_STATUS_AND_UPDATE_NEW_STATUS.getConstantVal())) {
				FilingStatus filingStatus = filingStatusRepo.getDataByFilingStatusId(changedFIllingStatus);
				if (filingStatus != null) {
					List<ReturnsUploadDetails> returnUploadDetailsList = null;
					if (CollectionUtils.isEmpty(returnCodeList)) {
						returnUploadDetailsList = returnUploadDetailsRepository.getReturnUploadDetailsRecordByStatusForUpdate(fillingStatusIdList, fileTypeList, PageRequest.of(0, totalRecordCount));
					} else {
						returnUploadDetailsList = returnUploadDetailsRepository.getReturnUploadDetailsRecordByStatusForUpdate(fillingStatusIdList, fileTypeList, returnCodeList, PageRequest.of(0, totalRecordCount));
					}

					long[] uploadId = returnUploadDetailsList.stream().mapToLong(p -> p.getUploadId()).toArray();
					if (uploadId.length > 0) {
						int updatedRecordCount = returnUploadDetailsRepository.updateReturnUploadDetailsRecordStatus(uploadId, filingStatus, new Date());
						LOGGER.info("Updated record count : " + updatedRecordCount + "Size of fetched record " + returnUploadDetailsList.size());
					}
					return returnUploadDetailsList;
				} else {
					throw new ServiceException("Status to update the record not found");
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	/**
	 * @param uploadId
	 */
	public ReturnsUploadDetails getReturnUploadDetailsByUploadIdNActive(Long uploadId) {
		return returnUploadDetailsRepository.getReturnUploadDetailsByUploadIdNActive(uploadId);
	}

	public List<ReturnsUploadDetails> getReturnUploadDetailsByReturnId(Date endDate, Long returnId, ReturnProperty returnPropertyObj) {
		if (returnPropertyObj == null) {
			return returnUploadDetailsRepository.getReturnUploadDetailsByReturnIdWithoutProp(endDate, returnId);
		} else {
			return returnUploadDetailsRepository.getReturnUploadDetailsByReturnId(endDate, returnId, returnPropertyObj.getReturnProprtyId());
		}
	}

	/**
	 * @param returnUploadDetailsMap
	 * @param workflowMap
	 * @param returnUploadDetails
	 * @param csvFormulaResult
	 * @throws Exception
	 */
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void updateValidationStatus(ReturnUploadNBusinessData returnUploadNBusinessData, ReturnUploadNBusinessResult returnUploadNBusinessResult, Map<Long, ReturnsUploadDetails> returnUploadDetailsMap) throws Exception {
		Long uploadedID = returnUploadNBusinessData.getUploadId();
		ReturnsUploadDetails returnUploadDetails = returnUploadDetailsMap.get(uploadedID);

		boolean isvalidationFailed = false;
		if (returnUploadDetails != null) {

			if (returnUploadNBusinessData.getFillingStatusId() == GeneralConstants.BUSINESS_VALIDATION_FAIL.getConstantIntVal() || returnUploadNBusinessData.getFillingStatusId() == GeneralConstants.TECHNICAL_ERROR_ID.getConstantIntVal()) {
				isvalidationFailed = true;
				prepareObjectForBusinessValidationFailed(returnUploadDetails, returnUploadNBusinessResult, returnUploadNBusinessData);
			} else if (returnUploadNBusinessData.getFillingStatusId() == GeneralConstants.PENDING_PAN_PROCESSING.getConstantIntVal()) {
				isvalidationFailed = true;
				prepareObjectForPendingPanProcessing(returnUploadDetails, returnUploadNBusinessResult, returnUploadNBusinessData);
				//				add(returnUploadDetails);
				//				return;
			} else {
				if (returnUploadNBusinessData.getEtlFolderPath() == null) {
					throw new ApplicationException("error.uploadId.etl folder path is null", "error.uploadId.etl folder path is null");
				}
				boolean isDataInserted = insertInETLAuditLog(returnUploadNBusinessData, returnUploadDetails);
				if (isDataInserted) {
					FilingStatus filingStatus = new FilingStatus();
					filingStatus.setFilingStatusId(returnUploadNBusinessData.getFillingStatusId());
					returnUploadDetails.setFilingStatus(filingStatus);
				} else {
					//********if custom json is not created
					FilingStatus filingStatus = new FilingStatus();
					filingStatus.setFilingStatusId(GeneralConstants.TECHNICAL_ERROR_ID.getConstantIntVal());
					returnUploadDetails.setFilingStatus(filingStatus);
				}
			}
			returnUploadDetails.setConsistancyCheckJson(returnUploadNBusinessData.getConsistancyCheckJson());
			returnUploadDetails.setIsConsistancyCheckFailure(returnUploadNBusinessData.getIsConsistancyCheckFailure());
			returnUploadDetails.setNoOfErrors(returnUploadNBusinessData.getNoOfErrors());
			returnUploadDetails.setNoOfWarnings(returnUploadNBusinessData.getNoOfWarnings());

			if (returnUploadNBusinessData.getNoOfErrors() == 0 && returnUploadNBusinessData.getNoOfWarnings() > 0 && !StringUtils.isEmpty(returnUploadNBusinessData.getCsvValidatorJson())) {
				if (!CollectionUtils.isEmpty(returnUploadDetails.getBusinessValidationFailedList())) {
					returnUploadDetails.getBusinessValidationFailedList().get(0).setValidationFailedJSON(returnUploadNBusinessData.getCsvValidatorJson());
					returnUploadDetails.getBusinessValidationFailedList().get(0).setCreatedOn(new Date());
					returnUploadDetails.getBusinessValidationFailedList().get(0).setReturnsUploadDetails(returnUploadDetails);
				} else {
					List<BusinessValidationFailed> businessValidationFieldList = new ArrayList<>();
					BusinessValidationFailed businessValidationFailed = new BusinessValidationFailed();
					businessValidationFailed.setReturnsUploadDetails(returnUploadDetails);
					businessValidationFailed.setValidationFailedJSON(returnUploadNBusinessData.getCsvValidatorJson());
					businessValidationFailed.setCreatedOn(new Date());
					businessValidationFieldList.add(businessValidationFailed);
					returnUploadDetails.setBusinessValidationFailedList(businessValidationFieldList);
				}
			}

			LOGGER.info("Update Filing status process Internal started for upload ID : " + uploadedID + "  With Status : " + returnUploadDetails.getFilingStatus().getFilingStatusId());
			add(returnUploadDetails);
			LOGGER.info("Update Filing status process Internal end for upload ID : " + uploadedID + "  With Status : " + returnUploadDetails.getFilingStatus().getFilingStatusId());

			if (returnUploadDetails.getAlertTypeId() == null) {
				returnUploadDetails.setAlertTypeId(91L);
			}
			returnUploadNBusinessResult.setStatus(true);
			if (!isvalidationFailed) {
				// Send Successfull validation email
				sendMail(returnUploadDetails.getAlertTypeId(), GeneralConstants.UPLOAD_FILLING_MENU_ID.getConstantLongVal(), returnUploadDetails, prepareDynamicContentBodyForValidationSuccess(returnUploadDetails));
				notificationController.sendFilingApprovalNotificationToUsers(returnUploadDetails.getUploadId(), null, returnUploadDetails.getUploadedBy().getUserId(), true);
			}
		} else {
			throw new ApplicationException("error.uploadId.notFound", "Upload ID not found");
		}
	}

	private void prepareObjectForBusinessValidationFailed(ReturnsUploadDetails returnUploadDetails, ReturnUploadNBusinessResult returnUploadNBusinessResult, ReturnUploadNBusinessData returnUploadNBusinessData) {
		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(returnUploadNBusinessData.getFillingStatusId());

		returnUploadDetails.setFilingStatus(filingStatus);

		returnUploadNBusinessResult.setUploadId(returnUploadDetails.getUploadId());
		returnUploadNBusinessResult.setFillingStatusId(GeneralConstants.BUSINESS_VALIDATION_FAIL.getConstantIntVal());

		if (!StringUtils.isEmpty(returnUploadNBusinessData.getCsvValidatorJson())) {
			if (!CollectionUtils.isEmpty(returnUploadDetails.getBusinessValidationFailedList())) {
				returnUploadDetails.getBusinessValidationFailedList().get(0).setValidationFailedJSON(returnUploadNBusinessData.getCsvValidatorJson());
				returnUploadDetails.getBusinessValidationFailedList().get(0).setCreatedOn(new Date());
				returnUploadDetails.getBusinessValidationFailedList().get(0).setReturnsUploadDetails(returnUploadDetails);
			} else {
				List<BusinessValidationFailed> businessValidationFieldList = new ArrayList<>();
				BusinessValidationFailed businessValidationFailed = new BusinessValidationFailed();
				businessValidationFailed.setReturnsUploadDetails(returnUploadDetails);
				businessValidationFailed.setValidationFailedJSON(returnUploadNBusinessData.getCsvValidatorJson());
				businessValidationFailed.setCreatedOn(new Date());
				businessValidationFieldList.add(businessValidationFailed);
				returnUploadDetails.setBusinessValidationFailedList(businessValidationFieldList);
			}
		}
	}

	private void prepareObjectForPendingPanProcessing(ReturnsUploadDetails returnUploadDetails, ReturnUploadNBusinessResult returnUploadNBusinessResult, ReturnUploadNBusinessData returnUploadNBusinessData) {
		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(returnUploadNBusinessData.getFillingStatusId());
		returnUploadDetails.setFilingStatus(filingStatus);
		returnUploadNBusinessResult.setUploadId(returnUploadDetails.getUploadId());
		returnUploadNBusinessResult.setFillingStatusId(returnUploadNBusinessData.getFillingStatusId());
	}

	private void prepareObjectForWorkflowAndBusinessValidationSuccess(ReturnsUploadDetails returnUploadDetails, String json, ReturnUploadNBusinessResult returnUploadNBusinessResult) throws Exception {
		LOGGER.info("Workflow json string: " + json);
		WorkflowJsonBean workflowJsonBean = WorkflowUtility.getWorkflowJsonBeanOfCurrentStep(json, returnUploadDetails.getCurrentWFStep());
		if (workflowJsonBean.getFilingStatusId() <= 0) {
			LOGGER.error("FILLING status can not be 0");
		}
		returnUploadDetails.setAlertTypeId(workflowJsonBean.getNotify().getEmailalerttype());
		List<ReturnApprovalDetail> returnApprovalDtlList = new ArrayList<>();
		getReturnApprovalDtlList(returnApprovalDtlList, returnUploadDetails, workflowJsonBean.getNextStep(), json);

		FilingStatus filingStatus = getFillingStatus(returnApprovalDtlList, workflowJsonBean.getFilingStatusId());
		returnUploadDetails.setFilingStatus(filingStatus);
		//		returnUploadDetails.getFileDetailsBean().setFilingStatus(filingStatus);

		if (CollectionUtils.isEmpty(returnUploadDetails.getReturnApprovalDetailsList())) {
			returnUploadDetails.setReturnApprovalDetailsList(returnApprovalDtlList);
		} else {
			returnUploadDetails.getReturnApprovalDetailsList().addAll(returnApprovalDtlList);
		}

		returnUploadNBusinessResult.setUploadId(returnUploadDetails.getUploadId());
		returnUploadNBusinessResult.setFillingStatusId(filingStatus.getFilingStatusId());
	}

	private FilingStatus getFillingStatus(List<ReturnApprovalDetail> returnApprovalDtlList, int filingStatusId) {
		for (int i = returnApprovalDtlList.size() - 1; i >= 0; i--) {
			if (returnApprovalDtlList.get(i).getFilingStatusId() != 0) {
				FilingStatus filingStatus = new FilingStatus();
				filingStatus.setFilingStatusId(returnApprovalDtlList.get(i).getFilingStatusId());
				return filingStatus;
			} else {
				continue;
			}
		}
		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(filingStatusId);
		return filingStatus;
	}

	private void getReturnApprovalDtlList(List<ReturnApprovalDetail> returnApprovalDetails, ReturnsUploadDetails returnsUploadDetails, int stepNo, String workflowJson) {
		ReturnApprovalDetail newReturnApprovalDetail = new ReturnApprovalDetail();
		newReturnApprovalDetail.setReturnUploadDetails(returnsUploadDetails);
		WorkflowJsonBean workflowJsonBean = null;
		try {
			workflowJsonBean = WorkflowUtility.getWorkflowJsonBeanOfCurrentStep(workflowJson, stepNo);
			newReturnApprovalDetail.setWorkflowStep(workflowJsonBean.getStepNo());
			WorkFlowActivity workFlowActivity = new WorkFlowActivity();
			workFlowActivity.setActivityId(new Long(workflowJsonBean.getActivities().get(0).getActivityId() + ""));

			newReturnApprovalDetail.setWorkFlowActivity(workFlowActivity);
			newReturnApprovalDetail.setCreationTime(new Date());

			if (workflowJsonBean.isAutoApproved()) {
				newReturnApprovalDetail.setComment("AUTO_APPROVED");
				newReturnApprovalDetail.setReviewStatus("APPROVED");
				Action action = workflowJsonBean.getActions().stream().filter(f -> f.getActionStatusId() == 1).findAny().orElse(null);
				if (action != null) {
					if (action.getNextStep() != 0) {
						newReturnApprovalDetail.setFilingStatusId(action.getFilingStatusId());
						newReturnApprovalDetail.setComplete(false);
						returnApprovalDetails.add(newReturnApprovalDetail);
						getReturnApprovalDtlList(returnApprovalDetails, returnsUploadDetails, action.getNextStep(), workflowJson);
					} else {
						newReturnApprovalDetail.setFilingStatusId(action.getFilingStatusId());
						newReturnApprovalDetail.setComplete(true);
						returnApprovalDetails.add(newReturnApprovalDetail);
					}
				}
			} else {
				newReturnApprovalDetail.setComplete(false);
				returnApprovalDetails.add(newReturnApprovalDetail);
			}
		} catch (Exception e) {
			LOGGER.error("Exception :", e);
		}
	}

	private boolean insertInETLAuditLog(ReturnUploadNBusinessData returnUploadNBusinessData, ReturnsUploadDetails returnUploadDetails) {
		try {
			LOGGER.info("insertInETLAuditLog started");
			if (returnUploadNBusinessData.getEtlFolderPath() != null) {
				String folderPath = returnUploadNBusinessData.getEtlFolderPath();
				List<File> files = (List<File>) FileUtils.listFiles(new File(folderPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
				File dest = new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.instance.filetopush"));
				LOGGER.info("Upload ID : " + returnUploadDetails.getUploadId());
				LOGGER.info("Destination : " + dest.getAbsolutePath());
				boolean flag;
				for (File file : files) {
					flag = FileManager.copyFileToDirectory(file, dest, true);
					LOGGER.info("File copied : " + flag);
					LOGGER.info("File Path : " + file.getAbsolutePath() + " " + file.getName());
				}
			}
			ETLAuditLog etlAuditLog = new ETLAuditLog();
			etlAuditLog.setNillable(returnUploadDetails.isNillable());
			etlAuditLog.setReturnUploadDetails(returnUploadDetails);
			etlAuditLog.setStatus(0L);
			etlAuditLog.setDocPath(returnUploadNBusinessData.getEtlFolderPath());
			eTLAuditLogService.add(etlAuditLog);
			LOGGER.info("insertInETLAuditLog completed");
		} catch (Exception e) {
			LOGGER.error("Exception in insertInETLAuditLog method", e);
			return false;
		}
		return true;
	}

	private List<DynamicContent> prepareDynamicContentBodyForValidationSuccess(ReturnsUploadDetails returnsUploadDetails) {
		try {
			List<DynamicContent> dynamicContents = new ArrayList<>();

			DynamicContent dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.name"));
			dynamicContent.setValue(returnsUploadDetails.getReturnObj().getReturnName());
			dynamicContent.setKey("#RETURN_NAME");
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.code"));
			dynamicContent.setValue(returnsUploadDetails.getReturnObj().getReturnCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.entity.entityCode"));
			dynamicContent.setValue(returnsUploadDetails.getEntity().getEntityCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.uploadfiling.entity"));
			dynamicContent.setValue(returnsUploadDetails.getEntity().getEntityName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.reporting.year"));
			dynamicContent.setValue("" + DateManip.convertDateToString(returnsUploadDetails.getStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()) + " TO " + DateManip.convertDateToString(returnsUploadDetails.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()) + "");
			dynamicContent.setKey("#END_DATE");
			dynamicContents.add(dynamicContent);

			if (returnsUploadDetails.getReturnPropertyValue() != null) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.returnProperty"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue("en", returnsUploadDetails.getReturnPropertyValue().getReturnProValue().trim()));
				dynamicContents.add(dynamicContent);
			}

			String datetFormat = DateConstants.DD_MMM_YYYY.getDateConstants();
			String calendarFormat = "en";
			String timeFormat = DateConstants.HH_MM_SS.getDateConstants() + " " + DateConstants.AM_PM.getDateConstants();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filingMngt.uplodedDate"));
			dynamicContent.setValue(DateManip.formatAppDateTime(returnsUploadDetails.getFileDetailsBean().getCreationDate(), datetFormat + " " + timeFormat, calendarFormat));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filingMngt.uploadedBy"));
			dynamicContent.setValue(returnsUploadDetails.getUploadedBy().getUserName());
			dynamicContents.add(dynamicContent);

			return dynamicContents;
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return null;
	}

	private void sendMail(Long alertId, Long menuId, ReturnsUploadDetails returnUploadDetails, List<DynamicContent> dynamicContentList) {
		try {
			LOGGER.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(alertId);
			mailServiceBean.setMenuId(menuId);
			mailServiceBean.setRoleId(returnUploadDetails.getUploadUsrRole().getUserRoleId());
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setUserId(returnUploadDetails.getUploadedBy().getUserId());
			mailServiceBean.setEntityCode(returnUploadDetails.getEntity().getEntityCode());
			mailServiceBean.setReturnCode(returnUploadDetails.getReturnObj().getReturnCode());
			mailServiceBean.setDynamicContentsList(dynamicContentList);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				LOGGER.info("Mail sent successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Exception while sending email", e);
		}
	}

	void sendMailForFilingApproveOrReject(Long alertId, Long menuId, Long roleId, Long userId, String entityCode, List<DynamicContent> dynamicContentList, String returnCode) {
		try {
			LOGGER.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(alertId);
			mailServiceBean.setMenuId(menuId);
			mailServiceBean.setRoleId(roleId);
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setUserId(userId);
			mailServiceBean.setEntityCode(entityCode);
			mailServiceBean.setReturnCode(returnCode);
			mailServiceBean.setDynamicContentsList(dynamicContentList);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				LOGGER.info("Mail sent successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Exception while sending email", e);
		}

	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public ServiceResponse appriveRejectFiling(ReturnApprovalDataDto returnApprovalDataDto) throws Exception {
		ReturnApprovalDetail returnApprovalDetail = null;
		ReturnsUploadDetails returnsUploadDetails = null;
		Date approveRejectOn = null;
		Action action = null;

		synchronized (lock1) {
			returnApprovalDetail = returnApprovalDetailService.getDataById(returnApprovalDataDto.getReturnApprovalDtlId());
			if (!StringUtils.isEmpty(returnApprovalDetail.getReviewStatus())) {
				if (returnApprovalDetail.getReviewStatus().equalsIgnoreCase(APPROVED)) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0488.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0488.toString())).build();
				} else {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0489.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0489.toString())).build();
				}
			}

			returnsUploadDetails = returnApprovalDetail.getReturnUploadDetails();

			String worklowJson = returnApprovalDetail.getReturnUploadDetails().getWorkFlowMaster().getWorkFlowJson();

			WorkflowJsonBean workflowJsonBean = WorkflowUtility.getWorkflowJsonBeanOfCurrentStep(worklowJson, returnApprovalDetail.getWorkflowStep());

			action = workflowJsonBean.getActions().stream().filter(f -> f.getActionStatusId() == returnApprovalDataDto.getIsApproved()).findAny().orElse(null);

			approveRejectOn = new Date();
			if (action != null) {
				returnApprovalDetail.setComment(returnApprovalDataDto.getComment());

				if (returnApprovalDataDto.getIsApproved() == 1) {
					returnApprovalDetail.setReviewStatus(GeneralConstants.APPROVED.getConstantVal());
				} else {
					returnApprovalDetail.setReviewStatus(GeneralConstants.REJECTED.getConstantVal());
				}

				UserMaster userMaster = new UserMaster();
				userMaster.setUserId(returnApprovalDataDto.getApprovalUserId());
				returnApprovalDetail.setApprovedRejectedBy(userMaster);

				UserRole userRole = new UserRole();
				userRole.setUserRoleId(returnApprovalDataDto.getApprovalRoleId());
				returnApprovalDetail.setUserRole(userRole);

				returnApprovalDetail.setApprovedRejectedOn(approveRejectOn);
				if (action.getNextStep() == 0) {
					//Step completed
					returnApprovalDetail.setComplete(true);
					FilingStatus filingStatus = new FilingStatus();
					filingStatus.setFilingStatusId(action.getFilingStatusId());
					returnsUploadDetails.setFilingStatus(filingStatus);
				} else {
					List<ReturnApprovalDetail> returnApprovalDtlList = new ArrayList<>();
					getReturnApprovalDtlList(returnApprovalDtlList, returnsUploadDetails, action.getNextStep(), worklowJson, approveRejectOn);

					FilingStatus filingStatus = getFillingStatus(returnApprovalDtlList, action.getFilingStatusId());
					returnsUploadDetails.setFilingStatus(filingStatus);
					if (CollectionUtils.isEmpty(returnsUploadDetails.getReturnApprovalDetailsList())) {
						returnsUploadDetails.setReturnApprovalDetailsList(returnApprovalDtlList);
					} else {
						returnsUploadDetails.getReturnApprovalDetailsList().addAll(returnApprovalDtlList);
					}
				}
				add(returnsUploadDetails);
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0833.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0833.toString())).build();
			}
		}

		List<DynamicContent> dynamicContentList = prepareDynamicContentListForApproval(returnsUploadDetails, returnApprovalDataDto.getIsApproved(), returnApprovalDataDto, approveRejectOn, returnApprovalDataDto.getComment());

		sendMailForFilingApproveOrReject(action.getNotify().getEmailalerttype(), GeneralConstants.UPLOAD_FILLING_MENU_ID.getConstantLongVal(), returnApprovalDataDto.getApprovalRoleId(), returnApprovalDataDto.getApprovalUserId(), returnsUploadDetails.getEntity().getEntityCode(), dynamicContentList, returnsUploadDetails.getReturnObj().getReturnCode());

		notificationController.sendFilingApprovalNotificationToUsers(returnsUploadDetails.getUploadId(), returnApprovalDetail.getReturnApprovalDetailId(), returnsUploadDetails.getUploadedBy().getUserId(), true);

		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).build();
	}

	private void getReturnApprovalDtlList(List<ReturnApprovalDetail> returnApprovalDetails, ReturnsUploadDetails returnsUploadDetails, int stepNo, String workflowJson, Date approveRejectOn) {
		ReturnApprovalDetail newReturnApprovalDetail = null;

		newReturnApprovalDetail = new ReturnApprovalDetail();
		newReturnApprovalDetail.setReturnUploadDetails(returnsUploadDetails);
		WorkflowJsonBean workflowJsonBean = null;
		try {
			workflowJsonBean = WorkflowUtility.getWorkflowJsonBeanOfCurrentStep(workflowJson, stepNo);
			newReturnApprovalDetail.setWorkflowStep(workflowJsonBean.getStepNo());
		} catch (Exception e) {
			LOGGER.error("Exception:", e);
		}

		if (workflowJsonBean != null) {
			WorkFlowActivity workFlowActivity = new WorkFlowActivity();
			workFlowActivity.setActivityId(new Long(workflowJsonBean.getActivities().get(0).getActivityId() + ""));

			newReturnApprovalDetail.setWorkFlowActivity(workFlowActivity);
			newReturnApprovalDetail.setCreationTime(approveRejectOn);
			newReturnApprovalDetail.setApprovedRejectedOn(approveRejectOn);

			if (workflowJsonBean.isAutoApproved()) {
				newReturnApprovalDetail.setComment(AUTO_APPROVED);
				newReturnApprovalDetail.setReviewStatus(APPROVED);
				Action action = workflowJsonBean.getActions().stream().filter(f -> f.getActionStatusId() == 1).findAny().orElse(null);
				if (action != null) {
					if (action.getNextStep() != 0) {
						getReturnApprovalDtlList(returnApprovalDetails, returnsUploadDetails, workflowJsonBean.getStepNo(), workflowJson, approveRejectOn);
					} else {
						newReturnApprovalDetail.setFilingStatusId(action.getFilingStatusId());
						newReturnApprovalDetail.setComplete(true);
						returnApprovalDetails.add(newReturnApprovalDetail);
					}
				}
			} else {
				newReturnApprovalDetail.setComplete(false);
				returnApprovalDetails.add(newReturnApprovalDetail);
			}
		}
	}

	private List<DynamicContent> prepareDynamicContentListForApproval(ReturnsUploadDetails returnsUploadDetails, Integer approvedOrRejected, ReturnApprovalDataDto returnApprovalDataDto, Date approveRejectOn, String comment) {
		String languageCode = "en";
		List<DynamicContent> dynamicContentList = new ArrayList<>();

		UserMaster userMaster = userMasterService.getDataById(returnApprovalDataDto.getApprovalUserId());

		DynamicContent dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityName"));
		EntityLabelBean entitylabel = returnsUploadDetails.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equals(languageCode)).findAny().orElse(null);
		if (entitylabel != null) {
			dynamicContent.setValue(entitylabel.getEntityNameLabel());
		} else {
			dynamicContent.setValue(returnsUploadDetails.getEntity().getEntityName());
		}
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.name"));

		ReturnLabel returnLabel = returnsUploadDetails.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(languageCode)).findAny().orElse(null);
		if (returnLabel != null) {
			dynamicContent.setValue(returnLabel.getReturnLabel());
		} else {
			dynamicContent.setValue(returnsUploadDetails.getReturnObj().getReturnName());
		}
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.startdate"));
		dynamicContent.setValue(DateManip.convertDateToString(returnsUploadDetails.getStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.enddate"));
		dynamicContent.setValue(DateManip.convertDateToString(returnsUploadDetails.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.filingMngt.approvedSlashRejected"));

		if (approvedOrRejected == 1) {
			dynamicContent.setValue(APPROVED);
			dynamicContentList.add(dynamicContent);
		} else {
			dynamicContent.setValue(REJECTED);
			dynamicContentList.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.comments"));
			dynamicContent.setValue(comment);
			dynamicContentList.add(dynamicContent);
		}

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.filingMngt.approvedRejectedBy"));
		dynamicContent.setValue(userMaster.getUserName());
		dynamicContentList.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.filingMngt.approvedRejectedOn"));
		try {
			System.out.println(DateManip.formatAppDateTime(approveRejectOn, returnApprovalDataDto.getDatetFormat() + " " + returnApprovalDataDto.getTimeFormat(), returnApprovalDataDto.getCalendarFormat()));
			dynamicContent.setValue(DateManip.formatAppDateTime(approveRejectOn, returnApprovalDataDto.getDatetFormat() + " " + returnApprovalDataDto.getTimeFormat(), returnApprovalDataDto.getCalendarFormat()));
		} catch (Exception e) {
			dynamicContent.setValue("");
			LOGGER.error("Exception : ", e);
		}
		dynamicContentList.add(dynamicContent);

		return dynamicContentList;
	}

	public ReturnPropertyValue getReturnPropertyValue(Integer returnProprtyValId) {
		return returnPropertyValueRepo.findByReturnProprtyValId(returnProprtyValId);
	}

}