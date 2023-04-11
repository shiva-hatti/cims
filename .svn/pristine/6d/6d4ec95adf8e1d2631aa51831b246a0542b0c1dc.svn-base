package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.RosMaster;

public interface RosMasterRepo extends JpaRepository<RosMaster, Long> {

	@Query("FROM RosMaster where returnObj.returnCode =:returnCode and entityCode =:entityCode and isActive = 1 order by instituteName asc")
	List<RosMaster> findRosMasterDetailsByEntityCode(@Param("returnCode") String returnCode, @Param("entityCode") String entityCode);

	@Query("FROM RosMaster where returnObj.returnCode =:returnCode and isActive = 1 order by instituteName asc")
	List<RosMaster> findRosMasterDetailsByReturnCode(@Param("returnCode") String returnCode);
}
