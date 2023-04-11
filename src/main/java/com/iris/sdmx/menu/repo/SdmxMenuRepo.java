package com.iris.sdmx.menu.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.menu.entity.SdmxMenu;

/**
 * @author vjadhav
 *
 */
public interface SdmxMenuRepo extends JpaRepository<SdmxMenu, Long> {
	@Query("from SdmxMenu where isActive = true")
	List<SdmxMenu> getAllActiveMenus();

	@Query("select new com.iris.sdmx.menu.entity.SdmxMenu(sm.sdmxMenuId,sm.sdmxMenuUrl, sm.defaultName, sm.parentMenuId, sm.level," + "sm.icon, sm.orderNo, sm.groupId, sm.menuLabelKey, srtm.addRight,srtm.editRight,srtm.viewRight,srtm.addApproval,srtm.editApproval) from SdmxMenu sm, SdmxRoleTypeMenuMapping srtm where sm.isActive = true and " + "srtm.roleTypeIdFk.roleTypeId =:roleTypeId and srtm.isAdmin =:isAdmin and " + "srtm.isMainDept =:isMainDept and srtm.viewRight = '1' and " + "sm.sdmxMenuId = srtm.sdmxMenuIdFk.sdmxMenuId")
	List<SdmxMenu> getRoleTypeWiseActiveMenus(Long roleTypeId, Boolean isAdmin, Boolean isMainDept);

	@Query("from SdmxMenu where sdmxMenuId =:sdmxMenuId and isActive = true")
	SdmxMenu findByMenuIdAndIsActive(Long sdmxMenuId);
}
