package com.iris.rbrToEbr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;

/**
 * @author vjadhav
 *
 */
@Repository
public interface EbrRbrFlowMasterRepo extends JpaRepository<EbrRbrFlowMaster, Long> {

	@Query("select new com.iris.rbrToEbr.entity.EbrRbrFlowMaster(ERF.flowId, ERF.flowName, ERF.taskName, ERF.sequence, ERF.priority) from EbrRbrFlowMaster ERF where ERF.flowId=:flowId")
	List<EbrRbrFlowMaster> getDataByFlowIdd(int flowId);

	@Query("from EbrRbrFlowMaster")
	List<EbrRbrFlowMaster> getAllData();
}
