package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.XBRLWebFormPartialData;

public interface XbrlWebFormRepo extends JpaRepository<XBRLWebFormPartialData, Long> {

	public XBRLWebFormPartialData findByGuid(String guid);

	@Query("from XBRLWebFormPartialData where returnsUploadDetails.uploadId =:uploadId")
	public XBRLWebFormPartialData findByUploadId(Long uploadId);
}
