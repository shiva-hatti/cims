
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ReturnGroupMapping;

/**
 * @author bthakare
 *
 */
public interface ReturnGroupMappingRepo extends JpaRepository<ReturnGroupMapping, Long> {
	/*
	 * @Query(value = "FROM ReturnGroupMapping where returnGroupMapId=:id")
	 * ReturnGroupMapping getDataById(@Param("id") Long id);
	 */

	@Query(value = "select * FROM TBL_RETURN_GROUP_MAPPING rgm where rgm.RETURN_GROUP_MAP_ID=:id ", nativeQuery = true)
	ReturnGroupMapping getDataById(Long id);

	@Query(value = "select * FROM TBL_RETURN_GROUP_MAPPING rgm where rgm.IS_ACTIVE = '1' order by RETURN_GROUP_MAP_ID desc", nativeQuery = true)
	List<ReturnGroupMapping> getAllActiveData();

	@Query(value = "select * FROM TBL_RETURN_GROUP_MAPPING rgm where rgm.IS_ACTIVE = '1' and rgm.IS_CROSS_VALIDATION = '1'", nativeQuery = true)
	List<ReturnGroupMapping> getSetList();

	@Query("SELECT new com.iris.model.ReturnGroupMapping(retGroup.returnGroupMapId, usr.userName, retGroup.createdOn, usr1.userName, retGroup.lastModifiedOn, groupLable.groupLabel, retGroup.isActive, count(ret.returnGroupMapIdFk.returnGroupMapId)) FROM ReturnGroupMapping retGroup left join  ReturnGroupLabelMapping groupLable on groupLable.returnGroupMapIdFk.returnGroupMapId = retGroup.returnGroupMapId left join Return ret  on ret.returnGroupMapIdFk.returnGroupMapId = retGroup.returnGroupMapId and ret.isActive = '1' left join UserMaster usr on usr.userId = retGroup.createdBy.userId left join UserMaster usr1 on usr1.userId = retGroup.userModify.userId where groupLable.langIdFk.languageCode = :langCode group by retGroup.returnGroupMapId, groupLable.groupLabel order by retGroup.lastUpdateOn desc")
	List<ReturnGroupMapping> getReturnGroupList(@Param("langCode") String langCode);

}
