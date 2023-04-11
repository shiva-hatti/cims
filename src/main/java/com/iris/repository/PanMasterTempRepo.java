package com.iris.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.PanMasterTemp;

@Repository
public interface PanMasterTempRepo extends JpaRepository<PanMasterTemp, Integer> {

	PanMasterTemp findTopByPanNumberOrderByCreatedOnDesc(String panNumber);

	PanMasterTemp findTopByPanNumberAndVerificationStatusOrderByCreatedOnDesc(String panNumber, Integer verificationStatus);

	PanMasterTemp findByPanNumberAndEntryType(@Param("panNumber") String panNumber, @Param("entryType") Integer entryType);

	@Query("FROM PanMasterTemp pmt where pmt.status =:status order by createdOn desc")
	List<PanMasterTemp> findByVerificationStatusAndEntityBeanOrderByCreatedOnDesc(@Param("status") String status, Pageable pageable);

	@Query("From PanMasterTemp where panId in (select max(a.panId) from PanMasterTemp a where a.status = :status group by a.panNumber) and status = :status order by createdOn desc")
	List<PanMasterTemp> findLatestPan(@Param("status") String status, Pageable pageable);

	@Query("From PanMasterTemp where panNumber like (:panNumber) order by panId desc")
	List<PanMasterTemp> findByRbiGeneratedTrueOrderByPanIdDesc(@Param("panNumber") String panNumber);

	List<PanMasterTemp> findByStatusAndRbiGeneratedTrueOrderByCreatedOnAsc(String status);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update PanMasterTemp SET status = :status, comment = :comment, approvedOn = current_timestamp, approvedByFk.userId = :approvedByFk  " + " where panNumber =:panNumber and verificationStatus = 0")
	int updateStatus(@Param("status") String status, @Param("comment") String comment, @Param("approvedByFk") Long approvedByFk, @Param("panNumber") String panNumber);

	@Query("From PanMasterTemp where panNumber = :panNumber")
	PanMasterTemp getPanDetails(@Param("panNumber") String panNumber);

	//	@Query(value ="From PanMasterTemp  where panNumber IN (:panNumber) and createdOn IN(Select MAX(createdOn) from PanMasterTemp where panNumber IN (:panNumber) group by panNumber) order by createdOn desc")
	@Query(value = "From PanMasterTemp  where panId IN (Select MAX(panId) from PanMasterTemp  where panNumber IN (:panNumber) group by panNumber)")
	List<PanMasterTemp> getDataByPanNum(@Param("panNumber") Set<String> panNumber);

	//	@Query(value = "From PanMasterTemp where verificationStatus = 1 and rbiGenerated = 0")
	//	@EntityGraph(attributePaths = {"createdBy","entityBean","panIdFk",})
	//	List<PanMasterTemp> getData();

	@Query(value = "Select MAX(approvedOn) from PanMasterTemp")
	Date getMaxApprovedOnDate();

	@Query("From PanMasterTemp where panNumber IN (:panNumber)")
	List<PanMasterTemp> getPanNumber(@Param("panNumber") List<String> panNumber);

	@Query("From PanMasterTemp where panNumber IN (select MAX(panNumber) from PanMasterTemp where panNumber like (:rbiGeneratedPan))")
	PanMasterTemp findTopByPanNumberOrderByPanNumberDescContaining(String rbiGeneratedPan);

	@Query("select count(*) FROM PanMasterTemp pmt where pmt.status =:status")
	Long getTotalCount(@Param("status") String status);

}
