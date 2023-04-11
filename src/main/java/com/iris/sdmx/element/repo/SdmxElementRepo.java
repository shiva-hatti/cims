package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.element.entity.SdmxElementEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxElementRepo extends JpaRepository<SdmxElementEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementEntity where isActive=:isActive ORDER BY elementLabel ASC")
	List<SdmxElementEntity> findEntitiesByActiveStatus(Boolean isActive);

	@Query("from SdmxElementEntity where isActive=:isActive and agencyMaster.agencyMasterCode=:agencyMasterCode ORDER BY elementLabel ASC")
	List<SdmxElementEntity> findEntitiesByActiveStatusWithAgency(Boolean isActive, String agencyMasterCode);

	/**
	 * @param isActive
	 * @param isPending
	 * @return
	 */
	@Query("SELECT DISTINCT new com.iris.sdmx.element.bean.SdmxElementBean(see.elementId, see.dsdCode, see.elementLabel, see.elementVer) from SdmxElementEntity see where isActive=:isActive and isPending=:isPending and agencyMaster.agencyMasterCode = 'A001' ORDER BY elementLabel ASC")
	List<SdmxElementBean> findEntitiesByActiveStatus(Boolean isActive, Boolean isPending);

	/**
	 * @param isActive
	 * @param isPending
	 * @param agencyCode
	 * @return
	 */
	@Query("SELECT DISTINCT new com.iris.sdmx.element.bean.SdmxElementBean(see.elementId, see.dsdCode, see.elementLabel, see.elementVer) from SdmxElementEntity see where isActive=:isActive and isPending=:isPending and agencyMaster.agencyMasterCode =:agencyCode ORDER BY elementLabel ASC")
	List<SdmxElementBean> findEntitiesByActiveStatusNAgencyCode(Boolean isActive, Boolean isPending, String agencyCode);

	/**
	 * @param elementId
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementEntity where elementId=:elementId and isActive=:isActive")
	SdmxElementEntity findEntityByActiveStatus(Long elementId, Boolean isActive);

	@Query("from SdmxElementEntity where elementId=:elementId and isActive=:isActive and isPending=:isPending")
	SdmxElementEntity findEntityByActiveStatus(Long elementId, Boolean isActive, Boolean isPending);

	/**
	 * @param elementId
	 * @param isActive
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementEntity u WHERE u.elementId=:elementId and u.isActive=:isActive")
	int findByElementIdAndActiveStatus(Long elementId, Boolean isActive);

	/**
	 * @param dsdCode
	 * @param elementVer
	 * @param isActive
	 * @return
	 */
	@Query("SELECT u.elementId FROM SdmxElementEntity u WHERE u.dsdCode=:dsdCode and u.elementVer=:elementVer and u.isActive=:isActive and u.agencyMaster.agencyMasterCode=:agencyMasterCode and  u.agencyMaster.isActive = 1")
	Long findByDsdCodeAndVerAndIsActive(String dsdCode, String elementVer, Boolean isActive, String agencyMasterCode);

	/**
	 * @param dsdCode
	 * @param isActive
	 * @return
	 */
	@Query("SELECT u.elementVer FROM SdmxElementEntity u WHERE u.dsdCode=:dsdCode and u.isActive=:isActive and u.agencyMaster.agencyMasterCode=:agencyMasterCode and  u.agencyMaster.isActive = 1")
	List<String> findVersionsByDsdCode(String dsdCode, Boolean isActive, String agencyMasterCode);

	@Query("FROM SdmxElementEntity u WHERE u.dsdCode=:dsdCode and u.elementVer=:elementVer and u.isActive=:isActive and u.agencyMaster.agencyMasterCode=:agencyCode")
	SdmxElementEntity findByDsdCodeAndVer(String dsdCode, String elementVer, Boolean isActive, String agencyCode);

	/**
	 * @param elementId
	 * @param isActive
	 * @return
	 */
	// @Query("from SdmxElementEntity where parentElementIdFk=:elementId and
	// isActive=:isActive")
	List<SdmxElementEntity> findByIsActiveAndParentElementIdFkElementId(Boolean isActive, Long elementId);

	@Query("from SdmxElementEntity where elementId=:elementId")
	SdmxElementEntity findByElementId(Long elementId);

	@Modifying(clearAutomatically = true)
	@Query("update SdmxElementEntity u set u.isActive =:isActive where u.elementId =:elementId")
	int activateInactivateElement(Long elementId, boolean isActive);

	@Modifying(clearAutomatically = true)
	@Query("update SdmxElementEntity u set u.isPending =:isPending where u.elementId =:elementId")
	int setIsPending(Long elementId, boolean isPending);

	@Query("select new com.iris.sdmx.element.entity.SdmxElementEntity(u.elementId,u.isActive,u.isPending) from SdmxElementEntity u " + "where u.dsdCode=:dsdCode and u.elementVer=:elementVer and u.isActive=:isActive and u.agencyMaster.agencyMasterCode=:agencyMaserCode and  u.agencyMaster.isActive = 1")
	SdmxElementEntity findByDsdCodeVersionIsActive(String dsdCode, String elementVer, Boolean isActive, String agencyMaserCode);

	List<SdmxElementEntity> findByDsdCodeInAndIsActive(List<String> elementCodeList, boolean isActive);

}
