package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.CountryRiskMapping;

public interface CountryRiskMappingRepo extends JpaRepository<CountryRiskMapping, Long> {

	@Query("FROM CountryRiskMapping where returnObj.returnCode =:returnCode and isActive = 1 order by countryIdFk.countryName asc")
	List<CountryRiskMapping> findCountryRiskMappingDetailsByReturnCode(@Param("returnCode") String returnCode);
}
