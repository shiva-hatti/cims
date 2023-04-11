package com.iris.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.HiveDimNbfc;

public interface HiveDimNbfcRepo extends JpaRepository<HiveDimNbfc, Integer> {

	@Query("FROM HiveDimNbfc where UPPER(nbfcCode)=:entityCode and startEffectiveDate<=:reportEndDate and endEffectiveDate>:reportEndDate")
	HiveDimNbfc getEntityByEntityCode(@Param("entityCode") String entityCode, @Param("reportEndDate") Date reportEndDate);

}