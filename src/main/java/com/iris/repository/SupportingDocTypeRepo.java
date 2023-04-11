package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.SupportingDocType;

public interface SupportingDocTypeRepo extends JpaRepository<SupportingDocType, Long> {

	@Query("FROM SupportingDocType order by docType asc")
	List<SupportingDocType> getSupportingDocTypeList();
}
