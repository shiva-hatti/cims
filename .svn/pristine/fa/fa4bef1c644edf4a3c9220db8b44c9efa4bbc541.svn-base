/**
 * 
 */
package com.iris.sdmx.dimesnsion.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.codelist.entity.CodeListMaster;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;

/**
 * @author sajadhav
 *
 */
@Repository
public interface DimensionRepo extends JpaRepository<DimensionMaster, Long> {

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimCode and  dm.isActive = 1 and dm.agencyMaster.agencyMasterCode =:agencyCode and dm.agencyMaster.isActive =1 and dm.conceptVersion =:conceptVersion ")
	public List<DimensionMaster> getSdmxDimensionMasterUsingDimCode(String dimCode, String agencyCode, String conceptVersion);

	List<DimensionMaster> findByIsActiveAndParentDimensionMasterDimesnsionMasterId(boolean isActive, Long parentDimensionMasterId);

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimCode and  dm.isActive = 1 and dm.agencyMaster.agencyMasterCode =:agencyCode and dm.agencyMaster.isActive =1 and dm.conceptVersion =:conceptVersion ")
	DimensionMaster getDimensionByCodeAndAgency(String dimCode, String agencyCode, String conceptVersion);

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimensionCode and  dm.isActive =:isActive and dm.agencyMaster.agencyMasterId =:agencyId and dm.conceptVersion =:conceptVersion ")
	DimensionMaster findByDimensionCodeIgnoreCaseAndIsActive(String dimensionCode, Long agencyId, String conceptVersion, boolean isActive);

	// List<DimensionMaster> findByDimensionCodeInAndIsActive(List<String>
	// dmCodeList, boolean isActive);

	@Query("FROM DimensionMaster dm where dm.dimensionCode IN(:dmCodeList) and  dm.isActive =:isActive and dm.agencyMaster.agencyMasterId =:agencyId and dm.conceptVersion =:conceptVersion ")
	List<DimensionMaster> findByDimensionCodeInAndIsActive(List<String> dmCodeList, Long agencyId, String conceptVersion, boolean isActive);

	@Query("FROM DimensionMaster dm where dm.dimensionCode IN(:dmCodeList) and  dm.isActive = 1 and dm.agencyMaster.agencyMasterId =:agencyId " + "and dm.conceptVersion =:conceptVersion and dm.isPending =:isPending ")
	List<DimensionMaster> findPendingDimensions(List<String> dmCodeList, Long agencyId, String conceptVersion, boolean isPending);

	List<DimensionMaster> findByIsActiveTrue();

	@Query("FROM DimensionMaster dm where dm.isActive = 1 and (dm.isCommon = 1 or dm.isMandatory = 1)")
	List<DimensionMaster> getCommonOrMandatoryDimension();

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimCode and  dm.isActive = 1 ")
	public DimensionMaster getEntityUsingDimCode(String dimCode);

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimCode and  dm.isActive =:isActive and dm.isPending=:isPending")
	public DimensionMaster getEntityUsingDimCode(String dimCode, Boolean isActive, Boolean isPending);

	@Query("FROM DimensionMaster dm where dm.dimensionCode =:dimCode and  dm.isActive =:isActive and dm.isPending=:isPending and dm.conceptVersion=:conceptVersion and dm.agencyMaster.agencyMasterCode=:agencyMasterCode")
	public DimensionMaster getEntityUsingDimCode(String dimCode, Boolean isActive, Boolean isPending, String conceptVersion, String agencyMasterCode);

	List<DimensionMaster> findByIsActive(boolean isActive);

	List<DimensionMaster> findByDimesnsionMasterIdIn(List<Long> longList);

	@Query("FROM DimensionMaster dm where dm.codeListMaster.clCode =:clCode and " + " dm.codeListMaster.clVersion =:clVersion and dm.isActive = 1 and dm.agencyMaster.agencyMasterCode =:agencyMasterCode and dm.agencyMaster.isActive =1 ")
	public List<DimensionMaster> checkCodeListMasterMappWithDim(String clCode, String clVersion, String agencyMasterCode);

	@Query("FROM DimensionMaster dm where dm.codeListMaster =:codeListMaster and  dm.isActive = 1 ")
	public List<DimensionMaster> getAllDimensionUsingClID(CodeListMaster codeListMaster);

	@Query("FROM DimensionMaster dm where dm.codeListMaster =:codeListMaster and  dm.isActive = 1 and dm.agencyMaster.agencyMasterCode =:agencyMasterCode ")
	public List<DimensionMaster> getAllDimensionUsingClIDAndAgencyCode(CodeListMaster codeListMaster, String agencyMasterCode);

	@Modifying(clearAutomatically = true)
	@Query("update DimensionMaster d set d.isPending =:isPending where d.dimesnsionMasterId =:dimesnsionMasterId")
	int setIsPending(Long dimesnsionMasterId, boolean isPending);

	DimensionMaster findByDimesnsionMasterId(Long dimesnsionMasterId);

	@Query("FROM DimensionMaster dm where dm.conceptVersion =:conceptVersion and  dm.isActive =:isActive and dm.agencyMaster.agencyMasterCode =:agencyCode")
	List<DimensionMaster> getByIsActiveAndConceptVersion(boolean isActive, String conceptVersion, String agencyCode);
}
