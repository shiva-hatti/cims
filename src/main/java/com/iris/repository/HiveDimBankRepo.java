package com.iris.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.HiveDimBank;

public interface HiveDimBankRepo extends JpaRepository<HiveDimBank, Integer> {

	@Query("FROM HiveDimBank where UPPER(bankWorkingCode)=:entityCode and startEffectiveDate<=:reportEndDate and endEffectiveDate>:reportEndDate")
	HiveDimBank getEntityByEntityCode(@Param("entityCode") String entityCode, @Param("reportEndDate") Date reportEndDate);

}