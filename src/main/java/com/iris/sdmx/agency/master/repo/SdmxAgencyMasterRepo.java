package com.iris.sdmx.agency.master.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.agency.master.entity.AgencyMaster;

@Repository
public interface SdmxAgencyMasterRepo extends JpaRepository<AgencyMaster, Long> {

	List<AgencyMaster> findByIsActiveTrue();

	@Query("FROM AgencyMaster dm where dm.agencyMasterCode =:agencyCode and  dm.isActive = 1 ")
	AgencyMaster findByAgencyCode(String agencyCode);

	@Query("SELECT agencyFusionName FROM AgencyMaster dm where dm.agencyMasterCode =:agencyCode and  dm.isActive =:isActive ")
	String findAgencyNameByAgencyCode(String agencyCode, Boolean isActive);

	/**
	 * @param isActive
	 * @return
	 */
	@Query("SELECT new com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean(agencyMasterCode,agencyMasterLabel) " + "FROM AgencyMaster dm where dm.isActive =:isActive ORDER BY dm.agencyMasterLabel ASC")
	List<SdmxAgencyMasterBean> findAgencyNameByStatus(Boolean isActive);

	@Query("SELECT agencyMasterId FROM AgencyMaster dm where dm.agencyMasterCode =:agencyCode and  dm.isActive =:isActive ")
	Long findAgencyIdByAgencyCode(String agencyCode, Boolean isActive);

}
