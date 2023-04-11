package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.EntityAuditorMapMod;

public interface EntityAuditorMapModRepo extends JpaRepository<EntityAuditorMapMod, Long> {
	@Query(value = "SELECT * FROM TBL_ENTITY_AUDITOR_MAPPING_MOD  WHERE ENTITY_AUDITOR_MAP_ID_FK= :entityAudMapId ORDER BY LAST_MODIFIED_ON DESC LIMIT 1", nativeQuery = true)
	EntityAuditorMapMod getPreviousEntityAuditorMap(@Param("entityAudMapId") Long entityAudMapId);

}
