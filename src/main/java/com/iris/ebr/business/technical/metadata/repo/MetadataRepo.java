package com.iris.ebr.business.technical.metadata.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.ebr.business.technical.metadata.entity.BusMetadatProcess;

@Repository
public interface MetadataRepo extends JpaRepository<BusMetadatProcess, Long> {

	@Query("FROM BusMetadatProcess dm where dm.returnCode =:returnCode and  dm.isActive = 1 and dm.returnVersion=:returnVersion and dm.ebrVersion=:ebrVersion ")
	public BusMetadatProcess getActiveRecordForReturnAndEbrVersion(String returnCode, String returnVersion, String ebrVersion);

}
