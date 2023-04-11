/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.WorkFlowMasterBean;

/**
 * @author Shivabasava Hatti
 *
 */
public interface WorkFlowMasterRepo extends JpaRepository<WorkFlowMasterBean, Long> {

	@Query(value = "FROM WorkFlowMasterBean where isActive = '1' order by workflowId asc")
	List<WorkFlowMasterBean> getWorkflowNameList();

	@Query(value = "FROM WorkFlowMasterBean where isActive = '1' and workflowId=:workflowId")
	WorkFlowMasterBean getWorkflowDataById(@Param("workflowId") Long workflowId);

	List<WorkFlowMasterBean> findByWorkflowIdIn(List<Long> workflowIds);
}
