package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.CountryMaster;

/**
 * @author sikhan
 */
public interface CountryMasterRepo extends JpaRepository<CountryMaster, Long> {

	@Query("FROM CountryMaster where isActive = 1 order by countryName ")
	List<CountryMaster> findAllActiveData();

	@Query("FROM CountryMaster where UPPER(countryName) = UPPER(:countryName)")
	CountryMaster getCountryByName(@Param("countryName") String countryName);

	@Query("FROM CountryMaster where UPPER(countryNameBil) = UPPER(:countryName)")
	CountryMaster getCountryByNameBil(@Param("countryName") String countryName);

}