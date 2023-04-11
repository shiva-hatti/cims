package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.iris.model.UserModified;

public interface UserModifiedRepo extends JpaRepository<UserModified, Long> {

	UserModified findByUserModifiedId(@Param("userModifiedId") Long userModifiedId);
}
