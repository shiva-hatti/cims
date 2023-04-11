package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.iris.nbfc.model.NbfcShareHolder;

public interface NbfcShareHolderRepo extends JpaRepository<NbfcShareHolder, Long> {

	List<NbfcShareHolder> findByIsActiveTrue();

}
