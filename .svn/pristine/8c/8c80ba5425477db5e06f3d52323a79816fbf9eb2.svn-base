package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.RoleType;

public interface RoleTypeRepo extends JpaRepository<RoleType, Long> {

	List<RoleType> findByIsActiveTrue();

	@Query(value = "select distinct (TUT.ROLE_TYPE_ID) as ROLE_TYPE_ID,TUT.ROLE_TYPE_DESC as ROLE_TYPE_DESC,TUT.IS_ACTIVE as IS_ACTIVE from TBL_USER_ROLE_MASTER AS TURM " + " left outer join TBL_USER_MASTER AS TUM ON TURM.USER_MASTER_ID_FK=TUM.USER_ID  left outer join TBL_USER_ROLE AS TUR ON TURM.USER_ROLE_ID_FK=TUR.USER_ROLE_ID " + " left outer join TBL_ROLE_TYPE AS TUT ON TUR.ROLE_TYPE_FK=TUT.ROLE_TYPE_ID WHERE TURM.IS_ACTIVE=1 " + " AND TUM.IS_ACTIVE=1 AND TUR.IS_ACTIVE=1  AND TUT.IS_ACTIVE=1  AND TUM.USER_ID=:userId", nativeQuery = true)
	List<RoleType> findByUserId(@Param("userId") Long userId);

}
