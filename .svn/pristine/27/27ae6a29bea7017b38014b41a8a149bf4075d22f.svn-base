package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.nbfc.model.NbfcRoute;

public interface NbfcRoutRepo extends JpaRepository<NbfcRoute, Long> {

	List<NbfcRoute> findByIsActiveTrue();
}
