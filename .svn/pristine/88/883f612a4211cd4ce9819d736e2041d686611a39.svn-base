/**
 * 
 */
package com.iris.sdmx.status.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.status.entity.SdmxModuleDetailEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxModuleDetailRepo extends JpaRepository<SdmxModuleDetailEntity, Long> {

	/**
	 * @return
	 */
	@Query("from SdmxModuleDetailEntity where moduleCode=:moduleCode")
	public SdmxModuleDetailEntity findModuleByModuleCode(String moduleCode);

	@Query("from SdmxModuleDetailEntity where moduleId=:moduleId")
	public SdmxModuleDetailEntity findModuleByModuleId(Long moduleId);
}
