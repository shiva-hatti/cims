/**
 * 
 */
package com.iris.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.FilingPan;

/**
 * @author sajadhav
 *
 */
public interface FilingPanRepo extends JpaRepository<FilingPan, Long> {

	@Transactional
	@Modifying
	@Query("update FilingPan set status = :status where filingPanId = :filingPanId")
	void updateFilingStatusById(@Param("filingPanId") Integer filingPanId, @Param("status") String status);

	@Query("from FilingPan where returnsUploadDetails.uploadId IN(:uploadId) and status =:status")
	List<FilingPan> getDataByStatusAndUploadId(@Param("uploadId") Long uploadId, @Param("status") String status);

	FilingPan findByCompanyPanAndReturnsUploadDetailsUploadId(String companyPan, Long uploadId);
}
