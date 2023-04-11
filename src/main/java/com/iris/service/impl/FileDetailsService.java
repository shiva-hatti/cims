package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.FileDetailRequestDto;
import com.iris.dto.FilingHistoryDto;
import com.iris.exception.ServiceException;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.repository.FileDetailsRepo;
import com.iris.sdmx.status.entity.SdmxFileActivityLog;
import com.iris.sdmx.status.service.SdmxFileActivityLogService;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class FileDetailsService implements GenericService<FileDetails, Long> {

	static final Logger LOGGER = LogManager.getLogger(FileDetailsService.class);

	@Autowired
	private FileDetailsRepo fileDetailsRepo;

	@Autowired
	private EntityManager em;

	@Autowired
	private SdmxFileActivityLogService sdmxFileActivityLogService;

	@Override
	public FileDetails add(FileDetails entity) throws ServiceException {
		try {
			return fileDetailsRepo.save(entity);
		} catch (Exception e) {
			throw new ServiceException("Exception : ", e);
		}
	}

	@Override
	public boolean update(FileDetails entity) throws ServiceException {
		try {
			fileDetailsRepo.save(entity);
			return true;
		} catch (Exception e) {
			throw new ServiceException("Exception : ", e);
		}
	}

	@Override
	public List<FileDetails> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public List<FileDetails> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> statusList = null;
			List<String> uploadChannelList = null;
			if (columnValueMap != null) {
				for (String columnName : columnValueMap.keySet()) {
					if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
						if (columnName.equalsIgnoreCase(ColumnConstants.STATUS.getConstantVal())) {
							statusList = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.UPLOAD_CHANNEL.getConstantVal())) {
							uploadChannelList = columnValueMap.get(columnName);
						}
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_ACTIVE_FILE_DETAILS_DATA_BY_STATUS.getConstantVal())) {
				return fileDetailsRepo.getAciveFileDetailsDataByStatus(statusList, uploadChannelList);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_UNPROCESSED_DATA_AND_UPDATE_PROCESSING_FLAG.getConstantVal())) {
				List<FileDetails> list = fileDetailsRepo.getUnProcessedData();
				if (!CollectionUtils.isEmpty(list)) {
					fileDetailsRepo.updateIsProcessingFlag(list.stream().map(f -> f.getId()).collect(Collectors.toList()));
				}
				return list;
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<FileDetails> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<FileDetails> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<FileDetails> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(FileDetails bean) throws ServiceException {

	}

	@Override
	public FileDetails getDataById(Long id) throws ServiceException {
		try {
			return fileDetailsRepo.getDataById(id);
		} catch (Exception e) {
			throw new ServiceException("Exception : ", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileDetails> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			//			String methodName = null;
			//			if (!CollectionUtils.isEmpty(fileDetailRequestDto.getIfscCode())
			//					&& !CollectionUtils.isEmpty(fileDetailRequestDto.getReturnCode())) {
			//				methodName = "getFileDetailsDataByEntityCodeAndReturnCode";
			//			} else if (!CollectionUtils.isEmpty(fileDetailRequestDto.getIfscCode())) {
			//				methodName = "getFileDetailsDataByEntityCode";
			//			} else {
			//				methodName = "getFileDetailsData";
			//			}

			Object status = null;
			Object uploadChannelList = null;
			Object returnCode = null;
			Object ifscCode = null;
			Object unMappedReturns = null;
			if (columnValueMap != null) {
				for (String columnName : columnValueMap.keySet()) {
					if (columnValueMap.get(columnName) != null) {
						if (columnName.equalsIgnoreCase(ColumnConstants.STATUS.getConstantVal())) {
							status = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.UPLOAD_CHANNEL.getConstantVal())) {
							uploadChannelList = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_CODE.getConstantVal())) {
							returnCode = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.IFSC_CODE.getConstantVal())) {
							ifscCode = columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.UNMAPPED_RETURN.getConstantVal())) {
							unMappedReturns = columnValueMap.get(columnName);
						}
					}
				}
			}
			if (methodName.equalsIgnoreCase("getFileDetailsData")) {
				List<Long> uploadChannelsList = (List<Long>) uploadChannelList;
				List<Integer> statusList = (List<Integer>) status;
				String startsDate = columnValueMap.get(ColumnConstants.STARTDATE.getConstantVal()).toString();
				String endsDate = columnValueMap.get(ColumnConstants.ENDDATE.getConstantVal()).toString();
				return fileDetailsRepo.getFileDetailsData(uploadChannelsList, statusList, startsDate, endsDate);
			} else if (methodName.equalsIgnoreCase("getFileDetailsDataByEntityCode")) {
				List<Long> uploadChannelsList = (List<Long>) uploadChannelList;
				List<Integer> statusList = (List<Integer>) status;
				List<String> ifscCodeList = (List<String>) ifscCode;
				if (ifscCodeList != null) {
					ifscCodeList.replaceAll(String::toUpperCase);
				}
				String startsDate = columnValueMap.get(ColumnConstants.STARTDATE.getConstantVal()).toString();
				String endsDate = columnValueMap.get(ColumnConstants.ENDDATE.getConstantVal()).toString();
				if ((boolean) unMappedReturns) {
					return fileDetailsRepo.getFileDetailsDataWithEntityAndUnMappedReturn(uploadChannelsList, statusList, ifscCodeList, startsDate, endsDate);
				} else {
					return fileDetailsRepo.getFileDetailsDataByEntityCode(uploadChannelsList, statusList, ifscCodeList, startsDate, endsDate);
				}
			} else if (methodName.equalsIgnoreCase("getFileDetailsDataByEntityCodeAndReturnCode")) {
				List<Long> uploadChannelsList = (List<Long>) uploadChannelList;
				List<Integer> statusList = (List<Integer>) status;
				List<String> returnCodeList = (List<String>) returnCode;
				if (returnCodeList != null) {
					returnCodeList.replaceAll(String::toUpperCase);
				}
				List<String> ifscCodeList = (List<String>) ifscCode;
				if (ifscCodeList != null) {
					ifscCodeList.replaceAll(String::toUpperCase);
				}
				String startsDate = columnValueMap.get(ColumnConstants.STARTDATE.getConstantVal()).toString();
				String endsDate = columnValueMap.get(ColumnConstants.ENDDATE.getConstantVal()).toString();
				if ((boolean) unMappedReturns) {
					return fileDetailsRepo.getFileDetailsDataWithUnMappedReturns(uploadChannelsList, statusList, returnCodeList, ifscCodeList, startsDate, endsDate);
				} else {
					return fileDetailsRepo.getFileDetailsDataByEntityCodeAndReturnCode(uploadChannelsList, statusList, returnCodeList, ifscCodeList, startsDate, endsDate);
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * @param ebrFileAuditIds
	 * @param statusId
	 * @param jobProcessingId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateStatusOfSdmxFileAuditRecords(List<Long> ebrFileAuditIds, Integer statusId, String jobProcessingId) {
		LOGGER.debug("Start updateStatusOfSdmxFileAuditRecords with job processing id - " + jobProcessingId);
		FilingStatus status = new FilingStatus(statusId);
		fileDetailsRepo.updateStatusOfSdmxFileAuditRecords(ebrFileAuditIds, status);
		LOGGER.debug("End updateStatusOfSdmxFileAuditRecords with job processing id - " + jobProcessingId);
	}

	public List<FilingHistoryDto> getFilingHistoryDataForRBR(FileDetailRequestDto fileDetailRequestDto) {
		StringBuilder query = null;
		query = getBasicQueryForReturnFilingHistory(fileDetailRequestDto);

		boolean isReturnCodeAddedInQuery = false;

		if (!StringUtils.isEmpty(fileDetailRequestDto.getReturnCode())) {
			isReturnCodeAddedInQuery = true;
			List<String> returnCodeList = fileDetailRequestDto.getReturnCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			query.append("and (UPPER(FD.returnCode) in ('" + String.join("','", returnCodeList) + "')");
		}

		if (fileDetailRequestDto.getUnMappedReturn().equals(Boolean.TRUE)) {
			query.append("or FD.returnCode not in (select returnCode from Return)");
			query.append("or FD.returnCode is NULL");
		}

		if (isReturnCodeAddedInQuery) {
			query.append(")");
		}

		query.append(" order by FD.creationDate desc");

		Query queryResult = em.createQuery(query.toString());
		return (List<FilingHistoryDto>) queryResult.getResultList();
	}

	private StringBuilder getBasicQueryForReturnFilingHistory(FileDetailRequestDto fileDetailRequestDto) {

		Set<Integer> fileDetailsFilingStatusId = null;

		Set<Integer> returnsUploadFilingStatusId = null;

		for (Integer integer : fileDetailRequestDto.getStatus()) {
			if (integer == 17 || integer == 18 || integer == 19) {
				if (fileDetailsFilingStatusId == null) {
					fileDetailsFilingStatusId = new HashSet<>();
				}
				fileDetailsFilingStatusId.add(integer);
			} else {
				if (returnsUploadFilingStatusId == null) {
					returnsUploadFilingStatusId = new HashSet<>();
				}
				returnsUploadFilingStatusId.add(integer);
			}
		}

		StringBuilder stringBuilder = new StringBuilder("select new com.iris.dto.FilingHistoryDto(FD.id, FSTATUS.status, RFSTATUS.status, UPCHANNEL.uploadChannelDesc, FD.returnCode, " + "" + " RET.returnCode, RET.returnId, RET.returnName, FD.ifscCode, ENT.ifscCode, ENT.entityId, FD.entityCode, ENT.entityCode, " + " ENT.entityName, " + "RD.startDate, RD.endDate, FD.frequencyIdFk.frequencyId, " + " FREQ.frequencyId, FREQ.frequencyCode, FREQ.frequencyName, FD.fileType," + " FD.fileName, RD.instanceFile, " + " FD.size, FD.reasonOfNotProcessed, RD.prevUploadId, RD.revisionRequestId.revisionRequestId," + " RD.unlockingReqId.unlockingReqId, " + " temp.returnTemplateId, temp.versionNumber," + " RD.returnPropertyValue.returnProprtyValId," + " RET_PROP_VAL.returnProValue,  RD.noOfErrors, RD.noOfWarnings, FD.fileMimeType, " + " FD.creationDate, RD.uploadId, " + " FD.reportingPeriodStartDate," + " FD.reportingPeriodEndDate, " + " FSTATUS.filingStatusId, RFSTATUS.filingStatusId, FD.supportiveDocName," + " FD.supportiveDocType, RD.attachedFile, USR.userName, " + " FD.emailId,businessValidationSD.processEndTime,metaDataSD.processEndTime,ETL.docPushEndTime,ETL.etlEndTime,ETL.targetJobEndTime, RD.nillable) " + "" + "  FROM FileDetails FD " + " left join ReturnsUploadDetails RD  on FD.id = RD.fileDetailsBean.id  " + " left join ReturnTemplate temp  " + " on RD.taxonomyId.returnTemplateId = temp.returnTemplateId " + " left join Frequency FREQ on RD.frequency.frequencyId = FREQ.frequencyId " + " " + "left join FilingStatus FSTATUS on FSTATUS.filingStatusId = FD.filingStatus.filingStatusId " + "" + " left join FilingStatus RFSTATUS on RFSTATUS.filingStatusId = RD.filingStatus.filingStatusId " + " " + " left join UploadChannel UPCHANNEL on UPCHANNEL.uploadChannelId = FD.uploadChannelIdFk.uploadChannelId " + " " + " left join Return RET on RET.returnCode = FD.returnCode " + "" + "  left join ReturnPropertyValue RET_PROP_VAL on RET_PROP_VAL.returnProprtyValId = RD.returnPropertyValue.returnProprtyValId" + " " + " left join EntityBean ENT on UPPER(ENT.ifscCode) = UPPER(FD.ifscCode)" + " " + " left join UserMaster USR on USR.userId = FD.userMaster.userId" + " " + " left join SdmxFileActivityLog metaDataSD on metaDataSD.fileDetailsIdFk.id =FD.id " + " and metaDataSD.sdmxProcessDetailIdFk.processId=4" + " left join SdmxFileActivityLog " + " businessValidationSD on businessValidationSD.fileDetailsIdFk.id =FD.id and businessValidationSD.sdmxProcessDetailIdFk.processId=6" + " left join ETLAuditLog ETL on ETL.returnUploadDetails.uploadId =RD.uploadId" + " where UPCHANNEL.uploadChannelId IN " + "(" + org.apache.commons.lang3.StringUtils.join(fileDetailRequestDto.getUploadChannelList().toArray(), ",") + ")" + " and date(FD.creationDate) between date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getStartDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getEndDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and FD.isEbrFiling = 0 " + " and (");

		if (fileDetailsFilingStatusId != null && returnsUploadFilingStatusId != null) {
			if (fileDetailsFilingStatusId != null) {
				stringBuilder.append("FD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(fileDetailsFilingStatusId.toArray(), ",") + ")");
			}

			if (returnsUploadFilingStatusId != null) {
				stringBuilder.append("OR RD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(returnsUploadFilingStatusId.toArray(), ",") + ")");
			}
		} else {
			if (fileDetailsFilingStatusId != null) {
				stringBuilder.append("FD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(fileDetailsFilingStatusId.toArray(), ",") + ")");
			}

			if (returnsUploadFilingStatusId != null) {
				stringBuilder.append("RD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(returnsUploadFilingStatusId.toArray(), ",") + ")");
			}
		}

		stringBuilder.append(")");

		if (!StringUtils.isEmpty(fileDetailRequestDto.getIfscCode())) {
			List<String> ifscCodeList = fileDetailRequestDto.getIfscCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append("and UPPER(FD.ifscCode) in ('" + String.join("','", ifscCodeList) + "')");
		}

		return stringBuilder;
	}

	public List<FilingHistoryDto> getFilingHistoryDataForEbr(FileDetailRequestDto fileDetailRequestDto) {

		Set<Integer> returnsUploadFilingStatusId = new HashSet<>();
		;

		for (Integer integer : fileDetailRequestDto.getStatus()) {
			returnsUploadFilingStatusId.add(integer);
		}

		StringBuilder stringBuilder = new StringBuilder("select new com.iris.dto.FilingHistoryDto(RFSTATUS.status," + " RET.returnCode, RET.returnId, RET.returnName, ENT.ifscCode, ENT.entityId, ENT.entityCode, " + " ENT.entityName, RD.startDate, RD.endDate,  " + " FREQ.frequencyId, FREQ.frequencyCode, FREQ.frequencyName, RD.instanceFile, " + " RD.prevUploadId, RD.revisionRequestId.revisionRequestId, RD.unlockingReqId.unlockingReqId, " + " RD.returnPropertyValue.returnProprtyValId, RET_PROP_VAL.returnProValue,  RD.noOfErrors, RD.noOfWarnings,  " + " RD.uploadedDate, RD.uploadId, RFSTATUS.filingStatusId, RD.attachedFile, USR.userName, true) " + " FROM ReturnsUploadDetails RD " + " left join Frequency FREQ on RD.frequency.frequencyId = FREQ.frequencyId " + " left join FilingStatus RFSTATUS on RFSTATUS.filingStatusId = RD.filingStatus.filingStatusId " + " left join Return RET on RET.returnId = RD.returnObj.returnId " + " left join ReturnPropertyValue RET_PROP_VAL on RET_PROP_VAL.returnProprtyValId = RD.returnPropertyValue.returnProprtyValId" + " left join EntityBean ENT on ENT.entityId = RD.entity.entityId" + " left join UserMaster USR on USR.userId = RD.uploadedBy.userId" + " where date(RD.uploadedDate) between date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getStartDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getEndDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + "and RD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(returnsUploadFilingStatusId.toArray(), ",") + ")" + " and RD.sdmxEbrToRbrPreparation.ebrToRbrPreparationId is not NULL ");

		if (!StringUtils.isEmpty(fileDetailRequestDto.getIfscCode())) {
			List<String> ifscCodeList = fileDetailRequestDto.getIfscCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append(" and UPPER(RD.entity.ifscCode) in ('" + String.join("','", ifscCodeList) + "')");
		}

		if (!StringUtils.isEmpty(fileDetailRequestDto.getReturnCode())) {
			List<String> returnCodeList = fileDetailRequestDto.getReturnCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append(" and UPPER(RD.returnObj.returnCode) in ('" + String.join("','", returnCodeList) + "')");
		}

		stringBuilder.append(" order by RD.uploadedDate desc");

		Query queryResult = em.createQuery(stringBuilder.toString());
		return (List<FilingHistoryDto>) queryResult.getResultList();
	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public List<FileDetails> getEbrFilingDataForMetaDataValidation(Long recordToBeProcessed) throws ServiceException {
		try {

			if (recordToBeProcessed == null || recordToBeProcessed == 0) {
				recordToBeProcessed = 5L;
			}

			StringBuilder query = new StringBuilder("select * FROM TBL_FILE_DETAILS fd where fd.FILING_STATUS_ID_FK in('17') and fd.IS_ACTIVE = '1' and fd.PROCESSING_FLAG = '0' and IS_EBR_FILING = '1' order by ID asc LIMIT " + recordToBeProcessed + " for update");

			Query queryResult = em.createNativeQuery(query.toString(), Tuple.class);
			@SuppressWarnings("unchecked")
			List<Tuple> list = queryResult.getResultList();

			String updateQuery;

			List<FileDetails> fileDetailsList = new ArrayList<>();
			Query updateQueryResult;
			for (Tuple item : list) {
				updateQuery = "update TBL_FILE_DETAILS set PROCESSING_FLAG = 1 where ID = " + item.get("ID") + "";
				updateQueryResult = em.createNativeQuery(updateQuery);
				updateQueryResult.executeUpdate();

				FileDetails fileDetails = new FileDetails();
				fileDetails.setId(Long.parseLong(item.get("ID").toString()));

				com.iris.model.UploadChannel uploadChannel = new com.iris.model.UploadChannel();
				uploadChannel.setUploadChannelId(Long.parseLong(item.get("UPLOAD_CHANNEL_ID_FK").toString()));
				fileDetails.setUploadChannelIdFk(uploadChannel);

				if (item.get("ENTITY_CODE") != null) {
					fileDetails.setEntityCode((String) item.get("ENTITY_CODE"));
				}

				if (item.get("USER_ID_FK") != null) {
					UserMaster userMaster = new UserMaster();
					userMaster.setUserId(Long.parseLong(item.get("USER_ID_FK").toString()));
					fileDetails.setUserMaster(userMaster);
				}

				if (item.get("ROLE_ID_FK") != null) {
					UserRole userRole = new UserRole();
					userRole.setUserRoleId(Long.parseLong(item.get("ROLE_ID_FK").toString()));
					fileDetails.setUserRole(userRole);
				}

				fileDetails.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
				fileDetails.setFileName((String) item.get("FILE_NAME"));
				fileDetails.setApplicationProcessId((String) item.get("APP_PROCESS_ID"));
				fileDetailsList.add(fileDetails);
			}
			return fileDetailsList;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public int updateFileDetailsWithMetaDataFailed(String reasonOfFailure, Long fileDetailsId, Integer filingStatus) {
		String updateQuery = "update TBL_FILE_DETAILS set REASON_OF_NOT_PROCESSED = ?, FILING_STATUS_ID_FK = ?  where ID = ?";

		Query updateQueryResult = em.createNativeQuery(updateQuery);
		updateQueryResult.setParameter(1, reasonOfFailure);
		updateQueryResult.setParameter(2, filingStatus);
		updateQueryResult.setParameter(3, fileDetailsId);
		return updateQueryResult.executeUpdate();
	}

	public FileDetails getDataByVtlRequestId(String vtlRequestId, Integer filingStatusId) {
		return fileDetailsRepo.fetchFileDetailsRecordBasedOnVTLRequestId(vtlRequestId, filingStatusId);
	}

	public List<FileDetails> fetchFileDetailsRecordForVTLHtmlCreation(Integer filingStatusId) {
		return fileDetailsRepo.fetchFileDetailsRecordForVTLHtmlCreation(filingStatusId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateFileDetailsStatusAndFileActivityLog(long[] fileDetailsId, Integer filingStatusId, boolean isValidationFailed, Integer vtlStatus) {
		FilingStatus filingStatus = new FilingStatus();
		filingStatus.setFilingStatusId(filingStatusId);

		updateVtlRecordStatus(fileDetailsId, filingStatus);

		SdmxFileActivityLog sdmxFileActivityLog = sdmxFileActivityLogService.getDataByFileDetailsIdAndProcessCode(Long.valueOf(fileDetailsId[0]), GeneralConstants.EBR_BUSINESS_VALIDATION_PROCESS_CODE.getConstantVal());
		sdmxFileActivityLog.setProcessEndTime(new Date());
		sdmxFileActivityLog.setIsSuccess(isValidationFailed);

		sdmxFileActivityLogService.update(sdmxFileActivityLog);

		if (!Objects.isNull(vtlStatus)) {
			updateVtlCallBackStatus(fileDetailsId[0], vtlStatus);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateVtlRecordStatus(long[] fileDetailsId, FilingStatus filingStatus) {
		fileDetailsRepo.updateRecordStatus(fileDetailsId, filingStatus);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateVtlCallBackStatus(Long fileDetailsId, Integer vtlStatus) {
		fileDetailsRepo.updateVtlCallBackStatus(fileDetailsId, vtlStatus);
	}
}
