package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.model.UserRolePlatformMapModified;

public interface RolePlatMapModRepo extends JpaRepository<UserRolePlatformMapModified, Long> {

}
