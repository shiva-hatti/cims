package com.iris.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.HiveDimPrimaryDealer;

public interface HiveDimPrimaryDealerRepo extends JpaRepository<HiveDimPrimaryDealer, Integer> {

	@Query("FROM HiveDimPrimaryDealer where UPPER(primaryDealerCode)=:entityCode and startEffectiveDate<=:reportEndDate and endEffectiveDate>:reportEndDate")
	HiveDimPrimaryDealer getEntityByEntityCode(@Param("entityCode") String entityCode, @Param("reportEndDate") Date reportEndDate);

}