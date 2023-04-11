package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.AuditFirmLabelMod;

public interface AuditFirmLabelModRepo extends JpaRepository<AuditFirmLabelMod, Long> {

	@Query(value = "SELECT * FROM TBL_AUDIT_FIRM_LABEL_MOD_HISTORY where AUDIT_FIRM_LABEL_ID_FK =:auditFirmId ORDER BY  LAST_MODIFIED_ON DESC LIMIT 1", nativeQuery = true)
	AuditFirmLabelMod getPreviousAuditFirmLabelById(@Param("auditFirmId") Long auditFirmId);

}
