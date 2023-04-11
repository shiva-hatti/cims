/**
 * 
 */
package com.iris.nbfc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iris.nbfc.model.NbfcDisplayMessages;

/**
 * @author sikhan
 *
 */
public interface NbfcMessagesRepo extends JpaRepository<NbfcDisplayMessages, Long> {

	@Query(value = "FROM NbfcDisplayMessages where isActive = 1 and companyType.companyTypeId =:companyType")
	List<NbfcDisplayMessages> getActiveMessgaesDataBycompanyType(@Param("companyType") Integer companyType);

}
