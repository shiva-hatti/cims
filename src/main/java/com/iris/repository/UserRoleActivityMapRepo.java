package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.Menu;
import com.iris.model.UserRoleActivityMap;
import com.iris.model.WorkFlowActivity;

public interface UserRoleActivityMapRepo extends JpaRepository<UserRoleActivityMap, Long> {

	List<UserRoleActivityMap> findByRoleUserRoleIdAndIsActiveTrue(Long userRoleId);

	List<UserRoleActivityMap> findByRoleUserRoleId(Long userRoleId);

	@Transactional
	@Modifying
	@Query("update UserRoleActivityMap u set u.isActive = false where u.role.userRoleId=:userRoleId")
	void cancelUserRoleActivityMapping(@Param("userRoleId") Long userRoleId);

	@Query(value = "FROM WorkFlowActivity wa where wa.activityId in (:activityIds)")
	List<WorkFlowActivity> getActivityFromJsonIds(@Param("activityIds") List<Long> activityIds);
}
