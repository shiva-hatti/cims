package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.WebformVersionMap;

public interface WebFormVersionRepo extends JpaRepository<WebformVersionMap, Long> {

	//@Query("FROM WebformVersionMap vm WHERE taxonomyIdFk.taxonomyId =: ")
	List<WebformVersionMap> findByTaxonomyIdFkReturnTemplateId(Long taxonomyIdFk);
}
