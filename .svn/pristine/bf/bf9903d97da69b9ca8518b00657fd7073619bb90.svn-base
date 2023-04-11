/**
 * 
 */
package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.EmailAlert;

/**
 * @author sajadhav
 *
 */
public interface PrepareSendMailRepo extends JpaRepository<EmailAlert, Long> {

	@Query(value = "FROM EmailAlert where emailAlertId=:id")
	EmailAlert getDataById(@Param("id") Long id);

}
