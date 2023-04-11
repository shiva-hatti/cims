/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.nbfc.model.NbfcCompanyTypeOther;

/**
 * @author pmohite
 */
@Repository
public interface NbfcCompanyTypeOtherRepo extends JpaRepository<NbfcCompanyTypeOther, Long> {

	List<NbfcCompanyTypeOther> findByIsActiveTrue();
}
