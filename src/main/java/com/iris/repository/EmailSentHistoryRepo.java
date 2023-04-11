/**
 * 
 */
package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.EmailSentHistory;

/**
 * @author sajadhav
 *
 */
public interface EmailSentHistoryRepo extends JpaRepository<EmailSentHistory, Long> {

}
