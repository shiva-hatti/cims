/**
 * 
 */
package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.TimeFormat;

/**
 * @author Siddique
 *
 */
public interface TimeFormatRepo extends JpaRepository<TimeFormat, Long> {

	TimeFormat findByIsActiveTrue();

}
