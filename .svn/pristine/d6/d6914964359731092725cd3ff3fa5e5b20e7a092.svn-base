package com.iris.ebr.metadata.flow.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.ebr.metadata.flow.entity.EbrMetadataFlow;

@Repository
public interface EbrMetadataFlowRepo extends JpaRepository<EbrMetadataFlow, Long> {

	@Query(value = "SELECT * FROM TBL_CTL_EBR_METADATA_FLOW where FLOW_ID_FK =:flowMasterID order by CREATED_DATE desc LIMIT 1", nativeQuery = true)
	List<EbrMetadataFlow> checkEntryPresent(int flowMasterID);
}
