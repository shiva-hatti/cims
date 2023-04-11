package com.iris.rbrToEbr.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;

import com.iris.rbrToEbr.entity.EbrRbrFlow;

/**
 * @author vjadhav
 *
 */
@Repository
public interface EbrRbrFlowRepo extends JpaRepository<EbrRbrFlow, Long> {

	@Query(value = "SELECT * FROM TBL_CTL_EBR_RBR_FLOW e WHERE e.ENTITY_CODE=:entityCode AND e.RETURN_CODE=:returnCode \r\n" + "AND e.REPORTING_PERIOD=:reportingDate AND e.AUDIT_STATUS=:auditStatus and e.STATUS in(0,1)", nativeQuery = true)
	List<EbrRbrFlow> checkRecordExist(@Param("returnCode") String returnCode, @Param("entityCode") String entityCode, @Param("reportingDate") @DateTimeFormat(iso = ISO.DATE) String reportingDate, @Param("auditStatus") int auditStatus);

	@Query("select e from EbrRbrFlow e where e.entityCode=:entityCode and e.returnCode=:returnCode and \n" + "e.reportingPeriod=:reportingPeriod and e.auditStatus=:auditStatus and e.flowIdfk =:flowIdfk and e.status in(0,1)")
	List<EbrRbrFlow> checkRecordExist1(String entityCode, String returnCode, Date reportingPeriod, int auditStatus, int flowIdfk);

	@Query("select e.controlId from EbrRbrFlow e where e.entityCode=:entityCode and \n" + "e.reportingPeriod=:reportingPeriod and e.auditStatus=:auditStatus and e.status in(0)")
	Integer checkRecordExistWithStatusNEntityCode(String entityCode, Date reportingPeriod, int auditStatus);

	@Query(value = "SELECT * FROM TBL_CTL_EBR_RBR_FLOW e WHERE e.ENTITY_CODE=:entityCode \r\n" + "AND e.REPORTING_PERIOD=:reportingDate AND e.AUDIT_STATUS=:auditStatus and e.STATUS in(0)", nativeQuery = true)
	List<EbrRbrFlow> checkRecordExistWithStatusNEntityCode(@Param("entityCode") String entityCode, @Param("reportingDate") @DateTimeFormat(iso = ISO.DATE) String reportingDate, @Param("auditStatus") int auditStatus);

	/*------------------*/

	@Query(value = "SELECT * FROM TBL_CTL_EBR_RBR_FLOW e WHERE e.ENTITY_CODE=:entityCode AND e.RETURN_CODE=:returnCode \r\n" + "AND e.REPORTING_PERIOD=:reportingDate AND e.AUDIT_STATUS=:auditStatus order by e.CREATED_DATE DESC limit 1", nativeQuery = true)
	EbrRbrFlow getRecordJobId(@Param("returnCode") String returnCode, @Param("entityCode") String entityCode, @Param("reportingDate") @DateTimeFormat(iso = ISO.DATE) String reportingDate, @Param("auditStatus") int auditStatus);

	EbrRbrFlow findTop1ByEntityCodeAndReturnCodeAndReportingPeriodAndAuditStatusOrderByCreatedDateDesc(String entityCode, String returnCode, Date reportingPeriod, int auditStatus);

	/*--------------------------*/

	@Query("from EbrRbrFlow order by createdDate desc")
	List<EbrRbrFlow> getAllLogs();

	@Query("from EbrRbrFlow where entityCode =:entityCode order by createdDate desc")
	List<EbrRbrFlow> getAllLogsForEntity(String entityCode);
}
