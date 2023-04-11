package com.iris.sdmx.ebrtorbr.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.ebrtorbr.entity.SdmxEbrToRbrPreparation;

public interface SdmxEbrToRbrPreparationRepo extends JpaRepository<SdmxEbrToRbrPreparation, Long> {

	//	@Query("select new com.iris.sdmx.ebrtorbr.bean.SdmxEbrToRbrPreparationBean(entity.entityId, startDate, endDate, returnObj.returnId, "
	//			+ " mandateDatapointExpectedJson, mandateDatapointReceivedJson, optionalDatapointExpectedJson, "
	//			+ " optionalDatapointReceivedJson, reportPreStartOn, reportPreEndOn, returnPropertyVal.returnProprtyValId) "
	//			+ " from SdmxEbrToRbrPreparation where isFilingDone = 0")
	//	List<SdmxEbrToRbrPreparationBean> getNotCompletedEBRFiling();

	//	@EntityGraph(attributePaths = {"returnPropertyVal"})
	@Query("from SdmxEbrToRbrPreparation where isFilingDone = 0")
	//	@Query("from SdmxEbrToRbrPreparation where ebrToRbrPreparationId = 72")
	//	@Query("from SdmxEbrToRbrPreparation where ebrToRbrPreparationId = 5")
	List<SdmxEbrToRbrPreparation> getNotCompletedEBRFiling();

	@Query("from SdmxEbrToRbrPreparation where entity.entityId =:entityId and returnObj.returnId =:returnId and endDate =:endDate and isFilingDone = '1'")
	List<SdmxEbrToRbrPreparation> getExisitngFilingRecord(Long entityId, Long returnId, Date endDate);

	@Query(value = "SELECT RETURN_ID_FK FROM TBL_SDMX_EBR_TO_RBR_PREP WHERE RETURN_ID_FK IN (:returnIds) and date(END_DATE) = date(:endDate) ", nativeQuery = true)
	List<Long> checkMappingExistForReportingDateAndReturnIds(List<Long> returnIds, String endDate);
}
