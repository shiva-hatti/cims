/**
 * 
 */
package com.iris.sdmx.codelist.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.codelist.entity.CodeListMaster;

/**
 * @author sajadhav
 *
 */
public interface CodeListMasterRepo extends JpaRepository<CodeListMaster, Long> {

	List<CodeListMaster> findByIsActiveTrueOrderByClCodeAsc();

	CodeListMaster findByClId(Long clId);

	CodeListMaster findByClCodeIgnoreCaseAndClVersionAndIsActive(String clCode, String clVersion, boolean isActive);

	@Query("from  CodeListMaster c where c.clCode=:clCode and c.clVersion=:clVersion and " + " c.agencyMaster.agencyMasterCode=:agencyMasterCode and c.agencyMaster.isActive =:isActive and c.isActive =:isActive ")
	CodeListMaster findByCodeVersionAndAgency(String clCode, String clVersion, String agencyMasterCode, boolean isActive);

	List<CodeListMaster> findByClCodeIn(List<String> clCodeList);

	List<CodeListMaster> findByIsActiveTrueAndClCode(String clCode);

	@Modifying(clearAutomatically = true)
	@Query("update CodeListMaster c set c.isPending =:isPending where c.clId =:clMasterId")
	int setIsPending(Long clMasterId, boolean isPending);

}
