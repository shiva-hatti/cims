package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.nbfc.model.NbfcStatus;

public interface NbfcStatusRepo extends JpaRepository<NbfcStatus, Long> {

	List<NbfcStatus> findByIsActiveTrue();

}
