package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.UserRegulator;

public interface UserRegulatorRepo extends JpaRepository<UserRegulator, Long> {
	@Query("FROM UserRegulator")
	List<UserRegulator> getUserRegulator();

}
