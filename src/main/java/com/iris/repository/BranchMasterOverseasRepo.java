package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.BranchMasterOverseas;

public interface BranchMasterOverseasRepo extends JpaRepository<BranchMasterOverseas, Long> {

	@Query(value = " FROM BranchMasterOverseas bmd where bmd.countryIdFk.countryCode =:countryCode and bmd.bankCode =:entityCode and bmd.isActive = 1")
	List<BranchMasterOverseas> getBranchMasterByCountryCode(@Param("countryCode") String countryCode, @Param("entityCode") String entityCode);

	//Added By psahoo

	@Query(value = " FROM BranchMasterOverseas bmo where bmo.isActive = 1 and bmo.bankCode =:bankCode and bmo.countryIdFk.isActive = 1")
	List<BranchMasterOverseas> getCountryBranchMasterListByEntityCode(@Param("bankCode") String bankCode);

}
