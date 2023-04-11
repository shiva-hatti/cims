package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.CityMaster;

public interface CityMasterRepo extends JpaRepository<CityMaster, Long> {
	@Query(value = "FROM CityMaster where isActive = 1 and districtIdFk.isActive = 1 order by cityName ASC")
	List<CityMaster> findAllActiveData();

	@Query(value = " FROM CityMaster a,DistrictMaster b where a.districtIdFk.id=b.id and b.stateIdFk.stateCode = :stateCode and a.isActive = 1  order by a.cityName ASC")
	List<CityMaster> getCityListByState(@Param("stateCode") String stateCode);

	@Query(value = " FROM CityMaster a where a.districtIdFk.stateIdFk.stateName =:stateName and a.isActive = 1  and  a.districtIdFk.isActive = 1 and " + "a.districtIdFk.stateIdFk.isActive = 1 order by a.cityName ASC")
	List<CityMaster> getCityListByStateName(@Param("stateName") String stateName);
}
