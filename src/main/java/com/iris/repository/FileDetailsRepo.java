package com.iris.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.sdmx.ebrvalidation.bean.EbrFileDetails;

/**
 * @author apagaria
 *
 */
public interface FileDetailsRepo extends JpaRepository<FileDetails, Long> {

	@Query(value = "FROM FileDetails fd where fd.isActive = '1' and fd.filingStatus.filingStatusId IN (:statusList) and fd.uploadChannelIdFk.uploadChannelDesc IN (:uploadChannelList) and isEbrFiling = '0' ")
	List<FileDetails> getAciveFileDetailsDataByStatus(@Param("statusList") List<String> statusList, @Param("uploadChannelList") List<String> uploadChannelList);

	@Query(value = "select * FROM TBL_FILE_DETAILS fd where fd.FILING_STATUS_ID_FK in('17') and fd.IS_ACTIVE = '1' and fd.PROCESSING_FLAG = '0' and IS_EBR_FILING = '0' order by ID asc for update", nativeQuery = true)
	//	@Query(value = "select * FROM TBL_FILE_DETAILS fd where fd.id = 8039 for update", nativeQuery = true)
	List<FileDetails> getUnProcessedData();

	@Query(value = "FROM FileDetails fd  where fd.uploadChannelIdFk.uploadChannelId IN (:uploadChannelList) and fd.filingStatus.filingStatusId IN (:statusList) and  date(fd.creationDate) between date(:startsDate) and date(:endsDate) and isEbrFiling = '0' order by fd.creationDate desc")
	List<FileDetails> getFileDetailsData(@Param("uploadChannelList") List<Long> uploadChannelList, @Param("statusList") List<Integer> statusList, @Param("startsDate") String startsDate, @Param("endsDate") String endsDate);

	@Query(value = "FROM FileDetails fd where fd.uploadChannelIdFk.uploadChannelId IN (:uploadChannelList) and fd.filingStatus.filingStatusId IN (:statusList) and upper(fd.ifscCode) IN (:ifscCodeList) and date(fd.creationDate) between date(:startsDate) and date(:endsDate) and isEbrFiling = '0' order by fd.creationDate desc")
	List<FileDetails> getFileDetailsDataByEntityCode(@Param("uploadChannelList") List<Long> uploadChannelList, @Param("statusList") List<Integer> statusList, @Param("ifscCodeList") List<String> ifscCodeList, @Param("startsDate") String startsDate, @Param("endsDate") String endsDate);

	@Query(value = "FROM FileDetails fd where fd.uploadChannelIdFk.uploadChannelId IN (:uploadChannelList) and fd.filingStatus.filingStatusId IN (:statusList) and upper(fd.ifscCode) IN (:ifscCodeList) and fd.returnCode IS NULL and date(fd.creationDate) between date(:startsDate) and date(:endsDate) and isEbrFiling = '0' order by fd.creationDate desc")
	List<FileDetails> getFileDetailsDataWithEntityAndUnMappedReturn(@Param("uploadChannelList") List<Long> uploadChannelList, @Param("statusList") List<Integer> statusList, @Param("ifscCodeList") List<String> ifscCodeList, @Param("startsDate") String startsDate, @Param("endsDate") String endsDate);

	@Query(value = "FROM FileDetails fd where fd.uploadChannelIdFk.uploadChannelId IN (:uploadChannelList) and fd.filingStatus.filingStatusId IN (:statusList) and upper(fd.returnCode) IN (:returnCodeList) and upper(fd.ifscCode) IN (:ifscCodeList) and date(fd.creationDate) between date(:startsDate) and date(:endsDate) and isEbrFiling = '0' order by fd.creationDate desc")
	List<FileDetails> getFileDetailsDataByEntityCodeAndReturnCode(@Param("uploadChannelList") List<Long> uploadChannelList, @Param("statusList") List<Integer> statusList, @Param("returnCodeList") List<String> returnCodeList, @Param("ifscCodeList") List<String> ifscCodeList, @Param("startsDate") String startsDate, @Param("endsDate") String endsDate);

	@Query(value = "FROM FileDetails fd where fd.uploadChannelIdFk.uploadChannelId IN (:uploadChannelList) " + " and fd.filingStatus.filingStatusId IN (:statusList) " + " and ifnull(upper(fd.returnCode),'null') in (:returnCodeList,'null')  " + " and upper(fd.ifscCode) IN (:ifscCodeList) and date(fd.creationDate) between date(:startsDate) and date(:endsDate) and isEbrFiling = '0' order by fd.creationDate desc")
	List<FileDetails> getFileDetailsDataWithUnMappedReturns(@Param("uploadChannelList") List<Long> uploadChannelList, @Param("statusList") List<Integer> statusList, @Param("returnCodeList") List<String> returnCodeList, @Param("ifscCodeList") List<String> ifscCodeList, @Param("startsDate") String startsDate, @Param("endsDate") String endsDate);

