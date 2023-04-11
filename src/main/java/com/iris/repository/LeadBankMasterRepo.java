package com.iris.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iris.model.LeadBankMaster;

public interface LeadBankMasterRepo extends JpaRepository<LeadBankMaster, Long> {

	@Query("FROM LeadBankMaster where isActive = 1 order by leadBankName asc")
	List<LeadBankMaster> findLeadBankDetailsByReturnCode();

}
