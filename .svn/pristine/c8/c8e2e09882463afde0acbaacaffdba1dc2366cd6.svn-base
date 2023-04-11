
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.ReturnGroupMod;

/**
 * @author pmohite
 *
 */
public interface ReturnGroupModRepo extends JpaRepository<ReturnGroupMod, Long> {

	@Query(value = "SELECT new com.iris.model.ReturnGroupMod(rglm.returnGroupModId, rgl.groupLabel , rglm.actionIdFK, rglm.returnsMapped,usr.userName, rgm.createdOn, usr1.userName, rglm.modifiedOn, rglm.isActive) FROM ReturnGroupMod rglm left join  ReturnGroupMapping rgm on rgm.returnGroupMapId = rglm.returnGroupMap.returnGroupMapId left join ReturnGroupLabelMapping rgl on rgl.returnGroupMapIdFk.returnGroupMapId = rglm.returnGroupMap.returnGroupMapId left join LanguageMaster lang on lang.languageCode = rgl.langIdFk.languageCode and lang.languageCode =:langCode  left join UserMaster usr on usr.userId = rgm.createdBy.userId left join UserMaster usr1 on usr1.userId = rglm.userModify.userId where rgm.returnGroupMapId =:returnGroupMapId order by rglm.modifiedOn desc")
	List<ReturnGroupMod> getDataByReturnGroupId(Long returnGroupMapId, String langCode, org.springframework.data.domain.Pageable pagebPageable);

}
