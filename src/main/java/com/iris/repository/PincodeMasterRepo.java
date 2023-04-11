package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.iris.model.PincodeMaster;

public interface PincodeMasterRepo extends JpaRepository<PincodeMaster, Long> {
	@Query(value = "FROM PincodeMaster where isActive = 1 and cityIdFk.isActive = 1 order by pincode ASC")
	List<PincodeMaster> findAllActiveData();

	@Query(value = "FROM PincodeMaster where cityIdFk.id = :cityId and isActive = 1 order by pincode ASC")
	List<PincodeMaster> getpincodeListByCity(@Param("cityId") Long cityId);

	@Query(value = "FROM PincodeMaster where cityIdFk.cityName = :cityName and isActive = 1 order by pincode ASC")
	List<PincodeMaster> getpincodeListByCityName(@Param("cityName") String cityName);
}
