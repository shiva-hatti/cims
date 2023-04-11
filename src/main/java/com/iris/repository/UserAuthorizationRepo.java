/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.UserMaster;

/**
 * @author sajadhav
 *
 */

public interface UserAuthorizationRepo extends JpaRepository<UserMaster, Long> {

	@Query("FROM UserMaster um where um.userId=:userId and um.isActive = 1")
	List<UserMaster> getDataByRefId(@Param("userId") long userId);

}
