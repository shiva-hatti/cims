package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.iris.model.ReturnReturnTypeMapping;

public interface ReturnReturnTypeMappingRepo extends JpaRepository<ReturnReturnTypeMapping, Long> {
	@Query(value = "SELECT DISTINCT retTypeMapping from ReturnReturnTypeMapping retTypeMapping " + "where retTypeMapping.isActive = 1 and retTypeMapping.returnTypeIdFk.isActive = 1 " + "and retTypeMapping.returnIdFk.returnCode =:returnCode")
	List<ReturnReturnTypeMapping> getActiveMappForReturnCode(@Param("returnCode") String returnCode);
}
