/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.dto.UserDto;
import com.iris.model.UserRole;

/**
 * @author Siddique
 *
 */
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {

	@Query(value = "FROM UserRole where isActive=1 and userRoleId =:roleId")
	UserRole findByUserRoleIdAndIsActiveTrue(@Param("roleId") Long roleId);

	@Query("from UserRole userRole where userRoleId IN :roleIds")
	List<UserRole> findByUserRoleIds(@Param("roleIds") List<Long> roleIds);

	@Query("select distinct act.workFlowActivity.activityId from UserRole usr , UserRoleActivityMap act where usr.userRoleId IN :roleIds and usr.userRoleId = act.role.userRoleId")
	List<Long> getActivityIdByRoleIds(@Param("roleIds") List<Long> roleIds);

	@Query("from UserRole userRole where userRoleId IN :roleIds")
	List<UserRole> findByUserRoleIds(@Param("roleIds") Long[] roleIds);

	List<UserRole> findByCreatedByRoleUserRoleIdAndIsActiveTrue(Long userRoleId);

	List<UserRole> getDataByRoleNameInAndIsActiveTrue(List<String> userNamwe);

	Long countByRoleNameAndIsActiveTrue(String roleName);

	List<UserRole> findByRoleName(String string);

	@Query("from UserRole where isActive=1 and roleType.roleTypeId =:roleTypeId")
	List<UserRole> fetchDeptList(@Param("roleTypeId") Long roleTypeId);

	@Query("select new com.iris.dto.UserDto(map.workFlowActivity.activityId, rl.userRoleId, usr.userId)" + "  from UserMaster usr,  UserRole rl, UserRoleMaster mas, UserRoleActivityMap map" + " where usr.userId =:userId " + " and map.workFlowActivity.activityId =:activityId and usr.userId = mas.userMaster.userId " + "" + "  and mas.userRole.userRoleId =  rl.userRoleId and  rl.userRoleId = map.role.userRoleId")
	List<UserDto> getRoleDetailsBeaseUponActivityAndUserId(@Param("userId") Long userId, @Param("activityId") Long activityId);

}
