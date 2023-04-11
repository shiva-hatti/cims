/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.PanMasterDetails;

/**
 * @author Siddique
 *
 */
public interface PanMasterDetailsRepo extends JpaRepository<PanMasterDetails, Integer> {

	@Query(value = "From PanMasterDetails order by Id desc")
	List<PanMasterDetails> getDataOrderByCreatedOnDesc();

	@Query(value = "From PanMasterDetails where status IN ('2','3') order by Id desc")
	List<PanMasterDetails> getPanMasterData();

}
