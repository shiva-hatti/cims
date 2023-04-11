/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.IsinMaster;

/**
 * @author Siddique H Khan
 *
 */
public interface IsinMasterRepository extends JpaRepository<IsinMaster, Integer> {

	@Query(value = "From IsinMaster")
	List<IsinMaster> fetchAllRecords();

}
