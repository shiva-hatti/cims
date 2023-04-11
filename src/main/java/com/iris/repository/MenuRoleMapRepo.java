package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.iris.model.Menu;
import com.iris.model.MenuRoleMap;

public interface MenuRoleMapRepo extends JpaRepository<MenuRoleMap, Long> {

	//@Query(" from MenuRoleMap menuRoleMap left join fetch menuRoleMap.userRoleIdFk left join fetch menuRoleMap.menuIDFk.parentMenu  left join fetch menuRoleMap.menuIDFk  left join fetch menuRoleMap.menuIDFk.menuLabelSet  where menuRoleMap.userRoleIdFk.userRoleId=:userRoleId and menuRoleMap.isActive=true order by menuRoleMap.menuIDFk.parentMenu.menuId ASC ")
	@EntityGraph(attributePaths = { "userRoleIdFk", "menuIDFk.parentMenu", "menuIDFk", "menuIDFk.menuLabelSet" })
	List<MenuRoleMap> findByUserRoleIdFkUserRoleIdAndIsActiveTrueOrderByMenuIDFkParentMenuMenuIdAsc(@Param("userRoleId") Long userRoleId);

	@Transactional
	@Modifying
	@Query("update MenuRoleMap u set u.isActive = false where u.userRoleIdFk.userRoleId=:userRoleId")
	void cancelMenuRoleMap(@Param("userRoleId") Long userRoleId);

	@Query(value = "FROM Menu menu where menu.isActive='1' and menu.isEntity='1' and menu.redirectUrl!='#'  ")
	List<Menu> getMenuForEntity();

	@Query(value = "FROM Menu menu where menu.isActive='1' and menu.isAuditor='1' and menu.redirectUrl!='#'  ")
	List<Menu> getMenuForAuditor();

	List<MenuRoleMap> findByUserRoleIdFkUserRoleId(Long userRoleId);

	List<MenuRoleMap> findByUserRoleIdFkUserRoleIdAndIsActiveTrue(Long userRoleId);

	@Query(value = "FROM Menu menu where menu.menuId in (:menuIds)")
	List<Menu> getMenuFromJsonIds(@Param("menuIds") List<Long> menuIds);

	@Query(value = "FROM Menu menu where menu.isActive='1' and menu.isDept='1' and menu.redirectUrl!='#'  ")
	List<Menu> getMenuForDept();

	@Query("From MenuRoleMap menuRoleMap where menuRoleMap.userRoleIdFk.userRoleId IN (:userRoleIds) AND menuRoleMap.isActive=1 ORDER BY menuRoleMap.menuIDFk.parentMenu.menuId ASC")
	List<MenuRoleMap> getMenuForAllRoles(@Param("userRoleIds") List<Long> userRoleIds);
}
