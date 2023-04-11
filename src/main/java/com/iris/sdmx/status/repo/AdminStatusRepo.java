package com.iris.sdmx.status.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author apagaria
 *
 */
public interface AdminStatusRepo extends JpaRepository<AdminStatus, Long> {

	/**
	 * @param isActive
	 * @return
	 */
	@Query("from AdminStatus where isActive =:isActive")
	List<AdminStatus> findByActiveStatus(Boolean isActive);

	@Query("from AdminStatus where statusTechCode =:statusTechCode")
	AdminStatus findByStatusTechCode(String statusTechCode);

	@Query("from AdminStatus where adminStatusId =:adminStatusId")
	AdminStatus findByAdminStatusId(Long adminStatusId);

}