package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.iris.model.ReturnCustomDate;

public interface ReturnCustomDateRepo extends JpaRepository<ReturnCustomDate, Long> {

	@Query("FROM ReturnCustomDate rcd where rcd.returnIdFk.returnCode=:returnCode")
	ReturnCustomDate getReturnCustomDate(@Param("returnCode") String returnCode);
}
