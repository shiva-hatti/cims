
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ReturnEntityMappingNewMod;
import com.iris.model.ReturnsUploadDetails;

/**
 * @author sajadhav
 *
 */
public interface ReturnEntityMapNewModRepo extends JpaRepository<ReturnEntityMappingNewMod, Long> {

	@Query("from ReturnEntityMappingNewMod where entity.entityId =:entityId order by createdOn desc")
	List<ReturnEntityMappingNewMod> getEntityReturnModHistoryByEntityId(@Param("entityId") Long entityId, org.springframework.data.domain.Pageable pagebPageable);

}
