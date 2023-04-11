package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.nbfc.model.NbfcCapitalType;

/**
 * @author Lkumar
 */
@Repository
public interface NbfcCapitalTypeRepo extends JpaRepository<NbfcCapitalType, Long> {

	List<NbfcCapitalType> findByIsActiveTrue();

}
