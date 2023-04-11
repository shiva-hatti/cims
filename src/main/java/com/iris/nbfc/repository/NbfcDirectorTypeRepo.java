package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.nbfc.model.NbfcDirectorType;

public interface NbfcDirectorTypeRepo extends JpaRepository<NbfcDirectorType, Long> {
	List<NbfcDirectorType> findByIsActiveTrue();
}
