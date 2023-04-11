package com.iris.sdmx.upload.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.model.FilingStatus;
import com.iris.sdmx.upload.entity.ElementAudit;

/**
 * @author vjadhav
 *
 */
@Repository
public interface ElementAuditRepo extends JpaRepository<ElementAudit, Long> {

	/**
	 * @param status
	 * @return
	 */
	@Query("SELECT distinct new com.iris.sdmx.upload.entity.ElementAudit(ea.elementAuditId, ea.elementCode, ea.elementVersion, ea.eleReturnRef," + "ea.convertedFileName) " + "FROM ElementAudit ea where ea.status=:status and ea.fileDetails.id =:fileDetailsId " + "ORDER BY ea.elementAuditId ASC ")
	List<ElementAudit> fetchElementAuditRecords(FilingStatus status, Long fileDetailsId);

	@Modifying
	@Query("update ElementAudit SET status =:status where elementAuditId IN(:elementAuditId)")
	void updateStatusOfSdmxElementAuditRecords(List<Long> elementAuditId, FilingStatus status);

	@Query("from ElementAudit where  fileDetails.id =:fileDetailsId ORDER BY elementAuditId ASC")
	List<ElementAudit> fetchElementAuditRecordsUsingFileDetailsID(Long fileDetailsId);

	@Query("from ElementAudit where status.filingStatusId=:status and fileDetails.filingStatus.filingStatusId =:status ORDER BY elementAuditId ASC")
	List<ElementAudit> fetchElementAuditRecordsWithStatus(Integer status);

}
