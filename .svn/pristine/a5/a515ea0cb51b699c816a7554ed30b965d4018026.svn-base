package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.Platform;

public interface PortalRepo extends JpaRepository<Platform, Long> {

	List<Platform> findByIsActiveTrue();

	@Query("FROM Platform where platFormCode=:code")
	Platform getDataByCode(@Param("code") String code);

	@Query("select plt.platFormId from Platform plt where plt.platFormCode IN :codeList")
	List<Long> getDataByPortalCode(@Param("codeList") List<String> codeList);
}
