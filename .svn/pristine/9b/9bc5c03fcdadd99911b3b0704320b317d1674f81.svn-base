/**
 * 
 */
package com.iris.sdmx.status.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.status.entity.SdmxProcessDetailEntity;

/**
 * @author apagaria
 *
 */
public interface SdmxProcessDetailRepo extends JpaRepository<SdmxProcessDetailEntity, Long> {

	/**
	 * @return
	 */
	@Query("from SdmxProcessDetailEntity where processCode=:processCode")
	public SdmxProcessDetailEntity findProcessByProcessCode(String processCode);
}
