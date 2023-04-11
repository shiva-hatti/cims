package com.iris.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ReturnTableMap;

public interface ReturnTableMapRepo extends JpaRepository<ReturnTableMap, Long> {

	@Query(value = "select new com.iris.model.ReturnTableMap(rtm.Id,rtm.tableCode,rtm.tableName,rtm.isActive,rtm.isOptional,rtm.isBusinessRuleApplication,rtm.createdOn,rtm.lastModifiedOn) from " + "ReturnTableMap rtm," + " Return ret" + " where rtm.returnIdFk.returnCode =:returnCode" + " and rtm.isActive =:isActive")
	Set<ReturnTableMap> getListOfTableCodes(@Param("returnCode") String returnCode, @Param("isActive") boolean isActive);

	@Query(value = "From ReturnTableMap where isActive = 1 and returnIdFk.returnId in (:ids)")
	List<ReturnTableMap> findByIdAndIsActiveTrue(@Param("ids") Long[] ids);

}
