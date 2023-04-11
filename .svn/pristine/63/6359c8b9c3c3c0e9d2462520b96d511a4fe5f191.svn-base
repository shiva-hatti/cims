package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.ReturnSectionVersionMap;

public interface ReturnSectionVersionMapRepo extends JpaRepository<ReturnSectionVersionMap, Integer> {

	@Query(value = "FROM ReturnSectionVersionMap rsvm where rsvm.isActive=1 and rsvm.taxonomyIdFk.returnTemplateId = :returnTemplateId")
	List<ReturnSectionVersionMap> findByReturnTemplate(Long returnTemplateId);

}
