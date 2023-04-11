/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ReturnPropertyValue;

public interface ReturnPropertyValueRepository extends JpaRepository<ReturnPropertyValue, Long> {
	@Query("FROM ReturnPropertyValue where returnProprtyValId=:returnProprtyValId")
	ReturnPropertyValue getPropValInfo(@Param("returnProprtyValId") Integer returnProprtyValId);

	@Query("FROM ReturnPropertyValue")
	List<ReturnPropertyValue> getAllData();

	ReturnPropertyValue findByReturnProprtyValId(Integer returnProprtyValId);

}
