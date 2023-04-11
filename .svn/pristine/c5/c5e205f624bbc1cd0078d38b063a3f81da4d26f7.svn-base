/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.nbfc.model.NbfcCorRegistrationStatus;

/**
 * @author Shivabasava Hatti
 *
 */
@Repository
public interface NbfcCorRegistrationStatusRepo extends JpaRepository<NbfcCorRegistrationStatus, Long> {

	@Query(value = "from NbfcCorRegistrationStatus where userIdFk.userId = :userId and corRegIdFk.corRegistrationId = :entityId")
	List<NbfcCorRegistrationStatus> getCorRegistrationStatusByUserIdAndNbfcEntityId(Long entityId, Long userId);

}
