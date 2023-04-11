package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.iris.model.NbfcProfileDetailsTempBean;

public interface NbfcProfileDetailsTempRepo extends JpaRepository<NbfcProfileDetailsTempBean, Long> {
	@Query("FROM NbfcProfileDetailsTempBean where entityBean.entityId = :entityId and isActive = 1 and isSubmitted = 0 and pageNumber = :pageNo")
	NbfcProfileDetailsTempBean getNbfcProfileDetailsTemp(@Param("entityId") Long entityId, @Param("pageNo") Long pageNumber);

	@Query("FROM NbfcProfileDetailsTempBean where entityBean.entityId = :entityId and isActive = 1 and isSubmitted = 0 and pageNumber in(:pageNumber)")
	List<NbfcProfileDetailsTempBean> getNbfcProfileDetailsTempUponPageNoList(@Param("entityId") Long entityId, @Param("pageNumber") List<Long> pageNumberList);

	@Query("FROM NbfcProfileDetailsTempBean where entityBean.entityId = :entityId and pageNumber = :pageNo")
	NbfcProfileDetailsTempBean getNbfcProfileDetailsForNbfc(@Param("entityId") Long entityId, @Param("pageNo") Long pageNumber);

}
