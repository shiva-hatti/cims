package com.iris.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.dto.UserDto;
import com.iris.model.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Long> {

	List<UserMaster> findByUserIdIn(@Param("userId") Long[] userIds);

	UserMaster findByUserId(@Param("userId") Long userId);

	@Query("select UM.userName from UserRole UR " + " left join UserRoleMaster URM on UR.userRoleId = URM.userRole.userRoleId" + " left join UserMaster UM on UM.userId = URM.userMaster.userId " + " where UR.roleDesc = :roleDesc")
	List<String> getDataByName(@Param("roleDesc") String roleDesc);

	List<UserMaster> getDataByPrimaryEmailInAndIsActiveTrue(List<String> emailIds);

	List<UserMaster> getDataByUserNameInAndIsActiveTrue(List<String> userNamwe);

	boolean existsByUserName(String userName);

	UserMaster findByUserIdAndIsActiveTrue(Long id);

	@Query("FROM UserMaster um where um.isActive = 1 and um.userName in(:userNameList)")
	List<UserMaster> getDataByUserName(@Param("userNameList") List<String> userNameList);

	@Query("SELECT um.userName FROM UserMaster um where um.isActive = 1 and um.userId =:userId")
	String getUserNameByUserId(@Param("userId") Long userId);

	List<UserMaster> findByUserIdInAndIsActiveIn(List<Long> userIds, List<Boolean> isActive);

	List<UserMaster> findByDepartmentIdFkRegulatorIdInAndIsActiveTrue(List<Long> regulatorId);

	@Query("FROM UserMaster um where um.isActive = 1 and um.roleType.roleTypeId = 3")
	List<UserMaster> getAuditorList();

	@Query("select UM.userId from UserRole UR " + " left join UserRoleMaster URM on UR.userRoleId = URM.userRole.userRoleId" + " left join UserMaster UM on UM.userId = URM.userMaster.userId " + " where UR.userRoleId = :userRoleId")
	List<Long> getUserMasterList(@Param("userRoleId") Long userId);

	@Query("FROM UserMaster um where um.isActive = 1 and um.roleType.roleTypeId =:roleTypeId and um.departmentIdFk.regulatorId =:regulatorId and um.roleType.isActive = 1 and um.departmentIdFk.isActive = 1")
	List<UserMaster> fetchUserListByRoleAndDeptId(@Param("roleTypeId") Long roleTypeId, @Param("regulatorId") Long regulatorId);

	@Query("FROM UserMaster um where um.isActive = 1 and um.roleType.roleTypeId =:roleTypeId")
	List<UserMaster> getUserListByRoleId(@Param("roleTypeId") Long roleTypeId);

	@Query("FROM UserMaster um where um.isActive = 1 and um.userName =:userName")
	UserMaster getUserDetails(@Param("userName") String userName);

	@Query("SELECT new com.iris.dto.UserDto(mas.userId, mas.userName, dep.regulatorId, dep.isMaster, roleType.roleTypeId)" + " FROM UserMaster mas left join " + " RoleType roleType " + " on mas.roleType.roleTypeId = roleType.roleTypeId left join " + " Regulator dep on mas.departmentIdFk.regulatorId = dep.regulatorId and dep.isActive = 1" + " where mas.userId =:userId and mas.isActive = 1 ")
	UserDto getLighWeightUserDto(Long userId);

	@Query("SELECT new com.iris.dto.UserDto(mas.userId, mas.userName, mas.roleType.roleTypeId, usrRole.userRoleId, " + "  entityRole.entityBean.entityId, act.workFlowActivity.activityId) " + " FROM UserMaster mas left join UserRoleMaster usrRoleMas on usrRoleMas.userMaster.userId = mas.userId " + " left join UserRole usrRole on usrRole.userRoleId = usrRoleMas.userRole.userRoleId left join" + " UserEntityRole entityRole " + " on usrRoleMas.userRoleMasterId = entityRole.userRoleMaster.userRoleMasterId left join " + " UserRoleActivityMap act " + " on act.role.userRoleId = usrRole.userRoleId" + " where mas.userId =:userId and usrRole.userRoleId =:roleId and mas.isActive = 1 " + " and entityRole.isActive = 1 and act.isActive = 1 and entityRole.entityBean.isActive = 1 " + " and usrRoleMas.isActive = 1 and usrRole.isActive = 1")
	List<UserDto> getUserDetailForEbrFileUpload(@Param("userId") Long userId, @Param("roleId") Long roleId);

	@Query("FROM UserMaster um where um.userName in(:userNameList)")
	List<UserMaster> findAllUserDetails(@Param("userNameList") List<String> userNameList);

	@Query("FROM UserMaster um where um.userId =:userId")
	UserMaster findByUserIdDeatils(@Param("userId") Long userId);

	@Query("SELECT DISTINCT user FROM UserRoleMaster usrRoleMast, UserMaster user WHERE usrRoleMast.userRole.userRoleId IN (:userRoleIds) AND " + "usrRoleMast.userMaster.userId = user.userId AND user.roleType.roleTypeId=:roleTypeId AND " + "usrRoleMast.isActive=1 order by user.lastUpdatedOn DESC")
	List<UserMaster> getMainDeptUserList(@Param("roleTypeId") Long roleTypeId, @Param("userRoleIds") List<Long> userRoleIds);

	@Query("SELECT DISTINCT user FROM UserRoleMaster usrRoleMast, UserMaster user WHERE usrRoleMast.userRole.userRoleId IN (:userRoleIds) AND " + "usrRoleMast.userMaster.userId = user.userId AND user.roleType.roleTypeId=:roleTypeId AND " + "usrRoleMast.isActive=1 AND user.departmentIdFk.regulatorId=:loggedInUserDeptId order by user.lastUpdatedOn DESC")
	List<UserMaster> getNonMainDeptUserList(@Param("roleTypeId") Long roleTypeId, @Param("userRoleIds") List<Long> userRoleIds, @Param("loggedInUserDeptId") Long loggedInUserDeptId);

	@Query("SELECT DISTINCT um.userId FROM UserMaster um where um.roleType.roleTypeId =:roleTypeId and um.userId IN(:userIdsSet)")
	Set<Long> isRegulatorForRolesCreatedByUsers(@Param("roleTypeId") Long roleTypeId, @Param("userIdsSet") Set<Long> userIdsSet);
}
