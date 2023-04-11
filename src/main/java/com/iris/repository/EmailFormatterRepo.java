/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.iris.model.EmailFormatter;

/**
 * @author sajadhav
 *
 */
public interface EmailFormatterRepo extends JpaRepository<EmailFormatter, Long> {

	@Query("FROM EmailFormatter")
	List<EmailFormatter> getAllDataFor();

}
