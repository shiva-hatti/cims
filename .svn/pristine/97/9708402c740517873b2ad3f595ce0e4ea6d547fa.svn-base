
package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.ReturnGroupLblMod;

/**
 * @author pmohite
 *
 */
public interface ReturnGroupLabelModRepo extends JpaRepository<ReturnGroupLblMod, Long> {

	@Query(value = "SELECT new com.iris.model.ReturnGroupLblMod(rglm.returngroupLblModId, rglm.rtnGroupLabelName, rglm.actionIdFK, usr.userName, retGroup.createdOn, retGroup.isActive, usr1.userName, rglm.modifiedOn, rglm.langIdFk.languageName) FROM ReturnGroupLblMod rglm \r\n" + "left join  ReturnGroupLabelMapping groupLable on groupLable.returnGroupLabelMapId = rglm.rtnGroupLabel.returnGroupLabelMapId left join ReturnGroupMapping retGroup on retGroup.returnGroupMapId = groupLable.returnGroupMapIdFk.returnGroupMapId left join UserMaster usr on usr.userId = retGroup.createdBy.userId left join UserMaster usr1 on usr1.userId = rglm.userModify.userId where groupLable.returnGroupLabelMapId =:returnGroupLabelMapId order by rglm.modifiedOn desc")
	List<ReturnGroupLblMod> getDataByReturnGroupLabelId(Long returnGroupLabelMapId, org.springframework.data.domain.Pageable pagebPageable);
}
