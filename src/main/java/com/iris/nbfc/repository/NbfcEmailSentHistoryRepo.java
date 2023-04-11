/**
 * 
 */
package com.iris.nbfc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.nbfc.model.NBFCEmailSentHistory;

/**
 * @author sajadhav
 *
 */
public interface NbfcEmailSentHistoryRepo extends JpaRepository<NBFCEmailSentHistory, Long> {

}
