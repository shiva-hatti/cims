package com.iris.rbrToEbr.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;

import com.iris.rbrToEbr.entity.EbrRbrConvData;

@Repository
public interface EbrRbrConvRepo extends JpaRepository<EbrRbrConvData, Long> {

	@Query("select c from EbrRbrConvData c where c.bankCode=:entityCode and c.returnCode=:returnCode and " + "c.reportingDate=:reportingdate and c.auditFlag=:auditStatus")
	EbrRbrConvData checkIfValidRequest(String entityCode, String returnCode, Date reportingdate, int auditStatus);

	@Query(value = "SELECT * FROM TBL_EBR_RBR_CONVERSION_DATA where BANK_WORKING_CODE=:entityCode and " + "RETURN_CODE=:returnCode and REPORTING_DATE=:reportingDate and AUDIT_FLAG=:auditStatus", nativeQuery = true)
	EbrRbrConvData checkIfValidRequestNative(@Param("returnCode") String returnCode, @Param("entityCode") String entityCode, @Param("reportingDate") @DateTimeFormat(iso = ISO.DATE) String reportingDate, @Param("auditStatus") int auditStatus);
}
