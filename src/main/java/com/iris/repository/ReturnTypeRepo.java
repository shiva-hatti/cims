package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.ReturnType;

/**
 * @author Bhavana Thakare
 *
 */
public interface ReturnTypeRepo extends JpaRepository<ReturnType, Long> {
	ReturnType findByReturnTypeId(Long retTypeId);

}
