package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.dto.UserRolePlatformDto;
import com.iris.model.UserRoleLabel;

public interface UserRoleLabelRepo extends JpaRepository<UserRoleLabel, Long> {

	@Query("from UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId=:roleTypeId AND langIdFk.languageId=:langId AND userRoleIdFk.createdByRole.user.userId IN " + "(SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId)")
	List<UserRoleLabel> getAllUserRoleForLoggedInUser(@Param("roleTypeId") Long roleTypeId, @Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("langId") Long langId);

	@Query("from UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId=:roleTypeId AND langIdFk.languageId=:langId AND userRoleIdFk.isActive=1 AND userRoleIdFk.user.userId IN " + "(SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId) order by userRoleLabel")
	List<UserRoleLabel> getAllUserRoleForLoggedInUserIsActive(@Param("roleTypeId") Long roleTypeId, @Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("langId") Long langId);

	@Query("FROM UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND langIdFk.languageCode=:langCode AND userRoleIdFk.isActive=1 " + "order by UPPER(userRoleLabel)")
	List<UserRoleLabel> getAllActiveRoles(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(roleLabel.userRoleIdFk.userRoleId, roleLabel.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) from UserRoleLabel roleLabel, UserRolePlatFormMap platMap" + " WHERE roleLabel.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) " + "AND roleLabel.langIdFk.languageCode=:langCode AND roleLabel.userRoleIdFk.isActive=1 " + " and platMap.userRole.userRoleId =  roleLabel.userRoleIdFk.userRoleId" + " order by UPPER(roleLabel.userRoleLabel)")
	List<UserRolePlatformDto> getRolePlatFormMapping(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("FROM UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND userRoleIdFk.isActive = 1 AND langIdFk.languageCode=:langCode AND " + "userRoleIdFk.user.userId IN (SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId IN (:loggedInUserDeptId)) " + "order by UPPER(userRoleLabel)")
	List<UserRoleLabel> getDeptActiveUserRoleForEditUser(@Param("loggedInUserDeptId") List<Long> loggedInUserDeptId, @Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId," + " userRoleLbl.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) " + " FROM UserRoleLabel userRoleLbl,  UserRolePlatFormMap platMap" + "  WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId " + " IN (:roleTypeId) AND userRoleLbl.userRoleIdFk.isActive = 1 AND platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " AND userRoleLbl.langIdFk.languageCode=:langCode AND " + " " + " userRoleLbl.userRoleIdFk.user.userId IN (SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId IN (:loggedInUserDeptId)) " + " " + "order by UPPER(userRoleLbl.userRoleLabel)")
	List<UserRolePlatformDto> getDeptActiveUserRoleAndPlatFormForEditUser(@Param("loggedInUserDeptId") List<Long> loggedInUserDeptId, @Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("FROM UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND userRoleIdFk.isActive = 1 AND langIdFk.languageCode=:langCode AND " + "userRoleIdFk.user.userId IN (SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId) " + "order by UPPER(userRoleLabel)")
	List<UserRoleLabel> getDeptActiveUserRoleForAddUser(@Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("FROM UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND userRoleIdFk.isActive = 1 AND langIdFk.languageCode=:langCode " + "order by UPPER(userRoleLabel)")
	List<UserRoleLabel> getAudActiveUserRoleForAddUser(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("SELECT DISTINCT userRoleLbl FROM UserRoleLabel userRoleLbl WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND " + "userRoleLbl.userRoleIdFk.isActive = 1 AND userRoleLbl.langIdFk.languageCode=:langCode AND userRoleLbl.userRoleIdFk.user.userId IN " + "(SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId) AND userRoleLbl.userRoleIdFk.userRoleId IN " + "(SELECT userRole.userRoleId FROM UserRoleEntityMapping where entity.entityCode =:entityCode AND isActive = 1) order by UPPER(userRoleLbl.userRoleLabel)")
	List<UserRoleLabel> getEntActiveUserRoleForAddUserMainDept(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode, @Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("entityCode") String entityCode);

	@Query("FROM UserRoleLabel WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) AND userRoleIdFk.isActive = 1 AND langIdFk.languageCode=:langCode AND " + "userRoleIdFk.user.userId IN (SELECT user.userId FROM UserMaster user, UserRoleMaster usrRoleMast, UserEntityRole entityRole WHERE " + "usrRoleMast.userMaster.userId = user.userId AND usrRoleMast.userRole.isActive = 1 AND " + "usrRoleMast.userRoleMasterId = entityRole.userRoleMaster.userRoleMasterId AND entityRole.entityBean.entityCode = :entityCode AND " + "entityRole.entityBean.isActive = 1) order by UPPER(userRoleLabel)")
	List<UserRoleLabel> getEntActiveUserRoleForAddUserEntUsr(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode, @Param("entityCode") String entityCode);

	@Query("FROM UserRoleLabel userRoleLbl WHERE userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) " + " AND userRoleLbl.userRoleIdFk.userRoleId " + " IN(SELECT userRole.userRoleId FROM UserRoleMaster WHERE userMaster.userId=:loggedInUserId AND isActive = 1) " + " AND userRoleLbl.userRoleIdFk.isActive = 1 AND userRoleLbl.langIdFk.languageCode=:langCode")
	List<UserRoleLabel> getAllAssignedRoles(@Param("roleTypeId") List<Long> roleTypeId, @Param("loggedInUserId") Long loggedInUserId, @Param("langCode") String langCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId," + " userRoleLbl.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) " + " FROM UserRoleLabel userRoleLbl,  UserRolePlatFormMap platMap " + " WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) " + " AND userRoleLbl.userRoleIdFk.userRoleId " + " IN(SELECT userRole.userRoleId FROM UserRoleMaster " + " WHERE userMaster.userId=:loggedInUserId AND isActive = 1) " + " AND userRoleLbl.userRoleIdFk.isActive = 1 and platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " AND userRoleLbl.langIdFk.languageCode=:langCode")
	List<UserRolePlatformDto> getAllAssignedRolesAndPlatForm(@Param("roleTypeId") List<Long> roleTypeId, @Param("loggedInUserId") Long loggedInUserId, @Param("langCode") String langCode);

	@Query("SELECT DISTINCT new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId, userRoleLbl.userRoleLabel, " + " platMap.platForm.platFormId, platMap.platForm.platFormKey)" + " FROM UserRoleLabel userRoleLbl, UserRolePlatFormMap platMap " + " WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) " + " AND " + "userRoleLbl.userRoleIdFk.isActive = 1 " + " AND userRoleLbl.langIdFk.languageCode=:langCode AND userRoleLbl.userRoleIdFk.user.userId " + " IN " + "(SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId) " + " AND userRoleLbl.userRoleIdFk.userRoleId IN " + "" + " (SELECT userRole.userRoleId FROM UserRoleEntityMapping where entity.entityCode =:entityCode AND isActive = 1) " + " AND platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " order by UPPER(userRoleLbl.userRoleLabel)")
	List<UserRolePlatformDto> getEntActiveUserRoleAndPlatformForAddUserMainDept(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode, @Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("entityCode") String entityCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId," + " userRoleLbl.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) " + " FROM UserRoleLabel userRoleLbl, UserRolePlatFormMap platMap  WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId)" + "  AND userRoleLbl.userRoleIdFk.isActive = 1 AND userRoleLbl.langIdFk.languageCode=:langCode AND " + "" + " userRoleLbl.userRoleIdFk.user.userId IN (SELECT userId FROM UserMaster WHERE departmentIdFk.regulatorId=:loggedInUserDeptId) " + "" + " AND platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " order by UPPER(userRoleLbl.userRoleLabel)")
	List<UserRolePlatformDto> getDeptActiveUserRoleAndPlatformForAddUser(@Param("loggedInUserDeptId") Long loggedInUserDeptId, @Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId," + " userRoleLbl.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) " + "FROM UserRoleLabel userRoleLbl, UserRolePlatFormMap platMap" + " WHERE userRoleLbl.userRoleIdFk.roleType.roleTypeId IN (:roleTypeId) " + " AND userRoleLbl.userRoleIdFk.isActive = 1 AND userRoleLbl.langIdFk.languageCode=:langCode AND " + " " + "userRoleLbl.userRoleIdFk.user.userId IN (SELECT user.userId FROM UserMaster user, " + " UserRoleMaster usrRoleMast, UserEntityRole entityRole WHERE " + " " + "usrRoleMast.userMaster.userId = user.userId AND usrRoleMast.userRole.isActive = 1 AND" + "  " + "usrRoleMast.userRoleMasterId = entityRole.userRoleMaster.userRoleMasterId " + " AND entityRole.entityBean.entityCode = :entityCode AND " + "entityRole.entityBean.isActive = 1) " + " AND platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " order by UPPER(userRoleLbl.userRoleLabel)")
	List<UserRolePlatformDto> getEntActiveUserRoleAndPlatformForAddUserEntUsr(@Param("roleTypeId") List<Long> roleTypeId, @Param("langCode") String langCode, @Param("entityCode") String entityCode);

	@Query("select new com.iris.dto.UserRolePlatformDto(userRoleLbl.userRoleIdFk.userRoleId," + " userRoleLbl.userRoleLabel, platMap.platForm.platFormId, platMap.platForm.platFormKey) " + " FROM UserRoleLabel userRoleLbl,  UserRolePlatFormMap platMap " + " WHERE userRoleLbl.userRoleIdFk.userRoleId IN (:roleIds) " + " AND platMap.userRole.userRoleId =  userRoleLbl.userRoleIdFk.userRoleId" + " AND userRoleLbl.langIdFk.languageCode=:langCode")
	List<UserRolePlatformDto> getAlottedRolesAndPlatForm(@Param("roleIds") List<Long> roleIds, @Param("langCode") String langCode);
}
