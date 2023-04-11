package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.InstitutionType;

public interface InstitutionTypeRepo extends JpaRepository<InstitutionType, Long> {

	@Query("FROM InstitutionType order by instTypeName asc")
	List<InstitutionType> getInstitutionTypeList();
}
