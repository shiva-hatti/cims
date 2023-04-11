package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.DeptUserEntityMapping;

/**
 * @author pradnya
 */
public interface DeptUserEntityMappingRepo extends JpaRepository<DeptUserEntityMapping, Long> {

	@Query(value = "From DeptUserEntityMapping where userIdFk.userId = :userId")
	List<DeptUserEntityMapping> fetchAllActiveDataByUserId(Long userId);

	@Query(value = "From DeptUserEntityMapping where userIdFk.userId=:userId and isActive=1")
	List<DeptUserEntityMapping> getAllActiveDataByUserId(@Param("userId") Long userId);

	@Query(value = "select count(*) FROM DeptUserEntityMapping deptUserEntityMapping where deptUserEntityMapping.userIdFk.userId = :userId")
	int getMappedEntityCountForUserId(@Param("userId") Long userId);

	@Transactional
	@Modifying
	@Query(value = "Update DeptUserEntityMapping SET isActive = '0'  where userIdFk.userId In (:userId) ")
	int updateEntityOfUser(@Param("userId") Long[] userId);
}