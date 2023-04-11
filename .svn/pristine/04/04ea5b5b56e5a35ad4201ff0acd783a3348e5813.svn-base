package com.iris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.ModuleApprovalDeptWise;

/**
 * @author sajadhav
 * @version 1.0
 * @date 10/11/2021
 */
public interface ModuleApprovalDeptWiseRepo extends JpaRepository<ModuleApprovalDeptWise, Long> {

	@Query("select modApprovalDeptWiseId FROM ModuleApprovalDeptWise where menuIdFk.menuId=:menuId and deptIdFk.regulatorId=:deptId and roleTypeIdFk.roleTypeId=:roleTypeId and isActive = 1")
	Long fetchModuleApprovalDeptwiseForDept(@Param("menuId") Long menuId, @Param("deptId") Long deptId, @Param("roleTypeId") Long roleTypeId);

	@Query("select modApprovalDeptWiseId  FROM ModuleApprovalDeptWise where menuIdFk.menuId=:menuId and roleTypeIdFk.roleTypeId=:roleTypeId and isActive = 1")
	Long fetchModuleApprovalDeptwiseForEntity(@Param("menuId") Long menuId, @Param("roleTypeId") Long roleTypeId);
}
