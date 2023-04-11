package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.UserAuditorRole;

public interface UserAuditorRoleRepo extends JpaRepository<UserAuditorRole, Long> {

	@Query("FROM UserAuditorRole where isActive = 1")
	List<UserAuditorRole> getAuditorList();

}
