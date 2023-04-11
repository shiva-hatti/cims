package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.model.Menu;

public interface MenuRepo extends JpaRepository<Menu, Long> {

	List<Menu> findByIsDeptTrueAndIsActiveTrue();

	@Query("SELECT DISTINCT menu FROM Menu menu, MenuRoleMap menuRoleMap where menuRoleMap.userRoleIdFk.userRoleId IN (:roleIdLst) AND " + "menuRoleMap.isActive=1 AND menuRoleMap.menuIDFk.menuId=menu.menuId AND menu.isActive=1 order by menu.orderNo asc")
	List<Menu> getMenuListOnRoleList(@Param("roleIdLst") List<Long> roleIdLst);

}