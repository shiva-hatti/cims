package com.iris.sdmx.lockrecord.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.sdmx.lockrecord.entity.SdmxLockRecordEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxLockRecordRepo extends JpaRepository<SdmxLockRecordEntity, Long> {

	@Query("FROM SdmxLockRecordEntity where recordDetailEncode=:recordDetailEncode and isActive =:isActive")
	SdmxLockRecordEntity findByRecordDetailEncodeNActive(String recordDetailEncode, Boolean isActive);

	@Query("FROM SdmxLockRecordEntity where recordDetailEncode=:recordDetailEncode")
	SdmxLockRecordEntity findByRecordDetailEncode(String recordDetailEncode);

	@Query(value = "SELECT * FROM TBL_SDMX_LOCK_RECORD where MODULE_ID=:moduleId and IS_ACTIVE=1 and JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnTemplateId') =:returnTemplateIdFk " + " and JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnPreviewId') =:returnPreviewId", nativeQuery = true)
	List<SdmxLockRecordEntity> findEntityByModelDimNElementId(@Param("moduleId") Long moduleId, @Param("returnTemplateIdFk") Long returnTemplateIdFk, @Param("returnPreviewId") Long returnPreviewId);

	@Query(value = "SELECT * FROM TBL_SDMX_LOCK_RECORD where IS_ACTIVE=1 and JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnPreviewId') =:returnPreviewId and" + " JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnTemplateId') =:returnTemplateId", nativeQuery = true)
	List<SdmxLockRecordEntity> getMultipleCellRefLock(@Param("returnTemplateId") Long returnTemplateId, @Param("returnPreviewId") Long returnPreviewId);

	@Query(value = "SELECT * FROM TBL_SDMX_LOCK_RECORD where IS_ACTIVE=1 and JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnPreviewId') =:returnPreviewId ", nativeQuery = true)
	List<SdmxLockRecordEntity> getCellRefLockLogs(@Param("returnPreviewId") Long returnPreviewId);

	@Query(value = "SELECT * FROM TBL_SDMX_LOCK_RECORD where IS_ACTIVE=1 and JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnPreviewId') =:returnPreviewId and" + " JSON_EXTRACT(`RECORD_DETAIL_JSON` , '$.returnCellRef') =:returnCellRef", nativeQuery = true)
	SdmxLockRecordEntity isReturnCellRefLocked(@Param("returnPreviewId") Long returnPreviewId, @Param("returnCellRef") Long returnCellRef);

	@Query("FROM SdmxLockRecordEntity where moduleId=:moduleId and isActive =:isActive order by createdOn desc")
	List<SdmxLockRecordEntity> getModuleWiseLocks(Long moduleId, Boolean isActive);

	@Query("FROM SdmxLockRecordEntity where isActive =:isActive and lockRecordId IN(:lockRecordIdList) and moduleId=:moduleId")
	List<SdmxLockRecordEntity> getLocks(List<Long> lockRecordIdList, Long moduleId, Boolean isActive);
}
