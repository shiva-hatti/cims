package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.FinYearFreqDates;

public interface FinYearFreqDatesRepository extends JpaRepository<FinYearFreqDates, Long> {

	//List<FinYearFreqDates> findAll();

}
