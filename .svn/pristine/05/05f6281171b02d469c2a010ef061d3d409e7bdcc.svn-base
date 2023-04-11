package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.PSUCategoryMaster;

/**
 * @author sikhan
 */
public interface PSUCategoryMasterRepo extends JpaRepository<PSUCategoryMaster, Long> {

	@Query("FROM PSUCategoryMaster where isActive = 1")
	List<PSUCategoryMaster> findAllActiveData();

}