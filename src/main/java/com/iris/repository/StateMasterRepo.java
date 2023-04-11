/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.StateMaster;

/**
 * @author sikhan
 *
 */
public interface StateMasterRepo extends JpaRepository<StateMaster, Long> {

	@Query(value = "FROM StateMaster where isActive = 1 and regionIdFk.isActive = 1")
	List<StateMaster> getActiveStateData();

}
