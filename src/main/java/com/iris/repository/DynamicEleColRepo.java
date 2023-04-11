/**
 * 
 */
package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.DynamicEleCol;

/**
 * @author Siddique
 *
 */
public interface DynamicEleColRepo extends JpaRepository<DynamicEleCol, Long> {

	@Query("select dynamicUrl from DynamicEleCol where fieldTypeIdfK.fieldTypeId = 2 and defaultFieldName =:val")
	String getDyanmicUrl(@Param("val") String val);

}
