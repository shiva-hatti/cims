/**
 * 
 */
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.GroupMasterBulk;

/**
 * @author Siddique
 *
 */
public interface GroupMasterBulkRepo extends JpaRepository<GroupMasterBulk, Long> {

	@Query("From GroupMasterBulk groupMasterBulk, EntityBean entityBean, EntityLabelBean entityLabelBean, LanguageMaster languageMaster where " + " groupMasterBulk.entityBean.entityId = entityBean.entityId and entityBean.entityId = entityLabelBean.entityBean.entityId and entityLabelBean.languageMaster.languageId = languageMaster.languageId " + " and entityBean.isActive = 1 and languageMaster.isActive = 1 and entityBean.entityCode =:entityCode and languageMaster.languageCode =:langCode order by groupMasterBulk.createdOn desc ")
	List<GroupMasterBulk> getDataByEntityCodeAndLangCode(String entityCode, String langCode);

	@Query(value = "From GroupMasterBulk gmb where gmb.isActive = 1 and gmb.isProcessed = 0 and gmb.statusId.panStatusId = 1 order by gmb.id asc ")
	//	@Query(value ="From GroupMasterBulk gmb where gmb.id = 1")
	List<GroupMasterBulk> getUnprocessedData();

}
