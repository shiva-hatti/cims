package com.iris.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.PanMapping;
import com.iris.model.PanMasterTemp;

@Repository
public interface PanMappingRepo extends JpaRepository<PanMapping, Long> {
	@Query("select count(*) FROM PanMapping pmt where pmt.status IN (:verificationStatus)")
	Long getTotalCount(@Param("verificationStatus") List<String> verificationStatus);

	@Query("FROM PanMapping pmt where pmt.status IN (:verificationStatus) order by createdOn desc")
	List<PanMapping> findByStatusAndRequestedByEntityIdOrderByCreatedOnDesc(@Param("verificationStatus") List<String> verificationStatus, Pageable pageable);

	//@Query("FROM PanMapping pmt where pmt.requestedByEntityId =:entityId")
	List<PanMapping> findByRequestedByEntityId(Long entityId);

	@Query("FROM PanMapping pmt where pmt.status =:verificationStatus order by createdOn desc")
	List<PanMapping> findByStatusOrderByCreatedOnDesc(@Param("verificationStatus") String verificationStatus);

	@Query("From PanMapping pmt where pmt.status =:verificationStatus and pmt.dummyPanNumber in (:dummyPanNumber)")
	List<PanMapping> getPanList(@Param("dummyPanNumber") List<String> dummyPanNumber, @Param("verificationStatus") Integer verificationStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update PanMapping SET status = :verificationStatus, comment = :comment, approvedOn = current_timestamp, approvedByFk.userId = :approvedByFk  " + " where panMappingId in (:panMappingId) ")
	int updateVerificationStatus(@Param("verificationStatus") String verificationStatus, @Param("comment") String comment, @Param("approvedByFk") Long approvedByFk, @Param("panMappingId") Long panMappingId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update PanMapping SET status = :verificationStatus where actualPanNumber IN (:panNumberList) ")
	int updateVerificationStatusOfActualPan(@Param("verificationStatus") String verificationStatus, @Param("panNumberList") List<String> panNumberList);

	PanMapping getDataByPanMappingIdAndIsActiveTrue(Long id);

	@Query("FROM PanMapping pmt where pmt.dummyPanNumber =:pannumber and pmt.status NOT IN ('REJECTED')")
	PanMapping findByPanNumber(@Param("pannumber") String pannumber);

	@Query("select pmt.dummyPanNumber FROM PanMapping pmt where pmt.panMappingId =:panMappingId")
	String findDummyPanNumber(@Param("panMappingId") Long panMappingId);

}
