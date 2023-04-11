package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementSectorEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxElementSectorRepo extends JpaRepository<SdmxElementSectorEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementSectorEntity where isActive =:isActive")
	List<SdmxElementSectorEntity> findByActiveStatus(Boolean isActive);

	/**
	 * @param sectorName
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementSectorEntity u WHERE u.sectorName=:sectorName")
	int isDataExistWithSectorName(String sectorName);

	/**
	 * @param sectorId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementSectorEntity u WHERE u.sectorId=:sectorId")
	int isDataExistWithId(Long sectorId);

	/**
	 * @param sectorName
	 * @return
	 */
	@Query("FROM SdmxElementSectorEntity WHERE sectorName=:sectorName")
	SdmxElementSectorEntity findBySectorName(String sectorName);

	/**
	 * @param sectorId
	 * @return
	 */
	@Query("FROM SdmxElementSectorEntity WHERE sectorId=:sectorId")
	SdmxElementSectorEntity findBySectorId(Long sectorId);

	/**
	 * @param sectorId
	 * @return
	 */
	@Query("SELECT u.sectorName FROM SdmxElementSectorEntity u WHERE u.sectorId=:sectorId")
	String findNameBySectorId(Long sectorId);

}