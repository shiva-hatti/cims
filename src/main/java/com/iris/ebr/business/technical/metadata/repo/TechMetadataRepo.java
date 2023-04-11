package com.iris.ebr.business.technical.metadata.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.ebr.business.technical.metadata.entity.TechMetadatProcess;

@Repository
public interface TechMetadataRepo extends JpaRepository<TechMetadatProcess, Long> {

	@Query("FROM TechMetadatProcess dm where  dm.isActive = 1 ")
	public TechMetadatProcess getActiveTechnicalMetadata();
}
