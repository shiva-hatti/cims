package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.Scheduler;

public interface SchedulerRepo extends JpaRepository<Scheduler, Long> {
	@Query("FROM Scheduler where schedulerId=:schedulerId")
	Scheduler findBySchedulerId(@Param("schedulerId") Long schedulerId);
}
