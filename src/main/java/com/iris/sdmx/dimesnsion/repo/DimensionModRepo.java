/**
 * 
 */
package com.iris.sdmx.dimesnsion.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.dimesnsion.entity.DimensionMasterMod;

/**
 * @author sajadhav
 *
 */
@Repository
public interface DimensionModRepo extends JpaRepository<DimensionMasterMod, Long> {
	@Query("from DimensionMasterMod d where d.dimCode IN(:dimCode) and d.adminStatusId =:adminStatus " + "and d.conceptVersion =:conceptVersion and d.agencyIdFk.agencyMasterId =:agencyId and d.isActive = 1 ")
	List<DimensionMasterMod> findByDimCodeInAndAdminStatusId(List<String> dimCode, String conceptVersion, Long agencyId, int adminStatus);

	List<DimensionMasterMod> findByAdminStatusId(int adminStatusId);

	DimensionMasterMod findByDimMasterModId(Long dimMasterModId);

	@Modifying(clearAutomatically = true)
	@Query("update DimensionMasterMod d set d.adminStatusId =:adminStatusId, d.comments =:comments where d.dimMasterModId =:dimMasterModId")
	int approveRejectRequest(int adminStatusId, String comments, Long dimMasterModId);

	@Modifying(clearAutomatically = true)
	@Query("update DimensionMasterMod d set d.dimensionmaster.dimesnsionMasterId =:dimIdFk where d.dimMasterModId =:dimMasterModId")
	void updateDimIdFk(Long dimMasterModId, Long dimIdFk);
}
