/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.ReturnProperty;

public interface ReturnPropertyRepository extends JpaRepository<ReturnProperty, Long> {
	@Query("FROM ReturnProperty")
	List<ReturnProperty> getAllData();

}
