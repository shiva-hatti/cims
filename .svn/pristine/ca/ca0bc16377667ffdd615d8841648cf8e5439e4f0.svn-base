
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ReturnGroupLabelMapping;

/**
 * @author pmohite
 *
 */
public interface ReturnGroupLabelRepo extends JpaRepository<ReturnGroupLabelMapping, Long> {

	@Query("SELECT new com.iris.model.ReturnGroupLabelMapping(returnGroupLabelMapId, groupLabel) FROM ReturnGroupLabelMapping where UPPER(groupLabel)=:returnGroupName")
	List<ReturnGroupLabelMapping> checkReturnGroupNameExist(@Param("returnGroupName") String returnGroupName);

	@Query("SELECT new com.iris.model.ReturnGroupLabelMapping(groupLable.returnGroupLabelMapId, groupLable.groupLabel, retGroup.returnGroupMapId, retGroup.defaultGroupName, retGroup.isActive, usr.userId, usr.userName, retGroup.createdOn, usr1.userId, usr1.userName, groupLable.lastModifiedOn, groupLable.langIdFk.languageName) FROM ReturnGroupLabelMapping groupLable left join ReturnGroupMapping retGroup on retGroup.returnGroupMapId = groupLable.returnGroupMapIdFk.returnGroupMapId left join UserMaster usr on usr.userId = retGroup.createdBy.userId left join UserMaster usr1 on usr1.userId = groupLable.userModify.userId where groupLable.langIdFk.languageCode=:langCode order by groupLable.lastUpdateOn desc")
	List<ReturnGroupLabelMapping> getReturnGroupLabelList(@Param("langCode") String langCode);

	@Query("SELECT new com.iris.model.ReturnGroupLabelMapping(returnGroupLabelMapId, groupLabel) FROM ReturnGroupLabelMapping where UPPER(groupLabel)=:returnGroupName and langIdFk.languageCode=:langCode")
	List<ReturnGroupLabelMapping> checkReturnGroupLabelExist(@Param("returnGroupName") String returnGroupName, @Param("langCode") String langCode);

	@Query(value = "FROM ReturnGroupLabelMapping where returnGroupLabelMapId =:id ")
	ReturnGroupLabelMapping getDataById(Long id);

}
