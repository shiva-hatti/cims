package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.iris.model.RfaUpdateMaster;

public interface RfaUpdateMasterRepo extends JpaRepository<RfaUpdateMaster, Long> {

	RfaUpdateMaster findByBorrowerId(@Param("borrowerId") Long borrowerId);

}