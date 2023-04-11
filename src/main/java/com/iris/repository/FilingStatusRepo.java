package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.FilingStatus;

public interface FilingStatusRepo extends JpaRepository<FilingStatus, Long> {

	FilingStatus getDataByStatus(String status);

	FilingStatus getDataByFilingStatusId(Integer statusId);

	@Query(value = "FROM FilingStatus where filingStatusId not in ('3','14','15','16') order by status asc")
	List<FilingStatus> fetchAllFilingStatus();

	@Query(value = "FROM FilingStatus  order by status asc")
	List<FilingStatus> fetchAllFillingStatusData();

}