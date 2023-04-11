package com.iris.sdmx.fusion.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.fusion.entity.SdmxFusionDsdDetailEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxFusionDsdDetailRepo extends JpaRepository<SdmxFusionDsdDetailEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxFusionDsdDetailEntity where isActive=:isActive ORDER BY createdOn DESC")
	List<SdmxFusionDsdDetailEntity> findEntitiesByActiveStatus(Boolean isActive);

	@Query(value = "SELECT MAX(CAST(DSD_VER as float )) as DSD_VER from TBL_SDMX_FUSION_DSD_DETAIL  where IS_ACTIVE =:isActive and AGENCY_MASTER_ID_FK =:agencyMasterID  ORDER BY CREATED_ON DESC", nativeQuery = true)
	String findMaxVersionActiveStatus(Boolean isActive, long agencyMasterID);

}
