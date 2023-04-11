package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementFlowTypeEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxElementFlowTypeRepo extends JpaRepository<SdmxElementFlowTypeEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementFlowTypeEntity where isActive =:isActive")
	List<SdmxElementFlowTypeEntity> findByActiveStatus(Boolean isActive);

	/**
	 * @param flowTypeName
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementFlowTypeEntity u WHERE u.flowTypeName=:flowTypeName")
	int isDataExistWithName(String flowTypeName);

	/**
	 * @param flowTypeId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementFlowTypeEntity u WHERE u.flowTypeId=:flowTypeId")
	int isDataExistWithId(Long flowTypeId);

	/**
	 * @param flowTypeName
	 * @return
	 */
	@Query("FROM SdmxElementFlowTypeEntity WHERE flowTypeName=:flowTypeName")
	SdmxElementFlowTypeEntity findByFlowTypeName(String flowTypeName);

	/**
	 * @param flowTypeId
	 * @return
	 */
	@Query("FROM SdmxElementFlowTypeEntity WHERE flowTypeId=:flowTypeId")
	SdmxElementFlowTypeEntity findByFlowTypeId(Long flowTypeId);

	@Query("SELECT u.flowTypeName FROM SdmxElementFlowTypeEntity u WHERE u.flowTypeId=:flowTypeId")
	String findNameByFlowTypeId(Long flowTypeId);

}