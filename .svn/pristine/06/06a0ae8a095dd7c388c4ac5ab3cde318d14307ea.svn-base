package com.iris.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.HiveDimIndianAgent;

public interface HiveDimIndianAgentRepo extends JpaRepository<HiveDimIndianAgent, Integer> {

	@Query("FROM HiveDimIndianAgent where UPPER(indianAgentCode)=:entityCode and startEffectiveDate<=:reportEndDate and endEffectiveDate>:reportEndDate")
	HiveDimIndianAgent getEntityByEntityCode(@Param("entityCode") String entityCode, @Param("reportEndDate") Date reportEndDate);

}