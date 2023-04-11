/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.PanMasterBulk;

/**
 * @author Siddique
 *
 */
public interface PanMasterBulkRepo extends JpaRepository<PanMasterBulk, Long> {

	@Query(value = "From PanMasterBulk pmb where pmb.isActive = 1 and pmb.isProcessed = 0 and pmb.statusId.panStatusId = 1 order by pmb.id asc ")
	//	@Query(value ="From PanMasterBulk pmb where pmb.id = 23 ")
	List<PanMasterBulk> getUnprocessedData();

	@Query("From PanMasterBulk panMasterBulk, EntityBean entityBean, EntityLabelBean entityLabelBean, LanguageMaster languageMaster where " + " panMasterBulk.entityBean.entityId = entityBean.entityId and entityBean.entityId = entityLabelBean.entityBean.entityId and entityLabelBean.languageMaster.languageId = languageMaster.languageId " + " and entityBean.isActive = 1 and languageMaster.isActive = 1 and entityBean.entityCode =:entityCode and languageMaster.languageCode =:langCode order by panMasterBulk.createdOn desc ")
	List<PanMasterBulk> getDataByEntityCodeAndLangCode(@Param("entityCode") String entityCode, @Param("langCode") String langCode);

}
