package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementFrequencyEntity;

public interface SdmxElementFrequencyRepo extends JpaRepository<SdmxElementFrequencyEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementFrequencyEntity where isActive =:isActive")
	List<SdmxElementFrequencyEntity> findByActiveStatus(Boolean isActive);

	@Query("SELECT u.frequencyName FROM SdmxElementFrequencyEntity u WHERE u.frequencyId=:frequencyId")
	String findNameByFrequencyId(Long frequencyId);
}
