/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionBean;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimension;

/**
 * @author sajadhav
 *
 */
public interface ElementDimensionRepo extends JpaRepository<ElementDimension, Long> {

	List<ElementDimension> findByIsActiveTrue();

	@Query("FROM ElementDimension em where em.element.dsdCode=:dsdCode and em.element.elementVer =:elementVersion and " + "em.element.agencyMaster.agencyMasterCode =:agencyCode and em.isActive = 1")
	ElementDimension findByElementDsdCodeAndElementElementVer(String dsdCode, String elementVersion, String agencyCode);

	ElementDimension findByIsActiveAndElementElementId(Boolean isActive, Long elementId);

	@Query("FROM ElementDimension em where em.element=:element and em.isActive = 1 ")
	ElementDimension findByElement(SdmxElementEntity element);

	@Query("SELECT em.elementDimensionJson FROM ElementDimension em where em.element.dsdCode=:elementCode and em.element.elementVer=:elementVersion and em.isActive=:isActive and em.element.isActive=:isActive and em.element.agencyMaster.agencyMasterCode =:agencyMasterCode ")
	String findByElementCodeDsdCodeAgencyNisActiveStatus(String elementCode, String elementVersion, String agencyMasterCode, Boolean isActive);

	/*
	 * @Query("FROM ElementDimension em where em.elementDimensionJson IS NOT NULL and em.isActive = 1 "
	 * ) List<ElementDimension> checkDimensionMappedWithElement(String dimCode);
	 */

	@Modifying(clearAutomatically = true)
	@Query("update ElementDimension e set e.isPending =:isPending where e.elementDimensionId =:elementDimensionId")
	int setIsPending(Long elementDimensionId, boolean isPending);

	@Query("select  new com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionBean(eleDim.elementDimensionId, eleDim.element.elementId, eleDim.elementDimensionJson, eleDim.element.elementVer, eleDim.element.dsdCode) from  ElementDimension eleDim, SdmxElementEntity ele " + " where ele.elementId in (:dsdIDList) and ele.elementId = eleDim.element.elementId ")
	List<ElementDimensionBean> findByComboUsingDsdCode(List<Long> dsdIDList);

}
