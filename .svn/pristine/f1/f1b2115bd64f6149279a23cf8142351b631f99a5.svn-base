package com.iris.sdmx.element.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.sdmx.element.entity.SdmxElementRegulatorEntity;

public interface SdmxElementRegulatorRepo extends JpaRepository<SdmxElementRegulatorEntity, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxElementRegulatorEntity where isActive =:isActive")
	List<SdmxElementRegulatorEntity> findByActiveStatus(Boolean isActive);

	@Query(value = "SELECT new com.iris.sdmx.element.entity.SdmxElementRegulatorEntity(regulator.regulatorId, regLabel.regulatorLabel, regulator.regulatorCode)  FROM RegulatorLabel regLabel , SdmxElementRegulatorEntity regulator where regLabel.langIdFk.languageId = :languageId" + " and regLabel.regulatorIdFk.regulatorId = regulator.regulatorId and regulator.isActive=true")
	List<SdmxElementRegulatorEntity> findRegulatorLabelByIsactiveAndLanguageId(@Param("languageId") Long languageId);

	@Query(value = "SELECT new com.iris.sdmx.element.entity.SdmxElementRegulatorEntity(regulator.regulatorId, regLabel.regulatorLabel, regulator.regulatorCode)  FROM  SdmxElementRegulatorEntity regulator " + "join RegulatorLabel regLabel on regLabel.regulatorIdFk.regulatorId = regulator.regulatorId where regLabel.langIdFk.languageId = :languageId" + " and regLabel.regulatorIdFk.regulatorId =:regulatorId and regulator.isActive=true")
	List<SdmxElementRegulatorEntity> findByLanguageIdAndRegulatorId(@Param("languageId") Long languageId, @Param("regulatorId") Long regulatorId);

}
