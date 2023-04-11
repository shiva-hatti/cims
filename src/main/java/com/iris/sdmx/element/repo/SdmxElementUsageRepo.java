package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementUsageEntity;

public interface SdmxElementUsageRepo extends JpaRepository<SdmxElementUsageEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementUsageEntity where isActive =:isActive")
	List<SdmxElementUsageEntity> findByActiveStatus(Boolean isActive);

	@Query("SELECT u.usageName FROM SdmxElementUsageEntity u WHERE u.usageId=:usageId")
	String findNameByUsageId(Long usageId);

}
