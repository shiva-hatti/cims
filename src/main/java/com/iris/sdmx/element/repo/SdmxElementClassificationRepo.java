package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.element.entity.SdmxElementClassificationEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxElementClassificationRepo extends JpaRepository<SdmxElementClassificationEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementClassificationEntity where isActive =:isActive")
	List<SdmxElementClassificationEntity> findByActiveStatus(Boolean isActive);

	/**
	 * @param classificationName
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementClassificationEntity u WHERE u.classificationName=:classificationName")
	int isDataExistWithClassificationName(String classificationName);

	/**
	 * @param classificationId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM SdmxElementClassificationEntity u WHERE u.classificationId=:classificationId")
	int isDataExistWithId(Long classificationId);

	/**
	 * @param classificationName
	 * @return
	 */
	@Query("FROM SdmxElementClassificationEntity WHERE classificationName=:classificationName")
	SdmxElementClassificationEntity findByClassificationName(String classificationName);

	/**
	 * @param classificationId
	 * @return
	 */
	@Query("FROM SdmxElementClassificationEntity WHERE classificationId=:classificationId")
	SdmxElementClassificationEntity findByClassificationId(Long classificationId);

	@Query("SELECT u.classificationName FROM SdmxElementClassificationEntity u WHERE u.classificationId=:classificationId")
	String findNameByClassificationId(Long classificationId);

}
