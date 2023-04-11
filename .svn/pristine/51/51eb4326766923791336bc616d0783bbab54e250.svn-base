/**
 * 
 */
package com.iris.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.PanMaster;

/**
 * @author Siddique
 *
 */

public interface PanMasterRepo extends JpaRepository<PanMaster, Long> {

	@Query("select distinct new com.iris.model.PanMaster(pm.panMasterId, pm.panNumber, pm.borrowerName ) FROM PanMaster pm order by pm.panNumber asc")
	List<PanMaster> getPanMasterData();

	@Query("FROM PanMaster pm order by pm.panNumber asc")
	List<PanMaster> getPanMasterDataWithCorporateName();

	@Query("FROM PanMaster pm where pm.panNumber =:panNumber and pm.borrowerName =:borrowerName")
	PanMaster getPanInfoByPanNumAndBorrowerName(@Param("panNumber") String panNumber, @Param("borrowerName") String borrowerName);

	@Query("FROM PanMaster pm where UPPER(pm.panNumber) LIKE :panNumber% order by pm.panNumber asc")
	List<PanMaster> getCustomisedPanMasterData(@Param("panNumber") String panNumber);

	@Query("select distinct new com.iris.model.PanMaster(pm.panMasterId, pm.panNumber, pm.borrowerName ) FROM PanMaster pm where pm.panNumber =:pannumber")
	PanMaster findByPanNumber(@Param("pannumber") String pannumber);

	@Query("FROM PanMaster pm where pm.panNumber IN (:panNumberList)")
	List<PanMaster> getRecordsByPanNumList(@Param("panNumberList") List<String> panNumberList);

	@Query("FROM PanMaster pm where pm.panNumber =:panNumber and pm.rbiGenerated =1")
	PanMaster checkIfRbiGeneratedPan(@Param("panNumber") String panNumber);

	@Transactional
	@Modifying
	@Query("update PanMaster pm SET pm.isActive = 0, pm.lastModifiedOn = current_timestamp, pm.lastModifiedBy.userId =:modifiedBy  " + " where pm.panNumber =:panNumber")
	int updatePanStatus(@Param("panNumber") String panNumber, @Param("modifiedBy") Long modifiedBy);

	@Query("FROM PanMaster pm where pm.borrowerName LIKE :borrowerName% order by pm.borrowerName asc")
	List<PanMaster> getCustomisedPanMasterDataByBorrowerName(@Param("borrowerName") String borrowerName);
}
