package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iris.model.WebServiceComponentUrl;

@Repository
public interface WebServiceComponentUrlRepo extends JpaRepository<WebServiceComponentUrl, Long> {

	public List<WebServiceComponentUrl> findByComponentTypeInAndUrlHttpMethodTypeInAndIsActiveTrue(List<String> componentTypeList, List<String> urlHttpMethodTypeList);

	@Query("FROM WebServiceComponentUrl where componentUrlId =:id")
	WebServiceComponentUrl getDataById(@Param("id") Long id);

}
