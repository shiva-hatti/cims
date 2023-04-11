package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.EntUsrInfoMapping;

public interface EntUserInfoMappingRepo extends JpaRepository<EntUsrInfoMapping, Long> {

	@Query("FROM EntUsrInfoMapping where entityIdFk.entityId=:entityId")
	EntUsrInfoMapping getEntUserInfoMappingByEntId(@Param("entityId") Long entityId);

}
