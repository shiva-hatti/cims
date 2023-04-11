package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.iris.model.XbrlTaxonomy;

public interface XbrlTaxonomyRepo extends JpaRepository<XbrlTaxonomy, Long> {
	@Query("FROM XbrlTaxonomy xt WHERE xt.validFromDate in(Select max(validFromDate) from XbrlTaxonomy)")
	XbrlTaxonomy findByLatestTaxonomyByValidFromDate();
}