	FileDetails getDataById(Long id);

	//	FileDetails getDataByVtlRequestIdAndFilingStatusFilingStatusId(String vtlRequestId, Integer filingStatusId);

	@Modifying
	@Query("update FileDetails det set det.filingStatus =:filingStatus, det.vtlRequestId =:vtlRequestId where id IN (:fileDetailId) ")
	int updateRecordStatus(@Param("fileDetailId") long[] fileDetailId, @Param("filingStatus") FilingStatus filingStatus, @Param("vtlRequestId") String vtlRequestId);

	@Modifying
	@Query("update FileDetails det set det.filingStatus =:filingStatus where id IN (:fileDetailId) ")
	int updateRecordStatus(@Param("fileDetailId") long[] fileDetailId, @Param("filingStatus") FilingStatus filingStatus);

	@Modifying
	@Query("update FileDetails det set det.vtlCallBackStatus =:vtlCallBackStatus where id =:fileDetailId ")
	int updateVtlCallBackStatus(@Param("fileDetailId") Long fileDetailId, @Param("vtlCallBackStatus") Integer vtlCallBackStatus);

	@Modifying
	@Query("update ElementAudit det set det.status =:filingStatus where det.fileDetails.id IN (:fileDetailId) ")
	int updateElementRecordStatus(@Param("fileDetailId") long[] fileDetailId, @Param("filingStatus") FilingStatus filingStatus);

	@Modifying
	@Query("update FileDetails det set det.processingFlag = 1 where id IN (:fileDetailId) ")
	int updateIsProcessingFlag(@Param("fileDetailId") List<Long> fileDetailId);

	@Query("SELECT distinct new com.iris.model.FileDetails(fd.id, fd.fileName,fd.systemModifiedFileName,fd.fileType," + "fd.fileMimeType,fd.creationDate,fd.entityCode, fd.isActive," + "fd.userMaster, fd.ifscCode, fd.jsonFileName) from FileDetails fd WHERE fd.filingStatus=:status and fd.isEbrFiling = '1' ORDER BY fd.creationDate ASC")
	List<FileDetails> fetchSdmxAuditRecords(FilingStatus status, Pageable pageable);

	@Modifying
	@Query("update FileDetails SET filingStatus =:status  where ID IN(:ebrFileAuditIds) and isEbrFiling = '1' ")
	void updateStatusOfSdmxFileAuditRecords(List<Long> ebrFileAuditIds, FilingStatus status);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select new com.iris.sdmx.ebrvalidation.bean.EbrFileDetails(fd.id, fd.entityCode, fd.creationDate, fd.systemModifiedFileName, ent.entityName, fd.userMaster.userId) " + " from FileDetails fd, EntityBean ent " + " where fd.filingStatus.filingStatusId IN(:status) and upper(fd.fileType) IN (:fileType) " + " and fd.isEbrFiling = 1 and fd.entityCode = ent.entityCode" + " ORDER BY id asc")
	List<EbrFileDetails> getEbrDocRecordsByStatusForUpdate(@Param("status") List<Integer> statusIdList, @Param("fileType") List<String> fileTypeList, org.springframework.data.domain.Pageable pagebPageable);

	@Query("SELECT new com.iris.model.FileDetails(fd.id, fd.entityCode, en.entityName, fd.systemModifiedFileName, fd.creationDate) from FileDetails fd, EntityBean en " + " WHERE fd.vtlRequestId =:vtlRequestId and fd.filingStatus.filingStatusId =:filingStatusId and fd.entityCode = en.entityCode")
	FileDetails fetchFileDetailsRecordBasedOnVTLRequestId(@Param("vtlRequestId") String vtlRequestId, @Param("filingStatusId") Integer filingStatusId);

	@Query("SELECT new com.iris.model.FileDetails(fd.id, fd.entityCode, en.entityName, fd.systemModifiedFileName, fd.creationDate, fd.vtlCallBackStatus, fd.vtlRequestId) from FileDetails fd, EntityBean en " + " WHERE fd.filingStatus.filingStatusId =:filingStatusId and fd.vtlCallBackStatus in (1,2) and fd.entityCode = en.entityCode")
	List<FileDetails> fetchFileDetailsRecordForVTLHtmlCreation(@Param("filingStatusId") Integer filingStatusId);
}
