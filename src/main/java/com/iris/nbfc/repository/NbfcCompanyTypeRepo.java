/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.nbfc.model.NbfcCompanyType;

/**
 * @author pmohite
 */
@Repository
public interface NbfcCompanyTypeRepo extends JpaRepository<NbfcCompanyType, Long> {

	List<NbfcCompanyType> findByIsActiveTrue();

}
