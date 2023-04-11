package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.WorkflowReturnMapping;

public interface WorkflowRepo extends JpaRepository<WorkflowReturnMapping, Long> {

	List<WorkflowReturnMapping> getDataByWorkflowIdIn(Long[] workflowIds);

	List<WorkflowReturnMapping> findByActiveTrue();

	@Query("FROM WorkflowReturnMapping where workFlowMaster.workflowId=:workflowId")
	WorkflowReturnMapping getDataByWorkflowId(@Param("workflowId") Long workflowId);

	@Query("FROM WorkflowReturnMapping where workFlowMaster.workflowId=:workflowMasterId and active=1")
	List<WorkflowReturnMapping> fetchWorkFlowData(@Param("workflowMasterId") Long workflowMasterId);

	List<WorkflowReturnMapping> findByReturnIdFkReturnIdAndChannelIdFkUploadChannelIdAndActiveTrue(Long returnId, Long workflowId);

	@Query("select new com.iris.model.WorkflowReturnMapping(map.workflowId, map.workFlowMaster.workflowId, map.returnIdFk.returnId, map.channelIdFk.uploadChannelId, map.workFlowMaster.workFlowName) " + " FROM WorkflowReturnMapping map where workFlowMaster.workflowId=:workflowMasterId and active=1")
	List<WorkflowReturnMapping> fetchWorkFlowDtoData(@Param("workflowMasterId") Long workflowMasterId);

	@Query("select new com.iris.model.WorkflowReturnMapping(map.workflowId, map.workFlowMaster.workflowId, map.returnIdFk.returnId, map.channelIdFk.uploadChannelId, map.workFlowMaster.workFlowName) " + " FROM WorkflowReturnMapping map where active=1")
	List<WorkflowReturnMapping> fetchWorkFlowDtoData();
}