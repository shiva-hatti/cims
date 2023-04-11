/**
 * 
 */
package com.iris.sdmx.status.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.status.entity.SdmxModuleDetailEntity;
import com.iris.sdmx.status.entity.SdmxModuleStatus;

/**
 * @author apagaria
 *
 */
public interface SdmxModuleStatusRepo extends JpaRepository<SdmxModuleStatus, Long> {

	/**
	 * @param moduleId
	 * @param isActive
	 * @return
	 */
	@Query("from SdmxModuleStatus where moduleDetailIdFk=:moduleDetailIdFk and isActive =:isActive")
	List<SdmxModuleStatus> findModuleStatusByModuleIdNActive(SdmxModuleDetailEntity moduleDetailIdFk, Boolean isActive);

}
