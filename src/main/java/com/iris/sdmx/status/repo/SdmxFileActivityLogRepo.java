package com.iris.sdmx.status.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.status.entity.SdmxFileActivityLog;

/**
 * @author apagaria
 *
 */
public interface SdmxFileActivityLogRepo extends JpaRepository<SdmxFileActivityLog, Long> {

	@Query("from SdmxFileActivityLog where  fileDetailsIdFk.id =:fileDetailsId ")
	SdmxFileActivityLog fetchSdmxFileActivity(Long fileDetailsId);

	@Query("from SdmxFileActivityLog where  fileDetailsIdFk.id =:fileDetailsId and sdmxProcessDetailIdFk.processCode =:processCode")
	SdmxFileActivityLog fetchSdmxFileActivity(Long fileDetailsId, String processCode);

}