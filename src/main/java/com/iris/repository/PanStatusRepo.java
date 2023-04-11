/**
 * 
 */
package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.PanStatus;

/**
 * @author Administrator
 *
 */
public interface PanStatusRepo extends JpaRepository<PanStatus, Long> {

	PanStatus findByPanStatusId(Long panStatusId);

}
