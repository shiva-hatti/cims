package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.AutoCalVersionMap;

public interface AutoCalVersionMapRepo extends JpaRepository<AutoCalVersionMap, Long> {

	@Query("FROM AutoCalVersionMap acvm where acvm.isActive = 1 and acvm.returnTempIdFk.returnTemplateId =:returnTemplateId")
	AutoCalVersionMap findByReturnTemplateId(@Param("returnTemplateId") Long returnTemplateId);

	@Query(value = "FROM TBL_AUTO_CAL_VERSION_MAP where IS_ACTIVE = 1 and RETURN_TEMPLATE_ID_FK in (select RETURN_TEMPLATE_ID from FROM TBL_RETURN_TEMPLATE WHERE RETURN_ID_FK = :returnId order by RETURN_TEMPLATE_ID desc LIMIT 1)", nativeQuery = true)
	AutoCalVersionMap findReturnTemplateIdByReturnId(@Param("returnId") Long returnId);

}