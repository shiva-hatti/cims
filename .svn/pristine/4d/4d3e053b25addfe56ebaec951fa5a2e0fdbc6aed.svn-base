package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.UserRoleMaster;

public interface UserRoleMasterRepo extends JpaRepository<UserRoleMaster, Long> {

	@Query("from UserRoleMaster userRoleMaster where userRoleMaster.userRole.userRoleId IN :roleIds")
	List<UserRoleMaster> findByUserRoleIds(@Param("roleIds") List<Long> roleIds);

	@Query("from UserRoleMaster userRoleMaster where userRoleMaster.userRole.userRoleId IN :roleId and userRoleMaster.userMaster.userId IN :userId")
	List<UserRoleMaster> findByUserRoleIdInAndUserIdIn(@Param("roleId") Long roleIds, @Param("userId") Long userIds);

	List<UserRoleMaster> findByUserMasterUserId(Long userId);
}
