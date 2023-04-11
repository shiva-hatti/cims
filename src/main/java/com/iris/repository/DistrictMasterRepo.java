/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.DistrictMaster;

/**
 * @author Siddique H Khan
 *
 */
public interface DistrictMasterRepo extends JpaRepository<DistrictMaster, Long> {

	@Query(value = "FROM DistrictMaster where isActive = 1 and stateIdFk.isActive = 1")
	List<DistrictMaster> findAllActiveData();

	@Query(value = " FROM DistrictMaster where stateIdFk.stateCode = :stateCode and isActive = 1")
	List<DistrictMaster> getDistrictListByState(@Param("stateCode") String stateCode);
}
