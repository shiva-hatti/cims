package com.iris.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.CustodianDetails;

/**
 * @author Bhavana Thakare
 *
 */
public interface CustodianNameRepo extends JpaRepository<CustodianDetails, Long> {
	@Query("FROM CustodianDetails cud where cud.isActive = 1 ORDER BY cud.clientName ASC ")
	List<CustodianDetails> findByIsActiveTrue();
}
