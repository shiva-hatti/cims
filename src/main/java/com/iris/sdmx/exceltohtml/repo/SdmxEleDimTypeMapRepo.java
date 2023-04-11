package com.iris.sdmx.exceltohtml.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.exceltohtml.entity.SdmxEleDimTypeMapEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxEleDimTypeMapRepo extends JpaRepository<SdmxEleDimTypeMapEntity, Long> {

	/**
	 * @param eleDimTypeMapId
	 * @return
	 */
	@Query("from SdmxEleDimTypeMapEntity where eleDimTypeMapId=:eleDimTypeMapId")
	SdmxEleDimTypeMapEntity findByEleDimTypeMapId(Long eleDimTypeMapId);

	/**
	 * @param eleDimHash
	 * @return
	 */
	@Query("SELECT eleDimTypeMapId FROM SdmxEleDimTypeMapEntity u where u.eleDimHash=:eleDimHash")
	List<Long> findEntityByEleDimHash(String eleDimHash);

	/**
	 * @param eleDimHash
	 * @return
	 */
	@Query("SELECT DISTINCT eleDimHash FROM SdmxEleDimTypeMapEntity u where u.eleDimHash=:eleDimHash")
	String checkEntityExistByEleDimHash(String eleDimHash);

	/**
	 * @param dsdCode
	 * @param elementVer
	 * @return
	 */
	@Query("SELECT max(groupNum) FROM SdmxEleDimTypeMapEntity u where u.dsdCode=:dsdCode and u.elementVer=:elementVer")
	Integer fetchMaxGroupNumByEleNVersion(String dsdCode, String elementVer);
}
