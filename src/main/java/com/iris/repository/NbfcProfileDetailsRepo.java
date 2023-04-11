package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.NbfcProfileDetailsBean;

/**
 * @author BHAVANA
 *
 */
public interface NbfcProfileDetailsRepo extends JpaRepository<NbfcProfileDetailsBean, Long> {

	@Query("FROM NbfcProfileDetailsBean where entityBean.entityCode = :entityCode and isActive = 1" + " and createdOn IN(Select MAX(createdOn) from NbfcProfileDetailsBean where entityBean.entityCode = :entityCode and isActive = 1)" + " ORDER BY createdOn DESC")
	NbfcProfileDetailsBean getNbfcProfileDetails(@Param("entityCode") String entityCode);

}
