package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.SapBoDetails;

/**
 * @author psawant
 * @version 1.0
 * @date 10/09/2020
 */
public interface SapBoReportRepo extends JpaRepository<SapBoDetails, Integer> {

	List<SapBoDetails> findAll();

	@Query("FROM SapBoDetails where sapBoDetailsId=:sapBoDetailsId")
	SapBoDetails findDataById(@Param("sapBoDetailsId") Integer sapBoDetailsId);

	@Query("FROM SapBoDetails where identifier=:identifier and returnCode IS NOT NULL")
	List<SapBoDetails> getAllSadpDataWithReturnCode(@Param("identifier") String identifier);

	@Query("FROM SapBoDetails where identifier=:identifier and returnCode IS NULL")
	SapBoDetails getAllSadpData(@Param("identifier") String identifier);

}