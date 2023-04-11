package com.iris.repository;

/**
 * 
 */

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.ETLConceptsInfo;

/**
 * @author akhandagale
 *
 */
public interface ETLConceptInfoRepo extends JpaRepository<ETLConceptsInfo, Long> {

}
