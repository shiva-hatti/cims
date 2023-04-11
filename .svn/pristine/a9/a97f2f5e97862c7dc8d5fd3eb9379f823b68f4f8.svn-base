package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.UserRolePlatFormMap;

/**
 * 
 * @author svishwakarma
 *
 */
public interface UserRolePortalMapRepo extends JpaRepository<UserRolePlatFormMap, Long> {

	public List<UserRolePlatFormMap> findByIsActiveTrue();

	public List<UserRolePlatFormMap> findAllByUserRoleUserRoleIdAndIsActiveTrue(Long userRoleId);

	public List<UserRolePlatFormMap> findAllByUserRoleUserRoleId(Long userRoleId);

	@Modifying
	@Query("update UserRolePlatFormMap u set u.isActive = false where u.userRole.userRoleId=:userRoleId")
	void cancelRolePortalMap(@Param("userRoleId") Long userRoleId);

	@Query(value = "FROM UserRolePlatFormMap userPlat where userPlat.userRole.userRoleId in (:userRoleIds)")
	List<UserRolePlatFormMap> getDataByRoleIdsInAndIsActiveTrue(@Param("userRoleIds") List<Long> userRoleIds);

	@Query("from UserRolePlatFormMap urpm where urpm.isActive = 1 and urpm.platForm.isActive = 1 and urpm.userRole.isActive = 1 and urpm.userRole.userRoleId IN" + "(Select urm.userRole.userRoleId from UserRoleMaster urm where UPPER(urm.userMaster.userName) = UPPER(:userName) and urm.userRole.isActive = 1 and urm.isActive = 1 and urm.userMaster.isActive = 1)")
	List<UserRolePlatFormMap> findBydPlatformLByUserName(@Param("userName") String userName);

}
