package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementDependencyTypeEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxElementDependencyTypeRepo extends JpaRepository<SdmxElementDependencyTypeEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementDependencyTypeEntity where isActive =:isActive")
	List<SdmxElementDependencyTypeEntity> findByActiveStatus(Boolean isActive);

	/**
	 * @param classificationName
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementDependencyTypeEntity u WHERE u.dependencyTypeName=:dependencyTypeName")
	int isDataExistWithDependencyTypeName(String dependencyTypeName);

	/**
	 * @param classificationId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementDependencyTypeEntity u WHERE u.dependencyTypeId=:dependencyTypeId")
	int isDataExistWithId(Long dependencyTypeId);

	/**
	 * @param classificationName
	 * @return
	 */
	@Query("FROM SdmxElementDependencyTypeEntity WHERE dependencyTypeName=:dependencyTypeName")
	SdmxElementDependencyTypeEntity findByDependencyTypeName(String dependencyTypeName);

	/**
	 * @param classificationId
	 * @return
	 */
	@Query("FROM SdmxElementDependencyTypeEntity WHERE dependencyTypeId=:dependencyTypeId")
	SdmxElementDependencyTypeEntity findByDependencyTypeId(Long dependencyTypeId);

	@Query("SELECT u.dependencyTypeName FROM SdmxElementDependencyTypeEntity u WHERE u.dependencyTypeId=:dependencyTypeId")
	String findNameByDependencyId(Long dependencyTypeId);

}