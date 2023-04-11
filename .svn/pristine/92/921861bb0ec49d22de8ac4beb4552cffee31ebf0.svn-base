/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.nbfc.model.NbfcNoteMessages;

/**
 * @author sikhan
 *
 */
public interface NbfcNoteMessagesRepo extends JpaRepository<NbfcNoteMessages, Long> {

	List<NbfcNoteMessages> findByIsActiveTrue();

	@Query(value = " FROM NbfcNoteMessages where nbfcPageMaster.pageMasterId = :nbfcPageMasterId and isActive = '1'")
	List<NbfcNoteMessages> getNbfcNoteMessagesByNbfcPageMaster(@Param("nbfcPageMasterId") Long nbfcPageMasterId);

}
