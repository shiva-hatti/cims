package com.iris.rbrToEbr.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;

import com.iris.rbrToEbr.entity.EbrDataConversionLog;

/**
 * @author vjadhav
 *
 */
@Repository
public interface EbrDataConversionLogRepo extends JpaRepository<EbrDataConversionLog, Long> {

	@Query(value = "SELECT * FROM TBL_EBR_DATA_CONVERSION_LOG", nativeQuery = true)
	List<EbrDataConversionLog> getAllData();

	@Query(value = "SELECT * FROM TBL_EBR_DATA_CONVERSION_LOG e WHERE e.ENTITY_ID_FK=:entityId AND e.RETURN_ID_FK=:returnId \r\n" + "AND e.END_DATE=:endDate", nativeQuery = true)
	EbrDataConversionLog checkRecordExist(@Param("returnId") Long returnId, @Param("entityId") Long entityId, @Param("endDate") @DateTimeFormat(iso = ISO.DATE) String endDate);
}
