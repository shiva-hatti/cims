package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.dto.FilingApprovalNotificationBean;
import com.iris.model.WorkFlowActivity;

public interface WorkFlowActivityRepo extends JpaRepository<WorkFlowActivity, Long> {

	List<WorkFlowActivity> findByIsApplicableForEntityTrue();

	List<WorkFlowActivity> findByIsApplicableForDeptTrue();

	List<WorkFlowActivity> findByIsActiveTrue();

	@Query("SELECT new com.iris.dto.FilingApprovalNotificationBean(wk.activityId, userRole.userRoleId, userRole.roleType.roleTypeId, usr.userId, " + " usr.userName,ent.entityId, ent.entityCode, ent.entityName)  FROM WorkFlowActivity wk " + " left join UserRoleActivityMap roleAct on wk.activityId = roleAct.workFlowActivity.activityId " + " left join UserRole userRole on roleAct.role.userRoleId = userRole.userRoleId  left join " + " UserRoleMaster roleMaster on roleMaster.userRole.userRoleId = userRole.userRoleId left join " + " UserMaster usr on roleMaster.userMaster.userId = usr.userId  left join UserEntityRole entRole" + " on roleMaster.userRoleMasterId = entRole.userRoleMaster.userRoleMasterId and entRole.isActive = 1 " + " left join EntityBean ent on ent.entityId = entRole.entityBean.entityId and ent.isActive = 1 " + " where wk.activityId =:activityId  and wk.isActive = 1 and roleAct.isActive = 1 " + " and usr.isActive = 1 and userRole.isActive = 1 and roleMaster.isActive = 1")
	List<FilingApprovalNotificationBean> getFillingNotificationData(@Param("activityId") Long activityId);
}
