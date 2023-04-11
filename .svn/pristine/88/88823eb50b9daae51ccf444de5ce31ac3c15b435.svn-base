package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.NbfcEntityProfileDetails;

public interface NbfcEntityProfileDetailsRepo extends JpaRepository<NbfcEntityProfileDetails, Long> {
	@Query("FROM NbfcEntityProfileDetails where entityBean.entityId = :entityId")
	NbfcEntityProfileDetails getNbfcEntityProfileDetails(@Param("entityId") Long entityId);
}
