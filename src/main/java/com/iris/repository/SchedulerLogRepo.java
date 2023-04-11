package com.iris.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iris.model.SchedulerLog;

@Repository
public interface SchedulerLogRepo extends JpaRepository<SchedulerLog, Long> {

	SchedulerLog getDataById(Long id);

	@Query(value = "FROM SchedulerLog where schedulerIdFk.schedulerId = :schedulerId and isRunning = '1'")
	public List<SchedulerLog> findDataBySchedulerId(@Param("schedulerId") Long schedulerId);

	@Query(value = "FROM SchedulerLog where id = :schedulerLogId and isRunning = '1'")
	public SchedulerLog findDataBySchedulerLogId(@Param("schedulerLogId") Long schedulerLogId);

	@Transactional
	@Modifying
	@Query(value = "update SchedulerLog set isRunning = :isRunning where schedulerIdFk.schedulerId = :schedulerId")
	void stopScheduler(@Param("schedulerId") Long schedulerId, @Param("isRunning") boolean isRunning);

}
