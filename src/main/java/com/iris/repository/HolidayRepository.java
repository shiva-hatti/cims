package com.iris.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	@Query("FROM Holiday WHERE isActive = 1")
	public List<Holiday> findActiveHoliday();

	@Query("FROM Holiday h WHERE isActive = 1 and h.holidayDate >= :fromYear and h.holidayDate <= :toYear")
	public List<Holiday> findActiveHoliday(@Param("fromYear") Date fromYear, @Param("toYear") Date toYear);
}
